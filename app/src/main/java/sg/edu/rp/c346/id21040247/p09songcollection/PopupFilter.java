package sg.edu.rp.c346.id21040247.p09songcollection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class PopupFilter extends AppCompatActivity {

    Spinner spStars, spYears;
    EditText etSongTitle, etSingers;

    ArrayList<String> alYears;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_filter);

        //View Linking
        etSongTitle = findViewById(R.id.etSongTitle);
        etSingers = findViewById(R.id.etSingers);
        spYears = findViewById(R.id.spYears);
        spStars = findViewById(R.id.spStars);

        final Button btnFilter = (Button) findViewById(R.id.btnFilter);

        ArrayAdapter<CharSequence> spinnerRating = ArrayAdapter.createFromResource(this, R.array.rating, android.R.layout.simple_spinner_item);
        spinnerRating.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spStars.setAdapter(spinnerRating);

        //spinner
        alYears = new ArrayList<>();
        alYears.add("okay");

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Songs> songCollection = new ArrayList<>();

                String title = etSongTitle.getText().toString();
                String singers = etSingers.getText().toString();
                String year = spYears.getSelectedItem().toString();
                String stars = spStars.getSelectedItem().toString();

                Intent intent = new Intent(PopupFilter.this, ViewActivity.class);
                DBHelper dbh = new DBHelper(PopupFilter.this);

                songCollection.addAll(dbh.showSongs(title, singers, year, stars+".0", 0));

                intent.putExtra("songCollectionPU", songCollection);
                intent.putExtra("titlePU", title);
                intent.putExtra("singersPU", singers);
                intent.putExtra("yearPU", year);
                intent.putExtra("starsPU", stars);

                dbh.close();
                startActivity(intent);
                finish();
            }
        });



//        DBHelper dbh = new DBHelper(MainActivity.this);
//        alYears.addAll(dbh.showYears());
        //        dbh.close();
    }
    }