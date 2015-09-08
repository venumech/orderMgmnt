package org.venu.develop.service;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.web.multipart.MultipartFile;
import org.venu.develop.model.Order;

public interface OrderProcessService {


	public Order saveOrder(MultipartFile mFile) throws IOException, SQLException;
	
	public Order searchOrder (Long orderId) throws SQLException, IOException, ClassNotFoundException;
}
