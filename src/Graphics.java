
//Created: 3/13/2021
//Handles graphics rendering

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;
import java.util.Vector;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Graphics {

    private static long window;

    public Graphics(int mapSize) {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(10 * mapSize, 10 * mapSize, "Lion King", NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);
    }

    public static void Update() {
        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    public void term() {
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public long getWindow() {
        return window;
    }

    public static void DrawRange(Zebra zebra) {
        float x = zebra.getX() / 100 - 1f;
        float y = zebra.getY() / 100 - 1f;
        float rad = zebra.getDetectRange() / 100f;

        GL11.glColor3f(167f / 255f, 5f / 255f, 237f / 255f);

        glBegin(GL_LINE_LOOP);
        for (float i = 0; i < 2 * Math.PI; i += 0.01f) {
            glVertex2f(x + rad * (float) Math.cos(i), y + rad * (float) Math.sin(i));
        }
        glEnd();
    }

    public static void DrawDir(Animal animal) {
        float x = animal.getX() / 100 - 1f;
        float y = animal.getY() / 100 - 1f;

        GL11.glColor3f(172f / 255f, 13f / 255f, 13f / 255f);

        Vector<Float> targetDir = animal.getDirection();
        //System.out.println("this: "+targetDir);

        float targetX = x+targetDir.get(0) * 1;
        float targetY = y+targetDir.get(1) * 1;

        glBegin(GL_LINES);
        glVertex2f(targetX, targetY);
        glVertex2f(x, y);
        glEnd();

    }

    public static void DrawObject(Object object) {
        float x,y;
        if (object instanceof Zebra) {
            DrawDir((Zebra)object);
            DrawRange((Zebra)object);
            if (((Zebra) object).isTargeted()) {
                GL11.glColor3f(33f / 255f, 248f / 255f, 255f / 255f);
            } else {
                GL11.glColor3f(170f / 255f, 170f / 255f, 170f / 255f);
            }

            x = ((Zebra)object).getX() / 100 - 1f;
            y = ((Zebra)object).getY() / 100 - 1f;
        }
        else if (object instanceof Lion) {
            DrawDir((Lion)object);
            GL11.glColor3f(243f / 255f, 105f / 255f, 25f / 255f);

            x = ((Lion)object).getX() / 100 - 1f;
            y = ((Lion)object).getY() / 100 - 1f;
        }
        else {
            if (((Plant)object).isTargeted()) {
                GL11.glColor3f(17f / 255f, 54f / 255f, 240f / 255f);
            } else {
                GL11.glColor3f(10f / 255f, 153f / 255f, 35f / 255f);
            }

            x = ((Plant)object).getX() / 100 - 1f;
            y = ((Plant)object).getY() / 100 - 1f;
        }

        glBegin(GL_QUADS);
        glVertex2f(x - 1f / 100f, y + 1f / 100f);
        glVertex2f(x + 1f / 100f, y + 1f / 100f);
        glVertex2f(x + 1f / 100f, y - 1f / 100f);
        glVertex2f(x - 1f / 100f, y - 1f / 100f);
        glEnd();
    }


}

//    CODE GRAVEYARD
//============================================================================
//
//    public void DrawZebra(Zebra zebra) {
//        if (zebra.isTargeted()) {
//            GL11.glColor3f(33f / 255f, 248f / 255f, 255f / 255f);
//        } else {
//            GL11.glColor3f(170f / 255f, 170f / 255f, 170f / 255f);
//        }
//
//        float x = zebra.getX() / 100 - 1f;
//        float y = zebra.getY() / 100 - 1f;
//
//        glBegin(GL_QUADS);
//        glVertex2f(x - 1f / 100f, y + 1f / 100f);
//        glVertex2f(x + 1f / 100f, y + 1f / 100f);
//        glVertex2f(x + 1f / 100f, y - 1f / 100f);
//        glVertex2f(x - 1f / 100f, y - 1f / 100f);
//        glEnd();
//    }
//
//    public void DrawLion(Lion lion) {
//        GL11.glColor3f(243f / 255f, 105f / 255f, 25f / 255f);
//
//        float x = lion.getX() / 100 - 1f;
//        float y = lion.getY() / 100 - 1f;
//
//        glBegin(GL_QUADS);
//        glVertex2f(x - 1f / 100f, y + 1f / 100f);
//        glVertex2f(x + 1f / 100f, y + 1f / 100f);
//        glVertex2f(x + 1f / 100f, y - 1f / 100f);
//        glVertex2f(x - 1f / 100f, y - 1f / 100f);
//        glEnd();
//    }
//
//    public void DrawPlant(Plant plant) {
//        if (plant.isTargeted()) {
//            GL11.glColor3f(17f / 255f, 54f / 255f, 240f / 255f);
//        } else {
//            GL11.glColor3f(10f / 255f, 153f / 255f, 35f / 255f);
//        }
//
//        float x = plant.getX() / 100 - 1f;
//        float y = plant.getY() / 100 - 1f;
//
//        glBegin(GL_QUADS);
//        glVertex2f(x - 1f / 100f, y + 1f / 100f);
//        glVertex2f(x + 1f / 100f, y + 1f / 100f);
//        glVertex2f(x + 1f / 100f, y - 1f / 100f);
//        glVertex2f(x - 1f / 100f, y - 1f / 100f);
//        glEnd();
//    }

//    public void run() {
//        //System.out.println("Hello LWJGL " + Version.getVersion() + "!");
//
//        init();
//        loop();
//
//        // Free the window callbacks and destroy the window
//        glfwFreeCallbacks(window);
//        glfwDestroyWindow(window);
//
//        // Terminate GLFW and free the error callback
//        glfwTerminate();
//        glfwSetErrorCallback(null).free();
//    }

//    public void init() {
//        // Setup an error callback. The default implementation
//        // will print the error message in System.err.
//        GLFWErrorCallback.createPrint(System.err).set();
//
//        // Initialize GLFW. Most GLFW functions will not work before doing this.
//        if ( !glfwInit() )
//            throw new IllegalStateException("Unable to initialize GLFW");
//
//        // Configure GLFW
//        glfwDefaultWindowHints(); // optional, the current window hints are already the default
//        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
//        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // the window will be resizable
//
//        // Create the window
//        window = glfwCreateWindow(1000, 1000, "Lion King", NULL, NULL);
//        if ( window == NULL )
//            throw new RuntimeException("Failed to create the GLFW window");
//
//        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
//        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
//            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
//                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
//        });
//
//        // Get the thread stack and push a new frame
//        try ( MemoryStack stack = stackPush() ) {
//            IntBuffer pWidth = stack.mallocInt(1); // int*
//            IntBuffer pHeight = stack.mallocInt(1); // int*
//
//            // Get the window size passed to glfwCreateWindow
//            glfwGetWindowSize(window, pWidth, pHeight);
//
//            // Get the resolution of the primary monitor
//            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
//
//            // Center the window
//            glfwSetWindowPos(
//                    window,
//                    (vidmode.width() - pWidth.get(0)) / 2,
//                    (vidmode.height() - pHeight.get(0)) / 2
//            );
//        } // the stack frame is popped automatically
//
//        // Make the OpenGL context current
//        glfwMakeContextCurrent(window);
//        // Enable v-sync
//        glfwSwapInterval(1);
//
//        // Make the window visible
//        glfwShowWindow(window);
//    }

//    public void loop() {
//        // This line is critical for LWJGL's interoperation with GLFW's
//        // OpenGL context, or any context that is managed externally.
//        // LWJGL detects the context that is current in the current thread,
//        // creates the GLCapabilities instance and makes the OpenGL
//        // bindings available for use.
//        GL.createCapabilities();
//
//        // Set the clear color
//        glClearColor(163f/255f, 191f/255f, 69f/255f, 0.0f);
//
//        // Run the rendering loop until the user has attempted to close
//        // the window or has pressed the ESCAPE key.
//        while ( !glfwWindowShouldClose(window) ) {
//            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
//            DrawSquare();
//            Update();
////
////            glfwSwapBuffers(window); // swap the color buffers
////
////            // Poll for window events. The key callback above will only be
////            // invoked during this call.
////            glfwPollEvents();
//
//        }
//    }
