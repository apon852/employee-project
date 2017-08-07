package ems.ui.floor;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ems.database.DatabaseHandler;

public class AddFloorController implements Initializable {
	ObservableList<Floor> list = FXCollections.observableArrayList();
	DatabaseHandler databaseHandler;
	
	@FXML
	private BorderPane rootPane;
	@FXML
	private TextField fId;
	@FXML
	private TextField txtFloorEng;
	@FXML
	private TextField txtFloorBan;
	@FXML
	private TextField txtSearch;
	
	@FXML
	private Button saveBtn;
	@FXML
	private Button editBtn;
	@FXML
	private Button searchBtn;
	@FXML
	private Button clearBtn;
	@FXML
	private Button cancelBtn;
	
	@FXML
	private TableView<Floor> tableView;
	@FXML
	private TableColumn<Floor, String> idCol;
	@FXML
	private TableColumn<Floor, String> eFloorCol;
	@FXML
	private TableColumn<Floor, String> bFloorCol;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		databaseHandler = DatabaseHandler.getInstance();
		fId.setVisible(false);
		txtSearch.setVisible(false);	
		initCol();
		loadData();
		setCellValue();
		setSearchValue();
		
	}
	
	@FXML
	private void OnSaveAction(ActionEvent event) {
		String eFloor= txtFloorEng.getText();
		String bFloor = txtFloorBan.getText();

		if (eFloor.isEmpty() || bFloor.isEmpty()) {
			ems.notify.Messages.errorMessages("Error Messages", "Please Enter in all fields");

			return;
		}
		String qu = "INSERT INTO FloorTb(EnglishFloor,BanglaFloor)" + " VALUES(" + "'" + eFloor + "'," + "'" + bFloor + "')";
		System.out.println(qu);
		if (databaseHandler.execAction(qu)) {
			ems.notify.Messages.successMessage("Information Dailog", "Saved Success");
			
			

		} else {
			ems.notify.Messages.errorMessages("Error Message", "Save Faild!");
		}

	}
	
	@FXML
	private void OnUpdateAction(ActionEvent event) {
		String fIdNo = fId.getText();
		String eFname = txtFloorEng.getText();
		String bFname = txtFloorBan.getText();

		if (fIdNo.isEmpty() || eFname.isEmpty() || bFname.isEmpty()) {
			ems.notify.Messages.errorMessages("Error Messages", "Please Enter in all fields");
			return;
		}
		String qu = "UPDATE FloorTb SET Id='" + fIdNo + "', EnglishFloor='" + eFname + "', BanglaFloor='" + bFname + "' WHERE Id='" + fIdNo + "'";
		System.out.println(qu);
		if (databaseHandler.execAction(qu)) {
			ems.notify.Messages.successMessage("Information Dailog", "Update Success");
			loadData();
		} else {
			ems.notify.Messages.errorMessages("Information Dailog", "Update Faild");
		}
	}
	
	
	@FXML
	private void OnClearAction(ActionEvent event){
		fId.clear();
		txtFloorEng.clear();
		txtFloorBan.clear();
		loadData();
		txtSearch.setVisible(false);
		
	}
	
	@FXML
	private void OnCancelAction(ActionEvent event) {
		Stage stage = (Stage) rootPane.getScene().getWindow();
		stage.close();

	}

	
	@FXML
	private void OnSearchAction(ActionEvent event){
		txtSearch.setVisible(true);
	}
	
	private void initCol() {
		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		eFloorCol.setCellValueFactory(new PropertyValueFactory<>("ef"));
		//bFloorCol.setCellValueFactory(new PropertyValueFactory<>("bf"));
		bFloorCol.setCellValueFactory(
                 new PropertyValueFactory<>("bf"));
		bFloorCol.getStyleClass().add("TeamViewer12,14");

			}
	private void loadData() {
		DatabaseHandler handler = DatabaseHandler.getInstance();
		String qu = "SELECT*FROM FloorTb";
		ResultSet rs = handler.execQuery(qu);
		list.removeAll(list);
		try {

			while (rs.next()) {
				String id = rs.getString("Id");
				String engFloor = rs.getString("EnglishFloor");
				String bngFloor = rs.getString("BanglaFloor");
				list.add(new Floor(id,engFloor,bngFloor));
			}

		} catch (SQLException e) {
			System.out.println("SQL Error");
			e.printStackTrace();
		}

		tableView.getItems().setAll(list);
	}
	
	public class Floor {
		private final SimpleStringProperty id;
		private final SimpleStringProperty ef;
		private final SimpleStringProperty bf;

		Floor(String fId, String engFloorName, String banFloorName) {
			this.id = new SimpleStringProperty(fId);
			this.ef = new SimpleStringProperty(engFloorName);
			this.bf = new SimpleStringProperty(banFloorName);
		}

		public String getId() {
			return id.get();
		}

		public String getEf() {
			return ef.get();
		}

		public String getBf() {
			return bf.get();
		}

		
	}
	
	private void setCellValue(){
		tableView.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				Floor p1=tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
				//txtId.setEditable(false);
				fId.setVisible(false);
				fId.setText(p1.getId());
				txtFloorEng.setText(p1.getEf());
				txtFloorBan.setText(p1.getBf());
				
			}
		});
	}
	
	/**
	 * 
	 */
	private void setSearchValue() {
		FilteredList<Floor> filterData = new FilteredList<>(list, e -> true);
		txtSearch.setOnKeyReleased(e -> {
			txtSearch.textProperty().addListener((ObservableValue, oldValue, newValue) -> {
				filterData.setPredicate((Predicate<? super Floor>) floor -> {
					if (newValue == null || newValue.isEmpty()) {
						return true;
					}
					String lowerCaseFilter = newValue.toLowerCase();
					if (floor.getId().contains(newValue)) {
						return true;
					} else if (floor.getEf().toLowerCase().contains(lowerCaseFilter)) {
						return true;
					}
					else if (floor.getBf().toLowerCase().contains(lowerCaseFilter)) {
						return true;
					}
					
					return false;
					
				});
			});
			SortedList<Floor> sortedData = new SortedList<>(filterData);
			sortedData.comparatorProperty().bind(tableView.comparatorProperty());
			tableView.setItems(sortedData);

		});
	}
	
	

}
