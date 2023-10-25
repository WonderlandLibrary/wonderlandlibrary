/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.text2speech;

import com.google.common.collect.Queues;
import com.mojang.text2speech.Narrator;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.WString;
import java.util.Queue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NarratorWindows
implements Narrator {
    private static boolean libraryFound = false;
    private static final Logger LOGGER = LogManager.getLogger();
    private final NarratorThread narratorThread = new NarratorThread();

    public NarratorWindows() {
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
        String result = "";
        try {
            Native.register(SAPIWrapperSolutionDLL.class, NativeLibrary.getInstance("SAPIWrapper_x64"));
            libraryFound = true;
            LOGGER.info("Narrator library for x64 successfully loaded");
        }
        catch (UnsatisfiedLinkError e) {
            result = result + "ERROR : Couldn't load Narrator library : " + e.getMessage() + "\n";
        }
        catch (Throwable e) {
            result = result + "ERROR : Generic error while loading narrator : " + e.getMessage() + "\n";
        }
        if (!libraryFound) {
            try {
                Native.register(SAPIWrapperSolutionDLL.class, NativeLibrary.getInstance("SAPIWrapper_x86"));
                libraryFound = true;
                LOGGER.info("Narrator library for x86 successfully loaded");
            }
            catch (UnsatisfiedLinkError e) {
                result = result + "ERROR : Couldn't load Narrator library : " + e.getMessage() + "\n";
            }
            catch (Throwable e) {
                result = result + "ERROR : Generic error while loading narrator : " + e.getMessage() + "\n";
            }
        }
        if (!libraryFound) {
            LOGGER.warn(result);
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
            SAPIWrapperSolutionDLL.say(new WString(text.replaceAll("[<>]", "")));
        }
    }

    private static class SAPIWrapperSolutionDLL {
        private SAPIWrapperSolutionDLL() {
        }

        public static native long say(WString var0);
    }
}

