package com.managedbeans;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.config.AppConfig;

public class WebAppInitializer implements WebApplicationInitializer {

	
	// Cette méthode est appelée lors du démarrage de l'application web
	   public void onStartup(ServletContext servletContext) throws ServletException {
		// Crée un contexte de l'application basé sur la configuration Java
	        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
	     // Enregistre la classe de configuration de l'application (AppConfig)
	        ctx.register(AppConfig.class);
	     // Définit le contexte du servlet
	        ctx.setServletContext(servletContext);
	     // Ajoute un écouteur pour charger le contexte de l'application lors du démarrage
	        servletContext.addListener(new ContextLoaderListener(ctx));
	     // Ajoute un écouteur pour gérer le contexte de la requête lors des requêtes web
	        servletContext.addListener(new RequestContextListener());
	     // Crée et configure le DispatcherServlet pour traiter les requêtes
	        Dynamic dynamic = servletContext.addServlet("dispatcher", new DispatcherServlet(ctx));
	     // Définit la racine de l'URL pour les requêtes gérées par le DispatcherServlet
	        dynamic.addMapping("/");
	        // Définit la priorité de chargement du DispatcherServlet lors du démarrage
	        dynamic.setLoadOnStartup(1);
	    

}
}
