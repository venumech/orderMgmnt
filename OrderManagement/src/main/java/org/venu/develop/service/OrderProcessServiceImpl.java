package org.venu.develop.service;

import java.io.IOException;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.venu.develop.dao.OrderDBInfc;
import org.venu.develop.model.Order;

/**
 * service Layer
 * @author Venu
 * 
 */
@Service 
public class OrderProcessServiceImpl implements OrderProcessService {

	private final Logger logger = LoggerFactory.getLogger(OrderProcessServiceImpl.class);
    @Autowired
	private OrderParser orderParser;
    
    @Autowired
    private OrderDBInfc orderDao;

    @Autowired
    private LocalCacheService<Long, Order> lruCache;

   
  public OrderProcessServiceImpl() {}


	@Override
	public Order saveOrder(MultipartFile mFile) throws IOException, SQLException {
		Order order = orderParser.parse(mFile);
		order = orderDao.saveId(order);
		lruCache.put(order.getId(), order);
		logger.debug("the order object is added to server side local cache! cache.size= " + lruCache.size());
		return order;
	}

	@Override
	public Order searchOrder(Long orderId) throws IOException, SQLException, ClassNotFoundException {
		logger.debug("in OrderProcessServiceImpl.searchOrder( id:" +orderId + ")");
		
		Order order = new Order();
		order = lruCache.get(orderId);
		if (order != null) {
			logger.debug("the 'Order' object is available in cache. no need to fetch from database.");
			
		}else {
			logger.debug("the order object is not in cache! Now, attepting to fetch from database.");
			order = orderDao.findById(orderId);
			lruCache.put(order.getId(), order);
		}

		logger.debug("ids --> "); lruCache.printItems();
		logger.debug("Done. OrderProcessServiceImpl.searchOrder( id:" +orderId + ")");
		return order;
	}

}