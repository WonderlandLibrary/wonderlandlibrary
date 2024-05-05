package slavikcodd3r.rainbow.module.modules.misc;

import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.block.BlockNote;
import net.minecraft.block.state.pattern.BlockHelper;
import net.minecraft.util.BlockPos;
import slavikcodd3r.rainbow.event.Event;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.utils.ClientUtils;

import java.util.ArrayList;
import java.util.List;

@Module.Mod
public class NoteblockPlayer extends Module
{
    boolean repeat;
    boolean extraLook;
    public static List<Integer> notes;
    public static int note;
    
    static {
        NoteblockPlayer.notes = new ArrayList<Integer>();
    }
    
    public NoteblockPlayer() {
        NoteblockPlayer.notes = new ArrayList<Integer>();
        NoteblockPlayer.note = 0;
        this.repeat = true;
        this.extraLook = true;
    }
    
    @Override
    public void enable() {
        NoteblockPlayer.note = 0;
        super.enable();
    }
    
    @EventTarget
    public void onEvent(final UpdateEvent event) {
        final Event.State state = event.getState();
        event.getState();
        if (state == Event.State.PRE) {
            if (NoteblockPlayer.notes.isEmpty()) {
                return;
            }
            try {
                final int noteToPlay = NoteblockPlayer.notes.get(NoteblockPlayer.note);
                if (noteToPlay != -1) {
                    final int startX = (int)Math.floor(ClientUtils.mc().thePlayer.posX) - 2;
                    final int startY = (int)Math.floor(ClientUtils.mc().thePlayer.posY) - 1;
                    final int startZ = (int)Math.floor(ClientUtils.mc().thePlayer.posZ) - 2;
                    final int x = startX + noteToPlay % 5;
                    final int z = startZ + noteToPlay / 5;
                    final float[] values = BlockHelper.getFacingRotations(x, startY, z, BlockHelper.getFacing(new BlockPos(x, startY, z)));
                    event.setYaw(values[0]);
                    event.setPitch(values[1]);
                }
            }
            catch (IndexOutOfBoundsException ex) {}
        }
        final Event.State state2 = event.getState();
        event.getState();
        if (state2 != Event.State.POST) {
            return;
        }
        int loops = 0;
        if (NoteblockPlayer.notes.isEmpty()) {
            return;
        }
        while (true) {
            if (NoteblockPlayer.note >= NoteblockPlayer.notes.size()) {
                NoteblockPlayer.note = 0;
                if (!this.repeat) {
                    super.disable();
                }
            }
            try {
                final int noteToPlay2 = NoteblockPlayer.notes.get(NoteblockPlayer.note);
                ++NoteblockPlayer.note;
                if (noteToPlay2 == -1) {
                    return;
                }
                final int startX2 = (int)Math.floor(ClientUtils.mc().thePlayer.posX) - 2;
                final int startY2 = (int)Math.floor(ClientUtils.mc().thePlayer.posY) - 1;
                final int startZ2 = (int)Math.floor(ClientUtils.mc().thePlayer.posZ) - 2;
                final int x2 = startX2 + noteToPlay2 % 5;
                final int z2 = startZ2 + noteToPlay2 / 5;
                final BlockPos pos = new BlockPos(x2, startY2, z2);
                if (ClientUtils.mc().theWorld.getBlockState(pos).getBlock() instanceof BlockNote) {
                    if (this.repeat && loops != 0) {
                        final float[] values2 = BlockHelper.getFacingRotations(pos.getX(), pos.getY(), pos.getZ(), BlockHelper.getFacing(pos));
                        ClientUtils.mc().getNetHandler().addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(values2[0], values2[1], ClientUtils.mc().thePlayer.onGround));
                    }
                    ClientUtils.mc().getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, BlockHelper.getFacing(pos)));
                    ClientUtils.mc().getNetHandler().addToSendQueue(new C0APacketAnimation());
                    ClientUtils.mc().getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, pos, BlockHelper.getFacing(pos)));
                }
                ++loops;
            }
            catch (IndexOutOfBoundsException ex2) {}
        }
    }
    
    private class ChordFunction
    {
        private void play(final int i) {
            NoteblockPlayer.notes.add(i);
        }
        
        public void call(final int i) {
            switch (i) {
                case 1: {
                    this.play(1);
                    this.play(5);
                    this.play(8);
                    break;
                }
                case 2: {
                    this.play(2);
                    this.play(6);
                    this.play(9);
                    break;
                }
                case 3: {
                    this.play(3);
                    this.play(6);
                    this.play(10);
                    break;
                }
                case 4: {
                    this.play(4);
                    this.play(6);
                    this.play(10);
                    break;
                }
                case 5: {
                    this.play(5);
                    this.play(7);
                    this.play(12);
                    break;
                }
                case 6: {
                    this.play(6);
                    this.play(10);
                    this.play(13);
                    break;
                }
                case 7: {
                    this.play(7);
                    this.play(10);
                    this.play(14);
                    break;
                }
                case 8: {
                    this.play(8);
                    this.play(12);
                    this.play(15);
                    break;
                }
                case 9: {
                    this.play(9);
                    this.play(13);
                    this.play(16);
                    break;
                }
                case 10: {
                    this.play(10);
                    this.play(13);
                    this.play(17);
                    break;
                }
                case 11: {
                    this.play(11);
                    this.play(15);
                    this.play(18);
                    break;
                }
                case 12: {
                    this.play(12);
                    this.play(16);
                    this.play(19);
                    break;
                }
                case 13: {
                    this.play(13);
                    this.play(17);
                    this.play(20);
                    break;
                }
            }
        }
    }
    
    private class PlayNoteFunction
    {
        public void call(final int i) {
            NoteblockPlayer.notes.add(i);
        }
    }
    
    private class RestFunction
    {
        public void call(final int integer) {
            for (int i = 0; i < integer; ++i) {
                NoteblockPlayer.notes.add(-1);
            }
        }
    }
}
