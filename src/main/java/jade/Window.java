package jade;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import org.lwjgl.opengl.GL;

import util.Time;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.opengl.GL11.*;


public class Window {
    private int width, height;
    private String title;
    private long glfwWindow;

    public float r, g, b, a;
    // Fade controls
    private boolean fadingToBlack = false;
    private boolean fadingToWhite = false;
    private float fadeTimer = 0.0f; // hold time at black
    private static final float FADE_SPEED = 0.5f; // units per second
    private static final float FADE_HOLD = 1.0f; // seconds to stay black

    private static Window window = null;

    private static Scene currentScene;


    private Window(){
        this.width = 1920;
        this.height = 1080;
        this.title = "Mario";
        r = 1;
        g = 1;
        b = 1;
        a = 1;
    }

    public void setR(float r){
        this.r = r;
    }

    public void setG(float g){
        this.g = g;
    }

    public void setB(float b){
        this.b = b;
    }

    public void setA(float a){
        this.a = a;
    }

    public void setClearColor(float r, float g, float b, float a){
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }
    
    public static void changeScene(int newScene){
        switch(newScene){
            case 0:
                currentScene = new LevelEditorscene();
                currentScene.init();
                break;
            case 1:
                currentScene = new LevelScene();
                currentScene.init();
                break;
             default:
                assert false : "Unknown scene '" + newScene + "'";    
                break;
        }
    }

    public static Window get(){
        if(Window.window == null){
            Window.window = new Window();
        }
        return Window.window;
    }
    public void run(){
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        // Free The Memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);  

        //Terminate GLFW and free the error callback 
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
    public void init(){
        // Setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();

        //Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        glfwDefaultWindowHints(); 
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); 
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // Create the window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if(glfwWindow == NULL){
            throw new IllegalStateException("Failed to create the GLFW window");
        }

        glfwSetCursorPosCallback(glfwWindow, mouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, mouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, mouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, keyListener::keyCallback);

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(glfwWindow);

        // This line is critical for LWJGL's interoperation with GLFW's OpenGL context, or any context that is managed externally. LWJGL detects the context that is current in the current thread, creates the GLCapabilities instance and makes the OpenGL bindings available for use.
        GL.createCapabilities();

        Window.changeScene(0);

    }


    public void loop(){
        float beginTime = Time.getTime();
        float endTime;
        float dt = 0.0f;

        while(!glfwWindowShouldClose(glfwWindow)){
            // time
            endTime = Time.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;

            // poll events
            glfwPollEvents();

            // Start fade only if space pressed and not already fading, and currently white
            if(keyListener.isKeyPressed(GLFW_KEY_SPACE) && !fadingToBlack && !fadingToWhite && r == 1.0f && g == 1.0f && b == 1.0f){
                fadingToBlack = true;
            }

            // Handle fading
            if(fadingToBlack){
                r = Math.max(r - FADE_SPEED * dt, 0.0f);
                g = Math.max(g - FADE_SPEED * dt, 0.0f);
                b = Math.max(b - FADE_SPEED * dt, 0.0f);
                if(r <= 0.0f && g <= 0.0f && b <= 0.0f){
                    fadingToBlack = false;
                    fadeTimer = FADE_HOLD;
                }
            } else if(fadeTimer > 0.0f){
                fadeTimer -= dt;
                if(fadeTimer <= 0.0f){
                    fadingToWhite = true;
                }
            } else if(fadingToWhite){
                r = Math.min(r + FADE_SPEED * dt, 1.0f);
                g = Math.min(g + FADE_SPEED * dt, 1.0f);
                b = Math.min(b + FADE_SPEED * dt, 1.0f);
                if(r >= 1.0f && g >= 1.0f && b >= 1.0f){
                    fadingToWhite = false;
                }
            }

            // update scene
            if(currentScene != null){
                currentScene.update(dt);
            }

            // render
            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);

            glfwSwapBuffers(glfwWindow);
        }
    }
}
