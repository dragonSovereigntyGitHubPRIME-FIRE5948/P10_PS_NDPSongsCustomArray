package sg.edu.rp.c346.id21040247.p09songcollection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter {

    Context parent_context;
    int layout_id;
    ArrayList<Songs> alSongs;

    public CustomAdapter(Context context, int resource, ArrayList<Songs> objects) {
        super(context, resource, objects);
        parent_context = context;
        layout_id = resource;
        alSongs = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtain the LayoutInflater object
        LayoutInflater inflater = (LayoutInflater)
                parent_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // "Inflate" the View for each row
        View rowView = inflater.inflate(layout_id, parent, false);

        // Obtain the UI components and do the necessary binding
        TextView tvTitle = rowView.findViewById(R.id.tvTitle);
        TextView tvSinger = rowView.findViewById(R.id.tvSinger);
        TextView tvYear = rowView.findViewById(R.id.tvYear);
        RatingBar rtbStars = rowView.findViewById(R.id.rtbStars);

        // Obtain the Android Version information based on the position
        Songs songObject = alSongs.get(position);
        Float rating = songObject.getStars();

        // Set values to the TextView to display the corresponding information
        tvTitle.setText(songObject.getTitle());
        tvYear.setText(" -  " + songObject.getYear());
        tvSinger.setText(songObject.getSinger());
        rtbStars.setNumStars(Math.round(rating));
        rtbStars.setRating(rating);
        return rowView;
    }


}
