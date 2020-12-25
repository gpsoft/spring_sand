package jp.dip.gpsoft.springsand.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.dip.gpsoft.springsand.service.RiverService;

@Controller
@RequestMapping("/rivers")
public class RiverController {

	@Autowired
	private RiverService riverService;

	@GetMapping
	public String index(Model model) {
		model.addAttribute("rivers", riverService.findAllRivers());
		return "river/index";
	}

	@PostMapping("/{id:^[\\d]+$}/delete")
	public String destroy(@PathVariable("id") Integer id) {
		riverService.deleteRiver(id);
		return "redirect:/rivers";
	}
}