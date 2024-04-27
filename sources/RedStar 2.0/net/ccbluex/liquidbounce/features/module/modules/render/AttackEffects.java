package net.ccbluex.liquidbounce.features.module.modules.render;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import me.sound.SoundPlayer;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.event.AttackEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.server.SPacketSpawnGlobalEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="AttackEffects", description="Rise.", category=ModuleCategory.RENDER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000F\n\n\n\b\n\n\b\n\b\n\u0000\n\n\b\n\n\b\n\n\b\n\n\u0000\n\n\u0000\n\n\b\b\u0000 20:B¢J020HJ020HR0¢\b\n\u0000\bR0\bX¢\n\u0000R\t0\nX¢\n\u0000R0\nX¢\n\u0000R\f0\rX¢\n\u0000\b\"\bR0X¢\n\u0000\b\"\b¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/render/AttackEffects;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "amount", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "getAmount", "()Lnet/ccbluex/liquidbounce/value/IntegerValue;", "lastAttackedEntity", "", "lightingSoundValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "sound", "target", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;", "getTarget", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;", "setTarget", "(Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;)V", "target2", "Lnet/minecraft/entity/EntityLivingBase;", "getTarget2", "()Lnet/minecraft/entity/EntityLivingBase;", "setTarget2", "(Lnet/minecraft/entity/EntityLivingBase;)V", "onAttack", "", "event", "Lnet/ccbluex/liquidbounce/event/AttackEvent;", "onMotion", "Lnet/ccbluex/liquidbounce/event/MotionEvent;", "Companion", "Pride"})
public final class AttackEffects
extends Module {
    @NotNull
    private final IntegerValue amount = new IntegerValue("Amount", 5, 1, 20);
    private final BoolValue sound = new BoolValue("Sound", true);
    private final BoolValue lightingSoundValue = new BoolValue("LightingSound", true);
    private int lastAttackedEntity;
    @Nullable
    private IEntityLivingBase target;
    @Nullable
    private EntityLivingBase target2;
    @NotNull
    private static final ListValue atksound;
    @NotNull
    private static final ListValue mode;
    public static final Companion Companion;

    @NotNull
    public final IntegerValue getAmount() {
        return this.amount;
    }

    @Nullable
    public final IEntityLivingBase getTarget() {
        return this.target;
    }

    public final void setTarget(@Nullable IEntityLivingBase iEntityLivingBase) {
        this.target = iEntityLivingBase;
    }

    @Nullable
    public final EntityLivingBase getTarget2() {
        return this.target2;
    }

    public final void setTarget2(@Nullable EntityLivingBase entityLivingBase) {
        this.target2 = entityLivingBase;
    }

    @EventTarget
    public final void onAttack(@NotNull AttackEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (event.getTargetEntity() instanceof IEntityLivingBase) {
            this.target = (IEntityLivingBase)event.getTargetEntity();
        }
    }

    @EventTarget
    public final void onMotion(@NotNull MotionEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (event.isPre() && this.target != null) {
            IEntityLivingBase iEntityLivingBase = this.target;
            if (iEntityLivingBase == null) {
                Intrinsics.throwNpe();
            }
            if (iEntityLivingBase.getHurtTime() >= 3) {
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                IEntityLivingBase iEntityLivingBase2 = this.target;
                if (iEntityLivingBase2 == null) {
                    Intrinsics.throwNpe();
                }
                double d = iEntityLivingBase2.getPosX();
                IEntityLivingBase iEntityLivingBase3 = this.target;
                if (iEntityLivingBase3 == null) {
                    Intrinsics.throwNpe();
                }
                double d2 = iEntityLivingBase3.getPosY();
                IEntityLivingBase iEntityLivingBase4 = this.target;
                if (iEntityLivingBase4 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP.getDistance(d, d2, iEntityLivingBase4.getPosZ()) < (double)10) {
                    boolean bl;
                    String string;
                    IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP2 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (iEntityPlayerSP2.getTicksExisted() > 4) {
                        string = (String)atksound.get();
                        bl = false;
                        String string2 = string;
                        if (string2 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                        }
                        String string3 = string2.toLowerCase();
                        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
                        switch (string3) {
                            case "knock": {
                                new SoundPlayer().playSound(SoundPlayer.SoundType.Crack, LiquidBounce.INSTANCE.getModuleManager().getToggleVolume());
                                break;
                            }
                            case "skeet": {
                                new SoundPlayer().playSound(SoundPlayer.SoundType.SKEET, LiquidBounce.INSTANCE.getModuleManager().getToggleVolume());
                                break;
                            }
                            case "neko": {
                                new SoundPlayer().playSound(SoundPlayer.SoundType.NEKO, LiquidBounce.INSTANCE.getModuleManager().getToggleVolume());
                                break;
                            }
                        }
                    }
                    IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP3 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (iEntityPlayerSP3.getTicksExisted() > 3) {
                        string = (String)mode.get();
                        bl = false;
                        String string4 = string;
                        if (string4 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                        }
                        String string5 = string4.toLowerCase();
                        Intrinsics.checkExpressionValueIsNotNull(string5, "(this as java.lang.String).toLowerCase()");
                        switch (string5) {
                            case "blood": {
                                int i;
                                for (i = 0; i < ((Number)this.amount.getValue()).intValue(); ++i) {
                                    WorldClient worldClient = MinecraftInstance.mc2.world;
                                    IEntityLivingBase iEntityLivingBase5 = this.target;
                                    if (iEntityLivingBase5 == null) {
                                        Intrinsics.throwNpe();
                                    }
                                    double d3 = iEntityLivingBase5.getPosX();
                                    IEntityLivingBase iEntityLivingBase6 = this.target;
                                    if (iEntityLivingBase6 == null) {
                                        Intrinsics.throwNpe();
                                    }
                                    double d4 = iEntityLivingBase6.getPosY();
                                    IEntityLivingBase iEntityLivingBase7 = this.target;
                                    if (iEntityLivingBase7 == null) {
                                        Intrinsics.throwNpe();
                                    }
                                    double d5 = d4 + (double)iEntityLivingBase7.getHeight() - 0.75;
                                    IEntityLivingBase iEntityLivingBase8 = this.target;
                                    if (iEntityLivingBase8 == null) {
                                        Intrinsics.throwNpe();
                                    }
                                    double d6 = iEntityLivingBase8.getPosZ();
                                    int[] nArray = new int[1];
                                    Block block = Blocks.REDSTONE_BLOCK;
                                    Intrinsics.checkExpressionValueIsNotNull(block, "Blocks.REDSTONE_BLOCK");
                                    nArray[0] = Block.getStateId((IBlockState)block.getDefaultState());
                                    worldClient.func_175688_a(EnumParticleTypes.BLOCK_CRACK, d3, d5, d6, 0.0, 0.0, 0.0, nArray);
                                }
                                if (!((Boolean)this.sound.get()).booleanValue()) break;
                                MinecraftInstance.mc.getSoundHandler().playSound("minecraft:block.anvil.break", 1.0f);
                                break;
                            }
                            case "criticals": {
                                int i;
                                for (i = 0; i < ((Number)this.amount.getValue()).intValue(); ++i) {
                                    ParticleManager particleManager = MinecraftInstance.mc2.effectRenderer;
                                    int n = EnumParticleTypes.CRIT.getParticleID();
                                    IEntityLivingBase iEntityLivingBase9 = this.target;
                                    if (iEntityLivingBase9 == null) {
                                        Intrinsics.throwNpe();
                                    }
                                    double d7 = iEntityLivingBase9.getPosX();
                                    IEntityLivingBase iEntityLivingBase10 = this.target;
                                    if (iEntityLivingBase10 == null) {
                                        Intrinsics.throwNpe();
                                    }
                                    double d8 = iEntityLivingBase10.getPosY();
                                    IEntityLivingBase iEntityLivingBase11 = this.target;
                                    if (iEntityLivingBase11 == null) {
                                        Intrinsics.throwNpe();
                                    }
                                    double d9 = iEntityLivingBase11.getPosZ();
                                    IEntityLivingBase iEntityLivingBase12 = this.target;
                                    if (iEntityLivingBase12 == null) {
                                        Intrinsics.throwNpe();
                                    }
                                    double d10 = iEntityLivingBase12.getPosX();
                                    IEntityLivingBase iEntityLivingBase13 = this.target;
                                    if (iEntityLivingBase13 == null) {
                                        Intrinsics.throwNpe();
                                    }
                                    double d11 = iEntityLivingBase13.getPosY();
                                    IEntityLivingBase iEntityLivingBase14 = this.target;
                                    if (iEntityLivingBase14 == null) {
                                        Intrinsics.throwNpe();
                                    }
                                    particleManager.spawnEffectParticle(n, d7, d8, d9, d10, d11, iEntityLivingBase14.getPosZ(), new int[0]);
                                }
                                break;
                            }
                            case "magic": {
                                int i;
                                for (i = 0; i < ((Number)this.amount.getValue()).intValue(); ++i) {
                                    ParticleManager particleManager = MinecraftInstance.mc2.effectRenderer;
                                    int n = EnumParticleTypes.CRIT_MAGIC.getParticleID();
                                    IEntityLivingBase iEntityLivingBase15 = this.target;
                                    if (iEntityLivingBase15 == null) {
                                        Intrinsics.throwNpe();
                                    }
                                    double d12 = iEntityLivingBase15.getPosX();
                                    IEntityLivingBase iEntityLivingBase16 = this.target;
                                    if (iEntityLivingBase16 == null) {
                                        Intrinsics.throwNpe();
                                    }
                                    double d13 = iEntityLivingBase16.getPosY();
                                    IEntityLivingBase iEntityLivingBase17 = this.target;
                                    if (iEntityLivingBase17 == null) {
                                        Intrinsics.throwNpe();
                                    }
                                    double d14 = iEntityLivingBase17.getPosZ();
                                    IEntityLivingBase iEntityLivingBase18 = this.target;
                                    if (iEntityLivingBase18 == null) {
                                        Intrinsics.throwNpe();
                                    }
                                    double d15 = iEntityLivingBase18.getPosX();
                                    IEntityLivingBase iEntityLivingBase19 = this.target;
                                    if (iEntityLivingBase19 == null) {
                                        Intrinsics.throwNpe();
                                    }
                                    double d16 = iEntityLivingBase19.getPosY();
                                    IEntityLivingBase iEntityLivingBase20 = this.target;
                                    if (iEntityLivingBase20 == null) {
                                        Intrinsics.throwNpe();
                                    }
                                    particleManager.spawnEffectParticle(n, d12, d13, d14, d15, d16, iEntityLivingBase20.getPosZ(), new int[0]);
                                }
                                break;
                            }
                            case "lighting": {
                                INetHandlerPlayClient iNetHandlerPlayClient = MinecraftInstance.mc.getNetHandler2();
                                World world = (World)MinecraftInstance.mc2.world;
                                IEntityLivingBase iEntityLivingBase21 = this.target;
                                if (iEntityLivingBase21 == null) {
                                    Intrinsics.throwNpe();
                                }
                                double d17 = iEntityLivingBase21.getPosX();
                                IEntityLivingBase iEntityLivingBase22 = this.target;
                                if (iEntityLivingBase22 == null) {
                                    Intrinsics.throwNpe();
                                }
                                double d18 = iEntityLivingBase22.getPosY();
                                IEntityLivingBase iEntityLivingBase23 = this.target;
                                if (iEntityLivingBase23 == null) {
                                    Intrinsics.throwNpe();
                                }
                                iNetHandlerPlayClient.handleSpawnGlobalEntity(new SPacketSpawnGlobalEntity((Entity)new EntityLightningBolt(world, d17, d18, iEntityLivingBase23.getPosZ(), true)));
                                if (!((Boolean)this.lightingSoundValue.get()).booleanValue()) break;
                                MinecraftInstance.mc.getSoundHandler().playSound("entity.lightning.impact", 0.5f);
                                break;
                            }
                            case "smoke": {
                                ParticleManager particleManager = MinecraftInstance.mc2.effectRenderer;
                                int n = EnumParticleTypes.SMOKE_NORMAL.getParticleID();
                                IEntityLivingBase iEntityLivingBase24 = this.target;
                                if (iEntityLivingBase24 == null) {
                                    Intrinsics.throwNpe();
                                }
                                double d19 = iEntityLivingBase24.getPosX();
                                IEntityLivingBase iEntityLivingBase25 = this.target;
                                if (iEntityLivingBase25 == null) {
                                    Intrinsics.throwNpe();
                                }
                                double d20 = iEntityLivingBase25.getPosY();
                                IEntityLivingBase iEntityLivingBase26 = this.target;
                                if (iEntityLivingBase26 == null) {
                                    Intrinsics.throwNpe();
                                }
                                double d21 = iEntityLivingBase26.getPosZ();
                                IEntityLivingBase iEntityLivingBase27 = this.target;
                                if (iEntityLivingBase27 == null) {
                                    Intrinsics.throwNpe();
                                }
                                double d22 = iEntityLivingBase27.getPosX();
                                IEntityLivingBase iEntityLivingBase28 = this.target;
                                if (iEntityLivingBase28 == null) {
                                    Intrinsics.throwNpe();
                                }
                                double d23 = iEntityLivingBase28.getPosY();
                                IEntityLivingBase iEntityLivingBase29 = this.target;
                                if (iEntityLivingBase29 == null) {
                                    Intrinsics.throwNpe();
                                }
                                particleManager.spawnEffectParticle(n, d19, d20, d21, d22, d23, iEntityLivingBase29.getPosZ(), new int[0]);
                                break;
                            }
                            case "water": {
                                ParticleManager particleManager = MinecraftInstance.mc2.effectRenderer;
                                int n = EnumParticleTypes.WATER_DROP.getParticleID();
                                IEntityLivingBase iEntityLivingBase30 = this.target;
                                if (iEntityLivingBase30 == null) {
                                    Intrinsics.throwNpe();
                                }
                                double d24 = iEntityLivingBase30.getPosX();
                                IEntityLivingBase iEntityLivingBase31 = this.target;
                                if (iEntityLivingBase31 == null) {
                                    Intrinsics.throwNpe();
                                }
                                double d25 = iEntityLivingBase31.getPosY();
                                IEntityLivingBase iEntityLivingBase32 = this.target;
                                if (iEntityLivingBase32 == null) {
                                    Intrinsics.throwNpe();
                                }
                                double d26 = iEntityLivingBase32.getPosZ();
                                IEntityLivingBase iEntityLivingBase33 = this.target;
                                if (iEntityLivingBase33 == null) {
                                    Intrinsics.throwNpe();
                                }
                                double d27 = iEntityLivingBase33.getPosX();
                                IEntityLivingBase iEntityLivingBase34 = this.target;
                                if (iEntityLivingBase34 == null) {
                                    Intrinsics.throwNpe();
                                }
                                double d28 = iEntityLivingBase34.getPosY();
                                IEntityLivingBase iEntityLivingBase35 = this.target;
                                if (iEntityLivingBase35 == null) {
                                    Intrinsics.throwNpe();
                                }
                                particleManager.spawnEffectParticle(n, d24, d25, d26, d27, d28, iEntityLivingBase35.getPosZ(), new int[0]);
                                break;
                            }
                            case "heart": {
                                ParticleManager particleManager = MinecraftInstance.mc2.effectRenderer;
                                int n = EnumParticleTypes.HEART.getParticleID();
                                IEntityLivingBase iEntityLivingBase36 = this.target;
                                if (iEntityLivingBase36 == null) {
                                    Intrinsics.throwNpe();
                                }
                                double d29 = iEntityLivingBase36.getPosX();
                                IEntityLivingBase iEntityLivingBase37 = this.target;
                                if (iEntityLivingBase37 == null) {
                                    Intrinsics.throwNpe();
                                }
                                double d30 = iEntityLivingBase37.getPosY();
                                IEntityLivingBase iEntityLivingBase38 = this.target;
                                if (iEntityLivingBase38 == null) {
                                    Intrinsics.throwNpe();
                                }
                                double d31 = iEntityLivingBase38.getPosZ();
                                IEntityLivingBase iEntityLivingBase39 = this.target;
                                if (iEntityLivingBase39 == null) {
                                    Intrinsics.throwNpe();
                                }
                                double d32 = iEntityLivingBase39.getPosX();
                                IEntityLivingBase iEntityLivingBase40 = this.target;
                                if (iEntityLivingBase40 == null) {
                                    Intrinsics.throwNpe();
                                }
                                double d33 = iEntityLivingBase40.getPosY();
                                IEntityLivingBase iEntityLivingBase41 = this.target;
                                if (iEntityLivingBase41 == null) {
                                    Intrinsics.throwNpe();
                                }
                                particleManager.spawnEffectParticle(n, d29, d30, d31, d32, d33, iEntityLivingBase41.getPosZ(), new int[0]);
                                break;
                            }
                            case "fire": {
                                ParticleManager particleManager = MinecraftInstance.mc2.effectRenderer;
                                int n = EnumParticleTypes.LAVA.getParticleID();
                                IEntityLivingBase iEntityLivingBase42 = this.target;
                                if (iEntityLivingBase42 == null) {
                                    Intrinsics.throwNpe();
                                }
                                double d34 = iEntityLivingBase42.getPosX();
                                IEntityLivingBase iEntityLivingBase43 = this.target;
                                if (iEntityLivingBase43 == null) {
                                    Intrinsics.throwNpe();
                                }
                                double d35 = iEntityLivingBase43.getPosY();
                                IEntityLivingBase iEntityLivingBase44 = this.target;
                                if (iEntityLivingBase44 == null) {
                                    Intrinsics.throwNpe();
                                }
                                double d36 = iEntityLivingBase44.getPosZ();
                                IEntityLivingBase iEntityLivingBase45 = this.target;
                                if (iEntityLivingBase45 == null) {
                                    Intrinsics.throwNpe();
                                }
                                double d37 = iEntityLivingBase45.getPosX();
                                IEntityLivingBase iEntityLivingBase46 = this.target;
                                if (iEntityLivingBase46 == null) {
                                    Intrinsics.throwNpe();
                                }
                                double d38 = iEntityLivingBase46.getPosY();
                                IEntityLivingBase iEntityLivingBase47 = this.target;
                                if (iEntityLivingBase47 == null) {
                                    Intrinsics.throwNpe();
                                }
                                particleManager.spawnEffectParticle(n, d34, d35, d36, d37, d38, iEntityLivingBase47.getPosZ(), new int[0]);
                                break;
                            }
                        }
                    }
                    this.target = null;
                }
            }
        }
    }

    static {
        Companion = new Companion(null);
        atksound = new ListValue("AttackSound", new String[]{"None", "Skeet", "Neko", "Knock"}, "None");
        mode = new ListValue("Mode", new String[]{"Blood", "Lighting", "Fire", "Heart", "Water"}, "Blood");
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\u0000\n\b\n\n\b\b\u000020B\b¢R0¢\b\n\u0000\bR0¢\b\n\u0000\b\b¨\t"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/render/AttackEffects$Companion;", "", "()V", "atksound", "Lnet/ccbluex/liquidbounce/value/ListValue;", "getAtksound", "()Lnet/ccbluex/liquidbounce/value/ListValue;", "mode", "getMode", "Pride"})
    public static final class Companion {
        @NotNull
        public final ListValue getAtksound() {
            return atksound;
        }

        @NotNull
        public final ListValue getMode() {
            return mode;
        }

        private Companion() {
        }

        public Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}
