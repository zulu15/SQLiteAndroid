package com.example.sql.data;

import java.util.List;

import com.example.model.Articulo;

import com.example.sql.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<Articulo>{

	private List<Articulo> articulos;
	LayoutInflater inflater;
	public CustomAdapter(Context context, List<Articulo> objects) {
		super(context,-1, objects);
		this.articulos=objects;
		this.inflater=LayoutInflater.from(getContext());

	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		Articulo currentArticle = articulos.get(position);
		if(convertView==null){
			//CREO MI LAYOUT PERSONALIZADO PARA QUE MI LISTA SE VEA COMO YO QUIERO
			convertView = inflater.inflate(R.layout.list_item, null);
			holder = new ViewHolder();
			holder.codigo=(TextView)convertView.findViewById(R.id.art_codigo);
			holder.descripcion=(TextView)convertView.findViewById(R.id.art_descripcion);
			holder.precio=(TextView)convertView.findViewById(R.id.art_precio);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		holder.codigo.setText(currentArticle.getCodigo());
		holder.descripcion.setText(currentArticle.getDescripcion());
		holder.precio.setText("$"+currentArticle.getPrecio());
		
		return convertView;
	}
	
	private static class ViewHolder {
		
		public TextView codigo,descripcion,precio;
		
	}
}
