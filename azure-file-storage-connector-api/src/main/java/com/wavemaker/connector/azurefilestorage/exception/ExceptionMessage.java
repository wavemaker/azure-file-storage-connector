package com.wavemaker.connector.azurefilestorage.exception;

/**
 * Created by saraswathir on 22/9/20
 */
public class ExceptionMessage {

    public static final String SHARE_CREATION = "Unable to create share";
    public static final String LIST_SHARES = "Unable to list shares";
    public static final String DELETE_SHARE = "Unable to delete share with exception";
    public static final String CREATE_DIRECTORY = "Unable to create directories in given share";
    public static final String LIST_FILES_AND_DIRECTORIES = "Unable to list directories and files make sure directory path exists in given share";
    public static final String DELETE_DIRECTORY = "Unable to delete directories make sure directory is empty";
    public static final String CREATE_FILE = "Failed to create source client file in given directory path";
    public static final String UPLOAD_DATA_TO_FILE = "Failed to upload the data.";
    public static final String DOWNLOAD_FILE = "Failed to download file from storage";
    public static final String DELETE_FILE = "Failed to delete the file from storage";
    public static final String FILE_PROPERTIES = "Failed to get file properties";
    public static final String NO_FILE = "No Such File found in azure storage";
}
