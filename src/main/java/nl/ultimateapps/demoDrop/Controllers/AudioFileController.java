package nl.ultimateapps.demoDrop.Controllers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nl.ultimateapps.demoDrop.Models.AudioFile;
import nl.ultimateapps.demoDrop.Services.AudioFileService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
@RequestMapping("/audiofiles")
@AllArgsConstructor
public class AudioFileController {

    @Getter
    @Setter
    private AudioFileService audioFileService;

    @PostMapping("")
    AudioFile uploadSingleFile(@RequestParam("multipartFile") MultipartFile multipartFile){
        AudioFile newAudioFile = audioFileService.processFileUpload(multipartFile);
        return newAudioFile;
    }

    @GetMapping("/{fileId}")
    ResponseEntity<Resource> downLoadSingleFile(@PathVariable long fileId, HttpServletRequest request) {
        Resource resource = audioFileService.downLoadFile(fileId);
        // this mediaType decides witch type you accept if you only accept 1 type
         MediaType mediaType = new MediaType("audio", "mpeg");
        // for download attachment use next line
         return ResponseEntity.ok().contentType(mediaType).header(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName=" + resource.getFilename()).body(resource);
    }

}
