package shopdackh.controller.admin;

import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import shopdackh.constant.GlobalConstant;
import shopdackh.constant.URLConstant;
import shopdackh.constant.ViewNameConstant;
import shopdackh.model.Address;
import shopdackh.model.User;
import shopdackh.service.AddressService;
import shopdackh.service.LocationDistrictService;
import shopdackh.service.LocationProvinceService;
import shopdackh.service.LocationWardService;
import shopdackh.service.UserService;
import shopdackh.util.FileUtil;
import shopdackh.validate.AddressValidate;
import shopdackh.validate.UserValidate;

@Controller
public class AdminProfileController {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private LocationProvinceService provinceService;

	@Autowired
	private LocationDistrictService districtService;

	@Autowired
	private LocationWardService wardService;

	@Autowired
	private UserService userService;

	@Autowired
	private AddressService addressService;

	@Autowired
	private UserValidate userValidate;

	@Autowired
	private AddressValidate addressValidate;

	@GetMapping(URLConstant.ADMIN_PROFILE)
	public String profile(Model model, HttpSession session) {
		User userLogin = (User) session.getAttribute("adminUserLogin");
		model.addAttribute("objUser", userLogin);
		Address address = userLogin.getUserAddress();
		model.addAttribute("objAddress", address);
		model.addAttribute("listProvince", provinceService.getAll());
		model.addAttribute("listDistrict", districtService.findByProvinceId(address.getProvince().getProvinceId()));
		model.addAttribute("listWard", wardService.findByDistrictId(address.getDistrict().getDistrictId()));
		return ViewNameConstant.ADMIN_PROFILE;
	}

	@Transactional
	@PostMapping(URLConstant.ADMIN_PROFILE)
	public String profile(@Valid @ModelAttribute("userError") User user, BindingResult userRs,
			@Valid @ModelAttribute("addressError") Address address, BindingResult addressRs,
			@RequestParam("picture") MultipartFile multipartFile, Model model, RedirectAttributes ra,
			HttpSession session) {
		User userLogin = (User) session.getAttribute("adminUserLogin");
		user.setAvatar(userLogin.getAvatar());
		user.setRole(userLogin.getRole());
		user.setUserId(userLogin.getUserId());
		model.addAttribute("listProvince", provinceService.getAll());
		if (address.getProvince().getProvinceId() > 0) {
			model.addAttribute("listDistrict", districtService.findByProvinceId(address.getProvince().getProvinceId()));
			if (address.getDistrict().getDistrictId() > 0) {
				model.addAttribute("listWard", wardService.findByDistrictId(address.getDistrict().getDistrictId()));
			}
		}
		model.addAttribute("objUser", user);
		model.addAttribute("objAddress", address);
		userValidate.validate(user, userLogin, userRs);
		userValidate.validatePicture(multipartFile, userRs);
		addressValidate.validate(address, addressRs);
		if (userRs.hasErrors() || addressRs.hasErrors()) {
			model.addAttribute("error", messageSource.getMessage("formError", null, Locale.getDefault()));
			return ViewNameConstant.ADMIN_PROFILE;
		}
		address.setAddressId(userLogin.getUserAddress().getAddressId());
		int updateAddress = addressService.update(address);
		if (updateAddress == 0) {
			ra.addFlashAttribute("error", messageSource.getMessage("error", null, Locale.getDefault()));
			return "redirect:/" + URLConstant.ADMIN_PROFILE;
		}
		String avatar = FileUtil.uploadFile(multipartFile, GlobalConstant.DIR_UPLOAD_IMAGE_AVATAR, servletContext);
		if (!avatar.equals(GlobalConstant.EMPTY)) {
			user.setAvatar(avatar); // c?? upload avatar => update avatar m???i
		}
		if (userService.update(user) > 0) {
			// n???u c?? upload avatar => xo?? avatar c?? (n???u c??)
			if (!avatar.equals(GlobalConstant.EMPTY) && !userLogin.getAvatar().equals(GlobalConstant.EMPTY)) {
				FileUtil.delFile(userLogin.getAvatar(), GlobalConstant.DIR_UPLOAD_IMAGE_AVATAR, servletContext);
			}
			session.setAttribute("adminUserLogin", userService.findById(userLogin.getUserId())); // c???p nh???t l???i session
			ra.addFlashAttribute("success", messageSource.getMessage("updateSuccess", null, Locale.getDefault()));
		} else {
			// n???u c?? upload avatar => xo?? avatar v???a upload
			if (!avatar.equals(GlobalConstant.EMPTY)) {
				FileUtil.delFile(avatar, GlobalConstant.DIR_UPLOAD_IMAGE_AVATAR, servletContext);
			}
			ra.addFlashAttribute("error", messageSource.getMessage("error", null, Locale.getDefault()));
		}
		return "redirect:/" + URLConstant.ADMIN_PROFILE;
	}

	@GetMapping(URLConstant.ADMIN_CHANGE_PASSWORD)
	public String changePassword() {
		return ViewNameConstant.ADMIN_CHANGE_PASSWORD;
	}

	@PostMapping(URLConstant.ADMIN_CHANGE_PASSWORD)
	public String changePassword(@Valid @ModelAttribute("userError") User user, BindingResult rs,
			@RequestParam String oldPassword, @RequestParam String confirmPassword, Model model, RedirectAttributes ra,
			HttpSession session) {
		User userLogin = (User) session.getAttribute("adminUserLogin");
		userValidate.validatePassword(user, rs, userLogin, confirmPassword, oldPassword, model);
		if (rs.hasFieldErrors("password")) {
			model.addAttribute("error", messageSource.getMessage("formError", null, Locale.getDefault()));
			return ViewNameConstant.ADMIN_CHANGE_PASSWORD;
		}
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setUserId(userLogin.getUserId());
		if (userService.updatePassword(user) > 0) {
			userLogin.setPassword(user.getPassword());
			session.setAttribute("adminUserLogin", userLogin);
			ra.addFlashAttribute("success",
					messageSource.getMessage("changePasswordSuccess", null, Locale.getDefault()));
		} else {
			ra.addFlashAttribute("error", messageSource.getMessage("error", null, Locale.getDefault()));
		}
		return "redirect:/" + URLConstant.ADMIN_PROFILE;
	}

}
