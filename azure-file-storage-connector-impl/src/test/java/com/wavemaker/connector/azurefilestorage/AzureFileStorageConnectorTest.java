package com.wavemaker.connector.azurefilestorage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wavemaker.connector.azurefilestorage.model.AzureFile;
import com.wavemaker.connector.azurefilestorage.model.AzureFileResponse;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AzureFileStorageConnectorTestConfiguration.class)
public class AzureFileStorageConnectorTest {

    @Autowired
    private AzureFileStorageConnector connectorInstance;

    @Test
    public void createShare() {
        String shareName = "qashare";
        connectorInstance.createShare(shareName);
    }

    @Test
    public void listShares() {
        List<String> strings = connectorInstance.listShares();
        System.out.println("Shares list: " + strings);
    }

    @Test
    public void createDirectory() {
        String shareName = "qadirshare";
        connectorInstance.createShare(shareName);
        String directoryName = "qadirl1/qadirl2/qadirl3";
        connectorInstance.createDirectory(shareName, directoryName);
        connectorInstance.deleteShare(shareName);
    }

    @Test
    public void deleteDirectory() {
        String shareName = "qadeldir";
        connectorInstance.createShare(shareName);
        String resourcePath = "qadirl1/qadirl2/qadirl3";
        connectorInstance.createDirectory(shareName, resourcePath);
        connectorInstance.deleteDirectory(shareName, "qadirl1", true);
        connectorInstance.deleteShare(shareName);
    }

    @Test
    public void listFiles() {
        String shareName = "qalistdirshare";
        connectorInstance.createShare(shareName);
        List<AzureFile> infos = connectorInstance.listFiles(shareName, null);
        System.out.println("Parent Folder Info: " + infos);
        connectorInstance.deleteShare(shareName);
    }

    @Test
    public void uploadData() {
        try {
            String shareName = "qauploadshare";
            connectorInstance.createShare(shareName);
            String filePath = "qadirl1/qadirl2/qadirl3/sample.txt";
            byte[] bytes = FileUtils.readFileToByteArray(new File("/tmp/data.json"));
            boolean data = connectorInstance.upload(shareName, filePath, new ByteArrayInputStream(bytes));
            boolean data1 = connectorInstance.upload(shareName, "sample.txt", new ByteArrayInputStream(bytes));
            System.out.println(data);
            System.out.println(data1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void downloadFile() {
        try {
            String shareName = "qadownloadshare";
            connectorInstance.createShare(shareName);
            String parentDir = "qadirl1/qadirl2/sample.txt";
            byte[] bytes = FileUtils.readFileToByteArray(new File("/tmp/data.json"));
            connectorInstance.upload(shareName, parentDir, new ByteArrayInputStream(bytes));
            OutputStream outputStream = connectorInstance.downloadFile(shareName, parentDir);
            System.out.println(outputStream.toString());
            connectorInstance.deleteShare(shareName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void downloadFileWithResponse() {
        try {
            String shareName = "qadfpshare";
            connectorInstance.createShare(shareName);
            String parentDir = "qadirl1/qadirl2/sample.txt";
            byte[] bytes = FileUtils.readFileToByteArray(new File("/tmp/data.json"));
            connectorInstance.upload(shareName, parentDir, new ByteArrayInputStream(bytes));
            AzureFileResponse outputStream = connectorInstance.downloadFileWithProperties(shareName, parentDir);
            System.out.println(outputStream.toString());
            connectorInstance.deleteShare(shareName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deleteFile() {
        try {
            String shareName = "qadelfileshare";
            connectorInstance.createShare(shareName);
            String parentDir = "qadirl1/qadirl2/sample.txt";
            byte[] bytes = FileUtils.readFileToByteArray(new File("/tmp/data.json"));
            connectorInstance.upload(shareName, parentDir, new ByteArrayInputStream(bytes));
            connectorInstance.deleteFile(shareName, parentDir);
            connectorInstance.deleteShare(shareName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deleteShare() {
        String shareName = "qadelshare";
        connectorInstance.createShare(shareName);
        connectorInstance.deleteShare(shareName);
    }

}