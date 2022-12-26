package com.example.treasurehunter;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
    private Text outputFieldSell;
    @FXML
    private RadioButton sellOption1,sellOption2,sellOption3,sellOption4,sellOption5,sellOption6;

    @FXML
    public OutputInterface outputBuy = (info) -> {
        outputFieldBuy.setText(info);
    };

    @FXML
    public OutputInterface outputSell = (info) -> {
        outputFieldSell.setText(info);
    };


    @FXML
    protected void onPurchaseClick(){
        RadioButton shopChoice = (RadioButton) buyOption.getSelectedToggle();
        String shopChoiceValue = shopChoice.getText();

        boolean doesExist = shop.checkMarketPrice(shopChoiceValue, true) != 0;

        if (!doesExist) {
            outputBuy.output("We ain't got none of those.");
        } else {
            if(buyButton.getText().equals("Buy")){
                // First click

                int[] costs = {
                        Shop.WATER_COST, Shop.ROPE_COST, Shop.MACHETE_COST, Shop.HORSE_COST, Shop.BOAT_COST, Shop.PENCIL_COST};
                if (Mode.isDev()) {
                    costs = new int[]{1, 1, 1, 1, 1, 1};
                }
                int idx = Integer.parseInt(shopChoice.getId().substring(9, 10));

                buyButton.setText("Pay " + costs[idx] + " gold?");
                outputBuy.output("It'll cost you " + costs[idx] + " gold. Buy it? ");
            } else {
                // Confirming
                if(shop.buyItem(shopChoiceValue)) {
                    outputBuy.output("Ye' got yerself a " + shopChoiceValue + ". Come again soon.");
                } else{
                    outputBuy.output("Hmm, either you don't have enough gold or you've already got one of those!");
                }
                buyButton.setText("Buy");
            }


        }
    }

    @FXML
    protected void onSellClick(){
        RadioButton shopChoice = (RadioButton) sellOption.getSelectedToggle();
        String shopChoiceValue = shopChoice.getText();

        int cost = shop.checkMarketPrice(shopChoiceValue, false);

        boolean doesExist = cost != 0;

        if (!doesExist) {
            outputBuy.output("We don't want none of those.");
        } else {
            if(sellButton.getText().equals("Sell")){
                // First click
                sellButton.setText("Trade for " + cost + " gold?");
                outputSell.output("It'll get you " + cost + " gold. Sell it? ");
            } else {
                // Confirming
                boolean didSell = shop.sellItem(shopChoiceValue);
                sellButton.setText("Sell");
                if (didSell) {
                    outputSell.output( "Pleasure doin' business with you." );
                } else {
                   outputSell.output("Stop stringin' me along!");
                }
            }


        }
    }


    @FXML
    public void initialize() {
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public void setHunter(Hunter hunter) {
        this.hunter = hunter;
        shop.buyItem("water");
        shop.buyItem("machete");
        RadioButton[] buttons = new RadioButton[]{sellOption1,sellOption2,sellOption3,sellOption4,sellOption5,sellOption6};

        for(int i = 0; i < 6; i++){
            RadioButton button = buttons[i];
            if(!hunter.getInventory().contains(button.getText())){
                button.setDisable(true);
            }
        }

    }
}