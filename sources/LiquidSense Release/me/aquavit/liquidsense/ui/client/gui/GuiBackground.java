package me.aquavit.liquidsense.ui.client.gui;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.utils.misc.MiscUtils;
import me.aquavit.liquidsense.ui.client.gui.elements.GuiButtonElement;
import me.aquavit.liquidsense.ui.font.Fonts;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public class GuiBackground extends GuiScreen {

    private GuiButtonElement enabledButton;
    private GuiButtonElement particlesButton;

    private final GuiScreen prevGui;

    private static boolean enabled = true;
    private static boolean particles = false;
    public static final Companion Companion = new Companion();

    public GuiBackground(GuiScreen prevGui) {
        this.prevGui = prevGui;
    }

    public void initGui() {
        enabledButton = new GuiButtonElement(1, width / 2 - 100, height / 4 + 35, 200, 20,"Enabled (" + (enabled ? "On" : "Off") + ")");
        buttonList.add(enabledButton);
        particlesButton = new GuiButtonElement(2, width / 2 - 100, height / 4 + 50 + 25, 200, 20,"Particles (" + (particles ? "On" : "Off") + ")");
        buttonList.add(particlesButton);
        buttonList.add(new GuiButtonElement(3, width / 2 - 100, height / 4 + 50 + 25 * 2, 98, 20, "Change wallpaper"));
        buttonList.add(new GuiButtonElement(4, width / 2 + 2, height / 4 + 50 + 25 * 2, 98, 20, "Reset wallpaper"));
        buttonList.add(new GuiButtonElement(0, width / 2 - 100, height / 4 + 55 + 25 * 4 + 5, 200, 20,"Back"));
        super.initGui();
    }

    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 1: {
                enabled = !enabled;
                this.enabledButton.displayString = "Enabled (" + (enabled ? "On" : "Off") + ')';
                break;
            }
            case 2: {
                particles = !particles;
                this.particlesButton.displayString = "Particles (" + (particles ? "On" : "Off") + ')';
                break;
            }
            case 3: {
                File file = MiscUtils.openFileChooser();
                if (file == null) {
                    return;
                }
                if (file.isDirectory()) {
                    return;
                }
                try {
                    Files.copy(file.toPath(), Files.newOutputStream(LiquidSense.getFileManager().backgroundFile.toPath()));
                    BufferedImage image = ImageIO.read(Files.newInputStream(LiquidSense.getFileManager().backgroundFile.toPath()));
                    LiquidSense.setBackground(new ResourceLocation(LiquidSense.CLIENT_NAME.toLowerCase() + "/background.png"));
                    mc.getTextureManager().loadTexture(LiquidSense.getBackground(), new DynamicTexture(image));
                } catch (Exception e) {
                    e.printStackTrace();
                    MiscUtils.showErrorPopup("Error", "Exception class: " + e.getClass().getName() + "\nMessage: " + e.getMessage());
                    LiquidSense.fileManager.backgroundFile.delete();
                }
                break;
            }
            case 4: {
                LiquidSense.setBackground(null);
                LiquidSense.fileManager.backgroundFile.delete();
                break;
            }
            case 0: {
                this.mc.displayGuiScreen(this.prevGui);
            }
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawBackground(0);
        Fonts.logo.drawCenteredString("Background", (float)this.width / 2.0f, (float)this.height / 8.0f + 5.0f, new Color(255, 255, 255, 255).getRGB(), true);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (Keyboard.KEY_ESCAPE == keyCode) {
            this.mc.displayGuiScreen(this.prevGui);
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }

    public static final class Companion {
        public final boolean getEnabled() {
            return enabled;
        }

        public final void setEnabled(boolean state) {
            enabled = state;
        }

        public final boolean getParticles() {
            return particles;
        }

        public final void setParticles(boolean state) {
            particles = state;
        }

        private Companion() {
        }
    }
}
