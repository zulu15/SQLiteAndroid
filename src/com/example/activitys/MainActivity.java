package com.example.activitys;

import java.util.List;

import com.example.daos.ArticuloDAO;
import com.example.model.Articulo;
import com.example.sql.R;

import com.google.gson.Gson;

import android.app.Activity;
import android.app.ProgressDialog;

import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	private EditText editCodigo, editDescripcion, editPrecio;
	private ArticuloDAO dao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Obtenemos los inputs
		editCodigo = (EditText) findViewById(R.id.edit_codigo);
		editDescripcion = (EditText) findViewById(R.id.edit_descripcion);
		editPrecio = (EditText) findViewById(R.id.edit_precio);

		dao = new ArticuloDAO(getApplicationContext());

	}

	public void alta(View v) {

		long result = dao.agregarArticulo(new Articulo(editCodigo.getText().toString(), editDescripcion.getText().toString(), editPrecio.getText().toString()));
		if (result == -1) {
			Toast.makeText(getApplicationContext(), "El código esta repetido elija otro", Toast.LENGTH_SHORT).show();
			editCodigo.requestFocus();
		} else if (result == 5) {
			Toast.makeText(getApplicationContext(), "Ingrese todos los datos!", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getApplicationContext(), "Se cargaron los datos exitosamente", Toast.LENGTH_SHORT).show();
			limpiarCampos();

		}

	}

	public void bajaXcodigo(View v) {

		final String codigo = editCodigo.getText().toString();
		if (!codigo.isEmpty() && codigo != null) {
			int cant = dao.eliminarArticulo(codigo);
			limpiarCampos();
			if (cant == 1)
				Toast.makeText(this, "Se borró el artículo con código " + codigo, Toast.LENGTH_SHORT).show();
			else
				Toast.makeText(this, "No existe un artículo con código " + codigo, Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getApplicationContext(), "Ingrese el codigo para eliminar el artículo", Toast.LENGTH_SHORT).show();
			editCodigo.requestFocus();
		}

	}

	public void listar(View v) {

		new AsyncTask<Void, Void, Void>() {

			String listOfArticles;
			private ProgressDialog progressDialog;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				progressDialog = new ProgressDialog(MainActivity.this);
				progressDialog.setMessage("Cargando los artículos...");
				progressDialog.show();
			}

			@Override
			protected Void doInBackground(Void... params) {

				List<Articulo> list = dao.listarArticulos();
				listOfArticles = new Gson().toJson(list);

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				progressDialog.dismiss();
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
