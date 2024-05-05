package club.cloverhook.ui.main.components.tab.configuration;

import club.cloverhook.Cloverhook;
import club.cloverhook.configuration.Configuration;
import club.cloverhook.ui.main.Interface;
import club.cloverhook.ui.main.components.Component;
import club.cloverhook.ui.main.components.base.BaseContainer;
import club.cloverhook.utils.ColorCreator;
import club.cloverhook.utils.Draw;

/**
 * @author antja03
 */
public class ContainerConfigurations extends BaseContainer {

    private int scrollIndex;
    private Configuration selectedConfig;

    public ContainerConfigurations(Interface theInterface, double x, double y, double width, double height) {
        super(theInterface, x, y, width, height);
        this.scrollIndex = 0;
        refresh();
    }

    public void refresh() {
        components.clear();
        Cloverhook.instance.configurationManager.getConfigurationList().forEach(config -> {
            components.add(new ButtonConfiguration(theInterface, config, this.positionX, this.positionY + (10 * Cloverhook.instance.configurationManager.getConfigurationList().indexOf(config)), this.maxWidth - 2.5, 10,
                    (int button) -> {
                        if (button == 0) {
                            if (selectedConfig != config) {
                                selectedConfig = config;
                            } else {
                                selectedConfig = null;
                            }
                        }
                    }, () -> selectedConfig == config));
        });

    }

    public void drawComponent(double x, double y) {
        double barHeight = this.maxHeight;
        double div = barHeight / components.size();
        if (components.size() > 19) {
            barHeight -= (components.size() - 19) * div;
        }
        double barPosition = div * scrollIndex;

        Draw.drawRectangle(x, y, x + this.maxWidth, y + this.maxHeight, theInterface.getColor(35, 35, 35));
        Draw.drawRectangle(x + this.maxWidth - 2.5, y + barPosition, x + this.maxWidth - 1, y + barPosition + barHeight, theInterface.getColor(255, 255, 255));

        int index = 0;
        for (Component component : components) {
            if (components.indexOf(component) >= scrollIndex && components.indexOf(component) < scrollIndex + 19) {
                component.drawComponent(theInterface.getPositionX() + component.positionX, y + (10 * index));
                index += 1;
            }
        }
    }

    public boolean mouseButtonClicked(int button) {
        for (Component component : this.components) {
            if (components.indexOf(component) >= scrollIndex && components.indexOf(component) < scrollIndex + 19) {
                if (component.mouseButtonClicked(button)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void mouseScrolled(final int scrollDirection) {
        if (theInterface.getCurrentFrameMouseX() > theInterface.getPositionX() + this.maxWidth)
            return;

        if (scrollDirection == 1) {
            if (scrollIndex < components.size() - 19) {
                scrollIndex += 1;
            }
        } else {
            if (scrollIndex > 0) {
                scrollIndex -= 1;
            }
        }

        for (Component component : components) {
            if (components.indexOf(component) >= scrollIndex && components.indexOf(component) < scrollIndex + 9) {
                component.mouseScrolled(scrollDirection);
            }
        }
    }

    public Configuration getSelectedConfig() {
        return selectedConfig;
    }
}
