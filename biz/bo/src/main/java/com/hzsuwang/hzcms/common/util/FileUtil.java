package com.hzsuwang.hzcms.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件处理工具类
 * 
 * @author tony.yan
 */
public class FileUtil {

    public static String[]                   CONTACT_ALLOW_TYPES = { "gif", "jpg", "jpeg", "bmp", "png", "x-png", "x-bmp", "x-ms-bmp" };

    public static String[]                   SOUND_FILE_TYPES    = { "amr", "mp3", "wma", "wmv", "midi", "mp4", "mod", "cda", "fla", "flac", "mid" };
    private final static Map<String, String> FILE_TYPE_MAP       = new HashMap<String, String>();
    private static Logger                    log                 = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 获取文件类型
     *
     * @param file
     * @return
     */
    public static String getFileType(File file) {
        String res = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            // 获取文件头的前六位
            byte[] b = new byte[3];
            fis.read(b, 0, b.length);
            String fileCode = bytesToHexString(b);
            Iterator<String> keyIter = FILE_TYPE_MAP.keySet().iterator();
            while (keyIter.hasNext()) {
                String key = keyIter.next();
                // 比较前几位是否相同就可以判断文件格式（相同格式文件文件头后面几位会有所变化）
                if (key.toLowerCase().startsWith(fileCode.toLowerCase()) || fileCode.toLowerCase().startsWith(key.toLowerCase())) {
                    res = FILE_TYPE_MAP.get(key);
                    break;
                }
            }
            log.info("文件头:" + fileCode + "-----文件类型:" + res);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    /**
     * byte数组转换成16进制字符串
     *
     * @param src
     * @return
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 获取当前文件保存路径
     * 
     * @return
     */
    public static String getBaseDirPath(String fileType, String fileRoot) {
        GregorianCalendar now;
        String year, day, month, hour;
        now = new GregorianCalendar();
        year = String.valueOf(now.get(Calendar.YEAR));
        month = (now.get(Calendar.MONTH) + 1) < 10 ? "0" + String.valueOf(now.get(Calendar.MONTH) + 1) : String.valueOf(now.get(Calendar.MONTH) + 1);
        day = String.valueOf(now.get(Calendar.DAY_OF_MONTH));
        hour = String.valueOf(now.get(Calendar.HOUR_OF_DAY));
        StringBuilder sb = new StringBuilder();
        sb.append(fileRoot).append(fileType).append(FileConstants.FileServerSeperator);
        sb.append(year).append(FileConstants.FileServerSeperator);
        sb.append(month).append(FileConstants.FileServerSeperator);
        sb.append(day).append(FileConstants.FileServerSeperator);
        sb.append(hour).append(FileConstants.FileServerSeperator);
        return sb.toString();
    }

    /**
     * 文件的本地全目录
     * 
     * @param propertyBean
     * @param fileType
     * @return
     */
    public static String getLocalDirPath(PropertyBean propertyBean, String fileType) {
        String path = getBaseDirPath(fileType, FileConstants.FileServerRoot);
        return propertyBean.getImgLocal() + path;
    }

    /**
     * 文件的url地扯
     * 
     * @param propertyBean
     * @param fileType
     * @return
     */
    public static String getFileDirPath(PropertyBean propertyBean, String fileType) {
        String path = getBaseDirPath(fileType, FileConstants.FileServerRoot);
        return propertyBean.getImgServerDomain() + path;

    }

    /**
     * 文件名
     * 
     * @return
     */
    public static String getUniqueId() {
        String uuidram = "" + UUID.randomUUID().getLeastSignificantBits();
        return uuidram.replace("-", "");
    }

    /**
     * 文件重命名
     * 
     * @param extname
     * @return
     */
    public static String createFileNewName(String extname) {
        return FileUtil.getUniqueId() + "." + extname;
    }

    public static String inputStream2String(InputStream in) throws IOException {
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1;) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }

    public static String getFileExtName(String fileName) {
        String ext = "";
        if (StringUtils.isBlank(fileName)) {
            return ext;
        }
        int i = fileName.lastIndexOf(".");
        ext = fileName.substring(i + 1); // --扩展名
        return ext;
    }

    /**
     * 验证图片格式
     * 
     * @param fileName
     * @return
     */
    public static boolean checkImgFileName(String fileName) {
        boolean checkResult = false;
        String fileType = getFileExtName(fileName);
        for (String allowType : CONTACT_ALLOW_TYPES) {
            if (allowType.equalsIgnoreCase(fileType)) {
                checkResult = true;
                break;
            }
        }
        return checkResult;
    }

    /**
     * 验证声音文件
     * 
     * @param fileName
     * @return
     */
    public static boolean checkSoundFileName(String fileName) {
        boolean checkResult = false;
        String fileType = getFileExtName(fileName);
        for (String allowType : SOUND_FILE_TYPES) {
            if (allowType.equals(fileType)) {
                checkResult = true;
                break;
            }
        }
        return checkResult;
    }

    /**
     * 得到amr的时长
     * 
     * @param file
     * @return
     * @throws IOException
     */
    public static long getAmrDuration(File file) throws IOException {
        long duration = -1;
        int[] packedSize = { 12, 13, 15, 17, 19, 20, 26, 31, 5, 0, 0, 0, 0, 0, 0, 0 };
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(file, "rw");
            long length = file.length();// 文件的长度
            int pos = 6;// 设置初始位置
            int frameCount = 0;// 初始帧数
            int packedPos = -1;
            // ///////////////////////////////////////////////////
            byte[] datas = new byte[1];// 初始数据值
            while (pos <= length) {
                randomAccessFile.seek(pos);
                if (randomAccessFile.read(datas, 0, 1) != 1) {
                    duration = length > 0 ? ((length - 6) / 650) : 0;
                    break;
                }
                packedPos = (datas[0] >> 3) & 0x0F;
                pos += packedSize[packedPos] + 1;
                frameCount++;
            }
            // ///////////////////////////////////////////////////
            duration += frameCount * 20;// 帧数*20
        } finally {
            if (randomAccessFile != null) {
                randomAccessFile.close();
            }
        }
        return duration;
    }

    /**
     * 创建文件目录
     * 
     * @param localDirName
     */
    public static void createFileDir(String localDirName) {
        File createFile6 = new File(localDirName);
        if (!createFile6.exists()) {
            createFile6.mkdirs();
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            String name = getUniqueId();
            System.out.println(name);
        }

    }

    public static String getMd5ByFile(File file) throws FileNotFoundException {
        String value = null;
        FileInputStream in = new FileInputStream(file);
        try {
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }

}
