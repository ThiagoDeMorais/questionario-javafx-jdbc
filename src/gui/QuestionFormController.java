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
	private TextField txtIdA1;

	@FXML
	private TextField txtIdA2;

	@FXML
	private TextField txtIdA3;

	@FXML
	private TextField txtIdA4;

	@FXML
	private TextField txtIdA5;

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
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}

		try {
			entity = getFormData();
			List<Alternative> list = getAlternatives(entity);
			service.saveOrUpdate(entity);
			list.forEach(alternative -> {
				alternativeService.saveOrUpdate(alternative);
			});
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		} catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		} catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		}

	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	private Question getFormData() {
		Question obj = new Question();

		ValidationException exception = new ValidationException("Validation Error");

		obj.setId(Utils.tryParseToInt(txtId.getText()));

		if (txtDescription.getText() == null || txtDescription.getText().trim().equals("")) {
			exception.addErrors("pergunta", "Há um camp campo vazio");
		}
		obj.setDescription(txtDescription.getText());

		if (exception.getErrors().size() > 0) {
			throw exception;
		}

		return obj;
	}

	private List<Alternative> getAlternatives(Question question) {
		Alternative alternativeObj1 = new Alternative();
		Alternative alternativeObj2 = new Alternative();
		Alternative alternativeObj3 = new Alternative();
		Alternative alternativeObj4 = new Alternative();
		Alternative alternativeObj5 = new Alternative();

		ValidationException exception = new ValidationException("Validation Error");

		alternativeObj1.setId(Utils.tryParseToInt(txtIdA1.getText()));// id group
		if (txtDescriptionA1 == null || txtDescriptionA1.getText().trim().equals("")) {
			exception.addErrors("alternativa", "Há um camp campo vazio");
		}
		alternativeObj1.setQuestion(question);
		alternativeObj1.setDescription(txtDescriptionA1.getText());
		alternativeObj1.setIsCorrect(rButtonA1.isSelected() == true ? "V" : "F");

		alternativeObj2.setId(Utils.tryParseToInt(txtIdA2.getText()));// id group
		if (txtDescriptionA2 == null || txtDescriptionA2.getText().trim().equals("")) {
			exception.addErrors("alternativa", "Há um camp campo vazio");
		}
		alternativeObj2.setQuestion(question);
		alternativeObj2.setDescription(txtDescriptionA2.getText());
		alternativeObj2.setIsCorrect(rButtonA2.isSelected() == true ? "V" : "F");

		alternativeObj3.setId(Utils.tryParseToInt(txtIdA3.getText()));// id group
		if (txtDescriptionA3 == null || txtDescriptionA3.getText().trim().equals("")) {
			exception.addErrors("alternativa", "Há um camp campo vazio");
		}
		alternativeObj3.setQuestion(question);
		alternativeObj3.setDescription(txtDescriptionA3.getText());
		alternativeObj3.setIsCorrect(rButtonA3.isSelected() == true ? "V" : "F");


		alternativeObj4.setId(Utils.tryParseToInt(txtIdA4.getText()));// id group
		if (txtDescriptionA4 == null || txtDescriptionA4.getText().trim().equals("")) {
			exception.addErrors("alternativa", "Há um camp campo vazio");
		}
		alternativeObj4.setQuestion(question);
		alternativeObj4.setDescription(txtDescriptionA4.getText());
		alternativeObj4.setIsCorrect(rButtonA4.isSelected() == true ? "V" : "F");

		alternativeObj5.setId(Utils.tryParseToInt(txtIdA5.getText()));// id group
		if (txtDescriptionA5 == null || txtDescriptionA5.getText().trim().equals("")) {
			exception.addErrors("alternativa", "Há um camp campo vazio");
		}
		alternativeObj5.setQuestion(question);
		alternativeObj5.setDescription(txtDescriptionA5.getText());
		alternativeObj5.setIsCorrect(rButtonA5.isSelected() == true ? "V" : "F");

		List<Alternative> list = new ArrayList<>();
		list.add(alternativeObj1);
		list.add(alternativeObj2);
		list.add(alternativeObj3);
		list.add(alternativeObj4);
		list.add(alternativeObj5);
		

		if (exception.getErrors().size() > 0) {
			throw exception;
		}

		return list;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
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
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId() == null ? " " : entity.getId()));
		txtDescription.setText(entity.getDescription());
	}

	public void loadAssociatedObjects() {

		TextArea[] txtAlternativeDescriptions = { txtDescriptionA1, txtDescriptionA2, txtDescriptionA3,
				txtDescriptionA4, txtDescriptionA5 };

		RadioButton[] radioAlternativeButtons = { rButtonA1, rButtonA2, rButtonA3, rButtonA4, rButtonA5 };

		TextField[] txAlternativestId = {txtIdA1, txtIdA2, txtIdA3, txtIdA4, txtIdA5};
		
		if (alternativeService == null) {
			throw new IllegalStateException("Service was null");
		}

		List<Alternative> alternatives = alternativeService.findByQuestion(entity.getId());

		for (int i = 0; i < alternatives.size(); i++) {
			txtAlternativeDescriptions[i].setText(alternatives.get(i).getDescription());
			radioAlternativeButtons[i].setSelected((alternatives.get(i).getIsCorrect()).equals("V")  ? true : false);	
			txAlternativestId[i].setText(String.valueOf(alternatives.get(i).getId() == null ? " ": alternatives.get(i).getId() ));
		}

	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		if (fields.contains("pergunta")) {
			labelErrorName.setText(errors.get("pergunta"));
		}
	}

}
