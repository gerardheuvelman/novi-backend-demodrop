package nl.ultimateapps.demoDrop.Services;

import lombok.Getter;
import lombok.Setter;
import nl.ultimateapps.demoDrop.Dtos.output.AudioFileDto;
import nl.ultimateapps.demoDrop.Exceptions.RecordNotFoundException;
import nl.ultimateapps.demoDrop.Helpers.mappers.AudioFileMapper;
import nl.ultimateapps.demoDrop.Models.AudioFile;
import nl.ultimateapps.demoDrop.Repositories.AudioFileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class AudioFileService {

    @Value("${my.upload_location}")
    @Getter
    @Setter
    private Path fileStoragePath;
    @Getter
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

    public List<AudioFileDto> getAudioFiles(int limit) { //ADMIN ONLY
        List<AudioFileDto> audioFileDtos = new ArrayList<>();
        Iterable<AudioFile> audioFileIterable = audioFileRepository.findAll();
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

    public AudioFileDto getAudioFile(long audioFileId) { // ADMIN ONLY
        if (audioFileRepository.findById(audioFileId).isPresent()) {
            AudioFile audioFile = audioFileRepository.findById(audioFileId).get();
            AudioFileDto audioFileDto = AudioFileMapper.mapToDto(audioFile);
            return audioFileDto;
        } else {
            throw new RecordNotFoundException();
        }
    }

    public long deleteAudioFile(long audioFileId) { //ADMIN ONLY
        AudioFile audioFile;
        if (audioFileRepository.findById(audioFileId).isPresent()) {
            audioFile = audioFileRepository.findById(audioFileId).get();
        } else throw new RecordNotFoundException();
        long retrievedId = audioFile.getAudioFileId();
        audioFileRepository.deleteById(audioFileId);
        return retrievedId;
    }


    public int deleteOrphanedMp3Files(String fileStorageLocation) {
        AtomicInteger numDeletedFiles = new AtomicInteger();
        // for each  mp3 file in the storage folder, check to sse if a corresponding Audiofile record exists. if not, delete the file and increment numdeletedFiles
        File uploadFolder = new File(fileStorageLocation);
        Set<String> audioFileIds = new HashSet<>();
        // Get the ids of all AudioFile objects
        Iterable<AudioFile> audioFileIterable = audioFileRepository.findAll();
        for (AudioFile audioFile : audioFileIterable) {
            audioFileIds.add(String.valueOf(audioFile.getAudioFileId()));
        }
        // Delete mp3 files that have no corresponding AudioFile object
        File[] mp3Files = uploadFolder.listFiles((dir, name) -> name.endsWith(".mp3"));
        if (mp3Files != null) {
            Arrays.stream(mp3Files)
                    .filter(mp3File -> !audioFileIds.contains(mp3File.getName()))
                    .forEach(mp3File -> {
                        if (!mp3File.delete()) {
                            System.err.println("Failed to delete file: " + mp3File.getAbsolutePath());
                        } else numDeletedFiles.getAndIncrement();
                    });
        }
        return numDeletedFiles.get();
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
        audioFileRepository.save(newAudioFile);
        return newAudioFile;
    }
}