package jade;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ShaderManager {
    private final Map<String, Shader> shaders = new HashMap<>();
    private final Map<String, Long> lastModified = new HashMap<>();

    public Shader get(String name){
        return shaders.get(name);
    }

    public Shader load(String name){
        if(shaders.containsKey(name)) return shaders.get(name);
        Shader s = new Shader(name);
        shaders.put(name, s);
        File f = new File("assets/shaders/" + name);
        if(f.exists()) lastModified.put(name, f.lastModified());
        return s;
    }

    public void update(){
        for(String name : shaders.keySet()){
            File f = new File("assets/shaders/" + name);
            if(f.exists()){
                long lm = f.lastModified();
                if(!lastModified.containsKey(name) || lm > lastModified.get(name)){
                    // reload
                    System.out.println("Reloading shader: " + name);
                    Shader old = shaders.get(name);
                    try { old.dispose(); } catch(Exception ignored){}
                    Shader s = new Shader(name);
                    shaders.put(name, s);
                    lastModified.put(name, lm);
                }
            }
        }
    }

    public void dispose(){
        for(Shader s : shaders.values()) s.dispose();
        shaders.clear();
        lastModified.clear();
    }
}
