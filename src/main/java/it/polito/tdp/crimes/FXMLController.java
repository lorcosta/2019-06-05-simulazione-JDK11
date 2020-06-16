/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.model.Model;
import it.polito.tdp.crimes.model.Vicino;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxAnno"
    private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxMese"
    private ComboBox<Integer> boxMese; // Value injected by FXMLLoader

    @FXML // fx:id="boxGiorno"
    private ComboBox<Integer> boxGiorno; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaReteCittadina"
    private Button btnCreaReteCittadina; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaReteCittadina(ActionEvent event) {
    	this.txtResult.clear();
    	Integer anno=this.boxAnno.getValue();
    	if(anno==null) {
    		this.txtResult.appendText("ATTENZIONE! Nessun anno selezionato.");
    	}
    	this.model.creaGrafo(anno);
    	this.txtResult.appendText("Grafo creato!\n");
    	
    	this.txtResult.appendText("VICINI DEI DISTRETTI:\n");
    	for(Integer d: this.model.getVertici()) {
    		List<Vicino> vicini=this.model.getVicini(d);
    		this.txtResult.appendText("DISTRETTO "+d+":\n");
    		for(Vicino v:vicini) {
    			this.txtResult.appendText("-"+v.getVicino()+" "+v.getDistanza()+"\n");
    		}
    	}
    }

    @FXML
    void doSimula(ActionEvent event) {
    	this.txtResult.clear();
    	Integer anno=this.boxAnno.getValue();
    	if(anno==null) {
    		this.txtResult.appendText("ATTENZIONE! Nessun anno selezionato.");
    	}
    	Integer mese=this.boxMese.getValue();
    	if(mese==null) {
    		this.txtResult.appendText("ATTENZIONE! Nessun mese selezionato.");
    	}
    	Integer giorno=this.boxGiorno.getValue();
    	if(giorno==null) {
    		this.txtResult.appendText("ATTENZIONE! Nessun giorno selezionato.");
    	}
    	String Nstring=this.txtN.getText();
    	Integer N=null;
    	try {
    		N=Integer.parseInt(Nstring);
    	}catch (NumberFormatException e){
    		e.printStackTrace();
    		this.txtResult.appendText("Formato N non corretto!");
    		throw new NumberFormatException();
    	}
    	try {
    		LocalDate.of(anno, mese, giorno);
    	}catch (DateTimeException e) {
    		e.printStackTrace();
    		this.txtResult.appendText("Data inserita non corretta.");
    	}
    	this.txtResult.appendText("Simulo con "+N+" agenti.");
    	this.txtResult.appendText("\nCRIMINI MAL GESTITI: "+this.model.simula(anno, mese, giorno, N));
    	
    	
    }
    
    void loadData() {
    	this.boxAnno.getItems().addAll(model.getAnni());
    	this.boxGiorno.getItems().addAll(model.getGiorni());
    	this.boxMese.getItems().addAll(model.getMesi());
    }
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGiorno != null : "fx:id=\"boxGiorno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaReteCittadina != null : "fx:id=\"btnCreaReteCittadina\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	loadData();
    }
}
