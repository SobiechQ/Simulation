package Elements.Api.MultiBlockCore;

import Elements.Api.Core.Element;

import java.util.HashSet;
import java.util.Set;

public class MultiBlockRegistry {

    private final Set<MultiBlockCore<? extends Element>> registry;

    public MultiBlockRegistry() {
        this.registry = new HashSet<>();
    }
    public void refresh() {
        this.registry.forEach(MultiBlockCore::refresh);
    }
    public void register(MultiBlockCore<? extends Element> multiBlockCore){
        this.registry.add(multiBlockCore);
    }


}
