// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.HUD;

import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import net.minecraft.item.ItemStack;
import java.util.ArrayList;
import java.util.Collections;
import ru.tuskevich.event.EventTarget;
import ru.tuskevich.util.font.FontRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.util.EnumHand;
import ru.tuskevich.util.font.Fonts;
import ru.tuskevich.util.color.ColorUtility;
import net.minecraft.client.entity.AbstractClientPlayer;
import ru.tuskevich.util.render.RenderUtility;
import ru.tuskevich.util.render.GlowUtility;
import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import ru.tuskevich.util.math.MathUtility;
import com.mojang.realmsclient.gui.ChatFormatting;
import ru.tuskevich.modules.impl.PLAYER.StreamerMode;
import net.minecraft.entity.player.EntityPlayer;
import ru.tuskevich.util.animations.AnimationMath;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.Minecraft;
import ru.tuskevich.modules.impl.COMBAT.KillAura;
import ru.tuskevich.event.events.impl.EventDisplay;
import ru.tuskevich.ui.dropui.setting.Setting;
import ru.tuskevich.Minced;
import net.minecraft.entity.EntityLivingBase;
import ru.tuskevich.util.drag.Dragging;
import ru.tuskevich.ui.dropui.setting.imp.ModeSetting;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "TargetHUD", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd", type = Type.HUD)
public class TargetHUD extends Module
{
    public ModeSetting thudMode;
    public final Dragging targetHUDDrag;
    private static EntityLivingBase curTarget;
    private double scale;
    float hp;
    float ar;
    int posX;
    int posY;
    
    public TargetHUD() {
        this.thudMode = new ModeSetting("Mode", "Type 1", new String[] { "Type 1" });
        this.targetHUDDrag = Minced.getInstance().createDrag(this, "targetHUDDrag", 282.0f, 266.0f);
        this.scale = 0.0;
        this.add(new Setting[0]);
    }
    
    @EventTarget
    public void onDispay(final EventDisplay eventDisplay) {
        if (KillAura.target == null) {
            final Minecraft mc = TargetHUD.mc;
            if (Minecraft.player != null && TargetHUD.mc.currentScreen instanceof GuiChat) {
                final Minecraft mc2 = TargetHUD.mc;
                TargetHUD.curTarget = Minecraft.player;
                this.scale = AnimationMath.animation((float)this.scale, 1.0f, (float)(3.0 * AnimationMath.deltaTime()));
            }
            else {
                this.scale = AnimationMath.animation((float)this.scale, 0.0f, (float)(3.0 * AnimationMath.deltaTime()));
            }
        }
        else {
            TargetHUD.curTarget = KillAura.target;
            this.scale = AnimationMath.animation((float)this.scale, 1.0f, (float)(3.0 * AnimationMath.deltaTime()));
        }
        if (TargetHUD.curTarget == null || !(TargetHUD.curTarget instanceof EntityPlayer)) {
            return;
        }
        String string = null;
        Label_0254: {
            if (Minced.getInstance().manager.getModule(StreamerMode.class).state) {
                final String name2 = TargetHUD.curTarget.getName();
                final Minecraft mc3 = TargetHUD.mc;
                if (name2.equals(Minecraft.player.getName())) {
                    string = ChatFormatting.RED + "\u0417\u0430\u0449\u0438\u0442\u0430 \u0430\u0440\u0433\u0438\u043d\u0442\u043e\u0437\u0430";
                    break Label_0254;
                }
            }
            string = ((Minced.getInstance().manager.getModule(StreamerMode.class).state && StreamerMode.youtuber.get()) ? (ChatFormatting.GREEN + "\u041d\u0435\u043c\u0443 \u043f\u0440\u0430\u0442\u0435\u043a\u0442") : ChatFormatting.stripFormatting(TargetHUD.curTarget.getName()));
        }
        final String name = string;
        if (this.thudMode.is("124125125 2")) {
            final float width = 90.0f;
            final float height = 30.0f;
            this.targetHUDDrag.setWidth(width);
            this.targetHUDDrag.setHeight(height);
            this.posX = (int)this.targetHUDDrag.getX();
            this.posY = (int)this.targetHUDDrag.getY();
            this.hp = MathUtility.clamp(MathUtility.lerp(this.hp, TargetHUD.curTarget.getHealth() / TargetHUD.curTarget.getMaxHealth(), (float)(12.0 * AnimationMath.deltaTime())), 0.0f, 1.0f);
            GlStateManager.pushMatrix();
            Minced.getInstance().scaleMath.pushScale();
            GlStateManager.translate(this.posX + width / 2.0f, (float)(this.posY + 21), 0.0f);
            GlStateManager.scale(this.scale, this.scale, 1.0);
            GlStateManager.translate(-this.posX - width / 2.0f, (float)(-this.posY - 21), 0.0f);
            GlowUtility.drawGlow((float)this.posX, (float)this.posY, width, height, 15, new Color(8, 8, 8, 100));
            RenderUtility.drawRound((float)this.posX, (float)this.posY, width, height, 5.0f, new Color(8, 8, 8));
            RenderUtility.drawFace((float)(this.posX + 5), (float)(this.posY + 4), 8.0f, 8.0f, 8, 8, 22, 22, 64.0f, 64.0f, (AbstractClientPlayer)TargetHUD.curTarget);
            RenderUtility.drawRound((float)(this.posX + 2), (float)(this.posY + 29), width + 16.0f, 5.0f, 1.0f, new Color(121, 121, 121, 64));
            RenderUtility.drawGradientRound((float)(this.posX + 3), (float)(this.posY + 30), (width + 18.0f) * this.hp - 4.0f, 3.0f, 1.0f, ColorUtility.applyOpacity(Hud.getColor(270), 0.85f), Hud.getColor(0), Hud.getColor(180), Hud.getColor(90));
            Fonts.Nunito14.drawString(name, (float)(this.posX + 30), (float)(this.posY + 10), -1);
            if (!TargetHUD.curTarget.getHeldItem(EnumHand.OFF_HAND).isEmpty()) {
                GlowUtility.drawGlow(this.posX + width + 7.0f, (float)(this.posY + 5), 18.0f, 20.0f, 14, new Color(8, 8, 8, 100));
                RenderUtility.drawRound(this.posX + width + 7.0f, (float)(this.posY + 5), 18.0f, 20.0f, 5.0f, new Color(8, 8, 8));
                final float pox = (TargetHUD.curTarget.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof ItemAppleGold) ? (this.posX + width + 7.0f) : (this.posX + width + 9.0f);
                GlStateManager.enableTexture2D();
                GlStateManager.depthMask(true);
                GlStateManager.clear(256);
                GlStateManager.enableDepth();
                GlStateManager.disableAlpha();
                TargetHUD.mc.getRenderItem().zLevel = -150.0f;
                RenderHelper.enableStandardItemLighting();
                TargetHUD.mc.getRenderItem().renderItemAndEffectIntoGUI(TargetHUD.curTarget.getHeldItem(EnumHand.OFF_HAND), (int)pox, this.posY + 6);
                TargetHUD.mc.getRenderItem().renderItemOverlays(TargetHUD.mc.fontRenderer, TargetHUD.curTarget.getHeldItem(EnumHand.OFF_HAND), (int)pox, this.posY + 6.5f);
                RenderHelper.disableStandardItemLighting();
                TargetHUD.mc.getRenderItem().zLevel = 0.0f;
                GlStateManager.enableAlpha();
            }
            Minced.getInstance().scaleMath.popScale();
            GlStateManager.popMatrix();
        }
        if (this.thudMode.is("Type 1")) {
            final float width = 80.0f;
            final float height = 30.0f;
            this.targetHUDDrag.setWidth(width + 20.0f);
            this.targetHUDDrag.setHeight(height);
            this.posX = (int)this.targetHUDDrag.getX();
            this.posY = (int)this.targetHUDDrag.getY();
            final float x2 = (float)this.posX;
            this.hp = MathUtility.clamp(MathUtility.lerp(this.hp, TargetHUD.curTarget.getHealth() / TargetHUD.curTarget.getMaxHealth(), (float)(12.0 * AnimationMath.deltaTime())), 0.0f, 1.0f);
            GlStateManager.pushMatrix();
            Minced.getInstance().scaleMath.pushScale();
            GlStateManager.translate(this.posX + width / 2.0f, (float)(this.posY + 21), 0.0f);
            GlStateManager.scale(this.scale, this.scale, 1.0);
            GlStateManager.translate(-this.posX - width / 2.0f, (float)(-this.posY - 21), 0.0f);
            if (Hud.arrayListElements.get(0)) {
                GlowUtility.drawGlow((float)this.posX, (float)this.posY, width + 20.0f, 36.5f, 10, ColorUtility.applyOpacity(new Color(Hud.getColor(28).getRGB()), 255));
                RenderUtility.drawRound((float)this.posX, (float)this.posY, width + 20.0f, 36.5f, 2.0f, new Color(21, 20, 22, 255));
            }
            RenderUtility.drawRound((float)this.posX, (float)this.posY, width + 20.0f, 36.5f, 2.0f, new Color(21, 21, 21, 200));
            RenderUtility.drawRect(this.posX + 2, this.posY + 2, 24.0, 24.0, Color.BLACK.getRGB());
            RenderUtility.drawFace((float)(this.posX + 2), (float)(this.posY + 2), 8.0f, 8.0f, 8, 8, 24, 24, 64.0f, 64.0f, (AbstractClientPlayer)TargetHUD.curTarget);
            Fonts.Nunito14.drawString(ChatFormatting.WHITE + name, (float)(this.posX + 28), (float)(this.posY + 4), -1);
            final FontRenderer nunito12 = Fonts.Nunito12;
            final StringBuilder append = new StringBuilder().append(ChatFormatting.WHITE).append("Health: ").append(ChatFormatting.GREEN).append(MathUtility.round(this.hp * 20.0f, 0.10000000149011612)).append(ChatFormatting.GRAY).append("/").append(ChatFormatting.WHITE).append("Dist: ").append(ChatFormatting.RED);
            final EntityLivingBase curTarget = TargetHUD.curTarget;
            final Minecraft mc4 = TargetHUD.mc;
            nunito12.drawString(append.append(MathUtility.round(curTarget.getDistance(Minecraft.player), 0.10000000149011612)).toString(), (float)(this.posX + 28), (float)(this.posY + 12), -1);
            drawItemTargetHUD((EntityPlayer)TargetHUD.curTarget, (float)this.posX, (float)this.posY, x2);
            RenderUtility.drawRound((float)(this.posX + 2), (float)(this.posY + 29), width + 16.0f, 5.0f, 1.0f, new Color(121, 121, 121, 64));
            RenderUtility.drawGradientRound((float)(this.posX + 3), (float)(this.posY + 30), (width + 18.0f) * this.hp - 4.0f, 3.0f, 1.0f, ColorUtility.applyOpacity(Hud.getColor(270), 0.85f), Hud.getColor(0), Hud.getColor(180), Hud.getColor(90));
            Minced.getInstance().scaleMath.popScale();
            GlStateManager.popMatrix();
        }
    }
    
    public static void drawItemTargetHUD(final EntityPlayer player, final float posX, final float posY, float x2) {
        final List<ItemStack> list = new ArrayList<ItemStack>(Collections.singletonList(player.getHeldItemMainhand()));
        final float pox = (TargetHUD.curTarget.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof ItemAppleGold) ? posX : (posX + 9.0f);
        for (int i = 1; i < 5; ++i) {
            final ItemStack getEquipmentInSlot = player.getEquipmentInSlot(i);
            list.add(getEquipmentInSlot);
        }
        for (final ItemStack itemStack : list) {
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.pushMatrix();
            GlStateManager.translate(posX, posY, 1.0f);
            GlStateManager.scale(0.6f, 0.6f, 0.6f);
            GlStateManager.translate(-posX - 5.0f, -posY + 23.0f, 1.0f);
            renderItem(itemStack, (int)x2 + 65, (int)(posY + 5.0f));
            GlStateManager.popMatrix();
            x2 += 18.0f;
        }
    }
    
    public static void renderItem(final ItemStack itemStack, final int x, final int y) {
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
        GlStateManager.clear(256);
        GlStateManager.enableDepth();
        GlStateManager.disableAlpha();
        TargetHUD.mc.getRenderItem().zLevel = -150.0f;
        RenderHelper.enableStandardItemLighting();
        TargetHUD.mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, x, y);
        TargetHUD.mc.getRenderItem().renderItemOverlays(TargetHUD.mc.fontRenderer, itemStack, x, y);
        RenderHelper.disableStandardItemLighting();
        TargetHUD.mc.getRenderItem().zLevel = 0.0f;
        GlStateManager.enableAlpha();
    }
    
    static {
        TargetHUD.curTarget = null;
    }
}
