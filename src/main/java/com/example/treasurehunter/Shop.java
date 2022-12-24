package com.example.treasurehunter; /**
 * The Shop class controls the cost of the items in the Treasure Hunt game.<p>
 * The Shop class also acts as a go between for the Hunter's buyItem() method.<p>
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

import java.util.Scanner;

public class Shop {
    // constants
    private static final int WATER_COST = 2;
    private static final int ROPE_COST = 4;
    private static final int MACHETE_COST = 6;
    private static final int HORSE_COST = 12;
    private static final int BOAT_COST = 20;
    private static final int PENCIL_COST = 8;

    // instance variables
    private double markdown;
    private Hunter customer;

    //Constructor
    public Shop(double markdown) {
        this.markdown = markdown;
        customer = null;
    }

    /**
     * method for entering the shop
     *
     * @param hunter    the Hunter entering the shop
     * @param buyOrSell String that determines if hunter is "B"uying or "S"elling
     */
    public void enter(Hunter hunter, String buyOrSell) {
        customer = hunter;

        Scanner scanner = new Scanner(System.in);
        if (buyOrSell.equalsIgnoreCase("b")) {
            System.out.println("Welcome to the shop! We have the finest wares in town.");
            System.out.println(Colors.CYAN + "Currently we have the following items:" + Colors.RESET);
            System.out.println(Colors.PURPLE + inventory() + Colors.RESET);
            System.out.print(Colors.GREEN + "What're you lookin' to buy? " + Colors.RESET);
            String item = scanner.nextLine();
            int cost = checkMarketPrice(item, true);
            if (cost == 0) {
                System.out.println(Colors.RED + "We ain't got none of those." + Colors.RESET);
            } else {
                System.out.print(Colors.YELLOW + "It'll cost you " + cost + " gold. Buy it (y/n)? " + Colors.RESET);
                String option = scanner.nextLine();

                if (option.equals("y") || option.equals("Y")) {
                    buyItem(item);
                }
            }
        } else {
            System.out.println(Colors.GREEN + "What're you lookin' to sell? " + Colors.RESET);
            System.out.print("You currently have the following items: " + Colors.PURPLE + customer.getInventory() + Colors.RESET);
            String item = scanner.nextLine();
            int cost = checkMarketPrice(item, false);
            if (cost == 0) {
                System.out.println(Colors.RED + "We don't want none of those." + Colors.RESET);
            } else {
                System.out.print(Colors.YELLOW + "It'll get you " + cost + " gold. Sell it (y/n)? " + Colors.RESET);
                String option = scanner.nextLine();

                if (option.equals("y") || option.equals("Y")) {
                    sellItem(item);
                }
            }
        }
    }

    /**
     * A method that returns a string showing the items available in the shop (all shops sell the same items)
     *
     * @return the string representing the shop's items available for purchase and their prices
     */
    public String inventory() {
        int[] costs = {
                WATER_COST, ROPE_COST, MACHETE_COST, HORSE_COST, BOAT_COST, PENCIL_COST};
        if (Mode.isDev()) {
            costs = new int[]{1, 1, 1, 1, 1, 1};
        }
        String str = "Water: " + costs[0] + " gold\n";
        str += "Rope: " + costs[1] + " gold\n";
        str += "Machete: " + costs[2] + " gold\n";
        str += "Horse: " + costs[3] + " gold\n";
        str += "Boat: " + costs[4] + " gold\n";
        str += "Pencil: " + costs[5] + " gold\n";
        return str;
    }

    /**
     * A method that lets the customer (a Hunter) buy an item.
     *
     * @param item The item being bought.
     */
    public void buyItem(String item) {
        int costOfItem = checkMarketPrice(item, true);
        if (customer.buyItem(item, costOfItem)) {
            System.out.println(Colors.GREEN + "Ye' got yerself a " + item + ". Come again soon." + Colors.RESET);
        } else {
            System.out.println(Colors.RED + "Hmm, either you don't have enough gold or you've already got one of those!" + Colors.RESET);
        }
    }

    /**
     * A pathway method that lets the Hunter sell an item.
     *
     * @param item The item being sold.
     */
    public void sellItem(String item) {
        int buyBackPrice = checkMarketPrice(item, false);
        if (customer.sellItem(item, buyBackPrice)) {
            System.out.println(Colors.GREEN + "Pleasure doin' business with you." + Colors.RESET);
        } else {
            System.out.println(Colors.RED + "Stop stringin' me along!" + Colors.RESET);
        }
    }

    /**
     * Determines and returns the cost of buying or selling an item.
     *
     * @param item     The item in question.
     * @param isBuying Whether the item is being bought or sold.
     * @return The cost of buying or selling the item based on the isBuying parameter.
     */
    public int checkMarketPrice(String item, boolean isBuying) {
        if (Mode.isDev()) {
            return 1;
        }
        if (isBuying) {
            return getCostOfItem(item);
        } else {
            return getBuyBackCost(item);
        }
    }

    /**
     * Checks the item entered against the costs listed in the static variables.
     *
     * @param item The item being checked for cost.
     * @return The cost of the item or 0 if the item is not found.
     */
    public int getCostOfItem(String item) {
        if (item.equalsIgnoreCase("water")) {
            return WATER_COST;
        } else if (item.equalsIgnoreCase("rope")) {
            return ROPE_COST;
        } else if (item.equalsIgnoreCase("machete")) {
            return MACHETE_COST;
        } else if (item.equalsIgnoreCase("horse")) {
            return HORSE_COST;
        } else if (item.equalsIgnoreCase("boat")) {
            return BOAT_COST;
        } else if (item.equalsIgnoreCase("pencil")) {
            return PENCIL_COST;
        } else {
            return 0;
        }
    }

    /**
     * Checks the cost of an item and applies the markdown.
     *
     * @param item The item being sold.
     * @return The sell price of the item.
     */
    public int getBuyBackCost(String item) {
        return (int) (getCostOfItem(item) * markdown);
    }
}