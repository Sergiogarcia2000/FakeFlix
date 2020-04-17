package org.sergio.elements;

public class Element {

    protected String name;
    protected String director;
    protected int date;
    protected int copies;
    protected String type;

    protected Element(String name, String director, int date, int copies, String type){
        this.name = name;
        this.director = director;
        this.date = date;
        this.copies = copies;
        this.type = type;
    }

    public String getType(){
        return this.type;
    }

    public String getName(){
        return name;
    }

    public String getDirector(){
        return director;
    }

    public int getDate(){
        return date;
    }

    public int getCopies(){
        return copies;
    }

}
