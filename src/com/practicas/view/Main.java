/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.practicas.view;

import com.practicas.service.AuthService;
import com.practicas.service.PracticaService;

public class Main {

    public static void main(String[] args) {
        
        javax.swing.SwingUtilities.invokeLater(() -> {
            new FrmPrincipal().setVisible(true);
        });
}
}