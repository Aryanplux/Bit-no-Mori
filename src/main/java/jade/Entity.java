package jade;

import java.util.HashMap;
import java.util.Map;

public class Entity {
    private static int NEXT_ID = 1;
    private final int id;
    private final Map<Class<? extends Component>, Component> components = new HashMap<>();

    public Entity(){
        this.id = NEXT_ID++;
    }

    public int getId(){
        return id;
    }

    public <T extends Component> void addComponent(T comp){
        components.put(comp.getClass(), comp);
    }

    public <T extends Component> T getComponent(Class<T> clazz){
        return clazz.cast(components.get(clazz));
    }

    public boolean hasComponent(Class<? extends Component> clazz){
        return components.containsKey(clazz);
    }
}
