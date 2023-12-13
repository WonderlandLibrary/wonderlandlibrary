// 
// Decompiled by Procyon v0.5.36
// 

package dev.xulu.clickgui.item;

import java.util.Iterator;
import dev.xulu.clickgui.ClickGui;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import com.elementars.eclient.Xulu;
import com.elementars.eclient.util.XuluTessellator;
import com.elementars.eclient.util.ColorUtils;
import dev.xulu.newgui.util.ColorUtil;
import com.elementars.eclient.module.render.ExeterGui;
import dev.xulu.clickgui.Panel;
import com.elementars.eclient.util.Helper;
import dev.xulu.clickgui.Labeled;

public class Button extends Item implements Labeled, Helper
{
    private Panel parent;
    private boolean state;
    
    public Button(final String label, final Panel parent) {
        super(label);
        this.parent = parent;
        this.height = 15;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        XuluTessellator.drawRectGradient(this.x, this.y, this.x + this.width, this.y + this.height, this.getState() ? ColorUtils.changeAlpha(ExeterGui.getRainbow() ? this.parent.rgb : ColorUtil.getClickGUIColor().getRGB(), 225) : 861230421, -1);
        if (this.isHovering(mouseX, mouseY)) {
            if (this.getState()) {
                XuluTessellator.drawRectGradient(this.x, this.y, this.x + this.width, this.y + this.height, ColorUtils.changeAlpha(ColorUtils.Colors.BLACK, 30), -1);
            }
            else {
                XuluTessellator.drawRectGradient(this.x, this.y, this.x + this.width, this.y + this.height, ColorUtils.changeAlpha(ColorUtils.Colors.WHITE, 30), -1);
            }
        }
        if (ExeterGui.getCF()) {
            Xulu.cFontRenderer.drawStringWithShadow(this.getLabel(), this.x + 2.3f, this.y + 3.0f, this.getState() ? -1 : -5592406);
        }
        else {
            Button.fontRenderer.drawStringWithShadow(this.getLabel(), this.x + 2.3f, this.y + 4.0f, this.getState() ? -1 : -5592406);
        }
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (mouseButton == 0 && this.isHovering(mouseX, mouseY)) {
            this.state = !this.state;
            this.toggle();
            if (ExeterGui.getSound()) {
                Button.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            }
        }
    }
    
    public void toggle() {
    }
    
    public boolean getState() {
        return this.state;
    }
    
    @Override
    public int getHeight() {
        return 14;
    }
    
    protected boolean isHovering(final int mouseX, final int mouseY) {
        for (final Panel panel : ClickGui.getClickGui().getPanels()) {
            if (panel.drag) {
                return false;
            }
        }
        return mouseX >= this.getX() && mouseX <= this.getX() + this.getWidth() && mouseY >= this.getY() && mouseY <= this.getY() + this.height;
    }
}
