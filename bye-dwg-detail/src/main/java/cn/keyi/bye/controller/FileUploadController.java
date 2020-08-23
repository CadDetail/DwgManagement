package cn.keyi.bye.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileUploadController {
	
	@PostMapping("/upload")
	public Object upload(MultipartFile uploadFile, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String realPath = request.getSession().getServletContext().getRealPath("/uplaodFile/");
		File folder = new File(realPath);
		if(!folder.isDirectory()) {
			folder.mkdirs();
		}
		String oldName = uploadFile.getOriginalFilename();
		int dotPosition = oldName.lastIndexOf(".");		
		String newName = oldName.substring(0, dotPosition) + System.currentTimeMillis() + oldName.substring(dotPosition);
		// System.out.println(folder + "\\" + newName);
		try {
			uploadFile.transferTo(new File(folder, newName));
			// URL路径，便于像图片的回显
			// String filePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/uploadFile/" + newName;
			// 物理路径，便于在服务端操作文件
			String filePhysicalPath = folder + "\\" + newName;
			map.put("status", 1);
			map.put("message", filePhysicalPath);
			return map;
		} catch (IOException e) {
			map.put("status", 0);
			map.put("message", e.getMessage());
		}
		return map;
	}
	
}
