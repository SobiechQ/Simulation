package Window;

import Map.Grid;
import Elements.*;

import javax.swing.*;
import java.awt.*;

public class GridFrame extends JFrame {
    private final int gridSize = 100;
    private final int elementSize = 8;
    private final Grid grid;
    private final JPanel canvas = new JPanel(){
@Override
        public void paint(Graphics g) {
            super.paint(g);
            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    var optionalElement = grid.getElement(i, j);
                    if (optionalElement.isEmpty()) continue;
                    var element = optionalElement.get();
                    g.setColor(element.getColor());
                    g.fillRect(i * elementSize, j * elementSize, elementSize, elementSize);
                }
            }
        }

        @Override
        public void update(Graphics g) {
            paint(g);
        }
    };
    public GridFrame(){
        this.grid = new Grid(gridSize);
        JFrame frame = new JFrame("Hello world");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize((int) Math.round(gridSize * elementSize + elementSize*1.5), gridSize * elementSize + elementSize + 28);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.add(canvas);

        new Thread(()->{
            while (true){
                canvas.repaint();
                System.out.println("repaint");

                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.grid.nextFrame();
            }
        }).start();
    }

}
