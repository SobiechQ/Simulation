package GUI.Top;

import javax.swing.*;

public class MyJMenuBar extends JMenuBar {
    public MyJMenuBar() {
        this.add(this.getNewFileMenu());
        this.add(this.getNewViewMenu());
        this.add(this.getNewInfoMenu());
    }

    private JMenu getNewFileMenu() {
        final var menu = new JMenu("File");
        menu.add(this.getNewSaveMenu());
        return menu;
    }
    private JMenu getNewSaveMenu() {
        final var saveMenu = new JMenu("Save");
        final var save = new JMenuItem("Save");
        final var saveAs = new JMenuItem("Save as");

        saveMenu.add(save);
        saveMenu.add(saveAs);
        return saveMenu;
    }
    private JMenu getNewViewMenu() {
        final var viewMenu = new JMenu("View");
        final var debugPopupWindow = new JMenuItem("Show debug popup window");
        viewMenu.add(debugPopupWindow);
        return viewMenu;
    }
    private JMenu getNewInfoMenu() {
        final var infoMenu = new JMenu("Info");
        final var about = new JMenuItem("About");
        infoMenu.add(about);
        return infoMenu;
    }

}
