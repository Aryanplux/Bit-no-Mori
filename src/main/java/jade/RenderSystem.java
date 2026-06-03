package jade;

import static org.lwjgl.opengl.GL11.*;

public class RenderSystem {
    private final EntityManager entityManager;

    public RenderSystem(EntityManager em){
        this.entityManager = em;
    }

    public void render(){
        for(Entity e : entityManager.getEntities()){
            if(e.hasComponent(RenderComponent.class) && e.hasComponent(TransformComponent.class)){
                RenderComponent rc = e.getComponent(RenderComponent.class);
                TransformComponent tc = e.getComponent(TransformComponent.class);

                if(rc.shader != null) rc.shader.bind();

                // In a more complete engine we'd set transform uniforms here
                rc.mesh.render();

                if(rc.shader != null) rc.shader.unbind();
            }
        }
    }
}
