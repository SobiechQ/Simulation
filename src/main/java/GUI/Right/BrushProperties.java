package GUI.Right;

import Elements.Api.Core.Element;
import Elements.Loose.Sand;
import lombok.Data;

@Data
public class BrushProperties {
    private int brushSize;
    private boolean isReplace;
    private Class<? extends Element> elementClass = Sand.class;
    public BrushProperties() {
        this.brushSize = 10;
        this.isReplace = false;
    }
}
