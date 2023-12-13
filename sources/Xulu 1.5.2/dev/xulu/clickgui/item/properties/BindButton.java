// 
// Decompiled by Procyon v0.5.36
// 

package dev.xulu.clickgui.item.properties;

import java.io.IOException;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import com.elementars.eclient.Xulu;
import com.elementars.eclient.module.render.ExeterGui;
import org.lwjgl.input.Keyboard;
import dev.xulu.settings.Bind;
import com.elementars.eclient.util.XuluTessellator;
import com.elementars.eclient.util.ColorUtils;
import dev.xulu.newgui.util.ColorUtil;
import dev.xulu.clickgui.Panel;
import dev.xulu.settings.Value;
import dev.xulu.clickgui.item.Button;

public class BindButton extends Button
{
    private boolean listening;
    
    public BindButton(final Value property) {
        super(property.getName(), null);
        this.setValue(property);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        XuluTessellator.drawRectGradient(this.x, this.y, this.x + this.width + 7.4f, this.y + this.height, this.getState() ? ColorUtils.changeAlpha(ColorUtil.getClickGUIColor().getRGB(), 200) : 290805077, -1);
        if (this.isHovering(mouseX, mouseY)) {
            if (this.getState()) {
                XuluTessellator.drawRectGradient(this.x, this.y, this.x + this.width, this.y + this.height, ColorUtils.changeAlpha(ColorUtils.Colors.BLACK, 25), -1);
            }
            else {
                XuluTessellator.drawRectGradient(this.x, this.y, this.x + this.width, this.y + this.height, ColorUtils.changeAlpha(ColorUtils.Colors.WHITE, 25), -1);
            }
        }
        final String s = this.listening ? "..." : Keyboard.getKeyName(this.property.getValue().getNum());
        if (ExeterGui.getCF()) {
            Xulu.cFontRenderer.drawStringWithShadow(String.format("%s§7 %s", this.getLabel(), s), this.x + 2.3f, this.y + 3.0f, this.getState() ? -1 : -1);
        }
        else {
            BindButton.fontRenderer.drawStringWithShadow(String.format("%s§7 %s", this.getLabel(), s), this.x + 2.3f, this.y + 4.0f, this.getState() ? -1 : -1);
        }
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY)) {
            if (ExeterGui.getSound()) {
                BindButton.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            }
            if (mouseButton == 0) {
                this.listening = true;
            }
        }
    }
    
    @Override
    public boolean keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (this.listening) {
            if (keyCode != 1) {
                this.property.getParentMod().setKey(keyCode);
            }
            else {
                this.property.getParentMod().setKey(0);
            }
            this.listening = false;
            return true;
        }
        return super.keyTyped(typedChar, keyCode);
    }
    
    @Override
    public int getHeight() {
        return 14;
    }
    
    @Override
    public void toggle() {
    }
    
    @Override
    public boolean getState() {
        return false;
    }
}
