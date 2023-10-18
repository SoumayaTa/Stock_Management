package com.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "articles_approvisionnement")
public class Commande {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codeArt;
	
	private Long qteCommande;
	
	 @Temporal(TemporalType.TIMESTAMP)
	 @Column(name = "datePrevueLivraison")
	private Date datePrevueLivraison;
    private String etat;
	
	public String getEtat() {
		return etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}

	public Long getCodeArt() {
		return codeArt;
	}
	
	public void setCodeArt(Long codeArt) {
		this.codeArt = codeArt;
	}
	
	public Long getQteCommande() {
		return qteCommande;
	}
	
	public void setQteCommande(Long qteCommande) {
		this.qteCommande = qteCommande;
	}
	
	public Date getDatePrevueLivraison() {
		return datePrevueLivraison;
	}
	
	public void setDatePrevueLivraison(Date datePrevueLivraison) {
		this.datePrevueLivraison = datePrevueLivraison;
	}
}
