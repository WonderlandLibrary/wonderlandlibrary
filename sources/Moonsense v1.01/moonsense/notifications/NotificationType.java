// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.notifications;

public enum NotificationType
{
    INFO("INFO", 0, "info"), 
    WARNING("WARNING", 1, "warning"), 
    DEBUG("DEBUG", 2, "debug");
    
    public String type;
    
    private NotificationType(final String name, final int ordinal, final String type) {
        this.type = type;
    }
}
