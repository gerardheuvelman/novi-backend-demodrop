package nl.ultimateapps.demoDrop.Services;

import nl.ultimateapps.demoDrop.Dtos.output.DemoDto;
import nl.ultimateapps.demoDrop.Exceptions.RecordNotFoundException;
import nl.ultimateapps.demoDrop.Helpers.DemoMapper;
import nl.ultimateapps.demoDrop.Models.Demo;
import nl.ultimateapps.demoDrop.Repositories.ConversationRepository;
import nl.ultimateapps.demoDrop.Repositories.DemoRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import lombok.*;

@Service
@AllArgsConstructor
public class DemoService {

    @Getter
    @Setter
    private DemoRepository repos;

    @Getter
    @Setter
    private ConversationRepository conRepos;

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
        Iterable<Demo> allDemos = repos.findAll();
        ArrayList<DemoDto> resultList = new ArrayList<>();
        for (Demo demo : allDemos) {
            DemoDto newDemoDto = DemoMapper.mapToDto(demo);
            resultList.add(newDemoDto);
        }
        return resultList;
    }

    public DemoDto getDemo(long id) {
        if (repos.findById(id).isPresent()) {
            Demo demo = repos.findById(id).get();
            DemoDto demoDto = DemoMapper.mapToDto(demo);
            return demoDto;
        } else {
            throw new RecordNotFoundException();
        }
    }

    public long createDemo(DemoDto demoDto) {
        Demo demo =  DemoMapper.mapToModel(demoDto);
        Demo savedDemo = repos.save(demo);
        return savedDemo.getId();
    }

    public long updateDemo(long id, DemoDto demoDto) {
        if (repos.findById(id).isPresent()) {
            Demo demo = repos.findById(id).get();
            demo.setTitle(demoDto.getTitle());
            demo.setCreatedDate(demoDto.getCreatedDate());
            demo.setLength(demoDto.getLength());
            demo.setAudiofileUrl(demoDto.getAudiofileUrl());
            repos.save(demo);
            return demo.getId();
        } else {
            throw new RecordNotFoundException();
        }
    }

    public long partialUpdateDemo(long id, DemoDto DemoDto) {
        if (repos.findById(id).isPresent()) {
            Demo Demo = repos.findById(id).get();
            if (Demo.getTitle() != null) {
                Demo.setTitle(DemoDto.getTitle());
            }
            repos.save(Demo);
            return Demo.getId();
        } else {
            throw new RecordNotFoundException();
        }
    }

    public long deleteDemo(long id) {
        if (repos.findById(id).isPresent()) {
            Demo demo = repos.findById(id).get();
            long retrievedId = demo.getId();
            repos.deleteById(id);
            return retrievedId;
        } else {
            throw new RecordNotFoundException();
        }
    }

    public Iterable<DemoDto> getDemoContaining(String query) {
        Iterable<Demo> foundDemos = repos.findByTitleContaining(query);
        ArrayList<DemoDto> resultList = new ArrayList<>();
        for (Demo d : foundDemos) {
            DemoDto newDemoDto = DemoMapper.mapToDto(d);
            resultList.add(newDemoDto);
        }
        return resultList;
    }
}