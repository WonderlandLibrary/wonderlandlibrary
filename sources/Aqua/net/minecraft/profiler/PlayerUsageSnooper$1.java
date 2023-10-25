package net.minecraft.profiler;

import com.google.common.collect.Maps;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import net.minecraft.profiler.PlayerUsageSnooper;
import net.minecraft.util.HttpUtil;

/*
 * Exception performing whole class analysis ignored.
 */
class PlayerUsageSnooper.1
extends TimerTask {
    PlayerUsageSnooper.1() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void run() {
        if (PlayerUsageSnooper.access$000((PlayerUsageSnooper)PlayerUsageSnooper.this).isSnooperEnabled()) {
            HashMap map;
            Object object = PlayerUsageSnooper.access$100((PlayerUsageSnooper)PlayerUsageSnooper.this);
            synchronized (object) {
                map = Maps.newHashMap((Map)PlayerUsageSnooper.access$200((PlayerUsageSnooper)PlayerUsageSnooper.this));
                if (PlayerUsageSnooper.access$300((PlayerUsageSnooper)PlayerUsageSnooper.this) == 0) {
                    map.putAll(PlayerUsageSnooper.access$400((PlayerUsageSnooper)PlayerUsageSnooper.this));
                }
                map.put((Object)"snooper_count", (Object)PlayerUsageSnooper.access$308((PlayerUsageSnooper)PlayerUsageSnooper.this));
                map.put((Object)"snooper_token", (Object)PlayerUsageSnooper.access$500((PlayerUsageSnooper)PlayerUsageSnooper.this));
            }
            HttpUtil.postMap((URL)PlayerUsageSnooper.access$600((PlayerUsageSnooper)PlayerUsageSnooper.this), (Map)map, (boolean)true);
        }
    }
}
