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
		Button cancelButton = (Button) findViewById(R.id.episode_dia_button2);
		EditText epiNameEditText = (EditText) findViewById(R.id.editEpisodeName);

		confirmButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addNewEpisode(((EditText) EventMakeMain.this
						.findViewById(R.id.editEpisodeName)).getText()
						.toString());
				getIntent();
				finish();
			}
		});

		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void addNewEpisode(String episodeName) {
		EventManager.getInstance().createEvent().setEventName(episodeName);
	}
}
