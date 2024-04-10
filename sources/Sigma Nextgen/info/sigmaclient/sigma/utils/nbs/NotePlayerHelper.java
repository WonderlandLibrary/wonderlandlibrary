package info.sigmaclient.sigma.utils.nbs;

import net.minecraft.block.NoteBlock;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

import static info.sigmaclient.sigma.minimap.minimap.Minimap.mc;

public class NotePlayerHelper
{
    public ArrayList<ANoteBlock> zBJd;
    public boolean SBcf;
    
    public NotePlayerHelper() {
        this.zBJd = new ArrayList<>();
        this.SBcf = false;
    }
    
    public void initFile() {
            this.WLVz(new ANoteBlock(this.ZrME(-2, -1, -2), 0));
            this.WLVz(new ANoteBlock(this.ZrME(-1, -1, -2), 1));
            this.WLVz(new ANoteBlock(this.ZrME(0, -1, -2), 2));
            this.WLVz(new ANoteBlock(this.ZrME(1, -1, -2), 3));
            this.WLVz(new ANoteBlock(this.ZrME(2, -1, -2), 4));
            this.WLVz(new ANoteBlock(this.ZrME(-2, -1, -1), 5));
            this.WLVz(new ANoteBlock(this.ZrME(-1, -1, -1), 6));
            this.WLVz(new ANoteBlock(this.ZrME(0, -1, -1), 7));
            this.WLVz(new ANoteBlock(this.ZrME(1, -1, -1), 8));
            this.WLVz(new ANoteBlock(this.ZrME(2, -1, -1), 9));
            this.WLVz(new ANoteBlock(this.ZrME(-2, -1, 0), 10));
            this.WLVz(new ANoteBlock(this.ZrME(-1, -1, 0), 11));
            this.WLVz(new ANoteBlock(this.ZrME(0, -1, 0), 12));
            this.WLVz(new ANoteBlock(this.ZrME(1, -1, 0), 13));
            this.WLVz(new ANoteBlock(this.ZrME(2, -1, 0), 14));
            this.WLVz(new ANoteBlock(this.ZrME(-2, -1, 1), 15));
            this.WLVz(new ANoteBlock(this.ZrME(-1, -1, 1), 16));
            this.WLVz(new ANoteBlock(this.ZrME(0, -1, 1), 17));
            this.WLVz(new ANoteBlock(this.ZrME(1, -1, 1), 18));
            this.WLVz(new ANoteBlock(this.ZrME(2, -1, 1), 19));
            this.WLVz(new ANoteBlock(this.ZrME(-2, -1, 2), 20));
            this.WLVz(new ANoteBlock(this.ZrME(-1, -1, 2), 21));
            this.WLVz(new ANoteBlock(this.ZrME(0, -1, 2), 22));
            this.WLVz(new ANoteBlock(this.ZrME(1, -1, 2), 23));
            this.WLVz(new ANoteBlock(this.ZrME(2, -1, 2), 24));
    }
    
    public void WLVz(final ANoteBlock psqP) {
        if (mc.world.getBlockState(psqP.blockPos).getBlock() instanceof NoteBlock) {
            this.zBJd.add(psqP);
        }
    }
    
    public BlockPos ZrME(final int n, final int n2, final int n3) {
        return new BlockPos(mc.player.getPosX() + n, mc.player.getPosY() + n2, mc.player.getPosZ() + n3);
    }
}
