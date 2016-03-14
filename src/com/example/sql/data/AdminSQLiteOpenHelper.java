package com.example.sql.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper{
	/*
	 * Hemos codificado en el método onCreate la creación de la tabla articulos con los campos 
	 * codigo (que es entero y clave primaria), descripcion que es de tipo texto y precio es un valor real.
	 *  El método onCreate se ejecutará una única vez 
	 *  (Eventualmente si uno quiere modificar la estructura de la tabla debemos hacerlo en el método onUpgrade).
	 */

	public AdminSQLiteOpenHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		 db.execSQL("create table articulos(codigo int primary key,descripcion text,precio real)");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//Todas las operaciones para actualizar nuestra base de datos!
		
	}

}
