package me.earth.earthhack.impl.core.ducks;

public interface IWorld
{

    boolean isChunkLoaded(int x, int z, boolean allowEmpty);

}
