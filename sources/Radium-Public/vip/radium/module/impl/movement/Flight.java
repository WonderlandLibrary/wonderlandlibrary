package vip.radium.module.impl.movement;

import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import vip.radium.RadiumClient;
import vip.radium.event.EventBusPriorities;
import vip.radium.event.impl.player.MoveEntityEvent;
import vip.radium.event.impl.player.StepEvent;
import vip.radium.event.impl.player.UpdatePositionEvent;
import vip.radium.module.Module;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.ModuleManager;
import vip.radium.module.impl.combat.KillAura;
import vip.radium.notification.Notification;
import vip.radium.notification.NotificationType;
import vip.radium.property.Property;
import vip.radium.property.impl.DoubleProperty;
import vip.radium.property.impl.EnumProperty;
import vip.radium.utils.MovementUtils;
import vip.radium.utils.ServerUtils;
import vip.radium.utils.TimerUtil;
import vip.radium.utils.Wrapper;

@ModuleInfo(label = "Flight", category = ModuleCategory.MOVEMENT)
public final class Flight extends Module {

    private static final int MAX_ENDER_PEARL_SCAN_DIST = 20;

    private static final long PEARL_DISABLE_DURATION = 2000L;

    private static final C08PacketPlayerBlockPlacement BLOCK_PLACEMENT = new C08PacketPlayerBlockPlacement(
            new BlockPos(-1, -1, -1), 255, null, 0.0f, 0.0f, 0.0f);

    private final EnumProperty<FlightMode> flightModeProperty = new EnumProperty<>("Mode", FlightMode.MOTION);
    private final Property<Boolean> viewBobbingProperty = new Property<>("View Bobbing", true);
    private final Property<Boolean> pearlFlyProperty = new Property<>("Pearl Exploit", true);
    private final Property<Boolean> toggleAuraProperty = new Property<>("Toggle Aura", true);
    private final Property<Boolean> timerProperty = new Property<>("Timer", true);
    private final DoubleProperty speedProperty = new DoubleProperty("Speed", 2.5, 0.1, 5.0, 0.05);

    private final TimerUtil timer = new TimerUtil();
    private final TimerUtil disabledTimer = new TimerUtil();

    private long estimatedTimeUntilLanded;
    private double distWhenThrown;
    private boolean killauraWasEnabled;
    private KillAura aura;
    private boolean pearlAirBourne;
    private boolean isThrowing;
    private boolean hasLanded;
    private int pearlSlot;

    @EventLink
    public final Listener<StepEvent> onStepEvent = event -> event.setStepHeight(0.0F);

    @EventLink(EventBusPriorities.HIGHEST)
    public final Listener<MoveEntityEvent> MoveEntityEvent = e -> {
        switch (flightModeProperty.getValue()) {
            case MOTION:
                if (pearlFlyProperty.getValue() && !hasLanded) {
                    e.setCancelled();
                    return;
                }

                if (MovementUtils.isMoving())
                    MovementUtils.setSpeed(e, speedProperty.getValue());
                break;
        }
    };

    @EventLink(EventBusPriorities.HIGHEST)
    public final Listener<UpdatePositionEvent> onUpdatePositionEvent = e -> {
        if (e.isPre()) {
            if (pearlFlyProperty.getValue()) {
                if (disabledTimer.hasElapsed(PEARL_DISABLE_DURATION)) {
                    toggle();
                    return;
                }

                if (!pearlAirBourne && !hasLanded) {
                    final int pearlStackSlot = findPearlsInHotBar();

                    if (pearlStackSlot == -1) {
                        RadiumClient.getInstance().getNotificationManager().add(new Notification(
                                "Pearl Fly",
                                "You must have pearls on your hotbar",
                                NotificationType.ERROR));
                        toggle();
                        return;
                    }

                    if (!isThrowing) {
                        final double dist = MovementUtils.estimateDistFromGround(MAX_ENDER_PEARL_SCAN_DIST);

                        if (dist < MAX_ENDER_PEARL_SCAN_DIST) {
                            final boolean needSwitch = Wrapper.getPlayer().inventory.currentItem != pearlStackSlot;

                            if (needSwitch)
                                Wrapper.sendPacketDirect(new C09PacketHeldItemChange(pearlStackSlot));

                            distWhenThrown = dist;
                            pearlSlot = pearlStackSlot;
                            isThrowing = true;
                            e.setPitch(90.0F);
                        }
                    } else {
                        if (pearlStackSlot != pearlSlot) {
                            toggle();
                            return;
                        }

                        estimatedTimeUntilLanded = (long) (((distWhenThrown / 20) / 1.5) * 1000);
                        Wrapper.sendPacketDirect(BLOCK_PLACEMENT);
                        final int physicalHeldItem = Wrapper.getPlayer().inventory.currentItem;
                        if (pearlSlot != physicalHeldItem)
                            Wrapper.sendPacketDirect(new C09PacketHeldItemChange(physicalHeldItem));
                        isThrowing = false;
                        pearlAirBourne = true;
                        disabledTimer.reset();
                    }
                }

                if (pearlAirBourne && disabledTimer.hasElapsed(estimatedTimeUntilLanded + ServerUtils.getPingToCurrentServer() * 2)) {
                    RadiumClient.getInstance().getNotificationManager().add(new Notification(
                            "Pearl Fly",
                            String.format("You can now fly for %ss", PEARL_DISABLE_DURATION / 1000),
                            PEARL_DISABLE_DURATION,
                            NotificationType.SUCCESS));
                    hasLanded = true;
                    disabledTimer.reset();
                    pearlAirBourne = false;
                }
            }

            final EntityPlayerSP player = Wrapper.getPlayer();

            if (viewBobbingProperty.getValue())
                player.cameraYaw = 0.105F;

            switch (flightModeProperty.getValue()) {
                case MOTION:
                    if (Wrapper.getGameSettings().keyBindJump.isKeyDown()) {
                        player.motionY = 1.0F;
                    } else if (Wrapper.getGameSettings().keyBindSneak.isKeyDown()) {
                        player.motionY = -1.0F;
                    } else {
                        Wrapper.getPlayer().motionY = 0.0D;
                    }
                    break;
            }
        }
    };

    public Flight() {
        setSuffixListener(flightModeProperty);
    }

    @Override
    public void onEnable() {
        Step.cancelStep = true;
        isThrowing = false;
        hasLanded = false;
        pearlAirBourne = false;
        pearlSlot = -1;

        disabledTimer.reset();
        timer.reset();

        if (aura == null)
            aura = ModuleManager.getInstance(KillAura.class);

        if (toggleAuraProperty.getValue() && (killauraWasEnabled = aura.isEnabled()))
            aura.toggle();
    }

    private int findPearlsInHotBar() {
        for (int i = 36; i < 45; i++) {
            ItemStack stack = Wrapper.getStackInSlot(i);

            if (stack != null && stack.getItem() instanceof ItemEnderPearl)
                return i - 36;
        }

        return -1;
    }

    @Override
    public void onDisable() {
        Step.cancelStep = false;
        Wrapper.getTimer().timerSpeed = 1.0F;

        if (toggleAuraProperty.getValue() && (killauraWasEnabled && !aura.isEnabled()))
            aura.toggle();
    }

    private enum FlightMode {
        MOTION
    }
}
