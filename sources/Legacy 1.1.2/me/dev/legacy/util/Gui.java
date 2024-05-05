package me.dev.legacy.util;

import javax.swing.*;

public class Gui extends JFrame {
    public RefreshLog thread;

    static class RefreshLog extends Thread {
        public Gui INSTANCE;
        public boolean running;

        public RefreshLog(Gui instance) {
            INSTANCE = instance;
            running = true;
        }
    }

    public void setThead(RefreshLog thread){
        this.thread = thread;
    }

}
