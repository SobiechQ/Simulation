package Elements.Api;

public interface Flameable extends Refreshable{
    void setOnFire();
    void extinguish();
    boolean isSetOnFire();
}
