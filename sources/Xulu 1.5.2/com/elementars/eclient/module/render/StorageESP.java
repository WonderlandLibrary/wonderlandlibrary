// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.render;

import com.elementars.eclient.util.OutlineUtils2;
import java.awt.Color;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import java.util.Iterator;
import net.minecraft.world.World;
import com.elementars.eclient.util.MathUtil;
import com.elementars.eclient.util.XuluTessellator;
import com.elementars.eclient.util.OutlineUtils;
import net.minecraft.client.renderer.GlStateManager;
import com.elementars.eclient.event.events.RenderEvent;
import java.time.LocalDate;
import com.elementars.eclient.util.Wrapper;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecartHopper;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityShulkerBox;
import com.elementars.eclient.util.ColorUtils;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntity;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import com.elementars.eclient.module.Category;
import java.util.Random;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class StorageESP extends Module
{
    private final Value<String> mode;
    private final Value<Boolean> all;
    private static Value<Boolean> future;
    private final Value<Float> width;
    private final Value<Boolean> save;
    private final Value<Integer> threshold;
    Random random;
    private int delay;
    private int count;
    
    public StorageESP() {
        super("StorageESP", "Highlights storage blocks", 0, Category.RENDER, true);
        this.mode = this.register(new Value<String>("Mode", this, "Solid", new ArrayList<String>(Arrays.asList("Solid", "Full", "Outline", "Shader"))));
        this.all = this.register(new Value<Boolean>("All Tile Entities", this, false));
        this.width = this.register(new Value<Float>("Line Width", this, 1.0f, 1.0f, 10.0f));
        this.save = this.register(new Value<Boolean>("Save coords above threshold", this, false));
        this.threshold = this.register(new Value<Integer>("Threshold", this, 200, 1, 2000));
        this.random = new Random();
        StorageESP.future = this.register(new Value<Boolean>("Future Colors", this, false));
        this.count = 1;
    }
    
    public static int getTileEntityColor(final TileEntity tileEntity) {
        if (tileEntity instanceof TileEntityChest || tileEntity instanceof TileEntityDispenser) {
            return ColorUtils.Colors.ORANGE;
        }
        if (tileEntity instanceof TileEntityShulkerBox) {
            return ColorUtils.toRGBA(255, 0, 95, 255);
        }
        if (tileEntity instanceof TileEntityEnderChest) {
            return ColorUtils.Colors.PURPLE;
        }
        if (tileEntity instanceof TileEntityFurnace) {
            return ColorUtils.Colors.GRAY;
        }
        if (tileEntity instanceof TileEntityHopper) {
            return ColorUtils.Colors.DARK_RED;
        }
        return -1;
    }
    
    public static int getTileEntityColorF(final TileEntity tileEntity) {
        if (tileEntity instanceof TileEntityChest) {
            return ColorUtils.toRGBA(200, 200, 101, 255);
        }
        if (tileEntity instanceof TileEntityShulkerBox) {
            return ColorUtils.toRGBA(180, 21, 99, 255);
        }
        if (tileEntity instanceof TileEntityEnderChest) {
            return ColorUtils.toRGBA(155, 0, 200, 255);
        }
        if (tileEntity instanceof TileEntityFurnace) {
            return ColorUtils.Colors.GRAY;
        }
        if (tileEntity instanceof TileEntityHopper) {
            return ColorUtils.Colors.GRAY;
        }
        return -1;
    }
    
    private int getEntityColor(final Entity entity) {
        if (entity instanceof EntityMinecartChest) {
            return ColorUtils.Colors.ORANGE;
        }
        if (entity instanceof EntityMinecartHopper) {
            return ColorUtils.Colors.DARK_RED;
        }
        if (entity instanceof EntityItemFrame && ((EntityItemFrame)entity).getDisplayedItem().getItem() instanceof ItemShulkerBox) {
            return ColorUtils.Colors.YELLOW;
        }
        return -1;
    }
    
    @Override
    public String getHudInfo() {
        final long chests = StorageESP.mc.world.loadedTileEntityList.stream().filter(tileEntity -> tileEntity instanceof TileEntityChest).count();
        if (this.save.getValue() && (int)chests >= this.threshold.getValue() && this.delay == 0) {
            Wrapper.getFileManager().saveStorageESP(LocalDate.now().toString() + "-" + this.random.nextInt(9999) + " - " + this.count, (int)StorageESP.mc.player.posX + " " + (int)StorageESP.mc.player.posY + " " + (int)StorageESP.mc.player.posZ, String.valueOf(chests));
            this.delay = 4000;
            ++this.count;
        }
        return "Chests: " + chests;
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        if (this.delay > 0) {
            --this.delay;
        }
        final ArrayList<Triplet<TileEntity, Integer, Integer>> a = new ArrayList<Triplet<TileEntity, Integer, Integer>>();
        GlStateManager.pushMatrix();
        if (this.mode.getValue().equalsIgnoreCase("Shader")) {
            OutlineUtils.checkSetupFBO();
        }
        for (final TileEntity tileEntity : Wrapper.getWorld().loadedTileEntityList) {
            final BlockPos pos = tileEntity.getPos();
            int color;
            if (StorageESP.future.getValue()) {
                color = getTileEntityColorF(tileEntity);
            }
            else {
                color = getTileEntityColor(tileEntity);
            }
            int side = 63;
            if (tileEntity instanceof TileEntityChest) {
                final TileEntityChest chest = (TileEntityChest)tileEntity;
                if (chest.adjacentChestZNeg != null) {
                    side = ~(side & 0x4);
                }
                if (chest.adjacentChestXPos != null) {
                    side = ~(side & 0x20);
                }
                if (chest.adjacentChestZPos != null) {
                    side = ~(side & 0x8);
                }
                if (chest.adjacentChestXNeg != null) {
                    side = ~(side & 0x10);
                }
            }
            if (color != -1) {
                a.add(new Triplet<TileEntity, Integer, Integer>(tileEntity, color, side));
            }
        }
        for (final Triplet<TileEntity, Integer, Integer> pair : a) {
            try {
                if (this.mode.getValue().equalsIgnoreCase("Solid")) {
                    XuluTessellator.prepare(7);
                    XuluTessellator.drawBox(pair.getFirst().getPos(), this.changeAlpha(pair.getSecond(), 100), pair.getThird());
                    XuluTessellator.release();
                }
                else if (this.mode.getValue().equalsIgnoreCase("Outline")) {
                    final IBlockState iBlockState2 = StorageESP.mc.world.getBlockState(pair.getFirst().getPos());
                    final Vec3d interp2 = MathUtil.interpolateEntity((Entity)StorageESP.mc.player, StorageESP.mc.getRenderPartialTicks());
                    XuluTessellator.prepare(7);
                    XuluTessellator.drawBoundingBox(iBlockState2.getSelectedBoundingBox((World)StorageESP.mc.world, pair.getFirst().getPos()).grow(0.0020000000949949026).offset(-interp2.x, -interp2.y, -interp2.z), 1.5f, pair.getSecond());
                    XuluTessellator.release();
                }
                else if (this.mode.getValue().equalsIgnoreCase("Full")) {
                    final IBlockState iBlockState3 = StorageESP.mc.world.getBlockState(pair.getFirst().getPos());
                    final Vec3d interp3 = MathUtil.interpolateEntity((Entity)StorageESP.mc.player, StorageESP.mc.getRenderPartialTicks());
                    XuluTessellator.drawFullBox2(iBlockState3.getSelectedBoundingBox((World)StorageESP.mc.world, pair.getFirst().getPos()).grow(0.0020000000949949026).offset(-interp3.x, -interp3.y, -interp3.z), pair.getFirst().getPos(), 1.5f, this.changeAlpha(pair.getSecond(), 100));
                }
                else {
                    if (this.mode.getValue().equalsIgnoreCase("Shader")) {
                        continue;
                    }
                    XuluTessellator.prepare(7);
                    XuluTessellator.drawBox(pair.getFirst().getPos(), this.changeAlpha(pair.getSecond(), 100), pair.getThird());
                    XuluTessellator.release();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        GlStateManager.popMatrix();
        GlStateManager.enableTexture2D();
    }
    
    int changeAlpha(int origColor, final int userInputedAlpha) {
        origColor &= 0xFFFFFF;
        return userInputedAlpha << 24 | origColor;
    }
    
    public static void renderNormal(final float n) {
        RenderHelper.enableStandardItemLighting();
        for (final TileEntity tileEntity : Wrapper.getMinecraft().world.loadedTileEntityList) {
            if (!(tileEntity instanceof TileEntityChest) && !(tileEntity instanceof TileEntityEnderChest) && !(tileEntity instanceof TileEntityShulkerBox)) {
                continue;
            }
            GL11.glPushMatrix();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            TileEntityRendererDispatcher.instance.render(tileEntity, tileEntity.getPos().getX() - StorageESP.mc.renderManager.renderPosX, tileEntity.getPos().getY() - StorageESP.mc.renderManager.renderPosY, tileEntity.getPos().getZ() - StorageESP.mc.renderManager.renderPosZ, n);
            GL11.glPopMatrix();
        }
    }
    
    public static void renderColor(final float n) {
        RenderHelper.enableStandardItemLighting();
        for (final TileEntity tileEntity : Wrapper.getMinecraft().world.loadedTileEntityList) {
            if (!(tileEntity instanceof TileEntityChest) && !(tileEntity instanceof TileEntityEnderChest) && !(tileEntity instanceof TileEntityShulkerBox) && !(tileEntity instanceof TileEntityFurnace) && !(tileEntity instanceof TileEntityHopper)) {
                continue;
            }
            if (StorageESP.future.getValue()) {
                OutlineUtils2.setColor(new Color(getTileEntityColorF(tileEntity)));
            }
            else {
                OutlineUtils2.setColor(new Color(getTileEntityColor(tileEntity)));
            }
            TileEntityRendererDispatcher.instance.render(tileEntity, tileEntity.getPos().getX() - StorageESP.mc.renderManager.renderPosX, tileEntity.getPos().getY() - StorageESP.mc.renderManager.renderPosY, tileEntity.getPos().getZ() - StorageESP.mc.renderManager.renderPosZ, n);
        }
    }
    
    public class Triplet<T, U, V>
    {
        private final T first;
        private final U second;
        private final V third;
        
        public Triplet(final T first, final U second, final V third) {
            this.first = first;
            this.second = second;
            this.third = third;
        }
        
        public T getFirst() {
            return this.first;
        }
        
        public U getSecond() {
            return this.second;
        }
        
        public V getThird() {
            return this.third;
        }
    }
}
