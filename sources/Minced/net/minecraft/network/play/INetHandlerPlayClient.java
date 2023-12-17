// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play;

import net.minecraft.network.play.server.SPacketPlaceGhostRecipe;
import net.minecraft.network.play.server.SPacketSelectAdvancementsTab;
import net.minecraft.network.play.server.SPacketAdvancementInfo;
import net.minecraft.network.play.server.SPacketMoveVehicle;
import net.minecraft.network.play.server.SPacketCooldown;
import net.minecraft.network.play.server.SPacketUpdateBossInfo;
import net.minecraft.network.play.server.SPacketResourcePackSend;
import net.minecraft.network.play.server.SPacketPlayerListHeaderFooter;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.network.play.server.SPacketWorldBorder;
import net.minecraft.network.play.server.SPacketCamera;
import net.minecraft.network.play.server.SPacketServerDifficulty;
import net.minecraft.network.play.server.SPacketCombatEvent;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketEntityProperties;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.network.play.server.SPacketCollectItem;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.network.play.server.SPacketSpawnPosition;
import net.minecraft.network.play.server.SPacketUpdateScore;
import net.minecraft.network.play.server.SPacketTeams;
import net.minecraft.network.play.server.SPacketUpdateHealth;
import net.minecraft.network.play.server.SPacketSetExperience;
import net.minecraft.network.play.server.SPacketEntityEquipment;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import net.minecraft.network.play.server.SPacketDisplayObjective;
import net.minecraft.network.play.server.SPacketHeldItemChange;
import net.minecraft.network.play.server.SPacketEntityHeadLook;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.network.play.server.SPacketRemoveEntityEffect;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.network.play.server.SPacketPlayerAbilities;
import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketEntity;
import net.minecraft.network.play.server.SPacketJoinGame;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.network.play.server.SPacketUnloadChunk;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.network.play.server.SPacketKeepAlive;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketSetPassengers;
import net.minecraft.network.play.server.SPacketEntityAttach;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketUseBed;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.network.play.server.SPacketWindowProperty;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.network.play.server.SPacketWindowItems;
import net.minecraft.network.play.server.SPacketCloseWindow;
import net.minecraft.network.play.server.SPacketConfirmTransaction;
import net.minecraft.network.play.server.SPacketMaps;
import net.minecraft.network.play.server.SPacketMultiBlockChange;
import net.minecraft.network.play.server.SPacketTabComplete;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketBlockAction;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.network.play.server.SPacketSignEditorOpen;
import net.minecraft.network.play.server.SPacketBlockBreakAnim;
import net.minecraft.network.play.server.SPacketRecipeBook;
import net.minecraft.network.play.server.SPacketStatistics;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.network.play.server.SPacketSpawnPlayer;
import net.minecraft.network.play.server.SPacketSpawnPainting;
import net.minecraft.network.play.server.SPacketScoreboardObjective;
import net.minecraft.network.play.server.SPacketSpawnMob;
import net.minecraft.network.play.server.SPacketSpawnGlobalEntity;
import net.minecraft.network.play.server.SPacketSpawnExperienceOrb;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.network.INetHandler;

public interface INetHandlerPlayClient extends INetHandler
{
    void handleSpawnObject(final SPacketSpawnObject p0);
    
    void handleSpawnExperienceOrb(final SPacketSpawnExperienceOrb p0);
    
    void handleSpawnGlobalEntity(final SPacketSpawnGlobalEntity p0);
    
    void handleSpawnMob(final SPacketSpawnMob p0);
    
    void handleScoreboardObjective(final SPacketScoreboardObjective p0);
    
    void handleSpawnPainting(final SPacketSpawnPainting p0);
    
    void handleSpawnPlayer(final SPacketSpawnPlayer p0);
    
    void handleAnimation(final SPacketAnimation p0);
    
    void handleStatistics(final SPacketStatistics p0);
    
    void handleRecipeBook(final SPacketRecipeBook p0);
    
    void handleBlockBreakAnim(final SPacketBlockBreakAnim p0);
    
    void handleSignEditorOpen(final SPacketSignEditorOpen p0);
    
    void handleUpdateTileEntity(final SPacketUpdateTileEntity p0);
    
    void handleBlockAction(final SPacketBlockAction p0);
    
    void handleBlockChange(final SPacketBlockChange p0);
    
    void handleChat(final SPacketChat p0);
    
    void handleTabComplete(final SPacketTabComplete p0);
    
    void handleMultiBlockChange(final SPacketMultiBlockChange p0);
    
    void handleMaps(final SPacketMaps p0);
    
    void handleConfirmTransaction(final SPacketConfirmTransaction p0);
    
    void handleCloseWindow(final SPacketCloseWindow p0);
    
    void handleWindowItems(final SPacketWindowItems p0);
    
    void handleOpenWindow(final SPacketOpenWindow p0);
    
    void handleWindowProperty(final SPacketWindowProperty p0);
    
    void handleSetSlot(final SPacketSetSlot p0);
    
    void handleCustomPayload(final SPacketCustomPayload p0);
    
    void handleDisconnect(final SPacketDisconnect p0);
    
    void handleUseBed(final SPacketUseBed p0);
    
    void handleEntityStatus(final SPacketEntityStatus p0);
    
    void handleEntityAttach(final SPacketEntityAttach p0);
    
    void handleSetPassengers(final SPacketSetPassengers p0);
    
    void handleExplosion(final SPacketExplosion p0);
    
    void handleChangeGameState(final SPacketChangeGameState p0);
    
    void handleKeepAlive(final SPacketKeepAlive p0);
    
    void handleChunkData(final SPacketChunkData p0);
    
    void processChunkUnload(final SPacketUnloadChunk p0);
    
    void handleEffect(final SPacketEffect p0);
    
    void handleJoinGame(final SPacketJoinGame p0);
    
    void handleEntityMovement(final SPacketEntity p0);
    
    void handlePlayerPosLook(final SPacketPlayerPosLook p0);
    
    void handleParticles(final SPacketParticles p0);
    
    void handlePlayerAbilities(final SPacketPlayerAbilities p0);
    
    void handlePlayerListItem(final SPacketPlayerListItem p0);
    
    void handleDestroyEntities(final SPacketDestroyEntities p0);
    
    void handleRemoveEntityEffect(final SPacketRemoveEntityEffect p0);
    
    void handleRespawn(final SPacketRespawn p0);
    
    void handleEntityHeadLook(final SPacketEntityHeadLook p0);
    
    void handleHeldItemChange(final SPacketHeldItemChange p0);
    
    void handleDisplayObjective(final SPacketDisplayObjective p0);
    
    void handleEntityMetadata(final SPacketEntityMetadata p0);
    
    void handleEntityVelocity(final SPacketEntityVelocity p0);
    
    void handleEntityEquipment(final SPacketEntityEquipment p0);
    
    void handleSetExperience(final SPacketSetExperience p0);
    
    void handleUpdateHealth(final SPacketUpdateHealth p0);
    
    void handleTeams(final SPacketTeams p0);
    
    void handleUpdateScore(final SPacketUpdateScore p0);
    
    void handleSpawnPosition(final SPacketSpawnPosition p0);
    
    void handleTimeUpdate(final SPacketTimeUpdate p0);
    
    void handleSoundEffect(final SPacketSoundEffect p0);
    
    void handleCustomSound(final SPacketCustomSound p0);
    
    void handleCollectItem(final SPacketCollectItem p0);
    
    void handleEntityTeleport(final SPacketEntityTeleport p0);
    
    void handleEntityProperties(final SPacketEntityProperties p0);
    
    void handleEntityEffect(final SPacketEntityEffect p0);
    
    void handleCombatEvent(final SPacketCombatEvent p0);
    
    void handleServerDifficulty(final SPacketServerDifficulty p0);
    
    void handleCamera(final SPacketCamera p0);
    
    void handleWorldBorder(final SPacketWorldBorder p0);
    
    void handleTitle(final SPacketTitle p0);
    
    void handlePlayerListHeaderFooter(final SPacketPlayerListHeaderFooter p0);
    
    void handleResourcePack(final SPacketResourcePackSend p0);
    
    void handleUpdateBossInfo(final SPacketUpdateBossInfo p0);
    
    void handleCooldown(final SPacketCooldown p0);
    
    void handleMoveVehicle(final SPacketMoveVehicle p0);
    
    void handleAdvancementInfo(final SPacketAdvancementInfo p0);
    
    void handleSelectAdvancementsTab(final SPacketSelectAdvancementsTab p0);
    
    void func_194307_a(final SPacketPlaceGhostRecipe p0);
}
