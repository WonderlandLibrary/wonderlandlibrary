// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.player;

import exhibition.event.impl.EventTick;
import exhibition.event.RegisterEvent;
import net.minecraft.util.BlockPos;
import exhibition.util.misc.BlockUtil;
import exhibition.event.Event;
import exhibition.module.data.ModuleData;
import exhibition.module.Module;

public class AutoTool extends Module
{
    public AutoTool(final ModuleData data) {
        super(data);
    }
    
    @RegisterEvent(events = { EventTick.class })
    @Override
    public void onEvent(final Event event) {
        if (!AutoTool.mc.gameSettings.keyBindAttack.getIsKeyPressed()) {
            return;
        }
        if (AutoTool.mc.objectMouseOver == null) {
            return;
        }
        final BlockPos pos = AutoTool.mc.objectMouseOver.getBlockPos();
        if (pos == null) {
            return;
        }
        BlockUtil.updateTool(pos);
    }
}
