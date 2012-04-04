package skp.collarge.main;

import skp.collarge.R;
import skp.collarge.event.EventManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

public class EventMakeMain extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.episode_make);

		Button confirmButton = (Button) findViewById(R.id.episode_dia_button1);
		Button cancleButton = (Button) findViewById(R.id.episode_dia_button2);
		final EditText epiNameEditText = (EditText) findViewById(R.id.editEpisodeName);

		confirmButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addNewEpisode(epiNameEditText.getText().toString());
			}
		});

		cancleButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(EventMakeMain.this, CollargeMain.class);
				startActivity(intent);
			}
		});
	}

	private void addNewEpisode(String epsiodeName) {
		EventManager.getInstance().createEvent();
		GridView gridview = (GridView) findViewById(R.id.gridview_collarge);
		gridview.setAdapter(new ImageAdapter(this, epsiodeName));
	}
}
