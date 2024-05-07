package Window;

import Elements.Solid.Air;
import Elements.Api.Core.Element;
import Elements.Solid.Wood;
import Map.Chunk;
import Map.GridManager;
import Map.Link;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.ToIntFunction;

public class GridFrame extends JFrame {
    private final int gridChunkWidth = 20;
    private final int gridChunkHeight = 12;
    private final int elementSize = 5;
    private final GridManager gridManager;
    private final Menu menu = new Menu();
    private final JPanel panel = new JPanel() {
        @Override
        public void paint(Graphics g) {
            super.paint(g);
            gridManager.linkStream()
                    .forEach(link -> {
                        g.setColor(link.getElement().getColor());
                        g.fillRect(gridManager.getXReal(link) * elementSize, gridManager.getYReal(link) * elementSize, elementSize, elementSize);
                    });
//            gridManager.chunkStream()
//                    .forEach(chunk -> {
//                        g.setColor(new Color(255, 0, 255, 204));
//                        g.drawRect(chunk.getChunkX() * Chunk.CHUNK_SIZE * elementSize, chunk.getChunkY() * Chunk.CHUNK_SIZE * elementSize, Chunk.CHUNK_SIZE * elementSize, Chunk.CHUNK_SIZE * elementSize);
////                        if (chunk.isWorking){
////                            g.setColor(new Color(255, 0, 255, 51));
////                            g.fillRect(chunk.getChunkX() * Chunk.CHUNK_SIZE * elementSize, chunk.getChunkY() * Chunk.CHUNK_SIZE * elementSize, Chunk.CHUNK_SIZE * elementSize, Chunk.CHUNK_SIZE * elementSize);
////                        }
//                    });
//            for (int i = 0; i < Wood.noice.vectors.length; i++) {
//                for (int j = 0; j < Wood.noice.vectors[i].length; j++) {
//                    g.setColor(Color.GREEN);
//                    g.drawLine(
//                            j*elementSize*Chunk.CHUNK_SIZE,
//                            i*elementSize*Chunk.CHUNK_SIZE,
//                            (int) (j*elementSize*Chunk.CHUNK_SIZE + Wood.noice.vectors[i][j].getX() * elementSize /2),
//                            (int) (i*elementSize*Chunk.CHUNK_SIZE + Wood.noice.vectors[i][j].getY() * elementSize /2)
//                    );
//                }
//            }
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

                @SuppressWarnings("unchecked")
                final Constructor<? extends Element> constructor =
                        Arrays.stream(GridFrame.this.menu.getCommandElementClass().getConstructors())
                                .map(c -> (Constructor<? extends Element>) c)
                                .filter(c -> Arrays.equals(c.getParameterTypes(), new Class[]{Link.class}) || Arrays.equals(c.getParameterTypes(), new Class[]{}))
                                .max(Comparator.comparingInt(Constructor::getParameterCount))
                                .orElseThrow(() -> new RuntimeException("No constructor found"));

                gridManager.linkStream()
                        .filter(link -> link.distance(x, y) < GridFrame.this.menu.getCommandSize())
                        .forEach(l -> {
                            try {
                                l.set(constructor.getParameterCount() == 0 ? constructor.newInstance() : constructor.newInstance(l));
                            } catch (InvocationTargetException | InstantiationException | IllegalAccessException _) {
                                throw new RuntimeException();
                            }
                        });
            }
        });


        new Thread(() -> {
            while (true) {
                panel.repaint();

                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
        new ScheduledThreadPoolExecutor(1).scheduleAtFixedRate(() -> {
                    var count = new HashMap<Element, Set<Link>>();
                    GridFrame.this.gridManager.linkStream()
                            .forEach(l -> {
                                final var element = l.getElement();
                                count.putIfAbsent(element, new HashSet<>());
                                count.get(element).add(l);
                            });
                    count.entrySet()
                            .stream()
                            .filter(e -> e.getValue().size() != 1)
                            .peek(System.out::println)
                            .forEach(e -> e.getValue().forEach(l -> l.set(new Air())));
                }, 1000, 1000, TimeUnit.MILLISECONDS
        );
    }
}
