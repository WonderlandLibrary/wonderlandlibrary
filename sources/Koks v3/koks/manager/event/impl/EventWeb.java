package koks.manager.event.impl;

import koks.manager.event.Event;

/**
 * @author deleteboys | lmao | kroko
 * @created on 13.09.2020 : 05:38
 */
public class EventWeb extends Event {
    boolean isWeb;

    public boolean isWeb() {
        return isWeb;
    }

    public void setWeb(boolean web) {
        isWeb = web;
    }

    public EventWeb(boolean isWeb) {
        this.isWeb = isWeb;
    }
}
