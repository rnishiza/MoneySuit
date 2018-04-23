package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import application.model.Categoria;
import application.model.Elemento;
import application.model.SubCategoria;
import application.view.CategoryEditDialogController;
import application.view.LogInWindowController;
import application.view.MainWindowController;
import application.view.SubCategoryEditDialogController;
import application.view.SubCategoryWindowController;
import db.DBConnection;
import db.MyConnectionUtil;
import entity.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainApp extends Application {
	
	public static final int NEW_ELEMENTO = 1;
	public static final int NEW_SUBCATEGORIA = 2;
	
	private ObservableList<Categoria> categoriaData = FXCollections.observableArrayList();
	
	private HashMap<Categoria, ObservableList<SubCategoria>> subCategoriaData;
	
	private HashMap<SubCategoria, ObservableList<Elemento>> elementoData;

	private Stage primaryStage;
    private BorderPane rootLayout;
    
    private User user;
    
    public MainApp() {
    }
    
    public ObservableList<Categoria> getCategoriaData() {
        return categoriaData;
    }
    
    public ObservableList<SubCategoria> getSubCategoriaData(Categoria categoria) {
        return subCategoriaData.get(categoria);
    }
    
    public ObservableList<Elemento> getElementoData(SubCategoria subCategoria) {
    		return elementoData.get(subCategoria);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("MoneySuit");
        
        connect();
        setOnCloseConnection();

        initRootLayout();
        showLogIn();
    }
    
	private void setOnCloseConnection() {
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				closeConnection();
				Platform.exit();
			}
		});
	}

    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showError(String message) {
		Alert alert = new Alert(AlertType.ERROR, message, ButtonType.OK);
		alert.showAndWait();
	}
    
    public void showInfo(String message) {
		Alert alert = new Alert(AlertType.INFORMATION, message, ButtonType.OK);
		alert.showAndWait();
	}
    
    public void showLogIn() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/LogInWindow.fxml"));
			AnchorPane mainWindow = (AnchorPane) loader.load();

			rootLayout.setCenter(mainWindow);

			LogInWindowController controller = loader.getController();
			controller.setMainApp(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public void showOverview() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/MainWindow.fxml"));
            AnchorPane mainWindow = (AnchorPane) loader.load();

            rootLayout.setCenter(mainWindow);
            
            MainWindowController controller = loader.getController();
            controller.setMainApp(this);
            setCategoriaData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showSubCategory(Categoria categoria) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/SubCategoryWindow.fxml"));
            AnchorPane mainWindow = (AnchorPane) loader.load();

            rootLayout.setCenter(mainWindow);
            
            SubCategoryWindowController controller = loader.getController();
            controller.setMainApp(this, categoria);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public boolean showCategoryEditDialog(Categoria categoria) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/CategoryEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Editar Categoria");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);
            // Set the person into the controller.
            CategoryEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setCategory(categoria);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public int showSubCategoryEditDialog(Categoria categoria, SubCategoria subCategoria, Elemento elemento) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/SubCategoryEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Editar Sub-Categoria");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);
            // Set the person into the controller.
            SubCategoryEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setSubCategory(subCategoria, categoria);
            controller.setElemento(elemento, subCategoriaData.get(categoria));

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            boolean elementoFlag = controller.isElementOkClicked();
            boolean subCategoriaFlag = controller.isSubCategoriaOkClicked();
            
            if(elementoFlag) {
            		return NEW_ELEMENTO;
            } else if(subCategoriaFlag) {
            		return NEW_SUBCATEGORIA;
            } else {
            		return 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    private void setCategoriaData() {
    		categoriaData.clear();
    		subCategoriaData = new HashMap<Categoria, ObservableList<SubCategoria>>();
    		elementoData = new HashMap<SubCategoria, ObservableList<Elemento>>();
    		
    		ObservableList<SubCategoria> subCategoriasAux = FXCollections.observableArrayList();
    		
        categoriaData.addAll(DBConnection.getCategoriaData(user));
        for(Categoria categoria : categoriaData) {
        		subCategoriasAux.addAll(DBConnection.getSubCategoriaData(categoria));
        		subCategoriaData.put(categoria, subCategoriasAux);
        		for(SubCategoria sc : subCategoriasAux) {
        			elementoData.put(sc, DBConnection.getElementoData(sc));
        		}
        }
    }

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

    public static void main(String[] args) {
        launch(args);
    }
    
    private static void connect() {
		try {
			Connection con = MyConnectionUtil.getConnection();
			if(con != null){
				System.out.println("Connected");
			}else{
				System.out.println("Failed to connect");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void closeConnection(){
		System.out.println("Closing connection");
		MyConnectionUtil.closeConnection();
	}
}
