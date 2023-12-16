// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.elements.settings;

import java.util.Iterator;
import moonsense.features.SCModule;
import moonsense.features.ModuleManager;
import net.minecraft.client.resources.I18n;
import moonsense.features.SettingsManager;
import moonsense.ui.utils.GuiUtils;
import java.awt.Color;
import org.lwjgl.input.Keyboard;
import moonsense.MoonsenseClient;
import moonsense.utils.KeyBinding;
import moonsense.ui.screen.AbstractGuiScreen;
import java.util.function.BiConsumer;
import moonsense.settings.Setting;

public class SettingElementKeybind extends SettingElement
{
    public int keycode;
    public boolean selection;
    private int bgFade;
    
    public SettingElementKeybind(final int x, final int y, final int width, final int height, final Setting setting, final BiConsumer<Setting, SettingElement> consumer, final AbstractGuiScreen parent) {
        super(x, y, width, height, setting, consumer, parent);
        this.selection = false;
        this.bgFade = 50;
        this.front = true;
        this.keycode = ((KeyBinding)setting.getObject()).getKeyCode();
        if (this.keycode == -1) {
            this.width = (int)Math.max(10.0f, MoonsenseClient.titleRenderer.getWidth("Not Set") + 4.0f);
        }
        else {
            this.width = (int)Math.max(10.0f, MoonsenseClient.titleRenderer.getWidth(Keyboard.getKeyName(this.keycode)) + 4.0f);
        }
    }
    
    @Override
    public void renderElement(final float partialTicks) {
        if (this.keycode == -1) {
            MoonsenseClient.titleRenderer.drawCenteredString(this.selection ? "><" : "Not Set", this.getX() + this.width / 2.0f - 1.0f, (float)this.getY(), 16777215);
        }
        else {
            MoonsenseClient.titleRenderer.drawCenteredString(this.selection ? "><" : Keyboard.getKeyName(this.keycode), this.getX() + this.width / 2.0f - 1.0f, (float)this.getY(), 16777215);
        }
    }
    
    @Override
    public void renderBackground(final float partialTicks) {
        final boolean flag = !this.conflictingKeybindName().isEmpty() && !((KeyBinding)this.setting.getObject()).ignoreClashes();
        GuiUtils.drawRoundedRect((float)this.getX(), (float)this.getY(), (float)(this.getX() + this.width), (float)(this.getY() + this.height), 1.0f, flag ? new Color(210, 75, 75, this.bgFade).getRGB() : MoonsenseClient.getMainColor(this.bgFade));
        GuiUtils.drawRoundedOutline(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, 1.0f, 2.0f, flag ? new Color(210, 75, 75, this.bgFade).getRGB() : MoonsenseClient.getMainColor(255));
        if (!this.conflictingKeybindName().isEmpty() && this.hovered) {
            GuiUtils.drawRoundedRect((float)(this.getX() + this.width + 2), (float)this.getY(), this.getX() + this.width + 2 + MoonsenseClient.titleRenderer.getWidth(this.conflictingKeybindName()), (float)(this.getY() + this.height), 1.0f, new Color(210, 75, 75, 150).getRGB());
            GuiUtils.drawRoundedOutline((float)(this.getX() + this.width + 2), (float)this.getY(), this.getX() + this.width + 2 + MoonsenseClient.titleRenderer.getWidth(this.conflictingKeybindName()), (float)(this.getY() + this.height), 1.0f, 2.0f, new Color(210, 75, 75, 255).getRGB());
            MoonsenseClient.titleRenderer.drawString(this.conflictingKeybindName(), this.getX() + this.width + 2, this.getY(), Color.white.darker().getRGB());
        }
    }
    
    @Override
    public void update() {
        if (this.hovered && this.bgFade + 10 < 150) {
            this.bgFade += 10;
        }
        else if (!this.hovered && this.bgFade - 10 > 50) {
            this.bgFade -= 10;
        }
    }
    
    @Override
    public boolean mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (this.enabled && this.hovered) {
            this.selection = true;
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
        if (this.selection) {
            if (keyCode == 1 && this.setting != SettingsManager.INSTANCE.hudEditorKeybind) {
                this.selection = false;
                this.keycode = -1;
                this.width = (int)Math.max(10.0f, MoonsenseClient.titleRenderer.getWidth("Not Set") + 4.0f);
                if (this.consumer != null) {
                    this.consumer.accept(this.setting, this);
                }
                this.selection = false;
                return;
            }
            if (keyCode == 1) {
                return;
            }
            this.keycode = keyCode;
            this.width = (int)Math.max(10.0f, MoonsenseClient.titleRenderer.getWidth(Keyboard.getKeyName(keyCode)) + 4.0f);
            if (this.consumer != null) {
                this.consumer.accept(this.setting, this);
            }
            this.selection = false;
        }
    }
    
    private String conflictingKeybindName() {
        if (this.keycode == -1) {
            return "";
        }
        if (((KeyBinding)this.setting.getObject()).ignoreClashes()) {
            return "";
        }
        boolean keyBindClashes = false;
        String clashingName = "";
        if (this.keycode != 0) {
            for (final net.minecraft.client.settings.KeyBinding var14 : this.mc.gameSettings.keyBindings) {
                if (var14.getKeyCode() == this.keycode) {
                    keyBindClashes = true;
                    clashingName = "Clashes with: " + I18n.format(var14.getKeyDescription(), new Object[0]);
                    break;
                }
            }
            if (!keyBindClashes) {
                for (final SCModule m : ModuleManager.INSTANCE.modules) {
                    for (final Setting s : m.settings) {
                        if (s.getType() == Setting.Type.KEYBIND) {
                            if (s.equals(this.setting)) {
                                continue;
                            }
                            if (s.getValue().get(0).getKeyCode() == this.keycode) {
                                keyBindClashes = true;
                                clashingName = " Clashes with: (" + m.displayName + ") " + s.getDescription() + " ";
                                break;
                            }
                            break;
                        }
                    }
                }
            }
        }
        return clashingName;
    }
}
