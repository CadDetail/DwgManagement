package cn.keyi.bye.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.keyi.bye.model.Artifact;
import cn.keyi.bye.model.ArtifactDetail;
import cn.keyi.bye.model.CookieUser;
import cn.keyi.bye.service.ArtifactDetailService;
import cn.keyi.bye.service.ArtifactService;

@RestController
@RequestMapping("/export")
public class ExportController {
	
	@Autowired
	ArtifactService artifactService;
	@Autowired
	ArtifactDetailService artifactDetailService;
	
	@RequestMapping("/exportDetail")
	@RequiresPermissions(value={"system:all","detail:export","detail:print"},logical=Logical.OR)
	public void exportDetail(Long masterId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 先获取到明细列表
		List<Map<String, Object>> listDetail = new ArrayList<Map<String, Object>>();
		Artifact master = artifactService.getArtifactById(masterId);
		int times = 1;	// 初始倍数置为1, 即最开始要遍历下级零件的产品看成是1件
		if(master != null) {			
			// artifactDetailService.findRecursiveDetailByMaster(master, times, listDetail);
			listDetail = artifactDetailService.getDetailByMaster(master, times);
		}
		// 处理Excel表格	
		InputStream xlsFile = this.getClass().getResourceAsStream("/static/files/DetailTemplate.xls");
		// 创建 HSSFWorkbook 对象
		HSSFWorkbook wb = null;
		try {
			wb = new HSSFWorkbook(xlsFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(wb == null) { return; }
		for(int i=0; i<listDetail.size(); i++) {
			Sheet sheet = wb.cloneSheet(0);
			// 工作表名称
			wb.setSheetName(i+1, "Artifact" + Integer.toString(i+1));
			// 产品型号
			Cell cell = sheet.getRow(32).getCell(10);
			cell.setCellValue(master.getProductModel());
			// 图号
			cell = sheet.getRow(31).getCell(13);
			cell.setCellValue(master.getArtifactCode());
			// 第几页
			cell = sheet.getRow(32).getCell(14);
			cell.setCellValue(i+1);
			// 共几页
			cell = sheet.getRow(32).getCell(18);
			cell.setCellValue(listDetail.size());
			// 编制（会签人）
			Subject subject = SecurityUtils.getSubject();
			CookieUser cookieUser = (CookieUser) subject.getPrincipal();
			cell = sheet.getRow(30).getCell(26);
			cell.setCellValue(cookieUser.getUserName());
			// 具体明细
			Artifact artifact = (Artifact) listDetail.get(i).get("artifact");
			//Integer numberMulti = (Integer) listDetail.get(i).get("times");
			@SuppressWarnings("unchecked")
			List<ArtifactDetail> details = (List<ArtifactDetail>) listDetail.get(i).get("detail");
			// 零件名称
			cell = sheet.getRow(34).getCell(10);
			cell.setCellValue(artifact.getArtifactName());			
			// 如果明细记录数超过Excel模板文件可容纳的行数，则先插入若干空行
			if(details.size() > 25) {
				insertRow(sheet, 10, details.size() - 25);
			}
			for(int j=0; j<details.size(); j++) {
				Row row = sheet.getRow(5+j);
				row.getCell(1).setCellValue(j+1);
				row.getCell(2).setCellValue(details.get(j).getSlave().getArtifactCode());
				row.getCell(4).setCellValue(details.get(j).getSlave().getArtifactName());				
				row.getCell(6).setCellValue(details.get(j).getMaster().getArtifactCode());
				//row.getCell(8).setCellValue(details.get(j).getNumber() * numberMulti);
				row.getCell(8).setCellValue(details.get(j).getNumber());
				Float weight = details.get(j).getSlave().getWeight();
				// if(weight != null) { row.getCell(9).setCellValue(weight * numberMulti); }
				if(weight != null) { row.getCell(9).setCellValue(weight); }
				String materialName = details.get(j).getSlave().getMaterialName();
				if(materialName != null) { row.getCell(11).setCellValue(materialName); }
				String materialCode = details.get(j).getSlave().getMaterialCode();
				if(materialCode != null) { row.getCell(12).setCellValue(materialCode); }
				String memo = details.get(j).getSlave().getArtifactMemo();
				if(memo != null) { row.getCell(18).setCellValue(memo); }
				String dimension = details.get(j).getDimension();
				if(dimension != null) { row.getCell(21).setCellValue(dimension); }
				String unit = details.get(j).getUnit();
				if(unit != null) { row.getCell(24).setCellValue(unit); }
				String quota = details.get(j).getQuota();
				if(quota != null) { row.getCell(25).setCellValue(quota); }
				String workingSteps = details.get(j).getWorkingSteps();
				if(workingSteps != null) { row.getCell(27).setCellValue(workingSteps); }
			}
		}
		wb.removeSheetAt(0);
		// 导出的Excel文件名
		String fileName = master.getArtifactName() + "(" + master.getArtifactCode()  + ")" + System.currentTimeMillis() + ".xls";
		//响应到客户端
		try {
			this.setResponseHeader(response, fileName);
			OutputStream os = response.getOutputStream();
			wb.write(os);
			os.flush();
			os.close();
			wb.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
		
	// 在指定行开始插入指定数量的行
	private void insertRow(Sheet sheet, int startRow, int rows) {  
        sheet.shiftRows(startRow, sheet.getLastRowNum(), rows, true, false); 
        for (int i = 0; i < rows; i++) {  
            Row sourceRow = null; 	// 原始位置  
            Row targetRow = null; 	// 移动后位置  
            Cell sourceCell = null;  
            Cell targetCell = null;  
            sourceRow = sheet.createRow(startRow);  
            targetRow = sheet.getRow(startRow + rows);  
            sourceRow.setHeight(targetRow.getHeight());  
            for (int m = targetRow.getFirstCellNum(); m < targetRow.getPhysicalNumberOfCells(); m++) {  
                sourceCell = sourceRow.createCell(m);  
                targetCell = targetRow.getCell(m);  
                sourceCell.setCellStyle(targetCell.getCellStyle());
            }
            // 插入行的指定单元格进行合并，参数说明：1：开始行 2：结束行  3：开始列 4：结束列
            sheet.addMergedRegion(new CellRangeAddress(startRow, startRow, 2, 3));
            sheet.addMergedRegion(new CellRangeAddress(startRow, startRow, 4, 5));
            sheet.addMergedRegion(new CellRangeAddress(startRow, startRow, 6, 7));
            sheet.addMergedRegion(new CellRangeAddress(startRow, startRow, 9, 10));
            sheet.addMergedRegion(new CellRangeAddress(startRow, startRow, 12, 17));
            sheet.addMergedRegion(new CellRangeAddress(startRow, startRow, 18, 20));
            sheet.addMergedRegion(new CellRangeAddress(startRow, startRow, 21, 23));
            sheet.addMergedRegion(new CellRangeAddress(startRow, startRow, 25, 26));
            startRow++;  
        }
	}
	
	//发送响应流方法
    private void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
