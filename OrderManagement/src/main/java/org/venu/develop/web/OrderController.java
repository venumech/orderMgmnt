package org.venu.develop.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.venu.develop.model.Order;
import org.venu.develop.model.OrderError;
import org.venu.develop.service.OrderProcessService;

import com.google.gson.Gson;


/**
 * need to build a simple RIA (Rich Internet Application) that allows for uploading and 
 * looking up Transportation Orders. The front end of the RIA would consist of a 
 * single HTML (JSP) page containing a form whose layout would slightly change depending 
 * on the chosen action:
 * 'Create Order’ action uploads the Transportation Order XML to the server, 
 * whereas ‘Search Order’ action looks up the order by its assigned ID.
 *
 * This runs on Servlet 3 specification
 * @author Venu
 *
 */
@Controller
public class OrderController {

	private final Logger logger = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private  OrderProcessService orderProcessServiceImpl;
/*
	public OrderController(OrderProcessService orderProcessServiceImpl) {
		this.orderProcessServiceImpl = orderProcessServiceImpl;
	}

*/

    @RequestMapping(value = "/order.do", method = RequestMethod.GET)
    public String index(Model model, WebRequest webRequest) {
    	
    	/* Toggle between "order.jsp" and "orderJQ.jsp"   	 */
    	
        //return "order"; //UI is Angular enabled
    	return "orderJQ"; //UI is JQuery enabled
    }
    
    @RequestMapping(value = "/createOrder.do", method = RequestMethod.POST)
    public @ResponseBody String createOrder(@RequestParam("file") MultipartFile mFile) {

		logger.debug("createOrder() is started!");
		Order order = null;
		OrderError oError = new OrderError();
		Boolean isError = false;
        try {
            if (mFile != null && mFile.getSize() > 0) {
                order = orderProcessServiceImpl.saveOrder(mFile);
            } else {
            	oError.setErrorMsg("No file uploaded! ");
            	oError.setError(true);
            	isError = true;
            }
        } catch (Exception e) {
        	isError = true;
        	String status = "Error occurred while processing order, please try later." +e.getMessage();
        	oError.setError(isError);
        	oError.setErrorMsg(status);
        }
        
        if (isError) {
        	logger.error(new Gson().toJson(oError));
        	return  new Gson().toJson(oError);
        } else {
            logger.debug(  new Gson().toJson(order) ); //success
        }
        
    	return  new Gson().toJson(order);
    }

    /*
     * Search Order activity
     */
    @RequestMapping(value = "/searchOrder.do", method = RequestMethod.GET)
    public @ResponseBody String searchOrder(@RequestParam("q") String query) {

        String jsonObj ="";
        String errorStr ="";
		OrderError oError = new OrderError();
		Boolean isError = false;
        Order order = null;
		logger.debug("searchOrder() is started!; query=" + query);

        Long orderId = null;
        try{
        	orderId = Long.parseLong(query);        	
        	logger.debug("searchOrder(): orderId =" +orderId); 

            //do lookup in service layer
        	order = orderProcessServiceImpl.searchOrder(orderId);

        } catch (NumberFormatException e){
        	isError=true;
        	errorStr= "'" + query +"', Not a valid Order Id. Please try with a valid number";
        	oError.setError(isError);
        	oError.setErrorMsg(errorStr);
        } catch (SQLException e) {
        	errorStr= e.getMessage();
        	isError=true;
        	oError.setError(isError);
        	oError.setErrorMsg(errorStr);
		} catch (IOException e) {
			isError=true;
        	errorStr= e.getMessage() ;  
        	oError.setError(isError);
        	oError.setErrorMsg(errorStr);
		} catch (ClassNotFoundException e) {
			isError=true;
        	errorStr=  e.getMessage();
        	oError.setError(isError);
        	oError.setErrorMsg(errorStr);
		}


        //for json display
        if (isError || order == null){
        	logger.error(errorStr);
        	
        	return new Gson().toJson(oError); 
        }
        
        
        jsonObj = new Gson().toJson(order); 
        System.out.println(jsonObj);
    		
        return jsonObj;
    }
    
    /*
     * autocomplete feature added.
     * This is to send the matched order ids to the browser client 
     */
    
    @RequestMapping(value = "/getMatchedIds.do", method = RequestMethod.GET)
        public @ResponseBody List<String> getMachedNames(@RequestParam("term") String query){
        
        List<String> matchedIds = orderProcessServiceImpl.getAutoCompleteList(query);
        
        return matchedIds;
        }



}