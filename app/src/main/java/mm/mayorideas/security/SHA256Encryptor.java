package mm.mayorideas.security;

import java.security.MessageDigest;

import mm.mayorideas.security.exception.EncryptionException;

/**
 * Based on previous group work.
 * Source: https://github.com/begleynk/SEG3-Server/blob/master/src/Helpers/PasswordEncryptor.java
 */
public class SHA256Encryptor implements Encryptor {

    private static final String salt = "eFmVkKCL16GaEgW";

    @Override
    public String encrypt(String message) {
            try
            {
                message += salt;

                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest(message.getBytes("UTF-8"));

                StringBuilder sb = new StringBuilder();
                for (byte aHash : hash) {
                    sb.append(Integer.toString((aHash & 0xff) + 0x100, 16).substring(1));
                }
                return sb.toString();
            }
            catch (Exception e) {
                throw new EncryptionException("Error hashing password.", e);
            }
    }

    @Override
    public String decrypt(String message) {
        throw new EncryptionException("Impossible to decrypt with SHA256!");
    }
}
