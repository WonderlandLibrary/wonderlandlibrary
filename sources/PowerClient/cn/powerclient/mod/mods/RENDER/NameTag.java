/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.mod.mods.RENDER;

import com.darkmagician6.eventapi.EventTarget;
import com.google.common.collect.Ordering;
import com.mojang.authlib.GameProfile;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import me.AveReborn.Value;
import me.AveReborn.events.EventRender;
import me.AveReborn.mod.Category;
import me.AveReborn.mod.Mod;
import me.AveReborn.mod.mods.COMBAT.Antibot;
import me.AveReborn.mod.mods.MISC.Teams;
import me.AveReborn.util.GLUtil;
import me.AveReborn.util.RenderUtil;
import me.AveReborn.util.timeUtils.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Timer;
import org.lwjgl.opengl.GL11;

public class NameTag
extends Mod {
    public static ArrayList<Entity> invalid = new ArrayList();
    public Value<Boolean> remove = new Value<Boolean>("NameTag_Remove", false);
    public TimeHelper timer = new TimeHelper();

    public NameTag() {
        super("NameTag", Category.RENDER);
    }

    @EventTarget
    public void onRender(EventRender event) {
        for (Object o2 : this.mc.theWorld.playerEntities) {
            EntityPlayer p2 = (EntityPlayer)o2;
            if (p2 == Minecraft.thePlayer || !p2.isEntityAlive()) continue;
            double pX = p2.lastTickPosX + (p2.posX - p2.lastTickPosX) * (double)this.mc.timer.renderPartialTicks - RenderManager.renderPosX;
            double pY = p2.lastTickPosY + (p2.posY - p2.lastTickPosY) * (double)this.mc.timer.renderPartialTicks - RenderManager.renderPosY;
            double pZ = p2.lastTickPosZ + (p2.posZ - p2.lastTickPosZ) * (double)this.mc.timer.renderPartialTicks - RenderManager.renderPosZ;
            this.renderNameTag(p2, p2.getName(), pX, pY, pZ);
        }
        for (Object obj : this.mc.theWorld.loadedEntityList) {
            EntityPlayer ent = (EntityPlayer)obj;
            String str = ent.getDisplayName().getFormattedText();
            if (ent.getDistanceToEntity(Minecraft.thePlayer) > 100.0f || ent == Minecraft.thePlayer || invalid.contains(ent) || ent.isDead || this.getTabPlayerList().contains(ent)) continue;
            if (this.remove.getValueState().booleanValue()) {
                this.mc.theWorld.removeEntity(ent);
                this.timer.reset();
                continue;
            }
            invalid.add(ent);
            this.timer.reset();
        }
    }

    public List<EntityPlayer> getTabPlayerList() {
        NetHandlerPlayClient nhpc = Minecraft.thePlayer.sendQueue;
        ArrayList<EntityPlayer> list = new ArrayList<EntityPlayer>();
        new net.minecraft.client.gui.GuiPlayerTabOverlay(this.mc, this.mc.ingameGUI);
        List<NetworkPlayerInfo> players = GuiPlayerTabOverlay.field_175252_a.sortedCopy(nhpc.getPlayerInfoMap());
        for (NetworkPlayerInfo o2 : players) {
            NetworkPlayerInfo info = o2;
            if (info == null) continue;
            list.add(this.mc.theWorld.getPlayerEntityByName(info.getGameProfile().getName()));
        }
        return list;
    }

    private void renderNameTag(EntityPlayer entity, String tag, double pX, double pY, double pZ) {
        FontRenderer fr2 = this.mc.fontRendererObj;
        float size = Minecraft.thePlayer.getDistanceToEntity(entity) / 6.0f;
        if (size < 1.1f) {
            size = 1.1f;
        }
        float scale = size * 2.0f;
        tag = entity.getName();
        String bot2 = "";
        bot2 = Antibot.invalid.contains(entity) ? "\u00a79" : "";
        String team = "";
        team = Teams.isOnSameTeam(entity) ? "\u00a7a" : "";
        String lol = String.valueOf(team) + bot2 + tag;
        double plyHeal = entity.getHealth();
        String hp2 = "Health:" + (int)plyHeal + " Dist:" + (int)entity.getDistanceToEntity(Minecraft.thePlayer) + "m";
        GL11.glPushMatrix();
        GL11.glTranslatef((float)pX, (float)(pY += entity.isSneaking() ? 0.5 : 0.7) + 1.4f, (float)pZ);
        GL11.glNormal3f(0.0f, 1.0f, 0.0f);
        GL11.glRotatef(- RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(RenderManager.playerViewX, 1.0f, 0.0f, 0.0f);
        GL11.glScalef(- scale, - scale, scale /= 100.0f);
        GLUtil.setGLCap(2896, false);
        GLUtil.setGLCap(2929, false);
        int width = 31;
        GLUtil.setGLCap(3042, true);
        GL11.glBlendFunc(770, 771);
        this.drawBorderedRectNameTag(- width - 16, -17.5f, width + 10, -0.0f, 1.0f, 1426063360, Integer.MIN_VALUE);
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        fr2.drawString(lol, - width - 15, -17, -1);
        GL11.glScaled(0.6000000238418579, 0.6000000238418579, 0.6000000238418579);
        fr2.drawString(hp2, - width - 46, (int)(-19.0f - (float)fr2.FONT_HEIGHT + 15.0f), -1);
        GL11.glScaled(1.0, 1.0, 1.0);
        int COLOR = new Color(188, 0, 0).getRGB();
        if (entity.getHealth() > 20.0f) {
            COLOR = -65292;
        }
        int xLeft = (- width) / 2 - 66;
        RenderUtil.drawRect((float)(- width) - 50.0f + 149.2f * Math.min(1.0f, entity.getHealth() / 20.0f), 0.0f, (float)((- width) / 2) - 63.2f, -3.0f, COLOR);
        GL11.glPushMatrix();
        GL11.glScaled(1.5, 1.5, 1.5);
        int xOffset = 0;
        ItemStack[] arritemStack = entity.inventory.armorInventory;
        int n2 = arritemStack.length;
        int n3 = 0;
        while (n3 < n2) {
            ItemStack armourStack = arritemStack[n3];
            if (armourStack != null) {
                xOffset -= 11;
            }
            ++n3;
        }
        if (entity.getHeldItem() != null) {
            xOffset -= 8;
            ItemStack renderStack = entity.getHeldItem().copy();
            if (renderStack.hasEffect() && (renderStack.getItem() instanceof ItemTool || renderStack.getItem() instanceof ItemArmor)) {
                renderStack.stackSize = 1;
            }
            this.renderItemStack(renderStack, xOffset, -37);
            xOffset += 20;
        }
        ItemStack[] arritemStack2 = entity.inventory.armorInventory;
        int n4 = arritemStack2.length;
        n2 = 0;
        while (n2 < n4) {
            ItemStack armourStack = arritemStack2[n2];
            if (armourStack != null) {
                ItemStack renderStack1 = armourStack.copy();
                if (renderStack1.hasEffect() && (renderStack1.getItem() instanceof ItemTool || renderStack1.getItem() instanceof ItemArmor)) {
                    renderStack1.stackSize = 1;
                }
                this.renderItemStack(renderStack1, xOffset, -36);
                xOffset += 20;
            }
            ++n2;
        }
        GL11.glPopMatrix();
        GLUtil.revertAllCaps();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }

    public void renderItemStack(ItemStack stack, int x2, int y2) {
        GL11.glPushMatrix();
        GL11.glDepthMask(true);
        GlStateManager.clear(256);
        RenderHelper.enableStandardItemLighting();
        this.mc.getRenderItem().zLevel = -150.0f;
        this.whatTheFuckOpenGLThisFixesItemGlint();
        this.mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x2, y2);
        this.mc.getRenderItem().renderItemOverlays(this.mc.fontRendererObj, stack, x2, y2);
        this.mc.getRenderItem().zLevel = 0.0f;
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableCull();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        GlStateManager.scale(0.5, 0.5, 0.5);
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GL11.glPopMatrix();
    }

    private void whatTheFuckOpenGLThisFixesItemGlint() {
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
    }

    public void drawBorderedRectNameTag(float x2, float y2, float x22, float y22, float l1, int col1, int col2) {
        RenderUtil.drawRect(x2, y2, x22, y22, col2);
        float f2 = (float)(col1 >> 24 & 255) / 255.0f;
        float f22 = (float)(col1 >> 16 & 255) / 255.0f;
        float f3 = (float)(col1 >> 8 & 255) / 255.0f;
        float f4 = (float)(col1 & 255) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glColor4f(f22, f3, f4, f2);
        GL11.glLineWidth(l1);
        GL11.glBegin(1);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x2, y22);
        GL11.glVertex2d(x22, y22);
        GL11.glVertex2d(x22, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x22, y2);
        GL11.glVertex2d(x2, y22);
        GL11.glVertex2d(x22, y22);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }
}

