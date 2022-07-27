package sg.edu.rp.c346.id21040247.p09songcollection;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.database.sqlite.SQLiteDatabase;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ViewActivity extends AppCompatActivity {

    //INITIALISATION arraylist, arrayadapter
    ArrayList<Songs> alSong = new ArrayList<>();
    ArrayList<Songs> alSortedSong = new ArrayList<>();
    ArrayList<Songs> alRefreshSong = new ArrayList<>();
    ArrayAdapter<Songs> aaSong;
    ArrayAdapter<Songs> aaSortedSong;

    CustomAdapter caSongs;

    ListView lvSongs;

    TextView tvTitle;
    TextView tvFilterTitle;
    Spinner spSort;

    //REFRESHES listview upon re-entry into update/delete completion in EditActivity
    @Override
    protected void onResume() {
        super.onResume();

        Intent i = getIntent();
        String intentTitle = i.getStringExtra("title");
        String intentSingers = i.getStringExtra("singers");
        String intentYear = i.getStringExtra("year");
        String intentStars = i.getStringExtra("stars");

        DBHelper dbh = new DBHelper(ViewActivity.this);
        alRefreshSong = dbh.showSongs(intentTitle, intentSingers, intentYear, intentStars, 0);
        aaSong = new CustomAdapter(ViewActivity.this, R.layout.row, alRefreshSong);
        lvSongs.setAdapter(aaSong);
        dbh.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        lvSongs = findViewById(R.id.lvSongs);
        final Button btnFilter = (Button) findViewById(R.id.btnFilter);
        tvTitle = findViewById(R.id.tvTitle);
        tvFilterTitle = findViewById(R.id.tvFilterTitle);
        spSort = findViewById(R.id.spSort);

        String title = "";
        String filter = "";

        Intent i = getIntent();
        String intentTitle = i.getStringExtra("title");
        String intentSingers = i.getStringExtra("singers");
        String intentYear = i.getStringExtra("year");
        String intentStars = i.getStringExtra("stars");

        if (intentTitle.trim().length() > 0) {
            title = "Filtered Show Songs";
            filter += "Song Title Filter: " + intentTitle + "\n";
            tvTitle.setText(title);
            tvFilterTitle.setVisibility(View.VISIBLE);
            tvFilterTitle.setText(filter);
        }
        if (intentSingers.trim().length() > 0) {
            if (title == "") {
                title = "Filtered Show Songs" + "\n";
            }
            filter += "Song Singer Filter: " + intentSingers;
            tvTitle.setText(title);
            tvFilterTitle.setVisibility(View.VISIBLE);
            tvFilterTitle.setText(filter);
        }
        if (intentYear.trim().length() > 0) {
            if (title == "") {
                title = "Filtered Show Songs";
            }
            filter += "Song Year Filter: " + intentYear + "\n";
            tvTitle.setText(title);
            tvFilterTitle.setVisibility(View.VISIBLE);
            tvFilterTitle.setText(filter);
        }
        if (intentStars.trim().length() > 0) {
            if (title == "") {
                title = "Filtered Show Songs";
            }
            filter += "Song Rating Filter: " + intentStars + "\n";
            tvTitle.setText(title);
            tvFilterTitle.setVisibility(View.VISIBLE);
            tvFilterTitle.setText(filter);
        }

        Intent al = getIntent();
        alSong.clear();
        alSong = (ArrayList<Songs>) al.getSerializableExtra("songCollection");


        caSongs = new CustomAdapter(this, R.layout.row, alSong);
        lvSongs.setAdapter(caSongs);
//        aaSong = new ArrayAdapter<>(ViewActivity.this, android.R.layout.simple_list_item_1, alSong);
//        lvSongs.setAdapter(aaSong);

        //OPEN individual items (new activity)
        lvSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long identity) {
                Songs getSong = alSong.get(position);
                Intent i = new Intent(ViewActivity.this, EditActivity.class);
                i.putExtra("getSong", getSong);
                startActivity(i);
            }
        });

        spSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int sortID = position;
                switch (sortID) {
                    case 0:
                        sortID = 0;
                        break;
                    case 1:
                        sortID = 1;
                        break;
                    case 2:
                        sortID = 2;
                        break;
                    case 3:
                        sortID = 3;
                        break;
                    case 4:
                        sortID = 4;
                        break;
                    case 5:
                        sortID = 5;
                        break;
                    case 6:
                        sortID = 6;
                        break;
                    case 7:
                        sortID = 7;
                        break;
                    case 8:
                        sortID = 8;
                        break;
                }
                DBHelper dbh = new DBHelper(ViewActivity.this);
                alSortedSong.clear();
                alSortedSong = dbh.showSongs(intentTitle, intentSingers, intentYear, intentStars, sortID);
                aaSortedSong = new CustomAdapter(ViewActivity.this, R.layout.row, alSortedSong);
                lvSongs.setAdapter(aaSortedSong);
                dbh.close();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView){
            }
        });

        //ignore
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonShowPopupWindowClick(btnFilter);
            }

        });
    }


    public void onButtonShowPopupWindowClick(View view) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.activity_popup_filter, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }


}

        //show 5 star songs



