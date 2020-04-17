package org.sergio.backend;


import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * @author SERGIO GARCÍA MAYO
 * ESTA CLASE REALIZA PRÁCTICAMENTE LOS MISMOS ALGORITMOS QUE LAS OTRAS CLASES QUE GESTIONAN LOS DATOS
 */
public class SeasonsDataBase {

    // ENCONTRAR RUTA DEL DIRECTORIO DOCUMENTOS DEL USUARIO

    private static String documentsDirectory = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();

    // NOMBRE DEL ARCHIVO CON LOS DATOS
    private static String elementsData = "FakeFlixSeasons.dat";

    // LA RUTA DEL FICHERO DE DATOS
    private static String dataPath = documentsDirectory + "\\FakeFlix\\" + elementsData;

    private static ArrayList<String> elements = new ArrayList<>();

    private static Scanner sc = new Scanner(System.in);

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
     * GUARDA INFORMACIÓN DE UNA TEMPORADA
     * UTILIZO UN MÉTODO DE LA CLASE FILES EL CUAL GUARDA DENTRO DE CADA POSICION DE UNA LISTA EN FORMATO ARRAYLIST CADA LINEA DE UN FICHERO
     * CADA VEZ QUE GUARDO UN ELEMENTO HAGO UN SALTO DE LÍNEA Y AÑADO UN PUNTO, AUNQUE ESTO QUEDE MUY FEO MÁS TARDE ME RESUELVE MUCHOS PROBLEMAS PARA ELMINIAR TEMPORADAS...
     * LA PRIMERA VEZ QUE SE GUARDAA UN CONTACTO DETECTA QUE EL FICHERO ESTÁ VACÍO LO UNICO QUE HACE ES AÑADIR UNA NUEVA LINEA A LA LISTA VACÍA
     *  EL RESTO DE VECES QUE SE GUARDA DETECTA DONDE ESTÁ EL PRIMER PUNTO DISPONIBLE Y LO SOBREESCRIBE
     * @param name NOMBRE DE LA SERIA
     * @param chapters CAPITULOS
     */
    public static void saveSeason(String name, int chapters){

        try{

            List<String> fileContent = new ArrayList<>(Files.readAllLines(Paths.get(dataPath), StandardCharsets.UTF_8));
            String newSeason = ElementsDataBase.getLastElementNumber() + "-" + name + "-" + chapters + "\n" + ".";

            if (fileContent.isEmpty()){
                fileContent.add(newSeason);
                Files.write(Paths.get(dataPath), fileContent, StandardCharsets.UTF_8);
            }else{
                for (int i = 0; i < fileContent.size(); i++){
                    if (fileContent.get(i).length() <= 2){
                        fileContent.set(i,newSeason);
                        Files.write(Paths.get(dataPath), fileContent, StandardCharsets.UTF_8);
                        return;
                    }
                }
            }

        }catch(NumberFormatException | IOException a) {
                a.printStackTrace();
        }

    }

    /**
     * BUSCA UNA MEDIANTE EL CÓDIGO DE LA SERIE A LA QUE PERTENECE
     * @param elementID ID DEL ELEMENTO QUE SE QUIERE BUSCAR
     * @return LA INFORMACIÓN DEL ELEMENTO
     */
    public static List<String> getSeasons(String elementID){

        List<String> seasons = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(dataPath));
            String line = reader.readLine();
            while (line != null) {

                String[] ultimaString = line.split("-");

                if (ultimaString.length > 1){
                    if (ultimaString[0].equals(elementID)){
                        seasons.add(ultimaString[0] + "-" + ultimaString[1] + "-" + ultimaString[2]);
                    }
                }
                // AVANZA UNA LINEA
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return seasons;
    }

    /**
     * ELIMINA LAS TEMPORADAS INDICANDO EL CÓDIGO QUE SE QUIERE ELIMINAR
     * EL MÉTODO SE LLAMA RECURSIVAMENTE HASTA QUE NO ENCUENTRA MAS COINCIDENCIAS DE EL CÓDIGO INTRODUCIDO
     * @param cod CÓDIGO DE LA SERIE A LA QUE PERTENECEN LAS TEMPORADAS
     */
    public static void removeElement(String cod){

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

                if (line[0].equals(cod)) {
                    founded = true;
                }
            }
            // SE GUARDAN LOS CAMBIOS
            Files.write(Paths.get(dataPath), fileContent, StandardCharsets.UTF_8);

            if (founded){
                removeElement(cod);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
