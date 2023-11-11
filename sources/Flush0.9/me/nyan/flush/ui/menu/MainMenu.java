package me.nyan.flush.ui.menu;

import me.nyan.flush.Flush;
import me.nyan.flush.altmanager.GuiAltManager;
import me.nyan.flush.ui.elements.Button;
import me.nyan.flush.ui.elements.User;
import me.nyan.flush.ui.fontrenderer.GlyphPageFontRenderer;
import me.nyan.flush.utils.other.MouseUtils;
import me.nyan.flush.utils.render.ColorUtils;
import me.nyan.flush.utils.render.RenderUtils;
import net.minecraft.client.gui.*;

import java.awt.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class MainMenu extends GuiScreen {
    private final ArrayList<me.nyan.flush.ui.elements.Button> buttons = new ArrayList<>();

    private static float alpha;
    private static int skipFrame = 3;

    public MainMenu(User user) {
        String hwid = null;
        try {
            byte[] hash = MessageDigest.getInstance("md5")
                    .digest((System.getenv("COMPUTERNAME")
                            + System.getProperty("user.name")
                            + System.getenv("PROCESSOR_IDENTIFIER")
                            + System.getenv("PROCESSOR_LEVEL"))
                            .getBytes());
            StringBuilder builder = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) {
                    builder.append('0');
                }
                builder.append(hex);
            }
            hwid = builder.toString();
        } catch (NoSuchAlgorithmException ignored) {
        }
        if (user == null || user.getUsername() == null || user.getUsername().isEmpty() || !user.getHwid().equals(hwid)) {
            Runtime.getRuntime().halt(-1);
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        buttons.clear();

        String[] buttonNames = {"Singleplayer", "Multiplayer", "Alt Manager", "Settings"};

        GlyphPageFontRenderer font = Flush.getFont("GoogleSansDisplay", 60);
        float y = height / 2F + 2 - (buttonNames.length * 24 + font.getFontHeight() + 8) / 2F + font.getFontHeight() + 8;
        for (String name : buttonNames) {
            buttons.add(new me.nyan.flush.ui.elements.Button(name, width / 2F - 50, y, 100, 20));
            y += 24;
        }

        Flush.getInstance().getDiscordRP().update("Idle...", "Main Menu");
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (skipFrame > 0) {
            drawRect(0, 0, width, height, 0xFF000000);
            skipFrame--;
            return;
        }

        if (alpha < 1) {
            alpha += 0.001 * Flush.getFrameTime();
            renderShader = true;

            if (alpha > 1) {
                alpha = 1;
            }
        }

        drawFlush(mouseX, mouseY);

        if (mc.isFullScreen()) {
            RenderUtils.drawCross(width - 12, 4, 8, 8,
                    MouseUtils.hovered(mouseX, mouseY, width - 12, 4, width - 4, 12) ?
                            Color.LIGHT_GRAY.getRGB() : -1);
        }

        drawButtons(mouseX, mouseY);

        /*
        Flush.getFont("GoogleSansDisplay", 18)
                .drawXCenteredString(
                        String.valueOf(Flush.NAME.charAt(0)) + EnumChatFormatting.WHITE + Flush.NAME.substring(1),
                        width / 2f, height / 2F - (buttons.size() / 2F * 24) + 5, 0xFF0062FF);
         */

        if (alpha < 1) {
            drawRect(0, 0, width, height, ColorUtils.alpha(0xFF000000, (int) (255 - alpha * 255)));
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawButtons(int mouseX, int mouseY) {
        GlyphPageFontRenderer font = Flush.getFont("GoogleSansDisplay", 60);

        float bgWidth = 108,
                bgHeight = buttons.size() * 24 + 4 + (font.getFontHeight() + 8);
        float x = width / 2F - bgWidth / 2F,
                y = height / 2F - bgHeight / 2F;
        RenderUtils.fillRoundRect(x, y + (font.getFontHeight() + 8), bgWidth,
                bgHeight - (font.getFontHeight() + 8), 6, 0xFF000000);

        float stringWidth = font.getStringWidth(Flush.NAME);
        char firstChar = Flush.NAME.charAt(0);
        font.drawChar(firstChar, width / 2F - stringWidth / 2F, y + 4, 0xFF0000FF, true);
        font.drawString(Flush.NAME.substring(1),
                width / 2F - stringWidth / 2F + font.getCharWidth(firstChar),
                y + 4, 0xFFFFFFFF, true);

        for (Button button : buttons) {
            button.drawButton(mouseX, mouseY);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mc.isFullScreen()) {
            if (MouseUtils.hovered(mouseX, mouseY, width - 12, 4, width - 4, 12) && mouseButton == 0) {
                mc.shutdown();
            }
        }

        buttons.forEach(button -> {
            if (button.mouseClicked(mouseX, mouseY, mouseButton)) {
                Flush.playClickSound();

                switch (button.getName().toLowerCase()) {
                    case "singleplayer":
                        mc.displayGuiScreen(new GuiSelectWorld(this));
                        break;
                    case "multiplayer":
                        mc.displayGuiScreen(new GuiMultiplayer(this));
                        break;
                    case "alt manager":
                        mc.displayGuiScreen(new GuiAltManager(this));
                        break;
                    case "settings":
                        mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
                        break;
                }
            }
        });
    }
}