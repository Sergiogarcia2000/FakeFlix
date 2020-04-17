package org.sergio.backend;

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

    private static void login(){

        ArrayList<String> users = UsersDataBase.getNames();
        boss = false;

        System.out.println("--Iniciar sesión--");
        System.out.println("==================");

        System.out.print("Usuario: ");
        String user = sc.nextLine();
        System.out.print("Contrasña: ");
        String pass = sc.nextLine();

        // COMPRUEBA CADA USUARIO EN LA BASE DE DATOS Y COMPARA EL NOMBRE Y LA CONTRASEÑA
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


    /**
     * CONFIRM: UTILIZADO PARA COMPROBAR QUE EL USUARIO ESTÁ SEGURO DE LA ACCIÓN QUE LE VA A APLICAR AL ELEMENTO INDICADO
     * POR SI SE CONFUNDE DE CÓDIGO
     * @param cod CÓDIGO DEL ELEMENTO
     * @return CONFIRMACIÓN DEL USUARIO
     */
    private static boolean confirm(String cod){
        String element = ElementsDataBase.searchElement(cod);
        System.out.println("Has seleccionado: ");
        System.out.println(element.replace("-", " | "));
        System.out.println("¿Estas seguro?");
        System.out.print("(Si/No)>>> ");
        String option = sc.nextLine();
        return option.equalsIgnoreCase("si");
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
                sc.nextLine();

                for(int i = 0; i < seasons; i++){
                    String seasonName = null;
                    int chapters = 0;
                    System.out.print("Nombre de la " + (i + 1) + " temporada: ");
                    seasonName = sc.nextLine();
                    System.out.print("Número de capítulos: ");
                    chapters = sc.nextInt();
                    sc.nextLine();
                    SeasonsDataBase.saveSeason(seasonName, chapters);
                }
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

    private static void removeElement(){
        System.out.println("== ELIMINAR ELEMENTO ==");
        System.out.print("Introduce el código: ");
        String elementCode = sc.nextLine();
        String element = ElementsDataBase.searchElement(elementCode);

        if (!element.equalsIgnoreCase("-1")){

            if (confirm(elementCode)){
                ElementsDataBase.removeElement(elementCode);
                System.out.println("Se ha eliminado con éxito.");
            }else {
                System.out.println("Has seleccionado no o la entrada es inválida.");
                System.out.println("Volviendo al menú");
            }
        }else {
            System.out.println("No se ha encontrado el artículo.");
        }
    }

    private static void editElement(){
        System.out.println("== EDITAR ELEMENTO ==");
        System.out.println("== Dejar en blanco(sin espacio) el campo que no quieras modificar==");

        System.out.print("Introduce el código: ");
        String cod = sc.nextLine();
        String element = ElementsDataBase.searchElement(cod);

        if (!element.equalsIgnoreCase("-1")){

            if (confirm(cod)){
                System.out.print("Nuevo nombre: ");
                String newName = sc.nextLine();

                System.out.print("Nuevo director: ");
                String newDirector = sc.nextLine();

                int newYear;
                do {
                    System.out.print("Nuevo año (0 para no editar): ");
                    newYear = sc.nextInt();
                    if (newYear == 0) break;
                }while (newYear <= 1894 || newYear >= 2021);

                ElementsDataBase.editElement(cod, newName, newDirector, newYear);
            }else {
                System.out.println("Has seleccionado no o la entrada es inválida.");
                System.out.println("Volviendo al menú");
            }
        }
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
            System.out.println("5. Copias");

            do {
                System.out.print(">>> ");
                opc = sc.nextInt();
            }while (opc <= 0 || opc >= 6);

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
        System.out.println("COD | NOMBRE | DIRECTOR | FECHA | VALORACIÓN MEDIA | COPIAS | DISPONIBILIDAD |CATEGORÍA | SUBCATEGORÍA");
        for (String s : elements) {
            if (s.length() > 1) {
                String[] line = s.split("-");
                // COMPRUEBA SI ES UNA PELICULA PARA IMPRIMIR LA CATEGORIA Y SI SU CATEGORÍA ES ANIMACIÓN IMPRIME TAMBIÉN SU SUBCATEGORIA
                if (line[5].equalsIgnoreCase("Movie")) {
                    if (line[10].equalsIgnoreCase("Animación")){
                        System.out.println(line[0] + " | " + line[1] + " | " + line[2] + " | " + line[3] + " | " + ElementsDataBase.getValoration(line[0]) + " | " + line[4] + " | " + line[9] + " | " + line[10] + " | " + line[11]);
                    }else {
                        System.out.println(line[0] + " | " + line[1] + " | " + line[2] + " | " + line[3] + " | " + ElementsDataBase.getValoration(line[0]) + " | " + line[4] + " | " + line[9] + " | " + line[10]);
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
        System.out.println("COD | NOMBRE | DIRECTOR | FECHA | VALORACIÓN MEDIA | COPIAS | DISPONIBILIDAD | TIPO | TEMPORADAS");
        for (String element : elements) {
            if (element.length() > 1) {
                String[] line = element.split("-");

                if (line[5].equalsIgnoreCase("Serie")) {
                    System.out.println(line[0] + " | " + line[1] + " | " + line[2] + " | " + line[3] + " | " + ElementsDataBase.getValoration(line[0]) + " | " + line[4] + " | " + line[9] + " | " + line[10]);
                    found = true;
                }
            }
        }

        if (!found){
            System.out.println("NO SE HA ENCONTRADO NINGUNA SERIE");
        }
    }

    private static void showSerie(){
        String cod;
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("=== INFORMACIÓN SOBRE SERIE ===");
            System.out.print("Introduce el código de serie: ");
            cod = Integer.toString(sc.nextInt());

            String info = ElementsDataBase.searchElement(cod);
            List<String> seasons = SeasonsDataBase.getSeasons(cod);

            if (ElementsDataBase.isSerie(cod)){
                System.out.println("== COD | NOMBRE | DIRECTOR | AÑO | COPIAS | DISPONIBILIDAD | TEMPORADAS | VALORACIÓN");
                System.out.println("== " + info.replace("-", " | ") + " | " + ElementsDataBase.getValoration(cod) + " ==");
                for(int i = 0; i  < seasons.size(); i++){
                    String[] line = seasons.get(i).split("-");
                    System.out.println("  Temporada " + (i+1) + ": " + line[1]);
                    for (int j = 0; j < Integer.parseInt(line[2]); j++){
                        System.out.println("    Episodio " + (j+1));
                    }
                }
            }else {
                System.out.println("Código introducido no válido.");
            }


        }catch (InputMismatchException e ){
            System.out.println("Entrada inválida.");
        }
    }

    /**
     * MÉTODO PARA ORDENAR
     * EL ALGORITMO UTILIZADO ES UN BUBBLE SORT QUE SE HACE DE DOS MANERAS DISTINTAS DEPENDIENDO DEL FORMATO
     * EMPLEO UN TRY CATCH PARA EL MOMENTO EN EL QUE INTENTO PARSEAR UN NUMERO Y ES UNA STRING CAPTURO EL ERROR
     * Y APLICO EL MISMO ALGORITMO PERO PARA STRING
     * @param num NUM INDICA EL CAMPO POR EL QUE SE VA A ORDENAR (Ej. 0 = Código, 1 = Nombre, 2 = Director...)
     * @param format FORMATO INDICA LA FORMA EN LA QUE SE ORDENARÁ (1 = ASCENDENTE, 2 = DESCENDENTE)
     * @return LISTA CON LOS ELEMENTOS TOTALMENTE ORDENADOS
     */
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
        String cod;
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
                sc.nextLine();
            }while(opc <= 0 || opc >= 3);

            System.out.print("Introduce codigo de elemento: ");
            cod = sc.nextLine();
            element = ElementsDataBase.searchElement(cod);
            if (!element.equalsIgnoreCase("-1")){

                if (confirm(cod)){
                    if (opc == 1){
                        ElementsDataBase.substract(cod);
                        System.out.println("Elemento mandado a reparar correctamente.");
                    }else {
                        if (ElementsDataBase.canReturn(Integer.parseInt(cod))){
                            ElementsDataBase.returnElement(cod);
                            System.out.println("Se ha reestablecido un elemento correctamente.");
                        }else{
                            System.out.println("Ya estan todas las copias reestablecidas.");
                        }
                    }
                }else {
                    System.out.println("Has seleccionado no o una entrada inválida.");
                }
            }else{
                System.out.println("Código no encontrado.");
            }
        }catch (InputMismatchException e){
            System.out.println("Entrada inválida.");
        }
    }

    private static void lendElement(){
        String cod;
        String element;

        System.out.println("=== PRESTAR ELEMENTO ===");
        System.out.print("Introduce el código de producto: ");
        cod = sc.nextLine();
        element = ElementsDataBase.searchElement(cod);
        if (!element.equalsIgnoreCase("-1")){

            if (confirm(cod)){
                ElementsDataBase.substract(cod);
            }else {
                System.out.println("Has introducido no o una opción inválida");
            }
        }else{
            System.out.println("El codigo introducido es inválido.");
        }
    }

    private static void returnElement(){
        int cod = -1;
        int val = 0;
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
            if(ElementsDataBase.canReturn(cod)) {
                if (confirm(Integer.toString(cod))) {
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
                } else {
                    System.out.println("Has introducido no o una opción inválida.");
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
            if (confirm(Integer.toString(cod))){
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
            }else{
                System.out.println("Has introducido no o una opción introducida inválida.");
            }
        }else{
            System.out.println("El codigo introducido es inválido.");
        }
    }

    /**
     * BUSCA UN ELEMENTO COMPLETO A PARTIR DE UNA PALABRA CLAVE YA SEA SU NOMBRE, DIRECTOR, COPIAS...
     * COMPRUEBO CADA CAMPO DE CADA ELEMENTO CON LA PALABRA CLAVE Y SI HAY ALGUNA COINCIDENCIA
     * IMPRIME TODA LA INFORMACIÓN DEL ELEMENTO
     */
    private static void searchElement(){
        ArrayList<String> elements = ElementsDataBase.getElements();

        String key;
        boolean found = false;

        System.out.println("=== BUSCAR ===");
        System.out.print("Introduce la palabra clave que quieras buscar (Título, Año, Director...): ");
        key = sc.nextLine();

        System.out.println("===== BÚSQUEDA =====");
        System.out.println("COD | NOMBRE | DIRECTOR | FECHA | COPIAS | TIPO | CATEGORIA | SUBCATEGORIA | VALORACIÓN");
        for (String e : elements){
            String[] line = e.split("-");
            if (line.length > 1){
                for (String l : line) {
                    if (l.equalsIgnoreCase(key)) {
                        System.out.println(ElementsDataBase.searchElement(line[0]).replace("-", " | ") + " | " + ElementsDataBase.getValoration(line[0]));
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

    private static void toggleAvailability(){
        int cod;
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("=== CAMBIAR DISPONIBILIDAD ===");
            System.out.print("Introduce el código: ");
            cod = sc.nextInt();
            if (confirm(Integer.toString(cod))){
                ElementsDataBase.toggleAvailability(Integer.toString(cod));
            }else {
                System.out.println("Has introducido no o una opción inválida.");
            }
        }catch (InputMismatchException e){
            System.out.println("Entrada introducida inválida.");
        }
    }

    private static void displayMenu(){

        int option;

        System.out.println("PANEL DE CONTROL");
        System.out.println("====================");
        System.out.println("1. Mostrar elementos disponibles");
        System.out.println("2. Mostrar información de serie");
        System.out.println("3. Añadir elemento");
        System.out.println("4. Quitar elemento");
        System.out.println("5. Añadir copias");
        System.out.println("6. Mantenimiento de copias");
        System.out.println("7. Prestar elemento");
        System.out.println("8. Devolver elemento");
        System.out.println("9. Disponibilidad");
        System.out.println("10. Buscar elemento");
        System.out.println("11. Modificar elemento");
        System.out.println("12. Cerrar Sesión");
        System.out.println("13. Salir del programa");
        if (boss){
            System.out.println("14. Registrar usuario");
            System.out.println("15. Eliminar usuario");
            System.out.println("16. Editar usuario");
            System.out.println("17. Mostrar usuarios");
        }
        System.out.println("====================");

        try {
            Scanner sc = new Scanner(System.in);
            if (!boss){
                do {
                    System.out.print("Introduce una opción de la lista: ");
                    option = sc.nextInt();
                }while (option <= 0 || option >= 13);
            }else {
                do {
                    System.out.print("Introduce una opción de la lista: ");
                    option = sc.nextInt();
                }while (option <= 0 || option >= 18);
            }

            sc.nextLine();

            switch (option){
                case 1: showElements();
                    break;
                case 2: showSerie();
                    break;
                case 3: addElement();
                    break;
                case 4: removeElement();
                    break;
                case 5: addCopies();
                    break;
                case 6: copyManagement();
                    break;
                case 7: lendElement();
                    break;
                case 8: returnElement();
                    break;
                case 9: toggleAvailability();
                    break;
                case 10: searchElement();
                    break;
                case 11: editElement();
                    break;
                case 12: logout();
                    break;
                case 13: System.exit(1);
                    break;
                case 14: register();
                    break;
                case 15: UsersDataBase.removeUser();
                    break;
                case 16: UsersDataBase.editContact();
                    break;
                case 17: displayUsers();
                    break;
                default:
                    System.out.println("Opcion inválida");
            }

        } catch (InputMismatchException e){
            System.out.println("Opción introducida inválida.");
        }

    }
}
