package com.services;
import java.util.List;

import com.entities.Commande;

public interface CommandeService {
	public List<Commande> findAll();
	public Commande find(Long codeArt);
	public void save(Commande commande);
	public void update(Commande commande);
	public void delete(Commande commande);
}
