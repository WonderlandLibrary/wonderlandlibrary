// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.unix.openbsd;

import java.util.Iterator;
import java.util.List;
import oshi.util.ParseUtil;
import oshi.util.ExecutingCommand;
import java.util.ArrayList;
import oshi.util.Memoizer;
import oshi.util.tuples.Triplet;
import java.util.function.Supplier;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.common.AbstractSensors;

@ThreadSafe
final class OpenBsdSensors extends AbstractSensors
{
    private final Supplier<Triplet<Double, int[], Double>> tempFanVolts;
    
    OpenBsdSensors() {
        this.tempFanVolts = Memoizer.memoize(OpenBsdSensors::querySensors, Memoizer.defaultExpiration());
    }
    
    public double queryCpuTemperature() {
        return this.tempFanVolts.get().getA();
    }
    
    public int[] queryFanSpeeds() {
        return this.tempFanVolts.get().getB();
    }
    
    public double queryCpuVoltage() {
        return this.tempFanVolts.get().getC();
    }
    
    private static Triplet<Double, int[], Double> querySensors() {
        double volts = 0.0;
        final List<Double> cpuTemps = new ArrayList<Double>();
        final List<Double> allTemps = new ArrayList<Double>();
        final List<Integer> fanRPMs = new ArrayList<Integer>();
        for (final String line : ExecutingCommand.runNative("systat -ab sensors")) {
            final String[] split = ParseUtil.whitespaces.split(line);
            if (split.length > 1) {
                if (split[0].contains("cpu")) {
                    if (split[0].contains("temp0")) {
                        cpuTemps.add(ParseUtil.parseDoubleOrDefault(split[1], Double.NaN));
                    }
                    else {
                        if (!split[0].contains("volt0")) {
                            continue;
                        }
                        volts = ParseUtil.parseDoubleOrDefault(split[1], 0.0);
                    }
                }
                else if (split[0].contains("temp0")) {
                    allTemps.add(ParseUtil.parseDoubleOrDefault(split[1], Double.NaN));
                }
                else {
                    if (!split[0].contains("fan")) {
                        continue;
                    }
                    fanRPMs.add(ParseUtil.parseIntOrDefault(split[1], 0));
                }
            }
        }
        final double temp = cpuTemps.isEmpty() ? listAverage(allTemps) : listAverage(cpuTemps);
        final int[] fans = new int[fanRPMs.size()];
        for (int i = 0; i < fans.length; ++i) {
            fans[i] = fanRPMs.get(i);
        }
        return new Triplet<Double, int[], Double>(temp, fans, volts);
    }
    
    private static double listAverage(final List<Double> doubles) {
        double sum = 0.0;
        int count = 0;
        for (final Double d : doubles) {
            if (!d.isNaN()) {
                sum += d;
                ++count;
            }
        }
        return (count > 0) ? (sum / count) : 0.0;
    }
}
