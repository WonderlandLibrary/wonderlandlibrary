package net.minecraft.entity;

import java.util.concurrent.Callable;

class Entity.2
implements Callable<String> {
    Entity.2() {
    }

    public String call() throws Exception {
        return Entity.this.getName();
    }
}
