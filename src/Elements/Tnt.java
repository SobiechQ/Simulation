package Elements;
import Elements.Api.Loose;
import Elements.Api.Solid;
import Map.GridDecorator;

import java.awt.*;

public class Tnt extends Solid {
    public Tnt(){

    }

    @Override
    public Color getColor() {
        return Color.RED;
    }

    private int frames = 0;
//    public void nextFrame(Griderator griderator){
//        frames++;
//        if(frames == 32){
//            frames = 0;
//            explode(griderator);
//        }
//    }
//    public void explode(Griderator griderator){
//        griderator.inRadius(20)
//                .forEach(e -> {
//                    if(e instanceof Loose loose){
//                        loose.getVelocity().setX(10);
//                        loose.getVelocity().setY(10);
//                    }
//                });
//
//    }
}
