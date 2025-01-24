class Message {
    NotificationType type ; //email, sms
    int msgId;
    String content ;
}
class MessagingServiceImpl {
    static {
        //map from notification type to the respective handler
        map.put("SMS",new SMSHandler());
        map.put("Email",new EmailHandler());
    }
    void processMessage(Message message) {
        Handler handler = map.get(message.getNotificationType());
        handler.handleMessage();
    }
}
public abstract class Handler {
    public abstract void handle(Mesage message) ;
}
public EmailHandler extends Handler {
    public void handle(Message message) {
        System.out.println("Sending email"): // similar class for phone.
    }
}


// abstract class Message {
//     private msgId;
    
//     public void handleMessage(String content);
// }

// class EmailMessage extends Message{
//     ContentType content;

//     public EmailMessage(int msgId){
//         super(msgId);
//     }
//     public void setContent(ContentType content){
//         this.content = content;
//     }
//     public void handleMessage(String content){

//     }
// }

// interface ContentType{
//     public void processContentType();
// }

// class SmallContents implements ContentType{

// }

// public PushNotificationHandler extends Handler {
//     public void handle(Message message) {
//         message.handleMessage(message); // similar class for phone.
//     }
// }


public interface MessageHandler {
    void handle(Message message);
}

public class MessagingServiceImpl {
    private Map<String, MessageHandler> handlerMap = new HashMap<>();
    
    // Dynamic handler registration using reflection or configuration
    public void registerHandler(String type, MessageHandler handler) {
        handlerMap.put(type, handler);
    }

    public void processMessage(Message message) {
        MessageHandler handler = handlerMap.get(message.getType());
        if (handler != null) {
            handler.handle(message);
        }
    }
}


public abstract class Message {
    private String msgId;
    private String type;

    public abstract String formatContent();  // Each subclass will define its own content formatting

    public String getType() {
        return type;
    }

    public String getMsgId() {
        return msgId;
    }
}

public class SMSMessage extends Message {
    private String content;

    @Override
    public String formatContent() {
        return content.length() > 160 ? content.substring(0, 160) : content;  // Truncate content for SMS
    }
}

public class EmailMessage extends Message {
    private String subject;
    private String body;

    @Override
    public String formatContent() {
        return "Subject: " + subject + "\nBody: " + body;  // Format for email
    }
}


public interface MessagingService {
    void processMessage(Message message);
}

public class MessagingServiceImpl implements MessagingService {
    private Map<String, MessageHandler> handlerMap;

    public void processMessage(Message message) {
        MessageHandler handler = handlerMap.get(message.getType());
        handler.handle(message);
    }
}


public class Message {
    private String msgId;
    private List<String> channels;  // List of channels (SMS, Email, etc.)

    public void sendMessage() {
        for (String channel : channels) {
            if (channel.equals("SMS")) {
                // Call SMSHandler
            } else if (channel.equals("Email")) {
                // Call EmailHandler
            }
            // Add additional channels here
        }
    }
}
