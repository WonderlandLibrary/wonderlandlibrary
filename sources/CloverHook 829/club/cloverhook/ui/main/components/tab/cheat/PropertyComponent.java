package club.cloverhook.ui.main.components.tab.cheat;

import java.awt.*;

import club.cloverhook.ui.main.Interface;
import club.cloverhook.ui.main.components.Component;
import club.cloverhook.utils.font.Fonts;
import club.cloverhook.utils.property.abs.Property;

/**
 * @author antja03
 */
public abstract class PropertyComponent extends Component {

    private Property property;

    public PropertyComponent(Interface theInterface, Property value, double x, double y, double width, double height) {
        super(theInterface, x, y, width, height);
        this.property = value;
    }

    public abstract void drawComponent(double x, double y);

    public boolean mouseButtonClicked(int button) {
        return false;
    }

    public boolean keyTyped(char typedChar, int keyCode) {
        return false;
    }


    public Property getProperty() {
        return property;
    }

}
