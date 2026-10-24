module com.hurricane {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires javafx.web;

    opens com.hurricane to javafx.fxml;
    exports com.hurricane;

    opens com.model to javafx.fxml;
    exports com.model;
    exports com.model.users;
    exports com.model.location;
}
