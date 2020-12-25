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
import jp.dip.gpsoft.springsand.model.River;
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

	@GetMapping("/{id:^[\\d]+$}")
	public String show(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("river", riverService.lookupRiver(id));
		return "river/show";
	}

	@GetMapping("/new")
	public String newRiver(Model model) {
		model.addAttribute("river", new River("", "", ""));
		return "river/form";
	}

	@GetMapping("/{id:^[\\d]+$}/edit")
	public String editRiver(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("river", riverService.lookupRiver(id));
		return "river/form";
	}

	@PostMapping("")
	public String create(@ModelAttribute River river) {
		riverService.saveRiver(river);
		return "redirect:/rivers";
	}

	@PostMapping("/{id:^[\\d]+$}")
	public String update(@PathVariable("id") Integer id, @ModelAttribute River river) {
		if (!id.equals(river.getId())) {
			throw new BadRequestStatusException();
		}
		riverService.saveRiver(river);
		return "redirect:/rivers";
	}

	@PostMapping("/{id:^[\\d]+$}/delete")
	public String destroy(@PathVariable("id") Integer id) {
		riverService.deleteRiver(id);
		return "redirect:/rivers";
	}
}
