/*
 * Decompiled with CFR 0.152.
 */
package a.a.a;

import a.a.a.d;
import a.a.a.e;
import java.awt.image.BufferedImage;

public final class b
extends d {
    private float g = 0.5f;

    public b() {
        this.e = 2.0f;
    }

    public final void a(float f2) {
        this.g = f2;
    }

    @Override
    public final BufferedImage filter(BufferedImage bufferedImage, BufferedImage bufferedImage2) {
        int n2 = bufferedImage.getWidth();
        int n3 = bufferedImage.getHeight();
        if (bufferedImage2 == null) {
            bufferedImage2 = this.createCompatibleDestImage(bufferedImage, null);
        }
        int[] nArray = new int[n2 * n3];
        int[] nArray2 = new int[n2 * n3];
        bufferedImage.getRGB(0, 0, n2, n3, nArray, 0, n2);
        if (this.e > 0.0f) {
            a.a.a.b.a(this.f, nArray, nArray2, n2, n3, this.c, this.c && this.d, false, a);
            a.a.a.b.a(this.f, nArray2, nArray, n3, n2, this.c, false, this.c && this.d, a);
        }
        bufferedImage.getRGB(0, 0, n2, n3, nArray2, 0, n2);
        float f2 = 4.0f * this.g;
        int n4 = 0;
        int n5 = 0;
        while (n5 < n3) {
            int n6 = 0;
            while (n6 < n2) {
                int n7 = nArray2[n4];
                int n8 = n7 >> 16 & 0xFF;
                int n9 = n7 >> 8 & 0xFF;
                int n10 = n7 & 0xFF;
                int n11 = nArray[n4];
                int n12 = n11 >> 16 & 0xFF;
                int n13 = n11 >> 8 & 0xFF;
                n8 = a.a.a.e.a((int)((float)n8 + f2 * (float)n12));
                n9 = a.a.a.e.a((int)((float)n9 + f2 * (float)n13));
                n10 = a.a.a.e.a((int)((float)n10 + f2 * (float)(n11 &= 0xFF)));
                nArray[n4] = n7 & 0xFF000000 | n8 << 16 | n9 << 8 | n10;
                ++n4;
                ++n6;
            }
            ++n5;
        }
        bufferedImage2.setRGB(0, 0, n2, n3, nArray, 0, n2);
        return bufferedImage2;
    }

    @Override
    public final String toString() {
        return "Blur/Glow...";
    }
}

