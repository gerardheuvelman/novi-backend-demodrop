package nl.ultimateapps.demoDrop.Helpers.mappers;

import nl.ultimateapps.demoDrop.Dtos.output.UserDto;
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
}