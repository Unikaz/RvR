package RvRUI;

import LootEntities.HealthEntity;
import RvR.Robot;
import RvR.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import static javax.swing.JOptionPane.showMessageDialog;

/**
 * Created by Unikaz on 16/01/2016.
 */
public class UI extends JFrame implements RvREvents, ListSelectionListener {

    RvR rvr;
    GameUI gameUI;

    boolean startStop = true;

    // Window's elements
    JPanel pan;
    JList jbotList;
    JButton addBtn;
    final JButton startStopBtn;

    private final String botsDir = "bots";

    public UI(){
        this.setSize(610, 580);
        this.setTitle("Robot vs Robot");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);


        // Menu
        JMenuBar menu = new JMenuBar();
        // Bouton Start/Stop
        startStopBtn = new JButton("Start");
        startStopBtn.setEnabled(false);
        menu.add(startStopBtn);
        startStopBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(startStop) {
                    rvr.startRvr();
                    startStopBtn.setText("Stop");
                    startStop = false;
                }else{
                    rvr.stopRvr();
                    init();
                    repaint();
                    startStopBtn.setEnabled(false);
                    startStopBtn.setText("Start");
                    startStop = true;
                }
            }
        });

        // Sous-menu entitées
        JMenu menuEntities = new JMenu("Entities");
        JMenuItem addHealth = new JMenuItem("Add Health");
        addHealth.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rvr.addEntity(new HealthEntity()); repaint();
            }
        });
        menuEntities.add(addHealth);
        menu.add(menuEntities);

        JButton reloadBtn = new JButton("Reload Robots list");
        menu.add(reloadBtn);
        reloadBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadBotList();
            }
        });

        JButton openEditor = new JButton("Open Robot Editor");
        menu.add(openEditor);
        openEditor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RobotEditor re = new RobotEditor();
            }
        });
        setJMenuBar(menu);


        // Panel List of robots
        JPanel listPan = new JPanel(new BorderLayout());
        listPan.setPreferredSize(new Dimension(100, 100));
        jbotList = new JList();
        jbotList.setFixedCellWidth(100);
        jbotList.setSize(100, 100);
        listPan.setSize(100, 300);
        addBtn = new JButton(">");
        addBtn.setEnabled(false);
        JButton editBtn = new JButton("E");
        JPanel listMenu = new JPanel();
        listPan.setPreferredSize(new Dimension(100, 300));
        addBtn.setPreferredSize(new Dimension(20,20));
        editBtn.setPreferredSize(new Dimension(20,20));
        listMenu.add(addBtn);
        listMenu.add(editBtn);
        listPan.add(listMenu, BorderLayout.NORTH);
        listPan.add(jbotList, BorderLayout.CENTER);
        loadBotList();

        // Les actions des boutons de la list
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Robot r = (Robot)RobotLoader.dynamicLoadRobot(jbotList.getSelectedValue().toString());
                if(r != null) {
                    rvr.addEntity(r);
                    System.out.println("[" + r.getEntityName() + "] " + r.getPresentation());
                    repaint();
                    if(!rvr.isRunning()){
                        startStopBtn.setEnabled(true);
                    }
                }else{
                    System.out.println("Can't load the robot");
                }
            }
        });
        editBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RobotEditor re = new RobotEditor();
            }
        });


        // on ajoute au JPanel général
        pan = new JPanel(new BorderLayout());
        pan.add(listPan, BorderLayout.WEST);
        this.getContentPane().add(pan);
        this.setVisible(true);


        // on balance init, ça sera fait ^^
        init();
        // et un chargement de la botlist
        loadBotList();

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


                jbotList.clearSelection();
                jbotList.setListData(validFiles.toArray());
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
        String robots = "";
        for(Entity e : rvr.getEntities()){
            if(e instanceof Robot){
                robots += "["+e.getRobotType()+"] "+ e.getEntityName() + " survive : " + e.getTimeAlive() + " - Health : " + e.getHealth() + "\n";
            }
        }
        showMessageDialog(null, "End \n"+robots);
        rvr.clearRun();
        // reset UI
        init();
        startStop = true;
        startStopBtn.setEnabled(false);
        startStopBtn.setText("Start");
        //gameUI.sendMessage("END \n" + robots);
        System.out.println("-----------------------------------------------------------");

    }


    // Event de la JList
    @Override
    public void valueChanged(ListSelectionEvent e) {
        addBtn.setEnabled(true);
    }
}
