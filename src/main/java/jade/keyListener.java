package jade;

public class keyListener {
    private static keyListener instance;
    private boolean keyPressed[] = new boolean[350];

    private keyListener(){

    }

    public static keyListener get(){
        if(keyListener.instance == null){
            instance = new keyListener();
        }
        return keyListener.instance;
    }   
    public static void keyCallback(long window, int key, int scancode, int action, int mods){
        if(action == 0){ 
            get().keyPressed[key] = false;
        }else if(action == 1){ 
            get().keyPressed[key] = true;
        }   
    }   
}
