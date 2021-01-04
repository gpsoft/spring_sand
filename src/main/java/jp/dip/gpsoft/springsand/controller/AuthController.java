package jp.dip.gpsoft.springsand.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import jp.dip.gpsoft.springsand.form.AuthForm;

@Controller
public class AuthController {

	@ModelAttribute("authForm")
	public AuthForm emptyAuthForm() {
		AuthForm af = new AuthForm();
		return af;
	}

	@GetMapping("/login")
	public String loginForm(Model model) {
		model.addAttribute("noLoginBtn", true);
		return "auth/form";
	}
}
