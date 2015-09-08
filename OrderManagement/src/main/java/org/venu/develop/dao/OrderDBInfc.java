/**
 * 
 */
package org.venu.develop.dao;

import java.io.IOException;
import java.sql.SQLException;

import org.venu.develop.model.Order;

/**
 * @author Venu
 *
 */

public interface OrderDBInfc {
	public Order lookUpDB(Long orderId) throws IOException, SQLException, ClassNotFoundException;
	public Order dataInsert(Order order) throws SQLException;

}