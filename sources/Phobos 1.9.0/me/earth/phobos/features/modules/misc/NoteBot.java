package me.earth.phobos.features.modules.misc;

import me.earth.phobos.Phobos;
import me.earth.phobos.event.events.ClientEvent;
import me.earth.phobos.event.events.PacketEvent;
import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
import me.earth.phobos.features.command.Command;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.BlockUtil;
import me.earth.phobos.util.MathUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.server.SPacketBlockAction;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class NoteBot
        extends Module {
    private final Setting<Boolean> tune = this.register(new Setting<Boolean>("Tune", false));
    private final Setting<Boolean> active = this.register(new Setting<Boolean>("Active", false));
    private final Setting<Boolean> downloadSongs = this.register(new Setting<Boolean>("DownloadSongs", false));
    private final Setting<String> loadFileSet = this.register(new Setting<String>("Load", "Load File..."));
    private final Map<Sound, Byte> soundBytes = new HashMap<Sound, Byte>();
    private final List<SoundEntry> soundEntries = new ArrayList<SoundEntry>();
    private final List<BlockPos> posList = new ArrayList<BlockPos>();
    private final File file = new File(Phobos.fileManager.getNotebot().toString());
    private IRegister[] registers;
    private int soundIndex;
    private int index;
    private Map<BlockPos, AtomicInteger> posPitch;
    private Map<Sound, BlockPos[]> soundPositions;
    private BlockPos currentPos;
    private BlockPos nextPos;
    private BlockPos endPos;
    private int tuneStage;
    private int tuneIndex;
    private boolean tuned;

    public NoteBot() {
        super("NoteBot", "Plays songs.", Module.Category.MISC, true, false, false);
    }

    public static Map<Sound, BlockPos[]> setUpSoundMap() {
        BlockPos var0 = NoteBot.mc.player.getPosition();
        LinkedHashMap<Sound, BlockPos[]> result = new LinkedHashMap<Sound, BlockPos[]>();
        HashMap atomicSounds = new HashMap();
        Arrays.asList(Sound.values()).forEach(sound -> {
            BlockPos[] var10002 = new BlockPos[25];
            result.put((Sound) sound, var10002);
            atomicSounds.put(sound, new AtomicInteger());
        });
        for (int x = -6; x < 6; ++x) {
            for (int y = -1; y < 5; ++y) {
                for (int z = -6; z < 6; ++z) {
                    Sound sound2;
                    int soundByte;
                    BlockPos pos = NoteBot.mc.player.getPosition().add(x, y, z);
                    Block block = NoteBot.mc.world.getBlockState(pos).getBlock();
                    if (!(NoteBot.distanceSqToCenter(pos) < 27.040000000000003) || block != Blocks.NOTEBLOCK || (soundByte = ((AtomicInteger) atomicSounds.get(sound2 = NoteBot.getSoundFromBlockState(NoteBot.mc.world.getBlockState(pos.down())))).getAndIncrement()) >= 25)
                        continue;
                    result.get(sound2)[soundByte] = pos;
                }
            }
        }
        return result;
    }

    private static double distanceSqToCenter(BlockPos pos) {
        double var1 = Math.abs(NoteBot.mc.player.posX - (double) pos.getX() - 0.5);
        double var3 = Math.abs(NoteBot.mc.player.posY + (double) NoteBot.mc.player.getEyeHeight() - (double) pos.getY() - 0.5);
        double var5 = Math.abs(NoteBot.mc.player.posZ - (double) pos.getZ() - 0.5);
        return var1 * var1 + var3 * var3 + var5 * var5;
    }

    private static IRegister[] createRegister(File file) throws IOException {
        int n2;
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] arrby = new byte[fileInputStream.available()];
        fileInputStream.read(arrby);
        ArrayList<IRegister> arrayList = new ArrayList<IRegister>();
        boolean bl = true;
        byte[] arrby2 = arrby;
        int n4 = arrby2.length;
        for (int i = 0; i < arrby2.length; ++i) {
            n2 = arrby2[i];
            if (n2 != 64) continue;
            bl = false;
            break;
        }
        int n = 0;
        int n6 = 0;
        while (n6 < arrby.length) {
            n4 = arrby[n];
            if (n4 == (bl ? 5 : 64)) {
                byte[] arrby3 = new byte[]{arrby[++n], arrby[++n]};
                byte[] arrby4 = arrby3;
                n2 = arrby3[0] & 0xFF | (arrby4[1] & 0xFF) << 8;
                arrayList.add(new SimpleRegister(n2));
            } else {
                arrayList.add(new SoundRegister(Sound.values()[n4], arrby[++n]));
            }
            n6 = ++n;
        }
        ArrayList<IRegister> arrayList2 = arrayList;
        return arrayList2.toArray(new IRegister[arrayList2.size()]);
    }

    public static void unzip(File file1, File fileIn) {
        ZipEntry zipEntry;
        ZipInputStream zipInputStream;
        byte[] var2 = new byte[1024];
        try {
            if (!fileIn.exists()) {
                fileIn.mkdir();
            }
            zipInputStream = new ZipInputStream(new FileInputStream(file1));
            zipEntry = zipInputStream.getNextEntry();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        }
        while (true) {
            FileOutputStream outputStream;
            try {
                int index;
                if (zipEntry == null) break;
                String fileName = zipEntry.getName();
                File newFile = new File(fileIn, fileName);
                new File(newFile.getParent()).mkdirs();
                outputStream = new FileOutputStream(newFile);
                while ((index = zipInputStream.read(var2)) > 0) {
                    outputStream.write(var2, 0, index);
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
                return;
            }
            try {
                outputStream.close();
                zipEntry = zipInputStream.getNextEntry();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                return;
            }
        }
        try {
            zipInputStream.closeEntry();
            zipInputStream.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static Sound getSoundFromBlockState(IBlockState state) {
        if (state.getBlock() == Blocks.CLAY) {
            return Sound.CLAY;
        }
        if (state.getBlock() == Blocks.GOLD_BLOCK) {
            return Sound.GOLD;
        }
        if (state.getBlock() == Blocks.WOOL) {
            return Sound.WOOL;
        }
        if (state.getBlock() == Blocks.PACKED_ICE) {
            return Sound.ICE;
        }
        if (state.getBlock() == Blocks.BONE_BLOCK) {
            return Sound.BONE;
        }
        if (state.getMaterial() == Material.ROCK) {
            return Sound.ROCK;
        }
        if (state.getMaterial() == Material.SAND) {
            return Sound.SAND;
        }
        if (state.getMaterial() == Material.GLASS) {
            return Sound.GLASS;
        }
        return state.getMaterial() == Material.WOOD ? Sound.WOOD : Sound.NONE;
    }

    @Override
    public void onLoad() {
        if (NoteBot.fullNullCheck()) {
            this.disable();
        }
    }

    @Override
    public void onEnable() {
        if (NoteBot.nullCheck()) {
            this.disable();
            return;
        }
        this.soundEntries.clear();
        this.getNoteBlocks();
        this.soundIndex = 0;
        this.index = 0;
        this.resetTuning();
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent event) {
        if (event.getStage() == 2 && event.getSetting() != null && this.equals(event.getSetting().getFeature())) {
            if (event.getSetting().equals(this.loadFileSet)) {
                String file = this.loadFileSet.getPlannedValue();
                try {
                    this.registers = NoteBot.createRegister(new File("phobos/notebot/" + file));
                    Command.sendMessage("Loaded: " + file);
                } catch (Exception e) {
                    Command.sendMessage("An Error occurred with " + file);
                    e.printStackTrace();
                }
                event.setCanceled(true);
            } else if (event.getSetting().equals(this.tune) && this.tune.getPlannedValue().booleanValue()) {
                this.resetTuning();
            }
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (this.tune.getValue().booleanValue() && event.getPacket() instanceof SPacketBlockAction && this.tuneStage == 0 && this.soundPositions != null) {
            SPacketBlockAction packet = event.getPacket();
            Sound sound = Sound.values()[packet.getData1()];
            int pitch = packet.getData2();
            BlockPos[] positions = this.soundPositions.get(sound);
            for (int i = 0; i < 25; ++i) {
                BlockPos position = positions[i];
                if (!packet.getBlockPosition().equals(position)) continue;
                if (this.posPitch.get(position).intValue() != -1) break;
                int pitchDif = i - pitch;
                if (pitchDif < 0) {
                    pitchDif += 25;
                }
                this.posPitch.get(position).set(pitchDif);
                if (pitchDif == 0) break;
                this.tuned = false;
                break;
            }
            if (this.endPos.equals(packet.getBlockPosition()) && this.tuneIndex >= this.posPitch.values().size()) {
                this.tuneStage = 1;
            }
        }
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayerEvent(UpdateWalkingPlayerEvent event) {
        if (this.downloadSongs.getValue().booleanValue()) {
            this.downloadSongs();
            Command.sendMessage("Songs downloaded");
            this.downloadSongs.setValue(false);
        }
        if (event.getStage() == 0) {
            if (this.tune.getValue().booleanValue()) {
                this.tunePre();
            } else if (this.active.getValue().booleanValue()) {
                this.noteBotPre();
            }
        } else if (this.tune.getValue().booleanValue()) {
            this.tunePost();
        } else if (this.active.getValue().booleanValue()) {
            this.noteBotPost();
        }
    }

    private void tunePre() {
        this.currentPos = null;
        if (this.tuneStage == 1 && this.getAtomicBlockPos(null) == null) {
            if (this.tuned) {
                Command.sendMessage("Done tuning.");
                this.tune.setValue(false);
            } else {
                this.tuned = true;
                this.tuneStage = 0;
                this.tuneIndex = 0;
            }
        } else {
            if (this.tuneStage != 0) {
                this.nextPos = this.currentPos = this.getAtomicBlockPos(this.nextPos);
            } else {
                while (this.tuneIndex < 250 && this.currentPos == null) {
                    this.currentPos = this.soundPositions.get(Sound.values()[(int) Math.floor(this.tuneIndex / 25)])[this.tuneIndex % 25];
                    ++this.tuneIndex;
                }
            }
            if (this.currentPos != null) {
                Phobos.rotationManager.lookAtPos(this.currentPos);
            }
        }
    }

    private void tunePost() {
        if (this.tuneStage == 0 && this.currentPos != null) {
            EnumFacing facing = BlockUtil.getFacing(this.currentPos);
            NoteBot.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, this.currentPos, facing));
            NoteBot.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.currentPos, facing));
        } else if (this.currentPos != null) {
            this.posPitch.get(this.currentPos).decrementAndGet();
            NoteBot.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(this.currentPos, BlockUtil.getFacing(this.currentPos), EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
        }
    }

    private void resetTuning() {
        if (NoteBot.mc.world == null || NoteBot.mc.player == null) {
            this.disable();
            return;
        }
        this.tuned = true;
        this.soundPositions = NoteBot.setUpSoundMap();
        this.posPitch = new LinkedHashMap<BlockPos, AtomicInteger>();
        this.soundPositions.values().forEach(array -> Arrays.asList(array).forEach(pos -> {
            if (pos != null) {
                this.endPos = pos;
                this.posPitch.put(pos, new AtomicInteger(-1));
            }
        }));
        this.tuneStage = 0;
        this.tuneIndex = 0;
    }

    private BlockPos getAtomicBlockPos(BlockPos blockPos) {
        AtomicInteger atomicInteger;
        BlockPos blockPos2;
        Iterator<Map.Entry<BlockPos, AtomicInteger>> iterator = this.posPitch.entrySet().iterator();
        do {
            if (!iterator.hasNext()) {
                return null;
            }
            Map.Entry<BlockPos, AtomicInteger> entry = iterator.next();
            blockPos2 = entry.getKey();
            atomicInteger = entry.getValue();
        } while (blockPos2 == null || blockPos2.equals(blockPos) || atomicInteger.intValue() <= 0);
        return blockPos2;
    }

    private void noteBotPre() {
        this.posList.clear();
        if (this.registers == null) {
            return;
        }
        while (this.index < this.registers.length) {
            IRegister register = this.registers[this.index];
            if (register instanceof SimpleRegister) {
                SimpleRegister simpleRegister = (SimpleRegister) register;
                if (++this.soundIndex >= simpleRegister.getSound()) {
                    ++this.index;
                    this.soundIndex = 0;
                }
                if (this.posList.size() > 0) {
                    BlockPos blockPos = this.posList.get(0);
                    Phobos.rotationManager.lookAtPos(blockPos);
                }
                return;
            }
            if (!(register instanceof SoundRegister)) continue;
            SoundRegister soundRegister = (SoundRegister) register;
            BlockPos pos = this.getRegisterPos(soundRegister);
            if (pos != null) {
                this.posList.add(pos);
            }
            ++this.index;
        }
        this.index = 0;
    }

    private void noteBotPost() {
        for (int i = 0; i < this.posList.size(); ++i) {
            BlockPos pos = this.posList.get(i);
            if (pos == null) continue;
            if (i != 0) {
                float[] rotations = MathUtil.calcAngle(NoteBot.mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d((float) pos.getX() + 0.5f, (float) pos.getY() + 0.5f, (float) pos.getZ() + 0.5f));
                NoteBot.mc.player.connection.sendPacket(new CPacketPlayer.Rotation(rotations[0], rotations[1], NoteBot.mc.player.onGround));
            }
            this.clickNoteBlock(pos);
        }
    }

    private void getNoteBlocks() {
        this.fillSoundBytes();
        for (int x = -6; x < 6; ++x) {
            for (int y = -1; y < 5; ++y) {
                for (int z = -6; z < 6; ++z) {
                    Sound sound;
                    byte soundByte;
                    BlockPos pos = NoteBot.mc.player.getPosition().add(x, y, z);
                    Block block = NoteBot.mc.world.getBlockState(pos).getBlock();
                    if (!(pos.distanceSqToCenter(NoteBot.mc.player.posX, NoteBot.mc.player.posY + (double) NoteBot.mc.player.getEyeHeight(), NoteBot.mc.player.posZ) < 27.0) || block != Blocks.NOTEBLOCK || (soundByte = this.soundBytes.get(sound = NoteBot.getSoundFromBlockState(NoteBot.mc.world.getBlockState(pos.down()))).byteValue()) > 25)
                        continue;
                    this.soundEntries.add(new SoundEntry(pos, new SoundRegister(sound, soundByte)));
                    this.soundBytes.replace(sound, (byte) (soundByte + 1));
                }
            }
        }
    }

    private void fillSoundBytes() {
        this.soundBytes.clear();
        for (Sound sound : Sound.values()) {
            this.soundBytes.put(sound, (byte) 0);
        }
    }

    private void clickNoteBlock(BlockPos pos) {
        EnumFacing facing = BlockUtil.getFacing(pos);
        NoteBot.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, facing));
        NoteBot.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, pos, facing));
    }

    private BlockPos getRegisterPos(SoundRegister register) {
        SoundEntry soundEntry = this.soundEntries.stream().filter(entry -> entry.getRegister().equals(register)).findFirst().orElse(null);
        if (soundEntry == null) {
            return null;
        }
        return soundEntry.getPos();
    }

    private void downloadSongs() {
        new Thread(() -> {
            try {
                File songFile = new File(this.file, "songs.zip");
                FileChannel fileChannel = new FileOutputStream(songFile).getChannel();
                ReadableByteChannel readableByteChannel = Channels.newChannel(new URL("https://www.futureclient.net/future/songs.zip").openStream());
                fileChannel.transferFrom(readableByteChannel, 0L, Long.MAX_VALUE);
                NoteBot.unzip(songFile, this.file);
                songFile.deleteOnExit();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }).start();
    }

    public enum Sound {
        NONE,
        GOLD,
        GLASS,
        BONE,
        WOOD,
        CLAY,
        ICE,
        SAND,
        ROCK,
        WOOL

    }

    public interface IRegister {
    }

    public static class SoundRegister
            implements IRegister {
        private final Sound sound;
        private final byte soundByte;

        public SoundRegister(Sound soundIn, byte soundByteIn) {
            this.sound = soundIn;
            this.soundByte = soundByteIn;
        }

        public Sound getSound() {
            return this.sound;
        }

        public byte getSoundByte() {
            return this.soundByte;
        }

        public boolean equals(Object other) {
            if (other instanceof SoundRegister) {
                SoundRegister soundRegister = (SoundRegister) other;
                return soundRegister.getSound() == this.getSound() && soundRegister.getSoundByte() == this.getSoundByte();
            }
            return false;
        }
    }

    public static class SimpleRegister
            implements IRegister {
        private int sound;

        public SimpleRegister(int soundIn) {
            this.sound = soundIn;
        }

        public int getSound() {
            return this.sound;
        }

        public void setSound(int sound) {
            this.sound = sound;
        }
    }

    public static class SoundEntry {
        private final BlockPos pos;
        private final SoundRegister register;

        public SoundEntry(BlockPos posIn, SoundRegister soundRegisterIn) {
            this.pos = posIn;
            this.register = soundRegisterIn;
        }

        public BlockPos getPos() {
            return this.pos;
        }

        public SoundRegister getRegister() {
            return this.register;
        }
    }
}

