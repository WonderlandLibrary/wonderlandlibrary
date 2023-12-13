package cn.hanabi.modules.modules.world;

import cn.hanabi.events.EventUpdate;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.AbuseUtil;
import cn.hanabi.utils.TimeHelper;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import java.util.List;

public class AutoL extends Mod {
   public static final Value ad = new Value("AutoL", "AD", true);
   public static final Value wdr = new Value("AutoL", "AutoReport", true);
   public static final Value clientname = new Value("AutoL", "ClientName", true);
   public static final Value abuse = new Value("AutoL", "Abuse", false);
   public static TimeHelper LTimer = new TimeHelper();
   public static List wdred = new ArrayList();
   public static List power = new ArrayList();

   public AutoL() {
      super("AutoL", Category.WORLD);
   }

   public static String getAutoLMessage(String PlayerName) {
      String abuse = AbuseUtil.getAbuseGlobal();
      return "/ac " + ((Boolean)clientname.getValueState() ? "[Hanabi] " : "") + PlayerName + " L" + ((Boolean)AutoL.abuse.getValueState() ? " " + abuse : "");
   }

   @EventTarget
   public void onUpdate(EventUpdate e) {
      this.setDisplayName("English");
   }
}
