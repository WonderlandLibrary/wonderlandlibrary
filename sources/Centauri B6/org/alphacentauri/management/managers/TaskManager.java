package org.alphacentauri.management.managers;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.alphacentauri.management.tasks.Task;

public class TaskManager {
   public ArrayList tasks = new ArrayList();

   public void add(Task task) {
      this.tasks.add(task);
   }

   public void exec() {
      ArrayList<Task> remove = new ArrayList();
      this.tasks.stream().filter(Task::canExec).forEach((task) -> {
         task.exec();
         remove.add(task);
      });
      this.tasks.removeAll(remove);
   }
}
