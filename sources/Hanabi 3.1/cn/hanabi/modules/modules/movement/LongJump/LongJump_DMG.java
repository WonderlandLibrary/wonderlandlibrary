package cn.hanabi.modules.modules.movement.LongJump;

import cn.hanabi.events.EventMove;
import cn.hanabi.events.EventPreMotion;
import cn.hanabi.modules.ModManager;
import cn.hanabi.utils.MoveUtils;
import cn.hanabi.utils.PlayerUtil;
import cn.hanabi.utils.TimeHelper;
import cn.hanabi.utils.random.Random;
import cn.hanabi.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;

public class LongJump_DMG {
   final Minecraft mc = Minecraft.getMinecraft();
   private int stage;
   private double speed;
   private double verticalSpeed;
   static TimeHelper timer = new TimeHelper();
   private static final Value flag = new Value("LongJump", "Flags", false);
   private static final Value uhc = new Value("LongJump", "Extra DMG", false);
   public static final Value height = new Value("LongJump", "Jump Height", 1.0, 0.5, 1.4, 0.1);

   public void onPre(EventPreMotion e) {
   }

   public void onMove(EventMove e) {
      if (MoveUtils.isOnGround(0.01) || this.stage > 0) {
         switch (this.stage) {
            case 0:
               this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.004 * Math.random(), this.mc.thePlayer.posZ);
               if ((Boolean)flag.getValue()) {
                  damage2();
               } else {
                  fallDistDamage();
               }

               this.verticalSpeed = PlayerUtil.getBaseJumpHeight() * (Double)height.getValue();
               this.speed = MoveUtils.getBaseMoveSpeed(0.2877, 0.2) * 2.14;
               break;
            case 1:
               this.speed *= 0.77;
               break;
            default:
               this.speed *= 0.98;
         }

         e.setY(this.verticalSpeed);
         if (this.stage > 8) {
            this.verticalSpeed -= 0.032;
         } else {
            this.verticalSpeed *= 0.87;
         }

         ++this.stage;
         if (MoveUtils.isOnGround(0.01) && this.stage > 4) {
            ModManager.getModule("LongJump").set(false);
         }

         MoveUtils.setMotion(e, Math.max(MoveUtils.getBaseMoveSpeed(0.2877, 0.1), this.speed));
      }

   }

   public void onEnable() {
      this.stage = 0;
   }

   public void onDisable() {
      this.stage = 0;
   }

   public static void fallDistDamage() {
      double randomOffset = Math.random() * 3.000000142492354E-4;
      double jumpHeight = 0.0625 - randomOffset;
      int packets = (int)(getMinFallDist() / (jumpHeight - randomOffset) + 1.0);

      for(int i = 0; i < packets; ++i) {
         Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY + jumpHeight, Minecraft.getMinecraft().thePlayer.posZ, false));
         Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY + randomOffset, Minecraft.getMinecraft().thePlayer.posZ, false));
      }

      Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY, Minecraft.getMinecraft().thePlayer.posZ, true));
      timer.reset();
   }

   public static void damage2() {
      double packets = Math.ceil(getMinFallDist() / 0.0625);
      double random = Random.nextDouble(0.00101001, 0.00607009);

      for(int i = 0; (double)i < packets; ++i) {
         Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY + 0.0625 + random, Minecraft.getMinecraft().thePlayer.posZ, false));
         Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY + random, Minecraft.getMinecraft().thePlayer.posZ, false));
      }

      Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer(true));
      timer.reset();
   }

   public static double getMotionY() {
      double mY = 0.41999998688697815;
      if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.jump)) {
         mY += (double)(Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1;
      }

      return mY;
   }

   public static double getMinFallDist() {
      double baseFallDist = (Boolean)uhc.getValue() ? 4.0 : 3.0;
      if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.jump)) {
         baseFallDist += (double)((float)Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1.0F);
      }

      return baseFallDist;
   }
}
