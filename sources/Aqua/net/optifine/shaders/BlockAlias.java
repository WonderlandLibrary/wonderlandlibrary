package net.optifine.shaders;

import java.util.ArrayList;
import java.util.HashSet;
import net.minecraft.src.Config;
import net.optifine.config.MatchBlock;

public class BlockAlias {
    private int blockAliasId;
    private MatchBlock[] matchBlocks;

    public BlockAlias(int blockAliasId, MatchBlock[] matchBlocks) {
        this.blockAliasId = blockAliasId;
        this.matchBlocks = matchBlocks;
    }

    public int getBlockAliasId() {
        return this.blockAliasId;
    }

    public boolean matches(int id, int metadata) {
        for (int i = 0; i < this.matchBlocks.length; ++i) {
            MatchBlock matchblock = this.matchBlocks[i];
            if (!matchblock.matches(id, metadata)) continue;
            return true;
        }
        return false;
    }

    public int[] getMatchBlockIds() {
        HashSet set = new HashSet();
        for (int i = 0; i < this.matchBlocks.length; ++i) {
            MatchBlock matchblock = this.matchBlocks[i];
            int j = matchblock.getBlockId();
            set.add((Object)j);
        }
        Integer[] ainteger = (Integer[])set.toArray((Object[])new Integer[set.size()]);
        int[] aint = Config.toPrimitive((Integer[])ainteger);
        return aint;
    }

    public MatchBlock[] getMatchBlocks(int matchBlockId) {
        ArrayList list = new ArrayList();
        for (int i = 0; i < this.matchBlocks.length; ++i) {
            MatchBlock matchblock = this.matchBlocks[i];
            if (matchblock.getBlockId() != matchBlockId) continue;
            list.add((Object)matchblock);
        }
        MatchBlock[] amatchblock = (MatchBlock[])list.toArray((Object[])new MatchBlock[list.size()]);
        return amatchblock;
    }

    public String toString() {
        return "block." + this.blockAliasId + "=" + Config.arrayToString((Object[])this.matchBlocks);
    }
}
