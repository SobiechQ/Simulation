package Elements.Api.Core;

import Elements.Api.Core.Element;
import Map.Link;
import lombok.NonNull;

public abstract non-sealed class Solid extends Element {

    public Solid(@NonNull Link link) {
        super(link);
    }
    public Solid(){

    }
}
