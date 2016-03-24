package com.example.daos;

import java.util.ArrayList;
import java.util.List;

import com.example.model.Articulo;
import com.example.sql.data.AdminSQLiteOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ArticuloDAO {
	private AdminSQLiteOpenHelper admin;
	private SQLiteDatabase bd;

	public ArticuloDAO(Context contexto) {
		admin = new AdminSQLiteOpenHelper(contexto.getApplicationContext(), "administracion", null, 1);
		bd = admin.getWritableDatabase();
	}

	public int eliminarArticulo(final String codigo) {

		return bd.delete("articulos", "codigo=" + codigo, null);
	}

	public long agregarArticulo(Articulo articulo) {
		long resultadoQuery = 0;
		if (articulo.getCodigo() != null && !articulo.getCodigo().isEmpty() && articulo.getDescripcion() != null && !articulo.getDescripcion().isEmpty() && articulo.getPrecio() != null && !articulo.getPrecio().isEmpty()) {
			ContentValues registro = new ContentValues();
			registro.put("codigo", articulo.getCodigo());
			registro.put("descripcion", articulo.getDescripcion());
			registro.put("precio", articulo.getPrecio());
			resultadoQuery = bd.insert("articulos", null, registro);
		} else {
			// Es decir no completo todos los datos
			resultadoQuery = 5;
		}
		return resultadoQuery;
	}

	public List<Articulo> listarArticulos() {
		Cursor fila = bd.rawQuery("select * from articulos ", null);
		List<Articulo> list = new ArrayList<Articulo>();
		Articulo articulo = null;
		if (fila.moveToFirst()) {
			do {
				articulo = new Articulo(fila.getString(0), fila.getString(1), fila.getString(2));
				list.add(articulo);
			} while (fila.moveToNext());

		}
		return list;
	}

	public int modificarArticulo(Articulo articulo) {
		int resultadoQuery = 0;
		if (articulo.getCodigo() != null && !articulo.getCodigo().isEmpty() && articulo.getDescripcion() != null && !articulo.getDescripcion().isEmpty() && articulo.getPrecio() != null && !articulo.getPrecio().isEmpty()) {
			ContentValues registro = new ContentValues();
			registro.put("codigo", articulo.getCodigo());
			registro.put("descripcion", articulo.getDescripcion());
			registro.put("precio", articulo.getPrecio());
			resultadoQuery = bd.update("articulos", registro, "codigo=" + articulo.getCodigo(), null);
		}
		return resultadoQuery;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		bd.close();
	}

	/*
	 * public void busquedaXdescripcion(View v) {
	 * 
	 * bd = admin.getWritableDatabase(); final String descri =
	 * editDescripcion.getText().toString(); if (descri != null &&
	 * !descri.isEmpty()) { Cursor fila = bd.rawQuery(
	 * "select codigo,precio from articulos where descripcion='" + descri + "'",
	 * null); if (fila.moveToFirst()) { editCodigo.setText(fila.getString(0));
	 * editPrecio.setText(fila.getString(1)); } else Toast.makeText(this,
	 * "No existe un artículo con dicha descripción",
	 * Toast.LENGTH_SHORT).show(); bd.close(); } else {
	 * Toast.makeText(getApplicationContext(),
	 * "Ingrese la descripción para realizar la búsqueda",
	 * Toast.LENGTH_SHORT).show(); editDescripcion.requestFocus(); }
	 * 
	 * }
	 * 
	 * public void busquedaXcodigo(View v) {
	 * 
	 * bd = admin.getWritableDatabase(); final String codigo =
	 * editCodigo.getText().toString(); if (codigo != null && !codigo.isEmpty())
	 * { Cursor fila = bd.rawQuery(
	 * "select descripcion, precio from articulos where codigo = " + codigo,
	 * null); if (fila.moveToFirst()) {
	 * editDescripcion.setText(fila.getString(0));
	 * editPrecio.setText(fila.getString(1)); } else {
	 * Toast.makeText(getApplicationContext(),
	 * "No existe el articulo con codigo: " + codigo,
	 * Toast.LENGTH_SHORT).show(); } bd.close(); } else {
	 * Toast.makeText(getApplicationContext(),
	 * "Ingrese el código para realizar la búsqueda",
	 * Toast.LENGTH_SHORT).show(); editCodigo.requestFocus(); }
	 * 
	 * }
	 */

}
