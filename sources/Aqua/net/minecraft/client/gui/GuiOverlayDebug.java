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
import net.minecraft.client.gui.GuiOverlayDebug;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.src.Config;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.FrameTimer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.Chunk;
import net.optifine.SmartAnimations;
import net.optifine.TextureAnimations;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorMethod;
import net.optifine.util.MemoryMonitor;
import net.optifine.util.NativeMemory;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class GuiOverlayDebug
extends Gui {
    private final Minecraft mc;
    private final FontRenderer fontRenderer;
    private String debugOF = null;
    private List<String> debugInfoLeft = null;
    private List<String> debugInfoRight = null;
    private long updateInfoLeftTimeMs = 0L;
    private long updateInfoRightTimeMs = 0L;

    public GuiOverlayDebug(Minecraft mc) {
        this.mc = mc;
        this.fontRenderer = mc.fontRendererObj;
    }

    public void renderDebugInfo(ScaledResolution scaledResolutionIn) {
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
            this.debugInfoLeft = list = this.call();
            this.updateInfoLeftTimeMs = System.currentTimeMillis() + 100L;
        }
        for (int i = 0; i < list.size(); ++i) {
            String s = (String)list.get(i);
            if (Strings.isNullOrEmpty((String)s)) continue;
            int j = FontRenderer.FONT_HEIGHT;
            int k = this.fontRenderer.getStringWidth(s);
            int l = 2;
            int i1 = 2 + j * i;
            GuiOverlayDebug.drawRect((int)1, (int)(i1 - 1), (int)(2 + k + 1), (int)(i1 + j - 1), (int)-1873784752);
            this.fontRenderer.drawString(s, 2, i1, 0xE0E0E0);
        }
    }

    protected void renderDebugInfoRight(ScaledResolution scaledRes) {
        List<String> list = this.debugInfoRight;
        if (list == null || System.currentTimeMillis() > this.updateInfoRightTimeMs) {
            this.debugInfoRight = list = this.getDebugInfoRight();
            this.updateInfoRightTimeMs = System.currentTimeMillis() + 100L;
        }
        for (int i = 0; i < list.size(); ++i) {
            String s = (String)list.get(i);
            if (Strings.isNullOrEmpty((String)s)) continue;
            int j = FontRenderer.FONT_HEIGHT;
            int k = this.fontRenderer.getStringWidth(s);
            int l = scaledRes.getScaledWidth() - 2 - k;
            int i1 = 2 + j * i;
            GuiOverlayDebug.drawRect((int)(l - 1), (int)(i1 - 1), (int)(l + k + 1), (int)(i1 + j - 1), (int)-1873784752);
            this.fontRenderer.drawString(s, l, i1, 0xE0E0E0);
        }
    }

    protected List<String> call() {
        BlockPos blockpos = new BlockPos(this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().getEntityBoundingBox().minY, this.mc.getRenderViewEntity().posZ);
        if (this.mc.debug != this.debugOF) {
            StringBuffer stringbuffer = new StringBuffer(this.mc.debug);
            int i = Config.getFpsMin();
            int j = this.mc.debug.indexOf(" fps ");
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
            this.debugOF = this.mc.debug = stringbuffer.toString();
        }
        StringBuilder stringbuilder = new StringBuilder();
        TextureMap texturemap = Config.getTextureMap();
        stringbuilder.append(", A: ");
        if (SmartAnimations.isActive()) {
            stringbuilder.append(texturemap.getCountAnimationsActive() + TextureAnimations.getCountAnimationsActive());
            stringbuilder.append("/");
        }
        stringbuilder.append(texturemap.getCountAnimations() + TextureAnimations.getCountAnimations());
        String s1 = stringbuilder.toString();
        if (this.isReducedDebug()) {
            return Lists.newArrayList((Object[])new String[]{"Minecraft 1.8.9 (" + this.mc.getVersion() + "/" + ClientBrandRetriever.getClientModName() + ")", this.mc.debug, this.mc.renderGlobal.getDebugInfoRenders(), this.mc.renderGlobal.getDebugInfoEntities(), "P: " + this.mc.effectRenderer.getStatistics() + ". T: " + this.mc.theWorld.getDebugLoadedEntities() + s1, this.mc.theWorld.getProviderName(), "", String.format((String)"Chunk-relative: %d %d %d", (Object[])new Object[]{blockpos.getX() & 0xF, blockpos.getY() & 0xF, blockpos.getZ() & 0xF})});
        }
        Entity entity = this.mc.getRenderViewEntity();
        EnumFacing enumfacing = entity.getHorizontalFacing();
        String s = "Invalid";
        switch (1.$SwitchMap$net$minecraft$util$EnumFacing[enumfacing.ordinal()]) {
            case 1: {
                s = "Towards negative Z";
                break;
            }
            case 2: {
                s = "Towards positive Z";
                break;
            }
            case 3: {
                s = "Towards negative X";
                break;
            }
            case 4: {
                s = "Towards positive X";
            }
        }
        ArrayList list = Lists.newArrayList((Object[])new String[]{"Minecraft 1.8.9 (" + this.mc.getVersion() + "/" + ClientBrandRetriever.getClientModName() + ")", this.mc.debug, this.mc.renderGlobal.getDebugInfoRenders(), this.mc.renderGlobal.getDebugInfoEntities(), "P: " + this.mc.effectRenderer.getStatistics() + ". T: " + this.mc.theWorld.getDebugLoadedEntities() + s1, this.mc.theWorld.getProviderName(), "", String.format((String)"XYZ: %.3f / %.5f / %.3f", (Object[])new Object[]{this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().getEntityBoundingBox().minY, this.mc.getRenderViewEntity().posZ}), String.format((String)"Block: %d %d %d", (Object[])new Object[]{blockpos.getX(), blockpos.getY(), blockpos.getZ()}), String.format((String)"Chunk: %d %d %d in %d %d %d", (Object[])new Object[]{blockpos.getX() & 0xF, blockpos.getY() & 0xF, blockpos.getZ() & 0xF, blockpos.getX() >> 4, blockpos.getY() >> 4, blockpos.getZ() >> 4}), String.format((String)"Facing: %s (%s) (%.1f / %.1f)", (Object[])new Object[]{enumfacing, s, Float.valueOf((float)MathHelper.wrapAngleTo180_float((float)entity.rotationYaw)), Float.valueOf((float)MathHelper.wrapAngleTo180_float((float)entity.rotationPitch))})});
        if (this.mc.theWorld != null && this.mc.theWorld.isBlockLoaded(blockpos)) {
            DifficultyInstance difficultyinstance1;
            EntityPlayerMP entityplayermp;
            Chunk chunk = this.mc.theWorld.getChunkFromBlockCoords(blockpos);
            list.add((Object)("Biome: " + chunk.getBiome((BlockPos)blockpos, (WorldChunkManager)this.mc.theWorld.getWorldChunkManager()).biomeName));
            list.add((Object)("Light: " + chunk.getLightSubtracted(blockpos, 0) + " (" + chunk.getLightFor(EnumSkyBlock.SKY, blockpos) + " sky, " + chunk.getLightFor(EnumSkyBlock.BLOCK, blockpos) + " block)"));
            DifficultyInstance difficultyinstance = this.mc.theWorld.getDifficultyForLocation(blockpos);
            if (this.mc.isIntegratedServerRunning() && this.mc.getIntegratedServer() != null && (entityplayermp = this.mc.getIntegratedServer().getConfigurationManager().getPlayerByUUID(this.mc.thePlayer.getUniqueID())) != null && (difficultyinstance1 = this.mc.getIntegratedServer().getDifficultyAsync(entityplayermp.worldObj, new BlockPos((Entity)entityplayermp))) != null) {
                difficultyinstance = difficultyinstance1;
            }
            list.add((Object)String.format((String)"Local Difficulty: %.2f (Day %d)", (Object[])new Object[]{Float.valueOf((float)difficultyinstance.getAdditionalDifficulty()), this.mc.theWorld.getWorldTime() / 24000L}));
        }
        if (this.mc.entityRenderer != null && this.mc.entityRenderer.isShaderActive()) {
            list.add((Object)("Shader: " + this.mc.entityRenderer.getShaderGroup().getShaderGroupName()));
        }
        if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.mc.objectMouseOver.getBlockPos() != null) {
            BlockPos blockpos1 = this.mc.objectMouseOver.getBlockPos();
            list.add((Object)String.format((String)"Looking at: %d %d %d", (Object[])new Object[]{blockpos1.getX(), blockpos1.getY(), blockpos1.getZ()}));
        }
        return list;
    }

    protected List<String> getDebugInfoRight() {
        long i = Runtime.getRuntime().maxMemory();
        long j = Runtime.getRuntime().totalMemory();
        long k = Runtime.getRuntime().freeMemory();
        long l = j - k;
        ArrayList list = Lists.newArrayList((Object[])new String[]{String.format((String)"Java: %s %dbit", (Object[])new Object[]{System.getProperty((String)"java.version"), this.mc.isJava64bit() ? 64 : 32}), String.format((String)"Mem: % 2d%% %03d/%03dMB", (Object[])new Object[]{l * 100L / i, GuiOverlayDebug.bytesToMb(l), GuiOverlayDebug.bytesToMb(i)}), String.format((String)"Allocated: % 2d%% %03dMB", (Object[])new Object[]{j * 100L / i, GuiOverlayDebug.bytesToMb(j)}), "", String.format((String)"CPU: %s", (Object[])new Object[]{OpenGlHelper.getCpu()}), "", String.format((String)"Display: %dx%d (%s)", (Object[])new Object[]{Display.getWidth(), Display.getHeight(), GL11.glGetString((int)7936)}), GL11.glGetString((int)7937), GL11.glGetString((int)7938)});
        long i1 = NativeMemory.getBufferAllocated();
        long j1 = NativeMemory.getBufferMaximum();
        String s = "Native: " + GuiOverlayDebug.bytesToMb(i1) + "/" + GuiOverlayDebug.bytesToMb(j1) + "MB";
        list.add(4, (Object)s);
        list.set(5, (Object)("GC: " + MemoryMonitor.getAllocationRateMb() + "MB/s"));
        if (Reflector.FMLCommonHandler_getBrandings.exists()) {
            Object object = Reflector.call((ReflectorMethod)Reflector.FMLCommonHandler_instance, (Object[])new Object[0]);
            list.add((Object)"");
            list.addAll((Collection)Reflector.call((Object)object, (ReflectorMethod)Reflector.FMLCommonHandler_getBrandings, (Object[])new Object[]{false}));
        }
        if (this.isReducedDebug()) {
            return list;
        }
        if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.mc.objectMouseOver.getBlockPos() != null) {
            BlockPos blockpos = this.mc.objectMouseOver.getBlockPos();
            IBlockState iblockstate = this.mc.theWorld.getBlockState(blockpos);
            if (this.mc.theWorld.getWorldType() != WorldType.DEBUG_WORLD) {
                iblockstate = iblockstate.getBlock().getActualState(iblockstate, (IBlockAccess)this.mc.theWorld, blockpos);
            }
            list.add((Object)"");
            list.add((Object)String.valueOf((Object)Block.blockRegistry.getNameForObject((Object)iblockstate.getBlock())));
            for (Map.Entry entry : iblockstate.getProperties().entrySet()) {
                String s1 = ((Comparable)entry.getValue()).toString();
                if (entry.getValue() == Boolean.TRUE) {
                    s1 = EnumChatFormatting.GREEN + s1;
                } else if (entry.getValue() == Boolean.FALSE) {
                    s1 = EnumChatFormatting.RED + s1;
                }
                list.add((Object)(((IProperty)entry.getKey()).getName() + ": " + s1));
            }
        }
        return list;
    }

    private void renderLagometer() {
        GlStateManager.disableDepth();
        FrameTimer frametimer = this.mc.getFrameTimer();
        int i = frametimer.getLastIndex();
        int j = frametimer.getIndex();
        long[] along = frametimer.getFrames();
        ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        int k = i;
        int l = 0;
        GuiOverlayDebug.drawRect((int)0, (int)(scaledresolution.getScaledHeight() - 60), (int)240, (int)scaledresolution.getScaledHeight(), (int)-1873784752);
        while (k != j) {
            int i1 = frametimer.getLagometerValue(along[k], 30);
            int j1 = this.getFrameColor(MathHelper.clamp_int((int)i1, (int)0, (int)60), 0, 30, 60);
            this.drawVerticalLine(l, scaledresolution.getScaledHeight(), scaledresolution.getScaledHeight() - i1, j1);
            ++l;
            k = frametimer.parseIndex(k + 1);
        }
        GuiOverlayDebug.drawRect((int)1, (int)(scaledresolution.getScaledHeight() - 30 + 1), (int)14, (int)(scaledresolution.getScaledHeight() - 30 + 10), (int)-1873784752);
        this.fontRenderer.drawString("60", 2, scaledresolution.getScaledHeight() - 30 + 2, 0xE0E0E0);
        this.drawHorizontalLine(0, 239, scaledresolution.getScaledHeight() - 30, -1);
        GuiOverlayDebug.drawRect((int)1, (int)(scaledresolution.getScaledHeight() - 60 + 1), (int)14, (int)(scaledresolution.getScaledHeight() - 60 + 10), (int)-1873784752);
        this.fontRenderer.drawString("30", 2, scaledresolution.getScaledHeight() - 60 + 2, 0xE0E0E0);
        this.drawHorizontalLine(0, 239, scaledresolution.getScaledHeight() - 60, -1);
        this.drawHorizontalLine(0, 239, scaledresolution.getScaledHeight() - 1, -1);
        this.drawVerticalLine(0, scaledresolution.getScaledHeight() - 60, scaledresolution.getScaledHeight(), -1);
        this.drawVerticalLine(239, scaledresolution.getScaledHeight() - 60, scaledresolution.getScaledHeight(), -1);
        if (this.mc.gameSettings.limitFramerate <= 120) {
            this.drawHorizontalLine(0, 239, scaledresolution.getScaledHeight() - 60 + this.mc.gameSettings.limitFramerate / 2, -16711681);
        }
        GlStateManager.enableDepth();
    }

    private int getFrameColor(int p_181552_1_, int p_181552_2_, int p_181552_3_, int p_181552_4_) {
        return p_181552_1_ < p_181552_3_ ? this.blendColors(-16711936, -256, (float)p_181552_1_ / (float)p_181552_3_) : this.blendColors(-256, -65536, (float)(p_181552_1_ - p_181552_3_) / (float)(p_181552_4_ - p_181552_3_));
    }

    private int blendColors(int p_181553_1_, int p_181553_2_, float p_181553_3_) {
        int i = p_181553_1_ >> 24 & 0xFF;
        int j = p_181553_1_ >> 16 & 0xFF;
        int k = p_181553_1_ >> 8 & 0xFF;
        int l = p_181553_1_ & 0xFF;
        int i1 = p_181553_2_ >> 24 & 0xFF;
        int j1 = p_181553_2_ >> 16 & 0xFF;
        int k1 = p_181553_2_ >> 8 & 0xFF;
        int l1 = p_181553_2_ & 0xFF;
        int i2 = MathHelper.clamp_int((int)((int)((float)i + (float)(i1 - i) * p_181553_3_)), (int)0, (int)255);
        int j2 = MathHelper.clamp_int((int)((int)((float)j + (float)(j1 - j) * p_181553_3_)), (int)0, (int)255);
        int k2 = MathHelper.clamp_int((int)((int)((float)k + (float)(k1 - k) * p_181553_3_)), (int)0, (int)255);
        int l2 = MathHelper.clamp_int((int)((int)((float)l + (float)(l1 - l) * p_181553_3_)), (int)0, (int)255);
        return i2 << 24 | j2 << 16 | k2 << 8 | l2;
    }

    private static long bytesToMb(long bytes) {
        return bytes / 1024L / 1024L;
    }
}
