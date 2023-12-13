package summer.cheat.cheats.player;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S02PacketChat;
import summer.Summer;
import summer.base.manager.Selection;
import summer.cheat.guiutil.Setting;
import summer.cheat.eventsystem.EventTarget;
import summer.cheat.eventsystem.events.client.EventPacket;
import summer.base.utilities.TimerUtils;
import summer.base.manager.config.Cheats;

public class AutoJoin extends Cheats {
    public Minecraft mc = Minecraft.getMinecraft();
    private TimerUtils timer = new TimerUtils();
    public Setting mode;


    private final String[] change = new String[]{"1st Killer - ", "1st Place - ", "Winner: ", " - Damage Dealt - ",
            "1st - ", "Winning Team - ", "Winners: ", "Winner: ", "Winning Team: ", " win the game!", "Top Seeker: ",
            "1st Place: ", "Last team standing!", "Winner #1 (", "Top Survivors", "Winners - ",
            "You died! Want to play again? Click Here!"};
    private final String[] sky = new String[]{
            "Gather resources and equipment on your island"
    };

    public AutoJoin() {
        super("AutoJoin", "Only for skywars", Selection.PLAYER);
    }

    public void onSetup() {
        ArrayList<String> options = new ArrayList<>();
        options.add("SoloInsane");
        options.add("SoloNormal");
        options.add("TeamsNormal");
        options.add("TeamsInsane");
        Summer.INSTANCE.settingsManager.Property(mode = new Setting("Mode", this, "SoloInsane", options));

    }

    @EventTarget
    public void onPacket(EventPacket event) {
        this.setDisplayName("AutoJoin\u00A77 " + mode.getValString());
        if (this.timer.hasReached(5000.0D) && !event.isSending() && event.getPacket() instanceof S02PacketChat) {
            S02PacketChat packet = (S02PacketChat) event.getPacket();
            byte b;
            int i;
            String[] arrayOfString;
            for (i = (arrayOfString = this.change).length, b = 0; b < i; ) {
                String str = arrayOfString[b];
                if (packet.getChatComponent().getUnformattedText().contains(str) && mode.getValString().equalsIgnoreCase("SoloInsane")) {
                    Minecraft.thePlayer.sendChatMessage("/play solo_insane");
                } else if (packet.getChatComponent().getUnformattedText().contains(str) && mode.getValString().equalsIgnoreCase("SoloNormal")) {
                    Minecraft.thePlayer.sendChatMessage("/play solo_normal");
                } else if (packet.getChatComponent().getUnformattedText().contains(str) && mode.getValString().equalsIgnoreCase("TeamsNormal")) {
                    Minecraft.thePlayer.sendChatMessage("/play teams_normal");
                } else if (packet.getChatComponent().getUnformattedText().contains(str) && mode.getValString().equalsIgnoreCase("TeamsInsane")) {
                    Minecraft.thePlayer.sendChatMessage("/play teams_insane");
                }
                b++;
            }
        }
    }
}