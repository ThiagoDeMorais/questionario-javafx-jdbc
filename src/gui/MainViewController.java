package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.servicies.QuestionService;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemAlternative;

	@FXML
	private MenuItem menuItemQuestion;

	@FXML
	private MenuItem menuItemAbout;

	@FXML
	public void onMenuItemAlternativeAction() {
		System.out.println("menuItemAlternative");
	}

	@FXML
	public void onMenuItemQuestionAction() {
		loadView("/gui/QuestionList.fxml", (QuestionListController controller) -> {
			controller.setQuestionService(new QuestionService());
			controller.updateTableView();
		});

	}

	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml", x ->{});
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		// TODO Auto-generated method stub

	}

	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();

			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
			T controller = loader.getController();
			initializingAction.accept(controller);

		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error Loading view", e.getMessage(), AlertType.ERROR);
		}
	}
	
}
