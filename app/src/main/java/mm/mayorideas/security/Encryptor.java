package mm.mayorideas.security;

public interface Encryptor {

    String encrypt(String message);
    String decrypt(String message);

}
