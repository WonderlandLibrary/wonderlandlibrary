// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.event;

import java.util.Iterator;
import java.util.ArrayList;

public class SCEvent
{
    public SCEvent call() {
        final ArrayList<EventData> dataList = EventBus.get(this.getClass());
        if (dataList != null) {
            for (final EventData data : dataList) {
                try {
                    data.target.invoke(data.source, this);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return this;
    }
}
