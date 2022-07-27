package sg.edu.rp.c346.id21040247.p09songcollection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class EditActivity extends AppCompatActivity {

    Button btnUpdate, btnDelete, btnReturn;
    //TextView tvSongTitle, tvSingers, tvStars, tvYear;
    EditText etUpdateSongTitle, etUpdateSingers, etUpdateYear, etID;
    RatingBar rtbUpdateStars;
    Songs item;

    //INITIALISATION arraylist, arrayadapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        btnReturn = findViewById(R.id.btnReturn);
        etUpdateSongTitle = findViewById(R.id.etUpdateSongTitle);
        etUpdateSingers = findViewById(R.id.etUpdateSingers);
        etUpdateYear = findViewById(R.id.etUpdateYear);
        etID = findViewById(R.id.etID);
        rtbUpdateStars = findViewById(R.id.rtbUpdateStars);



        Intent i = getIntent();
        Songs song = (Songs) i.getSerializableExtra("getSong");

        int songID = song.get_id();
        String songTitle = song.getTitle();
        String songSingers = song.getSinger();
        int songYear = song.getYear();
        float songStars = song.getStars();

        etID.setText(String.valueOf(songID));
        etUpdateSongTitle.setText(String.valueOf(songTitle));
        etUpdateSingers.setText(String.valueOf(songSingers));
        etUpdateYear.setText(String.valueOf(songYear));
        rtbUpdateStars.setRating(songStars);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etUpdateSongTitle.getText().toString();
                String singers = etUpdateSingers.getText().toString();
                int year = Integer.parseInt(etUpdateYear.getText().toString());
                float stars = Float.parseFloat(String.valueOf(rtbUpdateStars.getRating()));

                DBHelper dbh = new DBHelper(EditActivity.this);
                int result = dbh.updateSong(songID, title, singers, year, stars);
                dbh.close();

                if (result == 1){
                    Toast.makeText(EditActivity.this, "Update Successful",
                            Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(EditActivity.this, "No Updates Were Made",
                            Toast.LENGTH_SHORT).show();
                }
                etUpdateSongTitle.setText("");
                etUpdateSingers.setText("");
                etUpdateYear.setText("");
                rtbUpdateStars.setRating(0);
                dbh.close();
                finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper dbh = new DBHelper(EditActivity.this);

                int result = dbh.deleteSong(songID);

                if (result == 1){
                    Toast.makeText(EditActivity.this, "Delete Successful",
                            Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(EditActivity.this, "No Deletes Were Made",
                            Toast.LENGTH_SHORT).show();
                }
                etUpdateSongTitle.setText("");
                etUpdateSingers.setText("");
                etUpdateYear.setText("");
                rtbUpdateStars.setRating(0);
                dbh.close();
                finish();
            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}