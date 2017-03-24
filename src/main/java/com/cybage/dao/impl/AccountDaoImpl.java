package com.cybage.dao.impl;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import com.cybage.dao.AccountDao;
import com.cybage.entity.Account;

@Transactional
public class AccountDaoImpl implements AccountDao
{
   @Autowired
	private SessionFactory sessionFactory;
  /* public void setSessionFactory(SessionFactory sessionFactory) 
	{
	    this.sessionFactory = sessionFactory;
	}*/
	
	@Override
	public Account findAccount(String userName) 
	{
		Session session= sessionFactory.getCurrentSession();
		Criteria crit= session.createCriteria(Account.class);
		crit.add(Restrictions.eq("userName", userName));
		return (Account) crit.uniqueResult();
	}

	public AccountDaoImpl() 
	{
	}

}
