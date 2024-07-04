package GUI.Center;

import GUI.Right.BrushPanel;
import GUI.Top.MyJMenuBar;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;


public class MainFrame extends JFrame {
    private final LayoutManager layout;
    private final JMenuBar menuBar;
    private final BrushPanel brushPanel;
    private final GridPanel gridPanel;


    public MainFrame() {
        System.out.println(Arrays.toString(UIManager.getInstalledLookAndFeels()));
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception _) {
            System.out.println("Failed to set look and feel");
        }
        this.layout = new BorderLayout();
        this.brushPanel = new BrushPanel(this);
        this.gridPanel = new GridPanel(this.brushPanel);
        this.menuBar = new MyJMenuBar(this.gridPanel.getGridManager());




        this.setTitle("Simulator");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(this.layout);
        this.setResizable(false);
        this.setVisible(true);

        this.add(gridPanel, BorderLayout.CENTER);
        this.add(brushPanel, BorderLayout.EAST);
        this.setJMenuBar(this.menuBar);

        this.pack();
    }
    public void save(){
        this.gridPanel.getGridManager().save();
    }
}
