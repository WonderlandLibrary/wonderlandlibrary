// 
// Decompiled by Procyon v0.5.36
// 

package dev.xulu.clickgui.item.properties;

import java.util.Iterator;
import dev.xulu.clickgui.Panel;
import dev.xulu.clickgui.ClickGui;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.MathHelper;
import com.elementars.eclient.Xulu;
import com.elementars.eclient.module.render.ExeterGui;
import com.elementars.eclient.util.XuluTessellator;
import com.elementars.eclient.util.ColorUtils;
import dev.xulu.newgui.util.ColorUtil;
import dev.xulu.settings.Value;
import com.elementars.eclient.util.Helper;
import dev.xulu.clickgui.item.Item;

public class NumberSlider extends Item implements Helper
{
    private boolean dragging;
    
    public NumberSlider(final Value numberProperty) {
        super(numberProperty.getName());
        this.setValue(numberProperty);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        String displayval;
        if (this.property.getValue() instanceof Integer) {
            displayval = "" + Math.round(this.property.getValue() * 100.0) / 100.0;
        }
        else if (this.property.getValue() instanceof Short) {
            displayval = "" + Math.round(this.property.getValue() * 100.0) / 100.0;
        }
        else if (this.property.getValue() instanceof Long) {
            displayval = "" + Math.round(this.property.getValue() * 100.0) / 100.0;
        }
        else if (this.property.getValue() instanceof Float) {
            displayval = "" + Math.round(this.property.getValue() * 100.0) / 100.0;
        }
        else if (this.property.getValue() instanceof Double) {
            displayval = "" + Math.round(this.property.getValue() * 100.0) / 100.0;
        }
        else {
            displayval = "";
        }
        double percentBar;
        if (this.property.getValue() instanceof Integer) {
            final double value = this.property.getValue();
            percentBar = (value - this.property.getMin()) / (this.property.getMax() - this.property.getMin());
        }
        else if (this.property.getValue() instanceof Short) {
            final double value = this.property.getValue();
            percentBar = (value - this.property.getMin()) / ((short)this.property.getMax() - (short)this.property.getMin());
        }
        else if (this.property.getValue() instanceof Long) {
            final double value = this.property.getValue();
            percentBar = (value - this.property.getMin()) / (this.property.getMax() - this.property.getMin());
        }
        else if (this.property.getValue() instanceof Float) {
            percentBar = (this.property.getValue() - this.property.getMin()) / (this.property.getMax() - this.property.getMin());
        }
        else if (this.property.getValue() instanceof Double) {
            percentBar = (this.property.getValue() - this.property.getMin()) / (this.property.getMax() - this.property.getMin());
        }
        else {
            percentBar = 0.0;
        }
        XuluTessellator.drawRectGradient(this.x, this.y, this.x + percentBar * (this.width + 7.4f), this.y + this.height, ColorUtils.changeAlpha(ColorUtil.getClickGUIColor().getRGB(), 200), -1);
        if (this.isHovering(mouseX, mouseY)) {
            XuluTessellator.drawRectGradient(this.x, this.y, this.x + this.width, this.y + this.height, ColorUtils.changeAlpha(ColorUtils.Colors.BLACK, 25), -1);
        }
        if (ExeterGui.getCF()) {
            Xulu.cFontRenderer.drawStringWithShadow(String.format("%s§7 %s", this.getLabel(), displayval), this.x + 2.3f, this.y + 3.0f, -1);
        }
        else {
            NumberSlider.fontRenderer.drawStringWithShadow(String.format("%s§7 %s", this.getLabel(), displayval), this.x + 2.3f, this.y + 4.0f, -1);
        }
        if (this.dragging) {
            Number val;
            if (this.property.getValue() instanceof Integer) {
                final int diff = this.property.getMax() - this.property.getMin();
                val = this.property.getMin() + MathHelper.clamp((mouseX - this.x) / (this.width + 7.4f), 0.0f, 1.0f) * diff;
            }
            else if (this.property.getValue() instanceof Short) {
                final short diff2 = (short)((short)this.property.getMax() - (short)this.property.getMin());
                val = this.property.getMin() + MathHelper.clamp((mouseX - this.x) / (this.width + 7.4f), 0.0f, 1.0f) * diff2;
            }
            else if (this.property.getValue() instanceof Long) {
                final long diff3 = this.property.getMax() - this.property.getMin();
                val = this.property.getMin() + MathHelper.clamp((mouseX - this.x) / (this.width + 7.4f), 0.0f, 1.0f) * diff3;
            }
            else if (this.property.getValue() instanceof Float) {
                final float diff4 = this.property.getMax() - this.property.getMin();
                val = this.property.getMin() + MathHelper.clamp((mouseX - this.x) / (this.width + 7.4f), 0.0f, 1.0f) * diff4;
            }
            else if (this.property.getValue() instanceof Double) {
                final double diff5 = this.property.getMax() - this.property.getMin();
                val = this.property.getMin() + MathHelper.clamp((mouseX - this.x) / (this.width + 7.4f), 0.0f, 1.0f) * diff5;
            }
            else {
                val = 0.0;
            }
            Number type;
            if (this.property.getValue() instanceof Integer) {
                type = val.intValue();
            }
            else if (this.property.getValue() instanceof Short) {
                type = val.shortValue();
            }
            else if (this.property.getValue() instanceof Long) {
                type = val.longValue();
            }
            else if (this.property.getValue() instanceof Float) {
                type = val.floatValue();
            }
            else if (this.property.getValue() instanceof Double) {
                type = val.doubleValue();
            }
            else {
                type = 0;
            }
            this.property.setValue(type);
        }
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (this.isHovering(mouseX, mouseY) && mouseButton == 0) {
            if (ExeterGui.getSound()) {
                NumberSlider.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            }
            this.dragging = true;
            return;
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
        this.dragging = false;
    }
    
    @Override
    public int getHeight() {
        return 14;
    }
    
    private boolean isHovering(final int mouseX, final int mouseY) {
        for (final Panel panel : ClickGui.getClickGui().getPanels()) {
            if (panel.drag) {
                return false;
            }
        }
        return mouseX >= this.getX() && mouseX <= this.getX() + this.getWidth() && mouseY >= this.getY() && mouseY <= this.getY() + this.height;
    }
    
    private float getValueWidth() {
        return this.property.getMax().floatValue() - this.property.getMin().floatValue() + this.property.getValue().floatValue();
    }
}
