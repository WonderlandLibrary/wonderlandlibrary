package net.minecraft.entity;

import java.util.concurrent.Callable;

class Entity.3
implements Callable<String> {
    Entity.3() {
    }

    public String call() throws Exception {
        return Entity.this.riddenByEntity.toString();
    }
}
