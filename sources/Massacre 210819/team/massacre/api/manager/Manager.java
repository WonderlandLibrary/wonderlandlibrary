package team.massacre.api.manager;

import java.util.ArrayList;
import java.util.List;

public class Manager<T> {
   protected List<T> elements;

   public Manager(List<T> elements) {
      this.elements = elements;
   }

   public Manager() {
      this.elements = new ArrayList();
   }

   public List<T> getElements() {
      return this.elements;
   }
}
