package com.example.treasurehunter;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;

public class ShopController {
    private Shop shop;
    private Hunter hunter;


    @FXML
    private ToggleGroup buyOption;
    @FXML
    private ToggleGroup sellOption;
    @FXML
    private Button buyButton;
    @FXML
    private Button sellButton;
    @FXML
    private Text outputFieldBuy;
    @FXML
    public OutputInterface outputBuy = (info) -> outputFieldBuy.setText(info);
    @FXML
    private Text outputFieldSell;
    @FXML
    public OutputInterface outputSell = (info) -> outputFieldSell.setText(info);
    @FXML
    private RadioButton sellOption1, sellOption2, sellOption3, sellOption4, sellOption5, sellOption6;
    @FXML
    private RadioButton buyOption1, buyOption2, buyOption3, buyOption4, buyOption5, buyOption6;
    @FXML
    private ProgressBar coinsSell;
    @FXML
    private ProgressBar coinsBuy;
    @FXML
    private Text numCoinsBuy;
    @FXML
    private Text numCoinsSell;
    private OutputInterface invOutput;
    private OutputInterface setNumCoins;

    @FXML
    protected void onPurchaseClick() {
        RadioButton shopChoice = (RadioButton) buyOption.getSelectedToggle();
        if (shopChoice != null) {

            String shopChoiceValue = shopChoice.getText();

            boolean doesExist = shop.checkMarketPrice(shopChoiceValue, true) != 0;

            if (!doesExist) {
                outputBuy.output("We ain't got none of those.");
            } else {
                if (buyButton.getText().equals("Buy")) {
                    // First click

                    int[] costs = {
                            Shop.WATER_COST, Shop.ROPE_COST, Shop.MACHETE_COST, Shop.HORSE_COST, Shop.BOAT_COST, Shop.PENCIL_COST};
                    if (Mode.isDev()) {
                        costs = new int[]{1, 1, 1, 1, 1, 1};
                    }
                    int idx = Integer.parseInt(shopChoice.getId().substring(9, 10)) - 1;

                    buyButton.setText("Pay " + costs[idx] + " gold?");
                    outputBuy.output("It'll cost you " + costs[idx] + " gold. Buy it? ");
                } else {
                    // Confirming
                    if (shop.buyItem(shopChoiceValue)) {
                        outputBuy.output("Ye' got yerself a " + shopChoiceValue + ". Come again soon.");
                    } else {
                        outputBuy.output("Hmm, you don't have enough gold!");
                    }
                    buyButton.setText("Buy");
                    updateShop();
                }
            }
        } else outputBuy.output("Select something before trying to buy it!");
    }

    @FXML
    protected void onSellClick() {
        RadioButton shopChoice = (RadioButton) sellOption.getSelectedToggle();
        if (shopChoice != null) {
            String shopChoiceValue = shopChoice.getText();

            int cost = shop.checkMarketPrice(shopChoiceValue, false);

            boolean doesExist = cost != 0;

            if (!doesExist) {
                outputBuy.output("We don't want none of those.");
            } else {
                if (sellButton.getText().equals("Sell")) {
                    // First click
                    sellButton.setText("Trade for " + cost + " gold?");
                    outputSell.output("It'll get you " + cost + " gold. Sell it? ");
                } else {
                    // Confirming
                    boolean didSell = shop.sellItem(shopChoiceValue);
                    if (didSell) {
                        outputSell.output("Pleasure doin' business with you.");
                    } else {
                        outputSell.output("Stop stringin' me along!");
                    }
                    sellButton.setText("Sell");
                    updateShop();
                }
            }
        } else outputSell.output("Select something before trying to buy it!");
    }

    @FXML
    public void initialize() {
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public void setHunter(Hunter hunter) {
        this.hunter = hunter;
    }

    public void setInvOutput(OutputInterface invOutput) {
        this.invOutput = invOutput;
    }
    public void setSetNumCoins(OutputInterface setNumCoins){
        this.setNumCoins = setNumCoins;
    }

    public void updateShop() {
        RadioButton[] sellButtons = new RadioButton[]{sellOption1, sellOption2, sellOption3, sellOption4, sellOption5, sellOption6};
        RadioButton[] buyButtons = new RadioButton[]{buyOption1, buyOption2, buyOption3, buyOption4, buyOption5, buyOption6};

        for (int i = 0; i < 6; i++) {
            RadioButton button = sellButtons[i];
            button.setSelected(false);
            button.setDisable(!hunter.getInventory().contains(button.getText()));
        }

        for (int i = 0; i < 6; i++) {
            RadioButton button = buyButtons[i];
            button.setSelected(false);
            button.setDisable(hunter.getInventory().contains(button.getText()));
        }

        coinsSell.setProgress(hunter.getGold() / 100.0);
        numCoinsBuy.setText(String.valueOf(hunter.getGold()));
        numCoinsSell.setText(String.valueOf(hunter.getGold()));
        coinsBuy.setProgress(hunter.getGold() / 100.0);
        invOutput.output(String.join("\r\n", hunter.getInventory().split(" ")));
        setNumCoins.output(String.valueOf(hunter.getGold()));
    }
}
