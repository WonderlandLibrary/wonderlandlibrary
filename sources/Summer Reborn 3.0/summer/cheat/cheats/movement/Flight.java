
package summer.cheat.cheats.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Timer;
import summer.Summer;
import summer.base.manager.CheatManager;
import summer.base.manager.Selection;
import summer.base.manager.config.Cheats;
import summer.base.utilities.MathUtils;
import summer.base.utilities.MoveUtils;
import summer.base.utilities.TimerUtils;
import summer.cheat.eventsystem.EventTarget;
import summer.cheat.eventsystem.events.client.EventPacket;
import summer.cheat.eventsystem.events.client.EventSendPacket;
import summer.cheat.eventsystem.events.player.EventMotion;
import summer.cheat.eventsystem.events.player.EventUpdate;
import summer.cheat.guiutil.Setting;

import java.util.ArrayList;

public class Flight extends Cheats {
    public static Minecraft mc = Minecraft.getMinecraft();
    private TimerUtils animationStopwatch;
    private double randomValue;
    private int hypixelCounter;
    private int hypixelCounter2;
    private long prevBoost;

    private String mode;
    private int counter;
    public static boolean damaged;
    boolean hasReached;
    private boolean canboost;
    private double moveSpeed;
    private double lastDist;
    private TimerUtils timer;
    private TimerUtils timer2;
    private float timervalue;
    private boolean hypixelboost;
    private boolean decreasing2;
    private boolean boosted;
    int level;
    private static double yPos;
    private TargetStrafe ts;
    public static ArrayList<Packet> packets = new ArrayList<Packet>();
    private Setting flyType;
   // private Setting zoom;
    private Setting damage;

    public Flight() {
        super("Flight", "Fly like a little birdie", Selection.MOVEMENT);
        this.damaged = false;
        this.hasReached = false;
        this.boosted = false;
        this.level = 0;
        this.timer = new TimerUtils();
        this.timer2 = new TimerUtils();
    }

    @Override
    public void onSetup() {
        final ArrayList<String> options = new ArrayList<String>();
        options.add("Hypixel");
        Summer.INSTANCE.settingsManager.Property(flyType = new Setting("Mode", this, "Hypixel", options));
        //SummerClient.instance.sm.Property(new Setting("HypixelSpeed", this, 0.60, 0.3, 0.60, false));
        //SummerClient.instance.sm.Property(new Setting("FastTime", this, 1000.0, 0.0, 1000.0, true));
        //Summer.INSTANCE.settingsManager.Property(zoom = new Setting("Zoom", this, false));
        Summer.INSTANCE.settingsManager.Property(damage = new Setting("Damage", this, true));

    }

    @Override
    public void onEnable() {
        PlayerCapabilities pc = new PlayerCapabilities();
        pc.isCreativeMode = true;
        pc.allowFlying = true;
        pc.isFlying = true;
       // mc.getNetHandler().addToSendQueueNoEvent(new C13PacketPlayerAbilities(pc));
        damaged = false;
        super.onEnable();
        //mc.thePlayer.stepHeight = 0F;
        this.timer.reset();
        this.timer2.reset();
        this.mode = flyType.getValString();
        if (damage.getValBoolean()) {

            if (!damaged) {
                Timer.timerSpeed = 1f;
               this.damagePlayer();
                //this.sendPackets();
            }

            for (int i = 9; i <= 44; i++) {
                //	final ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                //	if (itemStack != null && itemStack.getItem() instanceof ItemEnderPearl) {
                //	ticks = i;
                //tp = true;
                //break;
                //}
            }

            if (Minecraft.thePlayer == null) {
                return;
            }

            if (this.mode.equalsIgnoreCase("Hypixel")) {
                this.canboost = true;
                double motionY = 0.41999998688697815D;
                double y = Minecraft.thePlayer.posY;
                this.timervalue = 1.0f;
                if (Minecraft.thePlayer.onGround) {
                    if ((Minecraft.thePlayer.isCollidedVertically)) {
                        if (Minecraft.thePlayer.isPotionActive(Potion.jump)) {
                            motionY += (Minecraft.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1f;
                        }
                        mc.thePlayer.motionY = motionY;
                    }
                    this.level = 1;
                    this.moveSpeed = 0.1;
                    this.hypixelboost = true;
                    this.lastDist = 0.0;

                }
                this.timer.reset();
            }
        }
    }

    @Override
    public void onDisable() {
        PlayerCapabilities pc = new PlayerCapabilities();
        pc.isCreativeMode = false;
        pc.allowFlying = false;
        pc.isFlying = false;
        Timer.timerSpeed = 1.0F;
        mc.thePlayer.stepHeight = .6F;
        super.onDisable();
        damaged = false;
        Timer.timerSpeed = 1.0f;
        mc.gameSettings.keyBindJump.pressed = false;
        this.prevBoost = System.currentTimeMillis();
        this.boosted = false;
        this.timer.reset();
        this.sendPackets();
        //mc.thePlayer.motionX = 0.0;
        //mc.thePlayer.motionZ = 0.0;
        //mc.thePlayer.motionY = 0.0;
        //mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);

    }

    @EventTarget
    public void Packet(final EventPacket ep) {
        if (ep.getPacket() instanceof C03PacketPlayer) {
            this.packets.add(ep.getPacket());
           ep.setCancelled(true);
        }

    }

    @EventTarget
    public void onUpdate(final EventUpdate e) {
        if (mc.gameSettings.keyBindJump.isKeyDown()) {
            Minecraft.thePlayer.motionY = 0.4;
        }
        if (mc.gameSettings.keyBindSneak.isKeyDown()) {
            Minecraft.thePlayer.motionY = -0.4;
        }
        //this.sendPackets();
        Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ);
        Minecraft.thePlayer.stepHeight = 0f;
        this.mode = flyType.getValString();

        Minecraft.thePlayer.cameraYaw = 0.00f;
        if (boosted && timer.hasReached(200)) {
            //mc.thePlayer.cameraYaw = 0.06f;
        }
        Minecraft.thePlayer.onGround = true;
        if (packets.size() >= 250) {
            sendPackets();
        }


        if (Minecraft.thePlayer.hurtTime > 3)
            damaged = true;
        final double speed = 5.9;
        this.setDisplayName(String.valueOf(this.getName()) + EnumChatFormatting.GRAY + " " + this.mode);
        if (this.mode.toLowerCase().contains("Hypixel")) {
            Minecraft.thePlayer.motionY = 0.0;
        }
        if (e.isPre()) {
            if (this.mode.equalsIgnoreCase("HypixelDamage")) {
                ++this.counter;
                if (this.counter == 1) {
                    Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 8.0E-6, Minecraft.thePlayer.posZ);
                } else if (this.counter == 2) {
                    Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY - 8.0E-6, Minecraft.thePlayer.posZ);
                    this.counter = 0;
                }
                if (damaged) {
                    if (this.mode.toLowerCase().contains("HypixelZoom")) {
                        Timer.timerSpeed = 6f;
                    }
                } else {
                    Timer.timerSpeed = 1.0f;
                }
            } else if (this.mode.equalsIgnoreCase("Hypixel")) {
                if (!this.boosted) {
                    Timer.timerSpeed = 1f;
                    this.boosted = true;
                } else if (this.timer.hasReached(1500)) {
                    Timer.timerSpeed = 1f;
                    MoveUtils.setMotion(null, 0.0);
                }
                if (this.timer.hasReached(8900)) {
                    //toggle();
                }
                final double xDist = Minecraft.thePlayer.posX - Minecraft.thePlayer.prevPosX;
                final double zDist = Minecraft.thePlayer.posZ - Minecraft.thePlayer.prevPosZ;
                this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
                if (this.canboost && this.hypixelboost) {
                    this.timervalue += (float) (this.decreasing2 ? -0.01 : 0.05);
                    if (this.timervalue >= 1.4) {
                        this.decreasing2 = true;
                    }
                    if (this.timervalue <= 0.9) {
                        this.decreasing2 = false;
                    }
                    if (this.timer.hasReached(2000.0)) {
                        this.canboost = false;
                        Minecraft.thePlayer.cameraYaw = 0.105f;
                    }
                }

                if (Minecraft.thePlayer.ticksExisted % 2 == 0) {
                    Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX,
                            Minecraft.thePlayer.posY
                                    + MathUtils.getRandomInRange(1.2354235325235235E-14, 1.2354235325235233E-13),
                            Minecraft.thePlayer.posZ);
                }
                Minecraft.thePlayer.motionY = 0.0;
            } else if (this.mode.equalsIgnoreCase("SAC")) {
                Minecraft.thePlayer.motionY = -0.019999999552965164;
                final double forward = 0.62;
                if (this.timer.hasReached(450.0)) {
                    final EntityPlayerSP thePlayer5 = Minecraft.thePlayer;
                    thePlayer5.motionY -= 0.5;
                    this.timer.reset();
                }
                if (mc.gameSettings.keyBindForward.isKeyDown()) {
                    MoveUtils.setMotion(null, forward);
                }
            }
        }
    }

    @EventTarget
    public void onMove(final EventMotion e) {
//         if (timer.hasReached(100)){ mc.thePlayer.jump(); timer.reset(); }
        final float yaw = Minecraft.thePlayer.rotationYaw;
        double strafe = MovementInput.moveStrafe;
        double forward = MovementInput.moveForward;
        final double mx = -Math.sin(Math.toRadians(yaw));
        final double mz = Math.cos(Math.toRadians(yaw));
        if (this.mode.equalsIgnoreCase("Vanilla")) {
            if (mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown()
                    || mc.gameSettings.keyBindRight.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown()) {
                MoveUtils.setMotion(e, 2.0);
            }
            Minecraft.thePlayer.capabilities.isFlying = false;
            Minecraft.thePlayer.motionY = 0.085;
            Minecraft.thePlayer.jumpMovementFactor = 2.0f;
            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                final EntityPlayerSP thePlayer3;
                final EntityPlayerSP thePlayer = thePlayer3 = Minecraft.thePlayer;
                ++thePlayer3.motionY;
            }
            if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                final EntityPlayerSP thePlayer4;
                final EntityPlayerSP thePlayer2 = thePlayer4 = Minecraft.thePlayer;
                --thePlayer4.motionY;
            }
        } else if (this.mode.equalsIgnoreCase("Hypixel")) {
            if (this.ts == null)
                this.ts = CheatManager.getInstance(TargetStrafe.class);

            if (forward == 0.0 && strafe == 0.0) {
                e.setX(0.0);
                e.setZ(0.0);
            }
            if (forward != 0.0 && strafe != 0.0) {
                forward *= Math.sin(0.7853981633974483);
                strafe *= Math.cos(0.7853981633974483);
            }
            if (this.level != 1 || (Minecraft.thePlayer.moveForward == 0.0f && Minecraft.thePlayer.moveStrafing == 0.0f)) {
                if (this.level == 2) {
                    this.level = 3;
                    this.moveSpeed *= 2.1499999;
                } else if (this.level == 3) {
                    this.level = 4;
                    double difference;
                    if (MoveUtils.getSpeedEffect() > 0) {
                        difference = 1.1 - 0.7
                                * (this.lastDist - MathUtils.getBaseMovementSpeed());
                    } else {
                        difference = 1.0 - 0.7
                                * (this.lastDist - MathUtils.getBaseMovementSpeed());
                    }
                    this.moveSpeed = this.lastDist - difference;
                } else {
                    if (Minecraft.theWorld
                            .getCollidingBoundingBoxes(Minecraft.thePlayer,
                                    Minecraft.thePlayer.getEntityBoundingBox().offset(0.0, Minecraft.thePlayer.motionY, 0.0))
                            .size() > 0 || Minecraft.thePlayer.isCollidedVertically) {
                        this.level = 1;
                    }
                    this.moveSpeed = this.lastDist - this.lastDist / 159.99999;
                }
            } else {
                this.level = 2;
                final double boost = Minecraft.thePlayer.isPotionActive(Potion.moveSpeed) ? 1.706 : 2.034;
                this.moveSpeed = boost * MathUtils.getBaseMovementSpeed() - 0.01;
            }
            this.moveSpeed = Math.max(this.moveSpeed, MathUtils.getBaseMovementSpeed());
            e.setX(forward * this.moveSpeed * mx + strafe * this.moveSpeed * mz);
            e.setZ(forward * this.moveSpeed * mz - strafe * this.moveSpeed * mx);
            if (forward == 0.0 && strafe == 0.0) {
                e.setX(0.0);
                e.setZ(0.0);
            }
            if (this.timer.hasReached(1700.0) && this.hypixelboost) {
                this.hypixelboost = false;
            }
            if (this.ts.canStrafe()) {
                this.ts.strafe(e, Math.max(MoveUtils.getBaseSpeed(), this.moveSpeed));
            }
        }
    }

    @EventTarget
    public void onSend(final EventSendPacket ep) {
    }

    @EventTarget
    public void onRecieve(final EventPacket e) {
        if (e.getPacket() instanceof S00PacketKeepAlive && this.mode.equalsIgnoreCase("Vanilla")) {
            e.setCancelled(true);
        }
    }

    public static void damagePlayer() {
        double x = mc.thePlayer.posX;
        double y = mc.thePlayer.posY;
        double z = mc.thePlayer.posZ;
        float minValue = 3.1F;
        if (mc.thePlayer.isPotionActive(Potion.jump)) {
            minValue += mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1.0F;
        }
        for (int i = 0; i < (int) ((minValue / (randomNumber(0.0890D, 0.0849D) - 1.0E-3D - Math.random() * 0.0002F - Math.random() * 0.0002F)) + 18); i++) {
            mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, y + randomNumber(0.0655D, 0.0625D) - randomNumber(1.0E-3D, 1.0E-2D) - Math.random() * 0.0002F, z, false));
            mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, y + Math.random() * 0.0002F, z, false));
        }
        mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer(true));
    }
    public static double randomNumber(double max, double min) {
        return Math.random() * (max - min) + min;
    }
    
    public static void sendPackets() {
		yPos += 1E-10;
			try {
				for (int i = 0; i < packets.size(); i++) {
					Packet packet = packets.get(i);
					if (packet != null) {

						mc.thePlayer.sendQueue.addToSendQueueNoEvent(packet);
						
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			packets.clear();
			mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + yPos, mc.thePlayer.posZ);
		}


}
