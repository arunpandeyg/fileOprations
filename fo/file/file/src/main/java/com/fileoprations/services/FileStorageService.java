package com.fileoprations.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


@Service
public class FileStorageService {
   private final Path fileStoragePath;
    private final String fileStorageLocation;
    public FileStorageService(@Value("${file.storage.location:temp}") String fileStorageLocation) {
       this. fileStorageLocation = fileStorageLocation;

        fileStoragePath = Paths.get(fileStorageLocation).toAbsolutePath().normalize();
        try{
            Files.createDirectories(fileStoragePath);
        }catch (IOException e){
           throw  new RuntimeException("something went wrong while creating directory!!");
        }
    }

    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Path filePath = Paths.get(fileStoragePath + "\\" + fileName);
        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("something went wrong while storing file!!", e);
        }
        return fileName;
    }

    public Resource downloadFile(String fileName) {
        Path path = Paths.get(fileStorageLocation).toAbsolutePath().resolve(fileName);
        Resource resource;
        try {
            resource = new UrlResource(path.toUri());
                  return resource;

        } catch (MalformedURLException e) {
            throw new RuntimeException("something went wrong while downloading the file !!", e);
        }

    }
}
