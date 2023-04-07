package nl.ultimateapps.demoDrop.Services;

import nl.ultimateapps.demoDrop.Dtos.output.UserReportDto;
import org.springframework.security.access.AccessDeniedException;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.ArrayList;

public interface UserReportService {
    ArrayList<UserReportDto> getUserReports(int limit);

    UserReportDto getUserReport(long userReportId) throws AccessDeniedException;

    UserReportDto createUserReport(UserReportDto userReportDto) throws UserPrincipalNotFoundException, AccessDeniedException;

    long deleteUserReports();

    long deleteUserReport(long userReportId);
}
