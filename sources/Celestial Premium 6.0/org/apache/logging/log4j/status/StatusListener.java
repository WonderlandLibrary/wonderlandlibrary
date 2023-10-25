/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.status;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.status.StatusData;

public interface StatusListener {
    public void log(StatusData var1);

    public Level getStatusLevel();
}

