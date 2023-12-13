package team.massacre.api.event.api.bus;

public interface Bus<T> {
   void register(Object var1);

   void unregister(Object var1);

   void post(T var1);
}
