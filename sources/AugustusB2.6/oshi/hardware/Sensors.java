// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware;

import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public interface Sensors
{
    double getCpuTemperature();
    
    int[] getFanSpeeds();
    
    double getCpuVoltage();
}
