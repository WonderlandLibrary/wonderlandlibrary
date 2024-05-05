package net.ccbluex.liquidbounce.features.module.modules.misc;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotificationType;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.ccbluex.liquidbounce.value.TextValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

@ModuleInfo(name = "AntiStaff",description = "Anti you been banned by staff.",category = ModuleCategory.MISC)
public class AntiStaff extends Module {

    private final ListValue serverValue = new ListValue("Server",new String[]{"Mineland"},"Mineland");
    private final TextValue hubCommandValue = new TextValue("HubCommand","hub");
    private final IntegerValue quitDelayValue = new IntegerValue("QuitDelay",5,1,10);

    private int ticks;
    private boolean coolDown;

    @EventTarget
    public void onUpdate(UpdateEvent updateEvent) {
        if (ticks >= quitDelayValue.get() * 20) {
            coolDown = false;
            ticks = 0;
        }

        if (mc.thePlayer == null) return;

        if (serverValue.get().equalsIgnoreCase("Mineland")) {
            for (Entity entity : mc.theWorld.loadedEntityList) {
                if (entity instanceof EntityPlayer) {
                    EntityPlayer livingBase = (EntityPlayer) entity;
                    if (livingBase.getDisplayName() != null) {
                        if (livingBase.getDisplayName().getFormattedText().contains("Moder⁺") || livingBase.getDisplayName().getFormattedText().contains("Moder³") || livingBase.getDisplayName().getFormattedText().contains("Moder²") || livingBase.getDisplayName().getFormattedText().contains("Moder¹")) {
                            if (!coolDown) {
                                mc.thePlayer.sendChatMessage("/" + hubCommandValue.get());
                                LiquidBounce.hud.addNotification(new Notification(livingBase.getDisplayName().getFormattedText() + "§r is a staff!So you are leaving this game.", NotificationType.NORMAL));
                                coolDown = true;
                            }
                        }
                    }
                }
            }
        }

        if (coolDown) {
            ticks++;
        } else {
            ticks = 0;
        }
    }
}
