package com.masterof13fps.features.modules.impl.misc;

import com.masterof13fps.features.modules.Category;
import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.ModuleInfo;
import com.masterof13fps.manager.eventmanager.Event;
import com.masterof13fps.manager.eventmanager.impl.EventPacket;
import com.masterof13fps.manager.eventmanager.impl.EventUpdate;
import com.masterof13fps.manager.notificationmanager.NotificationType;
import com.masterof13fps.utils.NotifyUtil;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

@ModuleInfo(name = "FlagDetector", category = Category.MISC, description = "Detects all flags from anticheats")
public class FlagDetector extends Module {

    boolean flagged;

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
        if(event instanceof EventPacket){
            if(((EventPacket) event).getType() == EventPacket.Type.RECEIVE){
                if(((EventPacket) event).getPacket() instanceof S08PacketPlayerPosLook){
                    flagged = true;
                }
            }
        }

        if(event instanceof EventUpdate && flagged){
            notify.notification("Flag erkannt!", "Du wurdest geflagged, überprüfe deine aktiven Mods um einen Kick / Bann zu verhindern!", NotificationType.WARNING, 5);
            flagged = false;
        }
    }
}
