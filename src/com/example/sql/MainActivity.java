package com.example.sql;

import java.util.ArrayList;
import java.util.List;

import com.example.model.Articulo;
import com.example.sql.data.AdminSQLiteOpenHelper;
import com.google.gson.Gson;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	private EditText editCodigo, editDescripcion, editPrecio;
	public static AdminSQLiteOpenHelper admin;
	private  SQLiteDatabase bd;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Obtenemos los inputs
		editCodigo = (EditText) findViewById(R.id.edit_codigo);
		editDescripcion = (EditText) findViewById(R.id.edit_descripcion);
		editPrecio = (EditText) findViewById(R.id.edit_precio);

		admin = new AdminSQLiteOpenHelper(getApplicationContext(), "administracion", null, 1);
		bd = admin.getWritableDatabase();;
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// Metodos de los botones
	public void alta(View v) {
		bd = admin.getWritableDatabase();;
		final String codigoArticulo = editCodigo.getText().toString();
		final String descripcionArticulo = editDescripcion.getText().toString();
		final String precioArticulo = editPrecio.getText().toString();
		if (codigoArticulo != null && !codigoArticulo.isEmpty() && descripcionArticulo != null && !descripcionArticulo.isEmpty() && precioArticulo != null && !precioArticulo.isEmpty()) {
			ContentValues registro = new ContentValues();
			registro.put("codigo", codigoArticulo);
			registro.put("descripcion", descripcionArticulo);
			registro.put("precio", precioArticulo);
			long i=bd.insert("articulos", null, registro);
		
			if(i!=-1){
				Toast.makeText(getApplicationContext(), "Se cargaron los datos exitosamente", Toast.LENGTH_SHORT).show();	
				limpiarCampos();
				bd.close();
			}else{
				Toast.makeText(getApplicationContext(), "El código esta repetido elija otro", Toast.LENGTH_SHORT).show();
				editCodigo.requestFocus();
			}
		
			
		}else{
			Toast.makeText(getApplicationContext(), "Ingrese todos los datos!", Toast.LENGTH_SHORT).show();
		}

	}

	public void busquedaXcodigo(View v) {
		bd = admin.getWritableDatabase();
		final String codigo = editCodigo.getText().toString();
		if (codigo != null && !codigo.isEmpty()) {
			Cursor fila = bd.rawQuery("select descripcion, precio from articulos where codigo = " + codigo, null);
			if (fila.moveToFirst()) {
				editDescripcion.setText(fila.getString(0));
				editPrecio.setText(fila.getString(1));
			} else {
				Toast.makeText(getApplicationContext(), "No existe el articulo con codigo: " + codigo, Toast.LENGTH_SHORT).show();
			}
			bd.close();
		}else{
			Toast.makeText(getApplicationContext(), "Ingrese el código para realizar la búsqueda", Toast.LENGTH_SHORT).show();
			editCodigo.requestFocus();
		}

	}

	public void busquedaXdescripcion(View v) {
		bd = admin.getWritableDatabase();
		final String descri = editDescripcion.getText().toString();
		if (descri != null && !descri.isEmpty()) {
			Cursor fila = bd.rawQuery("select codigo,precio from articulos where descripcion='" + descri + "'", null);
			if (fila.moveToFirst()) {
				editCodigo.setText(fila.getString(0));
				editPrecio.setText(fila.getString(1));
			} else
				Toast.makeText(this, "No existe un artículo con dicha descripción", Toast.LENGTH_SHORT).show();
			bd.close();
		}else{
			Toast.makeText(getApplicationContext(), "Ingrese la descripción para realizar la búsqueda", Toast.LENGTH_SHORT).show();
			editDescripcion.requestFocus();
		}

	}

	public void bajaXcodigo(View v) {
		bd = admin.getWritableDatabase();
		final String codigo = editCodigo.getText().toString();
		if (!codigo.isEmpty() && codigo != null) {
			int cant = bd.delete("articulos", "codigo=" + codigo, null);
			bd.close();
			limpiarCampos();
			if (cant == 1)
				Toast.makeText(this, "Se borró el artículo con código " + codigo, Toast.LENGTH_SHORT).show();
			else
				Toast.makeText(this, "No existe un artículo con código " + codigo, Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(getApplicationContext(), "Ingrese el codigo para eliminar el artículo", Toast.LENGTH_SHORT).show();
			editCodigo.requestFocus();
		}


	}

	public void listar(View v) {
		bd = admin.getWritableDatabase();
		new AsyncTask<Void, Void, Void>() {

			String listOfArticles;

			@Override
			protected Void doInBackground(Void... params) {

				Cursor fila = bd.rawQuery("select * from articulos ", null);
				List<Articulo> list = new ArrayList<Articulo>();
				Articulo articulo = null;
				if (fila.moveToFirst()) {
					do {
						articulo = new Articulo(fila.getString(0), fila.getString(1), fila.getString(2));
						list.add(articulo);
					} while (fila.moveToNext());

				}
				listOfArticles = new Gson().toJson(list);

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				Intent i = new Intent(getApplicationContext(), ArticuloActivity.class);
				i.putExtra("articulos", listOfArticles);
				startActivity(i);

			}

		}.execute();

	}

	private void limpiarCampos() {
		editCodigo.setText("");
		editDescripcion.setText("");
		editPrecio.setText("");

	}
}
