package org.sergio.users;

import java.util.Scanner;

public class Boss extends User{
    public Boss(String name, String password) {
        super(name, password, true);
    }

    public void addUser(){

        String name, password;

        Scanner sc = new Scanner(System.in);

        try {
            System.out.println("REGISTRAR USUARIO");
            System.out.println("=================");
            System.out.print("Introduce un nombre: ");
            name = sc.nextLine();
            System.out.print("Introduce una contraseña: ");
            password = sc.nextLine();

            users.add(new Employee(name, password));
            System.out.println("El usuario ha sido creado con éxito");
        } catch (Exception e){
            System.out.println("El usuario no ha podido ser creado, revisa los campos.");
        }

    }

    public boolean checkCredentials(String name, String password){
       return name.equals(this.name) && this.password.equals(password);
    }

}
