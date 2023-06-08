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
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.entities.Alternative;
import model.entities.Question;
import model.exceptions.ValidationException;
import model.servicies.AlternativeService;
import model.servicies.QuestionService;

public class QuestionFormController implements Initializable {
	
	private Question entity;
	
	private QuestionService service;
	
	private AlternativeService alternativeService;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextArea txtDescription;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private TextArea txtDescriptionA1;
	
	@FXML
	private TextArea txtDescriptionA2;
	
	@FXML
	private TextArea txtDescriptionA3;
	
	@FXML
	private TextArea txtDescriptionA4;
	
	@FXML
	private TextArea txtDescriptionA5;
	
	@FXML
	private RadioButton rButtonA1;
	
	@FXML
	private RadioButton rButtonA2;
	
	@FXML
	private RadioButton rButtonA3;
	
	@FXML
	private RadioButton rButtonA4;
	
	@FXML
	private RadioButton rButtonA5;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	public void setQuestion(Question entity) {
		this.entity = entity;
	}
	
	public void setServices(QuestionService service, AlternativeService alternativeService) {
		this.service = service;
		this.alternativeService = alternativeService;
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

	private Question getFormData() {
		Question obj = new Question();
		
		ValidationException exception = new ValidationException("Validation Error");
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		if(txtDescription.getText() == null || txtDescription.getText().trim().equals("")) {
			exception.addErrors("pergunta", "O campo pergunta está campo vazio");
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
		initializaNodes();		
	}
	
	private void initializaNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextAreaMaxLength(txtDescription, 5000);

	}
	
	public void updateFormData() {
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId() == null?" ":entity.getId()));
		txtDescription.setText(entity.getDescription());
	}
	
	public void loadAssociatedObjects() {
		if (alternativeService == null) {
			throw new IllegalStateException("QuestionService was null");
		}
		
		TextArea[] txtAlternativeDescriptions = {txtDescriptionA1,
				txtDescriptionA2,
				txtDescriptionA3,
				txtDescriptionA4,
				txtDescriptionA5};
		
		RadioButton[] radioAlternativeButtons = {rButtonA1,
				rButtonA2,
				rButtonA3,
				rButtonA4,
				rButtonA5};
		
		List<Alternative> alternatives = alternativeService.findByQuestion(entity);

		for(int i = 0; i< alternatives.size(); i++) {
			txtAlternativeDescriptions[i].setText(alternatives.get(i).getDescription());
			radioAlternativeButtons[i].setSelected((alternatives.get(i).getIsCorrect()) == "V"? true: false);
			System.out.print(radioAlternativeButtons[i].isSelected());
		}
		
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if(fields.contains("pergunta")) {
			labelErrorName.setText(errors.get("pergunta"));
		}
	}

}
