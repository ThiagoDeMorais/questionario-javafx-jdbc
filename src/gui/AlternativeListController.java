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
import model.entities.Alternative;
import model.servicies.AlternativeService;
import model.servicies.QuestionService;

public class AlternativeListController implements Initializable, DataChangeListener {
	private AlternativeService service;

	@FXML
	private TableView<Alternative> tableViewAlternative;

	@FXML
	private TableColumn<Alternative, Integer> tableColumnId;

	@FXML
	private TableColumn<Alternative, String> tableColumnDescription;
	
	@FXML
	private TableColumn<Alternative, String> tableColumnIsCorrect;

	@FXML
	private TableColumn<Alternative, Alternative> tableColumnEDIT;

	@FXML
	private TableColumn<Alternative, Alternative> tableColumnREMOVE;
	
	@FXML
	private TableColumn<Alternative, Integer> tableColumnIdQuestion;


	private ObservableList<Alternative> obsList;
	

	public void setAlternativeService(AlternativeService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory((new PropertyValueFactory<>("id")));
		tableColumnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
		tableColumnIsCorrect.setCellValueFactory(new PropertyValueFactory<>("isCorrect"));
		tableColumnIdQuestion.setCellValueFactory( new PropertyValueFactory<>("Question"));;
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewAlternative.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Serviço nulo");
		}

		List<Alternative> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewAlternative.setItems(obsList);


	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

}
