package nl.ultimateapps.demoDrop.Helpers.mappers;

import nl.ultimateapps.demoDrop.Dtos.output.UserDto;
import nl.ultimateapps.demoDrop.Dtos.output.UserPublicDto;
import nl.ultimateapps.demoDrop.Models.User;
import org.modelmapper.ModelMapper;

public class UserMapper {

    public static User mapToModel(UserDto userDto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(userDto, User.class);
    }

    public static UserDto mapToDto(User user) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(user, UserDto.class);
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