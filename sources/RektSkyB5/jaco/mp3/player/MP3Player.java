/*
 * Decompiled with CFR 0.152.
 */
package jaco.mp3.player;

import jaco.mp3.player.b;
import jaco.mp3.player.c;
import jaco.mp3.player.plaf.MP3PlayerUI;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class MP3Player
extends JPanel {
    public static final String UI_CLASS_ID = String.valueOf(MP3Player.class.getName()) + "UI";
    private final Random a = new Random();
    private List b;
    private List c = new Vector();
    private int d;
    private boolean e = false;
    private boolean f = false;
    private volatile boolean g = false;
    private volatile boolean h = true;
    private volatile boolean i = false;

    static {
        if (UIManager.getDefaults().get(UI_CLASS_ID) == null) {
            MP3Player.setDefaultUI(MP3PlayerUI.class);
        }
    }

    @Override
    public String getUIClassID() {
        return UI_CLASS_ID;
    }

    public static void setDefaultUI(Class clazz) {
        UIManager.getDefaults().put(UI_CLASS_ID, clazz.getName());
    }

    public MP3Player() {
    }

    public MP3Player(URL ... object) {
        URL[] uRLArray = object;
        int n2 = ((URL[])object).length;
        int n3 = 0;
        while (n3 < n2) {
            object = uRLArray[n3];
            this.addToPlayList((URL)object);
            ++n3;
        }
    }

    public MP3Player(File ... object) {
        File[] fileArray = object;
        int n2 = ((File[])object).length;
        int n3 = 0;
        while (n3 < n2) {
            object = fileArray[n3];
            this.addToPlayList((File)object);
            ++n3;
        }
    }

    public synchronized void play() {
        Object object2;
        if (this.b != null) {
            for (Object object2 : this.b) {
                object2.a();
            }
        }
        if (this.g) {
            this.g = false;
            return;
        }
        this.a();
        this.h = false;
        object2 = new b(this);
        ((Thread)object2).setDaemon(true);
        ((Thread)object2).start();
    }

    public synchronized void pause() {
        if (this.b != null) {
            for (c c2 : this.b) {
                c2.b();
            }
        }
        this.g = true;
    }

    public synchronized void stop() {
        if (this.b != null) {
            Iterator iterator = this.b.iterator();
            while (iterator.hasNext()) {
                iterator.next();
            }
        }
        this.a();
    }

    private void a() {
        this.g = false;
        this.i = true;
        while (this.i && !this.h) {
            try {
                Thread.sleep(10L);
            }
            catch (Exception exception) {}
        }
        this.i = false;
        this.h = true;
    }

    public synchronized void skipForward() {
        if (this.e) {
            this.d = this.a.nextInt(this.c.size());
            this.play();
            return;
        }
        if (this.d >= this.c.size() - 1) {
            if (this.f) {
                this.d = 0;
                this.play();
                return;
            }
        } else {
            ++this.d;
            this.play();
        }
    }

    public synchronized void skipBackward() {
        if (this.e) {
            this.d = this.a.nextInt(this.c.size());
            this.play();
            return;
        }
        if (this.d <= 0) {
            if (this.f) {
                this.d = this.c.size() - 1;
                this.play();
                return;
            }
        } else {
            --this.d;
            this.play();
        }
    }

    public boolean isPaused() {
        return this.g;
    }

    public boolean isStopped() {
        return this.h;
    }

    public synchronized void addMP3PlayerListener(c c2) {
        if (this.b == null) {
            this.b = new ArrayList();
        }
        this.b.add(c2);
    }

    public synchronized void removeMP3PlayerListener(c c2) {
        this.b.remove(c2);
    }

    public synchronized void removeAllMP3PlayerListeners() {
        this.b.clear();
    }

    public void addToPlayList(URL uRL) {
        this.c.add(uRL);
    }

    public void addToPlayList(File file) {
        try {
            this.c.add(file.toURI().toURL());
            return;
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public List getPlayList() {
        return this.c;
    }

    public boolean isShuffle() {
        return this.e;
    }

    public void setShuffle(boolean bl) {
        this.e = bl;
    }

    public boolean isRepeat() {
        return this.f;
    }

    public void setRepeat(boolean bl) {
        this.f = bl;
    }

    static /* synthetic */ List a(MP3Player mP3Player) {
        return mP3Player.c;
    }

    static /* synthetic */ int b(MP3Player mP3Player) {
        return mP3Player.d;
    }

    static /* synthetic */ boolean c(MP3Player mP3Player) {
        return mP3Player.i;
    }

    static /* synthetic */ boolean d(MP3Player mP3Player) {
        return mP3Player.g;
    }

    static /* synthetic */ void a(MP3Player mP3Player, boolean bl) {
        mP3Player.i = false;
    }

    static /* synthetic */ void b(MP3Player mP3Player, boolean bl) {
        mP3Player.h = true;
    }
}

