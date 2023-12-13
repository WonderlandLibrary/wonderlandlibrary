// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.guirewrite.elements;

import net.minecraft.client.Minecraft;
import java.util.Iterator;
import com.elementars.eclient.util.ColourHolder;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.Entity;
import com.elementars.eclient.module.combat.PopCounter;
import com.elementars.eclient.command.Command;
import com.mojang.realmsclient.gui.ChatFormatting;
import com.elementars.eclient.Xulu;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.gui.Gui;
import com.elementars.eclient.util.ColorUtils;
import java.awt.Color;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import com.elementars.eclient.util.Pair;
import java.util.Arrays;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Blocks;
import java.util.List;
import java.util.Comparator;
import java.util.Collection;
import java.util.ArrayList;
import com.elementars.eclient.module.Module;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import dev.xulu.settings.Value;
import com.elementars.eclient.guirewrite.Element;

public class Target extends Element
{
    private final Value<Boolean> cf;
    private EntityPlayer target;
    private static RenderItem itemRender;
    BlockPos NORTH;
    BlockPos EAST;
    BlockPos SOUTH;
    BlockPos WEST;
    
    public Target() {
        super("Target");
        this.cf = this.register(new Value<Boolean>("Custom Font", this, false));
        this.NORTH = new BlockPos(0, 0, -1);
        this.EAST = new BlockPos(1, 0, 0);
        this.SOUTH = new BlockPos(0, 0, 1);
        this.WEST = new BlockPos(-1, 0, 0);
    }
    
    @Override
    public void onEnable() {
        this.width = 200.0;
        this.height = 100.0;
        super.onEnable();
    }
    
    @Override
    public void onUpdate() {
        if (Target.mc.player == null) {
            return;
        }
        final List<EntityPlayer> players = new ArrayList<EntityPlayer>(Target.mc.world.playerEntities);
        players.removeIf(player -> player == Target.mc.player);
        players.sort(Comparator.comparing(entityPlayer -> Target.mc.player.getDistance(entityPlayer)));
        if (!players.isEmpty()) {
            this.target = players.get(0);
        }
        else {
            this.target = null;
        }
        super.onUpdate();
    }
    
    public String getPing(final float ping) {
        if (ping > 200.0f) {
            return "c";
        }
        if (ping > 100.0f) {
            return "e";
        }
        return "a";
    }
    
    private String getDistance(final double distance) {
        if (distance < 15.0) {
            return "c";
        }
        return "a";
    }
    
    private boolean isBrockOrObby(final BlockPos pos) {
        return Target.mc.world.getBlockState(pos).getBlock() == Blocks.BEDROCK || Target.mc.world.getBlockState(pos).getBlock() == Blocks.OBSIDIAN;
    }
    
    private List<ItemStack> getNorth(final EntityPlayer player) {
        final BlockPos playerPos = new BlockPos(player.posX, player.posY, player.posZ);
        final List<ItemStack> items = new ArrayList<ItemStack>(Arrays.asList(this.isBrockOrObby(playerPos.add(this.NORTH.x, this.NORTH.y, this.NORTH.z)) ? new ItemStack(Target.mc.world.getBlockState(playerPos.add(this.NORTH.x, this.NORTH.y, this.NORTH.z)).getBlock()) : new ItemStack(Items.AIR), this.isBrockOrObby(playerPos.add(this.EAST.x, this.EAST.y, this.EAST.z)) ? new ItemStack(Target.mc.world.getBlockState(playerPos.add(this.EAST.x, this.EAST.y, this.EAST.z)).getBlock()) : new ItemStack(Items.AIR), this.isBrockOrObby(playerPos.add(this.SOUTH.x, this.SOUTH.y, this.SOUTH.z)) ? new ItemStack(Target.mc.world.getBlockState(playerPos.add(this.SOUTH.x, this.SOUTH.y, this.SOUTH.z)).getBlock()) : new ItemStack(Items.AIR), this.isBrockOrObby(playerPos.add(this.WEST.x, this.WEST.y, this.WEST.z)) ? new ItemStack(Target.mc.world.getBlockState(playerPos.add(this.WEST.x, this.WEST.y, this.WEST.z)).getBlock()) : new ItemStack(Items.AIR)));
        return items;
    }
    
    private void itemrender(final List<ItemStack> items, final int x, final int y) {
        final ArrayList<Pair<Integer, Integer>> coordinates = new ArrayList<Pair<Integer, Integer>>((Collection<? extends Pair<Integer, Integer>>)Arrays.asList(new Pair((T)(x + 16), (S)y), new Pair((T)(x + 32), (S)(y + 16)), new Pair((T)(x + 16), (S)(y + 32)), new Pair((T)x, (S)(y + 16))));
        for (int item = 0; item < 4; ++item) {
            preitemrender();
            Target.mc.getRenderItem().renderItemAndEffectIntoGUI((ItemStack)items.get(item), (int)coordinates.get(item).getKey(), (int)coordinates.get(item).getValue());
            postitemrender();
        }
    }
    
    private static void preitemrender() {
        GL11.glPushMatrix();
        GL11.glDepthMask(true);
        GlStateManager.clear(256);
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.scale(1.0f, 1.0f, 0.01f);
    }
    
    private static void postitemrender() {
        GlStateManager.scale(1.0f, 1.0f, 1.0f);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        GlStateManager.scale(0.5, 0.5, 0.5);
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GL11.glPopMatrix();
    }
    
    @Override
    public void onRender() {
        if (Target.mc.player == null || Target.mc.world == null) {
            return;
        }
        if (this.target != null) {
            Gui.drawRect((int)this.x, (int)this.y, (int)this.x + (int)this.width, (int)this.y + (int)this.height, ColorUtils.changeAlpha(Color.BLACK.getRGB(), 50));
            GlStateManager.pushMatrix();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            try {
                GuiInventory.drawEntityOnScreen((int)this.x + 30, (int)this.y + 90, 45, 0.0f, 0.0f, (EntityLivingBase)this.target);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            GlStateManager.popMatrix();
            if (this.cf.getValue()) {
                Xulu.cFontRenderer.drawStringWithShadow(this.target.getName(), this.x + 62.0, this.y + 3.0, -1);
                Xulu.cFontRenderer.drawStringWithShadow(((Target.mc.getConnection() == null || Target.mc.getConnection().getPlayerInfo(this.target.entityUniqueID) == null) ? (ChatFormatting.RED + "-1") : (Command.SECTIONSIGN() + this.getPing((float)Target.mc.getConnection().getPlayerInfo(this.target.entityUniqueID).getResponseTime()) + Target.mc.getConnection().getPlayerInfo(this.target.entityUniqueID).getResponseTime())) + "ms", this.x + 62.0, this.y + 18.0, -1);
                Xulu.cFontRenderer.drawStringWithShadow(PopCounter.INSTANCE.popMap.containsKey(this.target) ? (ChatFormatting.GREEN + "" + PopCounter.INSTANCE.popMap.get(this.target) + " Pops") : (ChatFormatting.RED + "0 Pops"), this.x + 62.0, this.y + 33.0, -1);
                Xulu.cFontRenderer.drawStringWithShadow(Command.SECTIONSIGN() + this.getDistance(Target.mc.player.getDistance((Entity)this.target)) + (int)Target.mc.player.getDistance((Entity)this.target) + " blocks away", this.x + 62.0, this.y + 48.0, -1);
            }
            else {
                Target.fontRenderer.drawStringWithShadow(this.target.getName(), (float)this.x + 62.0f, (float)this.y + 3.0f, -1);
                Target.fontRenderer.drawStringWithShadow(((Target.mc.getConnection() == null || Target.mc.getConnection().getPlayerInfo(this.target.entityUniqueID) == null) ? (ChatFormatting.RED + "-1") : (Command.SECTIONSIGN() + this.getPing((float)Target.mc.getConnection().getPlayerInfo(this.target.entityUniqueID).getResponseTime()) + Target.mc.getConnection().getPlayerInfo(this.target.entityUniqueID).getResponseTime())) + "ms", (float)this.x + 62.0f, (float)this.y + 18.0f, -1);
                Target.fontRenderer.drawStringWithShadow(PopCounter.INSTANCE.popMap.containsKey(this.target) ? (ChatFormatting.GREEN + "" + PopCounter.INSTANCE.popMap.get(this.target) + " Pops") : (ChatFormatting.RED + "0 Pops"), (float)this.x + 62.0f, (float)this.y + 33.0f, -1);
                Target.fontRenderer.drawStringWithShadow(Command.SECTIONSIGN() + this.getDistance(Target.mc.player.getDistance((Entity)this.target)) + (int)Target.mc.player.getDistance((Entity)this.target) + " blocks away", (float)((int)this.x + 62), (float)((int)this.y + 48), -1);
            }
            final float health = (float)MathHelper.clamp(MathHelper.ceil(this.target.getHealth()), 0, 20);
            final float percentBar = (20.0f - health) / 20.0f;
            final float red = 1.0f - percentBar;
            Gui.drawRect((int)this.x, (int)this.y + (int)this.height - 3, (int)(this.x + red * this.width), (int)this.y + (int)this.height, ColorUtils.changeAlpha(ColourHolder.toHex((int)(percentBar * 255.0f), (int)(red * 255.0f), 0), 200));
            this.itemrender(this.getNorth(this.target), (int)this.x + (int)this.width - 52, (int)this.y + 4);
            GlStateManager.pushMatrix();
            GlStateManager.enableTexture2D();
            int iteration = 0;
            for (final ItemStack is : this.target.inventory.armorInventory) {
                ++iteration;
                if (is.isEmpty()) {
                    continue;
                }
                final int x_2 = (int)this.x - 90 + (9 - iteration) * 20 + 2 - 12 + 60;
                final int y_2 = (int)this.y + (int)this.height - 24;
                GlStateManager.enableDepth();
                Target.itemRender.zLevel = 200.0f;
                Target.itemRender.renderItemAndEffectIntoGUI(is, x_2, y_2);
                Target.itemRender.renderItemOverlayIntoGUI(Target.mc.fontRenderer, is, x_2, y_2, "");
                Target.itemRender.zLevel = 0.0f;
                GlStateManager.enableTexture2D();
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                final String s2 = (is.getCount() > 1) ? (is.getCount() + "") : "";
                Target.mc.fontRenderer.drawStringWithShadow(s2, (float)(x_2 + 19 - 2 - Target.mc.fontRenderer.getStringWidth(s2)), (float)((int)this.y + 9), 16777215);
                final float green = (is.getMaxDamage() - (float)is.getItemDamage()) / is.getMaxDamage();
                final float red2 = 1.0f - green;
                final int dmg = 100 - (int)(red2 * 100.0f);
                if (this.cf.getValue()) {
                    Xulu.cFontRenderer.drawStringWithShadow(dmg + "", x_2 + 8 - Xulu.cFontRenderer.getStringWidth(dmg + "") / 2, y_2 - 11, ColourHolder.toHex((int)(red2 * 255.0f), (int)(green * 255.0f), 0));
                }
                else {
                    Target.fontRenderer.drawStringWithShadow(dmg + "", (float)(x_2 + 9 - Target.fontRenderer.getStringWidth(dmg + "") / 2), (float)(y_2 - 11), ColourHolder.toHex((int)(red2 * 255.0f), (int)(green * 255.0f), 0));
                }
            }
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
            GlStateManager.popMatrix();
        }
    }
    
    static {
        Target.itemRender = Minecraft.getMinecraft().getRenderItem();
    }
}
