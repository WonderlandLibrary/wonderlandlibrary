/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.iio;

public class ImageMetadata {
    public final Float gamma;
    public final Boolean blackIsZero;
    public final Integer backgroundIndex;
    public final Integer backgroundColor;
    public final Integer delayTime;
    public final Integer loopCount;
    public final Integer transparentIndex;
    public final Integer imageWidth;
    public final Integer imageHeight;
    public final Integer imageLeftPosition;
    public final Integer imageTopPosition;
    public final Integer disposalMethod;

    public ImageMetadata(Float f, Boolean bl, Integer n, Integer n2, Integer n3, Integer n4, Integer n5, Integer n6, Integer n7, Integer n8, Integer n9, Integer n10) {
        this.gamma = f;
        this.blackIsZero = bl;
        this.backgroundIndex = n;
        this.backgroundColor = n2;
        this.transparentIndex = n3;
        this.delayTime = n4;
        this.loopCount = n5;
        this.imageWidth = n6;
        this.imageHeight = n7;
        this.imageLeftPosition = n8;
        this.imageTopPosition = n9;
        this.disposalMethod = n10;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("[" + this.getClass().getName());
        if (this.gamma != null) {
            stringBuffer.append(" gamma: " + this.gamma);
        }
        if (this.blackIsZero != null) {
            stringBuffer.append(" blackIsZero: " + this.blackIsZero);
        }
        if (this.backgroundIndex != null) {
            stringBuffer.append(" backgroundIndex: " + this.backgroundIndex);
        }
        if (this.backgroundColor != null) {
            stringBuffer.append(" backgroundColor: " + this.backgroundColor);
        }
        if (this.delayTime != null) {
            stringBuffer.append(" delayTime: " + this.delayTime);
        }
        if (this.loopCount != null) {
            stringBuffer.append(" loopCount: " + this.loopCount);
        }
        if (this.transparentIndex != null) {
            stringBuffer.append(" transparentIndex: " + this.transparentIndex);
        }
        if (this.imageWidth != null) {
            stringBuffer.append(" imageWidth: " + this.imageWidth);
        }
        if (this.imageHeight != null) {
            stringBuffer.append(" imageHeight: " + this.imageHeight);
        }
        if (this.imageLeftPosition != null) {
            stringBuffer.append(" imageLeftPosition: " + this.imageLeftPosition);
        }
        if (this.imageTopPosition != null) {
            stringBuffer.append(" imageTopPosition: " + this.imageTopPosition);
        }
        if (this.disposalMethod != null) {
            stringBuffer.append(" disposalMethod: " + this.disposalMethod);
        }
        stringBuffer.append("]");
        return stringBuffer.toString();
    }
}

