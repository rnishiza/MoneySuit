package application.view;

import java.util.List;

import application.MainApp;
import application.model.Categoria;
import db.DBConnection;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import util.DateUtil;

public class MainWindowController {

	@FXML
	private TableView<Categoria> categoriaTable;
	@FXML
	private TableColumn<Categoria, String> categoriaColumn;
	@FXML
	private TableColumn<Categoria, Number> presupuestoColumn;
	@FXML
	private TableColumn<Categoria, Number> gastadoColumn;
	@FXML
	private TableColumn<Categoria, Number> restanteColumn;
	
	@FXML
	private TableView<Categoria> totalTable;
	@FXML
	private TableColumn<Categoria, String> categoriaTotalColumn;
	@FXML
	private TableColumn<Categoria, Number> presupuestoTotalColumn;
	@FXML
	private TableColumn<Categoria, Number> gastadoTotalColumn;
	@FXML
	private TableColumn<Categoria, Number> restanteTotalColumn;

	@FXML
	private Label saldoCuentaLabel;
	@FXML
	private Label saldoEfectivoLabel;
	@FXML
	private Label mesLabel;

	private MainApp mainApp;

	public MainWindowController() {
	}

	@FXML
	private void initialize() {
		categoriaColumn.setCellValueFactory(cellData -> cellData.getValue().categoriaProperty());
		presupuestoColumn.setCellValueFactory(cellData -> cellData.getValue().presupuestoProperty());
		gastadoColumn.setCellValueFactory(cellData -> cellData.getValue().gastadoProperty());
		restanteColumn.setCellValueFactory(cellData -> cellData.getValue().restanteProperty());
		
		categoriaTotalColumn.setCellValueFactory(cellData -> cellData.getValue().categoriaProperty());
		presupuestoTotalColumn.setCellValueFactory(cellData -> cellData.getValue().presupuestoProperty());
		gastadoTotalColumn.setCellValueFactory(cellData -> cellData.getValue().gastadoProperty());
		restanteTotalColumn.setCellValueFactory(cellData -> cellData.getValue().restanteProperty());

		handleDoubleClick();
	}

	private void handleDoubleClick() {
		categoriaTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getClickCount() == 2) {
					Categoria c = categoriaTable.getSelectionModel().getSelectedItem();
					if (c != null) {
						showSubCategory(c);
					}
				}
			}
		});
	}

	private void showSubCategory(Categoria c) {
		this.mainApp.showSubCategory(c);
	}

	@FXML
	private void handleDeleteCategory() {
		int selectedIndex = categoriaTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			Categoria c = categoriaTable.getItems().get(selectedIndex);
			if(mainApp.showWarning("¿Estás seguro?\tSe eliminarán todos los datos de la categoría: " + c.getCategoria())) {
				DBConnection.deleteCategory(mainApp.getUser(), c);
				categoriaTable.getItems().remove(selectedIndex);
				updateTotalTable();
			}			
		} else {
			mainApp.showError("Selecciona una categoria.");
		}
	}

	@FXML
	private void handleNewCategory() {
		Categoria tempCategoria = new Categoria();
		boolean okClicked = mainApp.showCategoryEditDialog(tempCategoria);
		if (okClicked) {
			DBConnection.addCategory(mainApp.getUser(), tempCategoria);
			mainApp.getCategoriaData().add(tempCategoria);
			updateTotalTable();
		}
	}

	@FXML
	private void handleEditCategory() {
		Categoria selectedCategory = categoriaTable.getSelectionModel().getSelectedItem();
		if (selectedCategory != null) {
			mainApp.showCategoryEditDialog(selectedCategory);
			DBConnection.updateCategory(mainApp.getUser(), selectedCategory);
			mainApp.setCategoriaData();
			updateTotalTable();
		} else {
			mainApp.showError("Selecciona una categoria.");
		}
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		
		mesLabel.setText(DateUtil.getMonth(DateUtil.getDate()) + " / " + DateUtil.getYear(DateUtil.getDate()));
		categoriaTable.setItems(mainApp.getCategoriaData());
		updateTotalTable();
	}
	
	public void updateTotalTable() {
		List<Categoria> items = categoriaTable.getItems();
		double presupuesto = 0.0;
		double gastado = 0.0;
		double restante = 0.0;
		for(Categoria c : items) {
			presupuesto = presupuesto + c.getPresupuesto();
			gastado = gastado + c.getGastado();
			restante = restante + c.getRestante();
		}
		totalTable.getItems().clear();
		totalTable.getItems().add(new Categoria("TOTAL", presupuesto, gastado, restante, 0));
	}
}
