package nl.ultimateapps.demoDrop.Helpers.mappers;

import nl.ultimateapps.demoDrop.Dtos.output.ConversationDto;
import nl.ultimateapps.demoDrop.Dtos.output.ConversationDto;
import nl.ultimateapps.demoDrop.Dtos.output.GenreDto;
import nl.ultimateapps.demoDrop.Models.Conversation;
import nl.ultimateapps.demoDrop.Models.Conversation;
import nl.ultimateapps.demoDrop.Models.Genre;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ConversationMapper {
    public static Conversation mapToModel(ConversationDto conversationDto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(conversationDto, Conversation.class);
    }

    public static List<Conversation> mapToModel(List<ConversationDto> conversationDtoList) {
        ModelMapper modelMapper = new ModelMapper();
        List<Conversation> conversationList = new ArrayList<>();
        for (ConversationDto conversationDto : conversationDtoList) {
            Conversation conversation = modelMapper.map(conversationDto, Conversation.class);
            conversationList.add(conversation);
        }
        return conversationList;
    }

    public static ConversationDto mapToDto(Conversation conversation) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(conversation, ConversationDto.class);
    }

    public static List<ConversationDto> mapToDto(List<Conversation> conversationList) {
        ModelMapper modelMapper = new ModelMapper();
        List<ConversationDto> conversationDtoList = new ArrayList<>();
        for (Conversation conversation : conversationList) {
            ConversationDto conversationDto = modelMapper.map(conversation, ConversationDto.class);
            conversationDtoList.add(conversationDto);
        }
        return conversationDtoList;
    }

    public static List<ConversationDto> mapToDto(Iterable<Conversation> conversationIterable) {
        List<Conversation> conversationList = StreamSupport.stream(conversationIterable.spliterator(), false)
                .collect(Collectors.toList());
        return mapToDto(conversationList);
    }

}