/*
 * Decompiled with CFR 0.152.
 */
package jaco.mp3.a;

final class h {
    private int a = 0;
    private int b = 0;
    private int c = 0;
    private final int[] d = new int[32768];

    h() {
    }

    public final int a() {
        return this.b;
    }

    /*
     * Unable to fully structure code
     */
    public final int a(int var1_1) {
        block2: {
            this.b += var1_1;
            var2_2 = 0;
            var3_3 = this.c;
            if (var3_3 + var1_1 >= 32768) ** GOTO lbl13
            while (var1_1-- > 0) {
                var2_2 <<= 1;
                var2_2 |= this.d[var3_3++] != 0 ? 1 : 0;
            }
            break block2;
lbl-1000:
            // 1 sources

            {
                var2_2 <<= 1;
                var2_2 |= this.d[var3_3] != 0 ? 1 : 0;
                var3_3 = var3_3 + 1 & 32767;
lbl13:
                // 2 sources

                ** while (var1_1-- > 0)
            }
        }
        this.c = var3_3;
        return var2_2;
    }

    public final int b() {
        ++this.b;
        int n2 = this.d[this.c];
        this.c = this.c + 1 & Short.MAX_VALUE;
        return n2;
    }

    public final void b(int n2) {
        int n3 = this.a;
        this.d[n3++] = n2 & 0x80;
        this.d[n3++] = n2 & 0x40;
        this.d[n3++] = n2 & 0x20;
        this.d[n3++] = n2 & 0x10;
        this.d[n3++] = n2 & 8;
        this.d[n3++] = n2 & 4;
        this.d[n3++] = n2 & 2;
        this.d[n3++] = n2 & 1;
        if (n3 == 32768) {
            this.a = 0;
            return;
        }
        this.a = n3;
    }

    public final void c(int n2) {
        this.b -= n2;
        this.c -= n2;
        if (this.c < 0) {
            this.c += 32768;
        }
    }

    public final void d(int n2) {
        n2 = 4096 << 3;
        this.b -= n2;
        this.c -= n2;
        if (this.c < 0) {
            this.c += 32768;
        }
    }
}

