// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.management.notifications;

public interface INotification
{
    String getHeader();
    
    String getSubtext();
    
    long getStart();
    
    long getDisplayTime();
    
    Notifications.Type getType();
    
    float getX();
    
    float getTarX();
    
    float getTarY();
    
    void setX(final int p0);
    
    void setTarX(final int p0);
    
    void setY(final int p0);
    
    long checkTime();
    
    float getY();
    
    long getLast();
    
    void setLast(final long p0);
}
