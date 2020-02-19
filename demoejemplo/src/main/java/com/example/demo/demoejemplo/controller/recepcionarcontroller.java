 package com.example.demo.demoejemplo.controller;
import com.example.demo.demoejemplo.models.service.IClienteService;
import com.example.demo.demoejemplo.models.service.IPedidoService;
import com.google.gson.Gson;
import com.restfb.json.JsonObject;
import java.util.List;

import java.util.logging.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recepcionarpedidos")
public class recepcionarcontroller {
	
	   private static final Logger logger = Logger.getLogger(recepcionarcontroller.class.getName());
	
	   
	 @Autowired
	 private IPedidoService pedidoService;

	   
	   
    @PostMapping("/probando")
    public String homeInit(@RequestBody String mensaje) {
    	mensaje = mensaje.substring(0, mensaje.length() - 1);
    	mensaje = mensaje.substring(1);
    	logger.info(mensaje);
    	pedidoService.saved(mensaje);
    	
        return "llegó el mensaje";
    }
}
