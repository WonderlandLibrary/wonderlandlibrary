package vestige.ui.click.dropdown;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import vestige.Vestige;
import vestige.font.VestigeFontRenderer;
import vestige.module.Category;
import vestige.module.Module;
import vestige.module.impl.visual.ClickGuiModule;
import vestige.setting.impl.*;
import vestige.ui.click.dropdown.impl.CategoryHolder;
import vestige.ui.click.dropdown.impl.ModuleHolder;
import vestige.ui.click.dropdown.impl.SettingHolder;
import vestige.util.render.ColorUtil;
import vestige.util.render.DrawUtil;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class DropdownClickGUI extends GuiScreen {

    private final ClickGuiModule module;

    private final ArrayList<CategoryHolder> categories = new ArrayList<>();

    private final int categoryXOffset = 90;
    private final int categoryYOffset = 20;
    private final int moduleYOffset = 16;

    private final int settingYOffset = 14;

    private final Color moduleDisabledColor = new Color(44,46,49);

    private final Color boolSettingEnabledColor = new Color(225,225,225);

    private final Color boolSettingBox = new Color(25,25,25);

    private final int mouseHoverColor = 0x30000000;

    private int lastMouseX, lastMouseY;

    private Module keyChangeModule;

    private int scrollY;

    public DropdownClickGUI(ClickGuiModule module) {
        this.module = module;

        int x = 40;
        int y = 70;

        for (Category category : Category.values()) {
            ArrayList<ModuleHolder> modules = new ArrayList<>();
            Vestige.instance.getModuleManager().modules.stream().filter(m -> m.getCategory() == category).forEach(m -> modules.add(new ModuleHolder(m)));

            categories.add(new CategoryHolder(category, modules, x, y, true));
            x += categoryXOffset + 20;
        }
    }

    @Override
    public void initGui() {
        categories.forEach(c -> c.getModules().forEach(m -> m.updateState()));

        scrollY = 0;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        VestigeFontRenderer fr = Vestige.instance.getFontManager().getProductSans();

        GL11.glTranslatef(0, scrollY, 0);

        for (CategoryHolder category : categories) {
            if (category.isShown()) {
                if(category.isHolded()) {
                    category.setX(category.getX() + mouseX - lastMouseX);
                    category.setY(category.getY() + mouseY - lastMouseY);
                }

                int x = category.getX();
                int y = category.getY();

                Gui.drawRect(x, y, x + categoryXOffset, y + categoryYOffset, module.getColor(x + 10));

                String categoryName = category.getCategory().toString().toLowerCase();
                String capital = categoryName.substring(0, 1).toUpperCase();
                String rest = categoryName.substring(1);

                fr.drawStringWithShadow(capital + rest, x + 4, y + 5.5, -1);

                float startX = x;
                float endX = startX + categoryXOffset;

                y += categoryYOffset;

                boolean firstModule = true;
                boolean firstModuleEnabled = false;

                for (ModuleHolder holder : category.getModules()) {
                    Module m = holder.getModule();

                    float startY = y;
                    float endY = startY + moduleYOffset;

                    double mult = holder.getTimer().getTimeElapsed() / 200.0;

                    Gui.drawRect(startX, startY, endX, endY, ColorUtil.getGradient(moduleDisabledColor, new Color(module.getColor((int) startX)), m.isEnabled() ? mult : 1 - mult).getRGB());

                    if(module.boxOnHover.isEnabled() && !m.isEnabled() && mouseX > startX && mouseX < endX && mouseY > startY && mouseY < endY) {
                        Gui.drawRect(startX, startY, endX, endY, mouseHoverColor);
                    }

                    fr.drawStringWithShadow(m.getName(), startX + 3, startY + 4, -1);

                    if(holder.getSettings().size() > 0) {
                        DrawUtil.renderTriangle(endX - 11, startY + 6, boolSettingEnabledColor.getRGB());
                    }

                    y += moduleYOffset;

                    if(firstModule && (m.isEnabled() || mult < 1)) {
                        firstModuleEnabled = true;
                    }

                    if(holder.isSettingsShown()) {
                        float startKeybindY = y;
                        float endKeybindY = y + settingYOffset;

                        Gui.drawRect(startX + 1, startKeybindY, endX - 1, endKeybindY, moduleDisabledColor.getRGB());

                        if(module.boxOnHover.isEnabled() && module.boxOnSettings.isEnabled() && mouseX > startX + 1 && mouseX < endX - 1 && mouseY > startKeybindY && mouseY < endKeybindY) {
                            Gui.drawRect(startX + 1, startKeybindY, endX - 1, endKeybindY, mouseHoverColor);
                        }

                        fr.drawStringWithShadow(keyChangeModule == m ? "Waiting..." : "Keybind : " + Keyboard.getKeyName(m.getKey()), startX + 3, startKeybindY + 3.5F, -1);

                        y += settingYOffset;

                        for(SettingHolder settingHolder : holder.getSettings()) {
                            if(settingHolder.getSetting().getVisibility().get()) {
                                float startSettingY = y;
                                float endSettingY = y + settingYOffset;

                                Gui.drawRect(startX + 1, startSettingY, endX - 1, endSettingY, moduleDisabledColor.getRGB());

                                boolean hoveringSetting = mouseX > startX + 1 && mouseX < endX - 1 && mouseY > startSettingY && mouseY < endSettingY;

                                if(settingHolder.getSetting() instanceof ModeSetting) {
                                    ModeSetting setting = settingHolder.getSetting();

                                    String toRender = setting.getName() + " : " + setting.getMode();

                                    Gui.drawRect(startX + 1, endSettingY, endX - 1, endSettingY + 6, moduleDisabledColor.getRGB());

                                    if(fr.getStringWidth(toRender) > categoryXOffset - 3) {
                                        if(module.boxOnHover.isEnabled() && module.boxOnSettings.isEnabled() && mouseX > startX + 1 && mouseX < endX - 1 && mouseY > startSettingY && mouseY < endSettingY + 6) {
                                            Gui.drawRect(startX + 1, startSettingY, endX - 1, endSettingY + 6, mouseHoverColor);
                                        }

                                        fr.drawStringWithShadow(setting.getName() + " :", startX + 3, startSettingY + 2.5F, -1);
                                        fr.drawStringWithShadow(setting.getMode(), startX + 3, startSettingY + 11, -1);

                                        y += 6;
                                    } else {
                                        if(module.boxOnHover.isEnabled() && module.boxOnSettings.isEnabled() && hoveringSetting) {
                                            Gui.drawRect(startX + 1, startSettingY, endX - 1, endSettingY, mouseHoverColor);
                                        }

                                        fr.drawStringWithShadow(toRender, startX + 3, startSettingY + 3, -1);
                                    }
                                } else {
                                    if(module.boxOnHover.isEnabled() && module.boxOnSettings.isEnabled() && hoveringSetting) {
                                        Gui.drawRect(startX + 1, startSettingY, endX - 1, endSettingY, mouseHoverColor);
                                    }

                                    if(settingHolder.getSetting() instanceof BooleanSetting) {
                                        BooleanSetting setting = settingHolder.getSetting();

                                        fr.drawStringWithShadow(setting.getName(), startX + 3, startSettingY + 3, -1);

                                        Gui.drawRect(endX - 13.5, startSettingY + 2, endX - 4, startSettingY + 12, boolSettingBox.getRGB());

                                        if(setting.isEnabled()) {
                                            Gui.drawRect(endX - 12, startSettingY + 3.5, endX - 5.5, startSettingY + 10.5, module.getColor((int) (endX - 12 + y)));
                                        }
                                    } else if(settingHolder.getSetting() instanceof EnumModeSetting) {
                                        EnumModeSetting setting = settingHolder.getSetting();

                                        fr.drawStringWithShadow(setting.getName() + " : " + setting.getMode().name(), startX + 3, startSettingY + 3, -1);
                                    } else if(settingHolder.getSetting() instanceof DoubleSetting) {
                                        DoubleSetting setting = settingHolder.getSetting();

                                        float startSettingX = startX + 1;
                                        float endSettingX = endX - 1;

                                        float length = endSettingX - startSettingX;

                                        if (settingHolder.isHoldingMouse() && mouseX >= x && mouseX <= startSettingX + endSettingX && mouseY > startSettingY && mouseY < endSettingY) {

                                            double mousePos = mouseX - startSettingX;
                                            double thing = (mousePos / length);

                                            setting.setValue(thing * (setting.getMax() - setting.getMin()) + setting.getMin());
                                        }

                                        double numberX = startSettingX + ((setting.getValue() - setting.getMin()) * length / (setting.getMax() - setting.getMin()));

                                        Gui.drawRect(startSettingX, startSettingY, numberX, endSettingY, 0x75000000);

                                        fr.drawStringWithShadow(setting.getName() + " : " + setting.getStringValue(), startSettingX + 2, startSettingY + 3, -1);
                                    } else if(settingHolder.getSetting() instanceof IntegerSetting) {
                                        IntegerSetting setting = settingHolder.getSetting();

                                        float startSettingX = startX + 1;
                                        float endSettingX = endX - 1;

                                        float length = endSettingX - startSettingX;

                                        if (settingHolder.isHoldingMouse() && mouseX >= x && mouseX <= startSettingX + endSettingX && mouseY > startSettingY && mouseY < endSettingY) {

                                            double mousePos = mouseX - startSettingX;
                                            double thing = (mousePos / length);

                                            int value = (int) (thing * (setting.getMax() - setting.getMin()) + setting.getMin());

                                            setting.setValue(value);
                                        }

                                        double numberX = startSettingX + ((setting.getValue() - setting.getMin()) * length / (setting.getMax() - setting.getMin()));

                                        Gui.drawRect(startSettingX, startSettingY, numberX, endSettingY, 0x75000000);

                                        fr.drawStringWithShadow(setting.getName() + " : " + setting.getValue(), startSettingX + 2, startSettingY + 3, -1);
                                    }
                                }

                                y += settingYOffset;
                            }
                        }
                    }

                    firstModule = false;
                }

                if(firstModuleEnabled) {
                    DrawUtil.drawGradientVerticalRect(startX, category.getY() + categoryYOffset, endX, category.getY() + categoryYOffset + 2, 0x50000000, 0x15000000);
                }
            }
        }

        GL11.glTranslatef(0, -scrollY, 0);

        lastMouseX = mouseX;
        lastMouseY = mouseY;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
        VestigeFontRenderer fr = Vestige.instance.getFontManager().getProductSans();

        for (CategoryHolder category : categories) {
            if(category.isShown()) {
                int x = category.getX();
                int y = category.getY() + scrollY;

                if (mouseX > x && mouseX < x + categoryXOffset && mouseY > y && mouseY < y + categoryYOffset) {
                    category.setHolded(true);
                }

                float startX = x;

                float endX = startX + categoryXOffset;

                y += categoryYOffset;

                for (ModuleHolder holder : category.getModules()) {
                    Module m = holder.getModule();

                    float startY = y;
                    float endY = startY + moduleYOffset;

                    if(mouseX > startX && mouseX < endX && mouseY > startY && mouseY < endY) {
                        if(button == 0) {
                            m.toggle();
                            holder.updateState();
                        } else if(button == 1) {
                            holder.setSettingsShown(!holder.isSettingsShown());
                        }
                    }

                    y += moduleYOffset;

                    if(holder.isSettingsShown()) {
                        float startKeybindY = y;
                        float endKeybindY = y + settingYOffset;

                        if(button == 0 && mouseX > startX && mouseX < endX && mouseY > startKeybindY && mouseY < endKeybindY) {
                            keyChangeModule = m;
                        }

                        y += settingYOffset;

                        for(SettingHolder settingHolder : holder.getSettings()) {
                            if(settingHolder.getSetting().getVisibility().get()) {
                                float startSettingY = y;
                                float endSettingY = y + settingYOffset;

                                float realEndSettingY = endSettingY;

                                boolean hovering = mouseX > startX && mouseX < endX && mouseY > startSettingY && mouseY < realEndSettingY;

                                if(settingHolder.getSetting() instanceof BooleanSetting) {
                                    BooleanSetting setting = settingHolder.getSetting();

                                    if(button == 0 && hovering) {
                                        setting.setEnabled(!setting.isEnabled());
                                    }
                                } else if(settingHolder.getSetting() instanceof ModeSetting) {
                                    ModeSetting setting = settingHolder.getSetting();

                                    String toRender = setting.getName() + " : " + setting.getMode();

                                    if(fr.getStringWidth(toRender) > categoryXOffset - 3) {
                                        if(mouseX > startX && mouseX < endX && mouseY > startSettingY && mouseY < endSettingY + 6) {
                                            realEndSettingY = endSettingY + 6;

                                            if(button == 0) {
                                                setting.increment();
                                            } else if(button == 1) {
                                                setting.decrement();
                                            }
                                        }

                                        y += 6;
                                    } else {
                                        if(hovering) {
                                            if(button == 0) {
                                                setting.increment();
                                            } else if(button == 1) {
                                                setting.decrement();
                                            }
                                        }
                                    }
                                } else if(settingHolder.getSetting() instanceof EnumModeSetting) {
                                    EnumModeSetting setting = settingHolder.getSetting();

                                    if(hovering) {
                                        if(button == 0) {
                                            setting.increment();
                                        } else if(button == 1) {
                                            setting.decrement();
                                        }
                                    }
                                }

                                if(hovering && button == 0) {
                                    settingHolder.setHoldingMouse(true);
                                }

                                y += settingYOffset;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        categories.forEach(c -> {
            c.setHolded(false);
            c.getModules().forEach(m -> m.getSettings().forEach(s -> s.setHoldingMouse(false)));
        });
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        int i = Integer.signum(Mouse.getEventDWheel());

        scrollY -= i * 4;

        scrollY = Math.max(-500, Math.min(500, scrollY));
    }

    @Override
    protected void keyTyped(char typedChar, int key) throws IOException {
        if (key == 1) {
            this.mc.displayGuiScreen(null);

            if (this.mc.currentScreen == null) {
                this.mc.setIngameFocus();
            }
        }

        if(keyChangeModule != null) {
            keyChangeModule.setKey(key == 14 ? 0 : key);
            keyChangeModule = null;
        }
    }

    @Override
    public void onGuiClosed() {
        module.setEnabled(false);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

}