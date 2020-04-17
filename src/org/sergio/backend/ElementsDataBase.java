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

/**
 * @author SERGIO GARCÍA MAYO
 * ESTA CLASE REALIZA PRÁCTICAMENTE LOS MISMOS ALGORITMOS QUE LAS OTRAS CLASES QUE GESTIONAN LOS DATOS
 */
public class ElementsDataBase {

    // ENCONTRAR RUTA DEL DIRECTORIO DOCUMENTOS DEL USUARIO

    private static String documentsDirectory = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();

    // NOMBRE DEL ARCHIVO CON LOS DATOS
    private static String elementsData = "FakeFlixElements.dat";

    // LA RUTA DEL FICHERO DE DATOS
    private static String dataPath = documentsDirectory + "\\FakeFlix\\" + elementsData;

    private static ArrayList<String> elements = new ArrayList<>();

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
     * GUARDA INFORMACIÓN DE UNA PELÍCULA
     * UTILIZO UN MÉTODO DE LA CLASE FILES EL CUAL GUARDA DENTRO DE CADA POSICION DE UNA LISTA EN FORMATO ARRAYLIST CADA LINEA DE UN FICHERO
     * CADA VEZ QUE GUARDO UN ELEMENTO HAGO UN SALTO DE LÍNEA Y AÑADO UN PUNTO, AUNQUE ESTO QUEDE MUY FEO MÁS TARDE ME RESUELVE MUCHOS PROBLEMAS PARA ELMINIAR ELEMENTOS...
     * LA PRIMERA VEZ QUE SE GUARDAA UN CONTACTO DETECTA QUE EL FICHERO ESTÁ VACÍO LO UNICO QUE HACE ES AÑADIR UNA NUEVA LINEA A LA LISTA VACÍA
     * EL RESTO DE VECES QUE SE GUARDA DETECTA DONDE ESTÁ EL PRIMER PUNTO DISPONIBLE Y LO SOBREESCRIBE
     * @param movie EL OBJETO QUE VOY A UTILIZAR AUNQUE REALMENTE NO ES NECESARIO CREAR UN USUARIO PARA ESTO
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
            List<String> fileContent = new ArrayList<>(Files.readAllLines(Paths.get(dataPath), StandardCharsets.UTF_8));
            String newString = getLastElementNumber() + "-" + movie.getName() + "-" + movie.getDirector() + "-" + movie.getDate() + "-" + movie.getCopies() +
                               "-" + movie.getType() + "-"+ movie.getCopies() + "-0-0-" + "Disponible" + "-" + category + "-" + subcategory + "\n" + ".";

            if (fileContent.isEmpty()){
                fileContent.add(newString);
                Files.write(Paths.get(dataPath), fileContent, StandardCharsets.UTF_8);
            }else{
                for (int i = 0; i < fileContent.size(); i++){
                    if (fileContent.get(i).length() <= 2){
                        fileContent.set(i,newString);
                        Files.write(Paths.get(dataPath), fileContent, StandardCharsets.UTF_8);
                        System.out.println("¡Elemento guardado exitosamente!");
                        return;
                    }
                }
            }
        }catch (IOException exc){
            exc.printStackTrace();
        }
    }

    /**
     * GUARDA INFORMACIÓN DE UNA SERIE
     * UTILIZO UN MÉTODO DE LA CLASE FILES EL CUAL GUARDA DENTRO DE CADA POSICION DE UNA LISTA EN FORMATO ARRAYLIST CADA LINEA DE UN FICHERO
     * CADA VEZ QUE GUARDO UN ELEMENTO HAGO UN SALTO DE LÍNEA Y AÑADO UN PUNTO, AUNQUE ESTO QUEDE MUY FEO MÁS TARDE ME RESUELVE MUCHOS PROBLEMAS PARA ELMINIAR ELEMENTOS...
     * LA PRIMERA VEZ QUE SE GUARDAA UN CONTACTO DETECTA QUE EL FICHERO ESTÁ VACÍO LO UNICO QUE HACE ES AÑADIR UNA NUEVA LINEA A LA LISTA VACÍA
     * EL RESTO DE VECEES QUE SE GUARDA DETECTA DONDE ESTÁ EL PRIMER PUNTO DISPONIBLE Y LO SOBREESCRIBE
     * @param serie EL OBJETO QUE VOY A UTILIZAR AUNQUE REALMENTE NO ES NECESARIO CREAR UN USUARIO PARA ESTO
     */
    public static void saveSerieData(Serie serie){

        try{

            List<String> fileContent = new ArrayList<>(Files.readAllLines(Paths.get(dataPath), StandardCharsets.UTF_8));
            String newString = getLastElementNumber() + "-" + serie.getName() + "-" + serie.getDirector() + "-" + serie.getDate() + "-" + serie.getCopies() +
                    "-" + serie.getType() + "-"+ serie.getCopies() + "-0-0-" + "Disponible" + "-" + serie.getSeasons() + "\n" + ".";

            if (fileContent.isEmpty()){
                fileContent.add(newString);
                Files.write(Paths.get(dataPath), fileContent, StandardCharsets.UTF_8);
            }else{
                for (int i = 0; i < fileContent.size(); i++){
                    if (fileContent.get(i).length() <= 2){
                        fileContent.set(i,newString);
                        Files.write(Paths.get(dataPath), fileContent, StandardCharsets.UTF_8);
                        System.out.println("¡Elemento guardado exitosamente!");
                        return;
                    }
                }
            }
        }catch (IOException exc){
            exc.printStackTrace();
        }
    }

    /**
     * COMPRUEBA SI SE PUEDE DEVOLVER UN ELEMENTO
     * COMPRUEBA SI LAS COPIAS DISPONIBLES SON IGUALES A LAS TOTALES, SI ES ASÍ NO DEJA DEVOLVER YA QUE TODAS ESTÁN EN LA TIENDA
     * @param cod CÓDIGO DE PRODUCTO
     * @return BOOLEAN SI PUEDE DEVOLVER ELEMENTO O NO
     */
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
     * COMPRUEBA LA ÚLTIMA LINEA CON UN ELEMENTO Y GUARDA EL NÚMERO
     * @return ULTIMO NÚMERO + 1
     */
    public static int getLastElementNumber(){

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

    /**
     * RELLENA UN ARRAYLIST CON CADA ELEMENTO DE LA BASE DE DATOS
     * @return ARRAYLIST CON LOS ELEMENTOS
     */
    public static ArrayList<String> getElements(){
        elements.clear();
        fillElements();
        return elements;
    }

    /**
     * RECORRE EL FICHERO Y GUARDA CADA ELEMENTO DENTRO DE UN ARRAYLIST
     */
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

    /**
     * RESTA UNA COPIA DE EL ELEMENTO INTRODUCIDO POR PARÁMETRO
     * COMPRUEBA SI EL ELEMENTO QUE SE VA A PRESTAR ESTÁ DISPONIBLE Y SI ES ASÍ LO DEVUELVE Y RESTA UNA A LAS COPIAS DISPONIBLES
     * @param cod CÓDIGO DEL PRODUCTO
     */
    public static void substract(String cod){

        try {
            List<String> fileContent = new ArrayList<>(Files.readAllLines(Paths.get(dataPath), StandardCharsets.UTF_8));

            for (int i = 0; i < fileContent.size(); i++){

                String[] line = fileContent.get(i).split("-");

                if (line.length > 1){
                    if (line[0].equalsIgnoreCase(cod)){
                        if (isAvailable(cod)){
                            if (Integer.parseInt(line[6]) > 0){
                                line[4] = Integer.toString(Integer.parseInt(line[4]) - 1);

                                fileContent.set(i, Arrays.toString(line).replace("[", "").replace("]","").replace(",","-").replace("- ", "-"));
                            }else{
                                System.out.println("El producto no está en stock");
                            }
                        }else{
                            System.out.println("El producto no está disponible");
                        }
                    }
                }
            }
            Files.write(Paths.get(dataPath), fileContent, StandardCharsets.UTF_8);

        }catch (IOException ignored){

        }
    }

    /**
     * DEVUELVE UN PRODUCTO QUE HA SIDO USADO POR UN CLIENTE
     * AL DEVOLVERLO SUMA UNA A LAS COPIAS DISPONIBLES, SUMA UNO A LAS VECES QUE SE HA ALQUILADO ESTE ELEMENTO Y SUMA LA VALORACIÓN A LA VALORACIÓN TOTAL
     * @param cod CÓDIGO DEL PRODUCTO
     * @param assessment VALORACIÓN DEL CLIENTE
     */
    public static void returnElement(String cod, int assessment){

        try {
            List<String> fileContent = new ArrayList<>(Files.readAllLines(Paths.get(dataPath), StandardCharsets.UTF_8));

            for (int i = 0; i < fileContent.size(); i++){

                String[] line = fileContent.get(i).split("-");

                if (line.length > 1) {
                    if (line[0].equalsIgnoreCase(cod)) {
                        line[4] = Integer.toString(Integer.parseInt(line[4]) + 1);
                        line[7] = Integer.toString(Integer.parseInt(line[7]) + 1);
                        line[8] = Integer.toString(Integer.parseInt(line[8]) + assessment);

                        fileContent.set(i, Arrays.toString(line).replace("[", "").replace("]","").replace(",","-").replace("- ", "-"));
                        System.out.println("Se ha devuelto exitosamente.");
                    }
                }
            }
            Files.write(Paths.get(dataPath), fileContent, StandardCharsets.UTF_8);

        }catch (IOException ignored){}
    }

    /**
     * DEVUELVE UN ELEMENTO PERO CUANDO HA SIDO MANDADO A REPARAR
     * HACE LO MISMO QUE EL MÉTODO ANTERIOR A EXCEPCIÓN DE QUE ESTE NO SE LE SUMA NINGUNA VALORACIÓN NI COPIAS TOTALES ALQUILADAS
     * @param cod CÓDIGO DEL PRODUCTO
     */
    public static void returnElement(String cod){

        try {
            List<String> fileContent = new ArrayList<>(Files.readAllLines(Paths.get(dataPath), StandardCharsets.UTF_8));

            for (int i = 0; i < fileContent.size(); i++){

                String[] line = fileContent.get(i).split("-");

                if (line.length > 1) {
                    if (line[0].equalsIgnoreCase(cod)) {
                        line[4] = Integer.toString(Integer.parseInt(line[4]) + 1);

                        fileContent.set(i, Arrays.toString(line).replace("[", "").replace("]","").replace(",","-").replace("- ", "-"));
                    }
                }
            }
            Files.write(Paths.get(dataPath), fileContent, StandardCharsets.UTF_8);

        }catch (IOException ignored){

        }
    }


    /**
     * DEVUELVE VERDADERO SI ES UNA SERIE
     * @param cod CÓDIGO DEL ELEMENTO
     * @return SI ES SERIE O PELICULA EN FORMATO BOOLEAN
     */
    public static boolean isSerie(String cod){
        try {
            reader = new BufferedReader(new FileReader(dataPath));
            String line = reader.readLine();
            while (line != null) {

                String[] ultimaString = line.split("-");

                if (ultimaString[0].equals(cod)){
                    return ultimaString[5].equalsIgnoreCase("Serie");
                }

                // AVANZA UNA LINEA
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * AÑADE COPIAS A UN ELEMENTO
     * AÑADE A LAS DISPONIBLES Y A LAS TOTALES
     * @param cod CÓDIGO DEL ELEMENTO
     * @param num NUMERO DE COPIAS QUE SE VAN A AÑADIR
     */
    public static void addCopies(String cod, int num){

        try {
            List<String> fileContent = new ArrayList<>(Files.readAllLines(Paths.get(dataPath), StandardCharsets.UTF_8));

            for (int i = 0; i < fileContent.size(); i++){

                String[] line = fileContent.get(i).split("-");

                if (line.length > 1) {
                    if (line[0].equalsIgnoreCase(cod)) {
                        line[4] = Integer.toString(Integer.parseInt(line[4]) + num);
                        line[6] = Integer.toString(Integer.parseInt(line[6]) + num);

                        fileContent.set(i, Arrays.toString(line).replace("[", "").replace("]","").replace(",","-").replace("- ", "-"));
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
     * COMPRUEBA SI UN ELEMENTO ESTÁ DISPONIBLE
     * @param cod CÓDIGO DEL ELEMENTO
     * @return BOOLEANO TRUE SI ESTÁ DISPONIBLE FALSE SI NO
     */
    private static boolean isAvailable(String cod) {
        try{
            List<String> fileContent = new ArrayList<>(Files.readAllLines(Paths.get(dataPath), StandardCharsets.UTF_8));

            for (String s : fileContent) {
                String[] line = s.split("-");

                if (line[0].equalsIgnoreCase(cod)) {
                    return line[9].equalsIgnoreCase("Disponible");
                }
            }
        }catch (IOException e){
            System.out.println("No se ha podido cambiar disponibilidad");
        }

        return false;
    }

    /**
     * CAMBIA LA DISPONIBILIDAD
     * @param cod CODIGO DE ELEMENTO
     */
    public static void toggleAvailability(String cod){
        boolean founded = false;
        try{
            List<String> fileContent = new ArrayList<>(Files.readAllLines(Paths.get(dataPath), StandardCharsets.UTF_8));

            for (int i = 0; i < fileContent.size(); i++){
                String[] line = fileContent.get(i).split("-");

                if (line[0].equalsIgnoreCase(cod)){
                    founded = true;
                    if (line[9].equalsIgnoreCase("Disponible")){
                        line[9] = "No Disponible";
                        System.out.println(line[1] + " ahora no está disponible.");
                    }else {
                        line[9] = "Disponible";
                        System.out.println(line[1] + " ahora está disponible.");

                    }
                    fileContent.set(i, Arrays.toString(line).replace("[", "").replace("]","").replace(",","-").replace("- ", "-"));
                }
            }

            if (!founded){
                System.out.println("No se ha encontrado el usuario.");
            }

            Files.write(Paths.get(dataPath), fileContent, StandardCharsets.UTF_8);

        }catch (IOException e){
            System.out.println("No se ha podido cambiar disponibilidad");
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

                if (ultimaString[0].equals(elementID)){
                    return ultimaString[0] + "-" + ultimaString[1] + "-" + ultimaString[2] + "-" + ultimaString[3] + "-" + ultimaString[4] + "-" + ultimaString[9] + "-" + ultimaString[5];
                }


                // AVANZA UNA LINEA
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "-1";
    }

    /**
     * DEVUELVE LA VALORACIÓN MEDIA GLOBAL DE UN ELEMENTO
     * LA FORMULA QUE REALIZA ES (SUMA TOTAL DE VALORACIONES DE CLIENTES / NUMERO DE COPIAS TOTALES ALQUILADAS)
     * EN EL CASO DE QUE LA DIVISIÓN SEA 0 LA DIVISION DEVUELVE NAN (Not A Number) Y LO CAMBIO POR UN 0.0
     * @param cod CÓDIGO DEL ELEMENTO
     * @return VALORACIÓN MEDIA
     */
    public static double getValoration(String cod){

        try {
            reader = new BufferedReader(new FileReader(dataPath));
            String line = reader.readLine();
            while (line != null) {

                String[] ultimaString = line.split("-");

                if (ultimaString.length > 1){
                    double assesment = Double.parseDouble(ultimaString[8]) / Double.parseDouble(ultimaString[7]);
                    if (ultimaString[0].equals(cod)){
                        if (!Double.isNaN(assesment)){
                            return assesment;
                        }
                    }
                }

                // AVANZA UNA LINEA
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    /**
     * ELIMINA UN ELEMENTO DE LA BASE DE DATOS
     * AQUÍ UTILIZO LOS PUNTOS DEL FICHERO
     * RECORRO CADA LINEA HASTA QUE ENCUENTRO EL ELEMENTO INDICADO
     * A PARTIR DE AHÍ CAMBIO EL ELEMENTO DE LA SIGUIENTE LÍNEA POR ESE HASTA QUE TODOS ESTÁN CAMBIADOS
     * SI NO ESTUVIESE EL . AL FINAL QUEDARÍAN 2 ELEMENTOS DUPLICADOS
     * @param elementCode CÓDIGO DEL ELEMENTO
     */
    public static void removeElement(String elementCode){

        try{
            List<String> fileContent = new ArrayList<>(Files.readAllLines(Paths.get(dataPath), StandardCharsets.UTF_8));

            boolean founded = false;
            for (int i = 0; i < fileContent.size(); i++) {

                String[] line = fileContent.get(i).split("-");

                if (founded){
                    SeasonsDataBase.removeElement(elementCode);
                    try {
                        fileContent.set(i - 1, Arrays.toString(line).replace("[", "").replace("]","").replace(",","-").replace("- ", "-"));

                    }catch(NumberFormatException a) {
                        fileContent.set(i - 1, Arrays.toString(line).replace("[", "").replace("]","").replace(",","-").replace("- ", "-"));
                    }
                }

                if (line[0].equals(elementCode)) {
                    founded = true;
                }
            }

            Files.write(Paths.get(dataPath), fileContent, StandardCharsets.UTF_8);

        }catch (IOException e){
            System.out.println("ERROR AL ELIMINAR EL ELEMENTO.");
            e.printStackTrace();
        }
    }

    /**
     * EDITA UN ELEMENTO
     * SI UNO DE LOS PARAMETROS ES UNA CADENA VACÍA O EN EL CASO DEL AÑO ES UN 0 NO MODIFICA ESE CAMPO
     * @param cod CÓDIGO DE ELEMENTO
     * @param name NUEVO NOMBRE PARA EL ELEMENTO
     * @param director NUEVO DIRECTOR PARA EL ELEMENTO
     * @param year NUEVO AÑO PARA EL ELEMENTO
     */
    public static void editElement(String cod, String name, String director, int year){

            try{
                List<String> fileContent = new ArrayList<>(Files.readAllLines(Paths.get(dataPath), StandardCharsets.UTF_8));

                for (int i = 0; i < fileContent.size(); i++){

                    String[] line = fileContent.get(i).split("-");

                    if (line[0].equalsIgnoreCase(cod)){
                        if (!name.equalsIgnoreCase("")) line[1] = name;
                        if (!director.equalsIgnoreCase("")) line[2] = director;
                        if (year != 0) line[3] = Integer.toString(year);
                        fileContent.set(i, Arrays.toString(line).replace("[", "").replace("]","").replace(",","-").replace("- ", "-"));
                        Files.write(Paths.get(dataPath), fileContent, StandardCharsets.UTF_8);
                        System.out.println("Se ha modificado correctamente.");
                        return;
                    }
                }
            }catch(Exception e){
                System.out.println("Algo fué mal editando el contacto");
                e.printStackTrace();
            }
        }
}
