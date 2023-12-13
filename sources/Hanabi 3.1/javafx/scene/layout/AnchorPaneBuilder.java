package javafx.scene.layout;

/** @deprecated */
@Deprecated
public class AnchorPaneBuilder extends PaneBuilder {
   protected AnchorPaneBuilder() {
   }

   public static AnchorPaneBuilder create() {
      return new AnchorPaneBuilder();
   }

   public AnchorPane build() {
      AnchorPane var1 = new AnchorPane();
      this.applyTo(var1);
      return var1;
   }
}
