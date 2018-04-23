package application.view;

import java.math.BigInteger;
import java.security.MessageDigest;

import application.MainApp;
import db.DBConnection;
import entity.User;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LogInWindowController {
	
	@FXML
	private TextField user;
	@FXML
	private TextField pass;
	@FXML
	private TextField user_r;
	@FXML
	private TextField pass_r;
	@FXML
	private TextField pass_cr;
	
	private MainApp mainApp;
	
	public LogInWindowController() {
	}
	
	@FXML
	private void initialize() {
		
	}
	
	@FXML
	private void handleLogIn() {
		String user = this.user.textProperty().get();
		String pass = this.pass.textProperty().get();
		String pass_coded = getMD5Pass(pass);
		User u = DBConnection.logIn(user, pass_coded);
		if(u != null) {
			mainApp.setUser(u);
			mainApp.showOverview();
		} else {
			mainApp.showError("Not valid user or password.");
			clearFields();
		}
	}
	
	@FXML
	private void handleRegister() {
		String user = this.user_r.textProperty().get();
		String pass = this.pass_r.textProperty().get();
		String pass_c = this.pass_cr.textProperty().get();
		if(!pass.equals(pass_c)) {
			mainApp.showError("Password confirmation not the same.");
			clearFields();
			return;
		}
		String pass_coded = getMD5Pass(pass);
		boolean ok = DBConnection.register(user, pass_coded);
		if(ok) {
			mainApp.showInfo("User correctly created.");
			this.user.requestFocus();
		}else {
			mainApp.showError("Error");
		}
		clearFields();
	}
	
	private void clearFields() {
		this.user_r.textProperty().set("");
		this.pass_r.textProperty().set("");
		this.pass_cr.textProperty().set("");
		this.user.textProperty().set("");
		this.pass.textProperty().set("");
	}
	
	private String getMD5Pass(String pass) {
		try {
			byte[] pass_byte = pass.getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] pass_md5 = md.digest(pass_byte);
			
			BigInteger bigInt = new BigInteger(1,pass_md5);
			String pass_coded = bigInt.toString(16);
			return pass_coded;
		} catch (Exception e) {
			return null;
		}
	}
	
	public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
	}

}
