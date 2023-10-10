package gnu.trove.list;

import java.io.Serializable;

public interface TLinkable extends Serializable {
   long serialVersionUID = 997545054865482562L;

   TLinkable getNext();

   TLinkable getPrevious();

   void setNext(TLinkable var1);

   void setPrevious(TLinkable var1);
}
