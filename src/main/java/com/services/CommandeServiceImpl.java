package com.services;


import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.entities.Commande;
import com.respositories.CommandeRepository;

@Transactional(noRollbackFor = Exception.class)
public class CommandeServiceImpl implements CommandeService{
	private CommandeRepository commandeRepository;
	
	public CommandeServiceImpl(CommandeRepository commandeRepository) {
		super();
		this.commandeRepository = commandeRepository;
	}
	
	@Override
	public List<Commande> findAll() {
		return commandeRepository.findAll();
	}

	@Override
	public Commande find(Long codeArt) {
		return commandeRepository.find(codeArt);
	}

	@Override
	public void save(Commande commande) {
		commandeRepository.save(commande);
		
	}

	@Override
	public void update(Commande commande) {
		commandeRepository.update(commande);
		
	}

	@Override
	public void delete(Commande commande) {
		commandeRepository.delete(commande);
		
	}
}
