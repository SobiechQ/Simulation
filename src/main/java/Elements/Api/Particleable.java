package Elements.Api;

import Map.Link;

public interface Particleable extends Refreshable {
    default void refresh(Link link){
        this.generateParticles(link);
    }
    void generateParticles(Link link);
}
