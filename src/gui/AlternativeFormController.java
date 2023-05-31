package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
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
import model.entities.Alternative;
import model.exceptions.ValidationException;
import model.servicies.AlternativeService;

public class AlternativeFormController implements Initializable {
	
	private Alternative entity;
	
	private AlternativeService service;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
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
	
	public void setAlternative(Alternative entity) {
		this.entity = entity;
	}
	
	public void setAlternativeService(AlternativeService service) {
		this.service = service;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
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
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		}catch(DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
		catch(ValidationException e) {
			setErrorMessages(e.getErrors());
		}

	}
	
	private void notifyDataChangeListeners() {
		for(DataChangeListener listener: dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	private Alternative getFormData() {
		Alternative obj = new Alternative();
		
		ValidationException exception = new ValidationException("Validation Error");
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		if(txtDescription.getText() == null || txtDescription.getText().trim().equals("")) {
			exception.addErrors("alternativa", "O campo alternativa não pode ser vazio");
		}
		obj.setDescription(txtDescription.getText());
		
		if(exception.getErrors().size() > 0) {
			throw exception;
		}
		
		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		System.out.println("onBtCancelction");
		Utils.currentStage(event).close();
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
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if(fields.contains("alternativa")) {
			labelErrorName.setText(errors.get("alternativa"));
		}
	}

}
