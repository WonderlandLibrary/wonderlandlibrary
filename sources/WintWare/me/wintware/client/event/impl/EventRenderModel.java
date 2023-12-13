/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.event.impl;

import me.wintware.client.event.Event;
import me.wintware.client.event.types.EventType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class EventRenderModel
implements Event {
    Entity entity;
    EventType state;

    public EventRenderModel(Entity entity, EventType state) {
        this.entity = entity;
        this.state = state;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public void setEntity(EntityLivingBase entity) {
        this.entity = entity;
    }

    public EventType getState() {
        return this.state;
    }

    public void setState(EventType state) {
        this.state = state;
    }

    public static class EventColorFov
    implements Event {
        private float red;
        private float green;
        private float blue;

        public EventColorFov(EntityRenderer renderer, Entity entity, IBlockState state, double renderPartialTicks, float red, float green, float blue) {
            this.setRed(red);
            this.setGreen(green);
            this.setBlue(blue);
        }

        public float getRed() {
            return this.red;
        }

        public void setRed(float red) {
            this.red = red;
        }

        public float getGreen() {
            return this.green;
        }

        public void setGreen(float green) {
            this.green = green;
        }

        public float getBlue() {
            return this.blue;
        }

        public void setBlue(float blue) {
            this.blue = blue;
        }
    }
}

