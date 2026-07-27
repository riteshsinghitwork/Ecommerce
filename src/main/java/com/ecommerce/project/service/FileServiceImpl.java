package com.ecommerce.project.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService{
    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {String originalFileName = file.getOriginalFilename();
        // Print current working directory
        System.out.println("Current Directory : " + System.getProperty("user.dir"));

        // Print absolute image path
        System.out.println("Image Folder Path : " + Paths.get(path).toAbsolutePath());
        String randomId = UUID.randomUUID().toString();
        String fileName = randomId.concat(originalFileName.substring(originalFileName.lastIndexOf('.')));
        String filePath = path + File.separator + fileName;
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
            System.out.println("Folder Created Successfully");
        }
        Files.copy(file.getInputStream(), Paths.get(filePath));
        System.out.println("Image Saved Successfully : " + filePath);
        return fileName;
    }
}
