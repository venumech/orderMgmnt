package org.venu.develop.dao;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.springframework.stereotype.Repository;
import org.venu.develop.model.Order;

//@Repository("orderDao")
public class OrderSerializationHelper implements OrderDBInfc{

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public Order lookUpDB(Long orderId) throws IOException, ClassNotFoundException{
		Order order = null;
		try {
			FileInputStream fileIn = new FileInputStream(SERIALIZED_LOCATION + orderId + ".ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			order = (Order) in.readObject();
			in.close();
			fileIn.close();
		} catch (IOException e) {
			String error= "The Order Number, " + orderId + " not Found in the System. " +e.getMessage();
			System.out.println(error);
			throw new IOException(error);
		} catch (ClassNotFoundException e) {
			System.out.println("Order class not found");
			System.out.println(e.getMessage());

			throw new ClassNotFoundException(e.getMessage());
			//e.printStackTrace();
		}
		return order;
	}

	@Override
	public Order dataInsert(Order order) {
		long orderId =  System.currentTimeMillis(); 
		order.setId(orderId);

			try {
				FileOutputStream fileOut = new FileOutputStream(SERIALIZED_LOCATION + order.getId()+".ser");
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(order);
				out.close();
				fileOut.close();
				System.out.printf("Serialized data is saved in /tmp/employee.ser");
			} catch (IOException e) {
				//throw new IOException(e.getMessage());
				System.out.println(e.getMessage());
			}
		

		return order;
	}

}
