package nl.ultimateapps.demoDrop.Services;

import lombok.Getter;
import lombok.Setter;
import nl.ultimateapps.demoDrop.Dtos.output.AudioFileDto;
import nl.ultimateapps.demoDrop.Exceptions.RecordNotFoundException;
import nl.ultimateapps.demoDrop.Helpers.mappers.AudioFileMapper;
import nl.ultimateapps.demoDrop.Models.AudioFile;
import nl.ultimateapps.demoDrop.Repositories.AudioFileRepository;
import nl.ultimateapps.demoDrop.Utils.FileConverter;
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
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class AudioFileServiceImpl implements AudioFileService {

    @Value("${my.upload_location}")
    @Getter
    @Setter
    private Path fileStoragePath;
    @Getter
    private final String fileStorageLocation;

    private final AudioFileRepository audioFileRepository;

    public AudioFileServiceImpl(@Value("${my.upload_location}") String fileStorageLocation, AudioFileRepository audioFileRepository) {
        fileStoragePath = Paths.get(fileStorageLocation).toAbsolutePath().normalize();
        this.fileStorageLocation = fileStorageLocation;
        this.audioFileRepository = audioFileRepository;
        try {
            Files.createDirectories(fileStoragePath);
        } catch (IOException e) {
            throw new RuntimeException("Issue in creating file directory");
        }
    }

    @Override
    public AudioFile processFileUpload(MultipartFile multipartFile) {
      MultipartFile convertedFile = FileConverter.convertFile(multipartFile);
        AudioFile storedAudioFile = storeFile(convertedFile);
        return storedAudioFile;
    }

    @Override
    public List<AudioFileDto> getAudioFiles(int limit) { //ADMIN ONLY
        List<AudioFileDto> audioFileDtos = new ArrayList<>();
        Iterable<AudioFile> audioFileIterable = audioFileRepository.findAllByOrderByCreatedDateDesc();
        List<AudioFile> audioFiles = new ArrayList<>();
        audioFileIterable.forEach(audioFiles::add);
        int numResults = audioFiles.size();
        if (limit == 0) {
            // return full list
            for (AudioFile audioFile : audioFiles) {
                AudioFileDto audioFileDto = AudioFileMapper.mapToDto(audioFile);
                audioFileDtos.add(audioFileDto);
            }
        } else {
            // return limited list
            for (int i = 0; i < (Math.min(numResults, limit)); i++) {
                AudioFileDto audioFileDto = AudioFileMapper.mapToDto(audioFiles.get(i));
                audioFileDtos.add(audioFileDto);
            }
        }
        return audioFileDtos;
    }

    @Override
    public AudioFileDto getAudioFile(long audioFileId) { // ADMIN ONLY
        if (audioFileRepository.findById(audioFileId).isPresent()) {
            AudioFile audioFile = audioFileRepository.findById(audioFileId).get();
            AudioFileDto audioFileDto = AudioFileMapper.mapToDto(audioFile);
            return audioFileDto;
        } else {
            throw new RecordNotFoundException();
        }
    }

    public AudioFileDto editAudioFile(Long audioFileId, AudioFileDto audioFileDto ){
        AudioFile audioFile;
        if (audioFileRepository.findById(audioFileId).isPresent()) {
            audioFile = audioFileRepository.findById(audioFileId).get();
        } else throw new RecordNotFoundException();
        audioFile.setOriginalFileName(audioFileDto.getOriginalFileName());
        audioFileRepository.save(audioFile);
        return AudioFileMapper.mapToDto(audioFile);
    }


    @Override
    public long deleteAudioFile(long audioFileId) { //ADMIN ONLY
        AudioFile audioFile;
        if (audioFileRepository.findById(audioFileId).isPresent()) {
            audioFile = audioFileRepository.findById(audioFileId).get();
        } else throw new RecordNotFoundException();
        long retrievedId = audioFile.getAudioFileId();
        audioFileRepository.deleteById(audioFileId);
        return retrievedId;
    }


    @Override
    public int deleteOrphanedMp3Files(String fileStorageLocation) {
        AtomicInteger numDeletedFiles = new AtomicInteger();
        File uploadFolder = new File(fileStorageLocation);
        Set<String> filenamesFromAudioFiles = new HashSet<>();
        // Get the ids of all AudioFile objects
        Iterable<AudioFile> audioFileIterable = audioFileRepository.findAll();
        for (AudioFile audioFile : audioFileIterable) {
            filenamesFromAudioFiles.add((String.valueOf(audioFile.getAudioFileId())+ ".mp3"));
        }
        // Delete mp3 files that have no corresponding AudioFile object
        File[] mp3Files = uploadFolder.listFiles((dir, name) -> name.endsWith(".mp3"));
        if (mp3Files != null) {
            Arrays.stream(mp3Files)
                    .filter(mp3File -> !filenamesFromAudioFiles.contains(mp3File.getName()))
                    .forEach(mp3File -> {
                        if (!mp3File.delete()) {
                            System.err.println("Failed to delete file: " + mp3File.getAbsolutePath());
                        } else numDeletedFiles.getAndIncrement();
                    });
        }
        return numDeletedFiles.get();
    }

    @Override
    public Resource downloadMp3File(AudioFile audioFile) {
        String uniformFilename= audioFile.getAudioFileId() + ".mp3";
        Path pathToFile = Paths.get(getFileStorageLocation()).toAbsolutePath().resolve(uniformFilename); // STRING MADE FROM A LONG
        Resource resource;
        try {
            resource = new UrlResource(pathToFile.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Issue in reading the file", e);
        }
        if(resource.exists()&& resource.isReadable()) {
            return resource;
        } else {
            throw new RuntimeException("the file doesn't exist or not readable");
        }
    }

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
        newAudioFile.setCreatedDate(Date.from(Instant.now()));
        audioFileRepository.save(newAudioFile);
        return newAudioFile;
    }
}