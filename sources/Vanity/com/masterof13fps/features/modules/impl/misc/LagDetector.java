package com.masterof13fps.features.modules.impl.misc;

import com.masterof13fps.Client;
import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.ModuleInfo;
import com.masterof13fps.utils.time.TimeHelper;
import com.masterof13fps.manager.eventmanager.Event;
import com.masterof13fps.manager.eventmanager.impl.EventPacket;
import com.masterof13fps.manager.eventmanager.impl.EventRender;
import com.masterof13fps.manager.fontmanager.UnicodeFontRenderer;
import com.masterof13fps.features.modules.Category;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.network.play.server.S02PacketChat;

import java.awt.*;

@ModuleInfo(name = "LagDetector", category = Category.MISC, description = "Detects any kind of lag of the server")
public class LagDetector extends Module {

    TimeHelper timeHelper = new TimeHelper();

    @Override
    public void onToggle() {

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventRender) {
            if (((EventRender) event).getType() == EventRender.Type.twoD) {
                if (timeHelper.hasReached(1000L)) {
                    ScaledResolution s = new ScaledResolution(mc);

                    UnicodeFontRenderer font = Client.main().fontMgr().font("Comfortaa", 32, Font.BOLD);

                    int currentMS = (int) (timeHelper.getCurrentMS() / 1000);
                    int lastMS = (int) (timeHelper.getLastMS() / 1000);
                    int lagSeconds = currentMS - lastMS;
                    int lagMS = (int) (timeHelper.getCurrentMS() - timeHelper.getLastMS());

                    String lagMessage = "Der Server sendet keine Reaktionen mehr (Lag?)";
                    String laggingSince = "Lag seit: " + lagSeconds + " Sekunde(n) / " + lagMS + "ms";

                    font.drawStringWithShadow(lagMessage, s.width() / 2 - font.getStringWidth(lagMessage) / 2, 5, -1);
                    font.drawStringWithShadow(laggingSince, s.width() / 2 - font.getStringWidth(laggingSince) / 2, 20, -1);
                }
            }
        }
        if (event instanceof EventPacket) {
            if (((EventPacket) event).getType() == EventPacket.Type.RECEIVE) {
                if (!(((EventPacket) event).getPacket() instanceof S02PacketChat)) {
                    timeHelper.reset();
                }
            }
        }
    }
}
