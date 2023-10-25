/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.movement;

import de.Hero.settings.Setting;
import io.netty.util.internal.ThreadLocalRandom;
import java.util.ArrayList;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventAir;
import me.Tengoku.Terror.event.events.EventMotion;
import me.Tengoku.Terror.event.events.EventPacket;
import me.Tengoku.Terror.event.events.EventUpdate;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.module.render.HeadRotations;
import me.Tengoku.Terror.module.world.Scaffold;
import me.Tengoku.Terror.util.MathUtils;
import me.Tengoku.Terror.util.MoveUtil;
import me.Tengoku.Terror.util.MoveUtils;
import me.Tengoku.Terror.util.PlayerUtils;
import me.Tengoku.Terror.util.ScaffoldUtils;
import me.Tengoku.Terror.util.Timer;
import me.Tengoku.Terror.util.TimerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.item.ItemFireball;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class Fly
extends Module {
    private double lastDist;
    public static double yPos2;
    public static boolean damaged;
    private boolean hypixelboost;
    boolean canFly = false;
    private double speedStage;
    private boolean decreasing2;
    private EnumFacing currentFacing;
    private boolean canboost;
    private BlockPos currentPos;
    private Setting TPBoost;
    private double moveSpeed;
    public static double z;
    private boolean boosted;
    public static double x;
    private int hypixelCounter;
    private ArrayList<Packet> packets2;
    boolean jumped = false;
    int state = 0;
    int previousSlot;
    TimerUtils timer6;
    private float timervalue;
    private static Setting blinkPackets;
    private long prevBoost;
    int jumpcount = 0;
    static ArrayList<Packet> packets;
    boolean hasReached;
    private boolean doFly;
    private int hypixelCounter2;
    private static boolean hasClipped;
    private float stage;
    private Setting delay;
    int level;
    private float rotationYaw;
    int count = 0;
    public static double yPos;
    Timer timer = new Timer();
    private double randomValue;
    private int ticks;
    private int counter;
    private boolean hasJumped = false;
    public static double y;

    /*
     * Exception decompiling
     */
    public static void sendPacketsHypixel() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [3[DOLOOP]], but top level block is 1[TRYBLOCK]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    static {
        packets = new ArrayList();
        damaged = false;
        hasClipped = false;
    }

    /*
     * Unable to fully structure code
     */
    public static void sendPackets() {
        Fly.yPos += 1.0E-10;
        var0 = 0;
        while (var0 < Fly.packets.size()) {
            var1_2 = Fly.packets.get(var0);
            if (var1_2 == null || !(var1_2 instanceof C03PacketPlayer)) ** GOTO lbl12
            Minecraft.thePlayer.sendQueue.addToSendQueueNoEvent(var1_2);
            var2_3 = (C03PacketPlayer)var1_2;
            try {
                Minecraft.thePlayer.setPosition(var2_3.getPositionX(), var2_3.getPositionY(), var2_3.getPositionZ());
lbl12:
                // 2 sources

                ++var0;
            }
            catch (Exception var0_1) {
                var0_1.printStackTrace();
                break;
            }
        }
        Fly.packets.clear();
    }

    public static double randomNumber(double d, double d2) {
        return Math.random() * (d - d2) + d2;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.doFly = false;
        this.ticks = 0;
        this.stage = 0.0f;
        x = 0.0;
        y = 0.0;
        z = 0.0;
        hasClipped = false;
        PlayerCapabilities playerCapabilities = new PlayerCapabilities();
        playerCapabilities.isCreativeMode = false;
        playerCapabilities.allowFlying = false;
        playerCapabilities.isFlying = false;
        Minecraft.thePlayer.stepHeight = 0.6f;
        damaged = false;
        Minecraft.gameSettings.keyBindJump.pressed = false;
        this.prevBoost = System.currentTimeMillis();
        this.boosted = false;
        this.timer.reset();
        hasClipped = false;
        this.hasJumped = false;
        String string = Exodus.INSTANCE.settingsManager.getSettingByName("Fly Mode").getValString();
        if (string.equalsIgnoreCase("Blink")) {
            Fly.pushBlinkPackets();
        }
        if (string.equalsIgnoreCase("Watchdog_Blink")) {
            Fly.sendPackets();
        }
        this.jumpcount = 0;
        this.jumped = false;
        this.canFly = false;
        damaged = false;
        packets.clear();
        Minecraft.gameSettings.keyBindForward.pressed = false;
        Minecraft.thePlayer.capabilities.isFlying = false;
        Fly.mc.timer.timerSpeed = 1.0f;
    }

    @EventTarget
    public void onAir(EventAir eventAir) {
        String string = Exodus.INSTANCE.settingsManager.getSettingByName("Fly Mode").getValString();
        if (string.equalsIgnoreCase("Watchdog") && this.doFly) {
            eventAir.setCancelled(true);
            Fly.mc.timer.timerSpeed = 1.0f;
        }
        if (string.equalsIgnoreCase("AirWalk")) {
            eventAir.setCancelled(true);
            Fly.mc.timer.timerSpeed = 1.2f;
            MoveUtil.setMotion(0.15f);
        }
        if (string.equalsIgnoreCase("Hypixel_Fast")) {
            eventAir.setCancelled(true);
        }
        if (string.equalsIgnoreCase("Watchdog_Blink")) {
            eventAir.setCancelled(true);
        }
        if (string.equalsIgnoreCase("Blink")) {
            eventAir.setCancelled(true);
        }
    }

    protected int findDamageItems() {
        int n = 0;
        while (n < 9) {
            if (Minecraft.thePlayer.inventoryContainer.getSlot(36 + n).getHasStack()) {
                if (Minecraft.thePlayer.inventoryContainer.getSlot(36 + n).getStack().getItem() instanceof ItemFireball) {
                    return n;
                }
            }
            ++n;
        }
        return -1;
    }

    @Override
    public void onEnable() {
        String string = Exodus.INSTANCE.settingsManager.getSettingByName("Fly Mode").getValString();
        boolean bl = Exodus.INSTANCE.settingsManager.getSettingByName("Damage").getValBoolean();
        super.onEnable();
        hasClipped = false;
        if (Minecraft.thePlayer != null) {
            x = Minecraft.thePlayer.posX;
            y = Minecraft.thePlayer.posY;
            z = Minecraft.thePlayer.posZ;
        }
        if (string.equalsIgnoreCase("Watchdog")) {
            Exodus.INSTANCE.getModuleManager().getModuleByClass(Scaffold.class).toggle();
        }
        if (Minecraft.thePlayer != null) {
            yPos = Minecraft.thePlayer.getEyeHeight();
            yPos2 = Minecraft.thePlayer.posY;
        }
        if (string.equalsIgnoreCase("Watchdog_Blink")) {
            Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY - 0.5, Minecraft.thePlayer.posZ);
            Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + MathUtils.getRandomInRange(0.49172645807266235, 0.5287159085273743), Minecraft.thePlayer.posZ);
            this.canboost = true;
            double d = 0.42f;
            double d2 = Minecraft.thePlayer.posY;
            this.timervalue = (float)Exodus.INSTANCE.settingsManager.getSettingByModule("Fly Timer", this).getValDouble();
            if (Minecraft.thePlayer.onGround) {
                if (Minecraft.thePlayer.isCollidedVertically) {
                    if (Minecraft.thePlayer.isPotionActive(Potion.jump)) {
                        d += (double)((float)(Minecraft.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1f);
                    }
                    Minecraft.thePlayer.motionY = d;
                }
                this.level = 1;
                this.moveSpeed = 0.1;
                this.hypixelboost = true;
                this.lastDist = 0.0;
            }
            this.timer.reset();
        }
        if (string.equalsIgnoreCase("AAC")) {
            if (Minecraft.thePlayer.onGround) {
                Minecraft.thePlayer.jump();
            }
        }
        if (bl) {
            if (Minecraft.thePlayer != null) {
                if (string.equalsIgnoreCase("Watchdog")) {
                    Minecraft.thePlayer.motionY = 0.425;
                } else {
                    PlayerUtils.damagePlayer(1);
                }
            }
        }
    }

    public static void pushBlinkPackets() {
        int n = 0;
        while (n < packets.size()) {
            Packet packet = packets.get(n);
            if (packet != null && packet instanceof C03PacketPlayer) {
                if ((double)n > blinkPackets.getValDouble()) {
                    return;
                }
                Minecraft.thePlayer.sendQueue.addToSendQueueNoEvent(packet);
            }
            ++n;
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        String string = Exodus.INSTANCE.settingsManager.getSettingByName("Fly Mode").getValString();
        this.setDisplayName("Flight \ufffdf" + string);
        if (string.equalsIgnoreCase("Watchdog_Blink")) {
            Fly.mc.timer.timerSpeed = (float)Exodus.INSTANCE.settingsManager.getSettingByModule("Fly Timer", this).getValDouble();
            if (!this.boosted) {
                Fly.mc.timer.timerSpeed = (float)Exodus.INSTANCE.settingsManager.getSettingByModule("Fly Timer", this).getValDouble();
                this.boosted = true;
            } else if (this.timer.hasTimeElapsed(1500L, true)) {
                Fly.mc.timer.timerSpeed = (float)Exodus.INSTANCE.settingsManager.getSettingByModule("Fly Timer", this).getValDouble();
                MoveUtils.setMotion(null, 0.0);
            }
            double d = Minecraft.thePlayer.posX - Minecraft.thePlayer.prevPosX;
            double d2 = Minecraft.thePlayer.posZ - Minecraft.thePlayer.prevPosZ;
            this.lastDist = Math.sqrt(d * d + d2 * d2);
            if (this.canboost && this.hypixelboost) {
                this.timervalue += (float)(this.decreasing2 ? -0.01 : 0.05);
                if ((double)this.timervalue >= 1.4) {
                    this.decreasing2 = true;
                }
                if ((double)this.timervalue <= 0.9) {
                    this.decreasing2 = false;
                }
                if (this.timer.hasTimeElapsed(2000L, false)) {
                    this.canboost = false;
                    Minecraft.thePlayer.cameraYaw = 0.105f;
                }
            }
            if (Minecraft.thePlayer.ticksExisted % 2 == 0) {
                Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + MathUtils.getRandomInRange(1.2354235325235235E-14, 1.2354235325235233E-13), Minecraft.thePlayer.posZ);
            }
        }
        if (string.equalsIgnoreCase("Vanilla")) {
            Fly.mc.timer.timerSpeed = (float)Exodus.INSTANCE.settingsManager.getSettingByName("Fly Timer").getValDouble();
            Minecraft.thePlayer.capabilities.isFlying = true;
        }
    }

    public static void placeBlock(EventMotion eventMotion) {
        eventMotion.setPitch(90.0f);
        int n = ScaffoldUtils.grabBlockSlot();
        Minecraft.thePlayer.inventory.currentItem = n;
        if (eventMotion.isOnGround()) {
            Minecraft.thePlayer.jump();
        }
        Minecraft.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(Minecraft.thePlayer), 255, Minecraft.thePlayer.getHeldItem(), 0.0f, 0.0f, 0.0f));
        if (Exodus.INSTANCE.getModuleManager().getModuleByClass(HeadRotations.class).isToggled()) {
            Minecraft.thePlayer.rotationPitchHead = EventMotion.getPitch();
        }
    }

    public Fly() {
        super("Flight", 33, Category.MOVEMENT, "Ever wanted to be superman?");
        this.packets2 = new ArrayList();
        this.timer6 = new TimerUtils();
    }

    @EventTarget
    public void onPacket(EventPacket eventPacket) {
        Packet<INetHandlerPlayClient> packet;
        String string = Exodus.INSTANCE.settingsManager.getSettingByName("Fly Mode").getValString();
        if (eventPacket.getPacket() instanceof S08PacketPlayerPosLook) {
            packet = (S08PacketPlayerPosLook)eventPacket.getPacket();
            y = S08PacketPlayerPosLook.getY();
            if (string.equalsIgnoreCase("Watchdog_Y")) {
                y = S08PacketPlayerPosLook.getY();
                this.doFly = true;
            }
        }
        if (eventPacket.getPacket() instanceof C03PacketPlayer) {
            if (string.equalsIgnoreCase("Watchdog_Blink")) {
                packets.add(eventPacket.getPacket());
                eventPacket.setCancelled(true);
            }
            if (string.equalsIgnoreCase("Blink")) {
                packets.add(eventPacket.getPacket());
                eventPacket.setCancelled(true);
                packet = (C03PacketPlayer)eventPacket.getPacket();
                double d = Minecraft.thePlayer.posX;
                double d2 = Minecraft.thePlayer.posY;
                double d3 = Minecraft.thePlayer.posZ;
                float f = Minecraft.thePlayer.rotationYaw;
                float f2 = Minecraft.thePlayer.rotationPitch;
                if (((C03PacketPlayer)packet).isMoving()) {
                    d = ((C03PacketPlayer)packet).getPositionX();
                    d2 = ((C03PacketPlayer)packet).getPositionY();
                    d3 = ((C03PacketPlayer)packet).getPositionZ();
                }
                if (((C03PacketPlayer)packet).getRotating()) {
                    f = ((C03PacketPlayer)packet).getYaw();
                    f2 = ((C03PacketPlayer)packet).getPitch();
                }
                Minecraft.thePlayer.sendQueue.doneLoadingTerrain = true;
            }
        }
        if (eventPacket.getPacket() instanceof C0BPacketEntityAction && string.equalsIgnoreCase("Blink")) {
            packets.add(eventPacket.getPacket());
            eventPacket.setCancelled(true);
        }
        if (eventPacket.getPacket() instanceof C0BPacketEntityAction && string.equalsIgnoreCase("Watchdog_Blink")) {
            packets.add(eventPacket.getPacket());
            eventPacket.setCancelled(true);
        }
        if (eventPacket.getPacket() instanceof S08PacketPlayerPosLook && damaged && string.equalsIgnoreCase("Watchdog_Blink")) {
            eventPacket.setCancelled(true);
        }
    }

    public static void damagePlayer() {
        double d = Minecraft.thePlayer.posX;
        double d2 = Minecraft.thePlayer.posY;
        double d3 = Minecraft.thePlayer.posZ;
        float f = 3.1f;
        if (Minecraft.thePlayer.isPotionActive(Potion.jump)) {
            f += (float)Minecraft.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1.0f;
        }
        int n = 0;
        while (n < (int)((double)f / (Fly.randomNumber(0.089, 0.0849) - 0.001 - Math.random() * (double)2.0E-4f - Math.random() * (double)2.0E-4f) + 18.0)) {
            mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(d, d2 + Fly.randomNumber(0.0655, 0.0625) - Fly.randomNumber(0.001, 0.01) - Math.random() * (double)2.0E-4f, d3, false));
            mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(d, d2 + Math.random() * (double)2.0E-4f, d3, false));
            ++n;
        }
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public void onPre(EventMotion var1_1) {
        block27: {
            block28: {
                if (!var1_1.isPre()) break block27;
                var2_2 = Exodus.INSTANCE.settingsManager.getSettingByClass("Fly Mode", Fly.class).getValString();
                if (var2_2.equalsIgnoreCase("Watchdog_Y")) {
                    Minecraft.thePlayer.cameraPitch = 0.05f;
                    Minecraft.thePlayer.cameraYaw = 0.05f;
                    Minecraft.thePlayer.posY = Fly.y;
                    if (Minecraft.thePlayer.onGround && this.stage == 0.0f) {
                        Minecraft.thePlayer.motionY = 0.09;
                    }
                    this.stage += 1.0f;
                    if (Minecraft.thePlayer.onGround && this.stage > 2.0f && !Fly.hasClipped) {
                        Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY - 0.15, Minecraft.thePlayer.posZ, false));
                        Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.15, Minecraft.thePlayer.posZ, true));
                        Fly.hasClipped = true;
                    }
                    if (this.doFly) {
                        Minecraft.thePlayer.motionY = 0.0;
                        Minecraft.thePlayer.onGround = true;
                        Fly.mc.timer.timerSpeed = 1.25f;
                    } else {
                        MoveUtil.setMotion(0.0);
                    }
                }
                if (var2_2.equalsIgnoreCase("Watchdog")) {
                    if (!this.doFly && var1_1.isOnGround()) {
                        var3_3 = ScaffoldUtils.grabBlockSlot();
                        Minecraft.thePlayer.jump();
                        Minecraft.thePlayer.inventory.currentItem = var3_3;
                        if (Exodus.INSTANCE.getModuleManager().getModuleByClass(Scaffold.class).isToggled() && this.timer.hasTimeElapsed(1000L, true)) {
                            Minecraft.thePlayer.motionY = 0.041999999433755875;
                            Minecraft.thePlayer.posY -= Fly.y;
                            Fly.mc.timer.timerSpeed = 0.05f;
                            Minecraft.thePlayer.jump();
                            this.doFly = true;
                            Exodus.INSTANCE.getModuleManager().getModuleByClass(Scaffold.class).toggle();
                            Minecraft.gameSettings.keyBindForward.pressed = true;
                        }
                    }
                    var1_1.setPitch(90.0f);
                    if (Exodus.INSTANCE.getModuleManager().getModuleByClass(HeadRotations.class).isToggled()) {
                        Minecraft.thePlayer.rotationPitchHead = EventMotion.getPitch();
                    }
                    var1_1.setOnGround(ThreadLocalRandom.current().nextBoolean());
                }
                if (var2_2.equalsIgnoreCase("Watchdog_Blink")) {
                    var1_1.setOnGround(true);
                }
                if (var2_2.equalsIgnoreCase("Blink")) {
                    var1_1.setOnGround(true);
                }
                if (var2_2.equalsIgnoreCase("DecreaseY")) {
                    Minecraft.thePlayer.cameraYaw = 0.05f;
                    Minecraft.thePlayer.setSpeed(0.25);
                    Minecraft.thePlayer.motionY = 0.0;
                }
                if (!var2_2.equalsIgnoreCase("TP")) break block28;
                if (Minecraft.thePlayer == null) ** GOTO lbl-1000
                if (Minecraft.theWorld != null) {
                    Fly.mc.timer.timerSpeed = 0.25f;
                    Minecraft.thePlayer.capabilities.isFlying = true;
                    if (this.timer.hasTimeElapsed((long)this.delay.getValDouble(), true)) {
                        this.rotationYaw = Minecraft.thePlayer.rotationYaw;
                        var3_4 = Math.toRadians(this.rotationYaw);
                        var5_6 = -Math.sin(var3_4) * this.TPBoost.getValDouble();
                        var7_7 = Math.cos(var3_4) * this.TPBoost.getValDouble();
                        Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX + var5_6, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ + var7_7);
                        Fly.mc.timer.timerSpeed = (float)Exodus.INSTANCE.settingsManager.getSettingByName("Fly Timer").getValDouble();
                        this.timer.reset();
                    }
                } else lbl-1000:
                // 2 sources

                {
                    Fly.mc.timer.timerSpeed = 1.0f;
                    this.toggle();
                }
            }
            if (var2_2.equalsIgnoreCase("Motion")) {
                Minecraft.thePlayer.cameraYaw = 0.18181817f;
                Minecraft.thePlayer.setSpeed(0.5f);
                Minecraft.thePlayer.motionY = 0.0;
                Fly.mc.timer.timerSpeed = (float)Exodus.INSTANCE.settingsManager.getSettingByName("Fly Timer").getValDouble();
            }
            if (var2_2.equalsIgnoreCase("Watchdog_Blink")) {
                this.canboost = true;
                var3_5 = 0.41999998688697815;
                var5_6 = Minecraft.thePlayer.posY;
                this.timervalue = 1.0f;
                if (Minecraft.thePlayer.onGround) {
                    if (Minecraft.thePlayer.isCollidedVertically) {
                        if (Minecraft.thePlayer.isPotionActive(Potion.jump)) {
                            var3_5 += (double)((float)(Minecraft.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1f);
                        }
                    }
                    this.level = 1;
                    this.moveSpeed = 0.1;
                    this.hypixelboost = true;
                    this.lastDist = 0.0;
                }
            }
            if (var2_2.equalsIgnoreCase("Packet")) {
                var1_1.setOnGround(true);
                Minecraft.thePlayer.motionY = 0.0;
                if (Minecraft.thePlayer.isMoving()) {
                    Minecraft.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY - 9.99999993922529E-9 + 9.99999993922529E-9, Minecraft.thePlayer.posZ, var1_1.isOnGround()));
                    Minecraft.thePlayer.setSpeed(1.216f);
                }
                Fly.mc.timer.timerSpeed = 0.5f;
            }
            if ((var3_3 = (int)Exodus.INSTANCE.settingsManager.getSettingByName("Packet Rotations").getValBoolean()) != 0) {
                if (var2_2.equalsIgnoreCase("Packet")) {
                    var1_1.setPitch((float)(35.0 + Math.random()));
                    var1_1.setYaw(130.0f + EventMotion.getPitch());
                    if (Exodus.INSTANCE.moduleManager.getModuleByName("Head Rotations").isToggled()) {
                        Minecraft.thePlayer.rotationYawHead = EventMotion.getYaw();
                        Minecraft.thePlayer.renderYawOffset = EventMotion.getYaw();
                        Minecraft.thePlayer.rotationPitchHead = EventMotion.getPitch();
                    }
                }
            } else {
                var1_1.setYaw(130.0f + EventMotion.getPitch());
                var1_1.setPitch(0.0f);
                if (Exodus.INSTANCE.moduleManager.getModuleByName("Head Rotations").isToggled()) {
                    Minecraft.thePlayer.rotationYawHead = EventMotion.getYaw();
                    Minecraft.thePlayer.renderYawOffset = EventMotion.getYaw();
                    Minecraft.thePlayer.rotationPitchHead = EventMotion.getPitch();
                }
            }
        }
    }

    @Override
    public void setup() {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("Vanilla");
        arrayList.add("Packet");
        arrayList.add("TP");
        arrayList.add("Fast");
        arrayList.add("Motion");
        arrayList.add("Watchdog_Blink");
        arrayList.add("Blink");
        arrayList.add("Watchdog");
        arrayList.add("Watchdog_Y");
        arrayList.add("AirWalk");
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Fly Mode", (Module)this, "Packet", arrayList));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Fly Timer", this, 1.0, 1.0, 10.0, true));
        this.delay = new Setting("Delay", this, 500.0, 1.0, 1000.0, true);
        Exodus.INSTANCE.settingsManager.addSetting(this.delay);
        this.TPBoost = new Setting("TP Boost", this, 3.0, 1.0, 15.0, true);
        Exodus.INSTANCE.settingsManager.addSetting(this.TPBoost);
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Packet Rotations", this, false));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Damage", this, true));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Boost Factor", this, 2.0, 1.0, 15.0, true));
        blinkPackets = new Setting("Blink Packets", this, 10.0, 10.0, 100.0, true);
        Exodus.INSTANCE.settingsManager.addSetting(blinkPackets);
    }

    private void AAC4Longjump() {
        if (Minecraft.thePlayer.motionY > 0.0) {
            Fly.mc.timer.timerSpeed = 0.85f;
            Minecraft.thePlayer.speedInAir = 0.025f;
            if (Minecraft.thePlayer.motionY > 0.2) {
                Minecraft.thePlayer.motionX *= (double)1.017f;
                Minecraft.thePlayer.motionZ *= (double)1.017f;
            } else {
                Minecraft.thePlayer.motionX *= (double)1.0112f;
                Minecraft.thePlayer.motionZ *= (double)1.0112f;
            }
            Minecraft.thePlayer.motionY += (double)0.03f;
        } else {
            Fly.mc.timer.timerSpeed = 0.35f;
            Minecraft.thePlayer.motionY += (double)0.02f;
            Minecraft.thePlayer.speedInAir = 0.025f;
            if (Minecraft.thePlayer.motionY < (double)-0.6f) {
                if (Minecraft.thePlayer.motionX > 0.05) {
                    Minecraft.thePlayer.motionX += (double)0.027f;
                } else if (Minecraft.thePlayer.motionX < -0.05) {
                    Minecraft.thePlayer.motionX -= (double)0.027f;
                }
                if (Minecraft.thePlayer.motionZ > 0.05) {
                    Minecraft.thePlayer.motionZ += (double)0.027f;
                } else if (Minecraft.thePlayer.motionZ < -0.05) {
                    Minecraft.thePlayer.motionZ -= (double)0.027f;
                }
            } else {
                if (Minecraft.thePlayer.motionX > 0.05) {
                    Minecraft.thePlayer.motionX += (double)0.017f;
                } else if (Minecraft.thePlayer.motionX < -0.05) {
                    Minecraft.thePlayer.motionX -= (double)0.017f;
                }
                if (Minecraft.thePlayer.motionZ > 0.05) {
                    Minecraft.thePlayer.motionZ += (double)0.017f;
                } else if (Minecraft.thePlayer.motionZ < -0.05) {
                    Minecraft.thePlayer.motionZ -= (double)0.017f;
                }
            }
        }
    }
}

