/*
 * Decompiled with CFR 0.150.
 */
package baritone.behavior;

import baritone.Baritone;
import baritone.api.cache.IWaypoint;
import baritone.api.cache.Waypoint;
import baritone.api.command.IBaritoneChatControl;
import baritone.api.event.events.BlockInteractEvent;
import baritone.api.event.events.PacketEvent;
import baritone.api.event.events.PlayerUpdateEvent;
import baritone.api.event.events.TickEvent;
import baritone.api.event.events.type.EventState;
import baritone.api.utils.BetterBlockPos;
import baritone.api.utils.Helper;
import baritone.behavior.Behavior;
import baritone.cache.ContainerMemory;
import baritone.utils.BlockStateInterface;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.server.SPacketCloseWindow;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public final class MemoryBehavior
extends Behavior {
    private final List<FutureInventory> futureInventories = new ArrayList<FutureInventory>();
    private Integer enderChestWindowId;

    public MemoryBehavior(Baritone baritone) {
        super(baritone);
    }

    @Override
    public synchronized void onTick(TickEvent event) {
        if (!((Boolean)Baritone.settings().containerMemory.value).booleanValue()) {
            return;
        }
        if (event.getType() == TickEvent.Type.OUT) {
            this.enderChestWindowId = null;
            this.futureInventories.clear();
        }
    }

    @Override
    public synchronized void onPlayerUpdate(PlayerUpdateEvent event) {
        if (event.getState() == EventState.PRE) {
            this.updateInventory();
        }
    }

    @Override
    public synchronized void onSendPacket(PacketEvent event) {
        if (!((Boolean)Baritone.settings().containerMemory.value).booleanValue()) {
            return;
        }
        Packet<?> p = event.getPacket();
        if (event.getState() == EventState.PRE) {
            if (p instanceof CPacketPlayerTryUseItemOnBlock) {
                CPacketPlayerTryUseItemOnBlock packet = (CPacketPlayerTryUseItemOnBlock)event.cast();
                TileEntity tileEntity = this.ctx.world().getTileEntity(packet.getPos());
                if (tileEntity instanceof TileEntityLockable) {
                    TileEntityLockable lockable = (TileEntityLockable)tileEntity;
                    int size = lockable.getSizeInventory();
                    BetterBlockPos position = BetterBlockPos.from(tileEntity.getPos());
                    BetterBlockPos adj = BetterBlockPos.from(this.neighboringConnectedBlock(position));
                    System.out.println(position + " " + adj);
                    if (adj != null) {
                        size *= 2;
                        if (adj.getX() < position.getX() || adj.getZ() < position.getZ()) {
                            position = adj;
                        }
                    }
                    this.futureInventories.add(new FutureInventory(System.nanoTime() / 1000000L, size, lockable.getGuiID(), position));
                }
            }
            if (p instanceof CPacketCloseWindow) {
                this.getCurrent().save();
            }
        }
    }

    @Override
    public synchronized void onReceivePacket(PacketEvent event) {
        if (!((Boolean)Baritone.settings().containerMemory.value).booleanValue()) {
            return;
        }
        Packet<?> p = event.getPacket();
        if (event.getState() == EventState.PRE) {
            if (p instanceof SPacketOpenWindow) {
                SPacketOpenWindow packet = (SPacketOpenWindow)event.cast();
                this.futureInventories.removeIf(i -> System.nanoTime() / 1000000L - ((FutureInventory)i).time > 1000L);
                System.out.println("Received packet " + packet.getGuiId() + " " + packet.getEntityId() + " " + packet.getSlotCount() + " " + packet.getWindowId());
                System.out.println(packet.getWindowTitle());
                if (packet.getWindowTitle() instanceof TextComponentTranslation && ((TextComponentTranslation)packet.getWindowTitle()).getKey().equals("container.enderchest")) {
                    this.enderChestWindowId = packet.getWindowId();
                    return;
                }
                this.futureInventories.stream().filter(i -> ((FutureInventory)i).type.equals(packet.getGuiId()) && ((FutureInventory)i).slots == packet.getSlotCount()).findFirst().ifPresent(matched -> {
                    this.futureInventories.remove(matched);
                    this.getCurrentContainer().setup(((FutureInventory)matched).pos, packet.getWindowId(), packet.getSlotCount());
                });
            }
            if (p instanceof SPacketCloseWindow) {
                this.getCurrent().save();
            }
        }
    }

    @Override
    public void onBlockInteract(BlockInteractEvent event) {
        if (event.getType() == BlockInteractEvent.Type.USE && BlockStateInterface.getBlock(this.ctx, event.getPos()) instanceof BlockBed) {
            this.baritone.getWorldProvider().getCurrentWorld().getWaypoints().addWaypoint(new Waypoint("bed", IWaypoint.Tag.BED, BetterBlockPos.from(event.getPos())));
        }
    }

    @Override
    public void onPlayerDeath() {
        Waypoint deathWaypoint = new Waypoint("death", IWaypoint.Tag.DEATH, this.ctx.playerFeet());
        this.baritone.getWorldProvider().getCurrentWorld().getWaypoints().addWaypoint(deathWaypoint);
        TextComponentString component = new TextComponentString("Death position saved.");
        component.getStyle().setColor(TextFormatting.WHITE).setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("Click to goto death"))).setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("%s%s goto %s @ %d", IBaritoneChatControl.FORCE_COMMAND_PREFIX, "wp", deathWaypoint.getTag().getName(), deathWaypoint.getCreationTimestamp())));
        Helper.HELPER.logDirect(component);
    }

    private void updateInventory() {
        if (!((Boolean)Baritone.settings().containerMemory.value).booleanValue()) {
            return;
        }
        int windowId = this.ctx.player().openContainer.windowId;
        if (this.enderChestWindowId != null) {
            if (windowId == this.enderChestWindowId) {
                this.getCurrent().contents = this.ctx.player().openContainer.getInventory().subList(0, 27);
            } else {
                this.getCurrent().save();
                this.enderChestWindowId = null;
            }
        }
        if (this.getCurrentContainer() != null) {
            this.getCurrentContainer().getInventoryFromWindow(windowId).ifPresent(inventory -> inventory.updateFromOpenWindow(this.ctx));
        }
    }

    private ContainerMemory getCurrentContainer() {
        if (this.baritone.getWorldProvider().getCurrentWorld() == null) {
            return null;
        }
        return (ContainerMemory)this.baritone.getWorldProvider().getCurrentWorld().getContainerMemory();
    }

    private BlockPos neighboringConnectedBlock(BlockPos in) {
        BlockStateInterface bsi = this.baritone.bsi;
        Block block = bsi.get0(in).getBlock();
        if (block != Blocks.TRAPPED_CHEST && block != Blocks.CHEST) {
            return null;
        }
        for (int i = 0; i < 4; ++i) {
            BlockPos adj = in.offset(EnumFacing.byHorizontalIndex(i));
            if (bsi.get0(adj).getBlock() != block) continue;
            return adj;
        }
        return null;
    }

    public Optional<List<ItemStack>> echest() {
        return Optional.ofNullable(this.getCurrent().contents).map(Collections::unmodifiableList);
    }

    public EnderChestMemory getCurrent() {
        Path path = this.baritone.getWorldProvider().getCurrentWorld().directory;
        return EnderChestMemory.getByServerAndPlayer(path.getParent(), this.ctx.player().getUniqueID());
    }

    public static class EnderChestMemory {
        private static final Map<Path, EnderChestMemory> memory = new HashMap<Path, EnderChestMemory>();
        private final Path enderChest;
        private List<ItemStack> contents;

        private EnderChestMemory(Path enderChest) {
            this.enderChest = enderChest;
            System.out.println("Echest storing in " + enderChest);
            try {
                this.contents = ContainerMemory.readItemStacks(Files.readAllBytes(enderChest));
            }
            catch (IOException e) {
                e.printStackTrace();
                System.out.println("CANNOT read echest =( =(");
                this.contents = null;
            }
        }

        public synchronized void save() {
            System.out.println("Saving");
            if (this.contents != null) {
                try {
                    this.enderChest.getParent().toFile().mkdir();
                    Files.write(this.enderChest, ContainerMemory.writeItemStacks(this.contents), new OpenOption[0]);
                }
                catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("CANNOT save echest =( =(");
                }
            }
        }

        private static synchronized EnderChestMemory getByServerAndPlayer(Path serverStorage, UUID player) {
            return memory.computeIfAbsent(serverStorage.resolve("echests").resolve(player.toString()), EnderChestMemory::new);
        }
    }

    private static final class FutureInventory {
        private final long time;
        private final int slots;
        private final String type;
        private final BlockPos pos;

        private FutureInventory(long time, int slots, String type, BlockPos pos) {
            this.time = time;
            this.slots = slots;
            this.type = type;
            this.pos = pos;
            System.out.println("Future inventory created " + time + " " + slots + " " + type + " " + BetterBlockPos.from(pos));
        }
    }
}

