package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class QuestionFormController implements Initializable {
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtDescription;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	@FXML
	public void onBtSaveAction() {
		System.out.println("onBtSaveAction");
	}
	
	@FXML
	public void onBtCancelction() {
		System.out.println("onBtCancelction");
	}


	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub
		
	}
	
	private void initializaNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtDescription, 5000);

	}

}
