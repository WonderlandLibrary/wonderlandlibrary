package cn.hanabi.modules.modules.render;

import cn.hanabi.events.EventPacket;
import cn.hanabi.events.EventUpdate;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.server.S2BPacketChangeGameState;

public class WorldWeather extends Mod {
   public static Value modeValue = (new Value("World Weather", "Mode", 0)).LoadValue(new String[]{"Clear", "Rain", "Thunder"});

   public WorldWeather() {
      super("World Weather", Category.RENDER);
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      if (modeValue.isCurrentMode("Clear")) {
         mc.theWorld.setRainStrength(0.0F);
         mc.theWorld.setThunderStrength(0.0F);
      } else if (modeValue.isCurrentMode("Rain")) {
         mc.theWorld.setRainStrength(1.0F);
         mc.theWorld.setThunderStrength(0.0F);
      } else if (modeValue.isCurrentMode("Thunder")) {
         mc.theWorld.setRainStrength(1.0F);
         mc.theWorld.setThunderStrength(1.0F);
      }

   }

   @EventTarget
   public void onPacket(EventPacket event) {
      if (event.packet instanceof S2BPacketChangeGameState) {
         S2BPacketChangeGameState S2BPacket = (S2BPacketChangeGameState)event.packet;
         if (S2BPacket.getGameState() == 7 || S2BPacket.getGameState() == 8) {
            event.setCancelled(true);
         }
      }

   }
}
