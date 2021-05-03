package com.wavemaker.connector.azurefilestorage;

import com.azure.core.http.rest.PagedIterable;
import com.azure.core.util.Context;
import com.azure.storage.file.share.ShareFileClient;
import com.azure.storage.file.share.models.ShareFileItem;
import com.azure.storage.file.share.models.ShareFileProperties;
import com.azure.storage.file.share.models.ShareItem;
import com.azure.storage.file.share.models.ShareStorageException;
import com.wavemaker.connector.azurefilestorage.exception.AzureFileStorageException;
import com.wavemaker.connector.azurefilestorage.exception.ExceptionMessage;
import com.wavemaker.connector.azurefilestorage.model.AzureFile;
import com.wavemaker.connector.azurefilestorage.model.AzureFileResponse;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Primary
public class AzureFileStorageConnectorImpl extends AbstractAzureFileShareClient {

    private static final Logger logger = LoggerFactory.getLogger(AzureFileStorageConnectorImpl.class);

    /**
     * Creates a share in the storage account with the specified name.
     * This will create a new Share if the specified Share Name doesnot exists in the storage account.
     * If the share already exists then do nothing
     *
     * @param shareName Name of the share
     */
    @Override
    public void createShare(String shareName) {
        try {
            if (!getShareServiceClient().getShareClient(shareName).exists()) {
                getShareServiceClient().createShare(shareName);
                logger.info("Share Created in Azure Account with share Name: {}", shareName);
            }
        } catch (ShareStorageException e) {
            throw new AzureFileStorageException(ExceptionMessage.SHARE_CREATION, e.getMessage());
        }
    }

    /**
     * Lists all shares in the storage account.
     * This will return all the share names if no share exists then empty set will be returned.
     *
     * @return {@link String Share Name} in the storage directory
     */
    @Override
    public List<String> listShares() {
        List<String> shareNames = new ArrayList<>();
        try {
            PagedIterable<ShareItem> shareItems = getShareServiceClient().listShares();
            for (ShareItem shareItem : shareItems) {
                shareNames.add(shareItem.getName());
            }
            return shareNames;
        } catch (ShareStorageException e) {
            throw new AzureFileStorageException(ExceptionMessage.LIST_SHARES, e.getMessage());
        }
    }

    /**
     * Deletes the share in the storage account with the given name
     * This will delete the share if exists.
     * If the specified share does not exists then this will throw an exception
     *
     * @param shareName Name of the share
     */
    @Override
    public void deleteShare(String shareName) {
        try {
            getShareServiceClient().deleteShare(shareName);
        } catch (ShareStorageException e) {
            throw new AzureFileStorageException(ExceptionMessage.DELETE_SHARE, e.getMessage());
        }
    }

    /**
     * Creates a directory in the file share
     * The directory path should be seperated using File.seperator and creates all the directories in the path if does not exists
     *
     * @param shareName     Name of the share
     * @param directoryPath Directory path to create
     */
    @Override
    public void createDirectory(String shareName, String directoryPath) {
        try {
            String[] split = directoryPath.split(File.separator);
            String path = null;
            for (String s : split) {
                if (path != null) {
                    path = path + File.separator + s;
                } else {
                    path = s;
                }
                if (!getShareServiceClient().getShareClient(shareName).getDirectoryClient(path).exists()) {
                    getShareServiceClient().getShareClient(shareName).getDirectoryClient(path).create();
                }
            }
            logger.info("Directory with name: {} created successfully under share:{} ", directoryPath, shareName);
        } catch (ShareStorageException e) {
            throw new AzureFileStorageException(ExceptionMessage.CREATE_DIRECTORY, e.getMessage());
        }
    }

    /**
     * Deletes the directory in the file share.This will delete all sub-directories and files present in the given directory.
     * If force is set to true then this will delete all the inner sub-directories and files.
     * If force is set to false then this will throw an exception if the given directory is not empty.
     *
     * @param shareName     Name of the Share
     * @param directoryPath Directory path to delete
     * @param force         true/false
     */
    @Override
    public void deleteDirectory(String shareName, String directoryPath, boolean force) {
        try {
            deleteDirectories(shareName, directoryPath, force);
        } catch (ShareStorageException e) {
            throw new AzureFileStorageException(ExceptionMessage.DELETE_DIRECTORY, e.getMessage());
        }
    }

    private void deleteDirectories(String shareName, String directoryPath, boolean force) {
        if (force) {
            PagedIterable<ShareFileItem> shareFileItems = getShareServiceClient().getShareClient(shareName).getDirectoryClient(directoryPath).listFilesAndDirectories();
            for (ShareFileItem item : shareFileItems) {
                if (item.isDirectory()) {
                    String path = directoryPath + File.separator + item.getName();
                    deleteDirectories(shareName, path, force);
                } else {
                    getShareServiceClient().getShareClient(shareName).getDirectoryClient(directoryPath).deleteFile(item.getName());
                }
            }
        }
        getShareServiceClient().getShareClient(shareName).getDirectoryClient(directoryPath).delete();
    }

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
    @Override
    public List<AzureFile> listFiles(String shareName, String directoryPath) {
        List<AzureFile> fileOrFolderInfos = new ArrayList<>();
        try {
            PagedIterable<ShareFileItem> shareFileItems = null;
            if (directoryPath == null || directoryPath.equals("")) {
                shareFileItems = getShareServiceClient().getShareClient(shareName).getRootDirectoryClient().listFilesAndDirectories();
            } else {
                shareFileItems = getShareServiceClient().getShareClient(shareName).getDirectoryClient(directoryPath).listFilesAndDirectories();
            }
            if (shareFileItems.iterableByPage().iterator().hasNext()) {
                for (ShareFileItem shareFileItem : shareFileItems) {
                    fileOrFolderInfos.add(new AzureFile()
                            .setName(shareFileItem.getName())
                            .setDirectory(shareFileItem.isDirectory())
                            .setFileSize(shareFileItem.getFileSize()));
                }
            }
            return fileOrFolderInfos;
        } catch (ShareStorageException e) {
            throw new AzureFileStorageException(ExceptionMessage.LIST_FILES_AND_DIRECTORIES, e.getMessage());
        }

    }

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
    @Override
    public Boolean upload(String shareName, String filePath, InputStream uploadData) {
        try {
            long bytes = uploadData.available();
            createFile(shareName, filePath, bytes);
            ShareFileClient fileClient = getShareServiceClient().getShareClient(shareName).getFileClient(filePath);
            long chunkSize = 4194304L; //ShareFileAsyncClient.FILE_DEFAULT_BLOCK_SIZE //It is private

            if (bytes > chunkSize) {
                byte[] byteArray = IOUtils.toByteArray(uploadData);

                for (int offset = 0; offset < bytes; offset += chunkSize) {
                    try {
                        // the last chunk size is smaller than the others
                        chunkSize = Math.min(bytes - offset, chunkSize);

                        // select the chunk in the byte array
                        byte[] subArray = Arrays.copyOfRange(byteArray, offset, (int) (offset + chunkSize));

                        // upload the chunk
                        fileClient.uploadWithResponse(new ByteArrayInputStream(subArray), chunkSize, (long) offset, null, Context.NONE);
                    } catch (RuntimeException e) {
                        logger.error("Failed to upload the file chunks", e);
                        if (Boolean.TRUE.equals(fileClient.exists())) {
                            fileClient.delete();
                        }
                        throw e;
                    }
                }
            } else {
                fileClient.upload(uploadData, bytes);
            }

            return true;
        } catch (ShareStorageException | IOException e) {
            throw new AzureFileStorageException(ExceptionMessage.UPLOAD_DATA_TO_FILE, e.getMessage());
        }
    }

    private void createFile(String shareName, String filePath, long maxSize) {
        try {
            if (!filePath.contains(File.separator)) {
                getShareServiceClient().getShareClient(shareName).getFileClient(filePath).create(maxSize);
            } else {
                String directory = filePath.substring(0, filePath.lastIndexOf(File.separator));
                String fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1, filePath.length());
                createDirectory(shareName, directory);
                Boolean exists = getShareServiceClient().getShareClient(shareName).getDirectoryClient(directory).getFileClient(fileName).exists();
                if (!exists) {
                    getShareServiceClient().getShareClient(shareName).getDirectoryClient(directory).createFile(fileName, maxSize);
                }
            }
        } catch (ShareStorageException e) {
            throw new AzureFileStorageException(ExceptionMessage.CREATE_FILE, e.getMessage());
        }
    }

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
    @Override
    public OutputStream downloadFile(String shareName, String filePath) {
        try {
            OutputStream stream = new ByteArrayOutputStream();
            getShareServiceClient().getShareClient(shareName).getFileClient(filePath).download(stream);
            return stream;
        } catch (ShareStorageException e) {
            throw new AzureFileStorageException(ExceptionMessage.DOWNLOAD_FILE, e.getMessage());
        }
    }

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
    @Override
    public AzureFileResponse downloadFileWithProperties(String shareName, String filePath) {
        try {
            OutputStream stream = new ByteArrayOutputStream();
            if (!getShareServiceClient().getShareClient(shareName).getFileClient(filePath).exists()) {
                throw new AzureFileStorageException(ExceptionMessage.NO_FILE);
            }
            getShareServiceClient().getShareClient(shareName).getFileClient(filePath).download(stream);
            ShareFileProperties properties = getShareServiceClient().getShareClient(shareName).getFileClient(filePath).getProperties();
            return new AzureFileResponse()
                    .setOutputStream(stream)
                    .setFileId(properties.getSmbProperties().getFileId())
                    .setFileType(properties.getFileType())
                    .setContentLength(properties.getContentLength())
                    .setContentMd5(properties.getContentMd5())
                    .setContentType(properties.getContentType())
                    .setLastModified(properties.getLastModified());
        } catch (ShareStorageException e) {
            throw new AzureFileStorageException(ExceptionMessage.DOWNLOAD_FILE, e.getMessage());
        }
    }

    /**
     * Deletes the file associate with the client.
     * If method will delete if file exists and if file doesnot exists do noting.
     * The filepath should be specified using File.seperator to specify file along with directory path
     * This will throw an exception if the given file doesnot exists at given path
     *
     * @param shareName Name of the Share
     * @param filePath  path of the file to be deleted
     */
    @Override
    public void deleteFile(String shareName, String filePath) {
        try {
            Boolean exists = getShareServiceClient().getShareClient(shareName).getFileClient(filePath).exists();
            if (exists) {
                getShareServiceClient().getShareClient(shareName).getFileClient(filePath).delete();
            }
        } catch (ShareStorageException e) {
            throw new AzureFileStorageException(ExceptionMessage.DELETE_FILE, e.getMessage());
        }
    }
}
