package nl.ultimateapps.demoDrop.Helpers.mappers;

import nl.ultimateapps.demoDrop.Dtos.output.DemoDto;
import nl.ultimateapps.demoDrop.Dtos.output.UserPrivateDto;
import nl.ultimateapps.demoDrop.Dtos.output.UserPublicDto;
import nl.ultimateapps.demoDrop.Models.Demo;
import nl.ultimateapps.demoDrop.Models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserMapper {

    static boolean processing = false;

    public static User mapToModel(UserPrivateDto userPrivateDto) {
        if (processing) {
            return null;
        }
        processing = true;
        User user = mapUserPrivateDtoToUser(userPrivateDto);
        processing = false;
        return user;
    }


    private static User mapUserPrivateDtoToUser(UserPrivateDto userPrivateDto) {
        User user = new User();

        //normal fields:
        user.setUsername(userPrivateDto.getUsername());
        user.setPassword(userPrivateDto.getPassword());
        user.setEnabled(userPrivateDto.isEnabled());
        user.setEnabled(userPrivateDto.isEnabled());
        user.setEmail(userPrivateDto.getEmail());
        user.setCreatedDate(userPrivateDto.getCreatedDate());

        // Relationships (lists)
        user.setAuthorities(userPrivateDto.getAuthorities() != null ? AuthorityMapper.mapToModel(userPrivateDto.getAuthorities()) : null);

        user.setDemos(userPrivateDto.getDemos() != null ? DemoMapper.mapToModel(userPrivateDto.getDemos()) : null);

        user.setConversationsAsInitiator(userPrivateDto.getConversationsAsInitiator() != null ? ConversationMapper.mapToModel(userPrivateDto.getConversationsAsInitiator()) : null);

        user.setConversationsAsCorrespondent(userPrivateDto.getConversationsAsCorrespondent() != null ? ConversationMapper.mapToModel(userPrivateDto.getConversationsAsCorrespondent()) : null);

        user.setUserReportsAsReporter(userPrivateDto.getUserReportsAsReporter() != null ? UserReportMapper.mapToModel(userPrivateDto.getUserReportsAsReporter()) : null);

        user.setUserReportsAsReportedUser(userPrivateDto.getUserReportsAsReportedUser() != null ? UserReportMapper.mapToModel(userPrivateDto.getUserReportsAsReportedUser()) : null);

        user.setFavoriteDemos(userPrivateDto.getFavoriteDemos() != null ? DemoMapper.mapToModel(userPrivateDto.getFavoriteDemos()) : null);

        return user;
    }

    public static User mapToModel(UserPublicDto userPublicDto) {
        if (processing) {
            return null;
        }
        processing = true;
        User user = mapUserPublicDtoToUser(userPublicDto);
        processing = false;
        return user;
    }


    private static User mapUserPublicDtoToUser(UserPublicDto userPublicDto) {
        User user = new User();

        //normal fields:
        user.setUsername(userPublicDto.getUsername());
        user.setCreatedDate(userPublicDto.getCreatedDate());

         //Relationships (lists)
        user.setDemos(userPublicDto.getDemos() != null ? DemoMapper.mapToModel(userPublicDto.getDemos()) : null);

        return user;
    }

    public static List<User> mapToModelFromPrivate(List<UserPrivateDto> userPrivateDtoList) {
        List<User> userList = new ArrayList<>();
        for (UserPrivateDto userPrivateDto : userPrivateDtoList) {
            User user = mapToModel(userPrivateDto);
            userList.add(user);
        }
        return userList;
    }

    public static List<User> mapToModelFromPublic(List<UserPublicDto> userPublicDtoList) {
        List<User> userList = new ArrayList<>();
        for (UserPublicDto userPublicDto : userPublicDtoList) {
            User user = mapToModel(userPublicDto);
            userList.add(user);
        }
        return userList;
    }

    // Since we need to be cautious of privacy, we define two Data Transfer Objects: UserPublicDto, which only contains non-sensitive information, and UserPrivateDto, which contains all known user information.

    public static UserPrivateDto mapToPrivateDto(User user) {
        if (processing) {
            return null;
        }
        processing = true;
        UserPrivateDto userPrivateDto = mapUserToUserPrivateDto(user);
        processing = false;
        return userPrivateDto;
    }

    private static UserPrivateDto mapUserToUserPrivateDto(User user) {
        UserPrivateDto userPrivateDto = new UserPrivateDto();

        userPrivateDto.setUsername(user.getUsername());
        userPrivateDto.setPassword(user.getPassword());
        userPrivateDto.setEnabled(user.isEnabled());
        userPrivateDto.setEmail(user.getEmail());
        userPrivateDto.setCreatedDate(user.getCreatedDate());

        // Relationships (lists)
        userPrivateDto.setAuthorities(user.getAuthorities() != null ? AuthorityMapper.mapToDto(user.getAuthorities()) : null);

        userPrivateDto.setDemos(user.getDemos() != null ? DemoMapper.mapToDto(user.getDemos()) : null);

        userPrivateDto.setConversationsAsInitiator(user.getConversationsAsInitiator() != null ? ConversationMapper.mapToDto(user.getConversationsAsInitiator()) : null);

        userPrivateDto.setConversationsAsCorrespondent(user.getConversationsAsCorrespondent() != null ? ConversationMapper.mapToDto(user.getConversationsAsCorrespondent()) : null);

        userPrivateDto.setUserReportsAsReporter(user.getUserReportsAsReporter() != null ? UserReportMapper.mapToDto(user.getUserReportsAsReporter()): null);

        userPrivateDto.setUserReportsAsReportedUser(user.getUserReportsAsReportedUser() != null ? UserReportMapper.mapToDto(user.getUserReportsAsReportedUser()) : null);

        userPrivateDto.setFavoriteDemos(user.getFavoriteDemos() != null ? DemoMapper.mapToDto(user.getFavoriteDemos()) :null);
        return userPrivateDto;
    }

    public static List<UserPrivateDto> mapToPrivateDto(List<User> userList) {
        List<UserPrivateDto> userPrivateDtoList = new ArrayList<>();
        for (User user : userList) {
            UserPrivateDto userPrivateDto = mapToPrivateDto(user);
            userPrivateDtoList.add(userPrivateDto);
        }
        return userPrivateDtoList;
    }

    public static List<UserPrivateDto> mapToPrivateDto(Iterable<User> userIterable) {
        List<User> userList = StreamSupport.stream(userIterable.spliterator(), false)
                .collect(Collectors.toList());
        return mapToPrivateDto(userList);
    }


    public static UserPublicDto mapToPublicDto(User user) {
        if (processing) {
            return null;
        }
        processing = true;
        UserPublicDto userPublicDto = mapUserToUserPublicDto(user);
        processing = false;
        return userPublicDto;
    }


    public static UserPublicDto mapUserToUserPublicDto(User user) {
        UserPublicDto userPublicDto = new UserPublicDto();

        //Normal fields
        userPublicDto.setUsername(user.getUsername());
        userPublicDto.setCreatedDate(user.getCreatedDate());

        //Relationships (lists)
        userPublicDto.setDemos(user.getDemos() != null ? DemoMapper.mapToDto(user.getDemos()) : null);
        return userPublicDto;
    }

    public static List<UserPublicDto> mapToPublicDto(List<User> userList) {
        List<UserPublicDto> userPublicDtoList = new ArrayList<>();
        for (User user : userList) {
            UserPublicDto userPublicDto = mapToPublicDto(user);
            userPublicDtoList.add(userPublicDto);
        }
        return userPublicDtoList;
    }

    public static List<UserPublicDto> mapToPublicDto(Iterable<User> userIterable) {
        List<User> userList = StreamSupport.stream(userIterable.spliterator(), false)
                .collect(Collectors.toList());
        return mapToPublicDto(userList);
    }
}