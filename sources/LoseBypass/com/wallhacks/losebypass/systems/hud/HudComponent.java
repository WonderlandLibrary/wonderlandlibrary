/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package com.wallhacks.losebypass.systems.hud;

import com.wallhacks.losebypass.LoseBypass;
import com.wallhacks.losebypass.gui.ClickGui;
import com.wallhacks.losebypass.gui.tabs.HudTab;
import com.wallhacks.losebypass.systems.SettingsHolder;
import com.wallhacks.losebypass.systems.clientsetting.clientsettings.ClickGuiConfig;
import com.wallhacks.losebypass.systems.hud.HudSettings;
import com.wallhacks.losebypass.systems.setting.settings.BooleanSetting;
import com.wallhacks.losebypass.systems.setting.settings.ColorSetting;
import com.wallhacks.losebypass.systems.setting.settings.DoubleSetting;
import com.wallhacks.losebypass.systems.setting.settings.IntSetting;
import com.wallhacks.losebypass.systems.setting.settings.KeySetting;
import com.wallhacks.losebypass.systems.setting.settings.ModeSetting;
import com.wallhacks.losebypass.systems.setting.settings.StringSetting;
import com.wallhacks.losebypass.utils.GuiUtil;
import com.wallhacks.losebypass.utils.MC;
import com.wallhacks.losebypass.utils.SnapLine;
import com.wallhacks.losebypass.utils.font.GameFontRenderer;
import java.awt.Color;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lwjgl.input.Keyboard;

public abstract class HudComponent
extends SettingsHolder
implements MC {
    private final String name = this.getMod().name();
    private final String description = this.getMod().description();
    public int posX;
    public int posY;
    protected int width;
    protected int height;
    protected HudSettings hudSettings = HudTab.hudSettings;
    private boolean enabled = this.getMod().enabled();

    protected HudComponent() {
        LoseBypass.eventBus.register(this);
    }

    public ArrayList<SnapLine> updateSnapLines() {
        ArrayList<SnapLine> snapLines = new ArrayList<SnapLine>();
        snapLines.add(new SnapLine(SnapLine.Type.X, this.posX, this));
        snapLines.add(new SnapLine(SnapLine.Type.X, this.posX + this.width, this));
        snapLines.add(new SnapLine(SnapLine.Type.Y, this.posY, this));
        snapLines.add(new SnapLine(SnapLine.Type.Y, this.posY + this.height, this));
        return snapLines;
    }

    protected GameFontRenderer fontRenderer() {
        return LoseBypass.fontManager.getHudFont();
    }

    public Registration getMod() {
        return this.getClass().getAnnotation(Registration.class);
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            this.enable();
            return;
        }
        this.disable();
    }

    public void drawComponent(boolean hudEditor, float deltaTime) {
    }

    public boolean handleHudEditor(float deltaTime, int mouseX, int mouseY, boolean dragging, int offsetX, int offsetY, ArrayList<SnapLine> lines) {
        boolean hover;
        block17: {
            Color main;
            block15: {
                Iterator<SnapLine> iterator;
                ArrayList<SnapLine> snapped;
                boolean yTop;
                boolean xLeft;
                int YScore;
                int XScore;
                block16: {
                    block14: {
                        boolean bl = hover = dragging || mouseX > this.posX && mouseX < this.posX + this.width && mouseY > this.posY && mouseY < this.posY + this.height;
                        if (!dragging) break block14;
                        this.posX = mouseX - offsetX;
                        this.posY = mouseY - offsetY;
                        main = ClickGuiConfig.getInstance().getMainColor();
                        if (Keyboard.isKeyDown((int)42)) break block15;
                        XScore = 5;
                        YScore = 5;
                        xLeft = false;
                        yTop = false;
                        snapped = new ArrayList<SnapLine>();
                        iterator = lines.iterator();
                        break block16;
                    }
                    if (hover) {
                        GuiUtil.drawRect(this.posX, this.posY, this.posX + this.width, this.posY + this.height, new Color(30, 30, 30, 120).getRGB());
                    } else {
                        GuiUtil.drawRect(this.posX, this.posY, this.posX + this.width, this.posY + this.height, new Color(0, 0, 0, 120).getRGB());
                    }
                    break block17;
                }
                block8: while (iterator.hasNext()) {
                    SnapLine snap = iterator.next();
                    if (snap.parent == this) continue;
                    switch (1.$SwitchMap$com$wallhacks$losebypass$utils$SnapLine$Type[snap.type.ordinal()]) {
                        case 1: {
                            if (Math.abs(snap.offset - this.posX) < XScore) {
                                XScore = Math.abs(snap.offset - this.posX);
                                snapped.add(snap);
                                xLeft = false;
                                break;
                            }
                            if (Math.abs(snap.offset - (this.posX + this.width)) >= XScore) break;
                            XScore = Math.abs(snap.offset - (this.posX + this.width));
                            snapped.add(snap);
                            xLeft = true;
                            break;
                        }
                        case 2: {
                            if (Math.abs(snap.offset - this.posY) < YScore) {
                                YScore = Math.abs(snap.offset - this.posY);
                                snapped.add(snap);
                                yTop = false;
                                break;
                            }
                            if (Math.abs(snap.offset - (this.posY + this.height)) >= YScore) break;
                            YScore = Math.abs(snap.offset - (this.posY + this.height));
                            snapped.add(snap);
                            yTop = true;
                            continue block8;
                        }
                    }
                }
                block9: for (SnapLine snap : snapped) {
                    switch (1.$SwitchMap$com$wallhacks$losebypass$utils$SnapLine$Type[snap.type.ordinal()]) {
                        case 1: {
                            this.posX = !xLeft ? snap.offset : snap.offset - this.width;
                            GuiUtil.drawRect((float)snap.offset - 0.5f, -1.0f, (float)snap.offset + 0.5f, ClickGui.height, ClickGui.mainColor());
                            break;
                        }
                        case 2: {
                            this.posY = !yTop ? snap.offset : snap.offset - this.height;
                            GuiUtil.drawRect(0.0f, (float)snap.offset - 0.5f, ClickGui.width, (float)snap.offset + 0.5f, ClickGui.mainColor());
                            continue block9;
                        }
                    }
                }
            }
            GuiUtil.drawRect(this.posX, this.posY, this.posX + this.width, this.posY + this.height, new Color(main.getRed(), main.getGreen(), main.getBlue(), 80).getRGB());
        }
        GuiUtil.drawRect(this.posX, this.posY, this.posX + this.width, this.posY + 1, Color.BLACK.getRGB());
        GuiUtil.drawRect(this.posX, this.posY + this.height, this.posX + this.width, this.posY + this.height - 1, Color.BLACK.getRGB());
        GuiUtil.drawRect(this.posX, this.posY, this.posX + 1, this.posY + this.height, Color.BLACK.getRGB());
        GuiUtil.drawRect(this.posX + this.width - 1, this.posY, this.posX + this.width, this.posY + this.height, Color.BLACK.getRGB());
        this.drawComponent(true, deltaTime);
        return hover;
    }

    protected void drawBackground() {
        switch ((String)this.hudSettings.backgroundMode.getValue()) {
            case "Blur": 
            case "Outline": 
            case "Normal": {
                GuiUtil.drawRect(this.posX, this.posY, this.posX + this.width, this.posY + this.height, this.hudSettings.background.getRGB());
                return;
            }
        }
    }

    protected void drawStringCentered(String string) {
        float centerX = (float)this.posX + (float)this.width / 2.0f;
        float centerY = (float)this.posY + (float)this.height * 0.25f;
        this.fontRenderer().drawString(string, centerX -= (float)this.fontRenderer().getStringWidth(string) / 2.0f, centerY, this.hudSettings.textColor.getRGB());
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void toggle() {
        this.setEnabled(!this.isEnabled());
    }

    public void enable() {
        if (this.enabled) return;
        LoseBypass.eventBus.register(this);
        LoseBypass.logger.info("Enabled " + this.getName());
        this.enabled = true;
        this.onEnable();
    }

    public void disable() {
        if (!this.enabled) return;
        LoseBypass.eventBus.unregister(this);
        LoseBypass.logger.info("Disabled " + this.getName());
        this.enabled = false;
        this.onDisable();
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public BooleanSetting booleanSetting(String name, boolean value) {
        return new BooleanSetting(name, this, value);
    }

    public ColorSetting colorSetting(String name, Color value) {
        return new ColorSetting(name, this, value);
    }

    public DoubleSetting doubleSetting(String name, double value, double min, double max) {
        return new DoubleSetting(name, this, value, min, max);
    }

    public IntSetting intSetting(String name, int value, int min, int max) {
        return new IntSetting(name, this, value, min, max);
    }

    public KeySetting keySetting(String name, int value) {
        return new KeySetting(name, this, value);
    }

    public ModeSetting modeSetting(String name, String value, List<String> values) {
        return new ModeSetting(name, this, value, values);
    }

    public StringSetting modeSetting(String name, String value) {
        return new StringSetting(name, this, value);
    }

    @Retention(value=RetentionPolicy.RUNTIME)
    @Target(value={ElementType.TYPE})
    public static @interface Registration {
        public String name();

        public String description();

        public boolean enabled() default false;
    }
}

