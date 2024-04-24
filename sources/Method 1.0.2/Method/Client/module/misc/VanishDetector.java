
package Method.Client.module.misc;

import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.system.Connection;
import Method.Client.utils.visual.ChatUtils;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import static Method.Client.Main.setmgr;

public class VanishDetector extends Module {

    /////////////////////
    private final HashMap<UUID, String> Hashmap;

    public VanishDetector() {
        super("VanishDetector", Keyboard.KEY_NONE, Category.MISC, "Staff Vanish Detector");
        this.Hashmap = new HashMap<>();
    }

    DecimalFormat decimalFormat = new DecimalFormat("0.00");
    Setting EntityMove = setmgr.add(new Setting("EntityMove", this, false));
    Setting EntityBedloc = setmgr.add(new Setting("Entity Bed ", this, true, EntityMove));
    Setting StopRemove = setmgr.add(new Setting("Stop Entity Remove", this, true));
    Setting Soundpos = setmgr.add(new Setting("Sound pos", this, false));
    Setting BlockChanges = setmgr.add(new Setting("BlockChanges", this, false));

    @Override
    public boolean onPacket(Object packet, Connection.Side side) {

        if (side == Connection.Side.IN) {
            if (EntityMove.getValBoolean())
                if (packet instanceof SPacketEntity.S17PacketEntityLookMove) {
                    SPacketEntity.S17PacketEntityLookMove packet1 = (SPacketEntity.S17PacketEntityLookMove) packet;
                    if ((packet1.getEntity(mc.world) instanceof EntityOtherPlayerMP)) {
                        ChatUtils.message("Player: " + packet1.getEntity(mc.world).getName() + " pos X:" + decimalFormat.format(packet1.getEntity(mc.world).posX) + " Y: " + decimalFormat.format(packet1.getEntity(mc.world).posY) + " Z: " + decimalFormat.format(packet1.getEntity(mc.world).posZ));
                        if (EntityBedloc.getValBoolean())
                            ChatUtils.message("Player: " + packet1.getEntity(mc.world).getName() + " Bed " + ((EntityPlayer) packet1.getEntity(mc.world)).bedLocation);
                    }
                }
            if (Soundpos.getValBoolean())
                if (packet instanceof SPacketSoundEffect) {
                    SPacketSoundEffect packet1 = (SPacketSoundEffect) packet;
                    if (packet1.getCategory() == SoundCategory.PLAYERS) {
                        if (((SPacketSoundEffect) packet).posY < 250) {
                            boolean found = false;
                            for (Entity entity : mc.world.loadedEntityList) {
                                if (Math.sqrt(Math.pow((entity.posX - packet1.posX), 2) + Math.pow((entity.posY - packet1.posY), 2) + Math.pow((entity.posZ - packet1.posZ), 2)) < 8) {
                                    found = true;
                                    break;
                                }
                            }
                            for (TileEntity entity : mc.world.loadedTileEntityList) {
                                if (Math.sqrt(Math.pow((entity.getPos().x - packet1.posX), 2) + Math.pow((entity.getPos().y - packet1.posY), 2) + Math.pow((entity.getPos().z - packet1.posZ), 2)) < 8) {
                                    found = true;
                                    break;
                                }
                            }
                            if (!found) {
                                ChatUtils.message("Sound near: X: " + packet1.posX + " Y: " + packet1.posY + " Z: " + packet1.posZ);
                            }
                        }
                    }
                }
            if (packet instanceof SPacketDestroyEntities && StopRemove.getValBoolean()) {
                return false;
            }
            if (BlockChanges.getValBoolean()) {
                if (packet instanceof SPacketBlockChange) {
                    SPacketBlockChange packet1 = (SPacketBlockChange) packet;
                    boolean found = false;
                    for (Entity entity : mc.world.loadedEntityList) {
                        if (Math.sqrt(Math.pow((entity.posX - packet1.getBlockPosition().x), 2) + Math.pow((entity.posY - packet1.getBlockPosition().y), 2) + Math.pow((entity.posZ - packet1.getBlockPosition().z), 2)) < 8) {
                            found = true;
                            break;
                        }
                    }
                    for (TileEntity entity : mc.world.loadedTileEntityList) {
                        if (Math.sqrt(Math.pow((entity.getPos().x - packet1.getBlockPosition().x), 2) + Math.pow((entity.getPos().y - packet1.getBlockPosition().y), 2) + Math.pow((entity.getPos().z - packet1.getBlockPosition().z), 2)) < 8) {
                            found = true;
                            break;
                        }
                    }
                    if (found) {
                        ChatUtils.message("BlockChange: X: " + packet1.getBlockPosition().x + " Y: " + packet1.getBlockPosition().y + " Z: " + packet1.getBlockPosition().z);
                    }
                }
            }
        }
        if (packet instanceof SPacketPlayerListItem) {
            final SPacketPlayerListItem sPacketPlayerListItem = (SPacketPlayerListItem) packet;
            if (sPacketPlayerListItem.getAction() == SPacketPlayerListItem.Action.UPDATE_LATENCY) {
                final HashSet<UUID> set = new HashSet<>();
                for (final SPacketPlayerListItem.AddPlayerData addPlayerData : sPacketPlayerListItem.getEntries()) {
                    set.add(addPlayerData.getProfile().getId());
                }
                new Thread(() -> {
                    for (UUID uuid : set) {
                        this.Hashmap.put(uuid, uuid.toString());
                    }

                }).start();
            }
        }
        return true;
    }

    @Subscribe
    public void onClientTick(TickEvent.ClientTickEvent event) {
        synchronized (this.Hashmap) {
            for (final Map.Entry<UUID, String> entry : this.Hashmap.entrySet()) {
                ChatUtils.message("PlayerPreviewElement " + entry.getValue() + " has gone into vanish (" + entry.getKey() + ")");
            }
            this.Hashmap.clear();
        }
    }

}
