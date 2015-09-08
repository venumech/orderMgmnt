package org.venu.develop.web;

import java.io.IOException;
import java.sql.SQLException;
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
import org.venu.develop.model.LineItem;
import org.venu.develop.model.Order;
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
/*
	@Autowired
	private  OrderDBHelper orderDBHelper;
	
	public OrderController(OrderDBHelper orderDBHelper) {
		this.orderDBHelper = orderDBHelper;
	}
*/

	@Autowired
	private  OrderProcessService orderProcessServiceImpl;
/*
	public OrderController(OrderProcessService orderProcessServiceImpl) {
		this.orderProcessServiceImpl = orderProcessServiceImpl;
	}

*/

    @RequestMapping(value = "/order.do", method = RequestMethod.GET)
    public String index(Model model, WebRequest webRequest) {
        return "order";
    }
    
    @RequestMapping(value = "/createOrder.do", method = RequestMethod.POST)
    public @ResponseBody String createOrder(@RequestParam("file") MultipartFile mFile) {

		logger.debug("createOrder() is started!");
    	
        try {
            if (mFile != null && mFile.getSize() > 0) {
                Order order = orderProcessServiceImpl.saveOrder(mFile);
                //System.out.println(order.toString());
                return "Order saved successfully Id=" + order.getId() + "<BR> <BR> <BR> " +new Gson().toJson(order); //success
            } else {
                return "Error occurred while processing request, please try later."; //fail
            }
        } catch (Exception e) {
            return "Error occurred while processing order, please try later." +e.getMessage();
        }
    }

    @RequestMapping(value = "/searchOrder.do", method = RequestMethod.GET)
    public @ResponseBody String searchOrder(@RequestParam("q") String query) {
    	
        String result ="";
        Order order = null;
		logger.debug("searchOrder() is started!; query=" + query);

        Long orderId = null;
        try{
        	orderId = Long.parseLong(query);
        	//Integer.parseInt(query);
        	
        	logger.debug("searchOrder(): orderId =" +orderId); 

            //do lookup in service layer
        	order = orderProcessServiceImpl.searchOrder(orderId);

        } catch (NumberFormatException e){
        		result= "ERROR: " + query +", Not a valid order_id. please try with a valid number";
        		return result;
        } catch (SQLException e) {
			return "ERROR: Search order is not processed. <br>" + e.getMessage();
		} catch (IOException e) {
			return "ERROR: Search order is not processed. <br>" + e.getMessage();
		} catch (ClassNotFoundException e) {
			return "ERROR: Search order is not processed. <br>" + e.getMessage();
		}

	 /* {
            //do lookup in service layer

            Address from = new Address("Charlotte", "NC", "28277");
            Address to = new Address("Cary", "NC", "28213");

            Item item1 = new Item("1", 1.0, true, "Test1");
            Item item2 = new Item("2", 1.0, false, "Test2");

            List<Item> items = new ArrayList<Item>();
            items.add(item1);
            items.add(item2);

            shipment = new Shipment(from, to, items, "No instrunctions");

        }*/

        	//for text display...
       /* if (order != null)
        	result = proessTreeStructure(order);
        else result= "No data found for this orderid. please try with a valid id)";
	*/

        //for json display
        if (order == null){
        	return "ERROR: No data found";
        }
        result = new Gson().toJson(order); 
        //System.out.println(result);
    		
        return result;
    }



}