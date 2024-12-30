package com.feirui.oss.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
public class LocalFileUtil {

    public final static String LEFT_SLASH = "/";

    /**
     * 创建文件夹
     *
     * @param folder 待创建文件夹
     */
    public static Boolean createPath(String folder) {
        File file = new File(folder);
        return Boolean.TRUE.equals(file.mkdirs());
    }

    /**
     * 判断文件夹是否存在
     *
     * @param folder 文件夹名
     */
    public static Boolean doesPathExist(String folder) {
        File file = new File(folder);
        return Boolean.TRUE.equals(file.exists());
    }

    /**
     * 复制文件
     *
     * @param srcInput 输入流
     * @param outPath  输出路径
     * @return boolean 复制结果
     */
    public static boolean copyFile(InputStream srcInput, String outPath) {
        try (InputStream in = srcInput;
             OutputStream out = Files.newOutputStream(Paths.get(outPath))) {
            return copyFile(in, out);
        } catch (Exception ex) {
            log.error("复制文件失败 >>>>> ", ex);
        }
        return false;
    }

    /**
     * 复制文件
     *
     * @param in  输入流
     * @param out 输出流
     * @return boolean 复制结果
     */
    public static boolean copyFile(InputStream in, OutputStream out) {
        int len;
        try (BufferedInputStream bis = new BufferedInputStream(in);
             BufferedOutputStream bos = new BufferedOutputStream(out)) {
            while ((len = bis.read()) != -1) {
                bos.write(len);
            }
            return true;
        } catch (Exception ex) {
            log.error("复制文件失败 >>>>> ", ex);
        }
        return false;
    }

    /**
     * 输入流转换字节数组
     *
     * @param in 输入流
     * @return byte[] 字节数组
     */
    public static byte[] inputStream2byte(InputStream in) {
        byte[] b = new byte[1024];
        int n;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            while ((n = in.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            return bos.toByteArray();
        } catch (Exception e) {
            log.error("输入流转换字节数组失败 >>>>> ");
        } finally {
            closeInputStream(in);
        }
        return new byte[]{};
    }

    /**
     * 强制删除文件或目录
     * 1、如果是文件，直接删除
     * 2、如果是目录，删除目录下所有文件和子目录
     *
     * @param file 待删除文件或目录
     * @return boolean 删除结果
     */
    public static boolean forceDeleteFile(File file) {
        if (file == null || !file.exists()) {
            return false;
        }
        boolean result = false;
        try {
            FileUtils.forceDelete(file);
            result = true;
        } catch (IOException e) {
            log.error("强制删除文件或目录失败 >>>>>>>> ", e);
        }
        return result;
    }

    public static Long size(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[8192];
        int bytesRead;
        long totalBytesRead = 0L;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            totalBytesRead += bytesRead;
        }
        return totalBytesRead;
    }

    /**
     * 关闭输入流
     *
     * @param in 待关闭输入流
     */
    public static void closeInputStream(InputStream in) {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                log.error("关闭输入流失败 >>>>>> ");
            }
        }
    }

}
