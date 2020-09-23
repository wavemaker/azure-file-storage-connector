package com.wavemaker.connector.azurefilestorage.model;

/**
 * Created by saraswathir on 18/9/20
 */
public class AzureFile {
    private String name;
    private boolean directory;
    private Long fileSize;

    public AzureFile() {
    }

    public String getName() {
        return name;
    }

    public AzureFile setName(String name) {
        this.name = name;
        return this;
    }

    public boolean isDirectory() {
        return directory;
    }

    public AzureFile setDirectory(boolean directory) {
        this.directory = directory;
        return this;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public AzureFile setFileSize(Long fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    @Override
    public String toString() {
        return "AzureFile{" +
                "name='" + name + '\'' +
                ", directory=" + directory +
                ", fileSize=" + fileSize +
                '}';
    }
}
