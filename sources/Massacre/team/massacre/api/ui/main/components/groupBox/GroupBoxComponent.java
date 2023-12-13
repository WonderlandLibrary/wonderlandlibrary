package team.massacre.api.ui.main.components.groupBox;

import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;
import team.massacre.api.module.Module;
import team.massacre.api.property.Property;
import team.massacre.api.property.impl.EnumProperty;
import team.massacre.api.property.impl.MultiSelectEnumProperty;
import team.massacre.api.ui.framework.component.Component;
import team.massacre.api.ui.framework.component.ExpandableComponent;
import team.massacre.api.ui.framework.component.PredicateComponent;
import team.massacre.api.ui.main.components.groupBox.sub.CheckBoxComponent;
import team.massacre.api.ui.main.components.groupBox.sub.ComboBoxComponent;
import team.massacre.api.ui.main.components.tab.TabComponent;
import team.massacre.utils.OGLUtils;
import team.massacre.utils.RenderingUtils;

public abstract class GroupBoxComponent extends Component {
   private final String title;
   private final GroupBoxSide side;
   private final GroupBoxType type;
   private final GroupBoxPosition position;
   private final float margins = 4.0F;
   private boolean needsScrolling;

   public GroupBoxComponent(TabComponent parent, String title, GroupBoxPosition position, GroupBoxSide side, GroupBoxType type) {
      super(parent, 0.0F, 0.0F, 0.0F, 0.0F);
      if (position == GroupBoxPosition.THIRD_X2 && type != GroupBoxType.SMALL) {
         throw new IllegalArgumentException("Invalid group box position");
      } else if (position == GroupBoxPosition.THIRD && type != GroupBoxType.BIG) {
         throw new IllegalArgumentException("Invalid group box position");
      } else if (position == GroupBoxPosition.HALF && type != GroupBoxType.HALF) {
         throw new IllegalArgumentException("Invalid group box position");
      } else {
         this.title = title;
         this.side = side;
         this.type = type;
         this.position = position;
         this.initComponents();
      }
   }

   public abstract void initComponents();

   protected void addComponentsFromModule(final Module module) {
      this.addChild(new CheckBoxComponent(this, "Enabled") {
         public boolean isVisible() {
            return true;
         }

         public void onChecked() {
            module.toggle();
         }

         public boolean isChecked() {
            return module.getState();
         }
      });
      Iterator var2 = module.getValues().iterator();

      while(var2.hasNext()) {
         final Property<?> property = (Property)var2.next();
         if (property instanceof EnumProperty) {
            final EnumProperty<?> enumProperty = (EnumProperty)property;
            this.addChild(new ComboBoxComponent(this, property.getLabel()) {
               public Enum<?> getValue() {
                  return (Enum)enumProperty.getValue();
               }

               public List<Enum<?>> getMultiSelectValues() {
                  return null;
               }

               public void setValue(int index) {
                  enumProperty.setValue(index);
               }

               public boolean isMultiSelectable() {
                  return false;
               }

               public Enum<?>[] getValues() {
                  return enumProperty.getValues();
               }

               public boolean isVisible() {
                  return enumProperty.isAvailable();
               }
            });
         } else if (property instanceof MultiSelectEnumProperty) {
            final MultiSelectEnumProperty<?> multiSelectEnumProperty = (MultiSelectEnumProperty)property;
            this.addChild(new ComboBoxComponent(this, property.getLabel()) {
               public Enum<?> getValue() {
                  return null;
               }

               public List<Enum<?>> getMultiSelectValues() {
                  return (List)multiSelectEnumProperty.getValue();
               }

               public void setValue(int index) {
                  multiSelectEnumProperty.setValue(index);
               }

               public boolean isMultiSelectable() {
                  return true;
               }

               public Enum<?>[] getValues() {
                  return multiSelectEnumProperty.getValues();
               }

               public boolean isVisible() {
                  return multiSelectEnumProperty.isAvailable();
               }
            });
         } else if (property.getType() == Boolean.class) {
            this.addChild(new CheckBoxComponent(this, property.getLabel()) {
               public boolean isVisible() {
                  return property.isAvailable();
               }

               public void onChecked() {
                  Property<Boolean> booleanProperty = property;
                  booleanProperty.setValue(!(Boolean)booleanProperty.getValue());
               }

               public boolean isChecked() {
                  Property<Boolean> booleanProperty = property;
                  return (Boolean)booleanProperty.getValue();
               }
            });
         }
      }

   }

   public void drawComponent(ScaledResolution lockedResolution, int mouseX, int mouseY) {
      float x = this.getX();
      float y = this.getY();
      float width = this.getWidth();
      float height = this.getHeight();
      float titleMarginX = 1.0F;
      float titleOffsetX = 7.0F;
      Gui.drawRect((double)x, (double)y, (double)(x + width), (double)(y + height), -15987700);
      Gui.drawRect((double)(x + 0.5F), (double)(y + 0.5F), (double)(x + width - 0.5F), (double)(y + height - 0.5F), -14145496);
      Gui.drawRect((double)(x + 1.0F), (double)(y + 1.0F), (double)(x + width - 1.0F), (double)(y + height - 1.0F), -15263977);
      Gui.drawRect((double)(x + 7.0F - 1.0F), (double)y, (double)(x + 7.0F + GROUP_BOX_FONT_RENDERER.getWidth(this.title) + 1.0F), (double)(y + 1.0F), -15263977);
      GROUP_BOX_FONT_RENDERER.drawStringWithOutline(this.title, x + 7.0F, y - 2.0F, -16777216, -2302756);
      this.getClass();
      float pos = 10.0F + 4.0F;
      if (this.needsScrolling) {
         GL11.glEnable(3089);
         OGLUtils.startScissorBox(lockedResolution, (int)x, (int)y, (int)width, (int)height);
      }

      Iterator var11 = this.getChildren().iterator();

      while(true) {
         Component child;
         PredicateComponent predicateComponent;
         do {
            if (!var11.hasNext()) {
               if (this.needsScrolling) {
                  GL11.glDisable(3089);
                  RenderingUtils.drawGradientRect((double)x, (double)(y + height - 10.0F), (double)(x + width), (double)(x + height), false, 0, -2145970409);
               }

               return;
            }

            child = (Component)var11.next();
            if (!(child instanceof PredicateComponent)) {
               break;
            }

            predicateComponent = (PredicateComponent)child;
         } while(!predicateComponent.isVisible());

         child.setY(pos);
         child.drawComponent(lockedResolution, mouseX, mouseY);
         float var10001 = child.getHeight();
         this.getClass();
         pos += var10001 + 4.0F;
      }
   }

   public void onMouseClick(int mouseX, int mouseY, int button) {
      Iterator var4 = this.getChildren().iterator();

      Component child;
      do {
         PredicateComponent predicateComponent;
         do {
            if (!var4.hasNext()) {
               return;
            }

            child = (Component)var4.next();
            if (!(child instanceof PredicateComponent)) {
               break;
            }

            predicateComponent = (PredicateComponent)child;
         } while(!predicateComponent.isVisible());

         if (child instanceof ExpandableComponent) {
            ExpandableComponent expandableComponent = (ExpandableComponent)child;
            if (expandableComponent.isExpanded()) {
               float x = expandableComponent.getExpandedX();
               float y = expandableComponent.getExpandedY();
               float width = expandableComponent.getExpandedWidth();
               float height = expandableComponent.getExpandedHeight();
               if ((float)mouseX >= x && (float)mouseY >= y && (float)mouseX <= x + width && (float)mouseY <= y + height) {
                  child.onMouseClick(mouseX, mouseY, button);
                  return;
               }
            }
         }
      } while(!child.isHovered(mouseX, mouseY));

      child.onMouseClick(mouseX, mouseY, button);
   }

   public float getX() {
      return this.side == GroupBoxSide.RIGHT ? super.getX() + this.getParent().getWidth() - this.getWidth() : super.getX();
   }

   public float getY() {
      Component parent = this.getParent();
      float y = super.getY();
      switch(this.position) {
      case HALF:
      case THIRD_X2:
         y += parent.getHeight() - this.getHeight();
         break;
      case THIRD:
         y += 10.0F + this.getHeight();
      }

      return y;
   }

   public float getHeight() {
      Component parent = this.getParent();
      switch(this.type) {
      case BIG:
         return this.getThirdHeight() * 2.0F;
      case HALF:
         return this.getHalfHeight();
      case SMALL:
         return this.getThirdHeight();
      default:
         return parent.getHeight();
      }
   }

   public float getWidth() {
      Component parent = this.getParent();
      return (parent.getWidth() - 10.0F) / 2.0F;
   }

   private float getHalfHeight() {
      Component parent = this.getParent();
      return (parent.getHeight() - 10.0F) / 2.0F;
   }

   private float getThirdHeight() {
      Component parent = this.getParent();
      return (parent.getHeight() - 10.0F) / 3.0F;
   }

   private float getHeightNeeded() {
      float pos = 10.0F;

      float var10001;
      for(Iterator var2 = this.getChildren().iterator(); var2.hasNext(); pos += var10001 + 4.0F) {
         Component child = (Component)var2.next();
         var10001 = child.getHeight();
         this.getClass();
      }

      return pos;
   }
}
