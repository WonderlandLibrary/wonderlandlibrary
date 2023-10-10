package my.NewSnake.Tank.gui.click.component;

import my.NewSnake.Tank.command.Command;
import my.NewSnake.Tank.command.CommandManager;
import my.NewSnake.utils.ClientUtils;
import my.NewSnake.utils.minecraft.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

public class Console extends Component {
   private boolean autoCompleteNext;
   private GuiTextField textField = new GuiTextField(-69, ClientUtils.clientFont(), 0, 0, 0, 0);

   public void keyPress(int var1, char var2) {
   }

   public void drag(int var1, int var2, int var3) {
   }

   public Console() {
      super((Object)null, 0.0D, 0.0D, 0.0D, 0.0D);
   }

   public void click(int var1, int var2, int var3) {
   }

   public void release(int var1, int var2, int var3) {
   }

   public void draw(int var1, int var2) {
      ScaledResolution var3 = new ScaledResolution(ClientUtils.mc());
      this.textField.xPosition = (int)(var3.getScaledWidth_double() / 4.0D);
      this.textField.width = (int)(var3.getScaledWidth_double() / 2.0D);
      this.textField.yPosition = 2;
      this.textField.height = 18;
   }

   public boolean keyType(int var1, char var2) {
      if (this.textField.isFocused() && 1 == var1) {
         this.textField.setText("");
         this.textField.setFocused(false);
         return true;
      } else {
         if (!Keyboard.isKeyDown(42) && !Keyboard.isKeyDown(54) && 53 == var1) {
            this.textField.setText("");
            this.textField.setFocused(true);
         } else if (28 == var1) {
            String[] var3 = this.textField.getText().split(" ");
            Command var4 = CommandManager.getCommandFromMessage(this.textField.getText());
            var4.runCommand(var3);
            this.textField.setText("");
         } else if (this.textField.isFocused()) {
            this.autoCompleteNext = false;
            this.textField.textboxKeyTyped(var2, var1);
         }

         return false;
      }
   }
}
