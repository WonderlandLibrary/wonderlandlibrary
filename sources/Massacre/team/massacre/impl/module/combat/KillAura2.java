package team.massacre.impl.module.combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.apache.commons.lang3.RandomUtils;
import team.massacre.Massacre;
import team.massacre.api.event.api.annotations.Handler;
import team.massacre.api.module.Category;
import team.massacre.api.module.Module;
import team.massacre.api.property.Property;
import team.massacre.api.property.Representation;
import team.massacre.api.property.impl.DoubleProperty;
import team.massacre.api.property.impl.EnumProperty;
import team.massacre.api.property.impl.MultiSelectEnumProperty;
import team.massacre.impl.event.EventUpdate;
import team.massacre.impl.module.world.Scaffold2;
import team.massacre.utils.PacketUtil;
import team.massacre.utils.PlayerUtils;
import team.massacre.utils.RotationUtil;
import team.massacre.utils.TimerUtil;

public class KillAura2 extends Module {
   public EnumProperty<KillAura2.RotationsMode> rotationsMode;
   public EnumProperty<KillAura2.Mode> killauraMode;
   public EnumProperty<KillAura2.BlockMode> blockMode;
   public EnumProperty<KillAura2.SortingMode> sortingMode;
   public EnumProperty<KillAura2.EventMode> eventMode;
   public DoubleProperty maxCPS;
   public DoubleProperty minCPS;
   public DoubleProperty range;
   public DoubleProperty maxAngleChangeProperty;
   public MultiSelectEnumProperty<KillAura2.Targets> targetsMulti;
   public Property<Boolean> autoblock;
   public Property<Boolean> particles;
   public Property<Boolean> lockview;
   public Property<Boolean> noSwing;
   public Property<Boolean> autism;
   public List<EntityLivingBase> targets;
   private List<Packet> packets;
   public EntityLivingBase target;
   public int index;
   public int tick;
   public TimerUtil attackTimer;
   public boolean block;

   public KillAura2() {
      super("Kill Aura", 37, Category.COMBAT);
      this.rotationsMode = new EnumProperty("Rotations Mode", KillAura2.RotationsMode.Normal);
      this.killauraMode = new EnumProperty("KillAura Mode", KillAura2.Mode.Switch);
      this.blockMode = new EnumProperty("Autoblock Mode", KillAura2.BlockMode.NCP);
      this.sortingMode = new EnumProperty("Sorting Mode", KillAura2.SortingMode.Health);
      this.eventMode = new EnumProperty("Attack Event", KillAura2.EventMode.Post);
      this.maxCPS = new DoubleProperty("Maximum CPS", 13.0D, 1.0D, 20.0D, 1.0D);
      this.minCPS = new DoubleProperty("Minimum CPS", 9.0D, 1.0D, 20.0D, 1.0D);
      this.range = new DoubleProperty("Attack Range", 4.199999809265137D, 1.0D, 7.0D, 0.20000000298023224D, Representation.DISTANCE);
      this.maxAngleChangeProperty = new DoubleProperty("Max angle step", 45.0D, 1.0D, 201.25D, 0.25D, Representation.DEGREES);
      this.targetsMulti = new MultiSelectEnumProperty("Targets", new KillAura2.Targets[]{KillAura2.Targets.Players});
      this.autoblock = new Property("Autoblock", true);
      this.particles = new Property("Particles", false);
      this.lockview = new Property("Lock View", false);
      this.noSwing = new Property("No Swing", false);
      this.autism = new Property("Autism", true);
      this.targets = new ArrayList();
      this.packets = new ArrayList();
      this.attackTimer = new TimerUtil();
      this.addValues(new Property[]{this.killauraMode, this.sortingMode, this.blockMode, this.rotationsMode, this.eventMode, this.maxCPS, this.minCPS, this.range, this.maxAngleChangeProperty, this.autoblock, this.noSwing, this.autism, this.lockview, this.particles, this.targetsMulti});
   }

   public void onEnable() {
      super.onEnable();
      this.target = null;
      this.block = false;
      this.attackTimer.reset();
      this.tick = 100;
   }

   public void onDisable() {
      super.onDisable();
      if (this.block) {
         this.unblock();
         this.block = false;
      }

      this.target = null;
      this.attackTimer.reset();
   }

   public boolean hasSword() {
      return this.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && this.mc.thePlayer.getHeldItem() != null;
   }

   @Handler
   public void onMotionUpdate(EventUpdate event) {
      this.setSuffix(((KillAura2.Mode)this.killauraMode.getValue()).name() + ", " + ((KillAura2.BlockMode)this.blockMode.getValue()).name());
      Scaffold2 scaffold = (Scaffold2)Massacre.INSTANCE.getModuleManager().getModule(Scaffold2.class);
      if (scaffold == null || !scaffold.getState()) {
         if (this.block && (Boolean)this.autoblock.getValue()) {
            this.unblock();
            this.block = false;
         }

         this.getTargets();
         this.targetsSort();
         ++this.tick;
         if (this.index >= this.targets.size()) {
            this.index = 0;
         }

         this.target = !this.targets.isEmpty() && this.index < this.targets.size() ? (EntityLivingBase)this.targets.get(this.index) : null;
         if (event.isPre() && this.target != null && this.isValid(this.target)) {
            if (this.rotationsMode.getValue() == KillAura2.RotationsMode.Normal) {
               RotationUtil.rotate(event, RotationUtil.getRotationsToEntity(this.target), ((Double)this.maxAngleChangeProperty.getValue()).floatValue(), (Boolean)this.lockview.getValue());
            }

            if (this.eventMode.getValue() == KillAura2.EventMode.Pre && this.attackTimer.hasHit((float)(1000 / ThreadLocalRandom.current().nextInt(((Double)this.minCPS.getValue()).intValue(), ((Double)this.maxCPS.getValue()).intValue()))) && this.isValid(this.target)) {
               if ((Boolean)this.noSwing.getValue()) {
                  PacketUtil.sendPacketNoEvent(new C0APacketAnimation());
               } else {
                  this.mc.thePlayer.swingItem();
               }

               this.mc.thePlayer.onEnchantmentCritical(this.target);
               this.mc.thePlayer.onCriticalHit(this.target);
               this.tick = 0;
               this.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(this.target, C02PacketUseEntity.Action.ATTACK));
               this.attackTimer.reset();
            }
         } else if (event.isPost()) {
            if (this.attackTimer.hasHit((float)(1000 / ThreadLocalRandom.current().nextInt(((Double)this.minCPS.getValue()).intValue(), ((Double)this.maxCPS.getValue()).intValue()))) && this.eventMode.getValue() == KillAura2.EventMode.Post && this.isValid(this.target)) {
               if ((Boolean)this.noSwing.getValue()) {
                  PacketUtil.sendPacketNoEvent(new C0APacketAnimation());
               } else {
                  this.mc.thePlayer.swingItem();
               }

               this.mc.thePlayer.onEnchantmentCritical(this.target);
               this.mc.thePlayer.onCriticalHit(this.target);
               this.tick = 0;
               this.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(this.target, C02PacketUseEntity.Action.ATTACK));
               this.attackTimer.reset();
            }

            if (this.isValid(this.target) && this.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && this.mc.thePlayer.getHeldItem() != null && !this.block && (Boolean)this.autoblock.getValue() && this.target != null) {
               PacketUtil.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, this.mc.thePlayer.getHeldItem(), 0.0F, 0.0F, 0.0F));
               this.block = true;
            }
         }

      }
   }

   private void getTargets() {
      this.targets.clear();
      Iterator var1 = this.mc.theWorld.getLoadedEntityList().iterator();

      while(var1.hasNext()) {
         Entity ent = (Entity)var1.next();
         if (ent instanceof EntityLivingBase) {
            EntityLivingBase entLiving = (EntityLivingBase)ent;
            if (this.isValid(entLiving)) {
               this.targets.add(entLiving);
            }
         }
      }

   }

   public boolean isValid(EntityLivingBase entLiving) {
      return PlayerUtils.isValid(entLiving, this.targetsMulti.isSelected(KillAura2.Targets.Players), this.targetsMulti.isSelected(KillAura2.Targets.Mobs), this.targetsMulti.isSelected(KillAura2.Targets.Animals), this.targetsMulti.isSelected(KillAura2.Targets.Teamates), this.targetsMulti.isSelected(KillAura2.Targets.Invisibles), this.targetsMulti.isSelected(KillAura2.Targets.Passives), (double)((Double)this.range.getValue()).floatValue());
   }

   private void targetsSort() {
      switch((KillAura2.SortingMode)this.sortingMode.getValue()) {
      case Angle:
         this.targets.sort((o1, o2) -> {
            float[] rot1 = RotationUtil.getNeededRotations(o1);
            float[] rot2 = RotationUtil.getNeededRotations(o2);
            return (int)(this.mc.thePlayer.rotationYaw - rot1[0] - (this.mc.thePlayer.rotationYaw - rot2[0]));
         });
         break;
      case Health:
         this.targets.sort(Comparator.comparingDouble(EntityLivingBase::getHealth));
         break;
      case Range:
         this.targets.sort((o1, o2) -> {
            return (int)(o1.getDistanceToEntity(this.mc.thePlayer) - o2.getDistanceToEntity(this.mc.thePlayer));
         });
         break;
      case FOV:
         this.targets.sort(Comparator.comparingDouble((o) -> {
            return (double)RotationUtil.getDistanceBetweenAngles(this.mc.thePlayer.rotationPitch, RotationUtil.getNeededRotations(o)[0]);
         }));
      }

   }

   private void unblock() {
      switch((KillAura2.BlockMode)this.blockMode.getValue()) {
      case NCP:
         if (this.block && (Boolean)this.autoblock.getValue() && this.target != null) {
            PacketUtil.sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(Math.random() * ThreadLocalRandom.current().nextDouble(-100.0D, 100.0D), Math.random() * ThreadLocalRandom.current().nextDouble(-100.0D, 100.0D), Math.random() * ThreadLocalRandom.current().nextDouble(-100.0D, 100.0D)), EnumFacing.DOWN));
            this.block = false;
         }
         break;
      case Vanilla:
         if (this.block && (Boolean)this.autoblock.getValue() && this.target != null) {
            PacketUtil.sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            this.block = false;
         }
         break;
      case WatchdogTest:
         if (this.block && (Boolean)this.autoblock.getValue() && this.target != null) {
            this.mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(RandomUtils.nextDouble(Double.MIN_VALUE, Double.MAX_VALUE), RandomUtils.nextDouble(Double.MIN_VALUE, Double.MAX_VALUE), RandomUtils.nextDouble(Double.MIN_VALUE, Double.MAX_VALUE)), EnumFacing.DOWN));
            this.block = false;
         }
      }

   }

   private static enum BlockMode {
      NCP,
      Vanilla,
      WatchdogTest;
   }

   private static enum RotationsMode {
      Normal,
      Smooth,
      AAC,
      NCP;
   }

   private static enum SortingMode {
      Health,
      Range,
      Angle,
      FOV;
   }

   public static enum Targets {
      Players,
      Teamates,
      Mobs,
      Animals,
      Passives,
      Invisibles;
   }

   private static enum EventMode {
      Pre,
      Post;
   }

   private static enum Mode {
      Switch,
      Single,
      Multi;
   }
}
