/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.server.S0BPacketAnimation
 *  net.minecraft.network.play.server.S14PacketEntity
 *  net.minecraft.network.play.server.S18PacketEntityTeleport
 *  net.minecraft.network.play.server.S19PacketEntityStatus
 *  net.minecraft.potion.Potion
 *  net.minecraft.potion.PotionEffect
 *  net.minecraft.world.World
 *  org.jetbrains.annotations.NotNull
 */
package me.report.liquidware.modules.fun;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.math.MathKt;
import kotlin.ranges.RangesKt;
import me.report.liquidware.modules.fun.HackerData;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.event.WorldEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.misc.SoundFxPlayer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import obfuscator.NativeMethod;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="HackerDetector", category=ModuleCategory.FUN, description="Auto cheater check.")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000~\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0015\u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0017H\u0002J\u0010\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001cH\u0002J\u0010\u0010\u001d\u001a\u00020\u001a2\u0006\u0010\u001e\u001a\u00020\tH\u0002J\b\u0010\u001f\u001a\u00020\u001aH\u0002J\u000e\u0010 \u001a\u00020!2\u0006\u0010\u001b\u001a\u00020\u0017J\b\u0010\"\u001a\u00020\u001aH\u0016J\u0010\u0010#\u001a\u00020\u001a2\u0006\u0010$\u001a\u00020%H\u0007J\u0010\u0010&\u001a\u00020\u001a2\u0006\u0010$\u001a\u00020'H\u0007J\u0010\u0010(\u001a\u00020\u001a2\u0006\u0010$\u001a\u00020)H\u0007J$\u0010*\u001a\u00020\u001a*\u00020\n2\u0006\u0010+\u001a\u00020\u000f2\u0006\u0010,\u001a\u00020-2\u0006\u0010.\u001a\u00020\u000fH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R*\u0010\u0007\u001a\u001e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n0\bj\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n`\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010\r\u001a\u0012\u0012\u0004\u0012\u00020\u000f0\u000ej\b\u0012\u0004\u0012\u00020\u000f`\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006/"}, d2={"Lme/report/liquidware/modules/fun/HackerDetector;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "GRAVITY_FRICTION", "", "combatCheck", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "datas", "Ljava/util/HashMap;", "Lnet/minecraft/entity/player/EntityPlayer;", "Lme/report/liquidware/modules/fun/HackerData;", "Lkotlin/collections/HashMap;", "debugMode", "hackers", "Ljava/util/ArrayList;", "", "Lkotlin/collections/ArrayList;", "movementCheck", "notify", "vlValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "calculateYawDifference", "from", "Lnet/minecraft/entity/EntityLivingBase;", "to", "checkCombatHurt", "", "entity", "Lnet/minecraft/entity/Entity;", "checkPlayer", "player", "doGC", "isHacker", "", "onEnable", "onPacket", "event", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "onWorld", "Lnet/ccbluex/liquidbounce/event/WorldEvent;", "flag", "type", "vl", "", "msg", "KyinoClient"})
public final class HackerDetector
extends Module {
    private final double GRAVITY_FRICTION;
    private final BoolValue combatCheck = new BoolValue("Combat", true);
    private final BoolValue movementCheck = new BoolValue("Movement", true);
    private final BoolValue debugMode = new BoolValue("Debug", false);
    private final BoolValue notify = new BoolValue("Notifications", true);
    private final IntegerValue vlValue = new IntegerValue("VL", 300, 100, 500);
    private final HashMap<EntityPlayer, HackerData> datas = new HashMap();
    private final ArrayList<String> hackers = new ArrayList();

    @EventTarget
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        new Thread(new Runnable(this){
            final /* synthetic */ HackerDetector this$0;

            public final void run() {
                HackerDetector.access$doGC(this.this$0);
            }
            {
                this.this$0 = hackerDetector;
            }
        }).start();
    }

    private final void doGC() {
        ArrayList<EntityPlayer> needRemove = new ArrayList<EntityPlayer>();
        Map map = this.datas;
        boolean bl = false;
        Iterator iterator2 = map.entrySet().iterator();
        while (iterator2.hasNext()) {
            Map.Entry entry;
            Map.Entry entry2 = entry = iterator2.next();
            boolean bl2 = false;
            EntityPlayer player = (EntityPlayer)entry2.getKey();
            if (!player.field_70128_L) continue;
            needRemove.add(player);
        }
        for (EntityPlayer player : needRemove) {
            this.datas.remove(player);
            if (!((Boolean)this.debugMode.get()).booleanValue()) continue;
            StringBuilder stringBuilder = new StringBuilder().append("[GC] REMOVE ");
            EntityPlayer entityPlayer = player;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayer, "player");
            ClientUtils.displayChatMessage(stringBuilder.append(entityPlayer.func_70005_c_()).toString());
        }
    }

    @EventTarget
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (event.getPacket() instanceof S19PacketEntityStatus) {
            Packet<?> packet = event.getPacket();
            if (((Boolean)this.combatCheck.get()).booleanValue() && ((S19PacketEntityStatus)packet).func_149160_c() == 2) {
                new Thread(new Runnable(this, packet){
                    final /* synthetic */ HackerDetector this$0;
                    final /* synthetic */ Packet $packet;

                    public final void run() {
                        Entity entity = ((S19PacketEntityStatus)this.$packet).func_149161_a((World)HackerDetector.access$getMc$p$s1046033730().field_71441_e);
                        Intrinsics.checkExpressionValueIsNotNull(entity, "packet.getEntity(mc.theWorld)");
                        HackerDetector.access$checkCombatHurt(this.this$0, entity);
                    }
                    {
                        this.this$0 = hackerDetector;
                        this.$packet = packet;
                    }
                }).start();
            }
        } else if (event.getPacket() instanceof S0BPacketAnimation) {
            HackerData data;
            Packet<?> packet = event.getPacket();
            Entity entity = HackerDetector.access$getMc$p$s1046033730().field_71441_e.func_73045_a(((S0BPacketAnimation)packet).func_148978_c());
            if (!(entity instanceof EntityPlayer) || ((S0BPacketAnimation)packet).func_148977_d() != 0) {
                return;
            }
            HackerData hackerData = this.datas.get(entity);
            if (hackerData == null) {
                return;
            }
            Intrinsics.checkExpressionValueIsNotNull(hackerData, "datas[entity] ?: return");
            HackerData hackerData2 = data = hackerData;
            int n = hackerData2.getTempAps();
            hackerData2.setTempAps(n + 1);
        } else if (((Boolean)this.movementCheck.get()).booleanValue()) {
            if (event.getPacket() instanceof S18PacketEntityTeleport) {
                Packet<?> packet = event.getPacket();
                Entity entity = HackerDetector.access$getMc$p$s1046033730().field_71441_e.func_73045_a(((S18PacketEntityTeleport)packet).func_149451_c());
                if (!(entity instanceof EntityPlayer)) {
                    return;
                }
                new Thread(new Runnable(this, entity){
                    final /* synthetic */ HackerDetector this$0;
                    final /* synthetic */ Entity $entity;

                    public final void run() {
                        HackerDetector.access$checkPlayer(this.this$0, (EntityPlayer)this.$entity);
                    }
                    {
                        this.this$0 = hackerDetector;
                        this.$entity = entity;
                    }
                }).start();
            } else if (event.getPacket() instanceof S14PacketEntity) {
                Packet<?> packet = event.getPacket();
                Entity entity = ((S14PacketEntity)packet).func_149065_a((World)HackerDetector.access$getMc$p$s1046033730().field_71441_e);
                if (!(entity instanceof EntityPlayer)) {
                    return;
                }
                new Thread(new Runnable(this, entity){
                    final /* synthetic */ HackerDetector this$0;
                    final /* synthetic */ Entity $entity;

                    public final void run() {
                        HackerDetector.access$checkPlayer(this.this$0, (EntityPlayer)this.$entity);
                    }
                    {
                        this.this$0 = hackerDetector;
                        this.$entity = entity;
                    }
                }).start();
            }
        }
    }

    @Override
    public void onEnable() {
        this.datas.clear();
        this.hackers.clear();
    }

    @EventTarget
    public final void onWorld(@NotNull WorldEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        this.datas.clear();
    }

    public final boolean isHacker(@NotNull EntityLivingBase entity) {
        Intrinsics.checkParameterIsNotNull(entity, "entity");
        if (!(entity instanceof EntityPlayer)) {
            return false;
        }
        return this.hackers.contains(((EntityPlayer)entity).func_70005_c_());
    }

    private final void checkPlayer(EntityPlayer player) {
        HackerDetector hackerDetector;
        HackerData hackerData;
        String string;
        int n;
        StringBuilder stringBuilder;
        int ticks22;
        if (player.equals((Object)HackerDetector.access$getMc$p$s1046033730().field_71439_g) || EntityUtils.isFriend((Entity)player)) {
            return;
        }
        if (this.datas.get(player) == null) {
            ((Map)this.datas).put(player, new HackerData(player));
        }
        HackerData hackerData2 = this.datas.get(player);
        if (hackerData2 == null) {
            return;
        }
        Intrinsics.checkExpressionValueIsNotNull(hackerData2, "datas[player] ?: return");
        HackerData data = hackerData2;
        data.update();
        if (data.getAliveTicks() < 20) {
            return;
        }
        int minAirTicks = 10;
        if (player.func_70644_a(Potion.field_76430_j)) {
            PotionEffect potionEffect = player.func_70660_b(Potion.field_76430_j);
            Intrinsics.checkExpressionValueIsNotNull(potionEffect, "player.getActivePotionEffect(Potion.jump)");
            minAirTicks += potionEffect.func_76458_c() * 3;
        }
        double maxMotionY = 0.47;
        double maxOffset = 0.07;
        int triggerBalance = 100;
        int minimumClamp = 1000;
        boolean passed = true;
        long packetTimeNow = System.currentTimeMillis();
        double packetBalance = data.getPacketBalance();
        long rate = packetTimeNow - data.getLastMovePacket();
        packetBalance += 50.0;
        if ((packetBalance -= (double)rate) >= (double)triggerBalance) {
            ticks22 = MathKt.roundToInt(packetBalance / (double)50);
            packetBalance = -1 * (triggerBalance / 2);
            this.flag(data, "timer", 25, "OVERSHOT TIMER " + ticks22);
        } else if (packetBalance < (double)(-1 * minimumClamp)) {
            packetBalance = -1 * minimumClamp;
        }
        if (packetBalance < (double)triggerBalance) {
            data.setPacketBalance(packetBalance);
            data.setLastMovePacket(packetTimeNow);
        }
        if (player.field_70737_aN > 0) {
            ticks22 = player.field_70172_ad;
            if (7 <= ticks22 && 11 >= ticks22 && player.field_70169_q == player.field_70165_t && player.field_70161_v == player.field_70136_U && !HackerDetector.access$getMc$p$s1046033730().field_71441_e.func_72829_c(player.func_174813_aQ().func_72314_b(0.05, 0.0, 0.05))) {
                this.flag(data, "velocity", 50, "NO KNOCKBACK");
            }
            if (7 <= (ticks22 = player.field_70172_ad) && 11 >= ticks22 && player.field_70137_T == player.field_70163_u) {
                this.flag(data, "velocity", 50, "NO KNOCKBACK");
            }
            return;
        }
        if (data.getAps() >= 10) {
            this.flag(data, "killaura", 30, "HIGH APS(aps=" + data.getAps() + ')');
            passed = false;
        }
        if (data.getAps() > 2 && data.getAps() == data.getPreAps() && data.getAps() != 0) {
            this.flag(data, "killaura", 30, "STRANGE APS(aps=" + data.getAps() + ')');
            passed = false;
        }
        float ticks22 = player.field_70177_z - player.field_70126_B;
        boolean bl = false;
        if (Math.abs(ticks22) > (float)50 && player.field_70733_aJ != 0.0f && data.getAps() >= 3) {
            ticks22 = player.field_70177_z - player.field_70126_B;
            stringBuilder = new StringBuilder().append("YAW RATE(aps=").append(data.getAps()).append(",yawRot=");
            n = 30;
            string = "killaura";
            hackerData = data;
            hackerDetector = this;
            bl = false;
            float f = Math.abs(ticks22);
            hackerDetector.flag(hackerData, string, n, stringBuilder.append(f).append(')').toString());
            passed = false;
        }
        if (player.field_70154_o == null && data.getAirTicks() > minAirTicks / 2) {
            double ticks22 = data.getMotionY() - data.getLastMotionY();
            boolean bl2 = false;
            double d = Math.abs(ticks22);
            double d2 = data.getAirTicks() >= 115 ? 0.001 : 0.005;
            if (d < d2) {
                ticks22 = data.getMotionY() - data.getLastMotionY();
                stringBuilder = new StringBuilder().append("GLIDE(diff=");
                n = 20;
                string = "fly";
                hackerData = data;
                hackerDetector = this;
                bl2 = false;
                double d3 = Math.abs(ticks22);
                hackerDetector.flag(hackerData, string, n, stringBuilder.append(d3).append(')').toString());
                passed = false;
            }
            if (data.getMotionY() > maxMotionY) {
                this.flag(data, "fly", 20, "YAXIS(motY=" + data.getMotionY() + ')');
                passed = false;
            }
            if (data.getAirTicks() > minAirTicks && data.getMotionY() > 0.0) {
                this.flag(data, "fly", 30, "YAXIS(motY=" + data.getMotionY() + ')');
                passed = false;
            }
        }
        double d = data.getMotionXZ();
        boolean bl3 = false;
        double distanceXZ = Math.abs(d);
        if (data.getAirTicks() == 0) {
            double limit = 0.37;
            if (data.getGroundTicks() < 5) {
                limit += 0.1;
            }
            if (player.func_70632_aY()) {
                limit *= 0.45;
            }
            if (player.func_70093_af()) {
                limit *= 0.68;
            }
            if (player.func_70644_a(Potion.field_76424_c)) {
                PotionEffect potionEffect = player.func_70660_b(Potion.field_76424_c);
                Intrinsics.checkExpressionValueIsNotNull(potionEffect, "player.getActivePotionEffect(Potion.moveSpeed)");
                limit += (double)potionEffect.func_76458_c();
                limit *= 1.5;
            }
            if (distanceXZ > limit) {
                this.flag(data, "speed", 20, "GROUND SPEED(speed=" + distanceXZ + ",limit=" + limit + ')');
            }
        } else {
            double multiplier;
            double d4 = multiplier = 0.985;
            int n2 = data.getAirTicks() + 1;
            double d5 = 0.36;
            boolean bl4 = false;
            double d6 = Math.pow(d4, n2);
            double predict = d5 * d6;
            if (data.getAirTicks() >= 115) {
                predict = RangesKt.coerceAtLeast(0.08, predict);
            }
            double limit = 0.05;
            if (player.func_70644_a(Potion.field_76424_c)) {
                PotionEffect potionEffect = player.func_70660_b(Potion.field_76424_c);
                Intrinsics.checkExpressionValueIsNotNull(potionEffect, "player.getActivePotionEffect(Potion.moveSpeed)");
                predict += (double)potionEffect.func_76458_c() * 0.05;
                limit *= 1.2;
            }
            if (player.func_70644_a(Potion.field_76430_j)) {
                PotionEffect potionEffect = player.func_70660_b(Potion.field_76430_j);
                Intrinsics.checkExpressionValueIsNotNull(potionEffect, "player.getActivePotionEffect(Potion.jump)");
                predict += (double)potionEffect.func_76458_c() * 0.05;
            }
            if (player.func_70632_aY()) {
                predict *= 0.7;
            }
            if (distanceXZ - predict > limit) {
                this.flag(data, "speed", 20, "AIR SPEED(speed=" + distanceXZ + ",limit=" + limit + ",predict=" + predict + ')');
            }
        }
        if (passed) {
            HackerData hackerData3 = data;
            hackerData3.setVl(hackerData3.getVl() - 1);
        }
    }

    private final void flag(@NotNull HackerData $this$flag, String type, int vl, String msg) {
        if (!$this$flag.getUseHacks().contains(type)) {
            $this$flag.getUseHacks().add(type);
        }
        if (((Boolean)this.debugMode.get()).booleanValue()) {
            ClientUtils.displayChatMessage("\u00a7f" + $this$flag.getPlayer().func_70005_c_() + " \u00a7euse \u00a72" + type + " \u00a77" + msg + " \u00a7c" + $this$flag.getVl() + '+' + vl);
        }
        HackerData hackerData = $this$flag;
        hackerData.setVl(hackerData.getVl() + vl);
        if ($this$flag.getVl() > ((Number)this.vlValue.get()).intValue()) {
            String use = "";
            for (String typ : $this$flag.getUseHacks()) {
                use = use + typ + ',';
            }
            String string = use;
            int n = 0;
            int n2 = use.length() - 1;
            boolean bl = false;
            String string2 = string;
            if (string2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String string3 = string2.substring(n, n2);
            Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
            use = string3;
            ClientUtils.displayChatMessage("\u00a7f" + $this$flag.getPlayer().func_70005_c_() + " \u00a7eusing hack \u00a7a" + use);
            if (((Boolean)this.notify.get()).booleanValue()) {
                new SoundFxPlayer().playSound(SoundFxPlayer.SoundType.VICTORY, -8.0f);
                LiquidBounce.INSTANCE.getHud().addNotification(new Notification($this$flag.getPlayer().func_70005_c_() + " might use hack " + use, Notification.Type.INFO));
            }
            $this$flag.setVl(-((Number)this.vlValue.get()).intValue());
        }
    }

    private final void checkCombatHurt(Entity entity) {
        double yawDiff;
        if (!(entity instanceof EntityLivingBase)) {
            return;
        }
        EntityPlayer attacker = null;
        int attackerCount = 0;
        for (Entity worldEntity : HackerDetector.access$getMc$p$s1046033730().field_71441_e.field_72996_f) {
            if (!(worldEntity instanceof EntityPlayer) || worldEntity.func_70032_d(entity) > (float)7 || worldEntity.equals((Object)entity)) continue;
            ++attackerCount;
            attacker = (EntityPlayer)worldEntity;
        }
        if (attackerCount != 1) {
            return;
        }
        EntityPlayer entityPlayer = attacker;
        if (entityPlayer == null) {
            Intrinsics.throwNpe();
        }
        if (Intrinsics.areEqual(entityPlayer, entity) || EntityUtils.isFriend((Entity)attacker)) {
            return;
        }
        HackerData hackerData = this.datas.get(attacker);
        if (hackerData == null) {
            return;
        }
        Intrinsics.checkExpressionValueIsNotNull(hackerData, "datas[attacker] ?: return");
        HackerData data = hackerData;
        float reach = attacker.func_70032_d(entity);
        if ((double)reach > 3.7) {
            this.flag(data, "killaura", 70, "(reach=" + reach + ')');
        }
        if ((yawDiff = this.calculateYawDifference((EntityLivingBase)attacker, (EntityLivingBase)entity)) > (double)50) {
            this.flag(data, "killaura", 100, "(yawDiff=" + yawDiff + ')');
        }
    }

    private final double calculateYawDifference(EntityLivingBase from, EntityLivingBase to) {
        double d;
        double x = to.field_70165_t - from.field_70165_t;
        double z = to.field_70161_v - from.field_70161_v;
        if (x == 0.0 && z == 0.0) {
            d = from.field_70177_z;
        } else {
            double d2 = -x;
            boolean bl = false;
            double theta = Math.atan2(d2, z);
            double yaw = Math.toDegrees((theta + Math.PI * 2) % (Math.PI * 2));
            double d3 = yaw - (double)from.field_70177_z;
            double d4 = 180;
            boolean bl2 = false;
            double d5 = Math.abs(d3);
            d3 = d5 - (double)180;
            bl2 = false;
            d5 = Math.abs(d3);
            d3 = d4 - d5;
            bl2 = false;
            d = Math.abs(d3);
        }
        return d;
    }

    public HackerDetector() {
        this.GRAVITY_FRICTION = 0.98f;
    }

    public static final /* synthetic */ void access$doGC(HackerDetector $this) {
        $this.doGC();
    }

    public static final /* synthetic */ void access$checkCombatHurt(HackerDetector $this, Entity entity) {
        $this.checkCombatHurt(entity);
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }

    public static final /* synthetic */ void access$checkPlayer(HackerDetector $this, EntityPlayer player) {
        $this.checkPlayer(player);
    }
}

