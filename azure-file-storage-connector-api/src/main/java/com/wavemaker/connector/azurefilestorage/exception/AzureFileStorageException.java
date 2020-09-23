package com.wavemaker.connector.azurefilestorage.exception;

/**
 * Created by saraswathir on 22/9/20
 */
public class AzureFileStorageException extends RuntimeException {

    public AzureFileStorageException(String message, String exceptionMessage) {
        super(String.format("Azure File Storage Exception: %s failed with exception - %s", message, exceptionMessage));
    }

    public AzureFileStorageException(String message) {
        super(String.format("Azure File Storage Exception: %s", message));
    }
}
