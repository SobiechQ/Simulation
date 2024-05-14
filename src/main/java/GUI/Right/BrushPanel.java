package GUI.Right;

import Elements.Api.Core.Element;
import lombok.Getter;
import org.jooq.lambda.Seq;
import org.reflections.Reflections;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.lang.reflect.Modifier;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Getter
public class BrushPanel extends JPanel {
    private final BrushProperties brushProperties = new BrushProperties();


    public BrushPanel() {
        this.add(this.getNewBrushSizePanel());
        this.add(this.getNewBrushPropertiesPanel());
        this.add(this.getNewBrushElementTypePanel());

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));


    }

    @Deprecated(since = "1.0", forRemoval = true)
    public int getCommandSize() {
        return this.brushProperties.getBrushSize();
    }


    private JPanel getNewBrushSizePanel() {
        final var panel = new JPanel();
        final var valueLabel = new JLabel("10");
        valueLabel.setFont(new Font("Segoe UI Variable", Font.PLAIN, 20));
        panel.setBorder(BorderFactory.createTitledBorder("Brush Size"));

        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        final var nameLabel = new JLabel("Size: ");
        nameLabel.setFont(new Font("Segoe UI Variable", Font.PLAIN, 20));
        panel.add(nameLabel);


        final var slider = new JSlider(0, 30, this.getCommandSize());

        slider.addChangeListener(e -> {
            this.brushProperties.setBrushSize(slider.getValue());
            final var updatedString = slider.getValue() < 10 ? STR."0\{slider.getValue()}" : String.valueOf(slider.getValue());
            valueLabel.setText(updatedString);
        });
        slider.setMinorTickSpacing(1);
        slider.setMajorTickSpacing(5);
        slider.setSnapToTicks(true);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);


        panel.add(slider);
        panel.add(valueLabel);

        return panel;
    }

    public JPanel getNewBrushPropertiesPanel() {
        final var panel = new JPanel();
//        panel.setPreferredSize(new Dimension(600, 40));
        final var layout = new BoxLayout(panel, BoxLayout.X_AXIS);
        panel.setLayout(layout);

//        panel.getRootPane().setBorder(BorderFactory.createTitledBorder("Brush Properties"));

        panel.setBorder(BorderFactory.createTitledBorder("Brush Properties"));
        final var replace = new JToggleButton("Replace");
        final var positionAware = new JToggleButton("Position Aware", true);
        panel.add(replace);
        panel.add(positionAware);
        replace.addActionListener(e -> this.brushProperties.setReplace(replace.isSelected()));

        return panel;
    }

    public JPanel getNewBrushElementTypePanel() {
        final var packageToElementMap = Seq.seq(new Reflections("Elements")
                .getSubTypesOf(Element.class))
                .filter(c -> !Modifier.isAbstract(c.getModifiers()))
                .map(ComboBoxRecord::new)
                .groupBy(comboBoxRecord -> comboBoxRecord.elementClazz().getPackage().getName());




        final var panel = new JPanel();
        final var root = new DefaultMutableTreeNode("Element Types");
        final var tree = new JTree(root);
        tree.addTreeSelectionListener(e -> {
//            System.out.println(e);

            final var selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (Objects.isNull(selectedNode)) {
                return;
            }
            final var selectedElement = selectedNode.getUserObject();
            System.out.println(selectedElement.getClass().getName());
            if (selectedElement instanceof ComboBoxRecord record) {
                System.out.println("GABI BELKA");
                System.out.println(record);
                this.brushProperties.setElementClass(record.elementClazz());
            }
        });

        packageToElementMap.forEach((packageName, comboBoxRecords) -> {
            DefaultMutableTreeNode packageNode = new DefaultMutableTreeNode(packageName);
            comboBoxRecords.forEach(record -> {
                DefaultMutableTreeNode elementNode = new DefaultMutableTreeNode(record);
                packageNode.add(elementNode);
            });
            root.add(packageNode);
        });


        panel.setBorder(BorderFactory.createTitledBorder("Element Type"));

        JScrollPane scrollPane = new JScrollPane(tree);
        scrollPane.setPreferredSize(new Dimension(250, 200));

        panel.add(scrollPane);

        return panel;
    }



}

record ComboBoxRecord(Class<? extends Element> elementClazz) {
    @Override
    public String toString() {
        return this.elementClazz.getSimpleName();
    }

    public Class<? extends Element> newInstanceOf() {
        System.out.println(this.elementClazz);
        return this.elementClazz;
    }

}
