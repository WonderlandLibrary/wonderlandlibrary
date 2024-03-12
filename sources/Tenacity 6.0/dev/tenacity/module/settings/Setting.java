// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.settings;

import jdk.nashorn.api.scripting.JSObject;
import store.intent.intentguard.annotation.Strategy;
import store.intent.intentguard.annotation.Exclude;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;

public abstract class Setting
{
    @Expose
    @SerializedName("name")
    public String name;
    private List<ParentAttribute<? extends Setting>> parents;
    
    public Setting() {
        this.parents = new ArrayList<ParentAttribute<? extends Setting>>();
    }
    
    public boolean hasParent() {
        return !this.parents.isEmpty();
    }
    
    public List<ParentAttribute<? extends Setting>> getParents() {
        return this.parents;
    }
    
    public void setParents(final List<ParentAttribute<? extends Setting>> parents) {
        this.parents = parents;
    }
    
    public void addParent(final ParentAttribute<? extends Setting> parent) {
        this.parents.add(parent);
    }
    
    public <T extends Setting> void addParent(final T parent, final Predicate<T> condition) {
        this.addParent(new ParentAttribute<Setting>((Setting)parent, (Predicate<? extends Setting>)condition));
    }
    
    public static <T extends Setting> void addParent(final T parent, final Predicate<T> condition, final Setting... settings) {
        Arrays.asList(settings).forEach(s -> s.addParent(new ParentAttribute<Setting>((Setting)parent, (Predicate<? extends Setting>)condition)));
    }
    
    public boolean cannotBeShown() {
        return this.hasParent() && this.getParents().stream().noneMatch(ParentAttribute::isValid);
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public String getName() {
        return this.name;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public <T extends Setting> void addJSParent(final T parent, final JSObject scriptFunction) {
        Predicate<T> predicate;
        try {
            predicate = (object -> (boolean)scriptFunction.call(null, object));
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to create predicate for parent");
        }
        this.addParent(new ParentAttribute<Setting>((Setting)parent, (Predicate<? extends Setting>)predicate));
    }
    
    public abstract <T> T getConfigValue();
}
