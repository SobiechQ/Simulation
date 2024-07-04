package Elements.Solid;

import Elements.Api.Core.Solid;
import Elements.Api.Refreshable;
import Map.Link;
import Map.Utils.Direction;
import lombok.NonNull;

import java.awt.*;
import java.util.Set;

public class Dirt extends Solid implements Refreshable {
    private final static Set<Color> DIRT_COLORS = Set.of(
            new Color(186, 132, 93),
            new Color(148, 108, 76),
            new Color(124, 84, 59),
            new Color(91, 60, 38),
            new Color(126, 123, 121),
            new Color(116, 89, 68),
            new Color(114, 82, 57),
            new Color(124, 84, 58));
    private final static Set<Color> GRASS_COLORS = Set.of(
            new Color(6, 166, 23),
            new Color(3, 126, 15),
            new Color(0, 107, 12),
            new Color(4, 142, 19));
    private boolean isGrass;
    private final Color color;
    public Dirt(){
        this.isGrass = false;
        this.color = DIRT_COLORS.stream().skip((int) (DIRT_COLORS.size() * Math.random())).findFirst().orElseGet(this::getColor);
        this.setColor(this.color);
    }
    public Dirt(@NonNull Link link){
        this();
    }

    @Override
    public void refresh(@NonNull Link link) {
        if (!this.isGrass && Math.random() > 0.995 && link.isInstanceOf(Air.class, Direction.UP)) {
            this.setColor(GRASS_COLORS.stream().skip((int) (GRASS_COLORS.size() * Math.random())).findFirst().orElseGet(this::getColor));
            this.isGrass = true;
            return;
        }
        if (this.isGrass && !link.isInstanceOf(Air.class, Direction.UP)){
            this.setColor(this.color);
            this.isGrass = false;
        }

    }
}
