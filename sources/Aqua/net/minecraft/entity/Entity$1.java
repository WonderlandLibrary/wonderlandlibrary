package net.minecraft.entity;

import java.util.concurrent.Callable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;

class Entity.1
implements Callable<String> {
    Entity.1() {
    }

    public String call() throws Exception {
        return EntityList.getEntityString((Entity)Entity.this) + " (" + Entity.this.getClass().getCanonicalName() + ")";
    }
}
