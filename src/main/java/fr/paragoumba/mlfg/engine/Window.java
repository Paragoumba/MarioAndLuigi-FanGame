package fr.paragoumba.mlfg.engine;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private String title;
    private int width;
    private int height;
    private boolean vSync;
    private boolean resized;
    private long windowHandle;

    public Window(String title, int width, int height, boolean vSync){

        this.title = title;
        this.width = width;
        this.height = height;
        this.vSync = vSync;
        this.resized= false;

    }

    public void init(){

        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW.");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);

        if (windowHandle == NULL) throw new RuntimeException("Failed to create the GLFW window.");

        glfwSetFramebufferSizeCallback(windowHandle, (window, width, height) -> {

            this.width = width;
            this.height = height;
            this.setResized(true);

        });

        glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
            }
        });

        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        glfwSetWindowPos(
                windowHandle,
                (vidMode.width() - width) / 2,
                (vidMode.height() - height) / 2
        );

        glfwMakeContextCurrent(windowHandle);

        if (vSync) {

            glfwSwapInterval(1);

        }

        glfwShowWindow(windowHandle);
        GL.createCapabilities();
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glEnable(GL_DEPTH_TEST);
        //glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

        // Support for transparency
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

    }

    public void setClearColor(float r, float g, float b, float a){

        glClearColor(r, g, b, a);

    }

    public boolean isKeyPressed(int keyCode) {

        return glfwGetKey(windowHandle, keyCode) == GLFW_PRESS;

    }

    public boolean windowShouldClose(){

        return glfwWindowShouldClose(windowHandle);

    }

    public long getWindowHandle() {

        return windowHandle;

    }

    public String getTitle() {

        return title;

    }

    public int getWidth() {

        return width;

    }

    public int getHeight() {

        return height;

    }

    public boolean isResized() {

        return resized;

    }

    public void setResized(boolean resized) {

        this.resized = resized;

    }

    public boolean isvSync() {

        return vSync;

    }

    public void setvSync(boolean vSync) {

        this.vSync = vSync;

    }

    public void update(){

        glfwSwapBuffers(windowHandle);
        glfwPollEvents();

    }
}
