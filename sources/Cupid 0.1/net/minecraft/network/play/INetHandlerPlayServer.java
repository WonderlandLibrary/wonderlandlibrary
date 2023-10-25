package net.minecraft.network.play;

import net.minecraft.network.INetHandler;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.network.play.client.C11PacketEnchantItem;
import net.minecraft.network.play.client.C12PacketUpdateSign;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.client.C15PacketClientSettings;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;

public interface INetHandlerPlayServer extends INetHandler {
  void handleAnimation(C0APacketAnimation paramC0APacketAnimation);
  
  void processChatMessage(C01PacketChatMessage paramC01PacketChatMessage);
  
  void processTabComplete(C14PacketTabComplete paramC14PacketTabComplete);
  
  void processClientStatus(C16PacketClientStatus paramC16PacketClientStatus);
  
  void processClientSettings(C15PacketClientSettings paramC15PacketClientSettings);
  
  void processConfirmTransaction(C0FPacketConfirmTransaction paramC0FPacketConfirmTransaction);
  
  void processEnchantItem(C11PacketEnchantItem paramC11PacketEnchantItem);
  
  void processClickWindow(C0EPacketClickWindow paramC0EPacketClickWindow);
  
  void processCloseWindow(C0DPacketCloseWindow paramC0DPacketCloseWindow);
  
  void processVanilla250Packet(C17PacketCustomPayload paramC17PacketCustomPayload);
  
  void processUseEntity(C02PacketUseEntity paramC02PacketUseEntity);
  
  void processKeepAlive(C00PacketKeepAlive paramC00PacketKeepAlive);
  
  void processPlayer(C03PacketPlayer paramC03PacketPlayer);
  
  void processPlayerAbilities(C13PacketPlayerAbilities paramC13PacketPlayerAbilities);
  
  void processPlayerDigging(C07PacketPlayerDigging paramC07PacketPlayerDigging);
  
  void processEntityAction(C0BPacketEntityAction paramC0BPacketEntityAction);
  
  void processInput(C0CPacketInput paramC0CPacketInput);
  
  void processHeldItemChange(C09PacketHeldItemChange paramC09PacketHeldItemChange);
  
  void processCreativeInventoryAction(C10PacketCreativeInventoryAction paramC10PacketCreativeInventoryAction);
  
  void processUpdateSign(C12PacketUpdateSign paramC12PacketUpdateSign);
  
  void processPlayerBlockPlacement(C08PacketPlayerBlockPlacement paramC08PacketPlayerBlockPlacement);
  
  void handleSpectate(C18PacketSpectate paramC18PacketSpectate);
  
  void handleResourcePackStatus(C19PacketResourcePackStatus paramC19PacketResourcePackStatus);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\network\play\INetHandlerPlayServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */