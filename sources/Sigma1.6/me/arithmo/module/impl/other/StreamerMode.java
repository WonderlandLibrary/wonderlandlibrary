package me.arithmo.module.impl.other;

import java.util.Iterator;
import java.util.List;
import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventMotion;
import me.arithmo.event.impl.EventPacket;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import me.arithmo.module.data.Setting;
import me.arithmo.module.data.SettingsMap;
import me.arithmo.util.misc.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.util.IChatComponent;

public class StreamerMode
  extends Module
{
  private String NAMEPROTECT = "PROTECT";
  
  public StreamerMode(ModuleData data)
  {
    super(data);
    this.settings.put(this.NAMEPROTECT, new Setting(this.NAMEPROTECT, Boolean.valueOf(true), "Protects your name."));
  }
  
  @RegisterEvent(events={EventMotion.class, EventPacket.class})
  public void onEvent(Event event)
  {
    if ((event instanceof EventMotion))
    {
      EventMotion em = (EventMotion)event;
      if (!em.isPre()) {}
    }
    if ((event instanceof EventPacket))
    {
      EventPacket ep = (EventPacket)event;
      if (ep.isIncoming())
      {
        String temp;
        if ((ep.getPacket() instanceof S02PacketChat))
        {
          S02PacketChat packet = (S02PacketChat)ep.getPacket();
          if ((packet.func_148915_c().getUnformattedText().contains(mc.thePlayer.getName())) && (((Boolean)((Setting)this.settings.get(this.NAMEPROTECT)).getValue()).booleanValue()))
          {
            temp = packet.func_148915_c().getFormattedText();
            ChatUtil.printChat(temp.replaceAll(mc.thePlayer.getName(), "§k" + mc.thePlayer.getName() + "kIlOkIlOkIlO"));
            ep.setCancelled(true);
          }
        }
        if ((ep.getPacket() instanceof S38PacketPlayerListItem))
        {
          S38PacketPlayerListItem packet = (S38PacketPlayerListItem)ep.getPacket();
          Object localObject;
        }
      }
    }
  }
}
