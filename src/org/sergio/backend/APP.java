package org.sergio.backend;

import org.sergio.elements.Element;
import org.sergio.elements.Movie;
import org.sergio.elements.Serie;
import org.sergio.users.Employee;

import java.util.*;

public class APP {

    private static boolean logged = false;
    private static boolean boss = false;

    private static Scanner sc = new Scanner(System.in);

    public static void run(){

        while (true){
            if (!logged){
                login();
            }else{
                displayMenu();
            }
        }
    }

    private static void addElement(){

        try {

            Scanner sc = new Scanner(System.in);
            int option;

            String name;
            String director;
            int date;
            int type = 0;
            int subtype = 1;
            int copies;
            int seasons = 1;

            System.out.println("==AÑADIR ELEMENTO==");
            System.out.println("1. Película");
            System.out.println("2. Serie");

            do {
                System.out.print(">>> ");
                option = sc.nextInt();
            }while (option <= 0 || option >= 3);
            sc.nextLine();

            if (option == 1){
                do{
                    System.out.println("Tipo de película: ");
                    System.out.println("1. Ciencia ficción");
                    System.out.println("2. Animación");
                    System.out.print(">>> ");
                    type = sc.nextInt();
                }while (type <= 0 || type >= 3);

                do {
                    if (type == 2){
                        System.out.println("Selecciona una categoría: ");
                        System.out.println("1. Animación tradicional.");
                        System.out.println("2. Stop Motion.");
                        System.out.println("3. Animación computarizada.");
                        System.out.print(">>> ");
                        subtype = sc.nextInt();
                    }
                }while (subtype <= 0 || subtype >= 4);
                sc.nextLine();
            }

            do {
                System.out.print("Nombre: ");
                name = sc.nextLine();
            }while (name.isBlank());
            do {
                System.out.print("Director: ");
                director = sc.nextLine();
            }while (director.isBlank());

            do {
                System.out.print("Año de lanzamiento(yyyy): ");
                date = sc.nextInt();
            }while (date <= 1894 || date >= 2021);

            System.out.print("Número de copias: ");
            copies = sc.nextInt();

            if (option == 2){
                System.out.print("Número de temporadas: ");
                seasons = sc.nextInt();
            }

            switch (option){
                case 1:
                    ElementsDataBase.saveMovieData(new Movie(name, director, date, copies, type, subtype));
                    break;
                case 2:
                    ElementsDataBase.saveSerieData(new Serie(name, director, date, copies, seasons));
                    break;
            }
        }catch(InputMismatchException e){
            System.out.println("Entrada inválida.");
        }
    }

    /**
     * LOGIN: MÉTODO PARA INICIAR SESIÓN
     */
    private static void login(){

        ArrayList<String> users = UsersDataBase.getNames();
        boss = false;

        System.out.println("--Iniciar sesión--");
        System.out.println("==================");

        System.out.print("Usuario: ");
        String user = sc.nextLine();
        System.out.print("Contrasña: ");
        String pass = sc.nextLine();

        for (String s : users) {
            String[] line = s.split("-");
            if (user.equalsIgnoreCase(line[1]) && pass.equals(line[2])) {
                System.out.println("Inicio de sesión exitoso");
                if (line[3].equalsIgnoreCase("true")) {
                    boss = true;
                }
                logged = true;
                return;
            }
        }
        System.out.println("Nombre o contraseña incorrecto");
    }

    private static void logout(){
        System.out.println("HASTA PRONTO!");
        logged = false;
        boss = false;
    }

    private static void register(){

        System.out.println("==Registro==");

        System.out.print("Nombre de usuario: ");
        String user = sc.nextLine();
        System.out.print("Contraseña: ");
        String pass = sc.nextLine();

        UsersDataBase.saveContactData(new Employee(user,pass));
    }

    private static void showElements(){
        int opc;
        int format;
        boolean found = false;

        try {
            Scanner sc = new Scanner(System.in);

            System.out.println("==ELEMENTOS EN EL SISTEMA==");
            System.out.println("Como quieres ordenarlo: ");
            System.out.println("1. Código");
            System.out.println("2. Nombre");
            System.out.println("3. Director");
            System.out.println("4. Fecha");
            System.out.println("5. Valoración media");
            System.out.println("6. Copias");

            do {
                System.out.print(">>> ");
                opc = sc.nextInt();
            }while (opc <= 0 || opc >= 7);

            System.out.println("Formato: ");
            System.out.println("1. Ascendente");
            System.out.println("2. Descendente");
            do {
                System.out.print(">>> ");
                format = sc.nextInt();
            }while (format <= 0 || format >= 3);

        }catch (InputMismatchException e){
            System.out.println("Entrada inválida.");

            return;
        }

        List<String> elements = orderBy(opc - 1, format);

        System.out.println("==== PELÍCULAS ====");
        System.out.println("COD | NOMBRE | DIRECTOR | FECHA | VALORACIÓN MEDIA | COPIAS | CATEGORÍA | SUBCATEGORÍA");
        for (String s : elements) {
            if (s.length() > 1) {
                String[] line = s.split("-");
                if (line[5].equalsIgnoreCase("Movie")) {
                    if (line[9].equalsIgnoreCase("Animación")){
                        System.out.println(line[0] + " | " + line[1] + " | " + line[2] + " | " + line[3] + " | " + String.format("%.2f",Double.parseDouble(line[8]) / Double.parseDouble(line[7])) + " | " + line[4] + " | " + line[9] + " | " + line[10]);
                    }else {
                        System.out.println(line[0] + " | " + line[1] + " | " + line[2] + " | " + line[3] + " | " + String.format("%.2f",Double.parseDouble(line[8]) / Double.parseDouble(line[7])) + " | " + line[4] + " | " + line[9]);
                    }
                    found = true;
                }
            }
        }

        if (!found){
            System.out.println("NO SE HA ENCONTRADO NINGUNA PELÍCULA.");
        }
        found = false;
        System.out.println("==== SERIES ====");
        System.out.println("COD | NOMBRE | DIRECTOR | FECHA | VALORACIÓN MEDIA | COPIAS | TIPO | TEMPORADAS");
        for (String element : elements) {
            if (element.length() > 1) {
                String[] line = element.split("-");

                if (line[5].equalsIgnoreCase("Serie")) {
                    System.out.println(line[0] + " | " + line[1] + " | " + line[2] + " | " + line[3] + " | " + String.format("%.2f",Double.parseDouble(line[8]) / Double.parseDouble(line[7])) + " | " + line[4] + " | " + line[9]);
                    found = true;
                }
            }
        }

        if (!found){
            System.out.println("NO SE HA ENCONTRADO NINGUNA SERIE");
        }
    }

    private static List<String> orderBy(int num, int format){
        ArrayList<String> elements = ElementsDataBase.getElements();
        System.out.println(elements.size());
        if (elements.size() > 1){
            for (int i = 0; i < elements.size(); i++) {
                for (int j = 1; j < elements.size() - i; j++) {

                    String[] line1 = elements.get(j).split("-");
                    String[] line2 = elements.get(j - 1).split("-");

                    if (line1.length > 1 && line2.length > 1){
                        try {
                            if (format == 1){
                                if (Integer.parseInt(line1[num]) < Integer.parseInt(line2[num])) {
                                    String temp = elements.get(j - 1);
                                    elements.set(j - 1, elements.get(j));
                                    elements.set(j, temp);
                                }
                            }else{
                                if (Integer.parseInt(line1[num]) > Integer.parseInt(line2[num])) {
                                    String temp = elements.get(j - 1);
                                    elements.set(j - 1, elements.get(j));
                                    elements.set(j, temp);
                                }
                            }

                        } catch (NumberFormatException e) {
                            if (format == 1){
                                if (line1[num].compareTo(line2[num]) < 0) {
                                    String temp = elements.get(j - 1);
                                    elements.set(j - 1, elements.get(j));
                                    elements.set(j, temp);
                                }
                            }else {
                                if (line1[num].compareTo(line2[num]) > 0) {
                                    String temp = elements.get(j - 1);
                                    elements.set(j - 1, elements.get(j));
                                    elements.set(j, temp);
                                }
                            }
                        }
                    }
                }
            }
        }else{
            return elements;
        }
        return elements;
    }

    private static void copyManagement(){
        int cod;
        int opc;
        String element;
        Scanner sc = new Scanner(System.in);
        System.out.println("=== MANTENIMIENTO DE COPIAS ===");
        System.out.println("1. Mandar a reparar");
        System.out.println("2. Reponer una copia");
        try {
            do {
                System.out.print(">>> ");
                opc = sc.nextInt();
            }while(opc <= 0 || opc >= 3);

            System.out.print("Introduce codigo de elemento: ");
            cod = sc.nextInt();
            element = ElementsDataBase.searchElement(Integer.toString(cod));

            if (!element.equalsIgnoreCase("-1")){
                if (opc == 1){
                    ElementsDataBase.substract(Integer.toString(cod));
                    System.out.println("Elemento mandado a reparar correctamente.");
                }else {
                    if (ElementsDataBase.canReturn(cod)){
                        ElementsDataBase.returnElement(Integer.toString(cod));
                        System.out.println("Se ha reestablecido un elemento correctamente.");
                    }else{
                        System.out.println("Ya estan todas las copias reestablecidas.");
                    }
                }
            }else{
                System.out.println("Usuario no encontrado.");
            }
        }catch (InputMismatchException e){
            System.out.println("Entrada inválida.");
        }

    }

    private static void lendElement(){
        int cod = -1;
        String opt;
        String element;

        System.out.println("=== PRESTAR ELEMENTO ===");
        System.out.print("Introduce el código de producto: ");
        try{
            cod = sc.nextInt();
            sc.nextLine();
        }catch (InputMismatchException e){
            System.out.println("El código introducido es inválido");
        }
        element = ElementsDataBase.searchElement(Integer.toString(cod));
        if (!element.equalsIgnoreCase("-1")){
            System.out.println("Has seleccionado: ");
            System.out.println(element.replace("-", " | "));
            System.out.println("¿Estas seguro?");
            System.out.print("(SI/NO)>>> ");
            opt = sc.nextLine();
            if (opt.equalsIgnoreCase("SI")){
                ElementsDataBase.substract(Integer.toString(cod));
                System.out.println("Se ha prestado exitosamente.");
            }else if(opt.equalsIgnoreCase("NO")){
                System.out.println("Volviendo al menú...");
            }else {
                System.out.println("Opción inválida");
            }
        }else{
            System.out.println("El codigo introducido es inválido.");
        }
    }

    private static void returnElement(){
        int cod = -1;
        int val = 0;
        String opt;
        String element;

        System.out.println("=== DEVOLVER ELEMENTO ===");
        System.out.print("Introduce el código de producto: ");
        try{
            cod = sc.nextInt();
            sc.nextLine();
        }catch (InputMismatchException e){
            System.out.println("El código introducido es inválido");
        }
        element = ElementsDataBase.searchElement(Integer.toString(cod));
        if (!element.equalsIgnoreCase("-1")){
            System.out.println("Has seleccionado: ");
            System.out.println(element.replace("-", " | "));
            if(ElementsDataBase.canReturn(cod)) {
                System.out.println("¿Estas seguro?");
                System.out.print("(SI/NO)>>> ");
                opt = sc.nextLine();
                if (opt.equalsIgnoreCase("SI")) {
                    System.out.println("Nos encantaría conocer tu opinion sobre este artículo.");
                    do {
                        try {
                            Scanner sc = new Scanner(System.in);
                            System.out.print("Dale una valoración entre 1 y 5: ");
                            val = sc.nextInt();
                        } catch (InputMismatchException e) {
                            System.out.println("Has introducido una opción inválida.");
                        }
                    } while (val <= 0 || val >= 6);
                    ElementsDataBase.returnElement(Integer.toString(cod), val);
                } else if (opt.equalsIgnoreCase("NO")) {
                    System.out.println("Volviendo al menú.");
                } else {
                    System.out.println("Opción introducida inválida.");
                }
            }else {
                System.out.println("Este articulo nunca ha sido prestado.");
            }
        }else{
            System.out.println("El codigo introducido es inválido.");
        }
    }

    private static void addCopies(){
        int cod = -1;
        int val = 0;
        String opt;
        String element;

        System.out.println("=== AÑADIR COPIAS ===");
        System.out.print("Introduce el código de producto: ");
        try{
            cod = sc.nextInt();
            sc.nextLine();
        }catch (InputMismatchException e){
            System.out.println("El código introducido es inválido");
        }
        element = ElementsDataBase.searchElement(Integer.toString(cod));
        if (!element.equalsIgnoreCase("-1")){
            System.out.println("Has seleccionado: ");
            System.out.println(element.replace("-", " | "));
            System.out.println("¿Estas seguro?");
            System.out.print("(SI/NO)>>> ");
            opt = sc.nextLine();
            if (opt.equalsIgnoreCase("SI")){
                do {
                    try{
                        Scanner sc = new Scanner(System.in);
                        System.out.print("Número de copias: ");
                        val = sc.nextInt();
                    }catch (InputMismatchException e){
                        System.out.println("Has introducido una opción inválida.");
                    }
                }while(val <= 0);
                ElementsDataBase.addCopies(Integer.toString(cod), val);
            }else if (opt.equalsIgnoreCase("NO")){
                System.out.println("Volviendo al menú.");
            }else{
                System.out.println("Opción introducida inválida.");
            }
        }else{
            System.out.println("El codigo introducido es inválido.");
        }
    }

    private static void searchElement(){
        ArrayList<String> elements = ElementsDataBase.getElements();

        String key;
        boolean found = false;

        System.out.println("=== BUSCAR ===");
        System.out.print("Introduce la palabra clave que quieras buscar: ");
        key = sc.nextLine();

        System.out.println("===== BÚSQUEDA =====");
        System.out.println("COD | NOMBRE | DIRECTOR | FECHA | VALORACIÓN | COPIAS | TIPO | CATEGORIA | SUBCATEGORIA");
        for (String e : elements){
            String[] line = e.split("-");
            if (line.length > 1){
                for (String l : line) {
                    if (l.equalsIgnoreCase(key)) {
                        System.out.println(e.replace("[", "").replace("]", "").replace("-", " | "));
                        found = true;
                    }
                }
            }
        }

        if (!found){
            System.out.println("No se han encontrado resultados.");
        }

    }

    private static void displayUsers(){
        ArrayList<String> users = UsersDataBase.getNames();
        System.out.println("=== USUARIOS ===");
        System.out.println("CÓDIGO | NOMBRE");
        for (String user : users) {
            String[] line = user.split("-");
            System.out.print(line[0] + " | " + line[1]);
            if (line[3].equalsIgnoreCase("true")) {
                System.out.println(" - ADMIN -");
            } else {
                System.out.println();
            }
        }
    }

    private static void displayMenu(){

        int option;

        System.out.println("PANEL DE CONTROL");
        System.out.println("====================");
        System.out.println("1. Mostrar elementos disponibles");
        System.out.println("2. Añadir elemento");
        System.out.println("3. Quitar elemento");
        System.out.println("4. Añadir copias");
        System.out.println("5. Mantenimiento de copias");
        System.out.println("6. Prestar elemento");
        System.out.println("7. Devolver elemento");
        System.out.println("8. Buscar elemento");
        System.out.println("9. Cerrar Sesión");
        if (boss){
            System.out.println("10. Registrar usuario");
            System.out.println("11. Eliminar usuario");
            System.out.println("12. Editar usuario");
            System.out.println("13. Mostrar usuarios");
        }
        System.out.println("====================");

        try {
            Scanner sc = new Scanner(System.in);
            if (!boss){
                do {
                    System.out.print("Introduce una opción de la lista: ");
                    option = sc.nextInt();
                }while (option <= 0 || option >= 10);
            }else {
                do {
                    System.out.print("Introduce una opción de la lista: ");
                    option = sc.nextInt();
                }while (option <= 0 || option >= 14);
            }

            sc.nextLine();

            switch (option){
                case 1: showElements();
                    break;
                case 2: addElement();
                    break;
                case 3: ElementsDataBase.removeElement();
                    break;
                case 4: addCopies();
                    break;
                case 5: copyManagement();
                    break;
                case 6: lendElement();
                    break;
                case 7: returnElement();
                    break;
                case 8: searchElement();
                    break;
                case 9: logout();
                    break;
                case 10: register();
                    break;
                case 11: UsersDataBase.removeUser();
                    break;
                case 12: UsersDataBase.editContact();
                    break;
                case 13: displayUsers();
                    break;
                default:
                    System.out.println("Opcion inválida");
            }

        } catch (InputMismatchException e){
            System.out.println("Opción introducida inválida.");
        }

    }

}
