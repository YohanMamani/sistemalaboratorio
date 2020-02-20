package com.example.demo.demoejemplo.models.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.demoejemplo.models.dao.IPedidoDao;
import com.example.demo.demoejemplo.models.dao.IPedidoDetalleDao;
import com.example.demo.demoejemplo.models.dao.IPedidocrudDao;
import com.example.demo.demoejemplo.models.dao.IProductoBDDao;
import com.example.demo.demoejemplo.models.dao.IProductoDao;
import com.example.demo.demoejemplo.models.entity.Pedido;
import com.example.demo.demoejemplo.models.entity.PedidoDetalle;
import com.example.demo.demoejemplo.models.entity.Producto;
import com.example.demo.demoejemplo.models.entity.ProductoBD;
import com.restfb.json.JsonArray;
import com.restfb.json.JsonObject;

@Service
public class PedidoServiceImpl implements IPedidoService{
	
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String SERVER_PATH = "http://localhost/";
    static java.util.Date fecha = new Date();
    
    float montototal = 0;
    
    static String nombrelaboratorio = "Empresa SISLAB";
    
    static Date date = Calendar.getInstance().getTime();  
    
    static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
    
    static String strDate = dateFormat.format(date);  
    
    static String fechaconfirmacion = strDate;
    
    @Autowired
    IProductoBDDao productoDao;
	
	@Autowired
	IPedidoDao pedidodao;
	
	@Autowired
	IPedidocrudDao pedidocrud;
	
	@Autowired
	IPedidoDetalleDao detalledao;
	
	private static final Logger logger = Logger.getLogger(PedidoServiceImpl.class.getName());

	@Override
	@Transactional(readOnly = true)
	public List<Pedido> findAll() {
			// TODO Auto-generated method stub
			return (List<Pedido>) pedidodao.findAll();
	}
	
	
	
	
	@Override
	public void saved(String pedidoJson) {
    
		
		
    	JsonObject json = new JsonObject(pedidoJson);
    	Pedido  pedidonuevo = new Pedido();
    	String ruc = (String) json.get("ruc"); 
    	pedidonuevo.setRuc(ruc);
    	pedidonuevo.setNombre((String) json.get("nombre"));
    	pedidonuevo.setFecha((String) json.get("fecha"));
    	pedidonuevo.setEstado("PENDIENTE");
        logger.info(json.getString("fecha") );

    	//String productos = (String) json.get("productos");
    	//JsonObject json2 = new JsonObject(productos);
    	//PedidoDetalle pedidodetalle = new PedidoDetalle();
    	//pedidodetalle.setNombre("nombreproducto");
    	//pedidodetalle.setNombre("cantidad");
    	//pedidonuevo.setPedidosdetalles(pedidosdetalle);
    
       	JsonArray jsonArray = json.getJsonArray("productos");
       	Pedido pedidosave = pedidodao.save(pedidonuevo);
       	
       	pedidosave.setPedidosdetalles(CargarArray(jsonArray, pedidosave));
    	
    	pedidodao.save(pedidosave);
	}
	
	public ArrayList<PedidoDetalle> CargarArray(JsonArray jsonArray, Pedido pedidoid){
	ArrayList<String> Lista = new ArrayList<>();
	ArrayList<PedidoDetalle> pedidosdetalles = new ArrayList<>(); 
	    for(int i=0;i<jsonArray.length();i++){
	        try {
	        	
	        	PedidoDetalle  pedido = new PedidoDetalle();
	        	
	        	JsonObject json3 = jsonArray.getJsonObject(i);
	            //Aquí se obtiene el dato y es guardado en una lista
	            Lista.add(json3.getString("nombreproducto"));
	            pedido.setNombre(json3.getString("nombreproducto"));
	            pedido.setCantidad(Integer.parseInt(json3.getString("cantidad")));
	            pedido.setPedido(pedidoid);
	            pedidosdetalles.add(pedido);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	
	    return pedidosdetalles;
	}




	@Override
	@Transactional(readOnly = true)
	public Page<Pedido> findAll(Pageable pageable) {
		Page<Pedido>  pedidos = pedidodao.findAll(pageable);	
		List<Pedido>  Listpedido = new ArrayList<Pedido>();
		if(pedidos.isEmpty()) {
			return pedidos;
		}
		
		for(Pedido pedidito: pedidos) {
			if(pedidito.getEstado().equals("PENDIENTE")) {
				Listpedido.add(pedidito);
			}
		}
		
		
		final Page<Pedido> page = new PageImpl<>(Listpedido);

		return page;	
	}




	@Override
	@Transactional(readOnly = true)
	public Pedido fetchByIdWithFacturas(Long id) {
		
		List<PedidoDetalle> pedidosdetalles  = detalledao.findByidpedido(id);
		
		Pedido pedidolistar = pedidodao.fetchByIdWithFacturas(id);
		
		pedidolistar.setPedidosdetalles(pedidosdetalles);
		
		return pedidolistar;
	
	}




	@Override
	@Transactional(readOnly = true)
	public Pedido findOne(Long id) {
		return pedidodao.findById(id).orElse(null);
	}




	@Override
	public void delete(Long id) {
		
		Pedido pedidobd = pedidodao.findById(id).orElse(null);
		
		logger.info("ID DEL PEDIDO ES : "+pedidobd.getId() +"YY YYYYY "+pedidobd.getNombre());

		
		String mensaje = "No se ha podido realizar la entrega del pedido enviado el dia "+ pedidobd.getFecha() +". Lo sentimos";
		
        pedidobd.setEstado("RECHAZADO");
        
        
		logger.info("ID DEL PEDIDO ES : "+pedidobd.getId() +"YYZZZZZ YYYYY "+pedidobd.getEstado());

		//pedidodao.updateTitle(id,"RECHAZADO");
    	
		pedidodao.save(pedidobd);

       	
        //Creamos un objeto JSON
        JSONObject jsonObj = new JSONObject();
        //AÃ±adimos el nombre, apellidos y email del usuario
        jsonObj.put("nombrelaboratorio",nombrelaboratorio);
        jsonObj.put("nombrefarmacia", pedidobd.getNombre());
        jsonObj.put("fechaconfirmacion", fechaconfirmacion);
        jsonObj.put("mensaje", mensaje);
        //Creamos una lista para almacenar el JSON
        List  l = new LinkedList();
        l.addAll(Arrays.asList(jsonObj));
        //Generamos el String JSON
        String jsonString = JSONValue.toJSONString(l);

        
        try {
            //Codificar el json a URL
            jsonString = URLEncoder.encode(jsonString, "UTF-8");
            //Generar la URL
            String url = SERVER_PATH+"ventas/vistas/modulos/notificacion.php";
            //Creamos un nuevo objeto URL con la url donde queremos enviar el JSON
            URL obj = new URL(url);
            //Creamos un objeto de conexiÃ³n
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            //AÃ±adimos la cabecera
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            //Creamos los parametros para enviar
            String urlParameters = "json="+jsonString;
            // Enviamos los datos por POST
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            //Capturamos la respuesta del servidor
            int responseCode = con.getResponseCode();
            
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            //Mostramos la respuesta del servidor por consola
            //cerramos la conexiÃ³n
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
		


	}

	public boolean actualizarstock(Pedido pedido) {
			
		Iterable<ProductoBD> productos = productoDao.findAll();
		
		List<PedidoDetalle> pedidosdetalle = pedido.getPedidosdetalles();
		
		List<ProductoBD> pedidosdetalleactual = new ArrayList<ProductoBD>();
		boolean sentence =true;
		for(PedidoDetalle pede : pedidosdetalle) {
			
			for(ProductoBD prod : productos) {
				
				if(pede.getNombre().equals(prod.getNombre())) {
					
					int cantactual = 0;
					
					cantactual = prod.getCantidad() - pede.getCantidad(); 
					
					if(cantactual < 0 ) {
						sentence = false;
					}else {
						prod.setCantidad(cantactual);
						pedidosdetalleactual.add(prod);
					}
					
					
				}
				
			}
		}
		
		
		if(pedidosdetalle.size() != pedidosdetalleactual.size()) {
			sentence = false;
		}
		
		if(sentence) {
			for(ProductoBD prd :pedidosdetalleactual) {
				for(PedidoDetalle pd :pedidosdetalle) {
					if(prd.getNombre().equals(pd.getNombre())) {
						montototal= montototal + prd.getPrecio()*pd.getCantidad();
						productoDao.save(prd);
					}
				}
			}
		}
		
		return sentence;
	}
	


	@Override
	public String confirmar(Long id) {

		
		Pedido pedidobd = pedidodao.findById(id).orElse(null);

		if(!actualizarstock(pedidobd)) {
			return "No cuenta con el Stock suficiente para aceptar el pedido";
		}
		
		
		String mensaje = "Se confirma la entrega del pedido enviado el dia "+ pedidobd.getFecha() +". Monto total : S/"+montototal+".";
		
		
        //Creamos un objeto JSON
        JSONObject jsonObj = new JSONObject();
        //AÃ±adimos el nombre, apellidos y email del usuario
        jsonObj.put("nombrelaboratorio",nombrelaboratorio);
        jsonObj.put("nombrefarmacia", pedidobd.getNombre());
        jsonObj.put("fechaconfirmacion", fechaconfirmacion);
        jsonObj.put("mensaje", mensaje);
        //Creamos una lista para almacenar el JSON
        List  l = new LinkedList();
        l.addAll(Arrays.asList(jsonObj));
        //Generamos el String JSON
        String jsonString = JSONValue.toJSONString(l);
        System.out.println("JSON GENERADO:");
        System.out.println(jsonString);
        System.out.println("");
        
        try {
            //Codificar el json a URL
            jsonString = URLEncoder.encode(jsonString, "UTF-8");
            //Generar la URL
            String url = SERVER_PATH+"ventas/vistas/modulos/notificacion.php";
            //Creamos un nuevo objeto URL con la url donde queremos enviar el JSON
            URL obj = new URL(url);
            //Creamos un objeto de conexiÃ³n
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            //AÃ±adimos la cabecera
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            //Creamos los parametros para enviar
            String urlParameters = "json="+jsonString;
            // Enviamos los datos por POST
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            //Capturamos la respuesta del servidor
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + urlParameters);
            System.out.println("Response Code : " + responseCode);
            
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            //Mostramos la respuesta del servidor por consola
            System.out.println(response);
            //cerramos la conexiÃ³n
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
		
        pedidobd.setEstado("CONFIRMADO");
    	pedidodao.save(pedidobd);
		
		return "Se confirmo el pedido con éxito";

	}




	@Override
	public Page<Pedido> findAll2(Pageable pageable) {

		return  pedidodao.findAll(pageable);	
	}

}