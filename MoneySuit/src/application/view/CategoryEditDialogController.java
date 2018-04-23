package application.view;

import application.model.Categoria;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class CategoryEditDialogController {

	@FXML
	private TextField categoriaField;
	@FXML
	private TextField presupuestoField;

	private Stage dialogStage;
	private Categoria categoria;
	private boolean okClicked = false;

	@FXML
	private void initialize() {
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public void setCategory(Categoria categoria) {
		this.categoria = categoria;
		categoriaField.setText(categoria.getCategoria());
		presupuestoField.setText(Double.toString(categoria.getPresupuesto()));
	}

	public boolean isOkClicked() {
		return okClicked;
	}

	@FXML
	private void handleOk() {
		if (isInputValid()) {
			categoria.setCategoria(categoriaField.getText());
			categoria.setPresupuesto(Double.valueOf(presupuestoField.getText()));
			calculateCategoryValues(categoria);
			okClicked = true;
			dialogStage.close();
		}
	}

	private void calculateCategoryValues(Categoria categoria) {
		// TODO
		categoria.setGastado(0.0);
		categoria.setRestante(Double.valueOf(presupuestoField.getText()));
	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	private boolean isInputValid() {
		String errorMessage = "";
		
		if (categoriaField.getText() == null || categoriaField.getText().length() == 0) {
            errorMessage += "Categoria no valida\n"; 
        }
		
		if (presupuestoField.getText() == null || presupuestoField.getText().length() == 0) {
            errorMessage += "Presupuesto no valido\n"; 
        } else {
            try {
                Integer.parseInt(presupuestoField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "Presupuesto no valido (debe ser un numero)\n"; 
            }
        }
		if (errorMessage.length() == 0) {
            return true;
        } else {
        		showError(errorMessage);
        		return false;
        }
	}
	
    public void showError(String message) {
		Alert alert = new Alert(AlertType.ERROR, message, ButtonType.OK);
		alert.showAndWait();
	}

}
