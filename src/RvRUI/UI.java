package RvRUI;

import LootEntities.HealthEntity;
import Robots.*;
import RvR.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by Unikaz on 16/01/2016.
 */
public class UI extends JFrame implements RvREvents {

    RvR rvr;
    GameUI gameUI;
    JPanel pan;
    JList<String> jbotList;

    private final String botsDir = "/Users/AlexisB/Desktop/_Autres/_Java/rvr/bots";

    public UI(){
        this.setSize(520, 580);
        this.setTitle("Robot vs Robot");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        pan = new JPanel(new BorderLayout());
        //gameUI = new GameUI(rvr);
        JPanel panBotList = new JPanel();
        JPanel panRight = new JPanel();
        JPanel btnPan = new JPanel();
        final JTextArea console = new JTextArea();

        // Code panBotList
        jbotList = new JList();
        jbotList.setFixedCellWidth(100);
        panBotList.add(jbotList);

        pan.add(btnPan, BorderLayout.NORTH);
        //pan.add(console, BorderLayout.SOUTH);
        pan.add(panBotList, BorderLayout.WEST);
        pan.add(panRight, BorderLayout.EAST);
        this.getContentPane().add(pan);
        this.setVisible(true);

        // Bouton Start
        JButton initBtn = new JButton("Init");
        btnPan.add(initBtn, BorderLayout.NORTH);
        initBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               init();
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
        JButton stopBtn = new JButton("Stop");
        btnPan.add(stopBtn);
        stopBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rvr.stopRvr();
            }
        });
        JButton addRobot1 = new JButton("Add R1");
        btnPan.add(addRobot1);
        addRobot1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rvr.addEntity(new R1());
                repaint();
            }
        });
        JButton addRobot2 = new JButton("Add R2");
        btnPan.add(addRobot2);
        addRobot2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rvr.addEntity(new R2());
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
        /*
        JButton reloadBtn = new JButton("Reload Robots list");
        btnPan.add(reloadBtn);
        reloadBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadBotList();
            }
        });
        */

        // RvRUI.Console
        console.setSize(250,250);
        console.setBackground(Color.white);

        // game
        //rvr.addRvRListener(this);


        // on balance init, ça sera fait ^^
        init();
        // et un chargement de la botlist
        //loadBotList();
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
                jbotList = new JList<>(fileList);
                System.out.println(fileList.toString());

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
}
