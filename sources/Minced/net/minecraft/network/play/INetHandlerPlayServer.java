// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play;

import net.minecraft.network.play.client.CPacketSeenAdvancements;
import net.minecraft.network.play.client.CPacketRecipeInfo;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.network.play.client.CPacketSteerBoat;
import net.minecraft.network.play.client.CPacketResourcePackStatus;
import net.minecraft.network.play.client.CPacketSpectate;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUpdateSign;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerAbilities;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketPlaceRecipe;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketEnchantItem;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketClientSettings;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketTabComplete;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.INetHandler;

public interface INetHandlerPlayServer extends INetHandler
{
    void handleAnimation(final CPacketAnimation p0);
    
    void processChatMessage(final CPacketChatMessage p0);
    
    void processTabComplete(final CPacketTabComplete p0);
    
    void processClientStatus(final CPacketClientStatus p0);
    
    void processClientSettings(final CPacketClientSettings p0);
    
    void processConfirmTransaction(final CPacketConfirmTransaction p0);
    
    void processEnchantItem(final CPacketEnchantItem p0);
    
    void processClickWindow(final CPacketClickWindow p0);
    
    void func_194308_a(final CPacketPlaceRecipe p0);
    
    void processCloseWindow(final CPacketCloseWindow p0);
    
    void processCustomPayload(final CPacketCustomPayload p0);
    
    void processUseEntity(final CPacketUseEntity p0);
    
    void processKeepAlive(final CPacketKeepAlive p0);
    
    void processPlayer(final CPacketPlayer p0);
    
    void processPlayerAbilities(final CPacketPlayerAbilities p0);
    
    void processPlayerDigging(final CPacketPlayerDigging p0);
    
    void processEntityAction(final CPacketEntityAction p0);
    
    void processInput(final CPacketInput p0);
    
    void processHeldItemChange(final CPacketHeldItemChange p0);
    
    void processCreativeInventoryAction(final CPacketCreativeInventoryAction p0);
    
    void processUpdateSign(final CPacketUpdateSign p0);
    
    void processTryUseItemOnBlock(final CPacketPlayerTryUseItemOnBlock p0);
    
    void processTryUseItem(final CPacketPlayerTryUseItem p0);
    
    void handleSpectate(final CPacketSpectate p0);
    
    void handleResourcePackStatus(final CPacketResourcePackStatus p0);
    
    void processSteerBoat(final CPacketSteerBoat p0);
    
    void processVehicleMove(final CPacketVehicleMove p0);
    
    void processConfirmTeleport(final CPacketConfirmTeleport p0);
    
    void handleRecipeBookUpdate(final CPacketRecipeInfo p0);
    
    void handleSeenAdvancements(final CPacketSeenAdvancements p0);
}
