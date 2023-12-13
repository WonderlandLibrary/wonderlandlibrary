package koks.gui.clickgui.periodic;

import koks.Koks;
import koks.api.settings.Setting;
import koks.manager.cl.Role;
import koks.gui.clickgui.Element;
import koks.gui.clickgui.periodic.settings.DrawComboBox;
import koks.gui.clickgui.periodic.settings.DrawType;
import koks.manager.module.Module;
import koks.api.interfaces.Wrapper;
import koks.manager.module.impl.gui.ClickGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author kroko
 * @created on 12.11.2020 : 15:46
 */
public class ClickGUIPSE extends GuiScreen implements Wrapper {

    boolean bindModule, settingMenu;
    Module bindMod, curMod;
    Module.Category curCat;

    private final ArrayList<DrawModule> drawModules = new ArrayList<>();

    int size = 50, outline = 2, lineSize = 10, curScroll, catX = 0, settingScroll;
    ScaledResolution sr;

    public ClickGUIPSE() {
        sr = new ScaledResolution(Minecraft.getMinecraft());
        for (int i = 0; i < Koks.getKoks().moduleManager.getModules().size(); i++) {
            Module module = Koks.getKoks().moduleManager.getModules().get(i);
            int length = (int) Math.ceil((double) i / (double) lineSize);
            int indexY = length + 1;
            int indexX = i - (lineSize * length);
            int x;
            int y;

            int position = Math.min(10, Koks.getKoks().moduleManager.getModules().size() / 2);

            if (i <= position) {
                x = sr.getScaledWidth() / 2 + (size * (lineSize / 2)) + (size + 6) * indexX;
                y = sr.getScaledHeight() / 2 + (size + 6) * indexY + curScroll;
            } else {
                x = sr.getScaledWidth() / 2 + (size * (lineSize / 2)) + (size + 6) * indexX;
                y = sr.getScaledHeight() / 2 + (size + 6) * indexY + curScroll;
            }
            drawModules.add(new DrawModule(module, x, y, size, outline, module.getCategory().getCategoryColor()));
        }
    }

    public int x, y, settingsSize, up = 7;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);

        drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), new Color(16, 16, 16).getRGB());

        ClickGui clickGui = (ClickGui) Koks.getKoks().moduleManager.getModule(ClickGui.class);
        if (clickGui.fakeCredits.isToggled())
            drawString(fontRendererObj, "§aby " + clickGui.fakeAuthor.getTyped(), sr.getScaledWidth() - fontRendererObj.getStringWidth("by " + clickGui.fakeAuthor.getTyped()), sr.getScaledHeight() - fontRendererObj.FONT_HEIGHT, -1);

        for (int i = 0; i < drawModules.size(); i++) {
            Module module = drawModules.get(i).module;
            int length = (int) Math.ceil((double) (i - up) / (double) lineSize);
            int indexY = length + 1;
            int indexX = (i - up) - (lineSize * length);
            int x;
            int y;

            Color color = module.getCategory().getCategoryColor();

            if (curCat != null && !curCat.equals(module.getCategory()))
                color = color.darker().darker().darker();
            else if (curCat != null)
                color = color.brighter();


            if (i <= up) {
                length = (int) Math.ceil((double) (i + 1) / (double) lineSize);
                indexX = (i + 1 - (i == 0 ? 0 : 1) + (i > 2 ? (lineSize - up + (i != up ? 1 : 0)) : 0)) - (lineSize * length);
                indexY = length - 1 + (i + (up - 1)) / up - (i == up ? 1 : 0);
                x = sr.getScaledWidth() / 2 + (size * (lineSize / 2)) + (size + 6) * indexX;
            } else
                x = sr.getScaledWidth() / 2 + (size * (lineSize / 2)) + (size + 6) * indexX;

            y = sr.getScaledHeight() / 2 + (size + 6) * indexY + curScroll;

            drawModules.get(i).x = x;
            drawModules.get(i).y = y;
            drawModules.get(i).size = size;
            drawModules.get(i).outline = outline;
            drawModules.get(i).color = color;
        }

        for (Module.Category category : Module.Category.values()) {
            if (category != Module.Category.DEBUG || Koks.getKoks().CLManager.getUser().getRole() == Role.Developer) {
                int length = (int) Math.ceil((double) (lineSize / 2) / (double) lineSize);
                int indexX = ((lineSize / 2) - (lineSize * length));
                Color color = category.getCategoryColor();
                if (mouseX >= sr.getScaledWidth() / 2 + (size * (lineSize / 2)) / 2 + (size + 3) * indexX + catX && mouseX <= sr.getScaledWidth() / 2 + (size * (lineSize / 2)) / 2 + (size + 3) * indexX + catX + fontRendererObj.getStringWidth(category.name()) && mouseY >= sr.getScaledHeight() / 2 + size / 2 + curScroll && mouseY <= sr.getScaledHeight() / 2 + size / 2 + curScroll + fontRendererObj.FONT_HEIGHT) {
                    if (!settingMenu) {
                        color = color.brighter();
                        curCat = category;

                    }
                }
                if (curCat != null && !curCat.equals(category))
                    color = color.darker().darker().darker();

                fontRendererObj.drawString(category.name(), sr.getScaledWidth() / 2 + (size * (lineSize / 2)) / 2 + (size + 3) * indexX + catX, sr.getScaledHeight() / 2 + size / 2 + curScroll, color.getRGB());
                catX += fontRendererObj.getStringWidth(category.name()) + 5;
            }
        }

        catX = 0;


        for (DrawModule drawModule : drawModules) {
            drawModule.drawScreen(mouseX, mouseY);
        }

        x = sr.getScaledWidth() / 2;
        y = sr.getScaledHeight() / 2;
        settingsSize = sr.getScaledHeight() / 2;

        if (curMod != null && settingMenu) {
            renderUtil.drawOutlineRect(x - settingsSize / 2, y - settingsSize / 2, x + settingsSize / 2, y + settingsSize / 2, 2F, curMod.getCategory().getCategoryColor().getRGB(), new Color(16, 16, 16).getRGB());
            GL11.glPushMatrix();
            renderUtil.scissor(x - settingsSize / 2, y - settingsSize / 2, x + settingsSize / 2, y + settingsSize / 2);
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            fontRendererObj.drawString(curMod.getName(), x - fontRendererObj.getStringWidth(curMod.getName()) * 2 / 2, y - settingsSize / 2 + fontRendererObj.FONT_HEIGHT / 2 + settingScroll, 2F, curMod.isToggled() ? curMod.getCategory().getCategoryColor().getRGB() : curMod.getCategory().getCategoryColor().darker().getRGB());
            fontRendererObj.drawString(curMod.getKey() == 0 ? "" : Keyboard.getKeyName(curMod.getKey()), x - settingsSize / 2 + 3, y - settingsSize / 2 + fontRendererObj.FONT_HEIGHT / 2 + settingScroll, 1.6F, curMod.getCategory().getCategoryColor().getRGB());

            int setY = y - settingsSize / 2 + fontRendererObj.FONT_HEIGHT / 2 + settingScroll + 35;
            FontRenderer fr = fontRendererObj;

            for (DrawModule drawModule : drawModules) {
                if (drawModule.module.getName().equalsIgnoreCase(curMod.getName())) {
                    for (Element element : drawModule.elements) {

                        int settingWidth = 0;
                        String settingName = element.setting.getName();
                        if (element.setting.getType() == Setting.Type.CHECKBOX) {
                            String string = settingName;
                            int offset = 10;
                            if (settingWidth < fr.getStringWidth(string) - 4 + offset) {
                                settingWidth = fr.getStringWidth(string) - 4 + offset;
                            }
                        }
                        if (element.setting.getType() == Setting.Type.COMBOBOX) {
                            String string = settingName + (element.extended ? "-" : "+");
                            if (settingWidth < fr.getStringWidth(string) + 22) {
                                settingWidth = fr.getStringWidth(string) + 22;
                            }

                            for (String mode : element.setting.getModes()) {
                                int offset = 5;
                                if (settingWidth < fr.getStringWidth(mode) + offset) {
                                    settingWidth = fr.getStringWidth(mode) + offset;
                                }
                            }
                        }

                        if (element.setting.getType() == Setting.Type.TYPE) {
                            String typed = element.setting.getTyped();
                            int offset = fr.getStringWidth(element.setting.getName()) + 12;
                            if (settingWidth < fr.getStringWidth(typed) + offset) {
                                settingWidth = fr.getStringWidth(typed) + offset;
                            }
                        }

                        if (element.setting.getType() == Setting.Type.SLIDER) {
                            String string = settingName + "00.00";
                            int offset = 15;
                            if (settingWidth < fr.getStringWidth(string) + offset) {
                                settingWidth = fr.getStringWidth(string) + offset;
                            }
                        }

                        if (element.setting.getType() == Setting.Type.SLIDER)
                            setY += 6;
                        element.updatePosition(x - settingsSize / 2 + 10, setY, settingWidth, 1);
                        element.drawScreen(mouseX, mouseY, partialTicks);
                        switch (element.setting.getType()) {
                            case CHECKBOX:
                                setY += fontRendererObj.FONT_HEIGHT + 4;
                                break;
                            case COMBOBOX:
                                DrawComboBox comboBox = (DrawComboBox) element;
                                setY += fontRendererObj.FONT_HEIGHT + 10 + (element.extended ? (fontRendererObj.FONT_HEIGHT + 5) * element.setting.getModes().length : 0) + Math.abs(comboBox.animation.getAnimationY() - comboBox.y) / (comboBox.y * 0.5);
                                break;
                            case SLIDER:
                                setY += 10;
                                break;
                            case KEY:
                                setY += fontRendererObj.FONT_HEIGHT + 5;
                                break;
                            case TYPE:
                                setY += fontRendererObj.FONT_HEIGHT + 5;
                                break;
                        }
                    }
                }
            }

            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            GL11.glPopMatrix();
        }


        super.drawScreen(mouseX, mouseY, partialTicks);

    }

    @Override
    public void handleMouseInput() throws IOException {
        if (Mouse.isCreated() && !settingMenu) {
            int wheel = Mouse.getEventDWheel();
            int lastCurScroll = curScroll;
            curScroll += wheel / 8;
            int minScroll = sr.getScaledWidth() / 2;
            if (curScroll >= -minScroll)
                curScroll = -minScroll;

            int length = (int) Math.ceil((double) (drawModules.size() - up) / (double) lineSize);
            length +=1;
            int maxScroll = sr.getScaledHeight() / 2 + (size + 6) * length + curScroll;
            if(maxScroll <= 0)
                curScroll = lastCurScroll;
        } else if (settingMenu) {
            int wheel = Mouse.getEventDWheel();
            settingScroll += wheel / 8;
            if (settingScroll >= 0)
                settingScroll = 0;

            int settings = 0;

            for (Setting setting : Koks.getKoks().settingsManager.getSettings()) {
                if (setting.getModule().equals(curMod)) {
                    settings++;
                    if (setting.getType() == Setting.Type.COMBOBOX) {
                        for (DrawModule module : drawModules) {
                            for (Element element : module.elements) {
                                if (element.extended && element.setting.equals(setting)) {
                                    for (String mode : setting.getModes()) {
                                        settings++;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            int maxScroll = settings * (fontRendererObj.FONT_HEIGHT + 1);

            if (settingScroll <= -maxScroll)
                settingScroll = -maxScroll;

        }
        super.handleMouseInput();
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        for (DrawModule drawModule : drawModules) {
            drawModule.mouseReleased(mouseX, mouseY, state);
        }
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {

        for (DrawModule drawModule : drawModules) {
            drawModule.keyTyped(typedChar, keyCode);
        }

        boolean keyTyped = false;
        for (DrawModule drawModule : drawModules) {
            for (Element element : drawModule.elements) {
                if (element instanceof DrawType) {
                    DrawType drawType = (DrawType) element;
                    if (drawType.isKeyTyped)
                        keyTyped = true;
                }
            }
        }

        if (settingMenu && !keyTyped && !bindModule) {
            if (keyCode == Keyboard.KEY_ESCAPE || keyCode == Keyboard.KEY_BACK || keyCode == Keyboard.KEY_RETURN) {
                settingMenu = false;
                curMod = null;
            }
        }
        if (bindModule) {
            if (bindMod != null) {
                if (keyCode == Keyboard.KEY_ESCAPE || keyCode == Keyboard.KEY_BACK)
                    bindMod.setKey(Keyboard.KEY_NONE);
                else
                    bindMod.setKey(keyCode);
                bindModule = false;
                bindMod = null;
            }
        }
        if (!bindModule || !keyTyped)
            super.keyTyped(typedChar, keyCode);
    }

    public boolean isHoverMods(int mouseX, int mouseY) {
        for (DrawModule drawModule : drawModules) {
            if (drawModule.isHover(mouseX, mouseY))
                return true;
        }
        return false;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (DrawModule drawModule : drawModules) {
            drawModule.mouseClicked(mouseX, mouseY, mouseButton);
        }


        if (!isHoverMods(mouseX, mouseY) && !settingMenu)
            curCat = null;

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
