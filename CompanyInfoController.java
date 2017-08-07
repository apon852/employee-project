package ems.ui.company;

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
import javafx.stage.Stage;
import ems.database.DatabaseHandler;



public class CompanyInfoController implements Initializable {
	ObservableList<Company> list = FXCollections.observableArrayList();

	@FXML
	private BorderPane root;
	@FXML
	private TextField eComCode;
	@FXML
	private TextField eComName;
	@FXML
	private TextField eComAddress;
	@FXML
	private TextField eComContract;

	@FXML
	private TextField bComName;
	@FXML
	private TextField bComAddress;
	@FXML
	private TextField bComContract;
	@FXML
	private TextField bComOwner;

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
	
	@FXML
	private TableView<Company> tableView;
	@FXML
	private TableColumn<Company, String> codeCol;
	@FXML
	private TableColumn<Company, String> nameCol;
	@FXML
	private TableColumn<Company, String> addressCol;
	@FXML
	private TableColumn<Company, String> contractCol;


	DatabaseHandler databaseHandler;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		databaseHandler = DatabaseHandler.getInstance();
		initCol();
		loadData();
		setCellValue();
	}

	@FXML
	private void OnSaveAction(ActionEvent event) {
		String eCcode = eComCode.getText().toUpperCase();
		String eCname = eComName.getText();
		String eCaddress = eComAddress.getText();
		String eCcontract = eComContract.getText();
		
		String bCname = bComName.getText();
		String bCaddress = bComAddress.getText();
		String bCcontract = bComContract.getText();
		String bCownerName = bComOwner.getText();

		
		if (eCcode.isEmpty() || eCname.isEmpty() || eCaddress.isEmpty() || eCcontract.isEmpty()) {
			ems.notify.Messages.errorMessages("Error Messages", "Please Enter in all fields");

			return;
		}
		String qu = "INSERT INTO CompanyTb(ComId,ComName,ComAddress,ComContract,BComName,BComAddress,BComContract,AuName)"
				+ " VALUES(" + "'" + eCcode + "'," + "'"
				+ eCname + "'," + "'" + eCaddress + "'," + "'" + eCcontract + "'," + "'" + bCname + "', '"+bCaddress+"','"+bCcontract+"','"+bCownerName+"')";
		System.out.println(qu);
		if (databaseHandler.execAction(qu)) {
			ems.notify.Messages.successMessage("Information Dailog", "Item Saved Success");
			allClear();
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
		String eCcode = eComCode.getText().toUpperCase();
		String eCname = eComName.getText();
		String eCaddress = eComAddress.getText();
		String eCcontract = eComContract.getText();
		
		String bCname = bComName.getText();
		String bCaddress = bComAddress.getText();
		String bCcontract = bComContract.getText();
		String bCownerName = bComOwner.getText();
		
		
		if (eCcode.isEmpty() || eCname.isEmpty() || eCaddress.isEmpty() || eCcontract.isEmpty()) {
			ems.notify.Messages.errorMessages("Error Messages", "Please Enter in all fields");

			return;
		}
		String qu = "UPDATE CompanyTb SET ComId='" + eCcode + "',ComName='" + eCname + "',ComAddress='" + eCaddress
				+ "',ComContract='" + eCcontract + "',BComName='" + bCname + "',BComAddress='" + bCaddress + "',BComContract='" + bCcontract + "',AuName='" + bCownerName + "' WHERE ComId='" + eCcode + "'";
		System.out.println(qu);
		if (databaseHandler.execAction(qu)) {
			ems.notify.Messages.successMessage("Information Dailog", "Update Success");
			allClear();
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

		String ComCode = eComCode.getText();
		
		if (ComCode.isEmpty()) {
			ems.notify.Messages.errorMessages("Error Messages", "Please Enter Company Code No: ");

			return;
		}
		
		
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Confirm Delete Operation");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure want to delete the item " + eComCode.getText() + "?");

		Optional<ButtonType> response = alert.showAndWait();
		if(response.get()==ButtonType.OK){
			String qu = "DELETE FROM CompanyTb WHERE ComId='" + ComCode + "'";
			
			if(databaseHandler.execAction(qu)){
				ems.notify.Messages.successMessage("Infrmation Message", "Delete Success");
				allClear();
				loadData();
			}else {
				ems.notify.Messages.errorMessages("Error Message", "Delete Faild!");
			}
		}
	}
	private void initCol() {
		codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
		contractCol.setCellValueFactory(new PropertyValueFactory<>("contract"));
	}
	private void loadData() {
		DatabaseHandler handler = DatabaseHandler.getInstance();
		String qu = "SELECT*FROM CompanyTb";
		ResultSet rs = handler.execQuery(qu);
		list.removeAll(list);
		try {

			while (rs.next()) {
				String code = rs.getString("ComId");
				String name = rs.getString("ComName");
				String address = rs.getString("ComAddress");
				String contract = rs.getString("ComContract");
				list.add(new Company(code, name, address, contract));
			}

		} catch (SQLException e) {
			System.out.println("SQL Error");
			e.printStackTrace();
		}

		tableView.getItems().setAll(list);
	}
	
	public class Company {
		private final SimpleStringProperty code;
		private final SimpleStringProperty name;
		private final SimpleStringProperty address;
		private final SimpleStringProperty contract;

		Company(String cCode, String cName, String cAddress, String cContract) {
			this.code = new SimpleStringProperty(cCode);
			this.name = new SimpleStringProperty(cName);
			this.address = new SimpleStringProperty(cAddress);
			this.contract = new SimpleStringProperty(cContract);
		}

		public String getCode() {
			return code.get();
		}

		public String getName() {
			return name.get();
		}

		public String getAddress() {
			return address.get();
		}

		public String getContract() {
			return contract.get();
		}
}
	
	@FXML
	private void loadItemInfo(ActionEvent event) {

		Boolean flag = false;

		try {
			String code = eComCode.getText().toUpperCase();
			String st = "SELECT*FROM CompanyTb WHERE ComId='" + code + "'";
			ResultSet rs = databaseHandler.execQuery(st);

			while (rs.next()) {
				String companyCode = rs.getString("ComId");
				eComCode.setText(companyCode);
				String companyName = rs.getString("ComName");
				eComName.setText(companyName);
				String companyAddress = rs.getString("ComAddress");
				eComAddress.setText(companyAddress);
				String companyPhone = rs.getString("ComContract");
				eComContract.setText(companyPhone);

				String bCompanyName = rs.getString("BComName");
				bComName.setText(bCompanyName);
				String bCompanyAddress = rs.getString("BComAddress");
				bComAddress.setText(bCompanyAddress);
				String bCompanyPhone = rs.getString("BComContract");
				bComContract.setText(bCompanyPhone);
				String bCompanyOwner = rs.getString("AuName");
				bComOwner.setText(bCompanyOwner);

				flag = true;
			}
			if (!flag) {
				ems.notify.Messages.errorMessages("Error Message", "No Such Company Information Avaiable");

			}
		} catch (Exception e) {
			ems.notify.Messages.errorMessages("Error Message", "SQL Error");
		}
	}
	
	
	@FXML
	private void OnClearAction(ActionEvent event) {
		allClear();
		
	}
	
	@FXML
	private void OnCancelAction(ActionEvent event) {
		Stage stage = (Stage) root.getScene().getWindow();
		stage.close();

	}
	
	private void setCellValue(){
		tableView.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				Company p1=tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
				eComCode.setText(p1.getCode());
				eComName.setText(p1.getName());
				eComAddress.setText(p1.getAddress());
				eComContract.setText(p1.getContract());
				
			}
		});
	}
	
	
	void allClear() {
		eComCode.clear();
		eComName.clear();
		eComAddress.clear();
		eComContract.clear();
		bComName.clear();
		bComAddress.clear();
		bComContract.clear();
		bComOwner.clear();
		
		
	}

}










