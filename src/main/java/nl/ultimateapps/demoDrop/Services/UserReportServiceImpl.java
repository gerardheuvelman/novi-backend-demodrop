package nl.ultimateapps.demoDrop.Services;

import nl.ultimateapps.demoDrop.Dtos.output.UserReportDto;
import nl.ultimateapps.demoDrop.Exceptions.RecordNotFoundException;
import nl.ultimateapps.demoDrop.Helpers.mappers.UserReportMapper;
import nl.ultimateapps.demoDrop.Models.Conversation;
import nl.ultimateapps.demoDrop.Models.UserReport;
import nl.ultimateapps.demoDrop.Models.Demo;
import nl.ultimateapps.demoDrop.Models.User;
import nl.ultimateapps.demoDrop.Repositories.ConversationRepository;
import nl.ultimateapps.demoDrop.Repositories.UserReportRepository;
import nl.ultimateapps.demoDrop.Repositories.DemoRepository;
import nl.ultimateapps.demoDrop.Repositories.UserRepository;
import nl.ultimateapps.demoDrop.Utils.AuthHelper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.time.Instant;
import java.util.*;

import lombok.*;

@Service
@AllArgsConstructor
public class UserReportServiceImpl implements UserReportService {

    @Getter
    @Setter
    private UserReportRepository userReportRepository;

    @Getter
    @Setter
    private DemoRepository demoRepository;

    @Getter
    @Setter
    private UserRepository userRepository;

    @Getter
    @Setter
    private ConversationRepository conversationRepository;

    @Override
    public ArrayList<UserReportDto> getUserReports(int limit) { // ADMIN ONLY
        ArrayList<UserReportDto> userReportDtoArrayList = new ArrayList<>();
        Iterable<UserReport> userReportIterable = userReportRepository.findAll();
        ArrayList<UserReport> userReportArrayList = new ArrayList<>();
        userReportIterable.forEach(userReportArrayList::add);
        int numResults = userReportArrayList.size();
        if (limit == 0) {
            // return full list
            for (UserReport userReport : userReportArrayList) {
                UserReportDto userReportDto = UserReportMapper.mapToDto(userReport);
                userReportDtoArrayList.add(userReportDto);
            }
        } else {
            // return limited list
            for (int i = 0; i < (Math.min(numResults, limit)); i++) {
                UserReportDto userReportDto = UserReportMapper.mapToDto(userReportArrayList.get(i));
                userReportDtoArrayList.add(userReportDto);
            }
        }
        return userReportDtoArrayList;
    }

    @Override
    public UserReportDto getUserReport(long userReportId) throws AccessDeniedException { // //ADMIN ONLY
        if (userReportRepository.findById(userReportId).isEmpty()) {
            throw new RecordNotFoundException();
        }
        UserReport userReport = userReportRepository.findById(userReportId).get();
        return UserReportMapper.mapToDto(userReport);
    }

    @Override
    public UserReportDto createUserReport(UserReportDto userReportDto) throws UserPrincipalNotFoundException, AccessDeniedException { // AUTH ONLY
        User retrievedUser = null;
        Demo retrievedDemo = null;
        Conversation retrievedConversation = null;

        // First, check the type of user report that needs to be created
        switch (userReportDto.getType()) {
            case "user" :
                String username = userReportDto.getReportedUser().getUsername();
                if (userRepository.findById(username).isEmpty()) {
                    throw new RecordNotFoundException("User " + username + " not found in repository");
                }
                retrievedUser = userRepository.findById(username).get();
                break;
            case "demo" :
                long demoId = userReportDto.getReportedDemo().getDemoId();
                if (demoRepository.findById(demoId).isEmpty()) {
                    throw new RecordNotFoundException("Demo " + demoId + " not found in repository");
                }
                retrievedDemo = demoRepository.findById(demoId).get();
                break;
            case "conversation" :
                long conversationId = userReportDto.getReportedConversation().getConversationId();
                if (conversationRepository.findById(conversationId).isEmpty()) {
                    throw new RecordNotFoundException("Conversation " + conversationId + " not found in repository");
                }
                retrievedConversation = conversationRepository.findById(conversationId).get();
                break;
            default: throw new RuntimeException("The field  'type' in the UserReportDto was not set  to a  recognized value. It must be set to either 'user', 'demo' or 'conversation'. ");
        }

        UserReport userReport = UserReportMapper.mapToModel(userReportDto);
        Date now = Date.from(Instant.now());
        userReport.setCreatedDate(now);
        userReport.setReportedUser(retrievedUser);
        userReport.setReportedDemo(retrievedDemo);
        userReport.setReportedConversation(retrievedConversation);
        // set the field "reporter" from the current security principal.
        String currentPrincipalName = AuthHelper.getPrincipalUsername();
        if (userRepository.findById(currentPrincipalName).isEmpty()) {
            throw new UserPrincipalNotFoundException(currentPrincipalName);
        }
        User currentPrincipal = userRepository.findById(currentPrincipalName).get();
        userReport.setReporter(currentPrincipal);
        UserReport savedUserReport = userReportRepository.save(userReport);
        return UserReportMapper.mapToDto(savedUserReport);
    }

    @Override
    public long deleteUserReports() { //ADMIN ONLY
        if ((userReportRepository.findAll().size()) <= 0) {
            throw new RecordNotFoundException();
        }
        List<UserReport> userReports = userReportRepository.findAll();
        long numDeletedUserReports = 0;
        for (UserReport userReport : userReports) {
            userReportRepository.delete(userReport);
            numDeletedUserReports++;
        }
        return numDeletedUserReports;
    }

    @Override
    public long deleteUserReport(long userReportId) { // ADMIN ONLY
        if (userReportRepository.findById(userReportId).isPresent()) {
            UserReport userReport = userReportRepository.findById(userReportId).get();
            userReportRepository.deleteById(userReportId);
            return userReportId;
        } else {
            throw new RecordNotFoundException();
        }
    }
}