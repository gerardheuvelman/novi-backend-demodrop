package nl.ultimateapps.demoDrop.Helpers.mappers;

import nl.ultimateapps.demoDrop.Dtos.output.DemoDto;
import nl.ultimateapps.demoDrop.Models.Demo;
import org.modelmapper.ModelMapper;

public class DemoMapper {

    public static Demo mapToModel(DemoDto demoDto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(demoDto, Demo.class);
    }

    public static DemoDto mapToDto(Demo demo) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(demo, DemoDto.class);
    }
}