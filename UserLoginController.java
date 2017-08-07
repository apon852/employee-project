package ems.ui.login;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ems.database.DatabaseHandler;

public class UserLoginController implements Initializable {

	@FXML
	private BorderPane rootPane;
	@FXML
	private TextField userName;
	@FXML
	private PasswordField userPassword;
	@FXML
	private Button loginBtn;
	@FXML
	private Button cancelBtn;

	DatabaseHandler databaseHandler;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		databaseHandler = DatabaseHandler.getInstance();

	}

	@FXML
	private void OnLoginAction(ActionEvent event) {

		String uName = userName.getText();
		String uPassword = userPassword.getText();

		// Boolean flag = false;

		DatabaseHandler handler = DatabaseHandler.getInstance();
		String qu = "SELECT*FROM LoginTb WHERE UserName='" + uName + "'AND UserPassword='" + uPassword + "'";
		ResultSet rs = handler.execQuery(qu);
		try {
			while (rs.next()) {
				if (rs.getString("UserName") != null && rs.getString("UserPassword") != null) {
					((Node) (event.getSource())).getScene().getWindow().hide();
					loadWindow("/ems/ui/main/Main.fxml", "Employee Managment System");

				} else
					ems.notify.Messages.errorMessages("Error Messages", "Please Check your username and password");

			}
		} catch (Exception ex) {
			System.err.println(ex.getClass());

		}

	}

	@FXML
	private void OnCancelAction(ActionEvent event) {
		Stage stage = (Stage) rootPane.getScene().getWindow();
		stage.close();

	}

	public void loadWindow(String loc, String title) {
		try {
			Parent parent = FXMLLoader.load(getClass().getResource(loc));
			Stage stage = new Stage(StageStyle.DECORATED);
			stage.resizableProperty().setValue(Boolean.TRUE);
			stage.setMaximized(true);
			stage.setTitle(title);
			stage.setScene(new Scene(parent));
			stage.show();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

}
