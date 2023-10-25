package org.dreamcore.client.ui;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.dreamcore.client.dreamcore;
import org.dreamcore.client.helpers.input.MouseHelper;
import org.dreamcore.client.helpers.misc.ChatHelper;
import org.dreamcore.client.helpers.palette.PaletteHelper;
import org.dreamcore.client.helpers.render.RenderHelper;
import org.dreamcore.client.helpers.render.ScreenHelper;
import org.dreamcore.client.helpers.render.rect.RectHelper;
import org.dreamcore.client.settings.config.Config;
import org.dreamcore.client.settings.config.ConfigManager;
import org.dreamcore.client.ui.button.ConfigGuiButton;
import org.dreamcore.client.ui.button.ImageButton;
import org.dreamcore.client.ui.font.MCFontRenderer;
import org.dreamcore.client.ui.notification.NotificationManager;
import org.dreamcore.client.ui.notification.NotificationType;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class GuiConfig extends GuiScreen {

    public static GuiTextField search;
    public static Config selectedConfig = null;
    public ScreenHelper screenHelper;
    protected ArrayList<ImageButton> imageButtons = new ArrayList<>();
    private int width, height;
    private float scrollOffset;

    public GuiConfig() {
        this.screenHelper = new ScreenHelper(0, 0);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 1) {
            dreamcore.instance.configManager.saveConfig(search.getText());
            ChatHelper.addChatMessage(ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "saved config: " + ChatFormatting.RED + "\"" + search.getText() + "\"");
            NotificationManager.publicity("Config", ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "created config: " + ChatFormatting.RED + "\"" + search.getText() + "\"", 4, NotificationType.SUCCESS);
            ConfigManager.getLoadedConfigs().clear();
            dreamcore.instance.configManager.load();
            search.setFocused(false);
            search.setText("");
        }
        if (selectedConfig != null) {
            if (button.id == 2) {
                if (dreamcore.instance.configManager.loadConfig(selectedConfig.getName())) {
                    ChatHelper.addChatMessage(ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "loaded config: " + ChatFormatting.RED + "\"" + selectedConfig.getName() + "\"");
                    NotificationManager.publicity("Config", ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "loaded config: " + ChatFormatting.RED + "\"" + selectedConfig.getName() + "\"", 4, NotificationType.SUCCESS);
                } else {
                    ChatHelper.addChatMessage(ChatFormatting.RED + "Failed " + ChatFormatting.WHITE + "load config: " + ChatFormatting.RED + "\"" + selectedConfig.getName() + "\"");
                    NotificationManager.publicity("Config", ChatFormatting.RED + "Failed " + ChatFormatting.WHITE + "load config: " + ChatFormatting.RED + "\"" + selectedConfig.getName() + "\"", 4, NotificationType.ERROR);
                }
            } else if (button.id == 3) {
                if (dreamcore.instance.configManager.saveConfig(selectedConfig.getName())) {
                    ChatHelper.addChatMessage(ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "saved config: " + ChatFormatting.RED + "\"" + selectedConfig.getName() + "\"");
                    NotificationManager.publicity("Config", ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "saved config: " + ChatFormatting.RED + "\"" + selectedConfig.getName() + "\"", 4, NotificationType.SUCCESS);
                    ConfigManager.getLoadedConfigs().clear();
                    dreamcore.instance.configManager.load();
                } else {
                    ChatHelper.addChatMessage(ChatFormatting.RED + "Failed " + ChatFormatting.WHITE + "to save config: " + ChatFormatting.RED + "\"" + search.getText() + "\"");
                    NotificationManager.publicity("Config", ChatFormatting.RED + "Failed " + ChatFormatting.WHITE + "to save config: " + ChatFormatting.RED + "\"" + search.getText() + "\"", 4, NotificationType.ERROR);
                }
            } else if (button.id == 4) {
                if (dreamcore.instance.configManager.deleteConfig(selectedConfig.getName())) {
                    ChatHelper.addChatMessage(ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "deleted config: " + ChatFormatting.RED + "\"" + selectedConfig.getName() + "\"");
                    NotificationManager.publicity("Config", ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "deleted config: " + ChatFormatting.RED + "\"" + selectedConfig.getName() + "\"", 4, NotificationType.SUCCESS);
                } else {
                    ChatHelper.addChatMessage(ChatFormatting.RED + "Failed " + ChatFormatting.WHITE + "to delete config: " + ChatFormatting.RED + "\"" + selectedConfig.getName() + "\"");
                    NotificationManager.publicity("Config", ChatFormatting.RED + "Failed " + ChatFormatting.WHITE + "to delete config: " + ChatFormatting.RED + "\"" + selectedConfig.getName() + "\"", 4, NotificationType.ERROR);
                }
            }
        }
        super.actionPerformed(button);
    }

    private boolean isHoveredConfig(int x, int y, int width, int height, int mouseX, int mouseY) {
        return MouseHelper.isHovered(x, y, x + width, y + height, mouseX, mouseY);
    }

    @Override
    public void initGui() {
        this.screenHelper = new ScreenHelper(0, 0);
        ScaledResolution sr = new ScaledResolution(mc);
        width = sr.getScaledWidth() / 2;
        height = sr.getScaledHeight() / 2;
        search = new GuiTextField(228, mc.fontRendererObj, width - 100, height + 62, 85, 13);
        this.buttonList.add(new ConfigGuiButton(1, width - 105, height + 102, "Create"));
        this.buttonList.add(new ConfigGuiButton(2, width - 40, height - 48, "Load"));
        this.buttonList.add(new ConfigGuiButton(3, width - 40, height - 65, "Save"));
        this.buttonList.add(new ConfigGuiButton(4, width - 40, height - 82, "Delete"));
        this.imageButtons.clear();
        this.imageButtons.add(new ImageButton(new ResourceLocation("dreamcore/close-button.png"), width + 106, height - 135, 8, 8, "", 19));
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);
        this.drawWorldBackground(0);
        screenHelper.interpolate(sr.getScaledWidth(), sr.getScaledHeight(), 6);
        GL11.glPushMatrix();
        for (Config config : dreamcore.instance.configManager.getContents()) {
            if (config != null) {
                if (Mouse.hasWheel()) {
                    if (isHoveredConfig(width - 100, height - 122, 151, height + 59, mouseX, mouseY)) {
                        int wheel = Mouse.getDWheel();
                        if (wheel < 0) {
                            this.scrollOffset += 13;
                            if (this.scrollOffset < 0) {
                                this.scrollOffset = 0;
                            }
                        } else if (wheel > 0) {
                            this.scrollOffset -= 13;
                            if (this.scrollOffset < 0) {
                                this.scrollOffset = 0;
                            }
                        }
                    }
                }
            }
        }
        GlStateManager.pushMatrix();
        RectHelper.drawSkeetRectWithoutBorder(width - 70, height - 80, width + 80, height + 20);
        RectHelper.drawSkeetButton(width - 70, height - 80, width + 20, height + 90);
        org.dreamcore.client.helpers.render.RenderHelper.drawImage(new ResourceLocation("dreamcore/skeet.png"), width - 110, height - 140, 230, 1, Color.WHITE);
        mc.circleregular.drawStringWithOutline("Config System", width - 100, height - 135, -1);
        search.drawTextBox();
        if (search.getText().isEmpty() && !search.isFocused()) {
            MCFontRenderer.drawStringWithOutline(mc.fontRendererObj, "...", width - 97, height + 65, PaletteHelper.getColor(200));
        }
        for (ImageButton imageButton : this.imageButtons) {
            imageButton.draw(mouseX, mouseY, Color.WHITE);
            if (Mouse.isButtonDown(0)) {
                imageButton.onClick(mouseX, mouseY);
            }
        }
        int yDist = 0;
        int color;
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderHelper.scissorRect(0F, height - 119, width, height + 60);
        for (Config config : dreamcore.instance.configManager.getContents()) {
            if (config != null) {
                if (isHoveredConfig(width - 96, (int) (height - 117 + yDist - this.scrollOffset), mc.fontRendererObj.getStringWidth(config.getName()) + 5, 14, mouseX, mouseY)) {
                    color = -1;
                    if (Mouse.isButtonDown(0)) {
                        selectedConfig = new Config(config.getName());
                    }
                } else {
                    color = PaletteHelper.getColor(200);
                }
                if (selectedConfig != null && config.getName().equals(selectedConfig.getName())) {
                    RectHelper.drawBorder(width - 98, (height - 119 + yDist) - this.scrollOffset, width + mc.fontRendererObj.getStringWidth(config.getName()) - 90, (height - 107 + yDist) - this.scrollOffset, 0.65F, new Color(255, 255, 255, 75).getRGB(), new Color(0, 0, 0, 255).getRGB(), true);
                }
                mc.fontRendererObj.drawStringWithOutline(config.getName(), width - 95, (height - 117 + yDist) - this.scrollOffset, color);
                yDist += 15;
            }
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GlStateManager.popMatrix();
        GL11.glPopMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        search.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.scrollOffset < 0) {
            this.scrollOffset = 0;
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        for (Config config : dreamcore.instance.configManager.getContents()) {
            if (config != null) {
                if (keyCode == 200) {
                    this.scrollOffset += 13;
                } else if (keyCode == 208) {
                    this.scrollOffset -= 13;
                }
                if (this.scrollOffset < 0) {
                    this.scrollOffset = 0;
                }
            }
        }
        search.textboxKeyTyped(typedChar, keyCode);
        search.setText(search.getText().replace(" ", ""));
        if ((typedChar == '\t' || typedChar == '\r') && search.isFocused()) {
            search.setFocused(!search.isFocused());
        }
        try {
            super.keyTyped(typedChar, keyCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void onGuiClosed() {
        selectedConfig = null;
        super.onGuiClosed();
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
