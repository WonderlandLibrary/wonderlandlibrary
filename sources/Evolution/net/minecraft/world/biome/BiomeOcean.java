package net.minecraft.world.biome;

public class BiomeOcean extends Biome
{
    public BiomeOcean(BiomeProperties properties)
    {
        super(properties);
        this.spawnableCreatureList.clear();
    }

    public TempCategory getTempCategory()
    {
        return TempCategory.OCEAN;
    }
}
