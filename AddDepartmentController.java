package ems.ui.department;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import ems.database.DatabaseHandler;


public class AddDepartmentController implements Initializable {
	ObservableList<Company> list = FXCollections.observableArrayList();
	DatabaseHandler databaseHandler;
	@FXML
	private HBox dept_info;
	@FXML
	private BorderPane rootPane;
	@FXML
	private TextField dId;
	@FXML
	private TextField dept;
	@FXML
	private TableView<Company> tableView;
	@FXML
	private TableColumn<Company, String> idCol;
	@FXML
	private TableColumn<Company, String> deptCol;
	@FXML
	private Button saveBtn;
	@FXML
	private Button editBtn;
	@FXML
	private Button deleteBtn;
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
		String idNo = dId.getText().toUpperCase();
		String dName = dept.getText();

		if (idNo.isEmpty() || dName.isEmpty()) {
			ems.notify.Messages.errorMessages("Error Messages", "Please Enter in all fields");

			return;
		}
		String qu = "INSERT INTO DeptTb(Id,Dept)" + " VALUES(" + "'" + idNo + "'," + "'" + dName + "')";
		System.out.println(qu);
		if (databaseHandler.execAction(qu)) {
			ems.notify.Messages.successMessage("Information Dailog", "Item Saved Success");
			dId.clear();
			dept.clear();
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
		String idNo = dId.getText().toUpperCase();
		String dName = dept.getText();
		
		
		
		if (idNo.isEmpty() || dName.isEmpty()) {
			ems.notify.Messages.errorMessages("Error Messages", "Please Enter in all fields");

			return;
		}
		String qu = "UPDATE DeptTb SET Id='" +idNo + "',Dept='" + dName + "' WHERE Id='" +idNo + "'";
		System.out.println(qu);
		if (databaseHandler.execAction(qu)) {
			ems.notify.Messages.successMessage("Information Dailog", "Update Success");
			dId.clear();
			dept.clear();
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
	private void OnDeleteAction(ActionEvent event) {

		String id = dId.getText();
		
		if (id.isEmpty()) {
			ems.notify.Messages.errorMessages("Error Messages", "Please Enter ID No: ");

			return;
		}
		
		
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Confirm Delete Operation");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure want to delete the item " + dept.getText() + "?");

		Optional<ButtonType> response = alert.showAndWait();
		if(response.get()==ButtonType.OK){
			String qu = "DELETE FROM DeptTb WHERE Id='" + id + "'";
			
			if(databaseHandler.execAction(qu)){
				ems.notify.Messages.successMessage("Infrmation Message", "Delete Success");
				dId.clear();
				dept.clear();
				loadData();
			}else {
				ems.notify.Messages.errorMessages("Error Message", "Delete Faild!");
			}
		}
	}
	
	@FXML
	private void OnClearAction(ActionEvent event) {
		dId.clear();
		dept.clear();
		
	}
	
	@FXML
	private void OnCancelAction(ActionEvent event) {
		Stage stage = (Stage) rootPane.getScene().getWindow();
		stage.close();

	}
	
	private void initCol() {
		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		deptCol.setCellValueFactory(new PropertyValueFactory<>("dept"));
			}
	private void loadData() {
		DatabaseHandler handler = DatabaseHandler.getInstance();
		String qu = "SELECT*FROM DeptTb";
		ResultSet rs = handler.execQuery(qu);
		list.removeAll(list);
		try {

			while (rs.next()) {
				String id = rs.getString("Id");
				String dname = rs.getString("Dept");
								list.add(new Company(id,dname));
			}

		} catch (SQLException e) {
			System.out.println("SQL Error");
			e.printStackTrace();
		}

		tableView.getItems().setAll(list);
	}
	
	
	/*@FXML
	private void loadItemInfo(ActionEvent event) {

		Boolean flag = false;

		try {
			String id = dId.getText();
			String st = "SELECT*FROM DeptTb WHERE Id='" + id + "'";
			ResultSet rs = databaseHandler.execQuery(st);

			while (rs.next()) {
				String DeptId = rs.getString("Id");
				dId.setText(DeptId);
				String DeptName = rs.getString("Dept");
				dept.setText(DeptName);
				
				flag = true;
			}
			if (!flag) {
				ems.notify.Messages.errorMessages("Error Message", "No Such Department Avaiable");

			}
		} catch (Exception e) {
			ems.notify.Messages.errorMessages("Error Message", "SQL Error");
		}
	}*/
	
	private void setCellValue(){
		tableView.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				Company p1=tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
				//txtId.setEditable(false);
				//txtId.setVisible(false);
				dId.setText(p1.getId());
				dept.setText(p1.getDept());
				
			}
		});
	}
	
	public class Company {
		private final SimpleStringProperty id;
		private final SimpleStringProperty dept;

		Company(String dId, String dName) {
			this.id = new SimpleStringProperty(dId);
			this.dept = new SimpleStringProperty(dName);
		}

		public String getId() {
			return id.get();
		}

		public String getDept() {
			return dept.get();
		}
		

	}

}
