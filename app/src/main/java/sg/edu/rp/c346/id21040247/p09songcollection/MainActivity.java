package sg.edu.rp.c346.id21040247.p09songcollection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //initialisation
    Button btnInsert, btnShowList;

    TextView tvSongTitle, tvSingers, tvStars, tvYear;
    EditText etSongTitle, etSingers, etYear, etFilterTitle, etFilterSingers;
    RatingBar rtbStars;
    Spinner spFilterYear, spRating;


    //arraylist, arrayadapter initialisation
    ArrayList<String> alYears = new ArrayList<>();
    ArrayAdapter<String> aaYears;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //View Linking
        btnInsert = findViewById(R.id.btnInsert);
        btnShowList = findViewById(R.id.btnShowList);
        tvSongTitle = findViewById(R.id.tvSongTitle);
        tvSingers = findViewById(R.id.tvSingers);
        tvStars = findViewById(R.id.tvStars);
        tvYear = findViewById(R.id.tvYear);
        etSongTitle = findViewById(R.id.etSongTitle);
        etSingers = findViewById(R.id.etSingers);
        etYear = findViewById(R.id.etYear);
        etFilterTitle = findViewById(R.id.etFilterTitle);
        etFilterSingers = findViewById(R.id.etFilterSingers);
        spRating = findViewById(R.id.spRating);
        rtbStars = findViewById(R.id.rtbStars);
        spFilterYear = findViewById(R.id.spFilterYear);

        //spinner
        DBHelper dbh = new DBHelper(MainActivity.this);
        alYears.addAll(dbh.showYears());

        aaYears = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, alYears);
        aaYears.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spFilterYear.setAdapter(aaYears);
        dbh.close();

        /*
         condition 1 - no empty entry for title and singers
         condition 2 - no empty entry for year, at least 4 digits
         condition 3 - between year 1000 to 2022, cannot be negative so e.g. -2000 not allowed
         */
        //insert
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = 0;

                String title = etSongTitle.getText().toString();
                String singers = etSingers.getText().toString();
                String stringYear = etYear.getText().toString();
                float stars = rtbStars.getRating();

                //condition 2
                if (stringYear.trim().length() == 4){
                    year = Integer.parseInt(stringYear);
                }else{
                    Toast.makeText(MainActivity.this, "Year at least 4 digits",
                            Toast.LENGTH_SHORT).show();
                }

                //condition 1
                if (title.trim().length() > 0 && singers.trim().length() > 0 &&
                        //condition 3
                        year <= 2022 && year > -1 ){
                        DBHelper dbh = new DBHelper(MainActivity.this);
                        long inserted_id = dbh.insertSong(title, singers, year, stars);
                    if (inserted_id != -1){
                        Toast.makeText(MainActivity.this, "Insert Successful",
                                Toast.LENGTH_SHORT).show();

                        //resets the fields if inserted
                        etSongTitle.setText("");
                        etSingers.setText("");
                        etYear.setText("");
                        rtbStars.setRating(0);
                    }
                    dbh.close();
                } else{
                    Toast.makeText(MainActivity.this, "Missing/Wrong Inputs",
                            Toast.LENGTH_SHORT).show();
                }

                //update spinner filter when inserted
                DBHelper dbh = new DBHelper(MainActivity.this);
                alYears.clear();
                alYears.addAll(dbh.showYears());
                aaYears.notifyDataSetChanged();
                spFilterYear.setAdapter(aaYears);
                dbh.close();
            }
        });

        //show
        btnShowList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Songs> songCollection = new ArrayList<>();

                String title = etFilterTitle.getText().toString();
                String singers = etFilterSingers.getText().toString();
                String year = spFilterYear.getSelectedItem().toString();
                String stars = spRating.getSelectedItem().toString();

                Intent intent = new Intent(MainActivity.this, ViewActivity.class);
                DBHelper dbh = new DBHelper(MainActivity.this);

                songCollection.addAll(dbh.showSongs(title, singers, year, stars+".0", 0));

                intent.putExtra("songCollection", songCollection);
                intent.putExtra("title", title);
                intent.putExtra("singers", singers);
                intent.putExtra("year", year);
                intent.putExtra("stars", stars);

                etFilterTitle.setText("");
                etFilterSingers.setText("");
                spFilterYear.setSelection(0);
                spRating.setSelection(0);

                dbh.close();
                startActivity(intent);
            }
        });

    }
}