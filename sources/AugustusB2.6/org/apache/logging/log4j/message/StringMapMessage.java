// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.message;

import java.util.Map;
import org.apache.logging.log4j.util.PerformanceSensitive;

@AsynchronouslyFormattable
@PerformanceSensitive({ "allocation" })
public class StringMapMessage extends MapMessage<StringMapMessage, String>
{
    private static final long serialVersionUID = 1L;
    
    public StringMapMessage() {
    }
    
    public StringMapMessage(final int initialCapacity) {
        super(initialCapacity);
    }
    
    public StringMapMessage(final Map<String, String> map) {
        super(map);
    }
    
    @Override
    public StringMapMessage newInstance(final Map<String, String> map) {
        return new StringMapMessage(map);
    }
}
