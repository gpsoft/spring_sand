package jp.dip.gpsoft.springsand.controller;

import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

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
		return renderLoginForm(model);
	}

	@PostMapping("/loginError")
	public String loginError(Model model,
			@RequestAttribute(WebAttributes.AUTHENTICATION_EXCEPTION) Exception ex) {
		model.addAttribute("error", ex.getMessage());
		return renderLoginForm(model);
	}

	private String renderLoginForm(Model model) {
		model.addAttribute("noLoginBtn", true);
		return "auth/form";
	}
}
