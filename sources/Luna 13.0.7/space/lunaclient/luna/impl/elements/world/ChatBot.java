package space.lunaclient.luna.impl.elements.world;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import space.lunaclient.luna.api.element.Category;
import space.lunaclient.luna.api.element.Element;
import space.lunaclient.luna.api.element.ElementInfo;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.impl.events.EventUpdate;
import space.lunaclient.luna.util.ChatUtils;
import space.lunaclient.luna.util.MathUtils;

@ElementInfo(name="ChatBot", category=Category.WORLD, description="A simple chat-bot")
public class ChatBot
  extends Element
{
  public ChatBot() {}
  
  @EventRegister
  public void onUpdate(EventUpdate event)
  {
    ChatUtils.printMessage("!help", "Luna Chat Bot | 1.4 |", "Commands: help, penis (Your random penis length), server. Prefix: !");
    ChatUtils.printMessage("!penis", "Your penis length (Random, don't cry to mom): <" + MathUtils.getRandom(14) + "> centimeters. Max length -> 14.");
    ChatUtils.printMessage("!server", "Current server IP: " + mc.getCurrentServerData().serverIP);
    ChatUtils.printMessage("Play Again", "gg");
  }
}
