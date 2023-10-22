package javafx.concurrent;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;

public interface Worker {
   State getState();

   ReadOnlyObjectProperty stateProperty();

   Object getValue();

   ReadOnlyObjectProperty valueProperty();

   Throwable getException();

   ReadOnlyObjectProperty exceptionProperty();

   double getWorkDone();

   ReadOnlyDoubleProperty workDoneProperty();

   double getTotalWork();

   ReadOnlyDoubleProperty totalWorkProperty();

   double getProgress();

   ReadOnlyDoubleProperty progressProperty();

   boolean isRunning();

   ReadOnlyBooleanProperty runningProperty();

   String getMessage();

   ReadOnlyStringProperty messageProperty();

   String getTitle();

   ReadOnlyStringProperty titleProperty();

   boolean cancel();

   public static enum State {
      READY,
      SCHEDULED,
      RUNNING,
      SUCCEEDED,
      CANCELLED,
      FAILED;
   }
}
