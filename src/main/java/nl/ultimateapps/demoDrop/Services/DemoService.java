package nl.ultimateapps.demoDrop.Services;

import nl.ultimateapps.demoDrop.Dtos.output.DemoDto;
import nl.ultimateapps.demoDrop.Exceptions.RecordNotFoundException;
import nl.ultimateapps.demoDrop.Exceptions.UsernameNotFoundException;
import nl.ultimateapps.demoDrop.Helpers.mappers.DemoMapper;
import nl.ultimateapps.demoDrop.Models.*;
import nl.ultimateapps.demoDrop.Repositories.*;
import nl.ultimateapps.demoDrop.Utils.AuthHelper;
import nl.ultimateapps.demoDrop.Utils.HyperlinkBuilder;
import nl.ultimateapps.demoDrop.Utils.JwtUtil;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class DemoService {

    private final JwtUtil jwtUtil;
    private final DemoRepository demoRepository;
    private final UserRepository userRepository;
    private final GenreRepository genreRepository;
    private final AudioFileRepository audioFileRepository;
    private final AudioFileService audioFileService;
    private final EmailService emailService;

    public ArrayList<DemoDto> getDemos(int limit) { // NO AUTH
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

    public ArrayList<DemoDto> getPersonalDemos(String username) { // NO AUTH
        User user = userRepository.findById(username).get();
        Iterable<Demo> demoList = demoRepository.findByUserOrderByCreatedDateDesc(user);
        ArrayList<DemoDto> resultList = new ArrayList<>();
        for (Demo demo : demoList) {
            DemoDto demoDto = DemoMapper.mapToDto(demo);
            resultList.add(demoDto);
        }
        return resultList;
    }

    public ArrayList<DemoDto> getFavoriteDemos(String username) { //AUTH ONLY
        if (!AuthHelper.checkAuthorization(username)) { // check associative authorization
            throw new org.springframework.security.access.AccessDeniedException("User has insufficient rights to access Favorited demos for user " + username);
        }
        User user = userRepository.findById(username).get();
        Iterable<Demo> demoList = demoRepository.findByFavoriteOfUsersOrderByTitleAsc(user);
        ArrayList<DemoDto> resultList = new ArrayList<>();
        for (Demo demo : demoList) {
            DemoDto demoDto = DemoMapper.mapToDto(demo);
            resultList.add(demoDto);
        }
        return resultList;
    }

    public DemoDto getDemo(long demoId) { // NO AUTH
        if (demoRepository.findById(demoId).isPresent()) {
            Demo demo = demoRepository.findById(demoId).get();
            DemoDto demoDto = DemoMapper.mapToDto(demo);
            return demoDto;
        } else {
            throw new RecordNotFoundException();
        }
    }

    public boolean checkIsFav(long demoId) throws UserPrincipalNotFoundException { // AUTH ONLY
        if (demoRepository.findById(demoId).isPresent()) {
            Demo demo = demoRepository.findById(demoId).get();
            String currentPrincipalName = AuthHelper.getPrincipalUsername();
            if (userRepository.findById(currentPrincipalName).isPresent()) {
                User currentUser = (userRepository.findById(currentPrincipalName).get());
                return demo.getFavoriteOfUsers().contains(currentUser);
            } else throw new UserPrincipalNotFoundException(currentPrincipalName);
        } else {
            throw new RecordNotFoundException();
        }
    }

    public DemoDto createDemo(DemoDto demoDto) throws UserPrincipalNotFoundException { // AUTH ONLY
        Demo demo = DemoMapper.mapToModel(demoDto);
        demo.setCreatedDate(Date.from(Instant.now()));

        // Set the User object from the Security context (NOT the request body!)
        String currentPrincipalName = AuthHelper.getPrincipalUsername();
        if (userRepository.findById(currentPrincipalName).isPresent()) {
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

        emailDetails.setRecipientUsername(demo.getUser().getUsername());
        String sendResult = emailService.sendSimpleMail(emailDetails);

        return DemoMapper.mapToDto(savedDemo);
    }

    public DemoDto updateDemo(long demoId, DemoDto demoDto) { //AUTH ONLY
        if (demoRepository.findById(demoId).isPresent()) {
            Demo demo = demoRepository.findById(demoId).get();
            if (!AuthHelper.checkAuthorization(demo)) { // check associative authorization:
                throw new AccessDeniedException("User" + AuthHelper.getPrincipalUsername() + "has insufficient rights to update demo " + demoId);
            }
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

    public boolean setFavStatus(long demoId, boolean desiredStatus) throws UserPrincipalNotFoundException { // AUTH ONLY
        Demo demo;
        if (demoRepository.findById(demoId).isPresent()) {
            demo = demoRepository.findById(demoId).get();
        } else {
            throw new RecordNotFoundException();
        }
        // Set the User object from the Security context (NOT the request body!)
        User currentUser;
        String currentPrincipalName = AuthHelper.getPrincipalUsername();
        if (userRepository.findById(currentPrincipalName).isPresent()) {
            currentUser = (userRepository.findById(currentPrincipalName).get());
        } else throw new UserPrincipalNotFoundException(currentPrincipalName);
        List<User> usersWhoFavoritedThisDemo = demo.getFavoriteOfUsers();
        boolean currentStatus = demo.getFavoriteOfUsers().contains(currentUser);
        if (desiredStatus) {
            if (!currentStatus) {
                usersWhoFavoritedThisDemo.add(currentUser);
            }
        } else if (!desiredStatus) {
            if (currentStatus) {
                usersWhoFavoritedThisDemo.remove(currentUser);
            }
        } else throw new IllegalArgumentException();
        demo.setFavoriteOfUsers(usersWhoFavoritedThisDemo);
        demoRepository.save(demo);
        return checkIsFav(demoId);
    }

    public DemoDto assignGenreToDemo(long demoId, String name) throws AccessDeniedException { //AUTH ONLY
        Demo demo;
        Genre genre;
        if (demoRepository.findById(demoId).isPresent()) {
            demo = demoRepository.findById(demoId).get();
        } else {
            throw new RecordNotFoundException();
        }
        if (!AuthHelper.checkAuthorization(demo)) { // Check associative authorization
            throw new AccessDeniedException("User " + AuthHelper.getPrincipalUsername() + " has insufficient rights to assign a music genre to demo " + demoId);
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

//    public DemoDto addUserToFavoriteOfUsersList(long demoId, String username) { // AUTH ONLY
//        if (!AuthHelper.checkAuthorization(username)) { // Check associative authorization:
//            throw new AccessDeniedException("User " + AuthHelper.getPrincipalUsername() + " has insufficient rights to add a Demo to the 'favorite demos' list of user " + username);
//        }
//        User user;
//        if (userRepository.findById(username).isPresent()) {
//            user = userRepository.findById(username).get();
//        } else {
//            throw new RecordNotFoundException();
//        }
//        Demo demo;
//        if (demoRepository.findById(demoId).isPresent()) {
//            demo = demoRepository.findById(demoId).get();
//        } else {
//            throw new RecordNotFoundException();
//        }
//
//        // get list of users who favorited this demo:
//        List<User> userList = demo.getFavoriteOfUsers();
//        userList.add(user);
//        demo.setFavoriteOfUsers(userList);
//        demoRepository.save(demo);
//        return DemoMapper.mapToDto(demo);
//    }
//
//    public DemoDto removeUserFromFavoriteOfUsersList(long demoId, String username) { // AUTH ONLY
//        if (!AuthHelper.checkAuthorization(username)) { // Check associative authorization:
//            throw new AccessDeniedException("User " + AuthHelper.getPrincipalUsername() + " has insufficient rights to remove a Demo from the 'favorite demos' list of user " + username);
//        }
//        Demo demo;
//        User user;
//        if (demoRepository.findById(demoId).isPresent()) {
//            demo = demoRepository.findById(demoId).get();
//        } else {
//            throw new RecordNotFoundException();
//        }
//        if (userRepository.findById(username).isPresent()) {
//            user = userRepository.findById(username).get();
//        } else {
//            throw new RecordNotFoundException();
//        }
//        // get list of users who favorited this demo:
//        List<User> userList = demo.getFavoriteOfUsers();
//        boolean success;
//        if (userList.contains(user)) {
//            success = userList.remove(user);
//        } else throw new UsernameNotFoundException(username);
//        if (!success) {
//            throw new RuntimeException();
//        }
//        demo.setFavoriteOfUsers(userList);
//        demoRepository.save(demo);
//        return DemoMapper.mapToDto(demo);
//    }

    public long deleteDemos() { // ADMIN ONLY
        List<Demo> demos = demoRepository.findAll();
        long numDeletedDemos = 0;
        for (Demo demo : demos) {
            demoRepository.delete(demo);
            numDeletedDemos++;
        }
        return numDeletedDemos;

    }

    public long deleteDemo(long demoId) { // AUTH ONLY
        Demo demo;
        if (demoRepository.findById(demoId).isPresent()) {
            demo = demoRepository.findById(demoId).get();
        } else throw new RecordNotFoundException();
        if (!AuthHelper.checkAuthorization(demo)) { // Check associative authorization
            throw new AccessDeniedException("User" + AuthHelper.getPrincipalUsername() + "has insufficient rights to delete Demo " + demoId);
        }
        long retrievedId = demo.getDemoId();
        demoRepository.deleteById(demoId);
        return retrievedId;
    }

    public Iterable<DemoDto> getDemoContaining(String query) { // NO AUTH
        Iterable<Demo> foundDemos = demoRepository.findByTitleContaining(query);
        ArrayList<DemoDto> resultList = new ArrayList<>();
        for (Demo d : foundDemos) {
            DemoDto newDemoDto = DemoMapper.mapToDto(d);
            resultList.add(newDemoDto);
        }
        return resultList;
    }

    public boolean uploadFileAndAssignToDemo(Long demoId, MultipartFile multipartFile) throws AccessDeniedException { // AUTH ONLY
        DemoDto demoDto = getDemo(demoId);
        User associatedUser = demoDto.getUser();
        if (AuthHelper.checkAuthorization(associatedUser)) { // check associative authorization
            AudioFile audioFile = audioFileService.processFileUpload(multipartFile);
            return assignFileToDemo(audioFile.getAudioFileId(), demoId);
        } else
            throw new AccessDeniedException("User" + AuthHelper.getPrincipalUsername() + "has insufficient rights to upload a file to be associated with demo " + demoId);
    }

    public boolean assignFileToDemo(Long fileId, Long demoId) { // AUTH
        if (!demoRepository.findById(demoId).isPresent()){
            throw new RecordNotFoundException("Demo not found");
        }
        Demo demo = demoRepository.findById(demoId).get();

        if (!AuthHelper.checkAuthorization(demo)) { // check associative authorization
            throw new AccessDeniedException("User" + AuthHelper.getPrincipalUsername() + "has insufficient rights to assign a file toe Demo " + demoId);
        }
        if (!audioFileRepository.findById(fileId).isPresent()){
            throw new RecordNotFoundException("AudioFile not found");
        }
        AudioFile audioFile = audioFileRepository.findById(fileId).get();
        demo.setAudioFile(audioFile);
        demoRepository.save(demo);
        return true;
    }

    public Resource downloadMp3File(long demoId) {
        if (demoRepository.findById(demoId).isEmpty()) {
            throw new RecordNotFoundException("Demo not found");
        }
        Demo demo = demoRepository.findById(demoId).get();
        AudioFile audioFile = demo.getAudioFile();
        String uniformFilename= audioFile.getAudioFileId() + ".mp3";
        Path pathToFile = Paths.get(audioFileService.getFileStorageLocation()).toAbsolutePath().resolve(uniformFilename); // STRING MADE FROM A LONG
        Resource resource;
        try {
            resource = new UrlResource(pathToFile.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Issue in reading the file", e);
        }
        if(resource.exists()&& resource.isReadable()) {
            return resource;
        } else {
            throw new RuntimeException("the file doesn't exist or not readable");
        }
    }
}