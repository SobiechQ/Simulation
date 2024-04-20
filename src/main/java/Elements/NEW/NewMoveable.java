package Elements.NEW;

import Elements.Api.Refreshable;
import Map.Link;
import Map.Utils.Vector;
//todo private refreshable class that handles refresh element pool

public interface NewMoveable extends Refreshable {
    @Override
    default void refresh(Link link) {
        this.updateGravity(link);
        var nextLink = link;
        final var stepVelocity = new Vector(this.getVelocity());
        stepVelocity.x += this.getVelocity().stepX;
        stepVelocity.y += this.getVelocity().stepY;
        this.getVelocity().stepX = 0;
        this.getVelocity().stepY = 0;
        while (stepVelocity.step())
            nextLink = this.move(nextLink, stepVelocity);
        this.getVelocity().stepX += stepVelocity.x;
        this.getVelocity().stepY += stepVelocity.y;
    }

    Vector getVelocity();
    void updateGravity(Link link);

    Link move(Link link, Vector stepVelocity);
}
