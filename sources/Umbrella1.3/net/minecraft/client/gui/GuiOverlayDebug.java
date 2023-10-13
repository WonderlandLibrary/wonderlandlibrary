/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Strings
 *  com.google.common.collect.Lists
 *  org.lwjgl.opengl.Display
 *  org.lwjgl.opengl.GL11
 */
package net.minecraft.client.gui;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.Chunk;
import optifine.Reflector;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class GuiOverlayDebug
extends Gui {
    private final Minecraft mc;
    private final FontRenderer fontRenderer;
    private static final String __OBFID = "CL_00001956";

    public GuiOverlayDebug(Minecraft mc) {
        this.mc = mc;
        this.fontRenderer = mc.fontRendererObj;
    }

    public void func_175237_a(ScaledResolution scaledResolutionIn) {
        this.mc.mcProfiler.startSection("debug");
        GlStateManager.pushMatrix();
        this.func_180798_a();
        this.func_175239_b(scaledResolutionIn);
        GlStateManager.popMatrix();
        this.mc.mcProfiler.endSection();
    }

    private boolean func_175236_d() {
        return this.mc.thePlayer.func_175140_cp() || this.mc.gameSettings.field_178879_v;
    }

    protected void func_180798_a() {
        List var1 = this.call();
        for (int var2 = 0; var2 < var1.size(); ++var2) {
            String var3 = (String)var1.get(var2);
            if (Strings.isNullOrEmpty((String)var3)) continue;
            int var4 = this.fontRenderer.FONT_HEIGHT;
            int var5 = this.fontRenderer.getStringWidth(var3);
            boolean var6 = true;
            int var7 = 2 + var4 * var2;
            GuiOverlayDebug.drawRect(1.0, var7 - 1, 2 + var5 + 1, var7 + var4 - 1, -1873784752);
            this.fontRenderer.drawString(var3, 2.0, var7, 0xE0E0E0);
        }
    }

    protected void func_175239_b(ScaledResolution p_175239_1_) {
        List var2 = this.func_175238_c();
        for (int var3 = 0; var3 < var2.size(); ++var3) {
            String var4 = (String)var2.get(var3);
            if (Strings.isNullOrEmpty((String)var4)) continue;
            int var5 = this.fontRenderer.FONT_HEIGHT;
            int var6 = this.fontRenderer.getStringWidth(var4);
            int var7 = p_175239_1_.getScaledWidth() - 2 - var6;
            int var8 = 2 + var5 * var3;
            GuiOverlayDebug.drawRect(var7 - 1, var8 - 1, var7 + var6 + 1, var8 + var5 - 1, -1873784752);
            this.fontRenderer.drawString(var4, var7, var8, 0xE0E0E0);
        }
    }

    protected List call() {
        BlockPos var1 = new BlockPos(this.mc.func_175606_aa().posX, this.mc.func_175606_aa().getEntityBoundingBox().minY, this.mc.func_175606_aa().posZ);
        if (this.func_175236_d()) {
            return Lists.newArrayList((Object[])new String[]{"Minecraft 1.8 (" + this.mc.func_175600_c() + "/" + ClientBrandRetriever.getClientModName() + ")", this.mc.debug, this.mc.renderGlobal.getDebugInfoRenders(), this.mc.renderGlobal.getDebugInfoEntities(), "P: " + this.mc.effectRenderer.getStatistics() + ". T: " + this.mc.theWorld.getDebugLoadedEntities(), this.mc.theWorld.getProviderName(), "", String.format("Chunk-relative: %d %d %d", var1.getX() & 0xF, var1.getY() & 0xF, var1.getZ() & 0xF)});
        }
        Entity var2 = this.mc.func_175606_aa();
        EnumFacing var3 = var2.func_174811_aO();
        String var4 = "Invalid";
        switch (SwitchEnumFacing.field_178907_a[var3.ordinal()]) {
            case 1: {
                var4 = "Towards negative Z";
                break;
            }
            case 2: {
                var4 = "Towards positive Z";
                break;
            }
            case 3: {
                var4 = "Towards negative X";
                break;
            }
            case 4: {
                var4 = "Towards positive X";
            }
        }
        ArrayList var5 = Lists.newArrayList((Object[])new String[]{"Minecraft 1.8 (" + this.mc.func_175600_c() + "/" + ClientBrandRetriever.getClientModName() + ")", this.mc.debug, this.mc.renderGlobal.getDebugInfoRenders(), this.mc.renderGlobal.getDebugInfoEntities(), "P: " + this.mc.effectRenderer.getStatistics() + ". T: " + this.mc.theWorld.getDebugLoadedEntities(), this.mc.theWorld.getProviderName(), "", String.format("XYZ: %.3f / %.5f / %.3f", this.mc.func_175606_aa().posX, this.mc.func_175606_aa().getEntityBoundingBox().minY, this.mc.func_175606_aa().posZ), String.format("Block: %d %d %d", var1.getX(), var1.getY(), var1.getZ()), String.format("Chunk: %d %d %d in %d %d %d", var1.getX() & 0xF, var1.getY() & 0xF, var1.getZ() & 0xF, var1.getX() >> 4, var1.getY() >> 4, var1.getZ() >> 4), String.format("Facing: %s (%s) (%.1f / %.1f)", var3, var4, Float.valueOf(MathHelper.wrapAngleTo180_float(var2.rotationYaw)), Float.valueOf(MathHelper.wrapAngleTo180_float(var2.rotationPitch)))});
        if (this.mc.theWorld != null && this.mc.theWorld.isBlockLoaded(var1)) {
            EntityPlayerMP var8;
            Chunk var9 = this.mc.theWorld.getChunkFromBlockCoords(var1);
            var5.add("Biome: " + var9.getBiome((BlockPos)var1, (WorldChunkManager)this.mc.theWorld.getWorldChunkManager()).biomeName);
            var5.add("Light: " + var9.setLight(var1, 0) + " (" + var9.getLightFor(EnumSkyBlock.SKY, var1) + " sky, " + var9.getLightFor(EnumSkyBlock.BLOCK, var1) + " block)");
            DifficultyInstance var7 = this.mc.theWorld.getDifficultyForLocation(var1);
            if (this.mc.isIntegratedServerRunning() && this.mc.getIntegratedServer() != null && (var8 = this.mc.getIntegratedServer().getConfigurationManager().func_177451_a(this.mc.thePlayer.getUniqueID())) != null) {
                var7 = var8.worldObj.getDifficultyForLocation(new BlockPos(var8));
            }
            var5.add(String.format("Local Difficulty: %.2f (Day %d)", Float.valueOf(var7.func_180168_b()), this.mc.theWorld.getWorldTime() / 24000L));
        }
        if (this.mc.entityRenderer != null && this.mc.entityRenderer.isShaderActive()) {
            var5.add("Shader: " + this.mc.entityRenderer.getShaderGroup().getShaderGroupName());
        }
        if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.mc.objectMouseOver.func_178782_a() != null) {
            BlockPos var91 = this.mc.objectMouseOver.func_178782_a();
            var5.add(String.format("Looking at: %d %d %d", var91.getX(), var91.getY(), var91.getZ()));
        }
        return var5;
    }

    protected List func_175238_c() {
        long var1 = Runtime.getRuntime().maxMemory();
        long var3 = Runtime.getRuntime().totalMemory();
        long var5 = Runtime.getRuntime().freeMemory();
        long var7 = var3 - var5;
        ArrayList var9 = Lists.newArrayList((Object[])new String[]{String.format("Java: %s %dbit", System.getProperty("java.version"), this.mc.isJava64bit() ? 64 : 32), String.format("Mem: % 2d%% %03d/%03dMB", var7 * 100L / var1, GuiOverlayDebug.func_175240_a(var7), GuiOverlayDebug.func_175240_a(var1)), String.format("Allocated: % 2d%% %03dMB", var3 * 100L / var1, GuiOverlayDebug.func_175240_a(var3)), "", String.format("Display: %dx%d (%s)", Display.getWidth(), Display.getHeight(), GL11.glGetString((int)7936)), GL11.glGetString((int)7937), GL11.glGetString((int)7938)});
        if (Reflector.FMLCommonHandler_getBrandings.exists()) {
            Object var10 = Reflector.call(Reflector.FMLCommonHandler_instance, new Object[0]);
            var9.add("");
            var9.addAll((Collection)Reflector.call(var10, Reflector.FMLCommonHandler_getBrandings, false));
        }
        if (this.func_175236_d()) {
            return var9;
        }
        if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.mc.objectMouseOver.func_178782_a() != null) {
            BlockPos var101 = this.mc.objectMouseOver.func_178782_a();
            IBlockState var11 = this.mc.theWorld.getBlockState(var101);
            if (this.mc.theWorld.getWorldType() != WorldType.DEBUG_WORLD) {
                var11 = var11.getBlock().getActualState(var11, this.mc.theWorld, var101);
            }
            var9.add("");
            var9.add(String.valueOf(Block.blockRegistry.getNameForObject(var11.getBlock())));
            for (Map.Entry var13 : var11.getProperties().entrySet()) {
                String var14 = ((Comparable)var13.getValue()).toString();
                if (var13.getValue() == Boolean.TRUE) {
                    var14 = (Object)((Object)EnumChatFormatting.GREEN) + var14;
                } else if (var13.getValue() == Boolean.FALSE) {
                    var14 = (Object)((Object)EnumChatFormatting.RED) + var14;
                }
                var9.add(String.valueOf(((IProperty)var13.getKey()).getName()) + ": " + var14);
            }
        }
        return var9;
    }

    private static long func_175240_a(long p_175240_0_) {
        return p_175240_0_ / 1024L / 1024L;
    }

    static final class SwitchEnumFacing {
        static final int[] field_178907_a = new int[EnumFacing.values().length];
        private static final String __OBFID = "CL_00001955";

        static {
            try {
                SwitchEnumFacing.field_178907_a[EnumFacing.NORTH.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.field_178907_a[EnumFacing.SOUTH.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.field_178907_a[EnumFacing.WEST.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.field_178907_a[EnumFacing.EAST.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
        }

        SwitchEnumFacing() {
        }
    }
}

