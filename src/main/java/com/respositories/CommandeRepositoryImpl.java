package com.respositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

import com.entities.Commande;

@Transactional
public class CommandeRepositoryImpl implements CommandeRepository  {
	
    @PersistenceContext
    private EntityManager entityManager;
   
    @SuppressWarnings("unchecked")
    @Override
    public List<Commande> findAll() {
        try {
            return entityManager.createQuery("From Commande").getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    @Override
    public Commande find(Long codeArt) {
        try {
            return (Commande) entityManager.createQuery("FROM Commande WHERE codeArt = :codeArt")
                    .setParameter("codeArt", Long.valueOf(codeArt)).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    @Override
    public void update(Commande commande) {
        entityManager.merge(commande);
    }
    @Override
    public void delete(Commande commande) {
        entityManager.remove(entityManager.merge(commande));
    }
	@Override
	public void save(Commande commande) {
		entityManager.persist(commande);
	}
}
