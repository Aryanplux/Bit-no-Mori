package jade;

public class TransformComponent implements Component{
    public float x, y, z;
    public float sx = 1.0f, sy = 1.0f;
    public float rotation = 0.0f;

    public TransformComponent(){
        this(0,0,0);
    }

    public TransformComponent(float x, float y, float z){
        this.x = x; this.y = y; this.z = z;
    }
}
