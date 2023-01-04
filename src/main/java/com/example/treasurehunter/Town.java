package com.example.treasurehunter;

/**
 * The Town Class is where it all happens.
 * The Town is designed to manage all of the things a Hunter can do in town.
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */
public class Town {
    //instance variables
    private Hunter hunter;
    private Shop shop;
    private Terrain terrain;
    private boolean toughTown;
    private Treasure treasure;
    private boolean hasBeenSearched;
    private OutputInterface output;

    //Constructor

    /**
     * The Town Constructor takes in a shop and the surrounding terrain, but leaves the hunter as null until one arrives.
     *
     * @param shop      The town's shoppe.
     * @param toughness The surrounding terrain.
     */
    public Town(Shop shop, double toughness, OutputInterface output) {
        this.shop = shop;
        this.terrain = getNewTerrain();
        this.output = output;

        // the hunter gets set using the hunterArrives method, which
        // gets called from a client class
        hunter = null;

        // higher toughness = more likely to be a tough town
        toughTown = Math.random() < toughness;

        // initialize town treasure
        treasure = new Treasure();
        hasBeenSearched = false;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public Treasure getTreasure() {
        return treasure;
    }

    public Boolean getHasBeenSearched() {
        return hasBeenSearched;
    }

    public void setHasBeenSearched(boolean hasBeenSearched) {
        this.hasBeenSearched = hasBeenSearched;
    }

    /**
     * Assigns an object to the Hunter in town.
     *
     * @param hunter The arriving Hunter.
     */
    public String hunterArrives(Hunter hunter) {
        this.hunter = hunter;
        String msg = "Welcome to town, " + hunter.getName() + ".";

        if (toughTown) {
            msg += "\nIt's pretty rough around here, so watch yourself.";
        } else {
            msg += "\nWe're just a sleepy little town with mild mannered folk.";
        }
        return msg;
    }

    /**
     * Handles the action of the Hunter leaving the town.
     *
     * @return true if the Hunter was able to leave town.
     */
    public boolean leaveTown() {
        String printMessage = "";
        boolean canLeaveTown = terrain.canCrossTerrain(hunter);
        if (canLeaveTown) {
            String item = terrain.getNeededItem();
            printMessage = "You used your " + item + " to cross the " + terrain.getTerrainName() + ".";
            if (checkItemBreak()) {
                hunter.removeItemFromKit(item);
                printMessage += "\nUnfortunately, your " + item + " broke.";
            }

            output.output(printMessage);
            return true;
        }
        printMessage = "You can't leave town, " + hunter.getName() + ". You don't have a " + terrain.getNeededItem() + ".";

        output.output(printMessage);
        return false;
    }

    private String hunterWinsMessage() {
        String printMessage = "";
        switch (terrain.getTerrainName()) {
            case "Mountains" ->
                    printMessage += "We have a saying in the mountains: 'Alright wow ok you win gosh', here, take my gold.";
            case "Ocean" -> printMessage += "You've earned your fins along with my gold.";
            case "Plains" -> printMessage += "Take my gold but that was just plain unfair.";
            case "Desert" -> printMessage += "Fine, i'll desert my gold with you. Good brawl.";
            case "Jungle" ->
                    printMessage += "Well done. You have earned my admiration and my gold. Also I'm a monkey!! Ooh ooh ahh ahh!!";
            case "Schools" -> printMessage += "I hope you cherish your knowledge as much as you cherish my gold.";
            default -> printMessage += "Okay, stranger! You proved yer mettle. Here, take my gold.";
        }
        return printMessage;
    }

    private String hunterLosesMessage() {
        String printMessage = "";
        switch (terrain.getTerrainName()) {
            case "Mountains" -> printMessage += "Now scram! Outta my mountains! But like gimme ur gold first!";
            case "Ocean" -> printMessage += "Swim away little fish. Leave your gold too!";
            case "Plains" -> printMessage += "It'll be hard to catch a plane after you give me all o yur money!";
            case "Desert" ->
                    printMessage += "We call that a sandy defeat because you've been dried out of cash. I never got the saying either.";
            case "Jungle" -> printMessage += "Swing to another tree, outsider. We've earned your money now!";
            case "Schools" -> printMessage += "Pay your tuition and scram!!!";
            default -> printMessage += "That'll teach you to go lookin' fer trouble in MY town! Now pay up!";
        }
        return printMessage;
    }

    /**
     * Gives the hunter a chance to fight for some gold.<p>
     * The chances of finding a fight and winning the gold are based on the toughness of the town.<p>
     * The tougher the town, the easier it is to find a fight, and the harder it is to win one.
     */
    public int lookForTrouble() {
        // 200 No Trouble
        // 201 Trouble, Won
        // 400 Trouble, Lost
        // 401 Trouble, Bankrupt
        String printMessage = "";
        double noTroubleChance;
        if (toughTown) {
            noTroubleChance = 0.66;
        } else {
            noTroubleChance = 0.33;
        }

        if (Math.random() > noTroubleChance && !Mode.isDev()) {
            printMessage = "You couldn't find any trouble";
            output.output(printMessage);
            return 200;
        } else {
            printMessage = "You want trouble, stranger! You got it!\nOof! Umph! Ow!\n\n";
            int goldDiff = Mode.isDev() ? 100 : (int) (Math.random() * 10) + 1;
            if (Math.random() > noTroubleChance || Mode.isDev()) {
                printMessage += hunterWinsMessage();
                printMessage += "\n\nYou won the brawl and receive " + goldDiff + " gold.";
                hunter.changeGold(goldDiff);
                output.output(printMessage);
                return 201;
            } else {
                printMessage += hunterLosesMessage();
                printMessage += "\n\nYou lost the brawl and pay " + goldDiff + " gold.";
                hunter.changeGold(-1 * goldDiff);
                output.output(printMessage);
                return hunter.getGold() == 0 ? 401 : 400;
            }
        }
    }

    public String toString() {
        return "This nice little town is surrounded by " + terrain.getTerrainName() + ".";
    }

    /**
     * Determines the surrounding terrain for a town, and the item needed in order to cross that terrain.
     *
     * @return A Terrain object.
     */
    private Terrain getNewTerrain() {
        double rnd = (int) (Math.random() * 6) + 1;
        if (rnd == 1) {
            return new Terrain("Mountains", "Rope");
        } else if (rnd == 2) {
            return new Terrain("Ocean", "Boat");
        } else if (rnd == 3) {
            return new Terrain("Plains", "Horse");
        } else if (rnd == 4) {
            return new Terrain("Desert", "Water");
        } else if (rnd == 5) {
            return new Terrain("Jungle", "Machete");
        } else {
            return new Terrain("Schools", "Pencil");
        }
    }

    /**
     * Determines whether or not a used item has broken.
     *
     * @return true if the item broke.
     */
    private boolean checkItemBreak() {
        double rand = Math.random();
        return (rand < 0.5);
    }
}