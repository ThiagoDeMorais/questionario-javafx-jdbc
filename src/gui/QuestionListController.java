package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Question;
import model.servicies.AlternativeService;
import model.servicies.QuestionService;

public class QuestionListController implements Initializable, DataChangeListener {
	private QuestionService questionService;

	private AlternativeService alternativeService;

	@FXML
	private TableView<Question> tableViewQuestion;

	@FXML
	private TableColumn<Question, Integer> tableColumnId;

	@FXML
	private TableColumn<Question, String> tableColumnDescription;

	@FXML
	private TableColumn<Question, Question> tableColumnEDIT;

	@FXML
	private TableColumn<Question, Question> tableColumnREMOVE;

	@FXML
	private Button btNew;

	private ObservableList<Question> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Question obj = new Question();
		createDialogForm(obj, "/gui/QuestionForm.fxml", parentStage);

	}

	public void setQuestionService(QuestionService questionService) {
		this.questionService = questionService;
	}
	
	public void setAlternativeService(AlternativeService alternativeService) {
		this.alternativeService = alternativeService;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory((new PropertyValueFactory<>("id")));
		tableColumnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewQuestion.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if (questionService == null) {
			throw new IllegalStateException("Serviço nulo");
		}

		List<Question> list = questionService.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewQuestion.setItems(obsList);
		initEditButtons();
		initRemoveButtons();

	}

	private void createDialogForm(Question obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			QuestionFormController controller = loader.getController();
			controller.setQuestion(obj);
			controller.setServices(new QuestionService(), new AlternativeService());
			controller.loadAssociatedObjects();
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Entre com o enunciado da questão");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Question, Question>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Question obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createDialogForm(obj, "/gui/QuestionForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Question, Question>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Question obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeQuestionEntity(obj));
			}
		});
	}

	private void removeQuestionEntity(Question obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Tem certeza que deseja deletar?");

		if (result.get() == ButtonType.OK) {
			if (questionService == null) {
				throw new IllegalStateException("Service was null");
			}
			removeAlternativesEntity(obj);
			try {
				questionService.remove(obj);
				updateTableView();
			} catch (DbException e) {
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}

	private void removeAlternativesEntity(Question obj) {

		if (alternativeService == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			(alternativeService.findByQuestion(obj.getId())).forEach(alternative -> {
				alternativeService.remove(alternative);
			});;
		} catch (DbException e) {
			Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
		}

	}

//	private void removeEntity(Alternative obj) {
//		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Tem certeza que deseja deletar?");
//		
//		if(result.get() == ButtonType.OK) {
//			if(questionService == null) {
//				throw new IllegalStateException("Service was null");
//			}
//			try {
//				questionService.remove(obj);
//				updateTableView();
//			}catch(DbException e) {
//				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
//			}
//		}
//	}
}
