package net.optifine.config;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.src.Config;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.BiomeGenBase;
import net.optifine.ConnectedProperties;
import net.optifine.config.INameGetter;
import net.optifine.config.MatchBlock;
import net.optifine.config.NbtTagValue;
import net.optifine.config.RangeInt;
import net.optifine.config.RangeListInt;
import net.optifine.config.VillagerProfession;
import net.optifine.config.Weather;
import net.optifine.util.EntityUtils;

public class ConnectedParser {
    private String context = null;
    public static final VillagerProfession[] PROFESSIONS_INVALID = new VillagerProfession[0];
    public static final EnumDyeColor[] DYE_COLORS_INVALID = new EnumDyeColor[0];
    private static final INameGetter<Enum> NAME_GETTER_ENUM = new /* Unavailable Anonymous Inner Class!! */;
    private static final INameGetter<EnumDyeColor> NAME_GETTER_DYE_COLOR = new /* Unavailable Anonymous Inner Class!! */;

    public ConnectedParser(String context) {
        this.context = context;
    }

    public String parseName(String path) {
        int j;
        String s = path;
        int i = path.lastIndexOf(47);
        if (i >= 0) {
            s = path.substring(i + 1);
        }
        if ((j = s.lastIndexOf(46)) >= 0) {
            s = s.substring(0, j);
        }
        return s;
    }

    public String parseBasePath(String path) {
        int i = path.lastIndexOf(47);
        return i < 0 ? "" : path.substring(0, i);
    }

    public MatchBlock[] parseMatchBlocks(String propMatchBlocks) {
        if (propMatchBlocks == null) {
            return null;
        }
        ArrayList list = new ArrayList();
        String[] astring = Config.tokenize((String)propMatchBlocks, (String)" ");
        for (int i = 0; i < astring.length; ++i) {
            String s = astring[i];
            Object[] amatchblock = this.parseMatchBlock(s);
            if (amatchblock == null) continue;
            list.addAll((Collection)Arrays.asList((Object[])amatchblock));
        }
        MatchBlock[] amatchblock1 = (MatchBlock[])list.toArray((Object[])new MatchBlock[list.size()]);
        return amatchblock1;
    }

    public IBlockState parseBlockState(String str, IBlockState def) {
        MatchBlock[] amatchblock = this.parseMatchBlock(str);
        if (amatchblock == null) {
            return def;
        }
        if (amatchblock.length != 1) {
            return def;
        }
        MatchBlock matchblock = amatchblock[0];
        int i = matchblock.getBlockId();
        Block block = Block.getBlockById((int)i);
        return block.getDefaultState();
    }

    public MatchBlock[] parseMatchBlock(String blockStr) {
        if (blockStr == null) {
            return null;
        }
        if ((blockStr = blockStr.trim()).length() <= 0) {
            return null;
        }
        Object[] astring = Config.tokenize((String)blockStr, (String)":");
        String s = "minecraft";
        int i = 0;
        if (astring.length > 1 && this.isFullBlockName((String[])astring)) {
            s = astring[0];
            i = 1;
        } else {
            s = "minecraft";
            i = 0;
        }
        String s1 = astring[i];
        String[] astring1 = (String[])Arrays.copyOfRange((Object[])astring, (int)(i + 1), (int)astring.length);
        Block[] ablock = this.parseBlockPart(s, s1);
        if (ablock == null) {
            return null;
        }
        MatchBlock[] amatchblock = new MatchBlock[ablock.length];
        for (int j = 0; j < ablock.length; ++j) {
            MatchBlock matchblock;
            Block block = ablock[j];
            int k = Block.getIdFromBlock((Block)block);
            int[] aint = null;
            if (astring1.length > 0 && (aint = this.parseBlockMetadatas(block, astring1)) == null) {
                return null;
            }
            amatchblock[j] = matchblock = new MatchBlock(k, aint);
        }
        return amatchblock;
    }

    public boolean isFullBlockName(String[] parts) {
        if (parts.length < 2) {
            return false;
        }
        String s = parts[1];
        return s.length() < 1 ? false : (this.startsWithDigit(s) ? false : !s.contains((CharSequence)"="));
    }

    public boolean startsWithDigit(String str) {
        if (str == null) {
            return false;
        }
        if (str.length() < 1) {
            return false;
        }
        char c0 = str.charAt(0);
        return Character.isDigit((char)c0);
    }

    public Block[] parseBlockPart(String domain, String blockPart) {
        if (this.startsWithDigit(blockPart)) {
            int[] aint = this.parseIntList(blockPart);
            if (aint == null) {
                return null;
            }
            Block[] ablock1 = new Block[aint.length];
            for (int j = 0; j < aint.length; ++j) {
                int i = aint[j];
                Block block1 = Block.getBlockById((int)i);
                if (block1 == null) {
                    this.warn("Block not found for id: " + i);
                    return null;
                }
                ablock1[j] = block1;
            }
            return ablock1;
        }
        String s = domain + ":" + blockPart;
        Block block = Block.getBlockFromName((String)s);
        if (block == null) {
            this.warn("Block not found for name: " + s);
            return null;
        }
        Block[] ablock = new Block[]{block};
        return ablock;
    }

    public int[] parseBlockMetadatas(Block block, String[] params) {
        if (params.length <= 0) {
            return null;
        }
        String s = params[0];
        if (this.startsWithDigit(s)) {
            int[] aint = this.parseIntList(s);
            return aint;
        }
        IBlockState iblockstate = block.getDefaultState();
        Collection collection = iblockstate.getPropertyNames();
        HashMap map = new HashMap();
        for (int i = 0; i < params.length; ++i) {
            String s1 = params[i];
            if (s1.length() <= 0) continue;
            String[] astring = Config.tokenize((String)s1, (String)"=");
            if (astring.length != 2) {
                this.warn("Invalid block property: " + s1);
                return null;
            }
            String s2 = astring[0];
            String s3 = astring[1];
            IProperty iproperty = ConnectedProperties.getProperty((String)s2, (Collection)collection);
            if (iproperty == null) {
                this.warn("Property not found: " + s2 + ", block: " + block);
                return null;
            }
            List list = (List)map.get((Object)s2);
            if (list == null) {
                list = new ArrayList();
                map.put((Object)iproperty, (Object)list);
            }
            String[] astring1 = Config.tokenize((String)s3, (String)",");
            for (int j = 0; j < astring1.length; ++j) {
                String s4 = astring1[j];
                Comparable comparable = ConnectedParser.parsePropertyValue(iproperty, s4);
                if (comparable == null) {
                    this.warn("Property value not found: " + s4 + ", property: " + s2 + ", block: " + block);
                    return null;
                }
                list.add((Object)comparable);
            }
        }
        if (map.isEmpty()) {
            return null;
        }
        ArrayList list1 = new ArrayList();
        for (int k = 0; k < 16; ++k) {
            int l = k;
            try {
                IBlockState iblockstate1 = this.getStateFromMeta(block, l);
                if (!this.matchState(iblockstate1, (Map<IProperty, List<Comparable>>)map)) continue;
                list1.add((Object)l);
                continue;
            }
            catch (IllegalArgumentException illegalArgumentException) {
                // empty catch block
            }
        }
        if (list1.size() == 16) {
            return null;
        }
        int[] aint1 = new int[list1.size()];
        for (int i1 = 0; i1 < aint1.length; ++i1) {
            aint1[i1] = (Integer)list1.get(i1);
        }
        return aint1;
    }

    private IBlockState getStateFromMeta(Block block, int md) {
        try {
            IBlockState iblockstate = block.getStateFromMeta(md);
            if (block == Blocks.double_plant && md > 7) {
                IBlockState iblockstate1 = block.getStateFromMeta(md & 7);
                iblockstate = iblockstate.withProperty((IProperty)BlockDoublePlant.VARIANT, iblockstate1.getValue((IProperty)BlockDoublePlant.VARIANT));
            }
            return iblockstate;
        }
        catch (IllegalArgumentException var5) {
            return block.getDefaultState();
        }
    }

    public static Comparable parsePropertyValue(IProperty prop, String valStr) {
        Class oclass = prop.getValueClass();
        Comparable comparable = ConnectedParser.parseValue(valStr, oclass);
        if (comparable == null) {
            Collection collection = prop.getAllowedValues();
            comparable = ConnectedParser.getPropertyValue(valStr, (Collection<Comparable>)collection);
        }
        return comparable;
    }

    public static Comparable getPropertyValue(String value, Collection<Comparable> propertyValues) {
        for (Comparable comparable : propertyValues) {
            if (!ConnectedParser.getValueName(comparable).equals((Object)value)) continue;
            return comparable;
        }
        return null;
    }

    private static Object getValueName(Comparable obj) {
        if (obj instanceof IStringSerializable) {
            IStringSerializable istringserializable = (IStringSerializable)obj;
            return istringserializable.getName();
        }
        return obj.toString();
    }

    public static Comparable parseValue(String str, Class cls) {
        return (Comparable)(cls == String.class ? str : (cls == Boolean.class ? (Object)Boolean.valueOf((String)str) : (Object)(cls == Float.class ? (double)Float.valueOf((String)str).floatValue() : (cls == Double.class ? Double.valueOf((String)str) : (double)(cls == Integer.class ? (long)Integer.valueOf((String)str).intValue() : (cls == Long.class ? Long.valueOf((String)str) : null))))));
    }

    public boolean matchState(IBlockState bs, Map<IProperty, List<Comparable>> mapPropValues) {
        for (IProperty iproperty : mapPropValues.keySet()) {
            List list = (List)mapPropValues.get((Object)iproperty);
            Comparable comparable = bs.getValue(iproperty);
            if (comparable == null) {
                return false;
            }
            if (list.contains((Object)comparable)) continue;
            return false;
        }
        return true;
    }

    public BiomeGenBase[] parseBiomes(String str) {
        if (str == null) {
            return null;
        }
        str = str.trim();
        boolean flag = false;
        if (str.startsWith("!")) {
            flag = true;
            str = str.substring(1);
        }
        String[] astring = Config.tokenize((String)str, (String)" ");
        ArrayList list = new ArrayList();
        for (int i = 0; i < astring.length; ++i) {
            String s = astring[i];
            BiomeGenBase biomegenbase = this.findBiome(s);
            if (biomegenbase == null) {
                this.warn("Biome not found: " + s);
                continue;
            }
            list.add((Object)biomegenbase);
        }
        if (flag) {
            ArrayList list1 = new ArrayList((Collection)Arrays.asList((Object[])BiomeGenBase.getBiomeGenArray()));
            list1.removeAll((Collection)list);
            list = list1;
        }
        BiomeGenBase[] abiomegenbase = (BiomeGenBase[])list.toArray((Object[])new BiomeGenBase[list.size()]);
        return abiomegenbase;
    }

    public BiomeGenBase findBiome(String biomeName) {
        if ((biomeName = biomeName.toLowerCase()).equals((Object)"nether")) {
            return BiomeGenBase.hell;
        }
        BiomeGenBase[] abiomegenbase = BiomeGenBase.getBiomeGenArray();
        for (int i = 0; i < abiomegenbase.length; ++i) {
            String s;
            BiomeGenBase biomegenbase = abiomegenbase[i];
            if (biomegenbase == null || !(s = biomegenbase.biomeName.replace((CharSequence)" ", (CharSequence)"").toLowerCase()).equals((Object)biomeName)) continue;
            return biomegenbase;
        }
        return null;
    }

    public int parseInt(String str, int defVal) {
        if (str == null) {
            return defVal;
        }
        int i = Config.parseInt((String)(str = str.trim()), (int)-1);
        if (i < 0) {
            this.warn("Invalid number: " + str);
            return defVal;
        }
        return i;
    }

    public int[] parseIntList(String str) {
        if (str == null) {
            return null;
        }
        ArrayList list = new ArrayList();
        String[] astring = Config.tokenize((String)str, (String)" ,");
        for (int i = 0; i < astring.length; ++i) {
            String s = astring[i];
            if (s.contains((CharSequence)"-")) {
                String[] astring1 = Config.tokenize((String)s, (String)"-");
                if (astring1.length != 2) {
                    this.warn("Invalid interval: " + s + ", when parsing: " + str);
                    continue;
                }
                int k = Config.parseInt((String)astring1[0], (int)-1);
                int l = Config.parseInt((String)astring1[1], (int)-1);
                if (k >= 0 && l >= 0 && k <= l) {
                    for (int i1 = k; i1 <= l; ++i1) {
                        list.add((Object)i1);
                    }
                    continue;
                }
                this.warn("Invalid interval: " + s + ", when parsing: " + str);
                continue;
            }
            int j = Config.parseInt((String)s, (int)-1);
            if (j < 0) {
                this.warn("Invalid number: " + s + ", when parsing: " + str);
                continue;
            }
            list.add((Object)j);
        }
        int[] aint = new int[list.size()];
        for (int j1 = 0; j1 < aint.length; ++j1) {
            aint[j1] = (Integer)list.get(j1);
        }
        return aint;
    }

    public boolean[] parseFaces(String str, boolean[] defVal) {
        if (str == null) {
            return defVal;
        }
        EnumSet enumset = EnumSet.allOf(EnumFacing.class);
        String[] astring = Config.tokenize((String)str, (String)" ,");
        for (int i = 0; i < astring.length; ++i) {
            String s = astring[i];
            if (s.equals((Object)"sides")) {
                enumset.add((Object)EnumFacing.NORTH);
                enumset.add((Object)EnumFacing.SOUTH);
                enumset.add((Object)EnumFacing.WEST);
                enumset.add((Object)EnumFacing.EAST);
                continue;
            }
            if (s.equals((Object)"all")) {
                enumset.addAll((Collection)Arrays.asList((Object[])EnumFacing.VALUES));
                continue;
            }
            EnumFacing enumfacing = this.parseFace(s);
            if (enumfacing == null) continue;
            enumset.add((Object)enumfacing);
        }
        boolean[] aboolean = new boolean[EnumFacing.VALUES.length];
        for (int j = 0; j < aboolean.length; ++j) {
            aboolean[j] = enumset.contains((Object)EnumFacing.VALUES[j]);
        }
        return aboolean;
    }

    public EnumFacing parseFace(String str) {
        if (!(str = str.toLowerCase()).equals((Object)"bottom") && !str.equals((Object)"down")) {
            if (!str.equals((Object)"top") && !str.equals((Object)"up")) {
                if (str.equals((Object)"north")) {
                    return EnumFacing.NORTH;
                }
                if (str.equals((Object)"south")) {
                    return EnumFacing.SOUTH;
                }
                if (str.equals((Object)"east")) {
                    return EnumFacing.EAST;
                }
                if (str.equals((Object)"west")) {
                    return EnumFacing.WEST;
                }
                Config.warn((String)("Unknown face: " + str));
                return null;
            }
            return EnumFacing.UP;
        }
        return EnumFacing.DOWN;
    }

    public void dbg(String str) {
        Config.dbg((String)("" + this.context + ": " + str));
    }

    public void warn(String str) {
        Config.warn((String)("" + this.context + ": " + str));
    }

    public RangeListInt parseRangeListInt(String str) {
        if (str == null) {
            return null;
        }
        RangeListInt rangelistint = new RangeListInt();
        String[] astring = Config.tokenize((String)str, (String)" ,");
        for (int i = 0; i < astring.length; ++i) {
            String s = astring[i];
            RangeInt rangeint = this.parseRangeInt(s);
            if (rangeint == null) {
                return null;
            }
            rangelistint.addRange(rangeint);
        }
        return rangelistint;
    }

    private RangeInt parseRangeInt(String str) {
        if (str == null) {
            return null;
        }
        if (str.indexOf(45) >= 0) {
            String[] astring = Config.tokenize((String)str, (String)"-");
            if (astring.length != 2) {
                this.warn("Invalid range: " + str);
                return null;
            }
            int j = Config.parseInt((String)astring[0], (int)-1);
            int k = Config.parseInt((String)astring[1], (int)-1);
            if (j >= 0 && k >= 0) {
                return new RangeInt(j, k);
            }
            this.warn("Invalid range: " + str);
            return null;
        }
        int i = Config.parseInt((String)str, (int)-1);
        if (i < 0) {
            this.warn("Invalid integer: " + str);
            return null;
        }
        return new RangeInt(i, i);
    }

    public boolean parseBoolean(String str, boolean defVal) {
        if (str == null) {
            return defVal;
        }
        String s = str.toLowerCase().trim();
        if (s.equals((Object)"true")) {
            return true;
        }
        if (s.equals((Object)"false")) {
            return false;
        }
        this.warn("Invalid boolean: " + str);
        return defVal;
    }

    public Boolean parseBooleanObject(String str) {
        if (str == null) {
            return null;
        }
        String s = str.toLowerCase().trim();
        if (s.equals((Object)"true")) {
            return Boolean.TRUE;
        }
        if (s.equals((Object)"false")) {
            return Boolean.FALSE;
        }
        this.warn("Invalid boolean: " + str);
        return null;
    }

    public static int parseColor(String str, int defVal) {
        if (str == null) {
            return defVal;
        }
        str = str.trim();
        try {
            int i = Integer.parseInt((String)str, (int)16) & 0xFFFFFF;
            return i;
        }
        catch (NumberFormatException var3) {
            return defVal;
        }
    }

    public static int parseColor4(String str, int defVal) {
        if (str == null) {
            return defVal;
        }
        str = str.trim();
        try {
            int i = (int)(Long.parseLong((String)str, (int)16) & 0xFFFFFFFFFFFFFFFFL);
            return i;
        }
        catch (NumberFormatException var3) {
            return defVal;
        }
    }

    public EnumWorldBlockLayer parseBlockRenderLayer(String str, EnumWorldBlockLayer def) {
        if (str == null) {
            return def;
        }
        str = str.toLowerCase().trim();
        EnumWorldBlockLayer[] aenumworldblocklayer = EnumWorldBlockLayer.values();
        for (int i = 0; i < aenumworldblocklayer.length; ++i) {
            EnumWorldBlockLayer enumworldblocklayer = aenumworldblocklayer[i];
            if (!str.equals((Object)enumworldblocklayer.name().toLowerCase())) continue;
            return enumworldblocklayer;
        }
        return def;
    }

    public <T> T parseObject(String str, T[] objs, INameGetter nameGetter, String property) {
        if (str == null) {
            return null;
        }
        String s = str.toLowerCase().trim();
        for (int i = 0; i < objs.length; ++i) {
            T t = objs[i];
            String s1 = nameGetter.getName(t);
            if (s1 == null || !s1.toLowerCase().equals((Object)s)) continue;
            return t;
        }
        this.warn("Invalid " + property + ": " + str);
        return null;
    }

    public <T> T[] parseObjects(String str, T[] objs, INameGetter nameGetter, String property, T[] errValue) {
        if (str == null) {
            return null;
        }
        str = str.toLowerCase().trim();
        String[] astring = Config.tokenize((String)str, (String)" ");
        Object[] at = (Object[])Array.newInstance((Class)objs.getClass().getComponentType(), (int)astring.length);
        for (int i = 0; i < astring.length; ++i) {
            String s = astring[i];
            T t = this.parseObject(s, objs, nameGetter, property);
            if (t == null) {
                return errValue;
            }
            at[i] = t;
        }
        return at;
    }

    public Enum parseEnum(String str, Enum[] enums, String property) {
        return this.parseObject(str, enums, NAME_GETTER_ENUM, property);
    }

    public Enum[] parseEnums(String str, Enum[] enums, String property, Enum[] errValue) {
        return this.parseObjects(str, enums, NAME_GETTER_ENUM, property, errValue);
    }

    public EnumDyeColor[] parseDyeColors(String str, String property, EnumDyeColor[] errValue) {
        return this.parseObjects(str, EnumDyeColor.values(), NAME_GETTER_DYE_COLOR, property, errValue);
    }

    public Weather[] parseWeather(String str, String property, Weather[] errValue) {
        return this.parseObjects(str, Weather.values(), NAME_GETTER_ENUM, property, errValue);
    }

    public NbtTagValue parseNbtTagValue(String path, String value) {
        return path != null && value != null ? new NbtTagValue(path, value) : null;
    }

    public VillagerProfession[] parseProfessions(String profStr) {
        if (profStr == null) {
            return null;
        }
        ArrayList list = new ArrayList();
        String[] astring = Config.tokenize((String)profStr, (String)" ");
        for (int i = 0; i < astring.length; ++i) {
            String s = astring[i];
            VillagerProfession villagerprofession = this.parseProfession(s);
            if (villagerprofession == null) {
                this.warn("Invalid profession: " + s);
                return PROFESSIONS_INVALID;
            }
            list.add((Object)villagerprofession);
        }
        if (list.isEmpty()) {
            return null;
        }
        VillagerProfession[] avillagerprofession = (VillagerProfession[])list.toArray((Object[])new VillagerProfession[list.size()]);
        return avillagerprofession;
    }

    private VillagerProfession parseProfession(String str) {
        int i;
        String[] astring = Config.tokenize((String)(str = str.toLowerCase()), (String)":");
        if (astring.length > 2) {
            return null;
        }
        String s = astring[0];
        String s1 = null;
        if (astring.length > 1) {
            s1 = astring[1];
        }
        if ((i = ConnectedParser.parseProfessionId(s)) < 0) {
            return null;
        }
        int[] aint = null;
        if (s1 != null && (aint = ConnectedParser.parseCareerIds(i, s1)) == null) {
            return null;
        }
        return new VillagerProfession(i, aint);
    }

    private static int parseProfessionId(String str) {
        int i = Config.parseInt((String)str, (int)-1);
        return i >= 0 ? i : (str.equals((Object)"farmer") ? 0 : (str.equals((Object)"librarian") ? 1 : (str.equals((Object)"priest") ? 2 : (str.equals((Object)"blacksmith") ? 3 : (str.equals((Object)"butcher") ? 4 : (str.equals((Object)"nitwit") ? 5 : -1))))));
    }

    private static int[] parseCareerIds(int prof, String str) {
        HashSet set = new HashSet();
        String[] astring = Config.tokenize((String)str, (String)",");
        for (int i = 0; i < astring.length; ++i) {
            String s = astring[i];
            int j = ConnectedParser.parseCareerId(prof, s);
            if (j < 0) {
                return null;
            }
            set.add((Object)j);
        }
        Integer[] ainteger = (Integer[])set.toArray((Object[])new Integer[set.size()]);
        int[] aint = new int[ainteger.length];
        for (int k = 0; k < aint.length; ++k) {
            aint[k] = ainteger[k];
        }
        return aint;
    }

    private static int parseCareerId(int prof, String str) {
        int i = Config.parseInt((String)str, (int)-1);
        if (i >= 0) {
            return i;
        }
        if (prof == 0) {
            if (str.equals((Object)"farmer")) {
                return 1;
            }
            if (str.equals((Object)"fisherman")) {
                return 2;
            }
            if (str.equals((Object)"shepherd")) {
                return 3;
            }
            if (str.equals((Object)"fletcher")) {
                return 4;
            }
        }
        if (prof == 1) {
            if (str.equals((Object)"librarian")) {
                return 1;
            }
            if (str.equals((Object)"cartographer")) {
                return 2;
            }
        }
        if (prof == 2 && str.equals((Object)"cleric")) {
            return 1;
        }
        if (prof == 3) {
            if (str.equals((Object)"armor")) {
                return 1;
            }
            if (str.equals((Object)"weapon")) {
                return 2;
            }
            if (str.equals((Object)"tool")) {
                return 3;
            }
        }
        if (prof == 4) {
            if (str.equals((Object)"butcher")) {
                return 1;
            }
            if (str.equals((Object)"leather")) {
                return 2;
            }
        }
        return prof == 5 && str.equals((Object)"nitwit") ? 1 : -1;
    }

    public int[] parseItems(String str) {
        str = str.trim();
        TreeSet set = new TreeSet();
        String[] astring = Config.tokenize((String)str, (String)" ");
        for (int i = 0; i < astring.length; ++i) {
            String s = astring[i];
            ResourceLocation resourcelocation = new ResourceLocation(s);
            Item item = (Item)Item.itemRegistry.getObject((Object)resourcelocation);
            if (item == null) {
                this.warn("Item not found: " + s);
                continue;
            }
            int j = Item.getIdFromItem((Item)item);
            if (j < 0) {
                this.warn("Item has no ID: " + item + ", name: " + s);
                continue;
            }
            set.add((Object)new Integer(j));
        }
        Integer[] ainteger = (Integer[])set.toArray((Object[])new Integer[set.size()]);
        int[] aint = Config.toPrimitive((Integer[])ainteger);
        return aint;
    }

    public int[] parseEntities(String str) {
        str = str.trim();
        TreeSet set = new TreeSet();
        String[] astring = Config.tokenize((String)str, (String)" ");
        for (int i = 0; i < astring.length; ++i) {
            String s = astring[i];
            int j = EntityUtils.getEntityIdByName((String)s);
            if (j < 0) {
                this.warn("Entity not found: " + s);
                continue;
            }
            set.add((Object)new Integer(j));
        }
        Integer[] ainteger = (Integer[])set.toArray((Object[])new Integer[set.size()]);
        int[] aint = Config.toPrimitive((Integer[])ainteger);
        return aint;
    }
}
