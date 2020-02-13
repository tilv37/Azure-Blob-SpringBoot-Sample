package com.haramasu.azureblob;

import com.azure.core.http.rest.PagedIterable;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.models.BlobItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class HomeController {

    @Autowired
    private BlobContainerClient blobContainerClient;

    @PostMapping("/upload")
    public ResponseEntity upload(@RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        BlobClient blobClient = blobContainerClient.getBlobClient(file.getOriginalFilename());
        blobClient.upload(file.getInputStream(),file.getSize());
        String blobUrl = blobClient.getBlobUrl();
        return ResponseEntity.ok(null);
    }

    @GetMapping("/blobs")
    public ResponseEntity getAllBlobs(){
        PagedIterable<BlobItem> blobItems = blobContainerClient.listBlobs();
        List a=new ArrayList();
        for (BlobItem blobItem : blobItems) {
            a.add(blobItem.getName());
        }
        return ResponseEntity.ok(a);
    }

    @GetMapping("/download")
    public ResponseEntity download(@RequestParam(name = "fileName")String fileName, HttpServletResponse response) throws IOException {
        BlobClient blobClient = blobContainerClient.getBlobClient(fileName);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=myDoc.docx");
        ByteArrayOutputStream outputStream1=new ByteArrayOutputStream();
        blobClient.download(outputStream1);
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(outputStream1.size())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new ByteArrayResource(outputStream1.toByteArray()));
    }
}
