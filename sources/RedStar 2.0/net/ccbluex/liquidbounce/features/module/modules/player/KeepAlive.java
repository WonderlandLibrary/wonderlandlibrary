package net.ccbluex.liquidbounce.features.module.modules.player;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.enums.ItemType;
import net.ccbluex.liquidbounce.api.enums.WEnumHand;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.network.IINetHandlerPlayClient;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.injection.backend.WrapperImpl;
import net.ccbluex.liquidbounce.utils.InventoryUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="KeepAlive", description="Tries to prevent you from dying.", category=ModuleCategory.PLAYER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\n\n\b\n\n\b\n\n\u0000\n\n\u0000\n\n\u0000\b\u000020B¢J\t0\n20\fHR0¢\b\n\u0000\bR0\bX¢\n\u0000¨\r"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/player/KeepAlive;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "getModeValue", "()Lnet/ccbluex/liquidbounce/value/ListValue;", "runOnce", "", "onMotion", "", "event", "Lnet/ccbluex/liquidbounce/event/MotionEvent;", "Pride"})
public final class KeepAlive
extends Module {
    @NotNull
    private final ListValue modeValue = new ListValue("Mode", new String[]{"/heal", "Soup"}, "/heal");
    private boolean runOnce;

    @NotNull
    public final ListValue getModeValue() {
        return this.modeValue;
    }

    /*
     * WARNING - void declaration
     */
    @EventTarget
    public final void onMotion(@NotNull MotionEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        if (thePlayer.isDead() || thePlayer.getHealth() <= 0.0f) {
            if (this.runOnce) {
                return;
            }
            String string = (String)this.modeValue.get();
            boolean bl = false;
            String string2 = string;
            if (string2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String string3 = string2.toLowerCase();
            Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
            switch (string3) {
                case "/heal": {
                    thePlayer.sendChatMessage("/heal");
                    break;
                }
                case "soup": {
                    void hand$iv;
                    int soupInHotbar = InventoryUtils.findItem(36, 45, MinecraftInstance.classProvider.getItemEnum(ItemType.MUSHROOM_STEW));
                    if (soupInHotbar == -1) break;
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketHeldItemChange(soupInHotbar - 36));
                    IItemStack iItemStack = thePlayer.getInventory().getStackInSlot(soupInHotbar);
                    WEnumHand wEnumHand = WEnumHand.MAIN_HAND;
                    IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                    boolean $i$f$createUseItemPacket = false;
                    IPacket iPacket = WrapperImpl.INSTANCE.getClassProvider().createCPacketTryUseItem((WEnumHand)hand$iv);
                    iINetHandlerPlayClient.addToSendQueue(iPacket);
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketHeldItemChange(thePlayer.getInventory().getCurrentItem()));
                    break;
                }
            }
            this.runOnce = true;
        } else {
            this.runOnce = false;
        }
    }
}
