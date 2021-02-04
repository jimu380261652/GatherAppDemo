package com.jimu.dev;

import com.xiaomi.infra.galaxy.fds.client.FDSClientConfiguration;
import com.xiaomi.infra.galaxy.fds.client.GalaxyFDS;
import com.xiaomi.infra.galaxy.fds.client.GalaxyFDSClient;
import com.xiaomi.infra.galaxy.fds.client.credential.BasicFDSCredential;
import com.xiaomi.infra.galaxy.fds.client.credential.GalaxyFDSCredential;
import com.xiaomi.infra.galaxy.fds.client.exception.GalaxyFDSClientException;
import com.xiaomi.infra.galaxy.fds.model.HttpMethod;

import java.io.IOException;
import java.net.URI;
import java.util.Date;

/**
 * Created by Ljh on 2021/2/1.
 * Description:
 */
public class FDSDemo {
    String AccountKey = "5681741334680";
    String AccountSecret = "6XnU0N+KFYho+hlAHO4uAw==";
    //
    String x18AppID = "2882303761518956094";
    String x18AppKey = "5211895679094";
    String x18AppSecret = "XM774OYYQ+Ej+NQyA5TptQ==";
    //
    String baseUrl = "http://files.fds.api.xiaomi.com/";
    String bucketDir = "fridge-rear-camera-pic/pic.jpg?";
    //
    private static final String APP_ACCESS_KEY = "5211895679094"; // AppKey
    private static final String APP_ACCESS_SECRET = "XM774OYYQ+Ej+NQyA5TptQ=="; // AppSecret

    private static final String BUCKET_NAME = "fridge-rear-camera-pic"; // 创建的Bucket名字
    private static final String OBJECT_NAME = "pic.jpg"; // 上传的Object名字,可自定义

    public static void testMakeUrl()
            throws GalaxyFDSClientException, IOException {
        GalaxyFDSCredential credential = new BasicFDSCredential(
                APP_ACCESS_KEY, APP_ACCESS_SECRET);

        // Construct the GalaxyFDSClient object
        // set endpoint according to your needs.
        String endpoint = "staging-cnbj2-fds.api.xiaomi.net"; // 确认与Bucket所在region一致
        FDSClientConfiguration fdsConfig = new FDSClientConfiguration(endpoint);
        fdsConfig.enableHttps(true);
        // do not upload object via cdn in this client
        fdsConfig.enableCdnForUpload(false);
        // download object via cdn in this client
        fdsConfig.enableCdnForDownload(true);
        GalaxyFDS fdsClient = new GalaxyFDSClient(credential, fdsConfig);

        // 生成预签名链接，用于上传文件到指定bucket下
        URI uri = fdsClient.generatePresignedUri(BUCKET_NAME, OBJECT_NAME,
                new Date(System.currentTimeMillis() + 1000 * 20 * 60), HttpMethod.PUT);
        System.out.println(uri.toASCIIString());
    }
}

