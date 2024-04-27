package Window;

import Map.Chunk;
import Map.GridManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GridFrame extends JFrame {
    private final int gridChunkWidth = 15;
    private final int gridChunkHeight = 10;
    private final int elementSize = 5;
    private final GridManager gridManager;
    private final Menu menu = new Menu();
    private final JPanel panel = new JPanel() {
        @Override
        public void paint(Graphics g) {
            super.paint(g);
            gridManager.streamRandom()
                    .forEach(link -> {
                        g.setColor(link.getElement().getColor());
                        g.fillRect(gridManager.getXReal(link) * elementSize, gridManager.getYReal(link) * elementSize, elementSize, elementSize);
                    });
            gridManager.chunkStream()
                    .forEach(chunk->{
                        g.setColor(Color.magenta);
                        g.drawRect(chunk.getChunkX() * Chunk.CHUNK_SIZE * elementSize, chunk.getChunkY() * Chunk.CHUNK_SIZE * elementSize, Chunk.CHUNK_SIZE * elementSize, Chunk.CHUNK_SIZE * elementSize);
                    });
        }

        @Override
        public void update(Graphics g) {
            paint(g);
        }
    };

    public GridFrame() {
        this.gridManager = new GridManager(gridChunkWidth, gridChunkHeight);
        JFrame frame = new JFrame("Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize((int) Math.round(gridChunkWidth * elementSize * Chunk.CHUNK_SIZE + elementSize * 3.25), gridChunkHeight * elementSize * Chunk.CHUNK_SIZE + elementSize + 35);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.add(this.panel);
        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mouseClicked(e);
                int x = (e.getX() / elementSize) - 1;
                int y = (e.getY() / elementSize) - 6;
                gridManager.streamRandom()
                        .filter(link -> link.distance(x, y) < GridFrame.this.menu.getCommandSize())
                        .forEach(l -> l.set(GridFrame.this.menu.getCommandElement()));
            }
        });

        new Thread(() -> {
            while (true) {
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
                System.out.println(difference);
            }
        }).start();
    }
}
