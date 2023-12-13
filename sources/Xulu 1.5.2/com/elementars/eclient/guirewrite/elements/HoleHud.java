// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.guirewrite.elements;

import com.elementars.eclient.util.Pair;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.init.Blocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import net.minecraft.util.math.BlockPos;
import com.elementars.eclient.guirewrite.Element;

public class HoleHud extends Element
{
    BlockPos NORTH;
    BlockPos EAST;
    BlockPos SOUTH;
    BlockPos WEST;
    
    public HoleHud() {
        super("HoleHud");
        this.NORTH = new BlockPos(0, 0, -1);
        this.EAST = new BlockPos(1, 0, 0);
        this.SOUTH = new BlockPos(0, 0, 1);
        this.WEST = new BlockPos(-1, 0, 0);
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
    public void onEnable() {
        this.width = 48.0;
        this.height = 48.0;
        super.onEnable();
    }
    
    @Override
    public void onRender() {
        if (HoleHud.mc.player == null || HoleHud.mc.world == null) {
            return;
        }
        switch (HoleHud.mc.getRenderViewEntity().getHorizontalFacing()) {
            case NORTH: {
                this.itemrender(this.getNorth(), (int)this.x, (int)this.y);
                break;
            }
            case EAST: {
                this.itemrender(this.getEast(), (int)this.x, (int)this.y);
                break;
            }
            case SOUTH: {
                this.itemrender(this.getSouth(), (int)this.x, (int)this.y);
                break;
            }
            case WEST: {
                this.itemrender(this.getWest(), (int)this.x, (int)this.y);
                break;
            }
        }
    }
    
    private boolean isBrockOrObby(final BlockPos pos) {
        return HoleHud.mc.world.getBlockState(pos).getBlock() == Blocks.BEDROCK || HoleHud.mc.world.getBlockState(pos).getBlock() == Blocks.OBSIDIAN;
    }
    
    private List<ItemStack> getEast() {
        final BlockPos playerPos = new BlockPos(HoleHud.mc.player.posX, HoleHud.mc.player.posY, HoleHud.mc.player.posZ);
        final List<ItemStack> items = new ArrayList<ItemStack>(Arrays.asList(this.isBrockOrObby(playerPos.add(this.EAST.x, this.EAST.y, this.EAST.z)) ? new ItemStack(HoleHud.mc.world.getBlockState(playerPos.add(this.EAST.x, this.EAST.y, this.EAST.z)).getBlock()) : new ItemStack(Items.AIR), this.isBrockOrObby(playerPos.add(this.SOUTH.x, this.SOUTH.y, this.SOUTH.z)) ? new ItemStack(HoleHud.mc.world.getBlockState(playerPos.add(this.SOUTH.x, this.SOUTH.y, this.SOUTH.z)).getBlock()) : new ItemStack(Items.AIR), this.isBrockOrObby(playerPos.add(this.WEST.x, this.WEST.y, this.WEST.z)) ? new ItemStack(HoleHud.mc.world.getBlockState(playerPos.add(this.WEST.x, this.WEST.y, this.WEST.z)).getBlock()) : new ItemStack(Items.AIR), this.isBrockOrObby(playerPos.add(this.NORTH.x, this.NORTH.y, this.NORTH.z)) ? new ItemStack(HoleHud.mc.world.getBlockState(playerPos.add(this.NORTH.x, this.NORTH.y, this.NORTH.z)).getBlock()) : new ItemStack(Items.AIR)));
        return items;
    }
    
    private List<ItemStack> getSouth() {
        final BlockPos playerPos = new BlockPos(HoleHud.mc.player.posX, HoleHud.mc.player.posY, HoleHud.mc.player.posZ);
        final List<ItemStack> items = new ArrayList<ItemStack>(Arrays.asList(this.isBrockOrObby(playerPos.add(this.SOUTH.x, this.SOUTH.y, this.SOUTH.z)) ? new ItemStack(HoleHud.mc.world.getBlockState(playerPos.add(this.SOUTH.x, this.SOUTH.y, this.SOUTH.z)).getBlock()) : new ItemStack(Items.AIR), this.isBrockOrObby(playerPos.add(this.WEST.x, this.WEST.y, this.WEST.z)) ? new ItemStack(HoleHud.mc.world.getBlockState(playerPos.add(this.WEST.x, this.WEST.y, this.WEST.z)).getBlock()) : new ItemStack(Items.AIR), this.isBrockOrObby(playerPos.add(this.NORTH.x, this.NORTH.y, this.NORTH.z)) ? new ItemStack(HoleHud.mc.world.getBlockState(playerPos.add(this.NORTH.x, this.NORTH.y, this.NORTH.z)).getBlock()) : new ItemStack(Items.AIR), this.isBrockOrObby(playerPos.add(this.EAST.x, this.EAST.y, this.EAST.z)) ? new ItemStack(HoleHud.mc.world.getBlockState(playerPos.add(this.EAST.x, this.EAST.y, this.EAST.z)).getBlock()) : new ItemStack(Items.AIR)));
        return items;
    }
    
    private List<ItemStack> getWest() {
        final BlockPos playerPos = new BlockPos(HoleHud.mc.player.posX, HoleHud.mc.player.posY, HoleHud.mc.player.posZ);
        final List<ItemStack> items = new ArrayList<ItemStack>(Arrays.asList(this.isBrockOrObby(playerPos.add(this.WEST.x, this.WEST.y, this.WEST.z)) ? new ItemStack(HoleHud.mc.world.getBlockState(playerPos.add(this.WEST.x, this.WEST.y, this.WEST.z)).getBlock()) : new ItemStack(Items.AIR), this.isBrockOrObby(playerPos.add(this.NORTH.x, this.NORTH.y, this.NORTH.z)) ? new ItemStack(HoleHud.mc.world.getBlockState(playerPos.add(this.NORTH.x, this.NORTH.y, this.NORTH.z)).getBlock()) : new ItemStack(Items.AIR), this.isBrockOrObby(playerPos.add(this.EAST.x, this.EAST.y, this.EAST.z)) ? new ItemStack(HoleHud.mc.world.getBlockState(playerPos.add(this.EAST.x, this.EAST.y, this.EAST.z)).getBlock()) : new ItemStack(Items.AIR), this.isBrockOrObby(playerPos.add(this.SOUTH.x, this.SOUTH.y, this.SOUTH.z)) ? new ItemStack(HoleHud.mc.world.getBlockState(playerPos.add(this.SOUTH.x, this.SOUTH.y, this.SOUTH.z)).getBlock()) : new ItemStack(Items.AIR)));
        return items;
    }
    
    private List<ItemStack> getNorth() {
        final BlockPos playerPos = new BlockPos(HoleHud.mc.player.posX, HoleHud.mc.player.posY, HoleHud.mc.player.posZ);
        final List<ItemStack> items = new ArrayList<ItemStack>(Arrays.asList(this.isBrockOrObby(playerPos.add(this.NORTH.x, this.NORTH.y, this.NORTH.z)) ? new ItemStack(HoleHud.mc.world.getBlockState(playerPos.add(this.NORTH.x, this.NORTH.y, this.NORTH.z)).getBlock()) : new ItemStack(Items.AIR), this.isBrockOrObby(playerPos.add(this.EAST.x, this.EAST.y, this.EAST.z)) ? new ItemStack(HoleHud.mc.world.getBlockState(playerPos.add(this.EAST.x, this.EAST.y, this.EAST.z)).getBlock()) : new ItemStack(Items.AIR), this.isBrockOrObby(playerPos.add(this.SOUTH.x, this.SOUTH.y, this.SOUTH.z)) ? new ItemStack(HoleHud.mc.world.getBlockState(playerPos.add(this.SOUTH.x, this.SOUTH.y, this.SOUTH.z)).getBlock()) : new ItemStack(Items.AIR), this.isBrockOrObby(playerPos.add(this.WEST.x, this.WEST.y, this.WEST.z)) ? new ItemStack(HoleHud.mc.world.getBlockState(playerPos.add(this.WEST.x, this.WEST.y, this.WEST.z)).getBlock()) : new ItemStack(Items.AIR)));
        return items;
    }
    
    private void itemrender(final List<ItemStack> items, final int x, final int y) {
        final ArrayList<Pair<Integer, Integer>> coordinates = new ArrayList<Pair<Integer, Integer>>((Collection<? extends Pair<Integer, Integer>>)Arrays.asList(new Pair((T)(x + 16), (S)y), new Pair((T)(x + 32), (S)(y + 16)), new Pair((T)(x + 16), (S)(y + 32)), new Pair((T)x, (S)(y + 16))));
        for (int item = 0; item < 4; ++item) {
            preitemrender();
            InvPreview.mc.getRenderItem().renderItemAndEffectIntoGUI((ItemStack)items.get(item), (int)coordinates.get(item).getKey(), (int)coordinates.get(item).getValue());
            postitemrender();
        }
    }
}
