package org.sergio;

import org.sergio.backend.APP;
import org.sergio.backend.ElementsDataBase;
import org.sergio.backend.SeasonsDataBase;
import org.sergio.backend.UsersDataBase;

import java.util.Scanner;

/**
 * @author SERGIO GARCÍA MAYO
 */
public class Main {

    public static void main(String[] args) {
        System.out.println(" \\\\\\ FAKEFLIX //////");
        Scanner sc = new Scanner(System.in);
        UsersDataBase.newDataFile();
        ElementsDataBase.newDataFile();
        SeasonsDataBase.newDataFile();
        APP.run();
        sc.close();
    }
}
