/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.practicas.view;

import com.practicas.model.Practica;
import com.practicas.model.RolUsuario;
import com.practicas.model.Usuario;
import com.practicas.service.AuthService;
import com.practicas.service.PracticaService;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);


String usuarioBD = "GestionP";
String passwordBD = "GestionP";
String servicioBD = "orcl";

java.sql.Connection con = null;

try {

    con = com.practicas.util.DatabaseConnection.getConnection(
            usuarioBD,
            passwordBD,
            servicioBD
    );

    System.out.println("Conexión exitosa con Oracle.\n");

} catch (Exception e) {

    System.out.println("Error de conexión: " + e.getMessage());
    return;
}

        AuthService authService = new AuthService(con);
        PracticaService practicaService = new PracticaService();

        int opcion;

        do {
            System.out.println("\n SISTEMA DE GESTIÓN DE PRÁCTICAS ");
            System.out.println("1. Registrar usuario");
            System.out.println("2. Crear práctica");
            System.out.println("3. Mostrar usuarios");
            System.out.println("4. Mostrar prácticas");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {

                case 1:
                    Usuario usuario = new Usuario();

                    System.out.print("Nombre: ");
                    usuario.setNombre(scanner.nextLine());

                    System.out.print("Apellido: ");
                    usuario.setApellido(scanner.nextLine());

                    System.out.print("Email: ");
                    usuario.setEmail(scanner.nextLine());

                    System.out.print("Contraseña: ");
                    usuario.setPasswordHash(scanner.nextLine());

                    usuario.setRol(RolUsuario.ESTUDIANTE);
                    usuario.setActivo(true);

                    if (authService.registrarUsuario(usuario)) {
                        System.out.println("Usuario registrado correctamente.");
                    } else {
                        System.out.println("No se pudo registrar el usuario.");
                    }
                    break;

                case 2:
                    Practica practica = new Practica();

                    System.out.print("Nombre de la práctica: ");
                    practica.setNombre(scanner.nextLine());

                    System.out.print("Número de práctica (1-8): ");
                    practica.setNumeroPractica(scanner.nextInt());
                    scanner.nextLine();

                    System.out.print("Horas reglamentarias: ");
                    practica.setHorasReglamentarias(scanner.nextInt());
                    scanner.nextLine();

                    practicaService.crearPractica(practica);
                    break;

                case 3:
                    authService.mostrarUsuarios();
                    break;

                case 4:
                    practicaService.mostrarPracticas();
                    break;

                case 5:
                    System.out.println("Sistema finalizado.");
                    break;

                default:
                    System.out.println("Opción inválida.");
            }

        } while (opcion != 5);
    try {
        con.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
        scanner.close();
    }
}