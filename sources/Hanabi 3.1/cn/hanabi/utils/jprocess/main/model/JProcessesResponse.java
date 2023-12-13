package cn.hanabi.utils.jprocess.main.model;

public class JProcessesResponse {
   private boolean success;
   private String message;

   public boolean isSuccess() {
      return this.success;
   }

   public void setSuccess(boolean success) {
      this.success = success;
   }

   public String getMessage() {
      return this.message;
   }

   public void setMessage(String message) {
      this.message = message;
   }
}
