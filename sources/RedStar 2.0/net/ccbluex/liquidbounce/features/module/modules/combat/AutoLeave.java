package net.ccbluex.liquidbounce.features.module.modules.combat;

import java.util.Random;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.DoubleCompanionObject;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketUseEntity;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="AutoLeave", description="Automatically makes you leave the server whenever your health is low.", category=ModuleCategory.COMBAT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000$\n\n\n\b\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\b\u000020B¢J0\b2\t0\nHR0X¢\n\u0000R0X¢\n\u0000¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/combat/AutoLeave;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "healthValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "onUpdate", "", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "Pride"})
public final class AutoLeave
extends Module {
    private final FloatValue healthValue = new FloatValue("Health", 8.0f, 0.0f, 20.0f);
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Quit", "InvalidPacket", "SelfHurt", "IllegalChat"}, "Quit");

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        if (thePlayer.getHealth() <= ((Number)this.healthValue.get()).floatValue() && !thePlayer.getCapabilities().isCreativeMode() && !MinecraftInstance.mc.isIntegratedServerRunning()) {
            String string = (String)this.modeValue.get();
            boolean bl = false;
            String string2 = string;
            if (string2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String string3 = string2.toLowerCase();
            Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
            switch (string3) {
                case "quit": {
                    IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
                    if (iWorldClient == null) {
                        Intrinsics.throwNpe();
                    }
                    iWorldClient.sendQuittingDisconnectingPacket();
                    break;
                }
                case "invalidpacket": {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(DoubleCompanionObject.INSTANCE.getNaN(), DoubleCompanionObject.INSTANCE.getNEGATIVE_INFINITY(), DoubleCompanionObject.INSTANCE.getPOSITIVE_INFINITY(), !thePlayer.getOnGround()));
                    break;
                }
                case "selfhurt": {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketUseEntity((IEntity)thePlayer, ICPacketUseEntity.WAction.ATTACK));
                    break;
                }
                case "illegalchat": {
                    thePlayer.sendChatMessage(String.valueOf(new Random().nextInt()) + "§§§" + new Random().nextInt());
                    break;
                }
            }
            this.setState(false);
        }
    }
}
