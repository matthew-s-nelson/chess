package server;

import com.google.gson.Gson;

public class gson {
  private String message;
  private Object object;
  public gson(String message, Object object) {
    this.message = message;
    this.object = object;
  }

  public static Object fromJson(String message, Object object) {
    return new Gson().fromJson(message, object.getClass());
  }
}
