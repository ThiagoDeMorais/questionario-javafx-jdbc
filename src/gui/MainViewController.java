package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

public class MainViewController implements Initializable {
	
	@FXML
	private MenuItem menuItemAlternativa; 
	
	@FXML
	private MenuItem menuItemPergunta;
	
	@FXML
	private MenuItem menuItemSobre;

	@FXML
	public void onMenuItemAlternativaAction() {
		System.out.println("onMenuItemAlternativaAction");
	}
	
	@FXML
	public void onMenuItemPerguntaAction() {
		System.out.println("onMenuItemPerguntaAction");

	}
	
	public void onMenuItemSobreAction() {
		System.out.println("onMenuItemSobreAction");

	}
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		// TODO Auto-generated method stub
		
	}	
}
