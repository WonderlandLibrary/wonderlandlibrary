// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.datafix;

import org.apache.logging.log4j.LogManager;
import com.google.common.collect.Lists;
import net.minecraft.util.Util;
import net.minecraft.nbt.NBTTagCompound;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;

public class DataFixer implements IDataFixer
{
    private static final Logger LOGGER;
    private final Map<IFixType, List<IDataWalker>> walkerMap;
    private final Map<IFixType, List<IFixableData>> fixMap;
    private final int version;
    
    public DataFixer(final int versionIn) {
        this.walkerMap = (Map<IFixType, List<IDataWalker>>)Maps.newHashMap();
        this.fixMap = (Map<IFixType, List<IFixableData>>)Maps.newHashMap();
        this.version = versionIn;
    }
    
    public NBTTagCompound process(final IFixType type, final NBTTagCompound compound) {
        final int i = compound.hasKey("DataVersion", 99) ? compound.getInteger("DataVersion") : -1;
        return (i >= 1343) ? compound : this.process(type, compound, i);
    }
    
    @Override
    public NBTTagCompound process(final IFixType type, NBTTagCompound compound, final int versionIn) {
        if (versionIn < this.version) {
            compound = this.processFixes(type, compound, versionIn);
            compound = this.processWalkers(type, compound, versionIn);
        }
        return compound;
    }
    
    private NBTTagCompound processFixes(final IFixType type, NBTTagCompound compound, final int versionIn) {
        final List<IFixableData> list = this.fixMap.get(type);
        if (list != null) {
            for (int i = 0; i < list.size(); ++i) {
                final IFixableData ifixabledata = list.get(i);
                if (ifixabledata.getFixVersion() > versionIn) {
                    compound = ifixabledata.fixTagCompound(compound);
                }
            }
        }
        return compound;
    }
    
    private NBTTagCompound processWalkers(final IFixType type, NBTTagCompound compound, final int versionIn) {
        final List<IDataWalker> list = this.walkerMap.get(type);
        if (list != null) {
            for (int i = 0; i < list.size(); ++i) {
                compound = list.get(i).process(this, compound, versionIn);
            }
        }
        return compound;
    }
    
    public void registerWalker(final FixTypes type, final IDataWalker walker) {
        this.registerVanillaWalker(type, walker);
    }
    
    public void registerVanillaWalker(final IFixType type, final IDataWalker walker) {
        this.getTypeList(this.walkerMap, type).add(walker);
    }
    
    public void registerFix(final IFixType type, final IFixableData fixable) {
        final List<IFixableData> list = this.getTypeList(this.fixMap, type);
        final int i = fixable.getFixVersion();
        if (i > this.version) {
            DataFixer.LOGGER.warn("Ignored fix registered for version: {} as the DataVersion of the game is: {}", (Object)i, (Object)this.version);
        }
        else if (!list.isEmpty() && Util.getLastElement(list).getFixVersion() > i) {
            for (int j = 0; j < list.size(); ++j) {
                if (list.get(j).getFixVersion() > i) {
                    list.add(j, fixable);
                    break;
                }
            }
        }
        else {
            list.add(fixable);
        }
    }
    
    private <V> List<V> getTypeList(final Map<IFixType, List<V>> map, final IFixType type) {
        List<V> list = map.get(type);
        if (list == null) {
            list = (List<V>)Lists.newArrayList();
            map.put(type, list);
        }
        return list;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
