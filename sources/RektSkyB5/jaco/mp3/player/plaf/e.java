/*
 * Decompiled with CFR 0.152.
 */
package jaco.mp3.player.plaf;

import jaco.a.a;
import jaco.mp3.player.c;
import jaco.mp3.player.plaf.MP3PlayerUICompact;
import java.awt.image.BufferedImage;

final class e
implements c {
    private /* synthetic */ MP3PlayerUICompact a;
    private final /* synthetic */ BufferedImage b;
    private final /* synthetic */ BufferedImage c;
    private final /* synthetic */ BufferedImage d;
    private final /* synthetic */ BufferedImage e;
    private final /* synthetic */ BufferedImage f;
    private final /* synthetic */ BufferedImage g;

    e(MP3PlayerUICompact mP3PlayerUICompact, BufferedImage bufferedImage, BufferedImage bufferedImage2, BufferedImage bufferedImage3, BufferedImage bufferedImage4, BufferedImage bufferedImage5, BufferedImage bufferedImage6) {
        this.a = mP3PlayerUICompact;
        this.b = bufferedImage;
        this.c = bufferedImage2;
        this.d = bufferedImage3;
        this.e = bufferedImage4;
        this.f = bufferedImage5;
        this.g = bufferedImage6;
    }

    @Override
    public final void a() {
        MP3PlayerUICompact.a(this.a).setIcon(jaco.a.a.a(this.b));
        MP3PlayerUICompact.a(this.a).setRolloverIcon(jaco.a.a.a(this.c));
        MP3PlayerUICompact.a(this.a).setPressedIcon(jaco.a.a.a(this.d));
    }

    @Override
    public final void b() {
        MP3PlayerUICompact.a(this.a).setIcon(jaco.a.a.a(this.e));
        MP3PlayerUICompact.a(this.a).setRolloverIcon(jaco.a.a.a(this.f));
        MP3PlayerUICompact.a(this.a).setPressedIcon(jaco.a.a.a(this.g));
    }
}

