package info.sigmaclient.sigma.modules.combat;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import info.sigmaclient.sigma.modules.movement.AutoSprint;
import info.sigmaclient.sigma.premium.PremiumManager;
import info.sigmaclient.sigma.sigma5.jelloportal.florianmichael.vialoadingbase.ViaLoadingBase;
import info.sigmaclient.sigma.sigma5.jelloportal.florianmichael.viamcp.ViaMCP;
import info.sigmaclient.sigma.sigma5.killaura.NCPRotation;
import info.sigmaclient.sigma.utils.ChatUtils;
import info.sigmaclient.sigma.utils.Variable;
import info.sigmaclient.sigma.utils.render.blurs.Gradient;
import info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager;
import info.sigmaclient.sigma.gui.hud.notification.NotificationManager;
import info.sigmaclient.sigma.sigma5.utils.KillauraESP;
import info.sigmaclient.sigma.sigma5.utils.Sigma5AnimationUtil;
import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.config.values.ColorValue;
import info.sigmaclient.sigma.event.EventManager;
import info.sigmaclient.sigma.event.net.PacketEvent;
import info.sigmaclient.sigma.event.player.ClickEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.event.player.WindowUpdateEvent;
import info.sigmaclient.sigma.event.render.Render3DEvent;
import info.sigmaclient.sigma.event.render.RenderShaderEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.modules.gui.ColorChanger;
import info.sigmaclient.sigma.modules.movement.Speed;
import info.sigmaclient.sigma.modules.player.OldHitting;
import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.config.values.ModeValue;
import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.utils.RandomUtil;
import info.sigmaclient.sigma.utils.player.Rotation;
import info.sigmaclient.sigma.utils.player.RotationUtils;
import info.sigmaclient.sigma.utils.TimerUtil;
import info.sigmaclient.sigma.utils.render.ColorUtils;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import info.sigmaclient.sigma.utils.render.anims.PartialTicksAnim;
import info.sigmaclient.sigma.utils.render.blurs.Bloom;
import info.sigmaclient.sigma.utils.render.rendermanagers.TextureObf;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.SwordItem;
import net.minecraft.network.play.client.*;
import net.minecraft.potion.Effects;
import net.minecraft.resources.ResourcePack;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import top.fl0wowp4rty.phantomshield.annotations.Native;

import java.awt.*;
import java.util.*;
import java.util.List;

import static info.sigmaclient.sigma.sigma5.utils.SomeAnim.欫좯콵甐鶲㥇;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class Killaura extends Module {
    public ModeValue mode = new ModeValue("Type", "Single", new String[]{
            "Single", "Random", "Switch"
    });
    public NumberValue switchTime2 = new NumberValue("Switch Time", 5, 1, 20, NumberValue.NUMBER_TYPE.INT){
        @Override
        public boolean isHidden() {
            return !mode.is("Switch");
        }
    };
    public ModeValue SortMode = new ModeValue("Sort Type", "Distance",
            new String[]{
                    "Distance", "Health", "Armor", "RArmor"
    });
    public ModeValue rotation = new ModeValue("Rotation", "NCP", new String[]{
            "NCP",
//            "Matrix",
            "Custom",
            "None"
    });
    // CUSTOM
    public ModeValue customRotationMode = new ModeValue("Mode", "Nearest",
            new String[]{
                    "Nearest", "EyeHeight", "OnlyYaw"
            })
    {@Override public boolean isHidden() {return !rotation.is("Custom");}};
    public ModeValue customAddonsMode = new ModeValue("Addons", "None",
            new String[]{
                    "None", "Random", "Random2", "TickRandom", "MatrixRandom", "SupRandom", "SmalRandom"
            })
    {@Override public boolean isHidden() {return !rotation.is("Custom");}};
    public ModeValue customExtendsMode = new ModeValue("Extends", "None",
            new String[]{
                    "None", "Int", "Digit", "DelayInt", "DelayMove"
            })
    {@Override public boolean isHidden() {return !rotation.is("Custom");}};
    public ModeValue customSmoothMode = new ModeValue("Smooth", "Instant",
            new String[]{
                    "Instant", "MouseSens", "Custom", "Both"
            })
    {@Override public boolean isHidden() {return !rotation.is("Custom");}};

    public static ModeValue autoblockMode = new ModeValue("AutoBlock", "Vanilla",
            new String[]{
                    "None", "Vanilla", "NCP", "Always", "Percent", "Fake", "Hypixel", "Basic1", "Basic2", "Legit", "Intave"
    });
    public ModeValue cpsMode = new ModeValue("CPSMode", "Custom", new String[]{
            "Unstable", "Custom"
    }){
        @Override
        public boolean isHidden() {
            return true;
        }
    };
    public NumberValue maxCPS = new NumberValue("MaxCPS", 10.0, 0, 20, NumberValue.NUMBER_TYPE.INT){
        @Override
        public void onSetValue() {
            if(this.getValue().floatValue() < minCPS.getValue().floatValue()){
                this.pureSetValue(minCPS.getValue());
            }
        }
    };
    public NumberValue minCPS = new NumberValue("MinCPS", 10.0, 1, 20, NumberValue.NUMBER_TYPE.INT){
        @Override
        public void onSetValue() {
            if(this.getValue().floatValue() > maxCPS.getValue().floatValue()){
                this.pureSetValue(maxCPS.getValue());
            }
        }
    };
    public NumberValue maxTurnSpeed = new NumberValue("MaxTurn Speed", 80, 1, 180, NumberValue.NUMBER_TYPE.INT){
        @Override
        public void onSetValue() {
            if(this.getValue().floatValue() < minTurnSpeed.getValue().floatValue()){
                this.pureSetValue(minTurnSpeed.getValue());
            }
        }
    };
    public NumberValue minTurnSpeed = new NumberValue("MinTurn Speed", 20, 0, 180, NumberValue.NUMBER_TYPE.INT){
        @Override
        public void onSetValue() {
            if(this.getValue().floatValue() > maxTurnSpeed.getValue().floatValue()){
                this.pureSetValue(maxTurnSpeed.getValue());
            }
        }
    };
    public static NumberValue range = new NumberValue("Target Range", 3.0, 0, 8, NumberValue.NUMBER_TYPE.LOW_FLOAT);
    public static NumberValue range2 = new NumberValue("Attack Range", 3.0, 0, 8, NumberValue.NUMBER_TYPE.LOW_FLOAT);
    public NumberValue blockRange = new NumberValue("Block Range", 3.0, 0, 8, NumberValue.NUMBER_TYPE.LOW_FLOAT){
        @Override
        public void onSetValue() {
            if(this.getValue().floatValue() > range.getValue().floatValue()){
                this.pureSetValue(range.getValue());}}};
    public static NumberValue rayrange = new NumberValue("Raytrace Range", 3.0, 0, 8, NumberValue.NUMBER_TYPE.LOW_FLOAT){
        @Override public void onSetValue() {
            if(this.getValue().floatValue() > range.getValue().floatValue()){
                this.pureSetValue(range.getValue());
            }}@Override public boolean isHidden() {
            return super.isHidden();
        }};
    public static NumberValue hitboxExpand = new NumberValue("Hit Box Expand", 0, 0, 1, NumberValue.NUMBER_TYPE.FLOAT);
    public static NumberValue hitChange = new NumberValue("Hit Chance", 100, 0, 100, NumberValue.NUMBER_TYPE.FLOAT);
    public static BooleanValue invisible = new BooleanValue("Invisible", false);
    public static BooleanValue players = new BooleanValue("Players", false);
    public static BooleanValue mobs = new BooleanValue("Monster", false);
    public static BooleanValue npcs = new BooleanValue("NPC", false);
    public static BooleanValue animals = new BooleanValue("Animals", false);
    public static BooleanValue naked = new BooleanValue("No Naked", false);
    public static BooleanValue limit = new BooleanValue("Lock Target", false);
    public static BooleanValue polar = new BooleanValue("Polar", false);
    public static BooleanValue Interact = new BooleanValue("Interact autoblock", false);
    public static BooleanValue deadhit = new BooleanValue("NoDead", true){
        @Override
        public boolean isHidden() {
            return true;
        }
    };
    public static BooleanValue smoothBack = new BooleanValue("Smooth Back", true);
//    public BooleanValue movementFix = new BooleanValue("MovementFix", false);
    public ModeValue movementFix = new ModeValue("Move Correction", "None", new String[]{
            "None", "Matrix", "Strict"
    }){
        @Override
        public boolean isHidden() {
            return false;
        }
    };
    public BooleanValue slient = new BooleanValue("SlientFix", false);
//    public BooleanValue attackfix = new BooleanValue("1.8 attack", false);
    public BooleanValue throughWalls = new BooleanValue("Through walls", false);
    public BooleanValue silent = new BooleanValue("Silent", true);
    public static BooleanValue raytrace = new BooleanValue("Raytrace", false);
    public ModeValue TraceMode = new ModeValue("Trace Mode", "Target", new String[]{
            "Target", "All"
    }){
        @Override
        public boolean isHidden() {
            return !raytrace.isEnable();
        }
    };
    public static BooleanValue shieldBreaker = new BooleanValue("ShieldBreaker", false);
//    public static BooleanValue noReach = new BooleanValue("No Reach", false);
//    public BooleanValue keep = new BooleanValue("Keep swing", false);
    public BooleanValue cooldown = new BooleanValue("CPS-Cooldown", false);
    public BooleanValue fallhit = new BooleanValue("Critical Hit", false);
//    public BooleanValue fallhitPlus = new BooleanValue("SPCritical", false);
    public BooleanValue snap = new BooleanValue("Snap Hit-", false){
    @Override
    public boolean isHidden() {
        return true;
    }
};
    public static NumberValue snapTime = new NumberValue("Snap Time-", 1, 1, 20, NumberValue.NUMBER_TYPE.INT){
        @Override
        public boolean isHidden() {
            return true;
        }
    };
    public BooleanValue noswing = new BooleanValue("No Swing", false);
    public BooleanValue disable_on_death = new BooleanValue("Disable on death", false);

    public static BooleanValue render = new BooleanValue("ESP", false);
    public static ModeValue espMode = new ModeValue("ESPMode", "Sigma",
            new String[]{
                    "Sigma", "Nurik"
            }){
        @Override
        public boolean isHidden() {
            return !render.isEnable() || !PremiumManager.isPremium;
        }
    };
    public ColorValue renderColor = new ColorValue("ESPColor", -1){
        @Override
        public boolean isHidden() {
            return !espMode.is("Sigma");
        }
    };
    // anims
    private PartialTicksAnim translate = new PartialTicksAnim(0);
    private PartialTicksAnim translate2 = new PartialTicksAnim(0);
    private PartialTicksAnim translate3 = new PartialTicksAnim(0);
    private int cps;
    private boolean back = false;
    private int switchTime = 0;

    boolean c07ed;
    private int legitABdelay = 0;
    private final TimerUtil attackTimer = new TimerUtil();
    private final ArrayList<LivingEntity> targets = new ArrayList<>();
    public float[] lastRotation = null;
    boolean snaping = false;
    int snapTimeTicks = 0;
    int shieldBreakCooldown = 0;

    private static double blockTime;
    public static LivingEntity cacheAttackTarget = null;
    public static LivingEntity attackTarget = null;
    public Killaura() {
        super("KillAura", Category.Combat, "Automatically attack mobs");
     registerValue(mode);
     registerValue(switchTime2);
     registerValue(SortMode);
     registerValue(rotation);
     registerValue(customRotationMode);
     registerValue(customAddonsMode);
     registerValue(customExtendsMode);
     registerValue(customSmoothMode);
     registerValue(autoblockMode);
     registerValue(cpsMode);
     registerValue(maxCPS);
     registerValue(minCPS);
     registerValue(maxTurnSpeed);
     registerValue(minTurnSpeed);
     registerValue(range);
     registerValue(range2);
     registerValue(blockRange);
//     registerValue(rayrange);
     registerValue(TraceMode);
     registerValue(hitboxExpand);
     registerValue(hitChange);
     registerValue(invisible);
     registerValue(players);
     registerValue(mobs);
     registerValue(npcs);
     registerValue(animals);
     registerValue(naked);
     registerValue(limit);
     registerValue(polar);
     registerValue(Interact);
//     registerValue(deadhit);
//     registerValue(smoothBack);
     registerValue(movementFix);
//     registerValue(movementFix);
     registerValue(slient);
//     registerValue(attackfix);
     registerValue(throughWalls);
     registerValue(silent);
     registerValue(raytrace);
     registerValue(shieldBreaker);
//     registerValue(noReach);
//     registerValue(keep);
     registerValue(cooldown);
     registerValue(fallhit);
//     registerValue(fallhitPlus);
     registerValue(snap);
     registerValue(snapTime);
     registerValue(noswing);
     registerValue(disable_on_death);
     registerValue(render);
     registerValue(espMode);
     registerValue(renderColor);
    }
    @Native
    public static boolean isTargetEnable(LivingEntity LivingEntity){
        if(!LivingEntity.isAlive()) return false;
        if(LivingEntity instanceof PlayerEntity) {
            if(naked.isEnable()){
                boolean nake = true;
                for(ItemStack i : LivingEntity.getArmorInventoryList()){
                    if(!i.isEmpty()){
                        nake = false;
                        break;
                    }
                }
                if(nake) return false;
            }
            if(AntiBot.isServerBots(LivingEntity)) return false;
            if(Teams.isTeam((PlayerEntity) LivingEntity)) return false;
            if(SigmaNG.getSigmaNG().friendsManager.isFriend(LivingEntity)) return false;
            if(LivingEntity.equals(mc.player)) return false;
            if (invisible.isEnable() && LivingEntity.isInvisible())
                return true;
            return players.isEnable() && !LivingEntity.isInvisible();
        }
        if(LivingEntity instanceof VillagerEntity) {
            if (npcs.isEnable()) {
                return true;
            }
        }
        if(LivingEntity instanceof AnimalEntity) {
            return animals.isEnable();
        }
        if(LivingEntity instanceof MonsterEntity) {
            return mobs.isEnable();
        }
        return false;
    }
    @Native
    public void reset(){
        lastRotation = null;
        attackTarget = null;
        targets.clear();
    }
    @Native
    @Override
    public void onEnable() {
        sprintHit = false;
        canFallHit = false;
        snapTimeTicks = 0;
        groundTime = 0;
        reset();
        seed = 1f;
        legitABdelay = 0;
        blockTime = 0;
        translate.setValue(0);

        ArrayList<Entity> removes = new ArrayList<>();
        for (Map.Entry<Entity, ESPTarget> entityESPTargetEntry : espTargets.entrySet()) {
            removes.add(entityESPTargetEntry.getKey());
        }
        for(Entity e : removes)
            espTargets.remove(e);
        super.onEnable();
    }
    @Native
    public boolean isABEnable(){
        return !autoblockMode.is("None");
    }
    @Override
    @Native
    public void onPacketEvent(PacketEvent event) {
        if(Variable.stop_killaura){
            return;
        }
//        if(event.packet instanceof CClickWindowPacket){
//            ChatUtils.sendMessage(((CClickWindowPacket) event.packet).getSlotId() + " I");
//        }
        if(isABEnable()){
            if(mc.player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof SwordItem)
            switch (autoblockMode.getValue()) {
                case "Legit":
                case "Hypixel":
                case "Intave":
                    break;
            }
        }
        super.onPacketEvent(event);
    }

    @Native
    @Override
    public void onDisable() {
        KeyBinding.setKeyBindState(InputMappings.Type.MOUSE.getOrMakeInput(1),false);
        canFallHit = false;
        cacheAttackTarget = null;
        reset();
        EventManager.nextLegitUnblock = true;
        if(isABEnable()) {
            OldHitting.blocking = false;
        }

        super.onDisable();
    }
    boolean canFallHit = false;
    boolean sprintHit = false;
    int groundTime = 0;
    float seed = 1;
    @Native
    public void clicked(float yaw , float pitch) {
        ret:{
            float f2 = mc.player.getCooledAttackStrength(0.5F);
            boolean flag = f2 > 0.9F;
            boolean flag2 = flag && mc.player.fallDistance > 0.0F && !mc.player.onGround && !mc.player.isOnLadder() && !mc.player.isInWater() && !mc.player.isPotionActive(Effects.BLINDNESS) && !mc.player.isPassenger() && attackTarget instanceof LivingEntity;

            flag2 = flag2 && !mc.player.isSprinting();
        boolean cool = cooldown.isEnable() && mc.player.getCooledAttackStrength(0.5f) > 0.9f;
        if ((!cooldown.isEnable() && attackTimer.hasTimeElapsed(cps, false)) || (cooldown.isEnable() && cool && attackTimer.hasTimeElapsed(cps, false))) {
            if (snap.isEnable()) {
                snapTimeTicks = snapTime.getValue().intValue();
            }
            if (!throughWalls.getValue()) {
                if (!mc.player.canEntityBeSeen(attackTarget)) {
                    break ret;
                }
            }
            if (fallhit.isEnable()) {
                boolean dontCrit = !(!mc.player.isOnLadder() && !mc.player.isInWater() && !mc.player.isPotionActive(Effects.BLINDNESS) && !mc.player.isPassenger());
                if (!canFallHit && !dontCrit) {
                    break ret;
                }else if(sprintHit || mc.player.isSprinting()){
//                    if(fallhitPlus.isEnable()){
                        break ret;
//                    }
                }
                if(!flag2){
                    if(!dontCrit)
                        break ret;
                }
            }
            int cpss = (int) (minCPS.getValue().intValue() + (maxCPS.getValue().intValue() - minCPS.getValue().intValue() + 1) * new Random().nextDouble());
            if (cpss == 0)
                break ret;
            boolean airAttack = new Random().nextInt(100) > hitChange.getValue().floatValue();
            if (raytrace.isEnable() && !RotationUtils.isMouseOver(mc.player.lastReportedYaw, mc.player.lastReportedPitch, attackTarget, range2.getValue().floatValue(), hitboxExpand.getValue().floatValue(), TraceMode.getValue().equals("Target"))) {
//                if (keep.isEnable()) {
                if((!polar.isEnable()) || mc.player.getDistanceNearest(attackTarget) > (range2.getValue().floatValue() - new Random().nextFloat() * 3))
                    if (!noswing.isEnable() && !cooldown.isEnable())
                        mc.player.swingArm(Hand.MAIN_HAND);
//                }
                break ret;
            }
            if(mc.player.getDistanceNearest(attackTarget) > range2.getValue().floatValue() && !raytrace.getValue()){
//                if (keep.isEnable()) {
                    if (!noswing.isEnable() && !cooldown.isEnable())
                        mc.player.swingArm(Hand.MAIN_HAND);
//                }
                break ret;
            }
            cps = 1000 / cpss - 1;
            attackTimer.reset();
            if (airAttack) {
                if (!noswing.isEnable() && !cooldown.isEnable())
                    mc.player.swingArm(Hand.MAIN_HAND);
                break ret;
            }
            boolean canBlock = mc.player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof SwordItem &&
                    mc.player.getDistance(attackTarget) <= blockRange.getValue().longValue();
            if (isABEnable()) {
                if (!autoblockMode.is("Legit") && !autoblockMode.is("Intave")) {
                    unBlock();
                    unBlockTarget();
                }
            }
            if (isABEnable()) {
                if (autoblockMode.is("Legit") || autoblockMode.is("Intave")) {
                    if (legitABdelay > 0) {
                        legitABdelay--;
                        if (legitABdelay <= (autoblockMode.is("Intave") ? 1 : 0)) {
                            mc.gameSettings.keyBindUseItem.pressed = false;
                        }
                        break ret;
                    }
                    if (mc.player.isHandActive()) {
                        mc.gameSettings.keyBindUseItem.pressed = false;
                        break ret;
                    }
                }
            }

            attackTimer.reset();
            if (ViaLoadingBase.getInstance().getTargetVersion().isOlderThanOrEqualTo(ProtocolVersion.v1_8)) {
                if (!noswing.isEnable())
                    mc.player.swingArm(Hand.MAIN_HAND);
                mc.playerController.attackEntity(mc.player, attackTarget);
            } else {
                mc.playerController.attackEntity(mc.player, attackTarget);
                if (!noswing.isEnable())
                    mc.player.swingArm(Hand.MAIN_HAND);
            }
//            snapTimeTicks = 0;

            if (isABEnable() && canBlock) {
                if (Interact.isEnable()) {
                    Entity entity = mc.player;
                    Vector3d vector3d = entity.getEyePosition(1.0F);

                    mc.getProfiler().startSection("pick");
                    Vector3d vector3d1 = entity.getLookCustom(1.0f, yaw, pitch);
                    Vector3d vector3d2 = vector3d.add(vector3d1.x * 4.5, vector3d1.y * 4.5, vector3d1.z * 4.5);
                    AxisAlignedBB axisalignedbb = entity.getBoundingBox().grow(hitboxExpand.getValue().floatValue()).expand(vector3d1.scale(4.5)).grow(1.0D, 1.0D, 1.0D);
                    EntityRayTraceResult entityraytraceresult = ProjectileHelper.rayTraceEntities(entity, vector3d, vector3d2,
                            axisalignedbb, (p_lambda$getMouseOver$0_0_) ->
                                    !p_lambda$getMouseOver$0_0_.isSpectator() && p_lambda$getMouseOver$0_0_.canBeCollidedWith(), 4.5 * 4.5);

                    if (entityraytraceresult != null) {
                        entity = entityraytraceresult.getEntity();
                        mc.getConnection().sendPacket(new CUseEntityPacket(entity, Hand.MAIN_HAND, entityraytraceresult.getHitVec(), mc.player.isSneaking()));
                    }
                    mc.getProfiler().endSection();

                }
                if(mc.player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof SwordItem)
                switch (autoblockMode.getValue()) {
                    case "Basic2":
                        blockTime++;
                        break;
                    case "Always":
                        blockTime++;
                        break;
                    case "Vanilla":
                        blockTime++;
                        mc.getConnection().sendPacket(new CPlayerTryUseItemPacket(Hand.MAIN_HAND));
                        break;
                    case "Basic1":
                        blockTime++;
                        if (mc.player.ticksExisted % 2 == 0) {
                            mc.getConnection().sendPacket(new CPlayerTryUseItemPacket(Hand.MAIN_HAND));
                        }
                        break;
                    case "NCP":
                        blockTime++;
                        break;
                    case "Percent":
                        if (mc.player.ticksExisted % 3 == 0) {
                            mc.getConnection().sendPacket(new CPlayerDiggingPacket(CPlayerDiggingPacket.Action.RELEASE_USE_ITEM, BlockPos.ZERO, Direction.DOWN));
                            blockTime++;
                        }
                        break;
                    case "Legit":
                        legitABdelay = 2;
                        mc.gameSettings.keyBindUseItem.pressed = true;
                        blockTime++;
                        break;
                    case "Intave":
                        if(mc.player.ticksExisted % 20 >= 4 && mc.player.ticksExisted % 20 <= 8) {
                            legitABdelay = 2;
                            mc.gameSettings.keyBindUseItem.pressed = true;
                        }
                        blockTime++;
                        break;
                    case "Hypixel":
                        KeyBinding.setKeyBindState(InputMappings.Type.MOUSE.getOrMakeInput(1),true);
                        blockTime++;
                        break;
                }
            }
        }
    }
    }
    @Native

    public static void unBlockTarget(){
        if(blockTime > 0){
            blockTime = 0;
            switch (autoblockMode.getValue()) {
                case "Basic2":
                    mc.getConnection().sendPacket(new CPlayerTryUseItemPacket(Hand.MAIN_HAND));
                    break;
            }
        }
    }
    @Native
    public static void unBlock(){
        if(blockTime > 0){
            blockTime = 0;
            switch (autoblockMode.getValue()) {
                case "Legit":
                    mc.gameSettings.keyBindUseItem.pressed = false;
                    break;
                case "Intave":
                    mc.gameSettings.keyBindUseItem.pressed = false;
                    break;
                case "Basic2":
                    mc.getConnection().sendPacket(new CHeldItemChangePacket(mc.player.inventory.currentItem % 9 + 1));
                    mc.getConnection().sendPacket(new CHeldItemChangePacket(mc.player.inventory.currentItem));
                    mc.getConnection().sendPacket(new CPlayerTryUseItemPacket(Hand.MAIN_HAND));
                    break;
                case "Always":
                    break;
                case "Vanilla":
                    mc.getConnection().sendPacket(new CPlayerDiggingPacket(CPlayerDiggingPacket.Action.RELEASE_USE_ITEM, BlockPos.ZERO, Direction.UP));
                    break;
                case "NCP":
                    if (attackTarget == null) {
                        mc.getConnection().sendPacket(new CHeldItemChangePacket(mc.player.inventory.currentItem % 9 + 1));
                        mc.getConnection().sendPacket(new CHeldItemChangePacket(mc.player.inventory.currentItem));
                    } else {
                        mc.getConnection().sendPacket(new CPlayerTryUseItemPacket(Hand.MAIN_HAND));
                    }
                    break;
                case "Basic1":
                    mc.getConnection().sendPacket(new CHeldItemChangePacket(mc.player.inventory.currentItem % 9 + 1));
                    mc.getConnection().sendPacket(new CHeldItemChangePacket(mc.player.inventory.currentItem));
                    break;
                case "Percent":
                    Objects.requireNonNull(mc.getConnection()).sendPacket(new CPlayerDiggingPacket(CPlayerDiggingPacket.Action.RELEASE_USE_ITEM, BlockPos.ZERO, Direction.UP));
                    break;
                case "Hypixel":
                    KeyBinding.setKeyBindState(InputMappings.Type.MOUSE.getOrMakeInput(1),false);
                    break;
            }

        }
    }

    @Native
    @Override
    public void onWindowUpdateEvent(WindowUpdateEvent event) {
        if(disable_on_death.isEnable()){
            if(!mc.player.isAlive()){
                if(enabled) {
                    enabled = false;
                    onDisable();
                    NotificationManager.notify("Aura", "Aura disabled due to death");
                }
                return;
            }
        }
        if(render.isEnable()) {
            if(attackTarget != null){
                float tick = (float) (35);
                float tick2 = (float) (35);
                translate2.interpolate(back ? tick2 : -tick, -0.3 + (float) (new Random().nextGaussian()) * 0.1f);
                if (translate2.getValueNoTrans() >= tick2) {
                    back = false;
                } else if (translate2.getValueNoTrans() <= -tick) {
                    back = true;
                }
                int max = 360 * 5000;
                translate3.interpolate((translate2.getValueNoTrans() <= 0) ? -max : max, -Math.abs(translate2.getValueNoTrans()) * 0.3f);
                if (Math.abs(translate3.getValueNoTrans()) >= max) {
                    translate3.setValue(0);
                }
            }
        }
        drawTargetsTicks();
        super.onWindowUpdateEvent(event);
    }

    @Override
    public void onRenderShaderEvent(RenderShaderEvent event) {
        if(framebuffer == null) return;
//        Bloom.renderBlur(framebuffer.framebufferTexture, 2, 2);
        framebuffer.framebufferClear();
        mc.getFramebuffer().bindFramebuffer(true);
    }

    public void draw(){
        if(attackTarget == null) return;
        GlStateManager.pushMatrix();

        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

        float f = RenderUtils.getRenderPos().playerViewY;
        float f1 = RenderUtils.getRenderPos().playerViewX;

        double camX = RenderUtils.getRenderPos().renderPosX;
        double camY = RenderUtils.getRenderPos().renderPosY;
        double camZ = RenderUtils.getRenderPos().renderPosZ;
        double n = MathHelper.lerp(mc.timer.renderPartialTicks, attackTarget.lastTickPosX,  attackTarget.getPosX());
        final double ex = n - camX;
        double n2 = MathHelper.lerp(mc.timer.renderPartialTicks, attackTarget.lastTickPosY + 1,  attackTarget.getPosY() + 1);
        final double ey = n2 - camY;
        double n3 = MathHelper.lerp(mc.timer.renderPartialTicks, attackTarget.lastTickPosZ,  attackTarget.getPosZ());
        final double ez = n3 - camZ;

        GlStateManager.pushMatrix();

        GlStateManager.translate(ex, ey, ez);
        GlStateManager.rotate(-f, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(f1, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(translate3.getValue(), 0f, 0.0F, 1F);

        double scale = 0.025F;
        GlStateManager.scale(-scale, -scale, 1f);

        GlStateManager.resetColor();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        float a = 0.75f;
        Gradient.applyGradient(-24 * 11, -24 * 11, 48 * 22, 48 * 22,0.8f,
                ColorUtils.blend(ColorChanger.getColor(0, 10), new Color(255,255,255), 0.8f),
                ColorUtils.blend(ColorChanger.getColor(50, 10), new Color(255,255,255), 0.8f),
                ColorUtils.blend(ColorChanger.getColor(100, 10), new Color(255,255,255), 0.8f),
                ColorUtils.blend(ColorChanger.getColor(150, 10), new Color(255,255,255), 0.8f),
               () -> RenderUtils.drawTextureLocationZoom(-24, -24, 48, 48, "target",
                       ColorUtils.reAlpha(
                               new Color(255,255,255),
                               1)));
//        Gradient.applyGradient(-24 * 11, -24 * 11, 48 * 22, 48 * 22,0.8f,
//                ColorUtils.blend(ColorChanger.getColor(0, 10), new Color(255,255,255), 0.8f),
//                ColorUtils.blend(ColorChanger.getColor(50, 10), new Color(255,255,255), 0.8f),
//                ColorUtils.blend(ColorChanger.getColor(100, 10), new Color(255,255,255), 0.8f),
//                ColorUtils.blend(ColorChanger.getColor(150, 10), new Color(255,255,255), 0.8f),
//               () ->
                       RenderUtils.drawTextureLocationZoom(-24, -24, 48, 48, "target",
                       ColorUtils.reAlpha(
                               new Color(255,255,255),
                               0.41f));
//    );


        GlStateManager.popMatrix();

        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
//        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.resetColor();

        GlStateManager.popMatrix();
    }
    Framebuffer framebuffer;
    @Override
    public void onRender3DEvent(Render3DEvent event) {
        if(render.isEnable()) {
            if(espMode.is("Sigma") || !PremiumManager.isPremium){
                drawTargets();
            }else{
                draw();
            }
        }
        super.onRender3DEvent(event);
    }

    @Native
    @Override
    public void onClickEvent(ClickEvent event) {
        if(Variable.stop_killaura){
            Variable.stop_killaura = false;
            return;
        }
        lastRotation = null;
        targets.clear();
        seed = mc.player.onGround ? 1.0f : 0f;
        boolean refind = false;
        if(!limit.isEnable()) {
            refind = true;
        }else{
            if (attackTarget == null || !isTargetEnable(attackTarget) || mc.player.getDistanceNearest(attackTarget) > range.getValue().floatValue()) {
                attackTarget = null;
                refind = true;
            }
        }
//        if(attackTarget != null)
//            ChatUtils.sendMessageWithPrefix(attackTarget.getDistanceNearest(mc.player) + "m");
        if(refind){
            attackTarget = null;
            for (Entity entity : mc.world.getLoadedEntityList()) {
                if (!(entity instanceof LivingEntity)) continue;
                LivingEntity e = (LivingEntity) entity;
                if (!isTargetEnable(e)) continue;
                if (mc.player.getDistanceNearest(entity) > range.getValue().floatValue()) continue;
                targets.add(e);
            }
        }
        if(this.movementFix.is("Strict") && (mc.player.fallDistance > 0 || canFallHit) && fallhit.getValue() && attackTarget != null){
            mc.player.setSprinting(false);
            mc.gameSettings.keyBindSprint.pressed = false;
        }
        switch (SortMode.getValue()) {
            case "Distance":
                targets.sort(Comparator.comparingDouble(mc.player::getDistance));
                break;
            case "Health":
                targets.sort(Comparator.comparingDouble(LivingEntity::getHealth));
                break;
            case "Armor":
                targets.sort(Comparator.comparingDouble(LivingEntity::getTotalArmorValue));
                break;
            case "RArmor":
                targets.sort(Comparator.comparingDouble(LivingEntity::getTotalArmorValue).reversed());
                break;
        }
        switch (mode.getValue()){
            case "Single":
                if(targets.size() > 0)
                    attackTarget = targets.get(0);
                break;
            case "Random":
                if(targets.size() > 0)
                    attackTarget = targets.get(new Random().nextInt(targets.size()));
                break;
            case "Switch":
                if(targets.size() > 0){
                    if(mc.player.ticksExisted % switchTime2.getValue().intValue() == 0)
                        switchTime ++;
                    if(switchTime > targets.size() - 1){
                        switchTime = 0;
                    }
                    attackTarget = targets.get(switchTime);
                }else switchTime = 0;
                break;
        }
        float calcSpeed = (float) (minTurnSpeed.getValue().floatValue() + RotationUtils.random.nextDouble() * (maxTurnSpeed.getValue().floatValue() - minTurnSpeed.getValue().floatValue() + 1));
        if(attackTarget == null){
            Rotation calc = RotationUtils.limitAngleChange(new Rotation(mc.player.lastReportedYaw, mc.player.lastReportedPitch), new Rotation(mc.player.rotationYaw, mc.player.rotationPitch), calcSpeed);
            if(smoothBack.isEnable() && RotationUtils.getAngleDifference(calc.getYaw(), mc.player.lastReportedYaw) >= 5) {
                float[] calc2 = RotationUtils.mouseSens(calc.getYaw(), calc.getPitch(), mc.player.lastReportedYaw, mc.player.lastReportedPitch);
                lastRotation = new float[]{calc2[0], calc2[1]};
                RotationUtils.movementFixYaw = lastRotation[0];
                RotationUtils.movementFixPitch = lastRotation[1];
                RotationUtils.fixing = isMoveFix();
                RotationUtils.slient = slient.isEnable();
            }
        }
        if(attackTarget != null){
            cacheAttackTarget = attackTarget;
            // todo 视觉
            if(isABEnable()) {
                OldHitting.blocking = true;
            }
            float[] rotations = null;
            double calcY = attackTarget.getPosY() + attackTarget.getEyeHeight() - 0.019999999552965164;
            final double x = attackTarget.getPosX();
            final double z = attackTarget.getPosZ();
            Rotation NCPRot = RotationUtils.toRotation(new Vector3d(x, calcY, z));
            Rotation rots = NCPRotation.NCPRotation(attackTarget);
            switch (rotation.getValue()){
                case "None":
                    rotations = new float[]{mc.player.prevRotationYaw, mc.player.prevRotationPitch};
                    break;
                case "Matrix":
                    if (attackTarget.getBoundingBox().contains(mc.player.getEyePosition(1F))) {
                        rotations = null;
                    }else {
                        Rotation matrix = NCPRotation.NCPRotation(attackTarget);
                        rotations = new float[]{matrix.getYaw(), matrix.getPitch()};
//                        if (mc.player.lastTickPosX != mc.player.getPosX() || mc.player.lastTickPosZ != mc.player.getPosZ() || mc.player.lastTickPosY != mc.player.getPosY()) {
                            rotations[0] += (float) (2f * Math.sqrt(Math.min(Math.abs(mc.player.getPosY() - mc.player.lastTickPosY) * 1.444f, 4)) + Math.random() * 0.66666666666 * 2);
                            rotations[1] += (float) (2f * Math.sqrt(Math.min(Math.abs(mc.player.getPosX() - mc.player.lastTickPosX) * 1.444f + Math.abs(mc.player.getPosZ() - mc.player.lastTickPosZ) * 1.444f, 4)) + Math.random() * 0.66666666666 * 2);
//                        }
                        rotations[0] = (rotations[0] + RandomUtil.nextFloat(-1, 2) +( mc.player.ticksExisted % 20) / 20f);
                        rotations[1] = (rotations[1] + RandomUtil.nextFloat(-1, 2) +( mc.player.ticksExisted % 20) / 20f);
                        float sb = 欫좯콵甐鶲㥇(Math.abs(RotationUtils.getAngleDifference(mc.player.lastReportedYaw, rotations[0])) / 190f, 0.33, 0.456, 0.14, 0.5665775);
                        float sb2 = 欫좯콵甐鶲㥇(Math.abs(mc.player.lastReportedYaw - rotations[1]) / 45f, 0.33, 0.456, 0.14, 0.5665775);
                        Rotation limitM = RotationUtils.limitAngleChange(new Rotation(mc.player.lastReportedYaw, mc.player.lastReportedPitch), new Rotation(rotations[0], rotations[1]), sb * 100 + 40, sb2 * 40 + 10);
                        rotations = new float[]{limitM.getYaw(), limitM.getPitch()};
                        rotations = RotationUtils.mouseSens(rotations[0], rotations[1], mc.player.lastReportedYaw, mc.player.lastReportedPitch);
                        if(Math.abs(RotationUtils.getAngleDifference(rotations[0], mc.player.lastReportedYaw)) <= 1 && Math.abs(RotationUtils.getAngleDifference(rotations[1], mc.player.lastReportedPitch)) <= 1){
                            rotations[0] = mc.player.lastReportedYaw;
                            rotations[1] = mc.player.lastReportedPitch;
                        }
                    }
                    break;
                case "NCP":
                    Rotation NCP2 = NCPRotation.NCPRotation(attackTarget);
                    rotations = new float[]{NCP2.getYaw(), NCP2.getPitch()};
                    rotations = RotationUtils.mouseSens(rotations[0], rotations[1], mc.player.lastReportedYaw, mc.player.lastReportedPitch);
//                    RotationUtils.SMOOTH_BACK_TICK = 2;
                    break;
                case "Custom":
                    Rotation calc = null;
                    switch (customRotationMode.getValue()){
                        case "Nearest":
                            if(rots == null) break;
                            calc = rots;
                            break;
                        case "EyeHeight":
                            calc = NCPRot;
                            break;
                        case "OnlyYaw":
                            calc = NCPRot;
                            calc.setPitch(mc.player.rotationPitch);
                            break;
                    }
                    if(calc == null) break;
                    switch (customAddonsMode.getValue()){
                        case "None":
                            break;
                        case "Random":
                            calc.setYaw(calc.getYaw() + RandomUtil.nextFloat(-1, 2));
                            calc.setPitch(calc.getPitch() + RandomUtil.nextFloat(-1, 2));
                            break;
                        case "Random2":
                            calc.setYaw(calc.getYaw() + RandomUtil.nextFloat(-1, 2));
                            calc.setPitch(calc.getPitch() + RandomUtil.nextFloat(-1, 2));
                            calc.setYaw(calc.getYaw() + (mc.player.ticksExisted % 20 - 10) / 10f * 2);
                            calc.setPitch(calc.getPitch() + (mc.player.ticksExisted % 20 - 10) / 10f * 4);
                            break;
                        case "SupRandom":
                            calc.setYaw(calc.getYaw() + RandomUtil.nextFloat(-5, 10));
                            calc.setPitch(calc.getPitch() + RandomUtil.nextFloat(-5, 10));
                            break;
                        case "TickRandom":
                            calc.setYaw(calc.getYaw() + (mc.player.ticksExisted % 20 - 10) / 10f * 2);
                            calc.setPitch(calc.getPitch() + (mc.player.ticksExisted % 20 - 10) / 10f * 4);
                            break;
                        case "MatrixRandom":
                            calc.setYaw(calc.getYaw() + RotationUtils.lastRandomDeltaRotation[0]);
                            calc.setPitch(calc.getPitch() + RotationUtils.lastRandomDeltaRotation[1]);
                            break;
                        case "SmalRandom":
                            calc.setYaw(calc.getYaw() + RandomUtil.nextFloat(-0.01, 0.02));
                            calc.setPitch(calc.getPitch() + RandomUtil.nextFloat(-0.01, 0.02));
                            break;
                    }
                    switch (customExtendsMode.getValue()){
                        case "None":
                            break;
                        case "Int":
                            calc.setYaw((int) calc.getYaw());
                            calc.setPitch((int) calc.getPitch());
                            break;
                        case "DelayInt":
                            if(mc.player.ticksExisted % 2 == 0){
                                calc.setYaw((int) calc.getYaw());
                                calc.setPitch((int) calc.getPitch());
                            }
                            break;
                        case "DelayMove":
                            if(mc.player.ticksExisted % 2 == 0){
                                calc.setYaw(mc.player.lastReportedYaw);
                                calc.setPitch(mc.player.lastReportedPitch);
                            }
                            break;
                        case "Digit":
                            calc.setYaw((float)(Math.round(calc.getYaw()*10f))/10f);
                            calc.setPitch((float)(Math.round(calc.getPitch()*10f))/10f);
                            break;
                    }
                    // fixed
                    calc.setYaw(MathHelper.wrapAngleTo180_float(calc.getYaw()));
                    calc.setPitch(Math.max(Math.min(calc.getPitch(), 90), -90));
                    switch (customSmoothMode.getValue()){
                        case "Instant":
                            break;
                        case "MouseSens":
                            float[] listRots = new float[]{calc.getYaw(), calc.getPitch()};
                            listRots = RotationUtils.mouseSens(listRots[0], listRots[1], mc.player.lastReportedYaw, mc.player.lastReportedPitch);
                            calc = new Rotation(listRots[0], listRots[1]);
                            break;
                        case "Custom":
                            calc = RotationUtils.limitAngleChange(new Rotation(mc.player.lastReportedYaw, mc.player.lastReportedPitch), calc, calcSpeed);
                            break;
                        case "Both":
                            calc = RotationUtils.limitAngleChange(new Rotation(mc.player.lastReportedYaw, mc.player.lastReportedPitch), calc, calcSpeed);
                            float[] listRots2 = new float[]{calc.getYaw(), calc.getPitch()};
                            listRots2 = RotationUtils.mouseSens(listRots2[0], listRots2[1], mc.player.lastReportedYaw, mc.player.lastReportedPitch);
                            calc = new Rotation(listRots2[0], listRots2[1]);
                            break;
                    }
                    rotations = new float[]{calc.getYaw(), calc.getPitch()};
                    break;
            }
            if(!snap.isEnable() || snapTimeTicks > 0) {
                if(snapTimeTicks > 0) snapTimeTicks --;
                lastRotation = rotations;
            }
        }else{
            snapTimeTicks = 0;
            unBlock();
            // no target
            switch (rotation.getValue()) {
                case "Intave2":
                    break;
            }
        }
//        if(smoothBack.isEnable()){
//            RotationUtils.SMOOTH_BACK_TICK = 10;
//        }
        if(lastRotation != null) {
//            if (isMoveFix()) {
                RotationUtils.movementFixYaw = lastRotation[0];
                RotationUtils.movementFixPitch = lastRotation[1];
                RotationUtils.fixing = isMoveFix();
                RotationUtils.slient = slient.isEnable();
//            }
        }else{
        }
        if(attackTarget != null && lastRotation != null){
            if (shieldBreakCooldown == 0 && (!raytrace.getValue() || RotationUtils.isMouseOver(lastRotation[0], lastRotation[1], attackTarget, range2.getValue().floatValue(), 0, TraceMode.is("Target"))) && attackTarget instanceof PlayerEntity && attackTarget.getHeldItemOffhand().getItem() instanceof ShieldItem && attackTarget.isHandActive()) {
                if (shieldBreaker.getValue()) {
                    int slot = getAxeSlot();
                    if (slot != -1) {
                        mc.getConnection().sendPacket(new CHeldItemChangePacket(slot));
                        mc.playerController.attackEntity(mc.player, attackTarget);
                        mc.player.swing(Hand.MAIN_HAND, false);
                        mc.getConnection().sendPacket(new CHeldItemChangePacket(mc.player.inventory.currentItem));
                        shieldBreakCooldown = 20;
                        return;
                    }
                }
            }
            clicked(lastRotation[0], lastRotation[1]);
        }
        super.onClickEvent(event);
    }
    @Native
    int getAxeSlot() {
        for (int i = 0; i < 9; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() instanceof AxeItem) {
                return i;
            }
        }
        return -1;
    }

    @Native
    @Override
    public void onUpdateEvent(UpdateEvent event) {
        suffix = rotation.getValue();

        if(event.isPre()){
            canFallHit = mc.player.lastTickPosY > event.y && mc.player.fallDistance != Math.ceil(mc.player.fallDistance);
            sprintHit = mc.player.isSprinting();
            if(mc.player.onGround){
                groundTime ++;
            }else groundTime = 0;
            if(lastRotation != null){
                // :/

                setRotations(event, lastRotation);
            }
        }else{
            // todo post
            if(!silent.getValue()){
                if(lastRotation == null) return;
                mc.player.rotationYaw = lastRotation[0];
                mc.player.rotationPitch = lastRotation[1];
                lastRotation = null;
            }
            if(this.movementFix.is("Strict") && (mc.player.fallDistance > 0 || canFallHit) && fallhit.getValue() && attackTarget != null){
                mc.player.setSprinting(false);
                mc.gameSettings.keyBindSprint.pressed = false;
            }
//            if(fallhitPlus.isEnable()){
//            }
        }
        if(!this.movementFix.is("Strict") && mc.player.fallDistance > 0 && fallhit.getValue() && attackTarget != null){
            mc.player.setSprinting(false);
            mc.gameSettings.keyBindSprint.pressed = false;
            AutoSprint.sprint = false;
        }
        super.onUpdateEvent(event);
    }
    @Native
    public boolean isMoveFix(){
        if(movementFix.is("Strict")){
            return true;
        }else if(movementFix.is("Matrix")){
            return groundTime > 1 && Math.abs(RotationUtils.getAngleDifference(mc.player.rotationYaw, mc.player.lastReportedYaw)) <= 60;
        }else{
            return false;
        }
    }

    @Native
    public void setRotations(UpdateEvent event, float[] rotations){
        if(silent.getValue()) {
            if (isMoveFix()) {
            } else {
//                event.yaw = rotations[0];
//                event.pitch = rotations[1];
            }
        }else{
            lastRotation = rotations;
        }
    }
    HashMap<Entity, ESPTarget> espTargets = new HashMap<>();
    class ESPTarget {
        public Sigma5AnimationUtil anim = new Sigma5AnimationUtil(300, 300);
        public float alpha = 0;
    }
    @Native
    public void drawTargetsTicks() {
        final Iterator<Map.Entry<Entity, ESPTarget>> iterator4 = espTargets.entrySet().iterator();
        ArrayList<Entity> removes = new ArrayList<>();
        while (iterator4.hasNext()) {
            Map.Entry<Entity, ESPTarget> e;
            try {
                e = iterator4.next();
            }catch (ConcurrentModificationException e2){
                continue;
            }
            Entity entity = e.getKey();
            Sigma5AnimationUtil anim = e.getValue().anim;
            e.getValue().alpha = anim.getAnim();
            if(e.getValue().alpha == 0){
                removes.add(entity);
            }
        }
        for(Entity e : removes)
            espTargets.remove(e);
    }
    @Native
    public void drawTargets(){
        if(attackTarget != null){
            if(!espTargets.containsKey(attackTarget)){
                ESPTarget espTarget = new ESPTarget();
                espTargets.put(attackTarget, espTarget);
            }
        }
        final Iterator<Map.Entry<Entity, ESPTarget>> iterator4 = espTargets.entrySet().iterator();
        while (iterator4.hasNext()) {
            Map.Entry<Entity, ESPTarget> e;
            try {
                e = iterator4.next();
            }catch (ConcurrentModificationException e2){
                continue;
            }
            Entity entity = e.getKey();
            Sigma5AnimationUtil anim = e.getValue().anim;
            anim.animTo(entity == attackTarget ? Sigma5AnimationUtil.AnimState.ANIMING : Sigma5AnimationUtil.AnimState.SLEEPING);
            e.getValue().alpha = anim.getAnim();
        }

        for (Map.Entry<Entity, ESPTarget> e : espTargets.entrySet()) {
            Entity entity = e.getKey();
            KillauraESP.drawESP(entity, e.getValue().alpha, renderColor.getColor());
        }
    }
}
