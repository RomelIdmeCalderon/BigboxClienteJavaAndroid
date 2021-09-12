package com.romelidme.androidbigbox.entidades;

public class Producto_Entidad {
    private String id;
    private String descripcion;
    private String idCategoria;
    private String idMedida;
    private String idTienda;
    private String idUsuario;
    private String imagen;
    private String nombre;
    private String nombreCategoria;
    private String nombreMedida;
    private String precioProveedores;
    private Double precio;
    private int cantidad;


    public Producto_Entidad() {

    }

    public int getCantidad() { return cantidad; }

    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public String getPrecioProveedores() {
        return precioProveedores;
    }

    public void setPrecioProveedores(String precioProveedores) { this.precioProveedores = precioProveedores; }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(String idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getIdMedida() {
        return idMedida;
    }

    public void setIdMedida(String idMedida) {
        this.idMedida = idMedida;
    }

    public String getIdTienda() {
        return idTienda;
    }

    public void setIdTienda(String idTienda) {
        this.idTienda = idTienda;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) { this.nombreCategoria = nombreCategoria; }

    public String getNombreMedida() {
        return nombreMedida;
    }

    public void setNombreMedida(String nombreMedida) {
        this.nombreMedida = nombreMedida;
    }

}

