package nl.ultimateapps.demoDrop.Helpers.mappers;

import nl.ultimateapps.demoDrop.Dtos.output.*;
import nl.ultimateapps.demoDrop.Models.AudioFile;
import nl.ultimateapps.demoDrop.Models.Demo;
import nl.ultimateapps.demoDrop.Models.Genre;
import nl.ultimateapps.demoDrop.Models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class DemoMapper {

    static boolean processing = false;

    public static Demo mapToModel(DemoDto demoDto) {
        if (processing) {
            return null;
        }
        processing = true;
        Demo demo = mapDemoDtoToDemo(demoDto);
        processing = false;
        return demo;
    }

    public static Demo mapDemoDtoToDemo(DemoDto demoDto) {
        Demo demo = new Demo();

        //normal fields:
        demo.setDemoId(demoDto.getDemoId());
        demo.setCreatedDate(demoDto.getCreatedDate());
        demo.setTitle(demoDto.getTitle());
        demo.setLength(demoDto.getLength());
        demo.setBpm(demoDto.getBpm());

        // Relationships (objects):
        demo.setAudioFile(demoDto.getAudioFile() != null ? demoDto.getAudioFile().toModel() : null);
        demo.setGenre(demoDto.getGenre() != null ? demoDto.getGenre().toModel(): null);
        demo.setProducer(demoDto.getProducer() != null ? demoDto.getProducer().toModel() : null);

        // Relationships (lists)
        demo.setConversations(demoDto.getConversations() != null ? ConversationMapper.mapToModel(demoDto.getConversations()) : null);
        demo.setUserReports(demoDto.getUserReports() != null ? UserReportMapper.mapToModel(demoDto.getUserReports()) : null);

        return demo;
    }

    public static List<Demo> mapToModel(List<DemoDto> demoDtoList) {
        List<Demo> demoList = new ArrayList<>();
        for (DemoDto demoDto : demoDtoList) {
            Demo demo = mapToModel(demoDto);
            demoList.add(demo);
        }
        return demoList;
    }

    public static DemoDto mapToDto(Demo demo) {
        if (processing) {
            return null;
        }
        processing = true;
        DemoDto demoDto = mapDemoToDemoDto(demo);
        processing = false;
        return demoDto;
    }

    private static DemoDto mapDemoToDemoDto(Demo demo) {
        DemoDto demoDto = new DemoDto();

        //Normal fields
        demoDto.setDemoId(demo.getDemoId());
        demoDto.setCreatedDate(demo.getCreatedDate());
        demoDto.setTitle(demo.getTitle());
        demoDto.setLength(demo.getLength());
        demoDto.setBpm(demo.getBpm());

        // Relationships (objects)
        demoDto.setAudioFile(demo.getAudioFile() != null ? demo.getAudioFile().toDto() : null);
        demoDto.setGenre(demo.getGenre() != null ? demo.getGenre().toDto() : null);
        demoDto.setProducer(demo.getProducer() != null ? demo.getProducer().toPublicDto() : null);

        // Relationships (lists)
        demoDto.setConversations(demo.getConversations() != null ? ConversationMapper.mapToDto(demo.getConversations()) : null);
        demoDto.setFavoriteOfUsers(demo.getFavoriteOfUsers() != null ? UserMapper.mapToPublicDto(demo.getFavoriteOfUsers()): null);
        demoDto.setUserReports(demo.getUserReports() != null ? UserReportMapper.mapToDto(demo.getUserReports()) : null);

        return demoDto;
    }

    public static List<DemoDto> mapToDto(List<Demo> demoList) {
        List<DemoDto> demoDtoList = new ArrayList<>();
        for (Demo demo : demoList) {
            DemoDto demoDto = mapToDto(demo);
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