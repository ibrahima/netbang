package netbang.network;

public class Opcodes {
    enum Message{
        SETNAME(0),
        COMMAND(0),
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
    
    public static Message fromString(String str){
        return Message.valueOf(str);
    }
}
