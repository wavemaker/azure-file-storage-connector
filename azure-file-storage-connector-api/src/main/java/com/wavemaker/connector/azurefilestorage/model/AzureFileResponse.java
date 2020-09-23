package com.wavemaker.connector.azurefilestorage.model;

import java.io.OutputStream;
import java.time.OffsetDateTime;
import java.util.Arrays;

/**
 * Created by saraswathir on 19/9/20
 */
public class AzureFileResponse {
    private String fileId;
    private OffsetDateTime lastModified;
    private String fileType;
    private Long contentLength;
    private String contentType;
    private byte[] contentMd5;
    private OutputStream outputStream;

    public AzureFileResponse() {
    }

    public String getFileId() {
        return fileId;
    }

    public AzureFileResponse setFileId(String fileId) {
        this.fileId = fileId;
        return this;
    }

    public OffsetDateTime getLastModified() {
        return lastModified;
    }

    public AzureFileResponse setLastModified(OffsetDateTime lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    public String getFileType() {
        return fileType;
    }

    public AzureFileResponse setFileType(String fileType) {
        this.fileType = fileType;
        return this;
    }

    public Long getContentLength() {
        return contentLength;
    }

    public AzureFileResponse setContentLength(Long contentLength) {
        this.contentLength = contentLength;
        return this;
    }

    public String getContentType() {
        return contentType;
    }

    public AzureFileResponse setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public byte[] getContentMd5() {
        return contentMd5;
    }

    public AzureFileResponse setContentMd5(byte[] contentMd5) {
        this.contentMd5 = contentMd5;
        return this;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public AzureFileResponse setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
        return this;
    }

    @Override
    public String toString() {
        return "AzureFileResponse{" +
                "fileId='" + fileId + '\'' +
                ", lastModified=" + lastModified +
                ", fileType='" + fileType + '\'' +
                ", contentLength=" + contentLength +
                ", contentType='" + contentType + '\'' +
                ", contentMd5=" + Arrays.toString(contentMd5) +
                ", outputStream=" + outputStream +
                '}';
    }
}

