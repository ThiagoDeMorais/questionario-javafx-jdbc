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
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Alternative;
import model.entities.Question;
import model.exceptions.ValidationException;
import model.servicies.AlternativeService;
import model.servicies.QuestionService;

public class AlternativeFormController implements Initializable {

	private Alternative entity;

	private AlternativeService alternativeService;

	private QuestionService questionService;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtDescription;

	@FXML
	private ComboBox<Question> comboBoxQuestion;
	
	@FXML
	private ComboBox<String> comboBoxIsCorrect;///

	@FXML
	private Label labelErrorDescription;
	
	@FXML
	private Label labelErrorIsCorrect;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	private ObservableList<Question> obsListQuestion;
	
	private ObservableList<String> obsListIsCorrect;///

	public void setAlternative(Alternative entity) {
		this.entity = entity;
	}

	public void setServices(AlternativeService alternativeService, QuestionService questionService) {
		this.alternativeService = alternativeService;
		this.questionService = questionService;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (alternativeService == null) {
			throw new IllegalStateException("Service was null");
		}

		try {
			entity = getFormData();
			System.out.println(entity.getId());
			alternativeService.saveOrUpdate(entity);
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

	private Alternative getFormData() {
		Alternative obj = new Alternative();

		ValidationException exception = new ValidationException("Validation Error");

		obj.setId(Utils.tryParseToInt(txtId.getText()));

		if (txtDescription.getText() == null || txtDescription.getText().trim().equals("")) {
			exception.addErrors("alternativa", "O campo alternativa não pode ser vazio");
		}
		obj.setDescription(txtDescription.getText());

		if (exception.getErrors().size() > 0) {
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
		Constraints.setTextFieldMaxLength(txtDescription, 5000);
		initializeComboBoxQuestion();
		initializeComboBoxIsCorrect();///

	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId() == null ? " " : entity.getId()));
		txtDescription.setText(entity.getDescription());
		if (entity.getQuestion() == null) {
			comboBoxQuestion.getSelectionModel().selectFirst();
		} else {
			comboBoxQuestion.setValue(entity.getQuestion());
		}
		
		if (entity.getIsCorrect() == null) {///
			comboBoxIsCorrect.getSelectionModel().selectFirst();
		} else {
			comboBoxIsCorrect.setValue(entity.getIsCorrect());
		}
		comboBoxIsCorrect.setValue(entity.getIsCorrect());
		
	}

	public void loadAssociatedObjects() {
		if (questionService == null) {
			throw new IllegalStateException("QuestionService was null");
		}
		List<Question> listQuestion = questionService.findAll();
		obsListQuestion = FXCollections.observableArrayList(listQuestion);
		comboBoxQuestion.setItems(obsListQuestion);
		
		obsListIsCorrect = FXCollections.observableArrayList("V", "F");///
		comboBoxIsCorrect.setItems(obsListIsCorrect);///
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		if (fields.contains("alternativa")) {
			labelErrorDescription.setText(errors.get("alternativa"));
		}
	}

	private void initializeComboBoxQuestion() {
		Callback<ListView<Question>, ListCell<Question>> factory = lv -> new ListCell<Question>() {
			@Override
			protected void updateItem(Question item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getDescription());
			}
		};
		comboBoxQuestion.setCellFactory(factory);
		comboBoxQuestion.setButtonCell(factory.call(null));
	}
	
	private void initializeComboBoxIsCorrect() {///
		Callback<ListView<String>, ListCell<String>> factory = lv -> new ListCell<String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item);
			}
		};
		comboBoxIsCorrect.setCellFactory(factory);
		comboBoxIsCorrect.setButtonCell(factory.call(null));
	}
	
	



}
