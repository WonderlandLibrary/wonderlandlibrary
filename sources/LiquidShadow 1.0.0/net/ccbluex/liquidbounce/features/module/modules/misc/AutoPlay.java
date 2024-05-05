package net.ccbluex.liquidbounce.features.module.modules.misc;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoArmor;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.features.module.modules.player.InventoryCleaner;
import net.ccbluex.liquidbounce.features.module.modules.world.ChestAura;
import net.ccbluex.liquidbounce.features.module.modules.world.ChestStealer;
import net.ccbluex.liquidbounce.features.module.modules.world.Fucker;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotificationType;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S45PacketTitle;

@ModuleInfo(name = "AutoPlay",description = "Automatically play again this game when you end this game.",category = ModuleCategory.MISC)
public class AutoPlay extends Module {
    private final ListValue serverValue = new ListValue("Server",new String[]{"Mineland","LuoHuaXingYu"},"Mineland");
    private final IntegerValue delayValue = new IntegerValue("Delay",5,1,10);
    private final BoolValue autoGGValue = new BoolValue("AutoGG",true);

    private final BoolValue disableKillAuraValue = new BoolValue("DisableKillAura",true);
    private final BoolValue disableFuckerValue = new BoolValue("DisableFucker",true);
    private final BoolValue disableSpeedValue = new BoolValue("DisableSpeed",true);
    private final BoolValue disableChestStealerValue = new BoolValue("DisableChestStealer",true);
    private final BoolValue disableChestAuraValue = new BoolValue("DisableChestAura",true);
    private final BoolValue disableInventoryCleanerValue = new BoolValue("DisableInventoryCleaner",true);
    private final BoolValue disableAutoArmorValue = new BoolValue("DisableAutoArmor",true);

    private int ticks;
    private boolean needRestart;

    private int paperSlot;
    private boolean canRestart;

    @EventTarget
    public void onPacket(PacketEvent packetEvent) {
        Packet<?> packet = packetEvent.getPacket();
        if (packet instanceof S02PacketChat) {
            String message = ((S02PacketChat) packet).getChatComponent().getFormattedText();
            if (serverValue.get().equalsIgnoreCase("Mineland")) {
                if (message.toLowerCase().contains("§".toLowerCase()) && message.toLowerCase().contains("for the game.".toLowerCase()) && !message.toLowerCase().contains("[".toLowerCase())) {
                    canRestart = true;
                }
            }
        }
        if (packet instanceof S45PacketTitle) {
            String message = ((S45PacketTitle) packet).getMessage().getFormattedText();
            if (serverValue.get().equalsIgnoreCase("LuoHuaXingYu")) {
                if (message.contains("恭") && message.contains("喜") && message.contains("获") && message.contains("胜")) {
                    canRestart = true;
                }
            }
        }
//        if (packet instanceof S45PacketTitle) {
//            S45PacketTitle titlePacket = (S45PacketTitle) packet;
//            String message = titlePacket.getMessage().getFormattedText();
//            if (serverValue.get().equalsIgnoreCase("Mineland")) {
//                if (message.toLowerCase().contains("wasted".toLowerCase())) {
////                    restartMode = "Lose";
////                    needRestart = true;
//                }
//            }
//        }
//        if (packet instanceof S02PacketChat) {
//            String message = ((S02PacketChat) packet).getChatComponent().getFormattedText();
//            if (serverValue.get().equalsIgnoreCase("Mineland")) {
//                if (message.toLowerCase().contains("Victory".toLowerCase())) {
//                    restartMode = "Win";
//                    needRestart = true;
//                }
//                if (message.toLowerCase().contains("§".toLowerCase()) && message.toLowerCase().contains("for the game.")) {
//                    restartMode = "Lose";
//                    needRestart = true;
//                }
//            } else {}
//        }
    }

    @EventTarget
    public void onUpdate(UpdateEvent updateEvent) {
        if (serverValue.get().equalsIgnoreCase("Mineland") || serverValue.get().equalsIgnoreCase("LuoHuaXingYu")) {
            for (int i = 36;;) {
                if (i > 44) {
                    break;
                }
                ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (itemStack == null) {
                    i++;
                    continue;
                }
                if (itemStack.getItem() == Items.paper && canRestart) {
                    paperSlot = i;
                    needRestart = true;
                    break;
                }
                i++;
            }
        }


        if (ticks >= delayValue.get() * 20) {
            LiquidBounce.hud.addNotification(new Notification("Sending you to next game...", NotificationType.NORMAL));
            if (serverValue.get().equalsIgnoreCase("Mineland")) {
                ItemStack paperItem = mc.thePlayer.inventoryContainer.getSlot(paperSlot).getStack();
                mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(paperSlot - 36));
                mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(paperItem));
            } else if (serverValue.get().equalsIgnoreCase("LuoHuaXingYu")) {
                ItemStack paperItem = mc.thePlayer.inventoryContainer.getSlot(paperSlot).getStack();
                mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(paperSlot - 36));
                mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(paperItem));
            }
            canRestart = false;
            needRestart = false;
            ticks = 0;
        }
        if (needRestart && ticks == 0) {
            LiquidBounce.hud.addNotification(new Notification("Sending you to next game in " + delayValue.get() + " seconds.",NotificationType.NORMAL));

            if (disableKillAuraValue.get())
                LiquidBounce.moduleManager.getModule(KillAura.class).setState(false);
            if (disableFuckerValue.get())
                LiquidBounce.moduleManager.getModule(Fucker.class).setState(false);
            if (disableSpeedValue.get())
                LiquidBounce.moduleManager.getModule(Speed.class).setState(false);
            if (disableChestStealerValue.get())
                LiquidBounce.moduleManager.getModule(ChestStealer.class).setState(false);
            if (disableChestAuraValue.get())
                LiquidBounce.moduleManager.getModule(ChestAura.class).setState(false);
            if (disableInventoryCleanerValue.get())
                LiquidBounce.moduleManager.getModule(InventoryCleaner.class).setState(false);
            if (disableAutoArmorValue.get())
                LiquidBounce.moduleManager.getModule(AutoArmor.class).setState(false);

            if (autoGGValue.get()) {
                mc.thePlayer.sendChatMessage("gg");
            }
        }
        if (needRestart) {
            ticks++;
        } else {
            ticks = 0;
        }
    }
}
