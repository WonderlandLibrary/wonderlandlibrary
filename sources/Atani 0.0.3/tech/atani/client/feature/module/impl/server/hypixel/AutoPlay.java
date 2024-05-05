package tech.atani.client.feature.module.impl.server.hypixel;

import cn.muyang.nativeobfuscator.Native;
import com.google.common.base.Supplier;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.EnumChatFormatting;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.feature.value.impl.CheckBoxValue;
import tech.atani.client.feature.value.impl.SliderValue;
import tech.atani.client.feature.value.impl.StringBoxValue;
import tech.atani.client.listener.event.minecraft.network.PacketEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.utility.interfaces.Methods;
import tech.atani.client.utility.math.time.TimeHelper;

@Native
@ModuleData(name = "AutoPlay", identifier = "mc.hypixel.net AutoPlay", description = "Automatically joins new games.", category = Category.SERVER, supportedIPs = {"mc.hypixel.net"})
public class AutoPlay extends Module {

    private final CheckBoxValue skyWars = new CheckBoxValue("Skywars", "Should the module automatically join new skywars games?", this, true);
    private final StringBoxValue skyWarsMode = new StringBoxValue("Skywars Mode", "Which skywars gamemode should the module join?", this, new String[] {"Solo Normal", "Solo Insane"}, new Supplier[]{skyWars::isEnabled});
    public SliderValue<Long> delay = new SliderValue<Long>("Delay", "What will be the delay between games?", this, 1000L, 0L, 5000L, 0);

    // SkyWars
    private final TimeHelper swTimer = new TimeHelper();

    @Listen
    public void onPacket(PacketEvent event) {
        if(Methods.mc.thePlayer == null || Methods.mc.theWorld == null)
            return;

        if(event.getPacket() instanceof S02PacketChat && skyWars.getValue()) {
            String unformatted = EnumChatFormatting.getTextWithoutFormattingCodes(((S02PacketChat)event.getPacket()).getChatComponent().getUnformattedText()), text = unformatted.replace(" ", "");

            String victory = "You won! Want to play again? Click here!";
            String death = "You died! Want to play again? Click here!";

            if(unformatted.contains(victory) || unformatted.contains(death) && swTimer.hasReached(delay.getValue())) {
                sendPacketUnlogged(new C01PacketChatMessage(skyWarsMode.is("Solo Normal") ? "/play solo_normal" : "/play solo_insane"));
            }
        }
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

}
