// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.elements.waypoint;

import net.minecraft.client.gui.Gui;
import moonsense.ui.utils.GuiUtils;
import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import java.util.function.Consumer;
import moonsense.features.modules.type.world.waypoints.Waypoint;
import net.minecraft.util.ResourceLocation;
import moonsense.ui.elements.Element;

public class ElementTeleportToWaypoint extends Element
{
    private final ResourceLocation ICON;
    private final int textureIndex;
    private final Waypoint waypoint;
    private int fadeIcon;
    
    public ElementTeleportToWaypoint(final int x, final int y, final int width, final int height, final String iconLocation, final int textureIndex, final boolean shouldScissor, final Waypoint module, final Consumer<Element> consumer) {
        super(x, y, width, height, shouldScissor, consumer);
        this.textureIndex = textureIndex;
        this.ICON = new ResourceLocation("streamlined/" + iconLocation);
        this.waypoint = module;
        this.fadeIcon = 175;
    }
    
    @Override
    public void renderElement(final float partialTicks) {
        this.mc.getTextureManager().bindTexture(this.ICON);
        final int b = 14;
        GlStateManager.enableBlend();
        GuiUtils.setGlColor(new Color(0, 0, 0, 75).getRGB());
        GuiUtils.drawScaledCustomSizeModalRect(this.getX() + (this.width - 14) / 2.0f + 0.75f, this.getY() + (this.height - 28) / 2.0f + 1.0f + 0.75f, 0.0f, 0.0f, 14, 14, 14, 14, 14.0f, 14.0f);
        GuiUtils.setGlColor(this.hovered ? new Color(255, 255, 255, this.fadeIcon).getRGB() : new Color(120, 120, 120, this.fadeIcon).getRGB());
        Gui.drawScaledCustomSizeModalRect(this.getX() + (this.width - 14) / 2, this.getY() + (this.height - 28) / 2 + 1, 0.0f, 0.0f, 14, 14, 14, 14, 14.0f, 14.0f);
    }
    
    @Override
    public void renderBackground(final float partialTicks) {
    }
    
    @Override
    public void update() {
        if (this.hovered && this.fadeIcon + 5 <= 255) {
            this.fadeIcon += 5;
        }
        else if (!this.hovered && this.fadeIcon - 5 >= 175) {
            this.fadeIcon -= 5;
        }
    }
}
