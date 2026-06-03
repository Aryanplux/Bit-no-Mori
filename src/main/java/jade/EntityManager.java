package jade;

import java.util.ArrayList;
import java.util.List;

public class EntityManager {
    private final List<Entity> entities = new ArrayList<>();

    public Entity createEntity(){
        Entity e = new Entity();
        entities.add(e);
        return e;
    }

    public List<Entity> getEntities(){
        return entities;
    }
}
