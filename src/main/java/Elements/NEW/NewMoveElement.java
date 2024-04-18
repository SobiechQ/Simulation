package Elements.NEW;
import Elements.Api.*;
import Map.Link;

import static Map.Utils.Direction.RIGHT;

public class NewMoveElement extends Element implements Refreshable {


    public NewVector newVector = new NewVector();
    @Override
    public void refresh(Link l) {

        if (newVector.smallStep < 1) {
            this.newVector.smallStep += this.newVector.speed;
            return;
        }
        newVector.smallStep -=1;
        if (l.set(this, RIGHT))
            l.clear();


    }
}
