package com.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "articles_stock")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codeArt;
	private Long qteArt;
	private String nomArt;
	private String descArt;
	private Long prixArt;
	
	public Long getCodeArt() {
		return codeArt;
	}
	
	public void setCodeArt(Long codeArt) {
		this.codeArt = codeArt;
	}
	
	public Long getQteArt() {
		return qteArt;
	}
	
	public void setQteArt(Long qteArt) {
		this.qteArt = qteArt;
	}
	
	public String getNomArt() {
		return nomArt;
	}
	
	public void setNomArt(String nomArt) {
		this.nomArt = nomArt;
	}
	
	public String getDescArt() {
		return descArt;
	}
	
	public void setDescArt(String descArt) {
		this.descArt = descArt;
	}
	
	public Long getPrixArt() {
		return prixArt;
	}
	
	public void setPrixArt(Long prixArt) {
		this.prixArt = prixArt;
	}
}
