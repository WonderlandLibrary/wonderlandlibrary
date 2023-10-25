/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism;

import com.sun.prism.GraphicsResource;
import com.sun.prism.Image;
import com.sun.prism.MediaFrame;
import com.sun.prism.PixelFormat;
import java.nio.Buffer;

public interface Texture
extends GraphicsResource {
    public PixelFormat getPixelFormat();

    public int getPhysicalWidth();

    public int getPhysicalHeight();

    public int getContentX();

    public int getContentY();

    public int getContentWidth();

    public int getContentHeight();

    public int getMaxContentWidth();

    public int getMaxContentHeight();

    public void setContentWidth(int var1);

    public void setContentHeight(int var1);

    public int getLastImageSerial();

    public void setLastImageSerial(int var1);

    public void update(Image var1);

    public void update(Image var1, int var2, int var3);

    public void update(Image var1, int var2, int var3, int var4, int var5);

    public void update(Image var1, int var2, int var3, int var4, int var5, boolean var6);

    public void update(Buffer var1, PixelFormat var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, boolean var10);

    public void update(MediaFrame var1, boolean var2);

    public WrapMode getWrapMode();

    public boolean getUseMipmap();

    public Texture getSharedTexture(WrapMode var1);

    public boolean getLinearFiltering();

    public void setLinearFiltering(boolean var1);

    public void lock();

    public void unlock();

    public boolean isLocked();

    public int getLockCount();

    public void assertLocked();

    public void makePermanent();

    public void contentsUseful();

    public void contentsNotUseful();

    public boolean isSurfaceLost();

    public static final class WrapMode
    extends Enum<WrapMode> {
        public static final /* enum */ WrapMode CLAMP_NOT_NEEDED = new WrapMode();
        public static final /* enum */ WrapMode CLAMP_TO_ZERO = new WrapMode();
        public static final /* enum */ WrapMode CLAMP_TO_EDGE = new WrapMode();
        public static final /* enum */ WrapMode REPEAT = new WrapMode();
        public static final /* enum */ WrapMode CLAMP_TO_ZERO_SIMULATED = new WrapMode(CLAMP_TO_ZERO);
        public static final /* enum */ WrapMode CLAMP_TO_EDGE_SIMULATED = new WrapMode(CLAMP_TO_EDGE);
        public static final /* enum */ WrapMode REPEAT_SIMULATED = new WrapMode(REPEAT);
        private WrapMode simulates;
        private WrapMode simulatedBy;
        private static final /* synthetic */ WrapMode[] $VALUES;

        public static WrapMode[] values() {
            return (WrapMode[])$VALUES.clone();
        }

        public static WrapMode valueOf(String string) {
            return Enum.valueOf(WrapMode.class, string);
        }

        private WrapMode(WrapMode wrapMode) {
            this.simulates = wrapMode;
            wrapMode.simulatedBy = this;
        }

        private WrapMode() {
        }

        public WrapMode simulatedVersion() {
            return this.simulatedBy;
        }

        public boolean isCompatibleWith(WrapMode wrapMode) {
            return wrapMode == this || wrapMode == this.simulates || wrapMode == CLAMP_NOT_NEEDED;
        }

        private static /* synthetic */ WrapMode[] $values() {
            return new WrapMode[]{CLAMP_NOT_NEEDED, CLAMP_TO_ZERO, CLAMP_TO_EDGE, REPEAT, CLAMP_TO_ZERO_SIMULATED, CLAMP_TO_EDGE_SIMULATED, REPEAT_SIMULATED};
        }

        static {
            $VALUES = WrapMode.$values();
        }
    }

    public static final class Usage
    extends Enum<Usage> {
        public static final /* enum */ Usage DEFAULT = new Usage();
        public static final /* enum */ Usage DYNAMIC = new Usage();
        public static final /* enum */ Usage STATIC = new Usage();
        private static final /* synthetic */ Usage[] $VALUES;

        public static Usage[] values() {
            return (Usage[])$VALUES.clone();
        }

        public static Usage valueOf(String string) {
            return Enum.valueOf(Usage.class, string);
        }

        private static /* synthetic */ Usage[] $values() {
            return new Usage[]{DEFAULT, DYNAMIC, STATIC};
        }

        static {
            $VALUES = Usage.$values();
        }
    }
}

