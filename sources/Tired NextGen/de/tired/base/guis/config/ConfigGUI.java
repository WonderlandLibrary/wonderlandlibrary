package de.tired.base.guis.config;

import de.tired.util.misc.RenderableTextfield;
import de.tired.util.render.RenderUtil;
import de.tired.base.guis.config.setting.SettingRenderer;
import de.tired.util.render.ScrollHandler;
import de.tired.util.render.StencilUtil;
import de.tired.base.font.CustomFont;
import de.tired.base.font.FontManager;
import de.tired.base.config.Config;
import de.tired.util.render.shaderloader.ShaderManager;
import de.tired.util.render.shaderloader.list.RoundedRectOutlineShader;
import de.tired.util.render.shaderloader.list.RoundedRectShader;
import de.tired.Tired;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ConfigGUI extends GuiScreen {

    private int xPos, yPos, uiWidth, uiHeight;

    private CurrentSection selectedSection = CurrentSection.DASHBOARD;

    private final ArrayList<Config> configsToDelete = new ArrayList<>();

    private final SettingRenderer settingRenderer = new SettingRenderer();

    private boolean clickedNewConfig = false;

    private final RenderableTextfield textfield = new RenderableTextfield("Config Name", 0, 0, 0, 0);

    private final ScrollHandler scrollHandler = new ScrollHandler();

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.uiWidth = 500;
        this.uiHeight = 250;
        this.xPos = width / 2 - uiWidth / 2;
        this.yPos = height / 2 - uiHeight / 2;

        ShaderManager.shaderBy(RoundedRectShader.class).drawRound(xPos, yPos, uiWidth, uiHeight, 3, new Color(255, 255, 255));

        RenderUtil.instance.drawRect2(xPos - 1, yPos - 1, 113, uiHeight + 2, new Color(42, 46, 49).getRGB());

        FontManager.raleWay40.drawString("TIRED", calculateMiddle("TIRED", FontManager.raleWay40, xPos - 1, 113), yPos + 10, -1);

        ShaderManager.shaderBy(RoundedRectShader.class).drawRound(xPos + 4, yPos + 30, 101, 1, 1.2f, new Color(54, 56, 60));

        final AtomicInteger y = new AtomicInteger(yPos + 40);

        for (CurrentSection section : CurrentSection.values()) {
            ShaderManager.shaderBy(RoundedRectShader.class).drawRound(xPos + 4, y.get(), 100, 19, 2, new Color(54, 56, 60));

            switch (section.section) {
                case "Dashboard": {
                    FontManager.iconFont.drawString("¡", xPos + 11f, y.get() + 8.5f, -1);
                    break;
                }
                case "Settings": {
                    FontManager.iconFont.drawString("H", xPos + 11f, y.get() + 8.5f, -1);
                    break;
                }
            }

            FontManager.interMedium14.drawString(section.section, xPos + 33, y.get() + 9, -1);

            y.addAndGet(30);
        }

        if (selectedSection == CurrentSection.DASHBOARD) {
            FontManager.raleWay30.drawString("Dashboard", xPos + 120, yPos + 21, new Color(45, 46, 52).getRGB());

            ShaderManager.shaderBy(RoundedRectShader.class).drawRound(xPos + 120, yPos + 40, uiWidth - 130, 1, 1.2f, new Color(249, 249, 249));

            ShaderManager.shaderBy(RoundedRectShader.class).drawRound(xPos + 120, yPos + 50, uiWidth - 130, uiHeight - 60, 3, new Color(248, 248, 248));

            scrollHandler.handleScroll();

            if (!clickedNewConfig) {
                RenderUtil.instance.doRenderShadow(new Color(38, 111, 250), xPos + uiWidth - 60, yPos + 19, 50, 13, 4);

                ShaderManager.shaderBy(RoundedRectShader.class).drawRound(xPos + uiWidth - 60, yPos + 18, 50, 13, 1, new Color(38, 111, 250));

                FontManager.interMedium10.drawString("New Config", calculateMiddle("New Config", FontManager.interMedium10, xPos + uiWidth - 60, 50), yPos + 26, -1);
            } else {
                RenderUtil.instance.doRenderShadow(new Color(231, 53, 53), xPos + uiWidth - 60, yPos + 19, 50, 13, 4);

                ShaderManager.shaderBy(RoundedRectShader.class).drawRound(xPos + uiWidth - 60, yPos + 18, 50, 13, 1, new Color(231, 53, 53));

                FontManager.interMedium10.drawString("Close", calculateMiddle("Close", FontManager.interMedium10, xPos + uiWidth - 60, 50), yPos + 26, -1);

                final int startX = xPos + 135;

                textfield.height = 16;
                textfield.width = 140;
                textfield.x = (startX + textfield.width) / 2 + startX / 2 + 30;
                textfield.y = yPos + 120;
                RenderUtil.instance.doRenderShadow(new Color(20, 20, 20, 120), textfield.x - 20, textfield.y - 20, textfield.width + 40, 60, 60);
                ShaderManager.shaderBy(RoundedRectShader.class).drawRound(textfield.x - 20, textfield.y - 20, textfield.width + 40, 60, 3, Color.WHITE);

                FontManager.interMedium14.drawString("Select Config Name", calculateMiddle("Select Config Name", FontManager.interMedium14, textfield.x - 20, textfield.width + 40), textfield.y - 10, Integer.MIN_VALUE);

                textfield.drawTextBox(mouseX, mouseY);
            }

            if (clickedNewConfig) {
                if (textfield.keyInputUtil.typedString.length() > 1) {
                    final int buttonWidth = 60;
                    ShaderManager.shaderBy(RoundedRectShader.class).drawRound(textfield.x, textfield.y + 23, buttonWidth, 12, 1, new Color(38, 111, 250));
                    FontManager.interMedium14.drawString("Add Config", calculateMiddle("Add Config", FontManager.interMedium14, textfield.x, buttonWidth), textfield.y + 29, -1);
                }
            }

            if (!clickedNewConfig) {
                StencilUtil.initStencilToWrite();
                ShaderManager.shaderBy(RoundedRectShader.class).drawRound(xPos + 120, yPos + 50, uiWidth - 130, uiHeight - 60, 3, new Color(248, 248, 248));
                StencilUtil.readStencilBuffer(1);
                final AtomicInteger yConfig = new AtomicInteger((int) (yPos + 55 + scrollHandler.getScroll()));
                for (Config config : Tired.INSTANCE.configManager.configs()) {
                    ShaderManager.shaderBy(RoundedRectOutlineShader.class).drawRound(xPos + 124, yConfig.get() - 1, uiWidth - 138, 26, 2, 1f, new Color(223, 223, 223));
                    ShaderManager.shaderBy(RoundedRectShader.class).drawRound(xPos + 125, yConfig.get(), uiWidth - 140, 24, 1, Color.WHITE);

                    RenderUtil.instance.doRenderShadow(new Color(38, 111, 250), xPos + 131.5f, yConfig.get() + 10, 10, 10, 12);
                    RenderUtil.instance.drawCircle(xPos + 135, yConfig.get() + 12, 9, new Color(243, 243, 243).getRGB());
                    RenderUtil.instance.drawCircle(xPos + 135, yConfig.get() + 12, 8, Color.WHITE.getRGB());


                    if (!config.name().substring(0, 1).equalsIgnoreCase("J") && !config.name().substring(0, 1).equalsIgnoreCase("i"))
                        FontManager.raleWay15.drawString(config.name().substring(0, 1).toUpperCase(), xPos + 132.5f, yConfig.get() + 12, new Color(38, 111, 250).getRGB());
                    else
                        FontManager.raleWay15.drawString(config.name().substring(0, 1).toUpperCase(), xPos + 133f, yConfig.get() + 12, new Color(38, 111, 250).getRGB());
                    String name = config.name();
                    name = name.substring(0, 1).toUpperCase() + name.substring(1);
                    FontManager.raleWay15.drawString(name, xPos + 148, yConfig.get() + 12, new Color(45, 46, 52).getRGB());


                    ShaderManager.shaderBy(RoundedRectOutlineShader.class).drawRound(xPos + uiWidth - 72, yConfig.get() + 6, 47, 12, 2, 1, new Color(243, 243, 243));

                    ShaderManager.shaderBy(RoundedRectShader.class).drawRound(xPos + uiWidth - 71, yConfig.get() + 7, 45, 10, 1, new Color(255, 255, 255));

                    FontManager.raleWay10.drawString("Delete", calculateMiddle("Delete", FontManager.raleWay10, xPos + uiWidth - 71, 45), yConfig.get() + 13.5f, Integer.MIN_VALUE);

                    ShaderManager.shaderBy(RoundedRectShader.class).drawRound(xPos + uiWidth - 121, yConfig.get() + 7, 45, 10, 1, new Color(38, 111, 250));

                    FontManager.raleWay10.drawString("Load", calculateMiddle("Load", FontManager.raleWay10, xPos + uiWidth - 121, 45), yConfig.get() + 13.5f, -1);


                    yConfig.addAndGet(31);
                }
                StencilUtil.uninitStencilBuffer();

                if (Tired.INSTANCE.configManager.configs().isEmpty()) {
                    FontManager.raleWay20.drawString("No Configs.", calculateMiddle("No Configs.", FontManager.raleWay20, xPos + 120, uiWidth - 130), yPos + 135, new Color(45, 46, 52).getRGB());
                }

                float hiddenHeight = (yConfig.get() - scrollHandler.getScroll()) - uiHeight - 123;
                scrollHandler.setMaxScroll(Math.max(0, hiddenHeight));
            }
        }

        for (Config config : configsToDelete)
            Tired.INSTANCE.configManager.delete(config.name());

        Tired.INSTANCE.configManager.configs().removeAll(configsToDelete);

        if (selectedSection == CurrentSection.SETTINGS) {
            settingRenderer.render(xPos + 120, yPos + 50, mouseX, mouseY, uiWidth - 130, uiHeight - 60);
            ShaderManager.shaderBy(RoundedRectShader.class).drawRound(xPos + 120, yPos + 40, uiWidth - 130, 1, 1.2f, new Color(249, 249, 249));
            FontManager.raleWay30.drawString("Settings", xPos + 120, yPos + 21, new Color(45, 46, 52).getRGB());
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        final AtomicInteger y = new AtomicInteger((int) (yPos + 40 + scrollHandler.getScroll()));

        if (!clickedNewConfig) {
            if (isHovered(mouseX, mouseY, xPos + uiWidth - 60, yPos + 18, 50, 13)) {
                if (mouseButton == 0) {
                    this.clickedNewConfig = true;
                }
            }

            if (selectedSection == CurrentSection.DASHBOARD) {

                FontManager.raleWay30.drawString("Dashboard", xPos + 120, yPos + 21, new Color(45, 46, 52).getRGB());

                ShaderManager.shaderBy(RoundedRectShader.class).drawRound(xPos + 120, yPos + 40, uiWidth - 130, 1, 1.2f, new Color(249, 249, 249));

                RenderUtil.instance.doRenderShadow(new Color(38, 111, 250), xPos + uiWidth - 60, yPos + 19, 50, 13, 4);

                ShaderManager.shaderBy(RoundedRectShader.class).drawRound(xPos + uiWidth - 60, yPos + 18, 50, 13, 1, new Color(38, 111, 250));

                FontManager.interMedium10.drawString("New Config", calculateMiddle("New Config", FontManager.interMedium10, xPos + uiWidth - 60, 50), yPos + 26, -1);

                ShaderManager.shaderBy(RoundedRectShader.class).drawRound(xPos + 120, yPos + 50, uiWidth - 130, uiHeight - 60, 3, new Color(248, 248, 248));


                final AtomicInteger yConfig = new AtomicInteger(yPos + 55);
                for (Config config : Tired.INSTANCE.configManager.configs()) {

                    if (mouseButton == 0) {
                        if (isHovered(mouseX, mouseY, xPos + uiWidth - 71, yConfig.get() + 7, 45, 10))
                            configsToDelete.add(config);

                        if (isHovered(mouseX, mouseY, xPos + uiWidth - 121, yConfig.get() + 7, 45, 10)) {
                            if (!config.name().contains("online")) {
                                Tired.INSTANCE.configManager.load(config);
                            } else {
                                Tired.INSTANCE.configManager.loadConfigOnline(config.name());
                            }
                        }
                    }

                    yConfig.addAndGet(31);
                }
            }
        } else {
            textfield.mouseClicked();
            if (clickedNewConfig) {
                if (textfield.keyInputUtil.typedString.length() > 1) {
                    final int buttonWidth = 60;
                    if (isHovered(mouseX, mouseY, textfield.x, textfield.y + 23, buttonWidth, 12)) {
                        if (mouseButton == 0) {
                            Tired.INSTANCE.configManager.create(new Config(textfield.keyInputUtil.typedString));
                            textfield.keyInputUtil.typedString = "";
                        }
                    }
                }
            }
            if (isHovered(mouseX, mouseY, xPos + uiWidth - 60, yPos + 18, 50, 13)) {
                if (mouseButton == 0) {
                    this.clickedNewConfig = false;
                }
            }
        }
        for (CurrentSection section : CurrentSection.values()) {
            if (isHovered(mouseX, mouseY, xPos + 4, y.get(), 100, 19)) {
                if (mouseButton == 0)
                    this.selectedSection = section;
            }
            y.addAndGet(30);

        }
        super.mouseClicked(mouseX, mouseY, mouseButton);

    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (clickedNewConfig)
            textfield.keyTyped(typedChar, keyCode);

        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
    }

    public boolean isHovered(double mouseX, double mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public int calculateMiddle(String text, CustomFont fontRenderer, double x, double width) {
        return (int) ((float) (x + width) - (fontRenderer.getStringWidth(text) / 2f) - (float) width / 2);
    }
}
