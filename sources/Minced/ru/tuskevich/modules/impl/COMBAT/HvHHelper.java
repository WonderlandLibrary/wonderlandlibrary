// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.COMBAT;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import ru.tuskevich.event.events.impl.EventTick;
import ru.tuskevich.event.EventTarget;
import net.minecraft.world.World;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.server.SPacketEntityStatus;
import ru.tuskevich.event.events.impl.EventPacket;
import ru.tuskevich.ui.dropui.setting.Setting;
import ru.tuskevich.util.math.TimerUtility;
import ru.tuskevich.ui.dropui.setting.imp.SliderSetting;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "HvHHelper", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd", type = Type.COMBAT)
public class HvHHelper extends Module
{
    public SliderSetting delay;
    public static boolean isShield;
    public static boolean isGapple;
    TimerUtility timeUtils;
    
    public HvHHelper() {
        this.delay = new SliderSetting("Delay get item", 420.0f, 0.0f, 1000.0f, 1.0f);
        this.timeUtils = new TimerUtility();
        this.add(this.delay);
    }
    
    @EventTarget
    public void onPacket(final EventPacket e) {
        if (e.getPacket() instanceof SPacketEntityStatus) {
            final SPacketEntityStatus packet = (SPacketEntityStatus)e.getPacket();
            if (packet.getEntity(HvHHelper.mc.world) instanceof EntityPlayerSP) {
                if (packet.getOpCode() == 30) {
                    System.out.println("2");
                    HvHHelper.isGapple = true;
                }
                if (packet.getOpCode() == 9) {
                    System.out.println("1");
                    HvHHelper.isShield = true;
                }
            }
        }
    }
    
    @EventTarget
    public void onTick(final EventTick e) {
        final int gapple = AutoTotem.getSlotIDFromItem(Items.GOLDEN_APPLE);
        final int shield = AutoTotem.getSlotIDFromItem(Items.SHIELD);
        final Minecraft mc = HvHHelper.mc;
        final boolean gappleInHand = Minecraft.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE;
        final Minecraft mc2 = HvHHelper.mc;
        final boolean shieldInhand = Minecraft.player.getHeldItemOffhand().getItem() == Items.SHIELD;
        final Minecraft mc3 = HvHHelper.mc;
        final boolean totemInHand = Minecraft.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING;
        final Minecraft mc4 = HvHHelper.mc;
        final float health = Minecraft.player.getHealth();
        final Minecraft mc5 = HvHHelper.mc;
        final float hp = health + Minecraft.player.getAbsorptionAmount();
        final boolean gcheck = HvHHelper.isGapple || hp <= 13.0f;
        boolean b = false;
        Label_0185: {
            Label_0180: {
                if (!HvHHelper.isShield) {
                    final Minecraft mc6 = HvHHelper.mc;
                    if (Minecraft.player.getCooldownTracker().hasCooldown(Items.GOLDEN_APPLE)) {
                        final Minecraft mc7 = HvHHelper.mc;
                        if (!Minecraft.player.getCooldownTracker().hasCooldown(Items.SHIELD)) {
                            break Label_0180;
                        }
                    }
                    b = false;
                    break Label_0185;
                }
            }
            b = true;
        }
        final boolean scheck = b;
        if (totemInHand) {
            return;
        }
        if (this.timeUtils.hasTimeElapsed(this.delay.getIntValue())) {
            Label_0401: {
                if (gcheck) {
                    final Minecraft mc8 = HvHHelper.mc;
                    if (!Minecraft.player.getCooldownTracker().hasCooldown(Items.GOLDEN_APPLE) && !gappleInHand && gapple >= 0) {
                        final PlayerControllerMP playerController = HvHHelper.mc.playerController;
                        final int windowId = 0;
                        final int slotId = gapple;
                        final int mouseButton = 0;
                        final ClickType pickup = ClickType.PICKUP;
                        final Minecraft mc9 = HvHHelper.mc;
                        playerController.windowClick(windowId, slotId, mouseButton, pickup, Minecraft.player);
                        final PlayerControllerMP playerController2 = HvHHelper.mc.playerController;
                        final int windowId2 = 0;
                        final int slotId2 = 45;
                        final int mouseButton2 = 0;
                        final ClickType pickup2 = ClickType.PICKUP;
                        final Minecraft mc10 = HvHHelper.mc;
                        playerController2.windowClick(windowId2, slotId2, mouseButton2, pickup2, Minecraft.player);
                        final PlayerControllerMP playerController3 = HvHHelper.mc.playerController;
                        final int windowId3 = 0;
                        final int slotId3 = gapple;
                        final int mouseButton3 = 0;
                        final ClickType pickup3 = ClickType.PICKUP;
                        final Minecraft mc11 = HvHHelper.mc;
                        playerController3.windowClick(windowId3, slotId3, mouseButton3, pickup3, Minecraft.player);
                        break Label_0401;
                    }
                }
                if (scheck && !shieldInhand && shield >= 0) {
                    final PlayerControllerMP playerController4 = HvHHelper.mc.playerController;
                    final int windowId4 = 0;
                    final int slotId4 = shield;
                    final int mouseButton4 = 0;
                    final ClickType pickup4 = ClickType.PICKUP;
                    final Minecraft mc12 = HvHHelper.mc;
                    playerController4.windowClick(windowId4, slotId4, mouseButton4, pickup4, Minecraft.player);
                    final PlayerControllerMP playerController5 = HvHHelper.mc.playerController;
                    final int windowId5 = 0;
                    final int slotId5 = 45;
                    final int mouseButton5 = 0;
                    final ClickType pickup5 = ClickType.PICKUP;
                    final Minecraft mc13 = HvHHelper.mc;
                    playerController5.windowClick(windowId5, slotId5, mouseButton5, pickup5, Minecraft.player);
                    final PlayerControllerMP playerController6 = HvHHelper.mc.playerController;
                    final int windowId6 = 0;
                    final int slotId6 = shield;
                    final int mouseButton6 = 0;
                    final ClickType pickup6 = ClickType.PICKUP;
                    final Minecraft mc14 = HvHHelper.mc;
                    playerController6.windowClick(windowId6, slotId6, mouseButton6, pickup6, Minecraft.player);
                }
            }
            if (shieldInhand || gappleInHand) {
                HvHHelper.isShield = false;
                HvHHelper.isGapple = false;
            }
            this.timeUtils.reset();
        }
    }
    
    static {
        HvHHelper.isShield = false;
        HvHHelper.isGapple = false;
    }
}
