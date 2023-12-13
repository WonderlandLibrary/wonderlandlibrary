// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui;

import com.google.common.collect.UnmodifiableIterator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumChatFormatting;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.Display;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.chunk.Chunk;
import java.util.ArrayList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.entity.Entity;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.util.MathHelper;
import com.google.common.collect.Lists;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.util.BlockPos;
import java.util.List;
import com.google.common.base.Strings;
import net.minecraft.optifine.Lagometer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;

public class GuiOverlayDebug extends Gui
{
    private final Minecraft mc;
    private final FontRenderer fontRenderer;
    private static final String __OBFID = "CL_00001956";
    
    public GuiOverlayDebug(final Minecraft mc) {
        this.mc = mc;
        this.fontRenderer = mc.fontRendererObj;
    }
    
    public void func_175237_a(final ScaledResolution scaledResolutionIn) {
        this.mc.mcProfiler.startSection("debug");
        GlStateManager.pushMatrix();
        this.func_180798_a();
        this.func_175239_b(scaledResolutionIn);
        GlStateManager.popMatrix();
        Lagometer.showLagometer(scaledResolutionIn);
        this.mc.mcProfiler.endSection();
    }
    
    private boolean func_175236_d() {
        return this.mc.thePlayer.func_175140_cp() || this.mc.gameSettings.field_178879_v;
    }
    
    protected void func_180798_a() {
        final List var1 = this.call();
        for (int var2 = 0; var2 < var1.size(); ++var2) {
            final String var3 = var1.get(var2);
            if (!Strings.isNullOrEmpty(var3)) {
                final int var4 = this.fontRenderer.FONT_HEIGHT;
                final int var5 = this.fontRenderer.getStringWidth(var3);
                final boolean var6 = true;
                final int var7 = 2 + var4 * var2;
                Gui.drawRect(1, var7 - 1, 2 + var5 + 1, var7 + var4 - 1, -1873784752);
                this.fontRenderer.drawString(var3, 2.0f, var7, 14737632);
            }
        }
    }
    
    protected void func_175239_b(final ScaledResolution p_175239_1_) {
        final List var2 = this.func_175238_c();
        for (int var3 = 0; var3 < var2.size(); ++var3) {
            final String var4 = var2.get(var3);
            if (!Strings.isNullOrEmpty(var4)) {
                final int var5 = this.fontRenderer.FONT_HEIGHT;
                final int var6 = this.fontRenderer.getStringWidth(var4);
                final int var7 = p_175239_1_.getScaledWidth() - 2 - var6;
                final int var8 = 2 + var5 * var3;
                Gui.drawRect(var7 - 1, var8 - 1, var7 + var6 + 1, var8 + var5 - 1, -1873784752);
                this.fontRenderer.drawString(var4, var7, var8, 14737632);
            }
        }
    }
    
    protected List call() {
        final BlockPos var1 = new BlockPos(this.mc.func_175606_aa().posX, this.mc.func_175606_aa().getEntityBoundingBox().minY, this.mc.func_175606_aa().posZ);
        if (this.func_175236_d()) {
            return Lists.newArrayList((Object[])new String[] { "Minecraft 1.8 (" + this.mc.func_175600_c() + "/" + ClientBrandRetriever.getClientModName() + ")", this.mc.debug, this.mc.renderGlobal.getDebugInfoRenders(), this.mc.renderGlobal.getDebugInfoEntities(), "P: " + this.mc.effectRenderer.getStatistics() + ". T: " + this.mc.theWorld.getDebugLoadedEntities(), this.mc.theWorld.getProviderName(), "", String.format("Chunk-relative: %d %d %d", var1.getX() & 0xF, var1.getY() & 0xF, var1.getZ() & 0xF) });
        }
        final Entity var2 = this.mc.func_175606_aa();
        final EnumFacing var3 = var2.func_174811_aO();
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
                break;
            }
        }
        final ArrayList var5 = Lists.newArrayList((Object[])new String[] { "Minecraft 1.8 (" + this.mc.func_175600_c() + "/" + ClientBrandRetriever.getClientModName() + ")", this.mc.debug, this.mc.renderGlobal.getDebugInfoRenders(), this.mc.renderGlobal.getDebugInfoEntities(), "P: " + this.mc.effectRenderer.getStatistics() + ". T: " + this.mc.theWorld.getDebugLoadedEntities(), this.mc.theWorld.getProviderName(), "", String.format("XYZ: %.3f / %.5f / %.3f", this.mc.func_175606_aa().posX, this.mc.func_175606_aa().getEntityBoundingBox().minY, this.mc.func_175606_aa().posZ), String.format("Block: %d %d %d", var1.getX(), var1.getY(), var1.getZ()), String.format("Chunk: %d %d %d in %d %d %d", var1.getX() & 0xF, var1.getY() & 0xF, var1.getZ() & 0xF, var1.getX() >> 4, var1.getY() >> 4, var1.getZ() >> 4), String.format("Facing: %s (%s) (%.1f / %.1f)", var3, var4, MathHelper.wrapAngleTo180_float(var2.rotationYaw), MathHelper.wrapAngleTo180_float(var2.rotationPitch)) });
        if (this.mc.theWorld != null && this.mc.theWorld.isBlockLoaded(var1)) {
            final Chunk var6 = this.mc.theWorld.getChunkFromBlockCoords(var1);
            var5.add("Biome: " + var6.getBiome(var1, this.mc.theWorld.getWorldChunkManager()).biomeName);
            var5.add("Light: " + var6.setLight(var1, 0) + " (" + var6.getLightFor(EnumSkyBlock.SKY, var1) + " sky, " + var6.getLightFor(EnumSkyBlock.BLOCK, var1) + " block)");
            DifficultyInstance var7 = this.mc.theWorld.getDifficultyForLocation(var1);
            if (this.mc.isIntegratedServerRunning() && this.mc.getIntegratedServer() != null) {
                final EntityPlayerMP var8 = this.mc.getIntegratedServer().getConfigurationManager().func_177451_a(this.mc.thePlayer.getUniqueID());
                if (var8 != null) {
                    var7 = var8.worldObj.getDifficultyForLocation(new BlockPos(var8));
                }
            }
            var5.add(String.format("Local Difficulty: %.2f (Day %d)", var7.func_180168_b(), this.mc.theWorld.getWorldTime() / 24000L));
        }
        if (this.mc.entityRenderer != null && this.mc.entityRenderer.isShaderActive()) {
            var5.add("Shader: " + this.mc.entityRenderer.getShaderGroup().getShaderGroupName());
        }
        if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.mc.objectMouseOver.getBlockPos() != null) {
            final BlockPos var9 = this.mc.objectMouseOver.getBlockPos();
            var5.add(String.format("Looking at: %d %d %d", var9.getX(), var9.getY(), var9.getZ()));
        }
        return var5;
    }
    
    protected List func_175238_c() {
        final long var1 = Runtime.getRuntime().maxMemory();
        final long var2 = Runtime.getRuntime().totalMemory();
        final long var3 = Runtime.getRuntime().freeMemory();
        final long var4 = var2 - var3;
        final ArrayList var5 = Lists.newArrayList((Object[])new String[] { String.format("Java: %s %dbit", System.getProperty("java.version"), this.mc.isJava64bit() ? 64 : 32), String.format("Mem: % 2d%% %03d/%03dMB", var4 * 100L / var1, func_175240_a(var4), func_175240_a(var1)), String.format("Allocated: % 2d%% %03dMB", var2 * 100L / var1, func_175240_a(var2)), "", String.format("Display: %dx%d (%s)", Display.getWidth(), Display.getHeight(), GL11.glGetString(7936)), GL11.glGetString(7937), GL11.glGetString(7938) });
        if (this.func_175236_d()) {
            return var5;
        }
        if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.mc.objectMouseOver.getBlockPos() != null) {
            final BlockPos var6 = this.mc.objectMouseOver.getBlockPos();
            IBlockState var7 = this.mc.theWorld.getBlockState(var6);
            if (this.mc.theWorld.getWorldType() != WorldType.DEBUG_WORLD) {
                var7 = var7.getBlock().getActualState(var7, this.mc.theWorld, var6);
            }
            var5.add("");
            var5.add(String.valueOf(Block.blockRegistry.getNameForObject(var7.getBlock())));
            for (final Map.Entry var9 : var7.getProperties().entrySet()) {
                String var10 = var9.getValue().toString();
                if (var9.getValue() == Boolean.TRUE) {
                    var10 = EnumChatFormatting.GREEN + var10;
                }
                else if (var9.getValue() == Boolean.FALSE) {
                    var10 = EnumChatFormatting.RED + var10;
                }
                var5.add(var9.getKey().getName() + ": " + var10);
            }
        }
        return var5;
    }
    
    private static long func_175240_a(final long p_175240_0_) {
        return p_175240_0_ / 1024L / 1024L;
    }
    
    static final class SwitchEnumFacing
    {
        static final int[] field_178907_a;
        private static final String __OBFID = "CL_00001955";
        
        static {
            field_178907_a = new int[EnumFacing.values().length];
            try {
                SwitchEnumFacing.field_178907_a[EnumFacing.NORTH.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchEnumFacing.field_178907_a[EnumFacing.SOUTH.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                SwitchEnumFacing.field_178907_a[EnumFacing.WEST.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            try {
                SwitchEnumFacing.field_178907_a[EnumFacing.EAST.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
        }
    }
}
