package com.config;

/**
*La classe AppConfig est une classe de configuration utilisée dans le cadre d'une application Spring.
*Elle contient des annotations et des méthodes pour configurer différents composants de l'application.
*/

import java.util.Properties;

import javax.persistence.EntityManagerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.respositories.CommandeRepository;
import com.respositories.CommandeRepositoryImpl;
import com.respositories.ProductRepository;
import com.respositories.ProductRepositoryImpl;
import com.services.CommandeService;
import com.services.CommandeServiceImpl;
import com.services.ProductService;
import com.services.ProductServiceImpl;
@Configuration
@ComponentScan("com")
@EnableTransactionManagement
public class AppConfig {
	   // Configuration de la fabrique d'entités
	@Bean(name = "entityManager")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
		entityManager.setDataSource(dataSource());
		entityManager.setPackagesToScan(new String[] {"com"});
		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter() ;
		entityManager.setJpaVendorAdapter(vendorAdapter);
		entityManager.setJpaProperties(additionalProperties());
		return entityManager;
			
	}
	
	  // Configuration de la source de données
	@Bean
	public DriverManagerDataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/g_stock?serverTimezone=Europe/Paris");
		dataSource.setUsername("root");
		return dataSource;
	}
	public Properties additionalProperties() {
		Properties properties = new Properties();
		properties.setProperty("hibernate.dialect","org.hibernate.dialect.MySQL5Dialect");
		return properties;
		
	}
	
    // Configuration du gestionnaire de transactions
	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);
		return transactionManager;
		
	}
	// Configuration du processeur de traduction des exceptions liées à la persistance
	@Bean
	public PersistenceExceptionTranslationPostProcessor exeptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}
	 
	// Configuration du référentiel des produits
	@Bean
	public ProductRepository productRepository() {
		return new ProductRepositoryImpl();
	}
	 // Configuration du service des produits
	@Bean
	public ProductService productService() {
		return new ProductServiceImpl(productRepository());
	}
	// Configuration du référentiel des commandes
	@Bean
	public CommandeRepository commandeRepository() {
		return new CommandeRepositoryImpl();
	}
	// Configuration du service des commandes
	@Bean
	public CommandeService commandeService() {
		return new CommandeServiceImpl(commandeRepository());
	}

	

}
