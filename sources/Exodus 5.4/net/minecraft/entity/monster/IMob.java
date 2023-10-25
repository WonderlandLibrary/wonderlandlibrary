/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 */
package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.IAnimals;

public interface IMob
extends IAnimals {
    public static final Predicate<Entity> mobSelector = new Predicate<Entity>(){

        public boolean apply(Entity entity) {
            return entity instanceof IMob;
        }
    };
    public static final Predicate<Entity> VISIBLE_MOB_SELECTOR = new Predicate<Entity>(){

        public boolean apply(Entity entity) {
            return entity instanceof IMob && !entity.isInvisible();
        }
    };
}

