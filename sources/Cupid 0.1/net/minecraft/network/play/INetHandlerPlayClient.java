package net.minecraft.network.play;

import net.minecraft.network.INetHandler;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.network.play.server.S04PacketEntityEquipment;
import net.minecraft.network.play.server.S05PacketSpawnPosition;
import net.minecraft.network.play.server.S06PacketUpdateHealth;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S09PacketHeldItemChange;
import net.minecraft.network.play.server.S0APacketUseBed;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.network.play.server.S0DPacketCollectItem;
import net.minecraft.network.play.server.S0EPacketSpawnObject;
import net.minecraft.network.play.server.S0FPacketSpawnMob;
import net.minecraft.network.play.server.S10PacketSpawnPainting;
import net.minecraft.network.play.server.S11PacketSpawnExperienceOrb;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S13PacketDestroyEntities;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.network.play.server.S19PacketEntityHeadLook;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.network.play.server.S1BPacketEntityAttach;
import net.minecraft.network.play.server.S1CPacketEntityMetadata;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S1EPacketRemoveEntityEffect;
import net.minecraft.network.play.server.S1FPacketSetExperience;
import net.minecraft.network.play.server.S20PacketEntityProperties;
import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.network.play.server.S22PacketMultiBlockChange;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.network.play.server.S24PacketBlockAction;
import net.minecraft.network.play.server.S25PacketBlockBreakAnim;
import net.minecraft.network.play.server.S26PacketMapChunkBulk;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.network.play.server.S28PacketEffect;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraft.network.play.server.S31PacketWindowProperty;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.network.play.server.S33PacketUpdateSign;
import net.minecraft.network.play.server.S34PacketMaps;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.network.play.server.S36PacketSignEditorOpen;
import net.minecraft.network.play.server.S37PacketStatistics;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.network.play.server.S39PacketPlayerAbilities;
import net.minecraft.network.play.server.S3APacketTabComplete;
import net.minecraft.network.play.server.S3BPacketScoreboardObjective;
import net.minecraft.network.play.server.S3CPacketUpdateScore;
import net.minecraft.network.play.server.S3DPacketDisplayScoreboard;
import net.minecraft.network.play.server.S3EPacketTeams;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.network.play.server.S40PacketDisconnect;
import net.minecraft.network.play.server.S41PacketServerDifficulty;
import net.minecraft.network.play.server.S42PacketCombatEvent;
import net.minecraft.network.play.server.S43PacketCamera;
import net.minecraft.network.play.server.S44PacketWorldBorder;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.network.play.server.S46PacketSetCompressionLevel;
import net.minecraft.network.play.server.S47PacketPlayerListHeaderFooter;
import net.minecraft.network.play.server.S48PacketResourcePackSend;
import net.minecraft.network.play.server.S49PacketUpdateEntityNBT;

public interface INetHandlerPlayClient extends INetHandler {
  void handleSpawnObject(S0EPacketSpawnObject paramS0EPacketSpawnObject);
  
  void handleSpawnExperienceOrb(S11PacketSpawnExperienceOrb paramS11PacketSpawnExperienceOrb);
  
  void handleSpawnGlobalEntity(S2CPacketSpawnGlobalEntity paramS2CPacketSpawnGlobalEntity);
  
  void handleSpawnMob(S0FPacketSpawnMob paramS0FPacketSpawnMob);
  
  void handleScoreboardObjective(S3BPacketScoreboardObjective paramS3BPacketScoreboardObjective);
  
  void handleSpawnPainting(S10PacketSpawnPainting paramS10PacketSpawnPainting);
  
  void handleSpawnPlayer(S0CPacketSpawnPlayer paramS0CPacketSpawnPlayer);
  
  void handleAnimation(S0BPacketAnimation paramS0BPacketAnimation);
  
  void handleStatistics(S37PacketStatistics paramS37PacketStatistics);
  
  void handleBlockBreakAnim(S25PacketBlockBreakAnim paramS25PacketBlockBreakAnim);
  
  void handleSignEditorOpen(S36PacketSignEditorOpen paramS36PacketSignEditorOpen);
  
  void handleUpdateTileEntity(S35PacketUpdateTileEntity paramS35PacketUpdateTileEntity);
  
  void handleBlockAction(S24PacketBlockAction paramS24PacketBlockAction);
  
  void handleBlockChange(S23PacketBlockChange paramS23PacketBlockChange);
  
  void handleChat(S02PacketChat paramS02PacketChat);
  
  void handleTabComplete(S3APacketTabComplete paramS3APacketTabComplete);
  
  void handleMultiBlockChange(S22PacketMultiBlockChange paramS22PacketMultiBlockChange);
  
  void handleMaps(S34PacketMaps paramS34PacketMaps);
  
  void handleConfirmTransaction(S32PacketConfirmTransaction paramS32PacketConfirmTransaction);
  
  void handleCloseWindow(S2EPacketCloseWindow paramS2EPacketCloseWindow);
  
  void handleWindowItems(S30PacketWindowItems paramS30PacketWindowItems);
  
  void handleOpenWindow(S2DPacketOpenWindow paramS2DPacketOpenWindow);
  
  void handleWindowProperty(S31PacketWindowProperty paramS31PacketWindowProperty);
  
  void handleSetSlot(S2FPacketSetSlot paramS2FPacketSetSlot);
  
  void handleCustomPayload(S3FPacketCustomPayload paramS3FPacketCustomPayload);
  
  void handleDisconnect(S40PacketDisconnect paramS40PacketDisconnect);
  
  void handleUseBed(S0APacketUseBed paramS0APacketUseBed);
  
  void handleEntityStatus(S19PacketEntityStatus paramS19PacketEntityStatus);
  
  void handleEntityAttach(S1BPacketEntityAttach paramS1BPacketEntityAttach);
  
  void handleExplosion(S27PacketExplosion paramS27PacketExplosion);
  
  void handleChangeGameState(S2BPacketChangeGameState paramS2BPacketChangeGameState);
  
  void handleKeepAlive(S00PacketKeepAlive paramS00PacketKeepAlive);
  
  void handleChunkData(S21PacketChunkData paramS21PacketChunkData);
  
  void handleMapChunkBulk(S26PacketMapChunkBulk paramS26PacketMapChunkBulk);
  
  void handleEffect(S28PacketEffect paramS28PacketEffect);
  
  void handleJoinGame(S01PacketJoinGame paramS01PacketJoinGame);
  
  void handleEntityMovement(S14PacketEntity paramS14PacketEntity);
  
  void handlePlayerPosLook(S08PacketPlayerPosLook paramS08PacketPlayerPosLook);
  
  void handleParticles(S2APacketParticles paramS2APacketParticles);
  
  void handlePlayerAbilities(S39PacketPlayerAbilities paramS39PacketPlayerAbilities);
  
  void handlePlayerListItem(S38PacketPlayerListItem paramS38PacketPlayerListItem);
  
  void handleDestroyEntities(S13PacketDestroyEntities paramS13PacketDestroyEntities);
  
  void handleRemoveEntityEffect(S1EPacketRemoveEntityEffect paramS1EPacketRemoveEntityEffect);
  
  void handleRespawn(S07PacketRespawn paramS07PacketRespawn);
  
  void handleEntityHeadLook(S19PacketEntityHeadLook paramS19PacketEntityHeadLook);
  
  void handleHeldItemChange(S09PacketHeldItemChange paramS09PacketHeldItemChange);
  
  void handleDisplayScoreboard(S3DPacketDisplayScoreboard paramS3DPacketDisplayScoreboard);
  
  void handleEntityMetadata(S1CPacketEntityMetadata paramS1CPacketEntityMetadata);
  
  void handleEntityVelocity(S12PacketEntityVelocity paramS12PacketEntityVelocity);
  
  void handleEntityEquipment(S04PacketEntityEquipment paramS04PacketEntityEquipment);
  
  void handleSetExperience(S1FPacketSetExperience paramS1FPacketSetExperience);
  
  void handleUpdateHealth(S06PacketUpdateHealth paramS06PacketUpdateHealth);
  
  void handleTeams(S3EPacketTeams paramS3EPacketTeams);
  
  void handleUpdateScore(S3CPacketUpdateScore paramS3CPacketUpdateScore);
  
  void handleSpawnPosition(S05PacketSpawnPosition paramS05PacketSpawnPosition);
  
  void handleTimeUpdate(S03PacketTimeUpdate paramS03PacketTimeUpdate);
  
  void handleUpdateSign(S33PacketUpdateSign paramS33PacketUpdateSign);
  
  void handleSoundEffect(S29PacketSoundEffect paramS29PacketSoundEffect);
  
  void handleCollectItem(S0DPacketCollectItem paramS0DPacketCollectItem);
  
  void handleEntityTeleport(S18PacketEntityTeleport paramS18PacketEntityTeleport);
  
  void handleEntityProperties(S20PacketEntityProperties paramS20PacketEntityProperties);
  
  void handleEntityEffect(S1DPacketEntityEffect paramS1DPacketEntityEffect);
  
  void handleCombatEvent(S42PacketCombatEvent paramS42PacketCombatEvent);
  
  void handleServerDifficulty(S41PacketServerDifficulty paramS41PacketServerDifficulty);
  
  void handleCamera(S43PacketCamera paramS43PacketCamera);
  
  void handleWorldBorder(S44PacketWorldBorder paramS44PacketWorldBorder);
  
  void handleTitle(S45PacketTitle paramS45PacketTitle);
  
  void handleSetCompressionLevel(S46PacketSetCompressionLevel paramS46PacketSetCompressionLevel);
  
  void handlePlayerListHeaderFooter(S47PacketPlayerListHeaderFooter paramS47PacketPlayerListHeaderFooter);
  
  void handleResourcePack(S48PacketResourcePackSend paramS48PacketResourcePackSend);
  
  void handleEntityNBT(S49PacketUpdateEntityNBT paramS49PacketUpdateEntityNBT);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\network\play\INetHandlerPlayClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */