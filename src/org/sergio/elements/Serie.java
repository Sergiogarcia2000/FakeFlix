package org.sergio.elements;

public class Serie extends Element {

    int seasons;

    public Serie(String name, String director, int date, int copies, int seasons) {
        super(name, director, date, copies, "Serie");
        this.seasons = seasons;
    }

    public int getSeasons(){
        return this.seasons;
    }

}
