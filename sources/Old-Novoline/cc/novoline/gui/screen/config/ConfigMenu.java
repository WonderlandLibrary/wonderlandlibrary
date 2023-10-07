package cc.novoline.gui.screen.config;

import cc.novoline.Novoline;
import cc.novoline.commands.impl.ConfigCommand;
import cc.novoline.gui.screen.click.DiscordGUI;
import cc.novoline.gui.screen.click.Scroll;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.configurations.ConfigManager;
import cc.novoline.modules.visual.ClickGUI;
import cc.novoline.utils.RenderUtils;
import cc.novoline.utils.Timer;
import cc.novoline.utils.notifications.NotificationType;
import net.minecraft.util.ChatAllowedCharacters;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.List;

import static cc.novoline.utils.fonts.impl.Fonts.ICONFONT.ICONFONT_24.ICONFONT_24;
import static cc.novoline.utils.fonts.impl.Fonts.ICONFONT.ICONFONT_35.ICONFONT_35;
import static cc.novoline.utils.fonts.impl.Fonts.SFBOLD.SFBOLD_26.SFBOLD_26;
import static cc.novoline.utils.fonts.impl.Fonts.SFTHIN.SFTHIN_16.SFTHIN_16;
import static cc.novoline.utils.fonts.impl.Fonts.SFTHIN.SFTHIN_20.SFTHIN_20;
import static net.minecraft.client.gui.Gui.drawRect;

public final class ConfigMenu {

    static int block = 5;
    public static boolean isTextHovered;
    private static String message = "";
    private static final Timer backspace = new Timer();

    public static void drawScreen(int mouseX, int mouseY) {
        final Novoline novoline = Novoline.getInstance();
        final DiscordGUI discordGUI = novoline.getDiscordGUI();
        final Scroll scroll = DiscordGUI.scroll();

        final int xCoordinate = discordGUI.getXCoordinate();
        final int yCoordinate = discordGUI.getYCoordinate();

        if (scroll != null && !discordGUI.getConfigs().isEmpty()) {
            switch (scroll) {
                case DOWN:
                    if (discordGUI.getConfigs().get(discordGUI.getConfigs().size() - 1).getY() > yCoordinate + discordGUI
                            .getHeight() - 14) {
                        for (GuiConfig config : discordGUI.getConfigs()) {
                            config.setOffset(config.getOffset() - 7);
                        }
                    }

                    break;
                case UP:
                    if (discordGUI.getConfigs().get(0).getOffset() < 30) {
                        for (GuiConfig config : discordGUI.getConfigs()) {
                            config.setOffset(config.getOffset() + 7);
                        }
                    }

                    break;
            }
        }

        final int circleColor;

        if (discordGUI.isConfigsOpened() && block <= 10) {
            block++;
        } else if (!discordGUI.isConfigsOpened()) {
            block = 5;
        }

        final int guiColor = novoline.getModuleManager().getModule(ClickGUI.class).getGUIColor();

        if (isHovered(mouseX, mouseY)) {
            if (discordGUI.isConfigsOpened()) {
                drawRect(xCoordinate, yCoordinate + 274 - block, xCoordinate + 2, yCoordinate + 274 + block,
                        0xFFFFFFFF);
            } else {
                drawRect(xCoordinate, yCoordinate + 269, xCoordinate + 2, yCoordinate + 279, 0xFFFFFFFF);
            }
        } else if (discordGUI.isConfigsOpened()) {
            drawRect(xCoordinate, yCoordinate + 274 - block, xCoordinate + 2, yCoordinate + 274 + block, 0xFFFFFFFF);
        }

        RenderUtils.drawFilledCircle(xCoordinate + 22, yCoordinate + 274, 15,0xFF36393F);
        ICONFONT_35.drawString("E", xCoordinate + 16, yCoordinate + 268, isHovered(mouseX,mouseY) ? guiColor : 0xFFFFFFFF);

        if (discordGUI.isConfigsOpened()) {
            final boolean b = areEnabled(discordGUI);

            if (b && isLoad(mouseX, mouseY)) {
                drawRect(xCoordinate + 44, yCoordinate + 26, xCoordinate + 45 + 110, yCoordinate + 40, 0xFF36393E);
            } else if (b && isDelete(mouseX, mouseY)) {
                drawRect(xCoordinate + 44, yCoordinate + 44, xCoordinate + 45 + 110, yCoordinate + 58, 0xFF36393E);
            } else if (b && isSave(mouseX, mouseY)) {
                drawRect(xCoordinate + 44, yCoordinate + 62, xCoordinate + 45 + 110, yCoordinate + 76, 0xFF36393E);
            } else if (isRefresh(mouseX, mouseY)) {
                drawRect(xCoordinate + 44, yCoordinate + 80, xCoordinate + 45 + 110, yCoordinate + 94, 0xFF36393E);
            }

            ICONFONT_24.drawString("I", xCoordinate + 50, yCoordinate + 7, 0xFFFFFFFF);
            SFTHIN_20.drawString("Configs", xCoordinate + 63, yCoordinate + 7, 0xFFFFFFFF);

            final int color = b ? 0xFF868386 : new Color(0x4E4B4E).getRGB();

            SFBOLD_26.drawString("#", xCoordinate + 50, yCoordinate + 28, color);
            SFBOLD_26.drawString("#", xCoordinate + 50, yCoordinate + 46, color);
            SFBOLD_26.drawString("#", xCoordinate + 50, yCoordinate + 64, 0xFF868386);
            SFBOLD_26.drawString("#", xCoordinate + 50, yCoordinate + 82, 0xFF868386);
            SFTHIN_20.drawString("Load", xCoordinate + 63, yCoordinate + 30, color);
            SFTHIN_20.drawString("Delete", xCoordinate + 63, yCoordinate + 48, color);
            SFTHIN_20.drawString("Save", xCoordinate + 63, yCoordinate + 66, 0xFF868386);
            SFTHIN_20.drawString("Refresh", xCoordinate + 63, yCoordinate + 84, 0xFF868386);

            //region Text box
            if (isTextHovered && Keyboard.isKeyDown(Keyboard.KEY_BACK)) {
                if (backspace.delay(100) && message.length() >= 1) {
                    message = message.substring(0, message.length() - 1);
                    backspace.reset();
                }
            }

            RenderUtils.drawBorderedRect(xCoordinate + 50, yCoordinate + 100, xCoordinate + 120, yCoordinate + 110, 1,
                    isTextHovered(mouseX, mouseY) || isTextHovered ? 0xFF827DDC : 0x64000000, 0xFF2F2F2F);

            if (message.isEmpty()) {
                SFTHIN_16.drawString("Config name", xCoordinate + 52, yCoordinate + 102, 0x64FFFFFF);
            } else {
                if (SFTHIN_16.stringWidth(message) > 65) {
                    SFTHIN_16.drawString(SFTHIN_16.trimStringToWidth(message, 60, true), xCoordinate + 52,
                            yCoordinate + 102, 0xFFFFFFFF);
                } else {
                    SFTHIN_16.drawString(message, xCoordinate + 52, yCoordinate + 102, 0xFFFFFFFF);
                }
            }
            //endregion

            for (GuiConfig config : discordGUI.getConfigs()) {
                config.update();

                if (!config.isOutsideOfMenu()) {
                    config.drawScreen(mouseX, mouseY);
                }
            }
        }
    }

    private static boolean areEnabled(@NonNull DiscordGUI gui) {
        return gui.getConfigs().stream().anyMatch(GuiConfig::isSelected);
    }

    public static void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            final Novoline novoline = Novoline.getInstance();
            final ModuleManager moduleManager = novoline.getModuleManager();
            final ConfigManager configManager = moduleManager.getConfigManager();

            final DiscordGUI discordGUI = novoline.getDiscordGUI();
            final List<GuiConfig> configs = discordGUI.getConfigs();

            boolean b = false;

            for (GuiConfig config : configs) {
                if (config.isHovered(mouseX, mouseY)) {
                    for (GuiConfig config1 : configs) {
                        config1.setSelected(false);
                    }

                    config.setSelected(true);
                } else if (config.isSelected()) {
                    if (isLoad(mouseX, mouseY)) {
                        ConfigCommand.loadConfig(moduleManager.getConfigManager(), config.getName());
                        discordGUI.initConfigs();
                    } else if (isDelete(mouseX, mouseY)) {
                        ConfigCommand.deleteConfig(moduleManager.getConfigManager(), config.getName());
                        discordGUI.initConfigs();
                    } else if (isSave(mouseX, mouseY)) {
                        ConfigCommand.saveConfig(moduleManager.getConfigManager(), config.getName());
                        discordGUI.initConfigs();
                        b = true;
                    }
                }
            }

            a:
            if (!b && isSave(mouseX, mouseY)) {
                if (message.length() > 25) {
                    Novoline.getInstance().getNotificationManager().pop("Name is too long!", 2_000, NotificationType.ERROR);
                    break a;
                }

                ConfigCommand.saveConfig(moduleManager.getConfigManager(), message);
                discordGUI.initConfigs();
            } else if (isRefresh(mouseX, mouseY)) {
                discordGUI.initConfigs();
            }

            if (isTextHovered(mouseX, mouseY)) {
                isTextHovered = !isTextHovered;
            } else if (isTextHovered) {
                isTextHovered = false;
            }
        }
    }

    public static void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_F5) {
            Novoline.getInstance().getDiscordGUI().initConfigs();
            return;
        }

        if (isTextHovered) {
            if (keyCode == Keyboard.KEY_ESCAPE) {
                isTextHovered = false;
            } else if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
                message += Character.toString(typedChar);
            }
        }
    }

    public static boolean isLoad(int mouseX, int mouseY) {
        final DiscordGUI gui = Novoline.getInstance().getDiscordGUI();
        return mouseX >= gui.getXCoordinate() + 44 && mouseX <= gui.getXCoordinate() + 45 + 110 // @off
                && mouseY >= gui.getYCoordinate() + 26 && mouseY <= gui.getYCoordinate() + 40; // @on
    }

    public static boolean isDelete(int mouseX, int mouseY) {
        final DiscordGUI gui = Novoline.getInstance().getDiscordGUI();
        return mouseX >= gui.getXCoordinate() + 44 && mouseX <= gui.getXCoordinate() + 45 + 110 // @off
                && mouseY >= gui.getYCoordinate() + 44 && mouseY <= gui.getYCoordinate() + 58; // @on
    }

    public static boolean isSave(int mouseX, int mouseY) {
        final DiscordGUI gui = Novoline.getInstance().getDiscordGUI();
        return mouseX >= gui.getXCoordinate() + 44 && mouseX <= gui.getXCoordinate() + 45 + 110 // @off
                && mouseY >= gui.getYCoordinate() + 62 && mouseY <= gui.getYCoordinate() + 76; // @on
    }

    public static boolean isRefresh(int mouseX, int mouseY) {
        final DiscordGUI gui = Novoline.getInstance().getDiscordGUI();
        return mouseX >= gui.getXCoordinate() + 44 && mouseX <= gui.getXCoordinate() + 45 + 110 // @off
                && mouseY >= gui.getYCoordinate() + 80 && mouseY <= gui.getYCoordinate() + 94; // @on
    }

    public static boolean isHovered(int mouseX, int mouseY) {
        final DiscordGUI gui = Novoline.getInstance().getDiscordGUI();
        return mouseX >= gui.getXCoordinate() + 8 && mouseX <= gui.getXCoordinate() + 35 // @off
                && mouseY >= gui.getYCoordinate() + 259 && mouseY <= gui.getYCoordinate() + 289; // @on
    }

    public static boolean isTextHovered(int mouseX, int mouseY) {
        final DiscordGUI gui = Novoline.getInstance().getDiscordGUI();
        return mouseX >= gui.getXCoordinate() + 50 && mouseX <= gui.getXCoordinate() + 120 // @off
                && mouseY >= gui.getYCoordinate() + 100 && mouseY <= gui.getYCoordinate() + 110; // @on
    }

}
