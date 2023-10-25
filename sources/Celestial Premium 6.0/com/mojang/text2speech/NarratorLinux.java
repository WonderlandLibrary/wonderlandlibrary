/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.text2speech;

import com.google.common.collect.Queues;
import com.mojang.text2speech.Narrator;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import java.util.Queue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NarratorLinux
implements Narrator {
    private static boolean libraryFound = false;
    private static final Logger LOGGER = LogManager.getLogger();
    private final NarratorThread narratorThread = new NarratorThread();

    public NarratorLinux() {
        Thread thread = new Thread(this.narratorThread);
        thread.setDaemon(true);
        thread.setName("Narrator");
        thread.start();
    }

    @Override
    public void say(String msg) {
        this.narratorThread.add(msg);
    }

    @Override
    public void clear() {
        this.narratorThread.clear();
    }

    @Override
    public boolean active() {
        return libraryFound;
    }

    static {
        try {
            Native.register(FliteLibrary.class, NativeLibrary.getInstance("fliteWrapper"));
            FliteLibrary.init();
            libraryFound = true;
            LOGGER.info("Narrator library successfully loaded");
        }
        catch (UnsatisfiedLinkError e) {
            LOGGER.warn("ERROR : Couldn't load Narrator library : " + e.getMessage());
        }
        catch (Throwable e) {
            LOGGER.warn("ERROR : Generic error while loading narrator : " + e.getMessage());
        }
    }

    private static class NarratorThread
    implements Runnable {
        protected final Queue<String> msgs = Queues.newConcurrentLinkedQueue();

        private NarratorThread() {
        }

        @Override
        public void run() {
            while (true) {
                if (this.msgs.peek() != null) {
                    this.say(this.msgs.poll());
                }
                try {
                    Thread.sleep(100L);
                    continue;
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                    continue;
                }
                break;
            }
        }

        public void add(String msg) {
            this.msgs.add(msg);
        }

        public void clear() {
            this.msgs.clear();
        }

        private void say(String text) {
            if (libraryFound) {
                FliteLibrary.say(text.replaceAll("[<>]", ""));
            }
        }
    }

    private static class FliteLibrary {
        private FliteLibrary() {
        }

        public static native int init();

        public static native float say(String var0);
    }
}

