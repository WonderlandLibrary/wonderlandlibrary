package cn.hanabi.modules.modules.combat;

import aLph4anTi1eaK_cN.Annotation.ObfuscationClass;
import cn.hanabi.events.EventAttack;
import cn.hanabi.events.EventPacket;
import cn.hanabi.events.EventPreMotion;
import cn.hanabi.events.EventStep;
import cn.hanabi.events.EventUpdate;
import cn.hanabi.events.EventWorldChange;
import cn.hanabi.injection.interfaces.IC03PacketPlayer;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.modules.ModManager;
import cn.hanabi.utils.BlockUtils;
import cn.hanabi.utils.MoveUtils;
import cn.hanabi.utils.PlayerUtil;
import cn.hanabi.utils.TimeHelper;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;

@ObfuscationClass
public class Criticals extends Mod {
   public static boolean isReadyToCritical = false;
   public static Value modes = (new Value("Criticals", "Mode", 0)).LoadValue(new String[]{"Packet", "AACv4", "NoGround", "Jump"});
   public static Value pmode = (new Value("Criticals", "Packet Mode", 0)).LoadValue(new String[]{"Minus", "Drop", "Offest", "Old", "Hover", "Rise", "Abuse1", "Abuse2"});
   public static Value hurttime = new Value("Criticals", "Hurt Time", 15.0, 1.0, 20.0, 1.0);
   public static Value delay = new Value("Criticals", "Delay", 100.0, 50.0, 800.0, 10.0);
   public static Value steptick = new Value("Criticals", "Step Timer", 100.0, 50.0, 500.0, 10.0);
   public static Value editv = new Value("Criticals", "Edit Value", 0.0625, -0.0725, 0.0725, 0.0025);
   public static Random random = new Random();
   public static Value noti = new Value("Criticals", "Notification", false);
   public static Value prefall = new Value("Criticals", "Pre-FDistance", false);
   public static Value keep = new Value("Criticals", "Keep Packet", false);
   static TimeHelper stepTimer = new TimeHelper();
   static TimeHelper critTimer = new TimeHelper();
   static double[] y1 = new double[]{0.104080378093037, 0.105454222033912, 0.102888018147468, 0.099634532004642};

   public Criticals() {
      super("Criticals", Category.COMBAT);
   }

   public static void doCrit() {
      boolean toggled = ModManager.getModule("Criticals").isEnabled();
      boolean notoggled = !ModManager.getModule("Fly").isEnabled() && !ModManager.getModule("Scaffold").isEnabled();
      double[] packet = new double[]{0.051 * y1[(new Random()).nextInt(y1.length)] + ThreadLocalRandom.current().nextDouble(0.005), (!MoveUtils.isMoving() ? 0.001 : ThreadLocalRandom.current().nextDouble(3.0E-4, 5.0E-4)) + ThreadLocalRandom.current().nextDouble(1.0E-4), 0.031 * y1[(new Random()).nextInt(y1.length)] + ThreadLocalRandom.current().nextDouble(0.001), (!MoveUtils.isMoving() ? 0.008 : ThreadLocalRandom.current().nextDouble(1.0E-4, 7.0E-4)) + ThreadLocalRandom.current().nextDouble(1.0E-4)};
      double[] hover = new double[]{-0.0091165721 * y1[(new Random()).nextInt(y1.length)] * 10.0, 0.0679999 + (double)mc.thePlayer.ticksExisted % 0.0215, 0.0176063469198817 * y1[(new Random()).nextInt(y1.length)] * 10.0};
      double[] edit = new double[]{0.0, 0.075 + ThreadLocalRandom.current().nextDouble(0.008) * ((new Random()).nextBoolean() ? 0.98 : 0.99) + (double)mc.thePlayer.ticksExisted % 0.0215 * 0.94, ((new Random()).nextBoolean() ? 0.01063469198817 : 0.013999999) * ((new Random()).nextBoolean() ? 0.98 : 0.99) * y1[(new Random()).nextInt(y1.length)] * 10.0};
      double[] test = new double[]{0.06 + ThreadLocalRandom.current().nextDouble(1.0E-4), (0.06 + ThreadLocalRandom.current().nextDouble(1.0E-4)) / 2.0, (0.06 + ThreadLocalRandom.current().nextDouble(1.0E-4)) / 4.0, ThreadLocalRandom.current().nextDouble(1.5E-4, 1.63166800276E-4)};
      double[] old = new double[]{0.0, -0.0075, (MoveUtils.isMoving() ? 4.5E-4 : 0.0055) + ThreadLocalRandom.current().nextDouble(1.0E-4)};
      double[] offest = new double[]{-ThreadLocalRandom.current().nextDouble(1.5E-4, 1.63166800276E-4), 0.011 * y1[(new Random()).nextInt(y1.length)] + ThreadLocalRandom.current().nextDouble(0.005), 0.001 + ThreadLocalRandom.current().nextDouble(1.0E-4), 1.0E-4, (!MoveUtils.isMoving() ? 0.002 : ThreadLocalRandom.current().nextDouble(5.0E-4, 8.0E-4)) + ThreadLocalRandom.current().nextDouble(1.0E-5)};
      double[] morgan = new double[]{0.00124 + ThreadLocalRandom.current().nextDouble(1.0E-4, 9.0E-4), MoveUtils.isMoving() ? 8.5E-4 : 0.005 + ThreadLocalRandom.current().nextDouble(1.0E-4)};
      double[] morganfork = new double[]{0.012 + ThreadLocalRandom.current().nextDouble(1.0E-4, 9.0E-4), (MoveUtils.isMoving() ? 8.5E-4 : 0.005) + ThreadLocalRandom.current().nextDouble(0.001)};
      if ((Boolean)keep.getValue()) {
         double i1 = 0.0319 * y1[(new Random()).nextInt(y1.length)] + ThreadLocalRandom.current().nextDouble(0.005);
         double i2 = (!MoveUtils.isMoving() ? 0.008 : ThreadLocalRandom.current().nextDouble(1.0E-4, 7.0E-4)) + ThreadLocalRandom.current().nextDouble(1.0E-4);
         packet = new double[]{i1, i2, i1, i2};
      }

      if (toggled) {
         isReadyToCritical = !isReadyToCritical && (double)KillAura.target.hurtResistantTime <= (Double)hurttime.getValue() && mc.thePlayer.isCollidedVertically && mc.thePlayer.onGround && !BlockUtils.isInLiquid() && notoggled;
         if (!critTimer.isDelayComplete((Double)delay.getValueState()) || !stepTimer.isDelayComplete((Double)steptick.getValue()) && isReadyToCritical) {
            isReadyToCritical = false;
         }

         if (isReadyToCritical) {
            EntityPlayerSP p = mc.thePlayer;
            double[] i = null;
            if (modes.isCurrentMode("Packet")) {
               switch (pmode.getModeAt(pmode.getCurrentMode())) {
                  case "Minus":
                     i = packet;
                     break;
                  case "Hover":
                     i = hover;
                     break;
                  case "Rise":
                     i = edit;
                     break;
                  case "Drop":
                     i = test;
                     break;
                  case "Old":
                     i = old;
                     break;
                  case "Offest":
                     i = offest;
                     break;
                  case "Abuse1":
                     i = morgan;
                     break;
                  case "Abuse2":
                     i = morganfork;
               }
            }

            if (i != null) {
               if (MoveUtils.isOnGround(-1.0)) {
                  mc.thePlayer.jump();
               } else {
                  if ((Boolean)prefall.getValue()) {
                     mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(p.posX, p.posY + (Double)editv.getValue(), p.posZ, true));
                  }

                  double[] var19 = i;
                  var13 = i.length;

                  for(int var14 = 0; var14 < var13; ++var14) {
                     double offset = var19[var14];
                     mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(p.posX, p.posY + offset, p.posZ, false));
                  }
               }
            }

            if ((Boolean)noti.getValue()) {
               PlayerUtil.tellPlayer("Crit: " + randomNumber(-9999, 9999));
            }

            critTimer.reset();
         }
      }

   }

   private static int randomNumber(int max, int min) {
      return (int)(Math.random() * (double)(max - min)) + min;
   }

   @EventTarget
   public void onStep(EventStep e) {
      isReadyToCritical = false;
      if (e.getEventType() == EventType.POST) {
         stepTimer.reset();
      }

   }

   @EventTarget
   public void onChangeWorld(EventWorldChange e) {
      stepTimer.reset();
   }

   @EventTarget
   public void onPre(EventPreMotion e) {
      if (modes.isCurrentMode("Packet") && BlockUtils.isReallyOnGround() && mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ)).getBlock().isFullBlock() && !BlockUtils.isInLiquid() && ModManager.getModule("Speed").isEnabled() && ModManager.getModule("KillAura").isEnabled()) {
         EntityLivingBase entity = KillAura.target;
         int var3 = entity.hurtResistantTime;
      }

   }

   public void onEnable() {
   }

   @EventTarget
   public void onUpdate(EventUpdate e) {
      this.setDisplayName(modes.getModeAt(modes.getCurrentMode()));
   }

   @EventTarget
   public void onPacket(EventPacket event) {
      Packet packet = event.getPacket();
      if (packet instanceof C03PacketPlayer) {
         IC03PacketPlayer packet1 = (IC03PacketPlayer)packet;
         if (modes.isCurrentMode("NoGround")) {
            packet1.setOnGround(false);
         }
      }

   }

   @EventTarget
   public void onAttack(EventAttack event) {
      if (mc.thePlayer.onGround && !mc.thePlayer.isOnLadder() && !mc.thePlayer.isInWater() && !mc.thePlayer.isInLava() && event.entity instanceof EntityLivingBase && mc.thePlayer.ridingEntity == null) {
         double x = mc.thePlayer.posX;
         double y = mc.thePlayer.posY;
         double z = mc.thePlayer.posZ;
         if (modes.isCurrentMode("Jump")) {
            mc.thePlayer.jump();
         }

         if (modes.isCurrentMode("AACv4")) {
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 3.6E-15, z, false));
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
         }

      }
   }
}
