/**
 * Sample Skeleton for 'Meteo.fxml' Controller Class
 */

package it.polito.tdp.meteo;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.meteo.bean.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class MeteoController {
	Model model;
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtTemperatura"
    private TextField txtTemperatura; // Value injected by FXMLLoader

    @FXML // fx:id="btnCerca"
    private Button btnCerca; // Value injected by FXMLLoader

    @FXML // fx:id="btnCalcola"
    private Button btnCalcola; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcola(ActionEvent event) {
    	txtResult.clear();
    	String tMediaS = txtTemperatura.getText();
    	//controlli
    	if(tMediaS.length()==0){
    		txtResult.appendText("ERRORE: devi inserire una temperatura\n");
    		return;
    	}
    	int t;
    	try{
    		t = Integer.parseInt(tMediaS);
    	}catch(NumberFormatException nfe){
    		txtResult.appendText("ERRORE: la temperatura deve essere in formato numerico\n");
    		return;
    	}
    	model.creaGrafo();
    	txtResult.appendText("Temperature possibili nella distanza di due giorni:\n");
    	for(Integer i : model.analizza(t)){
    		txtResult.appendText(i+"\n");
    	}
    	


    }

    @FXML
    void doCerca(ActionEvent event) {
    	txtResult.clear();
    	String tMediaS = txtTemperatura.getText();
    	//controlli
    	if(tMediaS.length()==0){
    		txtResult.appendText("ERRORE: devi inserire una temperatura\n");
    		return;
    	}
    	int t;
    	try{
    		t = Integer.parseInt(tMediaS);
    	}catch(NumberFormatException nfe){
    		txtResult.appendText("ERRORE: la temperatura deve essere in formato numerico\n");
    		return;
    	}
    	String res = model.getDateETMedie(t);
    	if(res.length()==0){
    		txtResult.appendText("In nessuna data a torino si e' registrata questa temperatura media.");
    	}
    	txtResult.appendText(res);

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtTemperatura != null : "fx:id=\"txtTemperatura\" was not injected: check your FXML file 'Meteo.fxml'.";
        assert btnCerca != null : "fx:id=\"btnCerca\" was not injected: check your FXML file 'Meteo.fxml'.";
        assert btnCalcola != null : "fx:id=\"btnCalcola\" was not injected: check your FXML file 'Meteo.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Meteo.fxml'.";

    }

	public void setModel(Model model) {
		this.model= model;
		
	}
}