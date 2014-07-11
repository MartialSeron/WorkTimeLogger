package mseron.worktimelogger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mseron.worktimelogger.R;

public class OnGoingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_going);


        ListView listView = (ListView) findViewById(R.id.pauses_list);
        ArrayList<HashMap<String, String>> pausesList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;



        map = new HashMap<String, String>();
        map.put("label", "Pause cafe");
        map.put("time", "00:10:24");
        pausesList.add(map);

        map = new HashMap<String, String>();
        map.put("label", "Pause cafe");
        map.put("time", "00:09:16");
        pausesList.add(map);

        map = new HashMap<String, String>();
        map.put("label", "Pause dejeuner");
        map.put("time", "00:45:19");
        pausesList.add(map);

        map = new HashMap<String, String>();
        map.put("label", "Pause cafe");
        map.put("time", "00:05:10");
        pausesList.add(map);

        map = new HashMap<String, String>();
        map.put("label", "Pause cafe");
        map.put("time", "00:06:23");
        pausesList.add(map);

        SimpleAdapter adapter = new SimpleAdapter(this, pausesList, R.layout.pauses_list_item,
                new String[]{"label", "time"}, new int[]{R.id.pause_item_label, R.id.pause_item_time});

        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.on_going, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_stop_working_day:
                this.finishActivity(0);
                return true;
            case R.id.action_new_pause:
                PauseTypeDialogFragment pauseDialog = new PauseTypeDialogFragment();
                pauseDialog.show(getFragmentManager(), "pause_types");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
