/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.management.notifications;

import me.arithmo.management.notifications.Notifications;

public interface INotification {
    public String getHeader();

    public String getSubtext();

    public long getStart();

    public long getDisplayTime();

    public Notifications.Type getType();

    public float getX();

    public float getTarX();

    public float getTarY();

    public void setX(int var1);

    public void setTarX(int var1);

    public void setY(int var1);

    public long checkTime();

    public float getY();

    public long getLast();

    public void setLast(long var1);
}

