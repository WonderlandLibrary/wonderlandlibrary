/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.network.play;

import net.minecraft.network.INetHandler;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketClientSettings;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.client.CPacketEnchantItem;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlaceRecipe;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerAbilities;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketRecipeInfo;
import net.minecraft.network.play.client.CPacketResourcePackStatus;
import net.minecraft.network.play.client.CPacketSeenAdvancements;
import net.minecraft.network.play.client.CPacketSpectate;
import net.minecraft.network.play.client.CPacketSteerBoat;
import net.minecraft.network.play.client.CPacketTabComplete;
import net.minecraft.network.play.client.CPacketUpdateSign;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketVehicleMove;

public interface INetHandlerPlayServer
extends INetHandler {
    public void handleAnimation(CPacketAnimation var1);

    public void processChatMessage(CPacketChatMessage var1);

    public void processTabComplete(CPacketTabComplete var1);

    public void processClientStatus(CPacketClientStatus var1);

    public void processClientSettings(CPacketClientSettings var1);

    public void processConfirmTransaction(CPacketConfirmTransaction var1);

    public void processEnchantItem(CPacketEnchantItem var1);

    public void processClickWindow(CPacketClickWindow var1);

    public void func_194308_a(CPacketPlaceRecipe var1);

    public void processCloseWindow(CPacketCloseWindow var1);

    public void processCustomPayload(CPacketCustomPayload var1);

    public void processUseEntity(CPacketUseEntity var1);

    public void processKeepAlive(CPacketKeepAlive var1);

    public void processPlayer(CPacketPlayer var1);

    public void processPlayerAbilities(CPacketPlayerAbilities var1);

    public void processPlayerDigging(CPacketPlayerDigging var1);

    public void processEntityAction(CPacketEntityAction var1);

    public void processInput(CPacketInput var1);

    public void processHeldItemChange(CPacketHeldItemChange var1);

    public void processCreativeInventoryAction(CPacketCreativeInventoryAction var1);

    public void processUpdateSign(CPacketUpdateSign var1);

    public void processRightClickBlock(CPacketPlayerTryUseItemOnBlock var1);

    public void processPlayerBlockPlacement(CPacketPlayerTryUseItem var1);

    public void handleSpectate(CPacketSpectate var1);

    public void handleResourcePackStatus(CPacketResourcePackStatus var1);

    public void processSteerBoat(CPacketSteerBoat var1);

    public void processVehicleMove(CPacketVehicleMove var1);

    public void processConfirmTeleport(CPacketConfirmTeleport var1);

    public void func_191984_a(CPacketRecipeInfo var1);

    public void func_194027_a(CPacketSeenAdvancements var1);
}

