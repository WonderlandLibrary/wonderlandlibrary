package rip.helium.ui.main.components.tab.main;

import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import rip.helium.Helium;
import rip.helium.ui.main.Interface;
import rip.helium.ui.main.components.base.BaseButton;
import rip.helium.ui.main.tab.Tab;
import rip.helium.utils.Draw;

/**
 * @author antja03
 */
public class MainButtonTab extends BaseButton {

    private final ResourceLocation iconImageLocation;
    private final Tab tab;

    public MainButtonTab(Interface theInterface, Tab tab, ResourceLocation iconImageLocation, double positionX, double positionY, double maxWidth, double maxHeight, Action action) {
        super(theInterface, positionX, positionY, maxWidth, maxHeight, action);
        this.tab = tab;
        this.iconImageLocation = iconImageLocation;
    }

    @Override
    public void drawComponent(double x, double y) {
        super.drawComponent(x, y);
        double iconWidth = 16;
        double iconHeight = 16;
        if (Helium.instance.userInterface.theInterface.getCurrentTab() == tab)
            GL11.glColor3f(0.85f, 0.85f, 0.85f);
        else
            GL11.glColor3f(0.45f, 0.45f, 0.45f);
        Draw.drawImg(iconImageLocation, x, y, iconWidth, iconHeight);
    }

    public Tab getTab() {
        return tab;
    }
}
