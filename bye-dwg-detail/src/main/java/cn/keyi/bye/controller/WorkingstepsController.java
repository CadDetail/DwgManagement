package cn.keyi.bye.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.keyi.bye.model.Workingsteps;
import cn.keyi.bye.service.WorkingstepsService;

@RestController
@RequestMapping("/workingsteps")
public class WorkingstepsController {

	@Autowired
	WorkingstepsService workingstepsService;
	
	@RequestMapping("/findWorkingsteps")
	public List<Workingsteps> findWorkingsteps() {
		return workingstepsService.getAllWorkingsteps();
	}
	
}
