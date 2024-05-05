package vip.radium.module.impl.misc;

import io.github.nevalackin.homoBus.annotations.EventLink;
import io.github.nevalackin.homoBus.Listener;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import vip.radium.RadiumClient;
import vip.radium.event.impl.packet.PacketReceiveEvent;
import vip.radium.event.impl.player.UpdatePositionEvent;
import vip.radium.event.impl.render.DisplayTitleEvent;
import vip.radium.module.Module;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.ModuleManager;
import vip.radium.module.impl.combat.KillAura;
import vip.radium.module.impl.movement.LongJump;
import vip.radium.module.impl.movement.Speed;
import vip.radium.module.impl.player.InventoryManager;
import vip.radium.notification.Notification;
import vip.radium.notification.NotificationType;
import vip.radium.property.Property;
import vip.radium.utils.HypixelGameUtils;
import vip.radium.utils.TimerUtil;
import vip.radium.utils.Wrapper;

import java.util.Arrays;
import java.util.List;

@ModuleInfo(label = "Auto Hypixel", category = ModuleCategory.MISCELLANEOUS)
public final class AutoHypixel extends Module {

    private final Property<Boolean> autoDisableProperty = new Property<>("On Flag", true);
    private final Property<Boolean> respawnProperty = new Property<>("On Respawn", true);
    private final Property<Boolean> autoJoinProperty = new Property<>("Auto Join", true);

    private final TimerUtil gameTimer = new TimerUtil();
    private final TimerUtil respawnTimer = new TimerUtil();

    private List<Module> movementModules;
    private List<Module> disableOnRespawn;

    private boolean needSend;

    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceiveEvent = event -> {
        if (event.getPacket() instanceof S08PacketPlayerPosLook && autoDisableProperty.getValue()) {
            boolean msg = false;
            for (Module module : movementModules)
                if (module.isEnabled()) {
                    module.toggle();
                    if (!msg)
                        msg = true;
                }

            if (msg)
                RadiumClient.getInstance().getNotificationManager().add(new Notification("Flag",
                        "Disabling modules to prevent flags", NotificationType.WARNING));
        } else if (event.getPacket() instanceof S07PacketRespawn && respawnProperty.getValue()) {
            if (respawnTimer.hasElapsed(50L)) {
                boolean msg = false;
                for (Module module : disableOnRespawn) {
                    if (module.isEnabled()) {
                        module.toggle();
                        if (!msg)
                            msg = true;
                    }
                }

                if (msg)
                    RadiumClient.getInstance().getNotificationManager().add(new Notification("Respawned",
                            "Disabled some modules on respawn",
                            NotificationType.INFO));
                respawnTimer.reset();
            }
        } else if (event.getPacket() instanceof S02PacketChat) {
            S02PacketChat packetChat = (S02PacketChat) event.getPacket();
            if (packetChat.getChatComponent().getUnformattedText().contains("Protect your bed and destroy the enemy beds"))
                RadiumClient.getInstance().getNotificationManager().add(new Notification(
                        "Bedwars",
                        "Do not fly until this notification closes",
                        20000L,
                        NotificationType.WARNING));
        }
    };

    @EventLink
    public final Listener<DisplayTitleEvent> onDisplayTitleEvent = event -> {
        if (autoJoinProperty.getValue() && event.getTitle().contains("VICTORY")) {
            RadiumClient.getInstance().getNotificationManager().add(
                    new Notification("Auto Join",
                            "Sending you to a new game in 2 seconds", 2000L,
                            NotificationType.INFO));
            needSend = true;
            gameTimer.reset();
        }
    };
    @EventLink
    public final Listener<UpdatePositionEvent> onUpdatePositionEvent = event -> {
        if (event.isPre()) {
            if (needSend && gameTimer.hasElapsed(2000L)) {
                Wrapper.sendPacketDirect(new C01PacketChatMessage("/play " +
                        HypixelGameUtils.getSkyWarsMode().name().toLowerCase()));
                needSend = false;
            }

//            if (!pingSpoof.isEnabled() && ServerUtils.isOnHypixel()) {
//                pingSpoof.toggle();
//                RadiumClient.getInstance().getNotificationManager().add(new Notification("Bypass",
//                        "You must use Ping Spoof on hypixel", NotificationType.WARNING));
//            }
        }
    };

    @Override
    public void onEnable() {
        if (movementModules == null)
            movementModules = Arrays.asList(
                    ModuleManager.getInstance(Speed.class),
                    ModuleManager.getInstance(LongJump.class));

        if (disableOnRespawn == null)
            disableOnRespawn = Arrays.asList(
                    ModuleManager.getInstance(KillAura.class),
                    ModuleManager.getInstance(InventoryManager.class));
    }
}
