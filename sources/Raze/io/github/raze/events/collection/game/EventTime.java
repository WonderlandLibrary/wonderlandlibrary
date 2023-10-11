package io.github.raze.events.collection.game;

import io.github.raze.events.system.Event;

public class EventTime extends Event {

    public EventTime(long balance) { this.balance = balance; }

    public long getBalance() { return balance; }

    public void setBalance(long balance) { this.balance = balance; }

    public void decrementBalance(long amount) { balance -= amount; }

    private long balance = 0;
}