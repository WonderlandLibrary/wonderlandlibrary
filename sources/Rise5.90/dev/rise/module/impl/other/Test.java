/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.other;

import dev.rise.event.impl.motion.PostMotionEvent;
import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.event.impl.other.*;
import dev.rise.event.impl.packet.PacketReceiveEvent;
import dev.rise.event.impl.packet.PacketSendEvent;
import dev.rise.event.impl.render.BlurEvent;
import dev.rise.event.impl.render.Render2DEvent;
import dev.rise.event.impl.render.Render3DEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.module.impl.render.particles.Particle;
import dev.rise.ui.ingame.IngameGUI;
import dev.rise.util.InstanceAccess;
import dev.rise.util.math.TimeUtil;
import dev.rise.util.misc.EvictingList;
import dev.rise.util.player.MoveUtil;
import dev.rise.util.player.PacketUtil;
import dev.rise.util.render.ColorUtil;
import dev.rise.util.render.RenderUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@ModuleInfo(name = "Test", description = "Just made for Rise developers to test upcoming features", category = Category.OTHER)
public final class Test extends Module implements InstanceAccess {

    // A place for testing modules and bypasses, with all necessary features to do anything
    private int ticks, integer, nextMs;
    private float speed, interpolateSpeed;
    private Packet<?> p;
    private double posX, posY, posZ;
    private boolean bool, bool2, bool3;
    private final ConcurrentLinkedQueue<Packet<?>> packets = new ConcurrentLinkedQueue<>();
    private final TimeUtil timer = new TimeUtil();
    public static Entity entity;
    public static List<Vec3> pastPositions = new ArrayList<>();
    private String aString;

    public S08PacketPlayerPosLook s08;
    private Vec3 enabledPos = new Vec3(0, 0, 0);

    private final List<Double> jumpValues = new ArrayList<Double>() {{
        add(0.42);
        add(0.33319999363422365);
        add(0.24813599859094576);
        add(0.16477328182606651);
        add(0.08307781780646721);
        add(0.0030162615090425808);
        add(-0.0784000015258789);
        add(-0.1552320045166016);
        add(-0.230527368912964);
        add(-0.30431682745754424);
        add(-0.37663049823865513);
        add(-0.44749789698341763);
    }};

    private final List<Double> speedValues = new ArrayList<Double>() {{
        add(0.3023122842180768);
        add(0.2982909636320639);
        add(0.2946315653119493);
        add(0.29130151576857954);
        add(0.2882711731879136);
        add(0.285513563574277);
        add(0.283004140640921);
        add(0.2807205673107283);
        add(0.27864251688221625);
        add(0.2767514920910006);
        add(0.27503066045614655);
        add(0.27346470444576965);
    }};

    private double startingLocationX, startingLocationY, startingLocationZ, buffer;
    private boolean movingTowardsStartingLocation;

    private int ticksSinceFlag, pearlSlot;
    private int currentOffGroundTicks;
    private int offGroundTicks, vl, swings;
    private int maximumTestedValue;
    private NetworkManager networkManager;

    private Entity target;
    EvictingList<BlockPos> interactedBlocks = new EvictingList<>(100);
    private ArrayList<dev.rise.util.pathfinding.Vec3> path = new ArrayList<>();
    ArrayList<Double> averageSpeed = new ArrayList<>();
    Integer slot;
    private Vec3 hv;
    private final List<Particle> particles = new EvictingList<>(2000);

//    0.03684800296020513
//    -0.042288957922058
//    -0.11984318109609361
//    0.11760000228881837
//    0.03684800296020513
//    -0.042288957922058
//    -0.11984318109609361
//    0.11760000228881837
//    0.03684800296020513


    //0.40444491418477924


//        for (int i = 0; i < 3; i++) {
//        double position = startingLocationY;
//        for (double value : jumpValues) {
//            if (value == 0.42) value = 0.42f;
//            position += value;
//
//            if (position < startingLocationY) position = startingLocationY;
//            PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(startingLocationX, position, startingLocationZ, false));
//        }
//    }
//        PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(startingLocationX,startingLocationY,startingLocationZ, true));
//


    @Override
    public void onPreMotion(final PreMotionEvent event) {
        ticks++;
        ticksSinceFlag++;
        if (mc.thePlayer.onGround)
            offGroundTicks = 0;
        else
            offGroundTicks++;


        if (mc.thePlayer.onGround) {
            PacketUtil.sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY,
                    mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
            PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                    mc.thePlayer.posY - 0.1, mc.thePlayer.posZ, false));
            PacketUtil.sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY,
                    mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));

            mc.thePlayer.jump();

            mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 9.5, mc.thePlayer.posZ);
        }
    }

    @Override
    public void onPostMotion(final PostMotionEvent event) {
    }

    @Override
    public void onBlockCollide(final BlockCollideEvent event) {
    }

    public static void onStep() {
    }

    @Override
    public void onMoveButton(final MoveButtonEvent event) {

    }

    @Override
    public void onStrafe(final StrafeEvent event) {


    }

    double movementSpeed, baseSpeed, lastDistance;
    boolean lastDistanceReset;
    int stage;

    @Override
    public void onMove(final MoveEvent event) {
    }

    @Override
    public void onRender2DEvent(final Render2DEvent event) {
        final ScaledResolution scaledResolution = new ScaledResolution(mc);

    }

    @Override
    public void onRender3DEvent(final Render3DEvent event) {
    }

    @Override
    public void onAttackEvent(final AttackEvent event) {
    }

    @Override
    public void onBlur(final BlurEvent event) {
    }

    boolean lastGround;

    @Override
    public void onPacketSend(final PacketSendEvent event) {
        final Packet<?> p = event.getPacket();
    }

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        final Packet<?> p = event.getPacket();

        if (p instanceof S08PacketPlayerPosLook) {
            event.setCancelled(true);
        }
    }

    @Override
    protected void onEnable() {
        bool = false;
        bool2 = false;
        bool3 = false;
        ticks = 0;
        packets.clear();
        integer = 0;

        startingLocationX = mc.thePlayer.posX;
        startingLocationZ = mc.thePlayer.posZ;
        startingLocationY = mc.thePlayer.posY;
        movingTowardsStartingLocation = false;
        ticksSinceFlag = 999;
        currentOffGroundTicks = 0;
        entity = null;
        offGroundTicks = 0;
        target = null;
        stage = 0;
        lastDistance = 0;
        movementSpeed = MoveUtil.getBaseMoveSpeed(); // 0
        mc.timer.timerSpeed = 1.0F;
        timer.reset();
        speed = 0;
        averageSpeed.clear();
        path = new ArrayList<>();
        this.p = null;
        swings = 0;
        buffer = 0;
        vl = 0;

        ticksSinceFlag = -99999999;

        enabledPos = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
    }

    @Override
    protected void onDisable() {
        entity = null;
        mc.timer.timerSpeed = 1.0F;
        mc.thePlayer.speedInAir = 0.02f;
        mc.thePlayer.noClip = false;
        mc.thePlayer.stepHeight = 0.6f;
        EntityPlayer.movementYaw = null;
        MoveUtil.stop();

//        add(0.42);
//        add(0.33319999363422365);
//        add(0.24813599859094576);
//        add(0.16477328182606651);
//        add(0.08307781780646721);
//        add(0.0030162615090425808);
//        add(-0.0784000015258789);
//        add(-0.1552320045166016);
//        add(-0.230527368912964);
//        add(-0.30431682745754424);

        packets.forEach(PacketUtil::sendPacketWithoutEvent);
        packets.clear();
    }

    private void renderBreadCrumbs(final List<Vec3> vec3s) {
        GlStateManager.disableDepth();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        int i = 0;
        try {
            for (final Vec3 v : vec3s) {

                i++;

                boolean draw = true;

                final double x = v.xCoord - (mc.getRenderManager()).renderPosX;
                final double y = v.yCoord - (mc.getRenderManager()).renderPosY;
                final double z = v.zCoord - (mc.getRenderManager()).renderPosZ;

                final double distanceFromPlayer = mc.thePlayer.getDistance(v.xCoord, v.yCoord - 1, v.zCoord);
                int quality = (int) (distanceFromPlayer * 4 + 10);

                if (quality > 350)
                    quality = 350;

                if (i % 10 != 0 && distanceFromPlayer > 25) {
                    draw = false;
                }

                if (i % 3 == 0 && distanceFromPlayer > 15) {
                    draw = false;
                }

                if (draw) {

                    GL11.glPushMatrix();
                    GL11.glTranslated(x, y, z);

                    final float scale = 0.04f;
                    GL11.glScalef(-scale, -scale, -scale);

                    GL11.glRotated(-(mc.getRenderManager()).playerViewY, 0.0D, 1.0D, 0.0D);
                    GL11.glRotated((mc.getRenderManager()).playerViewX, 1.0D, 0.0D, 0.0D);

                    final Color color1 = new Color(78, 161, 253, 100);
                    final Color color2 = new Color(78, 253, 154, 100);

                    final Color c = ColorUtil.mixColors(color1, color2, (Math.sin(IngameGUI.ticks + -(i / (float) vec3s.size()) * 13) + 1) * 0.5f);
                    RenderUtil.drawFilledCircleNoGL(0, 0, 0.7, c.hashCode(), quality);

                    if (distanceFromPlayer < 4)
                        RenderUtil.drawFilledCircleNoGL(0, 0, 1.4, new Color(c.getRed(), c.getGreen(), c.getBlue(), 50).hashCode(), quality);

                    if (distanceFromPlayer < 20)
                        RenderUtil.drawFilledCircleNoGL(0, 0, 2.3, new Color(c.getRed(), c.getGreen(), c.getBlue(), 30).hashCode(), quality);

                    GL11.glScalef(0.8f, 0.8f, 0.8f);

                    GL11.glPopMatrix();

                }

            }
        } catch (final ConcurrentModificationException ignored) {
        }

        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GlStateManager.enableDepth();

        GL11.glColor3d(255, 255, 255);
    }
}
