package cn.hanabi.modules.modules.player;

import aLph4anTi1eaK_cN.Annotation.ObfuscationClass;
import cn.hanabi.events.EventPreMotion;
import cn.hanabi.events.EventRender;
import cn.hanabi.injection.interfaces.IRenderManager;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.PlayerUtil;
import cn.hanabi.utils.RenderUtil;
import cn.hanabi.utils.WorldUtil;
import cn.hanabi.utils.rotation.RotationUtil;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemSword;
import net.minecraft.util.Vec3;

@ObfuscationClass
public class AimBot extends Mod {
   public static Value players = new Value("AimBot", "Players", false);
   public static Value headshot = new Value("AimBot", "Only Head", false);
   public static Value range = new Value("AimBot", "Range", 100.0, 1.0, 500.0, 5.0);
   public static Value deviation = new Value("AimBot", "Pre-Attack", 1.5, 0.0, 10.0, 0.1);
   private Vec3 aimed;

   public AimBot() {
      super("AimBot", Category.PLAYER);
   }

   @EventTarget(3)
   private void onUpdatePre(EventPreMotion event) {
      List targets = (List)WorldUtil.getLivingEntities().stream().filter(this::isValid).sorted(Comparator.comparing((e) -> {
         return mc.thePlayer.getDistanceToEntity(e);
      })).collect(Collectors.toList());
      List list = new ArrayList();
      list.addAll((Collection)targets.stream().filter((entity) -> {
         return entity instanceof EntityGiantZombie || entity instanceof EntityWither;
      }).collect(Collectors.toList()));
      list.addAll((Collection)targets.stream().filter((entity) -> {
         return !(entity instanceof EntityGiantZombie) && !(entity instanceof EntityWither);
      }).collect(Collectors.toList()));
      if (list.size() > 0) {
         this.aimed = this.getFixedLocation((EntityLivingBase)list.get(0), ((Double)deviation.getValue()).floatValue(), (Boolean)headshot.getValue());
         float[] rotations = RotationUtil.getRotationToLocation(this.aimed);
         event.setYaw(rotations[0]);
         event.setPitch(rotations[1]);
         mc.thePlayer.rotationYawHead = rotations[0];
         if (!(mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) && !(mc.thePlayer.getHeldItem().getItem() instanceof ItemBook)) {
            mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
         }

      }
   }

   @EventTarget
   private void onRender3D(EventRender event) {
      if (this.aimed != null) {
         double posX = this.aimed.xCoord - ((IRenderManager)mc.getRenderManager()).getRenderPosX();
         double posY = this.aimed.yCoord - ((IRenderManager)mc.getRenderManager()).getRenderPosY();
         double posZ = this.aimed.zCoord - ((IRenderManager)mc.getRenderManager()).getRenderPosZ();
         RenderUtil.drawBlockESP(posX - 0.5, posY - 0.5, posZ - 0.5, (new Color(255, 0, 0, 100)).getRGB(), (new Color(16771328)).getRGB(), 0.4F, 0.1F);
      }
   }

   private Vec3 getFixedLocation(EntityLivingBase entity, float velocity, boolean head) {
      double x = entity.posX + (entity.posX - entity.lastTickPosX) * (double)velocity;
      double y = entity.posY + (entity.posY - entity.lastTickPosY) * (double)velocity * 0.3 + (head ? (double)entity.getEyeHeight() : 1.0);
      double z = entity.posZ + (entity.posZ - entity.lastTickPosZ) * (double)velocity;
      return new Vec3(x, y, z);
   }

   private boolean isValid(EntityLivingBase entity) {
      if (entity instanceof EntityZombie || entity instanceof EntitySilverfish || entity instanceof EntityWither || entity instanceof EntityGhast || entity instanceof EntitySpider || entity instanceof EntityGiantZombie || entity instanceof EntitySkeleton || entity instanceof EntityGolem || entity instanceof EntityEndermite || entity instanceof EntityWitch || entity instanceof EntityBlaze || entity instanceof EntitySlime || entity instanceof EntityCreeper || entity instanceof EntityWolf || entity instanceof EntityPlayer && (Boolean)players.getValue()) {
         if (!entity.isDead && !(entity.getHealth() <= 0.0F)) {
            return (double)mc.thePlayer.getDistanceToEntity(entity) > (Double)range.getValue() ? false : PlayerUtil.canEntityBeSeenFixed(entity);
         } else {
            return false;
         }
      } else {
         return false;
      }
   }
}
