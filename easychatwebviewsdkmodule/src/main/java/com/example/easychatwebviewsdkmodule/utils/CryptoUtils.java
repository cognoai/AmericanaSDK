package com.example.easychatwebviewsdkmodule.utils;

import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.zip.GZIPOutputStream;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * utility class to handle all encryption / decryption logic
 */

public class CryptoUtils {

    private static final String TAG = "CryptoUtils";

    /** returns encrypted string */
    public static String getEncryptedRequest(String requestJson) throws Exception {
        //Log.d(TAG, "request Json: " + requestJson);

        String key = getKey();
        byte[] IV = getInitializationVector();

        String encryptedMessage = encodeBase64(encrypt(requestJson.getBytes("UTF-8"), key, IV));

        String requestString = key + "." + encryptedMessage + "." + encodeBase64(IV);

        requestString = requestString.replace("\n", "");

        return requestString;
    }

    public static String getCompressedEncryptedRequest(String requestJson) throws Exception {
        //Log.d(TAG, "request Json: " + requestJson);

        String key = getKey();
        byte[] IV = getInitializationVector();

        String encryptedMessage = encodeBase64(encrypt(requestJson.getBytes("UTF-8"), key, IV));

        String requestString = key + "." + encryptedMessage + "." + encodeBase64(IV);

        requestString = requestString.replace("\n", "");

        String compressed = compress(requestString);

        Log.d(TAG, "compress: " +  requestString.getBytes().length);

        return compressed;
    }

    /** returns decrypted JSON */
    public static String getDecryptedResponse(String response) {
        String[] split = response.split("\\.");
        String key = split[0];
        String queryResponseJson = split[1];
        String iv = split[2];
        String decryptedMessage = null;
        try {
            decryptedMessage = CryptoUtils.decrypt(
                    queryResponseJson.trim().getBytes("UTF-8"),
                    key.getBytes("UTF-8"),
                    iv.getBytes("UTF-8"))
                    .trim();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return decryptedMessage;
    }

    public static byte[] encrypt(byte[] plaintext, String key, byte[] IV) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(IV);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] cipherText = cipher.doFinal(plaintext);
        return cipherText;
    }

    public static String decrypt(byte[] cipherText, byte[] key, byte[] IV) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(decodeBase64(IV));
            byte[] bytes = decodeBase64(cipherText);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            byte[] decryptedText = cipher.doFinal(bytes);
            return new String(decryptedText, "UTF-8");
        } catch (Exception e) {
            Log.e(TAG, "decrypt: ", e);
        }
        return null;
    }

    public static String getKey() {
        return randomString(16);
    }

    private static String randomString(int keySize) {
        SecureRandom random = new SecureRandom();
        String acceptedChars = "1234567890QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm";
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < keySize; i++)
            string.append(acceptedChars.charAt(random.nextInt(acceptedChars.length())));
        return string.toString();
    }

    public static byte[] getInitializationVector() {
        byte[] IV = new byte[16];
        SecureRandom random;
        random = new SecureRandom();
        random.nextBytes(IV);
        return IV;
    }

    private static byte[] decodeBase64(byte[] text) {
        return Base64.decode(text, Base64.DEFAULT);
    }

    public static String encodeBase64(byte[] text) {
        return Base64.encodeToString(text, Base64.DEFAULT);
    }

    private static String compress(String request) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            GZIPOutputStream gzipOutputStream = null;
            try {
                gzipOutputStream = new GZIPOutputStream(stream);
                gzipOutputStream.write(request.getBytes());
                //bitmap.compress(Bitmap.CompressFormat.JPEG, 75, gzipOutputStream);
                gzipOutputStream.flush();
            } finally {
                gzipOutputStream.close();
                stream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            stream = null;
        }
        if(stream != null) {
            byte[] byteArry=stream.toByteArray();
            Log.d(TAG, "compress: " + byteArry.length);
            return Arrays.toString(byteArry);
           // String encodedImage = Base64.encodeToString(byteArry, Base64.NO_WRAP);
            //return encodedImage;
        }
        return null;
    }
}
