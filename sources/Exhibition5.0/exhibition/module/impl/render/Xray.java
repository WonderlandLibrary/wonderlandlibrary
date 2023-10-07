// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.render;

import java.util.HashMap;
import exhibition.event.impl.EventTick;
import exhibition.event.RegisterEvent;
import exhibition.event.Event;
import java.util.Iterator;
import exhibition.module.data.Setting;
import com.google.common.collect.Lists;
import exhibition.module.data.ModuleData;
import java.util.List;
import java.util.HashSet;
import exhibition.module.Module;

public class Xray extends Module
{
    private static final String KEY_OPACITY = "OPACITY";
    private static final HashSet<Integer> blockIDs;
    private int opacity;
    List<Integer> KEY_IDS;
    
    public Xray(final ModuleData data) {
        super(data);
        this.opacity = 160;
        this.KEY_IDS = (List<Integer>)Lists.newArrayList((Object[])new Integer[] { 10, 11, 8, 9, 14, 15, 16, 21, 41, 42, 46, 48, 52, 56, 57, 61, 62, 73, 74, 84, 89, 103, 116, 117, 118, 120, 129, 133, 137, 145, 152, 153, 154 });
        ((HashMap<String, Setting<Integer>>)this.settings).put("OPACITY", new Setting<Integer>("OPACITY", 160, "Opacity for blocks you want to ignore.", 5.0, 0.0, 255.0));
    }
    
    @Override
    public void onEnable() {
        Xray.blockIDs.clear();
        this.opacity = ((HashMap<K, Setting<Number>>)this.settings).get("OPACITY").getValue().intValue();
        try {
            for (final Integer o : this.KEY_IDS) {
                Xray.blockIDs.add(o);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        Xray.mc.renderGlobal.loadRenderers();
    }
    
    @Override
    public void onDisable() {
        Xray.mc.renderGlobal.loadRenderers();
    }
    
    @RegisterEvent(events = { EventTick.class })
    @Override
    public void onEvent(final Event event) {
    }
    
    public boolean containsID(final int id) {
        return Xray.blockIDs.contains(id);
    }
    
    public int getOpacity() {
        return this.opacity;
    }
    
    static {
        blockIDs = new HashSet<Integer>();
    }
}
