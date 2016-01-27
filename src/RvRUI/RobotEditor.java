package RvRUI;

import javax.swing.*;
import java.awt.*;

/**
 * Created by AlexisB on 27/01/2016.
 */
public class RobotEditor extends JFrame {

    String directoryName;
    String fileName;

    public RobotEditor(){

        // Construction fenêtre
        this.setSize(500, 500);
        this.setTitle("Robot Editor ");

        JPanel mainContainer = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        JButton btnOpen = new JButton("Open");
        JButton btnSave = new JButton("Save");
        JTextArea textArea = new JTextArea();

        this.getContentPane().add(mainContainer);
        mainContainer.add(buttonPanel, BorderLayout.NORTH);
        buttonPanel.add(btnOpen);
        buttonPanel.add(btnSave);
        mainContainer.add(textArea);

        this.setVisible(true);


        // Config des variables liées aux fichiers
        directoryName = System.getProperty("user.dir")+"bots";
    }


    public void openFile(){

    }
    public void saveFile(){

    }
}
