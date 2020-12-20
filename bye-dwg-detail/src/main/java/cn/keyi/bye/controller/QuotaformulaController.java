package cn.keyi.bye.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.keyi.bye.model.Quotaformula;
import cn.keyi.bye.service.QuotaformulaService;

@RestController
@RequestMapping("/quotaformula")
public class QuotaformulaController {

	@Autowired
	QuotaformulaService quotaformulaService;
	
	@RequestMapping("/findQuotaformula")
	public List<Quotaformula> findAllQuotaformula() {
		return quotaformulaService.getAllQuotaformula();
	}
}
