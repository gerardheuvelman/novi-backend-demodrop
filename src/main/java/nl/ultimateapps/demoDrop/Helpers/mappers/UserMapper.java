package nl.ultimateapps.demoDrop.Helpers.mappers;

import nl.ultimateapps.demoDrop.Dtos.output.GenreDto;
import nl.ultimateapps.demoDrop.Dtos.output.UserDto;
import nl.ultimateapps.demoDrop.Dtos.output.UserDto;
import nl.ultimateapps.demoDrop.Dtos.output.UserPublicDto;
import nl.ultimateapps.demoDrop.Models.Genre;
import nl.ultimateapps.demoDrop.Models.User;
import nl.ultimateapps.demoDrop.Models.User;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserMapper {

    public static User mapToModel(UserDto userDto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(userDto, User.class);
    }

    public static List<User> mapToModel(List<UserDto> userDtoList) {
        ModelMapper modelMapper = new ModelMapper();
        List<User> userList = new ArrayList<>();
        for (UserDto userDto : userDtoList) {
            User user = modelMapper.map(userDto, User.class);
            userList.add(user);
        }
        return userList;
    }

    public static UserDto mapToDto(User user) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(user, UserDto.class);
    }

    public static List<UserDto> mapToDto(List<User> userList) {
        ModelMapper modelMapper = new ModelMapper();
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : userList) {
            UserDto userDto = modelMapper.map(user, UserDto.class);
            userDtoList.add(userDto);
        }
        return userDtoList;
    }

    public static List<UserDto> mapToDto(Iterable<User> userIterable) {
        List<User> userList = StreamSupport.stream(userIterable.spliterator(), false)
                .collect(Collectors.toList());
        return mapToDto(userList);
    }

//     since we need to be cautious of privacy, we define an extra Dto: UserPublicDto, that only contains non-sensitive information. This dto is never sent in a request, so we only need to map the Model to the DTO:

    public static UserPublicDto mapToPublicDto(User user) {
        UserPublicDto userPublicDto = new UserPublicDto();
        userPublicDto.setUsername(user.getUsername());
        userPublicDto.setCreatedDate(user.getCreatedDate());
        userPublicDto.setDemos(user.getDemos());
        return userPublicDto;
    }


}