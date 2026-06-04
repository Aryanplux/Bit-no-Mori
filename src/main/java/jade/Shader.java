package jade;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Shader extends renderer.Shader {
    public Shader(String name) {
        super((name.contains("/") || name.contains("\\\\")) ? name : ("assets/shaders/" + name));
    }

    public void dispose() {
        try { this.detach(); } catch (Exception ignored) {}
    }

    public void bind() { this.use(); }

    public void unbind() { this.detach(); }
}
