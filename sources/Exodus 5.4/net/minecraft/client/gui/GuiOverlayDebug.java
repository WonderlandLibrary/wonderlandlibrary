/*
 * Decompiled with CFR 0.152.
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
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.FrameTimer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3i;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.Chunk;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class GuiOverlayDebug
extends Gui {
    private final Minecraft mc;
    private final FontRenderer fontRenderer;

    private int func_181553_a(int n, int n2, float f) {
        int n3 = n >> 24 & 0xFF;
        int n4 = n >> 16 & 0xFF;
        int n5 = n >> 8 & 0xFF;
        int n6 = n & 0xFF;
        int n7 = n2 >> 24 & 0xFF;
        int n8 = n2 >> 16 & 0xFF;
        int n9 = n2 >> 8 & 0xFF;
        int n10 = n2 & 0xFF;
        int n11 = MathHelper.clamp_int((int)((float)n3 + (float)(n7 - n3) * f), 0, 255);
        int n12 = MathHelper.clamp_int((int)((float)n4 + (float)(n8 - n4) * f), 0, 255);
        int n13 = MathHelper.clamp_int((int)((float)n5 + (float)(n9 - n5) * f), 0, 255);
        int n14 = MathHelper.clamp_int((int)((float)n6 + (float)(n10 - n6) * f), 0, 255);
        return n11 << 24 | n12 << 16 | n13 << 8 | n14;
    }

    protected List<String> call() {
        Object object;
        BlockPos blockPos = new BlockPos(this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().getEntityBoundingBox().minY, this.mc.getRenderViewEntity().posZ);
        if (this.isReducedDebug()) {
            return Lists.newArrayList((Object[])new String[]{"Minecraft 1.8.8 (" + this.mc.getVersion() + "/" + ClientBrandRetriever.getClientModName() + ")", this.mc.debug, this.mc.renderGlobal.getDebugInfoRenders(), this.mc.renderGlobal.getDebugInfoEntities(), "P: " + this.mc.effectRenderer.getStatistics() + ". T: " + Minecraft.theWorld.getDebugLoadedEntities(), Minecraft.theWorld.getProviderName(), "", String.format("Chunk-relative: %d %d %d", blockPos.getX() & 0xF, blockPos.getY() & 0xF, blockPos.getZ() & 0xF)});
        }
        Entity entity = this.mc.getRenderViewEntity();
        EnumFacing enumFacing = entity.getHorizontalFacing();
        String string = "Invalid";
        switch (enumFacing) {
            case NORTH: {
                string = "Towards negative Z";
                break;
            }
            case SOUTH: {
                string = "Towards positive Z";
                break;
            }
            case WEST: {
                string = "Towards negative X";
                break;
            }
            case EAST: {
                string = "Towards positive X";
            }
        }
        ArrayList arrayList = Lists.newArrayList((Object[])new String[]{"Minecraft 1.8.8 (" + this.mc.getVersion() + "/" + ClientBrandRetriever.getClientModName() + ")", this.mc.debug, this.mc.renderGlobal.getDebugInfoRenders(), this.mc.renderGlobal.getDebugInfoEntities(), "P: " + this.mc.effectRenderer.getStatistics() + ". T: " + Minecraft.theWorld.getDebugLoadedEntities(), Minecraft.theWorld.getProviderName(), "", String.format("XYZ: %.3f / %.5f / %.3f", this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().getEntityBoundingBox().minY, this.mc.getRenderViewEntity().posZ), String.format("Block: %d %d %d", blockPos.getX(), blockPos.getY(), blockPos.getZ()), String.format("Chunk: %d %d %d in %d %d %d", blockPos.getX() & 0xF, blockPos.getY() & 0xF, blockPos.getZ() & 0xF, blockPos.getX() >> 4, blockPos.getY() >> 4, blockPos.getZ() >> 4), String.format("Facing: %s (%s) (%.1f / %.1f)", enumFacing, string, Float.valueOf(MathHelper.wrapAngleTo180_float(entity.rotationYaw)), Float.valueOf(MathHelper.wrapAngleTo180_float(entity.rotationPitch)))});
        if (Minecraft.theWorld != null && Minecraft.theWorld.isBlockLoaded(blockPos)) {
            EntityPlayerMP entityPlayerMP;
            object = Minecraft.theWorld.getChunkFromBlockCoords(blockPos);
            arrayList.add("Biome: " + ((Chunk)object).getBiome((BlockPos)blockPos, (WorldChunkManager)Minecraft.theWorld.getWorldChunkManager()).biomeName);
            arrayList.add("Light: " + ((Chunk)object).getLightSubtracted(blockPos, 0) + " (" + ((Chunk)object).getLightFor(EnumSkyBlock.SKY, blockPos) + " sky, " + ((Chunk)object).getLightFor(EnumSkyBlock.BLOCK, blockPos) + " block)");
            DifficultyInstance difficultyInstance = Minecraft.theWorld.getDifficultyForLocation(blockPos);
            if (this.mc.isIntegratedServerRunning() && this.mc.getIntegratedServer() != null && (entityPlayerMP = this.mc.getIntegratedServer().getConfigurationManager().getPlayerByUUID(Minecraft.thePlayer.getUniqueID())) != null) {
                difficultyInstance = entityPlayerMP.worldObj.getDifficultyForLocation(new BlockPos(entityPlayerMP));
            }
            arrayList.add(String.format("Local Difficulty: %.2f (Day %d)", Float.valueOf(difficultyInstance.getAdditionalDifficulty()), Minecraft.theWorld.getWorldTime() / 24000L));
        }
        if (this.mc.entityRenderer != null && this.mc.entityRenderer.isShaderActive()) {
            arrayList.add("Shader: " + this.mc.entityRenderer.getShaderGroup().getShaderGroupName());
        }
        if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.mc.objectMouseOver.getBlockPos() != null) {
            object = this.mc.objectMouseOver.getBlockPos();
            arrayList.add(String.format("Looking at: %d %d %d", ((Vec3i)object).getX(), ((Vec3i)object).getY(), ((Vec3i)object).getZ()));
        }
        return arrayList;
    }

    private int func_181552_c(int n, int n2, int n3, int n4) {
        return n < n3 ? this.func_181553_a(-16711936, -256, (float)n / (float)n3) : this.func_181553_a(-256, -65536, (float)(n - n3) / (float)(n4 - n3));
    }

    private void func_181554_e() {
        GlStateManager.disableDepth();
        FrameTimer frameTimer = this.mc.func_181539_aj();
        int n = frameTimer.func_181749_a();
        int n2 = frameTimer.func_181750_b();
        long[] lArray = frameTimer.func_181746_c();
        ScaledResolution scaledResolution = new ScaledResolution(this.mc);
        int n3 = n;
        int n4 = 0;
        GuiOverlayDebug.drawRect(0.0, scaledResolution.getScaledHeight() - 60, 240.0, scaledResolution.getScaledHeight(), -1873784752);
        while (n3 != n2) {
            int n5 = frameTimer.func_181748_a(lArray[n3], 30);
            int n6 = this.func_181552_c(MathHelper.clamp_int(n5, 0, 60), 0, 30, 60);
            this.drawVerticalLine(n4, scaledResolution.getScaledHeight(), scaledResolution.getScaledHeight() - n5, n6);
            ++n4;
            n3 = frameTimer.func_181751_b(n3 + 1);
        }
        GuiOverlayDebug.drawRect(1.0, scaledResolution.getScaledHeight() - 30 + 1, 14.0, scaledResolution.getScaledHeight() - 30 + 10, -1873784752);
        this.fontRenderer.drawString("60", 2.0, scaledResolution.getScaledHeight() - 30 + 2, 0xE0E0E0);
        GuiOverlayDebug.drawHorizontalLine(0, 239, scaledResolution.getScaledHeight() - 30, -1);
        GuiOverlayDebug.drawRect(1.0, scaledResolution.getScaledHeight() - 60 + 1, 14.0, scaledResolution.getScaledHeight() - 60 + 10, -1873784752);
        this.fontRenderer.drawString("30", 2.0, scaledResolution.getScaledHeight() - 60 + 2, 0xE0E0E0);
        GuiOverlayDebug.drawHorizontalLine(0, 239, scaledResolution.getScaledHeight() - 60, -1);
        GuiOverlayDebug.drawHorizontalLine(0, 239, scaledResolution.getScaledHeight() - 1, -1);
        this.drawVerticalLine(0, scaledResolution.getScaledHeight() - 60, scaledResolution.getScaledHeight(), -1);
        this.drawVerticalLine(239, scaledResolution.getScaledHeight() - 60, scaledResolution.getScaledHeight(), -1);
        if (Minecraft.gameSettings.limitFramerate <= 120) {
            GuiOverlayDebug.drawHorizontalLine(0, 239, scaledResolution.getScaledHeight() - 60 + Minecraft.gameSettings.limitFramerate / 2, -16711681);
        }
        GlStateManager.enableDepth();
    }

    private boolean isReducedDebug() {
        return Minecraft.thePlayer.hasReducedDebug() || Minecraft.gameSettings.reducedDebugInfo;
    }

    protected List<String> getDebugInfoRight() {
        long l = Runtime.getRuntime().maxMemory();
        long l2 = Runtime.getRuntime().totalMemory();
        long l3 = Runtime.getRuntime().freeMemory();
        long l4 = l2 - l3;
        ArrayList arrayList = Lists.newArrayList((Object[])new String[]{String.format("Java: %s %dbit", System.getProperty("java.version"), this.mc.isJava64bit() ? 64 : 32), String.format("Mem: % 2d%% %03d/%03dMB", l4 * 100L / l, GuiOverlayDebug.bytesToMb(l4), GuiOverlayDebug.bytesToMb(l)), String.format("Allocated: % 2d%% %03dMB", l2 * 100L / l, GuiOverlayDebug.bytesToMb(l2)), "", String.format("CPU: %s", OpenGlHelper.func_183029_j()), "", String.format("Display: %dx%d (%s)", Display.getWidth(), Display.getHeight(), GL11.glGetString((int)7936)), GL11.glGetString((int)7937), GL11.glGetString((int)7938)});
        if (this.isReducedDebug()) {
            return arrayList;
        }
        if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.mc.objectMouseOver.getBlockPos() != null) {
            BlockPos blockPos = this.mc.objectMouseOver.getBlockPos();
            IBlockState iBlockState = Minecraft.theWorld.getBlockState(blockPos);
            if (Minecraft.theWorld.getWorldType() != WorldType.DEBUG_WORLD) {
                iBlockState = iBlockState.getBlock().getActualState(iBlockState, Minecraft.theWorld, blockPos);
            }
            arrayList.add("");
            arrayList.add(String.valueOf(Block.blockRegistry.getNameForObject(iBlockState.getBlock())));
            for (Map.Entry entry : iBlockState.getProperties().entrySet()) {
                String string = ((Comparable)entry.getValue()).toString();
                if (entry.getValue() == Boolean.TRUE) {
                    string = (Object)((Object)EnumChatFormatting.GREEN) + string;
                } else if (entry.getValue() == Boolean.FALSE) {
                    string = (Object)((Object)EnumChatFormatting.RED) + string;
                }
                arrayList.add(String.valueOf(((IProperty)entry.getKey()).getName()) + ": " + string);
            }
        }
        return arrayList;
    }

    protected void renderDebugInfoLeft() {
        List<String> list = this.call();
        int n = 0;
        while (n < list.size()) {
            String string = list.get(n);
            if (!Strings.isNullOrEmpty((String)string)) {
                int n2 = this.fontRenderer.FONT_HEIGHT;
                int n3 = this.fontRenderer.getStringWidth(string);
                int n4 = 2;
                int n5 = 2 + n2 * n;
                GuiOverlayDebug.drawRect(1.0, n5 - 1, 2 + n3 + 1, n5 + n2 - 1, -1873784752);
                this.fontRenderer.drawString(string, 2.0, n5, 0xE0E0E0);
            }
            ++n;
        }
    }

    private static long bytesToMb(long l) {
        return l / 1024L / 1024L;
    }

    protected void renderDebugInfoRight(ScaledResolution scaledResolution) {
        List<String> list = this.getDebugInfoRight();
        int n = 0;
        while (n < list.size()) {
            String string = list.get(n);
            if (!Strings.isNullOrEmpty((String)string)) {
                int n2 = this.fontRenderer.FONT_HEIGHT;
                int n3 = this.fontRenderer.getStringWidth(string);
                int n4 = scaledResolution.getScaledWidth() - 2 - n3;
                int n5 = 2 + n2 * n;
                GuiOverlayDebug.drawRect(n4 - 1, n5 - 1, n4 + n3 + 1, n5 + n2 - 1, -1873784752);
                this.fontRenderer.drawString(string, n4, n5, 0xE0E0E0);
            }
            ++n;
        }
    }

    public void renderDebugInfo(ScaledResolution scaledResolution) {
        this.mc.mcProfiler.startSection("debug");
        GlStateManager.pushMatrix();
        this.renderDebugInfoLeft();
        this.renderDebugInfoRight(scaledResolution);
        GlStateManager.popMatrix();
        if (Minecraft.gameSettings.field_181657_aC) {
            this.func_181554_e();
        }
        this.mc.mcProfiler.endSection();
    }

    public GuiOverlayDebug(Minecraft minecraft) {
        this.mc = minecraft;
        this.fontRenderer = Minecraft.fontRendererObj;
    }
}

