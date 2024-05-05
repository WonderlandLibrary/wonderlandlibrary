package ru.smertnix.celestial.feature.impl.movement;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;
import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.player.EventPreMotion;
import ru.smertnix.celestial.event.events.impl.player.EventUpdate;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.feature.impl.combat.AttackAura;
import ru.smertnix.celestial.ui.clickgui.component.impl.ModuleComponent;
import ru.smertnix.celestial.ui.settings.impl.BooleanSetting;
import ru.smertnix.celestial.ui.settings.impl.ListSetting;
import ru.smertnix.celestial.ui.settings.impl.NumberSetting;
import ru.smertnix.celestial.utils.Helper;
import ru.smertnix.celestial.utils.math.TimerHelper;
import ru.smertnix.celestial.utils.movement.MovementUtils;
import ru.smertnix.celestial.utils.other.ChatUtils;

public class Spider extends Feature {
    TimerHelper spiderTimer = new TimerHelper();
    private final ListSetting spiderMode = new ListSetting("Spider Mode", "Matrix", () -> true, "Matrix", "Sunrise");
    public NumberSetting climbSpeed = new NumberSetting("Spider Speed", 1, 0, 5, 0.1F, () -> this.spiderMode.currentMode.equalsIgnoreCase("Matrix"));
    private final BooleanSetting bypass;

    public Spider() {
        super("Spider", "Позволяет подниматься на возвышенности", FeatureCategory.Movement);
        bypass = new BooleanSetting("MEGA BYPASS", true, () -> true);
        addSettings(spiderMode, climbSpeed);
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        setSuffix("" + spiderMode.getCurrentMode());
    }
    @EventTarget
    public void onPreMotion(EventPreMotion eventPreMotion) {
        if (spiderMode.currentMode.equals("Sunrise")) {
            if (Helper.mc.playerController.getCurrentGameType() == GameType.ADVENTURE && ModuleComponent.timerHelper.hasReached(1000)) {
                ChatUtils.addChatMessage(ChatFormatting.GREEN + "Возьми блоки в инвентарь , а то по попе дам!");
                ModuleComponent.timerHelper.reset();
            }
            if (Helper.mc.player.isCollidedHorizontally) {
                if (Helper.mc.playerController.getCurrentGameType() == GameType.ADVENTURE) {
                } else {
                    int block = -1;
                    for (int i = 0; i < 9; i++) {
                        ItemStack s = Helper.mc.player.inventory.getStackInSlot(i);
                        if (s.getItem() instanceof ItemBlock) {
                            block = i;
                            break;
                        }
                    }
                    if (block == -1 && ModuleComponent.timerHelper.hasReached(1000)) {
                        ChatUtils.addChatMessage(ChatFormatting.GREEN + "Возьми блоки в инвентарь , а то по попе дам!");
                        ModuleComponent.timerHelper.reset();
                        return;
                    }
                    if (ModuleComponent.timerHelper.hasReached(1.0f * 55)) {
                        try {
                            if (block != -1 && Helper.mc.objectMouseOver != null && Helper.mc.objectMouseOver.hitVec != null && Helper.mc.objectMouseOver.getBlockPos() != null && Helper.mc.objectMouseOver.sideHit != null) {
                                Helper.mc.player.connection.sendPacket(new CPacketHeldItemChange(block));
                                float prevPitch = Helper.mc.player.rotationPitch;
                                Helper.mc.player.rotationPitch = -60;
                                Helper.mc.entityRenderer.getMouseOver(1);
                                Vec3d facing = Helper.mc.objectMouseOver.hitVec;
                                BlockPos stack = Helper.mc.objectMouseOver.getBlockPos();
                                float f = (float) (facing.x - (double) stack.getX());
                                float f1 = (float) (facing.y - (double) stack.getY());
                                float f2 = (float) (facing.z - (double) stack.getZ());
                                Helper.mc.player.connection.sendPacket(new CPacketEntityAction(Helper.mc.player, CPacketEntityAction.Action.START_SNEAKING));
                                if (Helper.mc.world.getBlockState(new BlockPos(Helper.mc.player).add(0, 2, 0)).getBlock() == Blocks.AIR) {
                                    Helper.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(stack, Helper.mc.objectMouseOver.sideHit, EnumHand.MAIN_HAND, f, f1, f2));
                                } else {
                                    Helper.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, stack, Helper.mc.objectMouseOver.sideHit));
                                    Helper.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, stack, Helper.mc.objectMouseOver.sideHit));
                                }
                                Helper.mc.player.connection.sendPacket(new CPacketEntityAction(Helper.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                                Helper.mc.player.rotationPitch = prevPitch;
                                Helper.mc.entityRenderer.getMouseOver(1);
                                Helper.mc.player.connection.sendPacket(new CPacketHeldItemChange(Helper.mc.player.inventory.currentItem));
                                Helper.mc.player.onGround = true;
                                Helper.mc.player.isCollidedVertically = true;
                                Helper.mc.player.isCollidedHorizontally = true;
                                Helper.mc.player.isAirBorne = true;
                                Helper.mc.player.jump();
                                ModuleComponent.timerHelper.reset();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else if (spiderMode.currentMode.equals("Matrix")) {
            if (Helper.mc.player.isCollidedHorizontally) {
                if (ModuleComponent.timerHelper.hasReached(climbSpeed.getNumberValue() * 55)) {
                    Helper.mc.player.onGround = true;
                    Helper.mc.player.isCollidedVertically = true;
                    Helper.mc.player.isCollidedHorizontally = true;
                    Helper.mc.player.isAirBorne = true;
                    Helper.mc.player.jump();
                    ModuleComponent.timerHelper.reset();
                }
            }
        }
    }
}