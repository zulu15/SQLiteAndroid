package com.example.sql;


import java.util.Arrays;
import java.util.List;

import com.example.model.Articulo;
import com.example.sql.data.CustomAdapter;
import com.google.gson.Gson;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ArticuloActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_articulo);

		String listaSerializada = getIntent().getExtras().getString("articulos");
		Articulo[] ArrayArticulos = new Gson().fromJson(listaSerializada, Articulo[].class);
		List<Articulo> lista = (Arrays.asList(ArrayArticulos));

		ListView lis = (ListView)findViewById(R.id.list);
		CustomAdapter adapter = new CustomAdapter(this, lista);
		lis.setAdapter(adapter);

	}

}
