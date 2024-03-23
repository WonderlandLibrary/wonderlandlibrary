package dev.zprestige.prestige.client.protection.check;

public abstract class Check {

    public Category category;

    public Category getCategory() {
        return this.category;
    }

    public abstract void run();

    public Check(Category category) {
        this.category = category;
    }
}
