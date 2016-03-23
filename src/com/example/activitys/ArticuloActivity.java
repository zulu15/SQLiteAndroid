package com.example.activitys;

import java.util.List;

import com.example.daos.ArticuloDAO;
import com.example.fragments.EditFragment;
import com.example.fragments.EditFragment.EditFragmentListener;
import com.example.model.Articulo;
import com.example.sql.R;
import com.example.sql.data.CustomAdapter;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ArticuloActivity extends FragmentActivity implements EditFragmentListener {

	private ArticuloDAO dao;
	private CustomAdapter adapter;
	private ListView lis;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_articulo);

		lis = (ListView) findViewById(R.id.list);
		dao = new ArticuloDAO(getApplicationContext());

		adapter = new CustomAdapter(getApplicationContext(), dao.listarArticulos());
		lis.setAdapter(adapter);
		// le indico el listener para un largo click a la lista
		registerForContextMenu(lis);

	}

	// Creamos el memu
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		// Si se seleccion la lista
		if (v.getId() == R.id.list) {
			// Obtenemos la posicion que se toco con el objeto info ya podemos
			// obtener la posicion
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			int positionElement = info.position;
			// Le comolocamos de titulo al menu la descripcion del artículo
			List<Articulo> articulos = dao.listarArticulos();
			menu.setHeaderTitle("Opciones");
			String[] menuItems = getResources().getStringArray(R.array.menu_options);
			for (int i = 0; i < menuItems.length; i++) {
				menu.add(Menu.NONE, i, i, menuItems[i]);
			}

		} else {
			Toast.makeText(getApplicationContext(), "No se selecciono la lista", Toast.LENGTH_SHORT).show();
		}
	}

	// Registramos el click en los items
	@Override
	public boolean onContextItemSelected(MenuItem item) {

		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		int menuItemIndex = item.getItemId();
		// Obtengo los items del menu Editar Eliminar
		String[] menuItems = getResources().getStringArray(R.array.menu_options);

		// Obtengo el item seleccionado
		final String menuItemName = menuItems[menuItemIndex];
		// Obtengo el codigo del articulo
		List<Articulo> articulos = dao.listarArticulos();
		final String codigoArticulo = articulos.get(info.position).getCodigo();

		switch (menuItemName) {

		case "Editar":
			// Instancio y muestro el dialogo pasanddole el articulo que se
			// quiere editar
			DialogFragment dialog = new EditFragment(articulos.get(info.position));
			dialog.show(getSupportFragmentManager(), "tag");
			return true;

		case "Eliminar":
			dao.eliminarArticulo(codigoArticulo);
			actualizarUI();
			return true;

		}

		return true;

	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {

		Dialog dialogo = dialog.getDialog();

		EditText editCodigo = (EditText) dialogo.findViewById(R.id.dialog_edit_codigo);
		EditText editDescripcion = (EditText) dialogo.findViewById(R.id.dialog_edit_descripcion);
		EditText editPrecio = (EditText) dialogo.findViewById(R.id.dialog_edit_precio);

		Articulo modificado =new Articulo(editCodigo.getText().toString(), editDescripcion.getText().toString(), editPrecio.getText().toString());
		int resultado=dao.modificarArticulo(modificado);
		if (resultado == 0) {
			DialogFragment dialogoPersistente = new EditFragment(modificado);
			dialogoPersistente.show(getSupportFragmentManager(), "tag");
			Toast.makeText(getApplicationContext(), "Complete todos los campos por favor." , Toast.LENGTH_SHORT).show();
			
		} else {
			Toast.makeText(getApplicationContext(), "Se actualizo el artículo correctamente." , Toast.LENGTH_LONG).show();
			actualizarUI();
	}

	}

	private void actualizarUI() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				adapter.refreshEvents(dao.listarArticulos());
			}
		});
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		// Cierro el dialogo si el usuario se arrepintio
		dialog.dismiss();

	}
}
