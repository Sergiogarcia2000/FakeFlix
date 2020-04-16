package org.sergio.elements;

public class Movie extends Element {

    private int category;
    private int subcategory;

    public Movie(String name, String director, int date, int copies, int type, int subtype) {
        super(name, director, date, copies, "Movie");
        this.category = type;
        this.subcategory = subtype;
    }

    public int getCategory(){
        return this.category;
    }

    public int getSubcategory(){
        return this.subcategory;
    }

}
