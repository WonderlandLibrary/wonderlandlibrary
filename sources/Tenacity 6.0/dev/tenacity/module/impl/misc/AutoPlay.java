// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.misc;

import dev.tenacity.event.impl.player.ChatReceivedEvent;
import dev.tenacity.ui.notifications.NotificationManager;
import dev.tenacity.ui.notifications.NotificationType;
import dev.tenacity.module.impl.render.NotificationsMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.module.Category;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.module.Module;

public final class AutoPlay extends Module
{
    private final ModeSetting mode;
    private final ModeSetting gamemode;
    private boolean canPlay;
    
    public AutoPlay() {
        super("AutoPlay", "Auto Play", Category.MISC, "Automatically sends you to a new game");
        this.mode = new ModeSetting("Mode", "Mospixel", new String[] { "Mospixel", "MushMC" });
        this.gamemode = new ModeSetting("Gamemode", "SkyWars", new String[] { "SkyWars", "BedWars" });
        this.addSettings(this.mode);
    }
    
    @Override
    public void onEnable() {
        this.setSuffix(this.mode.getMode());
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        AutoPlay.mc.thePlayer.noClip = false;
        super.onDisable();
    }
    
    @Override
    public void onMotionEvent(final MotionEvent event) {
        if (!event.isPre()) {
            return;
        }
        if (this.canPlay) {
            final String mode = this.mode.getMode();
            int n = -1;
            switch (mode.hashCode()) {
                case -114863563: {
                    if (mode.equals("Mospixel")) {
                        n = 0;
                        break;
                    }
                    break;
                }
                case -1978942349: {
                    if (mode.equals("MushMC")) {
                        n = 1;
                        break;
                    }
                    break;
                }
            }
            Label_0314: {
                switch (n) {
                    case 0: {
                        AutoPlay.mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(7));
                        AutoPlay.mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement());
                        if (AutoPlay.mc.currentScreen instanceof GuiChest) {
                            final GuiChest chest = (GuiChest)AutoPlay.mc.currentScreen;
                            AutoPlay.mc.playerController.windowClick(chest.inventorySlots.windowId, 50, 1, 0, AutoPlay.mc.thePlayer);
                        }
                    }
                    case 1: {
                        final String mode2 = this.gamemode.getMode();
                        switch (mode2) {
                            case "SkyWars": {
                                AutoPlay.mc.thePlayer.sendChatMessage("/play swsolo");
                            }
                            case "BedWars": {
                                AutoPlay.mc.thePlayer.sendChatMessage("/play bwsolo");
                                break Label_0314;
                            }
                        }
                        break;
                    }
                }
            }
            if (NotificationsMod.toggleNotifications.isEnabled()) {
                NotificationManager.post(NotificationType.INFO, "AutoPlay", "Sending you to a new game now!");
            }
            this.canPlay = false;
        }
    }
    
    @Override
    public void onChatReceivedEvent(final ChatReceivedEvent event) {
        if (AutoPlay.mc.thePlayer == null) {
            return;
        }
        final String message = event.message.getUnformattedText();
        final String mode = this.mode.getMode();
        switch (mode) {
            case "Mospixel": {
                if (message.startsWith("use spectator menu to navigate around")) {
                    this.canPlay = true;
                }
            }
            case "MushMC": {
                if (message.startsWith("Deseja jogar novamente? CLIQUE AQUI!") || message.startsWith("CARNAVAL NO MUSH")) {
                    this.canPlay = true;
                    break;
                }
                break;
            }
        }
    }
}
