package nl.ultimateapps.demoDrop.Helpers.mappers;

import nl.ultimateapps.demoDrop.Dtos.output.ConversationDto;
import nl.ultimateapps.demoDrop.Models.Conversation;
import org.modelmapper.ModelMapper;

public class ConversationMapper {
    public static Conversation mapToModel(ConversationDto conversationDto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(conversationDto, Conversation.class);
    }

    public static ConversationDto mapToDto(Conversation conversation) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(conversation, ConversationDto.class);
    }
}