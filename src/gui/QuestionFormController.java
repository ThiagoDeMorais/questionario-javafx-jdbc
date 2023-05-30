package gui;

import java.net.URL;
import java.util.ResourceBundle;

import db.DbException;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Question;
import model.servicies.QuestionService;

public class QuestionFormController implements Initializable {
	
	private Question entity;
	
	private QuestionService service;
	
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
	
	public void setQuestion(Question entity) {
		this.entity = entity;
	}
	
	public void setQuestionService(QuestionService service) {
		this.service = service;
	}
	
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if(service == null) {
			throw new IllegalStateException("Service was null");
		}
		
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			Utils.curretStage(event).close();
		}catch(DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}

	}
	
	private Question getFormData() {
		Question obj = new Question();
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		obj.setDescription(txtDescription.getText());
		
		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		System.out.println("onBtCancelction");
		Utils.curretStage(event).close();
	}


	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub
		
	}
	
	private void initializaNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtDescription, 5000);

	}
	
	public void updateFormData() {
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId() == null?" ":entity.getId()));
		txtDescription.setText(entity.getDescription());
	}

}
