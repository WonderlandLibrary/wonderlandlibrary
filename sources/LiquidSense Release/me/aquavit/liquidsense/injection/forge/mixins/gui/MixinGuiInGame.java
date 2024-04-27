package me.aquavit.liquidsense.injection.forge.mixins.gui;

import me.aquavit.liquidsense.module.modules.client.AntiBlind;
import me.aquavit.liquidsense.utils.render.BlurBuffer;
import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.event.events.Render2DEvent;
import me.aquavit.liquidsense.module.modules.client.HUD;
import me.aquavit.liquidsense.utils.mc.ClassUtils;
import me.aquavit.liquidsense.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(GuiIngame.class)
@SideOnly(Side.CLIENT)
public abstract class MixinGuiInGame extends Gui {
    @Shadow
    protected abstract void renderHotbarItem(int index, int xPos, int yPos, float partialTicks, EntityPlayer player);

    @Final
    @Shadow
    protected Minecraft mc;

    private double slot = 0.0D;
    double speed = 1.0D;
    int lastSlot = 0;

    @Inject(method = "renderScoreboard", at = @At("HEAD"), cancellable = true)
    private void renderScoreboard(CallbackInfo callbackInfo) {
        if (LiquidSense.moduleManager.getModule(HUD.class).getState())
            callbackInfo.cancel();
    }

    @Inject(method = "renderTooltip", at = @At("HEAD"), cancellable = true)
    private void renderTooltip(ScaledResolution sr, float partialTicks, CallbackInfo callbackInfo) {
        final HUD hud = (HUD) LiquidSense.moduleManager.getModule(HUD.class);
        int ScrollSpeed = hud.hotbarSpeed.get() - 1;

        if (OpenGlHelper.shadersSupported && mc.getRenderViewEntity() instanceof EntityPlayer)
           BlurBuffer.updateBlurBuffer(20f,true);

        if(mc.getRenderViewEntity() instanceof EntityPlayer && hud.getState() && hud.blackHotbarValue.get()) {
            EntityPlayer entityPlayer = (EntityPlayer) mc.getRenderViewEntity();

            double currentItem = entityPlayer.inventory.currentItem;

            if (hud.moreinventory.get()){
                GlStateManager.pushMatrix();
                GlStateManager.translate((float) new ScaledResolution(mc).getScaledWidth() / 2 - 90,(float) new ScaledResolution(mc).getScaledHeight() - 25,0);
                RenderUtils.drawBorderedRect(0,1,180,-58,1,new Color(0,0,0,255).getRGB(),new Color(0,0,0,130).getRGB());
                RenderHelper.enableGUIStandardItemLighting();
                //renderArmor();
                int x2=1,x3=1,x4=1;
                int i1,i3,i4;
                for (i1 = 27; i1 < 36; ++i1){
                    renderItem(i1, 1+x2, -16, mc.thePlayer);
                    x2+=20;
                }
                for (i3 = 18; i3 < 27; ++i3){
                    renderItem(i3, 1+x3, -36, mc.thePlayer);
                    x3+=20;
                }
                for (i4 = 9; i4 < 18; ++i4){
                    renderItem(i4, 1+x4, -56, mc.thePlayer);
                    x4+=20;
                }
                RenderHelper.disableStandardItemLighting();
                GlStateManager.popMatrix();
            }

            this.speed = (Math.abs(this.slot + 1.0D - (currentItem + 1.0D)) / (100 - ScrollSpeed));

            if (Math.abs(entityPlayer.inventory.currentItem - this.slot) < this.speed) {
                currentItem = entityPlayer.inventory.currentItem;
            } else {
                double motion;

                if (entityPlayer.inventory.currentItem - this.slot > 0.0D) {
                    motion = this.speed;
                } else {
                    motion = -this.speed;
                }

                currentItem = this.slot + motion;
            }

            this.slot = currentItem;
            this.lastSlot = entityPlayer.inventory.currentItem;

            int middleScreen = sr.getScaledWidth() / 2;

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GuiIngame.drawRect(middleScreen - 90, sr.getScaledHeight() - 24, middleScreen + 90, sr.getScaledHeight(), Integer.MIN_VALUE);
            GuiIngame.drawRect((int) (middleScreen - 90 + this.slot * 20), sr.getScaledHeight() - 24, (int) (middleScreen - 94 + this.slot * 20 + 24), sr.getScaledHeight() - 22 - 1 + 24, Integer.MAX_VALUE);

            if (Math.abs(this.slot - this.lastSlot) <= 0.05D) {
                this.slot = this.lastSlot;
            }

            GlStateManager.enableRescaleNormal();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            RenderHelper.enableGUIStandardItemLighting();

            for(int j = 0; j < 9; ++j) {
                int k = sr.getScaledWidth() / 2 - 90 + j * 20 + 2;
                int l = sr.getScaledHeight() - 16 - 3;
                this.renderHotbarItem(j, k, l, partialTicks, entityPlayer);
            }

            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();

            LiquidSense.eventManager.callEvent(new Render2DEvent(partialTicks));
            callbackInfo.cancel();
        }
    }

    @Inject(method = "renderTooltip", at = @At("RETURN"))
    private void renderTooltipPost(ScaledResolution sr, float partialTicks, CallbackInfo callbackInfo) {
        if (!ClassUtils.hasClass("net.labymod.api.LabyModAPI")) {
            LiquidSense.eventManager.callEvent(new Render2DEvent(partialTicks));
        }
    }

    @Inject(method = "renderPumpkinOverlay", at = @At("HEAD"), cancellable = true)
    private void renderPumpkinOverlay(final CallbackInfo callbackInfo) {
        if (LiquidSense.moduleManager.getModule(AntiBlind.class).getState() && AntiBlind.pumpkinEffect.get())
            callbackInfo.cancel();
    }

    private void renderItem(int i, int x, int y , EntityPlayer player) {
        ItemStack itemstack = player.inventory.mainInventory[i];
        if (itemstack != null) {
            mc.getRenderItem().renderItemAndEffectIntoGUI(itemstack, x, y);
            mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, itemstack, x-1, y-1);
        }
    }
}