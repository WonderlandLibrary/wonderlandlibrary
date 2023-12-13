// 
// Decompiled by Procyon v0.5.36
// 

package dev.xulu.clickgui.item.properties;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import com.elementars.eclient.Xulu;
import com.elementars.eclient.module.render.ExeterGui;
import com.elementars.eclient.util.XuluTessellator;
import com.elementars.eclient.util.ColorUtils;
import dev.xulu.newgui.util.ColorUtil;
import dev.xulu.clickgui.Panel;
import dev.xulu.settings.Value;
import dev.xulu.clickgui.item.Button;

public class EnumButton extends Button
{
    public EnumButton(final Value property) {
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
        if (ExeterGui.getCF()) {
            Xulu.cFontRenderer.drawStringWithShadow(String.format("%s§7 %s", this.getLabel(), Xulu.getTitle(this.property.getValue().toString())), this.x + 2.3f, this.y + 3.0f, this.getState() ? -1 : -5592406);
        }
        else {
            EnumButton.fontRenderer.drawStringWithShadow(String.format("%s§7 %s", this.getLabel(), Xulu.getTitle(this.property.getValue().toString())), this.x + 2.3f, this.y + 4.0f, this.getState() ? -1 : -5592406);
        }
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY)) {
            if (ExeterGui.getSound()) {
                EnumButton.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            }
            final String s = (this.property.getValue() instanceof String) ? this.property.getValue() : this.property.getValue().toString();
            if (mouseButton == 0) {
                try {
                    if (!this.property.getCorrectOption(s).toString().equalsIgnoreCase(this.property.getOptions().get(this.property.getOptions().size() - 1).toString())) {
                        this.property.setEnumValue(this.property.getOptions().get(this.property.getOptions().indexOf(this.property.getCorrectOption(s)) + 1).toString());
                    }
                    else {
                        this.property.setEnumValue(this.property.getOptions().get(0).toString());
                    }
                }
                catch (Exception e) {
                    System.err.println("Error with invalid combo");
                    e.printStackTrace();
                    this.property.setEnumValue(this.property.getOptions().get(0).toString());
                }
            }
            else if (mouseButton == 1) {
                try {
                    if (this.property.getOptions().listIterator(this.property.getOptions().indexOf(this.property.getCorrectOption(s))).hasPrevious()) {
                        this.property.setEnumValue(this.property.getOptions().listIterator(this.property.getOptions().indexOf(this.property.getCorrectOption(s))).previous().toString());
                    }
                    else {
                        this.property.setEnumValue(this.property.getOptions().get(this.property.getOptions().size() - 1).toString());
                    }
                }
                catch (Exception e) {
                    System.err.println("Error with invalid combo");
                    e.printStackTrace();
                    this.property.setEnumValue(this.property.getOptions().get(0).toString());
                }
            }
        }
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
        return true;
    }
}
