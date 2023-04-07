package nl.ultimateapps.demoDrop.Controllers;

import nl.ultimateapps.demoDrop.Dtos.output.UserReportDto;
import nl.ultimateapps.demoDrop.Exceptions.RecordNotFoundException;
import nl.ultimateapps.demoDrop.Services.UserReportService;
import nl.ultimateapps.demoDrop.Services.UserReportServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.ArrayList;
import lombok.*;

@CrossOrigin
@RestController
@RequestMapping("/userreports")
@AllArgsConstructor
public class UserReportController {
    @Getter
    @Setter
    private UserReportService userReportService;

    @GetMapping("")
    public ResponseEntity<ArrayList<UserReportDto>> getUserReports(@RequestParam int limit) {
        ArrayList<UserReportDto> userReportDtos = userReportService.getUserReports(limit);
        if (userReportDtos.size()>0) {
            return ResponseEntity.ok(userReportDtos);
        } else {
            throw new RecordNotFoundException("No user reports found");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserReportDto> getUserReport (@PathVariable int id) throws AccessDeniedException {
        UserReportDto userReportDto = userReportService.getUserReport(id);
        return ResponseEntity.ok(userReportDto);
    }

    @PostMapping("")
    public ResponseEntity<UserReportDto> submitUserReport(@RequestBody UserReportDto userReportDto) throws UserPrincipalNotFoundException, AccessDeniedException {
        UserReportDto savedUserReportDto = userReportService.createUserReport(userReportDto);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/userreports/" + savedUserReportDto.getUserReportId()).toUriString());
        return ResponseEntity.created(uri).body(savedUserReportDto);
    }

    @DeleteMapping("")
    public ResponseEntity<String> deleteUserReports() {
        long numDeletedUserReports = userReportService.deleteUserReports();
        return ResponseEntity.ok(numDeletedUserReports + " user reports deleted successfully.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserReport(@PathVariable long id) {
        long deletedUserReport = userReportService.deleteUserReport(id);
        return ResponseEntity.ok("User report "+ deletedUserReport + " was deleted successfully.");
    }
}