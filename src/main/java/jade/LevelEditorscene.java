package jade;
import static org.lwjgl.glfw.GLFW.*;

public class LevelEditorscene extends Scene{

    private boolean changingScene = false;
    private float timeToChange = 2.0f;

    public LevelEditorscene(){
        System.out.println("Level Editor Scene");
    }

    @Override
    public void update(float dt){
        
        if(!changingScene  && keyListener.isKeyPressed(GLFW_KEY_ESCAPE)){
            changingScene = true;
        }

        if(changingScene && timeToChange > 0){
            timeToChange -= dt;
            float c = timeToChange / 5.0f;
            Window.get().setClearColor(c, c, c, 1.0f);
        }
        else if(changingScene){
            Window.changeScene(1);
        }
    }
}
