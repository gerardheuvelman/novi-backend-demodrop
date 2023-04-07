package nl.ultimateapps.demoDrop.Helpers.mappers;

import nl.ultimateapps.demoDrop.Models.Conversation;
import nl.ultimateapps.demoDrop.Models.Demo;
import nl.ultimateapps.demoDrop.Models.User;
import nl.ultimateapps.demoDrop.Models.UserReport;
import nl.ultimateapps.demoDrop.Dtos.output.UserReportDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserReportMapper {

    static boolean processing = false;

    public static UserReport mapToModel(UserReportDto userReportDto) {
        if (processing) {
            return null;
        }
        processing = true;
        UserReport userReport = mapUserReportDtoToUserReport(userReportDto);
        processing = false;
        return userReport;
    }

    private static UserReport mapUserReportDtoToUserReport(UserReportDto userReportDto) {
        UserReport userReport = new UserReport();

        // Normal fields
        userReport.setUserReportId(userReportDto.getUserReportId());
        userReport.setCreatedDate(userReportDto.getCreatedDate());
        userReport.setType(userReportDto.getType());
        userReport.setSubject(userReportDto.getSubject());
        userReport.setBody(userReportDto.getBody());

        // Relationships (objects)
        userReport.setReporter(userReportDto.getReporter() != null ? userReportDto.getReporter().toModel() : null);

        userReport.setReportedDemo(userReportDto.getReportedDemo() != null ? userReportDto.getReportedDemo().toModel() : null);

        userReport.setReportedConversation(userReportDto.getReportedConversation() != null ? userReportDto.getReportedConversation().toModel() : null);

        userReport.setReportedUser(userReportDto.getReportedUser() != null ? userReportDto.getReportedUser().toModel() : null);

        return userReport;
    }

    public static List<UserReport> mapToModel(List<UserReportDto> userReportDtoList) {
        List<UserReport> userReportList = new ArrayList<>();
        for (UserReportDto userReportDto : userReportDtoList) {
            UserReport userReport = mapToModel(userReportDto);
            userReportList.add(userReport);
        }
        return userReportList;
    }

    public static UserReportDto mapToDto(UserReport userReport) {
        if (processing) {
            return null;
        }
        processing = true;
        UserReportDto userReportDto = mapUserReportToUserReportDto(userReport);
        processing = false;
        return userReportDto;
    }


    private static UserReportDto mapUserReportToUserReportDto(UserReport userReport) {
        UserReportDto userReportDto = new UserReportDto();

        //Normal fields
        userReportDto.setUserReportId(userReport.getUserReportId());
        userReportDto.setCreatedDate(userReport.getCreatedDate());
        userReportDto.setType(userReport.getType());
        userReportDto.setSubject(userReport.getSubject());
        userReportDto.setBody(userReport.getBody());

        // Relationships (objects)
        userReportDto.setReporter(userReport.getReporter() != null ? userReport.getReporter().toPublicDto() : null);

        userReportDto.setReportedDemo(userReport.getReportedDemo() != null ? userReport.getReportedDemo().toDto() : null);

        userReportDto.setReportedConversation(userReport.getReportedConversation() != null ? userReport.getReportedConversation().toDto() : null);

        userReportDto.setReportedUser(userReport.getReportedUser() != null ? userReport.getReportedUser().toPublicDto() : null);

        return userReportDto;
    }

    public static List<UserReportDto> mapToDto(List<UserReport> userReportList) {
        List<UserReportDto> userReportDtoList = new ArrayList<>();
        for (UserReport userReport : userReportList) {
            UserReportDto userReportDto = mapToDto(userReport);
            userReportDtoList.add(userReportDto);
        }
        return userReportDtoList;
    }

    public static List<UserReportDto> mapToDto(Iterable<UserReport> userReportIterable) {
        List<UserReport> userReportList = StreamSupport.stream(userReportIterable.spliterator(), false)
                .collect(Collectors.toList());
        return mapToDto(userReportList);
    }
}