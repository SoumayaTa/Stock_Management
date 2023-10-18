package com.services;

import java.util.List;

import com.entities.Product;

public interface ProductService {
	public List<Product> findAll();
	public Product find(Long codeArt);
	public void save(Product product);
	public void update(Product product);
	public void delete(Product product);

}
