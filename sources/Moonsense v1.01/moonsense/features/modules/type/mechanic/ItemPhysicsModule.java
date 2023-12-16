// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.mechanic;

import moonsense.enums.ModuleCategory;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.WeakHashMap;
import moonsense.settings.Setting;
import net.minecraft.entity.item.EntityItem;
import java.util.Map;
import moonsense.features.SCModule;

public class ItemPhysicsModule extends SCModule
{
    public static ItemPhysicsModule INSTANCE;
    private Map<EntityItem, ItemData> dataMap;
    public final Setting rotationSpeed;
    public final Setting stackDroppedItems;
    
    public ItemPhysicsModule() {
        super("Item Physics", "Adds physics to thrown items.");
        this.dataMap = new WeakHashMap<EntityItem, ItemData>();
        ItemPhysicsModule.INSTANCE = this;
        this.rotationSpeed = new Setting(this, "Rotation Speed").setDefault(0.5f).setRange(0.1f, 4.0f, 0.1f);
        this.stackDroppedItems = new Setting(this, "Stack Dropped Items").setDefault(false);
    }
    
    public void onUpdate() {
        if (this.mc.theWorld != null) {
            final ArrayList<EntityItem> toRemove = new ArrayList<EntityItem>();
            for (final EntityItem item : this.dataMap.keySet()) {
                if (!this.mc.theWorld.loadedEntityList.contains(item)) {
                    toRemove.add(item);
                }
            }
            for (final EntityItem item : toRemove) {
                this.dataMap.remove(item);
            }
        }
    }
    
    public Map<EntityItem, ItemData> getItemDataMap() {
        return this.dataMap;
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.MECHANIC;
    }
    
    public static class ItemData
    {
        public long lastUpdate;
        public float rotation;
        
        public ItemData(final long lastUpdate) {
            this.lastUpdate = lastUpdate;
        }
    }
}
