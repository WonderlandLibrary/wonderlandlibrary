package team.massacre.api.property;

@FunctionalInterface
public interface ValueChangeListener<T> {
   void onValueChange(T var1, T var2);
}
