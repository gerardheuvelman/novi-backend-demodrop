package nl.ultimateapps.demoDrop.Controllers;

import nl.ultimateapps.demoDrop.Dtos.output.DemoDto;
import nl.ultimateapps.demoDrop.Exceptions.RecordNotFoundException;
import nl.ultimateapps.demoDrop.Services.DemoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.ArrayList;
import lombok.*;

@RestController
@RequestMapping("/demos")
@AllArgsConstructor
public class DemoController {

    @Getter
    @Setter
    private  DemoService service;

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
        ArrayList<DemoDto> demoDtos = service.getDemos();
        if (demoDtos.size()>0) {
            return ResponseEntity.ok(demoDtos);
        } else {
            throw new RecordNotFoundException("No demos found");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DemoDto> getDemo (@PathVariable long id) {
        DemoDto demoDto = service.getDemo(id);
        return ResponseEntity.ok(demoDto);
    }

    @PostMapping("")
    public ResponseEntity<String> postDemo(@RequestBody DemoDto demoDto) {
        long savedDemo = service.createDemo(demoDto);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/demos/" + savedDemo).toUriString());
        return ResponseEntity.created(uri).body("Demo created!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> putDemo(@PathVariable long id, @RequestBody DemoDto demoDto) {
        long updatedDemoId = service.updateDemo(id, demoDto);
        return ResponseEntity.ok("Demo " + updatedDemoId + " was updated successfully");
    }

    //Patch mapping (Owerkt het alleen met het veld "title" ):
    @PatchMapping("/{id}")
    public ResponseEntity<String> patchDemo(@PathVariable long id, @RequestBody DemoDto demoDto) {
        long partiallyUpdatedDemoId = service.partialUpdateDemo(id, demoDto);
        return ResponseEntity.ok("Demo " + partiallyUpdatedDemoId + " was partially updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDemo(@PathVariable long id) {
        long deletedDemo = service.deleteDemo(id);
        return ResponseEntity.ok("Demo "+ deletedDemo + " was deleted successfully.");
    }

    @GetMapping("/finduser") /// Dit heb ik misschien nodig om een lijst demo's voor een enkele gebruiker weer te geven
    public ResponseEntity<Iterable<DemoDto>> getDemoContaining(@RequestParam String query) {
        return ResponseEntity.ok(service.getDemoContaining(query));
    }
}