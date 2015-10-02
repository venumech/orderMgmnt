package org.venu.develop.dao;



import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.venu.develop.model.Address;
import org.venu.develop.model.LineItem;
import org.venu.develop.model.Order;

import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.STRUCT;
import oracle.sql.TypeDescriptor;

/**
  * Simple Java Program to connect Oracle database by using Oracle JDBC thin driver.
  * Make sure you have Oracle JDBC thin driver in your classpath before running this program
  * @author venu
  */
@Repository("orderDao")

public class OrderDBHelper implements OrderDBInfc{
	 
	@Value("${oracle.db.driver}")
	private String oraDriver;

	@Value("${oracle.db.url}")
	private String oraUrl;

	@Value("${oracle.user}")
	private String oraUser;
 
	@Value("${oracle.password}")
	private String oraPwd;
 
	private final Logger logger = LoggerFactory.getLogger(OrderDBHelper.class);

	private static final String sqlLookUp = 
		     "Select a.id, instructions, b.city as from_city, b.state as from_state, b.zip as from_zip,"
		     + " c.city as to_city, c.state as to_state, c.zip as to_zip,"
		     + " my_object(order_id , weight, volume, hazard, product) as ob "
		     + " from ORDERS a, (select * from ADDRESS) b, (select * from ADDRESS) c, LINE_ITEMS d"
		     + " where a.id = d.order_id and a.from_address_id = b.address_id"
		     + " and a.to_address_id = c.address_id"
		     + " and a.id = ";
  
    public Order saveId(Order o) throws SQLException{
     	Long id = dataInsertWork(o);
    	o.setId(id);
    	return o;
    	
    }
    
    private Long dataInsertWork(Order order) throws SQLException {

        Long orderId = 0l;
        String message = "";

         //URL of Oracle database server
         String url = oraUrl; //"jdbc:oracle:thin:@localhost:1521:LOGISTICS"; 
       
         //properties for creating connection to Oracle database
         Properties props = new Properties();
	     props.setProperty("user", oraUser);
	     props.setProperty("password", oraPwd);
       
         //creating connection to Oracle database using JDBC
         Connection conn=null;
         CallableStatement cstmt =null;
         
         /////////////////////////////////////
         Object[] personAttribs = new Object[order.getLines().size()];
         /////////////////////////////////////
         
		try {
			conn = DriverManager.getConnection(url,props);
			TypeDescriptor td = null;
			//StructDescriptor personStructDesc = StructDescriptor.createDescriptor("PERSON_REC", conn);
			
	         //creating PreparedStatement object to execute query
	         //PreparedStatement preStatement = conn.prepareStatement(sql);
	         cstmt = conn.prepareCall("begin " +  "venu.order_process_proc(:1,:2,:3,:4,:5,:6,:7,:8,:9, :10); end;");

	         cstmt.registerOutParameter(9, Types.CHAR); //order id 
	         cstmt.registerOutParameter(10, Types.CHAR); //message

	         cstmt.setString(1, order.getFrom().getCity());
	         cstmt.setString(2, order.getFrom().getState());
	         cstmt.setString(3, order.getFrom().getZip());
	         cstmt.setString(4, order.getTo().getCity());
	         cstmt.setString(5, order.getTo().getState());
	         cstmt.setString(6, order.getTo().getZip());
	         cstmt.setString(7, buildLineItemsSQL (order.getLines()));
	         cstmt.setString(8, order.getInstructions());

	         cstmt.execute();
	         orderId = cstmt.getLong(9);
	         message = cstmt.getString(10);
	         } 
		catch (SQLException e) {
			System.out.println("order not processed in database. " + message);
			System.out.println(e.toString());

			throw new SQLException("ERROR: " + e.getMessage());
 			//e.printStackTrace();
 		} finally{
 			try {
				if (cstmt !=null) cstmt.close();
				if (conn !=null) conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
 		}
		
       
        System.out.println("done: Order id = "+ orderId);
        
        return orderId;
       
     }
    
    /*
     * Dynamic sql generation. this is for inserting line items into databse as the number
     *  of line items for a each of the orders keep varies.
     * There are many ways to do but quick way is here
     */
    private String buildLineItemsSQL (List<LineItem> lineItems) {
    	StringBuilder sql = new StringBuilder();
    	sql.append ("INSERT ALL " );
    	for (LineItem lineitem: lineItems){ 
    		Boolean bl = lineitem.getHazard();
    		String hazard_str = "'N'";
    		 
    		if (bl ==null)
    			hazard_str = "null";
    		else if (bl){
    			hazard_str="'Y'";
    		}
        	sql.append ("INTO LINE_ITEMS (order_id , weight, volume, hazard, product ) ");
    		sql.append ("VALUES (:order_id_val, ");	
    		sql.append(lineitem.getWeight() +", ");
    		sql.append(lineitem.getVolume() +", ");
    		sql.append(hazard_str +", ");
    		sql.append("'"+lineitem.getProduct() +"') ");    		
    	}
    	sql.append("SELECT 1 FROM dual");
    	
    	System.out.println(sql);
    		
    	return sql.toString();
    }


	public Order lookUpDBBKUP(Long orderId) throws SQLException {
		
	    Order order = new Order();
		int rowCount=0;
	
	    order.setId(orderId);
	     //URL of Oracle database server
	     String url = oraUrl; //"jdbc:oracle:thin:@localhost:1521:LOGISTICS"; 

	     //properties for creating connection to Oracle database
	     Properties props = new Properties();
	     props.setProperty("user", oraUser);
	     props.setProperty("password", oraPwd);
	
	     //creating connection to Oracle database using JDBC
	     Connection conn;
		conn = DriverManager.getConnection(url,props);
		logger.debug("DriverManager constructed===================================================================" + oraUser);

	
	     //creating PreparedStatement object to execute query
			String sqlStr =  sqlLookUp + orderId ;
			System.out.println(sqlStr);
	     PreparedStatement pstmt = conn.prepareStatement(sqlStr);
	
	

	     pstmt.execute();
	     Address fromAddress = new Address();
	     Address toAddress = new Address();
	     List<LineItem> lineitems = new ArrayList<LineItem>();
	        ResultSet rs = pstmt.executeQuery();
	        String str = "";
	        Double val = 0d;
	        
	        
	        while (rs.next()) {
	        	rowCount++;
	        	LineItem lineItem = new LineItem();
	        	str = rs.getString("instructions");
	        	order.setInstructions(str);
	        	str = rs.getString("from_city");
	        	fromAddress.setCity(str);
	        	
	        	str = rs.getString("from_state");
	        	fromAddress.setState(str);
	        	str = rs.getString("from_zip");
	        	fromAddress.setZip(str);
	        	str = rs.getString("to_city");
	        	toAddress.setCity(str);
	        	str = rs.getString("to_state");
	        	toAddress.setState(str);
	        	str = rs.getString("to_zip");
	        	toAddress.setZip(str);
	        	val = rs.getDouble("weight");
	        	lineItem.setWeight(val);
	        	val = rs.getDouble("volume");
	        	lineItem.setVolume(val);
	        	str = rs.getString("hazard");
	        	lineItem.setHazard(new Boolean(str));
	        	str = rs.getString("product");
	        	lineItem.setProduct(str);

	        	lineitems.add(lineItem);
	        }
	        order.setFrom(fromAddress);
	        order.setTo(toAddress);
	        order.setLines(lineitems);

		    System.out.println("done: Order id = "+ orderId);
		

		if (rowCount ==0) {

	        System.out.println("FetchSize= "+ rowCount);
	        return null;
		}
		
 			try {
				if (pstmt !=null) pstmt.close();
				if (conn !=null) conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
 		
		//logger.debug("rowCount=" + rowCount);
		//logger.debug(order.toString());
	
	    return order;
	
	 }

	   /*
	    * search database for a given order id
	    */
	@SuppressWarnings("deprecation")
	public Order findById(Long orderId) throws SQLException {

		logger.debug("Entered OrderDBHelper.findById()");
		Order order = new Order();
		String message = "";

		order.setId(orderId);
		// URL of Oracle database server
		String url = oraUrl; // "jdbc:oracle:thin:@localhost:1521:LOGISTICS";

		// properties for creating connection to Oracle database
		Properties props = new Properties();
		props.setProperty("user", oraUser);
		props.setProperty("password", oraPwd);


		// creating connection to Oracle database using JDBC
		Connection conn = null;

		// creating PreparedStatement object to execute query
		CallableStatement cstmt = null;

		Address fromAddress = new Address();
		Address toAddress = new Address();
		List<LineItem> lineitems = new ArrayList<LineItem>();

		try {
			//load driver into classpath
			Class.forName(oraDriver);
			logger.debug("Trying Oracle Connection ===================================================================; Ora User" + oraUser
					+ oraUser);
			conn = DriverManager.getConnection(url, props);
			logger.debug("Connection successful; Ora User" + oraUser);

			conn = DriverManager.getConnection(url, props);

			// creating PreparedStatement object to execute query
			// PreparedStatement preStatement = conn.prepareStatement(sql);
			cstmt = conn.prepareCall("begin " + "VENU.ORDER_PROCESS_LOOKUP(:1,:2,:3,:4,:5,:6); end;");

			logger.debug("Callable statement prepared.");
			cstmt.registerOutParameter(1, Types.VARCHAR); // instructions
			cstmt.registerOutParameter(2, OracleTypes.ARRAY, "MY_TABLE"); // l_col_data:
			cstmt.registerOutParameter(3, OracleTypes.STRUCT, "ADDRESS_OBJ"); // from_address
			cstmt.registerOutParameter(4, OracleTypes.STRUCT, "ADDRESS_OBJ"); // to_address
			cstmt.setLong(5, orderId);
			cstmt.registerOutParameter(6, Types.CHAR); // message

			cstmt.execute();
			logger.debug("Extracting Line Items.");

			ARRAY array = (ARRAY) cstmt.getObject(2);
			Object[] rows = (Object[]) array.getArray();

			for (Object row : rows) {
				Object[] cols = ((oracle.sql.STRUCT) row).getAttributes();
				LineItem lI = new LineItem();

				lI = new LineItem(((BigDecimal) cols[0]).doubleValue(), ((BigDecimal) cols[1]).doubleValue(),
						cols[2].equals("Y") ? true : false, cols[3].toString());

				// for (Object col : cols) { System.out.print(col); }

				lineitems.add(lI);
			}

			ResultSetMetaData metaData = null;

			logger.debug("Extracting source Address.");
			Object[] data = (Object[]) ((STRUCT) cstmt.getObject(3)).getAttributes();// getOracleAttributes();
			fromAddress = new Address(data[0].toString(), data[1].toString(), data[2].toString());

			logger.debug("Extracting destination Address.");

			data = (Object[]) ((STRUCT) cstmt.getObject(4)).getAttributes();// getOracleAttributes();
			toAddress = new Address(data[0].toString(), data[1].toString(), data[2].toString());

			String instructions = cstmt.getString(1);
			Long id = cstmt.getLong(5);

			order.setFrom(fromAddress);
			order.setTo(toAddress);
			order.setInstructions(instructions);
			order.setLines(lineitems);
			order.setId(id);

			logger.debug("'Order' object constructed with the data.");

		} catch (SQLException | ClassNotFoundException e) {
			System.out.println(e.getMessage());
			throw new SQLException("ERROR: " + e.getMessage());
		} finally {
			if (cstmt != null)
				cstmt.close();
			if (conn != null)
				conn.close();
		}

		logger.debug("done: Order id = " + orderId);

		return order;

	}


}