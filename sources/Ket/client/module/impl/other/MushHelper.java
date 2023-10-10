package client.module.impl.other;

import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.PacketReceiveEvent;
import client.event.impl.other.Render2DEvent;
import client.module.Category;
import client.module.Module;
import client.module.ModuleInfo;
import client.util.MSTimer;
import client.util.TimeUtil;
import lombok.Getter;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;

import java.text.DecimalFormat;

@ModuleInfo(name = "Mush Helper", description = "", category = Category.PLAYER)
public class MushHelper extends Module {


}