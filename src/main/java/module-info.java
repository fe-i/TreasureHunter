module com.example.treasurehunter {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.treasurehunter to javafx.fxml;
    exports com.example.treasurehunter;
}