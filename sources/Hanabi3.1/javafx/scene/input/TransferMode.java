package javafx.scene.input;

public enum TransferMode {
   COPY,
   MOVE,
   LINK;

   public static final TransferMode[] ANY = new TransferMode[]{COPY, MOVE, LINK};
   public static final TransferMode[] COPY_OR_MOVE = new TransferMode[]{COPY, MOVE};
   public static final TransferMode[] NONE = new TransferMode[0];
}
