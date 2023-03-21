package nl.ultimateapps.demoDrop.Helpers.mappers;

import nl.ultimateapps.demoDrop.Dtos.output.EmailDetailsDto;
import nl.ultimateapps.demoDrop.Dtos.output.EmailDetailsDto;
import nl.ultimateapps.demoDrop.Dtos.output.GenreDto;
import nl.ultimateapps.demoDrop.Models.EmailDetails;
import nl.ultimateapps.demoDrop.Models.EmailDetails;
import nl.ultimateapps.demoDrop.Models.Genre;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class EmailDetailsMapper {

    public static EmailDetails mapToModel(EmailDetailsDto emailDetailsDto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(emailDetailsDto, EmailDetails.class);
    }

    public static List<EmailDetails> mapToModel(List<EmailDetailsDto> emailDetailsDtoList) {
        ModelMapper modelMapper = new ModelMapper();
        List<EmailDetails> emailDetailsList = new ArrayList<>();
        for (EmailDetailsDto emailDetailsDto : emailDetailsDtoList) {
            EmailDetails emailDetails = modelMapper.map(emailDetailsDto, EmailDetails.class);
            emailDetailsList.add(emailDetails);
        }
        return emailDetailsList;
    }
    
    public static EmailDetailsDto mapToDto(EmailDetails emailDetails) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(emailDetails, EmailDetailsDto.class);
    }

    public static List<EmailDetailsDto> mapToDto(List<EmailDetails> emailDetailsList) {
        ModelMapper modelMapper = new ModelMapper();
        List<EmailDetailsDto> emailDetailsDtoList = new ArrayList<>();
        for (EmailDetails emailDetails : emailDetailsList) {
            EmailDetailsDto emailDetailsDto = modelMapper.map(emailDetails, EmailDetailsDto.class);
            emailDetailsDtoList.add(emailDetailsDto);
        }
        return emailDetailsDtoList;
    }

    public static List<EmailDetailsDto> mapToDto(Iterable<EmailDetails> emailDetailsIterable) {
        List<EmailDetails> emailDetailsList = StreamSupport.stream(emailDetailsIterable.spliterator(), false)
                .collect(Collectors.toList());
        return mapToDto(emailDetailsList);
    }
}
