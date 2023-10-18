package com.managedbeans;

import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.OutputStream;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.entities.Product;
import com.services.CommandeService;
import com.services.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ManagedBean(name = "productManagedBean")
@SessionScoped
public class ProductManagedBean implements Serializable {
	
    private static final long serialVersionUID = 1L;

    @Autowired
    public ProductService productService;
    @Autowired
    public CommandeService commandeService;

    private List<Product> products;
    private Product product;
    
    // Méthode appelée lors du chargement de la page. 
    //Initialise l'objet "product" avec une nouvelle instance de "Product" et récupère tous les produits à partir du service "productService" pour les stocker dans la liste "products".
    public void onload() {
        this.product = new Product();
        this.products = productService.findAll();
    }
    //redirige l'utilisateur vers la page "add.xhtml" pour saisir les informations du produit.
    public String add() {
        this.setProduct(new Product());
        return "add?faces-redirect=true";
    }

    // Méthode utilisée pour enregistrer un produit dans la base de données.
	public String save() {
	    try {
	        productService.save(product);
	        FacesContext.getCurrentInstance().addMessage(null,
	                new FacesMessage(FacesMessage.SEVERITY_INFO, "Le produit a été ajouté avec succès", null));
	    } catch (Exception e) {
	        FacesContext.getCurrentInstance().addMessage(null,
	                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Une erreur s'est produite lors de l'ajout du produit", null));
	    }
	    return "stock?faces-redirect=false";
	}


	// Méthode utilisée pour supprimer un produit de la base de données.
    public String delete(Product product) {

        if (commandeService.find(product.getCodeArt()) != null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", "You cannot delete an article that has been ordered"));
            return null;
        } else {
            this.productService.delete(product);
            return "stock?faces-redirect=true";
        }
    }
    // Méthode utilisée pour déterminer si un champ de saisie a une erreur de validation.
    public String errorClass(String fieldId) {
        FacesContext context = FacesContext.getCurrentInstance();
        UIComponent component = UIComponent.getCurrentComponent(context);
        if (component instanceof UIInput) {
            UIInput input = (UIInput) component;
            if (!input.isValid()) {
                return "error-field";
            }
        }
        return "";
    }


   
    // Méthode utilisée pour déterminer si un champ de saisie est vide.
    public String errorClass1(String fieldId) {
        FacesContext context = FacesContext.getCurrentInstance();
        UIComponent component = UIComponent.getCurrentComponent(context);
        if (component instanceof UIInput) {
            UIInput input = (UIInput) component;
            Object value = input.getValue();
            if (value == null || value.toString().isEmpty()) {
                return "error-field";
            }
        }
        return "";
    }

    //redirige l'utilisateur vers la page "edit.xhtml".
    public String edit(Product product) {
        this.product = product;
        return "edit?faces-redirect=true";
    }
    // Méthode utilisée pour mettre à jour les informations d'un produit dans la base de données.
    public String update() {
        if (product.getNomArt() == null || product.getPrixArt() == null || product.getPrixArt() <= 0
                || product.getQteArt() < 0) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", "Please check your inputs "));
            return null; // Return null to stay on the same page
        }

        this.productService.update(this.product);
        return "stock?faces-redirect=true";
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

   
    // Méthode utilisée pour générer un fichier PDF contenant les informations du stock.
    public void generatePDF() {
        Document document = new Document();

        String filePath = "C:\\Users\\hp\\Desktop\\Nouveau dossier\\stock.pdf";

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));

            document.open();

            // Add the logo at the top of the page
            Image logo = Image.getInstance("C:\\Users\\hp\\Desktop\\master\\s2\\JEE\\project\\logo.png");
            logo.scaleToFit(100, 100); // Adjust the size of the logo if necessary
            document.add(logo);

            // Add the document title
            Paragraph title = new Paragraph("Etat du stock", new Font(Font.FontFamily.HELVETICA, 18));
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(Chunk.NEWLINE); // Add spacing between the title and the table

            // Get the list of products
            List<Product> productList = productService.findAll();

            // Create the table for the data
            PdfPTable table = new PdfPTable(4); // 4 columns for different information

            // Set header colors for each column
            PdfPCell headerCell1 = new PdfPCell(new Phrase("Nom"));
            headerCell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            PdfPCell headerCell2 = new PdfPCell(new Phrase("Prix"));
            headerCell2.setBackgroundColor(BaseColor.LIGHT_GRAY);
            PdfPCell headerCell3 = new PdfPCell(new Phrase("Description"));
            headerCell3.setBackgroundColor(BaseColor.LIGHT_GRAY);
            PdfPCell headerCell4 = new PdfPCell(new Phrase("Quantité"));
            headerCell4.setBackgroundColor(BaseColor.LIGHT_GRAY);

            // Add column headers
            table.addCell(headerCell1);
            table.addCell(headerCell2);
            table.addCell(headerCell3);
            table.addCell(headerCell4);

            // Add product data to the table
            for (Product product : productList) {
                table.addCell(product.getNomArt());
                table.addCell(String.valueOf(product.getPrixArt()));
                table.addCell(product.getDescArt());
                table.addCell(String.valueOf(product.getQteArt()));
            }

            // Add the table to the document
            document.add(table);

            document.close();

            System.out.println("Le fichier PDF a été généré avec succès.");

            // Send the PDF file to the browser for download
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
            response.reset();
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"stock.pdf\"");

            try (FileInputStream fileInputStream = new FileInputStream(filePath);
                    OutputStream responseOutputStream = response.getOutputStream()) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    responseOutputStream.write(buffer, 0, bytesRead);
                }
            }

            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
