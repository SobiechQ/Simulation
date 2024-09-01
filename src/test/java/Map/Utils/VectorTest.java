package Map.Utils;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.*;

public class VectorTest {
    @Test
    public void givenAngleAndLength_whenCreatingVector_thenVectorHasCorrectValues() {
        //--given--
        final var angle = 45;
        final var length = 10;

        //--when--
        final var vector = new Vector(angle, length, true);

        //--then--
        Assertions.assertEquals(angle, vector.getAngle());
        Assertions.assertEquals(length, vector.getLength());
    }

}