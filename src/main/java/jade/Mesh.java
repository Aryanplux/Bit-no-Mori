package jade;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;

import org.lwjgl.system.MemoryUtil;

public class Mesh {
    private int vaoId;
    private int vboId;
    private int vertexCount;

    public Mesh(float[] vertices){
        vertexCount = vertices.length / 6; // assuming pos(3) + color(3)

        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);

        FloatBuffer buffer = MemoryUtil.memAllocFloat(vertices.length);
        buffer.put(vertices).flip();
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

        // position
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        // color
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        MemoryUtil.memFree(buffer);
    }

    public void render(){
        glBindVertexArray(vaoId);
        glDrawArrays(GL_TRIANGLES, 0, vertexCount);
        glBindVertexArray(0);
    }

    public void dispose(){
        glDeleteBuffers(vboId);
        glDeleteVertexArrays(vaoId);
    }
}
