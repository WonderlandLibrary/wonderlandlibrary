package ru.smertnix.celestial.feature.impl.combat;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Vector3d;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketEntityEquipment;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.draggable.component.impl.DraggableTargetHUD;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.packet.EventAttackSilent;
import ru.smertnix.celestial.event.events.impl.packet.EventReceivePacket;
import ru.smertnix.celestial.event.events.impl.packet.EventSendPacket;
import ru.smertnix.celestial.event.events.impl.player.EventPreMotion;
import ru.smertnix.celestial.event.events.impl.player.EventUpdate;
import ru.smertnix.celestial.event.events.impl.render.EventRender2D;
import ru.smertnix.celestial.event.events.impl.render.EventRender3D;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.feature.impl.player.NameProtect;
import ru.smertnix.celestial.feature.impl.visual.CustomModel;
import ru.smertnix.celestial.feature.impl.visual.Notifications;
import ru.smertnix.celestial.ui.clickgui.component.impl.MultipleBoolSettingComponent;
import ru.smertnix.celestial.ui.notification.NotificationMode;
import ru.smertnix.celestial.ui.notification.NotificationRenderer;
import ru.smertnix.celestial.ui.settings.impl.BooleanSetting;
import ru.smertnix.celestial.ui.settings.impl.ListSetting;
import ru.smertnix.celestial.ui.settings.impl.MultipleBoolSetting;
import ru.smertnix.celestial.ui.settings.impl.NumberSetting;
import ru.smertnix.celestial.utils.Helper;
import ru.smertnix.celestial.utils.inventory.InvenotryUtil;
import ru.smertnix.celestial.utils.math.AnimationHelper;
import ru.smertnix.celestial.utils.math.GCDFix;
import ru.smertnix.celestial.utils.math.KillauraUtils;
import ru.smertnix.celestial.utils.math.MathematicHelper;
import ru.smertnix.celestial.utils.math.Point;
import ru.smertnix.celestial.utils.math.RotationHelper;
import ru.smertnix.celestial.utils.math.TimerHelper;
import ru.smertnix.celestial.utils.movement.MovementUtils;
import ru.smertnix.celestial.utils.other.ChatUtils;
import ru.smertnix.celestial.utils.other.Particles;
import ru.smertnix.celestial.utils.render.ClientHelper;
import ru.smertnix.celestial.utils.render.ColorUtils;
import ru.smertnix.celestial.utils.render.Colors;
import ru.smertnix.celestial.utils.render.RenderUtils;

import java.awt.Color;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import javax.vecmath.Vector4d;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;

public class AttackAura extends Feature {
	double height;
	public static Point targetp;
    boolean animat;
    static double diffX = 0;
    static double diffZ = 0;
    static double diffY = 0;
    static double x, y, z;
    private double circleAnim;
    private double circleValue;
    private boolean canDown;
    public static TimerHelper timerHelper = new TimerHelper();
    public static float yaw;
    public float pitch;
    public float pitch2 = 0;
    private int notiTicks;
    public static boolean isAttacking;
    TimerHelper shieldFixerTimer = new TimerHelper();
    public float yaw2 = 0;
    public static boolean isBreaked;
    public static EntityLivingBase target;
  //  public static NumberSetting maxticks = new NumberSetting("Max Ticks", "", 10.0F, 5, 50, 1, () -> true);
    public static ListSetting rotationMode = new ListSetting("Rotation Mode", "Advanced", () -> true, "Advanced", "Vulcan");
    public static ListSetting sortMode = new ListSetting("Priority Mode", "Distance", () -> true, "Distance", "Health", "FOV", "Single");
    public static NumberSetting range = new NumberSetting("Range", "��������� � ������� �� ������ ������� ������", 2.0F, 3, 6, 0.1f, () -> true);
    public static NumberSetting rangeRot = new NumberSetting("Rotate Range", "��������� � ������� �� ������ ������� ������", 1.0F, 0, 2, 0.1f, () -> true);
    public BooleanSetting rayCast = new BooleanSetting("Ray-Trace", "��������� �������� �� ������� �� ���-���� ������", false, () -> true);
    public static BooleanSetting onlyCritical = new BooleanSetting("Only Critical", "���� � ������ ������ ��� �����", false, () -> true);
    public static BooleanSetting noCrits = new BooleanSetting("NoCritsIfObsidian", "���� � ������ ������ ��� �����", false, () -> true);
    public BooleanSetting spaceOnly = new BooleanSetting("Only Space", "Only Crits ����� �������� ���� ����� ������", false, () -> onlyCritical.getBoolValue());
    public BooleanSetting shieldDesync = new BooleanSetting("Shield Desync", false, () -> true);
    public static MultipleBoolSetting targetsSetting = new MultipleBoolSetting("Targets", new BooleanSetting("Players", true), new BooleanSetting("Players no armor", true), new BooleanSetting("NPC / Bots"), new BooleanSetting("Mobs"));
    public BooleanSetting smartShield = new BooleanSetting("Smart Shield", false, () -> true);
    public BooleanSetting adaptiveCrits = new BooleanSetting("Adaptive Critical", false, () -> onlyCritical.getBoolValue());
    public BooleanSetting weaponOnly = new BooleanSetting("Weapon Only", false, () -> true);
    private float healthBarWidth;
    public static BooleanSetting targetHud = new BooleanSetting("Target HUD", true, () -> true);
    private double scale = 0;
    public BooleanSetting targetEsp = new BooleanSetting("Target ESP", false, () -> true);
    public AttackAura() {
        super("Attack Aura", "Автоматически бьёт Entity", FeatureCategory.Combat);
        addSettings(/*maxticks, */sortMode, rotationMode, targetsSetting, range, rangeRot, rayCast, shieldDesync/*, smartShield*/, onlyCritical, adaptiveCrits, spaceOnly, noCrits, weaponOnly, targetHud, targetEsp);
    }

    public static float getRenderHurtTime(EntityLivingBase hurt) {
        return (float) hurt.hurtTime - (hurt.hurtTime != 0 ? mc.timer.renderPartialTicks : 0.0f);
    }

    public static float getHurtPercent(EntityLivingBase hurt) {
        return getRenderHurtTime(hurt) / (float) 10;
    }

    @Override
    public void onEnable() {
        if (this.mc.gameSettings.ofFastRender) {
            this.mc.gameSettings.ofFastRender = false;
        }
        super.onEnable();
    }

    @EventTarget
    public void onRender(EventRender2D event3D) {

    }

    @EventTarget
    public void onRender(EventRender3D event3D) {
        int color = 0;
        int oneColor;
        if (!targetEsp.getBoolValue()) {
        	return;
        }
        if (target == null)
        	return;
        if (!AttackAura.target.isDead) {
            /*double x2 = this.x - mc.getRenderManager().renderPosX;
            double y2 = this.y - mc.getRenderManager().renderPosY;
            double z2 = this.z - mc.getRenderManager().renderPosZ;
            GlStateManager.pushMatrix();
            GL11.glBlendFunc(770, 771);
           RenderUtils.enableSmoothLine(2.5F);
            GL11.glShadeModel(7425);
            GL11.glEnable(2884);
            GL11.glEnable(3042);
        	color = 0;
        	color =  ClientHelper.getClientColor().getRGB();
            RenderUtils.color(color);
	      //  RenderUtils.drawColorBox(new AxisAlignedBB(x2 - 0.05, y2 -.05, z2-.05, x2 + 0.10f, y2 + .10, z2 + 0.10f), 0F, 0F, 0F, 0F);
	        final Sphere right = new Sphere();
	        GL11.glTranslated(x2, y2,z2);
			right.setDrawStyle(100013);
			right.draw(0.04f, 15, 20);
			 RenderUtils.disableSmoothLine();
		        GL11.glShadeModel(7424);
		        GL11.glDisable(3042);
		        GL11.glDisable(2884);
		        GlStateManager.popMatrix();*/
            if (AttackAura.mc.player.getDistanceToEntity(AttackAura.target) <= AttackAura.range.getNumberValue() + rangeRot.getNumberValue()) {
                if (AttackAura.target != null && Celestial.instance.featureManager.getFeature(AttackAura.class).isEnabled()) {
                    double iCos;
                    double iSin;
                    int i;
                    double z;
                    double y;
                    double x = AttackAura.target.lastTickPosX + (AttackAura.target.posX - AttackAura.target.lastTickPosX) * (double) AttackAura.mc.timer.renderPartialTicks - AttackAura.mc.getRenderManager().renderPosX;
                    y = AttackAura.target.lastTickPosY + (AttackAura.target.posY - AttackAura.target.lastTickPosY) * (double) AttackAura.mc.timer.renderPartialTicks - AttackAura.mc.getRenderManager().renderPosY;
                    z = AttackAura.target.lastTickPosZ + (AttackAura.target.posZ - AttackAura.target.lastTickPosZ) * (double) AttackAura.mc.timer.renderPartialTicks - AttackAura.mc.getRenderManager().renderPosZ;
                    this.circleValue += (double) 0.01f * (Minecraft.frameTime * 0.1);
                    float targetHeight = (float) (0.5 * (1.0 + Math.sin(Math.PI * 2 * (this.circleValue * (double) 0.3f))));
                    float size = AttackAura.target.width + 0.1f;
                    float endYValue = (float) ((float) ((((double) AttackAura.target.height)) * 1.0 + 0.2) * (double) targetHeight);
                    if ((double) targetHeight > 0.99) {
                        this.canDown = false;
                    } else if ((double) targetHeight < 0.01) {
                        this.canDown = true;
                    }
                    GlStateManager.enableBlend();
                    GL11.glBlendFunc(770, 771);
                    GL11.glEnable(2848);
                    GlStateManager.disableTexture2D();
                    GlStateManager.disableAlpha();
                    GL11.glLineWidth(2.0f);
                    GL11.glShadeModel(7425);
                    GL11.glDisable(2884);
                    GL11.glBegin(5);
                    float alpha = (this.canDown ? 255.0f * targetHeight : 255.0f * (1.0f - targetHeight)) / 255.0f;
                    for (i = 0; i < 360; ++i) {
                    	color = 0;
                    	color =  ClientHelper.getClientColor(i * 3.7f, 1, 4).getRGB();
                        float red = (float) (color >> 16 & 0xFF) / 255.0f;
                        float green = (float) (color >> 8 & 0xFF) / 255.0f;
                        float blue = (float) (color & 0xFF) / 255.0f;
                        RenderUtils.color(red, green, blue, alpha);
                        iSin = Math.sin(Math.toRadians(i)) * (double) size;
                        iCos = Math.cos(Math.toRadians(i)) * (double) size;
                        GL11.glVertex3d(x + iCos, y + (double) endYValue, z - iSin);
                        RenderUtils.color(red, green, blue, 0.0);
                        GL11.glVertex3d(x + iCos, y + (double) endYValue + (double) (this.canDown ? -0.5f * (1.0f - targetHeight) : 0.5f * targetHeight), z - iSin);

                    }
                    GL11.glEnd();
                    GL11.glBegin(2);
                    for (i = 0; i < 361; ++i) {
                    	color = 0;
                    	color =  ClientHelper.getClientColor(i * 3.7f, 1, 4).getRGB();
                        RenderUtils.color(color);
                        iSin = Math.sin(Math.toRadians(i)) * (double) size;
                        iCos = Math.cos(Math.toRadians(i)) * (double) size;
                        GL11.glVertex3d(x + iCos, y + (double) endYValue, z - iSin);
                    }
                    GL11.glEnd();
                    GL11.glBegin(2);
                    for (i = 0; i < 2; ++i) {
                    	color = 0;
                    	color =  ClientHelper.getClientColor(i * 3.7f, 1, 4).getRGB();
                        RenderUtils.color(color);
                        iSin = Math.sin(Math.toRadians(i)) * (double) size;
                        iCos = Math.cos(Math.toRadians(i)) * (double) size;
                        GL11.glVertex3d(x + iCos, y + (double) endYValue, z - iSin);
                    }
                    GL11.glEnd();
                    GlStateManager.enableAlpha();
                    GL11.glShadeModel(7424);
                    GL11.glDisable(2848);
                    GL11.glEnable(2884);
                    GlStateManager.enableTexture2D();
                    GlStateManager.disableBlend();
                    GlStateManager.resetColor();
                }
            } else {
                this.circleAnim = 0.0;
            }
        }
    }
    long id;
    short tsid;
    int twid;
    int ticks;
    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        /* Interact Fix */
    	int delay = 5000;
        if (event.getPacket() instanceof CPacketUseEntity) {
            CPacketUseEntity cPacketUseEntity = (CPacketUseEntity) event.getPacket();

            if (cPacketUseEntity.getAction() == CPacketUseEntity.Action.INTERACT) {
                event.setCancelled(true);
            }

            if (cPacketUseEntity.getAction() == CPacketUseEntity.Action.INTERACT_AT) {
                event.setCancelled(true);
            }
        }

    }
    public static float[] rot(Entity e) {
	        double boba = 0f;
	        double b = 0.15f;
	        double diffY;
	        final ArrayList<Point> p = new ArrayList<Point>();
	        	p.add(new Point(e.getEntityBoundingBox().maxX- boba, e.getEntityBoundingBox().maxY, e.getEntityBoundingBox().maxZ- boba));
		    	p.add(new Point(e.getEntityBoundingBox().maxX- boba, e.getEntityBoundingBox().maxY, e.getEntityBoundingBox().minZ- boba));
		    	p.add(new Point(e.getEntityBoundingBox().minX- boba, e.getEntityBoundingBox().maxY, e.getEntityBoundingBox().maxZ- boba));
		    	p.add(new Point(e.getEntityBoundingBox().minX- boba, e.getEntityBoundingBox().maxY, e.getEntityBoundingBox().minZ- boba));

	    	p.add(new Point(e.getEntityBoundingBox().maxX- boba, e.posY, e.getEntityBoundingBox().maxZ- boba));
	    	p.add(new Point(e.getEntityBoundingBox().maxX- boba, e.posY, e.getEntityBoundingBox().minZ- boba));
	    	p.add(new Point(e.getEntityBoundingBox().minX- boba, e.posY, e.getEntityBoundingBox().maxZ- boba));
	    	p.add(new Point(e.getEntityBoundingBox().minX- boba, e.posY, e.getEntityBoundingBox().minZ- boba));

	    	p.add(new Point(e.getEntityBoundingBox().maxX- boba, e.getEntityBoundingBox().minY, e.getEntityBoundingBox().maxZ- boba));
	    	p.add(new Point(e.getEntityBoundingBox().maxX- boba, e.getEntityBoundingBox().minY, e.getEntityBoundingBox().minZ- boba));
	    	p.add(new Point(e.getEntityBoundingBox().minX- boba, e.getEntityBoundingBox().minY, e.getEntityBoundingBox().maxZ- boba));
	    	p.add(new Point(e.getEntityBoundingBox().minX- boba, e.getEntityBoundingBox().minY, e.getEntityBoundingBox().minZ- boba));

	    	p.add(new Point(e.posX - boba, e.getEntityBoundingBox().minY, e.getEntityBoundingBox().maxZ- boba));
	    	p.add(new Point(e.posX - boba, e.getEntityBoundingBox().minY, e.getEntityBoundingBox().minZ- boba));
	    	p.add(new Point(e.getEntityBoundingBox().minX- boba, e.getEntityBoundingBox().minY, e.posZ - boba));
	    	p.add(new Point(e.getEntityBoundingBox().minX- boba, e.getEntityBoundingBox().minY, e.posZ - boba));

	       	p.add(new Point(e.posX - boba, e.getEntityBoundingBox().maxY, e.getEntityBoundingBox().maxZ- boba));
	    	p.add(new Point(e.posX - boba, e.getEntityBoundingBox().maxY, e.getEntityBoundingBox().minZ- boba));
	    	p.add(new Point(e.getEntityBoundingBox().minX- boba, e.getEntityBoundingBox().maxY, e.posZ - boba));
	    	p.add(new Point(e.getEntityBoundingBox().minX- boba, e.getEntityBoundingBox().maxY, e.posZ - boba));

	    	p.add(new Point(e.posX - boba, e.posY, e.getEntityBoundingBox().maxZ- boba));
	    	p.add(new Point(e.posX - boba, e.posY, e.getEntityBoundingBox().minZ- boba));
	    	p.add(new Point(e.getEntityBoundingBox().minX- boba, e.posY, e.posZ - boba));
	    	p.add(new Point(e.getEntityBoundingBox().minX- boba, e.posY, e.posZ - boba));

	    	p.add(new Point(e.posX + b - boba, e.getEntityBoundingBox().minY, e.getEntityBoundingBox().maxZ- boba));
	    	p.add(new Point(e.posX + b - boba, e.getEntityBoundingBox().minY, e.getEntityBoundingBox().minZ- boba));
	    	p.add(new Point(e.getEntityBoundingBox().minX- boba, e.getEntityBoundingBox().minY, e.posZ + b - boba));
	    	p.add(new Point(e.getEntityBoundingBox().minX- boba, e.getEntityBoundingBox().minY, e.posZ + b - boba));

	       	p.add(new Point(e.posX + b - boba, e.getEntityBoundingBox().maxY, e.getEntityBoundingBox().maxZ- boba));
	    	p.add(new Point(e.posX + b - boba, e.getEntityBoundingBox().maxY, e.getEntityBoundingBox().minZ- boba));
	    	p.add(new Point(e.getEntityBoundingBox().minX- boba, e.getEntityBoundingBox().maxY, e.posZ + b - boba));
	    	p.add(new Point(e.getEntityBoundingBox().minX- boba, e.getEntityBoundingBox().maxY, e.posZ + b - boba));

	    	p.add(new Point(e.posX + b - boba, e.posY, e.getEntityBoundingBox().maxZ- boba));
	    	p.add(new Point(e.posX + b - boba, e.posY, e.getEntityBoundingBox().minZ- boba));
	    	p.add(new Point(e.getEntityBoundingBox().minX- boba, e.posY, e.posZ + b - boba));
	    	p.add(new Point(e.getEntityBoundingBox().minX- boba, e.posY, e.posZ + b - boba));

	    	p.add(new Point(e.posX - b - boba, e.getEntityBoundingBox().minY, e.getEntityBoundingBox().maxZ- boba));
	    	p.add(new Point(e.posX - b - boba, e.getEntityBoundingBox().minY, e.getEntityBoundingBox().minZ- boba));
	    	p.add(new Point(e.getEntityBoundingBox().minX- boba, e.getEntityBoundingBox().minY, e.posZ + b - boba));
	    	p.add(new Point(e.getEntityBoundingBox().minX- boba, e.getEntityBoundingBox().minY, e.posZ + b - boba));

	       	p.add(new Point(e.posX - b - boba, e.getEntityBoundingBox().maxY, e.getEntityBoundingBox().maxZ- boba));
	    	p.add(new Point(e.posX - b - boba, e.getEntityBoundingBox().maxY, e.getEntityBoundingBox().minZ- boba));
	    	p.add(new Point(e.getEntityBoundingBox().minX- boba, e.getEntityBoundingBox().maxY, e.posZ - b - boba));
	    	p.add(new Point(e.getEntityBoundingBox().minX- boba, e.getEntityBoundingBox().maxY, e.posZ - b - boba));

	    	p.add(new Point(e.posX - b - boba, e.posY, e.getEntityBoundingBox().maxZ- boba));
	    	p.add(new Point(e.posX - b - boba, e.posY, e.getEntityBoundingBox().minZ- boba));
	    	p.add(new Point(e.getEntityBoundingBox().minX- boba, e.posY, e.posZ - b - boba));
	    	p.add(new Point(e.getEntityBoundingBox().minX- boba, e.posY, e.posZ - b - boba));


	    	p.add(new Point(e.getEntityBoundingBox().maxX- boba, e.getEntityBoundingBox().maxY - b, e.getEntityBoundingBox().maxZ- boba));
	    	p.add(new Point(e.getEntityBoundingBox().maxX- boba, e.getEntityBoundingBox().maxY - b, e.getEntityBoundingBox().minZ- boba));
	    	p.add(new Point(e.getEntityBoundingBox().minX- boba, e.getEntityBoundingBox().maxY - b, e.getEntityBoundingBox().maxZ- boba));
	    	p.add(new Point(e.getEntityBoundingBox().minX- boba, e.getEntityBoundingBox().maxY - b, e.getEntityBoundingBox().minZ- boba));

	    	p.add(new Point(e.getEntityBoundingBox().maxX- boba, e.getEntityBoundingBox().minY + b, e.getEntityBoundingBox().maxZ- boba));
	    	p.add(new Point(e.getEntityBoundingBox().maxX- boba, e.getEntityBoundingBox().minY + b, e.getEntityBoundingBox().minZ- boba));
	    	p.add(new Point(e.getEntityBoundingBox().minX- boba, e.getEntityBoundingBox().minY + b, e.getEntityBoundingBox().maxZ- boba));
	    	p.add(new Point(e.getEntityBoundingBox().minX- boba, e.getEntityBoundingBox().minY + b, e.getEntityBoundingBox().minZ- boba));

	    	p.add(new Point(e.posX - boba, e.getEntityBoundingBox().minY + b, e.getEntityBoundingBox().maxZ- boba));
	    	p.add(new Point(e.posX - boba, e.getEntityBoundingBox().minY + b, e.getEntityBoundingBox().minZ- boba));
	    	p.add(new Point(e.getEntityBoundingBox().minX- boba, e.getEntityBoundingBox().minY + b, e.posZ - boba));
	    	p.add(new Point(e.getEntityBoundingBox().minX- boba, e.getEntityBoundingBox().minY + b, e.posZ - boba));

	       	p.add(new Point(e.posX - boba, e.getEntityBoundingBox().maxY - b, e.getEntityBoundingBox().maxZ- boba));
	    	p.add(new Point(e.posX - boba, e.getEntityBoundingBox().maxY - b, e.getEntityBoundingBox().minZ- boba));
	    	p.add(new Point(e.getEntityBoundingBox().minX- boba, e.getEntityBoundingBox().maxY - b, e.posZ - boba));
	    	p.add(new Point(e.getEntityBoundingBox().minX- boba, e.getEntityBoundingBox().maxY - b, e.posZ - boba));

	    	p.add(new Point(e.posX + b - boba, e.getEntityBoundingBox().minY + b, e.getEntityBoundingBox().maxZ- boba));
	    	p.add(new Point(e.posX + b - boba, e.getEntityBoundingBox().minY + b, e.getEntityBoundingBox().minZ- boba));
	    	p.add(new Point(e.getEntityBoundingBox().minX- boba, e.getEntityBoundingBox().minY + b, e.posZ + b - boba));
	    	p.add(new Point(e.getEntityBoundingBox().minX- boba, e.getEntityBoundingBox().minY + b, e.posZ + b - boba));

	       	p.add(new Point(e.posX + b - boba, e.getEntityBoundingBox().maxY - b, e.getEntityBoundingBox().maxZ- boba));
	    	p.add(new Point(e.posX + b - boba, e.getEntityBoundingBox().maxY - b, e.getEntityBoundingBox().minZ- boba));
	    	p.add(new Point(e.getEntityBoundingBox().minX- boba, e.getEntityBoundingBox().maxY - b, e.posZ + b - boba));
	    	p.add(new Point(e.getEntityBoundingBox().minX- boba, e.getEntityBoundingBox().maxY - b, e.posZ + b - boba));

	    	p.add(new Point(e.posX - b - boba, e.getEntityBoundingBox().minY + b, e.getEntityBoundingBox().maxZ- boba));
	    	p.add(new Point(e.posX - b - boba, e.getEntityBoundingBox().minY + b, e.getEntityBoundingBox().minZ- boba));
	    	p.add(new Point(e.getEntityBoundingBox().minX- boba, e.getEntityBoundingBox().minY + b, e.posZ + b - boba));
	    	p.add(new Point(e.getEntityBoundingBox().minX- boba, e.getEntityBoundingBox().minY + b, e.posZ + b - boba));

	       	p.add(new Point(e.posX - b - boba, e.getEntityBoundingBox().maxY - b, e.getEntityBoundingBox().maxZ- boba));
	    	p.add(new Point(e.posX - b - boba, e.getEntityBoundingBox().maxY - b, e.getEntityBoundingBox().minZ- boba));
	    	p.add(new Point(e.getEntityBoundingBox().minX- boba, e.getEntityBoundingBox().maxY - b, e.posZ - b - boba));
	    	p.add(new Point(e.getEntityBoundingBox().minX- boba, e.getEntityBoundingBox().maxY - b, e.posZ - b - boba));



	        p.sort(Comparator.comparingDouble(m -> ((Point) m).getDistance(mc.player.posX,mc.player.posY + 1, mc.player.posZ)));
	        Point ideal = p.get(0);
	        targetp = ideal;
	        Point entityIn = ideal;
	       // float maxTicks = maxticks.getNumberValue();
	        mc.world.removeEntityFromWorld(-10);
	        EntityOtherPlayerMP ent = new EntityOtherPlayerMP(mc.world, mc.player.getGameProfile());
	         ent.inventory = mc.player.inventory;
	         ent.inventoryContainer = mc.player.inventoryContainer;
	         ent.setHealth(mc.player.getHealth());
	         ent.setPositionAndRotation(target.posX, target.getEntityBoundingBox().minY, target.posZ, target.rotationYaw, target.rotationPitch);
	         ent.rotationYawHead = target.rotationYawHead;
	  //     if (mc.player.connection.getPlayerInfo(mc.player.getUniqueID()).getResponseTime() < 70) {
	        	x = ideal.getX();
	   	        y = ideal.getY();
	   	        z = ideal.getZ();
	   	  //   mc.world.addEntityToWorld(-10, ent);
	      /* } else if(mc.player.connection.getPlayerInfo(mc.player.getUniqueID()).getResponseTime() >= maxTicks * 10) {
	        	if (target.ticksExisted % maxTicks < 2) {
	        		x = ideal.getX();
		   	        y = ideal.getY();
		   	        z = ideal.getZ();
			         ent.inventory = mc.player.inventory;
			         ent.inventoryContainer = mc.player.inventoryContainer;
			         ent.setHealth(mc.player.getHealth());
			         ent.setPositionAndRotation(target.posX, target.getEntityBoundingBox().minY, target.posZ, target.rotationYaw, target.rotationPitch);
			         ent.rotationYawHead = target.rotationYawHead;
			         mc.world.addEntityToWorld(-10, ent);
	        	}
	        } else {
	        	if (target.ticksExisted % mc.player.connection.getPlayerInfo(mc.player.getUniqueID()).getResponseTime() / 10 > 2) {
	        		x = ideal.getX();
		   	        y = ideal.getY();
		   	        z = ideal.getZ();
			         ent.inventory = mc.player.inventory;
			         ent.inventoryContainer = mc.player.inventoryContainer;
			         ent.setHealth(mc.player.getHealth());
			         ent.setPositionAndRotation(target.posX, target.getEntityBoundingBox().minY, target.posZ, target.rotationYaw, target.rotationPitch);
			         ent.rotationYawHead = target.rotationYawHead;
			         mc.world.addEntityToWorld(-10, ent);
	        	}
	        }*/
        return RotationHelper.getRotationVector(new Vec3d(x,y,z));
    }


    @EventTarget
    public void onPreAttack(EventPreMotion event) {
        String mode = rotationMode.getOptions();

        setSuffix("" + mode);
        /* Sorting */
        target = KillauraUtils.getSortEntities();

        /* ����� ������ */
        if (target == null) {
        	targetp = null;
            return;
        }
        /* RayCast */
        if (mc.player.connection.getPlayerInfo(mc.player.getUniqueID()).getResponseTime() < 70) {
        	if (!rotationMode.currentMode.equals("Vulcan") && !RotationHelper.isLookingAtEntity(false, yaw, pitch, 0.12f, 0.12f, 0.12f, target, (range.getNumberValue() + rangeRot.getNumberValue())) && rayCast.getBoolValue()) {
                return;
            }
        }

        /* Only Critical */
        mc.player.jumpTicks = 0;
        BlockPos blockPos = new BlockPos(mc.player.posX, mc.player.posY - 0.1, mc.player.posZ);
        Block block = mc.world.getBlockState(blockPos).getBlock();
        float f2 = mc.player.getCooledAttackStrength(0.5F);
        boolean flag = (f2 > 0.9F);
        if (!flag && onlyCritical.getBoolValue())
            return;
        if (!(!mc.gameSettings.keyBindJump.isKeyDown() && spaceOnly.getBoolValue() || checkObsidian())) {
            if (MovementUtils.airBlockAboveHead()) {
                if (!(mc.player.fallDistance >= 0.08 || block instanceof BlockLiquid || !onlyCritical.getBoolValue() || mc.player.isRiding() || mc.player.isOnLadder() || mc.player.isInLiquid() || mc.player.isInWeb)) {
                    mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
                    return;
                }
            } else if (!(!(mc.player.fallDistance > 0.0f) || mc.player.onGround || !onlyCritical.getBoolValue() || mc.player.isRiding() || mc.player.isOnLadder() || mc.player.isInLiquid() || mc.player.isInWeb)) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
                return;
            }
        }
        if (!(AttackAura.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword) && !(AttackAura.mc.player.getHeldItemMainhand().getItem() instanceof ItemAxe) && weaponOnly.getBoolValue()) {
            return;
        }
        KillauraUtils.attackEntity(target);
    }

    @EventTarget
    public void onRotations(EventPreMotion event) {
        String mode = rotationMode.getOptions();
        if (target == null) {
       	 mc.world.removeEntityFromWorld(-10);
       	 targetp = null;
            return;
        }
        if (!target.isDead) {
            /* ROTATIONS */
            float[] matrix = rot(target);
            if (mode.equalsIgnoreCase("Advanced")) {
            	 /*
                event.setYaw(matrix[0]);
                event.setPitch(matrix[1]);
                yaw = matrix[0];
                pitch = matrix[1];
                mc.player.renderYawOffset = matrix[0];
                mc.player.rotationYawHead = matrix[0];
                mc.player.rotationPitchHead = matrix[1];
                */
                event.setYaw(matrix[0]);
                event.setPitch(matrix[1]);
                yaw = matrix[0];
                pitch = matrix[1];
                mc.player.renderYawOffset = matrix[0];
                mc.player.rotationYawHead = matrix[0];
                mc.player.rotationPitchHead = matrix[1];
            } else if (mode.equalsIgnoreCase("Vulcan")) {
            	   event.setYaw(matrix[0]);
                   event.setPitch(matrix[1]);
                   yaw = matrix[0];
                   pitch = matrix[1];
                   mc.player.renderYawOffset = matrix[0];
                   mc.player.rotationYawHead = matrix[0];
                   mc.player.rotationPitchHead = matrix[1];
            }

        }
    }

    @EventTarget
    public void onAttackSilent(EventAttackSilent eventAttackSilent) {
        /* SHIELD Fix */
        isAttacking = true;
        if (mc.player.isBlocking() && mc.player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof ItemShield) {
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(900, 900, 900), EnumFacing.UP));
            mc.playerController.processRightClick(mc.player, mc.world, EnumHand.OFF_HAND);
            shieldFixerTimer.reset();
        }
    }


    @EventTarget
    public void onUpdate(EventUpdate event) {
        /* SHIELD Desync */
        if (shieldDesync.getBoolValue() && mc.player.isBlocking() && target != null && mc.player.ticksExisted % 8 == 0) {
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(900, 900, 900), EnumFacing.DOWN));
            mc.playerController.processRightClick(mc.player, mc.world, EnumHand.OFF_HAND);
        }
            if (target.getHeldItemMainhand().getItem() instanceof ItemAxe) {
                if (mc.gameSettings.keyBindUseItem.isKeyDown()) {
                    mc.gameSettings.keyBindUseItem.pressed = false;
                }
            } else {
                mc.gameSettings.keyBindUseItem.pressed = Mouse.isButtonDown(1);
            }
    }

    @EventTarget
    public void onSound(EventReceivePacket sound) {
        if (Celestial.instance.featureManager.getFeature(ShieldBreaker.class).isEnabled()) {
        	if (Notifications.shieldBreak.getBoolValue()) {
        		if (sound.getPacket() instanceof SPacketEntityStatus) {
                    SPacketEntityStatus sPacketEntityStatus = (SPacketEntityStatus) sound.getPacket();
                    if (sPacketEntityStatus.getOpCode() == 30) {
                        if (sPacketEntityStatus.getEntity(mc.world) == target) {
                            if (notiTicks < 2) {
                                NotificationRenderer.queue("Shield Debug", target.getName() + " shield destroyed", 2, NotificationMode.BREAK);
                            } else {
                                notiTicks = 0;
                            }
                        }
                    }
                }
        	}
        }
    }

    public static void BreakShield(EntityLivingBase tg) {
        if (InvenotryUtil.doesHotbarHaveAxe() && Celestial.instance.featureManager.getFeature(ShieldBreaker.class).isEnabled()) {
            int item = InvenotryUtil.getAxe();
            if (InvenotryUtil.getAxe() >= 0 && tg instanceof EntityPlayer && tg.isHandActive() && tg.getActiveItemStack().getItem() instanceof ItemShield) {
                mc.player.connection.sendPacket(new CPacketHeldItemChange(item));
                mc.playerController.attackEntity(mc.player, target);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
            }
        }
    }

    @Override
    public void onDisable() {
    	targetp = null;
        target = null;
   	 mc.world.removeEntityFromWorld(-10);
        super.onDisable();
    }

    private boolean checkObsidian() {
        if (!noCrits.getBoolValue()) {
            return false;
        }
        BlockPos pos = getSphere(getPlayerPosLocal(), (float) 5, 6, false, true, 0).stream().filter(this::IsValidBlockPos).min(Comparator.comparing(blockPos -> getDistanceOfEntityToBlock(mc.player, blockPos))).orElse(null);
        return pos != null;
    }

    public static double getDistanceOfEntityToBlock(Entity entity, BlockPos blockPos) {
        return getDistance(entity.posX, entity.posY, entity.posZ, blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    public static double getDistance(double n, double n2, double n3, double n4, double n5, double n6) {
        double n7 = n - n4;
        double n8 = n2 - n5;
        double n9 = n3 - n6;
        return MathHelper.sqrt(n7 * n7 + n8 * n8 + n9 * n9);
    }

    private boolean IsValidBlockPos(BlockPos pos) {
        IBlockState state = mc.world.getBlockState(pos);
        return state.getBlock() instanceof BlockObsidian;
    }

    public static BlockPos getPlayerPosLocal() {
        if (mc.player == null) {
            return BlockPos.ORIGIN;
        }
        return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
    }

    public static List<BlockPos> getSphere(BlockPos blockPos, float n, int n2, boolean b, boolean b2, int n3) {
        ArrayList<BlockPos> list = new ArrayList<BlockPos>();
        int x = blockPos.getX();
        int y = blockPos.getY();
        int z = blockPos.getZ();
        int n4 = x - (int)n;
        while ((float)n4 <= (float)x + n) {
            int n5 = z - (int)n;
            while ((float)n5 <= (float)z + n) {
                int n6 = b2 ? y - (int)n : y;
                while (true) {
                    float f = n6;
                    float f2 = b2 ? (float)y + n : (float)(y + n2);
                    if (!(f < f2)) break;
                    double n7 = (x - n4) * (x - n4) + (z - n5) * (z - n5) + (b2 ? (y - n6) * (y - n6) : 0);
                    if (n7 < (double)(n * n) && (!b || n7 >= (double)((n - 1.0f) * (n - 1.0f)))) {
                        list.add(new BlockPos(n4, n6 + n3, n5));
                    }
                    ++n6;
                }
                ++n5;
            }
            ++n4;
        }
        return list;
    }
}
