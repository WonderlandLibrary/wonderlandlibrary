// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.csgoGui.buttons.setting.settings;

import java.io.IOException;
import org.lwjgl.input.Keyboard;
import net.augustus.Augustus;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.Setting;
import net.augustus.csgoGui.buttons.setting.CSSetting;

public class CSSettingDouble extends CSSetting
{
    public CSSettingDouble(final int x, final int y, final int width, final int height, final Setting s) {
        super(x, y, width, height, s);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final double reach = ((DoubleValue)this.set).getValue();
        final double reach2 = reach * 100.0;
        final double reach3 = (double)Math.round(reach2);
        final double round = reach3 / 100.0;
        this.mc.fontRendererObj.drawString("<", this.x + 1, this.y + 1, this.isHoveredLeft(mouseX, mouseY) ? Augustus.getInstance().getClientColor().getRGB() : Integer.MAX_VALUE);
        this.mc.fontRendererObj.drawString(">", this.x + 1 + this.fr.getStringWidth(this.set.getName() + " " + round) + 15, this.y + 1, this.isHoveredRight(mouseX, mouseY) ? Augustus.getInstance().getClientColor().getRGB() : Integer.MAX_VALUE);
        this.mc.fontRendererObj.drawString(this.set.getName() + " " + round, this.x + 10, this.y + 1, Integer.MAX_VALUE);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    public boolean isHoveredLeft(final int mouseX, final int mouseY) {
        final boolean hoveredx = mouseX > this.x + 1 && mouseX < this.x + 1 + 5;
        final boolean hoveredy = mouseY > this.y + 1 && mouseY < this.y + this.mc.fontRendererObj.FONT_HEIGHT;
        return hoveredx && hoveredy;
    }
    
    public boolean isHoveredRight(final int mouseX, final int mouseY) {
        final double round = (double)(Math.round(((DoubleValue)this.set).getValue() * 10.0) / 10L);
        final boolean hoveredx = mouseX > this.x + 1 + this.fr.getStringWidth(this.set.getName() + " " + round) + 15 && mouseX < this.x + 1 + this.fr.getStringWidth(this.set.getName() + " " + round) + 20;
        final boolean hoveredy = mouseY > this.y + 1 && mouseY < this.y + this.mc.fontRendererObj.FONT_HEIGHT;
        return hoveredx && hoveredy;
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        final DoubleValue s = (DoubleValue)this.set;
        final boolean uhh = s.getDecimalPlaces() == 0;
        if (mouseButton == 0) {
            if (this.isHoveredLeft(mouseX, mouseY)) {
                final boolean more = Keyboard.isKeyDown(42);
                double plus = 0.0;
                if (!Keyboard.isKeyDown(42) && !Keyboard.isKeyDown(29)) {
                    if (uhh) {
                        plus = 1.0;
                    }
                    else {
                        plus = 0.1;
                    }
                }
                else if (Keyboard.isKeyDown(42) && !Keyboard.isKeyDown(29)) {
                    plus = (uhh ? 10.0 : 1.0);
                }
                else if (Keyboard.isKeyDown(42) && Keyboard.isKeyDown(29)) {
                    plus = (uhh ? 1.0 : 0.01);
                }
                else if (!Keyboard.isKeyDown(42) && Keyboard.isKeyDown(29)) {
                    plus = 0.0;
                    s.setValue(s.getMinValue());
                }
                if (s.getValue() - plus < s.getMinValue() || s.getValue() - plus == s.getMinValue()) {
                    s.setValue(s.getMinValue());
                }
                else if (s.getValue() - plus > s.getMinValue()) {
                    s.setValue(s.getValue() - plus);
                }
                if (Keyboard.isKeyDown(29)) {
                    s.setValue(s.getMinValue());
                }
            }
            else if (this.isHoveredRight(mouseX, mouseY)) {
                double plus2 = 0.0;
                if (!Keyboard.isKeyDown(42) && !Keyboard.isKeyDown(29)) {
                    if (uhh) {
                        plus2 = 1.0;
                    }
                    else {
                        plus2 = 0.1;
                    }
                }
                else if (Keyboard.isKeyDown(42) && !Keyboard.isKeyDown(29)) {
                    plus2 = (uhh ? 10.0 : 1.0);
                }
                else if (Keyboard.isKeyDown(42) && Keyboard.isKeyDown(29)) {
                    plus2 = (uhh ? 1.0 : 0.01);
                }
                else if (!Keyboard.isKeyDown(42) && Keyboard.isKeyDown(29)) {
                    plus2 = 0.0;
                    s.setValue(s.getMaxValue());
                }
                if (s.getValue() + plus2 > s.getMaxValue() || s.getValue() + plus2 == s.getMaxValue()) {
                    s.setValue(s.getMaxValue());
                }
                else if (s.getValue() + plus2 < s.getMaxValue()) {
                    s.setValue(s.getValue() + plus2);
                }
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}
