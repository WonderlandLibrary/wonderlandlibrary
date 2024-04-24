package me.crimp.claudius.mixins.mixins;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.crimp.claudius.claudius;
import me.crimp.claudius.mod.modules.client.HUD;
import me.crimp.claudius.utils.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;


@Mixin(GuiMainMenu.class)
public abstract class MixinGuiMainMenu extends GuiScreen {

    @Shadow
    public static ResourceLocation MINECRAFT_TITLE_TEXTURES;
    @Final
    @Shadow
    private float minceraftRoll;
    @Shadow
    private static final ResourceLocation field_194400_H = new ResourceLocation("textures/gui/title/edition.png");
    @Shadow
    private String splashText;
    @Shadow
    private int widthCopyrightRest;
    @Shadow
    private int widthCopyright;
    @Shadow
    private String openGLWarning1;
    @Shadow
    private String openGLWarning2;
    @Shadow
    private int openGLWarningX1;
    @Shadow
    private int openGLWarningY1;
    @Shadow
    private int openGLWarningX2;
    @Shadow
    private int openGLWarningY2;
    @Shadow
    private int openGLWarning2Width;
    @Shadow
    private float panoramaTimer;
    @Shadow
    private static ResourceLocation[] TITLE_PANORAMA_PATHS;
    private static final ResourceLocation BGTEXTURE = new ResourceLocation("textures/2.png");


    @Inject(method = "drawScreen", at = @At("HEAD"), cancellable = true)
    void drawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if (claudius.moduleManager.isModuleEnabled("CustomBackground")) {

            GlStateManager.enableAlpha();
            GlStateManager.disableCull();


            int j = this.width / 2 - 137;
            this.drawGradientRect(0, 0, this.width, this.height, ColorUtil.toARGB(255, 0, 0, 255), ColorUtil.toARGB(66, 155, 184, 255));
            this.drawGradientRect(0, 0, this.width, this.height, 0, Integer.MIN_VALUE);


            GlStateManager.pushMatrix();
            float f = ((float) (j % 8) / 8.0F - 0.5F) / 64.0F;
            float f1 = ((float) (j / 8) / 8.0F - 0.5F) / 64.0F;
            float f2 = 0.0F;
            GlStateManager.translate(f, f1, 0.0F);


            this.mc.getTextureManager().bindTexture(MINECRAFT_TITLE_TEXTURES);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            if ((double) this.minceraftRoll < 1.0E-4D) {
                this.drawTexturedModalRect(j + 0, 30, 0, 0, 99, 44);
                this.drawTexturedModalRect(j + 99, 30, 129, 0, 27, 44);
                this.drawTexturedModalRect(j + 99 + 26, 30, 126, 0, 3, 44);
                this.drawTexturedModalRect(j + 99 + 26 + 3, 30, 99, 0, 26, 44);
                this.drawTexturedModalRect(j + 155, 30, 0, 45, 155, 44);
            } else {
                this.drawTexturedModalRect(j + 0, 30, 0, 0, 155, 44);
                this.drawTexturedModalRect(j + 155, 30, 0, 45, 155, 44);
            }
            //this.backgroundTexture = new ResourceLocation("textures/booky.png");

            this.mc.getTextureManager().bindTexture(field_194400_H);
            drawModalRectWithCustomSizedTexture(j + 88, 67, 0.0F, 0.0F, 98, 14, 128.0F, 16.0F);

            this.splashText = net.minecraftforge.client.ForgeHooksClient.renderMainMenu(((GuiMainMenu) ((Object) this)), this.fontRenderer, this.width, this.height, this.splashText);

            GlStateManager.pushMatrix();
            GlStateManager.translate((float) (this.width / 2 + 90), 70.0F, 0.0F);
            GlStateManager.rotate(-20.0F, 0.0F, 0.0F, 1.0F);
            f = 1.8F - MathHelper.abs(MathHelper.sin((float) (Minecraft.getSystemTime() % 1000L) / 1000.0F * ((float) Math.PI * 2F)) * 0.1F);
            f = f * 100.0F / (float) (this.fontRenderer.getStringWidth(this.splashText) + 32);
            GlStateManager.scale(f, f, f);
            this.drawCenteredString(this.fontRenderer, this.splashText, 0, -8, -256);
            GlStateManager.popMatrix();

            String s = "Minecraft 1.12.2";

            if (this.mc.isDemo()) {
                s = s + " Demo";
            } else {
                s = s + ("release".equalsIgnoreCase(this.mc.getVersionType()) ? "" : "/" + this.mc.getVersionType());
            }

            java.util.List<String> brandings = com.google.common.collect.Lists.reverse(net.minecraftforge.fml.common.FMLCommonHandler.instance().getBrandings(true));
            for (int brdline = 0; brdline < brandings.size(); brdline++) {
                String brd = brandings.get(brdline);
                if (!com.google.common.base.Strings.isNullOrEmpty(brd)) {
                    this.drawString(this.fontRenderer, brd, 2, this.height - (10 + brdline * (this.fontRenderer.FONT_HEIGHT + 1)), 16777215);
                }
            }

            this.drawString(this.fontRenderer, "GET FUCKED, claudius On Top Best Best Best", this.widthCopyrightRest, this.height - 10, -1);

            if (mouseX > this.widthCopyrightRest && mouseX < this.widthCopyrightRest + this.widthCopyright && mouseY > this.height - 10 && mouseY < this.height && Mouse.isInsideWindow()) {
                drawRect(this.widthCopyrightRest, this.height - 1, this.widthCopyrightRest + this.widthCopyright, this.height, -1);
            }

            if (this.openGLWarning1 != null && !this.openGLWarning1.isEmpty()) {
                drawRect(this.openGLWarningX1 - 2, this.openGLWarningY1 - 2, this.openGLWarningX2 + 2, this.openGLWarningY2 - 1, 1428160512);
                this.drawString(this.fontRenderer, this.openGLWarning1, this.openGLWarningX1, this.openGLWarningY1, -1);
                this.drawString(this.fontRenderer, this.openGLWarning2, (this.width - this.openGLWarning2Width) / 2, (this.buttonList.get(0)).y - 12, -1);
            }

            GlStateManager.pushMatrix();


            if (claudius.moduleManager.isModuleEnabled("CustomBackground")) {
                ci.cancel();
                super.drawScreen(mouseX, mouseY, partialTicks);
            }
        }
    }
}

