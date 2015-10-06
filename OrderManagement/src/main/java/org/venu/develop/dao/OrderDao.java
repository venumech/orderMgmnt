package org.venu.develop.dao;

import java.io.IOException;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.venu.develop.model.Order;
import org.venu.develop.service.OrderProcessServiceImpl;

import oracle.jdbc.OracleTypes;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import org.venu.develop.model.Address;
import org.venu.develop.model.LineItem;
import org.venu.develop.model.Order;

@Repository("orderDao")
public class OrderDao extends JdbcDaoSupport  implements OrderDBInfc {

	private final Logger logger = LoggerFactory.getLogger(OrderProcessServiceImpl.class);
    
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private DriverManagerDataSource dataSource;
    
	public OrderDao(){}
	
	public OrderDao(DataSource basicDataSource) {
		this.jdbcTemplate = new JdbcTemplate(basicDataSource);
	}


	@PostConstruct
	private void initialize() {
		// logger.info("Database server time is: {}", 
		//		jdbcTemplate.queryForObject("SELECT CURRENT_TIMESTAMP from dual", Date.class));
		setDataSource(dataSource);
	}
	
	
	private OrderLookUpProcedure getOrder;
	
	//////////////////////
	private class OrderLookUpProcedure extends StoredProcedure {
		
        private static final String SQL = "sysdate";

        private List<LineItem> orderList = new ArrayList<LineItem>();
        private static final String SPROC_NAME = "VENU.ORDER_PROCESS_LOOKUP";

        private String instructions;
        
        public OrderLookUpProcedure(DataSource dataSource) {
            super(dataSource, SPROC_NAME);
            declareParameter(new SqlParameter("l_order_id", OracleTypes.INTEGER, instructions));
            declareParameter(new SqlOutParameter("l_instructions", OracleTypes.VARCHAR, instructions));
            declareParameter(new SqlOutParameter("LINES_TABLE", OracleTypes.ARRAY));
            declareParameter(new SqlOutParameter("ADDRESS_OBJ", OracleTypes.STRUCT));
            declareParameter(new SqlOutParameter("ADDRESS_OBJ", OracleTypes.STRUCT));
            compile();
        }

        public String aggregate(Integer start, Integer end) {
            Map<String, Integer> inParameters = new HashMap<String, Integer>(2);
            inParameters.put("l_order_id", 27309);

            Map outParameters = execute(inParameters);
            if (outParameters.size() > 0) {
              return (String) outParameters.get("l_instructions");
            } else {
              return "";
            }
          }
        
        public Map<String, Object> execute() {
            // again, this sproc has no input parameters, so an empty Map is supplied
            return super.execute(new HashMap<String, Object>());
        }
		
	}
	
	
	/////////////////////

	
	@Override
	public Order findById(Long orderId) throws IOException, SQLException, ClassNotFoundException {
		Order order = null;
		if (jdbcTemplate == null){
			System.out.println(" jdbcTemplate NULL +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++========" );
		}
		int returnval = jdbcTemplate.update("select 1 from dual");
		System.out.println("returnval ========" + returnval);
		
		String inst = new OrderLookUpProcedure(dataSource).aggregate(10, 1);
		System.out.println("returnval ========++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++" + inst);
	
/*		cstmt = conn.prepareCall("begin " + "VENU.ORDER_PROCESS_LOOKUP(:1,:2,:3,:4,:5,:6); end;");

		logger.debug("Callable statement prepared.");
		cstmt.registerOutParameter(1, Types.VARCHAR); // instructions
*/
		
		return order;
	}

	@Override
	public Order saveId(Order order) throws IOException, SQLException {
		
		
		return order;
	}

}
