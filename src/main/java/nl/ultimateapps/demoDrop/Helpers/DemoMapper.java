package nl.ultimateapps.demoDrop.Helpers;

import nl.ultimateapps.demoDrop.Dtos.output.DemoDto;
import nl.ultimateapps.demoDrop.Models.Demo;

public class DemoMapper {

    public static Demo mapToModel(DemoDto DemoDto) {
        Demo Demo = new Demo();
        Demo.setTitle(DemoDto.getTitle());
        Demo.setCreatedDate(DemoDto.getCreatedDate());
        Demo.setLength(DemoDto.getLength());
        Demo.setAudiofileUrl(DemoDto.getAudiofileUrl());
        if (DemoDto.getConversations() != null) {
            Demo.setConversations(DemoDto.getConversations());
        }
        return Demo;
    }

    public static DemoDto mapToDto(Demo Demo) {
        DemoDto dto = new DemoDto();
        dto.setTitle(Demo.getTitle());
        dto.setCreatedDate(Demo.getCreatedDate());
        dto.setLength(Demo.getLength());
        dto.setAudiofileUrl(Demo.getAudiofileUrl());
        if (Demo.getConversations() != null) {
            dto.setConversations(Demo.getConversations());
        }
        return dto;
    }
}