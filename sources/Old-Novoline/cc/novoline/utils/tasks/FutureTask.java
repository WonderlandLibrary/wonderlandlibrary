package cc.novoline.utils.tasks;

public abstract class FutureTask {

    /* fields */
    private final int delay;
    private long lastTime;

    /* constructors */
    protected FutureTask(final int delay) {
        this.delay = delay;
        this.lastTime = System.nanoTime() / 1000000L;
    }

    /* methods */
    public final boolean delay() {
        return System.nanoTime() / 1000000L - this.lastTime >= this.delay;
    }

    /**
     * Выполняется, когда пройдет задержка.
     */
    public abstract void execute();


    /**
     * Выполняется снова и снова, пока задержка полностью не пройдет.
     */
    public abstract void run();

}
