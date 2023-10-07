package cc.novoline.modules.misc;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.PacketEvent;
import cc.novoline.gui.screen.setting.Manager;
import cc.novoline.gui.screen.setting.Setting;
import cc.novoline.gui.screen.setting.SettingType;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.configurations.annotation.Property;
import cc.novoline.modules.configurations.property.object.BooleanProperty;
import cc.novoline.modules.configurations.property.object.IntProperty;
import cc.novoline.modules.configurations.property.object.PropertyFactory;
import cc.novoline.modules.visual.Waypoints;
import cc.novoline.utils.DebugUtil;
import cc.novoline.utils.notifications.NotificationType;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.util.ChatComponentText;
import org.checkerframework.checker.nullness.qual.NonNull;

public class LightningTracker extends AbstractModule {

    @Property("auto-waypoint")
    BooleanProperty auto_waypoint = PropertyFactory.booleanFalse();
    @Property("additional_y")
    private IntProperty add_y = PropertyFactory.createInt(10).minimum(10).maximum(90);

    public LightningTracker(@NonNull ModuleManager novoline) {
        super(novoline, EnumModuleType.MISC, "LightningTracker", "Lightning Tracker");
        Manager.put(new Setting("LT_ADD_Y", "Addition Y", SettingType.SLIDER, this, add_y, 5));
        Manager.put(new Setting("LT_AUTO_WP", "Auto waypoint", SettingType.CHECKBOX, this, auto_waypoint));
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        if (event.getState().equals(PacketEvent.State.INCOMING)) {
            if (event.getPacket() instanceof S29PacketSoundEffect) {
                S29PacketSoundEffect packet = (S29PacketSoundEffect) event.getPacket();

                if (packet.getSoundName().equals("ambient.weather.thunder")) {
                    int x = (int) packet.getX(), y = (int) packet.getY() + add_y.get(), z = (int) packet.getZ();

                    novoline.getNotificationManager().pop(getDisplayName(), "Lightning detected " + x + " " + y + " " + z, 3_000, NotificationType.INFO);
                    String command = mc.isSingleplayer() ? "/tp " : ".tp ", coords = x + " " + y + " " + z;
                    ChatComponentText coordinates = new ChatComponentText(coords);

                    coordinates.setChatStyle(coordinates.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(command + coords))));
                    coordinates.setChatStyle(coordinates.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command + coords)));
                    ChatComponentText message = new ChatComponentText(DebugUtil.prefix(getDisplayName()).getFormattedText() + "Lightning detected ");

                    message.appendSibling(coordinates);
                    mc.ingameGUI.getChatGUI().printChatMessage(message);
                    Waypoints waypoints = getModule(Waypoints.class);

                    if (auto_waypoint.get()) {
                        for (Waypoints.Waypoint waypoint : waypoints.getWaypointsList()) {
                            if (waypoint.getName().equals("Lightning") && waypoint.getX() == x && waypoint.getY() == y && waypoint.getZ() == z) {
                                return;
                            }
                        }

                        waypoints.addWaypoint(new Waypoints.Waypoint("Lightning", x, y, z));
                    }
                }
            }
        }
    }
}
