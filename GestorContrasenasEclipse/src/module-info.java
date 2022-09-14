module GestorContrasenasEclipse {
	
	requires javafx.controls;
	requires commons.codec;
	requires javafx.fxml;

	opens application to javafx.graphics, javafx.fxml;

	exports controllers;

	opens controllers to javafx.fxml;

	exports modelo;

	opens modelo to javafx.fxml;
}
