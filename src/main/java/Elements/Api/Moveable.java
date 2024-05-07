package Elements.Api;

import Map.Link;
import Map.Utils.Direction;
import Map.Utils.Vector;
import lombok.NonNull;

/**
 * Movable is an interface that allows elements to move in the simulation.
 * It provides required {@link Vector velocity} for calculations.
 */
public interface Moveable extends Refreshable { //todo update documentation after refactoring stepVector
    /**
     * On every refresh tick Moveable elements might move in the grid zero or more times.
     * That is determined by the length of the {@link Vector velocity vector} and by its "steps".
     * Steps are values in range [-1, 1] and they represent unused velocity from previous tics.
     * This allowes to move elements slower or faster than 1 pixel per tick.
     * For exaple vector with x = 0.1 will move the element by 1 pixel every 10 tics.
     * And vector with x = 1.5 will move element by 1 pixel or 2 pixels every first or second tic.
     * @param link the link that the element is refreshed on for the current tick. It allowes to perform actions with position awareness.
     */
    @Override
    default void refresh(@NonNull Link link) {
        this.updateGravity(link);
        var nextLink = link;
        final var stepVelocity = this.getVelocity().getStepVector().orElse(new Vector(0, 0));
        stepVelocity.addX(this.getVelocity().getX());
        stepVelocity.addY(this.getVelocity().getY());
        while (stepVelocity.isStepApplicable())
            nextLink = this.move(nextLink, stepVelocity);
    }

    /**
     * Represents velocity of the element in the grid.
     * @return This elements velocity. This should return reference to the same Vector.
     */
    Vector getVelocity();

    /**
     * Unlike {@link Moveable#move(Link, Vector) move} method, this method is called only once per every refresh tick.
     * It allows to update any values that are required for the movement of the element.
     * Its always called once before any {@link Moveable#move(Link, Vector) move} is invoked.
     * It is called even if {@link Moveable#move(Link, Vector) move} is not to be invoked.
     * @param link the link that the element is refreshed on for the current tick. It allowes to perform actions with position awareness.
     * @see Moveable#move(Link, Vector)
     * @see Moveable#refresh(Link)
     * @see Link
     */
    void updateGravity(@NonNull Link link);

    //todo add code example of returning moved to link

    /**
     * This method is called by {@link Moveable#refresh(Link) refresh} method.
     * It can be called zero or more times as desctibed in {@link Moveable#refresh(Link) refresh} method.
     * Implenetations of this method should move the element by one pixel sideways or up/down using the {@link Link#swap(Direction...) swap} method.
     * Because refresh method determines count of move method invocations for every movement, moved direction has to be subtracted from the {@link Vector stepVelocity} parameter.
     * Failing to do so will result in infinite loop.
     * @param stepVelocity Copy of the velocity created once for every tick.
     *                     The same stepVelocity is used for multiple move invocations.
     *                     After every movement it is required update it by the moved value on coordinate axis.
     *                     To see sugested direction of vector to move check {@link Vector#getDirection()}.
     * @param link The link that the element is refreshed on for the current tick. It allowes to perform actions with position awareness.
     * @return Link that the element was moved to. It can be the same link as the input link if no movement was performed.
     * This allowes to track location of the element after every movement for further invocation of the move method.
     * @see Vector
     * @see Direction
     * @see Link#swap(Direction...)
     */
    Link move(@NonNull Link link,@NonNull Vector stepVelocity);
}
