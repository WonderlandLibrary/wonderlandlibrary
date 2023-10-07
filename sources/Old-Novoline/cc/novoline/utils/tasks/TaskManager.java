package cc.novoline.utils.tasks;

import cc.novoline.events.EventManager;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class TaskManager {

    /* fields */
    private final List<FutureTask> futureTasks = new CopyOnWriteArrayList<>();

    /* constructors */
    public void queue(FutureTask task) {
        futureTasks.add(task);
        EventManager.register(this);
    }

    /* methods */
    //region Lombok
    public List<FutureTask> getFutureTasks() {
        return this.futureTasks;
    }
    //endregion

}
