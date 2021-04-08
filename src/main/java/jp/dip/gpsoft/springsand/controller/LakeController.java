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

	//	@Autowired
	//	private AutowireCapableBeanFactory factory;

	@ModelAttribute("lakeForm")
	public LakeForm emptyLakeForm() {
		// このメソッドが返すオブジェクトは、
		// lakeFormという名前の属性としてViewへ渡される。
		// model.addAttribute("lakeForm", ...)に相当。

		LakeForm cf = new LakeForm();
		// FormがPOJOなら↑
		// FormにDIしたい場合は↓
		//LakeForm cf = factory.createBean(LakeForm.class);
		return cf;
	}

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
		return "lake/form";
	}

	@GetMapping("/{id:^[\\d]+$}/edit")
	public String editLake(@PathVariable("id") Integer id, LakeForm lake, Model model) {
		lake.populateWith(lakeService.lookupLake(id));
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
