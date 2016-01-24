package fr.info.antillesinfov2.business.service.android;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import fr.info.antillesinfov2.R;
import fr.info.antillesinfov2.business.model.News;

public class NewsAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater;
	private List<News> listNews;

	public NewsAdapter(Context context, List<News> listNews) {
		super();
		this.layoutInflater = LayoutInflater.from(context);
		this.listNews = listNews;
	}

	/**
	 * r�cup�rer un item de la liste en fonction de sa position
	 * 
	 * @param position
	 *            - Position de l'item � r�cup�rer
	 * @return l'item r�cup�r�
	 */
	public Object getItem(int position) {
		return listNews.get(position);
	}

	/**
	 * R�cup�rer l'identifiant d'un item de la liste en fonction de sa position
	 * (plut�t utilis� dans le cas d'une base de donn�es, mais on va l'utiliser
	 * aussi)
	 * 
	 * @param position
	 *            - Position de l'item � r�cup�rer
	 * @return l'identifiant de l'item
	 */
	public long getItemId(int position) {
		return position;
	}

	/**
	 * Explication juste en dessous.
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(
					R.layout.simple_list_antille_info_view, null);
			holder = new ViewHolder();
			holder.titre = (TextView) convertView.findViewById(R.id.titre);
			holder.category = (TextView) convertView
					.findViewById(R.id.category);
			holder.image = (ImageView) convertView.findViewById(R.id.img);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		News newsItem = (News) getItem(position);

		holder.titre.setText(newsItem.getTitle());
		holder.category.setText(newsItem.getCategory());

		if (holder.image != null && newsItem.getImageUrl()!= null) {			
			UrlImageViewHelper.setUrlDrawable(holder.image,
					newsItem.getImageUrl());
		}

		return convertView;
	}

	@Override
	public int getCount() {
		return listNews.size();
	}

	/**
	 * classe contenant les vues proc�d� moins couteux en matiere de generation
	 * des vues
	 * 
	 * @author NEBLAI
	 * 
	 */
	static class ViewHolder {
		public TextView titre;
		public TextView category;
		public ImageView image;
	}
}
