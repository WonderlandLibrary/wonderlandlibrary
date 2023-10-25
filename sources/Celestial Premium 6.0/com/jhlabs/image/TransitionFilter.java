/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.AbstractBufferedImageOp;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

public class TransitionFilter
extends AbstractBufferedImageOp {
    private float transition = 0.0f;
    private BufferedImage destination;
    private String property;
    private Method method;
    protected BufferedImageOp filter;
    protected float minValue;
    protected float maxValue;

    private TransitionFilter() {
    }

    public TransitionFilter(BufferedImageOp filter, String property, float minValue, float maxValue) {
        this.filter = filter;
        this.property = property;
        this.minValue = minValue;
        this.maxValue = maxValue;
        try {
            BeanInfo info = Introspector.getBeanInfo(filter.getClass());
            PropertyDescriptor[] pds = info.getPropertyDescriptors();
            for (int i = 0; i < pds.length; ++i) {
                PropertyDescriptor pd = pds[i];
                if (!property.equals(pd.getName())) continue;
                this.method = pd.getWriteMethod();
                break;
            }
            if (this.method == null) {
                throw new IllegalArgumentException("No such property in object: " + property);
            }
        }
        catch (IntrospectionException e) {
            throw new IllegalArgumentException(e.toString());
        }
    }

    public void setTransition(float transition) {
        this.transition = transition;
    }

    public float getTransition() {
        return this.transition;
    }

    public void setDestination(BufferedImage destination) {
        this.destination = destination;
    }

    public BufferedImage getDestination() {
        return this.destination;
    }

    public void prepareFilter(float transition) {
        try {
            this.method.invoke(this.filter, new Float(transition));
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Error setting value for property: " + this.property);
        }
    }

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        float t;
        if (dst == null) {
            dst = this.createCompatibleDestImage(src, null);
        }
        if (this.destination == null) {
            return dst;
        }
        float itransition = 1.0f - this.transition;
        Graphics2D g = dst.createGraphics();
        if (this.transition != 1.0f) {
            t = this.minValue + this.transition * (this.maxValue - this.minValue);
            this.prepareFilter(t);
            g.drawImage(src, this.filter, 0, 0);
        }
        if (this.transition != 0.0f) {
            g.setComposite(AlphaComposite.getInstance(3, this.transition));
            t = this.minValue + itransition * (this.maxValue - this.minValue);
            this.prepareFilter(t);
            g.drawImage(this.destination, this.filter, 0, 0);
        }
        g.dispose();
        return dst;
    }

    public String toString() {
        return "Transitions/Transition...";
    }
}

