package javafx.beans;

public interface Observable {
   void addListener(InvalidationListener var1);

   void removeListener(InvalidationListener var1);
}
