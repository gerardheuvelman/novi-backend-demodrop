package nl.ultimateapps.demoDrop.Services;

import nl.ultimateapps.demoDrop.Dtos.output.DemoDto;
import nl.ultimateapps.demoDrop.Exceptions.RecordNotFoundException;
import nl.ultimateapps.demoDrop.Exceptions.UsernameNotFoundException;
import nl.ultimateapps.demoDrop.Helpers.mappers.DemoMapper;
import nl.ultimateapps.demoDrop.Models.*;
import nl.ultimateapps.demoDrop.Repositories.*;
import nl.ultimateapps.demoDrop.Utils.HyperlinkBuilder;
import nl.ultimateapps.demoDrop.Utils.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import lombok.*;

@Service
@AllArgsConstructor
public class DemoService {

    private final JwtUtil jwtUtil;
    private final DemoRepository demoRepository;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final AudioFileRepository audioFileRepository;
    private final GenreRepository genreRepository;
    private final EmailService emailService;

    public ArrayList<DemoDto> getDemos(int limit) {
        ArrayList<DemoDto> demoDtoList = new ArrayList<>();
        Iterable<Demo> demoIterable = demoRepository.findAllByOrderByCreatedDateDesc();
        ArrayList<Demo> demoArrayList = new ArrayList<>();
        demoIterable.forEach(demoArrayList::add);
        int numResults = demoArrayList.size();
        if (limit == 0) {
            // return full list
            for (Demo demo : demoArrayList) {
                DemoDto demoDto = DemoMapper.mapToDto(demo);
                demoDtoList.add(demoDto);
            }
        } else {
            // return limited list
            for (int i = 0; i < (Math.min(numResults, limit)); i++) {
                DemoDto demoDto = DemoMapper.mapToDto(demoArrayList.get(i));
                demoDtoList.add(demoDto);
            }
        }
        return demoDtoList;
    }

    public ArrayList<DemoDto> getPersonalDemos(String username) {
        User user = userRepository.findById(username).get();
        Iterable<Demo> demoList = demoRepository.findByUserOrderByCreatedDateDesc(user);
        ArrayList<DemoDto> resultList = new ArrayList<>();
        for (Demo demo: demoList) {
        DemoDto demoDto = DemoMapper.mapToDto(demo);
        resultList.add(demoDto);
        }
        return resultList;
    }

    public ArrayList<DemoDto> getFavoriteDemos(String username) {
        User user = userRepository.findById(username).get();
        Iterable<Demo> demoList = demoRepository.findByFavoriteOfUsersOrderByTitleAsc(user);
        ArrayList<DemoDto> resultList = new ArrayList<>();
        for (Demo demo: demoList) {
            DemoDto demoDto = DemoMapper.mapToDto(demo);
            resultList.add(demoDto);
        }
        return resultList;
    }

    public DemoDto getDemo(long id) {
        if (demoRepository.findById(id).isPresent()) {
            Demo demo = demoRepository.findById(id).get();
            DemoDto demoDto = DemoMapper.mapToDto(demo);
            return demoDto;
        } else {
            throw new RecordNotFoundException();
        }
    }

    public boolean checkIsFav(long id) throws UserPrincipalNotFoundException {
        if (demoRepository.findById(id).isPresent()) {
            Demo demo = demoRepository.findById(id).get();

            // Set the User object from the Security context (NOT the request body!)
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            if (userRepository.findById(currentPrincipalName) != null) {
               User currentUser = (userRepository.findById(currentPrincipalName).get());
                if (demo.getFavoriteOfUsers().contains(currentUser)) {
                    return true;
                } else return false;
            } else throw new UserPrincipalNotFoundException(currentPrincipalName);
        } else {
            throw new RecordNotFoundException();
        }
    }

    public DemoDto createDemo(DemoDto demoDto) throws UserPrincipalNotFoundException {
        Demo demo =  DemoMapper.mapToModel(demoDto);
        demo.setCreatedDate(Date.from(Instant.now()));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // Set the User object from the Security context (NOT the request body!)
        String currentPrincipalName = authentication.getName();
        if (userRepository.findById(currentPrincipalName) != null) {
            demo.setUser(userRepository.findById(currentPrincipalName).get());
        } else throw new UserPrincipalNotFoundException(currentPrincipalName);
        Demo savedDemo = demoRepository.save(demo);

        // before returning, send a confirmation email:
        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setSubject("DemoDrop - Demo upload confirmation");
        HyperlinkBuilder hyperlinkBuilder = new HyperlinkBuilder();
        String hyperlink = hyperlinkBuilder.buildDemoHyperlink(savedDemo);
        // build email body:
        StringBuilder bodyBuilder = new StringBuilder();
        bodyBuilder.append("Your new demo named ");
        bodyBuilder.append(demo.getTitle());
        bodyBuilder.append(" was successfully uploaded to the system.\nClick on the link below to see it.\n ");
        bodyBuilder.append(hyperlink);
        emailDetails.setMsgBody(bodyBuilder.toString());

        emailDetails.setRecipient(demo.getUser().getEmail());
        String sendResult = emailService.sendSimpleMail(emailDetails);
        System.out.println(sendResult);

        return DemoMapper.mapToDto(savedDemo);
    }

    public DemoDto updateDemo(long id, DemoDto demoDto) {
        if (demoRepository.findById(id).isPresent()) {
            Demo demo = demoRepository.findById(id).get();
            demo.setTitle(demoDto.getTitle());
            demo.setLength(demoDto.getLength());
            demo.setBpm(demoDto.getBpm());
            //Relationships
            if (demoDto.getAudioFile() != null) {
                demo.setAudioFile(demoDto.getAudioFile());
            }
            if (demoDto.getGenre() != null) {
                demo.setGenre(demoDto.getGenre());
            }
            demoRepository.save(demo);
            return DemoMapper.mapToDto(demo);
        } else {
            throw new RecordNotFoundException();
        }
    }

    public boolean setFavStatus(long id, boolean desiredStatus) throws UserPrincipalNotFoundException {
        Demo demo;
        if (demoRepository.findById(id).isPresent()) {
            demo = demoRepository.findById(id).get();
        } else {
            throw new RecordNotFoundException();
        }

        // Set the User object from the Security context (NOT the request body!)
        User currentUser;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        if (userRepository.findById(currentPrincipalName) != null) {
            currentUser = (userRepository.findById(currentPrincipalName).get());
        } else throw new UserPrincipalNotFoundException(currentPrincipalName);
        List<User> usersWhoFavoritedThisDemo = demo.getFavoriteOfUsers();
        Boolean currentStatus = demo.getFavoriteOfUsers().contains(currentUser);
        if (desiredStatus == true) {
            if (currentStatus == false) {
                usersWhoFavoritedThisDemo.add(currentUser);
            }
        }
        else if (desiredStatus == false) {
            if (currentStatus == true) {
                usersWhoFavoritedThisDemo.remove(currentUser);
            }
        }
        else throw new IllegalArgumentException();
        demo.setFavoriteOfUsers(usersWhoFavoritedThisDemo);
        demoRepository.save(demo);
        return checkIsFav(id);
    }

    public DemoDto assignGenreToDemo(long id, String name) {
        Demo demo;
        Genre genre;
        if (demoRepository.findById(id).isPresent()) {
            demo = demoRepository.findById(id).get();
        } else {
            throw new RecordNotFoundException();
        }
        if (genreRepository.findById(name).isPresent()) {
            genre = genreRepository.findById(name).get();
        } else {
            throw new RecordNotFoundException();
        }
        demo.setGenre(genre);
        demoRepository.save(demo);
        return DemoMapper.mapToDto(demo);
    }

    public DemoDto addUserToFavoriteOfUsersList(long id, String username) {
        Demo demo;
        User user ;
        if (demoRepository.findById(id).isPresent()) {
            demo = demoRepository.findById(id).get();
        } else {
            throw new RecordNotFoundException();
        }
        if (userRepository.findById(username).isPresent()) {
            user = userRepository.findById(username).get();
        } else {
            throw new RecordNotFoundException();
        }
        // get list of users who favorited this demo:
        List<User> userList = demo.getFavoriteOfUsers();
        userList.add(user);
        demo.setFavoriteOfUsers(userList);
        demoRepository.save(demo);
        return DemoMapper.mapToDto(demo);
    }

    public DemoDto removeUserFromFavoriteOfUsersList(long id, String username) {
        Demo demo;
        User user ;
        if (demoRepository.findById(id).isPresent()) {
            demo = demoRepository.findById(id).get();
        } else {
            throw new RecordNotFoundException();
        }
        if (userRepository.findById(username).isPresent()) {
            user = userRepository.findById(username).get();
        } else {
            throw new RecordNotFoundException();
        }
        // get list of users who favorited this demo:
        List<User> userList = demo.getFavoriteOfUsers();
        boolean success;
        if (userList.contains(user)) {
            success = userList.remove(user);
        } else throw  new UsernameNotFoundException(username);
        if (!success) {
            throw new RuntimeException();
        }
        demo.setFavoriteOfUsers(userList);
        demoRepository.save(demo);
        return DemoMapper.mapToDto(demo);
    }

    public long deleteDemos() {
        if (demoRepository.findAll() != null) {
            List<Demo> demos= demoRepository.findAll();
            long numDeletedDemos = 0;
            for ( Demo demo : demos  ) {
                demoRepository.delete(demo);
                numDeletedDemos++;
            }
            return numDeletedDemos;
        } else {
            throw new RecordNotFoundException();
        }
    }

    public long deleteDemo(long id) {
        if (demoRepository.findById(id).isPresent()) {
            Demo demo = demoRepository.findById(id).get();
            long retrievedId = demo.getDemoId();
            demoRepository.deleteById(id);
            return retrievedId;
        } else {
            throw new RecordNotFoundException();
        }
    }

    public Iterable<DemoDto> getDemoContaining(String query) {
        Iterable<Demo> foundDemos = demoRepository.findByTitleContaining(query);
        ArrayList<DemoDto> resultList = new ArrayList<>();
        for (Demo d : foundDemos) {
            DemoDto newDemoDto = DemoMapper.mapToDto(d);
            resultList.add(newDemoDto);
        }
        return resultList;
    }

    public String assignFileToDemo(Long fileId, Long demoId) {
        Optional<Demo> optionalDemo = demoRepository.findById(demoId);
        Optional<AudioFile> optionalFile = audioFileRepository.findById(fileId);
        if (optionalDemo.isPresent() && optionalFile.isPresent()) {
            Demo demo = optionalDemo.get();
            AudioFile audioFile = optionalFile.get();
            demo.setAudioFile(audioFile);
            demoRepository.save(demo);
            return "file " + fileId + " was successfully assigned to demo " + demoId;
        } else throw new RecordNotFoundException();
    }
}