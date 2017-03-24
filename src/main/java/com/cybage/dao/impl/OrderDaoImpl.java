package com.cybage.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.cybage.dao.OrderDao;
import com.cybage.dao.ProductDao;
import com.cybage.entity.Order;
import com.cybage.entity.OrderDetail;
import com.cybage.entity.Product;
import com.cybage.model.CartInfo;
import com.cybage.model.CartLineInfo;
import com.cybage.model.CustomerInfo;
import com.cybage.model.OrderDetailInfo;
import com.cybage.model.OrderInfo;
import com.cybage.model.PaginationResult;

@Transactional
public class OrderDaoImpl implements OrderDao
{
    
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private ProductDao productDao;
	
	/*public void setSessionFactory(SessionFactory sessionFactory) 
	{
	    this.sessionFactory = sessionFactory;
	}
    */  
	
	public OrderDaoImpl() {
	}

	
	@Override
	public void saveOrder(CartInfo cartInfo)
	{
	Session session = sessionFactory.getCurrentSession();
	int  orderNum = this.getMaxOrderNum()+1;
	Order order = new Order();
	order.setId(UUID.randomUUID().toString());
	order.setOrderNum(orderNum);	
	order.setOrderDate(new Date());
	order.setAmount(cartInfo.getAmountTotal());
	
	CustomerInfo customerInfo = cartInfo.getCustomerInfo();
	order.setCustomerName(customerInfo.getName());
	order.setCustomerAddress(customerInfo.getAddress());
	order.setCustomerEmail(customerInfo.getEmail());
	order.setCustomerPhone(customerInfo.getPhone());
	
	session.persist(order);
	
	List<CartLineInfo> lines = cartInfo.getCartLines();
		
    for(CartLineInfo line: lines)
    {
    OrderDetail detail= new OrderDetail();	
    detail.setId(UUID.randomUUID().toString());
    detail.setOrder(order);
    detail.setAmount(line.getAmount());
    detail.setPrice((line.getProductInfo()).getPrice());
    detail.setQuantity(line.getQuantity());
    
    String code=  (line.getProductInfo()).getCode();
    Product product = this.productDao.findProduct(code);
    detail.setProduct(product);
    
    session.persist(detail);
    }
     cartInfo.setOrderNum(orderNum);
	}

	
	private int getMaxOrderNum()
	{
	 String sql= "Select max(O.orderNum) from"+Order.class.getName()+" o ";
	 Session session = sessionFactory.getCurrentSession();
	 Query query= session.createQuery(sql);
	 Integer value= (Integer)query.uniqueResult();
	 if(value==null)
	 {
		 return 0;
	 }
    	return value;
	}

	@Override
	public PaginationResult<OrderInfo> listOrderInfo(int page, int maxResult, int maxNavigationPage) 
	{
		String sql= "Select new " + OrderInfo.class.getName()//
                + "(ord.id, ord.orderDate, ord.orderNum, ord.amount, "
                + " ord.customerName, ord.customerAddress, ord.customerEmail, ord.customerPhone) " + " from "
                + Order.class.getName() + " ord "//
                + " order by ord.orderNum desc";
		Session session= this.sessionFactory.getCurrentSession();
		
		Query query=session.createQuery(sql);
		
		return new PaginationResult<OrderInfo>(query, page, maxResult, maxNavigationPage);
	}

	@Override
	public OrderInfo getOrderInfo(String orderId) 
	{  
		Order order = this.findOrder(orderId);
		if(order== null)
		{
			return null;
		}
		return new OrderInfo(order.getId(),order.getOrderDate(),
				order.getOrderNum(), order.getAmount(), order.getCustomerName(), order.getCustomerAddress(), order.getCustomerEmail(), order.getCustomerPhone());
	}

	private Order findOrder(String orderId)
	{
	 Session session= sessionFactory.getCurrentSession();
	 Criteria crit = session.createCriteria(Order.class);
     crit.add(Restrictions.eq("id", orderId));
     return (Order) crit.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OrderDetailInfo> listOrderDetailInfos(String orderId) 
	{ 
	  String sql="Select new" + OrderDetailInfo.class.getName()+
			  "(d.id , d.product.code, d.product.name , d.quantity , d.price , d.amount)" 
			  + "from" + OrderDetail.class.getName()+"d" 
			  + "where d.order.id =: orderId ";
	  Session session = this.sessionFactory.getCurrentSession();
	  
	  Query query= session.createQuery(sql);
	  query.setParameter("oderId", orderId);
	  return query.list();
	}
	

}
