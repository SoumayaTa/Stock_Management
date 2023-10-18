package com.managedbeans;

import java.io.IOException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.springframework.stereotype.Component;

@Component
@ManagedBean(name = "loginManagedBean")
@SessionScoped
public class LoginManagedBean {
	private String nom;
	private String password;

	private boolean loggedIn;
	//Renvoie le nom d'utilisateur.
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	//Renvoie le mot de passe.
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
    
	 /**
     * Gère l'action de connexion.
     * Vérifie les informations de connexion et redirige l'utilisateur en fonction du résultat.
     */
	public String login() {
	    if (nom == null || nom.isEmpty()) {
	        FacesContext.getCurrentInstance().addMessage("loginForm:nom",
	                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Veuillez saisir le nom d'utilisateur", null));
	        return null;
	    } else if (password == null || password.isEmpty()) {
	        FacesContext.getCurrentInstance().addMessage("loginForm:password",
	                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Veuillez saisir le mot de passe", null));
	        return null;
	    } else if (nom.equals("wiam") && password.equals("ASFAR1958")) {
	        loggedIn = true;
	        return "stock?faces-redirect=true";
	    } else if (nom.equals("soumaya") && password.equals("1234")) {
	        loggedIn = true;
	        return "stock?faces-redirect=true";
	    } else {
	        FacesContext.getCurrentInstance().addMessage("loginForm:password",
	                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nom d'utilisateur ou mot de passe incorrect", null));
	        return null;
	    }
	}

	//Vérifie si l'utilisateur est connecté.
	public boolean isLoggedIn() {
		return loggedIn;
	}
	//Définit l'état de connexion de l'utilisateur.
	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}
	//Vérifie si l'utilisateur est connecté.
	  public void checkLoggedIn() {
	        if (!loggedIn) {
	            try {
	                FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
	            } catch (IOException e) {
	            }
	        }
	  }
      public void checkLoginStatus() {
	        if (loggedIn) {
	            try {
	                FacesContext.getCurrentInstance().getExternalContext().redirect("stock.xhtml");
	            } catch (IOException e) {
	              // Handle the exception
	            }
	        }
	  }
	   //Gère l'action de déconnexion.
	  public String logout() {
	    	 loggedIn = false;
	    	 FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
	    	 return "login?faces-redirect=true";
	   }
	  
}
