package cn.hanabi.modules.modules.combat;

import cn.hanabi.events.EventRender;
import cn.hanabi.events.EventUpdate;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.TimeHelper;
import cn.hanabi.utils.pathfinder.PathUtils;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;

public class TPAura extends Mod {
   public final Value cps = new Value("TPAura", "CPS", 2.0, 1.0, 20.0, 1.0);
   public final Value range = new Value("TPAura", "Range", 30.0, 10.0, 100.0, 1.0);
   public final Value targets = new Value("TPAura", "Targets", 1.0, 1.0, 10.0, 1.0);
   public final Value swingValue = new Value("TPAura", "Swing", true);
   public final Value renderPathValue = new Value("TPAura", "Render Path", true);
   private final TimeHelper timer = new TimeHelper();
   private final ArrayList path = new ArrayList();
   private Thread thread = new Thread(() -> {
   });

   public TPAura() {
      super("TPAura", Category.COMBAT);
   }

   public void onEnable() {
      this.path.clear();
      this.timer.reset();
   }

   @EventTarget
   private void onUpdate(EventUpdate event) {
      if (this.timer.isDelayComplete(KillAura.randomClickDelay((double)((Double)this.cps.getValue()).intValue(), (double)(((Double)this.cps.getValue()).intValue() + 1))) && !this.thread.isAlive()) {
         this.thread = new Thread(() -> {
            int target = 0;
            this.path.clear();
            Iterator var2 = mc.theWorld.loadedEntityList.iterator();

            while(var2.hasNext()) {
               Entity entity = (Entity)var2.next();
               if (KillAura.isValidEntityType(entity) && (double)mc.thePlayer.getDistanceToEntity(entity) < (Double)this.range.getValue()) {
                  ++target;
                  this.doTPHit((EntityLivingBase)entity);
                  if ((double)target >= (Double)this.targets.getValue()) {
                     break;
                  }
               }
            }

         });
         this.thread.start();
         this.timer.reset();
      }
   }

   private void doTPHit(EntityLivingBase entity) {
      List tpPath = PathUtils.computePath((EntityLivingBase)mc.thePlayer, (EntityLivingBase)entity);
      tpPath.forEach((vec3) -> {
         this.path.add(vec3);
         mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(vec3.getX(), vec3.getY(), vec3.getZ(), true));
      });
      mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(entity, Action.ATTACK));
      if ((Boolean)this.swingValue.getValue()) {
         mc.thePlayer.swingItem();
      } else {
         mc.getNetHandler().addToSendQueue(new C0APacketAnimation());
      }

      List tpPath = Lists.reverse(tpPath);
      tpPath.forEach((vec3) -> {
         mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(vec3.getX(), vec3.getY(), vec3.getZ(), true));
      });
   }

   @EventTarget
   public void onRender(EventRender event) {
   }
}
