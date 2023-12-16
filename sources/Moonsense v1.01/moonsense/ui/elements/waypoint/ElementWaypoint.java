// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.elements.waypoint;

import moonsense.MoonsenseClient;
import moonsense.features.SCModule;
import moonsense.ui.utils.GuiUtils;
import java.awt.Color;
import java.util.function.Consumer;
import moonsense.features.modules.type.world.waypoints.Waypoint;
import moonsense.ui.elements.Element;

public class ElementWaypoint extends Element
{
    private final Waypoint waypoint;
    private float scale;
    private int bgFade;
    private int toggleFade;
    private int optionsFade;
    private final int disabledColor;
    
    public ElementWaypoint(final int x, final int y, final int width, final int height, final Waypoint module, final Consumer<Element> consumer) {
        super(x, y, width, height, true, consumer);
        this.disabledColor = new Color(120, 120, 120, 255).brighter().getRGB();
        this.waypoint = module;
    }
    
    @Override
    public void init() {
        this.scale = this.rescale(1.0f);
    }
    
    @Override
    public void renderElement(final float partialTicks) {
        GuiUtils.drawRoundedRect((float)this.getX(), (float)this.getY(), (float)(this.getX() + this.width), (float)(this.getY() + this.height), 3.0f, this.hovered ? new Color(100, 111, 117, 100).getRGB() : new Color(42, 50, 55, 100).getRGB());
        GuiUtils.drawRoundedRect((float)(this.getX() + 5), (float)(this.getY() + 5), (float)(this.getX() + 5 + 15), (float)(this.getY() + 5 + 15), 2.0f, SCModule.getColor(this.waypoint.getColor()));
        MoonsenseClient.textRenderer.drawString("Waypoint: " + this.waypoint.getName(), this.getX() + 32 - 9, this.getY() + 3, -1);
        MoonsenseClient.textRenderer.drawString("Dimension: " + ((this.waypoint.getDimension() == 0) ? "Overworld" : ((this.waypoint.getDimension() == -1) ? "The Nether" : "The End")), (float)(this.getX() + 32 - 9), this.getY() + 7.5f + 5.0f, -1);
        GuiUtils.drawRoundedRect((float)(this.getX() + this.width - 26), (float)(this.getY() + 3), (float)(this.getX() + this.width - 25), (float)(this.getY() + this.getHeight() - 3), 0.0f, new Color(105, 116, 122, 170).getRGB());
        GuiUtils.drawRoundedOutline(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, 3.0f, 2.0f, this.waypoint.isVisible() ? new Color(50, 180, 50, 150).getRGB() : new Color(180, 50, 50, 150).getRGB());
    }
    
    @Override
    public void renderBackground(final float partialTicks) {
        GuiUtils.drawRoundedRect((float)this.getX(), (float)this.getY(), (float)(this.getX() + this.width), (float)(this.getY() + this.height), 7.0f, new Color(0, 0, 0, this.bgFade).getRGB());
    }
    
    @Override
    public void update() {
        if (this.hovered && this.bgFade + 5 <= 70) {
            this.bgFade += 5;
        }
        else if (!this.hovered && this.bgFade - 5 >= 0) {
            this.bgFade -= 5;
        }
    }
    
    private float rescale(final float scale) {
        if (MoonsenseClient.textRenderer.getWidth(this.waypoint.getName()) / (1.0f / scale) > this.width - 37) {
            return this.rescale(scale - 0.1f);
        }
        return scale;
    }
}
