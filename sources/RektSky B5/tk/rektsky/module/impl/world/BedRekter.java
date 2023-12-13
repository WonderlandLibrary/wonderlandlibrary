/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.world;

import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import tk.rektsky.Client;
import tk.rektsky.event.Event;
import tk.rektsky.event.impl.PacketReceiveEvent;
import tk.rektsky.event.impl.PacketSentEvent;
import tk.rektsky.event.impl.RenderEvent;
import tk.rektsky.event.impl.WorldTickEvent;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;
import tk.rektsky.module.ModulesManager;
import tk.rektsky.module.impl.exploits.Disabler;
import tk.rektsky.module.impl.render.Notification;
import tk.rektsky.module.settings.BooleanSetting;
import tk.rektsky.utils.block.BlockUtils;
import tk.rektsky.utils.combat.RotationUtil;
import tk.rektsky.utils.display.ColorUtil;

public class BedRekter
extends Module {
    float range;
    int ticks;
    int targetTick;
    public float shouldDamage;
    public float damage;
    public BlockPos target;
    public BlockPos playerBed;
    public BlockPos playerBedHead;
    public Long lastHudRenderTime;
    public Long firstHudRenderTime;
    String targetBlock = "tile.bed";
    boolean enable = true;
    public BooleanSetting espSetting = new BooleanSetting("ESP", true);
    public BooleanSetting hudSetting = new BooleanSetting("HUD", true);

    public BedRekter() {
        super("BedRekter", "Automatically destroys beds around you.", 0, Category.WORLD);
    }

    @Override
    public void onDisable() {
        RotationUtil.setRotating("bedrekter", false);
    }

    @Override
    public void onEnable() {
        this.ticks = 0;
        this.target = null;
        this.searchForPlayerBed();
    }

    public void doWarning(String msg) {
        Client.notify(new Notification.PopupMessage("BedRekter", msg, ColorUtil.NotificationColors.YELLOW, 40));
    }

    public void doError(String msg) {
        Client.notify(new Notification.PopupMessage("BedRekter", msg, ColorUtil.NotificationColors.RED, 40));
    }

    public void doNotification(String msg) {
        Client.notify(new Notification.PopupMessage("BedRekter", msg, ColorUtil.NotificationColors.GREEN, 40));
    }

    public void searchForPlayerBed() {
        ArrayList<BlockPos> beds = BlockUtils.searchForBlock(this.targetBlock, 50.0f);
        if (beds.size() <= 0) {
            this.doError(" I can't find your bed so I might try to break it! If you're sure this is Bedwars, please contact developer and tell them to fix me : )");
            return;
        }
        for (BlockPos pos : beds) {
            if (this.playerBed == null) {
                this.playerBed = pos;
                continue;
            }
            if (!(this.mc.thePlayer.getDistance(pos.getX(), pos.getY(), pos.getZ()) < this.mc.thePlayer.getDistance(this.playerBed.getX(), this.playerBed.getY(), this.playerBed.getZ()))) continue;
            this.playerBed = pos;
        }
        if (this.playerBed == null) {
            this.doError(" I can't find your bed so I might try to break it! If you're sure this is Bedwars, please contact developer and tell them to fix me : )");
            return;
        }
        BlockPos head = new BlockPos(this.playerBed.getX() + 1, this.playerBed.getY(), this.playerBed.getZ());
        if (this.mc.theWorld.getBlockState(head).getBlock().getUnlocalizedName().equals(this.targetBlock)) {
            this.playerBedHead = head;
        }
        if (this.mc.theWorld.getBlockState(head = new BlockPos(this.playerBed.getX() + 1, this.playerBed.getY(), this.playerBed.getZ())).getBlock().getUnlocalizedName().equals(this.targetBlock)) {
            this.playerBedHead = head;
        }
        if (this.mc.theWorld.getBlockState(head = new BlockPos(this.playerBed.getX(), this.playerBed.getY(), this.playerBed.getZ() + 1)).getBlock().getUnlocalizedName().equals(this.targetBlock)) {
            this.playerBedHead = head;
        }
        if (this.mc.theWorld.getBlockState(head = new BlockPos(this.playerBed.getX(), this.playerBed.getY(), this.playerBed.getZ() + 1)).getBlock().getUnlocalizedName().equals(this.targetBlock)) {
            this.playerBedHead = head;
        }
        if (this.mc.theWorld.getBlockState(head = new BlockPos(this.playerBed.getX() - 1, this.playerBed.getY(), this.playerBed.getZ())).getBlock().getUnlocalizedName().equals(this.targetBlock)) {
            this.playerBedHead = head;
        }
        if (this.mc.theWorld.getBlockState(head = new BlockPos(this.playerBed.getX() - 1, this.playerBed.getY(), this.playerBed.getZ())).getBlock().getUnlocalizedName().equals(this.targetBlock)) {
            this.playerBedHead = head;
        }
        if (this.mc.theWorld.getBlockState(head = new BlockPos(this.playerBed.getX(), this.playerBed.getY(), this.playerBed.getZ() - 1)).getBlock().getUnlocalizedName().equals(this.targetBlock)) {
            this.playerBedHead = head;
        }
        if (this.mc.theWorld.getBlockState(head = new BlockPos(this.playerBed.getX(), this.playerBed.getY(), this.playerBed.getZ() - 1)).getBlock().getUnlocalizedName().equals(this.targetBlock)) {
            this.playerBedHead = head;
        }
        if (this.playerBedHead == null) {
            this.doError(" I can't find your bed so I might going to break it! If you're sure this is Bedwars, please contact developer and tell them to fix me : )");
            return;
        }
        this.doNotification("Your bed is at " + this.playerBed.getX() + ", " + this.playerBed.getY() + "," + this.playerBed.getZ() + " !");
    }

    @Override
    public void onEvent(Event e2) {
        Packet<?> p2;
        if (e2 instanceof PacketReceiveEvent && Minecraft.getMinecraft().theWorld != null) {
            p2 = ((PacketReceiveEvent)e2).getPacket();
            if (p2 instanceof S07PacketRespawn) {
                this.playerBed = null;
                this.playerBedHead = null;
            }
            if (p2 instanceof S02PacketChat && ((S02PacketChat)p2).chatComponent.getUnformattedText().contains("Seu objetivo \u00e9 proteger sua cama enquanto tenta destruir as camas de ilhas advers\u00e1rias e eliminar os jogadores inimigos. Use os min\u00e9rios gerados em sua il") && (this.playerBed == null || this.playerBedHead == null)) {
                this.searchForPlayerBed();
            }
        }
        if (e2 instanceof PacketSentEvent && Minecraft.getMinecraft().theWorld != null && (p2 = ((PacketSentEvent)e2).getPacket()) instanceof C08PacketPlayerBlockPlacement) {
            C08PacketPlayerBlockPlacement c08PacketPlayerBlockPlacement = (C08PacketPlayerBlockPlacement)p2;
        }
        if (e2 instanceof RenderEvent && this.target != null) {
            this.ESPBed(this.target);
        }
        if (e2 instanceof WorldTickEvent) {
            if (!this.enable) {
                return;
            }
            this.range = this.mc.playerController.getBlockReachDistance() - 0.5f;
            if (this.target == null) {
                for (BlockPos b2 : BlockUtils.searchForBlock(this.targetBlock, this.range)) {
                    if (b2.equals(this.playerBed) || b2.equals(this.playerBedHead) || this.mc.thePlayer.getDistance(b2.getX(), b2.getY(), b2.getZ()) > (double)this.range) continue;
                    if (ModulesManager.getModuleByClass(Disabler.class).isToggled()) {
                        this.mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C06PacketPlayerPosLook(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, RotationUtil.getYaw(), RotationUtil.getPitch(), true));
                    }
                    this.doWarning("I'm breaking bed: " + b2.getX() + ", " + b2.getY() + ", " + b2.getZ() + " ! Please wait !");
                    this.target = b2;
                    RotationUtil.setRotating("bedrekter", true);
                    this.damage = 0.0f;
                    this.shouldDamage = this.mc.theWorld.getBlockState(this.target).getBlock().getPlayerRelativeBlockHardness(this.mc.thePlayer, this.mc.theWorld, this.target);
                    this.mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, this.target, EnumFacing.DOWN));
                    Vector2f yp = RotationUtil.tryFaceBlock(this.target, EnumFacing.DOWN);
                    RotationUtil.setYaw(yp.x);
                    RotationUtil.setPitch(yp.y);
                    return;
                }
                return;
            }
            ++this.targetTick;
            this.damage += this.shouldDamage;
            if (this.mc.thePlayer.getDistance(this.target.getX(), this.target.getY(), this.target.getZ()) < (double)this.range) {
                this.mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.target, EnumFacing.DOWN));
                if (!this.mc.theWorld.getBlockState(this.target).getBlock().getUnlocalizedName().equals(this.targetBlock)) {
                    this.target = null;
                    RotationUtil.setRotating("bedrekter", false);
                }
            } else {
                this.doWarning("You went too far away! Target removed!" + this.mc.thePlayer.getDistanceSqToCenter(this.target));
                this.target = null;
                RotationUtil.setRotating("bedrekter", false);
            }
            ++this.ticks;
        }
    }

    public void ESPBed(BlockPos head) {
        GlStateManager.pushMatrix();
        GL11.glDisable(2896);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glEnable(2848);
        GlStateManager.color(255.0f, 255.0f, 255.0f, 255.0f);
        GL11.glLineWidth(4.0f);
        GL11.glBegin(2);
        GL11.glVertex3d((double)head.getX() - this.mc.renderManager.viewerPosX, (double)head.getY() - this.mc.renderManager.viewerPosY + 0.5625, (double)head.getZ() - this.mc.renderManager.viewerPosZ + 1.0);
        GL11.glVertex3d((double)head.getX() - this.mc.renderManager.viewerPosX + 1.0, (double)head.getY() - this.mc.renderManager.viewerPosY + 0.5625, (double)head.getZ() - this.mc.renderManager.viewerPosZ + 1.0);
        GL11.glVertex3d((double)head.getX() - this.mc.renderManager.viewerPosX + 1.0, (double)head.getY() - this.mc.renderManager.viewerPosY + 0.5625, (double)head.getZ() - this.mc.renderManager.viewerPosZ);
        GL11.glVertex3d((double)head.getX() - this.mc.renderManager.viewerPosX, (double)head.getY() - this.mc.renderManager.viewerPosY + 0.5625, (double)head.getZ() - this.mc.renderManager.viewerPosZ);
        GL11.glEnd();
        GL11.glRotatef(-this.mc.renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(this.mc.renderManager.playerViewX, 1.0f, 0.0f, 0.0f);
        GL11.glEnable(2929);
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GlStateManager.popMatrix();
    }
}

