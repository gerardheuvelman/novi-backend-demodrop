package nl.ultimateapps.demoDrop.Services;

import lombok.Getter;
import lombok.Setter;
import nl.ultimateapps.demoDrop.Models.File;
import nl.ultimateapps.demoDrop.Repositories.FileRepository;
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
import java.util.Objects;

@Service
public class FileService {

    @Value("${my.upload_location}")
    @Getter
    @Setter
    private Path fileStoragePath;
    private final String fileStorageLocation;

    private final FileRepository repo;

    public FileService(@Value("${my.upload_location}") String fileStorageLocation, FileRepository repo) {
        fileStoragePath = Paths.get(fileStorageLocation).toAbsolutePath().normalize();
        this.fileStorageLocation = fileStorageLocation;
        this.repo = repo;
        try {
            Files.createDirectories(fileStoragePath);
        } catch (IOException e) {
            throw new RuntimeException("Issue in creating file directory");
        }
    }


    public String processFile(MultipartFile file, String url) {
        MultipartFile convertedFile = convertFile(file);
        return storeFile(convertedFile, url);
    }

    public Resource downLoadFile(String fileName) {
        Path path = Paths.get(fileStorageLocation).toAbsolutePath().resolve(fileName);
        Resource resource;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Issue in reading the file", e);
        }
        if(resource.exists()&& resource.isReadable()) {
            return resource;
        } else {
            throw new RuntimeException("the file doesn't exist or not readable");
        }
    }

    private MultipartFile convertFile(MultipartFile multipartFile) {
        //TODO IMPLEMENT METHOD
        return multipartFile;
    }

    private String storeFile(MultipartFile file, String url) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        Path filePath = Paths.get(fileStoragePath + java.io.File.separator + fileName); // LET OP: Door File.separator werkt dit nu op zowel Mac als Windows
        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Issue in storing the file", e);
        }
        repo.save(new File(fileName, file.getContentType(), url));
        return fileName;
    }
}

