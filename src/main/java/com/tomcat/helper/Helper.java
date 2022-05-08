package com.tomcat.helper;

import jakarta.servlet.http.HttpServletResponse;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;
import java.util.function.Consumer;


public class Helper {
    public static class RandomColor {
        private static final Random random = new Random();
        public static String createColorText(String str) { return String.format("\033[%d;2m%s\033[0m", 31 + random.nextInt(6), str); }
        public static Random getRandom() { return random; }
    }

    public static class HTML {
        private PrintWriter writer;
        private final HttpServletResponse response;
        public HTML(HttpServletResponse response) {
            this.response = response;
            try {
                this.writer = response.getWriter();
            } catch (IOException e) { e.printStackTrace();}
            this.response.setContentType("text/html;charset:GBK");
        }

        public void generatorHTML(Consumer<PrintWriter> writeContent, String title) {
            writer.println("<html><head><title>" + title + "</title></head><body>");
            writeContent.accept(writer);
            writer.println("</body></html>");
            writer.close();
        }

        public void write(String name, Object info) { writer.println("<br>" + name + ": " + info); }
        public void setContentType(String contentType) { this.response.setContentType(contentType); }
    }

    public static class EncodeTool {
        private static final String[] encodes = new String[] { "GBK", "GB2312", "ISO-8859-1", "UTF-8" };

        public static boolean isEncoding(String str, String encode) {
            try {
                if (str.equals(new String(str.getBytes(encode), encode))) return true;
            } catch (UnsupportedEncodingException e) { e.printStackTrace(); }
            return false;
        }

        public static String getEncoding(String str) { return Arrays.stream(encodes).filter(encode -> isEncoding(str, encode)).findFirst().orElse(null); }
    }

    public static class AESUtil {
        public enum SecretKeyLength {
            SECRET_KEY_128(0x80),
            SECRET_KEY_192(0xc0),
            SECRET_KEY_256(0x100);

            private final int secretKeyLength;
            SecretKeyLength(int secretKeyLength) { this.secretKeyLength = secretKeyLength; }
            public int getSecretKeyLength() { return secretKeyLength; }

        }

        private AESUtil() {}

        // 获得一个密钥为128、192或256位的AES密钥
        public static String getSecretKeyAES(SecretKeyLength secretKeyLength) throws NoSuchAlgorithmException {
            KeyGenerator kenGen = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = new SecureRandom(String.valueOf(System.currentTimeMillis()).getBytes());
            kenGen.init(secretKeyLength.getSecretKeyLength(), secureRandom);
            SecretKey secretKey = kenGen.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        }

        // 将使用Base64加密后的字符串类型的secretKey转为SecretKey
        public static SecretKey base64SecretKeyToSecretKey(String base64) { return new SecretKeySpec(Base64.getDecoder().decode(base64), "AES"); }

        /*
        * 加密
        * @param plaintextDataBytes 待加密的明文数据字节
        * @param secretKey 加密使用的AES密钥
        * @return 加密后的密文字节数组 */
        public static byte[] encryptAES(byte[] plaintextDataBytes, SecretKey secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException { return doAES(plaintextDataBytes, secretKey, Cipher.ENCRYPT_MODE); }

        /*
         * 解密
         * @param plaintextDataBytes 待解密的明文数据字节
         * @param secretKey 解密使用的AES密钥
         * @return 解密后的密文字节数组 */
        public static byte[] decryptAES(byte[] encryptDataBytes, SecretKey secretKey) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException { return doAES(encryptDataBytes, secretKey, Cipher.DECRYPT_MODE); }

        private static byte[] doAES(byte[] data, SecretKey secretKey, int mode) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
        {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(mode, secretKey);
            return cipher.doFinal(data);
        }
    }
}
