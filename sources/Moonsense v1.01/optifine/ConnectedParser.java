// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import java.util.EnumSet;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.biome.BiomeGenBase;
import java.util.Set;
import java.util.Iterator;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import net.minecraft.block.Block;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;

public class ConnectedParser
{
    private String context;
    private static final MatchBlock[] NO_MATCH_BLOCKS;
    
    static {
        NO_MATCH_BLOCKS = new MatchBlock[0];
    }
    
    public ConnectedParser(final String context) {
        this.context = null;
        this.context = context;
    }
    
    public String parseName(final String path) {
        String str = path;
        final int pos = path.lastIndexOf(47);
        if (pos >= 0) {
            str = path.substring(pos + 1);
        }
        final int pos2 = str.lastIndexOf(46);
        if (pos2 >= 0) {
            str = str.substring(0, pos2);
        }
        return str;
    }
    
    public String parseBasePath(final String path) {
        final int pos = path.lastIndexOf(47);
        return (pos < 0) ? "" : path.substring(0, pos);
    }
    
    public MatchBlock[] parseMatchBlocks(final String propMatchBlocks) {
        if (propMatchBlocks == null) {
            return null;
        }
        final ArrayList list = new ArrayList();
        final String[] blockStrs = Config.tokenize(propMatchBlocks, " ");
        for (int mbs = 0; mbs < blockStrs.length; ++mbs) {
            final String blockStr = blockStrs[mbs];
            final MatchBlock[] mbs2 = this.parseMatchBlock(blockStr);
            if (mbs2 == null) {
                return ConnectedParser.NO_MATCH_BLOCKS;
            }
            list.addAll(Arrays.asList(mbs2));
        }
        final MatchBlock[] var7 = list.toArray(new MatchBlock[list.size()]);
        return var7;
    }
    
    public MatchBlock[] parseMatchBlock(String blockStr) {
        if (blockStr == null) {
            return null;
        }
        blockStr = blockStr.trim();
        if (blockStr.length() <= 0) {
            return null;
        }
        final String[] parts = Config.tokenize(blockStr, ":");
        String domain = "minecraft";
        final boolean blockIndex = false;
        byte var14;
        if (parts.length > 1 && this.isFullBlockName(parts)) {
            domain = parts[0];
            var14 = 1;
        }
        else {
            domain = "minecraft";
            var14 = 0;
        }
        final String blockPart = parts[var14];
        final String[] params = Arrays.copyOfRange(parts, var14 + 1, parts.length);
        final Block[] blocks = this.parseBlockPart(domain, blockPart);
        if (blocks == null) {
            return null;
        }
        final MatchBlock[] datas = new MatchBlock[blocks.length];
        for (int i = 0; i < blocks.length; ++i) {
            final Block block = blocks[i];
            final int blockId = Block.getIdFromBlock(block);
            int[] metadatas = null;
            if (params.length > 0) {
                metadatas = this.parseBlockMetadatas(block, params);
                if (metadatas == null) {
                    return null;
                }
            }
            final MatchBlock bd = new MatchBlock(blockId, metadatas);
            datas[i] = bd;
        }
        return datas;
    }
    
    public boolean isFullBlockName(final String[] parts) {
        if (parts.length < 2) {
            return false;
        }
        final String part1 = parts[1];
        return part1.length() >= 1 && !this.startsWithDigit(part1) && !part1.contains("=");
    }
    
    public boolean startsWithDigit(final String str) {
        if (str == null) {
            return false;
        }
        if (str.length() < 1) {
            return false;
        }
        final char ch = str.charAt(0);
        return Character.isDigit(ch);
    }
    
    public Block[] parseBlockPart(final String domain, final String blockPart) {
        if (this.startsWithDigit(blockPart)) {
            final int[] var8 = this.parseIntList(blockPart);
            if (var8 == null) {
                return null;
            }
            final Block[] var9 = new Block[var8.length];
            for (int var10 = 0; var10 < var8.length; ++var10) {
                final int id = var8[var10];
                final Block block1 = Block.getBlockById(id);
                if (block1 == null) {
                    this.warn("Block not found for id: " + id);
                    return null;
                }
                var9[var10] = block1;
            }
            return var9;
        }
        else {
            final String fullName = String.valueOf(domain) + ":" + blockPart;
            final Block block2 = Block.getBlockFromName(fullName);
            if (block2 == null) {
                this.warn("Block not found for name: " + fullName);
                return null;
            }
            final Block[] blocks = { block2 };
            return blocks;
        }
    }
    
    public int[] parseBlockMetadatas(final Block block, final String[] params) {
        if (params.length <= 0) {
            return null;
        }
        final String param0 = params[0];
        if (this.startsWithDigit(param0)) {
            final int[] var19 = this.parseIntList(param0);
            return var19;
        }
        final IBlockState stateDefault = block.getDefaultState();
        final Collection properties = stateDefault.getPropertyNames();
        final HashMap mapPropValues = new HashMap();
        for (int listMetadatas = 0; listMetadatas < params.length; ++listMetadatas) {
            final String metadatas = params[listMetadatas];
            if (metadatas.length() > 0) {
                final String[] i = Config.tokenize(metadatas, "=");
                if (i.length != 2) {
                    this.warn("Invalid block property: " + metadatas);
                    return null;
                }
                final String e = i[0];
                final String valStr = i[1];
                final IProperty prop = ConnectedProperties.getProperty(e, properties);
                if (prop == null) {
                    this.warn("Property not found: " + e + ", block: " + block);
                    return null;
                }
                Object list = mapPropValues.get(e);
                if (list == null) {
                    list = new ArrayList();
                    mapPropValues.put(prop, list);
                }
                final String[] vals = Config.tokenize(valStr, ",");
                for (int v = 0; v < vals.length; ++v) {
                    final String val = vals[v];
                    final Comparable propVal = parsePropertyValue(prop, val);
                    if (propVal == null) {
                        this.warn("Property value not found: " + val + ", property: " + e + ", block: " + block);
                        return null;
                    }
                    ((List)list).add(propVal);
                }
            }
        }
        if (mapPropValues.isEmpty()) {
            return null;
        }
        final ArrayList var20 = new ArrayList();
        for (int var21 = 0; var21 < 16; ++var21) {
            final int var22 = var21;
            try {
                final IBlockState var23 = this.getStateFromMeta(block, var22);
                if (this.matchState(var23, mapPropValues)) {
                    var20.add(var22);
                }
            }
            catch (IllegalArgumentException ex) {}
        }
        if (var20.size() == 16) {
            return null;
        }
        int var22;
        int[] var24;
        for (var24 = new int[var20.size()], var22 = 0; var22 < var24.length; ++var22) {
            var24[var22] = var20.get(var22);
        }
        return var24;
    }
    
    private IBlockState getStateFromMeta(final Block block, final int md) {
        try {
            IBlockState e = block.getStateFromMeta(md);
            if (block == Blocks.double_plant && md > 7) {
                final IBlockState bsLow = block.getStateFromMeta(md & 0x7);
                e = e.withProperty(BlockDoublePlant.VARIANT_PROP, bsLow.getValue(BlockDoublePlant.VARIANT_PROP));
            }
            return e;
        }
        catch (IllegalArgumentException var5) {
            return block.getDefaultState();
        }
    }
    
    public static Comparable parsePropertyValue(final IProperty prop, final String valStr) {
        final Class valueClass = prop.getValueClass();
        Comparable valueObj = parseValue(valStr, valueClass);
        if (valueObj == null) {
            final Collection propertyValues = prop.getAllowedValues();
            valueObj = getPropertyValue(valStr, propertyValues);
        }
        return valueObj;
    }
    
    public static Comparable getPropertyValue(final String value, final Collection propertyValues) {
        for (final Comparable obj : propertyValues) {
            if (String.valueOf(obj).equals(value)) {
                return obj;
            }
        }
        return null;
    }
    
    public static Comparable parseValue(final String str, final Class cls) {
        return (Comparable)((cls == String.class) ? str : ((cls == Boolean.class) ? Boolean.valueOf(str) : ((Double)((cls == Float.class) ? Float.valueOf(str) : ((cls == Double.class) ? Double.valueOf(str) : ((double)((cls == Integer.class) ? Integer.valueOf(str) : ((long)((cls == Long.class) ? Long.valueOf(str) : null)))))))));
    }
    
    public boolean matchState(final IBlockState bs, final Map<IProperty, List<Comparable>> mapPropValues) {
        final Set keys = mapPropValues.keySet();
        for (final IProperty prop : keys) {
            final List vals = mapPropValues.get(prop);
            final Comparable bsVal = bs.getValue(prop);
            if (bsVal == null) {
                return false;
            }
            if (!vals.contains(bsVal)) {
                return false;
            }
        }
        return true;
    }
    
    public BiomeGenBase[] parseBiomes(final String str) {
        if (str == null) {
            return null;
        }
        final String[] biomeNames = Config.tokenize(str, " ");
        final ArrayList list = new ArrayList();
        for (int biomeArr = 0; biomeArr < biomeNames.length; ++biomeArr) {
            final String biomeName = biomeNames[biomeArr];
            final BiomeGenBase biome = this.findBiome(biomeName);
            if (biome == null) {
                this.warn("Biome not found: " + biomeName);
            }
            else {
                list.add(biome);
            }
        }
        final BiomeGenBase[] var7 = list.toArray(new BiomeGenBase[list.size()]);
        return var7;
    }
    
    public BiomeGenBase findBiome(String biomeName) {
        biomeName = biomeName.toLowerCase();
        if (biomeName.equals("nether")) {
            return BiomeGenBase.hell;
        }
        final BiomeGenBase[] biomeList = BiomeGenBase.getBiomeGenArray();
        for (int i = 0; i < biomeList.length; ++i) {
            final BiomeGenBase biome = biomeList[i];
            if (biome != null) {
                final String name = biome.biomeName.replace(" ", "").toLowerCase();
                if (name.equals(biomeName)) {
                    return biome;
                }
            }
        }
        return null;
    }
    
    public int parseInt(final String str) {
        if (str == null) {
            return -1;
        }
        final int num = Config.parseInt(str, -1);
        if (num < 0) {
            this.warn("Invalid number: " + str);
        }
        return num;
    }
    
    public int parseInt(final String str, final int defVal) {
        if (str == null) {
            return defVal;
        }
        final int num = Config.parseInt(str, -1);
        if (num < 0) {
            this.warn("Invalid number: " + str);
            return defVal;
        }
        return num;
    }
    
    public int[] parseIntList(final String str) {
        if (str == null) {
            return null;
        }
        final ArrayList list = new ArrayList();
        final String[] intStrs = Config.tokenize(str, " ,");
        for (int ints = 0; ints < intStrs.length; ++ints) {
            final String i = intStrs[ints];
            if (i.contains("-")) {
                final String[] val = Config.tokenize(i, "-");
                if (val.length != 2) {
                    this.warn("Invalid interval: " + i + ", when parsing: " + str);
                }
                else {
                    final int min = Config.parseInt(val[0], -1);
                    final int max = Config.parseInt(val[1], -1);
                    if (min >= 0 && max >= 0 && min <= max) {
                        for (int n = min; n <= max; ++n) {
                            list.add(n);
                        }
                    }
                    else {
                        this.warn("Invalid interval: " + i + ", when parsing: " + str);
                    }
                }
            }
            else {
                final int var12 = Config.parseInt(i, -1);
                if (var12 < 0) {
                    this.warn("Invalid number: " + i + ", when parsing: " + str);
                }
                else {
                    list.add(var12);
                }
            }
        }
        final int[] var13 = new int[list.size()];
        for (int var14 = 0; var14 < var13.length; ++var14) {
            var13[var14] = list.get(var14);
        }
        return var13;
    }
    
    public boolean[] parseFaces(final String str, final boolean[] defVal) {
        if (str == null) {
            return defVal;
        }
        final EnumSet setFaces = EnumSet.allOf(EnumFacing.class);
        final String[] faceStrs = Config.tokenize(str, " ,");
        for (int faces = 0; faces < faceStrs.length; ++faces) {
            final String i = faceStrs[faces];
            if (i.equals("sides")) {
                setFaces.add(EnumFacing.NORTH);
                setFaces.add(EnumFacing.SOUTH);
                setFaces.add(EnumFacing.WEST);
                setFaces.add(EnumFacing.EAST);
            }
            else if (i.equals("all")) {
                setFaces.addAll(Arrays.asList(EnumFacing.VALUES));
            }
            else {
                final EnumFacing face = this.parseFace(i);
                if (face != null) {
                    setFaces.add(face);
                }
            }
        }
        final boolean[] var8 = new boolean[EnumFacing.VALUES.length];
        for (int var9 = 0; var9 < var8.length; ++var9) {
            var8[var9] = setFaces.contains(EnumFacing.VALUES[var9]);
        }
        return var8;
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
        final RangeListInt list = new RangeListInt();
        final String[] parts = Config.tokenize(str, " ,");
        for (int i = 0; i < parts.length; ++i) {
            final String part = parts[i];
            final RangeInt ri = this.parseRangeInt(part);
            if (ri == null) {
                return null;
            }
            list.addRange(ri);
        }
        return list;
    }
    
    private RangeInt parseRangeInt(final String str) {
        if (str == null) {
            return null;
        }
        if (str.indexOf(45) >= 0) {
            final String[] val1 = Config.tokenize(str, "-");
            if (val1.length != 2) {
                this.warn("Invalid range: " + str);
                return null;
            }
            final int min = Config.parseInt(val1[0], -1);
            final int max = Config.parseInt(val1[1], -1);
            if (min >= 0 && max >= 0) {
                return new RangeInt(min, max);
            }
            this.warn("Invalid range: " + str);
            return null;
        }
        else {
            final int val2 = Config.parseInt(str, -1);
            if (val2 < 0) {
                this.warn("Invalid integer: " + str);
                return null;
            }
            return new RangeInt(val2, val2);
        }
    }
    
    public static boolean parseBoolean(final String str) {
        return str != null && str.toLowerCase().equals("true");
    }
    
    public static int parseColor(String str, final int defVal) {
        if (str == null) {
            return defVal;
        }
        str = str.trim();
        try {
            final int e = Integer.parseInt(str, 16) & 0xFFFFFF;
            return e;
        }
        catch (NumberFormatException var3) {
            return defVal;
        }
    }
}
