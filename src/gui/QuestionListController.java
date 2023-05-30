package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Question;
import model.servicies.QuestionService;

public class QuestionListController implements Initializable{
	private QuestionService service;
	
	@FXML
	private TableView<Question> tableViewQuestion;
	
	@FXML
	private TableColumn<Question,Integer> tableColumnId;
	
	@FXML
	private TableColumn<Question,String> tableColumnDescription;
	
	@FXML
	private Button btNew;
	
	private ObservableList<Question> obsList;
	
	@FXML
	public void onBtNewAction() {
		System.out.println("onBtNewAction");
	}
	
	public void setQuestionService(QuestionService service) {
		this.service = service;
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
		if(service == null) {
			throw new IllegalStateException("Serviço nulo");
		}
		
		List<Question> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewQuestion.setItems(obsList);
	}
}
