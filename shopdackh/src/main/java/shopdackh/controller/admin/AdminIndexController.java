package shopdackh.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import shopdackh.constant.GlobalConstant;
import shopdackh.constant.URLConstant;
import shopdackh.constant.ViewNameConstant;

@Controller
@RequestMapping(URLConstant.ADMIN_INDEX)
public class AdminIndexController {

	@GetMapping(GlobalConstant.EMPTY)
	public String index() {
		return ViewNameConstant.ADMIN_INDEX;
	}

}
