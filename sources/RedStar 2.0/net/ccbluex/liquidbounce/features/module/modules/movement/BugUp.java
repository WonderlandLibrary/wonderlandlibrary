package net.ccbluex.liquidbounce.features.module.modules.movement;

import java.util.ArrayList;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import me.utils.FallingPlayer;
import me.utils.PacketUtils;
import me.utils.timer.TimeHelper;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.movement.Fly;
import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold;
import net.ccbluex.liquidbounce.injection.backend.ClassProviderImpl;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.block.BlockAir;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="AntiVoid", category=ModuleCategory.MOVEMENT, description="544")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000b\n\n\n\b\n\n\u0000\n\n\b\n\n\b\n\n\u0000\n\n\u0000\n\n\b\n\n\n\n\b\f\n\n\b\t\n\n\b\n\n\u0000\n\n\u0000\b\u000020B¢J\b00HJ\b10HJ\b203HJ4032506HJ7032508HR0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R\b0X¢\n\u0000R\t0X¢\n\u0000R\n0X¢\n\u0000\b\f\r\"\bR0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R00j\b0`X¢\n\u0000R*00j\b0`X¢\n\u0000\b\"\b !R\"0X¢\n\u0000R#0X¢\n\u0000R$0X¢\n\u0000R%0X¢\n\u0000R&0X¢\n\u0000R'0X¢\n\u0000R(0)X¢\n\u0000\b*+\"\b,-R.0X¢\n\u0000R/0X¢\n\u0000¨9"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/BugUp;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "autoScaffoldValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "blink", "", "canBlink", "canSpoof", "flagged", "lastGroundPos", "", "getLastGroundPos", "()[D", "setLastGroundPos", "([D)V", "lastRecY", "", "maxFallDistValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "motionX", "motionY", "motionZ", "packetCache", "Ljava/util/ArrayList;", "Lnet/minecraft/network/play/client/CPacketPlayer;", "Lkotlin/collections/ArrayList;", "packets", "getPackets", "()Ljava/util/ArrayList;", "setPackets", "(Ljava/util/ArrayList;)V", "posX", "posY", "posZ", "pullbackTime", "resetMotionValue", "startFallDistValue", "timer", "Lme/utils/timer/TimeHelper;", "getTimer", "()Lme/utils/timer/TimeHelper;", "setTimer", "(Lme/utils/timer/TimeHelper;)V", "tried", "voidOnlyValue", "checkVoid", "isInVoid", "onEnable", "", "onPacket", "event", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "Pride"})
public class BugUp
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Hypixel", "Blink", "TPBack", "MotionFlag", "PacketFlag", "GroundSpoof", "OldHypixel", "Jartex", "OldCubecraft"}, "Blink");
    private final FloatValue pullbackTime = new FloatValue("Hypixel-PullbackTime", 800.0f, 800.0f, 1800.0f);
    private final FloatValue maxFallDistValue = new FloatValue("MaxFallDistance", 10.0f, 5.0f, 20.0f);
    private final BoolValue resetMotionValue = new BoolValue("ResetMotion", false);
    private final FloatValue startFallDistValue = new FloatValue("BlinkStartFallDistance", 2.0f, 0.0f, 5.0f);
    private final BoolValue autoScaffoldValue = new BoolValue("BlinkAutoScaffold", true);
    private final BoolValue voidOnlyValue = new BoolValue("OnlyVoid", true);
    private final ArrayList<CPacketPlayer> packetCache = new ArrayList();
    private boolean blink;
    private boolean canBlink;
    private boolean canSpoof;
    private boolean tried;
    private boolean flagged;
    private double posX;
    private double posY;
    private double posZ;
    private double motionX;
    private double motionY;
    private double motionZ;
    private double lastRecY;
    @NotNull
    private TimeHelper timer = new TimeHelper();
    @NotNull
    private double[] lastGroundPos = new double[3];
    @NotNull
    private ArrayList<CPacketPlayer> packets = new ArrayList();

    @NotNull
    public final TimeHelper getTimer() {
        return this.timer;
    }

    public final void setTimer(@NotNull TimeHelper timeHelper) {
        Intrinsics.checkParameterIsNotNull(timeHelper, "<set-?>");
        this.timer = timeHelper;
    }

    @NotNull
    public final double[] getLastGroundPos() {
        return this.lastGroundPos;
    }

    public final void setLastGroundPos(@NotNull double[] dArray) {
        Intrinsics.checkParameterIsNotNull(dArray, "<set-?>");
        this.lastGroundPos = dArray;
    }

    @NotNull
    public final ArrayList<CPacketPlayer> getPackets() {
        return this.packets;
    }

    public final void setPackets(@NotNull ArrayList<CPacketPlayer> arrayList) {
        Intrinsics.checkParameterIsNotNull(arrayList, "<set-?>");
        this.packets = arrayList;
    }

    /*
     * WARNING - void declaration
     */
    public boolean isInVoid() {
        int n = 0;
        int n2 = 128;
        while (n <= n2) {
            void i;
            if (MovementUtils.INSTANCE.isOnGround((double)i)) {
                return false;
            }
            ++i;
        }
        return true;
    }

    @Override
    public void onEnable() {
        this.blink = false;
        this.canBlink = false;
        this.canSpoof = false;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        this.lastRecY = iEntityPlayerSP.getPosY();
        this.tried = false;
        this.flagged = false;
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        v0 = MinecraftInstance.mc.getThePlayer();
        if (v0 == null) {
            Intrinsics.throwNpe();
        }
        if (v0.getOnGround()) {
            this.tried = false;
            this.flagged = false;
        }
        var2_2 = (String)this.modeValue.get();
        var3_3 = false;
        v1 = var2_2;
        if (v1 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        v2 = v1.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(v2, "(this as java.lang.String).toLowerCase()");
        var2_2 = v2;
        tmp = -1;
        switch (var2_2.hashCode()) {
            case -1167184852: {
                if (!var2_2.equals("jartex")) break;
                tmp = 1;
                break;
            }
            case 1438074052: {
                if (!var2_2.equals("oldcubecraft")) break;
                tmp = 2;
                break;
            }
            case -867535517: {
                if (!var2_2.equals("tpback")) break;
                tmp = 3;
                break;
            }
            case -529978910: {
                if (!var2_2.equals("groundspoof")) break;
                tmp = 4;
                break;
            }
            case 93826908: {
                if (!var2_2.equals("blink")) break;
                tmp = 5;
                break;
            }
            case -720374750: {
                if (!var2_2.equals("motionflag")) break;
                tmp = 6;
                break;
            }
            case 155895796: {
                if (!var2_2.equals("packetflag")) break;
                tmp = 7;
                break;
            }
        }
        switch (tmp) {
            case 4: {
                if (((Boolean)this.voidOnlyValue.get()).booleanValue() && !this.checkVoid()) break;
                v3 = MinecraftInstance.mc.getThePlayer();
                if (v3 == null) {
                    Intrinsics.throwNpe();
                }
                this.canSpoof = v3.getFallDistance() > ((Number)this.maxFallDistValue.get()).floatValue();
                break;
            }
            case 6: {
                if (((Boolean)this.voidOnlyValue.get()).booleanValue() && !this.checkVoid()) break;
                v4 = MinecraftInstance.mc.getThePlayer();
                if (v4 == null) {
                    Intrinsics.throwNpe();
                }
                if (!(v4.getFallDistance() > ((Number)this.maxFallDistValue.get()).floatValue()) || this.tried) break;
                v5 = MinecraftInstance.mc.getThePlayer();
                if (v5 == null) {
                    Intrinsics.throwNpe();
                }
                v5.setMotionY(v5.getMotionY() + (double)true);
                v6 = MinecraftInstance.mc.getThePlayer();
                if (v6 == null) {
                    Intrinsics.throwNpe();
                }
                v6.setFallDistance(0.0f);
                this.tried = true;
                break;
            }
            case 7: {
                if (((Boolean)this.voidOnlyValue.get()).booleanValue() && !this.checkVoid()) break;
                v7 = MinecraftInstance.mc.getThePlayer();
                if (v7 == null) {
                    Intrinsics.throwNpe();
                }
                if (!(v7.getFallDistance() > ((Number)this.maxFallDistValue.get()).floatValue()) || this.tried) break;
                v8 = MinecraftInstance.mc.getNetHandler();
                v9 = MinecraftInstance.mc.getThePlayer();
                if (v9 == null) {
                    Intrinsics.throwNpe();
                }
                v10 = v9.getPosX() + (double)true;
                v11 = MinecraftInstance.mc.getThePlayer();
                if (v11 == null) {
                    Intrinsics.throwNpe();
                }
                v12 = v11.getPosY() + (double)true;
                v13 = MinecraftInstance.mc.getThePlayer();
                if (v13 == null) {
                    Intrinsics.throwNpe();
                }
                v8.addToSendQueue(ClassProviderImpl.INSTANCE.createCPacketPlayerPosition(v10, v12, v13.getPosZ() + (double)true, false));
                this.tried = true;
                break;
            }
            case 3: {
                v14 = MinecraftInstance.mc.getThePlayer();
                if (v14 == null) {
                    Intrinsics.throwNpe();
                }
                if (v14.getOnGround()) {
                    v15 = MinecraftInstance.mc.getThePlayer();
                    if (v15 == null) {
                        Intrinsics.throwNpe();
                    }
                    v16 = v15.getPosX();
                    v17 = MinecraftInstance.mc.getThePlayer();
                    if (v17 == null) {
                        Intrinsics.throwNpe();
                    }
                    v18 = v17.getPosY() - 1.0;
                    v19 = MinecraftInstance.mc.getThePlayer();
                    if (v19 == null) {
                        Intrinsics.throwNpe();
                    }
                    blockPos$iv = new WBlockPos(v16, v18, v19.getPosZ());
                    $i$f$getBlock = false;
                    v20 = MinecraftInstance.mc.getTheWorld();
                    if (!((v20 != null && (v20 = v20.getBlockState(blockPos$iv)) != null ? v20.getBlock() : null) instanceof BlockAir)) {
                        v21 = MinecraftInstance.mc.getThePlayer();
                        if (v21 == null) {
                            Intrinsics.throwNpe();
                        }
                        this.posX = v21.getPrevPosX();
                        v22 = MinecraftInstance.mc.getThePlayer();
                        if (v22 == null) {
                            Intrinsics.throwNpe();
                        }
                        this.posY = v22.getPrevPosY();
                        v23 = MinecraftInstance.mc.getThePlayer();
                        if (v23 == null) {
                            Intrinsics.throwNpe();
                        }
                        this.posZ = v23.getPrevPosZ();
                    }
                }
                if (((Boolean)this.voidOnlyValue.get()).booleanValue() && !this.checkVoid()) break;
                v24 = MinecraftInstance.mc.getThePlayer();
                if (v24 == null) {
                    Intrinsics.throwNpe();
                }
                if (!(v24.getFallDistance() > ((Number)this.maxFallDistValue.get()).floatValue()) || this.tried) break;
                v25 = MinecraftInstance.mc.getThePlayer();
                if (v25 == null) {
                    Intrinsics.throwNpe();
                }
                v25.setPositionAndUpdate(this.posX, this.posY, this.posZ);
                v26 = MinecraftInstance.mc.getThePlayer();
                if (v26 == null) {
                    Intrinsics.throwNpe();
                }
                v26.setFallDistance(0.0f);
                v27 = MinecraftInstance.mc.getThePlayer();
                if (v27 == null) {
                    Intrinsics.throwNpe();
                }
                v27.setMotionX(0.0);
                v28 = MinecraftInstance.mc.getThePlayer();
                if (v28 == null) {
                    Intrinsics.throwNpe();
                }
                v28.setMotionY(0.0);
                v29 = MinecraftInstance.mc.getThePlayer();
                if (v29 == null) {
                    Intrinsics.throwNpe();
                }
                v29.setMotionZ(0.0);
                this.tried = true;
                break;
            }
            case 1: {
                this.canSpoof = false;
                if (!((Boolean)this.voidOnlyValue.get()).booleanValue() || this.checkVoid()) {
                    v30 = MinecraftInstance.mc.getThePlayer();
                    if (v30 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (v30.getFallDistance() > ((Number)this.maxFallDistValue.get()).floatValue()) {
                        v31 = MinecraftInstance.mc.getThePlayer();
                        if (v31 == null) {
                            Intrinsics.throwNpe();
                        }
                        if (v31.getPosY() < this.lastRecY + 0.01) {
                            v32 = MinecraftInstance.mc.getThePlayer();
                            if (v32 == null) {
                                Intrinsics.throwNpe();
                            }
                            if (v32.getMotionY() <= (double)false) {
                                v33 = MinecraftInstance.mc.getThePlayer();
                                if (v33 == null) {
                                    Intrinsics.throwNpe();
                                }
                                if (!v33.getOnGround() && !this.flagged) {
                                    v34 = MinecraftInstance.mc.getThePlayer();
                                    if (v34 == null) {
                                        Intrinsics.throwNpe();
                                    }
                                    v34.setMotionY(0.0);
                                    v35 = MinecraftInstance.mc.getThePlayer();
                                    if (v35 == null) {
                                        Intrinsics.throwNpe();
                                    }
                                    v35.setMotionZ(v35.getMotionZ() * 0.838);
                                    v36 = MinecraftInstance.mc.getThePlayer();
                                    if (v36 == null) {
                                        Intrinsics.throwNpe();
                                    }
                                    v36.setMotionX(v36.getMotionX() * 0.838);
                                    this.canSpoof = true;
                                }
                            }
                        }
                    }
                }
                v37 = MinecraftInstance.mc.getThePlayer();
                if (v37 == null) {
                    Intrinsics.throwNpe();
                }
                this.lastRecY = v37.getPosY();
                break;
            }
            case 2: {
                this.canSpoof = false;
                if (!((Boolean)this.voidOnlyValue.get()).booleanValue() || this.checkVoid()) {
                    v38 = MinecraftInstance.mc.getThePlayer();
                    if (v38 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (v38.getFallDistance() > ((Number)this.maxFallDistValue.get()).floatValue()) {
                        v39 = MinecraftInstance.mc.getThePlayer();
                        if (v39 == null) {
                            Intrinsics.throwNpe();
                        }
                        if (v39.getPosY() < this.lastRecY + 0.01) {
                            v40 = MinecraftInstance.mc.getThePlayer();
                            if (v40 == null) {
                                Intrinsics.throwNpe();
                            }
                            if (v40.getMotionY() <= (double)false) {
                                v41 = MinecraftInstance.mc.getThePlayer();
                                if (v41 == null) {
                                    Intrinsics.throwNpe();
                                }
                                if (!v41.getOnGround() && !this.flagged) {
                                    v42 = MinecraftInstance.mc.getThePlayer();
                                    if (v42 == null) {
                                        Intrinsics.throwNpe();
                                    }
                                    v42.setMotionY(0.0);
                                    v43 = MinecraftInstance.mc.getThePlayer();
                                    if (v43 == null) {
                                        Intrinsics.throwNpe();
                                    }
                                    v43.setMotionZ(0.0);
                                    v44 = MinecraftInstance.mc.getThePlayer();
                                    if (v44 == null) {
                                        Intrinsics.throwNpe();
                                    }
                                    v44.setMotionX(0.0);
                                    v45 = MinecraftInstance.mc.getThePlayer();
                                    if (v45 == null) {
                                        Intrinsics.throwNpe();
                                    }
                                    v45.setJumpMovementFactor(0.0f);
                                    this.canSpoof = true;
                                    if (!this.tried) {
                                        this.tried = true;
                                        v46 = MinecraftInstance.mc.getNetHandler();
                                        v47 = MinecraftInstance.mc.getThePlayer();
                                        if (v47 == null) {
                                            Intrinsics.throwNpe();
                                        }
                                        v48 = v47.getPosX();
                                        v49 = MinecraftInstance.mc.getThePlayer();
                                        if (v49 == null) {
                                            Intrinsics.throwNpe();
                                        }
                                        v46.addToSendQueue(ClassProviderImpl.INSTANCE.createCPacketPlayerPosition(v48, 32000.0, v49.getPosZ(), false));
                                    }
                                }
                            }
                        }
                    }
                }
                v50 = MinecraftInstance.mc.getThePlayer();
                if (v50 == null) {
                    Intrinsics.throwNpe();
                }
                this.lastRecY = v50.getPosY();
                break;
            }
            case 5: {
                if (this.blink) ** GOTO lbl292
                v51 = MinecraftInstance.mc.getThePlayer();
                if (v51 == null) {
                    Intrinsics.throwNpe();
                }
                v52 = v51.getPosX();
                v53 = MinecraftInstance.mc.getThePlayer();
                if (v53 == null) {
                    Intrinsics.throwNpe();
                }
                v54 = v53.getPosY();
                v55 = MinecraftInstance.mc.getThePlayer();
                if (v55 == null) {
                    Intrinsics.throwNpe();
                }
                collide = new FallingPlayer(v52, v54, v55.getPosZ(), 0.0, 0.0, 0.0, 0.0f, 0.0f, 0.0f, 0.0f).findCollision(60);
                if (!this.canBlink) ** GOTO lbl286
                if (collide == null) ** GOTO lbl260
                v56 = MinecraftInstance.mc.getThePlayer();
                if (v56 == null) {
                    Intrinsics.throwNpe();
                }
                if (!(v56.getPosY() - (double)collide.getY() > ((Number)this.startFallDistValue.get()).doubleValue())) ** GOTO lbl286
lbl260:
                // 2 sources

                v57 = MinecraftInstance.mc.getThePlayer();
                if (v57 == null) {
                    Intrinsics.throwNpe();
                }
                this.posX = v57.getPosX();
                v58 = MinecraftInstance.mc.getThePlayer();
                if (v58 == null) {
                    Intrinsics.throwNpe();
                }
                this.posY = v58.getPosY();
                v59 = MinecraftInstance.mc.getThePlayer();
                if (v59 == null) {
                    Intrinsics.throwNpe();
                }
                this.posZ = v59.getPosZ();
                v60 = MinecraftInstance.mc.getThePlayer();
                if (v60 == null) {
                    Intrinsics.throwNpe();
                }
                this.motionX = v60.getMotionX();
                v61 = MinecraftInstance.mc.getThePlayer();
                if (v61 == null) {
                    Intrinsics.throwNpe();
                }
                this.motionY = v61.getMotionY();
                v62 = MinecraftInstance.mc.getThePlayer();
                if (v62 == null) {
                    Intrinsics.throwNpe();
                }
                this.motionZ = v62.getMotionZ();
                this.packetCache.clear();
                this.blink = true;
lbl286:
                // 3 sources

                v63 = MinecraftInstance.mc.getThePlayer();
                if (v63 == null) {
                    Intrinsics.throwNpe();
                }
                if (!v63.getOnGround()) break;
                this.canBlink = true;
                break;
lbl292:
                // 1 sources

                v64 = MinecraftInstance.mc.getThePlayer();
                if (v64 == null) {
                    Intrinsics.throwNpe();
                }
                if (v64.getFallDistance() > ((Number)this.maxFallDistValue.get()).floatValue()) {
                    v65 = MinecraftInstance.mc.getThePlayer();
                    if (v65 == null) {
                        Intrinsics.throwNpe();
                    }
                    v65.setPositionAndUpdate(this.posX, this.posY, this.posZ);
                    if (((Boolean)this.resetMotionValue.get()).booleanValue()) {
                        v66 = MinecraftInstance.mc.getThePlayer();
                        if (v66 == null) {
                            Intrinsics.throwNpe();
                        }
                        v66.setMotionX(0.0);
                        v67 = MinecraftInstance.mc.getThePlayer();
                        if (v67 == null) {
                            Intrinsics.throwNpe();
                        }
                        v67.setMotionY(0.0);
                        v68 = MinecraftInstance.mc.getThePlayer();
                        if (v68 == null) {
                            Intrinsics.throwNpe();
                        }
                        v68.setMotionZ(0.0);
                        v69 = MinecraftInstance.mc.getThePlayer();
                        if (v69 == null) {
                            Intrinsics.throwNpe();
                        }
                        v69.setJumpMovementFactor(0.0f);
                    } else {
                        v70 = MinecraftInstance.mc.getThePlayer();
                        if (v70 == null) {
                            Intrinsics.throwNpe();
                        }
                        v70.setMotionX(this.motionX);
                        v71 = MinecraftInstance.mc.getThePlayer();
                        if (v71 == null) {
                            Intrinsics.throwNpe();
                        }
                        v71.setMotionY(this.motionY);
                        v72 = MinecraftInstance.mc.getThePlayer();
                        if (v72 == null) {
                            Intrinsics.throwNpe();
                        }
                        v72.setMotionZ(this.motionZ);
                        v73 = MinecraftInstance.mc.getThePlayer();
                        if (v73 == null) {
                            Intrinsics.throwNpe();
                        }
                        v73.setJumpMovementFactor(0.0f);
                    }
                    if (((Boolean)this.autoScaffoldValue.get()).booleanValue()) {
                        v74 = LiquidBounce.INSTANCE.getModuleManager().get(Scaffold.class);
                        if (v74 == null) {
                            Intrinsics.throwNpe();
                        }
                        v74.setState(true);
                    }
                    this.packetCache.clear();
                    this.blink = false;
                    this.canBlink = false;
                    break;
                }
                v75 = MinecraftInstance.mc.getThePlayer();
                if (v75 == null) {
                    Intrinsics.throwNpe();
                }
                if (!v75.getOnGround()) break;
                this.blink = false;
                var4_8 = this.packetCache.iterator();
                while (var4_8.hasNext()) {
                    v76 = packet = var4_8.next();
                    Intrinsics.checkExpressionValueIsNotNull(v76, "packet");
                    PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)((Packet)v76));
                }
                break;
            }
        }
    }

    private final boolean checkVoid() {
        if (MinecraftInstance.mc.getThePlayer() == null) {
            Intrinsics.throwNpe();
        }
        boolean dangerous = true;
        for (int i = (int)(-((v172549).getPosY() - 1.4857625)); i <= 0; ++i) {
            IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
            if (iWorldClient == null) {
                Intrinsics.throwNpe();
            }
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            IAxisAlignedBB iAxisAlignedBB = iEntityPlayerSP.getEntityBoundingBox();
            IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP2 == null) {
                Intrinsics.throwNpe();
            }
            double d = iEntityPlayerSP2.getMotionX() * 0.5;
            double d2 = i;
            IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP3 == null) {
                Intrinsics.throwNpe();
            }
            dangerous = iWorldClient.getCollisionBoxes(iAxisAlignedBB.offset(d, d2, iEntityPlayerSP3.getMotionZ() * 0.5)).isEmpty();
            if (dangerous) continue;
            break;
        }
        return dangerous;
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        IPacket packet = event.getPacket();
        String string = (String)this.modeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
        switch (string3) {
            case "blink": {
                if (!this.blink || !(packet instanceof CPacketPlayer)) break;
                this.packetCache.add((CPacketPlayer)packet);
                event.cancelEvent();
                break;
            }
            case "groundspoof": {
                if (!this.canSpoof || !(packet instanceof CPacketPlayer)) break;
                ((CPacketPlayer)packet).onGround = true;
                break;
            }
            case "jartex": {
                if (this.canSpoof && packet instanceof CPacketPlayer) {
                    ((CPacketPlayer)packet).onGround = true;
                }
                if (!this.canSpoof || !(packet instanceof SPacketPlayerPosLook)) break;
                this.flagged = true;
                break;
            }
            case "oldcubecraft": {
                if (this.canSpoof && packet instanceof CPacketPlayer && ((CPacketPlayer)packet).y < 1145.14191981) {
                    event.cancelEvent();
                }
                if (!this.canSpoof || !(packet instanceof SPacketPlayerPosLook)) break;
                this.flagged = true;
                break;
            }
            case "oldhypixel": {
                if (packet instanceof SPacketPlayerPosLook) {
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    if ((double)iEntityPlayerSP.getFallDistance() > 3.125) {
                        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP2 == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP2.setFallDistance(3.125f);
                    }
                }
                if (!(packet instanceof CPacketPlayer)) break;
                if (((Boolean)this.voidOnlyValue.get()).booleanValue()) {
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    if (iEntityPlayerSP.getFallDistance() >= ((Number)this.maxFallDistValue.get()).floatValue()) {
                        IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP3 == null) {
                            Intrinsics.throwNpe();
                        }
                        if (iEntityPlayerSP3.getMotionY() <= 0.0 && this.checkVoid()) {
                            ((CPacketPlayer)packet).y += 11.0;
                        }
                    }
                }
                if (((Boolean)this.voidOnlyValue.get()).booleanValue()) break;
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                if (!(iEntityPlayerSP.getFallDistance() >= ((Number)this.maxFallDistValue.get()).floatValue())) break;
                ((CPacketPlayer)packet).y += 11.0;
                break;
            }
            case "hypixel": {
                Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(Fly.class);
                if (module == null) {
                    Intrinsics.throwNpe();
                }
                if (!module.getState()) {
                    Module module2 = LiquidBounce.INSTANCE.getModuleManager().getModule(Scaffold.class);
                    if (module2 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (!module2.getState()) {
                        if (!this.packets.isEmpty()) {
                            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                            if (iEntityPlayerSP == null) {
                                Intrinsics.throwNpe();
                            }
                            if (iEntityPlayerSP.getTicksExisted() < 100) {
                                this.packets.clear();
                            }
                        }
                        if (packet instanceof CPacketPlayer) {
                            if (this.isInVoid()) {
                                event.cancelEvent();
                                this.packets.add((CPacketPlayer)packet);
                                if (this.timer.delay(((Number)this.pullbackTime.get()).floatValue())) {
                                    PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)((Packet)new CPacketPlayer.Position(this.lastGroundPos[0], this.lastGroundPos[1] - 1.0, this.lastGroundPos[2], true)));
                                }
                            } else {
                                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                                if (iEntityPlayerSP == null) {
                                    Intrinsics.throwNpe();
                                }
                                this.lastGroundPos[0] = iEntityPlayerSP.getPosX();
                                IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
                                if (iEntityPlayerSP4 == null) {
                                    Intrinsics.throwNpe();
                                }
                                this.lastGroundPos[1] = iEntityPlayerSP4.getPosY();
                                IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
                                if (iEntityPlayerSP5 == null) {
                                    Intrinsics.throwNpe();
                                }
                                this.lastGroundPos[2] = iEntityPlayerSP5.getPosZ();
                                if (!this.packets.isEmpty()) {
                                    Iterator<CPacketPlayer> iterator = this.packets.iterator();
                                    Intrinsics.checkExpressionValueIsNotNull(iterator, "packets.iterator()");
                                    Iterator<CPacketPlayer> var3 = iterator;
                                    ClientUtils.displayChatMessage("[AntiVoid] Release Packets - " + this.packets.size());
                                    while (var3.hasNext()) {
                                        CPacketPlayer p;
                                        if (var3.next() == null) {
                                            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.network.play.client.CPacketPlayer");
                                        }
                                        PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)((Packet)p));
                                    }
                                    this.packets.clear();
                                }
                                this.timer.reset();
                            }
                        }
                    }
                }
                if (!(packet instanceof SPacketPlayerPosLook) || this.packets.size() <= 1) break;
                ClientUtils.displayChatMessage("[AntiVoid] Pullbacks Detected, clear packets list!");
                this.packets.clear();
                break;
            }
        }
    }
}
