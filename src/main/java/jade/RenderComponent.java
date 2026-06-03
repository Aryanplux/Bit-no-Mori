package jade;

public class RenderComponent implements Component{
    public Mesh mesh;
    public Shader shader;

    public RenderComponent(Mesh mesh, Shader shader){
        this.mesh = mesh;
        this.shader = shader;
    }
}
