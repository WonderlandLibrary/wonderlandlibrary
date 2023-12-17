// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.storage.loot;

import java.net.URL;
import com.google.common.io.Resources;
import com.google.gson.JsonParseException;
import net.minecraft.util.JsonUtils;
import java.io.IOException;
import com.google.common.io.Files;
import java.nio.charset.StandardCharsets;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraft.world.storage.loot.functions.LootFunction;
import java.lang.reflect.Type;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheBuilder;
import javax.annotation.Nullable;
import java.io.File;
import net.minecraft.util.ResourceLocation;
import com.google.common.cache.LoadingCache;
import com.google.gson.Gson;
import org.apache.logging.log4j.Logger;

public class LootTableManager
{
    private static final Logger LOGGER;
    private static final Gson GSON_INSTANCE;
    private final LoadingCache<ResourceLocation, LootTable> registeredLootTables;
    private final File baseFolder;
    
    public LootTableManager(@Nullable final File folder) {
        this.registeredLootTables = (LoadingCache<ResourceLocation, LootTable>)CacheBuilder.newBuilder().build((CacheLoader)new Loader());
        this.baseFolder = folder;
        this.reloadLootTables();
    }
    
    public LootTable getLootTableFromLocation(final ResourceLocation ressources) {
        return (LootTable)this.registeredLootTables.getUnchecked((Object)ressources);
    }
    
    public void reloadLootTables() {
        this.registeredLootTables.invalidateAll();
        for (final ResourceLocation resourcelocation : LootTableList.getAll()) {
            this.getLootTableFromLocation(resourcelocation);
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
        GSON_INSTANCE = new GsonBuilder().registerTypeAdapter((Type)RandomValueRange.class, (Object)new RandomValueRange.Serializer()).registerTypeAdapter((Type)LootPool.class, (Object)new LootPool.Serializer()).registerTypeAdapter((Type)LootTable.class, (Object)new LootTable.Serializer()).registerTypeHierarchyAdapter((Class)LootEntry.class, (Object)new LootEntry.Serializer()).registerTypeHierarchyAdapter((Class)LootFunction.class, (Object)new LootFunctionManager.Serializer()).registerTypeHierarchyAdapter((Class)LootCondition.class, (Object)new LootConditionManager.Serializer()).registerTypeHierarchyAdapter((Class)LootContext.EntityTarget.class, (Object)new LootContext.EntityTarget.Serializer()).create();
    }
    
    class Loader extends CacheLoader<ResourceLocation, LootTable>
    {
        private Loader() {
        }
        
        public LootTable load(final ResourceLocation p_load_1_) throws Exception {
            if (p_load_1_.getPath().contains(".")) {
                LootTableManager.LOGGER.debug("Invalid loot table name '{}' (can't contain periods)", (Object)p_load_1_);
                return LootTable.EMPTY_LOOT_TABLE;
            }
            LootTable loottable = this.loadLootTable(p_load_1_);
            if (loottable == null) {
                loottable = this.loadBuiltinLootTable(p_load_1_);
            }
            if (loottable == null) {
                loottable = LootTable.EMPTY_LOOT_TABLE;
                LootTableManager.LOGGER.warn("Couldn't find resource table {}", (Object)p_load_1_);
            }
            return loottable;
        }
        
        @Nullable
        private LootTable loadLootTable(final ResourceLocation resource) {
            if (LootTableManager.this.baseFolder == null) {
                return null;
            }
            final File file1 = new File(new File(LootTableManager.this.baseFolder, resource.getNamespace()), resource.getPath() + ".json");
            if (file1.exists()) {
                if (file1.isFile()) {
                    String s;
                    try {
                        s = Files.toString(file1, StandardCharsets.UTF_8);
                    }
                    catch (IOException ioexception) {
                        LootTableManager.LOGGER.warn("Couldn't load loot table {} from {}", (Object)resource, (Object)file1, (Object)ioexception);
                        return LootTable.EMPTY_LOOT_TABLE;
                    }
                    try {
                        return JsonUtils.gsonDeserialize(LootTableManager.GSON_INSTANCE, s, LootTable.class);
                    }
                    catch (IllegalArgumentException | JsonParseException ex2) {
                        final RuntimeException ex;
                        final RuntimeException jsonparseexception = ex;
                        LootTableManager.LOGGER.error("Couldn't load loot table {} from {}", (Object)resource, (Object)file1, (Object)jsonparseexception);
                        return LootTable.EMPTY_LOOT_TABLE;
                    }
                }
                LootTableManager.LOGGER.warn("Expected to find loot table {} at {} but it was a folder.", (Object)resource, (Object)file1);
                return LootTable.EMPTY_LOOT_TABLE;
            }
            return null;
        }
        
        @Nullable
        private LootTable loadBuiltinLootTable(final ResourceLocation resource) {
            final URL url = LootTableManager.class.getResource("/assets/" + resource.getNamespace() + "/loot_tables/" + resource.getPath() + ".json");
            if (url != null) {
                String s;
                try {
                    s = Resources.toString(url, StandardCharsets.UTF_8);
                }
                catch (IOException ioexception) {
                    LootTableManager.LOGGER.warn("Couldn't load loot table {} from {}", (Object)resource, (Object)url, (Object)ioexception);
                    return LootTable.EMPTY_LOOT_TABLE;
                }
                try {
                    return JsonUtils.gsonDeserialize(LootTableManager.GSON_INSTANCE, s, LootTable.class);
                }
                catch (JsonParseException jsonparseexception) {
                    LootTableManager.LOGGER.error("Couldn't load loot table {} from {}", (Object)resource, (Object)url, (Object)jsonparseexception);
                    return LootTable.EMPTY_LOOT_TABLE;
                }
            }
            return null;
        }
    }
}
