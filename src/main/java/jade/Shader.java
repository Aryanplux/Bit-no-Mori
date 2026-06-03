package jade;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public class Shader {

    private int programID;

    public Shader(String filePath) {
        String resolved = filePath.startsWith("assets/") ? filePath : "assets/shaders/" + filePath;
        try {
            String src = Files.readString(Path.of(resolved));
            Map<Integer, String> shaders = parseShader(src);
            int vertex = compileShader(GL_VERTEX_SHADER, shaders.get(GL_VERTEX_SHADER));
            int fragment = compileShader(GL_FRAGMENT_SHADER, shaders.get(GL_FRAGMENT_SHADER));

            programID = glCreateProgram();
            glAttachShader(programID, vertex);
            glAttachShader(programID, fragment);
            glLinkProgram(programID);

            int status = glGetProgrami(programID, GL_LINK_STATUS);
            if (status == GL_FALSE) {
                String log = glGetProgramInfoLog(programID);
                throw new RuntimeException("Shader program link failed: " + log);
            }

            glDetachShader(programID, vertex);
            glDetachShader(programID, fragment);
            glDeleteShader(vertex);
            glDeleteShader(fragment);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load shader file: " + resolved, e);
        }
    }

    private Map<Integer, String> parseShader(String src) {
        Map<Integer, String> map = new HashMap<>();
        String[] lines = src.split("\n");
        StringBuilder sb = new StringBuilder();
        int type = -1;
        for (String raw : lines) {
            String line = raw.trim();
            if (line.startsWith("# type")) {
                if (type != -1) {
                    map.put(type, sb.toString());
                    sb.setLength(0);
                }
                if (line.contains("vertex")) type = GL_VERTEX_SHADER;
                else if (line.contains("fragment")) type = GL_FRAGMENT_SHADER;
                else type = -1;
                continue;
            }
            if (type != -1) {
                sb.append(raw).append('\n');
            }
        }
        if (type != -1) map.put(type, sb.toString());
        return map;
    }

    private int compileShader(int type, String source) {
        if (source == null) return -1;
        int id = glCreateShader(type);
        glShaderSource(id, source);
        glCompileShader(id);
        int status = glGetShaderi(id, GL_COMPILE_STATUS);
        if (status == GL_FALSE) {
            String log = glGetShaderInfoLog(id);
            throw new RuntimeException("Failed to compile shader: " + log + "\nSource:\n" + source);
        }
        return id;
    }

    public void bind() {
        glUseProgram(programID);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void dispose() {
        glDeleteProgram(programID);
    }
}
