package fr.info.antillesinfov2;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import fr.info.antillesinfov2.business.model.News;

public class DetailInfoActivity extends Activity {

	private TextView titreInfo;
	private TextView descriptionInfo;
	private ImageView imageInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_info);
		// Show the Up button in the action bar.
		// Get the message from the intent
		Intent intent = getIntent();
		News news = (News) intent
				.getSerializableExtra(MainActivity.EXTRA_MESSAGE);

		// Create the text view
		titreInfo = (TextView) findViewById(R.id.text_view_detail_info);
		descriptionInfo = (TextView) findViewById(R.id.text_view_description_info);
		imageInfo = (ImageView) findViewById(R.id.img_detail_info);
		titreInfo.setText(news.getTitle());
		descriptionInfo.setText(news.getDescription());
		UrlImageViewHelper.setUrlDrawable(imageInfo, news.getImageUrl());

		// Set the text view as the activity layout
		// setContentView(textView);
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail_info, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
