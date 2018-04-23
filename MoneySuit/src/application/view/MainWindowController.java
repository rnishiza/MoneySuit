package application.view;

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
    		
    		mesLabel.setText(DateUtil.getMonth() + " / " + DateUtil.getYear());
    		handleDoubleClick();
    }
    
    private void handleDoubleClick() {
    		categoriaTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					if (event.getClickCount() == 2) {
						Categoria c = categoriaTable.getSelectionModel().getSelectedItem();
						if(c != null) {
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
        		DBConnection.deleteCategory(mainApp.getUser(), c);
        		categoriaTable.getItems().remove(selectedIndex);
        }else {
        		mainApp.showError("Selecciona una categoría.");
        }
    }
    
    @FXML
    private void handleNewCategory() {
        Categoria tempCategoria = new Categoria();
        boolean okClicked = mainApp.showCategoryEditDialog(tempCategoria);
        if (okClicked) {
            DBConnection.addCategory(mainApp.getUser(), tempCategoria);
            mainApp.getCategoriaData().add(tempCategoria);
        }
    }

    @FXML
    private void handleEditCategory() {
    		Categoria selectedCategory = categoriaTable.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            mainApp.showCategoryEditDialog(selectedCategory);
            DBConnection.updateCategory(mainApp.getUser(), selectedCategory);
        } else {
        		mainApp.showError("Selecciona una categoría.");
        }
    }
    
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        categoriaTable.setItems(mainApp.getCategoriaData());
    }
}
