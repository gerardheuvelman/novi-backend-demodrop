package nl.ultimateapps.demoDrop.Helpers.mappers;

import nl.ultimateapps.demoDrop.Dtos.output.EmailDetailsDto;
import nl.ultimateapps.demoDrop.Models.EmailDetails;
import org.modelmapper.ModelMapper;

public class EmailDetailsMapper {

    public static EmailDetails mapToModel(EmailDetailsDto emailDetailsDto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(emailDetailsDto, EmailDetails.class);
    }

    public static EmailDetailsDto mapToDto(EmailDetails emailDetails) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(emailDetails, EmailDetailsDto.class);
    }
}
