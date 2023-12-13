package org.alphacentauri.management.tasks;

public interface Task {
   void exec();

   boolean canExec();
}
