package application.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SubCategoria {
	
	private final StringProperty subCategoria;
	private final DoubleProperty total;
	private int id;
	private int categoriaId;
	
	public SubCategoria() {
		this(null, 0, -1, -1);
	}
	
	public SubCategoria(String subCategoria, double total, int id, int categoriaId) {
		this.subCategoria = new SimpleStringProperty(subCategoria);
		this.total = new SimpleDoubleProperty(total);
		this.id = id;
		this.categoriaId = categoriaId;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getCategoriaId() {
		return categoriaId;
	}
	
	public void setCategoriaId(int categoriaId) {
		this.categoriaId = categoriaId;
	}
	
	public String getSubCategoria() {
		return this.subCategoria.get();
	}
	
	public Double getTotal() {
		return this.total.get();
	}
	
	public void setSubCategoria(String subCategoria) {
		this.subCategoria.set(subCategoria);
	}
	
	public void setTotal(double total) {
		this.total.set(total);
	}
	
	public StringProperty subCategoriaProperty() {
		return this.subCategoria;
	}
	
	public DoubleProperty totalProperty() {
		return this.total;
	}

	@Override
	public String toString() {
		return getSubCategoria();
	}
}
