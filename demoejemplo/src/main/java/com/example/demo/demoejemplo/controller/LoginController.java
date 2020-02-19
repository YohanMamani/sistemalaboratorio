package com.example.demo.demoejemplo.controller;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.demo.demoejemplo.models.entity.Login;

@Controller
public class LoginController {

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String  getLoginForm() {
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String  login(@ModelAttribute(name="loginform") Login login, Model model) {
		
		String username= login.getUsername();
		String password= login.getPassword();
		
		if("admin".equals(username) && "admin".equals(password)) {
			return "redirect:/listarpedidos";
		}
		
		
		model.addAttribute("${invalido}", true);
		
		return "login";
	}
	
}
