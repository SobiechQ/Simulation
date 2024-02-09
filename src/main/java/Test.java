import Elements.Api.Element;
import Elements.Api.GeneratesParticles;
import Elements.TestObj;
import Map.Utils.Vector;

public class Test {
    public static void main(String[] args) {
        var vector = new Vector(0, 0);
        var test = new TestObj();
        System.out.println(foo(test, GeneratesParticles.class));



    }
    public static <T> boolean foo(Element element, Class<T> clazz){
        return clazz.isInstance(element);
    }
}
