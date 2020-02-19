package com.example.demo.demoejemplo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.demoejemplo.models.entity.Cliente;
import com.example.demo.demoejemplo.models.entity.Pedido;
import com.example.demo.demoejemplo.models.service.IPedidoService;
import com.example.demo.demoejemplo.util.paginator.PageRender;

@Controller
@SessionAttributes("pedido")
public class PedidoController {


	@Autowired
	private IPedidoService pedidoservice;
	
	
	@RequestMapping(value = "/historialpedidos", method = RequestMethod.GET)
	public String historial(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

		Pageable pageRequest = PageRequest.of(page, 15);

		Page<Pedido> pedidos = pedidoservice.findAll2(pageRequest);

		PageRender<Pedido> pageRender = new PageRender<Pedido>("/listarpedidos", pedidos);
		model.addAttribute("titulo", "Listado de pedidos");
		model.addAttribute("pedidos", pedidos);
		model.addAttribute("page", pageRender);
		return "historial";
	}
	
	@RequestMapping(value = "/listarpedidos", method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

		Pageable pageRequest = PageRequest.of(page, 15);

		Page<Pedido> pedidos = pedidoservice.findAll(pageRequest);

		PageRender<Pedido> pageRender = new PageRender<Pedido>("/listarpedidos", pedidos);
		model.addAttribute("titulo", "Listado de pedidos");
		model.addAttribute("pedidos", pedidos);
		model.addAttribute("page", pageRender);
		return "listarpedidos";
	}
	
	@GetMapping(value = "/verpedido/{id}")
	public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

		Pedido pedido = pedidoservice.fetchByIdWithFacturas(id);
		if (pedido == null) {
			flash.addFlashAttribute("error", "El pedido no existe en la base de datos");
			return "redirect:/listarpedidos";
		}

		model.put("pedido", pedido);
		model.put("titulo", "Detalle cliente: " + pedido.getNombre());
		return "verpedido";
	}
	
	@RequestMapping(value = "/eliminarpedido/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {

		if (id > 0) {
			Pedido pedido = pedidoservice.findOne(id);

			pedidoservice.delete(id);
			flash.addFlashAttribute("success", "Pedido rechazado con éxito!");

		}
		return "redirect:/listarpedidos";
	}
	
	
	@RequestMapping(value = "/confirmarpedido/{id}")
	public String confirmar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {

		if (id > 0) {
			Pedido pedido = pedidoservice.findOne(id);

			pedidoservice.confirmar(id);
			flash.addFlashAttribute("success", "Pedido confirmado con éxito!");

		}
		return "redirect:/listarpedidos";
	}
	
}
