package mm.mayorideas.security;

import android.util.Base64;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import mm.mayorideas.security.exception.EncryptionException;

public final class AESEncryptor implements Encryptor{

    public IvParameterSpec getIv() {
        return this.iv;
    }

    public SecretKeySpec getSecret() {
        return this.secret;
    }

    private SecretKeySpec secret;
    private IvParameterSpec iv;
    private Cipher decryptor;
    private Cipher encryptor;

    public AESEncryptor() {
        createSecret();
        createIV();
        createEncryptor();
        createDecryptor();
    }

    @Override
    public String decrypt(String message)
    {
        try {
            byte[] encrypted = Base64.decode(message.getBytes("UTF-8"), Base64.DEFAULT);
            byte[] decrypted = this.decryptor.doFinal(encrypted);
            return new String(decrypted, "UTF-8").trim();
        } catch (Exception e) { throw new EncryptionException("Could not decrypt a message ["+message+"]", e); }
    }

    @Override
    public String encrypt(String message)
    {
        try {
            byte[] encrypted = this.encryptor.doFinal(message.getBytes("UTF-8"));
            return Base64.encodeToString(encrypted, Base64.DEFAULT).trim();
        } catch (Exception e) { throw new EncryptionException("Could not encrypt a message ["+message+"]", e); }
    }

    private void createSecret()
    {
        try {
            String password = "some totally secure password should be right here";
            String salt = "3FF2EC019C627B945225DEBAAEA8C628D4DFE84C95A70EB132882F88C0A59A55";

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), hex(salt), 1000, 128);
            SecretKey tmp = factory.generateSecret(spec);
            this.secret = new SecretKeySpec(tmp.getEncoded(), "AES");
        } catch (Exception e) { throw new EncryptionException("Could not create a secret!", e); }
    }

    private void createEncryptor()
    {
        try {
            this.encryptor = Cipher.getInstance("AES/CBC/PKCS5Padding");
            this.encryptor.init(Cipher.ENCRYPT_MODE, this.secret, this.iv);
        } catch (Exception e) { throw new EncryptionException("Could not create an Encryptor!", e); }
    }

    private void createIV()
    {
        String iv = "F27D5C9927726BAB407510B1BDD3D137";
        this.iv = new IvParameterSpec(hex(iv));
    }

    private void createDecryptor()
    {
        try {
            this.decryptor = Cipher.getInstance("AES/CBC/PKCS5Padding");
            this.decryptor.init(Cipher.DECRYPT_MODE, this.secret, this.iv);
        } catch (Exception e) { throw new EncryptionException("Could not create a Decryptor!", e); }
    }

    private byte[] hex(String str) {
        try {
            return Hex.decodeHex(str.toCharArray());
        }
        catch (DecoderException e) {
            throw new IllegalStateException(e);
        }
    }
}

