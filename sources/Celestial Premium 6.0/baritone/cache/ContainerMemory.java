/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 *  io.netty.buffer.Unpooled
 */
package baritone.cache;

import baritone.Baritone;
import baritone.api.cache.IContainerMemory;
import baritone.api.cache.IRememberedInventory;
import baritone.api.utils.IPlayerContext;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

public class ContainerMemory
implements IContainerMemory {
    private final Path saveTo;
    private final Map<BlockPos, RememberedInventory> inventories = new HashMap<BlockPos, RememberedInventory>();

    public ContainerMemory(Path saveTo) {
        this.saveTo = saveTo;
        try {
            this.read(Files.readAllBytes(saveTo));
        }
        catch (NoSuchFileException ignored) {
            this.inventories.clear();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            this.inventories.clear();
        }
    }

    private void read(byte[] bytes) throws IOException {
        PacketBuffer in = new PacketBuffer(Unpooled.wrappedBuffer((byte[])bytes));
        int chests = in.readInt();
        for (int i = 0; i < chests; ++i) {
            int x = in.readInt();
            int y = in.readInt();
            int z = in.readInt();
            RememberedInventory rem = new RememberedInventory();
            rem.items.addAll(ContainerMemory.readItemStacks(in));
            rem.size = rem.items.size();
            rem.windowId = -1;
            if (rem.items.isEmpty()) continue;
            this.inventories.put(new BlockPos(x, y, z), rem);
        }
    }

    public synchronized void save() throws IOException {
        if (!((Boolean)Baritone.settings().containerMemory.value).booleanValue()) {
            return;
        }
        ByteBuf buf = Unpooled.buffer((int)0, (int)Integer.MAX_VALUE);
        PacketBuffer out = new PacketBuffer(buf);
        out.writeInt(this.inventories.size());
        for (Map.Entry<BlockPos, RememberedInventory> entry : this.inventories.entrySet()) {
            out = new PacketBuffer(out.writeInt(entry.getKey().getX()));
            out = new PacketBuffer(out.writeInt(entry.getKey().getY()));
            out = new PacketBuffer(out.writeInt(entry.getKey().getZ()));
            out = ContainerMemory.writeItemStacks(entry.getValue().getContents(), out);
        }
        Files.write(this.saveTo, out.array(), new OpenOption[0]);
    }

    public synchronized void setup(BlockPos pos, int windowId, int slotCount) {
        RememberedInventory inventory = this.inventories.computeIfAbsent(pos, x -> new RememberedInventory());
        inventory.windowId = windowId;
        inventory.size = slotCount;
    }

    public synchronized Optional<RememberedInventory> getInventoryFromWindow(int windowId) {
        return this.inventories.values().stream().filter(i -> ((RememberedInventory)i).windowId == windowId).findFirst();
    }

    @Override
    public final synchronized RememberedInventory getInventoryByPos(BlockPos pos) {
        return this.inventories.get(pos);
    }

    @Override
    public final synchronized Map<BlockPos, IRememberedInventory> getRememberedInventories() {
        return new HashMap<BlockPos, IRememberedInventory>(this.inventories);
    }

    public static List<ItemStack> readItemStacks(byte[] bytes) throws IOException {
        PacketBuffer in = new PacketBuffer(Unpooled.wrappedBuffer((byte[])bytes));
        return ContainerMemory.readItemStacks(in);
    }

    public static List<ItemStack> readItemStacks(PacketBuffer in) throws IOException {
        int count = in.readInt();
        ArrayList<ItemStack> result = new ArrayList<ItemStack>();
        for (int i = 0; i < count; ++i) {
            result.add(in.readItemStack());
        }
        return result;
    }

    public static byte[] writeItemStacks(List<ItemStack> write) {
        ByteBuf buf = Unpooled.buffer((int)0, (int)Integer.MAX_VALUE);
        PacketBuffer out = new PacketBuffer(buf);
        out = ContainerMemory.writeItemStacks(write, out);
        return out.array();
    }

    public static PacketBuffer writeItemStacks(List<ItemStack> write, PacketBuffer out2) {
        PacketBuffer out = out2;
        out = new PacketBuffer(out.writeInt(write.size()));
        for (ItemStack stack : write) {
            out = out.writeItemStack(stack);
        }
        return out;
    }

    public static class RememberedInventory
    implements IRememberedInventory {
        private final List<ItemStack> items = new ArrayList<ItemStack>();
        private int windowId;
        private int size;

        private RememberedInventory() {
        }

        @Override
        public final List<ItemStack> getContents() {
            return Collections.unmodifiableList(this.items);
        }

        @Override
        public final int getSize() {
            return this.size;
        }

        public void updateFromOpenWindow(IPlayerContext ctx) {
            this.items.clear();
            this.items.addAll(ctx.player().openContainer.getInventory().subList(0, this.size));
        }
    }
}

