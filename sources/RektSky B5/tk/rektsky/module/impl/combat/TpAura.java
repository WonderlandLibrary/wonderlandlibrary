/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.combat;

import cc.hyperium.utils.HyperiumFontRenderer;
import com.google.common.collect.Lists;
import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.Vec3;
import org.greenrobot.eventbus.Subscribe;
import org.lwjgl.opengl.GL11;
import tk.rektsky.Client;
import tk.rektsky.event.impl.HUDRenderEvent;
import tk.rektsky.event.impl.PacketSentEvent;
import tk.rektsky.event.impl.RenderEvent;
import tk.rektsky.event.impl.WorldTickEvent;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;
import tk.rektsky.module.ModulesManager;
import tk.rektsky.module.impl.combat.KillAura;
import tk.rektsky.module.settings.BooleanSetting;
import tk.rektsky.module.settings.DoubleSetting;
import tk.rektsky.module.settings.IntSetting;
import tk.rektsky.utils.PathUtils;
import tk.rektsky.utils.display.ColorUtil;
import tk.rektsky.utils.entity.EntityUtil;

public class TpAura
extends Module {
    public EntityLivingBase target;
    public List<Vec3> path = new ArrayList<Vec3>();
    public DoubleSetting range = new DoubleSetting("Range", 5.0, 100.0, 50.0);
    public IntSetting delay = new IntSetting("Delay (Ticks)", 1, 60, 10);
    public BooleanSetting targetHud = new BooleanSetting("TargetHUD", true);
    public BooleanSetting onlyPlayer = new BooleanSetting("Only Player", true);
    private static final HyperiumFontRenderer fr = Client.getFont();
    private static final HyperiumFontRenderer frbig = Client.getFontBig();

    public TpAura() {
        super("TpAura", "Make you go brr with infinity reach", Category.COMBAT, false);
    }

    @Override
    public void onEnable() {
        this.path.clear();
        this.target = null;
    }

    @Override
    public void onDisable() {
    }

    @Subscribe
    public void onTick(WorldTickEvent event) {
        List targets = this.mc.theWorld.getLoadedEntityList().stream().filter(new EntityUtil.FriendFilter()).filter(new EntityUtil.LivingFilter()).sorted(new EntityUtil.RangeSorter()).collect(Collectors.toList());
        LinkedList entities = new LinkedList(targets);
        entities.sort(new EntityUtil.RangeSorter());
        for (Entity entity : entities) {
            double deltaX = this.mc.thePlayer.posX - entity.posX;
            double deltaY = this.mc.thePlayer.posY - entity.posY;
            double deltaZ = this.mc.thePlayer.posZ - entity.posZ;
            if (this.mc.thePlayer == entity || this.onlyPlayer.getValue().booleanValue() && !(entity instanceof EntityPlayer) || !(Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) <= this.range.getValue()) || !(entity instanceof EntityLivingBase)) continue;
            this.target = (EntityLivingBase)entity;
            if (this.mc.thePlayer.ticksExisted % this.delay.getValue() == 0) {
                List<Vec3> blinkPath = PathUtils.findBlinkPath(this.target.posX, this.target.posY, this.target.posZ, 8.0);
                this.path.clear();
                if (this.mc.thePlayer.floatingTickCount <= (double)(79 - blinkPath.size() * 2)) {
                    this.path.add(this.mc.thePlayer.getPositionVector());
                    this.path.addAll(blinkPath);
                    for (Vec3 vec3 : this.path) {
                        this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(vec3.xCoord, vec3.yCoord, vec3.zCoord, false));
                    }
                    this.mc.thePlayer.swingItem();
                    this.mc.playerController.attackEntity(this.mc.thePlayer, this.target);
                    for (Vec3 vec3 : Lists.reverse(this.path)) {
                        this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(vec3.xCoord, vec3.yCoord, vec3.zCoord, false));
                    }
                }
            }
            return;
        }
        this.target = null;
        this.path.clear();
    }

    @Subscribe
    public void renderPath(RenderEvent event) {
        GlStateManager.pushMatrix();
        GL11.glDisable(2896);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDisable(2912);
        GlStateManager.color(255.0f, 255.0f, 255.0f);
        GL11.glLineWidth(2.0f);
        GL11.glBegin(1);
        for (int i2 = 0; i2 < this.path.size() - 1; ++i2) {
            Vec3 first = this.path.get(i2);
            Vec3 second = this.path.get(i2 + 1);
            GL11.glVertex3d(first.xCoord - this.mc.renderManager.viewerPosX, first.yCoord - this.mc.renderManager.viewerPosY, first.zCoord - this.mc.renderManager.viewerPosZ);
            GL11.glVertex3d(second.xCoord - this.mc.renderManager.viewerPosX, second.yCoord - this.mc.renderManager.viewerPosY, second.zCoord - this.mc.renderManager.viewerPosZ);
        }
        GL11.glEnd();
        GL11.glRotatef(-this.mc.renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(this.mc.renderManager.playerViewX, 1.0f, 0.0f, 0.0f);
        GL11.glEnable(2929);
        GL11.glEnable(2912);
        GL11.glEnable(3553);
        GlStateManager.popMatrix();
    }

    public void espPath(Vec3 vec) {
        GlStateManager.pushMatrix();
        GL11.glTranslated(vec.xCoord - this.mc.renderManager.viewerPosX, vec.yCoord - this.mc.renderManager.viewerPosY, vec.zCoord - this.mc.renderManager.viewerPosZ);
        GL11.glRotatef(-this.mc.renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GL11.glTranslated(-(vec.xCoord - this.mc.renderManager.viewerPosX), -(vec.yCoord - this.mc.renderManager.viewerPosY), -(vec.zCoord - this.mc.renderManager.viewerPosZ));
        GL11.glDisable(2896);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDisable(2912);
        GlStateManager.color(255.0f, 255.0f, 255.0f);
        GL11.glLineWidth(2.0f);
        GL11.glBegin(2);
        GL11.glVertex3d(vec.xCoord - this.mc.renderManager.viewerPosX - 0.1, vec.yCoord - this.mc.renderManager.viewerPosY, vec.zCoord - this.mc.renderManager.viewerPosZ);
        GL11.glVertex3d(vec.xCoord - this.mc.renderManager.viewerPosX + 0.1, vec.yCoord - this.mc.renderManager.viewerPosY, vec.zCoord - this.mc.renderManager.viewerPosZ);
        GL11.glVertex3d(vec.xCoord - this.mc.renderManager.viewerPosX + 0.1, vec.yCoord - this.mc.renderManager.viewerPosY + 0.2, vec.zCoord - this.mc.renderManager.viewerPosZ);
        GL11.glVertex3d(vec.xCoord - this.mc.renderManager.viewerPosX - 0.1, vec.yCoord - this.mc.renderManager.viewerPosY + 0.2, vec.zCoord - this.mc.renderManager.viewerPosZ);
        GL11.glEnd();
        GL11.glRotatef(-this.mc.renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(this.mc.renderManager.playerViewX, 1.0f, 0.0f, 0.0f);
        GL11.glEnable(2929);
        GL11.glEnable(2912);
        GL11.glEnable(3553);
        GlStateManager.popMatrix();
    }

    @Subscribe
    public void onPacket(PacketSentEvent event) {
        if (event.getPacket() instanceof C03PacketPlayer && this.mc.thePlayer.ticksExisted % this.delay.getValue() == 0) {
            event.setCanceled(true);
        }
    }

    private float fromPercentToScaledX(float x2) {
        ScaledResolution newSR = new ScaledResolution(Minecraft.getMinecraft());
        return (float)newSR.getScaledWidth() / 100.0f * x2;
    }

    private float fromPercentToScaledY(float y2) {
        ScaledResolution newSR = new ScaledResolution(Minecraft.getMinecraft());
        return (float)newSR.getScaledHeight() / 100.0f * y2;
    }

    @Subscribe
    public void onHudRender(HUDRenderEvent event) {
        if (this.target == null) {
            return;
        }
        if (ModulesManager.getModuleByClass(KillAura.class).isAttacking) {
            return;
        }
        int offsetX = (int)this.fromPercentToScaledX(50.0f) + 30;
        int offsetY = (int)this.fromPercentToScaledY(50.0f) + 30;
        Gui.drawRect(offsetX, offsetY, offsetX + 200, offsetY + 75, -1442840576);
        Gui.drawRect(offsetX, offsetY, offsetX + 2, offsetY + 75, Color.HSBtoRGB(ColorUtil.getRainbowHue(6500.0f, 0), 0.7f, 1.0f));
        Gui.drawRect(offsetX + 10, offsetY + 20, offsetX + 10 + 180, offsetY + 20 + 3, -15646975);
        float health = this.target.getHealth();
        float maxHealth = this.target.getMaxHealth();
        float x2 = health / maxHealth * 180.0f;
        x2 = Math.min(x2, 360.0f);
        Gui.drawRect(offsetX + 10, offsetY + 20, offsetX + 10 + Math.round(x2), offsetY + 20 + 3, -8323244);
        fr.drawString((float)Math.round(health * 10.0f) / 10.0f + "/" + (float)Math.round(maxHealth * 10.0f) / 10.0f, offsetX + 10, offsetY + 25, 0xFFFFFF);
        Object targetInfo = null;
        int ping = 0;
        for (NetworkPlayerInfo info : this.mc.getNetHandler().getPlayerInfoMap()) {
            if (!info.getGameProfile().getName().equals(this.target.getName())) continue;
            ping = info.getResponseTime();
            break;
        }
        fr.drawString(ping + " ms", (float)(offsetX + 200) - fr.getWidth(ping + "ms") - 15.0f, offsetY + 10, 0xFFFFFF);
        frbig.drawString(this.target.getHeldItem() != null ? this.target.getHeldItem().getDisplayName() + " x" + this.target.getHeldItem().stackSize : "Nothing x0", offsetX + 10, offsetY + 37, -1);
        String text = Float.toString((float)Math.round(this.mc.thePlayer.getEntityBoundingBox().getDistanceTo(this.target.getPositionVector()) * 10.0) / 10.0f) + " blocks";
        fr.drawString(text, (float)(offsetX + 200) - fr.getWidth(text) - 12.0f, offsetY + 24, -1);
        for (int iii = 3; iii != -1; --iii) {
            if (this.target.getCurrentArmor(iii) == null) continue;
            this.mc.ingameGUI.itemRenderer.renderItemIntoGUI(this.target.getCurrentArmor(iii), iii * 16 + 10 + offsetX, offsetY + 50);
            this.mc.ingameGUI.itemRenderer.renderItemOverlays(this.mc.fontRendererObj, this.target.getCurrentArmor(iii), iii * 16 + 10 + offsetX, offsetY + 52);
        }
        frbig.drawString(this.target.getName(), offsetX + 10, offsetY + 5, 0xFFFFFF);
    }
}

