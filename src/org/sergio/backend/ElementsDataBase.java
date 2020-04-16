package org.sergio.backend;

import org.sergio.elements.Movie;
import org.sergio.elements.Serie;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ElementsDataBase {

    // ENCONTRAR RUTA DEL DIRECTORIO DOCUMENTOS DEL USUARIO

    private static String documentsDirectory = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();

    // NOMBRE DEL ARCHIVO CON LOS DATOS
    private static String elementsData = "FakeFlixElements.dat";

    // LA RUTA DEL FICHERO DE DATOS
    private static String dataPath = documentsDirectory + "\\FakeFlix\\" + elementsData;

    private static ArrayList<String> elements = new ArrayList<>();

    private static Scanner sc = new Scanner(System.in);

    private static BufferedReader reader;

    /**
     * INTENTA CREAR UN FICHERO PARA GUARDAR DATOS
     * SI EL FICHERO YA EXISTE NO LO CREA
     */
    public static void newDataFile(){

        try {
            // CREAMOS EL FICHERO EN LA RUTA DOCUMENTS
            File data = new File(dataPath);

            data.createNewFile();


        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * GUARDA INFORMACIÓN DE UN ELEMENTO
     */
    public static void saveMovieData(Movie movie){

        String category = "";
        String subcategory = "";

        if (movie.getCategory() == 1){
            category = "Ciencia ficción";
        }else if (movie.getCategory() == 2){
            category = "Animación";
            if (movie.getSubcategory() == 1){
                subcategory = "Animación tradicional";
            }else if (movie.getSubcategory() == 2){
                subcategory = "Stop Motion";
            }else if (movie.getSubcategory() == 3){
                subcategory = "Animación computarizada";
            }
        }


        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(dataPath, true));
            List<String> fileContent = new ArrayList<>(Files.readAllLines(Paths.get(dataPath), StandardCharsets.UTF_8));
            if (fileContent.size() == 0) {
                writer.append(Integer.toString(getLastElementNumber()));
                writer.append('-');
                writer.append(movie.getName());
                writer.append('-');
                writer.append(movie.getDirector());
                writer.append('-');
                writer.append(Integer.toString(movie.getDate()));
                writer.append('-');
                writer.append(Integer.toString(movie.getCopies()));
                writer.append('-');
                writer.append(movie.getType());
                writer.append('-');
                writer.append(Integer.toString(movie.getCopies()));
                writer.append('-');
                writer.append('0');
                writer.append('-');
                writer.append('0');
                writer.append('-');
                writer.append(category);
                writer.append('-');
                writer.append(subcategory);
                writer.append('\n');
                writer.append('.');
                writer.close();
                System.out.println("Elemento guardado exitosamente.");
                return;
            }

            for (int i = 0; i < fileContent.size(); i++) {

                try {
                    String[] lineSplitted = fileContent.get(i).split("-");
                    if (lineSplitted.length <= 1){
                        String newString =
                                        getLastElementNumber() +
                                        "-" +
                                        movie.getName() +
                                        "-" +
                                        movie.getDirector() +
                                        "-" +
                                        movie.getDate() +
                                        "-" +
                                        movie.getCopies() +
                                        "-" +
                                        movie.getType() +
                                        "-"+
                                        movie.getCopies() +
                                        "-" +
                                        "0" +
                                        "-" +
                                        "0" +
                                        "-" +
                                        category +
                                        "-" +
                                        subcategory +
                                        "\n" +
                                        ".";
                        fileContent.set(i, newString);
                        Files.write(Paths.get(dataPath), fileContent, StandardCharsets.UTF_8);
                        System.out.println("¡Elemento guardado exitosamente!");
                        return;
                    }

                }catch(NumberFormatException | IOException a) {
                    a.printStackTrace();
                }

            }

        }catch (IOException exc){
            exc.printStackTrace();
        }
    }

    /**
     * GUARDA INFORMACIÓN DE UN ELEMENTO
     * @param element INFORMACIÓN DEL ELEMENTO
     */
    public static void saveSerieData(Serie element){

        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(dataPath, true));
            List<String> fileContent = new ArrayList<>(Files.readAllLines(Paths.get(dataPath), StandardCharsets.UTF_8));
            if (fileContent.size() == 0) {
                writer.append(Integer.toString(getLastElementNumber()));
                writer.append('-');
                writer.append(element.getName());
                writer.append('-');
                writer.append(element.getDirector());
                writer.append('-');
                writer.append(Integer.toString(element.getDate()));
                writer.append('-');
                writer.append(Integer.toString(element.getCopies()));
                writer.append('-');
                writer.append(element.getType());
                writer.append('-');
                writer.append(Integer.toString(element.getCopies()));
                writer.append('-');
                writer.append(Integer.toString(element.getSeasons()));
                writer.append('-');
                writer.append('0');
                writer.append('-');
                writer.append('0');
                writer.append('\n');
                writer.append('.');
                writer.close();
                System.out.println("Elemento guardado exitosamente.");
                return;
            }

            for (int i = 0; i < fileContent.size(); i++) {

                try {
                    String[] lineSplitted = fileContent.get(i).split("-");
                    if (lineSplitted.length <= 1){
                        String newString =
                                getLastElementNumber() +
                                "-" +
                                element.getName() +
                                "-" +
                                element.getDirector() +
                                "-" +
                                element.getDate() +
                                "-" +
                                element.getCopies() +
                                "-" +
                                element.getType() +
                                "-" +
                                element.getSeasons() +
                                "-" +
                                element.getCopies() +
                                "-" +
                                "0" +
                                "-" +
                                "0" +
                                "\n" +
                                ".";
                        fileContent.set(i, newString);
                        Files.write(Paths.get(dataPath), fileContent, StandardCharsets.UTF_8);
                        System.out.println("¡Elemento guardado exitosamente!");
                        return;
                    }

                }catch(NumberFormatException | IOException a) {
                    a.printStackTrace();
                }

            }

        }catch (IOException exc){
            exc.printStackTrace();
        }
    }

    public static boolean canReturn(int cod){
        try{
            List<String> fileContent = new ArrayList<>(Files.readAllLines(Paths.get(dataPath), StandardCharsets.UTF_8));

            for (String s : fileContent){
                String[] line = s.split("-");
                if (Integer.parseInt(line[0]) == cod){
                    return Integer.parseInt(line[4]) != Integer.parseInt(line[6]);
                }
            }
        }catch (IOException e){
            System.out.println("Algo salió mal.");
        }
        return false;
    }

    /**
     * CONSIGUE EL NÚMERO DEL ÚLTIMO CONTACTO GUARDADO
     * @return ULTIMO NÚMERO
     */
    private static int getLastElementNumber(){

        String lastNum;
        int num = 0;

        try {

            List<String> fileContent = new ArrayList<>(Files.readAllLines(Paths.get(dataPath), StandardCharsets.UTF_8));
            for (int i = 0; i < fileContent.size(); i++){

                try {
                    String[] lineSplitted = fileContent.get(i).split("-");

                    if (fileContent.size() > 1 && lineSplitted[0] != null){
                        lastNum = lineSplitted[0];
                        num = Integer.parseInt(lastNum);
                    }
                }catch (Exception ignored){

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return num + 1;
    }

    public static ArrayList<String> getElements(){
        elements.clear();
        fillElements();
        return elements;
    }

    private static void fillElements(){

        try {
            reader = new BufferedReader(new FileReader(dataPath));
            String line = reader.readLine();
            while (line != null) {

                elements.add(line);
                // AVANZA UNA LINEA
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void substract(String cod){

        try {
            List<String> fileContent = new ArrayList<>(Files.readAllLines(Paths.get(dataPath), StandardCharsets.UTF_8));

            for (int i = 0; i < fileContent.size(); i++){

                String[] line = fileContent.get(i).split("-");

                if (line.length > 1){
                    if (line[0].equalsIgnoreCase(cod)){
                        if (Integer.parseInt(line[6]) > 0){
                            String newLine = "" + line[0] + "-" +
                                    line[1] +
                                    "-"     +
                                    line[2] +
                                    "-"     +
                                    line[3] +
                                    "-"     +
                                    (Integer.parseInt(line[4]) - 1) +
                                    "-"     +
                                    line[5] +
                                    "-"     +
                                    line[6] +
                                    "-" +
                                    line[7] +
                                    "-" +
                                    line[8] +
                                    "-" +
                                    line[9];
                            if (line[9].equalsIgnoreCase("Animación")){
                                newLine += line[10];
                            }
                            fileContent.set(i, newLine);
                        }else{
                            System.out.println("El producto no está en stock");
                        }
                    }
                }
            }
            Files.write(Paths.get(dataPath), fileContent, StandardCharsets.UTF_8);

        }catch (IOException ignored){

        }
    }

    public static void returnElement(String cod, int assessment){

        try {
            List<String> fileContent = new ArrayList<>(Files.readAllLines(Paths.get(dataPath), StandardCharsets.UTF_8));

            for (int i = 0; i < fileContent.size(); i++){

                String[] line = fileContent.get(i).split("-");

                if (line.length > 1) {
                    if (line[0].equalsIgnoreCase(cod)) {
                        String newLine = "" + line[0] + "-" +
                                line[1] +
                                "-" +
                                line[2] +
                                "-" +
                                line[3] +
                                "-" +
                                (Integer.parseInt(line[4]) + 1) +
                                "-" +
                                line[5] +
                                "-" +
                                line[6] +
                                "-" +
                                (Integer.parseInt(line[7]) + 1) +
                                "-" +
                                (Integer.parseInt(line[8]) + assessment) +
                                "-" +
                                line[9];
                        if (line[9].equalsIgnoreCase("Animación")){
                            newLine += line[10];
                        }
                        fileContent.set(i, newLine);
                        System.out.println("Se ha devuelto exitosamente.");
                    }
                }
            }
            Files.write(Paths.get(dataPath), fileContent, StandardCharsets.UTF_8);

        }catch (IOException ignored){

        }
    }

    public static void returnElement(String cod){

        try {
            List<String> fileContent = new ArrayList<>(Files.readAllLines(Paths.get(dataPath), StandardCharsets.UTF_8));

            for (int i = 0; i < fileContent.size(); i++){

                String[] line = fileContent.get(i).split("-");

                if (line.length > 1) {
                    if (line[0].equalsIgnoreCase(cod)) {
                        String newLine = "" + line[0] + "-" +
                                line[1] +
                                "-" +
                                line[2] +
                                "-" +
                                line[3] +
                                "-" +
                                (Integer.parseInt(line[4]) + 1) +
                                "-" +
                                line[5] +
                                "-" +
                                line[6] +
                                "-" +
                                line[7] +
                                "-" +
                                line[8] +
                                "-" +
                                line[9];
                        if (line[9].equalsIgnoreCase("Animación")){
                            newLine += line[10];
                        }
                        fileContent.set(i, newLine);
                    }
                }
            }
            Files.write(Paths.get(dataPath), fileContent, StandardCharsets.UTF_8);

        }catch (IOException ignored){

        }
    }


    public static void addCopies(String cod, int num){

        try {
            List<String> fileContent = new ArrayList<>(Files.readAllLines(Paths.get(dataPath), StandardCharsets.UTF_8));

            for (int i = 0; i < fileContent.size(); i++){

                String[] line = fileContent.get(i).split("-");

                if (line.length > 1) {
                    if (line[0].equalsIgnoreCase(cod)) {
                        String newLine = "" + line[0] + "-" +
                                line[1] +
                                "-" +
                                line[2] +
                                "-" +
                                line[3] +
                                "-" +
                                (Integer.parseInt(line[4]) + num) +
                                "-" +
                                line[5] +
                                "-" +
                                (Integer.parseInt(line[6]) + num) +
                                "-" +
                                line[7] +
                                "-" +
                                line[8] +
                                "-" +
                                line[9];
                        if (line[9].equalsIgnoreCase("Animación")){
                            newLine += line[10];
                        }
                        fileContent.set(i, newLine);
                        System.out.println("Se han añadido las copias exitosamente.");
                    }
                }
            }
            Files.write(Paths.get(dataPath), fileContent, StandardCharsets.UTF_8);

        }catch (IOException e){
            System.out.println("Ha habido algun problema al añadir las copias.");
        }
    }


    /**
     * BUSCA UN ELEMENTO MEDIANTE UN ID
     * @param elementID ID DEL ELEMENTO QUE SE QUIERE BUSCAR
     * @return LA INFORMACIÓN DEL ELEMENTO
     */
    public static String searchElement(String elementID){

        try {
            reader = new BufferedReader(new FileReader(dataPath));
            String line = reader.readLine();
            while (line != null) {

                String[] ultimaString = line.split("-");

                if (ultimaString[0].equals(elementID))
                    return line;

                // AVANZA UNA LINEA
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "-1";
    }

    public static void removeElement(){


        System.out.println("== ELIMINAR ELEMENTO ==");
        System.out.print("Introduce el código: ");
        String elementCode = sc.nextLine();
        try{
            // GUARDA CADA LINEA EN UN ESPACIO DEL ARRAYLIST
            List<String> fileContent = new ArrayList<>(Files.readAllLines(Paths.get(dataPath), StandardCharsets.UTF_8));

            boolean founded = false;
            // RECORRE CADA LÍNEA GUARDADA
            for (int i = 0; i < fileContent.size(); i++) {

                // SEPARA LA LÍNEA EN TROZOS
                String[] line = fileContent.get(i).split("-");

                if (founded){
                    try {
                        fileContent.set(i - 1, Arrays.toString(line).replace("[", "").replace("]","").replace(",","-").replace("- ", "-"));

                    }catch(NumberFormatException a) {
                        fileContent.set(i - 1, Arrays.toString(line).replace("[", "").replace("]","").replace(",","-").replace("- ", "-"));
                    }
                }

                // SI EL ID DE LA LINEA COINCIDE CON EL QUE SE HA INTRODUCIDO DEJA LA VARIABLE FOUNDED EN TRUE Y EMPIEZA A AÑADIR A LA CADENA LOS PARAMETROS QUE NO SON NULL
                if (line[0].equals(elementCode)) {
                    founded = true;
                }
            }
            // SE GUARDAN LOS CAMBIOS
            Files.write(Paths.get(dataPath), fileContent, StandardCharsets.UTF_8);
            System.out.println("¡Elemento eliminado correctamente!");
        }catch (Exception e){
            System.out.println("ERROR AL ELIMINAR EL ELEMENTO.");
            e.printStackTrace();
        }
    }
}
