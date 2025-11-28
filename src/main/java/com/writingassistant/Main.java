package com.writingassistant;

import com.writingassistant.controller.MainController;
import com.writingassistant.view.MainFrame;
import javax.swing.SwingUtilities;

/**
 * Main entry point for the Writing Assistant application.
 * Initializes the MVC architecture and launches the GUI.
 */
public class Main {
    public static void main(String[] args) {
        // SwingUtilities.invokeLater ensures GUI creation happens on EDT
        // This is critical for thread safety in Swing applications
        SwingUtilities.invokeLater(() -> {
            // Create the View (GUI window)
            MainFrame view = new MainFrame();

            // Create the Controller (connects Model and View)
            // Controller automatically initializes the Model components
            MainController controller = new MainController(view);

            // Display the window to the user
            view.setVisible(true);
        });
    }
}