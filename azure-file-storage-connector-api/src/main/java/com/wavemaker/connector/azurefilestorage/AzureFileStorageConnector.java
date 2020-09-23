package com.wavemaker.connector.azurefilestorage;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.wavemaker.connector.azurefilestorage.model.AzureFile;
import com.wavemaker.connector.azurefilestorage.model.AzureFileResponse;
import com.wavemaker.runtime.connector.annotation.WMConnector;


@WMConnector(name = "azurefilestorage",
        description = "A simple connector azurefilestorage that can be used in WaveMaker application")
public interface AzureFileStorageConnector {

    /**
     * Creates a share in the storage account with the specified name.
     * This will create a new Share if the specified Share Name doesnot exists in the storage account.
     * If the share already exists then do nothing
     *
     * @param shareName Name of the share
     */
    void createShare(String shareName);

    /**
     * Lists all shares in the storage account.
     * This will return all the share names if no share exists then empty set will be returned.
     *
     * @return {@link String Share Name} in the storage directory
     */
    List<String> listShares();

    /**
     * Deletes the share in the storage account with the given name
     * This will delete the share if exists.
     * If the specified share does not exists then this will throw an exception
     *
     * @param shareName Name of the share
     */
    void deleteShare(String shareName);

    /**
     * Creates a directory in the file share
     * The directory path should be seperated using File.seperator and creates all the directories in the path if does not exists
     *
     * @param shareName     Name of the share
     * @param directoryPath Directory path to create
     */
    void createDirectory(String shareName, String directoryPath);

    /**
     * Deletes the directory in the file share.This will delete all sub-directories and files present in the given directory.
     * If force is set to true then this will delete all the inner sub-directories and files.
     * If force is set to false then this will throw an exception if the given directory is not empty.
     *
     * @param shareName     Name of the Share
     * @param directoryPath Directory path to delete
     * @param force         true/false
     */
    void deleteDirectory(String shareName, String directoryPath, boolean force);

    /**
     * Lists all sub-directories and files in given directory path.
     * This will return empty set no file or directory exists in the specified directory path.
     * The directory path should be seperated with File.seperator.
     *
     * @param shareName     Name of the Share
     * @param directoryPath directory path directories seperated using File.seperator
     *
     * @return {@link AzureFile File info} in the storage directory
     */
    List<AzureFile> listFiles(String shareName, String directoryPath);

    /**
     * Uploads data to file in storage file service. Upload operations performs an write on the specified file.
     * If the file doesnot exists in the specified file path then this method will create directories and files using filepath.
     * If the data in the file needs to be cleared then send empty bytes[] of InputStream.
     * The filepath should have File.seperator for specifying the path
     *
     * @param shareName  Name of the Share
     * @param filePath   path of the file this will create the directories and file if not exists
     * @param uploadData Data to write into the file
     *
     * @return returns true if file is uploaded successfully
     */
    Boolean upload(String shareName, String filePath, InputStream uploadData);

    /**
     * Download the file from the storage account.
     * This will return the output stream of the file that is present at the specified filepath.
     * If file doesnot exists then this method will throw an exception.
     *
     * @param shareName Name of the Share
     * @param filePath  path of the file to be downloaded
     *
     * @return {@link OutputStream Stream info} in the storage directory
     */
    OutputStream downloadFile(String shareName, String filePath);

    /**
     * Download the file with file properties.
     * This will return the output stream along with file properties from the storage account.
     * If file doesnot exists then this method will throw an exception.
     *
     * @param shareName Name of the Share
     * @param filePath  path of the file to be downloaded
     *
     * @return {@link AzureFileResponse file info} in the storage directory
     */
    AzureFileResponse downloadFileWithProperties(String shareName, String filePath);

    /**
     * Deletes the file associate with the client.
     * If method will delete if file exists and if file doesnot exists do noting.
     * The filepath should be specified using File.seperator to specify file along with directory path
     * This will throw an exception if the given file doesnot exists at given path
     *
     * @param shareName Name of the Share
     * @param filePath  path of the file to be deleted
     */
    void deleteFile(String shareName, String filePath);

}