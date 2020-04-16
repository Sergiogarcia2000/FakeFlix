package org.sergio;

import org.sergio.backend.APP;
import org.sergio.backend.ElementsDataBase;
import org.sergio.backend.UsersDataBase;

public class Main {

    public static void main(String[] args) {

        UsersDataBase.newDataFile();
        ElementsDataBase.newDataFile();

        /*
        ArrayList<String> users = ElementsDataBase.getElements();

        for (String item : users){
            System.out.println(item);
        }

        */

        APP.run();





    }
}
