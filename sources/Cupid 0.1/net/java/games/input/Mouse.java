package net.java.games.input;

public abstract class Mouse extends AbstractController {
  protected Mouse(String name, Component[] components, Controller[] children, Rumbler[] rumblers) {
    super(name, components, children, rumblers);
  }
  
  public Controller.Type getType() {
    return Controller.Type.MOUSE;
  }
  
  public Component getX() {
    return getComponent(Component.Identifier.Axis.X);
  }
  
  public Component getY() {
    return getComponent(Component.Identifier.Axis.Y);
  }
  
  public Component getWheel() {
    return getComponent(Component.Identifier.Axis.Z);
  }
  
  public Component getPrimaryButton() {
    Component primaryButton = getComponent(Component.Identifier.Button.LEFT);
    if (primaryButton == null)
      primaryButton = getComponent(Component.Identifier.Button._1); 
    return primaryButton;
  }
  
  public Component getSecondaryButton() {
    Component secondaryButton = getComponent(Component.Identifier.Button.RIGHT);
    if (secondaryButton == null)
      secondaryButton = getComponent(Component.Identifier.Button._2); 
    return secondaryButton;
  }
  
  public Component getTertiaryButton() {
    Component tertiaryButton = getComponent(Component.Identifier.Button.MIDDLE);
    if (tertiaryButton == null)
      tertiaryButton = getComponent(Component.Identifier.Button._3); 
    return tertiaryButton;
  }
  
  public Component getLeft() {
    return getComponent(Component.Identifier.Button.LEFT);
  }
  
  public Component getRight() {
    return getComponent(Component.Identifier.Button.RIGHT);
  }
  
  public Component getMiddle() {
    return getComponent(Component.Identifier.Button.MIDDLE);
  }
  
  public Component getSide() {
    return getComponent(Component.Identifier.Button.SIDE);
  }
  
  public Component getExtra() {
    return getComponent(Component.Identifier.Button.EXTRA);
  }
  
  public Component getForward() {
    return getComponent(Component.Identifier.Button.FORWARD);
  }
  
  public Component getBack() {
    return getComponent(Component.Identifier.Button.BACK);
  }
  
  public Component getButton3() {
    return getComponent(Component.Identifier.Button._3);
  }
  
  public Component getButton4() {
    return getComponent(Component.Identifier.Button._4);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\java\games\input\Mouse.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */