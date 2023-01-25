package nl.ultimateapps.demoDrop.Controllers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nl.ultimateapps.demoDrop.Models.File;
import nl.ultimateapps.demoDrop.Services.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;

@CrossOrigin
@RestController
@RequestMapping("/files")
@AllArgsConstructor
public class FileController {

    @Getter
    @Setter
    private FileService fileService;

    @PostMapping("")
    File uploadSingleFile(@RequestParam("file") MultipartFile file){
        String url = ServletUriComponentsBuilder.fromCurrentContextPath().path("").path(Objects.requireNonNull(file.getOriginalFilename())).toUriString();
        String contentType = file.getContentType();
        String fileName = fileService.processFile(file, url);
        return new File(fileName, contentType, url);
    }

    @GetMapping("/{fileName}")
    ResponseEntity<Resource> downLoadSingleFile(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = fileService.downLoadFile(fileName);
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
