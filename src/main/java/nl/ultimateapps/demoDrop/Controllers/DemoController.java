package nl.ultimateapps.demoDrop.Controllers;

import nl.ultimateapps.demoDrop.Dtos.output.DemoDto;
import nl.ultimateapps.demoDrop.Exceptions.RecordNotFoundException;
import nl.ultimateapps.demoDrop.Models.File;
import nl.ultimateapps.demoDrop.Services.DemoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
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
    private FileController fileController;

//    @PutMapping("/{id}/conversation") // Bij TIE was hier Remote-controller gebruikt, wat een 1-op-1 relatie is. Nu moet ik het doen met Conversation (1-op-veel relatie!!!)
//    public ResponseEntity<String> putConversation(@PathVariable long id, @RequestBody IdInputDto idInputDto) {
//        long updatedConversationId = service.addConversationToDemo(id, idInputDto.id);
//        return ResponseEntity.ok("Demo " + updatedDemoId + " has been assigned conversation " + idInputDto.id);
//    }

//    WERKT NIET HELAAS, maar dit is dus een 1-to-many relatie!!!
//    @PutMapping("/{id}/cimodules")
//    public ResponseEntity<String> putCiModule(@PathVariable long id, @RequestBody IdsInputDto idsInputDto) {
//        long updatedTelevisionId = service.assignCiModulesToTelevision(id, idsInputDto.ids);
//        return ResponseEntity.ok("Television " + updatedTelevisionId + " has been assigned The following Ci Modules: " + idsInputDto.ids);
//    }

    @GetMapping("")
    public ResponseEntity<ArrayList<DemoDto>> getDemos() {
        ArrayList<DemoDto> demoDtos = demoService.getDemos();
        if (demoDtos.size()>0) {
            return ResponseEntity.ok(demoDtos);
        } else {
            throw new RecordNotFoundException("No demos found");
        }
    }

    @GetMapping("/toptwelve")
    public ResponseEntity<ArrayList<DemoDto>> getTopTwelveDemos() {
        ArrayList<DemoDto> demoDtos = demoService.getTopTwelveDemos();
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

    @PostMapping("")
    public ResponseEntity<String> createDemo(@RequestBody DemoDto demoDto) {
        long savedDemo = demoService.createDemo(demoDto);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/demos/" + savedDemo).toUriString());
        return ResponseEntity.created(uri).body("Demo created!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> UpdateDemo(@PathVariable long id, @RequestBody DemoDto demoDto) {
        long updatedDemoId = demoService.updateDemo(id, demoDto);
        return ResponseEntity.ok("Demo " + updatedDemoId + " was updated successfully");
    }

    //Patch mapping (IS BELANGRIJK VOOR EDITEN ZONDER ELKE KEER EEN NIEUWE FILE TE HOEVEN UPLOADEN ):
    @PatchMapping("/{id}")
    public ResponseEntity<String> updateDemoInfo(@PathVariable long id, @RequestBody DemoDto demoDto) {
        long partiallyUpdatedDemoId = demoService.partialUpdateDemo(id, demoDto);
        return ResponseEntity.ok("Demo " + partiallyUpdatedDemoId + " was partially updated successfully");
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
    public void assignFileToDemo(@PathVariable("id") Long demoId,
                                 @RequestParam("file") MultipartFile multipartFile) {
        File file = fileController.uploadSingleFile(multipartFile);
        demoService.assignFileToDemo(file.getFileName(), demoId);
    }
}