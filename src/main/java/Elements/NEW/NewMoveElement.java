package Elements.NEW;
import Elements.Air;
import Elements.Api.*;
import Map.Link;
import Map.Utils.Vector;

import static Map.Utils.Direction.RIGHT;

public class NewMoveElement extends Element implements NewMoveable {
    private final Vector velocity = new Vector();
    public NewMoveElement(){
        velocity.setSpeed(0.1);
        System.out.println("NewMoveElement created");
    }
    @Override
    public Vector getVelocity() {
        return this.velocity;
    }

    @Override
    public Link move(Link link) {
        var old = link.get(RIGHT);
        if (old.isEmpty())
            return link;
        link.set(old.get().getElement());
        link.set(this, RIGHT);
        return old.get();
    }
}
