// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.screen.circle;

import moonsense.ui.utils.GuiUtils;
import org.lwjgl.opengl.GL11;
import moonsense.ui.screen.AbstractGuiScreen;

public class GuiScreenCircle extends AbstractGuiScreen
{
    private int sectors;
    private int sectorRadius;
    
    public GuiScreenCircle() {
        this.sectors = 5;
    }
    
    @Override
    public void initGui() {
        super.initGui();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        GL11.glPushMatrix();
        for (int i = 0; i < this.sectors; ++i) {
            final float lx = (float)(Math.cos(i * (360 / this.sectors)) * this.sectorRadius);
            final float ly = (float)(Math.sin(i * (360 / this.sectors)) * this.sectorRadius);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glVertex3f((float)(this.width / 2), (float)(this.height / 2), 0.0f);
            GL11.glVertex3f(lx, ly, 0.0f);
            GL11.glDrawBuffer(1);
        }
        GL11.glPopMatrix();
        GuiUtils.drawPartialCircle((float)(this.width / 2), (float)(this.height / 2), (float)this.sectorRadius, 0, 360, 1.0f, -1, true);
    }
}
