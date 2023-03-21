package nl.ultimateapps.demoDrop.Helpers.mappers;

import nl.ultimateapps.demoDrop.Dtos.output.DemoDto;
import nl.ultimateapps.demoDrop.Dtos.output.DemoDto;
import nl.ultimateapps.demoDrop.Dtos.output.GenreDto;
import nl.ultimateapps.demoDrop.Models.Demo;
import nl.ultimateapps.demoDrop.Models.Demo;
import nl.ultimateapps.demoDrop.Models.Genre;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class DemoMapper {

    public static Demo mapToModel(DemoDto demoDto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(demoDto, Demo.class);
    }

    public static List<Demo> mapToModel(List<DemoDto> demoDtoList) {
        ModelMapper modelMapper = new ModelMapper();
        List<Demo> demoList = new ArrayList<>();
        for (DemoDto demoDto : demoDtoList) {
            Demo demo = modelMapper.map(demoDto, Demo.class);
            demoList.add(demo);
        }
        return demoList;
    }


    public static DemoDto mapToDto(Demo demo) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(demo, DemoDto.class);
    }

    public static List<DemoDto> mapToDto(List<Demo> demoList) {
        ModelMapper modelMapper = new ModelMapper();
        List<DemoDto> demoDtoList = new ArrayList<>();
        for (Demo demo : demoList) {
            DemoDto demoDto = modelMapper.map(demo, DemoDto.class);
            demoDtoList.add(demoDto);
        }
        return demoDtoList;
    }

    public static List<DemoDto> mapToDto(Iterable<Demo> demoIterable) {
        List<Demo> demoList = StreamSupport.stream(demoIterable.spliterator(), false)
                .collect(Collectors.toList());
        return mapToDto(demoList);
    }
    
}