/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.renderer.texture.DynamicTexture
 *  net.minecraft.client.renderer.texture.ITextureObject
 *  net.minecraft.util.ResourceLocation
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.ui.client;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import javax.imageio.ImageIO;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.special.GradientBackground;
import net.ccbluex.liquidbounce.utils.extensions.RendererExtensionKt;
import net.ccbluex.liquidbounce.utils.misc.MiscUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\f\n\u0002\b\u0004\u0018\u0000 \u001d2\u00020\u0001:\u0001\u001dB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0001\u00a2\u0006\u0002\u0010\u0003J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0005H\u0014J \u0010\u0011\u001a\u00020\u000f2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00132\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J\b\u0010\u0017\u001a\u00020\u000fH\u0016J\u0018\u0010\u0018\u001a\u00020\u000f2\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u0013H\u0014J\b\u0010\u001c\u001a\u00020\u000fH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X\u0082.\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0002\u001a\u00020\u0001\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u000e\u0010\r\u001a\u00020\u0005X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001e"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/GuiBackground;", "Lnet/minecraft/client/gui/GuiScreen;", "prevGui", "(Lnet/minecraft/client/gui/GuiScreen;)V", "animatedButton", "Lnet/minecraft/client/gui/GuiButton;", "blurButton", "enabledButton", "lastButton", "nextButton", "particlesButton", "getPrevGui", "()Lnet/minecraft/client/gui/GuiScreen;", "typeButton", "actionPerformed", "", "button", "drawScreen", "mouseX", "", "mouseY", "partialTicks", "", "initGui", "keyTyped", "typedChar", "", "keyCode", "updateButtons", "Companion", "KyinoClient"})
public final class GuiBackground
extends GuiScreen {
    private GuiButton enabledButton;
    private GuiButton blurButton;
    private GuiButton particlesButton;
    private GuiButton typeButton;
    private GuiButton lastButton;
    private GuiButton nextButton;
    private GuiButton animatedButton;
    @NotNull
    private final GuiScreen prevGui;
    private static boolean enabled;
    private static boolean particles;
    private static boolean blur;
    public static final Companion Companion;

    public void func_73866_w_() {
        GuiButton guiButton = this.enabledButton = new GuiButton(1, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 35, "");
        if (guiButton == null) {
            Intrinsics.throwUninitializedPropertyAccessException("enabledButton");
        }
        this.field_146292_n.add(guiButton);
        GuiButton guiButton2 = this.blurButton = new GuiButton(9, this.field_146294_l / 2 - 100, this.field_146295_m / 4, "");
        if (guiButton2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("blurButton");
        }
        this.field_146292_n.add(guiButton2);
        GuiButton guiButton3 = this.typeButton = new GuiButton(5, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 40 + 25, "");
        if (guiButton3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("typeButton");
        }
        this.field_146292_n.add(guiButton3);
        GuiButton guiButton4 = this.lastButton = new GuiButton(6, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 40 + 50, 20, 20, "<");
        if (guiButton4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("lastButton");
        }
        this.field_146292_n.add(guiButton4);
        GuiButton guiButton5 = this.nextButton = new GuiButton(7, this.field_146294_l / 2 + 80, this.field_146295_m / 4 + 40 + 50, 20, 20, ">");
        if (guiButton5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("nextButton");
        }
        this.field_146292_n.add(guiButton5);
        GuiButton guiButton6 = this.animatedButton = new GuiButton(8, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 40 + 75, "");
        if (guiButton6 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("animatedButton");
        }
        this.field_146292_n.add(guiButton6);
        GuiButton guiButton7 = this.particlesButton = new GuiButton(2, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 40 + 100, "");
        if (guiButton7 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("particlesButton");
        }
        this.field_146292_n.add(guiButton7);
        this.field_146292_n.add(new GuiButton(3, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 40 + 125, 98, 20, "%ui.background.change%"));
        this.field_146292_n.add(new GuiButton(4, this.field_146294_l / 2 + 2, this.field_146295_m / 4 + 40 + 125, 98, 20, "%ui.background.reset%"));
        this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 40 + 150 + 10, "%ui.back%"));
        this.updateButtons();
    }

    private final void updateButtons() {
        boolean hasCustomBackground;
        if (this.enabledButton == null) {
            Intrinsics.throwUninitializedPropertyAccessException("enabledButton");
        }
        this.enabledButton.field_146126_j = "%ui.status% (" + (enabled ? "%ui.on%" : "%ui.off%") + ')';
        if (this.blurButton == null) {
            Intrinsics.throwUninitializedPropertyAccessException("blurButton");
        }
        this.blurButton.field_146126_j = "Background Blur (" + (blur ? "%ui.on%" : "%ui.off%") + ')';
        if (this.typeButton == null) {
            Intrinsics.throwUninitializedPropertyAccessException("typeButton");
        }
        this.typeButton.field_146126_j = "%ui.background.gtype%: " + (Object)((Object)GradientBackground.INSTANCE.getGradientSide());
        if (this.particlesButton == null) {
            Intrinsics.throwUninitializedPropertyAccessException("particlesButton");
        }
        this.particlesButton.field_146126_j = "%ui.background.particles% (" + (particles ? "%ui.on%" : "%ui.off%") + ')';
        if (this.animatedButton == null) {
            Intrinsics.throwUninitializedPropertyAccessException("animatedButton");
        }
        this.animatedButton.field_146126_j = "%ui.background.ganimated% (" + (GradientBackground.INSTANCE.getAnimated() ? "%ui.on%" : "%ui.off%") + ')';
        boolean bl = hasCustomBackground = LiquidBounce.INSTANCE.getBackground() != null;
        if (this.lastButton == null) {
            Intrinsics.throwUninitializedPropertyAccessException("lastButton");
        }
        boolean bl2 = this.lastButton.field_146124_l = !hasCustomBackground;
        if (this.nextButton == null) {
            Intrinsics.throwUninitializedPropertyAccessException("nextButton");
        }
        boolean bl3 = this.nextButton.field_146124_l = !hasCustomBackground;
        if (this.typeButton == null) {
            Intrinsics.throwUninitializedPropertyAccessException("typeButton");
        }
        boolean bl4 = this.typeButton.field_146124_l = !hasCustomBackground;
        if (this.animatedButton == null) {
            Intrinsics.throwUninitializedPropertyAccessException("animatedButton");
        }
        this.animatedButton.field_146124_l = !hasCustomBackground;
    }

    protected void func_146284_a(@NotNull GuiButton button) {
        Intrinsics.checkParameterIsNotNull(button, "button");
        switch (button.field_146127_k) {
            case 1: {
                enabled = !enabled;
                break;
            }
            case 9: {
                blur = !blur;
                break;
            }
            case 2: {
                particles = !particles;
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
                    Files.copy(file2.toPath(), new FileOutputStream(LiquidBounce.INSTANCE.getFileManager().backgroundFile));
                    BufferedImage image2 = ImageIO.read(new FileInputStream(LiquidBounce.INSTANCE.getFileManager().backgroundFile));
                    String string = "LiquidBounce";
                    StringBuilder stringBuilder = new StringBuilder();
                    LiquidBounce liquidBounce = LiquidBounce.INSTANCE;
                    boolean bl = false;
                    String string2 = string.toLowerCase();
                    Intrinsics.checkExpressionValueIsNotNull(string2, "(this as java.lang.String).toLowerCase()");
                    String string3 = string2;
                    String string4 = stringBuilder.append(string3).append("/background.png").toString();
                    liquidBounce.setBackground(new ResourceLocation(string4));
                    Minecraft minecraft = this.field_146297_k;
                    Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                    minecraft.func_110434_K().func_110579_a(LiquidBounce.INSTANCE.getBackground(), (ITextureObject)new DynamicTexture(image2));
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
            case 5: {
                int index = ArraysKt.indexOf(GradientBackground.INSTANCE.getGradientSides(), GradientBackground.INSTANCE.getGradientSide());
                GradientBackground.INSTANCE.setGradientSide(index == ArraysKt.getLastIndex(GradientBackground.INSTANCE.getGradientSides()) ? GradientBackground.INSTANCE.getGradientSides()[0] : GradientBackground.INSTANCE.getGradientSides()[index + 1]);
                break;
            }
            case 6: 
            case 7: {
                int indexAffect = button.field_146127_k == 6 ? -1 : 1;
                int index = GradientBackground.INSTANCE.getGradients().indexOf(GradientBackground.INSTANCE.getNowGradient()) + indexAffect;
                GradientBackground.INSTANCE.setNowGradient(index > CollectionsKt.getLastIndex(GradientBackground.INSTANCE.getGradients()) ? GradientBackground.INSTANCE.getGradients().get(0) : (index < 0 ? GradientBackground.INSTANCE.getGradients().get(CollectionsKt.getLastIndex(GradientBackground.INSTANCE.getGradients())) : GradientBackground.INSTANCE.getGradients().get(index)));
                break;
            }
            case 8: {
                GradientBackground.INSTANCE.setAnimated(!GradientBackground.INSTANCE.getAnimated());
                break;
            }
            case 0: {
                this.field_146297_k.func_147108_a(this.prevGui);
            }
        }
        this.updateButtons();
    }

    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        this.func_146278_c(0);
        FontRenderer fontRenderer = this.field_146297_k.field_71466_p;
        Intrinsics.checkExpressionValueIsNotNull(fontRenderer, "mc.fontRendererObj");
        RendererExtensionKt.drawCenteredString(fontRenderer, "%ui.background%", (float)this.field_146294_l / 2.0f, (float)this.field_146295_m / 8.0f + 5.0f, 4673984, true);
        FontRenderer fontRenderer2 = this.field_146297_k.field_71466_p;
        Intrinsics.checkExpressionValueIsNotNull(fontRenderer2, "mc.fontRendererObj");
        String string = "%ui.background.gcurrent%: " + (LiquidBounce.INSTANCE.getBackground() == null ? GradientBackground.INSTANCE.getNowGradient().getName() : "Customized");
        float f = (float)this.field_146294_l / 2.0f;
        float f2 = (float)(this.field_146295_m / 4 + 40) + 50.0f + (float)(20 - this.field_146297_k.field_71466_p.field_78288_b) * 0.5f;
        Color color = Color.WHITE;
        Intrinsics.checkExpressionValueIsNotNull(color, "Color.WHITE");
        RendererExtensionKt.drawCenteredString(fontRenderer2, string, f, f2, color.getRGB(), true);
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }

    protected void func_73869_a(char typedChar, int keyCode) {
        if (1 == keyCode) {
            this.field_146297_k.func_147108_a(this.prevGui);
            return;
        }
        super.func_73869_a(typedChar, keyCode);
    }

    @NotNull
    public final GuiScreen getPrevGui() {
        return this.prevGui;
    }

    public GuiBackground(@NotNull GuiScreen prevGui) {
        Intrinsics.checkParameterIsNotNull(prevGui, "prevGui");
        this.prevGui = prevGui;
    }

    static {
        Companion = new Companion(null);
        enabled = true;
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u000b\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u0006\"\u0004\b\u000b\u0010\bR\u001a\u0010\f\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u0006\"\u0004\b\u000e\u0010\b\u00a8\u0006\u000f"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/GuiBackground$Companion;", "", "()V", "blur", "", "getBlur", "()Z", "setBlur", "(Z)V", "enabled", "getEnabled", "setEnabled", "particles", "getParticles", "setParticles", "KyinoClient"})
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

        public final boolean getBlur() {
            return blur;
        }

        public final void setBlur(boolean bl) {
            blur = bl;
        }

        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

