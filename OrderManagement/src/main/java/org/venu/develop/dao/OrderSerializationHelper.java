package org.venu.develop.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.venu.develop.model.Order;
import org.venu.develop.web.OrderController;

//@Repository("orderDao")
public class OrderSerializationHelper implements OrderDBInfc{

	private final static Logger logger = LoggerFactory.getLogger(OrderSerializationHelper.class);
	

	/*
	 * this is a kind of making the infrastructure ready before any instance methods run.
	 * the directory is created if not already exists 
	 */
	static {
		createDir();
	}
	
	/*
	 * check if the directory already exists. if not, crate one now!
	 */
	 private static void createDir() {
		File file = new File(SERIALIZED_DATA_LOCATION);
		logger.debug("check if the directory already exists: " +SERIALIZED_DATA_LOCATION);
		if (file.exists()) {
			logger.debug("Directory exists already!");
			
			return;
		}
			if (file.mkdirs()) {
				logger.debug("Directory is created!");
			} else {
				logger.error("Failed to create directory!");
			}
		
		
	}
	
	
	@Override
	public Order lookUpDB(Long orderId) throws IOException, ClassNotFoundException{
		Order order = null;
		try {
			FileInputStream fileIn = new FileInputStream(SERIALIZED_DATA_LOCATION + orderId + ".ser");
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



	/*
	 * (non-Javadoc)
	 * @see org.venu.develop.dao.OrderDBInfc#dataInsert(org.venu.develop.model.Order)
	 * saves the order object to the disk as mentioned in the folder: 'SERIALIZED_DATA_LOCATION'
	 */
	@Override
	public Order dataInsert(Order order) throws IOException {
		long orderId =  System.currentTimeMillis(); 
		order.setId(orderId);
		logger.debug("dataInsert() is started!");
			try {
				FileOutputStream fileOut = new FileOutputStream(SERIALIZED_DATA_LOCATION + order.getId()+".ser");
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(order);
				out.close();
				fileOut.close();
				logger.debug("Serialized data is saved in '" + SERIALIZED_DATA_LOCATION + "'. orderId="+ order.getId()+".ser");
			} catch (IOException e) {
				//throw new IOException(e.getMessage());
				logger.error("ERROR: order not saved. " + e.getMessage());
				throw new IOException(e.getMessage());
			}
		

		return order;
	}

}
