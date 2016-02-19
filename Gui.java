package pricetracker;

import java.io.File;
import java.util.Date;
import java.util.Optional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Control;
import javafx.scene.control.Dialog;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 * Sample application that shows how the sized of controls can be managed.
 * Sample is for demonstration purposes only, most controls are inactive.
 */
public class Gui extends Application {

	private String titleString = "PriceTracker v0.1";
	private String fileName = "C:/eclipse/workspace/PriceTracker/src/pricetracker/file.xml";
	private TrackerManager myTrackerManager;
	private ObservableList<String> items;
	private String selectedTracker;

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		Application.launch(Gui.class, args);


	}

	@Override
	public void start(Stage primaryStage) {
		System.setProperty("http.proxyHost", "alfaproxy");
		System.setProperty("http.proxyPort", "3128");

		myTrackerManager = new TrackerManager();
		myTrackerManager.addTracker("Aberlour 12",
				"http://www.whisky.de/shop/Schottland/Single-Malt/Highlands/Aberlour-Double-Cask-12-Jahre.html?listtype=search&searchparam=Aberlour");
		myTrackerManager.updatePrices();

		selectedTracker = "";

		// Make Exit button bigger by using larger font for label
		// btnExit.setStyle("-fx-font-size: 12pt;");
		// btnSave.setStyle("-fx-font-size: 12pt;");

		Scene scene = new Scene(mainPane(), 300, 400); // Manage scene size

		primaryStage.setTitle(titleString);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/*
	 * Creates the UI for the sizing sample, which demonstrates ways to manage
	 * the size of controls when you don't want the default sizes.
	 */
	private Pane mainPane() {

		BorderPane border = new BorderPane();
		border.setPadding(new Insets(20, 0, 20, 20));

		Label topLabel = new Label("PriceTracker -- Track your prices!");
		topLabel.setStyle("-fx-font-size: 14pt;");
		topLabel.setPadding(new Insets(5, 0, 20, 0));

		// ObservableList<String>
		items = FXCollections.observableArrayList(myTrackerManager.getAllNames());
		ListView<String> lvList = new ListView<>();
		lvList.setItems(items);
		lvList.setMaxHeight(Control.USE_PREF_SIZE);
		lvList.setPrefWidth(150.0);

		border.setTop(topLabel);
		border.setRight(createButtonColumn());
		border.setBottom(createButtonRow());
		border.setLeft(lvList);

		lvList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				System.out.println(
						"ListView selection changed from oldValue = " + oldValue + " to newValue = " + newValue);
				selectedTracker = newValue;
			}
		});

		return border;
	}

	/**
	 * Creates a column of buttons and makes them all the same width as the
	 * largest button.
	 */
	private VBox createButtonColumn() {

		Button btnAdd = new Button("Add");
		Button btnDelete = new Button("Delete");
		Button btnMoveUp = new Button("Move Up");
		Button btnMoveDown = new Button("Move Down");
		Button btnDetails = new Button("Show Deatails");

		btnAdd.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				newTrackerWindow();
			}
		});

		btnDetails.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				createDetailsWindow();
			}
		});
		btnDelete.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				createDeleteWindow();
			}
		});

		// Comment out the following statements to see the default button sizes
		btnAdd.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		btnDelete.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		btnMoveUp.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		btnMoveDown.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		btnMoveDown.setMinWidth(Control.USE_PREF_SIZE);

		VBox vbButtons = new VBox();
		vbButtons.setSpacing(12);
		vbButtons.setPadding(new Insets(0, 20, 10, 20));
		// btnDetails.setLineSpacing(100);

		vbButtons.getChildren().addAll(btnAdd, btnDelete, btnMoveUp, btnMoveDown, btnDetails);

		return vbButtons;
	}

	private TilePane createButtonRow() {

		// Let buttons grow, otherwise they will be different sizes based
		// on the length of the label
		// btnApply.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		Button btnLoad = new Button("Load");
		Button btnSave = new Button("Save");
		Button btnExit = new Button("Exit");
		btnSave.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		btnExit.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

		btnExit.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				closeConfirmation(btnExit);
			}
		});

		btnLoad.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				loadFromFile();
			}
		});

		btnSave.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				saveDataToFile();
			}
		});

		TilePane tileButtons = new TilePane(Orientation.HORIZONTAL);
		tileButtons.setPadding(new Insets(20, 10, 20, 0));
		tileButtons.setHgap(10.0);
		tileButtons.setVgap(12.0); // In case window is reduced and buttons
									// require another row
		// tileButtons.getChildren().addAll(btnApply, btnContinue, btnExit);
		tileButtons.getChildren().addAll(btnLoad, btnSave, btnExit);
		tileButtons.setAlignment(Pos.BOTTOM_RIGHT);

		return tileButtons;
	}

	private void createDeleteWindow() {
		Alert a = new Alert(AlertType.CONFIRMATION);
		a.setTitle(titleString);
		a.setContentText("Delete " + selectedTracker + " ?");

		Optional<ButtonType> result = a.showAndWait();
		if (result.get() == ButtonType.OK) {
			// ... user chose OK
			items.remove(selectedTracker);
			myTrackerManager.removeTracker(selectedTracker);
		} else {
			// ... user chose CANCEL or closed the dialog
			// Stage aStage = (Stage) b.getScene().getWindow();
			// aStage.close();
		}

	}

	private void createDetailsWindow() {
		Alert a = new Alert(AlertType.INFORMATION);
		a.setTitle(titleString);
		a.setContentText("selceted tracker is " + selectedTracker);

		a.showAndWait();

	}

	private void newTrackerWindow() {
		// Create the custom dialog.
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle(titleString);
		dialog.setHeaderText("Add new Tracker");

		// Set the button types.
		ButtonType saveButtonType = new ButtonType("Save", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

		// Create the username and password labels and fields.
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField name = new TextField();
		name.setPromptText("TrackerName");
		TextField url = new TextField();
		url.setPromptText("URL");

		grid.add(new Label("Name:"), 0, 0);
		grid.add(name, 1, 0);
		grid.add(new Label("URL:"), 0, 1);
		grid.add(url, 1, 1);

		// Enable/Disable login button depending on whether a username was
		// entered.
		Node loginButton = dialog.getDialogPane().lookupButton(saveButtonType);
		loginButton.setDisable(true);

		// Do some validation (using the Java 8 lambda syntax).
		name.textProperty().addListener((observable, oldValue, newValue) -> {
			loginButton.setDisable(newValue.trim().isEmpty());
		});

		dialog.getDialogPane().setContent(grid);

		// Request focus on the username field by default.
		Platform.runLater(() -> name.requestFocus());

		// Optional<Pair<String, String>> result =
		dialog.showAndWait();
		try {
			myTrackerManager.addTracker(name.getText(), url.getText());
			items.add(name.getText());
		} catch (Error e) {
			System.err.println(e.getMessage());
		}
	}

	private void closeConfirmation(Button b) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(titleString);
		alert.setHeaderText("PriceTracker Exit");
		alert.setContentText("Exit PriceTracker?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			// ... user chose OK
			Stage aStage = (Stage) b.getScene().getWindow();
			aStage.close();

		} else {
			// ... user chose CANCEL or closed the dialog
		}
	}

	public void loadFromFile() {
		File file = new File(fileName);
		try {
			JAXBContext context = JAXBContext.newInstance(TrackerManager.class);
			Unmarshaller um = context.createUnmarshaller();
			myTrackerManager = (TrackerManager) um.unmarshal(file);
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not load data");
			alert.setContentText("Could not load data from file:\n" + file.getPath());

			alert.showAndWait();
		}
	}

	/**
	 * Saves the current person data to the specified file.
	 * 
	 * @param file
	 */
	public void saveDataToFile() {
		try {
			JAXBContext context = JAXBContext.newInstance(TrackerManager.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			// m.marshal(myTrackerManager, System.out);
			// m.marshal(myTrackerManager, file);
			File f = new File(fileName);
			m.marshal(myTrackerManager, f);

		} catch (Exception e) { // catches ANY exception
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not save data");
			// alert.setContentText("Could not save data to file:\n" +
			// file.getPath() + "\n");

			alert.showAndWait();
		}
	}

	// public void makePlot() {
	// // TODO this would be the goal! But maybe better put this into controller
	// class
	// NumberAxis xAxis = new NumberAxis();
	// NumberAxis yAxis = new NumberAxis();
	//
	// LineChart trackerPlot = new LineChart(xAxis, yAxis);
	// trackerPlot.add
	//	}

}
