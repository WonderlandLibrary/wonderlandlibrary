/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.DefaultConstructorMarker
 */
package net.ccbluex.liquidbounce.ui.client;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import javax.imageio.ImageIO;
import kotlin.jvm.internal.DefaultConstructorMarker;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.IClassProvider;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiButton;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiScreen;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import net.ccbluex.liquidbounce.api.util.WrappedGuiScreen;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.misc.MiscUtils;

public final class GuiBackground
extends WrappedGuiScreen {
    private IGuiButton enabledButton;
    private IGuiButton particlesButton;
    private final IGuiScreen prevGui;
    private static boolean enabled;
    private static boolean particles;
    public static final Companion Companion;

    @Override
    public void initGui() {
        this.enabledButton = MinecraftInstance.classProvider.createGuiButton(1, this.getRepresentedScreen().getWidth() / 2 - 100, this.getRepresentedScreen().getHeight() / 4 + 35, "Enabled (" + (enabled ? "On" : "Off") + ')');
        this.getRepresentedScreen().getButtonList().add(this.enabledButton);
        this.particlesButton = MinecraftInstance.classProvider.createGuiButton(2, this.getRepresentedScreen().getWidth() / 2 - 100, this.getRepresentedScreen().getHeight() / 4 + 50 + 25, "Particles (" + (particles ? "On" : "Off") + ')');
        this.getRepresentedScreen().getButtonList().add(this.particlesButton);
        this.getRepresentedScreen().getButtonList().add(MinecraftInstance.classProvider.createGuiButton(3, this.getRepresentedScreen().getWidth() / 2 - 100, this.getRepresentedScreen().getHeight() / 4 + 50 + 50, 98, 20, "Change wallpaper"));
        this.getRepresentedScreen().getButtonList().add(MinecraftInstance.classProvider.createGuiButton(4, this.getRepresentedScreen().getWidth() / 2 + 2, this.getRepresentedScreen().getHeight() / 4 + 50 + 50, 98, 20, "Reset wallpaper"));
        this.getRepresentedScreen().getButtonList().add(MinecraftInstance.classProvider.createGuiButton(0, this.getRepresentedScreen().getWidth() / 2 - 100, this.getRepresentedScreen().getHeight() / 4 + 55 + 100 + 5, "Back"));
    }

    @Override
    public void actionPerformed(IGuiButton button) {
        switch (button.getId()) {
            case 1: {
                enabled = !enabled;
                this.enabledButton.setDisplayString("Enabled (" + (enabled ? "On" : "Off") + ')');
                break;
            }
            case 2: {
                particles = !particles;
                this.particlesButton.setDisplayString("Particles (" + (particles ? "On" : "Off") + ')');
                break;
            }
            case 3: {
                File file = MiscUtils.openFileChooser();
                if (file == null) {
                    return;
                }
                File file2 = file;
                if (file2.isDirectory()) {
                    return;
                }
                try {
                    Files.copy(file2.toPath(), (OutputStream)new FileOutputStream(LiquidBounce.INSTANCE.getFileManager().backgroundFile));
                    BufferedImage image2 = ImageIO.read(new FileInputStream(LiquidBounce.INSTANCE.getFileManager().backgroundFile));
                    String string = "LRQ";
                    StringBuilder stringBuilder = new StringBuilder();
                    IClassProvider iClassProvider = MinecraftInstance.classProvider;
                    boolean bl = false;
                    String string2 = string.toLowerCase();
                    IResourceLocation location = iClassProvider.createResourceLocation(stringBuilder.append(string2).append("/background.png").toString());
                    LiquidBounce.INSTANCE.setBackground(location);
                    MinecraftInstance.mc.getTextureManager().loadTexture(location, MinecraftInstance.classProvider.createDynamicTexture(image2));
                }
                catch (Exception e) {
                    e.printStackTrace();
                    MiscUtils.showErrorPopup("Error", "Exception class: " + e.getClass().getName() + "\nMessage: " + e.getMessage());
                    LiquidBounce.INSTANCE.getFileManager().backgroundFile.delete();
                }
                break;
            }
            case 4: {
                LiquidBounce.INSTANCE.setBackground(null);
                LiquidBounce.INSTANCE.getFileManager().backgroundFile.delete();
                break;
            }
            case 0: {
                MinecraftInstance.mc.displayGuiScreen(this.prevGui);
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.getRepresentedScreen().drawBackground(0);
        Fonts.fontBold180.drawCenteredString("Background", (float)this.getRepresentedScreen().getWidth() / 2.0f, (float)this.getRepresentedScreen().getHeight() / 8.0f + 5.0f, 4673984, true);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (1 == keyCode) {
            MinecraftInstance.mc.displayGuiScreen(this.prevGui);
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }

    public final IGuiScreen getPrevGui() {
        return this.prevGui;
    }

    public GuiBackground(IGuiScreen prevGui) {
        this.prevGui = prevGui;
    }

    static {
        Companion = new Companion(null);
        enabled = true;
    }

    public static final class Companion {
        public final boolean getEnabled() {
            return enabled;
        }

        public final void setEnabled(boolean bl) {
            enabled = bl;
        }

        public final boolean getParticles() {
            return particles;
        }

        public final void setParticles(boolean bl) {
            particles = bl;
        }

        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

