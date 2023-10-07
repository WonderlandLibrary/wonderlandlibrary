package com.ohare.client.gui.click.component.comps;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import com.ohare.client.gui.click.component.Component;
import com.ohare.client.gui.click.component.comps.impl.BooleanButton;
import com.ohare.client.gui.click.component.comps.impl.EnumButton;
import com.ohare.client.gui.click.component.comps.impl.SliderButton;
import com.ohare.client.gui.click.tab.Tab;
import com.ohare.client.module.Module;
import com.ohare.client.utils.RenderUtil;
import com.ohare.client.utils.font.Fonts;
import com.ohare.client.utils.value.Value;
import com.ohare.client.utils.value.impl.BooleanValue;
import com.ohare.client.utils.value.impl.EnumValue;
import com.ohare.client.utils.value.impl.NumberValue;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

/**
 * made by oHare for oHareWare
 *
 * @since 6/4/2019
 **/
public class ModButton extends Component {
    private Module mod;
    private Tab parent;
    private ArrayList<Component> subcomponents = new ArrayList<>();
    private int largestString;

    public ModButton(Tab parent, Module mod, float posX, float posY, float width, float height) {
        super(mod.getLabel(), posX, posY, width, height);
        this.parent = parent;
        this.mod = mod;
        float valY = parent.getPosY() + 18;
        for (Value value : mod.getValues()) {
            if (value instanceof BooleanValue) {
                BooleanButton booleanButton = new BooleanButton(this, ((BooleanValue) value), parent.getPosX() + parent.getLargestString() * 2 + 22, valY - parent.getParent().getValuescrollY(), width, height);
                subcomponents.add(booleanButton);
                valY += 14;
            }
            if (value instanceof NumberValue) {
                SliderButton numberButton = new SliderButton(this, ((NumberValue) value), parent.getPosX() + parent.getLargestString() * 2 + 22, valY - parent.getParent().getValuescrollY(), width, height);
                subcomponents.add(numberButton);
                valY += 14;
            }
            if (value instanceof EnumValue) {
                EnumButton enumButton = new EnumButton(this, ((EnumValue) value), parent.getPosX() + parent.getLargestString() * 2 + 22, valY - parent.getParent().getValuescrollY(), width, height);
                subcomponents.add(enumButton);
                valY += 14;
            }
            if (!mod.getValues().isEmpty()) {
                largestString = Fonts.clickfont.getStringWidth(mod.getValues().get(0).getLabel());
                for (int i = 0; i < mod.getValues().size(); i++) {
                    if (Fonts.clickfont.getStringWidth(mod.getValues().get(i).getLabel()) > largestString) {
                        largestString = Fonts.clickfont.getStringWidth(mod.getValues().get(i).getLabel());
                    }
                }
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.drawRoundedRect(parent.getPosX() + 4, getPosY() - 4, (parent.getLargestString() * 2) - 4, 12, 4, mod.isEnabled() ? (mouseWithinBounds(mouseX, mouseY, parent.getPosX() + 4, getPosY() - 4, (parent.getLargestString() * 2) - 4, 12) ? new Color(0, 130, 240, 255).getRGB() : new Color(0, 107, 214, 255).getRGB()) : (mouseWithinBounds(mouseX, mouseY, parent.getPosX() + 4, getPosY() - 4, (parent.getLargestString() * 2) - 4, 12) ? new Color(0x505760).getRGB() : new Color(48, 56, 66, 255).getRGB()));
        Fonts.clickfont.drawString(mod.getLabel(), getPosX(), getPosY(), -1);
        if (!mod.getValues().isEmpty()) {
            RenderUtil.drawRoundedRect(parent.getPosX() + (parent.getLargestString() * 2) + 2, getPosY() - 4, 6, 12, 4, (parent.getParent().isExtended() && parent.getParent().getExtendedmod() == mod) ? (mouseWithinBounds(mouseX, mouseY, parent.getPosX() + (parent.getLargestString() * 2) + 2, getPosY() - 4, 6, 12) ? new Color(0, 130, 240, 255).getRGB() : new Color(0, 107, 214, 255).getRGB()) : (mouseWithinBounds(mouseX, mouseY, parent.getPosX() + (parent.getLargestString() * 2) + 2, getPosY() - 4, 6, 12) ? new Color(0x505760).getRGB() : new Color(48, 56, 66, 255).getRGB()));
            Fonts.clickfont.drawString(".", parent.getPosX() + (parent.getLargestString() * 2) + 4, getPosY() - 5, -1);
            Fonts.clickfont.drawString(".", parent.getPosX() + (parent.getLargestString() * 2) + 4, getPosY() - 2, -1);
            Fonts.clickfont.drawString(".", parent.getPosX() + (parent.getLargestString() * 2) + 4, getPosY() + 1, -1);
        }

        if (parent.getParent().isExtended() && parent.getParent().getExtendedmod() == mod) {
            if (18 + (mod.getValues().size() * 14) > 178) {
                if (mouseWithinBounds(mouseX, mouseY, parent.getPosX() + parent.getLargestString() * 2 + 16, parent.getPosY(), ((largestString + 16) > (Fonts.hudfont.getStringWidth(parent.getParent().getExtendedmod().getLabel() + " Settings") + 16) ? (largestString + 16) : (Fonts.hudfont.getStringWidth(parent.getParent().getExtendedmod().getLabel() + " Settings") + 16)) > 100 ? ((largestString + 16) > (Fonts.hudfont.getStringWidth(parent.getParent().getExtendedmod().getLabel() + " Settings") + 16) ? (largestString + 16) : (Fonts.hudfont.getStringWidth(parent.getParent().getExtendedmod().getLabel() + " Settings") + 16)) : 100, 20 + (mod.getValues().size() * 14) > 180 ? 180 : 20 + (mod.getValues().size() * 14))) {
                    int wheel = Mouse.getDWheel();
                    if (Mouse.hasWheel()) {
                        if (wheel > 0) {
                            parent.getParent().setValuescrollY(parent.getParent().getValuescrollY() - 4);
                        } else if (wheel < 0) {
                            if (parent.getParent().getValuescrollY() >= (mod.getValues().size() * 14) - 162) {
                                parent.getParent().setValuescrollY((mod.getValues().size() * 14)- 162);
                            } else {
                                parent.getParent().setValuescrollY(parent.getParent().getValuescrollY() + 4);
                            }
                        }
                        if (parent.getParent().getValuescrollY() < 0) {
                            parent.getParent().setValuescrollY(0);
                        }
                    }
                }
            } else {
                if (parent.getParent().getValuescrollY() != 0) parent.getParent().setValuescrollY(0);
            }
            RenderUtil.drawBorderedRect(parent.getPosX() + parent.getLargestString() * 2 + 16, parent.getPosY(), (((largestString + 14) > (Fonts.hudfont.getStringWidth(parent.getParent().getExtendedmod().getLabel() + " Settings") + 14) ? (largestString + 14) : (Fonts.hudfont.getStringWidth(parent.getParent().getExtendedmod().getLabel() + " Settings") + 14)) > 100 ? ((largestString + 14) > (Fonts.hudfont.getStringWidth(parent.getParent().getExtendedmod().getLabel() + " Settings") + 14) ? (largestString + 14) : (Fonts.hudfont.getStringWidth(parent.getParent().getExtendedmod().getLabel() + " Settings") + 14)) : 96) - 4, 20 + (subcomponents.size() * 14) > 180 ? 180 : 20 + (subcomponents.size() * 14), 0.5, new Color(28, 34, 44, 255).getRGB(), new Color(31, 38, 48, 255).getRGB());

            final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            Fonts.hudfont.drawString(parent.getParent().getExtendedmod().getLabel() + " Settings", parent.getPosX() + parent.getLargestString() * 2 + 21, parent.getPosY() + 6 - parent.getParent().getValuescrollY(), -1);
            subcomponents.forEach(component -> component.drawScreen(mouseX, mouseY, partialTicks));
        }
        if (mouseWithinBounds(mouseX, mouseY, parent.getPosX() + 4, getPosY() - 4, (parent.getLargestString() * 2) - 4, 12)) {
            Fonts.clickfont.drawStringWithShadow(mod.getDescription(), mouseX + 5, mouseY, -1);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            if (mouseWithinBounds(mouseX, mouseY, parent.getPosX() + 4, getPosY() - 4, (parent.getLargestString() * 2) - 4, 12)) {
                mod.setEnabled(!mod.isEnabled());
            }
            if (!mod.getValues().isEmpty() && mouseWithinBounds(mouseX, mouseY, parent.getPosX() + (parent.getLargestString() * 2) + 2, getPosY() - 4, 6, 12)) {
                if (parent.getParent().isExtended() && parent.getParent().getExtendedmod() == mod) {
                    parent.getParent().setExtended(false);
                    parent.getParent().setExtendedmod(null);
                    parent.getParent().setValuescrollY(0);
                } else {
                    parent.getParent().setExtendedmod(mod);
                    parent.getParent().setExtended(true);
                    parent.getParent().setValuescrollY(0);
                }
            }
        }
        if (parent.getParent().getExtendedmod() == mod) {
            int largestString = (Fonts.clickfont.getStringWidth(mod.getValues().get(0).getLabel()));
            for (int i = 0; i < mod.getValues().size(); i++) {
                if (Fonts.clickfont.getStringWidth(mod.getValues().get(i).getLabel()) > largestString) {
                    largestString = Fonts.clickfont.getStringWidth(mod.getValues().get(i).getLabel());
                }
            }
            if (mouseWithinBounds(mouseX, mouseY, parent.getPosX() + parent.getLargestString() * 2 + 16, parent.getPosY(), (((largestString + 16) > (Fonts.hudfont.getStringWidth(parent.getParent().getExtendedmod().getLabel() + " Settings") + 16) ? (largestString + 16) : (Fonts.hudfont.getStringWidth(parent.getParent().getExtendedmod().getLabel() + " Settings") + 16)) > 100 ? ((largestString + 16) > (Fonts.hudfont.getStringWidth(parent.getParent().getExtendedmod().getLabel() + " Settings") + 16) ? (largestString + 16) : (Fonts.hudfont.getStringWidth(parent.getParent().getExtendedmod().getLabel() + " Settings") + 16)) : 100) + 200, 180)) {
                subcomponents.forEach(component -> component.mouseClicked(mouseX, mouseY, mouseButton));
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);

        if (parent.getParent().getExtendedmod() == mod) {
            subcomponents.forEach(component -> component.mouseReleased(mouseX, mouseY, mouseButton));
        }
    }

    @Override
    public void keyTyped(char typedChar, int key) {
        super.keyTyped(typedChar, key);
        if (parent.getParent().getExtendedmod() == mod) {
            subcomponents.forEach(component -> component.keyTyped(typedChar, key));
        }
    }

    public Tab getParent() {
        return this.parent;
    }

    @Override
	public boolean mouseWithinBounds(int mouseX, int mouseY, double x, double y, double width, double height) {
        return (mouseX >= x && mouseX <= (x + width)) && (mouseY >= y && mouseY <= (y + height));
    }

    public int getLargestString() {
        return this.largestString;
    }
}

