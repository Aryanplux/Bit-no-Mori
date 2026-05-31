package jade;

import static org.lwjgl.glfw.GLFW.*;

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
        if(action == GLFW_PRESS){ 
            get().keyPressed[key] = true;
        }else if(action == GLFW_RELEASE){ 
            get().keyPressed[key] = false;
        }   
    }  
    public static boolean isKeyPressed(int keyCode){
        return get().keyPressed[keyCode];
    } 
}
