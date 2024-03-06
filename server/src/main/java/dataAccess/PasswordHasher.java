package dataAccess;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHasher {
  public static String hashPassword(String clearTextPassword) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    String hashedPassword = encoder.encode(clearTextPassword);

    return hashedPassword;
  }

  public static boolean comparePassword(String clearTextPassword, String dbPassword) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    return encoder.matches(clearTextPassword, dbPassword);
  }
}
