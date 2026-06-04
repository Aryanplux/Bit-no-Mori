# Engine

Author: Aryan Dhiman

A lightweight Java game engine and in-game level editor built with LWJGL, ImGui, and a small custom ECS. This repository contains the engine sources, editor tools, and example scenes so you can run, edit, and extend 2D levels and gameplay.

## Features

- Core rendering and input using LWJGL (OpenGL + GLFW)
- Simple Entity-Component system for game objects and behaviors
- In-game editor with ImGui panels for placing tiles, prefabs and sounds
- Scene save/load via `level.txt` (JSON serialization)
- Build and packaging via Gradle (includes `fatJar` task)

## Quick start

1. Build the project:

```bash
./gradlew build
```

2. Run from IDE: run the `org.example.Main` class (default run configuration).

3. Or build the fat JAR and run it (ensure working directory contains `assets/` and `level.txt` if needed):

```bash
./gradlew fatJar
# Engine

Author: Aryan Dhiman

This repository contains a compact Java 2D game engine with an integrated in-game level editor. The codebase is intentionally small, readable, and modular to make it easy to learn engine internals and to extend features such as rendering, physics, and level authoring.

## Table of contents
- Overview
- Architecture & core components
- How the engine runs (render & update loop)
- Editor features and workflows
- Build & run (commands)
- Scene persistence (Save / Load)
- Extending the engine (developer guide)
- Troubleshooting & tips
- Contributing
- License

## Overview

The engine uses LWJGL3 for windowing, OpenGL rendering, and audio (OpenAL). ImGui (via `imgui-java`) provides the in-game editor user interface. Scenes and game objects are serialized using Gson, allowing you to save level data as JSON (`level.txt`). The system is centered around a minimal Entity-Component pattern where `GameObject` instances are composed from `Component`s.

Goals and design principles
- Small and approachable: make it easy to inspect and modify systems.
- Editor-first: fast in-engine iteration via ImGui tools.
- Portable build: Gradle tasks produce a runnable fat JAR for distribution.

## Architecture & core components

- `jade` — Core runtime needs: `Window`, input singletons (`KeyListener`, `MouseListener`), `ImGuiLayer`, and scene switching logic. `Window` orchestrates initialization, the render loop, and editor/play mode switching.
- `scenes` — `Scene` keeps `GameObject`s and manages lifecycle, `SceneInitializer` subclasses (e.g., `LevelEditorSceneInitializer`, `LevelSceneInitializer`) load resources and create scene content.
- `components` — Reusable building blocks (position, sprite renderer, physics colliders, controls, state machines, editor gizmos). Components encapsulate runtime and editor behavior and are serialized by Gson with custom deserializers.
- `renderer` — Low-level rendering helpers (`Mesh`, `Shader`, `Texture`, `Framebuffer`). The `Renderer` draws registered `GameObject`s each frame. Shaders live in `assets/shaders/` and are managed by `ShaderManager` and `AssetPool`.
- `physics2d` — A compact 2D physics layer with colliders and rigidbody-like components for gameplay and editor placement.
- `util` — `AssetPool` caches resources (textures, spritesheets, sounds, shaders), `Settings` holds engine constants and grid sizes.

Primary interactions
- `Window` calls `currentScene.update(dt)` in play mode or `currentScene.editorUpdate(dt)` in editor mode.
- `Scene` holds `GameObject`s and forwards lifecycle events. Editor UI (via `ImGuiLayer`) can create and mutate `GameObject`s and their `Component`s.

## How the engine runs (render & update loop)

Simplified flow per frame (in `Window.loop()`):
1. Poll GLFW events.
2. Render the scene to a picking texture (used for editor selection).
3. Render the main scene to the framebuffer and blit to the display.
4. If in editor mode: call `editorUpdate(dt)` and draw ImGui windows.
5. If in play mode: call `update(dt)`.

`KeyListener` and `MouseListener` provide frame-based input states used throughout input-handling components.

## Editor features and workflows

- ImGui windows (created in scene initializers) provide:
	- Tile palettes and image-buttons for placing sprites
	- Prefab buttons for player, enemies, pipes, and other common objects
	- Sound preview playback
	- Save / Load controls for scene persistence
- Editor game object (`LevelEditor`) registers `MouseControls` and `KeyControls` for placement, dragging, selection, copying, and deleting objects.

Example editor workflow
1. Run the engine in non-release mode (set `RELEASE_BUILD = false` in `jade.Window`).
2. While running, press `E` to toggle between Editor and Play.
3. Use the ImGui "Level Editor Stuff" window to pick tiles or prefabs.
4. Click in the world to place the picked object; move and tweak properties using the Properties window.
5. Click `Save` to write the scene to `level.txt` or `Load` to restore.

Default editor/key shortcuts
- `E` — toggle Editor/Play
- `Delete` — delete selected object(s)
- `Ctrl+D` — duplicate selection
- Arrow keys / `PageUp` / `PageDown` — nudge position or change z-index while editing

## Build & run

Build with Gradle (Windows examples):

```powershell
.\gradlew.bat build
.\gradlew.bat fatJar
```

Run from IDE: execute the `org.example.Main` launcher.

Run the fat JAR (ensure `assets/` and `level.txt` are available in the process working directory):

```powershell
java -jar build\libs\mario-1.0-SNAPSHOT.jar
```

Note: asset loading currently uses filesystem paths. If you move to packaging assets inside the JAR, update `AssetPool` and `Texture` to support classpath resource loading.

## Scene persistence (Save / Load)

Persistence uses Gson with custom adapters:
- `Scene.save()` gathers serializable `GameObject`s and writes JSON to `level.txt`.
- `Scene.load()` reads `level.txt`, deserializes objects using `GameObjectDeserializer` and `ComponentDeserializer`, and re-initializes internal ID counters.

Files of interest:
- `scenes/Scene.java` — scene lifecycle and save/load implementations
- `jade/Window.java` — scene switching and runtime/editor toggle handling
- `util/AssetPool.java` — resource re-binding used after deserialization

## Extending the engine (developer guide)

Create a new gameplay component
1. Add a class in `components/` extending the base `Component`.
2. Implement lifecycle hooks: `start()`, `update()`, `editorUpdate()`.
3. If the component needs textures, register them in `SceneInitializer.loadResources()` via `AssetPool`.

Create a prefab
1. Add a factory method (or extend `Prefabs` if present) that builds a `GameObject` with a predefined set of components and initial properties.
2. Add a button for the prefab in `LevelEditorSceneInitializer.imgui()` to allow placing it from the palette.

Add a new scene
1. Implement `SceneInitializer` with `loadResources(Scene)` and `init(Scene)`.
2. Call `Window.changeScene(new YourSceneInitializer())` to switch to it.

## Troubleshooting & tips

- Missing shader/texture: verify `assets/` is present and paths match the ones used in code.
- ImGui windows missing: ensure `RELEASE_BUILD` is `false` in `jade.Window`.
- If `level.txt` is not found: create an empty file or use the editor to save a level.
- If shaders fail to compile: check shader file paths and that `assets/shaders/default.glsl` exists.

## Contributing

Author: Aryan Dhiman

Contributions are welcome — open issues and pull requests. For small changes follow this checklist:
- Run `./gradlew build` and ensure compilation succeeds.
- Launch `org.example.Main` to smoke-test changes.
- Include small, focused commits with clear messages.

If you'd like, I can add a `CONTRIBUTING.md`, a license file (MIT/Apache-2.0), and a `levels/` folder plus a "Save As" UI option. Tell me which you'd like next.

## License

This project is authored by Aryan Dhiman. You are free to use, modify, and redistribute the code.
