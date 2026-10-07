# Parallel Wave Automata Physics Engine

An interactive, high-performance 2D fluid simulation sandbox written in Java Swing. This application utilizes a discrete cellular automaton approximation of the wave equation to simulate liquid surface ripples, obstacle wave deflection, and constructive interference patterns in real time.

---

## ⚡ Performance Optimization
Unlike standard graphics engines that can lag when drawing millions of points sequentially, this engine uses **Multi-Threaded Direct Pixel Manipulation**. By converting row operations into independent, parallel structures using Java's `Parallel Stream` API, the rendering load is balanced symmetrically across all available CPU cores. This guarantees a locked frame rate even on high-DPI full-screen displays.

---

## 🎮 Interactive Controls & Shortcuts

Launch the application to enter a seamless full-screen canvas. Use your mouse and keyboard to shape the fluid dynamics environment:

### 🖱️ Mouse Inputs
* **Left Click & Drag:** Emit fluid ripple wave pulses into the pool.
* **Right Click & Drag:** Paint solid boundary obstacle walls.
* **Scroll Wheel Click & Drag:** Erase walls and neutralize lingering grid energy.

### ⌨️ Keyboard Shortcuts
* `UP ARROW` : Increase the raw amplitude power of created waves.
* `DOWN ARROW` : Decrease the raw amplitude power of created waves.
* `U` : Increase fluid damping (waves conserve energy longer).
* `J` : Decrease fluid damping (waves dissolve faster).
* `H` : Toggle / Hide the Heads-Up Display (HUD) menu overlay.
* `SPACEBAR` : Instantly clear all wave profiles, metrics, and custom painted walls.
* `ESCAPE` : Cleanly exit the engine framework.

---

## 🛠️ How to Run Locally

### Prerequisites
* **Java Development Kit (JDK) 8 or higher** installed on your computer.

### Compilation
1. Open your terminal in the directory containing `Main.java`.
2. Compile the source file:
   ```bash
   javac Main.java
   ```
3. Run the compiled bytecode file:
   ```bash
   java Main
   ```
