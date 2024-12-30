package com.feirui.oss.utils;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * 本地文件加密工具
 */
@Slf4j
public class LocalEncryptUtil {

    /**
     * 对称加密模式
     */
    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";

    /**
     * 文件AES加密密钥
     */
    public static final String AES_KEY = "feirui";

    /**
     * 文件流AES加密，输出文件流
     */
    public static InputStream aesEncryptToStream(InputStream input) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(AES_KEY.getBytes(StandardCharsets.UTF_8), "AES"));
        } catch (Exception e) {
            log.error("LocalEncryptUtils.aesEncryptToStream.err: {}", e.getMessage(), e);
        }
        return new CipherInputStream(input, cipher);
    }

    /**
     * 文件流AES加密，输出目标路径
     */
    public static void aesEncryptToPath(InputStream input, String targetPath) {
        LocalFileUtil.copyFile(aesEncryptToStream(input), targetPath);
    }

    /**
     * 文件流AES加密，输出字节数组
     */
    public static byte[] aesEncryptToByte(InputStream input) {
        return LocalFileUtil.inputStream2byte(aesEncryptToStream(input));
    }

    /**
     * 文件流AES解密，输出文件流
     */
    public static InputStream aesDecryptToStream(InputStream input) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(AES_KEY.getBytes(StandardCharsets.UTF_8), "AES"));
        } catch (Exception e) {
            log.error("LocalEncryptUtils.aesDecryptToStream.err: {}", e.getMessage(), e);
        }
        return new CipherInputStream(input, cipher);
    }

    /**
     * 文件流AES解密，输出字节数组
     */
    public static byte[] aesDecryptToByte(InputStream input) {
        return LocalFileUtil.inputStream2byte(aesDecryptToStream(input));
    }

}
