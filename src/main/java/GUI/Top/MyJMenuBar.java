package GUI.Top;

import javax.swing.*;

public class MyJMenuBar extends JMenuBar {
    private final JMenu menu = new JMenu("Menu");


    public MyJMenuBar() {
        this.menu.add(new JMenuItem("Item"));
        this.menu.add(new JMenuItem("Item"));
        this.menu.add(new JMenuItem("Item"));
        this.menu.add(new JMenuItem("Item"));
        this.add(this.menu);
    }
}
