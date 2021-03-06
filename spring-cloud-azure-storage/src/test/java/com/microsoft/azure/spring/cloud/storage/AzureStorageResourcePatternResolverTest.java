/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.azure.spring.cloud.storage;

import com.azure.core.http.rest.PagedIterable;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobContainerItem;
import com.azure.storage.blob.models.BlobItem;
import com.azure.storage.blob.specialized.BlockBlobClient;
import com.azure.storage.file.share.ShareClient;
import com.azure.storage.file.share.ShareDirectoryClient;
import com.azure.storage.file.share.ShareServiceClient;
import com.azure.storage.file.share.models.ShareFileItem;
import com.azure.storage.file.share.models.ShareItem;
import java.io.IOException;
import java.util.ArrayList;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * The JUnit tests for the AzureStorageResourcePatternResolver class.
 */
@SpringBootTest(properties = "spring.main.banner-mode=off")
@RunWith(SpringRunner.class)
public class AzureStorageResourcePatternResolverTest {

    /**
     * Stores the BlobServiceClient.
     */
    @Autowired
    private BlobServiceClient blobServiceClient;

    /**
     * Stores the ShareServiceClient.
     */
    @Autowired
    private ShareServiceClient shareServiceClient;

    /**
     * Test getResources method.
     *
     * @throws IOException when an I/O error occurs.
     */
    @Test
    public void testGetResources() throws IOException {
        AzureStorageResourcePatternResolver resolver
                = new AzureStorageResourcePatternResolver(blobServiceClient);
        assertNotNull(resolver.getResources("azure-blob://mycontainer/myblob"));
    }

    /**
     * Test getResources method.
     *
     * @throws IOException when an I/O error occurs.
     */
    @Test
    public void testGetResources2() throws IOException {
        AzureStorageResourcePatternResolver resolver
                = new AzureStorageResourcePatternResolver(blobServiceClient);
        assertNotNull(resolver.getResources("azure-blob://mycontainer/*"));
    }

    /**
     * Test getResources method.
     *
     * @throws IOException when an I/O error occurs.
     */
    @Test
    public void testGetResources3() throws IOException {
        AzureStorageResourcePatternResolver resolver
                = new AzureStorageResourcePatternResolver(blobServiceClient);
        assertNotNull(resolver.getResources("azure-blob://*/myblob"));
    }

    /**
     * Test getResources method.
     *
     * @throws IOException when an I/O error occurs.
     */
    @Test
    public void testGetResources4() throws IOException {
        AzureStorageResourcePatternResolver resolver
                = new AzureStorageResourcePatternResolver(blobServiceClient);
        assertNotNull(resolver.getResources("azure-blob://*/*"));
    }

    /**
     * Test getResources method.
     *
     * @throws IOException when an I/O error occurs.
     */
    @Test
    public void testGetResources5() throws IOException {
        AzureStorageResourcePatternResolver resolver
                = new AzureStorageResourcePatternResolver(shareServiceClient);
        assertNotNull(resolver.getResources("azure-file://myshare/myfile"));
    }

    /**
     * Test getResources method.
     *
     * @throws IOException when an I/O error occurs.
     */
    @Test
    public void testGetResources6() throws IOException {
        AzureStorageResourcePatternResolver resolver
                = new AzureStorageResourcePatternResolver(shareServiceClient);
        assertNotNull(resolver.getResources("azure-file://myshare/*"));
    }

    /**
     * Test getResources method.
     *
     * @throws IOException when an I/O error occurs.
     */
    @Test
    public void testGetResources7() throws IOException {
        AzureStorageResourcePatternResolver resolver
                = new AzureStorageResourcePatternResolver(shareServiceClient);
        assertNotNull(resolver.getResources("azure-file://*/myfile"));
    }

    /**
     * Test getResources method.
     *
     * @throws IOException when an I/O error occurs.
     */
    @Test
    public void testGetResources8() throws IOException {
        AzureStorageResourcePatternResolver resolver
                = new AzureStorageResourcePatternResolver(shareServiceClient);
        assertNotNull(resolver.getResources("azure-file://*/*"));
    }

    @Configuration
    @Import(AzureStorageProtocolResolver.class)
    static class StorageApplication {

        @Bean
        public BlobServiceClient getBlobServiceClient() {
            BlobServiceClient client = mock(BlobServiceClient.class);
            PagedIterable<BlobContainerItem> blobContainerItems = mock(PagedIterable.class);
            BlobContainerClient containerClient = mock(BlobContainerClient.class);
            PagedIterable<BlobItem> blobItems = mock(PagedIterable.class);
            BlobClient blobClient = mock(BlobClient.class);
            BlockBlobClient blockBlobClient = mock(BlockBlobClient.class);

            BlobContainerItem containerItem1 = new BlobContainerItem();
            containerItem1.setName("mycontainer");
            ArrayList containerList = new ArrayList<BlobContainerItem>();
            containerList.add(containerItem1);

            BlobItem blobItem1 = new BlobItem();
            blobItem1.setName("myblob");
            ArrayList blobList = new ArrayList<>();
            blobList.add(blobItem1);

            when(client.listBlobContainers()).thenReturn(blobContainerItems);
            when(blobContainerItems.iterator()).thenReturn(containerList.iterator());
            when(client.getBlobContainerClient("mycontainer")).thenReturn(containerClient);
            when(containerClient.listBlobs()).thenReturn(blobItems);
            when(blobItems.iterator()).thenReturn(blobList.iterator());
            when(client.getBlobContainerClient("mycontainer")).thenReturn(containerClient);
            when(containerClient.getBlobClient("myblob")).thenReturn(blobClient);
            when(blobClient.getBlockBlobClient()).thenReturn(blockBlobClient);

            return client;
        }

        @Bean
        public ShareServiceClient getShareServiceClient() {
            ShareServiceClient client = mock(ShareServiceClient.class);
            PagedIterable<ShareItem> shareItems = mock(PagedIterable.class);
            ShareClient shareClient = mock(ShareClient.class);
            ShareDirectoryClient shareDirectoryClient = mock(ShareDirectoryClient.class);
            PagedIterable<ShareFileItem> shareFileItems = mock(PagedIterable.class);

            ShareItem shareItem1 = new ShareItem();
            shareItem1.setName("myshare");
            ArrayList shareList = new ArrayList<ShareItem>();
            shareList.add(shareItem1);

            ShareFileItem shareFileItem1 = new ShareFileItem("myfile", false, 0L);
            ArrayList shareFileList = new ArrayList<ShareFileItem>();
            shareFileList.add(shareFileItem1);

            when(client.listShares()).thenReturn(shareItems);
            when(shareItems.iterator()).thenReturn(shareList.iterator());
            when(client.getShareClient("myshare")).thenReturn(shareClient);
            when(shareClient.getRootDirectoryClient()).thenReturn(shareDirectoryClient);
            when(shareDirectoryClient.listFilesAndDirectories()).thenReturn(shareFileItems);
            when(shareFileItems.iterator()).thenReturn(shareFileList.iterator());

            return client;
        }
    }
}
