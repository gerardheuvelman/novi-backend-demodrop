package nl.ultimateapps.demoDrop.Helpers.mappers;

import nl.ultimateapps.demoDrop.Dtos.output.ConversationDto;
import nl.ultimateapps.demoDrop.Dtos.output.DemoDto;
import nl.ultimateapps.demoDrop.Dtos.output.UserPublicDto;
import nl.ultimateapps.demoDrop.Models.Conversation;
import nl.ultimateapps.demoDrop.Models.Demo;
import nl.ultimateapps.demoDrop.Models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ConversationMapper {

    static boolean processing = false;

    public static Conversation mapToModel(ConversationDto conversationDto) {
        if (processing) {
            return null;
        }
        processing = true;
        Conversation conversation = mapConversationDtoToConversation(conversationDto);
        processing = false;
        return conversation;
    }

    private static Conversation mapConversationDtoToConversation(ConversationDto conversationDto) {
        Conversation conversation = new Conversation();

        // Normal Fields:
        conversation.setConversationId(conversationDto.getConversationId());
        conversation.setCreatedDate(conversationDto.getCreatedDate());
        conversation.setLatestReplyDate(conversationDto.getLatestReplyDate());
        conversation.setHasDemo(conversationDto.isHasDemo());
        conversation.setSubject(conversationDto.getSubject());
        conversation.setBody(conversationDto.getBody());
        conversation.setReadByInitiator(conversationDto.isReadByInitiator());
        conversation.setReadByCorrespondent(conversationDto.isReadByCorrespondent());

        // Relationships (objects)
        conversation.setInitiator(conversationDto.getInitiator() != null ? conversationDto.getInitiator().toModel() : null);
        conversation.setCorrespondent(conversationDto.getCorrespondent() != null ? conversationDto.getCorrespondent().toModel() : null);
        conversation.setDemo(conversationDto.getDemo() != null ? conversationDto.getDemo().toModel() : null);

        // Relationships (lists)
        conversation.setUserReports(conversationDto.getUserReports() != null ? UserReportMapper.mapToModel(conversationDto.getUserReports()) : null);

        return conversation;
    }

    public static List<Conversation> mapToModel(List<ConversationDto> conversationDtoList) {
        List<Conversation> conversationList = new ArrayList<>();
        for (ConversationDto conversationDto : conversationDtoList) {
            Conversation conversation = mapToModel(conversationDto);
            conversationList.add(conversation);
        }
        return conversationList;
    }

    public static ConversationDto mapToDto(Conversation conversation) {
        if (processing) {
            return null;
        }
        processing = true;
        ConversationDto conversationDto = mapConversationToConversationDto(conversation);
        processing = false;
        return conversationDto;
    }

    private static ConversationDto mapConversationToConversationDto(Conversation conversation) {
        ConversationDto conversationDto = new ConversationDto();

        //Normal fields
        conversationDto.setConversationId(conversation.getConversationId());
        conversationDto.setCreatedDate(conversation.getCreatedDate());
        conversationDto.setLatestReplyDate(conversation.getLatestReplyDate());
        conversationDto.setHasDemo(conversation.getHasDemo());
        conversationDto.setSubject(conversation.getSubject());
        conversationDto.setBody(conversation.getBody());
        conversationDto.setReadByInitiator(conversation.isReadByInitiator());
        conversationDto.setReadByCorrespondent(conversation.isReadByCorrespondent());

        // Relationships (objects)
        conversationDto.setInitiator(conversation.getInitiator() != null ? conversation.getInitiator().toPublicDto() : null);
        conversationDto.setCorrespondent(conversation.getCorrespondent() != null ? conversation.getCorrespondent().toPublicDto() : null);
        conversationDto.setDemo(conversation.getDemo() != null ? conversation.getDemo().toDto() : null);

        // Relationships (lists)
        conversationDto.setUserReports(conversation.getUserReports() != null ? UserReportMapper.mapToDto(conversation.getUserReports()) : null);

        return conversationDto;
    }

    public static List<ConversationDto> mapToDto(List<Conversation> conversationList) {
        List<ConversationDto> conversationDtoList = new ArrayList<>();
        for (Conversation conversation : conversationList) {
            ConversationDto conversationDto = mapToDto(conversation);
            conversationDtoList.add(conversationDto);
        }
        return conversationDtoList;
    }

    public static List<ConversationDto> mapToDto(Iterable<Conversation> conversationIterable) {
        List<Conversation> conversationList = StreamSupport.stream(conversationIterable.spliterator(), false) .collect(Collectors.toList());
        return mapToDto(conversationList);
    }
}