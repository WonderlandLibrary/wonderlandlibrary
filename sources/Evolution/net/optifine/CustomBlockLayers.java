package net.optifine;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.src.Config;
import net.minecraft.util.BlockRenderLayer;
import net.optifine.config.ConnectedParser;
import net.optifine.config.MatchBlock;
import net.optifine.shaders.BlockAliases;
import net.optifine.util.PropertiesOrdered;
import net.optifine.util.ResUtils;

public class CustomBlockLayers
{
    private static BlockRenderLayer[] renderLayers = null;
    public static boolean active = false;

    public static BlockRenderLayer getRenderLayer(IBlockState blockState)
    {
        if (renderLayers == null)
        {
            return null;
        }
        else if (blockState.isOpaqueCube())
        {
            return null;
        }
        else if (!(blockState instanceof BlockStateBase))
        {
            return null;
        }
        else
        {
            BlockStateBase blockstatebase = (BlockStateBase)blockState;
            int i = blockstatebase.getBlockId();
            return i > 0 && i < renderLayers.length ? renderLayers[i] : null;
        }
    }

    public static void update()
    {
        renderLayers = null;
        active = false;
        List<BlockRenderLayer> list = new ArrayList<BlockRenderLayer>();
        String s = "optifine/block.properties";
        Properties properties = ResUtils.readProperties(s, "CustomBlockLayers");

        if (properties != null)
        {
            readLayers(s, properties, list);
        }

        if (Config.isShaders())
        {
            PropertiesOrdered propertiesordered = BlockAliases.getBlockLayerPropertes();

            if (propertiesordered != null)
            {
                String s1 = "shaders/block.properties";
                readLayers(s1, propertiesordered, list);
            }
        }

        if (!list.isEmpty())
        {
            renderLayers = (BlockRenderLayer[])list.toArray(new BlockRenderLayer[list.size()]);
            active = true;
        }
    }

    private static void readLayers(String pathProps, Properties props, List<BlockRenderLayer> list)
    {
        Config.dbg("CustomBlockLayers: " + pathProps);
        readLayer("solid", BlockRenderLayer.SOLID, props, list);
        readLayer("cutout", BlockRenderLayer.CUTOUT, props, list);
        readLayer("cutout_mipped", BlockRenderLayer.CUTOUT_MIPPED, props, list);
        readLayer("translucent", BlockRenderLayer.TRANSLUCENT, props, list);
    }

    private static void readLayer(String name, BlockRenderLayer layer, Properties props, List<BlockRenderLayer> listLayers)
    {
        String s = "layer." + name;
        String s1 = props.getProperty(s);

        if (s1 != null)
        {
            ConnectedParser connectedparser = new ConnectedParser("CustomBlockLayers");
            MatchBlock[] amatchblock = connectedparser.parseMatchBlocks(s1);

            if (amatchblock != null)
            {
                for (int i = 0; i < amatchblock.length; ++i)
                {
                    MatchBlock matchblock = amatchblock[i];
                    int j = matchblock.getBlockId();

                    if (j > 0)
                    {
                        while (listLayers.size() < j + 1)
                        {
                            listLayers.add(null);
                        }

                        if (listLayers.get(j) != null)
                        {
                            Config.warn("CustomBlockLayers: Block layer is already set, block: " + j + ", layer: " + name);
                        }

                        listLayers.set(j, layer);
                    }
                }
            }
        }
    }

    public static boolean isActive()
    {
        return active;
    }
}
