package jp.dip.gpsoft.springsand.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.dip.gpsoft.springsand.exception.BadRequestStatusException;
import jp.dip.gpsoft.springsand.model.Lake;
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
		model.addAttribute("lake", new Lake("", "", null));
		return "lake/form";
	}

	@GetMapping("/{id:^[\\d]+$}/edit")
	public String editLake(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("lake", lakeService.lookupLake(id));
		return "lake/form";
	}

	@PostMapping("")
	public String create(@ModelAttribute Lake lake) {
		lakeService.saveLake(lake);
		return "redirect:/lakes";
	}

	@PostMapping("/{id:^[\\d]+$}")
	public String update(@PathVariable("id") Integer id, @ModelAttribute Lake lake) {
		if (!id.equals(lake.getId())) {
			throw new BadRequestStatusException();
		}
		lakeService.saveLake(lake);
		return "redirect:/lakes";
	}

	@PostMapping("/{id:^[\\d]+$}/delete")
	public String destroy(@PathVariable("id") Integer id) {
		lakeService.deleteLake(id);
		return "redirect:/lakes";
	}
}
