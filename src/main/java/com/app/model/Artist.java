package main.java.com.app.model;

public class Artist {

    private String artistName;
    private String subgenre;


    public Artist(String artistName, String subgenre) {
        this.artistName = artistName;
        this.subgenre = subgenre;
    }

    public String getArtistName() {
        return artistName;
    }
    public String getSubgenre() {
        return subgenre;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
    public void setSubgenre(String subgenre) {
        this.subgenre = subgenre;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Artist)) {
            return false;
        }
        Artist other = (Artist) obj;
        return artistName != null && artistName.equalsIgnoreCase(other.artistName);
    }

    @Override
    public int hashCode() {
        return artistName != null ? artistName.toLowerCase().hashCode() : 0;
    }

    @Override
    public String toString() {
        return artistName + (subgenre != null && !subgenre.isBlank() ? " (" + subgenre + ")" : "");
    }
}
