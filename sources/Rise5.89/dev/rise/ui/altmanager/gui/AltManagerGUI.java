package dev.rise.ui.altmanager.gui;

import dev.rise.Rise;
import dev.rise.font.CustomFont;
import dev.rise.font.fontrenderer.TTFFontRenderer;
import dev.rise.ui.altmanager.AddAltThread;
import dev.rise.ui.altmanager.AltAccount;
import dev.rise.ui.altmanager.AltLoginThread;
import dev.rise.ui.altmanager.EnumAltStatus;
import dev.rise.ui.altmanager.gui.elements.RightClickMenu;
import dev.rise.ui.altmanager.gui.elements.RiseGuiButton;
import dev.rise.ui.altmanager.gui.elements.RiseGuiTextbox;
import dev.rise.ui.mainmenu.MainMenu;
import dev.rise.util.math.RandomUtil;
import dev.rise.util.render.ColorUtil;
import dev.rise.util.render.RenderUtil;
import dev.rise.util.render.UIUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public final class AltManagerGUI extends GuiScreen {
    private static final TTFFontRenderer fontRenderer = CustomFont.FONT_MANAGER.getFont("Dreamscape 96");
    private static final TTFFontRenderer fontRenderer2 = CustomFont.FONT_MANAGER.getFont("Dreamscape 60");
    public ArrayList<AltAccount> altList = new ArrayList<>();
    public RiseGuiTextbox username;
    public RiseGuiTextbox password;
    private ScaledResolution sr;
    private float x;
    private float y;
    private float screenWidth;
    private float screenHeight;
    private GuiTextField details;
    private AltLoginThread thread;
    private float offset;
    private RightClickMenu rightClickMenu;

    @Override
    public void initGui() {
        super.initGui();

        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        screenHeight = fontRenderer.getHeight(Rise.CLIENT_NAME);

        username = new RiseGuiTextbox(eventButton, mc.fontRendererObj, (sr.getScaledWidth() / 2) - 50, (sr.getScaledHeight() / 2) - 14, 100, 22, true);
        password = new RiseGuiTextbox(eventButton, mc.fontRendererObj, (sr.getScaledWidth() / 2) - 50, (sr.getScaledHeight() / 2) + 14, 100, 22, true);

        buttonList.add(new RiseGuiButton(69, (width / 2) - 51, (height / 2) + 42, 50, 22, "Add alt"));
        buttonList.add(new RiseGuiButton(71, (width / 2) + 1, (height / 2) + 42, 50, 22, "Go back"));
        buttonList.add(new RiseGuiButton(420, (width / 2) - 51, (height / 2) + 68, 102, 22, "Clipboard"));
        buttonList.add(new RiseGuiButton(1337, (width / 2) - 51, (height / 2) + 94, 102, 22, "Random username"));
        buttonList.add(new RiseGuiButton(1338, (width / 2) - 51, (height / 2) + 120, 102, 22, "Direct login"));

        username.setFocused(true);

        rightClickMenu = new RightClickMenu(0, 0);
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        offset += Mouse.getDWheel() / 10f;

        if (offset > 0) offset = 0;

        MainMenu.panoramaTimer++;

        RenderUtil.color(new Color(ColorUtil.getRainbow()));
        mc.getTextureManager().bindTexture(new ResourceLocation("rise/backgrounds/blue.png"));

        final float scale = 1.66f;
        final float amount = 1500;

        if (MainMenu.panoramaTimer % 100 == 0) {
            MainMenu.xOffSet = (float) (Math.random() - 0.5f) * amount;
            MainMenu.yOffSet = (float) (Math.random() - 0.5f) * amount;
        }

        MainMenu.smoothedX = (MainMenu.smoothedX * 250 + MainMenu.xOffSet) / 259;
        MainMenu.smoothedY = (MainMenu.smoothedY * 250 + MainMenu.yOffSet) / 259;

        drawModalRectWithCustomSizedTexture(0, 0, width / scale + MainMenu.smoothedX - 150, height / scale + MainMenu.smoothedY - 100, width, height, width * scale, height * scale);

        screenWidth = fontRenderer.getWidth(Rise.CLIENT_NAME);
        screenHeight = fontRenderer.getHeight(Rise.CLIENT_NAME);

        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        UIUtil.logoPosition = sr.getScaledHeight() / 2.0F - (screenHeight / 2.0F) - 6 - 28;

        x = (sr.getScaledWidth() / 2.0F) - (screenWidth / 2.0F);
        y = UIUtil.logoPosition;

        fontRenderer.drawString(Rise.CLIENT_NAME, x, y, new Color(255, 255, 255, 150).getRGB());

        username.drawTextBox();
        password.drawTextBox();

        RenderUtil.roundedRect(sr.getScaledWidth() * 0.01f, sr.getScaledHeight() * 0.1f, sr.getScaledWidth() * 0.358f, sr.getScaledHeight() * 0.80f, 15, new Color(255, 255, 255, 35));

        if (thread == null || thread.getStatus() == EnumAltStatus.NOT_LOGGED_IN) {
            CustomFont.drawCenteredMedium("Not logged in / Logging in", sr.getScaledWidth() / 2f, sr.getScaledHeight() / 2.0F + 28 * 5 + 6, Color.WHITE.getRGB());
        } else if (thread.getStatus() == EnumAltStatus.CRACKED) {
            CustomFont.drawCenteredMedium("Logged in " + thread.getUsername() + " (cracked)", sr.getScaledWidth() / 2f, sr.getScaledHeight() / 2.0F + 28 * 5 + 6, Color.YELLOW.getRGB());
        } else if (thread.getStatus() == EnumAltStatus.PREMIUM) {
            CustomFont.drawCenteredMedium("Logged in " + thread.getUsername() + " (premium)", sr.getScaledWidth() / 2f, sr.getScaledHeight() / 2.0F + 28 * 5 + 6, Color.GREEN.getRGB());
        } else if (thread.getStatus() == EnumAltStatus.FAILED) {
            CustomFont.drawCenteredMedium("Failed to log in \n please try again", sr.getScaledWidth() / 2f, sr.getScaledHeight() / 2.0F + 28 * 5 + 6, Color.RED.getRGB());
        }

        GlStateManager.pushMatrix();

        RenderUtil.scissor(sr.getScaledWidth() * 0.01f, sr.getScaledHeight() * 0.1f, sr.getScaledWidth() * 0.358f, sr.getScaledHeight() * 0.80f);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        int count = 0;
        int alternateCount = 0;

        try {
            for (final AltAccount alt : altList) {
                if (sr.getScaledWidth() * 0.01f + 2 + 108.5f * count > sr.getScaledWidth() * 0.01f + sr.getScaledWidth() * 0.358f) {
                    count = 0;
                    alternateCount++;

                }
                RenderUtil.roundedRect(sr.getScaledWidth() * 0.01f + 2 + 108.5f * count, sr.getScaledHeight() * 0.1f + 2 + 35 * alternateCount + offset, 100, 30, 15, new Color(127, 127, 127, 35));

                CustomFont.drawCenteredMedium(alt.getUsername().isEmpty() ? alt.getEmail() : alt.getUsername(), sr.getScaledWidth() * 0.01f + 2 + 108.5f * count + 50, sr.getScaledHeight() * 0.1f + 2 + 35 * alternateCount + 5 + offset, 0xffffffff);
                CustomFont.drawCenteredString(alt.isCracked() ? "Cracked" : "Premium", sr.getScaledWidth() * 0.01f + 2 + 108.5f * count + 50, sr.getScaledHeight() * 0.1f + 2 + 35 * alternateCount + 17 + offset, 0xffffffff);

                RenderUtil.circle(alt.isCracked() ? sr.getScaledWidth() * 0.01f + 2 + 108.5f * count + 50 + CustomFont.getWidth("Cracked") / 2 : sr.getScaledWidth() * 0.01f + 2 + 108.5f * count + 50 + CustomFont.getWidth("Premium") / 2, sr.getScaledHeight() * 0.1f + 2 + 35 * alternateCount + 20.5 + offset, 3, alt.isCracked() ? Color.RED : Color.GREEN);

                count++;
            }
        } catch (final ConcurrentModificationException ignored) {

        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GlStateManager.popMatrix();

        if (rightClickMenu.isVisible()) {
            rightClickMenu.draw(mouseX, mouseY);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        int count = 0;
        int alternateCount = 0;

        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        if (!rightClickMenu.isVisible()) {
            for (final AltAccount alt : altList) {
                if (sr.getScaledWidth() * 0.01f + 2 + 108.5f * count > sr.getScaledWidth() * 0.01f + sr.getScaledWidth() * 0.358f) {
                    count = 0;
                    alternateCount++;
                }

                if (mouseX >= sr.getScaledWidth() * 0.01f + 2 + 108.5f * count && mouseX <= sr.getScaledWidth() * 0.01f + 2 + 108.5f * count + 100 && mouseY >= sr.getScaledHeight() * 0.1f + 2 + 35 * alternateCount + offset && mouseY <= sr.getScaledHeight() * 0.1f + 2 + 35 * alternateCount + 30 + offset) {
                    if (mouseButton == 0) {
                        thread = new AltLoginThread(alt.getEmail(), alt.getPassword());
                        thread.start();
                        return;
                    } else if (mouseButton == 1) {
                        rightClickMenu.setX(mouseX);
                        rightClickMenu.setY(mouseY);
                        rightClickMenu.setVisible(true);
                        rightClickMenu.setSelectedAlt(alt);
                        return;
                    }
                }

                count++;
            }
        }

        if (mouseButton == 0 && rightClickMenu.isVisible()) {
            if (rightClickMenu.handleClick(mouseX, mouseY)) {
                altList.remove(rightClickMenu.getSelectedAlt());
            }
            rightClickMenu.setVisible(false);
        }

        username.mouseClicked(mouseX, mouseY, mouseButton);
        password.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        username.textboxKeyTyped(typedChar, keyCode);
        password.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        super.actionPerformed(button);

        if (button.id == 69) {
            final AddAltThread thread = new AddAltThread(username.getText(), password.getText());
            thread.start();
        } else if (button.id == 420) {
            String s = "x:xx";

            try {
                s = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
            } catch (final HeadlessException | UnsupportedFlavorException | IOException ignored) {
            }

            final String[] s1 = s.split(":");

            if (s1.length <= 1) return;

            final AddAltThread thread = new AddAltThread(s1[0], s1[1]);
            thread.start();
        } else if (button.id == 1337) {
            String rname = username.getText();

            if (username.getText().isEmpty()) rname = "Rise";

            final String name = rname + "_" + RandomUtil.randomString(15 - rname.length());

            final AddAltThread thread = new AddAltThread(name, "");
            thread.start();
        } else if (button.id == 71) {
            mc.displayGuiScreen(Rise.INSTANCE.getGuiMainMenu());
        } else if (button.id == 1338) {
            thread = new AltLoginThread(username.getText(), password.getText());
            thread.start();
        }
    }
}
