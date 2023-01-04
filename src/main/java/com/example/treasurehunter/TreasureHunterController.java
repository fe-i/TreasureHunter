package com.example.treasurehunter;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

public class TreasureHunterController {
    @FXML
    private Text outputField;
    @FXML
    public OutputInterface output = (info) -> outputField.setText(info);
    @FXML
    private Text townInfo;
    @FXML
    private Text inventory;
    @FXML
    private ImageView bronzeIcon, silverIcon, folwelliumIcon;
    @FXML
    private Text numCoins;
    @FXML
    private ProgressBar coins;
    @FXML
    public OutputInterface invOutput = (info) -> {
        if (info.length() > 0) inventory.setText(info);
        else inventory.setText("Your inventory is empty!");
    };
    @FXML
    public OutputInterface setNumCoins = (info) -> {
        numCoins.setText(info);
        coins.setProgress(Integer.parseInt(info) / 100.0);
    };
    @FXML
    private ImageView background;

    private Town currentTown;
    private Shop shop;
    private Parent shopRoot;
    private Scene shopScene;
    private Hunter hunter;
    private boolean isFighting;

    @FXML
    private ImageView hunterImage;
    @FXML
    private ImageView badGuyImage;

    public void welcomePlayer() {
        TextInputDialog nameDialog = new TextInputDialog("Hunter");
        nameDialog.setTitle("Treasure Hunter Dialog");
        nameDialog.setHeaderText("Welcome to TREASURE HUNTER!\nGoing hunting for the big treasure, eh?");
        nameDialog.setContentText("What's your name?");
        nameDialog.showAndWait();
        String name = nameDialog.getEditor().getText();

        TextInputDialog modeDialog = new TextInputDialog("Normal");
        modeDialog.setTitle("Treasure Hunter Dialog");
        modeDialog.setHeaderText("Choose your difficulty.\n[E]asy\n[N]ormal\n[H]ard");
        modeDialog.setContentText("Mode:");
        modeDialog.showAndWait();
        String modeChoice = modeDialog.getEditor().getText();

        /*Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to TREASURE HUNTER!");
        System.out.println("Going hunting for the big treasure, eh?");
        System.out.print("What's your name, Hunter? ");
        String name = scanner.nextLine();

        System.out.println("Choose your mode: " + Colors.GREEN + "(E)" + Colors.RESET + "asy " + Colors.YELLOW + "(N)" + Colors.RESET + "ormal " + Colors.RED + "(H)" + Colors.RESET + "ard");
        System.out.print("> ");
        String modeChoice = scanner.nextLine();*/
        if (modeChoice.equals("APCSAhacks")) {
            Mode.setCurrentMode(Mode.DEV_MODE);
        } else if (modeChoice.substring(0, 1).equalsIgnoreCase("h")) {
            Mode.setCurrentMode(Mode.HARD_MODE);
        } else if (modeChoice.substring(0, 1).equalsIgnoreCase("n")) {
            Mode.setCurrentMode(Mode.NORMAL_MODE);
        } else if (modeChoice.substring(0, 1).equalsIgnoreCase("e")) {
            Mode.setCurrentMode(Mode.EASY_MODE);
        }

        //System.out.println("Starting on " + Colors.BLUE + Mode.getCurrentMode() + Colors.RESET + " mode.");
        output.output("Starting the game in " + Mode.getCurrentMode() + " mode.");

        // set hunter instance variable
        hunter = new Hunter(name, Mode.getStartingGold());
        setNumCoins.output(String.valueOf(hunter.getGold()));
    }

    /**
     * Creates a new town and adds the Hunter to it.
     */
    public void enterTown() throws URISyntaxException {
        double markdown = Mode.getMarkdown();
        double toughness = Mode.getToughness();

        // note that we don't need to access the Shop object
        // outside this method, so it isn't necessary to store it as an instance
        // variable; we can leave it as a local variable
        shop = new Shop(markdown);
        shop.enter(hunter, "b");

        try {
            FXMLLoader loader = new FXMLLoader(TreasureHunterController.class.getResource("shop-view.fxml"));
            shopRoot = loader.load();
            shopScene = new Scene(shopRoot, 600, 400);
            ShopController controller = loader.getController();
            controller.setShop(shop);
            controller.setHunter(hunter);
            controller.setInvOutput(invOutput);
            controller.setSetNumCoins(setNumCoins);
            controller.updateShop();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // creating the new Town -- which we need to store as an instance
        // variable in this class, since we need to access the Town
        // object in other methods of this class
        currentTown = new Town(shop, toughness, output);

        // calling the hunterArrives method, which takes the Hunter
        // as a parameter; note this also could have been done in the
        // constructor for Town, but this illustrates another way to associate
        // an object with an object of a different class
        String arriveMsg = currentTown.hunterArrives(hunter);
        output.output(outputField.getText() + "\n" + arriveMsg);

        Image newBackground = new Image(
                Objects.requireNonNull(
                        getClass().getResource(
                                currentTown.getTerrain().getTerrainName().toLowerCase() + ".png"
                        )
                ).toURI().toString()
        );
        background.setImage(newBackground);
    }

    @FXML
    protected void onShopClick() {
        Stage stage = new Stage();
        stage.setTitle("Town Shop");
        stage.setScene(shopScene);
        stage.setResizable(false);
        stage.show();

//        currentTown.enterShop("b");
    }

    @FXML
    protected void onTroubleClick() {
        if(isFighting) return;

        isFighting = true;
        int troubleCode = currentTown.lookForTrouble();

        FadeTransition BGin = new FadeTransition(Duration.millis(1000), badGuyImage);
        BGin.setFromValue(0);
        BGin.setToValue(1);

        FadeTransition BGout = new FadeTransition(Duration.millis(1000), badGuyImage);
        BGout.setFromValue(1);
        BGout.setToValue(0);

        RotateTransition BGfall = new RotateTransition(Duration.millis(500), badGuyImage);
        BGfall.setByAngle(-90);

        RotateTransition BGrise = new RotateTransition(Duration.millis(500), badGuyImage);
        BGrise.setByAngle(90);

        RotateTransition GGfall = new RotateTransition(Duration.millis(500), hunterImage);
        GGfall.setByAngle(90);

        RotateTransition GGrise = new RotateTransition(Duration.millis(500), hunterImage);
        GGrise.setByAngle(-90);

        SequentialTransition winAnim = new SequentialTransition (
                BGin,
                new PauseTransition(Duration.millis(1000)),
                BGfall,
                new PauseTransition(Duration.millis(1000)),
                BGout,
                BGrise
        );

        SequentialTransition loseAnim = new SequentialTransition (
                BGin,
                new PauseTransition(Duration.millis(1000)),
                GGfall,
                new PauseTransition(Duration.millis(1000)),
                BGout,
                GGrise
        );

        EventHandler<ActionEvent> onEnd = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                isFighting = false;
            }
        };

        winAnim.setOnFinished(onEnd);
        loseAnim.setOnFinished(onEnd);

        if(troubleCode == 201) winAnim.play();
        else if(troubleCode >= 400) loseAnim.play();
        else isFighting=false;


        setNumCoins.output(String.valueOf(hunter.getGold()));
        if (troubleCode == 401) {
            // Bankrupt
            //output.output("You lost all of your gold in the brawl, " + hunter.getName() + "! How unfortunate :(");
            endGame("You lost all of your gold in the brawl, " + hunter.getName() + "! How unfortunate...");
        }
    }

    @FXML
    protected void onHuntClick() {
        if (!currentTown.getHasBeenSearched()) {
            boolean hasAllTreasure = hunter.addTreasure(output, currentTown.getTreasure());
            currentTown.setHasBeenSearched(true);
            String treasureList = hunter.getTreasures().toLowerCase();
            if (treasureList.length() > 0) {
                ColorAdjust brighten = new ColorAdjust();
                brighten.setBrightness(0);
                if (treasureList.contains("bronze")) bronzeIcon.setEffect(brighten);
                if (treasureList.contains("silver")) silverIcon.setEffect(brighten);
                if (treasureList.contains("folwellium")) folwelliumIcon.setEffect(brighten);
            }
            if (hasAllTreasure) {
                //output.output("You have successfully collected all 3 treasures! You win!");
                endGame("You have successfully collected all 3 treasures! You win!");
            }
        } else output.output("You have already searched this town.");
    }

    @FXML
    protected void onMoveClick() throws URISyntaxException {
        if (Mode.isEasy() || Mode.isDev() || currentTown.leaveTown()) {
            enterTown();
            townInfo.setText(currentTown.toString());

        } else output.output("You're unable to leave this town. You need a " + currentTown.getTerrain().getNeededItem() + ".");
    }

    public void endGame(String message) {
        Alert alert = new Alert(Alert.AlertType.NONE, "Game Over", ButtonType.CLOSE);
        alert.setTitle("Game Over");
        alert.setContentText(message);
        alert.showAndWait();
        System.exit(0);
    }

    @FXML
    public void initialize() throws URISyntaxException {
        welcomePlayer();
        enterTown();
        townInfo.setText(currentTown.toString());
        invOutput.output("");


        ColorAdjust blackout = new ColorAdjust();
        blackout.setBrightness(-1.0);

        bronzeIcon.setEffect(blackout);
        silverIcon.setEffect(blackout);
        folwelliumIcon.setEffect(blackout);

        isFighting = false;
    }
}
