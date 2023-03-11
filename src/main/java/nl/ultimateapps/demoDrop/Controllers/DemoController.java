package nl.ultimateapps.demoDrop.Controllers;

import nl.ultimateapps.demoDrop.Dtos.output.DemoDto;
import nl.ultimateapps.demoDrop.Exceptions.BadRequestException;
import nl.ultimateapps.demoDrop.Exceptions.RecordNotFoundException;
import nl.ultimateapps.demoDrop.Services.AudioFileService;
import nl.ultimateapps.demoDrop.Services.AudioFileServiceImpl;
import nl.ultimateapps.demoDrop.Services.DemoService;
import nl.ultimateapps.demoDrop.Services.DemoServiceImpl;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;

import lombok.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

@CrossOrigin
@RestController
@RequestMapping("/demos")
@AllArgsConstructor
public class DemoController {

    @Getter
    @Setter
    private DemoService demoService;

//    @Getter
//    @Setter
//    private AudioFileService audioFileService;

    @GetMapping("")
    public ResponseEntity<List<DemoDto>> getDemos(@RequestParam int limit) {
        List<DemoDto> demoDtos = demoService.getDemos(limit);
        if (demoDtos.size()>0) {
            return ResponseEntity.ok(demoDtos);
        } else {
            throw new RecordNotFoundException("No demos found");
        }
    }

    @GetMapping("/bygenre")
    public ResponseEntity<List<DemoDto>> getDemosByGenre(@RequestParam String genre, @RequestParam int limit) {
        List<DemoDto> demoDtos = demoService.getDemosByGenre(genre, limit);
        if (demoDtos.size()>0) {
            return ResponseEntity.ok(demoDtos);
        } else {
            throw new RecordNotFoundException("No demos found");
        }
    }

    @GetMapping("/{demoId}")
    public ResponseEntity<DemoDto> getDemo (@PathVariable long demoId) {
        DemoDto demoDto = demoService.getDemo(demoId);
        return ResponseEntity.ok(demoDto);
    }

    @GetMapping("/{demoId}/download")
    @Transactional // Dit moet omdat het anders niet toegestaan is om een large object (de mp3 file) automatisch mee te geven.
    public ResponseEntity<Resource> downloadMp3File(@PathVariable long demoId, HttpServletRequest request) {
         Resource mp3File = demoService.downloadMp3File(demoId);
        MediaType mediaType = new MediaType("audio", "mpeg");
        return ResponseEntity.ok().contentType(mediaType).header(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName=" + mp3File.getFilename()).body(mp3File);
    }

    @PostMapping("")
    public ResponseEntity<DemoDto> createDemo(@RequestBody DemoDto demoDto) throws UserPrincipalNotFoundException {
        DemoDto savedDemoDto = demoService.createDemo(demoDto);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/demos/" + savedDemoDto.getDemoId()).toUriString());
        return ResponseEntity.created(uri).body(savedDemoDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DemoDto> UpdateDemo(@PathVariable long id, @RequestBody DemoDto demoDto) {
          DemoDto updatedDemoDto = demoService.updateDemo(id, demoDto);
        return ResponseEntity.ok(updatedDemoDto);
    }

    @GetMapping("{id}/isfav")
    public ResponseEntity<Boolean> checkIsFav(@PathVariable long id) throws UserPrincipalNotFoundException {
        boolean favStatus  = demoService.checkIsFav(id);
        return ResponseEntity.ok(favStatus);
    }

    @PatchMapping("{id}/setfav")
    public ResponseEntity<Boolean> setFavStatus(@PathVariable long id, @RequestParam boolean status) throws UserPrincipalNotFoundException {
        boolean newFavStatus  = demoService.setFavStatus(id, status);
        return ResponseEntity.ok(newFavStatus);
    }

    @PatchMapping("/{id}/setgenre/{genrename}")
    public ResponseEntity<DemoDto> assignGenreToDemo(@PathVariable long id, @PathVariable String genrename) throws AccessDeniedException {
        DemoDto partiallyUpdatedDemoDto = demoService.assignGenreToDemo(id, genrename);
        return ResponseEntity.ok(partiallyUpdatedDemoDto);
    }

    @DeleteMapping("")
    public ResponseEntity<String> deleteDemos() {
        long numDeletedDemos = demoService.deleteDemos();
        return ResponseEntity.ok(numDeletedDemos + " demos deleted successfully.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDemo(@PathVariable long id) {
        long deletedDemo = demoService.deleteDemo(id);
        return ResponseEntity.ok("Demo "+ deletedDemo + " was deleted successfully.");
    }

    @GetMapping("/find")
    public ResponseEntity<Iterable<DemoDto>> getDemoContaining(@RequestParam String query) {
        return ResponseEntity.ok(demoService.getDemoContaining(query));
    }

    @PostMapping("/{id}/upload")
    public ResponseEntity<String> uploadFileAndAssignToDemo(@PathVariable("id") Long demoId, @RequestParam("file") MultipartFile multipartFile) throws AccessDeniedException {
        if (demoService.uploadFileAndAssignToDemo (demoId, multipartFile)) {
            return ResponseEntity.ok("A new file was successfully uploaded and associated with demoId " + demoId);
        } else throw new BadRequestException();
    }
}