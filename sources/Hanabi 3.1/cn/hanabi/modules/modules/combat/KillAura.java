package cn.hanabi.modules.modules.combat;

import aLph4anTi1eaK_cN.Annotation.ObfuscationClass;
import cn.hanabi.Hanabi;
import cn.hanabi.Wrapper;
import cn.hanabi.events.EventAttack;
import cn.hanabi.events.EventJump;
import cn.hanabi.events.EventPacket;
import cn.hanabi.events.EventPostMotion;
import cn.hanabi.events.EventPreMotion;
import cn.hanabi.events.EventRender;
import cn.hanabi.events.EventRender2D;
import cn.hanabi.events.EventStrafe;
import cn.hanabi.events.EventUpdate;
import cn.hanabi.events.EventWorldChange;
import cn.hanabi.gui.font.noway.ttfr.HFontRenderer;
import cn.hanabi.injection.interfaces.IEntityPlayer;
import cn.hanabi.injection.interfaces.IRenderManager;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.modules.ModManager;
import cn.hanabi.modules.modules.render.ClickGUIModule;
import cn.hanabi.modules.modules.world.AntiBot;
import cn.hanabi.modules.modules.world.AutoL;
import cn.hanabi.modules.modules.world.Teams;
import cn.hanabi.utils.AnimationUtil;
import cn.hanabi.utils.Colors;
import cn.hanabi.utils.FriendManager;
import cn.hanabi.utils.PaletteUtil;
import cn.hanabi.utils.PlayerUtil;
import cn.hanabi.utils.RenderUtil;
import cn.hanabi.utils.TargetManager;
import cn.hanabi.utils.TimeHelper;
import cn.hanabi.utils.rotation.Rotation;
import cn.hanabi.utils.rotation.RotationUtil;
import cn.hanabi.utils.rotation.VecRotation;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.yarukon.palette.ColorValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0BPacketEntityAction.Action;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldSettings.GameType;
import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;

@ObfuscationClass
public class KillAura extends Mod {
   public static Value autoBlock = new Value("KillAura", "AutoBlock", true);
   public static Value interact = new Value("KillAura", "Interact", false);
   public static Value dynamic = new Value("KillAura", "Dynamic-UnBlock", false);
   public static Value glich = new Value("KillAura", "Swing-Glich", false);
   public static Value reach = new Value("KillAura", "Range", 4.2, 3.0, 6.0, 0.1);
   public static Value switchsize = new Value("KillAura", "MaxTargets", 1.0, 1.0, 7.0, 1.0);
   public static List targets = new ArrayList();
   public static ArrayList attacked = new ArrayList();
   public static EntityLivingBase target = null;
   public static Value minCps = new Value("KillAura", "Min CPS", 6.0, 1.0, 20.0, 1.0);
   public static Value maxCps = new Value("KillAura", "Max CPS", 12.0, 1.0, 20.0, 1.0);
   public static Value blockReach = new Value("KillAura", "Block Range", 0.5, 0.0, 6.0, 0.1);
   public static Value attackPlayers = new Value("KillAura", "Players", true);
   public static Value attackAnimals = new Value("KillAura", "Animals", false);
   public static Value attackMobs = new Value("KillAura", "Mobs", false);
   public static Value throughblock = new Value("KillAura", "Through Block", true);
   public static Value invisible = new Value("KillAura", "Invisibles", false);
   public static Value fov = new Value("KillAura", "Fov Check", 360.0, 10.0, 360.0, 10.0);
   public static Value rotationStrafe = new Value("KillAura", "Rotation Strafe", false);
   public static Value yawxmin = new Value("KillAura", "Yaw X Min Offset", 0.0, -1.0, 1.0, 0.1);
   public static Value yawxmax = new Value("KillAura", "Yaw X Max Offset", 0.0, -1.0, 1.0, 0.1);
   public static Value yawzmin = new Value("KillAura", "Yaw Z Min Offset", 0.0, -1.0, 1.0, 0.1);
   public static Value yawzmax = new Value("KillAura", "Yaw Z Max Offset", 0.0, -1.0, 1.0, 0.1);
   public static Value pitchRand = new Value("KillAura", "Pitch Offset", 0.0, -1.0, 1.0, 0.1);
   public static Value hitbox = new Value("KillAura", "HitBox Expand", 0.1, 0.0, 0.5, 0.01);
   public static Value predict = new Value("KillAura", "Predict", false);
   public static Value resolverY = new Value("KillAura", "Y-Resolver", false);
   public static Value resolverXZ = new Value("KillAura", "Horizon-Resolver", true);
   public static Value xRandom = new Value("KillAura", "X Random", false);
   public static Value zRandom = new Value("KillAura", "Z Random", false);
   public static Value pitchRandom = new Value("KillAura", "Pitch Random", false);
   public static Value resolvers = new Value("KillAura", "Resolver Accuracy", 3.0, 1.0, 12.0, 1.0);
   public static Value predictor = new Value("KillAura", "Pre Attack Fov", 1.1, 1.0, 3.0, 0.01);
   public static Value smooth = new Value("KillAura", "Rotation Smooth", 0.0, 0.0, 100.0, 1.0);
   public static Value blockRate = new Value("KillAura", "Block Rate", 100.0, 10.0, 100.0, 5.0);
   public static Value kbBlock = new Value("KillAura", "ExtraKB When Hurt", false);
   public static Value hurtBlock = new Value("KillAura", "Block When Hurt", false);
   public static Random random = new Random();
   public static boolean isBlocking = false;
   public static EntityLivingBase needHitBot = null;
   public static Value namecheck = new Value("KillAura", "Check Name", true);
   public static Value reverse = new Value("KillAura", "Rotation Reverse Chance", 0.0, 0.0, 100.0, 5.0);
   public static Value outpoint = new Value("KillAura", "Rotation Mistake Chance", 0.0, 0.0, 100.0, 5.0);
   public static double animation = 0.0;
   public static Rotation serverRotation = new Rotation(0.0F, 0.0F);
   private final TimeHelper switchTimer = new TimeHelper();
   public Value switchDelay = new Value("KillAura", "Switch Delay", 50.0, 0.0, 2000.0, 10.0);
   public Value mistake = new Value("KillAura", "Mistakes", 0.0, 0.0, 20.0, 1.0);
   public Value autodisable = new Value("KillAura", "Auto Disable", true);
   public Value targetHUD = new Value("KillAura", "Show Target", true);
   public Value forcerise = new Value("KillAura", "Slient", true);
   public Value esp = new Value("KillAura", "ESP", true);
   public Value morekb = new Value("KillAura", "More KB", false);
   public Value pre = new Value("KillAura", "Pre Hit", false);
   public Value block = new Value("KillAura", "Pre Block", false);
   public Value aacbot = new Value("KillAura", "AAC Bot Check", false);
   public Value mult = new Value("KillAura", "Multi", false);
   public Value force = new Value("KillAura", "Force Update", false);
   public Value version = new Value("KillAura", "Attack Fix", false);
   public Value hitableCheck = new Value("KillAura", "Hitable Check", true);
   public Value canTurn = new Value("KillAura", "Rotate Head", true);
   public Value minTurn = new Value("KillAura", "Min Turn Head Speed", 60.0, 1.0, 180.0, 1.0);
   public Value maxTurn = new Value("KillAura", "Max Turn Head Speed", 60.0, 1.0, 180.0, 1.0);
   public Value witherPriority = new Value("KillAura", "Wither Priority", true);
   public Value blockMode = new Value("KillAura", "Block Mode", 0);
   public Value SensitivityMode = new Value("KillAura", "S-Fix Mode", 0);
   public Value EspMode = new Value("KillAura", "ESP Mode", 0);
   public Value hudMode = new Value("KillAura", "TargetHUD", 0);
   public Value priority = new Value("KillAura", "Priority", 1);
   public ColorValue boxColor = new ColorValue("Target ESP Color", 0.5F, 1.0F, 1.0F, 1.0F, false, false, 10.0F);
   public int index;
   public TimeHelper attacktimer = new TimeHelper();
   public TimeHelper rotationTimer = new TimeHelper();
   public DecimalFormat format = new DecimalFormat("0.0");
   float lastHealth = 0.0F;
   double delay = 0.0;
   boolean step = false;
   float[] lastRotations;
   double cps;
   boolean nulltarget = false;
   private double healthBarWidth;
   private double healthBarWidth2;
   private double hudHeight;
   public static int killCount = 0;

   public KillAura() {
      super("KillAura", Category.COMBAT);
      this.priority.LoadValue(new String[]{"Angle", "Range", "Armor", "Health", "Fov", "Hurt Time"});
      this.hudMode.LoadValue(new String[]{"Simple", "Fancy", "Flat", "Test"});
      this.EspMode.LoadValue(new String[]{"Box", "Circle", "New", "Cylinder", "ExeterCross"});
      this.blockMode.LoadValue(new String[]{"Simple", "Always", "Exploit"});
      this.SensitivityMode.LoadValue(new String[]{"None", "Normal", "Prefect"});
      attacked = new ArrayList();
   }

   public static double getRandomDoubleInRange(double minDouble, double maxDouble) {
      return minDouble >= maxDouble ? minDouble : (new Random()).nextDouble() * (maxDouble - minDouble) + minDouble;
   }

   public static double RandomFloat(float minFloat, float maxFloat) {
      return minFloat >= maxFloat ? (double)minFloat : (double)((new Random()).nextFloat() * (maxFloat - minFloat) + minFloat);
   }

   public static long randomClickDelay(double minCPS, double maxCPS) {
      return (long)(Math.random() * (1000.0 / minCPS - 1000.0 / maxCPS + 1.0) + 1000.0 / maxCPS);
   }

   private boolean isInMenu() {
      GuiScreen currentScreen = mc.currentScreen;
      return currentScreen != null;
   }

   public static boolean isValidEntityType(Entity entity) {
      if (entity instanceof EntityLivingBase && entity != mc.thePlayer && !mc.thePlayer.isDead && !(entity instanceof EntityArmorStand) && !(entity instanceof EntitySnowman)) {
         if (entity instanceof EntityPlayer && (Boolean)attackPlayers.getValueState()) {
            if (!mc.thePlayer.canEntityBeSeen(entity) && !(Boolean)throughblock.getValueState()) {
               return false;
            }

            if (entity.isInvisible() && !(Boolean)invisible.getValueState()) {
               return false;
            }

            return !AntiBot.isBot(entity) && !Teams.isOnSameTeam(entity);
         }

         if ((entity instanceof EntityMob || entity instanceof EntitySlime) && (Boolean)attackMobs.getValueState()) {
            return !AntiBot.isBot(entity);
         }

         if (entity instanceof EntityWither) {
            return !Teams.isOnSameTeam(entity);
         }

         if ((entity instanceof EntityAnimal || entity instanceof EntityVillager) && (Boolean)attackAnimals.getValueState()) {
            return !AntiBot.isBot(entity);
         }
      }

      return false;
   }

   public static boolean isValidEntity(Entity entity) {
      if (FriendManager.getFriends().size() > 0 && FriendManager.isFriend(entity.getName())) {
         return false;
      } else if (!KillAura.AimUtil.isVisibleFOV(entity, ((Double)fov.getValue()).floatValue())) {
         return false;
      } else {
         if (entity instanceof EntityLivingBase) {
            if (entity.isDead || ((EntityLivingBase)entity).getHealth() <= 0.0F) {
               if (attacked.contains(entity)) {
                  ++killCount;
                  if (ModManager.getModule("AutoL").isEnabled()) {
                     if ((Boolean)AutoL.wdr.getValueState() && !AutoL.wdred.contains(target.getName())) {
                        AutoL.wdred.add(target.getName());
                        mc.thePlayer.sendChatMessage("/wdr " + target.getName() + " ka fly reach nokb jesus ac");
                     }

                     mc.thePlayer.sendChatMessage(AutoL.getAutoLMessage(entity.getName()));
                  }

                  attacked.remove(entity);
               }

               return false;
            }

            if ((double)mc.thePlayer.getDistanceToEntity(entity) < (Double)reach.getValueState() + (Double)blockReach.getValueState() && entity != mc.thePlayer && !mc.thePlayer.isDead && !(entity instanceof EntityArmorStand) && !(entity instanceof EntitySnowman)) {
               if (entity instanceof EntityPlayer && (Boolean)attackPlayers.getValueState()) {
                  if (!mc.thePlayer.canEntityBeSeen(entity) && !(Boolean)throughblock.getValueState()) {
                     return false;
                  }

                  if (entity.isInvisible() && !(Boolean)invisible.getValueState()) {
                     return false;
                  }

                  return !AntiBot.isBot(entity) && !Teams.isOnSameTeam(entity);
               }

               if ((entity instanceof EntityMob || entity instanceof EntitySlime) && (Boolean)attackMobs.getValueState()) {
                  if (entity instanceof EntityWither) {
                     return !Teams.isOnSameTeam(entity);
                  }

                  if (entity.getName().contains(mc.thePlayer.getName()) && (Boolean)namecheck.getValue()) {
                     return false;
                  }

                  return !AntiBot.isBot(entity);
               }

               if ((entity instanceof EntityAnimal || entity instanceof EntityVillager) && (Boolean)attackAnimals.getValueState()) {
                  if (entity.getName().contains(mc.thePlayer.getName()) && (Boolean)namecheck.getValue()) {
                     return false;
                  }

                  return !AntiBot.isBot(entity);
               }
            }
         }

         return false;
      }
   }

   public static float getDistanceBetweenAngles(float angle1, float angle2) {
      float angle3 = Math.abs(angle1 - angle2) % 360.0F;
      if (angle3 > 180.0F) {
         angle3 = 0.0F;
      }

      return angle3;
   }

   public static Color getHealthColor(float health, float maxHealth) {
      float[] fractions = new float[]{0.0F, 0.5F, 1.0F};
      Color[] colors = new Color[]{new Color(0, 81, 179), new Color(0, 153, 255), new Color(47, 154, 241)};
      float progress = health / maxHealth;
      return blendColors(fractions, colors, progress).brighter();
   }

   public static int[] getFractionIndices(float[] fractions, float progress) {
      int[] range = new int[2];

      int startPoint;
      for(startPoint = 0; startPoint < fractions.length && fractions[startPoint] <= progress; ++startPoint) {
      }

      if (startPoint >= fractions.length) {
         startPoint = fractions.length - 1;
      }

      range[0] = startPoint - 1;
      range[1] = startPoint;
      return range;
   }

   public static Color blendColors(float[] fractions, Color[] colors, float progress) {
      if (fractions.length == colors.length) {
         int[] indices = getFractionIndices(fractions, progress);
         float[] range = new float[]{fractions[indices[0]], fractions[indices[1]]};
         Color[] colorRange = new Color[]{colors[indices[0]], colors[indices[1]]};
         float max = range[1] - range[0];
         float value = progress - range[0];
         float weight = value / max;
         return blend(colorRange[0], colorRange[1], (double)(1.0F - weight));
      } else {
         throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
      }
   }

   public static Color blend(Color color1, Color color2, double ratio) {
      float r = (float)ratio;
      float ir = 1.0F - r;
      float[] rgb1 = new float[3];
      float[] rgb2 = new float[3];
      color1.getColorComponents(rgb1);
      color2.getColorComponents(rgb2);
      return new Color(rgb1[0] * r + rgb2[0] * ir, rgb1[1] * r + rgb2[1] * ir, rgb1[2] * r + rgb2[2] * ir);
   }

   @EventTarget
   public void onReload(EventWorldChange e) {
      if ((Boolean)this.autodisable.getValueState()) {
         this.set(false);
      }

   }

   @EventTarget
   private void onStrafe(EventStrafe event) {
      if ((Boolean)rotationStrafe.getValueState()) {
         event.yaw = this.lastRotations[0];
      }

   }

   @EventTarget
   private void onJump(EventJump event) {
      if ((Boolean)rotationStrafe.getValueState()) {
         event.yaw = this.lastRotations[0];
      }

   }

   private void update() {
      if (!targets.isEmpty() && this.index >= targets.size()) {
         this.index = 0;
      }

      Iterator var1 = targets.iterator();

      EntityLivingBase ent;
      while(var1.hasNext()) {
         ent = (EntityLivingBase)var1.next();
         if (!isValidEntity(ent)) {
            targets.remove(ent);
         }
      }

      this.getTarget();
      if (targets.size() == 0) {
         target = null;
      } else {
         target = (EntityLivingBase)targets.get(this.index);
         if ((double)mc.thePlayer.getDistanceToEntity(target) > (Double)reach.getValueState()) {
            target = (EntityLivingBase)targets.get(0);
         } else if (TargetManager.getTarget().size() > 0) {
            var1 = targets.iterator();

            while(var1.hasNext()) {
               ent = (EntityLivingBase)var1.next();

               for(int i = 0; i < TargetManager.getTarget().size(); ++i) {
                  if (ent.getName().contains((String)TargetManager.getTarget().get(i))) {
                     targets.removeIf((entity) -> {
                        return !TargetManager.isTarget(entity.getName());
                     });
                  }
               }
            }
         }
      }

   }

   @EventTarget
   private void onUpdate(EventUpdate event) {
      if ((Boolean)rotationStrafe.getValue()) {
         this.update();
         needHitBot = null;
         if (target != null) {
            if (this.switchTimer.isDelayComplete((Double)this.switchDelay.getValueState()) && targets.size() > 1) {
               this.switchTimer.reset();
               ++this.index;
            }

            float[] realLastRot = this.lastRotations;
            this.rotation(realLastRot);
         } else {
            targets.clear();
            if (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && (Boolean)autoBlock.getValueState() && isBlocking) {
               this.unBlock(true);
            }

            this.lastRotations = new float[]{mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch};
         }

      }
   }

   @EventTarget
   public void onPre(EventPreMotion event) {
      boolean toggled = ModManager.getModule("Scaffold").isEnabled();
      if (this.blockMode.isCurrentMode("Always") && mc.thePlayer.getItemInUseCount() == 0) {
         isBlocking = false;
      }

      if (!(Boolean)rotationStrafe.getValue()) {
         this.update();
         needHitBot = null;
         if (target != null) {
            if (this.switchTimer.isDelayComplete((Double)this.switchDelay.getValueState()) && targets.size() > 1) {
               this.switchTimer.reset();
               ++this.index;
            }

            float[] realLastRot = this.lastRotations;
            this.rotation(realLastRot);
            if (target != null && (Boolean)this.canTurn.getValue()) {
               if (toggled) {
                  return;
               }

               if (!(Boolean)this.forcerise.getValueState()) {
                  mc.thePlayer.rotationYaw = this.lastRotations[0];
                  mc.thePlayer.rotationPitch = this.lastRotations[1];
               } else {
                  event.setYaw(this.lastRotations[0]);
                  event.setPitch(this.lastRotations[1]);
                  if ((Boolean)this.force.getValue()) {
                     mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(event.getX(), event.getY(), event.getZ(), event.getYaw(), event.getPitch(), event.isOnGround()));
                     event.setCancel(true);
                  }
               }

               if (mc.gameSettings.thirdPersonView != 0 && (!(Boolean)this.mult.getValue() || targets.size() <= 1)) {
                  mc.thePlayer.rotationYawHead = event.getYaw();
                  mc.thePlayer.renderYawOffset = event.getYaw();
               }
            }
         } else {
            targets.clear();
            if (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && (Boolean)autoBlock.getValueState() && isBlocking) {
               this.unBlock(true);
            }

            this.lastRotations = new float[]{mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch};
         }
      } else if (target != null && (Boolean)this.canTurn.getValue()) {
         if (toggled) {
            return;
         }

         if (!(Boolean)this.forcerise.getValueState()) {
            mc.thePlayer.rotationYaw = this.lastRotations[0];
            mc.thePlayer.rotationPitch = this.lastRotations[1];
         } else {
            event.setYaw(this.lastRotations[0]);
            event.setPitch(this.lastRotations[1]);
            if ((Boolean)this.force.getValue()) {
               mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(event.getX(), event.getY(), event.getZ(), event.getYaw(), event.getPitch(), event.isOnGround()));
               event.setCancel(true);
            }
         }

         if (mc.gameSettings.thirdPersonView != 0 && (!(Boolean)this.mult.getValue() || targets.size() <= 1)) {
            mc.thePlayer.rotationYawHead = event.getYaw();
            mc.thePlayer.renderYawOffset = event.getYaw();
         }
      }

      if ((Boolean)this.pre.getValue() && target != null) {
         while(this.cps > 0.0) {
            this.doAttack();
            --this.cps;
         }
      }

   }

   @EventTarget
   public void onPost(EventPostMotion event) {
      boolean toggled = ModManager.getModule("Scaffold").isEnabled();
      if (!(Boolean)this.pre.getValue() && target != null) {
         while(this.cps > 0.0) {
            this.doAttack();
            --this.cps;
         }
      }

   }

   private void doAttack() {
      if (target != null) {
         boolean isInRange = (double)mc.thePlayer.getDistanceToEntity(target) <= (Double)reach.getValueState();
         boolean hitable = !(Boolean)this.canTurn.getValue() || !(Boolean)this.hitableCheck.getValue();
         if (!hitable) {
            hitable = RotationUtil.isFaced(target, (Double)reach.getValueState(), RotationUtil.convert(this.lastRotations));
         }

         if (!hitable) {
            return;
         }

         if ((this.blockMode.isCurrentMode("Exploit") || this.blockMode.isCurrentMode("Simple") && PlayerUtil.isMoving2()) && (mc.thePlayer.isBlocking() || mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && (Boolean)autoBlock.getValueState() && isBlocking)) {
            this.unBlock(!mc.thePlayer.isBlocking() && !(Boolean)autoBlock.getValueState() && mc.thePlayer.getItemInUseCount() > 0);
         }

         if (isInRange) {
            if (random.nextInt(100) < ((Double)this.mistake.getValueState()).intValue()) {
               mc.getNetHandler().getNetworkManager().sendPacket(new C0APacketAnimation());
            } else {
               this.attack();
            }
         }

         if (target != null && (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && (Boolean)autoBlock.getValueState() || mc.thePlayer.isBlocking()) && !isBlocking && (double)(new Random()).nextInt(100) <= (Double)blockRate.getValue() && (!(Boolean)hurtBlock.getValue() || mc.thePlayer.hurtResistantTime > 0) && !this.blockMode.isCurrentMode("Vanilla")) {
            this.doBlock(true);
         }
      }

   }

   public void rotation(float[] realLastRot) {
      serverRotation.setYaw(this.lastRotations[0]);
      serverRotation.setPitch(this.lastRotations[1]);
      if (this.rotationTimer.isDelayComplete((100.0 - (Double)smooth.getValueState()) * (double)RandomUtils.nextFloat(2.6F, 3.1F))) {
         this.rotationTimer.reset();
         this.lastRotations = RotationUtil.getNeededRotations(KillAura.AimUtil.getLocation(((EntityLivingBase)targets.get(this.index)).getEntityBoundingBox().expand((Double)hitbox.getValue(), (Double)hitbox.getValue(), (Double)hitbox.getValue())), new Vec3(0.0, 0.0, 0.0));
         double minTurnSpeed = Math.min((Double)this.maxTurn.getValue(), (Double)this.minTurn.getValue());
         double maxTurnSpeed = Math.max((Double)this.maxTurn.getValue(), (Double)this.minTurn.getValue());
         if (!(Boolean)this.force.getValue()) {
            this.lastRotations = RotationUtil.convertBack(RotationUtil.limitAngleChange(RotationUtil.convert(realLastRot), RotationUtil.convert(this.lastRotations), (float)(Math.random() * (maxTurnSpeed - minTurnSpeed) + minTurnSpeed)));
         }

         switch (this.SensitivityMode.getModeAt(this.SensitivityMode.getCurrentMode())) {
            case "Normal":
               this.lastRotations = KillAura.AimUtil.NormalFix(this.lastRotations);
               break;
            case "Prefect":
               this.lastRotations = KillAura.AimUtil.PrefectFix(this.lastRotations);
         }
      }

   }

   private void attack() {
      ArrayList list = new ArrayList();
      Iterator var2 = mc.theWorld.loadedEntityList.iterator();

      while(true) {
         Entity entity;
         do {
            if (!var2.hasNext()) {
               if (list.size() == 0) {
                  list.add(target);
               }

               needHitBot = (EntityLivingBase)list.get(random.nextInt(list.size()));
               boolean superkb = !(Boolean)kbBlock.getValue() || mc.thePlayer.hurtResistantTime > 0;
               if (mc.thePlayer.getFoodStats().getFoodLevel() > 6 && (Boolean)this.morekb.getValue() && superkb) {
                  mc.getNetHandler().getNetworkManager().sendPacket(new C0BPacketEntityAction(mc.thePlayer, Action.STOP_SPRINTING));
                  mc.getNetHandler().getNetworkManager().sendPacket(new C0BPacketEntityAction(mc.thePlayer, Action.START_SPRINTING));
               }

               Criticals.doCrit();
               EventManager.call(new EventAttack(target));
               if ((Boolean)glich.getValue()) {
                  int beforeHeldItem = mc.thePlayer.inventory.currentItem;
                  mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem = 8));
                  mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem = beforeHeldItem));
               }

               this.attackEntity();
               if (mc.playerController.getCurrentGameType() != GameType.SPECTATOR && !((KeepSprint)ModManager.getModule(KeepSprint.class)).isEnabled()) {
                  mc.thePlayer.attackTargetEntityWithCurrentItem(target);
               }

               if (mc.thePlayer.getFoodStats().getFoodLevel() > 6 && (Boolean)this.morekb.getValue() && superkb) {
                  mc.getNetHandler().getNetworkManager().sendPacket(new C0BPacketEntityAction(mc.thePlayer, Action.STOP_SPRINTING));
               }

               AutoSword.publicItemTimer.reset();
               needHitBot = null;
               if (!attacked.contains(target) && target instanceof EntityPlayer) {
                  attacked.add(target);
               }

               return;
            }

            entity = (Entity)var2.next();
         } while(!(entity instanceof EntityMob) && !(entity instanceof EntityAnimal));

         if (entity.isInvisible() && (double)mc.thePlayer.getDistanceToEntity(entity) < (Double)reach.getValueState()) {
            list.add((EntityLivingBase)entity);
         }
      }
   }

   private void attackEntity() {
      mc.thePlayer.swingItem();
      if ((Boolean)interact.getValue()) {
         mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity((Boolean)this.aacbot.getValueState() ? (needHitBot == null ? target : needHitBot) : target, net.minecraft.network.play.client.C02PacketUseEntity.Action.INTERACT));
      }

      mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity((Boolean)this.aacbot.getValueState() ? (needHitBot == null ? target : needHitBot) : target, net.minecraft.network.play.client.C02PacketUseEntity.Action.ATTACK));
      if ((Boolean)this.mult.getValue()) {
         targets.stream().filter((entityLivingBase) -> {
            return entityLivingBase != target && entityLivingBase.hurtTime < 7;
         }).forEach((target) -> {
            mc.thePlayer.swingItem();
            mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity((Boolean)this.aacbot.getValueState() ? (needHitBot == null ? target : needHitBot) : target, net.minecraft.network.play.client.C02PacketUseEntity.Action.ATTACK));
         });
      }

   }

   private void doBlock(boolean setItemUseInCount) {
      if (setItemUseInCount) {
         ((IEntityPlayer)mc.thePlayer).setItemInUseCount(mc.thePlayer.getHeldItem().getMaxItemUseDuration());
      }

      mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
      isBlocking = true;
   }

   private void unBlock(boolean setItemUseInCount) {
      if (setItemUseInCount) {
         ((IEntityPlayer)mc.thePlayer).setItemInUseCount(0);
      }

      double blockvalue = -1.0;
      if (!PlayerUtil.isMoving2() && (Boolean)dynamic.getValue()) {
         blockvalue = ThreadLocalRandom.current().nextDouble(-1.0, -0.2);
      }

      if (!this.blockMode.isCurrentMode("Always")) {
         if (!this.blockMode.isCurrentMode("Exploit")) {
            mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(net.minecraft.network.play.client.C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(blockvalue, blockvalue, blockvalue), EnumFacing.DOWN));
         }

         isBlocking = false;
      }

   }

   public void onEnable() {
      attacked = new ArrayList();
      this.lastRotations = new float[]{mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch};
      super.onEnable();
   }

   public void onDisable() {
      if (isBlocking) {
         this.unBlock(true);
      }

      targets.clear();
      target = null;
      this.cps = 0.0;
      super.onDisable();
   }

   @EventTarget
   public void onPacket(EventPacket e) {
      if (e.getPacket() instanceof S30PacketWindowItems && isBlocking && this.blockMode.isCurrentMode("Exploit")) {
         e.setCancelled(true);
      }

   }

   private List getTargets() {
      Stream stream = mc.theWorld.loadedEntityList.stream().filter((entity) -> {
         return entity instanceof EntityLivingBase;
      }).map((entity) -> {
         return (EntityLivingBase)entity;
      }).filter(KillAura::isValidEntity);
      if (this.priority.isCurrentMode("Armor")) {
         stream = stream.sorted(Comparator.comparingInt((o) -> {
            return o instanceof EntityPlayer ? ((EntityPlayer)o).inventory.getTotalArmorValue() : (int)o.getHealth();
         }));
      } else if (this.priority.isCurrentMode("Range")) {
         stream = stream.sorted((o1, o2) -> {
            return (int)(o1.getDistanceToEntity(mc.thePlayer) - o2.getDistanceToEntity(mc.thePlayer));
         });
      } else if (this.priority.isCurrentMode("Fov")) {
         stream = stream.sorted(Comparator.comparingDouble((o) -> {
            return (double)getDistanceBetweenAngles(mc.thePlayer.rotationPitch, KillAura.AimUtil.getRotations(o)[0]);
         }));
      } else if (this.priority.isCurrentMode("Angle")) {
         stream = stream.sorted((o1, o2) -> {
            float[] rot1 = KillAura.AimUtil.getRotations(o1);
            float[] rot2 = KillAura.AimUtil.getRotations(o2);
            return (int)(mc.thePlayer.rotationYaw - rot1[0] - (mc.thePlayer.rotationYaw - rot2[0]));
         });
      } else if (this.priority.isCurrentMode("Health")) {
         stream = stream.sorted((o1, o2) -> {
            return (int)(o1.getHealth() - o2.getHealth());
         });
      } else if (this.priority.isCurrentMode("Hurt Time")) {
         stream = stream.sorted(Comparator.comparingInt((o) -> {
            return 20 - o.hurtResistantTime;
         }));
      }

      Object list;
      if ((Boolean)this.witherPriority.getValue()) {
         List sortedList = (List)stream.collect(Collectors.toList());
         list = new ArrayList();
         ((List)list).addAll((Collection)sortedList.stream().filter((entity) -> {
            return entity instanceof EntityWither;
         }).collect(Collectors.toList()));
         ((List)list).addAll((Collection)sortedList.stream().filter((entity) -> {
            return !(entity instanceof EntityWither);
         }).collect(Collectors.toList()));
      } else {
         list = (List)stream.collect(Collectors.toList());
      }

      if (Keyboard.isKeyDown(56)) {
         Collections.reverse((List)list);
      }

      return ((List)list).subList(0, Math.min(((List)list).size(), ((Double)switchsize.getValue()).intValue()));
   }

   private void getTarget() {
      int maxSize = ((Double)switchsize.getValueState()).intValue();
      if (maxSize > 1) {
         if ((Boolean)this.mult.getValue()) {
            this.setDisplayName("Multi");
         } else {
            this.setDisplayName("Switch");
         }
      } else {
         this.setDisplayName("Single");
      }

      try {
         targets = this.getTargets();
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   @EventTarget
   public void targetHud(EventRender2D event) {
      if ((Boolean)this.targetHUD.getValueState()) {
         ScaledResolution sr = new ScaledResolution(mc);
         HFontRenderer font;
         int blackcolor2;
         if (this.hudMode.isCurrentMode("Simple") && target != null) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            font = Hanabi.INSTANCE.fontManager.wqy18;
            font.drawStringWithShadow(target.getName(), (float)sr.getScaledWidth() / 2.0F - (float)font.getStringWidth(target.getName().replaceAll("§.", "")) / 2.0F, (float)sr.getScaledHeight() / 2.0F - 33.0F, -1);
            RenderHelper.enableGUIStandardItemLighting();
            mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/icons.png"));
            GL11.glDisable(2929);
            GL11.glEnable(3042);
            GL11.glDepthMask(false);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GlStateManager.color(1.0F, 1.0F, 1.0F);

            for(blackcolor2 = 0; (float)blackcolor2 < target.getMaxHealth() / 2.0F; ++blackcolor2) {
               mc.ingameGUI.drawTexturedModalRect((float)(sr.getScaledWidth() / 2) - target.getMaxHealth() / 2.0F * 9.5F / 2.0F + (float)(blackcolor2 * 10), (float)(sr.getScaledHeight() / 2 - 20), 16, 0, 9, 9);
            }

            for(blackcolor2 = 0; (float)blackcolor2 < target.getHealth() / 2.0F; ++blackcolor2) {
               mc.ingameGUI.drawTexturedModalRect((float)(sr.getScaledWidth() / 2) - target.getMaxHealth() / 2.0F * 9.5F / 2.0F + (float)(blackcolor2 * 10), (float)(sr.getScaledHeight() / 2 - 20), 52, 0, 9, 9);
            }

            GL11.glDepthMask(true);
            GL11.glDisable(3042);
            GL11.glEnable(2929);
            GlStateManager.disableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F);
            RenderHelper.disableStandardItemLighting();
         }

         float x;
         float y;
         float diff;
         if (this.hudMode.isCurrentMode("Fancy")) {
            font = Hanabi.INSTANCE.fontManager.wqy18;
            HFontRenderer font1 = Hanabi.INSTANCE.fontManager.wqy16;
            if (target != null) {
               int width = sr.getScaledWidth() / 2 + 100;
               int height = sr.getScaledHeight() / 2;
               EntityLivingBase player = target;
               if (ClickGUIModule.theme.isCurrentMode("Light")) {
                  Gui.drawRect(width - 70, height + 30, width + 80, height + 105, (new Color(255, 255, 255, 100)).getRGB());
               } else {
                  Gui.drawRect(width - 70, height + 30, width + 80, height + 105, (new Color(0, 0, 0, 140)).getRGB());
               }

               font.drawString(player.getName() + "             " + (Criticals.isReadyToCritical ? "Critical " : " "), (float)(width - 65), (float)(height + 35), 16777215);
               font1.drawString(player.onGround ? "On Ground" : "No Ground", (float)(width - 65), (float)(height + 50), 16777215);
               font1.drawString("HP: " + player.getHealth(), (float)(width - 65 + font1.getStringWidth("off Ground") + 13), (float)(height + 50), 16777215);
               font1.drawString("Distance: " + mc.thePlayer.getDistanceToEntity(player), (float)(width - 65), (float)(height + 60), -1);
               font1.drawString("FDistance: " + player.fallDistance, (float)(width - 65), (float)(height + 70), -1);
               font1.drawString("HurtTime: " + player.hurtTime, (float)(width - 5), (float)(height + 70), -1);
               font.drawString(player.getHealth() > mc.thePlayer.getHealth() ? "Lower Health" : "Higher Health", (float)(width - 65), (float)(height + 80), player.getHealth() > mc.thePlayer.getHealth() ? Color.RED.getRGB() : Color.GREEN.brighter().getRGB());
               GL11.glPushMatrix();
               GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
               GlStateManager.scale(1.0F, 1.0F, 1.0F);
               mc.getRenderItem().renderItemAndEffectIntoGUI(player.getHeldItem(), width + 50, height + 80);
               GL11.glPopMatrix();
               float health = player.getHealth();
               x = health / player.getMaxHealth();
               y = 0.0F;
               if (x != this.lastHealth) {
                  diff = x - this.lastHealth;
                  y = this.lastHealth;
                  this.lastHealth += diff / 8.0F;
               }

               Color healthcolor = Color.WHITE;
               if (x * 100.0F > 75.0F) {
                  healthcolor = Color.GREEN;
               } else if (x * 100.0F > 50.0F && x * 100.0F < 75.0F) {
                  healthcolor = Color.YELLOW;
               } else if (x * 100.0F < 50.0F && x * 100.0F > 25.0F) {
                  healthcolor = Color.ORANGE;
               } else if (x * 100.0F < 25.0F) {
                  healthcolor = Color.RED;
               }

               Gui.drawRect(width - 70, height + 104, (int)((float)(width - 70) + 149.0F * y), height + 106, healthcolor.getRGB());
               Gui.drawRect(width - 70, height + 104, (int)((float)(width - 70) + 149.0F * x), height + 106, Color.GREEN.getRGB());
               GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
               GuiInventory.drawEntityOnScreen(width + 60, height + 75, 20, (float)Mouse.getX(), (float)Mouse.getY(), player);
            }
         }

         if (this.hudMode.isCurrentMode("Flat")) {
            int blackcolor = (new Color(0, 0, 0, 180)).getRGB();
            blackcolor2 = (new Color(200, 200, 200, 160)).getRGB();
            ScaledResolution sr2 = new ScaledResolution(mc);
            float scaledWidth = (float)sr2.getScaledWidth();
            float scaledHeight = (float)sr2.getScaledHeight();
            HFontRenderer font1 = Hanabi.INSTANCE.fontManager.wqy16;
            this.nulltarget = target == null;
            x = scaledWidth / 2.0F - 50.0F;
            y = scaledHeight / 2.0F + 32.0F;
            double hpPercentage;
            Color hurt;
            int healthColor;
            String healthStr;
            if (this.nulltarget) {
               diff = 0.0F;
               hpPercentage = (double)(diff / 20.0F);
               hurt = Color.getHSBColor(0.8333333F, 0.0F, 1.0F);
               healthStr = String.valueOf(0.0F);
               healthColor = getHealthColor(0.0F, 20.0F).getRGB();
            } else {
               diff = target.getHealth();
               hpPercentage = (double)(diff / target.getMaxHealth());
               hurt = Color.getHSBColor(0.8611111F, (float)target.hurtTime / 10.0F, 1.0F);
               healthStr = String.valueOf((float)((int)target.getHealth()) / 2.0F);
               healthColor = getHealthColor(target.getHealth(), target.getMaxHealth()).getRGB();
            }

            hpPercentage = MathHelper.clamp_double(hpPercentage, 0.0, 1.0);
            double hpWidth = 140.0 * hpPercentage;
            if (this.nulltarget) {
               this.healthBarWidth2 = RenderUtil.getAnimationStateSmooth(0.0, this.healthBarWidth2, (double)(6.0F / (float)Minecraft.getDebugFPS()));
               this.healthBarWidth = RenderUtil.getAnimationStateSmooth(0.0, this.healthBarWidth, (double)(14.0F / (float)Minecraft.getDebugFPS()));
               this.hudHeight = RenderUtil.getAnimationStateSmooth(0.0, this.hudHeight, (double)(8.0F / (float)Minecraft.getDebugFPS()));
            } else {
               this.healthBarWidth2 = (double)AnimationUtil.moveUD((float)this.healthBarWidth2, (float)hpWidth, 6.0F / (float)Minecraft.getDebugFPS(), 3.0F / (float)Minecraft.getDebugFPS());
               this.healthBarWidth = RenderUtil.getAnimationStateSmooth(hpWidth, this.healthBarWidth, (double)(14.0F / (float)Minecraft.getDebugFPS()));
               this.hudHeight = RenderUtil.getAnimationStateSmooth(40.0, this.hudHeight, (double)(8.0F / (float)Minecraft.getDebugFPS()));
            }

            if (this.hudHeight == 0.0) {
               this.healthBarWidth2 = 140.0;
               this.healthBarWidth = 140.0;
            }

            GL11.glEnable(3089);
            RenderUtil.prepareScissorBox(x, (float)((double)y + 40.0 - this.hudHeight), x + 140.0F, (float)((double)y + 40.0));
            RenderUtil.drawRect(x, y, x + 140.0F, y + 40.0F, blackcolor);
            RenderUtil.drawRect(x, y + 37.0F, x + 140.0F, y + 40.0F, (new Color(0, 0, 0, 49)).getRGB());
            RenderUtil.drawRect(x, y + 37.0F, (float)((double)x + this.healthBarWidth2), y + 40.0F, (new Color(255, 0, 213, 220)).getRGB());
            RenderUtil.drawGradientSideways((double)x, (double)(y + 37.0F), (double)x + this.healthBarWidth, (double)(y + 40.0F), (new Color(0, 81, 179)).getRGB(), healthColor);
            font1.drawStringWithShadow(healthStr, x + 40.0F + 85.0F - (float)font1.getStringWidth(healthStr) / 2.0F + (float)mc.fontRendererObj.getStringWidth("❤") / 1.9F, y + 26.0F, blackcolor2);
            mc.fontRendererObj.drawStringWithShadow("❤", x + 40.0F + 85.0F - (float)font1.getStringWidth(healthStr) / 2.0F - (float)mc.fontRendererObj.getStringWidth("❤") / 1.9F, y + 26.5F, hurt.getRGB());
            HFontRenderer font2 = Hanabi.INSTANCE.fontManager.usans14;
            if (this.nulltarget) {
               font2.drawStringWithShadow("XYZ:0 0 0 | Hurt: false", x + 37.0F, y + 15.0F, Colors.WHITE.c);
               font1.drawStringWithShadow("(No target)", x + 36.0F, y + 5.0F, Colors.WHITE.c);
            } else {
               font2.drawStringWithShadow("XYZ:" + (int)target.posX + " " + (int)target.posY + " " + (int)target.posZ + " | Hurt: " + (target.hurtTime > 0), x + 37.0F, y + 15.0F, Colors.WHITE.c);
               if (target instanceof EntityPlayer) {
                  font2.drawStringWithShadow("Block: " + (((EntityPlayer)target).isBlocking() ? "True" : "False"), x + 37.0F, y + 25.0F, Colors.WHITE.c);
               }

               font1.drawStringWithShadow(target.getName(), x + 36.0F, y + 4.0F, Colors.WHITE.c);
               if (target instanceof EntityPlayer) {
                  GlStateManager.resetColor();
                  mc.getTextureManager().bindTexture(((AbstractClientPlayer)target).getLocationSkin());
                  GlStateManager.color(1.0F, 1.0F, 1.0F);
                  Gui.drawScaledCustomSizeModalRect((int)x + 3, (int)y + 3, 8.0F, 8.0F, 8, 8, 32, 32, 64.0F, 64.0F);
               }
            }

            GL11.glDisable(3089);
         }
      }

   }

   @EventTarget
   public void onRender(EventRender render) {
      if (target != null && this.attacktimer.isDelayComplete(randomClickDelay(Math.min((Double)minCps.getValue(), (Double)maxCps.getValue()), Math.max((Double)minCps.getValue(), (Double)maxCps.getValue())))) {
         ++this.cps;
         this.attacktimer.reset();
      }

      if (target != null && (Boolean)this.esp.getValueState()) {
         if (this.EspMode.isCurrentMode("Box")) {
            Iterator var2 = targets.iterator();

            while(var2.hasNext()) {
               EntityLivingBase entity = (EntityLivingBase)var2.next();
               this.renderBox(entity);
            }
         }

         if (this.EspMode.isCurrentMode("Circle")) {
            for(int i = 0; i < 5; ++i) {
               this.drawCircle(target, render.getPartialTicks(), 0.8, this.delay / 100.0);
            }
         }

         EntityLivingBase entity;
         if (this.EspMode.isCurrentMode("New")) {
            entity = target;
            drawESP(entity, entity.hurtTime >= 1 ? (new Color(255, 0, 0, 160)).getRGB() : (new Color(47, 116, 253, 255)).getRGB(), render);
         }

         double y;
         double z;
         double x;
         if (this.EspMode.isCurrentMode("Cylinder")) {
            entity = target;
            x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)Wrapper.getTimer().renderPartialTicks - ((IRenderManager)mc.getRenderManager()).getRenderPosX();
            y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)Wrapper.getTimer().renderPartialTicks - ((IRenderManager)mc.getRenderManager()).getRenderPosY();
            z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)Wrapper.getTimer().renderPartialTicks - ((IRenderManager)mc.getRenderManager()).getRenderPosZ();
            if (entity.hurtTime > 0) {
               RenderUtil.drawWolframEntityESP(entity, (new Color(255, 102, 113)).getRGB(), x, y, z);
            } else {
               RenderUtil.drawWolframEntityESP(entity, this.boxColor.getColor(), x, y, z);
            }
         }

         if (this.EspMode.isCurrentMode("ExeterCross")) {
            entity = target;
            x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)Wrapper.getTimer().renderPartialTicks - ((IRenderManager)mc.getRenderManager()).getRenderPosX();
            y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)Wrapper.getTimer().renderPartialTicks - ((IRenderManager)mc.getRenderManager()).getRenderPosY();
            z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)Wrapper.getTimer().renderPartialTicks - ((IRenderManager)mc.getRenderManager()).getRenderPosZ();
            if (entity.hurtTime > 0) {
               RenderUtil.drawExeterCrossESP(entity, (new Color(255, 102, 113)).getRGB(), x, y, z);
            } else {
               RenderUtil.drawExeterCrossESP(entity, this.boxColor.getColor(), x, y, z);
            }
         }

         if (this.delay > 200.0) {
            this.step = false;
         }

         if (this.delay < 0.0) {
            this.step = true;
         }

         if (this.step) {
            this.delay += 3.0;
         } else {
            this.delay -= 3.0;
         }

      }
   }

   public void renderBox(EntityLivingBase entity) {
      double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)Wrapper.getTimer().renderPartialTicks - ((IRenderManager)mc.getRenderManager()).getRenderPosX();
      double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)Wrapper.getTimer().renderPartialTicks - ((IRenderManager)mc.getRenderManager()).getRenderPosY();
      double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)Wrapper.getTimer().renderPartialTicks - ((IRenderManager)mc.getRenderManager()).getRenderPosZ();
      double width = entity.getEntityBoundingBox().maxX - entity.getEntityBoundingBox().minX - 0.1;
      double height = entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY + 0.25;
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      RenderUtil.color(entity.hurtTime > 1 ? RenderUtil.reAlpha((new Color(0.8F, 0.0F, 0.0F)).getRGB(), 0.2F) : RenderUtil.reAlpha(this.boxColor.getColor(), 0.15F));
      RenderUtil.drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
      GL11.glLineWidth(1.0F);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.0F);
      RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
      GL11.glDisable(2848);
      GL11.glEnable(3553);
      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
   }

   private void drawCircle(Entity entity, float partialTicks, double rad, double height) {
      GL11.glPushMatrix();
      GL11.glDisable(3553);
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      GL11.glLineWidth(2.0F);
      GL11.glBegin(3);
      double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks - mc.getRenderManager().viewerPosX;
      double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks - mc.getRenderManager().viewerPosY;
      double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks - mc.getRenderManager().viewerPosZ;
      double pix2 = 6.283185307179586;

      for(int i = 0; i <= 90; ++i) {
         GlStateManager.color((float)PaletteUtil.fade(Color.WHITE, 30, 14).getRed(), (float)PaletteUtil.fade(Color.WHITE, 30, 14).getGreen(), (float)PaletteUtil.fade(Color.WHITE, 30, 14).getBlue(), 0.8F);
         GL11.glVertex3d(x + rad * Math.cos((double)i * 6.283185307179586 / 45.0), y + height, z + rad * Math.sin((double)i * 6.283185307179586 / 45.0));
      }

      GL11.glEnd();
      GL11.glDepthMask(true);
      GL11.glEnable(2929);
      GL11.glEnable(3553);
      GL11.glPopMatrix();
   }

   public static void drawESP(EntityLivingBase entity, int color, EventRender e) {
      double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)e.getPartialTicks() - ((IRenderManager)mc.getRenderManager()).getRenderPosX();
      double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)e.getPartialTicks() - ((IRenderManager)mc.getRenderManager()).getRenderPosY();
      double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)e.getPartialTicks() - ((IRenderManager)mc.getRenderManager()).getRenderPosZ();
      float radius = 0.2F;
      int side = 6;
      GL11.glPushMatrix();
      GL11.glTranslated(x, y + 2.0, z);
      GL11.glRotatef(-entity.width, 0.0F, 1.0F, 0.0F);
      glColor((new Color(Math.max((new Color(color)).getRed() - 75, 0), Math.max((new Color(color)).getGreen() - 75, 0), Math.max((new Color(color)).getBlue() - 75, 0), (new Color(color)).getAlpha())).getRGB());
      enableSmoothLine(1.0F);
      Cylinder c = new Cylinder();
      GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
      c.setDrawStyle(100012);
      c.draw(0.0F, radius, 0.3F, side, 1);
      glColor(color);
      c.setDrawStyle(100012);
      GL11.glTranslated(0.0, 0.0, 0.3);
      c.draw(radius, 0.0F, 0.3F, side, 1);
      GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
      disableSmoothLine();
      GL11.glPopMatrix();
   }

   public static void glColor(int hex) {
      float alpha = (float)(hex >> 24 & 255) / 255.0F;
      float red = (float)(hex >> 16 & 255) / 255.0F;
      float green = (float)(hex >> 8 & 255) / 255.0F;
      float blue = (float)(hex & 255) / 255.0F;
      GL11.glColor4f(red, green, blue, alpha == 0.0F ? 1.0F : alpha);
   }

   public static void enableSmoothLine(float width) {
      GL11.glDisable(3008);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3553);
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      GL11.glEnable(2884);
      GL11.glEnable(2848);
      GL11.glHint(3154, 4354);
      GL11.glHint(3155, 4354);
      GL11.glLineWidth(width);
   }

   public static void disableSmoothLine() {
      GL11.glEnable(3553);
      GL11.glEnable(2929);
      GL11.glDisable(3042);
      GL11.glEnable(3008);
      GL11.glDepthMask(true);
      GL11.glCullFace(1029);
      GL11.glDisable(2848);
      GL11.glHint(3154, 4352);
      GL11.glHint(3155, 4352);
   }

   static class AimUtil {
      private static float getAngleDifference(float a, float b) {
         return ((a - b) % 360.0F + 540.0F) % 360.0F - 180.0F;
      }

      public static double getRotationDifference(Rotation a, Rotation b) {
         return Math.hypot((double)getAngleDifference(a.getYaw(), b.getYaw()), (double)(a.getPitch() - b.getPitch()));
      }

      public static double getRotationDifference(Rotation rotation) {
         return getRotationDifference(rotation, KillAura.serverRotation);
      }

      public static VecRotation searchCenter(AxisAlignedBB bb, boolean predict) {
         VecRotation vecRotation = null;
         double i = 0.1;
         switch (((Double)KillAura.resolvers.getValue()).intValue()) {
            case 1:
               i = 0.25;
               break;
            case 2:
               i = 0.2;
               break;
            case 3:
               i = 0.15;
               break;
            case 4:
               i = 0.125;
            case 5:
            default:
               break;
            case 6:
               i = 0.08;
               break;
            case 7:
               i = 0.075;
               break;
            case 8:
               i = 0.06;
               break;
            case 9:
               i = 0.05;
               break;
            case 10:
               i = 0.04;
               break;
            case 11:
               i = 0.03;
               break;
            case 12:
               i = 0.02;
         }

         for(double xSearch = 0.0; xSearch < 1.0; xSearch += i) {
            for(double ySearch = 0.0; ySearch < 1.0; ySearch += i) {
               for(double zSearch = 0.0; zSearch < 1.0; zSearch += i) {
                  Vec3 vec3 = new Vec3(bb.minX + (bb.maxX - bb.minX) * xSearch, bb.minY + (bb.maxY - bb.minY) * ySearch, bb.minZ + (bb.maxZ - bb.minZ) * zSearch);
                  Rotation rotation = toRotation(vec3, predict);
                  VecRotation currentVec = new VecRotation(vec3, rotation);
                  if (vecRotation == null || getRotationDifference(currentVec.getRotation()) < getRotationDifference(vecRotation.getRotation())) {
                     vecRotation = currentVec;
                  }
               }
            }
         }

         return vecRotation;
      }

      public static Rotation toRotation(Vec3 vec, boolean predict) {
         Vec3 eyesPos = new Vec3(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().minY + (double)Minecraft.getMinecraft().thePlayer.getEyeHeight(), Minecraft.getMinecraft().thePlayer.posZ);
         if (predict) {
            eyesPos.addVector(Minecraft.getMinecraft().thePlayer.motionX, Minecraft.getMinecraft().thePlayer.motionY, Minecraft.getMinecraft().thePlayer.motionZ);
         }

         double diffX = vec.xCoord - eyesPos.xCoord;
         double diffY = vec.yCoord - eyesPos.yCoord;
         double diffZ = vec.zCoord - eyesPos.zCoord;
         return new Rotation(MathHelper.wrapAngleTo180_float((float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F), MathHelper.wrapAngleTo180_float((float)(-Math.toDegrees(Math.atan2(diffY, Math.sqrt(diffX * diffX + diffZ * diffZ))))));
      }

      public static Vec3 getLocation(AxisAlignedBB bb) {
         double value = Math.random();
         boolean reverse = KillAura.random.nextInt(100) < ((Double)KillAura.reverse.getValueState()).intValue();
         boolean mistake = KillAura.random.nextInt(100) < ((Double)KillAura.outpoint.getValueState()).intValue();
         Vec3 resolve = null;
         if ((Boolean)KillAura.resolverY.getValue() || (Boolean)KillAura.resolverXZ.getValue()) {
            resolve = searchCenter(bb, true).getVec3();
         }

         double minX = Math.min((Double)KillAura.yawxmin.getValue(), (Double)KillAura.yawxmax.getValue());
         double maxX = Math.max((Double)KillAura.yawxmin.getValue(), (Double)KillAura.yawxmax.getValue());
         double minZ = Math.min((Double)KillAura.yawzmin.getValue(), (Double)KillAura.yawzmax.getValue());
         double maxZ = Math.max((Double)KillAura.yawzmin.getValue(), (Double)KillAura.yawzmax.getValue());
         double x = (Boolean)KillAura.xRandom.getValue() ? value : 0.5 + KillAura.getRandomDoubleInRange(minX, maxX) / 2.0;
         double z = (Boolean)KillAura.zRandom.getValue() ? value : 0.5 + KillAura.getRandomDoubleInRange(minZ, maxZ) / 2.0;
         double pitch = (Boolean)KillAura.pitchRandom.getValue() ? value : 0.5 + (Double)KillAura.pitchRand.getValue() / 2.0;
         return new Vec3((Boolean)KillAura.resolverXZ.getValue() ? ((Vec3)Objects.requireNonNull(resolve)).xCoord : (bb.minX + (bb.maxX - bb.minX) * (reverse ? 1.0 - x : x)) * (mistake ? 1.0 + value * 0.1 : 1.0), (Boolean)KillAura.resolverY.getValue() ? ((Vec3)Objects.requireNonNull(resolve)).yCoord : bb.minY + (bb.maxY - bb.minY) * pitch, (Boolean)KillAura.resolverXZ.getValue() ? ((Vec3)Objects.requireNonNull(resolve)).zCoord : bb.minZ + (bb.maxZ - bb.minZ) * (reverse ? 1.0 - z : z) * (mistake ? 1.0 + value * 0.1 : 1.0));
      }

      public static boolean isVisibleFOV(Entity e, float fov) {
         return (Math.abs(getRotations(e)[0] - Minecraft.getMinecraft().thePlayer.rotationYaw) % 360.0F > 180.0F ? 360.0F - Math.abs(getRotations(e)[0] - Minecraft.getMinecraft().thePlayer.rotationYaw) % 360.0F : Math.abs(getRotations(e)[0] - Minecraft.getMinecraft().thePlayer.rotationYaw) % 360.0F) <= fov;
      }

      float updateRotation(float curRot, float destination, float speed) {
         float f = MathHelper.wrapAngleTo180_float(destination - curRot);
         if (f > speed) {
            f = speed;
         }

         if (f < -speed) {
            f = -speed;
         }

         return curRot + f;
      }

      public static float[] getRotations(Entity entity) {
         if (entity == null) {
            return null;
         } else {
            double diffX = entity.posX - Minecraft.getMinecraft().thePlayer.posX;
            double diffZ = entity.posZ - Minecraft.getMinecraft().thePlayer.posZ;
            double diffY;
            if (entity instanceof EntityLivingBase) {
               EntityLivingBase elb = (EntityLivingBase)entity;
               diffY = elb.posY + (double)elb.getEyeHeight() - (Minecraft.getMinecraft().thePlayer.posY + (double)Minecraft.getMinecraft().thePlayer.getEyeHeight());
            } else {
               diffY = (entity.getEntityBoundingBox().minY + entity.getEntityBoundingBox().maxY) / 2.0 - (Minecraft.getMinecraft().thePlayer.posY + (double)Minecraft.getMinecraft().thePlayer.getEyeHeight());
            }

            double dist = (double)MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
            float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / Math.PI) - 90.0F;
            float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / Math.PI));
            return new float[]{yaw, pitch};
         }
      }

      public static float[] NormalFix(float[] rot) {
         float f = KillAura.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
         float f1 = f * f * f * 1.2F;
         return new float[]{rot[0] - rot[0] % f1, rot[1] - rot[1] % f1};
      }

      public static float[] PrefectFix(float[] rot) {
         float yaw = rot[0];
         float pitch = rot[1];
         float f = KillAura.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
         float pos = f * f * f * 8.0F;
         yaw += pos * 0.15F;
         pitch -= pos * 0.15F;
         return new float[]{yaw, pitch};
      }
   }
}
