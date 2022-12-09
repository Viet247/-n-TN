package shopdackh.controller.auth;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import shopdackh.constant.URLConstant;
import shopdackh.constant.ViewNameConstant;

@Controller
public class AdminAuthController {

	@GetMapping(URLConstant.ADMIN_LOGIN)
	public String login(HttpSession session) {
		if (session.getAttribute("adminUserLogin") != null) {
			return "redirect:/" + URLConstant.ADMIN_INDEX;
		}
		return ViewNameConstant.ADMIN_LOGIN;
	}

}
