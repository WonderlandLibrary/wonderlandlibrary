package xyz.northclient;

import xyz.northclient.util.shader.impl.RenderCallback;

public interface InstanceAccess {
    public NorthSingleton north = NorthSingleton.INSTANCE;
    default void addBloomTarget(RenderCallback callback) {

    }

    default void addBlurTarget(RenderCallback callback) {

    }
}
