package jp.dip.gpsoft.springsand.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.dip.gpsoft.springsand.Utils;
import jp.dip.gpsoft.springsand.exception.BadRequestStatusException;
import jp.dip.gpsoft.springsand.model.Valley;
import jp.dip.gpsoft.springsand.service.ValleyService;

@Controller
@RequestMapping("/valleys")
public class ValleyController {

	@Autowired
	private ValleyService valleyService;

	@GetMapping
	public String index(Model model, @RequestParam(name = "q", required = false) String q,
			@PageableDefault(size = 10) Pageable pageable) {
		model.addAttribute("valleys",
				q == null ? valleyService.findAllValleys(pageable)
						: valleyService.findValleys("%" + q + "%", pageable));
		model.addAttribute("q", q);
		model.addAttribute("pathWithPage", Utils.pathWithPage("", pageable, "q", q));
		model.addAttribute("pathWithSort", Utils.pathWithSort("", pageable, "q", q));
		return "valley/index";
	}

	@GetMapping("/{id:^[\\d]+$}")
	public String show(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("valley", valleyService.lookupValley(id));
		return "valley/details";
	}

	@GetMapping("/new")
	public String newValley(Model model) {
		model.addAttribute("valley", new Valley(""));
		return "valley/form";
	}

	@GetMapping("/{id:^[\\d]+$}/edit")
	public String editValley(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("valley", valleyService.lookupValley(id));
		return "valley/form";
	}

	@PostMapping("")
	public String create(@ModelAttribute Valley valley) {
		valleyService.saveValley(valley);
		return "redirect:/valleys";
	}

	@PostMapping("/{id:^[\\d]+$}")
	public String update(@PathVariable("id") Integer id, @ModelAttribute Valley valley) {
		if (!id.equals(valley.getId())) {
			throw new BadRequestStatusException();
		}
		valleyService.saveValley(valley);
		return "redirect:/valleys";
	}

	@PostMapping("/{id:^[\\d]+$}/delete")
	public String destroy(@PathVariable("id") Integer id) {
		valleyService.deleteValley(id);
		return "redirect:/valleys";
	}
}
