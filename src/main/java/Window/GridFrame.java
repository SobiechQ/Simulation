package Window;

import Elements.Stone;
import Elements.Tnt;
import Map.GridManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

public class GridFrame extends JFrame {
    private final int gridSize = 150;
    private final int elementSize = 5;
    private final GridManager gridManager;
    private final Menu menu = new Menu();
    private final JPanel panel = new JPanel(){
@Override
        public void paint(Graphics g) {
            super.paint(g);
            gridManager.stream()
                    .forEach(link -> {
                        g.setColor(link.getElement().getColor());
                        g.fillRect(link.getX() * elementSize, link.getY() * elementSize, elementSize, elementSize);
                    });
        }
        @Override
        public void update(Graphics g) {
            paint(g);
        }
    };
    public GridFrame(){
        this.gridManager = new GridManager(gridSize);
        JFrame frame = new JFrame("Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize((int) Math.round(gridSize * elementSize + elementSize*3.25), gridSize * elementSize + elementSize + 35);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.add(this.panel);
        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mouseClicked(e);
                int x = (e.getX() / elementSize) - 1;
                int y = (e.getY() / elementSize) - 6;
                gridManager.getGrid().stream()
                        .filter(link -> link.distance(x, y) < GridFrame.this.menu.getCommandSize())
                        .forEach(l-> l.set(GridFrame.this.menu.getCommandElement()));
            }
        });

        new Thread(()->{
            while (true){
                panel.repaint();
                try {
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                long start_time = System.nanoTime();
                this.gridManager.nextFrame();
                long end_time = System.nanoTime();
                double difference = (end_time - start_time) / 1e6;
//                System.out.println(difference);
            }
        }).start();
    }
}
