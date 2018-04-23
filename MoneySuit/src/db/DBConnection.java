package db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.model.Categoria;
import application.model.Elemento;
import application.model.SubCategoria;
import entity.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DBConnection {
	
	private static Connection _con;
	
	protected static void setConnection(Connection con) {
		_con = con;
	}
	
	private static Connection getCon() {
		try {
			if(_con == null || _con.isClosed()) {
				_con = MyConnectionUtil.getConnection();
			}
			return _con;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean register(String login, String pass) {
		try {
			PreparedStatement statement = getCon().prepareStatement
					("INSERT INTO USER VALUES (null, ?, ?);");
			statement.setString(1, login);
			statement.setString(2, pass);
			int i = statement.executeUpdate();
			if(i>=0) return true;
			return false;
		}catch (SQLException e) {
			return false;
		}
	}
	
	public static User logIn(String login, String pass) {
		try {
			PreparedStatement statement = getCon().prepareStatement
					("SELECT * FROM USER WHERE LOGIN = ? AND PASSWORD = ?");
			statement.setString(1, login);
			statement.setString(2, pass);
			ResultSet result = statement.executeQuery();
			result.last();
			if(result != null && result.getRow() > 0) {
				int id = result.getInt(User.ID);
				User user = new User(id, login);
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static ObservableList<Categoria> getCategoriaData(User user){
		ObservableList<Categoria> categoriaData = FXCollections.observableArrayList();
		try {
			PreparedStatement statement = getCon().prepareStatement
					("SELECT * FROM CATEGORIA WHERE ID_USER = ?");
			statement.setInt(1, user.getId());
			ResultSet result = statement.executeQuery();
			while(result.next()) {
				categoriaData.add(new Categoria(result.getString("NAME"), result.getDouble("PRESUPUESTO"),
						0.0, result.getDouble("PRESUPUESTO"), result.getInt("ID")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return categoriaData;
	}
	
	public static ObservableList<SubCategoria> getSubCategoriaData(Categoria categoria) {
		ObservableList<SubCategoria> subCategoriaData = FXCollections.observableArrayList();
		try {
			PreparedStatement statement = getCon().prepareStatement
					("SELECT * FROM SUBCATEGORIA WHERE ID_CATEGORIA = ?");
			statement.setInt(1, categoria.getId());
			ResultSet result = statement.executeQuery();
			while(result.next()) {
				subCategoriaData.add(new SubCategoria(result.getString("NAME"), 0, result.getInt("ID"),
						result.getInt("ID_CATEGORIA")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return subCategoriaData;
	}
	
	public static ObservableList<Elemento> getElementoData(SubCategoria subcategoria) {
		ObservableList<Elemento> elementoData = FXCollections.observableArrayList();
		try {
			PreparedStatement statement = getCon().prepareStatement
					("SELECT * FROM ELEMENTO WHERE ID_SUBCATEGORIA = ?");
			statement.setInt(1, subcategoria.getId());
			ResultSet result = statement.executeQuery();
			while(result.next()) {
				elementoData.add(new Elemento(result.getString("MOTIVO"), result.getDate("FECHA").toLocalDate(),
						result.getDouble("IMPORTE"), result.getString("LUGAR"), result.getString("DESCRIPCION"),
						result.getInt("ID"), result.getInt("ID_SUBCATEGORIA")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return elementoData;
	}

	public static boolean addCategory(User user, Categoria categoria) {
		try {
			PreparedStatement statement = getCon().prepareStatement
					("INSERT INTO CATEGORIA VALUES (null, ?, ?, ?) RETURNING ID;");
			statement.setInt(1, user.getId());
			statement.setString(2, categoria.getCategoria());
			statement.setDouble(3, categoria.getPresupuesto());
			ResultSet rs = statement.executeQuery();
			rs.next();
			categoria.setId(rs.getInt(1));
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public static boolean addSubCategory(Categoria categoria, SubCategoria subCategoria) {
		try {
			PreparedStatement statement = getCon().prepareStatement
					("INSERT INTO SUBCATEGORIA VALUES (null, ?, ?) RETURNING ID;");
			statement.setInt(1, categoria.getId());
			statement.setString(2, subCategoria.getSubCategoria());
			ResultSet rs = statement.executeQuery();
			rs.next();
			subCategoria.setId(rs.getInt(1));
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public static boolean addElemento(SubCategoria subCategoria, Elemento elemento) {
		try {
			PreparedStatement statement = getCon().prepareStatement
					("INSERT INTO ELEMENTO VALUES (null, ?, ?, ?, ?, ?, ?) RETURNING ID;");
			statement.setInt(1, subCategoria.getId());
			statement.setString(2, elemento.getMotivo());
			statement.setDate(3, Date.valueOf(elemento.getFecha()));
			statement.setDouble(4, elemento.getImporte());
			statement.setString(5, elemento.getLugar());
			statement.setString(6, elemento.getDescripcion());
			ResultSet rs = statement.executeQuery();
			rs.next();
			subCategoria.setId(rs.getInt(1));
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public static boolean updateCategory(User user, Categoria categoria) {
		try {
			PreparedStatement statement = getCon().prepareStatement
					("UPDATE CATEGORIA SET NAME = ?, PRESUPUESTO = ? WHERE ID = ? AND ID_USER = ?;");
			statement.setString(1, categoria.getCategoria());
			statement.setDouble(2, categoria.getPresupuesto());
			statement.setInt(3, categoria.getId());
			statement.setInt(4, user.getId());
			int i = statement.executeUpdate();
			if(i>=0) return true;
			return false;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public static boolean updateSubCategory(Categoria categoria, SubCategoria subCategoria) {
		try {
			PreparedStatement statement = getCon().prepareStatement
					("UPDATE SUBCATEGORIA SET NAME = ? WHERE ID = ? AND ID_CATEGORIA = ?;");
			statement.setString(1, subCategoria.getSubCategoria());
			statement.setInt(2, subCategoria.getId());
			statement.setInt(3, categoria.getId());
			int i = statement.executeUpdate();
			if(i>=0) return true;
			return false;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public static boolean updateElemento(Elemento elemento, SubCategoria subCategoria) {
		try {
			PreparedStatement statement = getCon().prepareStatement
					("UPDATE ELEMENTO SET MOTIVO = ?, FECHA = ?, IMPORTE = ?, LUGAR = ?, DESCRIPCION = ? "
							+ "WHERE ID = ? AND ID_SUBCATEGORIA = ?;");
			statement.setString(1, elemento.getMotivo());
			statement.setDate(2, Date.valueOf(elemento.getFecha()));
			statement.setDouble(3, elemento.getImporte());
			statement.setString(4, elemento.getLugar());
			statement.setString(5, elemento.getDescripcion());
			statement.setInt(6, elemento.getId());
			statement.setInt(7, subCategoria.getId());
			int i = statement.executeUpdate();
			if(i>=0) return true;
			return false;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public static boolean deleteCategory(User user, Categoria categoria) {
		try {
			PreparedStatement statement = getCon().prepareStatement
					("DELETE FROM CATEGORIA WHERE ID = ? AND ID_USER = ?;");
			statement.setInt(1, categoria.getId());
			statement.setInt(2, user.getId());
			int i = statement.executeUpdate();
			if(i>=0) return true;
			return false;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public static boolean deleteCategory(Categoria categoria, SubCategoria subCategoria) {
		try {
			PreparedStatement statement = getCon().prepareStatement
					("DELETE FROM SUBCATEGORIA WHERE ID = ? AND ID_CATEGORIA = ?;");
			statement.setInt(1, subCategoria.getId());
			statement.setInt(2, categoria.getId());
			int i = statement.executeUpdate();
			if(i>=0) return true;
			return false;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public static boolean deleteElemento(Elemento elemento, SubCategoria subCategoria) {
		try {
			PreparedStatement statement = getCon().prepareStatement
					("DELETE FROM ELEMENTO WHERE ID = ? AND ID_SUBCATEGORIA = ?;");
			statement.setInt(1, elemento.getId());
			statement.setInt(2, subCategoria.getId());
			int i = statement.executeUpdate();
			if(i>=0) return true;
			return false;
		} catch (SQLException e) {
			return false;
		}
	}

}
