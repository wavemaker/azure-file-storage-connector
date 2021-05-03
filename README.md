## Connector Introduction
Connector is a Java based backend extension for WaveMaker applications. Connectors are built as Java modules & exposes java based SDK to interact with the connector implementation.
Each connector is built for a specific purpose and can be integrated with one of the external services. Connectors are imported & used in the WaveMaker application. Each connector runs on its own container thereby providing the ability to have it’s own version of the third party dependencies.

## Features of Connectors
1. Connector is a java based extension which can be integrated with external services and reused in many Wavemaker applications.
2. Each connector can work as an SDK for an external system.
3. Connectors can be imported once in a WaveMaker application and used many times in the applications by creating multiple instances.
4. Connectors are executed in its own container in the WaveMaker application, as a result there are no dependency version conflict issues between connectors.

## About Azure File Storage Connector

### Introduction
Azure file storage mainly can be used if we want to have a shared drive between two servers or across users. In the Azure file storage structure, the first thing we need to have is an Azure storage account. Azure file storage is offered under the Azure storage account. And once we have created an Azure storage account, we'll create a file share.

We can create an unlimited number of file shares within a storage account. Once we create a file share, then we can create directories, just like folders, and then we can upload files into it.

Azure File storage (https://azure.microsoft.com/en-in/services/storage/files/)  connector provides simplified APIs to work with azure file storage to store & retrieve files from Azure Storage Service. Using this connector, one can build the Upload, Download, List & Delete operations of files in Azure storage and integrate with WaveMaker application.

### Prerequisite
1. Azure Storage access with connectionString
2. Java 1.8 or above
4. Maven 3.1.0 or above
5. Any java editor such as Eclipse, Intelij..etc
6. Internet connection

### Build

You can build this connector using following command
```Java
mvn clean install
```
### Deploy

You can import connector dist/azure-file-storage-connector.zip artifact in WaveMaker studio under file explorer.On after uploading into 
wavemaker, you can update your profile properties such connectionString.

### Using Azure File Storage Connector in WaveMaker
```Java
@Autowired
private AzureFileStorageConnector azureFileStorageConnector;

azureFileStorageConnector.upload(shareName, filePath, inputStream);

azureFileStorageConnector.downloadFile(shareName, filePath)
```

Apart from above apis, there are other apis exposed in this connector, please visit connector interface in the api module.
