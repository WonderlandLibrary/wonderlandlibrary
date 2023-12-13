// 
// Decompiled by Procyon v0.5.36
// 

package dev.xulu.clickgui.item;

import com.elementars.eclient.module.Category;
import java.io.IOException;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import com.elementars.eclient.module.render.ExeterGui;
import java.util.Iterator;
import dev.xulu.clickgui.item.properties.TextButton;
import dev.xulu.clickgui.item.properties.BindButton;
import com.elementars.eclient.guirewrite.Element;
import dev.xulu.clickgui.item.properties.EnumButton;
import dev.xulu.clickgui.item.properties.ModeButton;
import dev.xulu.clickgui.item.properties.NumberSlider;
import dev.xulu.clickgui.item.properties.BooleanButton;
import dev.xulu.settings.Value;
import com.elementars.eclient.Xulu;
import java.util.ArrayList;
import dev.xulu.clickgui.Panel;
import java.util.List;
import com.elementars.eclient.module.Module;
import com.elementars.eclient.util.Helper;

public class ModuleButton extends Button implements Helper
{
    private final Module module;
    private List<Item> items;
    private boolean subOpen;
    
    public ModuleButton(final Module module, final Panel parent) {
        super(module.getName(), parent);
        this.items = new ArrayList<Item>();
        this.module = module;
        if (Xulu.VALUE_MANAGER.getSettingsByMod(module) != null) {
            for (final Value s : Xulu.VALUE_MANAGER.getSettingsByMod(module)) {
                if (s.isToggle()) {
                    this.items.add(new BooleanButton(s));
                }
                else if (s.isNumber()) {
                    this.items.add(new NumberSlider(s));
                }
                else if (s.isMode()) {
                    this.items.add(new ModeButton(s));
                }
                else if (s.isEnum()) {
                    this.items.add(new EnumButton(s));
                }
                else if (s.isBind() && !(s.getParentMod() instanceof Element)) {
                    this.items.add(new BindButton(s));
                }
                else {
                    if (!s.isText()) {
                        continue;
                    }
                    this.items.add(new TextButton(s));
                }
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (!this.items.isEmpty()) {
            if (Xulu.VALUE_MANAGER.getValuesByMod(this.module) != null) {
                if (ExeterGui.getCF()) {
                    Xulu.cFontRenderer.drawStringWithShadow("...", this.x + this.width - Xulu.cFontRenderer.getStringWidth("...") - 3.0f, this.y + 3.0f, -1);
                }
                else {
                    ModuleButton.fontRenderer.drawStringWithShadow("...", this.x + this.width - ModuleButton.fontRenderer.getStringWidth("...") - 2.0f, this.y + 4.0f, -1);
                }
            }
            if (this.subOpen) {
                float height = 1.0f;
                for (final Item item : this.items) {
                    if (!item.property.isVisible()) {
                        continue;
                    }
                    height += 15.0f;
                    item.setLocation(this.x + 1.0f, this.y + height);
                    item.setHeight(15);
                    item.setWidth(this.width - 9);
                    item.drawScreen(mouseX, mouseY, partialTicks);
                }
            }
        }
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (!this.items.isEmpty()) {
            if (mouseButton == 1 && this.isHovering(mouseX, mouseY)) {
                this.subOpen = !this.subOpen;
                if (ExeterGui.getSound()) {
                    ModuleButton.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
                }
            }
            if (this.subOpen) {
                for (final Item item : this.items) {
                    if (!item.property.isVisible()) {
                        continue;
                    }
                    item.mouseClicked(mouseX, mouseY, mouseButton);
                }
            }
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int releaseButton) {
        this.items.forEach(item -> {
            if (item.property.isVisible()) {
                item.mouseReleased(mouseX, mouseY, releaseButton);
            }
            return;
        });
        super.mouseReleased(mouseX, mouseY, releaseButton);
    }
    
    @Override
    public boolean keyTyped(final char typedChar, final int keyCode) throws IOException {
        for (final Item i : this.items) {
            if (!i.property.isVisible()) {
                continue;
            }
            try {
                if (i.keyTyped(typedChar, keyCode)) {
                    return true;
                }
                continue;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return super.keyTyped(typedChar, keyCode);
    }
    
    @Override
    public int getHeight() {
        if (this.subOpen) {
            int height = 14;
            for (final Item item : this.items) {
                if (!item.property.isVisible()) {
                    continue;
                }
                height += item.getHeight() + 1;
            }
            return height + 2;
        }
        return 14;
    }
    
    @Override
    public void toggle() {
        if (this.module.getName().equalsIgnoreCase("HudEditor") || !this.module.getCategory().equals(Category.HUD)) {
            this.module.toggle();
        }
    }
    
    @Override
    public boolean getState() {
        return this.module.isToggled();
    }
}
