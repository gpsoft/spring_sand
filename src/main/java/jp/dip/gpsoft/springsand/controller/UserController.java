package jp.dip.gpsoft.springsand.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.dip.gpsoft.springsand.exception.BadRequestStatusException;
import jp.dip.gpsoft.springsand.form.UserForm;
import jp.dip.gpsoft.springsand.service.UserService;
import jp.dip.gpsoft.springsand.validation.OnInsert;
import jp.dip.gpsoft.springsand.validation.OnUpdate;

@Controller
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping
	public String index(Model model) {
		model.addAttribute("users", userService.findAllUsers());
		return "user/index";
	}

	@GetMapping("/{id:^[\\d]+$}")
	public String show(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("user", userService.lookupUser(id));
		return "user/details";
	}

	@GetMapping("/new")
	public String newUser(Model model) {
		model.addAttribute("userForm", userService.emptyUserForm());
		return "user/form";
	}

	@GetMapping("/{id:^[\\d]+$}/edit")
	public String editUser(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("userForm", userService.userForm(id));
		return "user/form";
	}

	@PostMapping("")
	public String create(@Validated(OnInsert.class) @ModelAttribute UserForm user,
			BindingResult result) {
		if (result.hasErrors()) {
			userService.userFormWithAllRoles(user);
			return "user/form";
		}
		userService.saveUser(user);
		return "redirect:/users";
	}

	@PostMapping("/{id:^[\\d]+$}")
	public String update(@PathVariable("id") Integer id,
			@Validated(OnUpdate.class) @ModelAttribute UserForm user, BindingResult result) {
		System.out.println(user.toString());
		if (!id.equals(user.getId())) {
			throw new BadRequestStatusException();
		}
		if (result.hasErrors()) {
			userService.userFormWithAllRoles(user);
			return "user/form";
		}
		userService.saveUser(user);
		return "redirect:/users";
	}

	@PostMapping("/{id:^[\\d]+$}/delete")
	public String destroy(@PathVariable("id") Integer id) {
		userService.deleteUser(id);
		return "redirect:/users";
	}

	@GetMapping("/uniqueloginid")
	@ResponseBody
	public Map<String, Object> ajaxUniqueLoginId(@RequestParam String loginId) {
		Map<String, Object> res = new HashMap<>();
		res.put("unique", userService.isAvailableLoginId(loginId));
		return res;
	}
}
