package jp.dip.gpsoft.springsand.controller;

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

import jp.dip.gpsoft.springsand.exception.BadRequestStatusException;
import jp.dip.gpsoft.springsand.form.LakeForm;
import jp.dip.gpsoft.springsand.service.LakeService;

@Controller
@RequestMapping("/lakes")
public class LakeController {

	@Autowired
	private LakeService lakeService;

	@GetMapping
	public String index(Model model) {
		model.addAttribute("lakes", lakeService.findAllLakes());
		return "lake/index";
	}

	@GetMapping("/{id:^[\\d]+$}")
	public String show(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("lake", lakeService.lookupLake(id));
		return "lake/details";
	}

	@GetMapping("/new")
	public String newLake(Model model) {
		model.addAttribute("lakeForm", new LakeForm());
		return "lake/form";
	}

	@GetMapping("/{id:^[\\d]+$}/edit")
	public String editLake(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("lakeForm", new LakeForm(lakeService.lookupLake(id)));
		return "lake/form";
	}

	@PostMapping("")
	public String create(@Validated @ModelAttribute LakeForm lake, BindingResult result) {
		if (result.hasErrors()) {
			return "lake/form";
		}
		lakeService.saveLake(lake.toLake());
		return "redirect:/lakes";
	}

	@PostMapping("/{id:^[\\d]+$}")
	public String update(@PathVariable("id") Integer id,
			@Validated @ModelAttribute LakeForm lake, BindingResult result) {
		if (!id.equals(lake.getId())) {
			throw new BadRequestStatusException();
		}
		if (result.hasErrors()) {
			return "lake/form";
		}
		lakeService.saveLake(lake.toLake());
		return "redirect:/lakes";
	}

	@PostMapping("/{id:^[\\d]+$}/delete")
	public String destroy(@PathVariable("id") Integer id) {
		lakeService.deleteLake(id);
		return "redirect:/lakes";
	}
}
