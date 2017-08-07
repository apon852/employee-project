package ems.ui.company;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Main extends Application {

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		// Parent root = (Parent)
		// FXMLLoader.load(Main.class.getResource("/store/ui/section/add_section.fxml"));
		// Parent root =
		// FXMLLoader.load(getClass().getResource("/store/ui/listitem/list_item.fxml"));
		 Parent root =
		 FXMLLoader.load(getClass().getResource("/ems/ui/designation/add_designation.fxml"));
		// stage.getIcons().add(new Image("/ems/icon/main.png"));
		 stage.getIcons().add(new Image("file:resources/images/ems_designation.png"));
		
		stage.setTitle("Add Designation");
		stage.resizableProperty().setValue(Boolean.FALSE);
		 //stage.initStyle(StageStyle.UTILITY);
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		new Thread(() -> {
			ems.database.DatabaseHandler.getInstance();

		}).start();

	}
}
