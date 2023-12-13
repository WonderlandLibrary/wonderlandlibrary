// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.render;

import com.elementars.eclient.event.EventTarget;
import com.elementars.eclient.event.events.EventRenderBlock;
import com.elementars.eclient.module.Category;
import net.minecraft.block.Block;
import java.util.ArrayList;
import com.elementars.eclient.module.Module;

public class Xray extends Module
{
    private static final ArrayList<Block> BLOCKS;
    public static Xray INSTANCE;
    
    public Xray() {
        super("Xray", "See through blocks!", 0, Category.RENDER, true);
        initblocks();
        Xray.INSTANCE = this;
    }
    
    public static void initblocks() {
        Xray.BLOCKS.add(Block.getBlockFromName("coal_ore"));
        Xray.BLOCKS.add(Block.getBlockFromName("iron_ore"));
        Xray.BLOCKS.add(Block.getBlockFromName("gold_ore"));
        Xray.BLOCKS.add(Block.getBlockFromName("redstone_ore"));
        Xray.BLOCKS.add(Block.getBlockById(74));
        Xray.BLOCKS.add(Block.getBlockFromName("lapis_ore"));
        Xray.BLOCKS.add(Block.getBlockFromName("diamond_ore"));
        Xray.BLOCKS.add(Block.getBlockFromName("emerald_ore"));
        Xray.BLOCKS.add(Block.getBlockFromName("quartz_ore"));
        Xray.BLOCKS.add(Block.getBlockFromName("clay"));
        Xray.BLOCKS.add(Block.getBlockFromName("glowstone"));
        Xray.BLOCKS.add(Block.getBlockById(8));
        Xray.BLOCKS.add(Block.getBlockById(9));
        Xray.BLOCKS.add(Block.getBlockById(10));
        Xray.BLOCKS.add(Block.getBlockById(11));
        Xray.BLOCKS.add(Block.getBlockFromName("crafting_table"));
        Xray.BLOCKS.add(Block.getBlockById(61));
        Xray.BLOCKS.add(Block.getBlockById(62));
        Xray.BLOCKS.add(Block.getBlockFromName("torch"));
        Xray.BLOCKS.add(Block.getBlockFromName("ladder"));
        Xray.BLOCKS.add(Block.getBlockFromName("tnt"));
        Xray.BLOCKS.add(Block.getBlockFromName("coal_block"));
        Xray.BLOCKS.add(Block.getBlockFromName("iron_block"));
        Xray.BLOCKS.add(Block.getBlockFromName("gold_block"));
        Xray.BLOCKS.add(Block.getBlockFromName("diamond_block"));
        Xray.BLOCKS.add(Block.getBlockFromName("emerald_block"));
        Xray.BLOCKS.add(Block.getBlockFromName("redstone_block"));
        Xray.BLOCKS.add(Block.getBlockFromName("lapis_block"));
        Xray.BLOCKS.add(Block.getBlockFromName("fire"));
        Xray.BLOCKS.add(Block.getBlockFromName("mossy_cobblestone"));
        Xray.BLOCKS.add(Block.getBlockFromName("mob_spawner"));
        Xray.BLOCKS.add(Block.getBlockFromName("end_portal_frame"));
        Xray.BLOCKS.add(Block.getBlockFromName("enchanting_table"));
        Xray.BLOCKS.add(Block.getBlockFromName("bookshelf"));
        Xray.BLOCKS.add(Block.getBlockFromName("command_block"));
    }
    
    @EventTarget
    public void onRender(final EventRenderBlock event) {
        final Block block = event.getBlockState().getBlock();
        if (shouldXray(block) && Xray.mc.getBlockRendererDispatcher().getBlockModelRenderer().renderModelFlat(event.getBlockAccess(), event.getBakedModel(), event.getBlockState(), event.getBlockPos(), event.getBufferBuilder(), event.isCheckSides(), event.getRand())) {
            event.setRenderable(true);
        }
        event.setCancelled(true);
    }
    
    public static ArrayList<Block> getBLOCKS() {
        return Xray.BLOCKS;
    }
    
    public static boolean shouldXray(final Block block) {
        return Xray.BLOCKS.contains(block);
    }
    
    public static boolean addBlock(final String string) {
        if (Block.getBlockFromName(string) != null) {
            Xray.BLOCKS.add(Block.getBlockFromName(string));
            return true;
        }
        return false;
    }
    
    public static boolean delBlock(final String string) {
        if (Block.getBlockFromName(string) != null) {
            Xray.BLOCKS.remove(Block.getBlockFromName(string));
            return true;
        }
        return false;
    }
    
    @Override
    public void onEnable() {
        Xray.mc.renderGlobal.loadRenderers();
    }
    
    @Override
    public void onDisable() {
        Xray.mc.renderGlobal.loadRenderers();
    }
    
    static {
        BLOCKS = new ArrayList<Block>();
    }
}
