package nl.ultimateapps.demoDrop.Services;

import lombok.Getter;
import lombok.Setter;
import nl.ultimateapps.demoDrop.Models.AudioFile;
import nl.ultimateapps.demoDrop.Repositories.AudioFileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class AudioFileService {

    @Value("${my.upload_location}")
    @Getter
    @Setter
    private Path fileStoragePath;
    private final String fileStorageLocation;

    private final AudioFileRepository audioFileRepository;

    public AudioFileService(@Value("${my.upload_location}") String fileStorageLocation, AudioFileRepository audioFileRepository) {
        fileStoragePath = Paths.get(fileStorageLocation).toAbsolutePath().normalize();
        this.fileStorageLocation = fileStorageLocation;
        this.audioFileRepository = audioFileRepository;
        try {
            Files.createDirectories(fileStoragePath);
        } catch (IOException e) {
            throw new RuntimeException("Issue in creating file directory");
        }
    }

    public AudioFile processFileUpload(MultipartFile file) {
//      MultipartFile convertedFile = convertFile(file); // TODO IMPLEMENTEREN EN AANZETTEN
        AudioFile storedAudioFile = storeFile(file);
        return storedAudioFile;
    }

    public Resource downLoadFile(Long fileId) {
        String filename= fileId.toString() + ".mp3";
        Path path = Paths.get(fileStorageLocation).toAbsolutePath().resolve(filename); // STRING MADE FROM A LONG
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

//    private MultipartFile convertFile(MultipartFile multipartFile) {
//        // first, save the file (temporarily) under its own name..
//        String originalFileName = multipartFile.getOriginalFilename();
//        Path filePath = Paths.get(fileStoragePath + java.io.File.separator + originalFileName); // LET OP: Door File.separator werkt dit nu op zowel Mac als Windows
//        try {
//            Files.copy(multipartFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//        } catch (IOException e) {
//            throw new RuntimeException("Issue in storing the temporary audio file", e);
//        }
//        // get a java File object from this temp file
//        File file = new File(filePath.toString());
//        // then, convert the file
//        convertFu
//
//        return multipartFile;
//    }

    private AudioFile storeFile(MultipartFile multipartFile) {
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        AudioFile newAudioFile = new AudioFile();
        audioFileRepository.save(newAudioFile); // This sets the FileId field
        Long audioFileId = newAudioFile.getAudioFileId();
        String newFilename = Long.toString(audioFileId) + ".mp3";
        Path filePath = Paths.get(fileStoragePath + java.io.File.separator + newFilename); // LET OP: Door File.separator werkt dit nu op zowel Mac als Windows
        try {
            Files.copy(multipartFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Issue in storing the file", e);
        }
        newAudioFile.setOriginalFileName(originalFileName);
        audioFileRepository.save(newAudioFile);
        return newAudioFile;
    }



}