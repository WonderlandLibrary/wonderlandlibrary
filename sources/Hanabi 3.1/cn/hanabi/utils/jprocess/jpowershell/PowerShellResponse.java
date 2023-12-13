package cn.hanabi.utils.jprocess.jpowershell;

public class PowerShellResponse {
   private final boolean error;
   private final String commandOutput;
   private final boolean timeout;

   PowerShellResponse(boolean isError, String commandOutput, boolean timeout) {
      this.error = isError;
      this.commandOutput = commandOutput;
      this.timeout = timeout;
   }

   public boolean isError() {
      return this.error;
   }

   public String getCommandOutput() {
      return this.commandOutput;
   }

   public boolean isTimeout() {
      return this.timeout;
   }
}
