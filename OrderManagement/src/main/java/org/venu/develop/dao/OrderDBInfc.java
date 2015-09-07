/**
 * 
 */
package org.venu.develop.dao;

import java.sql.SQLException;

import org.springframework.web.multipart.MultipartFile;
import org.venu.develop.model.Order;

/**
 * @author Venu
 *
 */

public interface OrderDBInfc {
	public Order lookUpDB(int orderId) throws SQLException;
	public Order dataInsert(Order order) throws SQLException;

}
