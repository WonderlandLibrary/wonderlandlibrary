/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.renderer.texture.DynamicTexture
 *  net.minecraft.client.renderer.texture.ITextureObject
 *  net.minecraft.util.ResourceLocation
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.gui.client;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import javax.imageio.ImageIO;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.Client;
import net.dev.important.utils.misc.MiscUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\f\n\u0002\b\u0003\u0018\u0000 \u00172\u00020\u0001:\u0001\u0017B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0001\u00a2\u0006\u0002\u0010\u0003J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0005H\u0014J \u0010\f\u001a\u00020\n2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\u0010\u001a\u00020\u0011H\u0016J\b\u0010\u0012\u001a\u00020\nH\u0016J\u0018\u0010\u0013\u001a\u00020\n2\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u000eH\u0014R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082.\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0002\u001a\u00020\u0001\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\b\u00a8\u0006\u0018"}, d2={"Lnet/dev/important/gui/client/GuiBackground;", "Lnet/minecraft/client/gui/GuiScreen;", "prevGui", "(Lnet/minecraft/client/gui/GuiScreen;)V", "enabledButton", "Lnet/minecraft/client/gui/GuiButton;", "particlesButton", "getPrevGui", "()Lnet/minecraft/client/gui/GuiScreen;", "actionPerformed", "", "button", "drawScreen", "mouseX", "", "mouseY", "partialTicks", "", "initGui", "keyTyped", "typedChar", "", "keyCode", "Companion", "LiquidBounce"})
public final class GuiBackground
extends GuiScreen {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final GuiScreen prevGui;
    private GuiButton enabledButton;
    private GuiButton particlesButton;
    private static boolean enabled = true;
    private static boolean particles;

    public GuiBackground(@NotNull GuiScreen prevGui) {
        Intrinsics.checkNotNullParameter(prevGui, "prevGui");
        this.prevGui = prevGui;
    }

    @NotNull
    public final GuiScreen getPrevGui() {
        return this.prevGui;
    }

    public void func_73866_w_() {
        this.enabledButton = new GuiButton(1, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 35, "Enabled (" + (enabled ? "On" : "Off") + ')');
        GuiButton guiButton = this.enabledButton;
        if (guiButton == null) {
            Intrinsics.throwUninitializedPropertyAccessException("enabledButton");
            guiButton = null;
        }
        this.field_146292_n.add(guiButton);
        this.particlesButton = new GuiButton(2, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 50 + 25, "Particles (" + (particles ? "On" : "Off") + ')');
        GuiButton guiButton2 = this.particlesButton;
        if (guiButton2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("particlesButton");
            guiButton2 = null;
        }
        this.field_146292_n.add(guiButton2);
        this.field_146292_n.add(new GuiButton(3, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 50 + 50, 98, 20, "Change wallpaper"));
        this.field_146292_n.add(new GuiButton(4, this.field_146294_l / 2 + 2, this.field_146295_m / 4 + 50 + 50, 98, 20, "Reset wallpaper"));
        this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 55 + 100 + 5, "Back"));
    }

    protected void func_146284_a(@NotNull GuiButton button) {
        Intrinsics.checkNotNullParameter(button, "button");
        switch (button.field_146127_k) {
            case 1: {
                enabled = !enabled;
                GuiButton guiButton = this.enabledButton;
                if (guiButton == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("enabledButton");
                    guiButton = null;
                }
                guiButton.field_146126_j = "Enabled (" + (enabled ? "On" : "Off") + ')';
                break;
            }
            case 2: {
                particles = !particles;
                GuiButton guiButton = this.particlesButton;
                if (guiButton == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("particlesButton");
                    guiButton = null;
                }
                guiButton.field_146126_j = "Particles (" + (particles ? "On" : "Off") + ')';
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
                    Files.copy(file2.toPath(), (OutputStream)new FileOutputStream(Client.INSTANCE.getFileManager().backgroundFile));
                    BufferedImage image = ImageIO.read(new FileInputStream(Client.INSTANCE.getFileManager().backgroundFile));
                    String string = "LiquidPlus".toLowerCase();
                    Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toLowerCase()");
                    Client.INSTANCE.setBackground(new ResourceLocation(Intrinsics.stringPlus(string, "/background.png")));
                    this.field_146297_k.func_110434_K().func_110579_a(Client.INSTANCE.getBackground(), (ITextureObject)new DynamicTexture(image));
                }
                catch (Exception e) {
                    e.printStackTrace();
                    MiscUtils.showErrorPopup("Error", "Exception class: " + e.getClass().getName() + "\nMessage: " + e.getMessage());
                    Client.INSTANCE.getFileManager().backgroundFile.delete();
                }
                break;
            }
            case 4: {
                Client.INSTANCE.setBackground(null);
                Client.INSTANCE.getFileManager().backgroundFile.delete();
                break;
            }
            case 0: {
                this.field_146297_k.func_147108_a(this.prevGui);
            }
        }
    }

    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        this.func_146278_c(0);
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }

    protected void func_73869_a(char typedChar, int keyCode) {
        if (1 == keyCode) {
            this.field_146297_k.func_147108_a(this.prevGui);
            return;
        }
        super.func_73869_a(typedChar, keyCode);
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\b\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u0006\"\u0004\b\u000b\u0010\b\u00a8\u0006\f"}, d2={"Lnet/dev/important/gui/client/GuiBackground$Companion;", "", "()V", "enabled", "", "getEnabled", "()Z", "setEnabled", "(Z)V", "particles", "getParticles", "setParticles", "LiquidBounce"})
    public static final class Companion {
        private Companion() {
        }

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

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

