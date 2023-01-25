package nl.ultimateapps.demoDrop.Services;

import nl.ultimateapps.demoDrop.Dtos.output.DemoDto;
import nl.ultimateapps.demoDrop.Exceptions.RecordNotFoundException;
import nl.ultimateapps.demoDrop.Helpers.mappers.DemoMapper;
import nl.ultimateapps.demoDrop.Models.Demo;
import nl.ultimateapps.demoDrop.Models.File;
import nl.ultimateapps.demoDrop.Models.User;
import nl.ultimateapps.demoDrop.Repositories.ConversationRepository;
import nl.ultimateapps.demoDrop.Repositories.DemoRepository;
import nl.ultimateapps.demoDrop.Repositories.FileRepository;
import nl.ultimateapps.demoDrop.Repositories.UserRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.*;

@Service
@AllArgsConstructor
public class DemoService {

    private final DemoRepository demoRepository;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final FileRepository fileRepository;

    // nah ga ik waarschinlijk niet gebruiken:
//    public long assignconversationToDemo(long demoId, long conId) {
//        if (repos.findById(demoId).isPresent()) {
//            Demo Demo = repos.findById(demoId).get();
//            if (Repos.findById(rcId).isPresent()) {
//                RemoteController remoteControllerFromRepo = rcRepos.findById(rcId).get();
//                Demo.setRemoteController(remoteControllerFromRepo);
//                repos.save(Demo);
//                return Demo.getId();
//            } else {
//                throw new RecordNotFoundException();
//            }
//        } else {
//            throw new RecordNotFoundException();
//        }
//    }

//    WERK NIET HELAAS, maar is meer toepasselijk dan het bovenstaande
//    public long assignConversationsToDemo(long tvId, long[] ConversationIds) {
//        if (repos.findById(tvId).isPresent()) {
//            Demo Demo = repos.findById(tvId).get();
//            for (long  ConversationId : ConversationIds) {
//                if (ciRepos.findById(ConversationId).isPresent()) {
//                    Conversation ConversationFromCiRepo = ciRepos.findById(ConversationId).get();
//                    List<Conversation> ConversationsFromRepo = Demo.getConversations();
//                    ConversationsFromRepo.add(ConversationFromCiRepo);
//                    Demo.setConversations(ConversationsFromRepo);
//                    repos.save(Demo);
//                } else {
//                    throw new RecordNotFoundException();
//                }
//            }
//                    return Demo.getId();
//        } else {
//            throw new RecordNotFoundException();
//        }
//    }

    public ArrayList<DemoDto> getDemos() {
        Iterable<Demo> demoList = demoRepository.findAll();
        ArrayList<DemoDto> resultList = new ArrayList<>();
        for (Demo demo: demoList) {
            DemoDto demoDto = DemoMapper.mapToDto(demo);
            resultList.add(demoDto);
        }
        return resultList;
    }

    public ArrayList<DemoDto> getTopTwelveDemos() {
        List<Demo> allDemos = demoRepository.findAll();
        ArrayList<DemoDto> resultList = new ArrayList<>();
        int numResults = allDemos.size();
        for (int i = 0; i < (Math.min(numResults, 12)); i++) {
            DemoDto newDemoDto = DemoMapper.mapToDto(allDemos.get(i));
            resultList.add(newDemoDto);
        }
        return resultList;
    }

    public ArrayList<DemoDto> getPersonalDemos(String username) {
        // first, get the user object.
        User user = userRepository.findById(username).get();
        Iterable<Demo> demoList = demoRepository.findByUserOrderByCreatedDateDesc(user);
        ArrayList<DemoDto> resultList = new ArrayList<>();
        for (Demo demo: demoList) {
        DemoDto demoDto = DemoMapper.mapToDto(demo);
        resultList.add(demoDto);
        }
        return resultList;
    }

    public DemoDto getDemo(long id) {
        if (demoRepository.findById(id).isPresent()) {
            Demo demo = demoRepository.findById(id).get();
            DemoDto demoDto = DemoMapper.mapToDto(demo);
            return demoDto;
        } else {
            throw new RecordNotFoundException();
        }
    }

    public long createDemo(DemoDto demoDto) {
        Demo demo =  DemoMapper.mapToModel(demoDto);
        Demo savedDemo = demoRepository.save(demo);
        return savedDemo.getDemoId();
    }
// Onderstaande code uit het data uitwisseling project bijt de bovenstaande methode // TODO Uitwissen
//    public Demo saveDemo(DemoDto demoDto) {
//        Demo demo =  DemoMapper.mapToModel(demoDto);
//        Demo savedDemo = demoRepository.save(demo);
//        return demoRepository.save(demo);
//    }

    public long updateDemo(long id, DemoDto demoDto) {
        if (demoRepository.findById(id).isPresent()) {
            Demo demo = demoRepository.findById(id).get();
            demo.setTitle(demoDto.getTitle());
            demo.setCreatedDate(demoDto.getCreatedDate());
            demo.setLength(demoDto.getLength());
            demo.setBPM(demoDto.getBPM());
            demoRepository.save(demo);
            return demo.getDemoId();
        } else {
            throw new RecordNotFoundException();
        }
    }

    public long partialUpdateDemo(long id, DemoDto DemoDto) {
        if (demoRepository.findById(id).isPresent()) {
            Demo Demo = demoRepository.findById(id).get();
            if (Demo.getTitle() != null) {
                Demo.setTitle(DemoDto.getTitle());
            }
            demoRepository.save(Demo);
            return Demo.getDemoId();
        } else {
            throw new RecordNotFoundException();
        }
    }

    public long deleteDemos() {
        if (demoRepository.findAll() != null) {
            List<Demo> demos= demoRepository.findAll();
            long numDeletedDemos = 0;
            for ( Demo demo : demos  ) {
                demoRepository.delete(demo);
                numDeletedDemos++;
            }
            return numDeletedDemos;
        } else {
            throw new RecordNotFoundException();
        }
    }

    public long deleteDemo(long id) {
        if (demoRepository.findById(id).isPresent()) {
            Demo demo = demoRepository.findById(id).get();
            long retrievedId = demo.getDemoId();
            demoRepository.deleteById(id);
            return retrievedId;
        } else {
            throw new RecordNotFoundException();
        }
    }

    public Iterable<DemoDto> getDemoContaining(String query) {
        Iterable<Demo> foundDemos = demoRepository.findByTitleContaining(query);
        ArrayList<DemoDto> resultList = new ArrayList<>();
        for (Demo d : foundDemos) {
            DemoDto newDemoDto = DemoMapper.mapToDto(d);
            resultList.add(newDemoDto);
        }
        return resultList;
    }

    public void assignFileToDemo(String name, Long demoNumber) {
        Optional<Demo> optionalDemo = demoRepository.findById(demoNumber);
        Optional<File> optionalFile = fileRepository.findByFileName(name);
        if (optionalDemo.isPresent() && optionalFile.isPresent()) {
            Demo demo = optionalDemo.get();
            File file = optionalFile.get();
            demo.setFile(file);
            demoRepository.save(demo);
        }
    }

}