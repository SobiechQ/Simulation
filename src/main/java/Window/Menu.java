package Window;

import Elements.Solid.Air;
import Elements.Api.Core.Element;
import org.reflections.Reflections;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Objects;

public class Menu extends JFrame {
    private final JPanel jPanel = new JPanel();
    private final JSpinner jSpinner = new JSpinner(new SpinnerNumberModel(2, 1, 30, 1));
    private record ComboBoxRecord(Class<? extends Element> element){
        @Override
        public String toString() {
            return this.element.getSimpleName();
        }
        public Class<? extends Element> newInstanceOf(){
            return this.element;
        }
    }
    private final JComboBox<ComboBoxRecord> jComboBox = new JComboBox<>();
    public Menu() {
        setTitle("Menu");
        this.jPanel.setLayout(new GridLayout(2, 5));
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        this.jPanel.add(new JLabel("Command size"));
        this.jPanel.add(new JLabel("List"));
        this.jPanel.add(this.jSpinner);
        this.jPanel.add(jComboBox);
        this.add(jPanel);

        new Reflections("Elements")
                .getSubTypesOf(Element.class)
                .stream()
                .filter(c -> !Modifier.isAbstract(c.getModifiers()))
                .map(ComboBoxRecord::new)
                .forEach(this.jComboBox::addItem);


    }
    public int getCommandSize(){
        return (int) this.jSpinner.getValue();
    }
    public Class<? extends Element> getCommandElementClass(){
        return ((ComboBoxRecord) Objects.requireNonNull(jComboBox.getSelectedItem())).newInstanceOf();
    }


}
