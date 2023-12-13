/*
 * Decompiled with CFR 0.152.
 */
package jaco.mp3.player;

import jaco.mp3.a.D;
import jaco.mp3.a.a_0;
import jaco.mp3.a.f_0;
import jaco.mp3.a.z;
import jaco.mp3.player.MP3Player;
import jaco.mp3.player.a;
import java.net.URL;

final class b
extends Thread {
    final /* synthetic */ MP3Player a;

    b(MP3Player mP3Player) {
        this.a = mP3Player;
    }

    @Override
    public final void run() {
        z z2;
        jaco.mp3.a.b b2 = new jaco.mp3.a.b();
        try {
            z2 = new z(((URL)MP3Player.a(this.a).get(MP3Player.b(this.a))).openStream());
        }
        catch (Exception exception) {
            z2 = null;
            exception.printStackTrace();
        }
        if (z2 != null) {
            Object object;
            f_0 f_02;
            try {
                f_02 = new f_0();
                f_02.a(b2);
            }
            catch (Exception exception) {
                f_02 = null;
                try {
                    z2.a();
                }
                catch (Exception exception2) {}
                exception.printStackTrace();
            }
            if (f_02 != null) {
                try {
                    while (!MP3Player.c(this.a)) {
                        if (MP3Player.d(this.a)) {
                            Thread.sleep(100L);
                            continue;
                        }
                        object = z2.b();
                        if (object != null) {
                            object = (a_0)b2.a((D)object, z2);
                            f_02.a(((a_0)object).a(), 0, ((a_0)object).b());
                            z2.d();
                            continue;
                        }
                        break;
                    }
                }
                catch (Exception exception) {
                    object = exception;
                    exception.printStackTrace();
                }
                if (!MP3Player.c(this.a)) {
                    f_02.c();
                }
                f_02.a();
            }
            try {
                z2.a();
            }
            catch (Exception exception) {
                object = exception;
                exception.printStackTrace();
            }
        }
        if (!MP3Player.c(this.a)) {
            new a(this).start();
        }
        MP3Player.a(this.a, false);
        MP3Player.b(this.a, true);
    }
}

