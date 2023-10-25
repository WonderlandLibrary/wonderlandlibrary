/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.network.play;

import net.minecraft.network.INetHandler;
import net.minecraft.network.play.server.SPacketAdvancementInfo;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.network.play.server.SPacketBlockAction;
import net.minecraft.network.play.server.SPacketBlockBreakAnim;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketCamera;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.network.play.server.SPacketCloseWindow;
import net.minecraft.network.play.server.SPacketCollectItem;
import net.minecraft.network.play.server.SPacketCombatEvent;
import net.minecraft.network.play.server.SPacketConfirmTransaction;
import net.minecraft.network.play.server.SPacketCooldown;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.network.play.server.SPacketDisplayObjective;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.network.play.server.SPacketEntity;
import net.minecraft.network.play.server.SPacketEntityAttach;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketEntityEquipment;
import net.minecraft.network.play.server.SPacketEntityHeadLook;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import net.minecraft.network.play.server.SPacketEntityProperties;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketHeldItemChange;
import net.minecraft.network.play.server.SPacketJoinGame;
import net.minecraft.network.play.server.SPacketKeepAlive;
import net.minecraft.network.play.server.SPacketMaps;
import net.minecraft.network.play.server.SPacketMoveVehicle;
import net.minecraft.network.play.server.SPacketMultiBlockChange;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.network.play.server.SPacketPlaceGhostRecipe;
import net.minecraft.network.play.server.SPacketPlayerAbilities;
import net.minecraft.network.play.server.SPacketPlayerListHeaderFooter;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketRecipeBook;
import net.minecraft.network.play.server.SPacketRemoveEntityEffect;
import net.minecraft.network.play.server.SPacketResourcePackSend;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.network.play.server.SPacketScoreboardObjective;
import net.minecraft.network.play.server.SPacketSelectAdvancementsTab;
import net.minecraft.network.play.server.SPacketServerDifficulty;
import net.minecraft.network.play.server.SPacketSetExperience;
import net.minecraft.network.play.server.SPacketSetPassengers;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.network.play.server.SPacketSignEditorOpen;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnExperienceOrb;
import net.minecraft.network.play.server.SPacketSpawnGlobalEntity;
import net.minecraft.network.play.server.SPacketSpawnMob;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.network.play.server.SPacketSpawnPainting;
import net.minecraft.network.play.server.SPacketSpawnPlayer;
import net.minecraft.network.play.server.SPacketSpawnPosition;
import net.minecraft.network.play.server.SPacketStatistics;
import net.minecraft.network.play.server.SPacketTabComplete;
import net.minecraft.network.play.server.SPacketTeams;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.network.play.server.SPacketUnloadChunk;
import net.minecraft.network.play.server.SPacketUpdateBossInfo;
import net.minecraft.network.play.server.SPacketUpdateHealth;
import net.minecraft.network.play.server.SPacketUpdateScore;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.network.play.server.SPacketUseBed;
import net.minecraft.network.play.server.SPacketWindowItems;
import net.minecraft.network.play.server.SPacketWindowProperty;
import net.minecraft.network.play.server.SPacketWorldBorder;

public interface INetHandlerPlayClient
extends INetHandler {
    public void handleSpawnObject(SPacketSpawnObject var1);

    public void handleSpawnExperienceOrb(SPacketSpawnExperienceOrb var1);

    public void handleSpawnGlobalEntity(SPacketSpawnGlobalEntity var1);

    public void handleSpawnMob(SPacketSpawnMob var1);

    public void handleScoreboardObjective(SPacketScoreboardObjective var1);

    public void handleSpawnPainting(SPacketSpawnPainting var1);

    public void handleSpawnPlayer(SPacketSpawnPlayer var1);

    public void handleAnimation(SPacketAnimation var1);

    public void handleStatistics(SPacketStatistics var1);

    public void func_191980_a(SPacketRecipeBook var1);

    public void handleBlockBreakAnim(SPacketBlockBreakAnim var1);

    public void handleSignEditorOpen(SPacketSignEditorOpen var1);

    public void handleUpdateTileEntity(SPacketUpdateTileEntity var1);

    public void handleBlockAction(SPacketBlockAction var1);

    public void handleBlockChange(SPacketBlockChange var1);

    public void handleChat(SPacketChat var1);

    public void handleTabComplete(SPacketTabComplete var1);

    public void handleMultiBlockChange(SPacketMultiBlockChange var1);

    public void handleMaps(SPacketMaps var1);

    public void handleConfirmTransaction(SPacketConfirmTransaction var1);

    public void handleCloseWindow(SPacketCloseWindow var1);

    public void handleWindowItems(SPacketWindowItems var1);

    public void handleOpenWindow(SPacketOpenWindow var1);

    public void handleWindowProperty(SPacketWindowProperty var1);

    public void handleSetSlot(SPacketSetSlot var1);

    public void handleCustomPayload(SPacketCustomPayload var1);

    public void handleDisconnect(SPacketDisconnect var1);

    public void handleUseBed(SPacketUseBed var1);

    public void handleEntityStatus(SPacketEntityStatus var1);

    public void handleEntityAttach(SPacketEntityAttach var1);

    public void handleSetPassengers(SPacketSetPassengers var1);

    public void handleExplosion(SPacketExplosion var1);

    public void handleChangeGameState(SPacketChangeGameState var1);

    public void handleKeepAlive(SPacketKeepAlive var1);

    public void handleChunkData(SPacketChunkData var1);

    public void processChunkUnload(SPacketUnloadChunk var1);

    public void handleEffect(SPacketEffect var1);

    public void handleJoinGame(SPacketJoinGame var1);

    public void handleEntityMovement(SPacketEntity var1);

    public void handlePlayerPosLook(SPacketPlayerPosLook var1);

    public void handleParticles(SPacketParticles var1);

    public void handlePlayerAbilities(SPacketPlayerAbilities var1);

    public void handlePlayerListItem(SPacketPlayerListItem var1);

    public void handleDestroyEntities(SPacketDestroyEntities var1);

    public void handleRemoveEntityEffect(SPacketRemoveEntityEffect var1);

    public void handleRespawn(SPacketRespawn var1);

    public void handleEntityHeadLook(SPacketEntityHeadLook var1);

    public void handleHeldItemChange(SPacketHeldItemChange var1);

    public void handleDisplayObjective(SPacketDisplayObjective var1);

    public void handleEntityMetadata(SPacketEntityMetadata var1);

    public void handleEntityVelocity(SPacketEntityVelocity var1);

    public void handleEntityEquipment(SPacketEntityEquipment var1);

    public void handleSetExperience(SPacketSetExperience var1);

    public void handleUpdateHealth(SPacketUpdateHealth var1);

    public void handleTeams(SPacketTeams var1);

    public void handleUpdateScore(SPacketUpdateScore var1);

    public void handleSpawnPosition(SPacketSpawnPosition var1);

    public void handleTimeUpdate(SPacketTimeUpdate var1);

    public void handleSoundEffect(SPacketSoundEffect var1);

    public void handleCustomSound(SPacketCustomSound var1);

    public void handleCollectItem(SPacketCollectItem var1);

    public void handleEntityTeleport(SPacketEntityTeleport var1);

    public void handleEntityProperties(SPacketEntityProperties var1);

    public void handleEntityEffect(SPacketEntityEffect var1);

    public void handleCombatEvent(SPacketCombatEvent var1);

    public void handleServerDifficulty(SPacketServerDifficulty var1);

    public void handleCamera(SPacketCamera var1);

    public void handleWorldBorder(SPacketWorldBorder var1);

    public void handleTitle(SPacketTitle var1);

    public void handlePlayerListHeaderFooter(SPacketPlayerListHeaderFooter var1);

    public void handleResourcePack(SPacketResourcePackSend var1);

    public void handleUpdateEntityNBT(SPacketUpdateBossInfo var1);

    public void handleCooldown(SPacketCooldown var1);

    public void handleMoveVehicle(SPacketMoveVehicle var1);

    public void func_191981_a(SPacketAdvancementInfo var1);

    public void func_194022_a(SPacketSelectAdvancementsTab var1);

    public void func_194307_a(SPacketPlaceGhostRecipe var1);
}

