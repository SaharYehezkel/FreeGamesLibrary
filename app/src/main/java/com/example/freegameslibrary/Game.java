package com.example.freegameslibrary;

import android.os.Parcel;
import android.os.Parcelable;

public class Game implements Parcelable {
    private int id;
    private String title;
    private String thumbnail;
    private String short_description;
    private String game_url;
    private String genre;
    private String platform;
    private String publisher;
    private String developer;
    private String release_date;
    private String freetogame_profile_url;

    // Constructor
    public Game() {
        // Default constructor required for calls to DataSnapshot.getValue(Game.class)
    }

    // Parcelable implementation
    protected Game(Parcel in) {
        id = in.readInt();
        title = in.readString();
        thumbnail = in.readString();
        short_description = in.readString();
        game_url = in.readString();
        genre = in.readString();
        platform = in.readString();
        publisher = in.readString();
        developer = in.readString();
        release_date = in.readString();
        freetogame_profile_url = in.readString();
    }

    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(thumbnail);
        dest.writeString(short_description);
        dest.writeString(game_url);
        dest.writeString(genre);
        dest.writeString(platform);
        dest.writeString(publisher);
        dest.writeString(developer);
        dest.writeString(release_date);
        dest.writeString(freetogame_profile_url);
    }


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getShortDescription() {
        return short_description;
    }

    public String getGameUrl() {
        return game_url;
    }

    public String getGenre() {
        return genre;
    }

    public String getPlatform() {
        return platform;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getDeveloper() {
        return developer;
    }

    public String getReleaseDate() {
        return release_date;
    }

    public String getFreetogameProfileUrl() {
        return freetogame_profile_url;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setShortDescription(String shortDescription) {
        this.short_description = shortDescription;
    }

    public void setGameUrl(String gameUrl) {
        this.game_url = gameUrl;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public void setReleaseDate(String releaseDate) {
        this.release_date = releaseDate;
    }

    public void setFreetogameProfileUrl(String freetogameProfileUrl) {
        this.freetogame_profile_url = freetogameProfileUrl;
    }
}
