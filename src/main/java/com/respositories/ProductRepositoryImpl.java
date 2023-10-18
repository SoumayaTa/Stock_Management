package com.respositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;
import com.entities.Product;
@Transactional
public class ProductRepositoryImpl implements ProductRepository {
	
	@PersistenceContext
	private EntityManager entityManager ;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Product> findAll() {
		try {
			return entityManager.createQuery("From Product").getResultList();
		}catch (Exception e) {
			return null;
		}
	}

	@Override
	public Product find(Long codeArt) {
		try {
			return (Product) entityManager.createQuery("FROM Product WHERE codeArt = :codeArt").setParameter("codeArt", Long.valueOf(codeArt)).getSingleResult();
		}catch (Exception e) {
			return null;
		}
	}

	@Override
	public void save(Product product) {
        entityManager.persist(product);
    }
		
	

	@Override
	public void update(Product product) {
		entityManager.merge(product);
		
	}

	@Override
	public void delete(Product product) {
		entityManager.remove(entityManager.merge(product));
		
	}
	
	

}
