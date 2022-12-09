package shopdackh.controller.error;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import shopdackh.constant.URLConstant;
import shopdackh.constant.ViewNameConstant;

@Controller
public class AdminErrorController {

	@GetMapping(URLConstant.ADMIN_ERROR_403)
	public String error403() {
		return ViewNameConstant.ADMIN_ERROR_403;
	}

	@GetMapping(URLConstant.ADMIN_ERROR_400)
	public String error400() {
		return ViewNameConstant.ADMIN_ERROR_400;
	}

	@GetMapping(URLConstant.ADMIN_ERROR_404)
	public String error404() {
		return ViewNameConstant.ADMIN_ERROR_404;
	}

}
