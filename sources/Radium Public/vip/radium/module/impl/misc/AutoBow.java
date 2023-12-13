package vip.radium.module.impl.misc;

import io.github.nevalackin.homoBus.annotations.EventLink;
import io.github.nevalackin.homoBus.Listener;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import vip.radium.RadiumClient;
import vip.radium.event.impl.player.UpdatePositionEvent;
import vip.radium.module.Module;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.notification.Notification;
import vip.radium.notification.NotificationType;
import vip.radium.utils.MathUtils;
import vip.radium.utils.MovementUtils;
import vip.radium.utils.Wrapper;

@ModuleInfo(label = "Auto Bow", category = ModuleCategory.MISCELLANEOUS)
public final class AutoBow extends Module {

    private static final C07PacketPlayerDigging PLAYER_DIGGING = new C07PacketPlayerDigging(
            C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN);
    private static final C08PacketPlayerBlockPlacement BLOCK_PLACEMENT = new C08PacketPlayerBlockPlacement(
            new BlockPos(-1, -1, -1), 255, null, 0.0f, 0.0f, 0.0f);
    private boolean isCharging;
    private int chargedTicks;
    private int bowSlot;
    @EventLink
    private final Listener<UpdatePositionEvent> onUpdatePosition = event -> {
        if (event.isPre()) {
            final int bowSlot = findBowInHotBar();

            if (bowSlot == -1) {
                RadiumClient.getInstance().getNotificationManager().add(new Notification(
                        "Auto Bow",
                        "You must have a bow on your hotbar",
                        NotificationType.ERROR));
                toggle();
                return;
            }

            if (!isCharging) {
                if (MovementUtils.isOnGround() || MathUtils.roundToDecimalPlace(MovementUtils.getBlockHeight(), 0.001D) == 0.166D) {
                    final boolean needSwitch = Wrapper.getPlayer().inventory.currentItem != bowSlot;

                    if (needSwitch)
                        Wrapper.sendPacketDirect(new C09PacketHeldItemChange(bowSlot));

                    this.bowSlot = bowSlot;
                    Wrapper.sendPacketDirect(BLOCK_PLACEMENT);
                    isCharging = true;
                    chargedTicks = 0;
                }
            } else {
                ++chargedTicks;

                if (bowSlot != this.bowSlot) {
                    toggle();
                    return;
                }

                switch (chargedTicks) {
                    case 2:
//                        event.setPitch(MovementUtils.isMoving() ? -45.0F : -90.0F);
//                        event.setYaw(MovementUtils.getMovementDirection());
                        event.setPitch(-90.0F);
                        break;
                    case 3:
                        final int physicalHeldItem = Wrapper.getPlayer().inventory.currentItem;
                        Wrapper.sendPacketDirect(PLAYER_DIGGING);
                        if (this.bowSlot != physicalHeldItem)
                            Wrapper.sendPacketDirect(new C09PacketHeldItemChange(physicalHeldItem));
                        isCharging = false;
                        toggle();
                }
            }
        }
    };

    @Override
    public void onEnable() {
        isCharging = false;
        chargedTicks = 0;
    }

    private int findBowInHotBar() {
        for (int i = 36; i < 45; i++) {
            ItemStack stack = Wrapper.getStackInSlot(i);

            if (stack != null && stack.getItem() instanceof ItemBow)
                return i - 36;
        }

        return -1;
    }
}
