package net.optifine.config;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockObserver;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.src.Config;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.optifine.ConnectedProperties;
import net.optifine.reflect.Reflector;

public class ConnectedParser
{
    private String context = null;
    public static final VillagerProfession[] PROFESSIONS_INVALID = new VillagerProfession[0];
    public static final EnumDyeColor[] DYE_COLORS_INVALID = new EnumDyeColor[0];
    private static final INameGetter<Enum> NAME_GETTER_ENUM = new INameGetter<Enum>()
    {
        public String getName(Enum en)
        {
            return en.name();
        }
    };
    private static final INameGetter<EnumDyeColor> NAME_GETTER_DYE_COLOR = new INameGetter<EnumDyeColor>()
    {
        public String getName(EnumDyeColor col)
        {
            return col.getName();
        }
    };

    public ConnectedParser(String context)
    {
        this.context = context;
    }

    public String parseName(String path)
    {
        String s = path;
        int i = path.lastIndexOf(47);

        if (i >= 0)
        {
            s = path.substring(i + 1);
        }

        int j = s.lastIndexOf(46);

        if (j >= 0)
        {
            s = s.substring(0, j);
        }

        return s;
    }

    public String parseBasePath(String path)
    {
        int i = path.lastIndexOf(47);
        return i < 0 ? "" : path.substring(0, i);
    }

    public MatchBlock[] parseMatchBlocks(String propMatchBlocks)
    {
        if (propMatchBlocks == null)
        {
            return null;
        }
        else
        {
            List list = new ArrayList();
            String[] astring = Config.tokenize(propMatchBlocks, " ");

            for (int i = 0; i < astring.length; ++i)
            {
                String s = astring[i];
                MatchBlock[] amatchblock = this.parseMatchBlock(s);

                if (amatchblock != null)
                {
                    list.addAll(Arrays.asList(amatchblock));
                }
            }

            MatchBlock[] amatchblock1 = (MatchBlock[])list.toArray(new MatchBlock[list.size()]);
            return amatchblock1;
        }
    }

    public IBlockState parseBlockState(String str, IBlockState def)
    {
        MatchBlock[] amatchblock = this.parseMatchBlock(str);

        if (amatchblock == null)
        {
            return def;
        }
        else if (amatchblock.length != 1)
        {
            return def;
        }
        else
        {
            MatchBlock matchblock = amatchblock[0];
            int i = matchblock.getBlockId();
            Block block = Block.getBlockById(i);
            return block.getDefaultState();
        }
    }

    public MatchBlock[] parseMatchBlock(String blockStr)
    {
        if (blockStr == null)
        {
            return null;
        }
        else
        {
            blockStr = blockStr.trim();

            if (blockStr.length() <= 0)
            {
                return null;
            }
            else
            {
                String[] astring = Config.tokenize(blockStr, ":");
                String s = "minecraft";
                int i = 0;

                if (astring.length > 1 && this.isFullBlockName(astring))
                {
                    s = astring[0];
                    i = 1;
                }
                else
                {
                    s = "minecraft";
                    i = 0;
                }

                String s1 = astring[i];
                String[] astring1 = (String[])Arrays.copyOfRange(astring, i + 1, astring.length);
                Block[] ablock = this.parseBlockPart(s, s1);

                if (ablock == null)
                {
                    return null;
                }
                else
                {
                    MatchBlock[] amatchblock = new MatchBlock[ablock.length];

                    for (int j = 0; j < ablock.length; ++j)
                    {
                        Block block = ablock[j];
                        int k = Block.getIdFromBlock(block);
                        int[] aint = null;

                        if (astring1.length > 0)
                        {
                            aint = this.parseBlockMetadatas(block, astring1);

                            if (aint == null)
                            {
                                return null;
                            }
                        }

                        MatchBlock matchblock = new MatchBlock(k, aint);
                        amatchblock[j] = matchblock;
                    }

                    return amatchblock;
                }
            }
        }
    }

    public boolean isFullBlockName(String[] parts)
    {
        if (parts.length < 2)
        {
            return false;
        }
        else
        {
            String s = parts[1];

            if (s.length() < 1)
            {
                return false;
            }
            else if (this.startsWithDigit(s))
            {
                return false;
            }
            else
            {
                return !s.contains("=");
            }
        }
    }

    public boolean startsWithDigit(String str)
    {
        if (str == null)
        {
            return false;
        }
        else if (str.length() < 1)
        {
            return false;
        }
        else
        {
            char c0 = str.charAt(0);
            return Character.isDigit(c0);
        }
    }

    public Block[] parseBlockPart(String domain, String blockPart)
    {
        if (this.startsWithDigit(blockPart))
        {
            int[] aint = this.parseIntList(blockPart);

            if (aint == null)
            {
                return null;
            }
            else
            {
                Block[] ablock1 = new Block[aint.length];

                for (int j = 0; j < aint.length; ++j)
                {
                    int i = aint[j];
                    Block block1 = Block.getBlockById(i);

                    if (block1 == null)
                    {
                        this.warn("Block not found for id: " + i);
                        return null;
                    }

                    ablock1[j] = block1;
                }

                return ablock1;
            }
        }
        else
        {
            String s = domain + ":" + blockPart;
            Block block = Block.getBlockFromName(s);

            if (block == null)
            {
                this.warn("Block not found for name: " + s);
                return null;
            }
            else
            {
                Block[] ablock = new Block[] {block};
                return ablock;
            }
        }
    }

    public int[] parseBlockMetadatas(Block block, String[] params)
    {
        if (params.length <= 0)
        {
            return null;
        }
        else
        {
            String s = params[0];

            if (this.startsWithDigit(s))
            {
                int[] aint = this.parseIntList(s);
                return aint;
            }
            else
            {
                IBlockState iblockstate = block.getDefaultState();
                Collection collection = iblockstate.getPropertyKeys();
                Map<IProperty, List<Comparable>> map = new HashMap<IProperty, List<Comparable>>();

                for (int i = 0; i < params.length; ++i)
                {
                    String s1 = params[i];

                    if (s1.length() > 0)
                    {
                        String[] astring = Config.tokenize(s1, "=");

                        if (astring.length != 2)
                        {
                            this.warn("Invalid block property: " + s1);
                            return null;
                        }

                        String s2 = astring[0];
                        String s3 = astring[1];
                        IProperty iproperty = ConnectedProperties.getProperty(s2, collection);

                        if (iproperty == null)
                        {
                            this.warn("Property not found: " + s2 + ", block: " + block);
                            return null;
                        }

                        List<Comparable> list = (List)map.get(s2);

                        if (list == null)
                        {
                            list = new ArrayList<Comparable>();
                            map.put(iproperty, list);
                        }

                        String[] astring1 = Config.tokenize(s3, ",");

                        for (int j = 0; j < astring1.length; ++j)
                        {
                            String s4 = astring1[j];
                            Comparable comparable = parsePropertyValue(iproperty, s4);

                            if (comparable == null)
                            {
                                this.warn("Property value not found: " + s4 + ", property: " + s2 + ", block: " + block);
                                return null;
                            }

                            list.add(comparable);
                        }
                    }
                }

                if (map.isEmpty())
                {
                    return null;
                }
                else
                {
                    List<Integer> list1 = new ArrayList<Integer>();

                    for (int k = 0; k < 16; ++k)
                    {
                        int l = k;

                        try
                        {
                            IBlockState iblockstate1 = this.getStateFromMeta(block, l);

                            if (this.matchState(iblockstate1, map))
                            {
                                list1.add(Integer.valueOf(l));
                            }
                        }
                        catch (IllegalArgumentException var18)
                        {
                            ;
                        }
                    }

                    if (list1.size() == 16)
                    {
                        return null;
                    }
                    else
                    {
                        int[] aint1 = new int[list1.size()];

                        for (int i1 = 0; i1 < aint1.length; ++i1)
                        {
                            aint1[i1] = ((Integer)list1.get(i1)).intValue();
                        }

                        return aint1;
                    }
                }
            }
        }
    }

    private IBlockState getStateFromMeta(Block block, int md)
    {
        try
        {
            IBlockState iblockstate = block.getStateFromMeta(md);

            if (block == Blocks.DOUBLE_PLANT && md > 7)
            {
                IBlockState iblockstate1 = block.getStateFromMeta(md & 7);
                iblockstate = iblockstate.withProperty(BlockDoublePlant.VARIANT, iblockstate1.getValue(BlockDoublePlant.VARIANT));
            }

            if (block == Blocks.OBSERVER && (md & 8) != 0)
            {
                iblockstate = iblockstate.withProperty(BlockObserver.POWERED, Boolean.valueOf(true));
            }

            return iblockstate;
        }
        catch (IllegalArgumentException var5)
        {
            return block.getDefaultState();
        }
    }

    public static Comparable parsePropertyValue(IProperty prop, String valStr)
    {
        Class oclass = prop.getValueClass();
        Comparable comparable = parseValue(valStr, oclass);

        if (comparable == null)
        {
            Collection collection = prop.getAllowedValues();
            comparable = getPropertyValue(valStr, collection);
        }

        return comparable;
    }

    public static Comparable getPropertyValue(String value, Collection propertyValues)
    {
        for (Object comparable1 : propertyValues)
        {
            Comparable comparable = (Comparable) comparable1;
            if (getValueName(comparable).equals(value))
            {
                return comparable;
            }
        }

        return null;
    }

    private static Object getValueName(Comparable obj)
    {
        if (obj instanceof IStringSerializable)
        {
            IStringSerializable istringserializable = (IStringSerializable)obj;
            return istringserializable.getName();
        }
        else
        {
            return obj.toString();
        }
    }

    public static Comparable parseValue(String str, Class cls)
    {
        if (cls == String.class)
        {
            return str;
        }
        else if (cls == Boolean.class)
        {
            return Boolean.valueOf(str);
        }
        else if (cls == Float.class)
        {
            return Float.valueOf(str);
        }
        else if (cls == Double.class)
        {
            return Double.valueOf(str);
        }
        else if (cls == Integer.class)
        {
            return Integer.valueOf(str);
        }
        else
        {
            return cls == Long.class ? Long.valueOf(str) : null;
        }
    }

    public boolean matchState(IBlockState bs, Map<IProperty, List<Comparable>> mapPropValues)
    {
        for (IProperty iproperty : mapPropValues.keySet())
        {
            List<Comparable> list = (List)mapPropValues.get(iproperty);
            Comparable comparable = bs.getValue(iproperty);

            if (comparable == null)
            {
                return false;
            }

            if (!list.contains(comparable))
            {
                return false;
            }
        }

        return true;
    }

    public Biome[] parseBiomes(String str)
    {
        if (str == null)
        {
            return null;
        }
        else
        {
            str = str.trim();
            boolean flag = false;

            if (str.startsWith("!"))
            {
                flag = true;
                str = str.substring(1);
            }

            String[] astring = Config.tokenize(str, " ");
            List list = new ArrayList();

            for (int i = 0; i < astring.length; ++i)
            {
                String s = astring[i];
                Biome biome = this.findBiome(s);

                if (biome == null)
                {
                    this.warn("Biome not found: " + s);
                }
                else
                {
                    list.add(biome);
                }
            }

            if (flag)
            {
                List<Biome> list1 = Lists.newArrayList(Biome.REGISTRY.iterator());
                list1.removeAll(list);
                list = list1;
            }

            Biome[] abiome = (Biome[])list.toArray(new Biome[list.size()]);
            return abiome;
        }
    }

    public Biome findBiome(String biomeName)
    {
        biomeName = biomeName.toLowerCase();

        if (biomeName.equals("nether"))
        {
            return Biomes.HELL;
        }
        else
        {
            for (ResourceLocation resourcelocation : Biome.REGISTRY.getKeys())
            {
                Biome biome = Biome.REGISTRY.getObject(resourcelocation);

                if (biome != null)
                {
                    String s = biome.getBiomeName().replace(" ", "").toLowerCase();

                    if (s.equals(biomeName))
                    {
                        return biome;
                    }
                }
            }

            return null;
        }
    }

    public int parseInt(String str, int defVal)
    {
        if (str == null)
        {
            return defVal;
        }
        else
        {
            str = str.trim();
            int i = Config.parseInt(str, -1);

            if (i < 0)
            {
                this.warn("Invalid number: " + str);
                return defVal;
            }
            else
            {
                return i;
            }
        }
    }

    public int[] parseIntList(String str)
    {
        if (str == null)
        {
            return null;
        }
        else
        {
            List<Integer> list = new ArrayList<Integer>();
            String[] astring = Config.tokenize(str, " ,");

            for (int i = 0; i < astring.length; ++i)
            {
                String s = astring[i];

                if (s.contains("-"))
                {
                    String[] astring1 = Config.tokenize(s, "-");

                    if (astring1.length != 2)
                    {
                        this.warn("Invalid interval: " + s + ", when parsing: " + str);
                    }
                    else
                    {
                        int k = Config.parseInt(astring1[0], -1);
                        int l = Config.parseInt(astring1[1], -1);

                        if (k >= 0 && l >= 0 && k <= l)
                        {
                            for (int i1 = k; i1 <= l; ++i1)
                            {
                                list.add(Integer.valueOf(i1));
                            }
                        }
                        else
                        {
                            this.warn("Invalid interval: " + s + ", when parsing: " + str);
                        }
                    }
                }
                else
                {
                    int j = Config.parseInt(s, -1);

                    if (j < 0)
                    {
                        this.warn("Invalid number: " + s + ", when parsing: " + str);
                    }
                    else
                    {
                        list.add(Integer.valueOf(j));
                    }
                }
            }

            int[] aint = new int[list.size()];

            for (int j1 = 0; j1 < aint.length; ++j1)
            {
                aint[j1] = ((Integer)list.get(j1)).intValue();
            }

            return aint;
        }
    }

    public boolean[] parseFaces(String str, boolean[] defVal)
    {
        if (str == null)
        {
            return defVal;
        }
        else
        {
            EnumSet enumset = EnumSet.allOf(EnumFacing.class);
            String[] astring = Config.tokenize(str, " ,");

            for (int i = 0; i < astring.length; ++i)
            {
                String s = astring[i];

                if (s.equals("sides"))
                {
                    enumset.add(EnumFacing.NORTH);
                    enumset.add(EnumFacing.SOUTH);
                    enumset.add(EnumFacing.WEST);
                    enumset.add(EnumFacing.EAST);
                }
                else if (s.equals("all"))
                {
                    enumset.addAll(Arrays.asList(EnumFacing.VALUES));
                }
                else
                {
                    EnumFacing enumfacing = this.parseFace(s);

                    if (enumfacing != null)
                    {
                        enumset.add(enumfacing);
                    }
                }
            }

            boolean[] aboolean = new boolean[EnumFacing.VALUES.length];

            for (int j = 0; j < aboolean.length; ++j)
            {
                aboolean[j] = enumset.contains(EnumFacing.VALUES[j]);
            }

            return aboolean;
        }
    }

    public EnumFacing parseFace(String str)
    {
        str = str.toLowerCase();

        if (!str.equals("bottom") && !str.equals("down"))
        {
            if (!str.equals("top") && !str.equals("up"))
            {
                if (str.equals("north"))
                {
                    return EnumFacing.NORTH;
                }
                else if (str.equals("south"))
                {
                    return EnumFacing.SOUTH;
                }
                else if (str.equals("east"))
                {
                    return EnumFacing.EAST;
                }
                else if (str.equals("west"))
                {
                    return EnumFacing.WEST;
                }
                else
                {
                    Config.warn("Unknown face: " + str);
                    return null;
                }
            }
            else
            {
                return EnumFacing.UP;
            }
        }
        else
        {
            return EnumFacing.DOWN;
        }
    }

    public void dbg(String str)
    {
        Config.dbg("" + this.context + ": " + str);
    }

    public void warn(String str)
    {
        Config.warn("" + this.context + ": " + str);
    }

    public RangeListInt parseRangeListInt(String str)
    {
        if (str == null)
        {
            return null;
        }
        else
        {
            RangeListInt rangelistint = new RangeListInt();
            String[] astring = Config.tokenize(str, " ,");

            for (int i = 0; i < astring.length; ++i)
            {
                String s = astring[i];
                RangeInt rangeint = this.parseRangeInt(s);

                if (rangeint == null)
                {
                    return null;
                }

                rangelistint.addRange(rangeint);
            }

            return rangelistint;
        }
    }

    private RangeInt parseRangeInt(String str)
    {
        if (str == null)
        {
            return null;
        }
        else if (str.indexOf(45) >= 0)
        {
            String[] astring = Config.tokenize(str, "-");

            if (astring.length != 2)
            {
                this.warn("Invalid range: " + str);
                return null;
            }
            else
            {
                int j = Config.parseInt(astring[0], -1);
                int k = Config.parseInt(astring[1], -1);

                if (j >= 0 && k >= 0)
                {
                    return new RangeInt(j, k);
                }
                else
                {
                    this.warn("Invalid range: " + str);
                    return null;
                }
            }
        }
        else
        {
            int i = Config.parseInt(str, -1);

            if (i < 0)
            {
                this.warn("Invalid integer: " + str);
                return null;
            }
            else
            {
                return new RangeInt(i, i);
            }
        }
    }

    public boolean parseBoolean(String str, boolean defVal)
    {
        if (str == null)
        {
            return defVal;
        }
        else
        {
            String s = str.toLowerCase().trim();

            if (s.equals("true"))
            {
                return true;
            }
            else if (s.equals("false"))
            {
                return false;
            }
            else
            {
                this.warn("Invalid boolean: " + str);
                return defVal;
            }
        }
    }

    public Boolean parseBooleanObject(String str)
    {
        if (str == null)
        {
            return null;
        }
        else
        {
            String s = str.toLowerCase().trim();

            if (s.equals("true"))
            {
                return Boolean.TRUE;
            }
            else if (s.equals("false"))
            {
                return Boolean.FALSE;
            }
            else
            {
                this.warn("Invalid boolean: " + str);
                return null;
            }
        }
    }

    public static int parseColor(String str, int defVal)
    {
        if (str == null)
        {
            return defVal;
        }
        else
        {
            str = str.trim();

            try
            {
                int i = Integer.parseInt(str, 16) & 16777215;
                return i;
            }
            catch (NumberFormatException var3)
            {
                return defVal;
            }
        }
    }

    public static int parseColor4(String str, int defVal)
    {
        if (str == null)
        {
            return defVal;
        }
        else
        {
            str = str.trim();

            try
            {
                int i = (int)(Long.parseLong(str, 16) & -1L);
                return i;
            }
            catch (NumberFormatException var3)
            {
                return defVal;
            }
        }
    }

    public BlockRenderLayer parseBlockRenderLayer(String str, BlockRenderLayer def)
    {
        if (str == null)
        {
            return def;
        }
        else
        {
            str = str.toLowerCase().trim();
            BlockRenderLayer[] ablockrenderlayer = BlockRenderLayer.values();

            for (int i = 0; i < ablockrenderlayer.length; ++i)
            {
                BlockRenderLayer blockrenderlayer = ablockrenderlayer[i];

                if (str.equals(blockrenderlayer.name().toLowerCase()))
                {
                    return blockrenderlayer;
                }
            }

            return def;
        }
    }

    public <T> T parseObject(String str, T[] objs, INameGetter nameGetter, String property)
    {
        if (str == null)
        {
            return (T)null;
        }
        else
        {
            String s = str.toLowerCase().trim();

            for (int i = 0; i < objs.length; ++i)
            {
                T t = (T)objs[i];
                String s1 = nameGetter.getName(t);

                if (s1 != null && s1.toLowerCase().equals(s))
                {
                    return t;
                }
            }

            this.warn("Invalid " + property + ": " + str);
            return (T)null;
        }
    }

    public <T> T[] parseObjects(String str, T[] objs, INameGetter nameGetter, String property, T[] errValue)
    {
        if (str == null)
        {
            return null;
        }
        else
        {
            str = str.toLowerCase().trim();
            String[] astring = Config.tokenize(str, " ");
            T[] at = (T[])((Object[])Array.newInstance(objs.getClass().getComponentType(), astring.length));

            for (int i = 0; i < astring.length; ++i)
            {
                String s = astring[i];
                T t = (T)this.parseObject(s, objs, nameGetter, property);

                if (t == null)
                {
                    return errValue;
                }

                at[i] = t;
            }

            return at;
        }
    }

    public Enum parseEnum(String str, Enum[] enums, String property)
    {
        return (Enum)this.parseObject(str, enums, NAME_GETTER_ENUM, property);
    }

    public Enum[] parseEnums(String str, Enum[] enums, String property, Enum[] errValue)
    {
        return (Enum[])this.parseObjects(str, enums, NAME_GETTER_ENUM, property, errValue);
    }

    public EnumDyeColor[] parseDyeColors(String str, String property, EnumDyeColor[] errValue)
    {
        return (EnumDyeColor[])this.parseObjects(str, EnumDyeColor.values(), NAME_GETTER_DYE_COLOR, property, errValue);
    }

    public Weather[] parseWeather(String str, String property, Weather[] errValue)
    {
        return (Weather[])this.parseObjects(str, Weather.values(), NAME_GETTER_ENUM, property, errValue);
    }

    public NbtTagValue parseNbtTagValue(String path, String value)
    {
        return path != null && value != null ? new NbtTagValue(path, value) : null;
    }

    public VillagerProfession[] parseProfessions(String profStr)
    {
        if (profStr == null)
        {
            return null;
        }
        else
        {
            List<VillagerProfession> list = new ArrayList<VillagerProfession>();
            String[] astring = Config.tokenize(profStr, " ");

            for (int i = 0; i < astring.length; ++i)
            {
                String s = astring[i];
                VillagerProfession villagerprofession = this.parseProfession(s);

                if (villagerprofession == null)
                {
                    this.warn("Invalid profession: " + s);
                    return PROFESSIONS_INVALID;
                }

                list.add(villagerprofession);
            }

            if (list.isEmpty())
            {
                return null;
            }
            else
            {
                VillagerProfession[] avillagerprofession = (VillagerProfession[])list.toArray(new VillagerProfession[list.size()]);
                return avillagerprofession;
            }
        }
    }

    private VillagerProfession parseProfession(String str)
    {
        str = str.toLowerCase();
        String[] astring = Config.tokenize(str, ":");

        if (astring.length > 2)
        {
            return null;
        }
        else
        {
            String s = astring[0];
            String s1 = null;

            if (astring.length > 1)
            {
                s1 = astring[1];
            }

            int i = parseProfessionId(s);

            if (i < 0)
            {
                return null;
            }
            else
            {
                int[] aint = null;

                if (s1 != null)
                {
                    aint = parseCareerIds(i, s1);

                    if (aint == null)
                    {
                        return null;
                    }
                }

                return new VillagerProfession(i, aint);
            }
        }
    }

    private static int parseProfessionId(String str)
    {
        int i = Config.parseInt(str, -1);

        if (i >= 0)
        {
            return i;
        }
        else if (str.equals("farmer"))
        {
            return 0;
        }
        else if (str.equals("librarian"))
        {
            return 1;
        }
        else if (str.equals("priest"))
        {
            return 2;
        }
        else if (str.equals("blacksmith"))
        {
            return 3;
        }
        else if (str.equals("butcher"))
        {
            return 4;
        }
        else
        {
            return str.equals("nitwit") ? 5 : -1;
        }
    }

    private static int[] parseCareerIds(int prof, String str)
    {
        IntSet intset = new IntArraySet();
        String[] astring = Config.tokenize(str, ",");

        for (int i = 0; i < astring.length; ++i)
        {
            String s = astring[i];
            int j = parseCareerId(prof, s);

            if (j < 0)
            {
                return null;
            }

            intset.add(j);
        }

        int[] aint = intset.toIntArray();
        return aint;
    }

    private static int parseCareerId(int prof, String str)
    {
        int i = Config.parseInt(str, -1);

        if (i >= 0)
        {
            return i;
        }
        else
        {
            if (prof == 0)
            {
                if (str.equals("farmer"))
                {
                    return 1;
                }

                if (str.equals("fisherman"))
                {
                    return 2;
                }

                if (str.equals("shepherd"))
                {
                    return 3;
                }

                if (str.equals("fletcher"))
                {
                    return 4;
                }
            }

            if (prof == 1)
            {
                if (str.equals("librarian"))
                {
                    return 1;
                }

                if (str.equals("cartographer"))
                {
                    return 2;
                }
            }

            if (prof == 2 && str.equals("cleric"))
            {
                return 1;
            }
            else
            {
                if (prof == 3)
                {
                    if (str.equals("armor"))
                    {
                        return 1;
                    }

                    if (str.equals("weapon"))
                    {
                        return 2;
                    }

                    if (str.equals("tool"))
                    {
                        return 3;
                    }
                }

                if (prof == 4)
                {
                    if (str.equals("butcher"))
                    {
                        return 1;
                    }

                    if (str.equals("leather"))
                    {
                        return 2;
                    }
                }

                return prof == 5 && str.equals("nitwit") ? 1 : -1;
            }
        }
    }

    public int[] parseItems(String str)
    {
        str = str.trim();
        Set<Integer> set = new TreeSet<Integer>();
        String[] astring = Config.tokenize(str, " ");

        for (int i = 0; i < astring.length; ++i)
        {
            String s = astring[i];
            ResourceLocation resourcelocation = new ResourceLocation(s);
            Item item = Item.REGISTRY.getObject(resourcelocation);

            if (item == null)
            {
                this.warn("Item not found: " + s);
            }
            else
            {
                int j = Item.getIdFromItem(item);

                if (j < 0)
                {
                    this.warn("Item has no ID: " + item + ", name: " + s);
                }
                else
                {
                    set.add(new Integer(j));
                }
            }
        }

        Integer[] ainteger = (Integer[])set.toArray(new Integer[set.size()]);
        int[] aint = Config.toPrimitive(ainteger);
        return aint;
    }

    public int[] parseEntities(String str)
    {
        str = str.trim();
        Set<Integer> set = new TreeSet<Integer>();
        String[] astring = Config.tokenize(str, " ");

        for (int i = 0; i < astring.length; ++i)
        {
            String s = astring[i];
            ResourceLocation resourcelocation = new ResourceLocation(s);
            Class oclass = this.getEntityClass(resourcelocation);

            if (oclass == null)
            {
                this.warn("Entity not found: " + s);
            }
            else
            {
                int j = this.getEntityTypeId(oclass);

                if (j < 0)
                {
                    this.warn("Entity has no ID: " + oclass + ", name: " + s);
                }
                else
                {
                    set.add(new Integer(j));
                }
            }
        }

        Integer[] ainteger = (Integer[])set.toArray(new Integer[set.size()]);
        int[] aint = Config.toPrimitive(ainteger);
        return aint;
    }

    private Class getEntityClass(ResourceLocation loc)
    {
        return Reflector.ForgeEntityList_getClass.exists() ? (Class)Reflector.call(Reflector.ForgeEntityList_getClass, loc) : (Class)EntityList.REGISTRY.getObject(loc);
    }

    private int getEntityTypeId(Class type)
    {
        return Reflector.ForgeEntityList_getID.exists() ? Reflector.callInt(Reflector.ForgeEntityList_getID, type) : EntityList.REGISTRY.getIDForObject(type);
    }
}
