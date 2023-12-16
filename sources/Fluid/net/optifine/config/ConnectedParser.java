// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.config;

import net.optifine.util.EntityUtils;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import java.util.TreeSet;
import java.util.Set;
import java.util.HashSet;
import java.lang.reflect.Array;
import net.minecraft.util.EnumWorldBlockLayer;
import java.util.EnumSet;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.util.IStringSerializable;
import java.util.Iterator;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.init.Blocks;
import java.util.Map;
import net.optifine.ConnectedProperties;
import net.minecraft.block.properties.IProperty;
import java.util.HashMap;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import java.util.List;
import java.util.Collection;
import java.util.Arrays;
import net.minecraft.src.Config;
import java.util.ArrayList;
import net.minecraft.item.EnumDyeColor;

public class ConnectedParser
{
    private String context;
    public static final VillagerProfession[] PROFESSIONS_INVALID;
    public static final EnumDyeColor[] DYE_COLORS_INVALID;
    private static final INameGetter<Enum> NAME_GETTER_ENUM;
    private static final INameGetter<EnumDyeColor> NAME_GETTER_DYE_COLOR;
    
    static {
        PROFESSIONS_INVALID = new VillagerProfession[0];
        DYE_COLORS_INVALID = new EnumDyeColor[0];
        NAME_GETTER_ENUM = new INameGetter<Enum>() {
            @Override
            public String getName(final Enum en) {
                return en.name();
            }
        };
        NAME_GETTER_DYE_COLOR = new INameGetter<EnumDyeColor>() {
            @Override
            public String getName(final EnumDyeColor col) {
                return col.getName();
            }
        };
    }
    
    public ConnectedParser(final String context) {
        this.context = null;
        this.context = context;
    }
    
    public String parseName(final String path) {
        String s = path;
        final int i = path.lastIndexOf(47);
        if (i >= 0) {
            s = path.substring(i + 1);
        }
        final int j = s.lastIndexOf(46);
        if (j >= 0) {
            s = s.substring(0, j);
        }
        return s;
    }
    
    public String parseBasePath(final String path) {
        final int i = path.lastIndexOf(47);
        return (i < 0) ? "" : path.substring(0, i);
    }
    
    public MatchBlock[] parseMatchBlocks(final String propMatchBlocks) {
        if (propMatchBlocks == null) {
            return null;
        }
        final List list = new ArrayList();
        final String[] astring = Config.tokenize(propMatchBlocks, " ");
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            final MatchBlock[] amatchblock = this.parseMatchBlock(s);
            if (amatchblock != null) {
                list.addAll(Arrays.asList(amatchblock));
            }
        }
        final MatchBlock[] amatchblock2 = list.toArray(new MatchBlock[list.size()]);
        return amatchblock2;
    }
    
    public IBlockState parseBlockState(final String str, final IBlockState def) {
        final MatchBlock[] amatchblock = this.parseMatchBlock(str);
        if (amatchblock == null) {
            return def;
        }
        if (amatchblock.length != 1) {
            return def;
        }
        final MatchBlock matchblock = amatchblock[0];
        final int i = matchblock.getBlockId();
        final Block block = Block.getBlockById(i);
        return block.getDefaultState();
    }
    
    public MatchBlock[] parseMatchBlock(String blockStr) {
        if (blockStr == null) {
            return null;
        }
        blockStr = blockStr.trim();
        if (blockStr.length() <= 0) {
            return null;
        }
        final String[] astring = Config.tokenize(blockStr, ":");
        String s = "minecraft";
        int i = 0;
        if (astring.length > 1 && this.isFullBlockName(astring)) {
            s = astring[0];
            i = 1;
        }
        else {
            s = "minecraft";
            i = 0;
        }
        final String s2 = astring[i];
        final String[] astring2 = Arrays.copyOfRange(astring, i + 1, astring.length);
        final Block[] ablock = this.parseBlockPart(s, s2);
        if (ablock == null) {
            return null;
        }
        final MatchBlock[] amatchblock = new MatchBlock[ablock.length];
        for (int j = 0; j < ablock.length; ++j) {
            final Block block = ablock[j];
            final int k = Block.getIdFromBlock(block);
            int[] aint = null;
            if (astring2.length > 0) {
                aint = this.parseBlockMetadatas(block, astring2);
                if (aint == null) {
                    return null;
                }
            }
            final MatchBlock matchblock = new MatchBlock(k, aint);
            amatchblock[j] = matchblock;
        }
        return amatchblock;
    }
    
    public boolean isFullBlockName(final String[] parts) {
        if (parts.length < 2) {
            return false;
        }
        final String s = parts[1];
        return s.length() >= 1 && !this.startsWithDigit(s) && !s.contains("=");
    }
    
    public boolean startsWithDigit(final String str) {
        if (str == null) {
            return false;
        }
        if (str.length() < 1) {
            return false;
        }
        final char c0 = str.charAt(0);
        return Character.isDigit(c0);
    }
    
    public Block[] parseBlockPart(final String domain, final String blockPart) {
        if (this.startsWithDigit(blockPart)) {
            final int[] aint = this.parseIntList(blockPart);
            if (aint == null) {
                return null;
            }
            final Block[] ablock1 = new Block[aint.length];
            for (int j = 0; j < aint.length; ++j) {
                final int i = aint[j];
                final Block block1 = Block.getBlockById(i);
                if (block1 == null) {
                    this.warn("Block not found for id: " + i);
                    return null;
                }
                ablock1[j] = block1;
            }
            return ablock1;
        }
        else {
            final String s = String.valueOf(domain) + ":" + blockPart;
            final Block block2 = Block.getBlockFromName(s);
            if (block2 == null) {
                this.warn("Block not found for name: " + s);
                return null;
            }
            final Block[] ablock2 = { block2 };
            return ablock2;
        }
    }
    
    public int[] parseBlockMetadatas(final Block block, final String[] params) {
        if (params.length <= 0) {
            return null;
        }
        final String s = params[0];
        if (this.startsWithDigit(s)) {
            final int[] aint = this.parseIntList(s);
            return aint;
        }
        final IBlockState iblockstate = block.getDefaultState();
        final Collection collection = iblockstate.getPropertyNames();
        final Map<IProperty, List<Comparable>> map = new HashMap<IProperty, List<Comparable>>();
        for (int i = 0; i < params.length; ++i) {
            final String s2 = params[i];
            if (s2.length() > 0) {
                final String[] astring = Config.tokenize(s2, "=");
                if (astring.length != 2) {
                    this.warn("Invalid block property: " + s2);
                    return null;
                }
                final String s3 = astring[0];
                final String s4 = astring[1];
                final IProperty iproperty = ConnectedProperties.getProperty(s3, collection);
                if (iproperty == null) {
                    this.warn("Property not found: " + s3 + ", block: " + block);
                    return null;
                }
                List<Comparable> list = map.get(s3);
                if (list == null) {
                    list = new ArrayList<Comparable>();
                    map.put(iproperty, list);
                }
                final String[] astring2 = Config.tokenize(s4, ",");
                for (int j = 0; j < astring2.length; ++j) {
                    final String s5 = astring2[j];
                    final Comparable comparable = parsePropertyValue(iproperty, s5);
                    if (comparable == null) {
                        this.warn("Property value not found: " + s5 + ", property: " + s3 + ", block: " + block);
                        return null;
                    }
                    list.add(comparable);
                }
            }
        }
        if (map.isEmpty()) {
            return null;
        }
        final List<Integer> list2 = new ArrayList<Integer>();
        for (int k = 0; k < 16; ++k) {
            final int l = k;
            try {
                final IBlockState iblockstate2 = this.getStateFromMeta(block, l);
                if (this.matchState(iblockstate2, map)) {
                    list2.add(l);
                }
            }
            catch (final IllegalArgumentException ex) {}
        }
        if (list2.size() == 16) {
            return null;
        }
        final int[] aint2 = new int[list2.size()];
        for (int i2 = 0; i2 < aint2.length; ++i2) {
            aint2[i2] = list2.get(i2);
        }
        return aint2;
    }
    
    private IBlockState getStateFromMeta(final Block block, final int md) {
        try {
            IBlockState iblockstate = block.getStateFromMeta(md);
            if (block == Blocks.double_plant && md > 7) {
                final IBlockState iblockstate2 = block.getStateFromMeta(md & 0x7);
                iblockstate = iblockstate.withProperty(BlockDoublePlant.VARIANT, (BlockDoublePlant.EnumPlantType)iblockstate2.getValue((IProperty<V>)BlockDoublePlant.VARIANT));
            }
            return iblockstate;
        }
        catch (final IllegalArgumentException var5) {
            return block.getDefaultState();
        }
    }
    
    public static Comparable parsePropertyValue(final IProperty prop, final String valStr) {
        final Class oclass = prop.getValueClass();
        Comparable comparable = parseValue(valStr, oclass);
        if (comparable == null) {
            final Collection collection = prop.getAllowedValues();
            comparable = getPropertyValue(valStr, collection);
        }
        return comparable;
    }
    
    public static Comparable getPropertyValue(final String value, final Collection propertyValues) {
        for (final Object o : propertyValues) {
            final Comparable comparable = (Comparable)o;
            if (getValueName(comparable).equals(value)) {
                return comparable;
            }
        }
        return null;
    }
    
    private static Object getValueName(final Comparable obj) {
        if (obj instanceof IStringSerializable) {
            final IStringSerializable istringserializable = (IStringSerializable)obj;
            return istringserializable.getName();
        }
        return obj.toString();
    }
    
    public static Comparable parseValue(final String str, final Class<?> cls) {
        if (cls == String.class) {
            return str;
        }
        if (cls == Boolean.class) {
            return Boolean.valueOf(str);
        }
        if (cls == Float.class) {
            return Float.valueOf(str);
        }
        if (cls == Double.class) {
            return Double.valueOf(str);
        }
        if (cls == Integer.class) {
            return Integer.valueOf(str);
        }
        if (cls == Long.class) {
            return Long.valueOf(str);
        }
        return null;
    }
    
    public boolean matchState(final IBlockState bs, final Map<IProperty, List<Comparable>> mapPropValues) {
        for (final IProperty iproperty : mapPropValues.keySet()) {
            final List<Comparable> list = mapPropValues.get(iproperty);
            final Comparable comparable = bs.getValue((IProperty<Comparable>)iproperty);
            if (comparable == null) {
                return false;
            }
            if (!list.contains(comparable)) {
                return false;
            }
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
        final String[] astring = Config.tokenize(str, " ");
        List list = new ArrayList();
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            final BiomeGenBase biomegenbase = this.findBiome(s);
            if (biomegenbase == null) {
                this.warn("Biome not found: " + s);
            }
            else {
                list.add(biomegenbase);
            }
        }
        if (flag) {
            final List<BiomeGenBase> list2 = new ArrayList<BiomeGenBase>(Arrays.asList(BiomeGenBase.getBiomeGenArray()));
            list2.removeAll(list);
            list = list2;
        }
        final BiomeGenBase[] abiomegenbase = list.toArray(new BiomeGenBase[list.size()]);
        return abiomegenbase;
    }
    
    public BiomeGenBase findBiome(String biomeName) {
        biomeName = biomeName.toLowerCase();
        if (biomeName.equals("nether")) {
            return BiomeGenBase.hell;
        }
        final BiomeGenBase[] abiomegenbase = BiomeGenBase.getBiomeGenArray();
        for (int i = 0; i < abiomegenbase.length; ++i) {
            final BiomeGenBase biomegenbase = abiomegenbase[i];
            if (biomegenbase != null) {
                final String s = biomegenbase.biomeName.replace(" ", "").toLowerCase();
                if (s.equals(biomeName)) {
                    return biomegenbase;
                }
            }
        }
        return null;
    }
    
    public int parseInt(String str, final int defVal) {
        if (str == null) {
            return defVal;
        }
        str = str.trim();
        final int i = Config.parseInt(str, -1);
        if (i < 0) {
            this.warn("Invalid number: " + str);
            return defVal;
        }
        return i;
    }
    
    public int[] parseIntList(final String str) {
        if (str == null) {
            return null;
        }
        final List<Integer> list = new ArrayList<Integer>();
        final String[] astring = Config.tokenize(str, " ,");
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            if (s.contains("-")) {
                final String[] astring2 = Config.tokenize(s, "-");
                if (astring2.length != 2) {
                    this.warn("Invalid interval: " + s + ", when parsing: " + str);
                }
                else {
                    final int k = Config.parseInt(astring2[0], -1);
                    final int l = Config.parseInt(astring2[1], -1);
                    if (k >= 0 && l >= 0 && k <= l) {
                        for (int i2 = k; i2 <= l; ++i2) {
                            list.add(i2);
                        }
                    }
                    else {
                        this.warn("Invalid interval: " + s + ", when parsing: " + str);
                    }
                }
            }
            else {
                final int j = Config.parseInt(s, -1);
                if (j < 0) {
                    this.warn("Invalid number: " + s + ", when parsing: " + str);
                }
                else {
                    list.add(j);
                }
            }
        }
        final int[] aint = new int[list.size()];
        for (int j2 = 0; j2 < aint.length; ++j2) {
            aint[j2] = list.get(j2);
        }
        return aint;
    }
    
    public boolean[] parseFaces(final String str, final boolean[] defVal) {
        if (str == null) {
            return defVal;
        }
        final EnumSet enumset = EnumSet.allOf(EnumFacing.class);
        final String[] astring = Config.tokenize(str, " ,");
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            if (s.equals("sides")) {
                enumset.add(EnumFacing.NORTH);
                enumset.add(EnumFacing.SOUTH);
                enumset.add(EnumFacing.WEST);
                enumset.add(EnumFacing.EAST);
            }
            else if (s.equals("all")) {
                enumset.addAll(Arrays.asList(EnumFacing.VALUES));
            }
            else {
                final EnumFacing enumfacing = this.parseFace(s);
                if (enumfacing != null) {
                    enumset.add(enumfacing);
                }
            }
        }
        final boolean[] aboolean = new boolean[EnumFacing.VALUES.length];
        for (int j = 0; j < aboolean.length; ++j) {
            aboolean[j] = enumset.contains(EnumFacing.VALUES[j]);
        }
        return aboolean;
    }
    
    public EnumFacing parseFace(String str) {
        str = str.toLowerCase();
        if (str.equals("bottom") || str.equals("down")) {
            return EnumFacing.DOWN;
        }
        if (str.equals("top") || str.equals("up")) {
            return EnumFacing.UP;
        }
        if (str.equals("north")) {
            return EnumFacing.NORTH;
        }
        if (str.equals("south")) {
            return EnumFacing.SOUTH;
        }
        if (str.equals("east")) {
            return EnumFacing.EAST;
        }
        if (str.equals("west")) {
            return EnumFacing.WEST;
        }
        Config.warn("Unknown face: " + str);
        return null;
    }
    
    public void dbg(final String str) {
        Config.dbg(this.context + ": " + str);
    }
    
    public void warn(final String str) {
        Config.warn(this.context + ": " + str);
    }
    
    public RangeListInt parseRangeListInt(final String str) {
        if (str == null) {
            return null;
        }
        final RangeListInt rangelistint = new RangeListInt();
        final String[] astring = Config.tokenize(str, " ,");
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            final RangeInt rangeint = this.parseRangeInt(s);
            if (rangeint == null) {
                return null;
            }
            rangelistint.addRange(rangeint);
        }
        return rangelistint;
    }
    
    private RangeInt parseRangeInt(final String str) {
        if (str == null) {
            return null;
        }
        if (str.indexOf(45) >= 0) {
            final String[] astring = Config.tokenize(str, "-");
            if (astring.length != 2) {
                this.warn("Invalid range: " + str);
                return null;
            }
            final int j = Config.parseInt(astring[0], -1);
            final int k = Config.parseInt(astring[1], -1);
            if (j >= 0 && k >= 0) {
                return new RangeInt(j, k);
            }
            this.warn("Invalid range: " + str);
            return null;
        }
        else {
            final int i = Config.parseInt(str, -1);
            if (i < 0) {
                this.warn("Invalid integer: " + str);
                return null;
            }
            return new RangeInt(i, i);
        }
    }
    
    public boolean parseBoolean(final String str, final boolean defVal) {
        if (str == null) {
            return defVal;
        }
        final String s = str.toLowerCase().trim();
        if (s.equals("true")) {
            return true;
        }
        if (s.equals("false")) {
            return false;
        }
        this.warn("Invalid boolean: " + str);
        return defVal;
    }
    
    public Boolean parseBooleanObject(final String str) {
        if (str == null) {
            return null;
        }
        final String s = str.toLowerCase().trim();
        if (s.equals("true")) {
            return Boolean.TRUE;
        }
        if (s.equals("false")) {
            return Boolean.FALSE;
        }
        this.warn("Invalid boolean: " + str);
        return null;
    }
    
    public static int parseColor(String str, final int defVal) {
        if (str == null) {
            return defVal;
        }
        str = str.trim();
        try {
            final int i = Integer.parseInt(str, 16) & 0xFFFFFF;
            return i;
        }
        catch (final NumberFormatException var3) {
            return defVal;
        }
    }
    
    public static int parseColor4(String str, final int defVal) {
        if (str == null) {
            return defVal;
        }
        str = str.trim();
        try {
            final int i = (int)(Long.parseLong(str, 16) & -1L);
            return i;
        }
        catch (final NumberFormatException var3) {
            return defVal;
        }
    }
    
    public EnumWorldBlockLayer parseBlockRenderLayer(String str, final EnumWorldBlockLayer def) {
        if (str == null) {
            return def;
        }
        str = str.toLowerCase().trim();
        final EnumWorldBlockLayer[] aenumworldblocklayer = EnumWorldBlockLayer.values();
        for (int i = 0; i < aenumworldblocklayer.length; ++i) {
            final EnumWorldBlockLayer enumworldblocklayer = aenumworldblocklayer[i];
            if (str.equals(enumworldblocklayer.name().toLowerCase())) {
                return enumworldblocklayer;
            }
        }
        return def;
    }
    
    public <T> T parseObject(final String str, final T[] objs, final INameGetter nameGetter, final String property) {
        if (str == null) {
            return null;
        }
        final String s = str.toLowerCase().trim();
        for (int i = 0; i < objs.length; ++i) {
            final T t = objs[i];
            final String s2 = nameGetter.getName(t);
            if (s2 != null && s2.toLowerCase().equals(s)) {
                return t;
            }
        }
        this.warn("Invalid " + property + ": " + str);
        return null;
    }
    
    public <T> T[] parseObjects(String str, final T[] objs, final INameGetter nameGetter, final String property, final T[] errValue) {
        if (str == null) {
            return null;
        }
        str = str.toLowerCase().trim();
        final String[] astring = Config.tokenize(str, " ");
        final Object[] at = (Object[])Array.newInstance(objs.getClass().getComponentType(), astring.length);
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            final T t = this.parseObject(s, objs, nameGetter, property);
            if (t == null) {
                return errValue;
            }
            at[i] = t;
        }
        return (T[])at;
    }
    
    public Enum parseEnum(final String str, final Enum[] enums, final String property) {
        return this.parseObject(str, enums, ConnectedParser.NAME_GETTER_ENUM, property);
    }
    
    public Enum[] parseEnums(final String str, final Enum[] enums, final String property, final Enum[] errValue) {
        return this.parseObjects(str, enums, ConnectedParser.NAME_GETTER_ENUM, property, errValue);
    }
    
    public EnumDyeColor[] parseDyeColors(final String str, final String property, final EnumDyeColor[] errValue) {
        return this.parseObjects(str, EnumDyeColor.values(), ConnectedParser.NAME_GETTER_DYE_COLOR, property, errValue);
    }
    
    public Weather[] parseWeather(final String str, final String property, final Weather[] errValue) {
        return this.parseObjects(str, Weather.values(), ConnectedParser.NAME_GETTER_ENUM, property, errValue);
    }
    
    public NbtTagValue parseNbtTagValue(final String path, final String value) {
        return (path != null && value != null) ? new NbtTagValue(path, value) : null;
    }
    
    public VillagerProfession[] parseProfessions(final String profStr) {
        if (profStr == null) {
            return null;
        }
        final List<VillagerProfession> list = new ArrayList<VillagerProfession>();
        final String[] astring = Config.tokenize(profStr, " ");
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            final VillagerProfession villagerprofession = this.parseProfession(s);
            if (villagerprofession == null) {
                this.warn("Invalid profession: " + s);
                return ConnectedParser.PROFESSIONS_INVALID;
            }
            list.add(villagerprofession);
        }
        if (list.isEmpty()) {
            return null;
        }
        final VillagerProfession[] avillagerprofession = list.toArray(new VillagerProfession[list.size()]);
        return avillagerprofession;
    }
    
    private VillagerProfession parseProfession(String str) {
        str = str.toLowerCase();
        final String[] astring = Config.tokenize(str, ":");
        if (astring.length > 2) {
            return null;
        }
        final String s = astring[0];
        String s2 = null;
        if (astring.length > 1) {
            s2 = astring[1];
        }
        final int i = parseProfessionId(s);
        if (i < 0) {
            return null;
        }
        int[] aint = null;
        if (s2 != null) {
            aint = parseCareerIds(i, s2);
            if (aint == null) {
                return null;
            }
        }
        return new VillagerProfession(i, aint);
    }
    
    private static int parseProfessionId(final String str) {
        final int i = Config.parseInt(str, -1);
        return (i >= 0) ? i : (str.equals("farmer") ? 0 : (str.equals("librarian") ? 1 : (str.equals("priest") ? 2 : (str.equals("blacksmith") ? 3 : (str.equals("butcher") ? 4 : (str.equals("nitwit") ? 5 : -1))))));
    }
    
    private static int[] parseCareerIds(final int prof, final String str) {
        final Set<Integer> set = new HashSet<Integer>();
        final String[] astring = Config.tokenize(str, ",");
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            final int j = parseCareerId(prof, s);
            if (j < 0) {
                return null;
            }
            set.add(j);
        }
        final Integer[] ainteger = set.toArray(new Integer[set.size()]);
        final int[] aint = new int[ainteger.length];
        for (int k = 0; k < aint.length; ++k) {
            aint[k] = ainteger[k];
        }
        return aint;
    }
    
    private static int parseCareerId(final int prof, final String str) {
        final int i = Config.parseInt(str, -1);
        if (i >= 0) {
            return i;
        }
        if (prof == 0) {
            if (str.equals("farmer")) {
                return 1;
            }
            if (str.equals("fisherman")) {
                return 2;
            }
            if (str.equals("shepherd")) {
                return 3;
            }
            if (str.equals("fletcher")) {
                return 4;
            }
        }
        if (prof == 1) {
            if (str.equals("librarian")) {
                return 1;
            }
            if (str.equals("cartographer")) {
                return 2;
            }
        }
        if (prof == 2 && str.equals("cleric")) {
            return 1;
        }
        if (prof == 3) {
            if (str.equals("armor")) {
                return 1;
            }
            if (str.equals("weapon")) {
                return 2;
            }
            if (str.equals("tool")) {
                return 3;
            }
        }
        if (prof == 4) {
            if (str.equals("butcher")) {
                return 1;
            }
            if (str.equals("leather")) {
                return 2;
            }
        }
        return (prof == 5 && str.equals("nitwit")) ? 1 : -1;
    }
    
    public int[] parseItems(String str) {
        str = str.trim();
        final Set<Integer> set = new TreeSet<Integer>();
        final String[] astring = Config.tokenize(str, " ");
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            final ResourceLocation resourcelocation = new ResourceLocation(s);
            final Item item = Item.itemRegistry.getObject(resourcelocation);
            if (item == null) {
                this.warn("Item not found: " + s);
            }
            else {
                final int j = Item.getIdFromItem(item);
                if (j < 0) {
                    this.warn("Item has no ID: " + item + ", name: " + s);
                }
                else {
                    set.add(new Integer(j));
                }
            }
        }
        final Integer[] ainteger = set.toArray(new Integer[set.size()]);
        final int[] aint = Config.toPrimitive(ainteger);
        return aint;
    }
    
    public int[] parseEntities(String str) {
        str = str.trim();
        final Set<Integer> set = new TreeSet<Integer>();
        final String[] astring = Config.tokenize(str, " ");
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            final int j = EntityUtils.getEntityIdByName(s);
            if (j < 0) {
                this.warn("Entity not found: " + s);
            }
            else {
                set.add(new Integer(j));
            }
        }
        final Integer[] ainteger = set.toArray(new Integer[set.size()]);
        final int[] aint = Config.toPrimitive(ainteger);
        return aint;
    }
}
