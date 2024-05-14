package GUI.Right;

import Elements.Api.Core.Element;
import Elements.Loose.MagicSand;
import Elements.Loose.Sand;
import GUI.Center.GridPanel;
import Map.Link;
import lombok.Data;
import lombok.NonNull;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Comparator;

@Data
public class BrushProperties {
    private int brushSize;
    private boolean isReplace;
    @NonNull
    private Class<? extends Element> elementClass;

    public BrushProperties() {
        this.elementClass = Sand.class;
        this.brushSize = 10;
        this.isReplace = false;
    }

    @SuppressWarnings("unchecked")
    public Constructor<Element> getElementConstructor() {
        return (Constructor<Element>) Arrays.stream(this.getElementClass().getConstructors())
                .map(c -> (Constructor<? extends Element>) c)
                .filter(c -> Arrays.equals(c.getParameterTypes(), new Class[]{Link.class}) || Arrays.equals(c.getParameterTypes(), new Class[]{}))
                .max(Comparator.comparingInt(Constructor::getParameterCount))
                .orElseThrow(() -> new RuntimeException("No constructor found"));
    }
}
