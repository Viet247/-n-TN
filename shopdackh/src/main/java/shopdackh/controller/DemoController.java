package shopdackh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import shopdackh.service.OrderDetailService;

@Controller
public class DemoController {

	@Autowired
	OrderDetailService service;

	@GetMapping("demo")
	public String demo(Model model) {
		return "demo";
	}

}
