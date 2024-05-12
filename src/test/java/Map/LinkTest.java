package Map;

import Elements.Loose.Sand;
import Elements.Solid.Air;
import Elements.Solid.Stone;
import Map.Utils.Direction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LinkTest {


    @Test
    public void givenLinkNonAirElement_whenClearing_thenThisElementSetToNewAir() {
        //--given--
        final var link = new Link(0, 0, new Chunk(new GridManager(1, 1), 0, 0), new Stone());

        //--when--
        link.clear();

        //--then--
        final var afterClearElementResult = link.getElement();
        Assertions.assertInstanceOf(Air.class, afterClearElementResult);
    }

    @Test
    public void givenLinkAirElement_whenClearing_thenThisElementSetToNewAir() { //todo to chyba zle
        //--given--
        final var link = new Link(0, 0, new Chunk(new GridManager(1, 1), 0, 0), new Air());
        final var oldAir = link.getElement();

        //--when--
        link.clear();

        //--then--
        final var afterClearElementResult = link.getElement();
        Assertions.assertInstanceOf(Air.class, oldAir);
        Assertions.assertInstanceOf(Air.class, afterClearElementResult);
        Assertions.assertNotSame(oldAir, afterClearElementResult);
    }

    @Test
    public void givenTwoLinksOnSameChunk_whenSwappingTopBottom_thenBothElementsSet() {
        //--given--
        final var gridManager = new GridManager(1, 1);
        final var topElement = new Sand();
        final var bottomElement = new Stone();

        final var topLinkOptional = gridManager.getLink(0, 0);
        Assertions.assertTrue(topLinkOptional.isPresent(), "Top link out of bounds");

        final var bottomLinkOptional = gridManager.getLink(0, 1);
        Assertions.assertTrue(bottomLinkOptional.isPresent(), "Bottom link out of bounds");

        //--when--
        topLinkOptional.get().setElement(topElement);
        bottomLinkOptional.get().setElement(bottomElement);
        topLinkOptional.get().swap(Direction.DOWN);

        //--then--
        Assertions.assertSame(bottomElement, topLinkOptional.get().getElement(), "Top link not set to bottom element");
        Assertions.assertSame(topElement, bottomLinkOptional.get().getElement(), "Bottom link not set to top element");
    }

    @Test
    public void givenTwoLinksOnSameChunk_whenSwappingBottomTop_thenBothElementsSet() {
        //--given--
        final var gridManager = new GridManager(1, 1);
        final var topElement = new Sand();
        final var bottomElement = new Stone();

        final var topLinkOptional = gridManager.getLink(0, 0);
        Assertions.assertTrue(topLinkOptional.isPresent(), "Top link out of bounds");

        final var bottomLinkOptional = gridManager.getLink(0, 1);
        Assertions.assertTrue(bottomLinkOptional.isPresent(), "Bottom link out of bounds");

        //--when--
        topLinkOptional.get().setElement(topElement);
        bottomLinkOptional.get().setElement(bottomElement);
        bottomLinkOptional.get().swap(Direction.UP);

        //--then--
        Assertions.assertSame(bottomElement, topLinkOptional.get().getElement(), "Top link not set to bottom element");
        Assertions.assertSame(topElement, bottomLinkOptional.get().getElement(), "Bottom link not set to top element");
    }

    @Test
    public void givenTwoLinksOnSameChunk_whenSwappingLeftRight_thenBothElementsSet() {
        //--given--
        final var gridManager = new GridManager(1, 1);
        final var leftElement = new Sand();
        final var rightElement = new Stone();

        final var leftLinkOptional = gridManager.getLink(0, 0);
        Assertions.assertTrue(leftLinkOptional.isPresent(), "Left link out of bounds");

        final var rightLinkOptional = gridManager.getLink(1, 0);
        Assertions.assertTrue(rightLinkOptional.isPresent(), "Right link out of bounds");

        //--when--
        leftLinkOptional.get().setElement(leftElement);
        rightLinkOptional.get().setElement(rightElement);
        leftLinkOptional.get().swap(Direction.RIGHT);

        //--then--
        Assertions.assertSame(rightElement, leftLinkOptional.get().getElement(), "Left link not set to right element");
        Assertions.assertSame(leftElement, rightLinkOptional.get().getElement(), "Right link not set to left element");
    }
    @Test
    public void givenTwoLinksOnSameChunk_whenSwappingRightLeft_thenBothElementsSet() {
        //--given--
        final var gridManager = new GridManager(1, 1);
        final var leftElement = new Sand();
        final var rightElement = new Stone();

        final var leftLinkOptional = gridManager.getLink(0, 0);
        Assertions.assertTrue(leftLinkOptional.isPresent(), "Left link out of bounds");

        final var rightLinkOptional = gridManager.getLink(1, 0);
        Assertions.assertTrue(rightLinkOptional.isPresent(), "Right link out of bounds");

        //--when--
        leftLinkOptional.get().setElement(leftElement);
        rightLinkOptional.get().setElement(rightElement);
        rightLinkOptional.get().swap(Direction.LEFT);

        //--then--
        Assertions.assertSame(rightElement, leftLinkOptional.get().getElement(), "Left link not set to right element");
        Assertions.assertSame(leftElement, rightLinkOptional.get().getElement(), "Right link not set to left element");
    }

    @Test
    public void givenTwoLinksOnDifferentChunks_whenSwappingTopBottom_thenBothElementsSet() {
        //--given--
        final var gridManager = new GridManager(2, 2);
        final var topElement = new Sand();
        final var bottomElement = new Stone();
        final var chunckSize = Chunk.CHUNK_SIZE;

        final var topLinkOptional = gridManager.getLink(0, chunckSize - 1);
        Assertions.assertTrue(topLinkOptional.isPresent(), "Top link out of bounds");

        final var bottomLinkOptional = gridManager.getLink(0, chunckSize);
        Assertions.assertTrue(bottomLinkOptional.isPresent(), "Bottom link out of bounds");

        //--when--
        topLinkOptional.get().setElement(topElement);
        bottomLinkOptional.get().setElement(bottomElement);
        topLinkOptional.get().swap(Direction.DOWN);

        //--then--
        Assertions.assertTrue(gridManager.getChunk(0, 0).isPresent(), "Top chunk out of bounds");
        Assertions.assertTrue(gridManager.getChunk(0, 1).isPresent(), "Bottom chunk out of bounds");
        Assertions.assertNotSame(topLinkOptional.get().getChunk(), bottomLinkOptional.get().getChunk(), "Top and bottom links are on the same chunk");
        Assertions.assertSame(bottomElement, topLinkOptional.get().getElement(), "Top link not set to bottom element");
        Assertions.assertSame(topElement, bottomLinkOptional.get().getElement(), "Bottom link not set to top element");
    }
}