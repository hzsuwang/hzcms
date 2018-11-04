package com.hzsuwang.hzcms.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CompleteMultipartUploadRequest;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.InitiateMultipartUploadRequest;
import com.aliyun.oss.model.InitiateMultipartUploadResult;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PartETag;
import com.aliyun.oss.model.UploadPartRequest;
import com.aliyun.oss.model.UploadPartResult;

/**
 * 该示例代码展示了如何使用OSS的Multipart上传方式进行多线程分段上传较大文件。 该示例代码执行的过程为： 1. 检查指定的Bucket是否存在，如果不存在则创建它； 2.
 * 根据文件的大小计算应该将文件分成多少个Part进行上传； 3. 初始化Multipart上传请求； 4. 使用ExecutorService并发在上传每个Part； 5. 如果所有Part均上传成功，则完成这个上传请求； 6.
 * 最后清理掉测试资源：删除上传的Object或未完成的Multipart上传请求，并删除这个Bucket。 尝试运行这段示例代码时需要注意： 1.
 * 为了展示在删除Bucket时除了需要删除其中的Objects，也需要取消掉未完成的Multipart uploads， 示例代码最后为删除掉指定的Bucket，因为不要使用您的已经有资源的Bucket进行测试！ 2.
 * 请使用您的API授权密钥填充ACCESS_ID和ACCESS_KEY常量； 3. 需要准确上传用的测试文件，该文件大小要大于一个Part的最小字节数5MB，但不能大于 一个Object允许的最大字节数5GB。
 * 并修改常量UPLOAD_FILE_PATH为测试文件的路径；修改常量DOWNLOAD_FILE_PATH为下载文件的路径。 4. 该程序仅为示例代码，仅供参考，并不能保证足够健壮。
 **/
public class AliyunOssUtil {

    private static final long PART_SIZE     = 512 * 1024L; // 每个Part的大小，最小为500kb
    private static final int  CONCURRENCIES = 2;               // 上传Part的并发线程数。

    @Autowired
    private PropertyBean      propertyBean;

    private static OSSClient  client        = null;

    private OSSClient getOSSClient() {
        if (client == null) {
            client = new OSSClient(propertyBean.getAliyunOssUrl(), propertyBean.getAliyunAccessId(), propertyBean.getAliyunAccessKey());
        }
        return client;
    }

    // 通过Multipart的方式上传一个大文件
 // 要上传文件的大小必须大于一个Part允许的最小大小，即500kb。
    public boolean uploadBigFile(String bucketName, String key, File uploadFile) throws OSSException, ClientException, InterruptedException {
        if (!propertyBean.isProduction()) {
            return true;
        }
        // 不能以/开头
        key = key.substring(1, key.length());

        boolean result = false;
        try {
            int partCount = calPartCount(uploadFile);
            if (partCount <= 1) {
                return false;
                // throw new IllegalArgumentException("要上传文件的大小必须大于一个Part的字节数：" + PART_SIZE);
            }
            String uploadId = initMultipartUpload(bucketName, key);
            ExecutorService pool = Executors.newFixedThreadPool(CONCURRENCIES);
            List<PartETag> eTags = Collections.synchronizedList(new ArrayList<PartETag>());
            for (int i = 0; i < partCount; i++) {
                long start = PART_SIZE * i;
                long curPartSize = PART_SIZE < uploadFile.length() - start ? PART_SIZE : uploadFile.length() - start;
                pool.execute(new UploadPartThread(getOSSClient(), bucketName, key, uploadFile, uploadId, i + 1, PART_SIZE * i, curPartSize, eTags));
            }
            pool.shutdown();
            while (!pool.isTerminated()) {
                pool.awaitTermination(5, TimeUnit.SECONDS);
            }
            if (eTags.size() != partCount) {
                // throw new IllegalStateException("Multipart上传失败，有Part未上传成功。");
                return result;
            }
            completeMultipartUpload(bucketName, key, uploadId, eTags);
            result = true;
            return result;
        } catch (Exception ex) {
            return false;
        }
    }

    // 根据文件的大小和每个Part的大小计算需要划分的Part个数。
    private int calPartCount(File f) {
        int partCount = (int) (f.length() / PART_SIZE);
        if (f.length() % PART_SIZE != 0) {
            partCount++;
        }
        return partCount;
    }

    // 初始化一个Multi-part upload请求。
    private String initMultipartUpload(String bucketName, String key) throws OSSException, ClientException {
        InitiateMultipartUploadRequest initUploadRequest = new InitiateMultipartUploadRequest(bucketName, key);
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentType("audio/mpeg");
        initUploadRequest.setObjectMetadata(meta);
        InitiateMultipartUploadResult initResult = getOSSClient().initiateMultipartUpload(initUploadRequest);
        return initResult.getUploadId();
    }

    // 完成一个multi-part请求。
    private void completeMultipartUpload(String bucketName, String key, String uploadId, List<PartETag> eTags) throws OSSException, ClientException {
        // 为part按partnumber排序
        Collections.sort(eTags, new Comparator<PartETag>() {

            public int compare(PartETag arg0, PartETag arg1) {
                PartETag part1 = arg0;
                PartETag part2 = arg1;
                return part1.getPartNumber() - part2.getPartNumber();
            }
        });
        CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(bucketName, key, uploadId, eTags);
        getOSSClient().completeMultipartUpload(completeMultipartUploadRequest);
    }

    private class UploadPartThread implements Runnable {

        private File           uploadFile;
        private String         bucket;
        private String         object;
        private long           start;
        private long           size;
        private List<PartETag> eTags;
        private int            partId;
        private OSSClient      client;
        private String         uploadId;

        UploadPartThread(OSSClient client, String bucket, String object, File uploadFile, String uploadId, int partId, long start, long partSize, List<PartETag> eTags){
            this.uploadFile = uploadFile;
            this.bucket = bucket;
            this.object = object;
            this.start = start;
            this.size = partSize;
            this.eTags = eTags;
            this.partId = partId;
            this.client = client;
            this.uploadId = uploadId;
        }

        @Override
        public void run() {

            InputStream in = null;
            try {
                in = new FileInputStream(uploadFile);
                in.skip(start);

                UploadPartRequest uploadPartRequest = new UploadPartRequest();
                uploadPartRequest.setBucketName(bucket);
                uploadPartRequest.setKey(object);
                uploadPartRequest.setUploadId(uploadId);
                uploadPartRequest.setInputStream(in);
                uploadPartRequest.setPartSize(size);
                uploadPartRequest.setPartNumber(partId);
                
                UploadPartResult uploadPartResult = client.uploadPart(uploadPartRequest);
                
                eTags.add(uploadPartResult.getPartETag());

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (in != null) try {
                    in.close();
                } catch (Exception e) {
                }
            }
        }
    }

    // 下载Object到本地文件。
    public void downloadFile(String bucketName, String key, String downloadFilePath) throws OSSException, ClientException {
        getOSSClient().getObject(new GetObjectRequest(bucketName, key), new File(downloadFilePath));
    }
}
