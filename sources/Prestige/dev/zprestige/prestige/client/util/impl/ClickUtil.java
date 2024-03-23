package dev.zprestige.prestige.client.util.impl;

//Kys zPrestige
public class ClickUtil {
    public int mode;
    public long time;
    public float idk;
    public float random;
    public boolean click;

    public ClickUtil(int n, long l, float f, float f2, boolean bl) {
        mode = n;
        time = l;
        idk = f;
        random = f2;
        click = bl;
    }

    public int getMode() {
        return mode;
    }

    public long getTime() {
        return time;
    }


    public float getIdk() {
        return idk;
    }

    public float getRandom() {
        return random;
    }

    public boolean shouldClick() {
        return click;
    }

    public void setClick(boolean bl) {
        click = bl;
    }
}
