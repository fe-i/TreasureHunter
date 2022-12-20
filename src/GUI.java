import java.awt.*;
import javax.swing.*;

public class GUI {
    final static boolean shouldFill = true;
    final static boolean shouldWeightX = true;
    final static boolean RIGHT_TO_LEFT = false;

    public static void addComponentsToPane(Container pane) {
        if (RIGHT_TO_LEFT) {
            pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }

        JButton button;
        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = 1;
        if (shouldFill) {
            //natural height, maximum width
            c.fill = GridBagConstraints.HORIZONTAL;
        }

        button = new JButton("Button 1");
        if (shouldWeightX) {
            c.weightx = 0.5;
        }
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        pane.add(button, c);

        button = new JButton("Button 2");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        pane.add(button, c);

        button = new JButton("Button 3");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        pane.add(button, c);

        button = new JButton("Button 4");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 3;
        pane.add(button, c);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        JLabel text = new JLabel("Reponse");
        text.setHorizontalAlignment(JLabel.CENTER);
        panel.setBackground(Color.RED);
        panel.add(text, c);
        c.gridx = 0;
        c.gridy = 4;
        c.ipady = 30;
        pane.add(panel, c);

        JPanel townInfoPanel = new JPanel();
        townInfoPanel.setLayout(new GridBagLayout());
        JLabel townInfoText = new JLabel("Reponse");
        townInfoText.setHorizontalAlignment(JLabel.CENTER);
        townInfoPanel.setBackground(Color.RED);
        townInfoPanel.add(townInfoText, c);
        c.gridx = 1;
        c.gridy = 0;
        c.ipady = 30;
        c.gridwidth = 3;
        pane.add(townInfoPanel, c);

        button = new JButton("Button new");
        c.gridwidth = 1;
        c.gridx = 2;
        c.gridy = 1;
        pane.add(button, c);
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("GridBagLayoutDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Set up the content pane.
        addComponentsToPane(frame.getContentPane());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}