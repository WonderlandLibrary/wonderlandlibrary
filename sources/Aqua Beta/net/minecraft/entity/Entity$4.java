package net.minecraft.entity;

import java.util.concurrent.Callable;

class Entity.4
implements Callable<String> {
    Entity.4() {
    }

    public String call() throws Exception {
        return Entity.this.ridingEntity.toString();
    }
}
