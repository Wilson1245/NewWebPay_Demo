package com.demo.newwebpaydemo.newWebPay;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;
import java.util.stream.Collectors;

public class NewWebUtil {

    // post api
    public static String sendPostMethod(String urlString, Map<String, String> params) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0"); // 添加 User-Agent 標頭
        conn.setDoOutput(true);
        // 將參數轉換為 key=value&key=value 格式
        String postData = params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));
        // 發送請求參數
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = postData.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        // 讀取響應
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            return br.lines().collect(Collectors.joining("\n"));
        }
    }

    public static <T>T convertJson(String json, Class<T> clazz) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, clazz);
    }

    // AES-256-CBC 加密方法
    public static String encryptAES(String data, String key, String iv) {
        try {
            // 將密鑰和 IV 轉換為字節陣列
            byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
            byte[] ivBytes = iv.getBytes(StandardCharsets.UTF_8);

            // 建立 Cipher 實例並設定模式和填充
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

            // 執行加密
            byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

            // 將加密結果轉為十六進制格式
            return bytesToHex(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting AES-256-CBC data", e);
        }
    }

    // 將字節數組轉換為十六進制格式
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    // 生成 SHA256 驗證碼
    public static String generateSHA256(String data1, String key, String iv) {
        try {
            // 1. 拼接 HashKey、data1 和 HashIV
            String hashString = "HashKey=" + key + "&" + data1 + "&HashIV=" + iv;

            // 2. 計算 SHA256 哈希值
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(hashString.getBytes(StandardCharsets.UTF_8));

            // 3. 將哈希值轉換為大寫的十六進制格式
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString().toUpperCase();
        } catch (Exception e) {
            throw new RuntimeException("Error generating SHA256 hash", e);
        }
    }

    // 生成 CheckValue
    public static String generateCheckValue(String amt, String merchantID, String merchantOrderNo, String hashKey, String hashIV) {
        try {
            // 按照 A~Z 排序參數
            String data = "Amt=" + amt + "&MerchantID=" + merchantID + "&MerchantOrderNo=" + merchantOrderNo;

            // 拼接字串：IV 在前，Key 在後
            String hashString = "IV=" + hashIV + "&" + data + "&Key=" + hashKey;

            // 將拼接後的字串轉為 SHA-256 大寫
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(hashString.getBytes(StandardCharsets.UTF_8));

            // 將 SHA-256 結果轉為大寫十六進制
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            // 返回大寫的 CheckValue
            return hexString.toString().toUpperCase();
        } catch (Exception e) {
            throw new RuntimeException("Error generating CheckValue", e);
        }
    }

    // AES-256-CBC 解密方法
    public static String decryptAES(String encryptedData, String key, String iv) {
        try {
            // 確保密鑰和 IV 的長度正確
            byte[] keyBytes = adjustKeyOrIvLength(key, 32); // 32字節密鑰
            byte[] ivBytes = adjustKeyOrIvLength(iv, 16);   // 16字節IV

            // 將加密數據從十六進制轉為字節陣列
            byte[] encryptedBytes = hexStringToByteArray(encryptedData);

            // 建立 Cipher 實例並設定模式和填充
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

            // 解密數據
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            // 轉換解密後的字節數組為字符串
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            // 打印錯誤訊息和堆疊追蹤以便調試
            e.printStackTrace();
            throw new RuntimeException("Error decrypting AES-256-CBC data", e);
        }
    }

    // 調整密鑰或 IV 的長度
    private static byte[] adjustKeyOrIvLength(String input, int length) {
        byte[] bytes = new byte[length];
        byte[] originalBytes = input.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(originalBytes, 0, bytes, 0, Math.min(originalBytes.length, length));
        return bytes;
    }

    // 將十六進制字符串轉換為字節數組
    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}
