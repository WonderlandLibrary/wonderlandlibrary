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
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.misc.MiscUtils;

public final class GuiBackground
extends WrappedGuiScreen {
    private final IGuiScreen prevGui;
    private IGuiButton particlesButton;
    private static boolean enabled;
    private IGuiButton enabledButton;
    private static boolean particles;
    public static final Companion Companion;

    public GuiBackground(IGuiScreen iGuiScreen) {
        this.prevGui = iGuiScreen;
    }

    public static final void access$setEnabled$cp(boolean bl) {
        enabled = bl;
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.getRepresentedScreen().drawBackground(0);
        super.drawScreen(n, n2, f);
    }

    @Override
    public void keyTyped(char c, int n) {
        if (1 == n) {
            MinecraftInstance.mc.displayGuiScreen(this.prevGui);
            return;
        }
        super.keyTyped(c, n);
    }

    public static final boolean access$getParticles$cp() {
        return particles;
    }

    @Override
    public void actionPerformed(IGuiButton iGuiButton) {
        switch (iGuiButton.getId()) {
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
                    BufferedImage bufferedImage = ImageIO.read(new FileInputStream(LiquidBounce.INSTANCE.getFileManager().backgroundFile));
                    String string = "atfield";
                    StringBuilder stringBuilder = new StringBuilder();
                    IClassProvider iClassProvider = MinecraftInstance.classProvider;
                    boolean bl = false;
                    String string2 = string.toLowerCase();
                    IResourceLocation iResourceLocation = iClassProvider.createResourceLocation(stringBuilder.append(string2).append("/background.png").toString());
                    LiquidBounce.INSTANCE.setBackground(iResourceLocation);
                    MinecraftInstance.mc.getTextureManager().loadTexture(iResourceLocation, MinecraftInstance.classProvider.createDynamicTexture(bufferedImage));
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                    MiscUtils.showErrorPopup("Error", "Exception class: " + exception.getClass().getName() + "\nMessage: " + exception.getMessage());
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
                break;
            }
        }
    }

    static {
        Companion = new Companion(null);
        enabled = true;
    }

    public static final boolean access$getEnabled$cp() {
        return enabled;
    }

    public static final void access$setParticles$cp(boolean bl) {
        particles = bl;
    }

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

    public final IGuiScreen getPrevGui() {
        return this.prevGui;
    }

    public static final class Companion {
        public final void setEnabled(boolean bl) {
            GuiBackground.access$setEnabled$cp(bl);
        }

        public final boolean getEnabled() {
            return GuiBackground.access$getEnabled$cp();
        }

        public final boolean getParticles() {
            return GuiBackground.access$getParticles$cp();
        }

        private Companion() {
        }

        public final void setParticles(boolean bl) {
            GuiBackground.access$setParticles$cp(bl);
        }

        public Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }
}

