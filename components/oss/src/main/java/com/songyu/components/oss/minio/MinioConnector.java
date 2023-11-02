package com.songyu.components.oss.minio;

import cn.hutool.core.date.StopWatch;
import com.songyu.commonutils.CommonFileUtils;
import io.minio.*;
import io.minio.messages.Item;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Minio 连接器
 * </p>
 *
 * @author songYu
 * @since 2023/10/20 17:28
 */
public class MinioConnector {

    private MinioClient minioClient;

    public MinioConnector(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @SneakyThrows
    public static void connect() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("连接 minio 客户端");
        MinioClient minioClient =
                MinioClient.builder()
                        .endpoint("http://175.178.65.6:9000")
                        .credentials("sTfWjZ86BF0irn8ZTBga", "4jxQGezhjqRSAbB6xQJqV62ZL423tsurh3kxRt5n")
                        .build();
        String BUCKET_NAME = "video";
        String VIDEO_PATH = "ubuntu.iso";
        BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder()
                .bucket(BUCKET_NAME).build();
        stopWatch.stop();
        stopWatch.start("重置桶");
        if (minioClient.bucketExists(bucketExistsArgs)) {
            Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().bucket(BUCKET_NAME).build());
            for (Result<Item> next : results) {
                minioClient.removeObject(RemoveObjectArgs.builder().bucket(BUCKET_NAME).object(next.get().objectName()).build());
            }
            minioClient.removeBucket(RemoveBucketArgs.builder().bucket(BUCKET_NAME).build());
        }
        minioClient.makeBucket(MakeBucketArgs.builder().bucket(BUCKET_NAME).build());
        // Upload unknown sized input stream.
        stopWatch.stop();
        stopWatch.start("上传文件");
        // 配置并行上传选项
        try (InputStream inputStream = CommonFileUtils.getInputStreamFromClassPath(VIDEO_PATH)) {
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(BUCKET_NAME).object(VIDEO_PATH).stream(
                                    inputStream, -1, 1024 * 1024 * 100)
                            .build());
        }
        stopWatch.stop();
        StopWatch.TaskInfo[] taskInfo = stopWatch.getTaskInfo();
        for (StopWatch.TaskInfo info : taskInfo) {
            System.out.printf("%s 耗时 %dms%n", info.getTaskName(), info.getTime(TimeUnit.MILLISECONDS));
        }
        minioClient.traceOff();
    }


}
