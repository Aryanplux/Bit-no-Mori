package jade;

public class LevelEditorscene extends Scene{
    private ShaderManager shaderManager;
    private EntityManager entityManager;
    private RenderSystem renderSystem;

    public LevelEditorscene(){

    }

    @Override
    public void init(){
        shaderManager = new ShaderManager();
        Shader shader = shaderManager.load("default.glsl");

        entityManager = new EntityManager();
        renderSystem = new RenderSystem(entityManager);

        // create a simple triangle mesh (pos.xyz + color.rgb)
        float[] verts = new float[]{
            // triangle
            -0.5f, -0.5f, 0.0f,  1f, 0f, 0f,
             0.5f, -0.5f, 0.0f,  0f, 1f, 0f,
             0.0f,  0.5f, 0.0f,  0f, 0f, 1f
        };

        Mesh mesh = new Mesh(verts);

        Entity e = entityManager.createEntity();
        e.addComponent(new TransformComponent(0,0,0));
        e.addComponent(new RenderComponent(mesh, shader));
    }

    @Override
    public void update(float dt){
        shaderManager.update();
        renderSystem.render();
    }

    @Override
    public void dispose(){
        // dispose meshes and shaders
        for(Entity e : entityManager.getEntities()){
            if(e.hasComponent(RenderComponent.class)){
                RenderComponent rc = e.getComponent(RenderComponent.class);
                try{ rc.mesh.dispose(); } catch(Exception ignored){}
            }
        }
        if(shaderManager != null) shaderManager.dispose();
    }

}
