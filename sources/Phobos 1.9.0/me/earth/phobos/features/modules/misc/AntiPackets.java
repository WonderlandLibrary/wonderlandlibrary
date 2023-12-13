package me.earth.phobos.features.modules.misc;

import me.earth.phobos.event.events.PacketEvent;
import me.earth.phobos.features.command.Command;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AntiPackets
        extends Module {
    private final Setting<Mode> mode = this.register(new Setting<Mode>("Packets", Mode.CLIENT));
    private final Setting<Integer> page = this.register(new Setting<Object>("SPackets", Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(10), v -> this.mode.getValue() == Mode.SERVER));
    private final Setting<Integer> pages = this.register(new Setting<Object>("CPackets", Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(4), v -> this.mode.getValue() == Mode.CLIENT));
    private final Setting<Boolean> AdvancementInfo = this.register(new Setting<Object>("AdvancementInfo", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 1));
    private final Setting<Boolean> Animation = this.register(new Setting<Object>("Animation", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 1));
    private final Setting<Boolean> BlockAction = this.register(new Setting<Object>("BlockAction", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 1));
    private final Setting<Boolean> BlockBreakAnim = this.register(new Setting<Object>("BlockBreakAnim", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 1));
    private final Setting<Boolean> BlockChange = this.register(new Setting<Object>("BlockChange", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 1));
    private final Setting<Boolean> Camera = this.register(new Setting<Object>("Camera", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 1));
    private final Setting<Boolean> ChangeGameState = this.register(new Setting<Object>("ChangeGameState", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 1));
    private final Setting<Boolean> Chat = this.register(new Setting<Object>("Chat", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 1));
    private final Setting<Boolean> ChunkData = this.register(new Setting<Object>("ChunkData", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 2));
    private final Setting<Boolean> CloseWindow = this.register(new Setting<Object>("CloseWindow", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 2));
    private final Setting<Boolean> CollectItem = this.register(new Setting<Object>("CollectItem", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 2));
    private final Setting<Boolean> CombatEvent = this.register(new Setting<Object>("Combatevent", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 2));
    private final Setting<Boolean> ConfirmTransaction = this.register(new Setting<Object>("ConfirmTransaction", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 2));
    private final Setting<Boolean> Cooldown = this.register(new Setting<Object>("Cooldown", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 2));
    private final Setting<Boolean> CustomPayload = this.register(new Setting<Object>("CustomPayload", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 2));
    private final Setting<Boolean> CustomSound = this.register(new Setting<Object>("CustomSound", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 2));
    private final Setting<Boolean> DestroyEntities = this.register(new Setting<Object>("DestroyEntities", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 3));
    private final Setting<Boolean> Disconnect = this.register(new Setting<Object>("Disconnect", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 3));
    private final Setting<Boolean> DisplayObjective = this.register(new Setting<Object>("DisplayObjective", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 3));
    private final Setting<Boolean> Effect = this.register(new Setting<Object>("Effect", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 3));
    private final Setting<Boolean> Entity = this.register(new Setting<Object>("Entity", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 3));
    private final Setting<Boolean> EntityAttach = this.register(new Setting<Object>("EntityAttach", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 3));
    private final Setting<Boolean> EntityEffect = this.register(new Setting<Object>("EntityEffect", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 3));
    private final Setting<Boolean> EntityEquipment = this.register(new Setting<Object>("EntityEquipment", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 3));
    private final Setting<Boolean> EntityHeadLook = this.register(new Setting<Object>("EntityHeadLook", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 4));
    private final Setting<Boolean> EntityMetadata = this.register(new Setting<Object>("EntityMetadata", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 4));
    private final Setting<Boolean> EntityProperties = this.register(new Setting<Object>("EntityProperties", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 4));
    private final Setting<Boolean> EntityStatus = this.register(new Setting<Object>("EntityStatus", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 4));
    private final Setting<Boolean> EntityTeleport = this.register(new Setting<Object>("EntityTeleport", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 4));
    private final Setting<Boolean> EntityVelocity = this.register(new Setting<Object>("EntityVelocity", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 4));
    private final Setting<Boolean> Explosion = this.register(new Setting<Object>("Explosion", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 4));
    private final Setting<Boolean> HeldItemChange = this.register(new Setting<Object>("HeldItemChange", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 4));
    private final Setting<Boolean> JoinGame = this.register(new Setting<Object>("JoinGame", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 5));
    private final Setting<Boolean> KeepAlive = this.register(new Setting<Object>("KeepAlive", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 5));
    private final Setting<Boolean> Maps = this.register(new Setting<Object>("Maps", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 5));
    private final Setting<Boolean> MoveVehicle = this.register(new Setting<Object>("MoveVehicle", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 5));
    private final Setting<Boolean> MultiBlockChange = this.register(new Setting<Object>("MultiBlockChange", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 5));
    private final Setting<Boolean> OpenWindow = this.register(new Setting<Object>("OpenWindow", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 5));
    private final Setting<Boolean> Particles = this.register(new Setting<Object>("Particles", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 5));
    private final Setting<Boolean> PlaceGhostRecipe = this.register(new Setting<Object>("PlaceGhostRecipe", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 5));
    private final Setting<Boolean> PlayerAbilities = this.register(new Setting<Object>("PlayerAbilities", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 6));
    private final Setting<Boolean> PlayerListHeaderFooter = this.register(new Setting<Object>("PlayerListHeaderFooter", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 6));
    private final Setting<Boolean> PlayerListItem = this.register(new Setting<Object>("PlayerListItem", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 6));
    private final Setting<Boolean> PlayerPosLook = this.register(new Setting<Object>("PlayerPosLook", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 6));
    private final Setting<Boolean> RecipeBook = this.register(new Setting<Object>("RecipeBook", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 6));
    private final Setting<Boolean> RemoveEntityEffect = this.register(new Setting<Object>("RemoveEntityEffect", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 6));
    private final Setting<Boolean> ResourcePackSend = this.register(new Setting<Object>("ResourcePackSend", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 6));
    private final Setting<Boolean> Respawn = this.register(new Setting<Object>("Respawn", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 6));
    private final Setting<Boolean> ScoreboardObjective = this.register(new Setting<Object>("ScoreboardObjective", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 7));
    private final Setting<Boolean> SelectAdvancementsTab = this.register(new Setting<Object>("SelectAdvancementsTab", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 7));
    private final Setting<Boolean> ServerDifficulty = this.register(new Setting<Object>("ServerDifficulty", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 7));
    private final Setting<Boolean> SetExperience = this.register(new Setting<Object>("SetExperience", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 7));
    private final Setting<Boolean> SetPassengers = this.register(new Setting<Object>("SetPassengers", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 7));
    private final Setting<Boolean> SetSlot = this.register(new Setting<Object>("SetSlot", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 7));
    private final Setting<Boolean> SignEditorOpen = this.register(new Setting<Object>("SignEditorOpen", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 7));
    private final Setting<Boolean> SoundEffect = this.register(new Setting<Object>("SoundEffect", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 7));
    private final Setting<Boolean> SpawnExperienceOrb = this.register(new Setting<Object>("SpawnExperienceOrb", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 8));
    private final Setting<Boolean> SpawnGlobalEntity = this.register(new Setting<Object>("SpawnGlobalEntity", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 8));
    private final Setting<Boolean> SpawnMob = this.register(new Setting<Object>("SpawnMob", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 8));
    private final Setting<Boolean> SpawnObject = this.register(new Setting<Object>("SpawnObject", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 8));
    private final Setting<Boolean> SpawnPainting = this.register(new Setting<Object>("SpawnPainting", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 8));
    private final Setting<Boolean> SpawnPlayer = this.register(new Setting<Object>("SpawnPlayer", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 8));
    private final Setting<Boolean> SpawnPosition = this.register(new Setting<Object>("SpawnPosition", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 8));
    private final Setting<Boolean> Statistics = this.register(new Setting<Object>("Statistics", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 8));
    private final Setting<Boolean> TabComplete = this.register(new Setting<Object>("TabComplete", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 9));
    private final Setting<Boolean> Teams = this.register(new Setting<Object>("Teams", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 9));
    private final Setting<Boolean> TimeUpdate = this.register(new Setting<Object>("TimeUpdate", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 9));
    private final Setting<Boolean> Title = this.register(new Setting<Object>("Title", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 9));
    private final Setting<Boolean> UnloadChunk = this.register(new Setting<Object>("UnloadChunk", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 9));
    private final Setting<Boolean> UpdateBossInfo = this.register(new Setting<Object>("UpdateBossInfo", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 9));
    private final Setting<Boolean> UpdateHealth = this.register(new Setting<Object>("UpdateHealth", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 9));
    private final Setting<Boolean> UpdateScore = this.register(new Setting<Object>("UpdateScore", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 9));
    private final Setting<Boolean> UpdateTileEntity = this.register(new Setting<Object>("UpdateTileEntity", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 10));
    private final Setting<Boolean> UseBed = this.register(new Setting<Object>("UseBed", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 10));
    private final Setting<Boolean> WindowItems = this.register(new Setting<Object>("WindowItems", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 10));
    private final Setting<Boolean> WindowProperty = this.register(new Setting<Object>("WindowProperty", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 10));
    private final Setting<Boolean> WorldBorder = this.register(new Setting<Object>("WorldBorder", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 10));
    private final Setting<Boolean> Animations = this.register(new Setting<Object>("Animations", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 1));
    private final Setting<Boolean> ChatMessage = this.register(new Setting<Object>("ChatMessage", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 1));
    private final Setting<Boolean> ClickWindow = this.register(new Setting<Object>("ClickWindow", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 1));
    private final Setting<Boolean> ClientSettings = this.register(new Setting<Object>("ClientSettings", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 1));
    private final Setting<Boolean> ClientStatus = this.register(new Setting<Object>("ClientStatus", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 1));
    private final Setting<Boolean> CloseWindows = this.register(new Setting<Object>("CloseWindows", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 1));
    private final Setting<Boolean> ConfirmTeleport = this.register(new Setting<Object>("ConfirmTeleport", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 1));
    private final Setting<Boolean> ConfirmTransactions = this.register(new Setting<Object>("ConfirmTransactions", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 1));
    private final Setting<Boolean> CreativeInventoryAction = this.register(new Setting<Object>("CreativeInventoryAction", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 2));
    private final Setting<Boolean> CustomPayloads = this.register(new Setting<Object>("CustomPayloads", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 2));
    private final Setting<Boolean> EnchantItem = this.register(new Setting<Object>("EnchantItem", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 2));
    private final Setting<Boolean> EntityAction = this.register(new Setting<Object>("EntityAction", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 2));
    private final Setting<Boolean> HeldItemChanges = this.register(new Setting<Object>("HeldItemChanges", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 2));
    private final Setting<Boolean> Input = this.register(new Setting<Object>("Input", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 2));
    private final Setting<Boolean> KeepAlives = this.register(new Setting<Object>("KeepAlives", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 2));
    private final Setting<Boolean> PlaceRecipe = this.register(new Setting<Object>("PlaceRecipe", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 2));
    private final Setting<Boolean> Player = this.register(new Setting<Object>("Player", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 3));
    private final Setting<Boolean> PlayerAbility = this.register(new Setting<Object>("PlayerAbility", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 3));
    private final Setting<Boolean> PlayerDigging = this.register(new Setting<Object>("PlayerDigging", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.CLIENT && this.page.getValue() == 3));
    private final Setting<Boolean> PlayerTryUseItem = this.register(new Setting<Object>("PlayerTryUseItem", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 3));
    private final Setting<Boolean> PlayerTryUseItemOnBlock = this.register(new Setting<Object>("TryUseItemOnBlock", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 3));
    private final Setting<Boolean> RecipeInfo = this.register(new Setting<Object>("RecipeInfo", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 3));
    private final Setting<Boolean> ResourcePackStatus = this.register(new Setting<Object>("ResourcePackStatus", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 3));
    private final Setting<Boolean> SeenAdvancements = this.register(new Setting<Object>("SeenAdvancements", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 3));
    private final Setting<Boolean> PlayerPackets = this.register(new Setting<Object>("PlayerPackets", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 4));
    private final Setting<Boolean> Spectate = this.register(new Setting<Object>("Spectate", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 4));
    private final Setting<Boolean> SteerBoat = this.register(new Setting<Object>("SteerBoat", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 4));
    private final Setting<Boolean> TabCompletion = this.register(new Setting<Object>("TabCompletion", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 4));
    private final Setting<Boolean> UpdateSign = this.register(new Setting<Object>("UpdateSign", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 4));
    private final Setting<Boolean> UseEntity = this.register(new Setting<Object>("UseEntity", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 4));
    private final Setting<Boolean> VehicleMove = this.register(new Setting<Object>("VehicleMove", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 4));
    private int hudAmount = 0;

    public AntiPackets() {
        super("AntiPackets", "Blocks Packets", Module.Category.MISC, true, false, false);
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (!this.isEnabled()) {
            return;
        }
        if (event.getPacket() instanceof CPacketAnimation && this.Animations.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketChatMessage && this.ChatMessage.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketClickWindow && this.ClickWindow.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketClientSettings && this.ClientSettings.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketClientStatus && this.ClientStatus.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketCloseWindow && this.CloseWindows.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketConfirmTeleport && this.ConfirmTeleport.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketConfirmTransaction && this.ConfirmTransactions.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketCreativeInventoryAction && this.CreativeInventoryAction.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketCustomPayload && this.CustomPayloads.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketEnchantItem && this.EnchantItem.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketEntityAction && this.EntityAction.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketHeldItemChange && this.HeldItemChanges.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketInput && this.Input.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketKeepAlive && this.KeepAlives.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketPlaceRecipe && this.PlaceRecipe.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketPlayer && this.Player.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketPlayerAbilities && this.PlayerAbility.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketPlayerDigging && this.PlayerDigging.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketPlayerTryUseItem && this.PlayerTryUseItem.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock && this.PlayerTryUseItemOnBlock.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketRecipeInfo && this.RecipeInfo.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketResourcePackStatus && this.ResourcePackStatus.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketSeenAdvancements && this.SeenAdvancements.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketSpectate && this.Spectate.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketSteerBoat && this.SteerBoat.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketTabComplete && this.TabCompletion.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketUpdateSign && this.UpdateSign.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketUseEntity && this.UseEntity.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketVehicleMove && this.VehicleMove.getValue().booleanValue()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (!this.isEnabled()) {
            return;
        }
        if (event.getPacket() instanceof SPacketAdvancementInfo && this.AdvancementInfo.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketAnimation && this.Animation.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketBlockAction && this.BlockAction.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketBlockBreakAnim && this.BlockBreakAnim.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketBlockChange && this.BlockChange.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketCamera && this.Camera.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketChangeGameState && this.ChangeGameState.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketChat && this.Chat.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketChunkData && this.ChunkData.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketCloseWindow && this.CloseWindow.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketCollectItem && this.CollectItem.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketCombatEvent && this.CombatEvent.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketConfirmTransaction && this.ConfirmTransaction.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketCooldown && this.Cooldown.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketCustomPayload && this.CustomPayload.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketCustomSound && this.CustomSound.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketDestroyEntities && this.DestroyEntities.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketDisconnect && this.Disconnect.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketChunkData && this.ChunkData.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketCloseWindow && this.CloseWindow.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketCollectItem && this.CollectItem.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketDisplayObjective && this.DisplayObjective.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketEffect && this.Effect.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketEntity && this.Entity.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketEntityAttach && this.EntityAttach.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketEntityEffect && this.EntityEffect.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketEntityEquipment && this.EntityEquipment.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketEntityHeadLook && this.EntityHeadLook.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketEntityMetadata && this.EntityMetadata.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketEntityProperties && this.EntityProperties.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketEntityStatus && this.EntityStatus.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketEntityTeleport && this.EntityTeleport.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketEntityVelocity && this.EntityVelocity.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketExplosion && this.Explosion.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketHeldItemChange && this.HeldItemChange.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketJoinGame && this.JoinGame.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketKeepAlive && this.KeepAlive.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketMaps && this.Maps.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketMoveVehicle && this.MoveVehicle.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketMultiBlockChange && this.MultiBlockChange.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketOpenWindow && this.OpenWindow.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketParticles && this.Particles.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketPlaceGhostRecipe && this.PlaceGhostRecipe.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketPlayerAbilities && this.PlayerAbilities.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketPlayerListHeaderFooter && this.PlayerListHeaderFooter.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketPlayerListItem && this.PlayerListItem.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketPlayerPosLook && this.PlayerPosLook.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketRecipeBook && this.RecipeBook.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketRemoveEntityEffect && this.RemoveEntityEffect.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketResourcePackSend && this.ResourcePackSend.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketRespawn && this.Respawn.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketScoreboardObjective && this.ScoreboardObjective.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSelectAdvancementsTab && this.SelectAdvancementsTab.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketServerDifficulty && this.ServerDifficulty.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSetExperience && this.SetExperience.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSetPassengers && this.SetPassengers.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSetSlot && this.SetSlot.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSignEditorOpen && this.SignEditorOpen.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSoundEffect && this.SoundEffect.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSpawnExperienceOrb && this.SpawnExperienceOrb.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSpawnGlobalEntity && this.SpawnGlobalEntity.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSpawnMob && this.SpawnMob.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSpawnObject && this.SpawnObject.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSpawnPainting && this.SpawnPainting.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSpawnPlayer && this.SpawnPlayer.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSpawnPosition && this.SpawnPosition.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketStatistics && this.Statistics.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketTabComplete && this.TabComplete.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketTeams && this.Teams.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketTimeUpdate && this.TimeUpdate.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketTitle && this.Title.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketUnloadChunk && this.UnloadChunk.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketUpdateBossInfo && this.UpdateBossInfo.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketUpdateHealth && this.UpdateHealth.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketUpdateScore && this.UpdateScore.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketUpdateTileEntity && this.UpdateTileEntity.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketUseBed && this.UseBed.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketWindowItems && this.WindowItems.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketWindowProperty && this.WindowProperty.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketWorldBorder && this.WorldBorder.getValue().booleanValue()) {
            event.setCanceled(true);
        }
    }

    @Override
    public void onEnable() {
        String standart = "\u00a7aAntiPackets On!\u00a7f Cancelled Packets: ";
        StringBuilder text = new StringBuilder(standart);
        if (!this.settings.isEmpty()) {
            for (Setting setting : this.settings) {
                if (!(setting.getValue() instanceof Boolean) || !((Boolean) setting.getValue()).booleanValue() || setting.getName().equalsIgnoreCase("Enabled") || setting.getName().equalsIgnoreCase("drawn"))
                    continue;
                String name = setting.getName();
                text.append(name).append(", ");
            }
        }
        if (text.toString().equals(standart)) {
            Command.sendMessage("\u00a7aAntiPackets On!\u00a7f Currently not cancelling any Packets.");
        } else {
            String output = this.removeLastChar(this.removeLastChar(text.toString()));
            Command.sendMessage(output);
        }
    }

    @Override
    public void onUpdate() {
        int amount = 0;
        if (!this.settings.isEmpty()) {
            for (Setting setting : this.settings) {
                if (!(setting.getValue() instanceof Boolean) || !((Boolean) setting.getValue()).booleanValue() || setting.getName().equalsIgnoreCase("Enabled") || setting.getName().equalsIgnoreCase("drawn"))
                    continue;
                ++amount;
            }
        }
        this.hudAmount = amount;
    }

    @Override
    public String getDisplayInfo() {
        if (this.hudAmount == 0) {
            return "";
        }
        return this.hudAmount + "";
    }

    public String removeLastChar(String str) {
        if (str != null && str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public enum Mode {
        CLIENT,
        SERVER

    }
}

