package com.ohare.client.module.modules.visuals.hudcomps.comps;

import com.ohare.client.Client;
import com.ohare.client.event.events.render.Render2DEvent;
import com.ohare.client.module.modules.combat.AntiBot;
import com.ohare.client.module.modules.visuals.hudcomps.HudComp;
import com.ohare.client.utils.RenderUtil;
import com.ohare.client.utils.TimerUtil;
import com.ohare.client.utils.value.impl.BooleanValue;
import net.minecraft.block.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.Chunk;
import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * made by oHare for oHareWare
 *
 * @since 5/31/2019
 **/
public class RadarComp extends HudComp {
    private ScaledResolution sr = RenderUtil.getResolution();
    public List<MiniMapBlock> blocks = new ArrayList<MiniMapBlock>();;
    private TimerUtil updateTimer = new TimerUtil();
    private BufferedImage img = new BufferedImage(82, 83, 2);
    private Minecraft mc = Minecraft.getMinecraft();

    private BooleanValue terrain = new BooleanValue("Terrain",false);

    public RadarComp() {
        super("RadarComp", 4, 157, 100, 100);
        addValues(terrain);
    }

    @Override
    public void onRender(ScaledResolution scaledResolution) {
        if (Minecraft.getMinecraft().gameSettings.showDebugInfo || Minecraft.getMinecraft().thePlayer == null) return;

        if(terrain.isEnabled()) {
            renderTerrain();
        } else {
            renderFlat();
        }

        if (getX() + getWidth() > scaledResolution.getScaledWidth())
            setX(scaledResolution.getScaledWidth() - getWidth() - 2);
        if (getX() < 0) setX(0);
        if (getY() + getHeight() > scaledResolution.getScaledHeight())
            setY(scaledResolution.getScaledHeight() - getHeight() - 2);
        if (getY() < 0) setY(0);
    }

    public void renderFlat() {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        float width = 100;
        float height = 100;
        setWidth(width);
        setHeight(height);
        GL11.glPushMatrix();
        GlStateManager.translate(getX(), getY(), 0);
        RenderUtil.drawRect(-0.5, -0.5, width + 1, height + 1, new Color(5, 5, 5, 255).getRGB());
        RenderUtil.drawBorderedRect(0, 0, width, height, 1f, new Color(26, 31, 41, 255).getRGB(), new Color(31, 38, 48, 255).getRGB());
        RenderUtil.drawRect(1.5, 1.5, width - 3, height - 3, new Color(38, 46, 52, 255).getRGB());
        RenderUtil.drawRect(width / 2 - 1, height / 2 - 1, 1, 1, new Color(0, 107, 214, 255).getRGB());
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtil.prepareScissorBox(scaledResolution, (float) getX() + 2, (float) getY() + 2, (float) getWidth() - 4, (float) getHeight() - 4);
        GlStateManager.pushMatrix();
        GL11.glTranslated(width / 2, height / 2, 0);
        GL11.glRotated(180 - player.rotationYaw, 0, 0, 1);

        Minecraft.getMinecraft().theWorld.playerEntities.forEach(ent -> {
            if (ent == player) return;
            final double difX = ent.prevPosX + (ent.posX - ent.prevPosX) * Minecraft.getMinecraft().timer.renderPartialTicks - (player.prevPosX + (player.posX - player.prevPosX) * Minecraft.getMinecraft().timer.renderPartialTicks);
            final double difZ = ent.prevPosZ + (ent.posZ - ent.prevPosZ) * Minecraft.getMinecraft().timer.renderPartialTicks - (player.prevPosZ + (player.posZ - player.prevPosZ) * Minecraft.getMinecraft().timer.renderPartialTicks);
            final double distance = Math.sqrt(difX * difX + difZ * difZ) * (getWidth() * 0.5 / width);
            final double rots = Math.toDegrees(Math.atan2(difZ, difX));
            final double angle = Math.toRadians(180 - rots - 90);
            final double renderX = Math.sin(angle) * distance;
            final double renderY = Math.cos(angle) * distance;
            RenderUtil.drawRect(renderX, renderY, 1, 1, getColor(ent));
        });
        GlStateManager.popMatrix();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glPopMatrix();
    }

    private void renderTerrain() {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        int drawX = 0;
        int drawY = 0;
        setWidth(83);
        setHeight(83);
        Gui.drawRect((int)(x - 1.5), (int)(y - 1.5), (int)(x + 84.5), (int)(y + 85.5),  new Color(26, 31, 41, 255).getRGB());
        Gui.drawRect(x, y, x + 82, y + 83, -4532256);

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtil.prepareScissorBox(scaledResolution,(float) getX(), (float) getY() , (float) getWidth() - 1, (float) getHeight() + 1);

        if (this.updateTimer.reach(100) && mc.theWorld != null) {
            this.blocks.clear();
            for (int i = (int)mc.thePlayer.posX - 41; i < (int)mc.thePlayer.posX + 41; ++i) {
                for (int i2 = (int)mc.thePlayer.posZ - 41; i2 < (int)mc.thePlayer.posZ + 42; ++i2) {
                    Block topBlock = null;
                    BlockPos topBlockPos = null;
                    boolean foundBlock = false;
                    Chunk chunk = mc.theWorld.getChunkFromBlockCoords(new BlockPos(i, 0, i2));
                    if (chunk != null) {
                        final BlockPos pos = new BlockPos(i, chunk.getHeight(new BlockPos(i, 0, i2)) - 1, i2);
                        if (!foundBlock) {
                            topBlock = mc.theWorld.getBlock(pos.getX(),pos.getY(),pos.getZ());
                            topBlockPos = pos;
                            foundBlock = true;
                        }
                    }
                    if (foundBlock) {
                        Block northBlock = mc.theWorld.getBlock(topBlockPos.getX(), topBlockPos.getY(), topBlockPos.getZ() - 1);
                        Block southBlock = mc.theWorld.getBlock(topBlockPos.getX(), topBlockPos.getY(), topBlockPos.getZ() + 1);
                        Block eastBlock = mc.theWorld.getBlock(topBlockPos.getX() + 1, topBlockPos.getY(), topBlockPos.getZ());
                        Block westBlock = mc.theWorld.getBlock(topBlockPos.getX() - 1, topBlockPos.getY(), topBlockPos.getZ());
                        int color = topBlock.getMapColor(mc.theWorld.getBlockState(new BlockPos(topBlockPos.getX(), topBlockPos.getY(), topBlockPos.getZ()))).colorValue;
                        if (topBlock instanceof BlockGrass || topBlock instanceof BlockTallGrass || topBlock instanceof BlockLeaves || topBlock instanceof BlockRedstoneLight
                                || topBlock instanceof BlockRailPowered || topBlock instanceof BlockBeacon || topBlock instanceof BlockPistonBase ||
                                topBlock instanceof BlockSlime || topBlock instanceof BlockAir) {
                            if (topBlock instanceof BlockGrass) {
                                color = topBlock.getBlockColor();
                            }
                            if (topBlock instanceof BlockLeaves) {
                                color = topBlock.getBlockColor();
                            }
                            if (topBlock instanceof BlockTallGrass) {
                                color = topBlock.getBlockColor();
                            }
                            if(topBlock instanceof BlockRedstoneLight) {
                                BlockRedstoneLight redstoneLight = (BlockRedstoneLight) topBlock;
                                if(redstoneLight.isOn()) {
                                    color = new Color(0xFF001C).getRGB();
                                } else {
                                    color = new Color(0x141414).getRGB();
                                }
                            }
                            if(topBlock instanceof BlockRailPowered) {
                                color = new Color(0xD66D6F).getRGB();
                            }
                            if(topBlock instanceof BlockBeacon) {
                                color = new Color(0x1BA780).getRGB();
                            }
                            if(topBlock instanceof BlockPistonBase) {
                                color = topBlock.getBlockColor();
                            }
                            if(topBlock instanceof BlockSlime) {
                                color = new Color(0x3EFF3E).getRGB();
                            }
                            if(topBlock instanceof BlockAir) {
                                color = new Color(0xFF89b0f9).getRGB();
                            }
                        }
                        BlockPos posLol = new BlockPos(i, chunk.getHeight(new BlockPos(i, 0, i2)), i2);
                        Block blockLol = mc.theWorld.getBlock(posLol.getX(),posLol.getY(),posLol.getZ());
                        if (!(blockLol instanceof BlockAir) && !(blockLol instanceof BlockTorch)) {
                            color = blockLol.getMapColor(mc.theWorld.getBlockState(new BlockPos(posLol.getX(),posLol.getY(),posLol.getZ()))).colorValue;
                        }
                        if ((southBlock instanceof BlockAir || eastBlock instanceof BlockAir) && !(northBlock instanceof BlockAir) && !(westBlock instanceof BlockAir)) {
                            color = new Color(color).darker().getRGB();
                        }
                        if ((northBlock instanceof BlockAir || westBlock instanceof BlockAir) && !(southBlock instanceof BlockAir) && !(eastBlock instanceof BlockAir)) {
                            Color color2 = new Color(color);
                            int red = (int)Math.round(Math.min(255.0, color2.getRed() + 38.25));
                            int green = (int)Math.round(Math.min(255.0, color2.getGreen() + 38.25));
                            int blue = (int)Math.round(Math.min(255.0, color2.getBlue() + 38.25));
                            color = new Color(red, green, blue, 255).getRGB();
                        }
                        blocks.add(new MiniMapBlock(drawX, drawY, color));
                    }
                    ++drawY;
                }
                drawY = 0;
                ++drawX;
            }

            if (!blocks.isEmpty()) blocks.forEach(block ->   img.setRGB(block.getX(), block.getY(), 0xFF000000 | block.getC()));

            updateTimer.reset();
        }
        GL11.glPushMatrix();
        TextureUtil.uploadTextureImage(888, img);
        TextureUtil.bindTexture(888);
        Gui.drawScaledCustomSizeModalRect(x, y, 0.0f, 0.0f, 82, 83, 82, 83, 82.0f, 83.0f);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslated(x + 40, y + 42.0, 0.0);

        //enemy niggers

        mc.theWorld.playerEntities.forEach(ent -> {
            if (ent != mc.thePlayer && !AntiBot.getBots().contains(ent)) {
                double tx = RenderUtil.interpolate((float) ent.prevPosX, (float) ent.posX, mc.timer.renderPartialTicks);
                double tz = RenderUtil.interpolate((float) ent.prevPosZ, (float) ent.posZ, mc.timer.renderPartialTicks);
                double px = RenderUtil.interpolate((float) mc.thePlayer.prevPosX, (float) mc.thePlayer.posX, mc.timer.renderPartialTicks);
                double pz = RenderUtil.interpolate((float) mc.thePlayer.prevPosZ, (float) mc.thePlayer.posZ, mc.timer.renderPartialTicks);
                double renderX = tx - px;
                double renderY = tz - pz - 1;
                Gui.drawRect(renderX,renderY,renderX + 1f,renderY + 1f,getColor(ent));
            }
        });


        //the player
        GL11.glRotatef(mc.thePlayer.rotationYaw, 0, 0, 1);

        /* Keep here can be used to do good positioning*/
//        Gui.drawRect(-1, 1, 1, (int)3.5, -16777216);
//        Gui.drawRect(-2, -2, 2, 2, -16777216);
//        Gui.drawRect((int)-0.5, 1, (int)0.5, 3, -1);
//        Gui.drawRect((int)-1.5, (int)-1.5, (int)1.5, (int)1.5, -1);

        RenderUtil.drawArrow(-3.9f,-2,-1);

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glPopMatrix();

    }

    @Override
    public void onResize(ScaledResolution scaledResolution) {
        if (sr.getScaledWidth() < scaledResolution.getScaledWidth() && getX() > sr.getScaledWidth() - getWidth() - 20) {
            setX(scaledResolution.getScaledWidth() - getWidth() - 2);
        }
        if (sr.getScaledHeight() < scaledResolution.getScaledHeight() && getY() > sr.getScaledHeight() - getHeight() - 20) {
            setY(scaledResolution.getScaledHeight() - getHeight() - 2);
        }
        if (sr.getScaledHeight() != scaledResolution.getScaledHeight()) {
            sr = scaledResolution;
        }
    }

    @Override
    public void onFullScreen(float w, float h) {
        if (sr.getScaledWidth() < w && getX() > sr.getScaledWidth() - getWidth() - 20) {
            setX(w - (sr.getScaledWidth() - getWidth()) - 2);
        }
        if (sr.getScaledHeight() < h && getY() > sr.getScaledHeight() - getHeight() - 20) {
            setY(h - (sr.getScaledHeight() - getHeight()) - 2);
        }
        if (sr.getScaledHeight() != new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight()) {
            sr = new ScaledResolution(Minecraft.getMinecraft());
        }
    }

    private int getColor(EntityLivingBase ent) {
        if (Client.INSTANCE.getFriendManager().isFriend(ent.getName())) return new Color(122, 190, 255).getRGB();
        else if (ent.getName().equals(Minecraft.getMinecraft().thePlayer.getName())) return new Color(0xFF99ff99).getRGB();
        return new Color(255,0,0).getRGB();
    }

    public class MiniMapBlock {
        private int x,y,c;

        public MiniMapBlock(int x, int y, int c) {
            this.x = x;
            this.y = y;
            this.c = c;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getC() {
            return c;
        }
    }

}
