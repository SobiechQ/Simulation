package Elements.Api;
import Map.Utils.Vector;
import Map.Link;

public interface Moveable extends Refreshable {
    @Override
    default void refresh(Link l){
        this.move(l);
    }
    Vector getVelocity();
    default void setVelocity(double x, double y) {
        if (this instanceof Loose loose && loose.getDebug()) {
            System.out.println("~~~~~~");
            System.out.printf("Zmiana z: [%s]\n", this.getVelocity());
        }
        this.getVelocity().setX(x);
        this.getVelocity().setY(y);
        if (this instanceof Loose loose && loose.getDebug()) {
            System.out.printf("Zmiana na: [%s]\n", this.getVelocity());
            System.out.println("~~~~~~");
        }
    }
    void move(Link link);
}
