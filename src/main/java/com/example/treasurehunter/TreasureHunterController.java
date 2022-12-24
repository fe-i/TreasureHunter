package com.example.treasurehunter;

import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.Text;

public class TreasureHunterController {
    @FXML
    private Text outputField;
    @FXML
    public OutputInterface output = (info) -> {
        outputField.setText(info);
    };
    @FXML
    private Text townInfo;
    @FXML
    private Text inventory;
    @FXML
    private ProgressBar coins;
    private Town currentTown;
    private Hunter hunter;

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

        output.output("Welcome " + name + "!\nStarting the game in " + Mode.getCurrentMode() + " mode.");

        // set hunter instance variable
        hunter = new Hunter(name, Mode.getStartingGold());
        coins.setProgress(hunter.getGold() / 100.0);
    }

    /**
     * Creates a new town and adds the Hunter to it.
     */
    public void enterTown() {
        double markdown = Mode.getMarkdown();
        double toughness = Mode.getToughness();

        // note that we don't need to access the Shop object
        // outside of this method, so it isn't necessary to store it as an instance
        // variable; we can leave it as a local variable
        Shop shop = new Shop(markdown);

        // creating the new Town -- which we need to store as an instance
        // variable in this class, since we need to access the Town
        // object in other methods of this class
        currentTown = new Town(shop, toughness);

        // calling the hunterArrives method, which takes the Hunter
        // as a parameter; note this also could have been done in the
        // constructor for Town, but this illustrates another way to associate
        // an object with an object of a different class
        currentTown.hunterArrives(hunter);
    }

    @FXML
    protected void onShopClick() {
        outputField.setText("u shopped");
        currentTown.enterShop("b");
        coins.setProgress(hunter.getGold() / 100.0);
    }

    @FXML
    protected void onTroubleClick() {
        boolean bankrupt = currentTown.lookForTrouble();
        coins.setProgress(hunter.getGold() / 100.0);
        if (bankrupt) {
            output.output("You lost all of your gold in the brawl, " + hunter.getHunterName() + "! How unfortunate :(");
            /* END GAME */
        }
    }

    @FXML
    protected void onHuntClick() {
        if (!currentTown.getHasBeenSearched()) {
            boolean hasAllTreasure = hunter.addTreasure(output, currentTown.getTreasure());
            currentTown.setHasBeenSearched(true);
            if (hasAllTreasure) {
                output.output("You have successfully collected all 3 treasures! You win!");
                /* END GAME */
            }
        } else output.output("You have already searched this town.");
    }

    @FXML
    protected void onMoveClick() {
        if (Mode.isEasy() || Mode.isDev() || currentTown.leaveTown()) {
            output.output(currentTown.getLatestNews());
            enterTown();
            townInfo.setText(currentTown.toString());
        } else output.output("You're unable to leave this town.");
    }

    @FXML
    public void initialize() {
        welcomePlayer();
        enterTown();
        townInfo.setText(currentTown.toString());
    }
}