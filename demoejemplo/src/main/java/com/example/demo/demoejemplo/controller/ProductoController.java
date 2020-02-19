package com.example.demo.demoejemplo.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.demoejemplo.models.dao.StudentRepository;
import com.example.demo.demoejemplo.models.entity.ProductoBD;




@Controller
@RequestMapping("/productos/")
public class ProductoController {
	
	
	@Autowired
	StudentRepository studentRepository;


	@GetMapping("signup")
	public String showSignUpForm(ProductoBD producto,Model model) {
		model.addAttribute("producto", producto);

		return "add-student";
	}

	@GetMapping("list")
	public String showUpdateForm(Model model) {
		model.addAttribute("students", studentRepository.findAll());
		return "index";
	}

	@PostMapping("add")
	public String addStudent(@Valid ProductoBD producto, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "add-student";
		}

		studentRepository.save(producto);
		return "redirect:list";
	}

	@GetMapping("edit/{id}")
	public String showUpdateForm(@PathVariable("id") long id, Model model) {
		ProductoBD student = studentRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid student Id:" + id));
		model.addAttribute("student", student);
		return "update-student";
	}

	@PostMapping("update/{id}")
	public String updateStudent(@PathVariable("id") long id, @Valid ProductoBD student, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			student.setId(id);
			return "update-student";
		}

		studentRepository.save(student);
		model.addAttribute("students", studentRepository.findAll());
		return "index";
	}

	@GetMapping("delete/{id}")
	public String deleteStudent(@PathVariable("id") long id, Model model) {
		ProductoBD student = studentRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid student Id:" + id));
		studentRepository.delete(student);
		model.addAttribute("students", studentRepository.findAll());
		return "index";
	}

}
