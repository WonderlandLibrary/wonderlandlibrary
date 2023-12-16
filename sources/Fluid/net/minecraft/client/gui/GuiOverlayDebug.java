// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui;

import java.util.Iterator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.block.properties.IProperty;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import java.util.Collection;
import net.optifine.reflect.Reflector;
import net.optifine.util.MemoryMonitor;
import net.optifine.util.NativeMemory;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.Display;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.util.EnumFacing;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.entity.Entity;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.util.MathHelper;
import com.google.common.collect.Lists;
import net.minecraft.client.ClientBrandRetriever;
import net.optifine.TextureAnimations;
import net.optifine.SmartAnimations;
import net.minecraft.src.Config;
import net.minecraft.util.BlockPos;
import com.google.common.base.Strings;
import net.minecraft.client.renderer.GlStateManager;
import java.util.List;
import net.minecraft.client.Minecraft;

public class GuiOverlayDebug extends Gui
{
    private final Minecraft mc;
    private final FontRenderer fontRenderer;
    private String debugOF;
    private List<String> debugInfoLeft;
    private List<String> debugInfoRight;
    private long updateInfoLeftTimeMs;
    private long updateInfoRightTimeMs;
    
    public GuiOverlayDebug(final Minecraft mc) {
        this.debugOF = null;
        this.debugInfoLeft = null;
        this.debugInfoRight = null;
        this.updateInfoLeftTimeMs = 0L;
        this.updateInfoRightTimeMs = 0L;
        this.mc = mc;
        this.fontRenderer = mc.fontRendererObj;
    }
    
    public void renderDebugInfo(final ScaledResolution scaledResolutionIn) {
        this.mc.mcProfiler.startSection("debug");
        GlStateManager.pushMatrix();
        this.renderDebugInfoLeft();
        this.renderDebugInfoRight(scaledResolutionIn);
        GlStateManager.popMatrix();
        if (this.mc.gameSettings.showLagometer) {
            this.renderLagometer();
        }
        this.mc.mcProfiler.endSection();
    }
    
    private boolean isReducedDebug() {
        return this.mc.thePlayer.hasReducedDebug() || this.mc.gameSettings.reducedDebugInfo;
    }
    
    protected void renderDebugInfoLeft() {
        List<String> list = this.debugInfoLeft;
        if (list == null || System.currentTimeMillis() > this.updateInfoLeftTimeMs) {
            list = this.call();
            this.debugInfoLeft = list;
            this.updateInfoLeftTimeMs = System.currentTimeMillis() + 100L;
        }
        for (int i = 0; i < list.size(); ++i) {
            final String s = list.get(i);
            if (!Strings.isNullOrEmpty(s)) {
                final int j = this.fontRenderer.FONT_HEIGHT;
                final int k = this.fontRenderer.getStringWidth(s);
                final int l = 2;
                final int i2 = 2 + j * i;
                Gui.drawRect(1, i2 - 1, 2 + k + 1, i2 + j - 1, -1873784752);
                this.fontRenderer.drawString(s, 2, i2, 14737632);
            }
        }
    }
    
    protected void renderDebugInfoRight(final ScaledResolution scaledRes) {
        List<String> list = this.debugInfoRight;
        if (list == null || System.currentTimeMillis() > this.updateInfoRightTimeMs) {
            list = this.getDebugInfoRight();
            this.debugInfoRight = list;
            this.updateInfoRightTimeMs = System.currentTimeMillis() + 100L;
        }
        for (int i = 0; i < list.size(); ++i) {
            final String s = list.get(i);
            if (!Strings.isNullOrEmpty(s)) {
                final int j = this.fontRenderer.FONT_HEIGHT;
                final int k = this.fontRenderer.getStringWidth(s);
                final int l = scaledRes.getScaledWidth() - 2 - k;
                final int i2 = 2 + j * i;
                Gui.drawRect(l - 1, i2 - 1, l + k + 1, i2 + j - 1, -1873784752);
                this.fontRenderer.drawString(s, l, i2, 14737632);
            }
        }
    }
    
    protected List<String> call() {
        final BlockPos blockpos = new BlockPos(this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().getEntityBoundingBox().minY, this.mc.getRenderViewEntity().posZ);
        if (this.mc.debug != this.debugOF) {
            final StringBuffer stringbuffer = new StringBuffer(this.mc.debug);
            final int i = Config.getFpsMin();
            final int j = this.mc.debug.indexOf(" fps ");
            if (j >= 0) {
                stringbuffer.insert(j, "/" + i);
            }
            if (Config.isSmoothFps()) {
                stringbuffer.append(" sf");
            }
            if (Config.isFastRender()) {
                stringbuffer.append(" fr");
            }
            if (Config.isAnisotropicFiltering()) {
                stringbuffer.append(" af");
            }
            if (Config.isAntialiasing()) {
                stringbuffer.append(" aa");
            }
            if (Config.isRenderRegions()) {
                stringbuffer.append(" reg");
            }
            if (Config.isShaders()) {
                stringbuffer.append(" sh");
            }
            this.mc.debug = stringbuffer.toString();
            this.debugOF = this.mc.debug;
        }
        final StringBuilder stringbuilder = new StringBuilder();
        final TextureMap texturemap = Config.getTextureMap();
        stringbuilder.append(", A: ");
        if (SmartAnimations.isActive()) {
            stringbuilder.append(texturemap.getCountAnimationsActive() + TextureAnimations.getCountAnimationsActive());
            stringbuilder.append("/");
        }
        stringbuilder.append(texturemap.getCountAnimations() + TextureAnimations.getCountAnimations());
        final String s1 = stringbuilder.toString();
        if (this.isReducedDebug()) {
            return Lists.newArrayList((Object[])new String[] { "Minecraft 1.8.9 (" + this.mc.getVersion() + "/" + ClientBrandRetriever.getClientModName() + ")", this.mc.debug, this.mc.renderGlobal.getDebugInfoRenders(), this.mc.renderGlobal.getDebugInfoEntities(), "P: " + this.mc.effectRenderer.getStatistics() + ". T: " + this.mc.theWorld.getDebugLoadedEntities() + s1, this.mc.theWorld.getProviderName(), "", String.format("Chunk-relative: %d %d %d", blockpos.getX() & 0xF, blockpos.getY() & 0xF, blockpos.getZ() & 0xF) });
        }
        final Entity entity = this.mc.getRenderViewEntity();
        final EnumFacing enumfacing = entity.getHorizontalFacing();
        String s2 = "Invalid";
        switch (enumfacing) {
            case NORTH: {
                s2 = "Towards negative Z";
                break;
            }
            case SOUTH: {
                s2 = "Towards positive Z";
                break;
            }
            case WEST: {
                s2 = "Towards negative X";
                break;
            }
            case EAST: {
                s2 = "Towards positive X";
                break;
            }
        }
        final List<String> list = Lists.newArrayList((Object[])new String[] { "Minecraft 1.8.9 (" + this.mc.getVersion() + "/" + ClientBrandRetriever.getClientModName() + ")", this.mc.debug, this.mc.renderGlobal.getDebugInfoRenders(), this.mc.renderGlobal.getDebugInfoEntities(), "P: " + this.mc.effectRenderer.getStatistics() + ". T: " + this.mc.theWorld.getDebugLoadedEntities() + s1, this.mc.theWorld.getProviderName(), "", String.format("XYZ: %.3f / %.5f / %.3f", this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().getEntityBoundingBox().minY, this.mc.getRenderViewEntity().posZ), String.format("Block: %d %d %d", blockpos.getX(), blockpos.getY(), blockpos.getZ()), String.format("Chunk: %d %d %d in %d %d %d", blockpos.getX() & 0xF, blockpos.getY() & 0xF, blockpos.getZ() & 0xF, blockpos.getX() >> 4, blockpos.getY() >> 4, blockpos.getZ() >> 4), String.format("Facing: %s (%s) (%.1f / %.1f)", enumfacing, s2, MathHelper.wrapAngleTo180_float(entity.rotationYaw), MathHelper.wrapAngleTo180_float(entity.rotationPitch)) });
        if (this.mc.theWorld != null && this.mc.theWorld.isBlockLoaded(blockpos)) {
            final Chunk chunk = this.mc.theWorld.getChunkFromBlockCoords(blockpos);
            list.add("Biome: " + chunk.getBiome(blockpos, this.mc.theWorld.getWorldChunkManager()).biomeName);
            list.add("Light: " + chunk.getLightSubtracted(blockpos, 0) + " (" + chunk.getLightFor(EnumSkyBlock.SKY, blockpos) + " sky, " + chunk.getLightFor(EnumSkyBlock.BLOCK, blockpos) + " block)");
            DifficultyInstance difficultyinstance = this.mc.theWorld.getDifficultyForLocation(blockpos);
            if (this.mc.isIntegratedServerRunning() && this.mc.getIntegratedServer() != null) {
                final EntityPlayerMP entityplayermp = this.mc.getIntegratedServer().getConfigurationManager().getPlayerByUUID(this.mc.thePlayer.getUniqueID());
                if (entityplayermp != null) {
                    final DifficultyInstance difficultyinstance2 = this.mc.getIntegratedServer().getDifficultyAsync(entityplayermp.worldObj, new BlockPos(entityplayermp));
                    if (difficultyinstance2 != null) {
                        difficultyinstance = difficultyinstance2;
                    }
                }
            }
            list.add(String.format("Local Difficulty: %.2f (Day %d)", difficultyinstance.getAdditionalDifficulty(), this.mc.theWorld.getWorldTime() / 24000L));
        }
        if (this.mc.entityRenderer != null && this.mc.entityRenderer.isShaderActive()) {
            list.add("Shader: " + this.mc.entityRenderer.getShaderGroup().getShaderGroupName());
        }
        if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.mc.objectMouseOver.getBlockPos() != null) {
            final BlockPos blockpos2 = this.mc.objectMouseOver.getBlockPos();
            list.add(String.format("Looking at: %d %d %d", blockpos2.getX(), blockpos2.getY(), blockpos2.getZ()));
        }
        return list;
    }
    
    protected List<String> getDebugInfoRight() {
        final long i = Runtime.getRuntime().maxMemory();
        final long j = Runtime.getRuntime().totalMemory();
        final long k = Runtime.getRuntime().freeMemory();
        final long l = j - k;
        final List<String> list = Lists.newArrayList((Object[])new String[] { String.format("Java: %s %dbit", System.getProperty("java.version"), this.mc.isJava64bit() ? 64 : 32), String.format("Mem: % 2d%% %03d/%03dMB", l * 100L / i, bytesToMb(l), bytesToMb(i)), String.format("Allocated: % 2d%% %03dMB", j * 100L / i, bytesToMb(j)), "", String.format("CPU: %s", OpenGlHelper.getCpu()), "", String.format("Display: %dx%d (%s)", Display.getWidth(), Display.getHeight(), GL11.glGetString(7936)), GL11.glGetString(7937), GL11.glGetString(7938) });
        final long i2 = NativeMemory.getBufferAllocated();
        final long j2 = NativeMemory.getBufferMaximum();
        final String s = "Native: " + bytesToMb(i2) + "/" + bytesToMb(j2) + "MB";
        list.add(4, s);
        list.set(5, "GC: " + MemoryMonitor.getAllocationRateMb() + "MB/s");
        if (Reflector.FMLCommonHandler_getBrandings.exists()) {
            final Object object = Reflector.call(Reflector.FMLCommonHandler_instance, new Object[0]);
            list.add("");
            list.addAll((Collection<? extends String>)Reflector.call(object, Reflector.FMLCommonHandler_getBrandings, false));
        }
        if (this.isReducedDebug()) {
            return list;
        }
        if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.mc.objectMouseOver.getBlockPos() != null) {
            final BlockPos blockpos = this.mc.objectMouseOver.getBlockPos();
            IBlockState iblockstate = this.mc.theWorld.getBlockState(blockpos);
            if (this.mc.theWorld.getWorldType() != WorldType.DEBUG_WORLD) {
                iblockstate = iblockstate.getBlock().getActualState(iblockstate, this.mc.theWorld, blockpos);
            }
            list.add("");
            list.add(String.valueOf(Block.blockRegistry.getNameForObject(iblockstate.getBlock())));
            for (final Map.Entry<IProperty, Comparable> entry : iblockstate.getProperties().entrySet()) {
                String s2 = entry.getValue().toString();
                if (entry.getValue() == Boolean.TRUE) {
                    s2 = EnumChatFormatting.GREEN + s2;
                }
                else if (entry.getValue() == Boolean.FALSE) {
                    s2 = EnumChatFormatting.RED + s2;
                }
                list.add(String.valueOf(entry.getKey().getName()) + ": " + s2);
            }
        }
        return list;
    }
    
    private void renderLagometer() {
    }
    
    private int getFrameColor(final int p_181552_1_, final int p_181552_2_, final int p_181552_3_, final int p_181552_4_) {
        return (p_181552_1_ < p_181552_3_) ? this.blendColors(-16711936, -256, p_181552_1_ / (float)p_181552_3_) : this.blendColors(-256, -65536, (p_181552_1_ - p_181552_3_) / (float)(p_181552_4_ - p_181552_3_));
    }
    
    private int blendColors(final int p_181553_1_, final int p_181553_2_, final float p_181553_3_) {
        final int i = p_181553_1_ >> 24 & 0xFF;
        final int j = p_181553_1_ >> 16 & 0xFF;
        final int k = p_181553_1_ >> 8 & 0xFF;
        final int l = p_181553_1_ & 0xFF;
        final int i2 = p_181553_2_ >> 24 & 0xFF;
        final int j2 = p_181553_2_ >> 16 & 0xFF;
        final int k2 = p_181553_2_ >> 8 & 0xFF;
        final int l2 = p_181553_2_ & 0xFF;
        final int i3 = MathHelper.clamp_int((int)(i + (i2 - i) * p_181553_3_), 0, 255);
        final int j3 = MathHelper.clamp_int((int)(j + (j2 - j) * p_181553_3_), 0, 255);
        final int k3 = MathHelper.clamp_int((int)(k + (k2 - k) * p_181553_3_), 0, 255);
        final int l3 = MathHelper.clamp_int((int)(l + (l2 - l) * p_181553_3_), 0, 255);
        return i3 << 24 | j3 << 16 | k3 << 8 | l3;
    }
    
    private static long bytesToMb(final long bytes) {
        return bytes / 1024L / 1024L;
    }
}
