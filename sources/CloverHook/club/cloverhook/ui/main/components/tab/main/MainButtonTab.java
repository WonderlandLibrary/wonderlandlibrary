package club.cloverhook.ui.main.components.tab.main;

import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import club.cloverhook.Cloverhook;
import club.cloverhook.ui.main.Interface;
import club.cloverhook.ui.main.components.base.BaseButton;
import club.cloverhook.ui.main.tab.Tab;
import club.cloverhook.utils.Draw;

/**
 * @author antja03
 */
public class MainButtonTab extends BaseButton {

    private Tab tab;
    private final ResourceLocation iconImageLocation;

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
        if (Cloverhook.instance.userInterface.theInterface.getCurrentTab() == tab)
            GL11.glColor3f(0.85f, 0.85f, 0.85f);
        else
            GL11.glColor3f(0.45f, 0.45f, 0.45f);
        Draw.drawImg(iconImageLocation, x, y, iconWidth, iconHeight);
    }

    public Tab getTab() {
        return tab;
    }
}
