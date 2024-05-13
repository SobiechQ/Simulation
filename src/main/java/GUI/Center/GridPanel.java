package GUI.Center;

import Elements.Api.Core.Element;
import GUI.Right.BrushPanel;
import Map.Chunk;
import Map.GridManager;
import Map.Link;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GridPanel extends JPanel {
    private final int gridChunkWidth = 20;
    private final int gridChunkHeight = 12;
    private final int elementSize = 5;
    private final GridManager gridManager;
    private final BrushPanel brushPanel;

    public GridPanel(BrushPanel brushPanel) {
        this.brushPanel = brushPanel;
        this.gridManager = new GridManager(gridChunkWidth, gridChunkHeight);
        this.setBackground(Color.BLACK);

        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

                final int x = (e.getX() / elementSize);
                final int y = (e.getY() / elementSize);

                @SuppressWarnings("unchecked")
                final Constructor<? extends Element> constructor =
                        Arrays.stream(GridPanel.this.brushPanel.getBrushProperties().getElementClass().getConstructors())
                                .map(c -> (Constructor<? extends Element>) c)
                                .filter(c -> Arrays.equals(c.getParameterTypes(), new Class[]{Link.class}) || Arrays.equals(c.getParameterTypes(), new Class[]{}))
                                .max(Comparator.comparingInt(Constructor::getParameterCount))
                                .orElseThrow(() -> new RuntimeException("No constructor found"));

                gridManager.linkStream()
                        .filter(link -> link.distance(x, y) < GridPanel.this.brushPanel.getBrushProperties().getBrushSize())
                        .forEach(l -> {
                            try {
                                l.setElement(constructor.getParameterCount() == 0 ? constructor.newInstance() : constructor.newInstance(l));
                            } catch (InvocationTargetException | InstantiationException | IllegalAccessException _) {
                                throw new RuntimeException();
                            }
                        });
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                GridPanel.this.getMouseMotionListeners()[0].mouseDragged(e);
            }
        });
        final var asyncRepaint = Executors.newScheduledThreadPool(10);
        asyncRepaint.scheduleAtFixedRate(this::repaint, 0, 10, TimeUnit.MILLISECONDS);

        final var asyncCheckErrors = Executors.newSingleThreadScheduledExecutor();
        asyncCheckErrors.scheduleAtFixedRate( () -> {
            var count = new HashMap<Element, Set<Link>>();
            GridPanel.this.gridManager.linkStream()
                    .forEach(l -> {
                        final var element = l.getElement();
                        count.putIfAbsent(element, new HashSet<>());
                        count.get(element).add(l);
                    });
            count.entrySet()
                    .stream()
                    .filter(e -> e.getValue().size() != 1)
                    .forEach(System.out::println);
        }, 1000, 1000, TimeUnit.MILLISECONDS);

    }
    @Override
    public void paint(Graphics g) {
        gridManager.linkStream()
                .forEach(link -> {
                    g.setColor(link.getColor());
                    g.fillRect(gridManager.getXAbsolute(link) * elementSize, gridManager.getYAbsolute(link) * elementSize, elementSize, elementSize);
                });
    }

    @Override
    public void update(Graphics g) {
        this.paint(g);
    }


    @Override
    public Dimension getPreferredSize() {
        return new Dimension(getTotalWidth(), getTotalHeight());
    }
    private int getTotalWidth() {
        return this.gridManager.getDimensionInElements().width * elementSize;
    }
    private int getTotalHeight() {
        return this.gridManager.getDimensionInElements().height * elementSize;
    }
}
