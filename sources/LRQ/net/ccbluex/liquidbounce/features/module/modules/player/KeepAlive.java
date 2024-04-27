/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 */
package net.ccbluex.liquidbounce.features.module.modules.player;

import kotlin.TypeCastException;
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

@ModuleInfo(name="KeepAlive", description="Tries to prevent you from dying.", category=ModuleCategory.PLAYER)
public final class KeepAlive
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"/heal", "Soup"}, "/heal");
    private boolean runOnce;

    public final ListValue getModeValue() {
        return this.modeValue;
    }

    /*
     * WARNING - void declaration
     */
    @EventTarget
    public final void onMotion(MotionEvent event) {
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
            switch (string2.toLowerCase()) {
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
                }
            }
            this.runOnce = true;
        } else {
            this.runOnce = false;
        }
    }
}

