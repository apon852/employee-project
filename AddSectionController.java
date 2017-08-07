package ems.ui.section;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import ems.database.DatabaseHandler;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class AddSectionController implements Initializable {
	/**
	 * 
	 */
	ObservableList<Section> list = FXCollections.observableArrayList();
	DatabaseHandler databaseHandler;
	@FXML
	private HBox sec_info;
	@FXML
	private BorderPane rootPane;
	@FXML
	private TextField sId;
	@FXML
	private TextField section;
	@FXML
	private TableView<Section> tableView;
	@FXML
	private TableColumn<Section, String> idCol;
	@FXML
	private TableColumn<Section, String> sectCol;
	@FXML
	private Button saveBtn;
	@FXML
	private Button editBtn;
	@FXML
	private Button clearBtn;
	@FXML
	private Button cancelBtn;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		databaseHandler = DatabaseHandler.getInstance();
		initCol();
		loadData();
		setCellValue();
	}

	@FXML
	private void OnSaveAction(ActionEvent event) {
		String idNo = sId.getText().toUpperCase();
		String sName = section.getText();

		if (idNo.isEmpty() || sName.isEmpty()) {
			ems.notify.Messages.errorMessages("Error Messages", "Please Enter in all fields");

			return;
		}
		String qu = "INSERT INTO SectionTb (Id,Section)" + " VALUES(" + "'" + idNo + "'," + "'" + sName + "')";
		System.out.println(qu);
		if (databaseHandler.execAction(qu)) {
			ems.notify.Messages.successMessage("Information Dailog", "Saved Success");
			sId.clear();
			section.clear();
			loadData();

		} else {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Information Diloag");
			alert.setHeaderText(null);
			alert.setContentText("Faild");
			alert.showAndWait();
		}
	}
	@FXML
	private void OnUpdateAction(ActionEvent event) {
		String idNo = sId.getText().toUpperCase();
		String sName = section.getText();

		if (idNo.isEmpty() || sName.isEmpty()) {
			ems.notify.Messages.errorMessages("Error Messages", "Please Enter in all fields");
			return;
		}
		String qu = "UPDATE SectionTb SET Id='" + idNo + "', Section='" + sName + "' WHERE Id='" + idNo + "'";
		System.out.println(qu);
		if (databaseHandler.execAction(qu)) {
			ems.notify.Messages.successMessage("Information Dailog", "Update Success");
			sId.clear();
			section.clear();
			loadData();
		} else {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Information Diloag");
			alert.setHeaderText(null);
			alert.setContentText("Faild");
			alert.showAndWait();
		}
	}

	@FXML
	private void OnClearAction(ActionEvent event) {
		sId.clear();
		section.clear();
		loadData();
	}

	@FXML
	private void OnCancelAction(ActionEvent event) {
		Stage stage = (Stage) rootPane.getScene().getWindow();
		stage.close();
	}

	private void initCol() {
		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		sectCol.setCellValueFactory(new PropertyValueFactory<>("section"));
	}

	private void loadData() {
		DatabaseHandler handler = DatabaseHandler.getInstance();
		String qu = "SELECT*FROM SectionTb";
		ResultSet rs = handler.execQuery(qu);
		list.removeAll(list);
		try {

			while (rs.next()) {
				String id = rs.getString("Id");
				String sname = rs.getString("Section");
				list.add(new Section(id, sname));
			}

		} catch (SQLException e) {
			System.out.println("SQL Error");
			e.printStackTrace();
		}

		tableView.getItems().setAll(list);
	}

	@FXML
	private void loadItemInfo(ActionEvent event) {

		Boolean flag = false;

		try {
			String id = sId.getText();
			String st = "SELECT*FROM SectionTb WHERE Id='" + id + "'";
			ResultSet rs = databaseHandler.execQuery(st);

			while (rs.next()) {
				String DeptId = rs.getString("Id");
				sId.setText(DeptId);
				String secName = rs.getString("Section");
				section.setText(secName);
				flag = true;
			}
			if (!flag) {
				ems.notify.Messages.errorMessages("Error Message", "No Such Section Avaiable");
			}
		} catch (Exception e) {
			ems.notify.Messages.errorMessages("Error Message", "SQL Error");
		}
	}

	public class Section {
		private final SimpleStringProperty id;
		private final SimpleStringProperty section;

		Section(String sId, String sName) {
			this.id = new SimpleStringProperty(sId);
			this.section = new SimpleStringProperty(sName);
		}

		public String getId() {
			return id.get();
		}

		public String getSection() {
			return section.get();
		}
	}
	
	private void setCellValue(){
		tableView.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				Section p1=tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
				//txtId.setEditable(false);
				//sId.setVisible(false);
				sId.setText(p1.getId());
				section.setText(p1.getSection());
			}
		});
	}

}
