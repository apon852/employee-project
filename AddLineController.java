package ems.ui.line;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import ems.database.DatabaseHandler;
import ems.ui.floor.AddFloorController.Floor;

public class AddLineController implements Initializable {
	ObservableList<Floor> list = FXCollections.observableArrayList();
	DatabaseHandler databaseHandler;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		databaseHandler = DatabaseHandler.getInstance();
		
	}
	
	
	
	private void loadData() {
		DatabaseHandler handler = DatabaseHandler.getInstance();
		String qu = "SELECT*FROM addDepartment";
		ResultSet rs = handler.execQuery(qu);
		try {

			while (rs.next()) {
				data.add(new Element(rs.getInt("Id"), rs.getString("Department")));

			}

		} catch (SQLException e) {
			System.out.println("SQL Error");
			e.printStackTrace();
		}

		comDepartment.getItems().setAll(data);

	}

	public class Element {

		private final IntegerProperty txtId;
		private final StringProperty comDepartment;

		Element(int id, String sName) {
			this.txtId = new SimpleIntegerProperty(id);
			this.comDepartment = new SimpleStringProperty(sName);
		}

		public int getTxtId() {
			return txtId.get();
		}

		public String getComDepartment() {
			return comDepartment.get();
		}

		@Override
		public String toString() {
			return comDepartment.get();
		}
	}

}
