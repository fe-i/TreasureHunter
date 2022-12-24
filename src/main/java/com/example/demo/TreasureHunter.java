package com.example.demo; /**
 * This class is responsible for controlling the Treasure Hunter game.<p>
 * It handles the display of the menu and the processing of the player's choices.<p>
 * It handles all of the display based on the messages it receives from the Town object.
 * <p>
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

import java.util.Scanner;

public class TreasureHunter {

    //Instance variables
    private Town currentTown;
    private Hunter hunter;

    //Constructor

    /**
     * Constructs the Treasure Hunter game.
     */
    public TreasureHunter() {
        // these will be initialized in the play method
        currentTown = null;
        hunter = null;
    }

    // starts the game; this is the only public method
    public void play() {
        welcomePlayer();
        enterTown();
        //showMenu();
    }

    /**
     * Creates a hunter object at the beginning of the game and populates the class member variable with it.
     */
    private void welcomePlayer() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to TREASURE HUNTER!");
        System.out.println("Going hunting for the big treasure, eh?");
        System.out.print("What's your name, Hunter? ");
        String name = scanner.nextLine();

        System.out.println("Choose your mode: " + Colors.GREEN + "(E)" + Colors.RESET + "asy " + Colors.YELLOW + "(N)" + Colors.RESET + "ormal " + Colors.RED + "(H)" + Colors.RESET + "ard");
        System.out.print("> ");
        String modeChoice = scanner.nextLine();
        if (modeChoice.equals("APCSAhacks")) {
            Mode.setCurrentMode(Mode.DEV_MODE);
        } else if (modeChoice.equalsIgnoreCase("h")) {
            Mode.setCurrentMode(Mode.HARD_MODE);
        } else if (modeChoice.equalsIgnoreCase("n")) {
            Mode.setCurrentMode(Mode.NORMAL_MODE);
        } else if (modeChoice.equalsIgnoreCase("e")) {
            Mode.setCurrentMode(Mode.EASY_MODE);
        }
        System.out.println("Starting on " + Colors.BLUE + Mode.getCurrentMode() + Colors.RESET + " mode.");

        // set hunter instance variable
        hunter = new Hunter(name, Mode.getStartingGold());
    }

    /**
     * Creates a new town and adds the Hunter to it.
     */
    private void enterTown() {
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

    /**
     * Displays the menu and receives the choice from the user.<p>
     * The choice is sent to the processChoice() method for parsing.<p>
     * This method will loop until the user chooses to exit.
     */
    private void showMenu() {
        Scanner scanner = new Scanner(System.in);
        String choice = "";

        while (!(choice.equals("X") || choice.equals("x"))) {
            System.out.println();
            System.out.println(currentTown.getLatestNews());
            System.out.println("***");
            System.out.println(hunter);
            System.out.println(currentTown);
            System.out.println("(B)uy something at the shop.");
            System.out.println("(S)ell something at the shop.");
            System.out.println("(L)ook for trouble!");
            System.out.println("(H)unt for treasure!");
            System.out.println("(M)ove on to a different town.");
            System.out.println("Give up the hunt and e(X)it.");
            System.out.println();
            System.out.print("What's your next move? ");
            choice = scanner.nextLine();
            boolean endGame = processChoice(choice);
            if (endGame) break;
        }
    }

    /**
     * Takes the choice received from the menu and calls the appropriate method to carry out the instructions.
     *
     * @param choice The action to process.
     */
    private boolean processChoice(String choice) {
        if (choice.equalsIgnoreCase("b") || choice.equalsIgnoreCase("s")) {
            currentTown.enterShop(choice);
        } else if (choice.equalsIgnoreCase("m")) {
            if (currentTown.leaveTown() || Mode.isEasy()) {
                //This town is going away so print its news ahead of time.
                System.out.println(currentTown.getLatestNews());
                enterTown();
            }
        } else if (choice.equalsIgnoreCase("l")) {
            boolean bankrupt = currentTown.lookForTrouble();
            if (bankrupt) {
                System.out.println("You lost all of your gold in the brawl, " + hunter.getHunterName() + "! How unfortunate :(");
            }
            return bankrupt;
        } else if (choice.equalsIgnoreCase("h")) {
            if (!currentTown.getHasBeenSearched()) {
//                boolean hasAllTreasure = hunter.addTreasure(currentTown.getTreasure());
                currentTown.setHasBeenSearched(true);
//                if (hasAllTreasure) {
//                    System.out.println("You have successfully collected all 3 treasures! You win!");
//                }
//                return hasAllTreasure;
            } else System.out.println("You have already searched this town.");
        } else if (choice.equalsIgnoreCase("x")) {
            System.out.println("Fare thee well, " + hunter.getHunterName() + "!");
        } else {
            System.out.println("Yikes! That's an invalid option! Try again.");
        }
        return false;
    }
}