package me.offeex.bloomware.client.module.network

import me.offeex.bloomware.api.util.CPlayerUtil.process
import me.offeex.bloomware.api.util.ChatUtil.addMessage
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventPacket
import net.minecraft.network.packet.c2s.handshake.HandshakeC2SPacket
import net.minecraft.network.packet.c2s.login.LoginHelloC2SPacket
import net.minecraft.network.packet.c2s.login.LoginKeyC2SPacket
import net.minecraft.network.packet.c2s.login.LoginQueryResponseC2SPacket
import net.minecraft.network.packet.c2s.play.*
import net.minecraft.network.packet.c2s.query.QueryPingC2SPacket
import net.minecraft.network.packet.c2s.query.QueryRequestC2SPacket
import net.minecraft.network.packet.s2c.login.*
import net.minecraft.network.packet.s2c.play.*
import net.minecraft.network.packet.s2c.query.QueryPongS2CPacket
import net.minecraft.network.packet.s2c.query.QueryResponseS2CPacket
import net.minecraft.util.Formatting

object PacketLogger : Module("PacketLogger", "", Category.NETWORK) {
	private val c2s = setting("C2S").group(false)

	private val movementC2S = c2s.setting("Movement").group()
	private val boatPaddleStateC2S =
		movementC2S.setting("BoatPaddleState").map(BoatPaddleStateC2SPacket::class.java)
	private val clientCommandC2S =
		movementC2S.setting("ClientCommand").map(ClientCommandC2SPacket::class.java)
	private val playerInputC2S =
		movementC2S.setting("PlayerInput").map(PlayerInputC2SPacket::class.java)
	private val playerMoveC2S = movementC2S.setting("PlayerMove").group()
	private val playerMovePositionAndOnGroundC2S = playerMoveC2S.setting("PositionAndOnGround",).map(PlayerMoveC2SPacket.PositionAndOnGround::class.java)
	private val playerMoveOnGroundOnlyC2S =
		playerMoveC2S.setting("OnGroundOnly").map(PlayerMoveC2SPacket.OnGroundOnly::class.java)
	private val playerMoveLookAndOnGroundC2S =
		playerMoveC2S.setting("LookAndOnGround").map(PlayerMoveC2SPacket.LookAndOnGround::class.java)
	private val playerMoveFullC2S =
		playerMoveC2S.setting("Full").map(PlayerMoveC2SPacket.Full::class.java)
	private val spectatorTeleportC2S =
		movementC2S.setting("SpectatorTeleport").map(SpectatorTeleportC2SPacket::class.java)
	private val teleportConfirmC2S =
		movementC2S.setting("TeleportConfirm").map(TeleportConfirmC2SPacket::class.java)
	private val updatePlayerAbilitiesC2S =
		movementC2S.setting("UpdPlayerAbilities").map(UpdatePlayerAbilitiesC2SPacket::class.java)
	private val vehicleMoveC2S =
		movementC2S.setting("VehicleMove").map(VehicleMoveC2SPacket::class.java)

	private val miscC2S = c2s.setting("Misc").group()
	private val advancementTabC2S =
		miscC2S.setting("AdvancementTab").map(AdvancementTabC2SPacket::class.java)
	private val chatMessageC2S = miscC2S.setting("ChatMessage").map(ChatMessageC2SPacket::class.java)
	private val clientSettingsC2S =
		miscC2S.setting("ClientSettings").map(ClientSettingsC2SPacket::class.java)
	private val clientStatusC2S = miscC2S.setting("ClientStatus").map(ClientStatusC2SPacket::class.java)
	private val keepAliveC2S = miscC2S.setting("KeepAlive").map(KeepAliveC2SPacket::class.java)
	private val requestCommandCompletionsC2S =
		miscC2S.setting("ReqCommandCompletion").map(RequestCommandCompletionsC2SPacket::class.java)
	private val updateDifficultyC2S =
		miscC2S.setting("UpdDifficulty").map(UpdateDifficultyC2SPacket::class.java)
	private val updateDifficultyLockC2S =
		miscC2S.setting("UpdDifficultyLock").map(UpdateDifficultyLockC2SPacket::class.java)

	private val interactionC2S = c2s.setting("Interaction").group()
	private val buttonClickC2S =
		interactionC2S.setting("ButtonClick").map(ButtonClickC2SPacket::class.java)
	private val clickSlotC2S = interactionC2S.setting("ClickSlot").map(ClickSlotC2SPacket::class.java)
	private val handSwingC2S = interactionC2S.setting("HandSwing").map(HandSwingC2SPacket::class.java)
	private val interactBlockC2S =
		interactionC2S.setting("InteractBlock").map(PlayerInteractBlockC2SPacket::class.java)
	private val interactEntityC2S =
		interactionC2S.setting("InteractEntity").map(PlayerInteractEntityC2SPacket::class.java)
	private val interactItemC2S =
		interactionC2S.setting("InteractItem").map(PlayerInteractItemC2SPacket::class.java)
	private val playerActionC2S =
		interactionC2S.setting("PlayerAction").map(PlayerActionC2SPacket::class.java)
	private val selectMerchantTradeC2S =
		interactionC2S.setting("SelectMerchantTrade").map(SelectMerchantTradeC2SPacket::class.java)
	private val updateBeaconC2S =
		interactionC2S.setting("UpdBeacon").map(UpdateBeaconC2SPacket::class.java)
	private val updateCommandBlockC2S =
		interactionC2S.setting("UpdCmdBlock").map(UpdateCommandBlockC2SPacket::class.java)
	private val updateCommandBlockMinecartC2S = interactionC2S.setting("UpdCmdBlockMinecart").map(UpdateCommandBlockMinecartC2SPacket::class.java)
	private val updateJigsawC2S =
		interactionC2S.setting("UpdJigsaw").map(UpdateJigsawC2SPacket::class.java)
	private val updateStructureBlockC2S =
		interactionC2S.setting("UpdStructureBlock").map(UpdateStructureBlockC2SPacket::class.java)

	private val inventoryC2S = c2s.setting("Inventory").group()
	private val bookUpdateC2S = inventoryC2S.setting("BookUpdate").map(BookUpdateC2SPacket::class.java)
	private val closeHandledScreenC2S =
		inventoryC2S.setting("CloseHandledScreen").map(CloseHandledScreenC2SPacket::class.java)
	private val craftRequestC2S =
		inventoryC2S.setting("CraftRequest").map(CraftRequestC2SPacket::class.java)
	private val creativeInventoryActionC2S = inventoryC2S.setting("CreativeInventoryAction").map(CreativeInventoryActionC2SPacket::class.java)
	private val pickFromInventoryC2S =
		inventoryC2S.setting("PickFromInventory").map(PickFromInventoryC2SPacket::class.java)
	private val playPongC2S = inventoryC2S.setting("PlayerPong").map(PlayPongC2SPacket::class.java)
	private val recipeBookDataC2S =
		inventoryC2S.setting("RecipeBookData").map(RecipeBookDataC2SPacket::class.java)
	private val recipeCategoryOptionsC2S =
		inventoryC2S.setting("RecipeCategoryOptions").map(RecipeCategoryOptionsC2SPacket::class.java)
	private val renameItemC2S = inventoryC2S.setting("RenameItem").map(RenameItemC2SPacket::class.java)

	private val networkC2S = c2s.setting("Network").group()
	private val customPayloadC2S =
		networkC2S.setting("CustomPayload").map(CustomPayloadC2SPacket::class.java)
	private val handshakeC2S = networkC2S.setting("Handshake").map(HandshakeC2SPacket::class.java)
	private val loginHelloC2S = networkC2S.setting("LoginHello").map(LoginHelloC2SPacket::class.java)
	private val loginKeyC2S = networkC2S.setting("LoginKey").map(LoginKeyC2SPacket::class.java)
	private val loginQueryResponseC2S =
		networkC2S.setting("LoginQueryResponse").map(LoginQueryResponseC2SPacket::class.java)
	private val resourcePackStatusC2S =
		networkC2S.setting("ResourcePackStatus").map(ResourcePackStatusC2SPacket::class.java)
	private val queryBlockNbtC2S =
		networkC2S.setting("QueryBlockNbt").map(QueryBlockNbtC2SPacket::class.java)
	private val queryEntityNbtC2S =
		networkC2S.setting("QueryEntityNbt").map(QueryEntityNbtC2SPacket::class.java)
	private val queryPingC2S = networkC2S.setting("QueryPing").map(QueryPingC2SPacket::class.java)
	private val queryRequestC2S =
		networkC2S.setting("QueryRequest").map(QueryRequestC2SPacket::class.java)

	private val s2c = setting("S2C").group(false)

	private val entitiesS2C = s2c.setting("Entities").group()
	private val bossBarS2C = entitiesS2C.setting("BossBar").map(BossBarS2CPacket::class.java)
	private val entitiesDestroyS2C =
		entitiesS2C.setting("EntitiesDestroy").map(EntitiesDestroyS2CPacket::class.java)
	private val entityAnimationS2C =
		entitiesS2C.setting("EntityAnimation").map(EntityAnimationS2CPacket::class.java)
	private val entityAttachS2C =
		entitiesS2C.setting("EntityAttach").map(EntityAttachS2CPacket::class.java)
	private val entityAttributesS2C =
		entitiesS2C.setting("EntityAttributes").map(EntityAttributesS2CPacket::class.java)
	private val entityEquipmentUpdateS2C =
		entitiesS2C.setting("EntityEquipmentUpdate").map(EntityEquipmentUpdateS2CPacket::class.java)
	private val entityPassengersSetS2C =
		entitiesS2C.setting("EntityPassengersSet").map(EntityPassengersSetS2CPacket::class.java)
	private val entityPositionS2C =
		entitiesS2C.setting("EntityPosition").map(EntityPositionS2CPacket::class.java)
	private val entityMoveS2C = entitiesS2C.setting("EntityMove").group()
	private val entityMoveRelativeS2C =
		entityMoveS2C.setting("MoveRelative").map(EntityS2CPacket.MoveRelative::class.java)
	private val entityRotateS2C =
		entityMoveS2C.setting("Rotate").map(EntityS2CPacket.Rotate::class.java)
	private val entityRotateAndMoveRelativeS2C =
		entityMoveS2C.setting("Full").map(EntityS2CPacket.RotateAndMoveRelative::class.java)
	private val entitySetHeadYawS2C =
		entitiesS2C.setting("EntitySetHeadYaw").map(EntitySetHeadYawS2CPacket::class.java)
	private val entitySpawnS2C =
		entitiesS2C.setting("EntitySpawn").map(EntitySpawnS2CPacket::class.java)
	private val entityStatusEffectS2C =
		entitiesS2C.setting("EntityStatusEffect").map(EntityStatusEffectS2CPacket::class.java)
	private val entityStatusS2C =
		entitiesS2C.setting("EntityStatus").map(EntityStatusS2CPacket::class.java)
	private val entityTrackerUpdateS2C =
		entitiesS2C.setting("EntityTrackerUpdate").map(EntityTrackerUpdateS2CPacket::class.java)
	private val entityVelocityUpdateS2C =
		entitiesS2C.setting("EntityVelocityUpdate").map(EntityVelocityUpdateS2CPacket::class.java)
	private val lookAtS2C = entitiesS2C.setting("LookAt").map(LookAtS2CPacket::class.java)
	private val playSoundFromEntityS2C =
		entitiesS2C.setting("PlaySoundFromEntity").map(PlaySoundFromEntityS2CPacket::class.java)
	private val playSoundS2C =
		entitiesS2C.setting("PlaySound").map(PlaySoundS2CPacket::class.java)
	private val removeEntityStatusEffectS2C = entitiesS2C.setting("RemoveEntityStatusEffect").map(RemoveEntityStatusEffectS2CPacket::class.java)
	private val setCameraEntityS2C =
		entitiesS2C.setting("SetCameraEntity").map(SetCameraEntityS2CPacket::class.java)

	private val inventoryS2C = s2c.setting("Inventory").group()
	private val craftFailedResponseS2C =
		inventoryS2C.setting("CraftFailedResponse").map(CraftFailedResponseS2CPacket::class.java)
	private val invS2C = inventoryS2C.setting("Inventory").map(InventoryS2CPacket::class.java)
	private val setTradeOffersS2C =
		inventoryS2C.setting("SetTradeOffers").map(SetTradeOffersS2CPacket::class.java)
	private val updateSelectedSlotS2C =
		inventoryS2C.setting("UpdateSelectedSlot").map(UpdateSelectedSlotS2CPacket::class.java)

	private val miscS2C = s2c.setting("Misc").group()
	private val chatMessageS2C = miscS2C.setting("ChatMessage").map(ChatMessageS2CPacket::class.java)
	private val commandSuggestionsS2C =
		miscS2C.setting("CommandSuggestions").map(CommandSuggestionsS2CPacket::class.java)
	private val deathMessageS2C = miscS2C.setting("DeathMessage").map(DeathMessageS2CPacket::class.java)
	private val difficultyS2C = miscS2C.setting("Difficulty").map(DifficultyS2CPacket::class.java)
	private val gameMessageS2C = miscS2C.setting("GameMessage").map(GameMessageS2CPacket::class.java)
	private val overlayMessageS2C =
		miscS2C.setting("OverlayMessage").map(OverlayMessageS2CPacket::class.java)
	private val selectAdvancementTabS2C =
		miscS2C.setting("SelectAdvancementTab").map(SelectAdvancementTabS2CPacket::class.java)
	private val subtitleS2C = miscS2C.setting("Subtitle").map(SubtitleS2CPacket::class.java)
	private val synchronizeRecipesS2C =
		miscS2C.setting("SynchronizeRecipes").map(SynchronizeRecipesS2CPacket::class.java)
	private val synchronizeTagsS2C =
		miscS2C.setting("SynchronizeTags").map(SynchronizeTagsS2CPacket::class.java)

	private val networkS2C = s2c.setting("Network").group()
	private val customPayloadS2C =
		networkS2C.setting("CustomPayload").map(CustomPayloadS2CPacket::class.java)
	private val disconnectS2C = networkS2C.setting("Disconnect").map(DisconnectS2CPacket::class.java)
	private val loginCompressionS2C =
		networkS2C.setting("LoginCompression").map(LoginCompressionS2CPacket::class.java)
	private val loginDisconnectS2C =
		networkS2C.setting("LoginDisconnect").map(LoginDisconnectS2CPacket::class.java)
	private val loginHelloS2C = networkS2C.setting("LoginHello").map(LoginHelloS2CPacket::class.java)
	private val loginQueryRequestS2C =
		networkS2C.setting("LoginQueryRequest").map(LoginQueryRequestS2CPacket::class.java)
	private val loginSuccessS2C =
		networkS2C.setting("LoginSuccess").map(LoginSuccessS2CPacket::class.java)
	private val playPingS2C = networkS2C.setting("PlayPing").map(PlayPingS2CPacket::class.java)
	private val resourcePackSendS2C =
		networkS2C.setting("ResourcePackSend").map(ResourcePackSendS2CPacket::class.java)
	private val unlockRecipesS2C =
		networkS2C.setting("UnlockRecipes").map(UnlockRecipesS2CPacket::class.java)
	private val nbtQueryResponseS2C =
		networkS2C.setting("NbtQueryResponse").map(NbtQueryResponseS2CPacket::class.java)
	private val queryPongS2C = networkS2C.setting("QueryPong").map(QueryPongS2CPacket::class.java)
	private val queryResponseS2C =
		networkS2C.setting("QueryResponse").map(QueryResponseS2CPacket::class.java)

	private val playerS2C = s2c.setting("Player").group()
	private val advancementUpdateS2C =
		playerS2C.setting("AdvancementUpdate").map(AdvancementUpdateS2CPacket::class.java)
	private val cooldownUpdateS2C =
		playerS2C.setting("CooldownUpdate").map(CooldownUpdateS2CPacket::class.java)
	private val endCombatS2C = playerS2C.setting("EndCombat").map(EndCombatS2CPacket::class.java)
	private val enterCombatS2C = playerS2C.setting("EnterCombat").map(EnterCombatS2CPacket::class.java)
	private val experienceBarUpdateS2C =
		playerS2C.setting("ExperienceBarUpdate").map(ExperienceBarUpdateS2CPacket::class.java)
	private val experienceOrbSpawnS2C =
		playerS2C.setting("ExperienceOrbSpawn").map(ExperienceOrbSpawnS2CPacket::class.java)
	private val healthUpdateS2C =
		playerS2C.setting("HealthUpdate").map(HealthUpdateS2CPacket::class.java)
	private val keepAliveS2C = playerS2C.setting("KeepAlive").map(KeepAliveS2CPacket::class.java)
	private val openWrittenBookS2C =
		playerS2C.setting("OpenWrittenBook").map(OpenWrittenBookS2CPacket::class.java)
	private val playerAbilitiesS2C =
		playerS2C.setting("PlayerAbilities").map(PlayerAbilitiesS2CPacket::class.java)
	private val playerActionResponseS2C =
		playerS2C.setting("PlayerActionResponse").map(PlayerActionResponseS2CPacket::class.java)
	private val playerListHeaderS2C =
		playerS2C.setting("PlayerListHeader").map(PlayerListHeaderS2CPacket::class.java)
	private val playerListS2C = playerS2C.setting("PlayerList").map(PlayerListS2CPacket::class.java)
	private val playerPositionLookS2C =
		playerS2C.setting("PlayerPositionLook").map(PlayerPositionLookS2CPacket::class.java)
	private val playerRespawnS2C =
		playerS2C.setting("PlayerRespawn").map(PlayerRespawnS2CPacket::class.java)
	private val playerSpawnPositionS2C =
		playerS2C.setting("PlayerSpawnPosition").map(PlayerSpawnPositionS2CPacket::class.java)
	private val playerSpawnS2C = playerS2C.setting("PlayerSpawn").map(PlayerSpawnS2CPacket::class.java)
	private val scoreboardDisplayS2C =
		playerS2C.setting("ScoreboardDisplay").map(ScoreboardDisplayS2CPacket::class.java)
	private val scoreBoardObjectiveUpdateS2C =
		playerS2C.setting("ScoreObjectiveUpdate").map(ScoreboardObjectiveUpdateS2CPacket::class.java)
	private val scoreboardPlayerUpdateS2C =
		playerS2C.setting("ScoreboardPlayerUpdate").map(ScoreboardPlayerUpdateS2CPacket::class.java)
	private val statisticsS2C = playerS2C.setting("Statistics").map(StatisticsS2CPacket::class.java)
	private val teamS2C = playerS2C.setting("Team").map(TeamS2CPacket::class.java)
	private val vehicleMoveS2C = playerS2C.setting("VehicleMove").map(VehicleMoveS2CPacket::class.java)

	private val screenS2C = s2c.setting("Screen").group()
	private val clearTitleS2C = screenS2C.setting("ClearTitle").map(ClearTitleS2CPacket::class.java)
	private val closeScreenS2C = screenS2C.setting("ClearTitle").map(CloseScreenS2CPacket::class.java)
	private val openHorseScreenS2C =
		screenS2C.setting("OpenHorseScreen").map(OpenHorseScreenS2CPacket::class.java)
	private val openScreenS2C = screenS2C.setting("OpenScreen").map(OpenScreenS2CPacket::class.java)
	private val screenHandlerPropertyUpdateS2C = screenS2C.setting("ScreenHandlerPropUpdate").map(ScreenHandlerPropertyUpdateS2CPacket::class.java)
	private val signEditorOpenS2C =
		screenS2C.setting("SignEditorOpen").map(SignEditorOpenS2CPacket::class.java)
	private val titleFadeS2C = screenS2C.setting("TitleFade").map(TitleFadeS2CPacket::class.java)
	private val titleS2C = screenS2C.setting("Title").map(TitleS2CPacket::class.java)

	private val worldS2C = s2c.setting("World").group()
	private val blockBreakingProgressS2C =
		worldS2C.setting("BlockBreakingProgress").map(BlockBreakingProgressS2CPacket::class.java)
	private val blockEntityUpdateS2C =
		worldS2C.setting("BlockEntityUpdate").map(BlockEntityUpdateS2CPacket::class.java)
	private val blockEventS2C = worldS2C.setting("BlockEvent").map(BlockEventS2CPacket::class.java)
	private val blockUpdateS2C = worldS2C.setting("BlockUpdate").map(BlockUpdateS2CPacket::class.java)
	private val chunkDataS2C = worldS2C.setting("ChunkData").map(ChunkDataS2CPacket::class.java)
	private val chunkDeltaUpdateS2C =
		worldS2C.setting("ChunkDeltaUpdate").map(ChunkDeltaUpdateS2CPacket::class.java)
	private val chunkLoadDistanceS2C =
		worldS2C.setting("ChunkLoadDistance").map(ChunkLoadDistanceS2CPacket::class.java)
	private val chunkRenderDistanceCenterS2C =
		worldS2C.setting("ChunkDistanceCenter").map(ChunkRenderDistanceCenterS2CPacket::class.java)
	private val explosionS2C = worldS2C.setting("Explosion").map(ExplosionS2CPacket::class.java)
	private val gameJoinS2C = worldS2C.setting("GameJoin").map(GameJoinS2CPacket::class.java)
	private val gameStateChangeS2C =
		worldS2C.setting("GameStateChange").map(GameStateChangeS2CPacket::class.java)
	private val itemPickupAnimationS2C =
		worldS2C.setting("ItemPickupAnimation").map(ItemPickupAnimationS2CPacket::class.java)
	private val lightUpdateS2C = worldS2C.setting("LightUpdate").map(LightUpdateS2CPacket::class.java)
	private val mapUpdateS2C = worldS2C.setting("MapUpdate").map(MapUpdateS2CPacket::class.java)
	private val particleS2C = worldS2C.setting("Particle").map(ParticleS2CPacket::class.java)
	private val simulationDistanceS2C =
		worldS2C.setting("SimulationDistance").map(SimulationDistanceS2CPacket::class.java)
	private val stopSoundS2C = worldS2C.setting("StopSound").map(StopSoundS2CPacket::class.java)
	private val unloadChunkS2C = worldS2C.setting("UnloadChunk").map(UnloadChunkS2CPacket::class.java)

	private val worldBorderS2C = s2c.setting("WorldBorder").group()
	private val worldBorderCenterChangedS2C =
		worldBorderS2C.setting("CenterChanged").map(WorldBorderCenterChangedS2CPacket::class.java)
	private val worldBorderInitializeS2C =
		worldBorderS2C.setting("Initialize").map(WorldBorderInitializeS2CPacket::class.java)
	private val worldBorderInterpolateSizeS2C =
		worldBorderS2C.setting("InterpolateSize").map(WorldBorderInterpolateSizeS2CPacket::class.java)
	private val worldBorderSizeChangedS2C =
		worldBorderS2C.setting("SizeChanged").map(WorldBorderSizeChangedS2CPacket::class.java)
	private val worldBorderWarningBlocksChangedS2C = worldBorderS2C.setting("WarningBlocksChanged").map(WorldBorderWarningBlocksChangedS2CPacket::class.java)
	private val worldBorderWarningTimeChangedS2C = worldBorderS2C.setting("WarningTimeChanged").map(WorldBorderWarningTimeChangedS2CPacket::class.java)
	private val worldEventS2C = worldS2C.setting("WorldEvent").map(WorldEventS2CPacket::class.java)
	private val worldTimeUpdateS2C =
		worldS2C.setting("WorldTimeUpdate").map(WorldTimeUpdateS2CPacket::class.java)

	@Subscribe
	private fun onPacketSend(event: EventPacket.Send) {
		if (!event.shift) c2s.process(event) { msg(event, "Sent", Formatting.RED) }
	}

	@Subscribe
	private fun onPacketReceive(event: EventPacket.Receive) {
		if (!event.shift) s2c.process(event) { msg(event, "Received", Formatting.GREEN) }
	}

	private fun msg(event: EventPacket, direction: String, color: Formatting) =
		addMessage("$direction packet -> " + color + event.packet.javaClass.simpleName)
}
