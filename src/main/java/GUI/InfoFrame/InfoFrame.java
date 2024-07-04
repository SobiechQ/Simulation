package GUI.InfoFrame;

import Map.Chunk;
import Map.GridManager;

import javax.swing.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class InfoFrame extends JFrame{
    private final GridManager gridManager;
    private final ScheduledExecutorService executorService;
    public InfoFrame(GridManager gridManager) {
        this.setAlwaysOnTop(true);
        this.gridManager = gridManager;
        this.setTitle("Info");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
        this.setSize(500, 500);
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.executorService.scheduleAtFixedRate(()->{
            this.gridManager.chunkStream()
                    .mapToDouble(Chunk::getLastSystemTimeDifference)
                    .average()
                    .ifPresent(average->{
                        System.out.println(average);
                    });
        }, 0, 100, java.util.concurrent.TimeUnit.MILLISECONDS);

    }

    @Override
    public void dispose() {
        super.dispose();
        this.executorService.shutdown();
    }
}
