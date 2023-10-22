package cn.hanabi.modules.modules.world;

import cn.hanabi.events.EventUpdate;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.client.C03PacketPlayer;

public class VClip extends Mod {
   public Value clips = new Value("DownClip", "Down Value", 4.0, 1.0, 10.0, 0.5);

   public VClip() {
      super("DownClip", Category.WORLD);
   }

   @EventTarget
   public void onUpdate(EventUpdate e) {
      mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - (Double)this.clips.getValue(), mc.thePlayer.posZ, true));
      mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - (Double)this.clips.getValue(), mc.thePlayer.posZ);
   }
}
