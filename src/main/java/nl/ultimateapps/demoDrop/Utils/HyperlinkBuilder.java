package nl.ultimateapps.demoDrop.Utils;

import io.github.cdimascio.dotenv.Dotenv;
import nl.ultimateapps.demoDrop.Models.Conversation;
import nl.ultimateapps.demoDrop.Models.Demo;

public class HyperlinkBuilder {

    public String buildConversationHyperlink(Conversation conversation) {
        Dotenv dotenv = Dotenv.load();
        String frontEndUrl = dotenv.get("CLIENT_APP_URL");
        String conversationIdString = conversation.getConversationId().toString();
        return frontEndUrl + "/conversations/" + conversationIdString;
    }

    public String buildDemoHyperlink(Demo demo) {
        Dotenv dotenv = Dotenv.load();
        String frontEndUrl = dotenv.get("CLIENT_APP_URL");
        String demoIdString = demo.getDemoId().toString();
        return frontEndUrl + "/demos/" + demoIdString;
    }
}
