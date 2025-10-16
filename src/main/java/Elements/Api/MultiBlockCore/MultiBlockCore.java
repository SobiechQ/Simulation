package Elements.Api.MultiBlockCore;

import Elements.Api.Core.Element;
import Map.Link;
import lombok.NonNull;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public abstract class MultiBlockCore<T extends Element> {

    private final Set<T> elements;

    public MultiBlockCore(@NonNull Link link){
        this.elements = new HashSet<>();
        link.getGridManager().getMultiBlockRegistry().register(this);
    }
    public void add(T element){
        this.elements.add(element);
    }
    public Stream<T> getElements(){
        return this.elements.stream();
    }
    public abstract void refresh();



}
