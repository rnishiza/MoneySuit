package application.model;

import java.time.LocalDate;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Elemento {
	
	private final StringProperty motivo;
	private final ObjectProperty<LocalDate> fecha;
	private final DoubleProperty importe;
	private String lugar;
	private String descripcion;
	private int id;
	private int subCategoriaId;
	
	public Elemento() {
		this(null, null, 0, null, null, -1, -1);
	}
	
	public Elemento(String motivo, LocalDate fecha, double importe, String lugar, String descripcion,
			int id, int subCategoriaId) {
		this.motivo = new SimpleStringProperty(motivo);
		this.fecha = new SimpleObjectProperty<LocalDate>(fecha);
		this.importe = new SimpleDoubleProperty(importe);
		this.lugar = lugar;
		this.descripcion = descripcion;
		this.id = id;
		this.subCategoriaId = subCategoriaId;
	}
	
	public String getLugar() {
		return this.lugar;
	}
	
	public String getDescripcion() {
		return this.descripcion;
	}
	
	public void setLugar(String lugar) {
		this.lugar = lugar;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getSubCategoriaId() {
		return subCategoriaId;
	}
	
	public void setSubCategoriaId(int subCategoriaId) {
		this.subCategoriaId = subCategoriaId;
	}
	
	public String getMotivo() {
		return this.motivo.get();
	}
	
	public LocalDate getFecha() {
		return this.fecha.get();
	}
	
	public Double getImporte() {
		return this.importe.get();
	}
	
	public void setMotivo(String motivo) {
		this.motivo.set(motivo);
	}
	
	public void setFecha(LocalDate fecha) {
		this.fecha.set(fecha);
	}
	
	public void setImporte(double importe) {
		this.importe.set(importe);
	}
	
	public StringProperty motivoProperty() {
		return this.motivo;
	}
	
	public ObjectProperty<LocalDate> fechaProperty() {
		return this.fecha;
	}
	
	public DoubleProperty importeProperty() {
		return this.importe;
	}

}
