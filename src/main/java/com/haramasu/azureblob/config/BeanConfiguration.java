package com.haramasu.azureblob.config;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class BeanConfiguration {

    @Bean
    public BlobServiceClient blobServiceClient(@Value("${blobEndpoint}")String endPoint,
                                               @Value("${sasToken}")String  sasToken){
        return new BlobServiceClientBuilder()
                .endpoint(endPoint)
                .sasToken(sasToken)
                .buildClient();
    }

    @Bean
    public BlobContainerClient containerClient(BlobServiceClient blobServiceClient,
                                               @Value("${containerName}")String  containerName){
        return blobServiceClient.getBlobContainerClient(containerName);

    }
}
