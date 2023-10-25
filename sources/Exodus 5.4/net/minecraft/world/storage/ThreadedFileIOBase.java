/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.world.storage;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import net.minecraft.world.storage.IThreadedFileIO;

public class ThreadedFileIOBase
implements Runnable {
    private static final ThreadedFileIOBase threadedIOInstance = new ThreadedFileIOBase();
    private List<IThreadedFileIO> threadedIOQueue = Collections.synchronizedList(Lists.newArrayList());
    private volatile long savedIOCounter;
    private volatile long writeQueuedCounter;
    private volatile boolean isThreadWaiting;

    @Override
    public void run() {
        while (true) {
            this.processQueue();
        }
    }

    private void processQueue() {
        int n = 0;
        while (n < this.threadedIOQueue.size()) {
            IThreadedFileIO iThreadedFileIO = this.threadedIOQueue.get(n);
            boolean bl = iThreadedFileIO.writeNextIO();
            if (!bl) {
                this.threadedIOQueue.remove(n--);
                ++this.savedIOCounter;
            }
            try {
                Thread.sleep(this.isThreadWaiting ? 0L : 10L);
            }
            catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            ++n;
        }
        if (this.threadedIOQueue.isEmpty()) {
            try {
                Thread.sleep(25L);
            }
            catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }
    }

    public static ThreadedFileIOBase getThreadedIOInstance() {
        return threadedIOInstance;
    }

    public void queueIO(IThreadedFileIO iThreadedFileIO) {
        if (!this.threadedIOQueue.contains(iThreadedFileIO)) {
            ++this.writeQueuedCounter;
            this.threadedIOQueue.add(iThreadedFileIO);
        }
    }

    public void waitForFinish() throws InterruptedException {
        this.isThreadWaiting = true;
        while (this.writeQueuedCounter != this.savedIOCounter) {
            Thread.sleep(10L);
        }
        this.isThreadWaiting = false;
    }

    private ThreadedFileIOBase() {
        Thread thread = new Thread((Runnable)this, "File IO Thread");
        thread.setPriority(1);
        thread.start();
    }
}

