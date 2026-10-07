# Wave Automata Physics Engine
An interavtive, fast, 2D fluid simulation made using java Swing. This application uses a cellular automaton approximation of the wave equation to simulate liquid ripples, obstacle wave deflection, and construction interference patterns.
---
## Interactive Controls
---
### Mouse Shortcuts
* **Left Click & Drag:** Emit waves where the mouse is.
* **Right Click & Drag:** Paint solid walls as boundaries.
* **Scroll Wheel Click & Drag:** Erase walls and neutralize waves.

### Keyboard Shortcuts
* `Up Arrow`: Increase the starting amplitude of the waves.
* `Down Arrow`: Decrease the starting amplitude of the waves.
* `U`: Increase dampening (waves conserver energy longer).
* `J`: Decrease dampening (waves fade faster).
* `H`: Toggle the Heads-Up Display (HUD) menu overlay.
* `Spacebar`: Instantly clear all waves and walls.
* `Escape`: Exit the application.
---
## How to Run Locally
### Prerequisites
* **Java Development Kit (JDK) 8 or higher** installed on your computer.
### Compilation
1. Open your terminal in the directory containing `Main.java`
2. Compile the source file:
   ```bash
   javac Main.java
   ```
3. Run the compiled bytecode file:
   ```bash
   java Main
   ```