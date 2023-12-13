package io.github.nevalackin.client.util.player;

public abstract class WindowClickRequest {
    private boolean completed;

    public abstract void performRequest();

    public boolean isCompleted() {
        return this.completed;
    }

    public void onCompleted() {
        this.completed = true;
    }
}
