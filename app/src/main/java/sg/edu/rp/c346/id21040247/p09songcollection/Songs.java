package sg.edu.rp.c346.id21040247.p09songcollection;

import java.io.Serializable;

public class Songs implements Serializable {

    //initialisation
    private int _id;
    private String title;
    private String singer;
    private int year;
    private float stars;

    //constructor
    public Songs(int _id, String title, String singer, int year, float stars) {
        this._id = _id;
        this.title = title;
        this.singer = singer;
        this.year = year;
        this.stars = stars;
    }

    //getter, setter
    public int get_id() {return _id;}

    public String getTitle() {
        return title;
    }

    public String getSinger() {
        return singer;
    }

    public int getYear() {
        return year;
    }

    public float getStars() {
        return stars;
    }

    //toString format
    @Override
    public String toString() {

        String rating = "";
        int check = Math.round(stars);

        switch (check){
            case 0: rating = "NIL";
                    break;
            case 1: rating = "*";
                    break;
            case 2: rating = "**";
                    break;
            case 3: rating = "***";
                    break;
            case 4: rating = "****";
                    break;
            case 5: rating = "*****";
                    break;
        }

        return "\n" + "Song Title: " + title + " - " + year + "\n" +
                "Singer: " + singer + "\n" +
                "Rating: " + rating + "\n";
    }
}
