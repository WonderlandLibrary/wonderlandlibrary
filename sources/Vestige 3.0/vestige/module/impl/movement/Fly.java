package vestige.module.impl.movement;

import net.minecraft.block.BlockAir;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import vestige.Vestige;
import vestige.event.Listener;
import vestige.event.impl.*;
import vestige.module.Category;
import vestige.module.Module;
import vestige.setting.impl.BooleanSetting;
import vestige.setting.impl.DoubleSetting;
import vestige.setting.impl.ModeSetting;
import vestige.util.network.PacketUtil;
import vestige.util.player.MovementUtil;
import vestige.util.player.PlayerUtil;
import vestige.util.world.WorldUtil;

public class Fly extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla", "Collision", "NCP", "Blocksmc", "Velocity");

    private final ModeSetting vanillaMode = new ModeSetting("Vanilla Mode", () -> mode.is("Vanilla"), "Motion", "Motion", "Creative");
    private final DoubleSetting vanillaSpeed = new DoubleSetting("Vanilla speed", () -> mode.is("Vanilla") && vanillaMode.is("Motion"), 2, 0.2, 9, 0.2);
    private final DoubleSetting vanillaVerticalSpeed = new DoubleSetting("Vanilla vertical speed", () -> mode.is("Vanilla") && vanillaMode.is("Motion"), 2, 0.2, 9, 0.2);

    private final ModeSetting collisionMode = new ModeSetting("Collision mode", () -> mode.is("Collision"), "Airwalk", "Airwalk", "Airjump");

    private final ModeSetting ncpMode = new ModeSetting("NCP Mode", () -> mode.is("NCP"), "Old", "Old");
    private final DoubleSetting ncpSpeed = new DoubleSetting("NCP speed", () -> mode.is("NCP") && ncpMode.is("Old"), 1, 0.3, 1.7, 0.05);
    private final BooleanSetting damage = new BooleanSetting("Damage", () -> mode.is("NCP") && ncpMode.is("Old"), false);

    private final ModeSetting velocityMode = new ModeSetting("Velocity Mode", () -> mode.is("Velocity"), "Bow", "Bow", "Bow2", "Wait for hit");
    private final BooleanSetting legit = new BooleanSetting("Legit", () -> mode.is("Bow") || mode.is("Bow2"), false);

    private final BooleanSetting automated = new BooleanSetting("Automated", () -> mode.is("Blocksmc"), false);

    private double speed;

    private boolean takingVelocity;

    private double velocityX, velocityY, velocityZ;
    private double velocityDist;

    private int ticksSinceVelocity;

    private int counter, ticks;

    private boolean started, done;

    private double lastMotionX, lastMotionY, lastMotionZ;

    private boolean hasBow;
    private int oldSlot;

    private boolean notMoving;

    private float lastYaw, lastPitch;

    private BlockPos lastBarrier;

    private double lastY;

    public Fly() {
        super("Fly", Category.MOVEMENT);
        this.addSettings(mode, vanillaMode, vanillaSpeed, vanillaVerticalSpeed, ncpMode, ncpSpeed, damage, velocityMode, legit, automated);
    }

    @Override
    public void onEnable() {
        ticksSinceVelocity = Integer.MAX_VALUE;

        counter = ticks = 0;

        started = done = false;

        hasBow = false;

        notMoving = false;

        lastMotionX = mc.thePlayer.motionX;
        lastMotionY = mc.thePlayer.motionY;
        lastMotionZ = mc.thePlayer.motionZ;

        lastYaw = mc.thePlayer.rotationYaw;
        lastPitch = mc.thePlayer.rotationPitch;

        lastY = mc.thePlayer.posY;

        lastBarrier = null;

        switch (mode.getMode()) {
            case "NCP":
                if(ncpMode.is("Old")) {
                    if(mc.thePlayer.onGround) {
                        speed = ncpSpeed.getValue();

                        if(damage.isEnabled()) {
                            PlayerUtil.ncpDamage();
                        }
                    } else {
                        speed = 0.28;
                    }
                }
                break;
            case "Velocity":
                if(mc.thePlayer.onGround) {
                    mc.thePlayer.jump();
                }
                break;
        }
    }

    @Override
    public void onDisable() {
        mc.thePlayer.capabilities.isFlying = false;

        Vestige.instance.getPacketBlinkHandler().stopAll();

        switch (mode.getMode()) {
            case "Vanilla":
                if(vanillaMode.is("Motion")) {
                    MovementUtil.strafe(0);
                }
                break;
            case "NCP":
                if(ncpMode.is("Old")) {
                    MovementUtil.strafe(0);
                }
                break;
            case "Velocity":
                switch (velocityMode.getMode()) {
                    case "Wait for hit":
                        mc.thePlayer.motionX = lastMotionX * 0.91;
                        mc.thePlayer.motionY = lastMotionY;
                        mc.thePlayer.motionZ = lastMotionZ * 0.91;
                        break;
                    case "Bow":
                        mc.thePlayer.rotationYaw = lastYaw;
                        mc.thePlayer.rotationPitch = -90;

                        mc.gameSettings.keyBindUseItem.pressed = false;
                        break;
                    case "Bow2":
                        mc.thePlayer.motionX = lastMotionX * 0.91;
                        mc.thePlayer.motionY = lastMotionY;
                        mc.thePlayer.motionZ = lastMotionZ * 0.91;

                        mc.thePlayer.rotationPitch = -90;

                        mc.gameSettings.keyBindUseItem.pressed = false;
                        break;
                }
                break;
            case "Blocksmc":
                MovementUtil.strafe(0);
                break;
        }

        if(lastBarrier != null) {
            mc.theWorld.setBlockToAir(lastBarrier);
        }

        mc.timer.timerSpeed = 1F;
    }

    @Listener
    public void onUpdate(UpdateEvent event) {
        switch (mode.getMode()) {
            case "Velocity":
                switch (velocityMode.getMode()) {
                    case "Bow":
                        if(takingVelocity) {
                            Vestige.instance.getPacketBlinkHandler().stopAll();

                            mc.thePlayer.motionY = velocityY;

                            boolean sameXDir = lastMotionX > 0.01 && velocityX > 0 || lastMotionX < -0.01 && velocityX < 0;
                            boolean sameZDir = lastMotionZ > 0.01 && velocityZ > 0 || lastMotionZ < -0.01 && velocityZ < 0;

                            if(sameXDir && sameZDir) {
                                mc.thePlayer.motionX = velocityX;
                                mc.thePlayer.motionZ = velocityZ;
                            }
                        }
                        break;
                }
                break;
            case "Collision":
                switch (collisionMode.getMode()) {
                    case "Airwalk":
                        mc.thePlayer.onGround = true;
                        break;
                    case "Airjump":
                        if(mc.thePlayer.onGround && !mc.gameSettings.keyBindJump.isKeyDown()) {
                            mc.thePlayer.jump();
                        }

                        if(mc.thePlayer.fallDistance > (mc.gameSettings.keyBindJump.isKeyDown() ? 0 : 0.7)) {
                            if(lastBarrier != null) {
                                mc.theWorld.setBlockToAir(lastBarrier);
                            }

                            lastBarrier = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);

                            mc.theWorld.setBlockState(lastBarrier, Blocks.barrier.getDefaultState());
                        }
                        break;
                }
                break;
            case "Test":
                if(mc.thePlayer.onGround) {
                    if(!mc.gameSettings.keyBindJump.isKeyDown()) {
                        mc.thePlayer.jump();
                    }
                } else {
                    if(ticks >= 2 && ticks <= 8) {
                        mc.thePlayer.motionY += 0.07;
                    }

                    ticks++;
                }
                break;
        }
    }

    @Listener
    public void onMove(MoveEvent event) {
        switch (mode.getMode()) {
            case "Vanilla":
                switch (vanillaMode.getMode()) {
                    case "Motion":
                        MovementUtil.strafe(event, vanillaSpeed.getValue());

                        if(mc.gameSettings.keyBindJump.isKeyDown()) {
                            event.setY(vanillaVerticalSpeed.getValue());
                        } else if(mc.gameSettings.keyBindSneak.isKeyDown()) {
                            event.setY(-vanillaVerticalSpeed.getValue());
                        } else {
                            event.setY(0);
                        }

                        mc.thePlayer.motionY = 0;
                        break;
                    case "Creative":
                        mc.thePlayer.capabilities.isFlying = true;
                        break;
                }
                break;
            case "Collision":
                if(collisionMode.is("Airwalk")) {
                    event.setY(mc.thePlayer.motionY = 0);
                }
                break;
            case "NCP":
                switch (ncpMode.getMode()) {
                    case "Old":
                        if(mc.thePlayer.onGround) {
                            MovementUtil.jump(event);
                            MovementUtil.strafe(event, 0.58);
                        } else {
                            event.setY(mc.thePlayer.motionY = 1E-10);

                            if(!MovementUtil.isMoving() || mc.thePlayer.isCollidedHorizontally || speed < 0.28) {
                                speed = 0.28;
                            }

                            MovementUtil.strafe(event, speed);

                            speed -= speed / 159;
                        }
                        break;
                }
                break;
            case "Velocity":
                switch (velocityMode.getMode()) {
                    case "Wait for hit":
                        if(takingVelocity) {
                            event.setY(mc.thePlayer.motionY = velocityY);

                            event.setX(mc.thePlayer.motionX = lastMotionX);
                            event.setZ(mc.thePlayer.motionZ = lastMotionZ);

                            notMoving = false;

                            ticks = 0;
                        } else {
                            if(event.getY() < -0.3 && !notMoving) {
                                lastMotionX = event.getX();
                                lastMotionY = event.getY();
                                lastMotionZ = event.getZ();

                                notMoving = true;
                            }

                            if(notMoving) {
                                event.setY(mc.thePlayer.motionY = 0);
                                MovementUtil.strafe(event, 0);
                            }

                            ticks++;
                        }
                        break;
                    case "Bow":
                        for(int i = 8; i >= 0; i--) {
                            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);

                            if(stack != null && stack.getItem() instanceof ItemBow) {
                                mc.thePlayer.inventory.currentItem = i;
                                break;
                            }
                        }

                        if(takingVelocity) {
                            mc.timer.timerSpeed = 1F;

                            notMoving = false;

                            ticks = 0;
                            counter = 0;

                            started = true;
                        } else {
                            if(ticks <= 3) {
                                if(started) {
                                    mc.timer.timerSpeed = 1.5F;
                                }
                                mc.gameSettings.keyBindUseItem.pressed = true;
                            } else {
                                mc.gameSettings.keyBindUseItem.pressed = false;
                            }

                            ticks++;
                        }

                        if(ticks >= 6) {
                            mc.timer.timerSpeed = 0.03F;
                        } else if(ticks == 5) {
                            mc.timer.timerSpeed = 0.1F;
                        }

                        if(started && !notMoving && !takingVelocity && MovementUtil.getHorizontalMotion() > 0.07) {
                            //event.setY(mc.thePlayer.motionY = event.getY() + 0.01);
                        }
                        break;
                    case "Bow2":
                        for(int i = 8; i >= 0; i--) {
                            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);

                            if(stack != null && stack.getItem() instanceof ItemBow) {
                                mc.thePlayer.inventory.currentItem = i;
                                break;
                            }
                        }

                        if(takingVelocity) {
                            event.setY(mc.thePlayer.motionY = velocityY);

                            boolean sameXDir = lastMotionX > 0 && velocityX > 0 || lastMotionX < 0 && velocityX < 0;
                            boolean sameZDir = lastMotionZ > 0 && velocityZ > 0 || lastMotionZ < 0 && velocityZ < 0;

                            if(sameXDir && sameZDir) {
                                event.setX(mc.thePlayer.motionX = velocityX);
                                event.setZ(mc.thePlayer.motionZ = velocityZ);
                            } else {
                                event.setX(mc.thePlayer.motionX = lastMotionX);
                                event.setZ(mc.thePlayer.motionZ = lastMotionZ);
                            }

                            notMoving = false;

                            ticks = 0;
                        } else {
                            if(ticks >= 6 && !notMoving) {
                                lastMotionX = event.getX();
                                lastMotionY = event.getY();
                                lastMotionZ = event.getZ();

                                notMoving = true;
                            }

                            if(ticks >= 1 && ticks <= 6) {
                                mc.gameSettings.keyBindUseItem.pressed = true;
                            } else {
                                mc.gameSettings.keyBindUseItem.pressed = false;
                            }

                            if(notMoving) {
                                event.setY(mc.thePlayer.motionY = 0);
                                MovementUtil.strafe(event, 0);
                            }

                            ticks++;
                        }
                        break;
                }
                break;
            case "Blocksmc":
                if(automated.isEnabled()) {
                    if(++counter < 6) {
                        float yaw = MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw);


                        double x = 0;
                        double z = 0;

                        EnumFacing facing = EnumFacing.UP;

                        if(yaw > 135 || yaw < -135) {
                            z = 1;
                            facing = EnumFacing.NORTH;
                        } else if(yaw > -135 && yaw < -45) {
                            x = -1;
                            facing = EnumFacing.EAST;
                        } else if(yaw > -45 && yaw < 45) {
                            z = -1;
                            facing = EnumFacing.SOUTH;
                        } else if(yaw > 45 && yaw < 135) {
                            x = 1;
                            facing = EnumFacing.WEST;
                        }

                        BlockPos pos;

                        switch (counter) {
                            case 1:
                                pos = new BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY - 1, mc.thePlayer.posZ + z);

                                mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), pos, EnumFacing.UP, WorldUtil.getVec3(pos, EnumFacing.DOWN, true));
                                break;
                            case 2:
                                pos = new BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);

                                mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), pos, EnumFacing.UP, WorldUtil.getVec3(pos, EnumFacing.DOWN, true));
                                break;
                            case 3:
                                pos = new BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY + 1, mc.thePlayer.posZ + z);

                                mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), pos, EnumFacing.UP, WorldUtil.getVec3(pos, EnumFacing.DOWN, true));
                                break;
                            case 5:
                                pos = new BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY + 2, mc.thePlayer.posZ + z);

                                mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), pos, facing, WorldUtil.getVec3(pos, facing, true));
                                break;
                        }

                        PacketUtil.sendPacket(new C0APacketAnimation());

                        MovementUtil.strafe(event, 0.04);
                        return;
                    }
                }

                BlockPos pos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + 2, mc.thePlayer.posZ);

                if(mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir) {
                    started = true;
                }

                Vestige.instance.getPacketBlinkHandler().startBlinkingAll();

                if(started) {
                    mc.timer.timerSpeed = 0.3F;

                    if(mc.thePlayer.onGround) {
                        if(ticks > 0) {
                            this.setEnabled(false);
                            return;
                        }

                        if(MovementUtil.isMoving()) {
                            MovementUtil.jump(event);
                            MovementUtil.strafe(event, 0.58);
                        }
                    } else if(ticks == 1) {
                        MovementUtil.strafe(event, 9.5);
                    }

                    ticks++;
                } else {
                    MovementUtil.strafe(event, 0.1);
                }
                break;
        }

        takingVelocity = false;
        ticksSinceVelocity++;
    }

    @Listener
    public void onEntityAction(EntityActionEvent event) {
        switch (mode.getMode()) {
            case "Velocity":
                if(velocityMode.is("Wait for hit")) {
                    event.setSprinting(true);
                } else if(velocityMode.is("Airjump")) {
                    if(!started) {
                        event.setSprinting(false);
                    }
                }
                break;
            case "Blocksmc":
                if(automated.isEnabled() && counter < 6) {
                    event.setSprinting(false);
                }
                break;
        }
    }

    @Listener
    public void onMotion(MotionEvent event) {
        switch (mode.getMode()) {
            case "Velocity":
                if(velocityMode.is("Bow") || velocityMode.is("Bow2")) {
                    event.setPitch(-90);
                }
                break;
            case "Collision":
                if(collisionMode.is("Airwalk")) {
                    event.setOnGround(true);
                }
                break;
        }
    }

    @Listener
    public void onPostMotion(PostMotionEvent event) {

    }

    @Listener
    public void onReceive(PacketReceiveEvent event) {
        if(event.getPacket() instanceof S12PacketEntityVelocity) {
            S12PacketEntityVelocity packet = event.getPacket();

            if(mc.thePlayer.getEntityId() == packet.getEntityID()) {
                takingVelocity = true;

                velocityX = packet.getMotionX() / 8000.0;
                velocityY = packet.getMotionY() / 8000.0;
                velocityZ = packet.getMotionZ() / 8000.0;

                velocityDist = Math.hypot(velocityX, velocityZ);

                ticksSinceVelocity = 0;

                if(mode.is("Velocity")) {
                    event.setCancelled(true);
                }
            }
        } else if(event.getPacket() instanceof S08PacketPlayerPosLook) {
            if(mode.is("Velocity")) {
                this.setEnabled(false);
                return;
            }
        }
    }

    @Listener
    public void onSend(PacketSendEvent event) {
        switch (mode.getMode()) {
            case "Velocity":
                if(velocityMode.is("Wait for hit") || velocityMode.is("Bow2")) {
                    if(event.getPacket() instanceof C03PacketPlayer && notMoving) {
                        event.setCancelled(true);
                    }
                }
                break;
        }
    }

    @Override
    public String getSuffix() {
        return mode.getMode();
    }

}
