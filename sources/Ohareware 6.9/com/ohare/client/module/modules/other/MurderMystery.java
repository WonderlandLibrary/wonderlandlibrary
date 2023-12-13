package com.ohare.client.module.modules.other;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.ohare.client.event.events.player.UpdateEvent;
import com.ohare.client.event.events.render.Render3DEvent;
import com.ohare.client.event.events.world.PacketEvent;
import com.ohare.client.module.Module;
import com.ohare.client.utils.GLUtil;
import com.ohare.client.utils.Printer;
import com.ohare.client.utils.RenderUtil;

import dorkbox.messageBus.annotations.Subscribe;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBed;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemPotion;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;

public class MurderMystery extends Module {
    public ArrayList<EntityPlayer> murderers = new ArrayList<>();
    public ArrayList<EntityPlayer> gudpeople = new ArrayList<>();
    private final String[] nigga = new String[]{"1st Killer - ", "1st Place - ", "Winner: ", " - Damage Dealt - ", "1st - ", "Winning Team - ", "Winners: ", "Winner: ", "Winning Team: ", " win the game!", "Top Seeker: ","1st Place: ","Last team standing!","Winner #1 (", "Top Survivors","Winners - "};

    public MurderMystery() {
        super("MurderMystery", Category.OTHER, new Color(62, 131, 227, 255).getRGB());
        setRenderlabel("Murder Mystery");
        setDescription("Shows all murderers and good guys.");
    }

    @Override
    public void onDisable() {
        murderers.clear();
        gudpeople.clear();
    }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        for (EntityPlayer murderer : murderers) {
            double x = murderer.lastTickPosX + (murderer.posX - murderer.lastTickPosX) * event.getPartialTicks();
            double y = murderer.lastTickPosY + (murderer.posY - murderer.lastTickPosY) * event.getPartialTicks();
            double z = murderer.lastTickPosZ + (murderer.posZ - murderer.lastTickPosZ) * event.getPartialTicks();
			drawEntityESP(x - RenderManager.renderPosX, y - RenderManager.renderPosY, z - RenderManager.renderPosZ, murderer.height - 0.1, murderer.width - 0.12,new Color(0xE33726));
        }
        for (EntityPlayer good : gudpeople) {
            double x = good.lastTickPosX + (good.posX - good.lastTickPosX) * event.getPartialTicks();
            double y = good.lastTickPosY + (good.posY - good.lastTickPosY) * event.getPartialTicks();
            double z = good.lastTickPosZ + (good.posZ - good.lastTickPosZ) * event.getPartialTicks();
			drawEntityESP(x - RenderManager.renderPosX, y - RenderManager.renderPosY, z - RenderManager.renderPosZ, good.height - 0.1, good.width - 0.12,new Color(0x3E83E3));
        }
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (mc.getCurrentServerData() != null && mc.theWorld != null) {
            for (Object entities : mc.theWorld.loadedEntityList) {
                if (entities instanceof EntityPlayer) {
                    EntityPlayer entity = (EntityPlayer) entities;
                    if (murderers.contains(entity) && !entity.isEntityAlive()) {
                        murderers.remove(entity);
                    }
                    if (gudpeople.contains(entity) && !entity.isEntityAlive()) {
                        gudpeople.remove(entity);
                    }
                    if (entity != mc.thePlayer && !entity.isDead) {
                        if (!murderers.contains(entity)) {
                            if (entity.getHeldItem() != null) {
                                if (!(entity.getHeldItem().getItem() instanceof ItemMap) && !(entity.getHeldItem().getItem() instanceof ItemBow) && !(entity.getHeldItem().getItem() instanceof ItemBed) && entity.getHeldItem().getItem() != Items.gold_ingot && entity.getHeldItem().getItem() != Items.arrow && entity.getHeldItem().getItem() != Items.dye && !(entity.getHeldItem().getItem() instanceof ItemPotion) && !(entity.getHeldItem().getItem() instanceof ItemBlock)) {
                                    Printer.print(EnumChatFormatting.RED + entity.getName() + " might be a murderer watch out!");
                                    murderers.add(entity);
                                }
                            }
                        }
                        if (!gudpeople.contains(entity) && !murderers.contains(entity)) {
                            if (entity.getHeldItem() != null) {
                                if (entity.getHeldItem().getItem() instanceof ItemBow) {
                                    Printer.print(EnumChatFormatting.BLUE + entity.getName() + " is a good guy.");
                                    gudpeople.add(entity);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (mc.thePlayer.isDead) {
            gudpeople.clear();
            murderers.clear();
        }
    }
    @Subscribe
    public void onPacket(PacketEvent event) {
        if (!event.isSending()) {
            if (event.getPacket() instanceof S02PacketChat) {
                S02PacketChat packet = (S02PacketChat) event.getPacket();
                for (String str : nigga) {
                    if (packet.getChatComponent().getUnformattedText().contains(str)) {
                        gudpeople.clear();
                        murderers.clear();
                    }
                }
            }
        }
    }
    private void drawEntityESP(double x, double y, double z, double height, double width, Color color) {
        GL11.glPushMatrix();
        GLUtil.setGLCap(3042, true);
        GLUtil.setGLCap(3553, false);
        GLUtil.setGLCap(2896, false);
        GLUtil.setGLCap(2929, false);
        GL11.glDepthMask(false);
        GL11.glLineWidth(1.8f);
        GL11.glBlendFunc(770, 771);
        GLUtil.setGLCap(2848, true);
        GL11.glDepthMask(true);
        RenderUtil.BB(new AxisAlignedBB(x - width, y + 0.1, z - width, x + width, y + height + 0.25, z + width), new Color(color.getRed(), color.getGreen(), color.getBlue(), 120).getRGB());
        RenderUtil.OutlinedBB(new AxisAlignedBB(x - width, y + 0.1, z - width, x + width, y + height + 0.25, z + width), 1, color.getRGB());
        GLUtil.revertAllCaps();
        GL11.glPopMatrix();
        GL11.glColor4f(1, 1, 1, 1);
    }
}