package nl.ultimateapps.demoDrop.Helpers.mappers;

import nl.ultimateapps.demoDrop.Dtos.output.AuthorityDto;
import nl.ultimateapps.demoDrop.Dtos.output.AuthorityDto;
import nl.ultimateapps.demoDrop.Dtos.output.AuthorityDto;
import nl.ultimateapps.demoDrop.Dtos.output.GenreDto;
import nl.ultimateapps.demoDrop.Models.Authority;
import nl.ultimateapps.demoDrop.Models.Authority;
import nl.ultimateapps.demoDrop.Models.Authority;
import nl.ultimateapps.demoDrop.Models.Genre;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AuthorityMapper {

    public static Authority mapToModel(AuthorityDto authorityDto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(authorityDto, Authority.class);
    }

    public static List<Authority> mapToModel(List<AuthorityDto> authorityDtoList) {
        ModelMapper modelMapper = new ModelMapper();
        List<Authority> authorityList = new ArrayList<>();
        for (AuthorityDto authorityDto : authorityDtoList) {
            Authority Authority = modelMapper.map(authorityDto, Authority.class);
            authorityList.add(Authority);
        }
        return authorityList;
    }

    public static Set<Authority> mapToModel(Set<AuthorityDto> authorityDtoSet) {
        ModelMapper modelMapper = new ModelMapper();
        Set<Authority> authoritySet = new HashSet<>();
        for (AuthorityDto authorityDto : authorityDtoSet) {
            Authority Authority = modelMapper.map(authorityDto, Authority.class);
            authoritySet.add(Authority);
        }
        return authoritySet;
    }
    
    public static AuthorityDto mapToDto(Authority authority) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(authority, AuthorityDto.class);
    }

    public static List<AuthorityDto> mapToDto(List<Authority> authorityList) {
        ModelMapper modelMapper = new ModelMapper();
        List<AuthorityDto> authorityDtoList = new ArrayList<>();
        for (Authority authority : authorityList) {
            AuthorityDto authorityDto = modelMapper.map(authority, AuthorityDto.class);
            authorityDtoList.add(authorityDto);
        }
        return authorityDtoList;
    }

    public static Set<AuthorityDto> mapToDto(Set<Authority> authoritySet) {
        ModelMapper modelMapper = new ModelMapper();
        Set<AuthorityDto> authorityDtoSet = new HashSet<>();
        for (Authority authority : authoritySet) {
            AuthorityDto authorityDto = modelMapper.map(authority, AuthorityDto.class);
            authorityDtoSet.add(authorityDto);
        }
        return authorityDtoSet;
    }

    public static List<AuthorityDto> mapToDto(Iterable<Authority> authorityIterable) {
        List<Authority> authorityList = StreamSupport.stream(authorityIterable.spliterator(), false)
                .collect(Collectors.toList());
        return mapToDto(authorityList);
    }

}
