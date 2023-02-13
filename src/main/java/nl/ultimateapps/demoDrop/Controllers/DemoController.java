package nl.ultimateapps.demoDrop.Controllers;

import nl.ultimateapps.demoDrop.Dtos.output.DemoDto;
import nl.ultimateapps.demoDrop.Exceptions.RecordNotFoundException;
import nl.ultimateapps.demoDrop.Models.AudioFile;
import nl.ultimateapps.demoDrop.Services.DemoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.ArrayList;

import lombok.*;
import javax.transaction.Transactional;

@CrossOrigin
@RestController
@RequestMapping("/demos")
@AllArgsConstructor
public class DemoController {

    @Getter
    @Setter
    private DemoService demoService;

    @Getter
    @Setter
    private AudioFileController audioFileController;

    @GetMapping("")
    public ResponseEntity<ArrayList<DemoDto>> getDemos(@RequestParam int limit) {
        ArrayList<DemoDto> demoDtos = demoService.getDemos(limit);
        if (demoDtos.size()>0) {
            return ResponseEntity.ok(demoDtos);
        } else {
            throw new RecordNotFoundException("No demos found");
        }
    }

    @GetMapping("/{id}")
    @Transactional
    // Dit moet omdat het anders niet toegestaan is om een large object (de mp3 file) automatisch mee te geven.
    public ResponseEntity<DemoDto> getDemo (@PathVariable long id) {
        DemoDto demoDto = demoService.getDemo(id);
        return ResponseEntity.ok(demoDto);
    }

    @GetMapping("{id}/isfav")
    public ResponseEntity<Boolean> checkIsFav(@PathVariable long id) throws UserPrincipalNotFoundException {
        boolean favStatus  = demoService.checkIsFav(id);
        return ResponseEntity.ok(favStatus);
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

    @PatchMapping("{id}/setfav")
    public ResponseEntity<Boolean> setFavStatus(@PathVariable long id, @RequestParam boolean status) throws UserPrincipalNotFoundException {
        boolean newFavStatus  = demoService.setFavStatus(id, status);
        return ResponseEntity.ok(newFavStatus);
    }

    @PatchMapping("/{id}/setgenre/{genrename}")
    public ResponseEntity<DemoDto> assignGenreToDemo(@PathVariable long id, @PathVariable String genrename) {
        DemoDto partiallyUpdatedDemoDto = demoService.assignGenreToDemo(id, genrename);
        return ResponseEntity.ok(partiallyUpdatedDemoDto);
    }

    @PatchMapping("/{id}/addfav/{username}")
    public ResponseEntity<DemoDto> addUserToFavoriteOfUsersList(@PathVariable long id, @PathVariable String username) {
        DemoDto partiallyUpdatedDemoDto = demoService.addUserToFavoriteOfUsersList(id, username);
        return ResponseEntity.ok(partiallyUpdatedDemoDto);
    }

    @PatchMapping("/{id}/remfav/{username}")
    public ResponseEntity<DemoDto> removeUserFromFavoriteOfUsersList(@PathVariable long id, @PathVariable String username) {
        DemoDto partiallyUpdatedDemoDto = demoService.removeUserFromFavoriteOfUsersList(id, username);
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

    @PostMapping("/{id}/file")
    public ResponseEntity<String> uploadFileAndAssignToDemo(@PathVariable("id") Long demoId, @RequestParam("file") MultipartFile multipartFile) {
        AudioFile audioFile = audioFileController.uploadSingleFile(multipartFile);
        String result = demoService.assignFileToDemo(audioFile.getFileId(), demoId);
        return ResponseEntity.ok(result);
    }
}