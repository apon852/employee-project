package ems.ui.designation;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ems.database.DatabaseHandler;

public class AddDesignationController implements Initializable {
	ObservableList<String> type = FXCollections.observableArrayList("Staff", "Worker");
	ObservableList<String> ot = FXCollections.observableArrayList("Y", "N");
	ObservableList<Designation> list = FXCollections.observableArrayList();
	@FXML
	private BorderPane rootPane;
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtDesig;
	@FXML
	ComboBox<String> comType;
	@FXML
	private TextField txtGrade;
	@FXML
	private TextField txtTiffinBill;
	@FXML
	private TextField txtAttBonus;
	@FXML
	private ComboBox<String> comOverTime;
	@FXML
	private TextField txtHoliday;
	@FXML
	private TextField txtLunchBill;

	@FXML
	private TableView<Designation> tableView;
	@FXML
	private TableColumn<Designation, String> idCol;
	@FXML
	private TableColumn<Designation, String> desigCol;
	@FXML
	private TableColumn<Designation, String> typeCol;
	@FXML
	private TableColumn<Designation, String> gradeCol;
	@FXML
	private TableColumn<Designation, String> tiffinCol;
	@FXML
	private TableColumn<Designation, String> attBonusCol;
	@FXML
	private TableColumn<Designation, String> overTimeCol;
	@FXML
	private TableColumn<Designation, String> holidayCol;
	@FXML
	private TableColumn<Designation, String> lunchBillCol;

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

	DatabaseHandler databaseHandler;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		databaseHandler = DatabaseHandler.getInstance();
		comType.setValue("Worker");
		comType.getItems().addAll(type);
		comOverTime.setValue("Y");
		comOverTime.getItems().addAll(ot);
		rootPane.setManaged(false);

		// txtId.setVisible(false);
		

		initCol();
		loadData();
		setCellValue();
		searchValue();

	}

	@FXML
	private void OnSaveAction(ActionEvent event) {
		try {
			String desig = txtDesig.getText();
			String type = comType.getValue();
			int grade = Integer.parseInt(txtGrade.getText());
			int tiffin = Integer.parseInt(txtTiffinBill.getText());
			int attBonus = Integer.parseInt(txtAttBonus.getText());
			String ot = comOverTime.getValue();
			int holiday = Integer.parseInt(txtHoliday.getText());
			int lunchBill = Integer.parseInt(txtLunchBill.getText());

			if (desig.isEmpty()) {
				ems.notify.Messages.errorMessages("Error Messages", "Please Enter in all fields");

				return;
			}
			String qu = "INSERT INTO DesigTb(Desig,Type,Grade,TiffinBill,AttBonus,OverTime,HoliyDay,LunchBill) "
					+ "VALUES('" + desig + "','" + type + "','" + grade + "','" + tiffin + "','" + attBonus + "','" + ot
					+ "','" + holiday + "','" + lunchBill + "')";
			System.out.println(qu);
			if (databaseHandler.execAction(qu)) {
				ems.notify.Messages.successMessage("Information Dailog", "Saved Success");

				loadData();

			} else {
				ems.notify.Messages.errorMessages("Error Message", "Sorry, con't Save");
			}
		} catch (Exception e) {
			ems.notify.Messages.errorMessages("Error Messages", "Faild for empty string: ");
		}

	}
	
	@FXML
	private void OnUpdateAction(ActionEvent event) {

		String id = txtId.getText();
		String desig = txtDesig.getText();
		String type = comType.getValue();
		String grade = txtGrade.getText();
		String tiffin = txtTiffinBill.getText();
		String attBonus = txtAttBonus.getText();
		String ot = comOverTime.getValue();
		String holiday = txtHoliday.getText();
		String lunchBill = txtLunchBill.getText();

		try {
			if (desig.isEmpty()) {
				ems.notify.Messages.errorMessages("Error Messages", "Please Enter in all fields");

				return;
			}
			String qu = "UPDATE DesigTb SET Desig='" + desig + "',Type='" + type + "',Grade='" + grade
					+ "',TiffinBill='" + tiffin + "',AttBonus='" + attBonus + "',OverTime='" + ot + "',HoliyDay='"
					+ holiday + "',lunchBill='" + lunchBill + "' WHERE Id='" + id + "'";
			System.out.println(qu);
			if (databaseHandler.execAction(qu)) {
				ems.notify.Messages.successMessage("Information Dailog", "Update Success");
				loadData();

			} else {
				ems.notify.Messages.errorMessages("Error Message", "Update Faild!");
			}
		} catch (Exception e) {
			ems.notify.Messages.errorMessages("Error Message", "Error: "+e);
		}
	}
	
	@FXML
	private void OnCancelAction(ActionEvent event) {
		Stage stage = (Stage) rootPane.getScene().getWindow();
		stage.close();
	}
	@FXML
	private void OnClearAction(ActionEvent event) {
		allClear();
		loadData();
	}
	private void initCol() {
		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		desigCol.setCellValueFactory(new PropertyValueFactory<>("designation"));
		typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
		gradeCol.setCellValueFactory(new PropertyValueFactory<>("grade"));
		tiffinCol.setCellValueFactory(new PropertyValueFactory<>("tiffin"));
		attBonusCol.setCellValueFactory(new PropertyValueFactory<>("attBonus"));
		overTimeCol.setCellValueFactory(new PropertyValueFactory<>("overTime"));
		holidayCol.setCellValueFactory(new PropertyValueFactory<>("holiday"));
		lunchBillCol.setCellValueFactory(new PropertyValueFactory<>("lunchBill"));
	}

	private void loadData() {
		DatabaseHandler handler = DatabaseHandler.getInstance();
		String qu = "SELECT*FROM DesigTb";
		ResultSet rs = handler.execQuery(qu);
		list.removeAll(list);
		try {

			while (rs.next()) {
				String id = rs.getString("Id");
				String designation = rs.getString("Desig");
				String type = rs.getString("Type");
				String grade = rs.getString("Grade");
				String tiffin = rs.getString("TiffinBill");
				String attBonus = rs.getString("AttBonus");
				String overTime = rs.getString("OverTime");
				String holiday = rs.getString("HoliyDay");
				String lunchBill = rs.getString("LunchBill");

				list.add(new Designation(id, designation, type, tiffin, grade, attBonus, overTime, holiday, lunchBill));
			}

		} catch (SQLException e) {
			System.out.println("SQL Error");
			e.printStackTrace();
		}

		tableView.getItems().setAll(list);

	}

	public class Designation {
		private final SimpleStringProperty id;
		private final SimpleStringProperty designation;
		private final SimpleStringProperty type;
		private final SimpleStringProperty grade;
		private final SimpleStringProperty tiffin;

		private final SimpleStringProperty attBonus;
		private final SimpleStringProperty overTime;
		private final SimpleStringProperty holiday;
		private final SimpleStringProperty lunchBill;

		Designation(String id2, String Desig, String Type, String tiffin, String grade2, String attBonus2,
				String OverTime, String holiday2, String lunchBill2) {
			this.id = new SimpleStringProperty(id2);
			this.designation = new SimpleStringProperty(Desig);
			this.type = new SimpleStringProperty(Type);
			this.grade = new SimpleStringProperty(grade2);
			this.tiffin = new SimpleStringProperty(tiffin);
			this.attBonus = new SimpleStringProperty(attBonus2);
			this.overTime = new SimpleStringProperty(OverTime);
			this.holiday = new SimpleStringProperty(holiday2);
			this.lunchBill = new SimpleStringProperty(lunchBill2);
		}
	
		public String getId() {
			return id.get();
		}

		public String getDesignation() {
			return designation.get();
		}

		public String getType() {
			return type.get();
		}

		public String getGrade() {
			return grade.get();
		}

		public String getTiffin() {
			return tiffin.get();
		}

		public String getAttBonus() {
			return attBonus.get();
		}

		public String getOverTime() {
			return overTime.get();
		}

		public String getHoliday() {
			return holiday.get();
		}

		public String getLunchBill() {
			return lunchBill.get();
		}
	}	

	@FXML
	private void loadItemInfo(ActionEvent event) {

		Boolean flag = false;

		try {
			// int id = Integer.parseInt(txtId.getText());
			String id = txtId.getText();
			String designation = txtId.getText();
			String st = "SELECT*FROM DesigTb WHERE Id='" + id + "' OR Desig='" + designation + "'";
			ResultSet rs = databaseHandler.execQuery(st);

			while (rs.next()) {
				String Designation = rs.getString("Desig");
				txtDesig.setText(Designation);
				String type = rs.getString("Type");
				comType.setValue(type);
				String grade = rs.getString("Grade");
				txtGrade.setText(grade);
				String tiffin = rs.getString("TiffinBill");
				txtTiffinBill.setText(tiffin);
				String att = rs.getString("AttBonus");
				txtAttBonus.setText(att);
				String ot = rs.getString("OverTime");
				comOverTime.setValue(ot);
				String holyday = rs.getString("HoliyDay");
				txtHoliday.setText(holyday);
				String lunch = rs.getString("lunchBill");
				txtLunchBill.setText(lunch);

				flag = true;
			}
			if (!flag) {
				ems.notify.Messages.errorMessages("Error Message", "No Such Avaiable");

			}
		} catch (Exception e) {
			ems.notify.Messages.errorMessages("Error Message", "SQL Error");
		}
	}
	
	
	void allClear() {
		txtId.clear();
		txtId.setVisible(true);
		txtDesig.clear();
		//comType.setValue("");
		txtGrade.clear();
		txtTiffinBill.clear();
		txtAttBonus.clear();
		//comOverTime.setValue("");
		txtHoliday.clear();
		txtLunchBill.clear();
	}
	private void setCellValue(){
		tableView.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				Designation p1=tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
				//txtId.setEditable(false);
				txtId.setVisible(false);
				txtId.setText(p1.getId());
				txtDesig.setText(p1.getDesignation());
				comType.setValue(p1.getType());
				txtGrade.setText(p1.getGrade());
				txtTiffinBill.setText(p1.getTiffin());
				txtAttBonus.setText(p1.getAttBonus());
				comOverTime.setValue(p1.getOverTime());
				txtHoliday.setText(p1.getHoliday());
				txtLunchBill.setText(p1.getLunchBill());
			}
		});
	}
	
	private void searchValue(){
		FilteredList<Designation> filterData= new FilteredList<>(list, e-> true);
		txtId.setOnKeyReleased(e->{
			txtId.textProperty().addListener((ObservableValue, oldValue,newValue) ->{
				filterData.setPredicate((Predicate<? super Designation>) designation->{
					if (newValue==null || newValue.isEmpty()) {
						return true;
					}
					String lowerCaseFilter= newValue.toLowerCase();
					if (designation.getId().contains(newValue)) {
						return true;
						
					}else if (designation.getDesignation().toLowerCase().contains(lowerCaseFilter)) {
						return true;
						
					}
					return false;
				});
			});
			SortedList<Designation> sortedData= new SortedList<>(filterData);
			sortedData.comparatorProperty().bind(tableView.comparatorProperty());
			tableView.setItems(sortedData);
			
		});
	}
}
	

