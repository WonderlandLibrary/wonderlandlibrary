package cn.hanabi.modules.modules.render;

import cn.hanabi.Client;
import cn.hanabi.events.EventText;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import java.util.Iterator;
import net.minecraft.client.network.NetworkPlayerInfo;
import org.apache.commons.lang3.StringUtils;

public class NameProtect extends Mod {
   public Value allPlayersValue = new Value("NameProtect", "AllPlayer", false);

   public NameProtect() {
      super("NameProtect", Category.RENDER);
   }

   @EventTarget
   public void onText(EventText event) {
      if (mc.thePlayer != null) {
         if (this.getState()) {
            event.setText(StringUtils.replace(event.getText(), mc.thePlayer.getName(), Client.username + "§f"));
            if ((Boolean)this.allPlayersValue.getValue()) {
               Iterator var2 = mc.getNetHandler().getPlayerInfoMap().iterator();

               while(var2.hasNext()) {
                  NetworkPlayerInfo playerInfo = (NetworkPlayerInfo)var2.next();
                  event.setText(StringUtils.replace(event.getText(), playerInfo.getGameProfile().getName(), "PROTECTION"));
               }
            }

         }
      }
   }
}
