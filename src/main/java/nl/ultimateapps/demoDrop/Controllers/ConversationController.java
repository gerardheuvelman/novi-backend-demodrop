package nl.ultimateapps.demoDrop.Controllers;

import nl.ultimateapps.demoDrop.Dtos.output.ConversationDto;
import nl.ultimateapps.demoDrop.Exceptions.RecordNotFoundException;
import nl.ultimateapps.demoDrop.Services.ConversationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.ArrayList;
import lombok.*;

@RestController
@RequestMapping("/conversations")
@AllArgsConstructor
public class ConversationController {

    @Getter
    @Setter
    private ConversationService service;

    @GetMapping("")
    public ResponseEntity<ArrayList<ConversationDto>> getConversations() {
        ArrayList<ConversationDto> conversationDtos = service.getConversations();
        if (conversationDtos.size()>0) {
            return ResponseEntity.ok(conversationDtos);
        } else {
            throw new RecordNotFoundException("No Conversations found");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConversationDto> getConversation (@PathVariable int id) {
        ConversationDto conversationDto = service.getConversation(id);
        return ResponseEntity.ok(conversationDto);
    }

    @PostMapping("")
    public ResponseEntity<String> postConversation(@RequestBody ConversationDto conversationDto) {
        long savedConversation = service.createConversation(conversationDto);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/conversations/" + savedConversation).toUriString());
        return ResponseEntity.created(uri).body("Conversation created!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> putConversation(@PathVariable long id, @RequestBody ConversationDto conversationDto) {
        long updatedConversationId = service.updateConversation(id, conversationDto);
        return ResponseEntity.ok("CI module " + updatedConversationId + " was updated successfully");
    }

    //Patch mapping (werkt het alleen met het veld "body"):
    @PatchMapping("/{id}")
    public ResponseEntity<String> patchCiModule(@PathVariable long id, @RequestBody ConversationDto ciModuleDto) {
        long partiallyUpdatedConversationId = service.partialUpdateConversation(id, ciModuleDto);
        return ResponseEntity.ok("Conversation" + partiallyUpdatedConversationId + " was partially updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteConversation(@PathVariable long id) {
        long deletedConversation = service.deleteConversation(id);
        return ResponseEntity.ok("Conversation "+ deletedConversation + " was deleted successfully.");
    }
}

