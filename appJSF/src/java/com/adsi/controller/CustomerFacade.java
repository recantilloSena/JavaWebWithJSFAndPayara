/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adsi.controller;

import com.adsi.modelo.Customer;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Ricardo
 */
@Stateless
public class CustomerFacade extends AbstractFacade<Customer> {

    @PersistenceContext(unitName = "appJSFPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CustomerFacade() {
        super(Customer.class);
    }
    
    
    
    
    
    @SuppressWarnings("unchecked")
	public List<Customer> getCustomerByRate(Integer p_rate) {
		Query q = em.createNamedQuery("Customer.findByRate");
                q.setParameter("p_rate", p_rate);

		return q.getResultList();
	}
    
    
    
    
}
