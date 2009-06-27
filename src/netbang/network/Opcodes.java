package netbang.network;

public class Opcodes {
    enum Message{
        SETNAME(1),
        SETROLE(1),
        COMMAND(1),
        READY(0);
        
        private Message(int i){
            options = i;
        }
        int options;
        public int numOptions(){
            return options;
        }
    }
    
    enum Commands{
        QUIT,
        CLOSESERVER,
        START,
        PROMPTING;
    }
    
    public static String createMessage(){
        return "";
    }
    public static Message fromString(String str){
        return Message.valueOf(str);
    }
    public static String[] getCommandParams(){
        return new String[]{""};
    }
}
