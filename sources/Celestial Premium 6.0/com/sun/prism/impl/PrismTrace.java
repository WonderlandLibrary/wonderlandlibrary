/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl;

import com.sun.prism.impl.PrismSettings;
import java.util.HashMap;
import java.util.Map;

public class PrismTrace {
    private static final boolean enabled = PrismSettings.printAllocs;
    private static Map<Long, Long> texData;
    private static long texBytes;
    private static Map<Long, Long> rttData;
    private static long rttBytes;

    private static String summary(long l, long l2, String string) {
        return String.format("%s=%d@%,dKB", string, l, l2 >> 10);
    }

    private static String summary(SummaryType summaryType) {
        switch (summaryType) {
            case TYPE_TEX: {
                return PrismTrace.summary(texData.size(), texBytes, " tex");
            }
            case TYPE_RTT: {
                return PrismTrace.summary(rttData.size(), rttBytes, " rtt");
            }
            case TYPE_ALL: {
                return PrismTrace.summary(texData.size() + rttData.size(), texBytes + rttBytes, " combined");
            }
        }
        return "ERROR";
    }

    private static long computeSize(int n, int n2, int n3) {
        long l = n;
        l *= (long)n2;
        return l *= (long)n3;
    }

    public static void textureCreated(long l, int n, int n2, long l2) {
        if (!enabled) {
            return;
        }
        texData.put(l, l2);
        texBytes += l2;
        System.out.println("Created Texture: id=" + l + ", " + n + "x" + n2 + " pixels, " + l2 + " bytes," + PrismTrace.summary(SummaryType.TYPE_TEX) + PrismTrace.summary(SummaryType.TYPE_ALL));
    }

    public static void textureCreated(long l, int n, int n2, int n3) {
        if (!enabled) {
            return;
        }
        long l2 = PrismTrace.computeSize(n, n2, n3);
        texData.put(l, l2);
        texBytes += l2;
        System.out.println("Created Texture: id=" + l + ", " + n + "x" + n2 + " pixels, " + l2 + " bytes," + PrismTrace.summary(SummaryType.TYPE_TEX) + PrismTrace.summary(SummaryType.TYPE_ALL));
    }

    public static void textureDisposed(long l) {
        if (!enabled) {
            return;
        }
        Long l2 = texData.remove(l);
        if (l2 == null) {
            throw new InternalError("Disposing unknown Texture " + l);
        }
        texBytes -= l2.longValue();
        System.out.println("Disposed Texture: id=" + l + ", " + l2 + " bytes" + PrismTrace.summary(SummaryType.TYPE_TEX) + PrismTrace.summary(SummaryType.TYPE_ALL));
    }

    public static void rttCreated(long l, int n, int n2, long l2) {
        if (!enabled) {
            return;
        }
        rttData.put(l, l2);
        rttBytes += l2;
        System.out.println("Created RTTexture: id=" + l + ", " + n + "x" + n2 + " pixels, " + l2 + " bytes," + PrismTrace.summary(SummaryType.TYPE_RTT) + PrismTrace.summary(SummaryType.TYPE_ALL));
    }

    public static void rttCreated(long l, int n, int n2, int n3) {
        if (!enabled) {
            return;
        }
        long l2 = PrismTrace.computeSize(n, n2, n3);
        rttData.put(l, l2);
        rttBytes += l2;
        System.out.println("Created RTTexture: id=" + l + ", " + n + "x" + n2 + " pixels, " + l2 + " bytes," + PrismTrace.summary(SummaryType.TYPE_RTT) + PrismTrace.summary(SummaryType.TYPE_ALL));
    }

    public static void rttDisposed(long l) {
        if (!enabled) {
            return;
        }
        Long l2 = rttData.remove(l);
        if (l2 == null) {
            throw new InternalError("Disposing unknown RTTexture " + l);
        }
        rttBytes -= l2.longValue();
        System.out.println("Disposed RTTexture: id=" + l + ", " + l2 + " bytes" + PrismTrace.summary(SummaryType.TYPE_RTT) + PrismTrace.summary(SummaryType.TYPE_ALL));
    }

    private PrismTrace() {
    }

    static {
        if (enabled) {
            texData = new HashMap<Long, Long>();
            rttData = new HashMap<Long, Long>();
            Runtime.getRuntime().addShutdownHook(new Thread("RTT printAlloc shutdown hook"){

                @Override
                public void run() {
                    System.out.println("Final Texture resources:" + PrismTrace.summary(SummaryType.TYPE_TEX) + PrismTrace.summary(SummaryType.TYPE_RTT) + PrismTrace.summary(SummaryType.TYPE_ALL));
                }
            });
        }
    }

    private static final class SummaryType
    extends Enum<SummaryType> {
        public static final /* enum */ SummaryType TYPE_TEX = new SummaryType();
        public static final /* enum */ SummaryType TYPE_RTT = new SummaryType();
        public static final /* enum */ SummaryType TYPE_ALL = new SummaryType();
        private static final /* synthetic */ SummaryType[] $VALUES;

        public static SummaryType[] values() {
            return (SummaryType[])$VALUES.clone();
        }

        public static SummaryType valueOf(String string) {
            return Enum.valueOf(SummaryType.class, string);
        }

        private static /* synthetic */ SummaryType[] $values() {
            return new SummaryType[]{TYPE_TEX, TYPE_RTT, TYPE_ALL};
        }

        static {
            $VALUES = SummaryType.$values();
        }
    }
}

