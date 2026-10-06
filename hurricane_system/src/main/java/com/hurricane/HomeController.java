package com.hurricane;

import java.io.IOException;

import com.model.HurricaneApplication;

import javafx.fxml.FXML;

/**
 * Controls the screen shown after logging in.
 *
 * TODO placeholder until the real home screens exist; show the user's name and options for their user type
 */
public class HomeController {

    @FXML
    private void logout() throws IOException {
        HurricaneApplication.getInstance().logout();
        App.setRoot("login");
    }
}
