// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.modules.player;

import java.util.Iterator;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.Packet;
import Lavish.utils.misc.NetUtil;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.item.ItemPotion;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiChest;
import Lavish.event.events.EventMotion;
import Lavish.event.Event;
import Lavish.ClickGUI.settings.Setting;
import Lavish.Client;
import Lavish.modules.Category;
import Lavish.utils.misc.Timer;
import Lavish.modules.Module;

public class AutoHeal extends Module
{
    public static boolean isPotting;
    Timer timer;
    
    static {
        AutoHeal.isPotting = false;
    }
    
    public AutoHeal() {
        super("AutoHeal", 0, true, Category.Player, "Pots automatically for you!");
        this.timer = new Timer();
        Client.instance.setmgr.rSetting(new Setting("Pots", this, true));
        Client.instance.setmgr.rSetting(new Setting("Pot Delay", this, 50.0, 1.0, 5000.0, false));
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventMotion && e.isPre()) {
            final int prevSlot = AutoHeal.mc.thePlayer.inventory.currentItem;
            if (!(AutoHeal.mc.currentScreen instanceof GuiChest) && !(AutoHeal.mc.currentScreen instanceof GuiChat) && !(AutoHeal.mc.currentScreen instanceof GuiInventory)) {
                for (int i = 0; i < 9; ++i) {
                    final ItemStack itemStack = AutoHeal.mc.thePlayer.inventory.getStackInSlot(i);
                    if (Client.instance.setmgr.getSettingByName("Pots").getValBoolean()) {
                        if (itemStack != null && itemStack.getItem() instanceof ItemPotion) {
                            final ItemPotion potion = (ItemPotion)itemStack.getItem();
                            if (ItemPotion.isSplash(itemStack.getItemDamage())) {
                                for (final PotionEffect effect : potion.getEffects(itemStack)) {
                                    final PotionEffect o = effect;
                                    if (effect.getPotionID() == Potion.heal.id || effect.getPotionID() == Potion.regeneration.id) {
                                        if (AutoHeal.mc.thePlayer.getHealth() > 13.0f || AutoHeal.mc.thePlayer.isPotionActive(Potion.regeneration) || !this.timer.hasTimeElapsed(Client.instance.setmgr.getSettingByName("Pot Delay").getValDouble(), true)) {
                                            continue;
                                        }
                                        if (!AutoHeal.mc.thePlayer.onGround) {
                                            continue;
                                        }
                                        final float oldPitch = AutoHeal.mc.thePlayer.rotationPitch;
                                        AutoHeal.isPotting = true;
                                        NetUtil.sendPacketNoEvents(new C03PacketPlayer.C06PacketPlayerPosLook(AutoHeal.mc.thePlayer.posX, AutoHeal.mc.thePlayer.posY, AutoHeal.mc.thePlayer.posZ, AutoHeal.mc.thePlayer.rotationYaw, 90.0f, AutoHeal.mc.thePlayer.onGround));
                                        NetUtil.sendPacketNoEvents(new C09PacketHeldItemChange(i));
                                        NetUtil.sendPacketNoEvents(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, AutoHeal.mc.thePlayer.getHeldItem(), 0.0f, 0.0f, 0.0f));
                                        NetUtil.sendPacketNoEvents(new C09PacketHeldItemChange(prevSlot));
                                        NetUtil.sendPacketNoEvents(new C03PacketPlayer.C06PacketPlayerPosLook(AutoHeal.mc.thePlayer.posX, AutoHeal.mc.thePlayer.posY, AutoHeal.mc.thePlayer.posZ, AutoHeal.mc.thePlayer.rotationYaw, oldPitch, AutoHeal.mc.thePlayer.onGround));
                                        this.timer.reset();
                                        AutoHeal.isPotting = false;
                                    }
                                    if ((effect.getPotionID() == Potion.moveSpeed.id || effect.getPotionID() == Potion.jump.id) && !AutoHeal.mc.thePlayer.isPotionActive(Potion.moveSpeed) && !AutoHeal.mc.thePlayer.isPotionActive(Potion.jump) && this.timer.hasTimeElapsed(Client.instance.setmgr.getSettingByName("Pot Delay").getValDouble(), true)) {
                                        if (!AutoHeal.mc.thePlayer.onGround) {
                                            continue;
                                        }
                                        final float oldPitch = AutoHeal.mc.thePlayer.rotationPitch;
                                        AutoHeal.isPotting = true;
                                        NetUtil.sendPacketNoEvents(new C03PacketPlayer.C06PacketPlayerPosLook(AutoHeal.mc.thePlayer.posX, AutoHeal.mc.thePlayer.posY, AutoHeal.mc.thePlayer.posZ, AutoHeal.mc.thePlayer.rotationYaw, 90.0f, AutoHeal.mc.thePlayer.onGround));
                                        NetUtil.sendPacketNoEvents(new C09PacketHeldItemChange(i));
                                        NetUtil.sendPacketNoEvents(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, AutoHeal.mc.thePlayer.getHeldItem(), 0.0f, 0.0f, 0.0f));
                                        NetUtil.sendPacketNoEvents(new C09PacketHeldItemChange(prevSlot));
                                        NetUtil.sendPacketNoEvents(new C03PacketPlayer.C06PacketPlayerPosLook(AutoHeal.mc.thePlayer.posX, AutoHeal.mc.thePlayer.posY, AutoHeal.mc.thePlayer.posZ, AutoHeal.mc.thePlayer.rotationYaw, oldPitch, AutoHeal.mc.thePlayer.onGround));
                                        this.timer.reset();
                                        AutoHeal.isPotting = false;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
