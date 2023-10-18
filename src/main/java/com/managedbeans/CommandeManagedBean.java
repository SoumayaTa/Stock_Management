package com.managedbeans;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Date;
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
import com.entities.Commande;
import com.entities.Product;
import com.services.CommandeService;
import com.services.ProductService;

@Component
@ManagedBean(name = "commandeManagedBean")
@SessionScoped
public class CommandeManagedBean {
    @Autowired
    private CommandeService commandeService;
    @Autowired
    public ProductService productService;

    private List<Commande> commandes;

    private List<SelectItem> productList;
    private Commande commande;

    public String commande() {
        return "commande?faces-redirect=true";
    }
    
    private Date currentDate; 

    public CommandeManagedBean() {
        currentDate = new Date(); 
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    /**
     * Initialise les données nécessaires pour le formulaire de commande.
     * Crée une nouvelle instance de la commande et initialise la liste des produits disponibles.
     */
    public void init() {
        commande = new Commande();
        productList = new ArrayList<>();
        productList.add(new SelectItem("", ""));
        List<Product> listprod = productService.findAll();
        for (Product o : listprod) {
            productList.add(new SelectItem(o.getCodeArt(), o.getNomArt()));
        }
    }

    /**
     * Redirige vers la page d'ajout de commande.
     * @return L'URL de la page d'ajout de commande avec une redirection de navigation.
     */
    public String addcommande() {
        return "addcommande?faces-redirect=true";
    }
    
    /**
     * Affiche les commandes en récupérant toutes les commandes à partir du service de commande.
     * Initialise également une nouvelle instance de commande.
     */
    public void afficher() {
        this.commande = new Commande();
        this.commandes = commandeService.findAll();
    }

    public List<SelectItem> getProductList() {
        return productList;
    }

    /**
     * Définit la liste des produits sous forme d'objets SelectItem.
     * @param productList La liste des produits sous forme d'objets SelectItem.
     */
    public void setProductList(List<SelectItem> productList) {
        this.productList = productList;
    }
    /**
     * Prépare la page d'édition de commande en définissant la commande à éditer.
     * @param commande La commande à éditer.
     * @return L'URL de la page d'édition de commande avec une redirection de navigation.
     */
    public String edit(Commande commande) {
        this.commande = commande;
        return "editcommande?faces-redirect=true";
    }
    //Met à jour la commande
    public String update() {
        if (commande.getQteCommande() != null && commande.getQteCommande().longValue() > 0 && commande.getDatePrevueLivraison() != null) {
            this.commandeService.update(this.commande);
            return "commande?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", "Incorrect innformation"));
            return "";
        }
    }
    //Supprime la commande
    public String delete(Commande commande) {
        this.commandeService.delete(commande);
        return "commande?faces-redirect=true";
    }
    public List<Commande> getCommandes() {
        return commandes;
    }

    public void setCommandes(List<Commande> commandes) {
        this.commandes = commandes;
    }

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    public CommandeService getCommandeService() {
        return commandeService;
    }

    public void setCommandeService(CommandeService commandeService) {
        this.commandeService = commandeService;
    }
    
    // Méthode appelée lorsque l'utilisateur souhaite enregistrer une commande
    public void saveCommande() {
    	this.commandeService.save(this.commande);
    }
   // Méthode pour générer un fichier PDF contenant les données des commandes
    public void generatePDF() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
        Document document = new Document();
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);
            // Add the logo at the top of the page
            Image logo = Image.getInstance("C:\\Users\\hp\\Desktop\\master\\s2\\JEE\\project\\logo.png");
            logo.scaleToFit(100, 100); // Adjust the size of the logo if necessary
            document.open();
            document.add(logo);
            // Add the document title
            Paragraph title = new Paragraph("Etat du stock", new Font(Font.FontFamily.HELVETICA, 18));
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE); // Add spacing between the title and the table
            // Get the list of commandes
            List<Commande> commandeList = commandeService.findAll();
            // Create the table for the data
            PdfPTable table = new PdfPTable(4); // 3 columns for Id, Quantité, and Date prévue de livraison
            // Set header colors for each column
            PdfPCell headerCell1 = new PdfPCell(new Phrase("Id"));
            headerCell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            PdfPCell headerCell2 = new PdfPCell(new Phrase("Quantité"));
            headerCell2.setBackgroundColor(BaseColor.LIGHT_GRAY);
            PdfPCell headerCell3 = new PdfPCell(new Phrase("Date prévue de livraison"));
            headerCell3.setBackgroundColor(BaseColor.LIGHT_GRAY);
            PdfPCell headerCell4 = new PdfPCell(new Phrase("Etat commande"));
            headerCell4.setBackgroundColor(BaseColor.LIGHT_GRAY);
            // Add column headers
            table.addCell(headerCell1);
            table.addCell(headerCell2);
            table.addCell(headerCell3);
            table.addCell(headerCell4);
            // Add commandes data to the table
            for (Commande commande : commandeList) {
                table.addCell(String.valueOf(commande.getCodeArt()));
                table.addCell(String.valueOf(commande.getQteCommande()));
                table.addCell(commande.getDatePrevueLivraison().toString());
                table.addCell(commande.getEtat());
            }
            // Add the table to the document
            document.add(table);
            document.close();
            byte[] pdfBytes = baos.toByteArray();
            // Send the PDF file to the browser for download
            response.reset();
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"stock.pdf\"");
            try (OutputStream responseOutputStream = response.getOutputStream()) {
                responseOutputStream.write(pdfBytes);
            }
            facesContext.responseComplete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private boolean commandeRecue;
    // Méthode pour vérifier si une commande a été reçue ou non
    public boolean isCommandeRecue() {
        return commandeRecue;
    }

    public void setCommandeRecue(boolean commandeRecue) {
        this.commandeRecue = commandeRecue;
    }
    // Méthode pour vérifier l'état d'une commande et retourner son libellé correspondant
    public String getCommandeEtat(Commande commande) {
        Date datePrevue = commande.getDatePrevueLivraison();
        Date dateActuelle = new Date();
        if (datePrevue.compareTo(dateActuelle) > 0) {
            setCommandeRecue(false); // Définir commandeRecue sur false
            return "En attente de livraison";
        } else {
            // Mettre à jour l'état de la commande en "Reçu"
            commande.setEtat("Reçu");
            setCommandeRecue(true); // Définir commandeRecue sur true
            
            // Afficher une notification à l'utilisateur
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Notification", "Votre commande a été reçue."));
            
            return "Reçu";
        }
    }
    // Méthode pour vérifier les dates de toutes les commandes
    public void verifierDateCommande() {
        // Récupérer toutes les commandes
        List<Commande> commandes = commandeService.findAll();
        // Parcourir les commandes
        for (Commande commande : commandes) {
            Date datePrevue = commande.getDatePrevueLivraison();
            Date dateActuelle = new Date();
            // Vérifier si la date prévue est dans le futur (état "En attente de livraison")
            if (datePrevue.compareTo(dateActuelle) > 0) {
                // Changer l'état de la commande à "En attente de livraison" si ce n'est pas déjà le cas
                if (!"En attente de livraison".equals(commande.getEtat())) {
                    commande.setEtat("En attente de livraison");
                    commandeService.update(commande);
                }
            } else {
                // Changer l'état de la commande à "Reçu" si ce n'est pas déjà le cas
                if (!"Reçu".equals(commande.getEtat())) {
                    Product produit = productService.find(commande.getCodeArt());
                    Long quantiteCommande = commande.getQteCommande();
                    Long stockActuel = produit.getQteArt();
                    produit.setQteArt(stockActuel + quantiteCommande);
                    // Changer l'état de la commande à "Reçu"
                    commande.setEtat("Reçu");
                    // Enregistrer les modifications dans la base de données
                    productService.update(produit);
                    commandeService.update(commande);
                    setCommandeRecue(true); // Définir commandeRecue sur true
                }
            }
        }
    }
    // Méthode pour récupérer toutes les commandes reçues
    public List<Commande> getCommandesRecues() {
        List<Commande> commandesRecues = new ArrayList<>();
        // Parcourir les commandes
        for (Commande commande : commandes) {
            if ("Reçu".equals(commande.getEtat())) {
                commandesRecues.add(commande);
            }
        }
        return commandesRecues;
    }
    
    /**
     * Renvoie une classe CSS d'erreur pour le champ de saisie spécifié si celui-ci n'est pas valide.
     * @param fieldId L'ID du champ de saisie
     * @return La classe CSS "error-field" si le champ a une erreur de validation, sinon une chaîne vide.
     */
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

    /**
     * Renvoie une classe CSS d'erreur pour le champ de saisie spécifié si celui-ci est vide.
     * @param fieldId L'ID du champ de saisie
     * @return La classe CSS "error-field" si le champ est vide, sinon une chaîne vide.
     */
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


}
