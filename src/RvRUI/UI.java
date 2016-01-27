package RvRUI;

import LootEntities.HealthEntity;
//import Robots.*;
import RvR.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

/**
 * Created by Unikaz on 16/01/2016.
 */
public class UI extends JFrame implements RvREvents, ListSelectionListener {

    RvR rvr;
    GameUI gameUI;
    JPanel pan;
    JList jbotList;

    private final String botsDir = "bots";

    public UI(){
        this.setSize(610, 580);
        this.setTitle("Robot vs Robot");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        pan = new JPanel(new BorderLayout());
        JPanel btnPan = new JPanel();
        final JTextArea console = new JTextArea();
        jbotList = new JList();
        loadBotList();


        pan.add(btnPan, BorderLayout.NORTH);
        pan.add(jbotList, BorderLayout.EAST);
        this.getContentPane().add(pan);
        this.setVisible(true);

        // Bouton Start
        JButton initBtn = new JButton("Init");
        btnPan.add(initBtn);
        initBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rvr.stopRvr();
                init();
                repaint();
            }
        });
        JButton startBtn = new JButton("Start");
        btnPan.add(startBtn);
        startBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rvr.presentation();
                rvr.startRvr();
            }
        });
        JButton addRobot1 = new JButton("Add Robot");
        btnPan.add(addRobot1);
        addRobot1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rvr.dynamicLoadRobot(jbotList.getSelectedValue().toString());
                repaint();
            }
        });

        JButton addHealth = new JButton("Add Health");
        btnPan.add(addHealth);
        addHealth.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rvr.addEntity(new HealthEntity());
                repaint();
            }
        });

        JButton reloadBtn = new JButton("Reload Robots list");
        btnPan.add(reloadBtn);
        reloadBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadBotList();
            }
        });

        JButton openEditor = new JButton("Open Robot Editor");
        btnPan.add(openEditor);
        openEditor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RobotEditor re = new RobotEditor();
            }
        });


        // RvRUI.Console
        console.setSize(250,250);
        console.setBackground(Color.white);

        // game
        //rvr.addRvRListener(this);


        // on balance init, ça sera fait ^^
        init();
        // et un chargement de la botlist
        loadBotList();
        //repaint();

    }

    public void init(){
        if(rvr != null){
            pan.remove(gameUI);
            rvr.removeRvRListener(this);
        }
        rvr = new RvR("Combat");
        gameUI = new GameUI(rvr);
        pan.add(gameUI, BorderLayout.CENTER);
        rvr.addRvRListener(this);
        this.setVisible(true);


        // C'est là qu'il faudrait charger les bots


        repaint();
    }

    public void loadBotList(){
        // On ouvre le dossier
        File dir = new File(botsDir);
        if(dir.exists()){
            if(dir.isDirectory()){
                String[] fileList = dir.list();
                ArrayList<String> validFiles = new ArrayList<>();
                for(String f : fileList){
                    if(f.split("\\.").length>1){
                        f = f.split("\\.")[0];
                        validFiles.add(f);
                    }
                }



                //jbotList = new JList(fileList);
                jbotList.clearSelection();
                jbotList.setListData(validFiles.toArray());
                //jbotList.setSize(200, 100);
                jbotList.setFixedCellWidth(100);
                jbotList.setSize(100, 500);
                pan.add(jbotList, BorderLayout.EAST);
                jbotList.removeListSelectionListener(this);
                jbotList.addListSelectionListener(this);

                repaint();
                this.setVisible(true);
            }
        }else{
            System.out.println("Can't find " + botsDir);
        }

        // on liste les objets du bon type
        // on ajoute les entrées dans la liste
    }

    // Tick du jeu
    @Override
    public void onTick() {
        this.repaint();
    }

    @Override
    public void onEnd() {

    }


    // Event de la JList
    @Override
    public void valueChanged(ListSelectionEvent e) {
        System.out.println(jbotList.getSelectedValue());

        //System.out.println();
    }
}
