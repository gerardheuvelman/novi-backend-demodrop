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
import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("/files")
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
        // MediaType contentType = MediaType.IMAGE_JPEG;
        // this is going to accept multiple types
        // LET OP: bovenstaand is niet erg, want er wordt in de front end (React) gefilterd op filetype (audio files).
        String mimeType;
        try{
            mimeType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        // for download attachment use next line
        // return ResponseEntity.ok().contentType(contentType).header(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName=" + resource.getFilename()).body(resource);
        // for showing image in browser
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(mimeType)).header(HttpHeaders.CONTENT_DISPOSITION, "inline;fileName=" + resource.getFilename()).body(resource);
    }
}
