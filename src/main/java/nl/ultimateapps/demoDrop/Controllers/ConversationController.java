package nl.ultimateapps.demoDrop.Controllers;

import nl.ultimateapps.demoDrop.Dtos.output.ConversationDto;
import nl.ultimateapps.demoDrop.Exceptions.RecordNotFoundException;
import nl.ultimateapps.demoDrop.Services.ConversationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.ArrayList;
import lombok.*;

@CrossOrigin
@RestController
@RequestMapping("/conversations")
@AllArgsConstructor
public class ConversationController {
    @Getter
    @Setter
    private ConversationService conversationService;

    @GetMapping("")
    public ResponseEntity<ArrayList<ConversationDto>> getConversations(@RequestParam int limit) {
        ArrayList<ConversationDto> conversationDtos = conversationService.getConversations(limit);
        if (conversationDtos.size()>0) {
            return ResponseEntity.ok(conversationDtos);
        } else {
            throw new RecordNotFoundException("No Conversations found");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConversationDto> getConversation (@PathVariable int id) {
        ConversationDto conversationDto = conversationService.getConversation(id);
        return ResponseEntity.ok(conversationDto);
    }

    @PostMapping("")
    public ResponseEntity<ConversationDto> startConversation(@RequestBody ConversationDto conversationDto) throws UserPrincipalNotFoundException {
        ConversationDto savedConversationDto = conversationService.startConversation(conversationDto);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/conversations/" + savedConversationDto.getConversationId()).toUriString());
        return ResponseEntity.created(uri).body(savedConversationDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConversationDto> replyToConversation(@PathVariable long id, @RequestBody ConversationDto conversationDto) {
        ConversationDto updatedConversationDto = conversationService.replyToConversation(id, conversationDto);
        return ResponseEntity.ok(updatedConversationDto);
    }

    @PatchMapping ("{id}")
    public ResponseEntity<ConversationDto> MarkConversationAsRead(@PathVariable long id) {
        ConversationDto updatedConversationDto = conversationService.markConversationAsRead(id);
        return ResponseEntity.ok(updatedConversationDto);
    }

    @DeleteMapping("")
    public ResponseEntity<String> deleteConversations() {
        long numDeletedConversations = conversationService.deleteConversations();
        return ResponseEntity.ok(numDeletedConversations + " conversations deleted successfully.");
    }

        @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteConversation(@PathVariable long id) {
        long deletedConversation = conversationService.deleteConversation(id);
        return ResponseEntity.ok("Conversation "+ deletedConversation + " was deleted successfully.");
    }
}