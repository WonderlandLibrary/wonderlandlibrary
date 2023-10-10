// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.net;

import org.apache.logging.log4j.Level;

public class Priority
{
    private final Facility facility;
    private final Severity severity;
    
    public Priority(final Facility facility, final Severity severity) {
        this.facility = facility;
        this.severity = severity;
    }
    
    public static int getPriority(final Facility facility, final Level level) {
        return toPriority(facility, Severity.getSeverity(level));
    }
    
    private static int toPriority(final Facility aFacility, final Severity aSeverity) {
        return (aFacility.getCode() << 3) + aSeverity.getCode();
    }
    
    public Facility getFacility() {
        return this.facility;
    }
    
    public Severity getSeverity() {
        return this.severity;
    }
    
    public int getValue() {
        return toPriority(this.facility, this.severity);
    }
    
    @Override
    public String toString() {
        return Integer.toString(this.getValue());
    }
}
