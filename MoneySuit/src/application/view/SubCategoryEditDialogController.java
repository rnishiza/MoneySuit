package application.view;

import application.model.Categoria;
import application.model.Elemento;
import application.model.SubCategoria;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SubCategoryEditDialogController {
	
	@FXML
	private ComboBox<SubCategoria> subCategoriaComboBox;
	@FXML
	private DatePicker fechaDatePicker;
	@FXML
	private TextField lugarField;
	@FXML
	private TextField motivoField;
	@FXML
	private TextField importeField;
	@FXML
	private TextField descripcionField;
	
	@FXML
	private TextField subCategoriaField;

	private Stage dialogStage;
	private Categoria categoria;
	private SubCategoria subCategoria;
	private Elemento elemento;
	private boolean elementoOkClicked = false;
	private boolean subCategoriaOkClicked = false;

	@FXML
	private void initialize() {
		//subCategoriaComboBox.setItems(value);
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public void setSubCategory(SubCategoria subCategoria, Categoria categoria) {
		this.subCategoria = subCategoria;
		this.categoria = categoria;
		subCategoriaField.setText(subCategoria.getSubCategoria());
	}
	
	public void setElemento(Elemento elemento, ObservableList<SubCategoria> subCategorias) {
		this.elemento = elemento;
		fechaDatePicker.setValue(elemento.getFecha());
		lugarField.setText(elemento.getLugar());
		motivoField.setText(elemento.getMotivo());
		importeField.setText(String.valueOf(elemento.getImporte()));
		descripcionField.setText(elemento.getDescripcion());
		
		subCategoriaComboBox.setItems(subCategorias);
		
		int index = 0;
		boolean encontrado = false;
		for(SubCategoria sc : subCategoriaComboBox.getItems()) {
			if(elemento.getSubCategoriaId() == sc.getId()) {
				encontrado = true;
				break;
			}
			index++;
		}
		
		if(encontrado) {
			subCategoriaComboBox.getSelectionModel().select(index);
		}
	}
	
	public boolean isSubCategoriaOkClicked() {
		return subCategoriaOkClicked;
	}
	
	public boolean isElementOkClicked() {
		return elementoOkClicked;
	}

	@FXML
	private void handleSubCategoriaOk() {
		if (isSubCategoriaInputValid()) {
			subCategoria.setSubCategoria(subCategoriaField.getText());
			subCategoria.setCategoriaId(this.categoria.getId());
			subCategoria.setTotal(0);
			subCategoriaOkClicked = true;
			dialogStage.close();
		}
	}
	
	@FXML
	private void handleElementoOk() {
		if(isElementoInputValid()) {
			elemento.setDescripcion(descripcionField.getText());
			elemento.setFecha(fechaDatePicker.getValue());
			elemento.setImporte(Integer.valueOf(importeField.getText()));
			elemento.setLugar(lugarField.getText());
			elemento.setMotivo(motivoField.getText());
			elemento.setSubCategoriaId(subCategoriaComboBox.getSelectionModel().getSelectedItem().getId());
			elementoOkClicked = true;
			dialogStage.close();
		}
	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	private boolean isSubCategoriaInputValid() {
		String errorMessage = "";
		
		if (subCategoriaField.getText() == null || subCategoriaField.getText().length() == 0) {
            errorMessage += "Sub-categoria no valida\n"; 
        }
		
		if (errorMessage.length() == 0) {
            return true;
        } else {
        		showError(errorMessage);
        		return false;
        }
	}
	
	private boolean isElementoInputValid() {
		
		String errorMessage = "";
		
		if (descripcionField.getText() == null || descripcionField.getText().length() == 0) {
            errorMessage += "Descripcion no valida\n"; 
        }
		
		if (lugarField.getText() == null || lugarField.getText().length() == 0) {
            errorMessage += "Lugar no valida\n"; 
        }
		
		if (motivoField.getText() == null || motivoField.getText().length() == 0) {
            errorMessage += "Motivo no valida\n"; 
        }
		
		if (subCategoriaComboBox.getSelectionModel().getSelectedItem() == null) {
			errorMessage += "Sub-categoria no valida\n";
		}
		
		if (fechaDatePicker.getValue() == null) {
			errorMessage += "Fecha no valida\n";
		}
		
		if (importeField.getText() == null || importeField.getText().length() == 0) {
            errorMessage += "Importe no valido\n"; 
        } else {
            try {
                Integer.parseInt(importeField.getText());
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
