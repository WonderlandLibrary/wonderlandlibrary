// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.stats;

import org.apache.logging.log4j.LogManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketStatistics;
import net.minecraft.entity.player.EntityPlayerMP;
import java.util.Collection;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import com.google.gson.JsonObject;
import net.minecraft.util.IJsonSerializable;
import com.google.gson.JsonElement;
import com.google.common.collect.Maps;
import com.google.gson.JsonParser;
import net.minecraft.entity.player.EntityPlayer;
import com.google.gson.JsonParseException;
import java.io.IOException;
import net.minecraft.util.TupleIntJsonSerializable;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import com.google.common.collect.Sets;
import java.util.Set;
import java.io.File;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Logger;

public class StatisticsManagerServer extends StatisticsManager
{
    private static final Logger LOGGER;
    private final MinecraftServer server;
    private final File statsFile;
    private final Set<StatBase> dirty;
    private int lastStatRequest;
    
    public StatisticsManagerServer(final MinecraftServer serverIn, final File statsFileIn) {
        this.dirty = (Set<StatBase>)Sets.newHashSet();
        this.lastStatRequest = -300;
        this.server = serverIn;
        this.statsFile = statsFileIn;
    }
    
    public void readStatFile() {
        if (this.statsFile.isFile()) {
            try {
                this.statsData.clear();
                this.statsData.putAll(this.parseJson(FileUtils.readFileToString(this.statsFile)));
            }
            catch (IOException ioexception) {
                StatisticsManagerServer.LOGGER.error("Couldn't read statistics file {}", (Object)this.statsFile, (Object)ioexception);
            }
            catch (JsonParseException jsonparseexception) {
                StatisticsManagerServer.LOGGER.error("Couldn't parse statistics file {}", (Object)this.statsFile, (Object)jsonparseexception);
            }
        }
    }
    
    public void saveStatFile() {
        try {
            FileUtils.writeStringToFile(this.statsFile, dumpJson(this.statsData));
        }
        catch (IOException ioexception) {
            StatisticsManagerServer.LOGGER.error("Couldn't save stats", (Throwable)ioexception);
        }
    }
    
    @Override
    public void unlockAchievement(final EntityPlayer playerIn, final StatBase statIn, final int p_150873_3_) {
        super.unlockAchievement(playerIn, statIn, p_150873_3_);
        this.dirty.add(statIn);
    }
    
    private Set<StatBase> getDirty() {
        final Set<StatBase> set = (Set<StatBase>)Sets.newHashSet((Iterable)this.dirty);
        this.dirty.clear();
        return set;
    }
    
    public Map<StatBase, TupleIntJsonSerializable> parseJson(final String p_150881_1_) {
        final JsonElement jsonelement = new JsonParser().parse(p_150881_1_);
        if (!jsonelement.isJsonObject()) {
            return (Map<StatBase, TupleIntJsonSerializable>)Maps.newHashMap();
        }
        final JsonObject jsonobject = jsonelement.getAsJsonObject();
        final Map<StatBase, TupleIntJsonSerializable> map = (Map<StatBase, TupleIntJsonSerializable>)Maps.newHashMap();
        for (final Map.Entry<String, JsonElement> entry : jsonobject.entrySet()) {
            final StatBase statbase = StatList.getOneShotStat(entry.getKey());
            if (statbase != null) {
                final TupleIntJsonSerializable tupleintjsonserializable = new TupleIntJsonSerializable();
                if (entry.getValue().isJsonPrimitive() && entry.getValue().getAsJsonPrimitive().isNumber()) {
                    tupleintjsonserializable.setIntegerValue(entry.getValue().getAsInt());
                }
                else if (entry.getValue().isJsonObject()) {
                    final JsonObject jsonobject2 = entry.getValue().getAsJsonObject();
                    if (jsonobject2.has("value") && jsonobject2.get("value").isJsonPrimitive() && jsonobject2.get("value").getAsJsonPrimitive().isNumber()) {
                        tupleintjsonserializable.setIntegerValue(jsonobject2.getAsJsonPrimitive("value").getAsInt());
                    }
                    if (jsonobject2.has("progress") && statbase.getSerializableClazz() != null) {
                        try {
                            final Constructor<? extends IJsonSerializable> constructor = statbase.getSerializableClazz().getConstructor((Class<?>[])new Class[0]);
                            final IJsonSerializable ijsonserializable = (IJsonSerializable)constructor.newInstance(new Object[0]);
                            ijsonserializable.fromJson(jsonobject2.get("progress"));
                            tupleintjsonserializable.setJsonSerializableValue(ijsonserializable);
                        }
                        catch (Throwable throwable) {
                            StatisticsManagerServer.LOGGER.warn("Invalid statistic progress in {}", (Object)this.statsFile, (Object)throwable);
                        }
                    }
                }
                map.put(statbase, tupleintjsonserializable);
            }
            else {
                StatisticsManagerServer.LOGGER.warn("Invalid statistic in {}: Don't know what {} is", (Object)this.statsFile, (Object)entry.getKey());
            }
        }
        return map;
    }
    
    public static String dumpJson(final Map<StatBase, TupleIntJsonSerializable> p_150880_0_) {
        final JsonObject jsonobject = new JsonObject();
        for (final Map.Entry<StatBase, TupleIntJsonSerializable> entry : p_150880_0_.entrySet()) {
            if (entry.getValue().getJsonSerializableValue() != null) {
                final JsonObject jsonobject2 = new JsonObject();
                jsonobject2.addProperty("value", (Number)entry.getValue().getIntegerValue());
                try {
                    jsonobject2.add("progress", entry.getValue().getJsonSerializableValue().getSerializableElement());
                }
                catch (Throwable throwable) {
                    StatisticsManagerServer.LOGGER.warn("Couldn't save statistic {}: error serializing progress", (Object)entry.getKey().getStatName(), (Object)throwable);
                }
                jsonobject.add(entry.getKey().statId, (JsonElement)jsonobject2);
            }
            else {
                jsonobject.addProperty(entry.getKey().statId, (Number)entry.getValue().getIntegerValue());
            }
        }
        return jsonobject.toString();
    }
    
    public void markAllDirty() {
        this.dirty.addAll(this.statsData.keySet());
    }
    
    public void sendStats(final EntityPlayerMP player) {
        final int i = this.server.getTickCounter();
        final Map<StatBase, Integer> map = (Map<StatBase, Integer>)Maps.newHashMap();
        if (i - this.lastStatRequest > 300) {
            this.lastStatRequest = i;
            for (final StatBase statbase : this.getDirty()) {
                map.put(statbase, this.readStat(statbase));
            }
        }
        player.connection.sendPacket(new SPacketStatistics(map));
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
