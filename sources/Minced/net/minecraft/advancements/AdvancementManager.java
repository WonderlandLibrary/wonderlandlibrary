// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.advancements;

import com.google.gson.TypeAdapterFactory;
import net.minecraft.util.EnumTypeAdapterFactory;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.ITextComponent;
import com.google.gson.JsonObject;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializer;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import java.io.BufferedReader;
import java.net.URI;
import java.net.URL;
import java.nio.file.FileSystem;
import java.io.Closeable;
import java.net.URISyntaxException;
import org.apache.commons.io.IOUtils;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.FileVisitOption;
import java.nio.file.FileSystems;
import java.util.Collections;
import java.nio.file.Paths;
import net.minecraft.item.crafting.CraftingManager;
import java.io.IOException;
import com.google.gson.JsonParseException;
import net.minecraft.util.JsonUtils;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.FileUtils;
import com.google.common.collect.Maps;
import java.util.Iterator;
import net.minecraft.util.ResourceLocation;
import java.util.Map;
import javax.annotation.Nullable;
import java.io.File;
import com.google.gson.Gson;
import org.apache.logging.log4j.Logger;

public class AdvancementManager
{
    private static final Logger LOGGER;
    private static final Gson GSON;
    private static final AdvancementList ADVANCEMENT_LIST;
    private final File advancementsDir;
    private boolean hasErrored;
    
    public AdvancementManager(@Nullable final File advancementsDirIn) {
        this.advancementsDir = advancementsDirIn;
        this.reload();
    }
    
    public void reload() {
        this.hasErrored = false;
        AdvancementManager.ADVANCEMENT_LIST.clear();
        final Map<ResourceLocation, Advancement.Builder> map = this.loadCustomAdvancements();
        this.loadBuiltInAdvancements(map);
        AdvancementManager.ADVANCEMENT_LIST.loadAdvancements(map);
        for (final Advancement advancement : AdvancementManager.ADVANCEMENT_LIST.getRoots()) {
            if (advancement.getDisplay() != null) {
                AdvancementTreeNode.layout(advancement);
            }
        }
    }
    
    public boolean hasErrored() {
        return this.hasErrored;
    }
    
    private Map<ResourceLocation, Advancement.Builder> loadCustomAdvancements() {
        if (this.advancementsDir == null) {
            return (Map<ResourceLocation, Advancement.Builder>)Maps.newHashMap();
        }
        final Map<ResourceLocation, Advancement.Builder> map = (Map<ResourceLocation, Advancement.Builder>)Maps.newHashMap();
        this.advancementsDir.mkdirs();
        for (final File file1 : FileUtils.listFiles(this.advancementsDir, new String[] { "json" }, true)) {
            final String s = FilenameUtils.removeExtension(this.advancementsDir.toURI().relativize(file1.toURI()).toString());
            final String[] astring = s.split("/", 2);
            if (astring.length == 2) {
                final ResourceLocation resourcelocation = new ResourceLocation(astring[0], astring[1]);
                try {
                    final Advancement.Builder advancement$builder = JsonUtils.gsonDeserialize(AdvancementManager.GSON, FileUtils.readFileToString(file1, StandardCharsets.UTF_8), Advancement.Builder.class);
                    if (advancement$builder == null) {
                        AdvancementManager.LOGGER.error("Couldn't load custom advancement " + resourcelocation + " from " + file1 + " as it's empty or null");
                    }
                    else {
                        map.put(resourcelocation, advancement$builder);
                    }
                }
                catch (IllegalArgumentException | JsonParseException ex2) {
                    final RuntimeException ex;
                    final RuntimeException jsonparseexception = ex;
                    AdvancementManager.LOGGER.error("Parsing error loading custom advancement " + resourcelocation, (Throwable)jsonparseexception);
                    this.hasErrored = true;
                }
                catch (IOException ioexception) {
                    AdvancementManager.LOGGER.error("Couldn't read custom advancement " + resourcelocation + " from " + file1, (Throwable)ioexception);
                    this.hasErrored = true;
                }
            }
        }
        return map;
    }
    
    private void loadBuiltInAdvancements(final Map<ResourceLocation, Advancement.Builder> map) {
        FileSystem filesystem = null;
        try {
            final URL url = AdvancementManager.class.getResource("/assets/.mcassetsroot");
            if (url != null) {
                final URI uri = url.toURI();
                Path path;
                if ("file".equals(uri.getScheme())) {
                    path = Paths.get(CraftingManager.class.getResource("/assets/minecraft/advancements").toURI());
                }
                else {
                    if (!"jar".equals(uri.getScheme())) {
                        AdvancementManager.LOGGER.error("Unsupported scheme " + uri + " trying to list all built-in advancements (NYI?)");
                        this.hasErrored = true;
                        return;
                    }
                    filesystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
                    path = filesystem.getPath("/assets/minecraft/advancements", new String[0]);
                }
                for (final Path path2 : Files.walk(path, new FileVisitOption[0])) {
                    if ("json".equals(FilenameUtils.getExtension(path2.toString()))) {
                        final Path path3 = path.relativize(path2);
                        final String s = FilenameUtils.removeExtension(path3.toString()).replaceAll("\\\\", "/");
                        final ResourceLocation resourcelocation = new ResourceLocation("minecraft", s);
                        if (map.containsKey(resourcelocation)) {
                            continue;
                        }
                        BufferedReader bufferedreader = null;
                        try {
                            bufferedreader = Files.newBufferedReader(path2);
                            final Advancement.Builder advancement$builder = JsonUtils.fromJson(AdvancementManager.GSON, bufferedreader, Advancement.Builder.class);
                            map.put(resourcelocation, advancement$builder);
                        }
                        catch (JsonParseException jsonparseexception) {
                            AdvancementManager.LOGGER.error("Parsing error loading built-in advancement " + resourcelocation, (Throwable)jsonparseexception);
                            this.hasErrored = true;
                        }
                        catch (IOException ioexception) {
                            AdvancementManager.LOGGER.error("Couldn't read advancement " + resourcelocation + " from " + path2, (Throwable)ioexception);
                            this.hasErrored = true;
                        }
                        finally {
                            IOUtils.closeQuietly((Reader)bufferedreader);
                        }
                    }
                }
                return;
            }
            AdvancementManager.LOGGER.error("Couldn't find .mcassetsroot");
            this.hasErrored = true;
        }
        catch (IOException ex) {}
        catch (URISyntaxException urisyntaxexception) {
            AdvancementManager.LOGGER.error("Couldn't get a list of all built-in advancement files", (Throwable)urisyntaxexception);
            this.hasErrored = true;
        }
        finally {
            IOUtils.closeQuietly((Closeable)filesystem);
        }
    }
    
    @Nullable
    public Advancement getAdvancement(final ResourceLocation id) {
        return AdvancementManager.ADVANCEMENT_LIST.getAdvancement(id);
    }
    
    public Iterable<Advancement> getAdvancements() {
        return AdvancementManager.ADVANCEMENT_LIST.getAdvancements();
    }
    
    static {
        LOGGER = LogManager.getLogger();
        GSON = new GsonBuilder().registerTypeHierarchyAdapter((Class)Advancement.Builder.class, (Object)new JsonDeserializer<Advancement.Builder>() {
            public Advancement.Builder deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
                final JsonObject jsonobject = JsonUtils.getJsonObject(p_deserialize_1_, "advancement");
                return Advancement.Builder.deserialize(jsonobject, p_deserialize_3_);
            }
        }).registerTypeAdapter((Type)AdvancementRewards.class, (Object)new AdvancementRewards.Deserializer()).registerTypeHierarchyAdapter((Class)ITextComponent.class, (Object)new ITextComponent.Serializer()).registerTypeHierarchyAdapter((Class)Style.class, (Object)new Style.Serializer()).registerTypeAdapterFactory((TypeAdapterFactory)new EnumTypeAdapterFactory()).create();
        ADVANCEMENT_LIST = new AdvancementList();
    }
}
