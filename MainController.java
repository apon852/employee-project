package ems.ui.main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import ems.database.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainController implements Initializable {
	 DatabaseHandler databaseHandler;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		 databaseHandler = DatabaseHandler.getInstance();
		
	}
	
	 @FXML
	    private void loadAddCompany(ActionEvent event) {
	        loadWindow("/ems/ui/company/company_info.fxml", "Add New Company");

	    }
	 
	 @FXML
	    private void loadAddDept(ActionEvent event) {
	        loadWindow("/ems/ui/department/add_department.fxml", "Add New Department");

	    }
	 
	 @FXML
	    private void loadAddSection(ActionEvent event) {
	        loadWindow("/ems/ui/section/add_section.fxml", "Add New Section");

	    }
	 
	 @FXML
	    private void loadAddDesignation(ActionEvent event) {
	        loadWindow("/ems/ui/designation/add_designation.fxml", "Add New Designation");

	    }
	 
	 @FXML
	    private void loadAddFloor(ActionEvent event) {
	        loadWindow("/ems/ui/floor/add_floor.fxml", "Add New Floor");

	    }

	public void loadWindow(String loc, String title) {
		try {
			Parent parent = FXMLLoader.load(getClass().getResource(loc));
			Stage stage = new Stage(StageStyle.DECORATED);
			stage.resizableProperty().setValue(Boolean.FALSE);
			stage.setTitle(title);
			stage.setScene(new Scene(parent));
			stage.show();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	}
