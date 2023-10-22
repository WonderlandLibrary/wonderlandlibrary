package cn.hanabi.utils.jprocess.jpowershell;

public class PowerShellNotAvailableException extends RuntimeException {
   PowerShellNotAvailableException(String message) {
      super(message);
   }

   PowerShellNotAvailableException(String message, Throwable cause) {
      super(message, cause);
   }
}
