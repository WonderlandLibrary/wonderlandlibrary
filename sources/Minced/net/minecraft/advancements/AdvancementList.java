// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.advancements;

import org.apache.logging.log4j.LogManager;
import javax.annotation.Nullable;
import java.util.function.Function;
import com.google.common.base.Functions;
import java.util.Iterator;
import com.google.common.collect.Sets;
import com.google.common.collect.Maps;
import java.util.Set;
import net.minecraft.util.ResourceLocation;
import java.util.Map;
import org.apache.logging.log4j.Logger;

public class AdvancementList
{
    private static final Logger LOGGER;
    private final Map<ResourceLocation, Advancement> advancements;
    private final Set<Advancement> roots;
    private final Set<Advancement> nonRoots;
    private Listener listener;
    
    public AdvancementList() {
        this.advancements = (Map<ResourceLocation, Advancement>)Maps.newHashMap();
        this.roots = (Set<Advancement>)Sets.newLinkedHashSet();
        this.nonRoots = (Set<Advancement>)Sets.newLinkedHashSet();
    }
    
    private void remove(final Advancement advancementIn) {
        for (final Advancement advancement : advancementIn.getChildren()) {
            this.remove(advancement);
        }
        AdvancementList.LOGGER.info("Forgot about advancement " + advancementIn.getId());
        this.advancements.remove(advancementIn.getId());
        if (advancementIn.getParent() == null) {
            this.roots.remove(advancementIn);
            if (this.listener != null) {
                this.listener.rootAdvancementRemoved(advancementIn);
            }
        }
        else {
            this.nonRoots.remove(advancementIn);
            if (this.listener != null) {
                this.listener.nonRootAdvancementRemoved(advancementIn);
            }
        }
    }
    
    public void removeAll(final Set<ResourceLocation> ids) {
        for (final ResourceLocation resourcelocation : ids) {
            final Advancement advancement = this.advancements.get(resourcelocation);
            if (advancement == null) {
                AdvancementList.LOGGER.warn("Told to remove advancement " + resourcelocation + " but I don't know what that is");
            }
            else {
                this.remove(advancement);
            }
        }
    }
    
    public void loadAdvancements(final Map<ResourceLocation, Advancement.Builder> advancementsIn) {
        final Function<ResourceLocation, Advancement> function = (Function<ResourceLocation, Advancement>)Functions.forMap((Map)this.advancements, (Object)null);
        while (!advancementsIn.isEmpty()) {
            boolean flag = false;
            Iterator<Map.Entry<ResourceLocation, Advancement.Builder>> iterator = advancementsIn.entrySet().iterator();
            while (iterator.hasNext()) {
                final Map.Entry<ResourceLocation, Advancement.Builder> entry = iterator.next();
                final ResourceLocation resourcelocation = entry.getKey();
                final Advancement.Builder advancement$builder = entry.getValue();
                if (advancement$builder.resolveParent(function)) {
                    final Advancement advancement = advancement$builder.build(resourcelocation);
                    this.advancements.put(resourcelocation, advancement);
                    flag = true;
                    iterator.remove();
                    if (advancement.getParent() == null) {
                        this.roots.add(advancement);
                        if (this.listener == null) {
                            continue;
                        }
                        this.listener.rootAdvancementAdded(advancement);
                    }
                    else {
                        this.nonRoots.add(advancement);
                        if (this.listener == null) {
                            continue;
                        }
                        this.listener.nonRootAdvancementAdded(advancement);
                    }
                }
            }
            if (!flag) {
                iterator = advancementsIn.entrySet().iterator();
                while (iterator.hasNext()) {
                    final Map.Entry<ResourceLocation, Advancement.Builder> entry2 = iterator.next();
                    AdvancementList.LOGGER.error("Couldn't load advancement " + entry2.getKey() + ": " + entry2.getValue());
                }
                break;
            }
        }
        AdvancementList.LOGGER.info("Loaded " + this.advancements.size() + " advancements");
    }
    
    public void clear() {
        this.advancements.clear();
        this.roots.clear();
        this.nonRoots.clear();
        if (this.listener != null) {
            this.listener.advancementsCleared();
        }
    }
    
    public Iterable<Advancement> getRoots() {
        return this.roots;
    }
    
    public Iterable<Advancement> getAdvancements() {
        return this.advancements.values();
    }
    
    @Nullable
    public Advancement getAdvancement(final ResourceLocation id) {
        return this.advancements.get(id);
    }
    
    public void setListener(@Nullable final Listener listenerIn) {
        this.listener = listenerIn;
        if (listenerIn != null) {
            for (final Advancement advancement : this.roots) {
                listenerIn.rootAdvancementAdded(advancement);
            }
            for (final Advancement advancement2 : this.nonRoots) {
                listenerIn.nonRootAdvancementAdded(advancement2);
            }
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public interface Listener
    {
        void rootAdvancementAdded(final Advancement p0);
        
        void rootAdvancementRemoved(final Advancement p0);
        
        void nonRootAdvancementAdded(final Advancement p0);
        
        void nonRootAdvancementRemoved(final Advancement p0);
        
        void advancementsCleared();
    }
}
