package application.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Categoria {
	
	private final StringProperty categoria;
	private final DoubleProperty presupuesto;
	private final DoubleProperty gastado;
	private final DoubleProperty restante;
	private int id;
	
	public Categoria() {
		this("", 0, 0, 0, -1);
	}
	
	public Categoria(String categoria, double presupuesto, 
			double gastado, double restante, int id) {
		this.categoria = new SimpleStringProperty(categoria);
		this.presupuesto = new SimpleDoubleProperty(presupuesto);
		this.gastado = new SimpleDoubleProperty(gastado);
		this.restante = new SimpleDoubleProperty(restante);
		this.id = id;
	}

	public String getCategoria() {
		return categoria.get();
	}
	
	public void setCategoria(String categoria) {
		this.categoria.set(categoria);
	}

	public Double getPresupuesto() {
		return presupuesto.get();
	}
	
	public void setPresupuesto(double presupuesto) {
		this.presupuesto.set(presupuesto);
	}

	public Double getGastado() {
		return gastado.get();
	}
	
	public void setGastado(double gastado) {
		this.gastado.set(gastado);
	}

	public Double getRestante() {
		return restante.get();
	}
	
	public void setRestante(double restante) {
		this.restante.set(restante);
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public StringProperty categoriaProperty() {
		return this.categoria;
	}
	
	public DoubleProperty presupuestoProperty() {
		return this.presupuesto;
	}
	
	public DoubleProperty gastadoProperty() {
		return this.gastado;
	}
	
	public DoubleProperty restanteProperty() {
		return this.restante;
	}
}
