package me.dev.legacy.util;

import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * Created by Gebruiker on 18/04/2017.
 */

public class ColorHolder {
    int r;
    int g;
    int b;
    int a;

    public ColorHolder(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = 255;
    }

    public ColorHolder(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public ColorHolder brighter(){
        return new ColorHolder(Math.min(r + 10, 255), Math.min(g + 10, 255), Math.min(b + 10, 255), getA());
    }

    public ColorHolder darker(){
        return new ColorHolder(Math.max(r - 10, 0), Math.max(g - 10, 0), Math.max(b - 10, 0), getA());
    }

    public void setGLColour(){
        setGLColour(-1,-1,-1,-1);
    }

    public void setGLColour(int dr, int dg, int db, int da){
        GL11.glColor4f((float) (dr == -1 ? r : dr) / 255,(float) (dg == -1 ? g : dg) / 255,(float) (db == -1 ? b : db) / 255,(float) (da == -1 ? a : da) / 255);
    }

    public void becomeGLColour(){
//        FloatBuffer buffer = FloatBuffer.allocate(16);
//        GL11.glGetFloat(GL11.GL_CURRENT_COLOR, buffer);
//        becomeHex((int) buffer.get());
    }

    public void becomeHex(int hex){
        setR((hex & 0xFF0000) >> 16);
        setG((hex & 0xFF00) >> 8);
        setB((hex & 0xFF));
        setA(255);
    }

    public static ColorHolder fromHex(int hex){
        ColorHolder n = new ColorHolder(0,0,0);
        n.becomeHex(hex);
        return n;
    }

    public static int toHex(int r, int g, int b){
        return  (0xff << 24) | ((r&0xff) << 16) | ((g&0xff) << 8) | (b&0xff);
    }

    public int toHex(){
        return toHex(r, g, b);
    }

    public int getB() {
        return b;
    }

    public int getG() {
        return g;
    }

    public int getR() {
        return r;
    }

    public int getA() {
        return a;
    }

    public ColorHolder setR(int r) {
        this.r = r;
        return this;
    }

    public ColorHolder setB(int b) {
        this.b = b;
        return this;
    }

    public ColorHolder setG(int g) {
        this.g = g;
        return this;
    }

    public ColorHolder setA(int a) {
        this.a = a;
        return this;
    }

    @Override
    public ColorHolder clone()  {
        return new ColorHolder(r, g, b, a);
    }

    public Color toJavaColour() {
        return new Color(r, g, b, a);
    }
}