/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.combat;

import dev.rise.Rise;
import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.util.player.PacketUtil;
import dev.rise.util.player.PlayerUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLadder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.Objects;

@ModuleInfo(name = "AutoPot", description = "Throws potions for you", category = Category.COMBAT)
public final class AutoPot extends Module {

    private final NumberSetting minHealth = new NumberSetting("Health", this, 15, 1, 20, 1);
    private final BooleanSetting randomiseRots = new BooleanSetting("Randomise Rotations", this, true);
    private final BooleanSetting packet = new BooleanSetting("Extra Packet", this, false);
    private final BooleanSetting jump = new BooleanSetting("Jump", this, false);

    private int ticksSinceLastSplash, ticksSinceCanSplash, oldSlot;
    private boolean needSplash, switchBack;

    private final ArrayList<Integer> acceptedPotions = new ArrayList() {{
        add(6);
        add(1);
        add(5);
        add(8);
        add(14);
        add(12);
        add(10);
        add(16);
    }};

    @Override
    public void onDisable() {
        needSplash = switchBack = false;
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        ticksSinceLastSplash++;

        if (Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getModule("Scaffold")).isEnabled() || mc.thePlayer.isInLiquid() || (PlayerUtil.getBlockRelativeToPlayer(0, -1, 0) instanceof BlockAir || PlayerUtil.getBlockRelativeToPlayer(0, -1, 0) instanceof BlockLadder))
            ticksSinceCanSplash = 0;
        else
            ticksSinceCanSplash++;

        if (switchBack) {
            PacketUtil.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            PacketUtil.sendPacket(new C09PacketHeldItemChange(oldSlot));
            switchBack = false;
            return;
        }

        if (ticksSinceCanSplash <= 1 || !mc.thePlayer.onGround)
            return;

        oldSlot = mc.thePlayer.inventory.currentItem;
        for (int i = 36; i < 45; ++i) {
            final ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (itemStack != null && mc.currentScreen == null) {
                final Item item = itemStack.getItem();
                if (item instanceof ItemPotion) {
                    final ItemPotion p = (ItemPotion) item;
                    if (ItemPotion.isSplash(itemStack.getMetadata())) {
                        if (p.getEffects(itemStack.getMetadata()) != null) {
                            final int potionID = p.getEffects(itemStack.getMetadata()).get(0).getPotionID();
                            boolean hasPotionIDActive = false;

                            for (final PotionEffect potion : mc.thePlayer.getActivePotionEffects()) {
                                if (potion.getPotionID() == potionID && potion.getDuration() > 0) {
                                    hasPotionIDActive = true;
                                    break;
                                }
                            }

                            if (acceptedPotions.contains(potionID) && !hasPotionIDActive && ticksSinceLastSplash > 20) {
                                final String effectName = p.getEffects(itemStack.getMetadata()).get(0).getEffectName();

                                if ((effectName.contains("regeneration") || effectName.contains("heal")) && mc.thePlayer.getHealth() > minHealth.getValue())
                                    continue;

                                if (jump.isEnabled()) {
                                    event.setPitch(randomiseRots.isEnabled() ? -RandomUtils.nextFloat(89, 90) : -90);
                                    if (!needSplash) {
                                        mc.thePlayer.jump();
                                        needSplash = true;

                                        new Thread(() -> {
                                            try {
                                                Thread.sleep(300L); // TODO: fix
                                            } catch (final InterruptedException e) {
                                                e.printStackTrace();
                                            }

                                            if (packet.isEnabled())
                                                PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, randomiseRots.isEnabled() ? -RandomUtils.nextFloat(89, 90) : -90, mc.thePlayer.onGround));

                                            needSplash = false;
                                        }).start();
                                    } else {
                                        PacketUtil.sendPacket(new C09PacketHeldItemChange(i - 36));
                                        PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(itemStack));
                                        switchBack = true;

                                        ticksSinceLastSplash = 0;
                                        needSplash = false;
                                    }
                                } else {
                                    event.setPitch(randomiseRots.isEnabled() ? RandomUtils.nextFloat(89, 90) : 90);
                                    if (!needSplash) {
                                        if (packet.isEnabled())
                                            PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, randomiseRots.isEnabled() ? RandomUtils.nextFloat(89, 90) : 90, mc.thePlayer.onGround));

                                        needSplash = true;
                                    } else {
                                        PacketUtil.sendPacket(new C09PacketHeldItemChange(i - 36));
                                        PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(itemStack));
                                        switchBack = true;

                                        ticksSinceLastSplash = 0;
                                        needSplash = false;
                                    }
                                }
                                return;
                            }
                        }
                    }
                }
            }
        }
    }
}