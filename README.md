<div align="center">

<br />

```
██████╗ ██╗████████╗       ███╗   ██╗ ██████╗
██╔══██╗██║╚══██╔══╝       ████╗  ██║██╔═══██╗
██████╔╝██║   ██║    █████╗██╔██╗ ██║██║   ██║
██╔══██╗██║   ██║    ╚════╝██║╚██╗██║██║   ██║
██████╔╝██║   ██║          ██║ ╚████║╚██████╔╝
╚═════╝ ╚═╝   ╚═╝          ╚═╝  ╚═══╝ ╚═════╝

███╗   ███╗ ██████╗ ██████╗ ██╗
████╗ ████║██╔═══██╗██╔══██╗██║
██╔████╔██║██║   ██║██████╔╝██║
██║╚██╔╝██║██║   ██║██╔══██╗██║
██║ ╚═╝ ██║╚██████╔╝██║  ██║██║
╚═╝     ╚═╝ ╚═════╝ ╚═╝  ╚═╝╚═╝
```

### **A Lightweight Java 2D Game Engine with Integrated In-Game Level Editor**
*Engineered for clarity. Built for extensibility. Powered by OpenGL.*

*Authored by **Aryan Dhiman***

<br />

[![License](https://img.shields.io/badge/License-Open_Use-22c55e?style=for-the-badge)](LICENSE)
[![Java](https://img.shields.io/badge/Java-17+-f59e0b?style=for-the-badge&logo=openjdk)](https://openjdk.org/)
[![LWJGL](https://img.shields.io/badge/LWJGL-3.x-3b82f6?style=for-the-badge)](https://lwjgl.org/)
[![OpenGL](https://img.shields.io/badge/OpenGL-Rendering-8b5cf6?style=for-the-badge)](https://opengl.org/)
[![ImGui](https://img.shields.io/badge/ImGui-Editor_UI-06b6d4?style=for-the-badge)](https://github.com/ocornut/imgui)
[![Gradle](https://img.shields.io/badge/Gradle-Build-ef4444?style=for-the-badge&logo=gradle)](https://gradle.org/)

<br />

| OpenGL Rendering | Custom ECS | In-Game Editor | JSON Persistence | Fat JAR Deploy |
|:----------------:|:----------:|:--------------:|:----------------:|:--------------:|
| LWJGL + GLFW | Entity-Component | ImGui Panels | Gson Serialization | Gradle Task |

<br />

</div>

---

## Table of Contents

1. [Introduction](#-introduction)
2. [Design Philosophy](#-design-philosophy)
3. [Engine Architecture](#-engine-architecture)
   - [High-Level Overview](#high-level-architectural-overview)
   - [Package Structure Map](#package-structure-map)
   - [Core Subsystem Breakdown](#core-subsystem-breakdown)
4. [The Render & Update Loop](#-the-render--update-loop)
5. [Entity-Component System (ECS)](#-entity-component-system-ecs)
6. [In-Game Level Editor](#-in-game-level-editor)
7. [Scene Persistence — Save & Load](#-scene-persistence--save--load)
8. [Renderer Pipeline](#-renderer-pipeline)
9. [Physics Layer](#-physics-layer)
10. [Asset Management](#-asset-management)
11. [Keyboard & Mouse Input](#-keyboard--mouse-input)
12. [Build & Run](#-build--run)
13. [Project Structure](#-project-structure)
14. [Developer Guide — Extending the Engine](#-developer-guide--extending-the-engine)
15. [Editor Shortcuts](#-editor-shortcuts)
16. [Troubleshooting](#-troubleshooting)
17. [Roadmap](#-roadmap)
18. [Author](#-author)

---

## Introduction

**Bit-No-Mori** *(ビットの森 — Japanese: "Forest of Bits")* is a compact, intentionally approachable Java 2D game engine built from first principles. It does not wrap an existing engine — every core system, from the OpenGL render loop to the ImGui editor integration, is handcrafted and readable.

The project is designed as both a **functional game engine** capable of producing playable 2D levels and a **learning resource** — an engine small enough that a single developer can trace execution from the GLFW window creation all the way through the ECS update tick without drowning in abstraction layers.

It ships with a fully featured **in-game level editor** that lets you place tiles, prefabs, sounds, and tweak object properties in real-time — all without leaving the running game window.

> *"An engine you can fit in your head is an engine you can truly control."*

---

## Design Philosophy

```
THREE CORE PRINCIPLES THAT SHAPED EVERY DECISION IN BIT-NO-MORI
─────────────────────────────────────────────────────────────────

  1. SMALL AND APPROACHABLE
     ─────────────────────────────────────────────────────────
     Every system is written to be read. No opaque abstractions.
     If you want to know how rendering works, read Renderer.java.
     If you want to know how scenes load, read Scene.java.
     The codebase fits in one developer's working memory.

  2. EDITOR-FIRST DEVELOPMENT
     ─────────────────────────────────────────────────────────
     Level iteration happens inside the running engine, not in
     external tools. ImGui panels give immediate feedback.
     Press E. Place a tile. Press Save. Done.

  3. PORTABLE BUILD
     ─────────────────────────────────────────────────────────
     ./gradlew fatJar produces a single runnable JAR.
     Share it. Run it. No installation required beyond the JVM.
```

---

## Engine Architecture

### High-Level Architectural Overview

Bit-No-Mori is organized into six cleanly separated subsystems. Each subsystem owns its domain completely and communicates through well-defined interfaces.

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                         BIT-NO-MORI ENGINE ARCHITECTURE                          │
└─────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────┐
│                              ENTRY POINT                                         │
│                        org.example.Main.java                                     │
│                     Bootstraps Window and launches loop                          │
└──────────────────────────────────┬──────────────────────────────────────────────┘
                                   │
                                   ▼
┌─────────────────────────────────────────────────────────────────────────────────┐
│                            jade / CORE RUNTIME                                   │
│                                                                                  │
│   ┌──────────────┐   ┌──────────────┐   ┌──────────────┐   ┌─────────────────┐ │
│   │   Window     │   │ KeyListener  │   │ MouseListener│   │  ImGuiLayer     │ │
│   │              │   │              │   │              │   │                 │ │
│   │ GLFW window  │   │ Frame-based  │   │ Frame-based  │   │ Editor panel    │ │
│   │ render loop  │   │ key states   │   │ mouse states │   │ orchestration   │ │
│   │ mode toggle  │   │ & callbacks  │   │ & callbacks  │   │ & draw calls    │ │
│   └──────┬───────┘   └──────────────┘   └──────────────┘   └─────────────────┘ │
└──────────┼──────────────────────────────────────────────────────────────────────┘
           │  currentScene.update(dt) / editorUpdate(dt)
           ▼
┌─────────────────────────────────────────────────────────────────────────────────┐
│                              scenes / SCENE LAYER                                │
│                                                                                  │
│   ┌──────────────────────────┐        ┌──────────────────────────────────────┐  │
│   │  Scene                   │        │  SceneInitializer (subclasses)        │  │
│   │                          │        │                                      │  │
│   │  Owns []GameObject list  │        │  LevelEditorSceneInitializer         │  │
│   │  Manages ECS lifecycle   │        │  LevelSceneInitializer               │  │
│   │  save() / load() logic   │        │  loadResources() + init() + imgui()  │  │
│   └──────────────────────────┘        └──────────────────────────────────────┘  │
└──────────┬──────────────────────────────────────────────────────────────────────┘
           │  forEach GameObject → update components
           ▼
┌─────────────────────────────────────────────────────────────────────────────────┐
│                          components / ECS LAYER                                  │
│                                                                                  │
│  ┌─────────────┐ ┌──────────────┐ ┌───────────────┐ ┌──────────────────────┐   │
│  │ Transform   │ │SpriteRenderer│ │RigidBody2D    │ │ StateMachine         │   │
│  │ Component   │ │ Component    │ │ Component     │ │ Component            │   │
│  └─────────────┘ └──────────────┘ └───────────────┘ └──────────────────────┘   │
│  ┌─────────────┐ ┌──────────────┐ ┌───────────────┐ ┌──────────────────────┐   │
│  │MouseControls│ │KeyControls   │ │ Collider2D    │ │ EditorGizmos         │   │
│  │ Component   │ │ Component    │ │ Component     │ │ Component            │   │
│  └─────────────┘ └──────────────┘ └───────────────┘ └──────────────────────┘   │
└──────────┬──────────────────────────────────────────────────────────────────────┘
           │
           ├──────────────────► renderer / RENDER PIPELINE
           │                    Mesh · Shader · Texture · Framebuffer
           │
           └──────────────────► util / ASSET MANAGEMENT
                                AssetPool · Settings · ShaderManager
```

---

### Package Structure Map

```
bit-no-mori/
│
├── 📂 src/main/java/
│   │
│   ├── 📂 jade/                          ← Core runtime
│   │   ├── Window.java                   ← GLFW window, main loop, mode switching
│   │   ├── ImGuiLayer.java               ← ImGui context + panel orchestration
│   │   ├── KeyListener.java              ← Static singleton, GLFW key callbacks
│   │   └── MouseListener.java            ← Static singleton, GLFW mouse callbacks
│   │
│   ├── 📂 scenes/                        ← Scene lifecycle management
│   │   ├── Scene.java                    ← GameObject container + save/load
│   │   ├── SceneInitializer.java         ← Abstract initializer contract
│   │   ├── LevelEditorSceneInitializer   ← Editor scene: tiles, prefabs, ImGui
│   │   └── LevelSceneInitializer         ← Play scene: game object setup
│   │
│   ├── 📂 components/                    ← ECS building blocks
│   │   ├── Component.java                ← Abstract base: start/update/editorUpdate
│   │   ├── SpriteRenderer.java           ← Renders a sprite from a spritesheet
│   │   ├── Transform.java                ← Position, rotation, scale
│   │   ├── RigidBody2D.java              ← Physics body properties
│   │   ├── BoxCollider2D.java            ← Axis-aligned box collider
│   │   ├── CircleCollider.java           ← Circle collider
│   │   ├── StateMachine.java             ← Finite state machine for animations
│   │   ├── MouseControls.java            ← Editor object placement + selection
│   │   ├── KeyControls.java              ← Editor keyboard shortcuts
│   │   └── EditorCamera.java             ← Camera pan/zoom in editor mode
│   │
│   ├── 📂 renderer/                      ← OpenGL rendering pipeline
│   │   ├── Renderer.java                 ← Batched draw call dispatcher
│   │   ├── Shader.java                   ← GLSL shader compilation + uniforms
│   │   ├── Texture.java                  ← OpenGL texture loading + binding
│   │   ├── Framebuffer.java              ← Off-screen render target
│   │   ├── Mesh.java                     ← VAO/VBO vertex buffer management
│   │   └── ShaderManager.java            ← Shader cache + lifecycle
│   │
│   ├── 📂 physics2d/                     ← 2D physics layer
│   │   ├── Physics2D.java                ← Physics world simulation step
│   │   ├── RigidBody2D.java              ← Body: mass, velocity, forces
│   │   └── Colliders.java                ← Collision detection utilities
│   │
│   ├── 📂 util/                          ← Shared utilities
│   │   ├── AssetPool.java                ← Resource cache: textures/shaders/sounds
│   │   └── Settings.java                 ← Engine constants and grid config
│   │
│   └── org/example/
│       └── Main.java                     ← Entry point
│
├── 📂 assets/
│   ├── shaders/
│   │   └── default.glsl                  ← Primary vertex + fragment shader
│   ├── images/                           ← Sprite sheets and textures
│   ├── sounds/                           ← Audio assets (OpenAL)
│   └── fonts/
│
├── level.txt                             ← Active scene serialization (JSON)
├── build.gradle                          ← Gradle build definition
└── gradlew / gradlew.bat                 ← Gradle wrapper scripts
```

---

### Core Subsystem Breakdown

| Subsystem | Package | Key Class | Responsibility |
|---|:---:|---|---|
| **Core Runtime** | `jade` | `Window.java` | GLFW init, render loop, editor/play toggle |
| **Input** | `jade` | `KeyListener`, `MouseListener` | Singleton, frame-state input maps |
| **Editor UI** | `jade` | `ImGuiLayer.java` | ImGui context management, panel dispatch |
| **Scene** | `scenes` | `Scene.java` | GameObject lifecycle, save/load |
| **Initializers** | `scenes` | `*SceneInitializer` | Resource loading, scene content setup |
| **ECS** | `components` | `Component.java` | Composable game object behaviors |
| **Rendering** | `renderer` | `Renderer.java` | OpenGL batched draw calls |
| **Physics** | `physics2d` | `Physics2D.java` | Collision detection and response |
| **Assets** | `util` | `AssetPool.java` | Texture, shader, sound resource caching |

---

## The Render & Update Loop

The entire engine runtime lives inside `Window.loop()`. Every frame executes a fixed sequence of operations regardless of mode.

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                         BIT-NO-MORI FRAME LOOP                                   │
│                              Window.loop()                                       │
└─────────────────────────────────────────────────────────────────────────────────┘

  FRAME START
       │
       ▼
  ┌─────────────────────────────────────────────────────────┐
  │  STEP 1 — POLL GLFW EVENTS                              │
  │  glfwPollEvents()                                       │
  │  KeyListener and MouseListener state maps refreshed     │
  └────────────────────────────┬────────────────────────────┘
                               │
                               ▼
  ┌─────────────────────────────────────────────────────────┐
  │  STEP 2 — PICKING PASS (Editor Only)                    │
  │  Render scene to PickingTexture framebuffer             │
  │  Each GameObject rendered with unique color ID          │
  │  Used for click-to-select object identification         │
  └────────────────────────────┬────────────────────────────┘
                               │
                               ▼
  ┌─────────────────────────────────────────────────────────┐
  │  STEP 3 — MAIN RENDER PASS                              │
  │  Render scene to primary Framebuffer                    │
  │  Blit framebuffer output to display                     │
  │  Renderer dispatches batched OpenGL draw calls          │
  └────────────────────────────┬────────────────────────────┘
                               │
                               ▼
  ┌─────────────────────────────────────────────────────────┐
  │  STEP 4 — MODE BRANCH                                   │
  │                                                         │
  │    ┌─── EDITOR MODE ──────────────────────────────┐     │
  │    │  currentScene.editorUpdate(dt)               │     │
  │    │  → forEach GameObject.editorUpdate()         │     │
  │    │  → MouseControls, KeyControls active         │     │
  │    │  → EditorCamera pan/zoom active              │     │
  │    │  ImGuiLayer.update() → draws all panels      │     │
  │    └──────────────────────────────────────────────┘     │
  │                                                         │
  │    ┌─── PLAY MODE ────────────────────────────────┐     │
  │    │  currentScene.update(dt)                     │     │
  │    │  → forEach GameObject.update()               │     │
  │    │  → Physics2D.update(dt)                      │     │
  │    │  → StateMachines, player controls active     │     │
  │    └──────────────────────────────────────────────┘     │
  └────────────────────────────┬────────────────────────────┘
                               │
                               ▼
  ┌─────────────────────────────────────────────────────────┐
  │  STEP 5 — BUFFER SWAP                                   │
  │  glfwSwapBuffers(window)                                │
  │  Present rendered frame to the OS window surface        │
  └────────────────────────────┬────────────────────────────┘
                               │
                               ▼
  NEXT FRAME  (loop continues until glfwWindowShouldClose)
```

**Delta Time** is computed each frame as `dt = (currentTime - lastTime) / 1e9` and passed to all update methods, ensuring frame-rate independent movement and physics.

---

## Entity-Component System (ECS)

Bit-No-Mori implements a **lightweight, composition-based ECS**. Rather than deep inheritance hierarchies, all game object behavior is expressed through reusable, stackable `Component` instances attached to a `GameObject`.

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                              ECS OVERVIEW                                        │
└─────────────────────────────────────────────────────────────────────────────────┘

  ENTITY = GameObject
  ┌──────────────────────────────────────────────────────────────┐
  │  GameObject                                                   │
  │  ─────────────────────────────────────────────────────────── │
  │  String name                                                  │
  │  int uid                         ← Unique persistent ID      │
  │  List<Component> components      ← Behavior stack            │
  │  boolean serializable            ← Include in save/load?     │
  │                                                               │
  │  addComponent(Component c)                                    │
  │  getComponent(Class<T> type) → T                             │
  │  start()      → calls c.start()    for each component        │
  │  update(dt)   → calls c.update()   for each component        │
  └──────────────────────────────────────────────────────────────┘

  COMPONENT = Behavior Unit (abstract base)
  ┌──────────────────────────────────────────────────────────────┐
  │  Component                                                    │
  │  ─────────────────────────────────────────────────────────── │
  │  GameObject gameObject   ← Back-reference to parent entity   │
  │                                                               │
  │  start()           ← Called once on scene load               │
  │  update(dt)        ← Called every frame in PLAY mode         │
  │  editorUpdate(dt)  ← Called every frame in EDITOR mode       │
  │  imgui()           ← Called by Properties panel in editor    │
  └──────────────────────────────────────────────────────────────┘

EXAMPLE: A "Player" GameObject composition
──────────────────────────────────────────────────────────────────
  GameObject "Player"
      │
      ├── Transform           { position(0,0), scale(1,1) }
      ├── SpriteRenderer      { spritesheet: mario.png, index: 0 }
      ├── RigidBody2D         { mass: 1.0, velocity: (0,0) }
      ├── BoxCollider2D       { width: 0.25, height: 0.25 }
      └── StateMachine        { states: [Idle, Run, Jump, Die] }

EXAMPLE: A "Goomba" Enemy GameObject
──────────────────────────────────────────────────────────────────
  GameObject "Goomba"
      │
      ├── Transform           { position(5,0), scale(1,1) }
      ├── SpriteRenderer      { spritesheet: enemies.png }
      ├── RigidBody2D         { mass: 0.5 }
      ├── BoxCollider2D       { width: 0.25, height: 0.25 }
      └── StateMachine        { states: [Walk, Squished] }
```

### Component Lifecycle

```
Scene.load() called
       │
       ▼
  For each GameObject:
       ├── component.start()         ← Resource binding, initial state setup
       │
  Per frame (Play Mode):
       ├── component.update(dt)      ← Gameplay logic, physics response
       │
  Per frame (Editor Mode):
       ├── component.editorUpdate()  ← Gizmo drawing, placement logic
       │
  Editor Properties Panel open:
       └── component.imgui()         ← Expose editable fields to ImGui
```

---

## In-Game Level Editor

The level editor runs **inside the game window** — no separate editor application, no round-trip to external tools. Toggle between edit and play mode at any time with a single keypress.

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                          IN-GAME LEVEL EDITOR LAYOUT                             │
└─────────────────────────────────────────────────────────────────────────────────┘

 ┌───────────────┬──────────────────────────────────────────────┬─────────────────┐
 │               │                                              │                 │
 │  TILE         │                                              │  PROPERTIES     │
 │  PALETTE      │         GAME VIEWPORT                        │  PANEL          │
 │               │         (Framebuffer rendered scene)         │                 │
 │ [Tile imgs]   │                                              │  Selected Obj:  │
 │ [Prefab btns] │         ← Click to place selected tile       │  Transform      │
 │               │         ← Drag to move objects              │  SpriteRenderer │
 │  SOUND        │         ← Right-click to delete             │  RigidBody2D    │
 │  PREVIEW      │                                              │  [+ Add Comp]   │
 │               │                                              │                 │
 │ [▶ Play sfx]  │                                              │ [Save] [Load]   │
 │               │                                              │                 │
 └───────────────┴──────────────────────────────────────────────┴─────────────────┘
  ImGui dockable panels                                          ImGui properties
```

### Editor Workflow

```
STEP 1 — LAUNCH IN EDITOR MODE
  Set RELEASE_BUILD = false in jade/Window.java
  Run org.example.Main
  Engine starts in editor scene (LevelEditorSceneInitializer)

STEP 2 — SELECT A TILE OR PREFAB
  Open "Level Editor Stuff" ImGui panel
  Click any tile image button → tile attached to mouse cursor
  Or click a prefab button (Player, Goomba, Pipe, etc.)

STEP 3 — PLACE IN WORLD
  Click anywhere in the game viewport
  Object is snapped to the configured grid size (Settings.GRID_WIDTH)
  Object appears immediately in the scene

STEP 4 — SELECT AND MODIFY
  Click an existing object to select it
  Properties panel populates with all component fields
  Edit position, scale, sprite index, physics params in-place

STEP 5 — TEST YOUR LEVEL
  Press E → switches to Play mode
  All editor gizmos hidden, physics activates, player control enabled
  Press E again → back to editor mode instantly

STEP 6 — SAVE
  Click "Save" in the Level Editor panel
  Scene serialized to level.txt (JSON via Gson)
  Click "Load" at any time to restore last saved state
```

### ImGui Panel Architecture

```
ImGuiLayer.update()
      │
      ├── ImGui.begin("Level Editor Stuff")
      │       ├── Tile palette (image buttons from AssetPool spritesheets)
      │       ├── Prefab placement buttons
      │       └── Sound preview controls
      │
      ├── ImGui.begin("Properties")
      │       └── selectedGameObject.getAllComponents()
      │               └── component.imgui()  ← Each component draws its own fields
      │
      ├── ImGui.begin("Viewport")
      │       └── Framebuffer texture displayed as ImGui image
      │
      └── ImGui.begin("Game Objects")
              └── Hierarchy list of all scene GameObjects
```

---

## Scene Persistence — Save & Load

Bit-No-Mori uses **Gson with custom type adapters** to serialize the full scene graph into a human-readable JSON file.

```
SAVE FLOW
─────────────────────────────────────────────────────────────────────────
  User clicks "Save" button in editor
       │
       ▼
  Scene.save() called
       │
       ▼
  Filter: keep only GameObjects where serializable == true
       │
       ▼
  Gson serializes:
    [
      {
        "name": "Ground Tile",
        "uid": 42,
        "transform": { "position": [2.0, -1.0], "scale": [1.0, 1.0] },
        "components": [
          { "type": "SpriteRenderer", "spriteIndex": 3 },
          { "type": "BoxCollider2D",  "width": 0.25 }
        ]
      },
      ...
    ]
       │
       ▼
  Written to level.txt (working directory)

LOAD FLOW
─────────────────────────────────────────────────────────────────────────
  User clicks "Load" or scene switch triggers reload
       │
       ▼
  Scene.load() reads level.txt
       │
       ▼
  GameObjectDeserializer reconstructs each GameObject
       │
       ▼
  ComponentDeserializer maps "type" field → correct Component subclass
       │
       ▼
  Internal UID counter reset (prevents collision with new objects)
       │
       ▼
  AssetPool re-binds textures/shaders to deserialized components
       │
       ▼
  Scene ready — all objects initialized via start()
```

**Files involved in persistence:**

| File | Role |
|---|---|
| `scenes/Scene.java` | `save()` and `load()` implementation |
| `jade/Window.java` | Scene switching and serialization trigger |
| `util/AssetPool.java` | Resource re-binding after deserialization |
| `level.txt` | Active scene data (JSON, human-readable) |

> **Tip:** `level.txt` is plain JSON — you can inspect, hand-edit, or diff it with any text editor. Version control it to track level design history.

---

## Renderer Pipeline

The renderer uses **OpenGL via LWJGL** with a framebuffer-based deferred display model.

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                          BIT-NO-MORI RENDER PIPELINE                             │
└─────────────────────────────────────────────────────────────────────────────────┘

  Scene GameObjects
       │
       ▼
  ┌──────────────────────────────────────────────────────────┐
  │  PASS 1 — PICKING RENDER (Editor Mode Only)              │
  │  Bind: PickingTexture framebuffer                        │
  │  Render each GameObject with flat unique-color shader    │
  │  Purpose: pixel readback → determine which obj clicked   │
  └──────────────────────────────────────────────────────────┘
       │
       ▼
  ┌──────────────────────────────────────────────────────────┐
  │  PASS 2 — MAIN SCENE RENDER                              │
  │  Bind: Primary Framebuffer                               │
  │  Shader: assets/shaders/default.glsl                    │
  │                                                          │
  │  For each SpriteRenderer component:                      │
  │    1. Sample sprite region from spritesheet texture      │
  │    2. Build quad vertices with UV coordinates            │
  │    3. Upload to VAO / VBO (Mesh)                         │
  │    4. Draw call: glDrawElements(GL_TRIANGLES, ...)       │
  └──────────────────────────────────────────────────────────┘
       │
       ▼
  ┌──────────────────────────────────────────────────────────┐
  │  PASS 3 — FRAMEBUFFER BLIT / IMGUI VIEWPORT              │
  │  Framebuffer texture → ImGui image panel (editor)        │
  │  Or direct blit to default framebuffer (play mode)       │
  └──────────────────────────────────────────────────────────┘

  GLSL Shader structure (default.glsl):
  ──────────────────────────────────────────────────────────
  Vertex Shader:
    in vec3 aPos;          ← Vertex position
    in vec4 aColor;        ← Vertex color tint
    in vec2 aTexCoords;    ← UV texture coordinates
    in float aTexId;       ← Texture slot selector
    uniform mat4 uProjection, uView;
    → Outputs: fColor, fTexCoords, fTexId

  Fragment Shader:
    Sample texture[fTexId] at fTexCoords
    Multiply by fColor tint
    → Output: final pixel color
```

---

## Physics Layer

The `physics2d` package provides a lightweight 2D physics simulation sufficient for platformer-style gameplay.

```
Component Graph (Physics):
──────────────────────────────────────────────────────────
  RigidBody2D              BoxCollider2D / CircleCollider
  ─────────────────        ──────────────────────────────
  mass                     width / height / radius
  velocity (Vec2)          offset from transform origin
  acceleration (Vec2)      isTrigger (no physics response)
  bodyType (STATIC /
            DYNAMIC /
            KINEMATIC)
       │                          │
       └──────────┬───────────────┘
                  │
                  ▼
           Physics2D.update(dt)
           ─────────────────────
           Integrate velocity
           Detect AABB / circle overlaps
           Resolve collisions → adjust positions + velocities
           Notify components via onCollision(other)
```

**Supported collision pairs:**

| Shape A | Shape B | Supported |
|:---:|:---:|:---:|
| BoxCollider2D | BoxCollider2D | ✅ AABB |
| CircleCollider | CircleCollider | ✅ Circle-Circle |
| BoxCollider2D | CircleCollider | ✅ AABB-Circle |

---

## Asset Management

`AssetPool` is the engine's **single source of truth for all runtime resources**. It caches every texture, shader, spritesheet, and sound by filepath, ensuring each asset is loaded from disk exactly once per session.

```
AssetPool — Resource Caching Strategy
──────────────────────────────────────────────────────────────────────
  Request: AssetPool.getTexture("assets/images/mario.png")
       │
       ├── Cache hit?  → return cached Texture instance immediately
       │
       └── Cache miss? → load from disk → cache → return

  Cached resource types:
  ┌──────────────────┬───────────────────────────────────────────┐
  │ Resource Type    │ Key                                       │
  ├──────────────────┼───────────────────────────────────────────┤
  │ Texture          │ file path string                          │
  │ Spritesheet      │ file path string                          │
  │ Shader           │ file path string                          │
  │ Sound            │ file path string                          │
  └──────────────────┴───────────────────────────────────────────┘

  Post-deserialization: AssetPool re-binds OpenGL handles to
  deserialized Texture/Shader instances (GPU handles don't
  survive serialization — only paths do).
```

---

## Keyboard & Mouse Input

Both input systems are **static singletons** registered as GLFW callbacks on window creation. Any component anywhere in the codebase can query input state without holding a reference.

```
INPUT SYSTEM DESIGN
──────────────────────────────────────────────────────────────────────
  GLFW Callback               Static Singleton                Usage
  ─────────────────────────   ───────────────────────────     ──────────────────
  glfwSetKeyCallback()     →  KeyListener.keyPressed[key]  →  KeyListener.isKeyPressed(GLFW_KEY_E)
  glfwSetMouseButton...()  →  MouseListener.mouseButtonP[] →  MouseListener.mouseButtonDown(0)
  glfwSetCursorPos...()    →  MouseListener.x, y           →  MouseListener.getX()
  glfwSetScrollCallback()  →  MouseListener.scrollX/Y      →  MouseListener.getScrollY()

  Frame-state model:
  ─────────────────────────────────────────────────────────────────
  End of each frame: KeyListener.endFrame() resets edge-triggered
  states (just-pressed / just-released) to prevent multi-frame
  detection of single keypresses.
```

---

## Build & Run

### Requirements

| Tool | Minimum Version | Verify |
|---|:---:|---|
| Java JDK | 17+ | `java --version` |
| Gradle | via wrapper | `./gradlew --version` |

> No system-wide Gradle installation needed — `gradlew` / `gradlew.bat` bootstrap the correct version automatically.

---

### Build Commands

```bash
# ── Compile & test ────────────────────────────────────────────────
./gradlew build                    # Unix / macOS
.\gradlew.bat build                # Windows

# ── Package (fat JAR with all dependencies) ────────────────────────
./gradlew fatJar                   # Unix / macOS
.\gradlew.bat fatJar               # Windows

# Output: build/libs/mario-1.0-SNAPSHOT.jar

# ── Run the fat JAR ────────────────────────────────────────────────
# Ensure assets/ and level.txt are in the SAME working directory

java -jar build/libs/mario-1.0-SNAPSHOT.jar          # Unix
java -jar build\libs\mario-1.0-SNAPSHOT.jar          # Windows

# ── Run from IDE ───────────────────────────────────────────────────
# Execute: org.example.Main
# Set working directory to project root (where assets/ lives)
```

### Editor vs Release Mode

```java
// jade/Window.java
public static final boolean RELEASE_BUILD = false;  // ← Editor mode (ImGui active)
public static final boolean RELEASE_BUILD = true;   // ← Release mode (ImGui hidden)
```

| Mode | `RELEASE_BUILD` | ImGui Panels | Editor Controls | Physics |
|:---:|:---:|:---:|:---:|:---:|
| **Editor** | `false` | ✅ Visible | ✅ Active | ❌ Paused |
| **Play** | `false` (press E) | ✅ Visible | ❌ Off | ✅ Running |
| **Release** | `true` | ❌ Hidden | ❌ Off | ✅ Running |

---

## Project Structure

```
bit-no-mori/
│
├── 📂 src/
│   └── main/java/
│       ├── jade/                   ← Core: Window, Input, ImGui
│       ├── scenes/                 ← Scene lifecycle + initializers
│       ├── components/             ← ECS component library
│       ├── renderer/               ← OpenGL pipeline
│       ├── physics2d/              ← 2D physics simulation
│       ├── util/                   ← AssetPool, Settings
│       └── org/example/Main.java   ← Entry point
│
├── 📂 assets/
│   ├── shaders/default.glsl        ← Primary GLSL shader
│   ├── images/                     ← Spritesheets and textures
│   ├── sounds/                     ← OpenAL audio files
│   └── fonts/                      ← ImGui font files
│
├── level.txt                       ← JSON scene data (save/load target)
├── build.gradle                    ← Gradle build config + fatJar task
├── gradlew                         ← Unix Gradle wrapper
├── gradlew.bat                     ← Windows Gradle wrapper
└── README.md
```

---

## Developer Guide — Extending the Engine

### Create a New Component

```java
// 1. Create class in components/ extending Component
public class BouncePad extends Component {

    public float bounceForce = 15.0f;   // Exposed to ImGui Properties panel

    @Override
    public void start() {
        // One-time init: cache references, load resources
    }

    @Override
    public void update(float dt) {
        // Called every frame in Play mode
        // Check collisions, apply forces, update state
    }

    @Override
    public void editorUpdate(float dt) {
        // Called every frame in Editor mode
        // Draw gizmos, show handles, etc.
    }

    @Override
    public void imgui() {
        // Expose editable fields to the Properties panel
        float[] bForce = { bounceForce };
        if (ImGui.dragFloat("Bounce Force", bForce)) {
            bounceForce = bForce[0];
        }
    }
}

// 2. Attach to any GameObject
GameObject pad = new GameObject("Bounce Pad");
pad.addComponent(new Transform());
pad.addComponent(new SpriteRenderer());
pad.addComponent(new BouncePad());
scene.addGameObjectToScene(pad);
```

---

### Create a New Prefab

```java
// In LevelEditorSceneInitializer or a Prefabs utility class:
public static GameObject createBouncePad(Spritesheet sheet) {
    GameObject pad = new GameObject("Bounce Pad",
        new Transform(new Vector2f(0, 0), new Vector2f(1, 0.5f)),
        ZIndex.DEFAULT
    );
    SpriteRenderer sr = new SpriteRenderer();
    sr.setSprite(sheet.getSprite(12));         // tile index 12
    pad.addComponent(sr);
    pad.addComponent(new BoxCollider2D());
    pad.addComponent(new BouncePad());
    return pad;
}

// Add a button to the editor ImGui panel:
// In LevelEditorSceneInitializer.imgui():
if (ImGui.button("Bounce Pad")) {
    GameObject prefab = createBouncePad(spritesheet);
    mouseControls.pickupObject(prefab);
}
```

---

### Create a New Scene

```java
// 1. Implement SceneInitializer
public class BossSceneInitializer extends SceneInitializer {

    @Override
    public void loadResources(Scene scene) {
        // Register textures and shaders with AssetPool
        AssetPool.getTexture("assets/images/boss_sheet.png");
    }

    @Override
    public void init(Scene scene) {
        // Build scene content: GameObjects, components, cameras
        GameObject boss = createBoss();
        scene.addGameObjectToScene(boss);
    }

    @Override
    public void imgui() {
        // Optional: editor panels specific to this scene
    }
}

// 2. Switch to the new scene from anywhere in the engine
Window.changeScene(new BossSceneInitializer());
```

---

## Editor Shortcuts

| Shortcut | Action |
|:---:|---|
| `E` | Toggle between **Editor** mode and **Play** mode |
| `Delete` | Delete the currently selected GameObject(s) |
| `Ctrl + D` | Duplicate the current selection |
| `← → ↑ ↓` | Nudge selected object's position by one grid unit |
| `Page Up` | Increase z-index (render layer) of selection |
| `Page Down` | Decrease z-index (render layer) of selection |
| `Left Click` | Place held tile / select existing object |
| `Right Click` | Delete object under cursor |
| `Scroll Wheel` | Zoom editor camera in/out |
| `Middle Click Drag` | Pan editor camera |

---

## Troubleshooting

| Symptom | Cause | Fix |
|---|---|---|
| Black screen, no assets visible | `assets/` folder not in working directory | Set run working dir to project root; verify `assets/` exists next to the JAR |
| ImGui panels not appearing | `RELEASE_BUILD = true` | Set `RELEASE_BUILD = false` in `jade/Window.java` |
| `level.txt` not found on load | File missing from working directory | Click **Save** first to generate it, or create an empty `level.txt` |
| Shader compile error on start | Shader file path incorrect | Confirm `assets/shaders/default.glsl` exists; check `ShaderManager` path constants |
| Textures appear as white quads | Texture path mismatch post-deserialization | Verify `AssetPool.getTexture()` paths match exactly what was serialized |
| Physics objects fall through floor | Collider not added or wrong body type | Add `BoxCollider2D` + set `RigidBody2D.bodyType = DYNAMIC` on falling objects |
| Extension components not in Properties panel | `imgui()` not overridden | Override `imgui()` in your component and add ImGui widget calls |

---

## Roadmap

```
Current State (v1.x)
─────────────────────────────────────────────────────────────────────────
  ✅ OpenGL rendering via LWJGL
  ✅ GLFW window + input management
  ✅ Entity-Component system
  ✅ In-game ImGui level editor
  ✅ Scene JSON persistence (save / load)
  ✅ Spritesheet + tile palette
  ✅ Prefab placement system
  ✅ 2D physics (AABB + circle colliders)
  ✅ OpenAL audio preview in editor
  ✅ Fat JAR Gradle packaging

Planned (v2.x)
─────────────────────────────────────────────────────────────────────────
  🔲 Sprite animation system with frame timeline editor
  🔲 Tiled map format (.tmx) import support
  🔲 Camera follow component with configurable lerp
  🔲 Particle system component
  🔲 In-editor undo / redo (command pattern)
  🔲 "Save As" dialog with named level slots
  🔲 CONTRIBUTING.md + MIT license file
  🔲 levels/ directory with bundled example scenes

Vision (v3.x)
─────────────────────────────────────────────────────────────────────────
  🔲 Classpath asset loading (assets inside JAR)
  🔲 Scripting via GraalVM / Nashorn for runtime behavior
  🔲 Networked multiplayer scene sync prototype
  🔲 Web export via TeaVM or GWT backend
```

---

## Author

<br />

```
  ╔══════════════════════════════════════════════════════════════════╗
  ║                                                                  ║
  ║   Bit-No-Mori — ビットの森                                        ║
  ║   "Forest of Bits"                                               ║
  ║                                                                  ║
  ║   Built on a single conviction: a game engine should be          ║
  ║   something a developer can hold in their mind completely.       ║
  ║                                                                  ║
  ║   No black boxes. No magic. Every system is readable,            ║
  ║   every loop is traceable, every component is yours             ║
  ║   to inspect, extend, or replace.                               ║
  ║                                                                  ║
  ║   Authored by Aryan Dhiman                                       ║
  ║   Software Engineer & Game Systems Enthusiast                    ║
  ║                                                                  ║
  ╚══════════════════════════════════════════════════════════════════╝
```

[![GitHub](https://img.shields.io/badge/GitHub-Aryanplux-181717?style=for-the-badge&logo=github)](https://github.com/Aryanplux)

---

### Contributing

Contributions are welcome — open issues and pull requests freely.

**Before submitting a PR:**

```bash
# 1. Ensure the project compiles cleanly
./gradlew build

# 2. Smoke-test your changes by running the engine
#    Run: org.example.Main
#    Test both Editor mode (E key) and Play mode

# 3. Keep commits small and focused with clear messages
#    Good:  "Add BouncePad component with configurable force"
#    Bad:   "Changes"
```

---

<div align="center">

```
────────────────────────────────────────────────────────────────────────
  Bit-No-Mori · ビットの森 · Java 2D Game Engine · Authored by Aryan Dhiman
  LWJGL · OpenGL · ImGui · ECS · Gradle · Open Use License
────────────────────────────────────────────────────────────────────────
```

*Small engine. Full control. Every pixel, yours.*

</div>
