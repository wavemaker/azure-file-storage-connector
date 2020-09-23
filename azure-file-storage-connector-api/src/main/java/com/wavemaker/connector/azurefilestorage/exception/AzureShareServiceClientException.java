package com.wavemaker.connector.azurefilestorage.exception;

/**
 * Created by saraswathir on 22/9/20
 */
public class AzureShareServiceClientException extends RuntimeException {

    public AzureShareServiceClientException(String exceptionMessage) {
        super(String.format("Failed to build Azure Share Service Client - %s", exceptionMessage));
    }
}
