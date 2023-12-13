package koks.api.clickgui.periodic;

import koks.Koks;
import koks.api.Methods;
import koks.api.clickgui.Element;
import koks.api.font.DirtyFontRenderer;
import koks.api.font.Fonts;
import koks.api.manager.cl.Role;
import koks.api.manager.value.Value;
import koks.api.manager.value.ValueManager;
import koks.api.registry.module.Module;
import koks.api.registry.module.ModuleRegistry;
import koks.api.utils.RenderUtil;
import koks.api.utils.Resolution;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * @author kroko
 * @created on 13.02.2021 : 02:34
 */
public class PeriodicClickGUI extends GuiScreen implements Methods {

    public boolean bindModule, settingMenu, hoverBypassed;
    public Module bindMod, currentModule;
    public Module.Category category;

    private final ArrayList<DrawModule> drawModules = new ArrayList<>();

    public int size = 50, outline = 2, lineSize = 10, curScroll, catX = 0, settingScroll;

    public PeriodicClickGUI() {
        final Resolution resolution = Resolution.getResolution();
        for (int i = 0; i < ModuleRegistry.getModules().size(); i++) {
            Module module = ModuleRegistry.getModules().get(i);
            final int length = (int) Math.ceil((double) i / (double) lineSize);
            final int indexY = length + 1;
            final int indexX = i - (lineSize * length);
            final int x = resolution.getWidth() / 2 + (size * (lineSize / 2)) + (size + 6) * indexX;
            final int y = resolution.getHeight() / 2 + (size + 6) * indexY + curScroll;

            drawModules.add(new DrawModule(module, x, y, size, outline, module.getCategory().getCategoryColor()));
            drawModules.sort(Comparator.comparing(drawModule -> drawModule.module.getCategory().name()));
        }
    }

    public int x, y, settingsSize, up = 7;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        final Resolution resolution = Resolution.getResolution();

        final DisplayMode displaymode = Display.getDisplayMode();

        lineSize = (int) MathHelper.clamp_double((double)resolution.getWidth() / (double)displaymode.getWidth() * 10, 8, Integer.MAX_VALUE);

        drawRect(0, 0, resolution.getWidth(), resolution.getHeight(), new Color(16, 16, 16).getRGB());

        for (int i = 0; i < drawModules.size(); i++) {
            final Module module = drawModules.get(i).module;
            int length = (int) Math.ceil((double) (i - up) / (double) lineSize);
            int indexY = length + 1;
            int indexX = (i - up) - (lineSize * length);

            Color color = module.getCategory().getCategoryColor();

            if (category != null && !category.equals(module.getCategory()) || hoverBypassed && !module.isBypass())
                color = color.darker().darker().darker();
            else if (category != null)
                color = color.brighter();


            if (i <= up) {
                length = (int) Math.ceil((double) (i + 1) / (double) lineSize);
                indexX = (i + 1 - (i == 0 ? 0 : 1) + (i > 2 ? (lineSize - up + (i != up ? 1 : 0)) : 0)) - (lineSize * length);
                indexY = length - 1 + (i + (up - 1)) / up - (i == up ? 1 : 0);
            }

            final int x = resolution.getWidth() / 2 + (size * (lineSize / 2)) + (size + 6) * indexX;
            final int y = resolution.getHeight() / 2 + (size + 6) * indexY + curScroll;

            drawModules.get(i).x = x;
            drawModules.get(i).y = y;
            drawModules.get(i).size = size;
            drawModules.get(i).outline = outline;
            drawModules.get(i).color = color;
        }

        final int length = (int) Math.ceil((double) (lineSize / 2) / (double) lineSize);
        final int indexX = ((lineSize / 2) - (lineSize * length));

        for (Module.Category category : Module.Category.values()) {
            if (category != Module.Category.DEBUG || Koks.getKoks().clManager.getUser().getRole() == Role.DEVELOPER) {
                Color color = category.getCategoryColor();
                if (mouseX >= resolution.getWidth() / 2 + (size * (lineSize / 2)) / 2 + (size + 3) * indexX + catX && mouseX <= resolution.getWidth() / 2F + (size * (lineSize / 2F)) / 2F + (size + 3) * indexX + catX + Fonts.arial18.getStringWidth(category.name()) && mouseY >= resolution.getHeight() / 2 + size / 2 + curScroll && mouseY <= resolution.getHeight() / 2F + size / 2F + curScroll + Fonts.arial18.getStringHeight(category.name().toUpperCase())) {
                    if (!settingMenu) {
                        color = color.brighter();
                        this.category = category;
                        hoverBypassed = false;
                    }
                }
                if (this.category != null && !this.category.equals(category) || hoverBypassed)
                    color = color.darker().darker().darker();

                Fonts.arial18.drawString(category.name(), resolution.getWidth() / 2F + (size * (lineSize / 2F)) / 2F + (size + 3) * indexX + catX, resolution.getHeight() / 2F + size / 2F + curScroll, color, true);
                catX += Fonts.arial18.getStringWidth(category.name()) + 5;
            }
        }

        final String bypassText = "Bypassed".toUpperCase();
        for(int i = 0; i < bypassText.length(); i++) {
            final char character = bypassText.charAt(i);
            Fonts.arial18.drawString(character + "", resolution.getWidth() / 2F + (size * (lineSize / 2F)) / 2F + (size + 3) * indexX + catX / 2F - Fonts.arial18.getStringWidth(bypassText) / 2 + Fonts.arial18.getStringWidth(bypassText.substring(0, i)), (int) (resolution.getHeight() / 2 + size / 2 + curScroll - Fonts.arial18.getStringHeight(character + "") * 1.5F), getRainbow(100 * i, 3000, 0.8F, this.category != null ? 0.3F : 1F), true);
        }

        if(mouseX >= resolution.getWidth() / 2 + (size * (lineSize / 2)) / 2 + (size + 3) * indexX + catX / 2 - fr.getStringWidth(bypassText) / 2 && mouseX <= resolution.getWidth() / 2F + (size * (lineSize / 2F)) / 2F + (size + 3) * indexX + catX / 2F - fr.getStringWidth(bypassText) / 2F + Fonts.arial18.getStringWidth(bypassText) && mouseY >=(int) (resolution.getHeight() / 2 + size / 2 + curScroll - Fonts.arial18.getStringHeight(bypassText) * 1.5F) && mouseY <= (int) (resolution.getHeight() / 2 + size / 2 + curScroll - Fonts.arial18.getStringHeight(bypassText) * 1.5F + Fonts.arial18.getStringHeight(bypassText))) {
            this.category = null;
            hoverBypassed = true;
        }

        catX = 0;


        for (DrawModule drawModule : drawModules) {
            drawModule.drawScreen(mouseX, mouseY);
        }

        x = resolution.getWidth() / 2;
        y = resolution.getHeight() / 2;
        settingsSize = resolution.getHeight() / 2;

        final RenderUtil renderUtil = RenderUtil.getInstance();
        if (this.currentModule != null && this.settingMenu) {
            renderUtil.drawOutlineRect(x - settingsSize / 2F, y - settingsSize / 2F, x + settingsSize / 2F, y + settingsSize / 2F, 2F, currentModule.getCategory().getCategoryColor().getRGB(), new Color(16, 16, 16).getRGB());
            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            renderUtil.scissor(x - settingsSize / 2, y - settingsSize / 2, x + settingsSize / 2, y + settingsSize / 2);
            Fonts.ralewayRegular35.drawString(currentModule.getName(), (int) (x - Fonts.ralewayRegular35.getStringWidth(currentModule.getName()) / 2), (int) (y - settingsSize / 2F + Fonts.ralewayRegular35.getStringHeight(currentModule.getName()) / 2F + settingScroll), currentModule.isToggled() ? currentModule.getCategory().getCategoryColor() : currentModule.getCategory().getCategoryColor().darker(), true);
            Fonts.arial25.drawString(currentModule.getKey() == 0 ? "" : Keyboard.getKeyName(currentModule.getKey()), (int) (x - settingsSize / 2F + 3), (int) (y - settingsSize / 2F + Fonts.arial25.getStringHeight(currentModule.getKey() == 0 ? "" : Keyboard.getKeyName(currentModule.getKey())) / 2 + settingScroll), currentModule.getCategory().getCategoryColor(), true);

            int setY = (int) (y - settingsSize / 2 + Fonts.arial18.getStringHeight(currentModule.getName()) / 2 + settingScroll + 35);

            for (final DrawModule drawModule : drawModules) {
                if (drawModule.module.getName().equalsIgnoreCase(currentModule.getName())) {
                    for (Element element : drawModule.elements) {
                        if(element.value.isVisible()) {
                            element.value.makeEnabled();
                            int settingWidth = 0;
                            final String settingName = element.value.getName();
                            if (element.value.getValue() instanceof Boolean) {
                                int offset = 10;
                                if (settingWidth < fr.getStringWidth(settingName) - 4 + offset) {
                                    settingWidth = fr.getStringWidth(settingName) - 4 + offset;
                                }
                            }
                            if (element.value.getValue() instanceof String) {
                                String longestSetting = element.value.castString();
                                for(String mode : element.value.getModes()) {
                                    if(longestSetting.length() < mode.length())
                                        longestSetting = mode;
                                }
                                final String string = settingName + ": " + longestSetting;
                                if (settingWidth < fr.getStringWidth(string) + 26) {
                                    settingWidth = fr.getStringWidth(string) + 26;
                                }
                            }

                            if (element.value.getValue() instanceof Double || element.value.getValue() instanceof Integer) {
                                final String string = settingName + "00.00";
                                int offset = 15;
                                if (settingWidth < fr.getStringWidth(string) + offset) {
                                    settingWidth = fr.getStringWidth(string) + offset;
                                }
                            }

                            element.updatePosition(x - settingsSize / 2 + 10, setY, settingWidth, element.height);
                            element.drawScreen(mouseX, mouseY, partialTicks);
                            setY += element.height;
                        } else {
                            element.value.makeDisabled();
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
        final Resolution resolution = Resolution.getResolution();
        if (Mouse.isCreated() && !settingMenu) {
            final int wheel = Mouse.getEventDWheel();

            final int length = (int) Math.ceil((double) drawModules.size() / (double) lineSize);
            final int indexY = length + 1;
            final int y = resolution.getHeight() / 2 + (size + 6) * indexY - resolution.getHeight();

            curScroll += wheel / 8;

            if(curScroll < y * -1)
                curScroll = y * -1;

            if (curScroll > (resolution.getHeight() / 2 + size / 2 - (size * 2)) * -1)
                curScroll = (resolution.getHeight() / 2 + size / 2 - (size * 2)) * -1;

        } else if (settingMenu) {
            int wheel = Mouse.getEventDWheel();
            settingScroll += wheel / 8;
            if (settingScroll >= 0)
                settingScroll = 0;

            int settings = 0;

            for (Value<?> setting : ValueManager.getInstance().getValues()) {
                if (setting.getObject().equals(currentModule)) {
                    settings++;
                    if (setting.getType().equalsIgnoreCase("COMBOBOX")) {
                        for (DrawModule module : drawModules) {
                            for (Element element : module.elements) {
                                if (element.extended && element.value.isVisible() && element.value.equals(setting)) {
                                    for (String ignored : setting.getModes()) {
                                        settings++;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            int maxScroll = settings * 8 + 1;

            if (settingScroll <= -maxScroll)
                settingScroll = -maxScroll;

        }
        super.handleMouseInput();
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
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


        if (settingMenu && !bindModule) {
            if (keyCode == Keyboard.KEY_ESCAPE || keyCode == Keyboard.KEY_BACK || keyCode == Keyboard.KEY_RETURN) {
                settingMenu = false;
                currentModule = null;
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
        if (!bindModule)
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


        if (!isHoverMods(mouseX, mouseY) && !settingMenu) {
            category = null;
            hoverBypassed = false;
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public boolean isClickGUI() {
        return true;
    }

}
