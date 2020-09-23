package com.wavemaker.connector.azurefilestorage;

import org.springframework.beans.factory.annotation.Value;

import com.azure.storage.file.share.ShareServiceClient;
import com.azure.storage.file.share.ShareServiceClientBuilder;
import com.wavemaker.connector.azurefilestorage.exception.AzureShareServiceClientException;

/**
 * Created by saraswathir on 23/9/20
 */
public abstract class AbstractAzureFileShareClient implements AzureFileStorageConnector {

    @Value("${azure.connectionString}")
    private String connectionString;

    private ShareServiceClient shareServiceClient;

    public ShareServiceClient getShareServiceClient() {
        if (shareServiceClient == null) {
            try {
                shareServiceClient = new ShareServiceClientBuilder()
                        .connectionString(connectionString)
                        .buildClient();
                return shareServiceClient;
            } catch (RuntimeException e) {
                throw new AzureShareServiceClientException(e.getMessage());
            }
        }
        return shareServiceClient;
    }
}