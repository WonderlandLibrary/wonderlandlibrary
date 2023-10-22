package javafx.scene.web;

import com.sun.webkit.BackForwardList;
import com.sun.webkit.WebPage;
import java.net.URL;
import java.util.Date;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public final class WebHistory {
   private final BackForwardList bfl;
   private final ObservableList list = FXCollections.observableArrayList();
   private final ObservableList ulist;
   private final ReadOnlyIntegerWrapper currentIndex = new ReadOnlyIntegerWrapper(this, "currentIndex");
   private IntegerProperty maxSize;

   WebHistory(WebPage var1) {
      this.ulist = FXCollections.unmodifiableObservableList(this.list);
      this.bfl = var1.createBackForwardList();
      this.setMaxSize(this.getMaxSize());
      this.bfl.addChangeListener((var1x) -> {
         if (this.bfl.size() > this.list.size()) {
            assert this.bfl.size() == this.list.size() + 1;

            this.list.add(new Entry(this.bfl.getCurrentEntry()));
            this.setCurrentIndex(this.list.size() - 1);
         } else {
            if (this.bfl.size() == this.list.size()) {
               if (this.list.size() == 0) {
                  return;
               }

               assert this.list.size() > 0;

               BackForwardList.Entry var2 = this.bfl.get(this.list.size() - 1);
               BackForwardList.Entry var3 = this.bfl.get(0);
               if (((Entry)this.list.get(this.list.size() - 1)).isPeer(var2)) {
                  this.setCurrentIndex(this.bfl.getCurrentIndex());
                  return;
               }

               if (!((Entry)this.list.get(0)).isPeer(var3)) {
                  this.list.remove(0);
                  this.list.add(new Entry(var2));
                  this.setCurrentIndex(this.bfl.getCurrentIndex());
                  return;
               }
            }

            assert this.bfl.size() <= this.list.size();

            this.list.remove(this.bfl.size(), this.list.size());
            int var4 = this.list.size() - 1;
            if (var4 >= 0 && !((Entry)this.list.get(var4)).isPeer(this.bfl.get(var4))) {
               this.list.remove(var4);
               this.list.add(new Entry(this.bfl.get(var4)));
            }

            this.setCurrentIndex(this.bfl.getCurrentIndex());
         }
      });
   }

   public ReadOnlyIntegerProperty currentIndexProperty() {
      return this.currentIndex.getReadOnlyProperty();
   }

   public int getCurrentIndex() {
      return this.currentIndexProperty().get();
   }

   private void setCurrentIndex(int var1) {
      this.currentIndex.set(var1);
   }

   public IntegerProperty maxSizeProperty() {
      if (this.maxSize == null) {
         this.maxSize = new SimpleIntegerProperty(this, "maxSize", 100) {
            public void set(int var1) {
               if (var1 < 0) {
                  throw new IllegalArgumentException("value cannot be negative.");
               } else {
                  super.set(var1);
               }
            }
         };
      }

      return this.maxSize;
   }

   public void setMaxSize(int var1) {
      this.maxSizeProperty().set(var1);
      this.bfl.setMaximumSize(var1);
   }

   public int getMaxSize() {
      return this.maxSizeProperty().get();
   }

   public ObservableList getEntries() {
      return this.ulist;
   }

   public void go(int var1) throws IndexOutOfBoundsException {
      if (var1 != 0) {
         int var2 = this.getCurrentIndex() + var1;
         if (var2 >= 0 && var2 < this.list.size()) {
            this.bfl.setCurrentIndex(var2);
         } else {
            throw new IndexOutOfBoundsException("the effective index " + var2 + " is out of the range [0.." + (this.list.size() - 1) + "]");
         }
      }
   }

   public final class Entry {
      private final URL url;
      private final ReadOnlyObjectWrapper title;
      private final ReadOnlyObjectWrapper lastVisitedDate;
      private final BackForwardList.Entry peer;

      private Entry(BackForwardList.Entry var2) {
         this.title = new ReadOnlyObjectWrapper(this, "title");
         this.lastVisitedDate = new ReadOnlyObjectWrapper(this, "lastVisitedDate");
         this.url = var2.getURL();
         this.title.set(var2.getTitle());
         this.lastVisitedDate.set(var2.getLastVisitedDate());
         this.peer = var2;
         var2.addChangeListener((var2x) -> {
            String var3 = var2.getTitle();
            if (var3 == null || !var3.equals(this.getTitle())) {
               this.title.set(var3);
            }

            Date var4 = var2.getLastVisitedDate();
            if (var4 != null && !var4.equals(this.getLastVisitedDate())) {
               this.lastVisitedDate.set(var4);
            }

         });
      }

      public String getUrl() {
         assert this.url != null;

         return this.url.toString();
      }

      public ReadOnlyObjectProperty titleProperty() {
         return this.title.getReadOnlyProperty();
      }

      public String getTitle() {
         return (String)this.title.get();
      }

      public ReadOnlyObjectProperty lastVisitedDateProperty() {
         return this.lastVisitedDate.getReadOnlyProperty();
      }

      public Date getLastVisitedDate() {
         return (Date)this.lastVisitedDate.get();
      }

      boolean isPeer(BackForwardList.Entry var1) {
         return this.peer == var1;
      }

      public String toString() {
         return "[url: " + this.getUrl() + ", title: " + this.getTitle() + ", date: " + this.getLastVisitedDate() + "]";
      }

      // $FF: synthetic method
      Entry(BackForwardList.Entry var2, Object var3) {
         this(var2);
      }
   }
}
