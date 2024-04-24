package Method.Client.utils.Screens.Custom;

import Method.Client.managers.FileManager;
import Method.Client.utils.Screens.SubGui;
import Method.Client.utils.visual.RenderUtils;
import Method.Client.utils.visual.SimpleButton;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.network.Packet;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.CPacketEncryptionResponse;
import net.minecraft.network.login.client.CPacketLoginStart;
import net.minecraft.network.login.server.SPacketDisconnect;
import net.minecraft.network.login.server.SPacketEnableCompression;
import net.minecraft.network.login.server.SPacketEncryptionRequest;
import net.minecraft.network.login.server.SPacketLoginSuccess;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.*;
import net.minecraft.network.status.client.CPacketPing;
import net.minecraft.network.status.client.CPacketServerQuery;
import net.minecraft.network.status.server.SPacketPong;
import net.minecraft.network.status.server.SPacketServerInfo;

import java.awt.*;
import java.util.ArrayList;

public class AntiPacketGui extends SubGui {

    public AntiPacketGui() {
        super("AntiPacket");
        ListSetup();
    }

    public static ArrayList<Packet> ListOfPackets = new ArrayList<>();

    @Override
    public ArrayList<SelectedThing> getItems() {
        ArrayList<SelectedThing> items = new ArrayList<>();
        for (Packet listOfPacket : ListOfPackets) {
            SelectedThing packet = new SelectedThing(listOfPacket.toString(), false);
            packet.packet = listOfPacket;
            items.add(packet);
        }
        return items;
    }

    @Override
    public void close() {
        FileManager.saveData(FileManager.files[7]);
    }

    @Override
    public ArrayList<SimpleButton> getButtons() {
        ArrayList<SimpleButton> buttons = new ArrayList<>();
        buttons.add(new SimpleButton(0, 10, 10, 100, 20, "Server", "Server"));
        buttons.add(new SimpleButton(1, 10, 10, 100, 20, "Client", "Client"));
        buttons.add(new SimpleButton(2, 10, 10, 100, 20, "All", "All"));
        buttons.add(new SimpleButton(3, 10, 10, 100, 20, "Clear", "Clear"));
        return buttons;
    }

    @Override
    public void buttonPressed(SimpleButton button) {
        if (button.id == 0) {
            for (SelectedThing selectedThing : listGui.list) {
                if (selectedThing.name.toLowerCase().startsWith("s"))
                    selectedThing.isSelected = !selectedThing.isSelected;
            }
        } else if (button.id == 1) {
            for (SelectedThing selectedThing : listGui.list) {
                if (selectedThing.name.toLowerCase().startsWith("c"))
                    selectedThing.isSelected = !selectedThing.isSelected;
            }
        } else if (button.id == 2) {
            for (SelectedThing selectedThing : listGui.list) {
                selectedThing.isSelected = true;
            }
        } else if (button.id == 3) {
            for (SelectedThing item : listGui.list) {
                item.isSelected = false;
            }
        }
    }

    @Override
    public void drawSlot(String name, boolean isSelected, int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
        if (isSelected)
            RenderUtils.scaledText(name, x + 5, slotTop, new Color(25, 211, 25, 1).getRGB(), true);
        else
            RenderUtils.scaledText(name, x + 5, slotTop, new Color(211, 25, 25, 1).getRGB(), true);
    }

    private void ListSetup() {
        ListOfPackets.clear();
        ListOfPackets.add(new CPacketEncryptionResponse());
        ListOfPackets.add(new C00Handshake());
        ListOfPackets.add(new CPacketLoginStart());
        ListOfPackets.add(new SPacketDisconnect());
        ListOfPackets.add(new SPacketEnableCompression());
        ListOfPackets.add(new SPacketEncryptionRequest());
        ListOfPackets.add(new SPacketLoginSuccess());
        ListOfPackets.add(new CPacketAnimation());
        ListOfPackets.add(new CPacketChatMessage());
        ListOfPackets.add(new CPacketClickWindow());
        ListOfPackets.add(new CPacketClientSettings());
        ListOfPackets.add(new CPacketClientStatus());
        ListOfPackets.add(new CPacketCloseWindow());
        ListOfPackets.add(new CPacketConfirmTeleport());
        ListOfPackets.add(new CPacketConfirmTransaction());
        ListOfPackets.add(new CPacketCreativeInventoryAction());
        ListOfPackets.add(new CPacketCustomPayload());
        ListOfPackets.add(new CPacketEnchantItem());
        ListOfPackets.add(new CPacketEntityAction());
        ListOfPackets.add(new CPacketHeldItemChange());
        ListOfPackets.add(new CPacketInput());
        ListOfPackets.add(new CPacketKeepAlive());
        ListOfPackets.add(new CPacketPlaceRecipe());
        ListOfPackets.add(new CPacketPlayer());
        ListOfPackets.add(new CPacketPlayerAbilities());
        ListOfPackets.add(new CPacketPlayerDigging());
        ListOfPackets.add(new CPacketPlayerTryUseItem());
        ListOfPackets.add(new CPacketPing());
        ListOfPackets.add(new CPacketPlayerTryUseItemOnBlock());
        ListOfPackets.add(new CPacketRecipeInfo());
        ListOfPackets.add(new CPacketResourcePackStatus());
        ListOfPackets.add(new CPacketServerQuery());
        ListOfPackets.add(new CPacketSeenAdvancements());
        ListOfPackets.add(new CPacketSpectate());
        ListOfPackets.add(new CPacketSteerBoat());
        ListOfPackets.add(new CPacketTabComplete());
        ListOfPackets.add(new CPacketUpdateSign());
        ListOfPackets.add(new CPacketUseEntity());
        ListOfPackets.add(new CPacketVehicleMove());
        ListOfPackets.add(new SPacketAdvancementInfo());
        ListOfPackets.add(new SPacketAnimation());
        ListOfPackets.add(new SPacketBlockAction());
        ListOfPackets.add(new SPacketBlockBreakAnim());
        ListOfPackets.add(new SPacketBlockChange());
        ListOfPackets.add(new SPacketCamera());
        ListOfPackets.add(new SPacketChangeGameState());
        ListOfPackets.add(new SPacketChat());
        ListOfPackets.add(new SPacketChunkData());
        ListOfPackets.add(new SPacketCloseWindow());
        ListOfPackets.add(new SPacketCollectItem());
        ListOfPackets.add(new SPacketCombatEvent());
        ListOfPackets.add(new SPacketConfirmTransaction());
        ListOfPackets.add(new SPacketCooldown());
        ListOfPackets.add(new SPacketCustomPayload());
        ListOfPackets.add(new SPacketDestroyEntities());
        ListOfPackets.add(new SPacketDisplayObjective());
        ListOfPackets.add(new SPacketEffect());
        ListOfPackets.add(new SPacketEntity());
        ListOfPackets.add(new SPacketEntityAttach());
        ListOfPackets.add(new SPacketEntityEffect());
        ListOfPackets.add(new SPacketEntityEquipment());
        ListOfPackets.add(new SPacketEntityHeadLook());
        ListOfPackets.add(new SPacketEntityMetadata());
        ListOfPackets.add(new SPacketEntityProperties());
        ListOfPackets.add(new SPacketEntityStatus());
        ListOfPackets.add(new SPacketEntityTeleport());
        ListOfPackets.add(new SPacketEntityVelocity());
        ListOfPackets.add(new SPacketExplosion());
        ListOfPackets.add(new SPacketHeldItemChange());
        ListOfPackets.add(new SPacketJoinGame());
        ListOfPackets.add(new SPacketKeepAlive());
        ListOfPackets.add(new SPacketMaps());
        ListOfPackets.add(new SPacketMoveVehicle());
        ListOfPackets.add(new SPacketMultiBlockChange());
        ListOfPackets.add(new SPacketOpenWindow());
        ListOfPackets.add(new SPacketParticles());
        ListOfPackets.add(new SPacketPlaceGhostRecipe());
        ListOfPackets.add(new SPacketPlayerAbilities());
        ListOfPackets.add(new SPacketPlayerListHeaderFooter());
        ListOfPackets.add(new SPacketPlayerListItem());
        ListOfPackets.add(new SPacketPlayerPosLook());
        ListOfPackets.add(new SPacketRecipeBook());
        ListOfPackets.add(new SPacketRemoveEntityEffect());
        ListOfPackets.add(new SPacketResourcePackSend());
        ListOfPackets.add(new SPacketRespawn());
        ListOfPackets.add(new SPacketScoreboardObjective());
        ListOfPackets.add(new SPacketSelectAdvancementsTab());
        ListOfPackets.add(new SPacketServerDifficulty());
        ListOfPackets.add(new SPacketSetExperience());
        ListOfPackets.add(new SPacketSetPassengers());
        ListOfPackets.add(new SPacketSetSlot());
        ListOfPackets.add(new SPacketSignEditorOpen());
        ListOfPackets.add(new SPacketSoundEffect());
        ListOfPackets.add(new SPacketSpawnExperienceOrb());
        ListOfPackets.add(new SPacketSpawnGlobalEntity());
        ListOfPackets.add(new SPacketSpawnMob());
        ListOfPackets.add(new SPacketSpawnObject());
        ListOfPackets.add(new SPacketSpawnPainting());
        ListOfPackets.add(new SPacketSpawnPlayer());
        ListOfPackets.add(new SPacketSpawnPosition());
        ListOfPackets.add(new SPacketStatistics());
        ListOfPackets.add(new SPacketTabComplete());
        ListOfPackets.add(new SPacketTeams());
        ListOfPackets.add(new SPacketTimeUpdate());
        ListOfPackets.add(new SPacketTitle());
        ListOfPackets.add(new SPacketUnloadChunk());
        ListOfPackets.add(new SPacketUpdateBossInfo());
        ListOfPackets.add(new SPacketUpdateHealth());
        ListOfPackets.add(new SPacketUpdateScore());
        ListOfPackets.add(new SPacketUpdateTileEntity());
        ListOfPackets.add(new SPacketUseBed());
        ListOfPackets.add(new SPacketWindowItems());
        ListOfPackets.add(new SPacketWindowProperty());
        ListOfPackets.add(new SPacketWorldBorder());
        ListOfPackets.add(new SPacketCustomSound());
        ListOfPackets.add(new SPacketPong());
        ListOfPackets.add(new SPacketServerInfo());
    }
}
