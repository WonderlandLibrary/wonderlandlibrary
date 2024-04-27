package net.ccbluex.liquidbounce.features.module.modules.player;

import java.util.concurrent.LinkedBlockingQueue;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.JvmField;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DoubleCompanionObject;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.math.MathKt;
import kotlin.reflect.KDeclarationContainer;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.IClassProvider;
import net.ccbluex.liquidbounce.api.enums.BlockType;
import net.ccbluex.liquidbounce.api.enums.EnumFacingType;
import net.ccbluex.liquidbounce.api.enums.ItemType;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.player.IEntityPlayer;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IPlayerControllerMP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.client.network.IINetHandlerPlayClient;
import net.ccbluex.liquidbounce.api.minecraft.item.IItem;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayer;
import net.ccbluex.liquidbounce.api.minecraft.potion.IPotionEffect;
import net.ccbluex.liquidbounce.api.minecraft.potion.PotionType;
import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3i;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.JumpEvent;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.event.WorldEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.render.FreeCam;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.Rotation;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.VecRotation;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.utils.misc.FallingPlayer;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.utils.timer.TickTimer;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="NoFall", description="Prevents you from taking fall damage.", category=ModuleCategory.PLAYER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\b\n\n\u0000\n\n\u0000\n\b\n\b\n\n\b\n\n\b\f\n\n\b\n\n\u0000\n\n\u0000\n\n\b\t\n\n\n\b\n\n\b\r\n\n\b\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\b\u000020B¢JC0\bJD02E0'2F0'JG0JH0J\bI0JHJK0J2\bL0MHJN0J2L0OHJP0J2L0QHJR0J2L0SHJT0J2\bL0UHJV0J2L0WHR0X¢\n\u0000R0X¢\n\u0000R0\bX¢\n\u0000R\t0X¢\n\u0000R\n0\bX¢\n\u0000R0X¢\n\u0000R\f0X¢\n\u0000\b\r\"\bR0\bX¢\n\u0000\b\"\bR0X¢\n\u0000R0\bX¢\n\u0000R0X¢\n\u0000R0\bX¢\n\u0000R0X¢\n\u0000\b\"\bR0X¢\n\u0000R 0X¢\n\u0000R!0X¢\n\u0000R\"0X¢\n\u0000R#0\bX¢\n\u0000R$0\bX¢\n\u0000R%0X¢\n\u0000R&0'X¢\n\u0000R(0X¢\n\u0000R)0*X¢\n\u0000R+0,X¢\n\u0000R-0.8X¢\n\u0000R/0X¢\n\u0000R00X¢\n\u0000\b1\"\b2R30\bX¢\n\u0000R40\bX¢\n\u0000R50X¢\n\u0000R60X¢\n\u0000R7\b0908X¢\n\u0000R:0,X¢\n\u0000R;0<8VX¢\b=>R?0X¢\n\u0000R@0X¢\n\u0000\bA\"\bB¨X"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/player/NoFall;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "aac4Fakelag", "", "aac4FlagCooldown", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "aac4FlagCount", "", "aac5Check", "aac5Timer", "aac5doFlag", "canNoFall", "getCanNoFall", "()Z", "setCanNoFall", "(Z)V", "count", "getCount", "()I", "setCount", "(I)V", "currentMlgBlock", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "currentMlgItemIndex", "currentMlgRotation", "Lnet/ccbluex/liquidbounce/utils/VecRotation;", "currentState", "doSpoof", "getDoSpoof", "setDoSpoof", "fakelag", "isDmgFalling", "jumped", "matrixCanSpoof", "matrixFallTicks", "matrixFlagWait", "matrixIsFall", "matrixLastMotionY", "", "matrixSend", "minFallDistance", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "mlgTimer", "Lnet/ccbluex/liquidbounce/utils/timer/TickTimer;", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "needSpoof", "nextSpoof", "getNextSpoof", "setNextSpoof", "oldaacState", "packet1Count", "packetModify", "packetmodify", "packets", "Ljava/util/concurrent/LinkedBlockingQueue;", "Lnet/ccbluex/liquidbounce/api/minecraft/network/IPacket;", "spartanTimer", "tag", "", "getTag", "()Ljava/lang/String;", "wasTimer", "worldChange", "getWorldChange", "setWorldChange", "getJumpEffect", "inAir", "height", "plus", "inVoid", "isBlockUnder", "onEnable", "", "onJump", "event", "Lnet/ccbluex/liquidbounce/event/JumpEvent;", "onMotionUpdate", "Lnet/ccbluex/liquidbounce/event/MotionEvent;", "onMove", "Lnet/ccbluex/liquidbounce/event/MoveEvent;", "onPacket", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "onWorld", "Lnet/ccbluex/liquidbounce/event/WorldEvent;", "Pride"})
public final class NoFall
extends Module {
    @JvmField
    @NotNull
    public final ListValue modeValue = new ListValue("Mode", new String[]{"Vulcan", "AAC4", "SpoofGround", "NoGround", "Packet", "LoyisaAAC4.4.2", "aac5.0.14", "AAC4.4.X-Flag", "MLG", "AAC", "LAAC", "AAC3.3.11", "AAC3.3.15", "Spartan", "CubeCraft", "Hypixel"}, "MLG");
    private final FloatValue minFallDistance = new FloatValue("MinMLGHeight", 5.0f, 2.0f, 50.0f);
    private final TickTimer spartanTimer = new TickTimer();
    private final TickTimer mlgTimer = new TickTimer();
    private int currentState;
    private boolean aac4Fakelag;
    private boolean jumped;
    private boolean isDmgFalling;
    private VecRotation currentMlgRotation;
    private int currentMlgItemIndex;
    private WBlockPos currentMlgBlock;
    private boolean fakelag;
    private boolean packetmodify;
    private final MSTimer aac4FlagCooldown = new MSTimer();
    private int aac4FlagCount;
    private final LinkedBlockingQueue<IPacket> packets = new LinkedBlockingQueue();
    private int oldaacState;
    private boolean packetModify;
    private boolean aac5doFlag;
    private boolean aac5Check;
    private int aac5Timer;
    private boolean needSpoof;
    private int packet1Count;
    private boolean matrixIsFall;
    private boolean matrixCanSpoof;
    private int matrixFallTicks;
    private double matrixLastMotionY;
    private int matrixFlagWait;
    private boolean wasTimer;
    private boolean matrixSend;
    private boolean canNoFall;
    private boolean nextSpoof;
    private boolean doSpoof;
    private boolean worldChange;
    private int count;

    public final boolean getCanNoFall() {
        return this.canNoFall;
    }

    public final void setCanNoFall(boolean bl) {
        this.canNoFall = bl;
    }

    public final boolean getNextSpoof() {
        return this.nextSpoof;
    }

    public final void setNextSpoof(boolean bl) {
        this.nextSpoof = bl;
    }

    public final boolean getDoSpoof() {
        return this.doSpoof;
    }

    public final void setDoSpoof(boolean bl) {
        this.doSpoof = bl;
    }

    public final boolean getWorldChange() {
        return this.worldChange;
    }

    public final void setWorldChange(boolean bl) {
        this.worldChange = bl;
    }

    public final int getCount() {
        return this.count;
    }

    public final void setCount(int n) {
        this.count = n;
    }

    @Override
    public void onEnable() {
        if (StringsKt.equals((String)this.modeValue.get(), "AAC4", true)) {
            this.fakelag = false;
            this.packetmodify = false;
        }
        this.canNoFall = false;
        this.nextSpoof = false;
        this.doSpoof = false;
        this.worldChange = true;
        this.count = 0;
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget(ignoreCondition=true)
    public final void onUpdate(@Nullable UpdateEvent event) {
        block139: {
            block138: {
                block137: {
                    block136: {
                        v0 = MinecraftInstance.mc.getThePlayer();
                        if (v0 == null) {
                            Intrinsics.throwNpe();
                        }
                        if (v0.getOnGround()) {
                            this.jumped = false;
                        }
                        v1 = MinecraftInstance.mc.getThePlayer();
                        if (v1 == null) {
                            Intrinsics.throwNpe();
                        }
                        if (v1.getMotionY() > (double)false) {
                            this.jumped = true;
                        }
                        if (!this.getState()) break block136;
                        v2 = LiquidBounce.INSTANCE.getModuleManager().getModule(FreeCam.class);
                        if (v2 == null) {
                            Intrinsics.throwNpe();
                        }
                        if (!v2.getState()) break block137;
                    }
                    return;
                }
                v3 = MinecraftInstance.mc.getThePlayer();
                if (v3 == null) {
                    Intrinsics.throwNpe();
                }
                if (BlockUtils.collideBlock(v3.getEntityBoundingBox(), (Function1<? super IBlock, Boolean>)new Function1<Object, Boolean>(MinecraftInstance.classProvider){

                    public final boolean invoke(@Nullable Object p1) {
                        return ((IClassProvider)this.receiver).isBlockLiquid(p1);
                    }

                    public final KDeclarationContainer getOwner() {
                        return Reflection.getOrCreateKotlinClass(IClassProvider.class);
                    }

                    public final String getName() {
                        return "isBlockLiquid";
                    }

                    public final String getSignature() {
                        return "isBlockLiquid(Ljava/lang/Object;)Z";
                    }
                })) break block138;
                v4 = MinecraftInstance.mc.getThePlayer();
                if (v4 == null) {
                    Intrinsics.throwNpe();
                }
                v5 = v4.getEntityBoundingBox().getMaxX();
                v6 = MinecraftInstance.mc.getThePlayer();
                if (v6 == null) {
                    Intrinsics.throwNpe();
                }
                v7 = v6.getEntityBoundingBox().getMaxY();
                v8 = MinecraftInstance.mc.getThePlayer();
                if (v8 == null) {
                    Intrinsics.throwNpe();
                }
                v9 = v8.getEntityBoundingBox().getMaxZ();
                v10 = MinecraftInstance.mc.getThePlayer();
                if (v10 == null) {
                    Intrinsics.throwNpe();
                }
                v11 = v10.getEntityBoundingBox().getMinX();
                v12 = MinecraftInstance.mc.getThePlayer();
                if (v12 == null) {
                    Intrinsics.throwNpe();
                }
                v13 = v12.getEntityBoundingBox().getMinY() - 0.01;
                v14 = MinecraftInstance.mc.getThePlayer();
                if (v14 == null) {
                    Intrinsics.throwNpe();
                }
                if (!BlockUtils.collideBlock(MinecraftInstance.classProvider.createAxisAlignedBB(v5, v7, v9, v11, v13, v14.getEntityBoundingBox().getMinZ()), (Function1<? super IBlock, Boolean>)new Function1<Object, Boolean>(MinecraftInstance.classProvider){

                    public final boolean invoke(@Nullable Object p1) {
                        return ((IClassProvider)this.receiver).isBlockLiquid(p1);
                    }

                    public final KDeclarationContainer getOwner() {
                        return Reflection.getOrCreateKotlinClass(IClassProvider.class);
                    }

                    public final String getName() {
                        return "isBlockLiquid";
                    }

                    public final String getSignature() {
                        return "isBlockLiquid(Ljava/lang/Object;)Z";
                    }
                })) break block139;
            }
            return;
        }
        var2_2 = (String)this.modeValue.get();
        var3_3 = false;
        v15 = var2_2;
        if (v15 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        v16 = v15.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(v16, "(this as java.lang.String).toLowerCase()");
        var2_2 = v16;
        tmp = -1;
        switch (var2_2.hashCode()) {
            case 96323: {
                if (!var2_2.equals("aac")) break;
                tmp = 1;
                break;
            }
            case -2011701869: {
                if (!var2_2.equals("spartan")) break;
                tmp = 2;
                break;
            }
            case 327072346: {
                if (!var2_2.equals("aac5.0.4")) break;
                tmp = 3;
                break;
            }
            case 3313751: {
                if (!var2_2.equals("laac")) break;
                tmp = 4;
                break;
            }
            case 1549308093: {
                if (!var2_2.equals("aac5.0.14")) break;
                tmp = 5;
                break;
            }
            case 1492139161: {
                if (!var2_2.equals("aac3.3.11")) break;
                tmp = 6;
                break;
            }
            case -995865464: {
                if (!var2_2.equals("packet")) break;
                tmp = 7;
                break;
            }
            case -805359837: {
                if (!var2_2.equals("vulcan")) break;
                tmp = 8;
                break;
            }
            case 1492139165: {
                if (!var2_2.equals("aac3.3.15")) break;
                tmp = 9;
                break;
            }
            case 1736568380: {
                if (!var2_2.equals("loyisaaac4.4.2")) break;
                tmp = 3;
                break;
            }
            case -1031473397: {
                if (!var2_2.equals("cubecraft")) break;
                tmp = 10;
                break;
            }
        }
        block13 : switch (tmp) {
            case 7: {
                v17 = MinecraftInstance.mc.getThePlayer();
                if (v17 == null) {
                    Intrinsics.throwNpe();
                }
                if (!(v17.getFallDistance() > 2.0f)) break;
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayer(true));
                break;
            }
            case 10: {
                v18 = MinecraftInstance.mc.getThePlayer();
                if (v18 == null) {
                    Intrinsics.throwNpe();
                }
                if (!(v18.getFallDistance() > 2.0f)) break;
                v19 = MinecraftInstance.mc.getThePlayer();
                if (v19 == null) {
                    Intrinsics.throwNpe();
                }
                v19.setOnGround(false);
                v20 = MinecraftInstance.mc.getThePlayer();
                if (v20 == null) {
                    Intrinsics.throwNpe();
                }
                v20.getSendQueue().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayer(true));
                break;
            }
            case 1: {
                v21 = MinecraftInstance.mc.getThePlayer();
                if (v21 == null) {
                    Intrinsics.throwNpe();
                }
                if (v21.getFallDistance() > 2.0f) {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayer(true));
                    this.currentState = 2;
                } else if (this.currentState == 2) {
                    v22 = MinecraftInstance.mc.getThePlayer();
                    if (v22 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (v22.getFallDistance() < (float)2) {
                        v23 = MinecraftInstance.mc.getThePlayer();
                        if (v23 == null) {
                            Intrinsics.throwNpe();
                        }
                        v23.setMotionY(0.1);
                        this.currentState = 3;
                        return;
                    }
                }
                switch (this.currentState) {
                    case 3: {
                        v24 = MinecraftInstance.mc.getThePlayer();
                        if (v24 == null) {
                            Intrinsics.throwNpe();
                        }
                        v24.setMotionY(0.1);
                        this.currentState = 4;
                        break block13;
                    }
                    case 4: {
                        v25 = MinecraftInstance.mc.getThePlayer();
                        if (v25 == null) {
                            Intrinsics.throwNpe();
                        }
                        v25.setMotionY(0.1);
                        this.currentState = 5;
                        break block13;
                    }
                    case 5: {
                        v26 = MinecraftInstance.mc.getThePlayer();
                        if (v26 == null) {
                            Intrinsics.throwNpe();
                        }
                        v26.setMotionY(0.1);
                        this.currentState = 1;
                        break block13;
                    }
                }
                break;
            }
            case 4: {
                if (this.jumped) break;
                v27 = MinecraftInstance.mc.getThePlayer();
                if (v27 == null) {
                    Intrinsics.throwNpe();
                }
                if (!v27.getOnGround()) break;
                v28 = MinecraftInstance.mc.getThePlayer();
                if (v28 == null) {
                    Intrinsics.throwNpe();
                }
                if (v28.isOnLadder()) break;
                v29 = MinecraftInstance.mc.getThePlayer();
                if (v29 == null) {
                    Intrinsics.throwNpe();
                }
                if (v29.isInWater()) break;
                v30 = MinecraftInstance.mc.getThePlayer();
                if (v30 == null) {
                    Intrinsics.throwNpe();
                }
                if (v30.isInWeb()) break;
                v31 = MinecraftInstance.mc.getThePlayer();
                if (v31 == null) {
                    Intrinsics.throwNpe();
                }
                v31.setMotionY(-6);
                break;
            }
            case 6: {
                v32 = MinecraftInstance.mc.getThePlayer();
                if (v32 == null) {
                    Intrinsics.throwNpe();
                }
                if (!(v32.getFallDistance() > (float)2)) break;
                v33 = MinecraftInstance.mc.getThePlayer();
                if (v33 == null) {
                    Intrinsics.throwNpe();
                }
                v33.setMotionZ(0.0);
                v34 = MinecraftInstance.mc.getThePlayer();
                if (v34 == null) {
                    Intrinsics.throwNpe();
                }
                v35 = MinecraftInstance.mc.getThePlayer();
                if (v35 == null) {
                    Intrinsics.throwNpe();
                }
                v34.setMotionX(v35.getMotionZ());
                v36 = MinecraftInstance.mc.getNetHandler();
                v37 = MinecraftInstance.mc.getThePlayer();
                if (v37 == null) {
                    Intrinsics.throwNpe();
                }
                v38 = v37.getPosX();
                v39 = MinecraftInstance.mc.getThePlayer();
                if (v39 == null) {
                    Intrinsics.throwNpe();
                }
                v40 = v39.getPosY() - 0.001;
                v41 = MinecraftInstance.mc.getThePlayer();
                if (v41 == null) {
                    Intrinsics.throwNpe();
                }
                v42 = v41.getPosZ();
                v43 = MinecraftInstance.mc.getThePlayer();
                if (v43 == null) {
                    Intrinsics.throwNpe();
                }
                v36.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(v38, v40, v42, v43.getOnGround()));
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayer(true));
                break;
            }
            case 9: {
                v44 = MinecraftInstance.mc.getThePlayer();
                if (v44 == null) {
                    Intrinsics.throwNpe();
                }
                if (!(v44.getFallDistance() > (float)2)) break;
                if (!MinecraftInstance.mc.isIntegratedServerRunning()) {
                    v45 = MinecraftInstance.mc.getNetHandler();
                    v46 = MinecraftInstance.mc.getThePlayer();
                    if (v46 == null) {
                        Intrinsics.throwNpe();
                    }
                    v47 = v46.getPosX();
                    v48 = DoubleCompanionObject.INSTANCE.getNaN();
                    v49 = MinecraftInstance.mc.getThePlayer();
                    if (v49 == null) {
                        Intrinsics.throwNpe();
                    }
                    v45.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(v47, v48, v49.getPosZ(), false));
                }
                v50 = MinecraftInstance.mc.getThePlayer();
                if (v50 == null) {
                    Intrinsics.throwNpe();
                }
                v50.setFallDistance(-9999);
                break;
            }
            case 2: {
                this.spartanTimer.update();
                v51 = MinecraftInstance.mc.getThePlayer();
                if (v51 == null) {
                    Intrinsics.throwNpe();
                }
                if (!((double)v51.getFallDistance() > 1.5) || !this.spartanTimer.hasTimePassed(10)) break;
                v52 = MinecraftInstance.mc.getNetHandler();
                v53 = MinecraftInstance.mc.getThePlayer();
                if (v53 == null) {
                    Intrinsics.throwNpe();
                }
                v54 = v53.getPosX();
                v55 = MinecraftInstance.mc.getThePlayer();
                if (v55 == null) {
                    Intrinsics.throwNpe();
                }
                v56 = v55.getPosY() + (double)10;
                v57 = MinecraftInstance.mc.getThePlayer();
                if (v57 == null) {
                    Intrinsics.throwNpe();
                }
                v52.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(v54, v56, v57.getPosZ(), true));
                v58 = MinecraftInstance.mc.getNetHandler();
                v59 = MinecraftInstance.mc.getThePlayer();
                if (v59 == null) {
                    Intrinsics.throwNpe();
                }
                v60 = v59.getPosX();
                v61 = MinecraftInstance.mc.getThePlayer();
                if (v61 == null) {
                    Intrinsics.throwNpe();
                }
                v62 = v61.getPosY() - (double)10;
                v63 = MinecraftInstance.mc.getThePlayer();
                if (v63 == null) {
                    Intrinsics.throwNpe();
                }
                v58.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(v60, v62, v63.getPosZ(), true));
                this.spartanTimer.reset();
                break;
            }
            case 3: {
                v64 = MinecraftInstance.mc.getThePlayer();
                if (v64 == null) {
                    Intrinsics.throwNpe();
                }
                if (v64.getFallDistance() > (float)3) {
                    this.isDmgFalling = true;
                }
                if (!Intrinsics.areEqual((String)this.modeValue.get(), "LoyisaAAC4.4.2")) break;
                if (this.aac4FlagCount >= 3 || this.aac4FlagCooldown.hasTimePassed(1500L)) {
                    return;
                }
                if (this.aac4FlagCooldown.hasTimePassed(1500L)) break;
                v65 = MinecraftInstance.mc.getThePlayer();
                if (v65 == null) {
                    Intrinsics.throwNpe();
                }
                if (!v65.getOnGround()) {
                    v66 = MinecraftInstance.mc.getThePlayer();
                    if (v66 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (!((double)v66.getFallDistance() < 0.5)) break;
                }
                v67 = MinecraftInstance.mc.getThePlayer();
                if (v67 == null) {
                    Intrinsics.throwNpe();
                }
                v67.setMotionX(0.0);
                v68 = MinecraftInstance.mc.getThePlayer();
                if (v68 == null) {
                    Intrinsics.throwNpe();
                }
                v68.setMotionZ(0.0);
                v69 = MinecraftInstance.mc.getThePlayer();
                if (v69 == null) {
                    Intrinsics.throwNpe();
                }
                v69.setOnGround(false);
                v70 = MinecraftInstance.mc.getThePlayer();
                if (v70 == null) {
                    Intrinsics.throwNpe();
                }
                v70.setJumpMovementFactor(0.0f);
                break;
            }
            case 5: {
                offsetYs = 0.0;
                this.aac5Check = false;
                while (true) {
                    v71 = MinecraftInstance.mc.getThePlayer();
                    if (v71 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (!(v71.getMotionY() - 1.5 < offsetYs)) break;
                    v72 = MinecraftInstance.mc.getThePlayer();
                    if (v72 == null) {
                        Intrinsics.throwNpe();
                    }
                    v73 = v72.getPosX();
                    v74 = MinecraftInstance.mc.getThePlayer();
                    if (v74 == null) {
                        Intrinsics.throwNpe();
                    }
                    v75 = v74.getPosY() + offsetYs;
                    v76 = MinecraftInstance.mc.getThePlayer();
                    if (v76 == null) {
                        Intrinsics.throwNpe();
                    }
                    blockPos = new WBlockPos(v73, v75, v76.getPosZ());
                    $i$f$getBlock = false;
                    v77 = MinecraftInstance.mc.getTheWorld();
                    v78 = v77 != null && (v77 = v77.getBlockState(blockPos)) != null ? v77.getBlock() : (block = null);
                    if (block == null) {
                        Intrinsics.throwNpe();
                    }
                    v79 = MinecraftInstance.mc.getTheWorld();
                    if (v79 == null) {
                        Intrinsics.throwNpe();
                    }
                    var11_12 = blockPos;
                    var10_11 = v79;
                    $i$f$getState = false;
                    v80 = MinecraftInstance.mc.getTheWorld();
                    v81 = var12_13 = v80 != null ? v80.getBlockState(blockPos) : null;
                    if (v81 == null) {
                        Intrinsics.throwNpe();
                    }
                    if ((axisAlignedBB = var9_10.getCollisionBoundingBox(var10_11, var11_12, v81)) != null) {
                        offsetYs = -999.9;
                        this.aac5Check = true;
                    }
                    offsetYs -= 0.5;
                }
                v82 = MinecraftInstance.mc.getThePlayer();
                if (v82 == null) {
                    Intrinsics.throwNpe();
                }
                if (v82.getOnGround()) {
                    v83 = MinecraftInstance.mc.getThePlayer();
                    if (v83 == null) {
                        Intrinsics.throwNpe();
                    }
                    v83.setFallDistance(-2.0f);
                    this.aac5Check = false;
                }
                if (this.aac5Timer > 0) {
                    --this.aac5Timer;
                }
                if (!this.aac5Check) ** GOTO lbl-1000
                v84 = MinecraftInstance.mc.getThePlayer();
                if (v84 == null) {
                    Intrinsics.throwNpe();
                }
                if (!((double)v84.getFallDistance() > 2.5)) ** GOTO lbl-1000
                v85 = MinecraftInstance.mc.getThePlayer();
                if (v85 == null) {
                    Intrinsics.throwNpe();
                }
                if (!v85.getOnGround()) {
                    this.aac5doFlag = true;
                    this.aac5Timer = 18;
                } else if (this.aac5Timer < 2) {
                    this.aac5doFlag = false;
                }
                if (!this.aac5doFlag) break;
                v86 = MinecraftInstance.mc.getThePlayer();
                if (v86 == null) {
                    Intrinsics.throwNpe();
                }
                if (v86.getOnGround()) {
                    v87 = MinecraftInstance.mc.getNetHandler();
                    v88 = MinecraftInstance.mc.getThePlayer();
                    if (v88 == null) {
                        Intrinsics.throwNpe();
                    }
                    v89 = v88.getPosX();
                    v90 = MinecraftInstance.mc.getThePlayer();
                    if (v90 == null) {
                        Intrinsics.throwNpe();
                    }
                    v91 = v90.getPosY() + 0.5;
                    v92 = MinecraftInstance.mc.getThePlayer();
                    if (v92 == null) {
                        Intrinsics.throwNpe();
                    }
                    v87.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(v89, v91, v92.getPosZ(), true));
                    break;
                }
                v93 = MinecraftInstance.mc.getNetHandler();
                v94 = MinecraftInstance.mc.getThePlayer();
                if (v94 == null) {
                    Intrinsics.throwNpe();
                }
                v95 = v94.getPosX();
                v96 = MinecraftInstance.mc.getThePlayer();
                if (v96 == null) {
                    Intrinsics.throwNpe();
                }
                v97 = v96.getPosY() + 0.42;
                v98 = MinecraftInstance.mc.getThePlayer();
                if (v98 == null) {
                    Intrinsics.throwNpe();
                }
                v93.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(v95, v97, v98.getPosZ(), true));
                break;
            }
            case 8: {
                if (this.worldChange) {
                    v99 = MinecraftInstance.mc.getThePlayer();
                    if (v99 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (v99.getOnGround()) {
                        this.worldChange = true;
                    }
                    return;
                }
                if (this.canNoFall) ** GOTO lbl-1000
                v100 = MinecraftInstance.mc.getThePlayer();
                if (v100 == null) {
                    Intrinsics.throwNpe();
                }
                if ((double)v100.getFallDistance() > 3.25) {
                    this.canNoFall = true;
                } else lbl-1000:
                // 2 sources

                {
                    v101 = MinecraftInstance.mc.getThePlayer();
                    if (v101 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (v101.getOnGround()) {
                        this.canNoFall = false;
                    }
                }
                if (this.nextSpoof) {
                    v102 = MinecraftInstance.mc.getThePlayer();
                    if (v102 == null) {
                        Intrinsics.throwNpe();
                    }
                    v102.setMotionY(-0.1);
                    MovementUtils.strafe(0.343f);
                    this.nextSpoof = false;
                }
                v103 = MinecraftInstance.mc.getThePlayer();
                if (v103 == null) {
                    Intrinsics.throwNpe();
                }
                if (!((double)v103.getFallDistance() > 3.65)) break;
                v104 = MinecraftInstance.mc.getThePlayer();
                if (v104 == null) {
                    Intrinsics.throwNpe();
                }
                v104.setFallDistance(0.0f);
                this.count = MinecraftInstance.mc.isIntegratedServerRunning() != false ? 2 : 1;
                this.doSpoof = true;
                this.nextSpoof = true;
                break;
            }
        }
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP.isInWater()) {
            return;
        }
        IPacket packet = event.getPacket();
        String mode = (String)this.modeValue.get();
        if (MinecraftInstance.classProvider.isSPacketEntityVelocity(event.getPacket()) && StringsKt.equals(mode, "AAC4.4.X-Flag", true)) {
            IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP2 == null) {
                Intrinsics.throwNpe();
            }
            if ((double)iEntityPlayerSP2.getFallDistance() > 1.8) {
                event.getPacket().asSPacketEntityVelocity().setMotionY((int)((double)event.getPacket().asSPacketEntityVelocity().getMotionY() * -0.1));
            }
        }
        if (MinecraftInstance.classProvider.isSPacketPlayerPosLook(event.getPacket()) && StringsKt.equals(mode, "LoyisaAAC4.4.2", true)) {
            int n = this.aac4FlagCount;
            this.aac4FlagCount = n + 1;
            if (this.matrixFlagWait > 0) {
                this.aac4FlagCooldown.reset();
                this.aac4FlagCount = 1;
                event.cancelEvent();
            }
        }
        if (StringsKt.equals(mode, "AAC4", true) && MinecraftInstance.classProvider.isCPacketPlayer(packet) && this.fakelag) {
            event.cancelEvent();
            if (this.packetmodify) {
                packet.asCPacketPlayer().setOnGround(true);
                this.packetmodify = false;
            }
            this.packets.add(packet);
        }
        if (StringsKt.equals(mode, "Vulcan", true) && MinecraftInstance.classProvider.isCPacketPlayer(packet) && this.doSpoof && this.count > 0) {
            NoFall noFall = this;
            noFall.count += -1;
            int cfr_ignored_0 = noFall.count;
            if (this.count == 0) {
                this.doSpoof = false;
            }
            packet.asCPacketPlayer().setOnGround(true);
            ICPacketPlayer iCPacketPlayer = packet.asCPacketPlayer();
            IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP3 == null) {
                Intrinsics.throwNpe();
            }
            iCPacketPlayer.setY(MathKt.roundToInt(iEntityPlayerSP3.getPosY() * (double)2) / 2);
            IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP4 == null) {
                Intrinsics.throwNpe();
            }
            IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP5 == null) {
                Intrinsics.throwNpe();
            }
            double d = iEntityPlayerSP5.getPosX();
            double d2 = packet.asCPacketPlayer().getY();
            IEntityPlayerSP iEntityPlayerSP6 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP6 == null) {
                Intrinsics.throwNpe();
            }
            iEntityPlayerSP4.setPosition(d, d2, iEntityPlayerSP6.getPosZ());
        }
        if (MinecraftInstance.classProvider.isCPacketPlayer(packet)) {
            ICPacketPlayer playerPacket = packet.asCPacketPlayer();
            if (StringsKt.equals(mode, "SpoofGround", true)) {
                playerPacket.setOnGround(true);
            }
            if (StringsKt.equals(mode, "NoGround", true)) {
                playerPacket.setOnGround(false);
            }
            if (StringsKt.equals(mode, "Hypixel", true) && MinecraftInstance.mc.getThePlayer() != null) {
                IEntityPlayerSP iEntityPlayerSP7 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP7 == null) {
                    Intrinsics.throwNpe();
                }
                if ((double)iEntityPlayerSP7.getFallDistance() > 1.5) {
                    IEntityPlayerSP iEntityPlayerSP8 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP8 == null) {
                        Intrinsics.throwNpe();
                    }
                    playerPacket.setOnGround(iEntityPlayerSP8.getTicksExisted() % 2 == 0);
                }
            }
        }
    }

    @EventTarget
    public final void onMove(@NotNull MoveEvent event) {
        block22: {
            block21: {
                Intrinsics.checkParameterIsNotNull(event, "event");
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                if (BlockUtils.collideBlock(iEntityPlayerSP.getEntityBoundingBox(), (Function1<? super IBlock, Boolean>)new Function1<Object, Boolean>(MinecraftInstance.classProvider){

                    public final boolean invoke(@Nullable Object p1) {
                        return ((IClassProvider)this.receiver).isBlockLiquid(p1);
                    }

                    public final KDeclarationContainer getOwner() {
                        return Reflection.getOrCreateKotlinClass(IClassProvider.class);
                    }

                    public final String getName() {
                        return "isBlockLiquid";
                    }

                    public final String getSignature() {
                        return "isBlockLiquid(Ljava/lang/Object;)Z";
                    }
                })) break block21;
                IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP2 == null) {
                    Intrinsics.throwNpe();
                }
                double d = iEntityPlayerSP2.getEntityBoundingBox().getMaxX();
                IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP3 == null) {
                    Intrinsics.throwNpe();
                }
                double d2 = iEntityPlayerSP3.getEntityBoundingBox().getMaxY();
                IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP4 == null) {
                    Intrinsics.throwNpe();
                }
                double d3 = iEntityPlayerSP4.getEntityBoundingBox().getMaxZ();
                IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP5 == null) {
                    Intrinsics.throwNpe();
                }
                double d4 = iEntityPlayerSP5.getEntityBoundingBox().getMinX();
                IEntityPlayerSP iEntityPlayerSP6 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP6 == null) {
                    Intrinsics.throwNpe();
                }
                double d5 = iEntityPlayerSP6.getEntityBoundingBox().getMinY() - 0.01;
                IEntityPlayerSP iEntityPlayerSP7 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP7 == null) {
                    Intrinsics.throwNpe();
                }
                if (!BlockUtils.collideBlock(MinecraftInstance.classProvider.createAxisAlignedBB(d, d2, d3, d4, d5, iEntityPlayerSP7.getEntityBoundingBox().getMinZ()), (Function1<? super IBlock, Boolean>)new Function1<Object, Boolean>(MinecraftInstance.classProvider){

                    public final boolean invoke(@Nullable Object p1) {
                        return ((IClassProvider)this.receiver).isBlockLiquid(p1);
                    }

                    public final KDeclarationContainer getOwner() {
                        return Reflection.getOrCreateKotlinClass(IClassProvider.class);
                    }

                    public final String getName() {
                        return "isBlockLiquid";
                    }

                    public final String getSignature() {
                        return "isBlockLiquid(Ljava/lang/Object;)Z";
                    }
                })) break block22;
            }
            return;
        }
        if (StringsKt.equals((String)this.modeValue.get(), "laac", true) && !this.jumped) {
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if (!iEntityPlayerSP.getOnGround()) {
                IEntityPlayerSP iEntityPlayerSP8 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP8 == null) {
                    Intrinsics.throwNpe();
                }
                if (!iEntityPlayerSP8.isOnLadder()) {
                    IEntityPlayerSP iEntityPlayerSP9 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP9 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (!iEntityPlayerSP9.isInWater()) {
                        IEntityPlayerSP iEntityPlayerSP10 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP10 == null) {
                            Intrinsics.throwNpe();
                        }
                        if (!iEntityPlayerSP10.isInWeb()) {
                            IEntityPlayerSP iEntityPlayerSP11 = MinecraftInstance.mc.getThePlayer();
                            if (iEntityPlayerSP11 == null) {
                                Intrinsics.throwNpe();
                            }
                            if (iEntityPlayerSP11.getMotionY() < 0.0) {
                                event.setX(0.0);
                                event.setZ(0.0);
                            }
                        }
                    }
                }
            }
        }
    }

    /*
     * WARNING - void declaration
     */
    @EventTarget
    private final void onMotionUpdate(MotionEvent event) {
        EventState eventState;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP.isInWater()) {
            return;
        }
        if (StringsKt.equals((String)this.modeValue.get(), "AAC4", true) && (eventState = event.getEventState()) == EventState.PRE) {
            if (!this.inVoid()) {
                if (this.fakelag) {
                    this.fakelag = false;
                    if (this.packets.size() > 0) {
                        for (IPacket packet : this.packets) {
                            IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                            if (iEntityPlayerSP2 == null) {
                                Intrinsics.throwNpe();
                            }
                            IINetHandlerPlayClient iINetHandlerPlayClient = iEntityPlayerSP2.getSendQueue();
                            IPacket iPacket = packet;
                            Intrinsics.checkExpressionValueIsNotNull(iPacket, "packet");
                            iINetHandlerPlayClient.addToSendQueue(iPacket);
                        }
                        this.packets.clear();
                    }
                }
                return;
            }
            IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP3 == null) {
                Intrinsics.throwNpe();
            }
            if (iEntityPlayerSP3.getOnGround() && this.fakelag) {
                this.fakelag = false;
                if (this.packets.size() > 0) {
                    for (IPacket packet : this.packets) {
                        IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP4 == null) {
                            Intrinsics.throwNpe();
                        }
                        IINetHandlerPlayClient iINetHandlerPlayClient = iEntityPlayerSP4.getSendQueue();
                        IPacket iPacket = packet;
                        Intrinsics.checkExpressionValueIsNotNull(iPacket, "packet");
                        iINetHandlerPlayClient.addToSendQueue(iPacket);
                    }
                    this.packets.clear();
                }
                return;
            }
            IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP5 == null) {
                Intrinsics.throwNpe();
            }
            if (iEntityPlayerSP5.getFallDistance() > (float)3 && this.fakelag) {
                this.packetmodify = true;
                IEntityPlayerSP iEntityPlayerSP6 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP6 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP6.setFallDistance(0.0f);
            }
            if (this.inAir(4.0, 1.0)) {
                return;
            }
            if (!this.fakelag) {
                this.fakelag = true;
            }
        }
        if (StringsKt.equals((String)this.modeValue.get(), "MLG", true)) {
            if (event.getEventState() == EventState.PRE) {
                this.currentMlgRotation = null;
                this.mlgTimer.update();
                if (!this.mlgTimer.hasTimePassed(10)) {
                    return;
                }
                IEntityPlayerSP iEntityPlayerSP7 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP7 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP7.getFallDistance() > ((Number)this.minFallDistance.get()).floatValue()) {
                    void z$iv;
                    void y$iv;
                    void x$iv22;
                    void this_$iv22;
                    IEntityPlayerSP iEntityPlayerSP8 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP8 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d = iEntityPlayerSP8.getPosX();
                    IEntityPlayerSP iEntityPlayerSP9 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP9 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d2 = iEntityPlayerSP9.getPosY();
                    IEntityPlayerSP iEntityPlayerSP10 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP10 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d3 = iEntityPlayerSP10.getPosZ();
                    IEntityPlayerSP iEntityPlayerSP11 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP11 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d4 = iEntityPlayerSP11.getMotionX();
                    IEntityPlayerSP iEntityPlayerSP12 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP12 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d5 = iEntityPlayerSP12.getMotionY();
                    IEntityPlayerSP iEntityPlayerSP13 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP13 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d6 = iEntityPlayerSP13.getMotionZ();
                    IEntityPlayerSP iEntityPlayerSP14 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP14 == null) {
                        Intrinsics.throwNpe();
                    }
                    float f = iEntityPlayerSP14.getRotationYaw();
                    IEntityPlayerSP iEntityPlayerSP15 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP15 == null) {
                        Intrinsics.throwNpe();
                    }
                    float f2 = iEntityPlayerSP15.getMoveStrafing();
                    IEntityPlayerSP iEntityPlayerSP16 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP16 == null) {
                        Intrinsics.throwNpe();
                    }
                    FallingPlayer fallingPlayer = new FallingPlayer(d, d2, d3, d4, d5, d6, f, f2, iEntityPlayerSP16.getMoveForward());
                    double maxDist = (double)MinecraftInstance.mc.getPlayerController().getBlockReachDistance() + 1.5;
                    IEntityPlayerSP iEntityPlayerSP17 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP17 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d7 = 1.0 / iEntityPlayerSP17.getMotionY() * -maxDist;
                    Object object = fallingPlayer;
                    boolean bl = false;
                    double d8 = Math.ceil(d7);
                    FallingPlayer.CollisionResult collisionResult = ((FallingPlayer)object).findCollision((int)d8);
                    if (collisionResult == null) {
                        return;
                    }
                    FallingPlayer.CollisionResult collision = collisionResult;
                    IEntityPlayerSP iEntityPlayerSP18 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP18 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d9 = iEntityPlayerSP18.getPosX();
                    IEntityPlayerSP iEntityPlayerSP19 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP19 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d10 = iEntityPlayerSP19.getPosY();
                    IEntityPlayerSP iEntityPlayerSP20 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP20 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d11 = d10 + (double)iEntityPlayerSP20.getEyeHeight();
                    IEntityPlayerSP iEntityPlayerSP21 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP21 == null) {
                        Intrinsics.throwNpe();
                    }
                    WVec3 wVec3 = new WVec3(d9, d11, iEntityPlayerSP21.getPosZ());
                    WBlockPos wBlockPos = collision.getPos();
                    Intrinsics.checkExpressionValueIsNotNull(wBlockPos, "collision.pos");
                    WVec3 wVec32 = new WVec3(wBlockPos);
                    double d12 = 0.5;
                    double d13 = 0.5;
                    double d14 = 0.5;
                    object = wVec3;
                    boolean $i$f$addVector = false;
                    WVec3 wVec33 = new WVec3(this_$iv22.getXCoord() + x$iv22, this_$iv22.getYCoord() + y$iv, this_$iv22.getZCoord() + z$iv);
                    double this_$iv22 = 0.75;
                    double d15 = MinecraftInstance.mc.getPlayerController().getBlockReachDistance();
                    double d16 = ((WVec3)object).distanceTo(wVec33);
                    int n = 0;
                    double d17 = Math.sqrt(this_$iv22);
                    boolean ok = d16 < d15 + d17;
                    IEntityPlayerSP iEntityPlayerSP22 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP22 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d18 = iEntityPlayerSP22.getMotionY();
                    double d19 = collision.getPos().getY() + 1;
                    IEntityPlayerSP iEntityPlayerSP23 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP23 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (d18 < d19 - iEntityPlayerSP23.getPosY()) {
                        ok = true;
                    }
                    if (!ok) {
                        return;
                    }
                    int index = -1;
                    int x$iv22 = 36;
                    n = 44;
                    while (x$iv22 <= n) {
                        IItem iItem;
                        void i;
                        IItemStack itemStack;
                        IEntityPlayerSP iEntityPlayerSP24 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP24 == null) {
                            Intrinsics.throwNpe();
                        }
                        if ((itemStack = iEntityPlayerSP24.getInventoryContainer().getSlot((int)i).getStack()) != null && (Intrinsics.areEqual(itemStack.getItem(), MinecraftInstance.classProvider.getItemEnum(ItemType.WATER_BUCKET)) || MinecraftInstance.classProvider.isItemBlock(itemStack.getItem()) && Intrinsics.areEqual((iItem = itemStack.getItem()) != null && (iItem = iItem.asItemBlock()) != null ? iItem.getBlock() : null, MinecraftInstance.classProvider.getBlockEnum(BlockType.WEB)))) {
                            index = i - 36;
                            IEntityPlayerSP iEntityPlayerSP25 = MinecraftInstance.mc.getThePlayer();
                            if (iEntityPlayerSP25 == null) {
                                Intrinsics.throwNpe();
                            }
                            if (iEntityPlayerSP25.getInventory().getCurrentItem() == index) break;
                        }
                        ++i;
                    }
                    if (index == -1) {
                        return;
                    }
                    this.currentMlgItemIndex = index;
                    this.currentMlgBlock = collision.getPos();
                    IEntityPlayerSP iEntityPlayerSP26 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP26 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (iEntityPlayerSP26.getInventory().getCurrentItem() != index) {
                        IEntityPlayerSP iEntityPlayerSP27 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP27 == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP27.getSendQueue().addToSendQueue(MinecraftInstance.classProvider.createCPacketHeldItemChange(index));
                    }
                    VecRotation vecRotation = this.currentMlgRotation = RotationUtils.faceBlock(collision.getPos());
                    if (vecRotation == null) {
                        Intrinsics.throwNpe();
                    }
                    Rotation rotation = vecRotation.getRotation();
                    IEntityPlayerSP iEntityPlayerSP28 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP28 == null) {
                        Intrinsics.throwNpe();
                    }
                    rotation.toPlayer(iEntityPlayerSP28);
                }
            } else if (this.currentMlgRotation != null) {
                IItemStack stack;
                IEntityPlayerSP iEntityPlayerSP29 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP29 == null) {
                    Intrinsics.throwNpe();
                }
                IItemStack iItemStack = stack = iEntityPlayerSP29.getInventory().getStackInSlot(this.currentMlgItemIndex + 36);
                if (iItemStack == null) {
                    Intrinsics.throwNpe();
                }
                if (MinecraftInstance.classProvider.isItemBucket(iItemStack.getItem())) {
                    IPlayerControllerMP iPlayerControllerMP = MinecraftInstance.mc.getPlayerController();
                    IEntityPlayerSP iEntityPlayerSP30 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP30 == null) {
                        Intrinsics.throwNpe();
                    }
                    IEntityPlayer iEntityPlayer = iEntityPlayerSP30;
                    IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
                    if (iWorldClient == null) {
                        Intrinsics.throwNpe();
                    }
                    iPlayerControllerMP.sendUseItem(iEntityPlayer, iWorldClient, stack);
                } else {
                    WVec3i dirVec = MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.UP).getDirectionVec();
                    IPlayerControllerMP iPlayerControllerMP = MinecraftInstance.mc.getPlayerController();
                    IEntityPlayerSP iEntityPlayerSP31 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP31 == null) {
                        Intrinsics.throwNpe();
                    }
                    IEntityPlayer iEntityPlayer = iEntityPlayerSP31;
                    IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
                    if (iWorldClient == null) {
                        Intrinsics.throwNpe();
                    }
                    if (iPlayerControllerMP.sendUseItem(iEntityPlayer, iWorldClient, stack)) {
                        this.mlgTimer.reset();
                    }
                }
                IEntityPlayerSP iEntityPlayerSP32 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP32 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP32.getInventory().getCurrentItem() != this.currentMlgItemIndex) {
                    IEntityPlayerSP iEntityPlayerSP33 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP33 == null) {
                        Intrinsics.throwNpe();
                    }
                    IINetHandlerPlayClient iINetHandlerPlayClient = iEntityPlayerSP33.getSendQueue();
                    IEntityPlayerSP iEntityPlayerSP34 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP34 == null) {
                        Intrinsics.throwNpe();
                    }
                    iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketHeldItemChange(iEntityPlayerSP34.getInventory().getCurrentItem()));
                }
            }
        }
    }

    public final boolean isBlockUnder() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP.getPosY() < 0.0) {
            return false;
        }
        int off = 0;
        while (true) {
            IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP2 == null) {
                Intrinsics.throwNpe();
            }
            if (off >= (int)iEntityPlayerSP2.getPosY() + 2) {
                return false;
            }
            IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP3 == null) {
                Intrinsics.throwNpe();
            }
            IAxisAlignedBB bb = iEntityPlayerSP3.getEntityBoundingBox().offset(0.0, -off, 0.0);
            IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
            if (iWorldClient == null) {
                Intrinsics.throwNpe();
            }
            IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP4 == null) {
                Intrinsics.throwNpe();
            }
            if (!iWorldClient.getCollidingBoundingBoxes(iEntityPlayerSP4, bb).isEmpty()) {
                return true;
            }
            off += 2;
        }
    }

    public final int getJumpEffect() {
        int n;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.JUMP))) {
            IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP2 == null) {
                Intrinsics.throwNpe();
            }
            IPotionEffect iPotionEffect = iEntityPlayerSP2.getActivePotionEffect(MinecraftInstance.classProvider.getPotionEnum(PotionType.JUMP));
            if (iPotionEffect == null) {
                Intrinsics.throwNpe();
            }
            n = iPotionEffect.getAmplifier() + 1;
        } else {
            n = 0;
        }
        return n;
    }

    public final boolean inVoid() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP.getPosY() < 0.0) {
            return false;
        }
        int off = 0;
        while (true) {
            double d = off;
            IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP2 == null) {
                Intrinsics.throwNpe();
            }
            if (!(d < iEntityPlayerSP2.getPosY() + (double)2)) break;
            IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP3 == null) {
                Intrinsics.throwNpe();
            }
            double d2 = iEntityPlayerSP3.getPosX();
            IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP4 == null) {
                Intrinsics.throwNpe();
            }
            double d3 = iEntityPlayerSP4.getPosY();
            IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP5 == null) {
                Intrinsics.throwNpe();
            }
            double d4 = iEntityPlayerSP5.getPosZ();
            IEntityPlayerSP iEntityPlayerSP6 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP6 == null) {
                Intrinsics.throwNpe();
            }
            double d5 = iEntityPlayerSP6.getPosX();
            double d6 = off;
            IEntityPlayerSP iEntityPlayerSP7 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP7 == null) {
                Intrinsics.throwNpe();
            }
            IAxisAlignedBB bb = MinecraftInstance.classProvider.createAxisAlignedBB(d2, d3, d4, d5, d6, iEntityPlayerSP7.getPosZ());
            IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
            if (iWorldClient == null) {
                Intrinsics.throwNpe();
            }
            IEntityPlayerSP iEntityPlayerSP8 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP8 == null) {
                Intrinsics.throwNpe();
            }
            if (!iWorldClient.getCollidingBoundingBoxes(iEntityPlayerSP8, bb).isEmpty()) {
                return true;
            }
            off += 2;
        }
        return false;
    }

    public final boolean inAir(double height, double plus) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP.getPosY() < 0.0) {
            return false;
        }
        int off = 0;
        while ((double)off < height) {
            IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP2 == null) {
                Intrinsics.throwNpe();
            }
            double d = iEntityPlayerSP2.getPosX();
            IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP3 == null) {
                Intrinsics.throwNpe();
            }
            double d2 = iEntityPlayerSP3.getPosY();
            IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP4 == null) {
                Intrinsics.throwNpe();
            }
            double d3 = iEntityPlayerSP4.getPosZ();
            IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP5 == null) {
                Intrinsics.throwNpe();
            }
            double d4 = iEntityPlayerSP5.getPosX();
            IEntityPlayerSP iEntityPlayerSP6 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP6 == null) {
                Intrinsics.throwNpe();
            }
            double d5 = iEntityPlayerSP6.getPosY() - (double)off;
            IEntityPlayerSP iEntityPlayerSP7 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP7 == null) {
                Intrinsics.throwNpe();
            }
            IAxisAlignedBB bb = MinecraftInstance.classProvider.createAxisAlignedBB(d, d2, d3, d4, d5, iEntityPlayerSP7.getPosZ());
            IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
            if (iWorldClient == null) {
                Intrinsics.throwNpe();
            }
            IEntityPlayerSP iEntityPlayerSP8 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP8 == null) {
                Intrinsics.throwNpe();
            }
            if (!iWorldClient.getCollidingBoundingBoxes(iEntityPlayerSP8, bb).isEmpty()) {
                return true;
            }
            off += (int)plus;
        }
        return false;
    }

    @EventTarget(ignoreCondition=true)
    public final void onJump(@Nullable JumpEvent event) {
        this.jumped = true;
    }

    @EventTarget
    public final void onWorld(@NotNull WorldEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        this.worldChange = true;
    }

    @Override
    @NotNull
    public String getTag() {
        return (String)this.modeValue.get();
    }
}
