package jade;
import java.awt.event.KeyEvent;

public class LevelEditorscene extends Scene{

    private boolean changingScene = false;
    private float timeToChange = 2.0f;

    public LevelEditorscene(){

    }

    @Override
    public void update(float dt){
        
        if(!changingScene  && keyListener.isKeyPressed(KeyEvent.VK_ESCAPE)){
            changingScene = true;
        }

        if(changingScene && timeToChange > 0){
            timeToChange -= dt;
            Window.get().setR(timeToChange / 5.0f);
            Window.get().setG(timeToChange / 5.0f);
            Window.get().setB(timeToChange / 5.0f);
        }
        else if(changingScene){
            Window.changeScene(1);
        }
    }
}
