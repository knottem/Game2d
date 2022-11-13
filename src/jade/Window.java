package jade;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private final int width;
    private final int height;
    private final String title;
    private long glfwWindow;

    private float r,g,b,a;

    //temp
    private boolean fadeToBlack = false;

    private static Window window = null;

    //FPS
    double previousTime;
    int frameCount;


    private Window(){
        this.width = 1920;
        this.height = 1080;
        this.title = "Test 2D";
        r = 1;
        g = 1;
        b = 1;
        a = 1;
    }

    public static Window get(){
        if( Window.window == null){
            Window.window = new Window();
        }
        return Window.window;
    }

    public void run(){

        System.out.println("Hello LWJGL + " +  Version.getVersion() + "!");

        init();
        loop();

        // Free the memory

        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        //Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();

    }

    public void init(){
        // Setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();

        //Initialize
        if(!glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW.");
        }

        //configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        //START MAXIMIZED
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        //Create the window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if(glfwWindow == NULL){
            throw new IllegalStateException("Failed to create the GLFW window.");
        }

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        // enable v-sync
        glfwSwapInterval(1);

        //Make the window visible
        glfwShowWindow(glfwWindow);


        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

    }

    public void showFps(){
        // Measure speed
        double currentTime = glfwGetTime();
        frameCount++;

        if ( currentTime - previousTime >= 1.0 ) {
            // Display the frame count here any way you want.
            System.out.println(frameCount);
            frameCount = 0;
            previousTime = currentTime;
        }
    }

    public void loop(){

        //FPS
        previousTime = glfwGetTime();
        frameCount = 0;


        while(!glfwWindowShouldClose(glfwWindow)){
            // Poll events
            glfwPollEvents();

            glClearColor(r,g,b,a);
            glClear(GL_COLOR_BUFFER_BIT);

            showFps();

            if(fadeToBlack){
                r = Math.max(r - 0.01f, 0);
                g = Math.max(g - 0.01f, 0);
                b = Math.max(b - 0.01f, 0);
            }

            if(KeyListener.isKeyPressed(GLFW_KEY_SPACE)){
                fadeToBlack = false;
            }
            if(MouseListener.isDragging()){
                System.out.println(MouseListener.getX());
                System.out.println(MouseListener.getY());
            }


            glfwSwapBuffers(glfwWindow);
        }

    }
}
