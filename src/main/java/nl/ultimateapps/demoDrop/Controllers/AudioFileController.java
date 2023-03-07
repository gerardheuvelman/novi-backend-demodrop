package nl.ultimateapps.demoDrop.Controllers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nl.ultimateapps.demoDrop.Dtos.output.AudioFileDto;
import nl.ultimateapps.demoDrop.Exceptions.RecordNotFoundException;
import nl.ultimateapps.demoDrop.Models.AudioFile;
import nl.ultimateapps.demoDrop.Services.AudioFileService;
import nl.ultimateapps.demoDrop.Services.AudioFileServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/audiofiles")
@AllArgsConstructor
public class AudioFileController {

    @Getter
    @Setter
    private AudioFileService audioFileService;

    @GetMapping("")
    ResponseEntity<List<AudioFileDto>> getAudiFiles (@RequestParam int limit) {
        List<AudioFileDto> audioFileDtos = audioFileService.getAudioFiles(limit);
        if (audioFileDtos.size()>0) {
            return ResponseEntity.ok(audioFileDtos);
        } else {
            throw new RecordNotFoundException("No audiofiles found");
        }
    }

    @GetMapping("/{audioFileId}")
    public ResponseEntity<AudioFileDto> getAudioFile (@PathVariable long audioFileId) {
        AudioFileDto audioFileDto = audioFileService.getAudioFile(audioFileId);
        return ResponseEntity.ok(audioFileDto);
    }

//    @GetMapping("/{audioFileId}/download")
//    @Transactional  // Dit moet omdat het anders niet toegestaan is om een large object (de mp3 file) automatisch mee te geven.
//    public ResponseEntity<Resource> downloadMp3File(@PathVariable long audioFileId, HttpServletRequest request) {
//        Resource mp3File = audioFileService.downloadMp3File(audioFileId);
//        MediaType mediaType = new MediaType("audio", "mpeg");
//        return ResponseEntity.ok().contentType(mediaType).header(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName=" + mp3File.getFilename()).body(mp3File);
//    }

    @PostMapping("")
    AudioFile uploadSingleFile(@RequestParam("multipartFile") MultipartFile multipartFile){
        AudioFile newAudioFile = audioFileService.processFileUpload(multipartFile);
        return newAudioFile;
    }

    @DeleteMapping("/{audioFileId}")
    public ResponseEntity<String> deleteAudioFile(@PathVariable long audioFileId) {
        long deletedAudioFileId = audioFileService.deleteAudioFile(audioFileId);
        return ResponseEntity.ok("AudioFile "+ deletedAudioFileId + " was deleted successfully.");
    }

    @DeleteMapping("/purge")
    public ResponseEntity<String> DeleteOrphanedMp3Files(@Value("${my.upload_location}") String fileStorageLocation) throws Exception {
        int numDeletedFiles = audioFileService.deleteOrphanedMp3Files(fileStorageLocation);
        return ResponseEntity.ok(numDeletedFiles + " orphaned mp3 files were successfully deleted from " + fileStorageLocation);
    }
}
