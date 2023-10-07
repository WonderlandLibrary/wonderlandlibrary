package koks.module.movement;

import god.buddy.aot.BCompiler;
import koks.api.event.Event;
import koks.api.manager.notification.NotificationManager;
import koks.api.manager.notification.NotificationType;
import koks.api.manager.value.annotation.Value;
import koks.api.registry.module.Module;
import koks.api.registry.module.ModuleRegistry;
import koks.api.utils.InventoryUtil;
import koks.api.utils.MovementUtil;
import koks.api.utils.TimeHelper;
import koks.event.*;
import koks.module.combat.Velocity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@Module.Info(name = "LongJump", description = "You can jump long", category = Module.Category.MOVEMENT)
public class LongJump extends Module {

    @Value(name = "Mode", modes = {"AAC3.3.12", "Redesky2021", "Redesky20220105", "CubeCraft", "Hypixel20211219", "Intave14GommeSW", "AGC2021", "QPlay20211023", "Spartan439"})
    String mode = "AAC3.3.12";

    @Value(name = "Spartan439-Boost", displayName = "Boost", minimum = 0.2, maximum = 7)
    double spartan439Boost = 5;

    @Value(name = "AGC2021-DamageMethod", displayName = "Damage Method", modes = {"Bow", "Packet"})
    String agc2021DamageMethod = "Bow";

    @Value(name = "AGC2021-Boost", displayName = "Boost", minimum = 1, maximum = 6)
    double agc2021Boost = 3.4;

    @Value(name = "AGC2021-HurtTime", displayName = "HurtTime", minimum = 5, maximum = 10)
    int agc2021HurtTime = 6;

    @Value(name = "AGC2021-FastStop", displayName = "FastStop")
    boolean agc2021FastStop = true;

    @Value(name = "AGC2021-ToggleDelay", displayName = "ToggleDelay")
    boolean agc2021ToggleDelay = true;

    @Value(name = "Intave14GommeSW-Boost", displayName = "Boost", minimum = 2, maximum = 4)
    int intave14GommeSWBoost = 4;

    @Value(name = "AAC3.3.12-Speed", displayName = "Speed", minimum = 4.5, maximum = 10)
    double aac3312Speed = 4.5;

    @Value(name = "Redesky2021-Motion", displayName = "Motion", minimum = 0, maximum = 1)
    double redesky2021Motion = 0.65;

    @Value(name = "Redesky2021-Boost", displayName = "Boost", minimum = 0.1, maximum = 0.6)
    double redesky2021Boost = 0.1;

    @Value(name = "Redesky2021-Speed", displayName = "Speed", minimum = 0.1, maximum = 2)
    double redesky2021Speed = 1.5;

    @Value(name = "Disable Velocity")
    boolean disableVelocity = false;

    boolean jump, hypixel20211219Damaged, hadVelocity, agc2021Shooted, agc2021Damaged, agc2021Switched, agc2021Respawned, qPlay20211023Damaged, spartan439WasBoosted, redesky20220105WasAir;
    int hypixel20211219State, redesky20220105Disable;
    final TimeHelper agc2021ShootHelper = new TimeHelper(), agc2021Helper = new TimeHelper(), agc2021Delay = new TimeHelper();

    @Override
    public boolean isVisible(koks.api.manager.value.Value<?> value, String name) {
        switch (name.split("-")[0]) {
            case "AGC2021":
                return mode.equalsIgnoreCase("AGC2021");
            case "Intave14GommeSW":
                return mode.equalsIgnoreCase("Intave14GommeSW");
            case "Redesky2021":
                return mode.equalsIgnoreCase("Redesky2021");
            case "Redesky20220105":
                return mode.equalsIgnoreCase("Redesky20220105");
            case "AAC3.3.12":
                return mode.equalsIgnoreCase("AAC3.3.12");
            case "Spartan439":
                return mode.equalsIgnoreCase("Spartan439");
        }
        return super.isVisible(value, name);
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    @Override
    @Event.Info
    public void onEvent(Event event) {
        final MovementUtil movementUtil = MovementUtil.getInstance();
        switch (mode) {
            case "QPlay20211023":
                if (event instanceof UpdateEvent) {
                    if (getHurtTime() > 0)
                        qPlay20211023Damaged = true;
                    if (!qPlay20211023Damaged)
                        return;

                    if (isMoving() && getPlayer().onGround && !getPlayer().isSneaking() && !getGameSettings().keyBindSneak.isKeyDown() && !getGameSettings().keyBindJump.isKeyDown()) {
                        getPlayer().jump();
                    }
                    if (getPlayer().fallDistance > 0) {
                        getPlayer().motionY += 0.05;
                        movementUtil.setSpeed(1);
                    } else {
                        movementUtil.setSpeed(0.95);
                    }
                }
                break;
            case "AGC2021":
                if (event instanceof final PacketEvent packetEvent) {
                    final Packet<?> packet = packetEvent.getPacket();
                    if (packet instanceof S07PacketRespawn) {
                        agc2021Respawned = true;
                        setToggled(false);
                    }
                }

                if (event instanceof ItemSyncEvent) {
                    if (!agc2021Shooted)
                        event.setCanceled(true);
                }

                if (event instanceof final RotationEvent rotationEvent) {
                    if (!agc2021Damaged && agc2021DamageMethod.equalsIgnoreCase("Bow"))
                        rotationEvent.setPitch(-90);
                }

                if (event instanceof UpdateEvent) {
                    if (getHurtTime() != 0 && agc2021Shooted) {
                        agc2021Damaged = true;
                        agc2021Delay.reset();
                        if (getHurtTime() >= agc2021HurtTime)
                            getPlayer().jump();
                    }

                    if (!agc2021Damaged) {
                        stopWalk();
                        switch (agc2021DamageMethod) {
                            case "Bow":
                                final int damageItem = InventoryUtil.getInstance().searchItemSlot(Items.bow, 0, 9);
                                if (damageItem != -1 && getPlayer().inventory.getStackInSlot(damageItem) != null) {
                                    final Item item = getPlayer().inventory.getStackInSlot(damageItem).getItem();

                                    if (!agc2021Switched) {
                                        getPlayerController().currentPlayerItem = damageItem;
                                        agc2021Switched = true;
                                    }

                                    if (getPlayer().inventory.fakeItem != damageItem)
                                        sendPacketUnlogged(new C09PacketHeldItemChange(damageItem));

                                    if (!agc2021Shooted && agc2021ShootHelper.hasReached(150) && (!(item instanceof ItemBow) || agc2021Switched)) {
                                        if (agc2021Helper.hasReached(200)) {
                                            sendPacketUnlogged(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                                            getPlayer().stopUsingItem();
                                            agc2021Helper.reset();
                                            agc2021Shooted = true;
                                        } else {
                                            sendPacketUnlogged(new C08PacketPlayerBlockPlacement(getPlayer().getFakeItem()));
                                        }
                                    } else {
                                        agc2021Helper.reset();
                                    }
                                }
                                break;
                            case "Packet":
                                if (getPlayer().ticksExisted % 50 == 0) {
                                    sendPacketUnlogged(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY() - 2.1, getZ(), false));
                                    agc2021Shooted = true;
                                }
                                break;
                        }
                    } else {
                        resumeWalk();

                        if (getBlockUnderPlayer(1) != Blocks.air && getPlayer().fallDistance > 0)
                            setToggled(false);
                        if (isMoving())
                            movementUtil.setSpeed(agc2021Boost, getYaw(), false);
                    }
                }
                break;
            case "Intave14GommeSW":
                if (event instanceof final PacketEvent packetEvent) {
                    final Packet<?> packet = packetEvent.getPacket();
                    if (packet instanceof final C03PacketPlayer packetPlayer) {
                        packetPlayer.onGround = false;
                    }
                }
                if (event instanceof UpdateEvent) {
                    for (int i = 0; i < intave14GommeSWBoost; i++) {
                        getPlayer().jump();
                    }
                    setToggled(false);
                }
                break;
            case "Hypixel20211219":
                if (event instanceof VelocityEvent) {
                    event.setCanceled(true);
                }
                if (event instanceof UpdateEvent) {
                    if (getHurtTime() == 10 && getPlayer().hurtResistantTime != 0) {
                        hypixel20211219Damaged = true;
                    }

                    if (getHurtTime() != 0 && hypixel20211219Damaged) {
                        resumeWalk();
                        getPlayer().setSprinting(true);
                        switch (hypixel20211219State) {
                            case 0:
                                getTimer().timerSpeed = 0.1F;
                                if (getPlayer().onGround)
                                    getPlayer().jump();
                                break;
                            default:
                                getTimer().timerSpeed = 1F;
                                if (!getPlayer().onGround) {
                                    getPlayer().motionY += randomInRange(0.035, 0.039);
                                    movementUtil.addMotion(randomInRange(0.045, 0.049));
                                }
                                break;
                        }
                        hypixel20211219State++;
                        if (hypixel20211219State > 1) {
                            if (getPlayer().onGround) {
                                setToggled(false);
                            }
                        }
                    } else {
                        stopWalk();
                        if (getPlayer().onGround && hypixel20211219State > 1)
                            setToggled(false);
                    }
                }
                break;
            case "Spartan439":
                if (event instanceof UpdateEvent) {
                    if (getHurtTime() == 10)
                        movementUtil.setSpeed(spartan439Boost);
                    else if (getHurtTime() > 0) {
                        spartan439WasBoosted = true;
                        getPlayer().motionY += 0.015;
                    }

                    if (getPlayer().onGround) {
                        if (spartan439WasBoosted) {
                            setToggled(false);
                        }
                    }
                }
                break;
            case "Redesky2021":
                if (event instanceof UpdateEvent) {
                    if (getPlayer().onGround)
                        getPlayer().jump();
                    getPlayer().motionY += redesky2021Motion / 10;
                    getPlayer().jumpMovementFactor = (float) redesky2021Boost;
                    getTimer().timerSpeed = (float) redesky2021Speed;
                }
                break;
            case "Redesky20220105":
                if (event instanceof UpdateEvent) {
                    if (getPlayer().onGround) {
                        redesky20220105Disable++;
                        if (redesky20220105Disable <= 2) {
                            setToggled(false);
                            redesky20220105Disable = 0;
                        }
                    }
                    getTimer().timerSpeed = 1.0f;
                    if (this.mc.thePlayer.onGround) {
                        this.redesky20220105WasAir = false;
                        if (isMoving()) {
                            if (!getGameSettings().keyBindJump.isKeyDown())
                                getPlayer().jump();
                            this.mc.thePlayer.motionY += 0.3;
                            this.mc.thePlayer.motionX *= 1.2;
                            this.mc.thePlayer.motionZ *= 1.2;
                        }
                        break;
                    }
                    if (this.redesky20220105WasAir) {
                        getPlayer().speedInAir = 0.02f;
                        break;
                    }
                    getPlayer().speedInAir = 0.06f;
                    this.redesky20220105WasAir = true;
                }
                break;
            case "AAC3.3.12":
                if (event instanceof UpdateEvent) {
                    if (getPlayer().ticksExisted % 3 == 0 && !getPlayer().onGround) {
                        movementUtil.teleportTo(aac3312Speed, getY(), getPlayer().rotationYaw);
                    }
                    setToggled(false);
                }
                break;
            case "CubeCraft2":
                if (event instanceof UpdateEvent) {
                    getGameSettings().keyBindSprint.pressed = true;
                    if (getHurtTime() != 0) {
                        jump = true;
                    }
                    if (jump) {
                        getTimer().timerSpeed = 2;
                        getPlayer().motionY = -0.003;
                        movementUtil.setSpeed(0.4);
                    }
                }
                break;
            case "CubeCraft":
                if (event instanceof UpdateEvent) {
                    getGameSettings().keyBindSprint.pressed = true;
                }
                if (event instanceof RunGameLoopEvent) {
                    if (mc.thePlayer.onGround) {
                        if (jump) {
                            setToggled(false);
                            return;
                        }
                        jump = true;
                        getPlayer().jump();
                        getTimer().timerSpeed = 0.2F;
                    } else {
                        if (getHurtTime() > 0) {
                            getTimer().timerSpeed = 0.6F;
                        }
                    }
                    if (getHurtTime() > 0) {
                        getPlayer().motionY += 0.005F;
                        getGameSettings().keyBindForward.pressed = true;
                        movementUtil.setSpeed(0.5, getPlayer().rotationYaw);
                    }
                }
                break;
        }
    }

    @Override
    public void onEnable() {
        if (ModuleRegistry.getModule(Velocity.class).isToggled() && disableVelocity) {
            hadVelocity = true;
            ModuleRegistry.getModule(Velocity.class).setToggled(false);
        }

        qPlay20211023Damaged = false;
        agc2021Switched = false;
        agc2021Damaged = false;
        agc2021Shooted = false;
        agc2021ShootHelper.reset();
        agc2021Helper.reset();

        hypixel20211219State = 0;
        hypixel20211219Damaged = false;
        spartan439WasBoosted = false;

        switch (mode) {
            case "QPlay20211023":
                for (int i = 0; i < 4; i++) {
                    sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY() + 0.825, getZ(), false));
                    sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY(), getZ(), false));
                }
                sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY(), getZ(), true));
                break;
            case "AGC2021":
                if (!agc2021Delay.hasReached(10000) && agc2021ToggleDelay) {
                    NotificationManager.getInstance().addNotification("Please wait...", "You must wait few seconds before toggle again", NotificationType.WARNING, 10000 - agc2021Delay.getMs());
                    this.setToggled(false);
                    return;
                }
                break;
            case "Spartan439":
                getPlayer().jump();
                sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY() + 0.42, getZ(), false));
                sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY() + 1, getZ(), false));
                sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY() + 3, getZ(), false));
                sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY() + 4, getZ(), false));
                sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY(), getZ(), false));
                sendPacket(new C03PacketPlayer(true));
                getPlayer().jump();
                break;
            case "Intave14GommeSW":
                if (!getPlayer().onGround) {
                    sendMessage("Please enable it onGround!");
                    this.setToggled(false);
                    return;
                }
                break;
            case "Hypixel20211219":
                if (!getPlayer().onGround) {
                    stopWalk();
                    setToggled(false);
                    return;
                }
                for (int i = 0; i <= 49; ++i) {
                    sendPacketUnlogged(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 0.06249, getPlayer().posZ, false));
                    sendPacketUnlogged(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY, getPlayer().posZ, false));
                }
                sendPacketUnlogged(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY, getPlayer().posZ, true));
                break;
            case "CubeCraft":
            case "CubeCraft2":
                if (getPlayer().getFoodStats().getFoodLevel() <= 6) {
                    sendMessage("You have Hunger, please eat some food!");
                    this.setToggled(false);
                    return;
                }
                if (!getPlayer().onGround) {
                    sendMessage("Please enable it onGround!");
                    this.setToggled(false);
                    return;
                }
                for (double distance = 0.06; distance < 3.075; distance += 0.06) {
                    sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY() + 0.06, getZ(), false));
                    distance += 0.1E-8D;
                    sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY() + 0.1E-8D, getZ(), false));
                }
                sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY(), getZ(), false));
                sendPacket(new C03PacketPlayer(true));
                getPlayer().jump();
                break;
        }
    }

    @Override
    public void onDisable() {
        redesky20220105WasAir = false;
        redesky20220105Disable = 0;
        if (hadVelocity && disableVelocity) {
            hadVelocity = false;
            ModuleRegistry.getModule(Velocity.class).setToggled(true);
        }
        getPlayer().speedInAir = 0.02F;
        getTimer().timerSpeed = 1F;
        resumeWalk();

        switch (mode) {
            case "QPlay20211023" -> getPlayer().motionY = 0;
            case "AGC2021" -> {
                if (getPlayer().inventory.fakeItem != getPlayer().inventory.currentItem)
                    sendPacketUnlogged(new C09PacketHeldItemChange(getPlayer().inventory.currentItem));
                if (agc2021FastStop && !agc2021Respawned)
                    setMotion(0);
                agc2021Respawned = false;
            }
        }
        jump = false;
    }
}
