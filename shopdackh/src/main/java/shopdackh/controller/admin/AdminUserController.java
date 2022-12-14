package shopdackh.controller.admin;

import java.util.List;
import java.util.Locale;

import javax.servlet.ServletContext;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;

import shopdackh.constant.GlobalConstant;
import shopdackh.constant.URLConstant;
import shopdackh.constant.ViewNameConstant;
import shopdackh.model.Address;
import shopdackh.model.Role;
import shopdackh.model.User;
import shopdackh.service.AddressService;
import shopdackh.service.LocationDistrictService;
import shopdackh.service.LocationProvinceService;
import shopdackh.service.LocationWardService;
import shopdackh.service.RoleService;
import shopdackh.service.UserService;
import shopdackh.util.FileUtil;
import shopdackh.util.PageUtil;
import shopdackh.util.bean.AjaxStatus;
import shopdackh.validate.AddressValidate;
import shopdackh.validate.UserValidate;

@Controller
@RequestMapping(URLConstant.ADMIN_USER_INDEX)
public class AdminUserController {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private LocationProvinceService provinceService;

	@Autowired
	private LocationDistrictService locationDistrictService;

	@Autowired
	private LocationWardService locationWardService;

	@Autowired
	private AddressService addressService;

	@Autowired
	private UserValidate userValidate;

	@Autowired
	private AddressValidate addressValidate;

	@GetMapping({ GlobalConstant.EMPTY, URLConstant.ADMIN_PAGINATION, URLConstant.ADMIN_SEARCH,
			URLConstant.ADMIN_USER_SEARCH_PAGINATION })
	public String index(@PathVariable(required = false) Integer page,
			@PathVariable(required = false) String usernameURL, @PathVariable(required = false) Integer enabledURL,
			@PathVariable(required = false) Integer roleIdURL, @RequestParam(required = false) String username,
			@RequestParam(required = false) Integer enabled, @RequestParam(required = false) Integer roleId,
			Model model, RedirectAttributes ra) {
		model.addAttribute("listRole", roleService.getAll());
		int currentPage = GlobalConstant.DEFAULT_PAGE;
		if (page != null) {
			if (page < GlobalConstant.DEFAULT_PAGE) {
				ra.addFlashAttribute("error", messageSource.getMessage("pageError", null, Locale.getDefault()));
				return "redirect:/" + URLConstant.ADMIN_USER_INDEX;
			}
			currentPage = page;
		}
		int offset = PageUtil.getOffset(currentPage);
		int totalRow = userService.totalRow();
		int totalPage = PageUtil.getTotalPage(totalRow);
		List<User> listUser = userService.getList(offset, GlobalConstant.TOTAL_ROW);
		if (usernameURL != null) {
			username = usernameURL;
		}
		if (enabledURL != null) {
			enabled = enabledURL;
		}
		if (roleIdURL != null) {
			roleId = roleIdURL;
		}
		if (username != null || enabled != null || roleId != null) {
			if (username.equals(GlobalConstant.EMPTY) && roleId == 0 && (enabled != 0 && enabled != 1)) {
				ra.addFlashAttribute("error", messageSource.getMessage("searchError", null, Locale.getDefault()));
				return "redirect:/" + URLConstant.ADMIN_USER_INDEX;
			}
			model.addAttribute("username", username);
			model.addAttribute("enabled", enabled);
			model.addAttribute("roleId", roleId);
			totalRow = userService.totalRowSearch(username, enabled, roleId);
			totalPage = PageUtil.getTotalPage(totalRow);
			listUser = userService.search(username, enabled, roleId, offset, GlobalConstant.TOTAL_ROW);
		}
		model.addAttribute("listUser", listUser);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalPage", totalPage);
		return ViewNameConstant.ADMIN_USER_INDEX;
	}

	@GetMapping(URLConstant.ADMIN_ADD)
	public String add(Model model) {
		model.addAttribute("listRole", roleService.getRoleNotAdmin());
		model.addAttribute("listProvince", provinceService.getAll());
		return ViewNameConstant.ADMIN_USER_ADD;
	}

	// Transaction: c??c c??ng vi???c (insert table address, user) ph???i ???????c ho??n th??nh
	// c??ng nhau
	@Transactional
	@PostMapping(URLConstant.ADMIN_ADD)
	public String add(@Valid @ModelAttribute("userError") User user, BindingResult userRs,
			@Valid @ModelAttribute("addressError") Address address, BindingResult addressRs,
			@RequestParam("picture") MultipartFile multipartFile, @RequestParam String confirmPassword,
			RedirectAttributes ra, Model model) {
		model.addAttribute("listRole", roleService.getRoleNotAdmin());
		model.addAttribute("listProvince", provinceService.getAll());
		if (address.getProvince().getProvinceId() > 0) {
			model.addAttribute("listDistrict",
					locationDistrictService.findByProvinceId(address.getProvince().getProvinceId()));
			if (address.getDistrict().getDistrictId() > 0) {
				model.addAttribute("listWard",
						locationWardService.findByDistrictId(address.getDistrict().getDistrictId()));
			}
		}
		model.addAttribute("objUser", user);
		model.addAttribute("objAddress", address);
		userValidate.validate(user, null, userRs);
		userValidate.validatePassword(user, userRs, null, confirmPassword, null, model);
		userValidate.validatePicture(multipartFile, userRs);
		addressValidate.validate(address, addressRs);
		if (userRs.hasErrors() || addressRs.hasErrors()) {
			model.addAttribute("error", messageSource.getMessage("formError", null, Locale.getDefault()));
			return ViewNameConstant.ADMIN_USER_ADD;
		}
		// n???u th??m role admin => chuy???n qua role user
		if (user.getRole().getRoleId() == GlobalConstant.ROLE_ADMIN) {
			user.setRole(new Role(GlobalConstant.ROLE_USER));
		}
		if (addressService.save(address) > 0) {
			user.setUserAddress(addressService.getNewAddress());
		} else {
			ra.addFlashAttribute("error", messageSource.getMessage("error", null, Locale.getDefault()));
			return "redirect:/" + URLConstant.ADMIN_USER_INDEX;
		}
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword())); // m?? ho?? password
		String avatar = FileUtil.uploadFile(multipartFile, GlobalConstant.DIR_UPLOAD_IMAGE_AVATAR, servletContext);
		user.setAvatar(avatar);
		if (userService.save(user) > 0) {
			ra.addFlashAttribute("success", messageSource.getMessage("addSuccess", null, Locale.getDefault()));
		} else {
			// n???u th??m kh??ng th??nh c??ng => xo?? h??nh avatar (n???u c??)
			if (!avatar.equals(GlobalConstant.EMPTY)) {
				FileUtil.delFile(avatar, GlobalConstant.DIR_UPLOAD_IMAGE_AVATAR, servletContext);
			}
			ra.addFlashAttribute("error", messageSource.getMessage("error", null, Locale.getDefault()));
		}
		return "redirect:/" + URLConstant.ADMIN_USER_INDEX;
	}

	// kho??, m??? kho?? t??i kho???n (update status enabled)
	@PostMapping(URLConstant.ADMIN_USER_UPDATE_STATUS)
	@ResponseBody
	public String updateStatus(@RequestParam int userId, @RequestParam int status) {
		User user = userService.findById(userId);
		if (user == null) {
			return new Gson()
					.toJson(new AjaxStatus(1, messageSource.getMessage("noDataError", null, Locale.getDefault())));
		}
		if (user.getRole().getRoleId() == GlobalConstant.ROLE_ADMIN) {
			return new Gson().toJson(
					new AjaxStatus(1, messageSource.getMessage("notDisabledAdminError", null, Locale.getDefault())));
		}
		user.setEnabled(status);
		if (userService.updateStatus(user) > 0) {
			if (status == 0) {
				return new Gson().toJson(
						new AjaxStatus(0, messageSource.getMessage("disabledUserSuccess", null, Locale.getDefault())));
			} else {
				return new Gson().toJson(
						new AjaxStatus(0, messageSource.getMessage("enabledUserSuccess", null, Locale.getDefault())));
			}
		}
		return new Gson().toJson(new AjaxStatus(1, messageSource.getMessage("error", null, Locale.getDefault())));
	}

}
