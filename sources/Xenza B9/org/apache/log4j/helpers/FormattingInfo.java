// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.helpers;

public class FormattingInfo
{
    int min;
    int max;
    boolean leftAlign;
    
    public FormattingInfo() {
        this.min = -1;
        this.max = Integer.MAX_VALUE;
        this.leftAlign = false;
    }
    
    void dump() {
        LogLog.debug("min=" + this.min + ", max=" + this.max + ", leftAlign=" + this.leftAlign);
    }
    
    void reset() {
        this.min = -1;
        this.max = Integer.MAX_VALUE;
        this.leftAlign = false;
    }
}
