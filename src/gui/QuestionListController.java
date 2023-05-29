package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Question;

public class QuestionListController implements Initializable{
	
	@FXML
	private TableView<Question> tableViewQuestion;
	
	@FXML
	private TableColumn<Question,Integer> tableColumnId;
	
	@FXML
	private TableColumn<Question,String> tableColumnDescription;
	
	@FXML
	private Button btNew;
	
	@FXML
	public void onBtNewAction() {
		System.out.println("onBtNewAction");
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory((new PropertyValueFactory<>("id")));
		tableColumnDescription.setCellValueFactory(new PropertyValueFactory<>("descricao"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewQuestion.prefHeightProperty().bind(stage.heightProperty());
	}
}
