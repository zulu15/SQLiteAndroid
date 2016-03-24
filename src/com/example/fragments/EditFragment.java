package com.example.fragments;

import com.example.model.Articulo;
import com.example.sql.R;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class EditFragment extends DialogFragment {
	private Articulo articuloSeleccionado;
	public EditFragment(Articulo art) {
		this.articuloSeleccionado=art;
	}

	// Interface para poder comunicarnos con la actividad
	public interface EditFragmentListener {
		public void onDialogPositiveClick(DialogFragment dialog);

		public void onDialogNegativeClick(DialogFragment dialog);
	}

	// Interface para poder comunicarnos con la actividad
	EditFragmentListener listener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {

			// Obtenemos la actividad a partir de la interface
			listener = (EditFragmentListener) activity;

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		Builder builder = new Builder(getActivity());
		// LayoutInflater inflater = getActivity().getLayoutInflater();
		builder.setTitle(R.string.title_edit_fragment);

		//Creo una vista ya que primero quiero setearle los datos del articulo seleccionado para modifcar
		View view = View.inflate(getActivity(), R.layout.dialog_modify, null);
		EditText editCodigo = (EditText) view.findViewById(R.id.dialog_edit_codigo);
		editCodigo.setText(articuloSeleccionado.getCodigo());
		//Lo desactivamos el codigo es primary key no quiero que nadie genere inconcistencias
		editCodigo.setEnabled(false);
		EditText editDescripcion = (EditText) view.findViewById(R.id.dialog_edit_descripcion);
		editDescripcion.setText(articuloSeleccionado.getDescripcion());
		EditText editPrecio= (EditText) view.findViewById(R.id.dialog_edit_precio);
		editPrecio.setText(articuloSeleccionado.getPrecio());
		
		// Configuramos el layout que tendra el dialogo
		builder.setView(view).setPositiveButton("Editar", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Pasamos el control a la actividad
				listener.onDialogPositiveClick(EditFragment.this);

			}
		}).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Pasamos el control a la actividad
				listener.onDialogNegativeClick(EditFragment.this);

			}
		});

		
		return builder.create();

	}

}
