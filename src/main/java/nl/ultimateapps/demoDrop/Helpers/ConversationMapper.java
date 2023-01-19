package nl.ultimateapps.demoDrop.Helpers;

import nl.ultimateapps.demoDrop.Dtos.output.ConversationDto;
import nl.ultimateapps.demoDrop.Models.Conversation;

public class ConversationMapper {
    public static Conversation mapToModel(ConversationDto ConversationDto) {
        Conversation conversation = new Conversation();
        conversation.setCreatedDate(ConversationDto.getCreatedDate());
        conversation.setLatestReplyDate(ConversationDto.getLatestReplyDate());
        conversation.setSubject(ConversationDto.getSubject());
        conversation.setBody(ConversationDto.getBody());
        if (ConversationDto.getDemo() != null) {
            conversation.setDemo(ConversationDto.getDemo());
        }
        return conversation;
    }

    public static ConversationDto mapToDto(Conversation Conversation) {
        ConversationDto dto = new ConversationDto();
        dto.setCreatedDate(Conversation.getCreatedDate());
        dto.setLatestReplyDate(Conversation.getLatestReplyDate());
        dto.setSubject(Conversation.getSubject());
        dto.setBody(Conversation.getBody());
        if (Conversation.getDemo() != null) {
            dto.setDemo(Conversation.getDemo());
        }
        return dto;
    }
}