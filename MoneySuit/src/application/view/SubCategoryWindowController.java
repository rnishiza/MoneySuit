package application.view;

import java.time.LocalDate;

import application.MainApp;
import application.model.Categoria;
import application.model.Elemento;
import application.model.SubCategoria;
import db.DBConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import util.DateUtil;

public class SubCategoryWindowController {
	
	@FXML
	private TableView<SubCategoria> subCategoriaTable;
	@FXML
    private TableColumn<SubCategoria, String> subCategoriaColumn;
    @FXML
    private TableColumn<SubCategoria, Number> totalColumn;
    
    @FXML
	private TableView<Elemento> elementoTable;
	@FXML
    private TableColumn<Elemento, String> motivoColumn;
    @FXML
    private TableColumn<Elemento, LocalDate> fechaColumn;
    @FXML
    private TableColumn<Elemento, Number> importeColumn;
    
    @FXML
    private Label categoriaLabel;
    @FXML
    private Label subCategoriaLabel;
    @FXML
    private Label fechaLabel;
    @FXML
    private Label lugarLabel;
    @FXML
    private Label motivoLabel;
    @FXML
    private Label importeLabel;
    @FXML
    private Label descripcionLabel;
    
    @FXML
    private Label mainCategoriaLabel;
    @FXML
    private Label month_year;
	
	private MainApp mainApp;
	private Categoria categoria;
	
	public SubCategoryWindowController() {}
	
	@FXML
    private void initialize() {
		subCategoriaColumn.setCellValueFactory(cellData -> cellData.getValue().subCategoriaProperty());
		totalColumn.setCellValueFactory(cellData -> cellData.getValue().totalProperty());
		
		motivoColumn.setCellValueFactory(cellData -> cellData.getValue().motivoProperty());
		fechaColumn.setCellValueFactory(cellData -> cellData.getValue().fechaProperty());
		importeColumn.setCellValueFactory(cellData -> cellData.getValue().importeProperty());
		
		showDetails(null);
		showElements(null);
		
		elementoTable.getSelectionModel().selectedItemProperty().addListener(
	            (observable, oldValue, newValue) -> showDetails(newValue));
		subCategoriaTable.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> showElements(newValue));
	}
	
	@FXML
	private void handleNewSubCategory() {
		SubCategoria tempCategoria = new SubCategoria();
		Elemento tempElemento = new Elemento();
		int okClicked = mainApp.showSubCategoryEditDialog(categoria, tempCategoria, tempElemento);
		if (okClicked == MainApp.NEW_SUBCATEGORIA) {
			DBConnection.addSubCategory(categoria, tempCategoria);
			mainApp.getSubCategoriaData(categoria).add(tempCategoria);
		} else if (okClicked == MainApp.NEW_ELEMENTO) {
			SubCategoria sub = getSubcategoriaByID(tempElemento.getSubCategoriaId());
			DBConnection.addElemento(sub, tempElemento);
			updateSubCategoryTotal(sub, tempElemento);
			mainApp.getElementoData(sub).add(tempElemento);
		}
	}
	
	@FXML 
	private void handleEditSubCategory() {
		SubCategoria sub = subCategoriaTable.getSelectionModel().getSelectedItem();
		Elemento elem = elementoTable.getSelectionModel().getSelectedItem();
		int okClicked = 0;
		if(elem != null && sub != null) {
			okClicked = mainApp.showSubCategoryEditDialog(categoria, sub, elem);
		} else if (sub != null) {
			Elemento tempElemento = new Elemento();
			okClicked = mainApp.showSubCategoryEditDialog(categoria, sub, tempElemento);
		} else {
			mainApp.showError("Selecciona un elemento.");
		}
		
		if (MainApp.NEW_SUBCATEGORIA == okClicked) {
			DBConnection.updateSubCategory(categoria, sub);
		} else if (MainApp.NEW_ELEMENTO == okClicked && elem != null) {
			DBConnection.updateElemento(elem, sub);
			updateSubCategoryTotal(sub, elem);
		}
		
		if(okClicked > 0) {
			mainApp.setCategoriaData();
			subCategoriaTable.setItems(this.mainApp.getSubCategoriaData(categoria));
		}
		
	}
	
	@FXML
	private void handleDeleteSubCategory() {
		SubCategoria sub = subCategoriaTable.getSelectionModel().getSelectedItem();
		Elemento elem = elementoTable.getSelectionModel().getSelectedItem();
		if(elem != null && sub != null) {
			if(mainApp.showWarning("¿Estás seguro?\tSe eliminará el gasto: " + elem.getMotivo())) {
				DBConnection.deleteElemento(elem, sub);
				elementoTable.getItems().remove(elem);
			}
		} else if (sub != null) {
			if(mainApp.showWarning("¿Estás seguro?\tSe eliminarán todos los datos de la sub-categoría: " + sub.getSubCategoria())) {
				DBConnection.deleteSubCategory(categoria, sub);
				subCategoriaTable.getItems().remove(sub);
			}
		} else {
			mainApp.showError("Selecciona un elemento.");
		}
	}
	
	private void updateSubCategoryTotal(SubCategoria sub, Elemento elemento) {
		double total = sub.getTotal();
		total = total + elemento.getImporte();
		sub.setTotal(total);
	}
	
	@FXML
	private void handleBack() {
		this.mainApp.showOverview();
	}
	
	public void setMainApp(MainApp mainApp, Categoria categoria) {
        this.mainApp = mainApp;
        this.categoria = categoria;
        subCategoriaTable.setItems(this.mainApp.getSubCategoriaData(categoria));
        mainCategoriaLabel.setText(categoria.getCategoria());
        month_year.setText(DateUtil.getMonth() + " / " + DateUtil.getYear());
	}
	
	private SubCategoria getSubcategoriaByID(int id) {
		for(SubCategoria sc : subCategoriaTable.getItems()) {
			if(sc.getId() == id) {
				return sc;
			}
		}
		return null;
	}
	
	private String getSubcategoria() {
		SubCategoria item = subCategoriaTable.getSelectionModel().getSelectedItem();
		return item != null ? item.getSubCategoria() : "";
	}
	
	private void showElements(SubCategoria subCategoria) {
		if(subCategoria != null) {
			elementoTable.setItems(this.mainApp.getElementoData(subCategoria));
		} else {
			elementoTable.setItems(null);
		}
	}
	
	private void showDetails(Elemento elemento) {
		if (elemento != null) {
			categoriaLabel.setText(categoria.getCategoria());
			subCategoriaLabel.setText(getSubcategoria());
			fechaLabel.setText(DateUtil.format(elemento.getFecha()));
			lugarLabel.setText(elemento.getLugar());
			motivoLabel.setText(elemento.getMotivo());
			importeLabel.setText(String.valueOf(elemento.getImporte()));
			descripcionLabel.setText(elemento.getDescripcion());
		} else {
			categoriaLabel.setText("");
			subCategoriaLabel.setText("");
			fechaLabel.setText("");
			lugarLabel.setText("");
			motivoLabel.setText("");
			importeLabel.setText("");
			descripcionLabel.setText("");
		}
	}

}
