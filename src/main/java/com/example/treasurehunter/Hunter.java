package com.example.treasurehunter;

/**
 * Hunter Class
 * This class represents the treasure hunter character (the player) in the Treasure Hunt game.
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */
public class Hunter {
    //instance variables
    private String name;
    private String inventory;
    private String treasures;
    private int gold;

    //constructor

    /**
     * The base constructor of a Hunter assigns the name to the hunter and an empty kit.
     *
     * @param name The hunter's name.
     */
    public Hunter(String name, int startingGold) {
        this.name = name;
        inventory = "";
        treasures = "";
        gold = startingGold;
    }

    //Accessors
    public String getName() {
        return name;
    }

    public String getInventory() {
        return inventory;
    }

    public int getGold() {
        return gold;
    }

    public boolean addTreasure(OutputInterface output, Treasure treasure) {
        if (treasure.getName() == "Sand")
            output.output("You thought you struck gold but in reality it was just a pile of sand. What a bummer.");
        else if (!treasures.contains(treasure.getName())) {
            treasures += treasure.getName() + " ";
            output.output("You stumbled upon a valuable piece of " + treasure.getName().toLowerCase() + " and decided to add it to your treasure collection.");
        } else
            output.output("You already have a piece of " + treasure.getName().toLowerCase() + " so you tossed it away.");
        /* END GAME */
        return treasures.split(" ").length == 3;
    }

    public void changeGold(int modifier) {
        gold += modifier;
        if (gold < 0) {
            gold = 0;
        }
    }

    /**
     * Buys an item from a shop.
     *
     * @param item       The item the hunter is buying.
     * @param costOfItem the cost of the item
     * @return true if the item is successfully bought.
     */
    public boolean buyItem(String item, int costOfItem) {
        if (costOfItem == 0 || gold < costOfItem || hasItem(item)) {
            return false;
        }
        gold -= costOfItem;
        return addItem(item.substring(0, 1).toUpperCase() + item.substring(1).toLowerCase());
    }

    /**
     * The Hunter is selling an item to a shop for gold.<p>
     * This method checks to make sure that the seller has the item and that the seller is getting more than 0 gold.
     *
     * @param item         The item being sold.
     * @param buyBackPrice the amount of gold earned from selling the item
     * @return true if the item was successfully sold.
     */
    public boolean sellItem(String item, int buyBackPrice) {
        if (buyBackPrice <= 0 || !hasItem(item)) {
            return false;
        }
        gold += buyBackPrice;
        removeItemFromKit(item);
        return true;
    }

    /**
     * Removes an item from the kit.
     *
     * @param item The item to be removed.
     */
    public void removeItemFromKit(String item) {
        // if item is found
        if (hasItem(item)) {
            int itmIdx = inventory.toLowerCase().indexOf(item.toLowerCase());
            int endIdx = inventory.indexOf(" ", itmIdx);
            String tmpKit = inventory.substring(0, itmIdx);
            tmpKit += inventory.substring(endIdx + 1);

            // update kit
            inventory = tmpKit;
        }
    }

    /**
     * Checks to make sure that the item is not already in the inventory.
     * If not, it adds an item to the end of the String representing the hunter's inventory.
     *
     * @param item The item to be added to the inventory.
     * @return true if the item is not in the inventory and has been added.
     */
    private boolean addItem(String item) {
        if (!hasItem(item)) {
            inventory += item + " ";
            return true;
        }
        return false;
    }

    /**
     * Searches the inventory String for a specified item.
     *
     * @param item The search item
     * @return true if the item is found.
     */
    public boolean hasItem(String item) {
        return inventory.toLowerCase().contains(item.toLowerCase());
    }

    /**
     * Returns a printable representation of the inventory, which
     * is a list of the items in kit, with the KIT_DELIMITER replaced with a space
     *
     * @return The printable String representation of the inventory
     * @deprecated it's now called getInventory
     */
    public String getKit() {
        return inventory;
    }

    /**
     * @return A string representation of the hunter.
     */
    public String toString() {
        String str = name + " has " + gold + " gold";
        if (!inventory.equals("")) {
            str += " and " + getInventory();
        }
        return str;
    }
}