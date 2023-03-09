package nl.ultimateapps.demoDrop.Services;

import nl.ultimateapps.demoDrop.Dtos.output.DemoDto;
import org.springframework.core.io.Resource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.ArrayList;
import java.util.List;

public interface DemoService {
    List<DemoDto> getDemos(int limit);

    List<DemoDto> getPersonalDemos(String username);

    List<DemoDto> getFavoriteDemos(String username);

    DemoDto getDemo(long demoId);

    boolean checkIsFav(long demoId) throws UserPrincipalNotFoundException;

    DemoDto createDemo(DemoDto demoDto) throws UserPrincipalNotFoundException;

    DemoDto updateDemo(long demoId, DemoDto demoDto);

    boolean setFavStatus(long demoId, boolean desiredStatus) throws UserPrincipalNotFoundException;

    DemoDto assignGenreToDemo(long demoId, String name) throws AccessDeniedException;

    long deleteDemos();

    long deleteDemo(long demoId);

    Iterable<DemoDto> getDemoContaining(String query);

    boolean uploadFileAndAssignToDemo(Long demoId, MultipartFile multipartFile) throws AccessDeniedException;

    boolean assignFileToDemo(Long fileId, Long demoId);

    Resource downloadMp3File(long demoId);

    List<DemoDto> getDemosByGenre(String genre, int limit);
}
