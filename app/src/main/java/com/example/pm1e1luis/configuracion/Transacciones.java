package com.example.pm1e1luis.configuracion;

public class Transacciones  {


    public static final String namedb = "PM010256";

    //Tablas de la base de datos
    public static final String Tabla  = "personas";

    // Campos de la tabla
    public static final String id = "id";
    public static final String pais = "pais";
    public static final String nombres = "nombres" ;
    public static final String telefono = "telefono";
    public static final String notas = "notas";
    public static final String imagen="imagen";

    public static final String CreateTablePersonas = "CREATE TABLE personas "+
            "( id INTEGER PRIMARY KEY AUTOINCREMENT,pais TEXT, nombres TEXT, telefono INTEGER, " +
            "notas TEXT )";

    public static final String DropTablePersonas  = "DROP TABLE IF EXISTS personas";

    public static final String SelectTablePersonas = "SELECT * FROM " + Transacciones.Tabla;


}
