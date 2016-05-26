package it.polito.tdp.rivers;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.rivers.model.Core;
import it.polito.tdp.rivers.model.River;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class RiversController {

	Core core;

	public void setCore(Core core) {
		this.core = core;
		boxRiver.getItems().addAll(core.getAllRivers());
		core.getAllFlows();
	}

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private ComboBox<River> boxRiver;

	@FXML
	private TextField txtEndDate;

	@FXML
	private TextField txtNumMeasurements;

	@FXML
	private TextArea txtResult;

	@FXML
	private TextField txtStartDate;

	@FXML
	private TextField txtFMed;

	@FXML
	private Button btnSimula;

	@FXML
	private TextField txtK;

	@FXML
	void doDaiInfoFiume(ActionEvent event) {
		txtResult.clear();
		River river = boxRiver.getValue();
		txtStartDate.setText(core.getFirstFlowOfRiver(river));
		txtEndDate.setText(core.getLastFlowOfRiver(river));
		txtNumMeasurements.setText(core.getNumberOfMeasurementsOfRiver(river));
		txtFMed.setText("" + (float) (core.getFMedOfRiver(river)) + "m^3/s.");
	}

	@FXML
	void doSimula(ActionEvent event) {
		txtResult.clear();
		River river = boxRiver.getValue();
		if (river == null)
			txtResult.setText("Selezionare un fiume.");
		else {
			try {
				int k = Integer.parseInt(txtK.getText());
				if (k > 0) {
					String textOutPut = core.simula(river, k);
					txtResult.setText(textOutPut);
				} else
					txtResult.setText("Inserire un numero maggiore di 0.");
			} catch (NumberFormatException nfe) {
				txtResult.setText("Inserire un numero intero.");
			}
		}
	}

	@FXML
	void initialize() {
		assert boxRiver != null : "fx:id=\"boxRiver\" was not injected: check your FXML file 'Rivers.fxml'.";
		assert txtEndDate != null : "fx:id=\"txtEndDate\" was not injected: check your FXML file 'Rivers.fxml'.";
		assert txtNumMeasurements != null : "fx:id=\"txtNumMeasurements\" was not injected: check your FXML file 'Rivers.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Rivers.fxml'.";
		assert txtStartDate != null : "fx:id=\"txtStartDate\" was not injected: check your FXML file 'Rivers.fxml'.";
		assert txtFMed != null : "fx:id=\"txtFMed\" was not injected: check your FXML file 'Rivers.fxml'.";
		assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Rivers.fxml'.";
		assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Rivers.fxml'.";
	}
}
