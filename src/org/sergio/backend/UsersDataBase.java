package org.sergio.backend;

import org.sergio.users.*;

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
public class UsersDataBase {

    // ENCONTRAR RUTA DEL DIRECTORIO DOCUMENTOS DEL USUARIO

    private static String documentsDirectory = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();

    // CREAR CARPETA

    private static String directory = documentsDirectory + "\\" + "FakeFlix";

    // NOMBRE DEL ARCHIVO CON LOS DATOS
    private static String usersData = "FakeFlixUsers.dat";

    // LA RUTA DEL FICHERO DE DATOS
    private static String dataPath = directory + "/" + usersData;

    // ARRAYLIST CON LOS NOMBRES DEL FICHERO
    private static ArrayList<String> userNames = new ArrayList<>();

    private static Scanner sc = new Scanner(System.in);

    private static BufferedReader reader;


    /**
     * COMPRUEBA SI EXISTE EL DIRECTORIO PARA GUARDAR LOS DATOS
     * SI NO EXISTE LO CREA
     * INTENTA CREAR UN FICHERO PARA GUARDAR DATOS
     * SI EL FICHERO YA EXISTE NO LO CREA
     */
    public static void newDataFile(){

        try {

            File dir = new File(directory);
            if (!dir.exists()){
                dir.mkdir();
            }

            // CREAMOS EL FICHERO EN LA RUTA DOCUMENTS
            File data = new File(dataPath);

            // SI EL FICHERO EXISTE ES FALSE SI EXISTE TRUE
            boolean exist = data.createNewFile();

            if (exist || data.length() <= 0){
                System.out.print("Introduce un nombre para el administrador: \n>>> ");
                String name = sc.nextLine();
                System.out.print("Introduce una contraseña para el administrador: \n>>> ");
                String pass = sc.nextLine();

                saveContactData(new Boss(name, pass));
                System.out.println("Administrador creado correctamente.");
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * GUARDA INFORMACIÓN DE UN USUARIO
     * UTILIZO UN MÉTODO DE LA CLASE FILES EL CUAL GUARDA DENTRO DE CADA POSICION DE UNA LISTA EN FORMATO ARRAYLIST CADA LINEA DE UN FICHERO
     * CADA VEZ QUE GUARDO UN ELEMENTO HAGO UN SALTO DE LÍNEA Y AÑADO UN PUNTO, AUNQUE ESTO QUEDE MUY FEO MÁS TARDE ME RESUELVE MUCHOS PROBLEMAS PARA ELMINIAR USUARIOS...
     * LA PRIMERA VEZ QUE SE GUARDAA UN CONTACTO DETECTA QUE EL FICHERO ESTÁ VACÍO LO UNICO QUE HACE ES AÑADIR UNA NUEVA LINEA A LA LISTA VACÍA
     * EL RESTO DE VECES QUE SE GUARDA DETECTA DONDE ESTÁ EL PRIMER PUNTO DISPONIBLE Y LO SOBREESCRIBE
     * @param user INFORMACIÓN DEL USUARIO
     */
    public static void saveContactData(User user) {

        if (user.getName().isBlank() && user.getPassword().isBlank()){
            System.out.println("No puedes introducir un usuario en blanco.");
            return;
        }

        try {

            List<String> fileContent = new ArrayList<>(Files.readAllLines(Paths.get(dataPath), StandardCharsets.UTF_8));
            String newString = getLastContactNumber() + "-" + user.getName() + "-" + user.getPassword() + "-" + user.getBoss() + "\n" + ".";

            if (fileContent.isEmpty()){
                fileContent.add(newString);
                Files.write(Paths.get(dataPath), fileContent, StandardCharsets.UTF_8);
            }else{
                for (int i = 0; i < fileContent.size(); i++){
                    if (fileContent.get(i).length() <= 2){
                        fileContent.set(i,newString);
                        Files.write(Paths.get(dataPath), fileContent, StandardCharsets.UTF_8);
                        System.out.println("¡Usuario guardado exitosamente!");
                        return;
                    }
                }
            }
        } catch (Exception asd) {
            asd.printStackTrace();
        }
    }

    /**
     * CONSIGUE EL NÚMERO DEL ÚLTIMO CONTACTO GUARDADO
     * @return ULTIMO NÚMERO + 1
     */
    public static int getLastContactNumber(){

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
     * @return UN ARRAYLIST CON LA INFORMACIÓN DE CADA USUARIO
     */
    public static ArrayList<String> getNames(){
        userNames.clear();
        fillContactsName();
        return userNames;
    }

    /**
     * RELLENA UN ARRAYLIST CON LA INFORMACIÓN DE CADA USUARIO
     */
    private static void fillContactsName(){
        try {
            reader = new BufferedReader(new FileReader(dataPath));
            String line = reader.readLine();
            while (line != null) {

                String[] lastLine = line.split("-");

                String newLine;

                if (lastLine.length > 1){
                    newLine = lastLine[0] + "-" + lastLine[1] + "-" + lastLine[2] + "-" + lastLine[3];
                    userNames.add(newLine);
                }
                // AVANZA UNA LINEA
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * BUSCA LA ID DE UN USUARIO A PARTIR DE UN NOMBRE
     * @param name NOMBRE DE USUARIO
     * @return USERID
     */
    private static String searchUserId(String name){

        String userID = "-1";

        try {
            reader = new BufferedReader(new FileReader(dataPath));
            String line = reader.readLine();
            while (line != null) {
                String[] ultimaString = line.split("-");
                try {
                    if (ultimaString[1].equalsIgnoreCase(name)){
                        userID = ultimaString[0];
                    }
                }catch (ArrayIndexOutOfBoundsException ignore){
                }

                // AVANZA UNA LINEA
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userID;
    }

    /**
     * EDITA UN CONTACTO DE LA BASE DE DATOS
     * SI UN CAMPO SE DEJA VACÍO NO SE MODIFICA
     */
    public static void editContact(){

        System.out.println("== EDITAR USUARIO ==");
        System.out.println("== Dejar en blanco el campo que no quieras modificar==");

        System.out.print("Introduce el nombre: ");
        String name = sc.nextLine();
        String userId = searchUserId(name);
        if (!userId.equals("-1")){
            try{
                // GUARDA CADA LINEA EN UN ESPACIO DEL ARRAYLIST
                List<String> fileContent = new ArrayList<>(Files.readAllLines(Paths.get(dataPath), StandardCharsets.UTF_8));

                // EMPIEZA LA NUEVA CADENA AÑADIENDO EL NÚMERO DE CONTACTO
                String newInput = userId + "-";

                StringBuilder newLine = new StringBuilder(newInput);

                System.out.print("Nuevo nombre: ");
                String newName = sc.nextLine();
                System.out.print("Nueva contraseña: ");
                String newPass = sc.nextLine();

                // RECORRE CADA LÍNEA GUARDADA
                for (int i = 0; i < fileContent.size(); i++) {

                    // SEPARA LA LÍNEA EN TROZOS
                    String[] line = fileContent.get(i).split("-");

                    // SI EL ID DE LA LINEA COINCIDE CON EL QUE SE HA PASADO DEJA LA VARIABLE FOUNDED EN TRUE Y EMPIEZA A AÑADIR A LA CADENA LOS PARAMETROS QUE NO SON NULL
                    if (line[0].equals(userId)) {


                        if (!newName.equals("")){
                            newLine.append(newName).append("-");
                        }else{
                            newLine.append(line[1]).append("-");
                        }

                        if (!newPass.equals("")){
                            newLine.append(newPass).append("-");
                        }else{
                            newLine.append(line[2]).append("-");
                        }

                        newLine.append(line[3]);

                        fileContent.set(i, newLine.toString());
                    }
                }

                // SE GUARDAN LOS CAMBIOS
                System.out.println("Se ha modificado correctamente.");
                Files.write(Paths.get(dataPath), fileContent, StandardCharsets.UTF_8);

            }catch(Exception e){
                System.out.println("Algo fué mal editando el contacto");
                e.printStackTrace();
            }
        }
    }

    /**
     * ELIMINA UN USUARIO DE LA BASE DE DATOS
     * AQUÍ UTILIZO LOS PUNTOS DEL FICHERO
     * RECORRO CADA LINEA HASTA QUE ENCUENTRO EL USUARIO INDICADO
     * A PARTIR DE AHÍ CAMBIO EL USUARIO DE LA SIGUIENTE LÍNEA POR ESE HASTA QUE TODOS ESTÁN CAMBIADOS
     * SI NO ESTUVIESE EL . AL FINAL QUEDARÍAN 2 ELEMENTOS DUPLICADOS
     * SI EL USUARIO ES UN ADMINISTRADOR SALTA UN AVISO DE QUE NO SE PUEDE ELIMINAR Y NO SE ELIMINA
     */
    public static void removeUser(){

        System.out.println("== ELIMINAR USUARIO ==");
        System.out.print("Introduce el nombre: ");
        String userName = sc.nextLine();
        String userId = searchUserId(userName);
        if (!userId.equals("-1")){
            try{
                List<String> fileContent = new ArrayList<>(Files.readAllLines(Paths.get(dataPath), StandardCharsets.UTF_8));

                boolean founded = false;

                for (int i = 0; i < fileContent.size(); i++) {

                    String[] line = fileContent.get(i).split("-");

                    if (founded){
                        try {
                            line[0] = Integer.toString(Integer.parseInt(line[0]) - 1);
                            fileContent.set(i - 1, Arrays.toString(line).replace("[", "").replace("]","").replace(",","-").replace("- ", "-"));

                        }catch(NumberFormatException a) {
                            fileContent.set(i - 1, Arrays.toString(line).replace("[", "").replace("]","").replace(",","-").replace("- ", "-"));
                        }
                    }

                    if (line[0].equals(userId)) {
                        if (line.length > 1){
                            if (line[3].equalsIgnoreCase("true")){
                                System.out.println("No puedes eliminar a un administrador.");
                                return;
                            }
                            founded = true;
                        }
                    }
                }
                Files.write(Paths.get(dataPath), fileContent, StandardCharsets.UTF_8);
                System.out.println("¡Usuario eliminado correctamente!");
            }catch (Exception e){
                System.out.println("ERROR AL ELIMINAR AL USUARIO");
                e.printStackTrace();
            }
        }else {
            System.out.println("El usuario introducido no puede ser eliminado.");
        }
    }
}
