package fr.paragoumba.mlfg.engine;

public class GameEngine implements Runnable {

    public static final int TARGET_FPS = 75;
    public static final int TARGET_UPS = 30;

    private final Thread gameLoopThread;
    private final Window window;
    private final IGameLogic gameLogic;
    private final Timer timer;
    private final MouseInput mouseInput;

    public GameEngine(String windowTitle, int width, int height, boolean vSync, IGameLogic gameLogic) {

        gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
        window = new Window(windowTitle, width, height, vSync);
        this.gameLogic = gameLogic;
        this.timer = new Timer();
        mouseInput = new MouseInput();

    }

    @Override
    public void run() {

        try {

            init();
            gameLoop();

        } catch (Exception e){

            e.printStackTrace();

        } finally {

            gameLogic.cleanup();

        }
    }

    public void start(){

        if (System.getProperty("os.name").contains("Mac")) gameLoopThread.run();
        else gameLoopThread.start();

    }

    protected void init() throws Exception {

        window.init();
        timer.init();
        gameLogic.init(window);
        mouseInput.init(window);

    }

    protected void gameLoop() throws Exception {

        float elapsedTime;
        float accumulator = 0f;
        float interval = 1f / TARGET_UPS;
        boolean running = true;

        while (running && !window.windowShouldClose()) {

            elapsedTime = timer.getElapsedTime();
            accumulator += elapsedTime;

            input();

            while (accumulator >= interval) {

                update(interval);
                accumulator -= interval;

            }

            render();

            if (!window.isvSync()) sync();

        }
    }

    private void sync (){

        float loopSlot = 1f / TARGET_FPS;
        double endTime = timer.getLastLoopTime() + loopSlot;

        while (timer.getTime() < endTime) {

            try {

                Thread.sleep(1);

            } catch (InterruptedException e) {

                e.printStackTrace();

            }
        }
    }

    protected void input(){

        gameLogic.input(window, mouseInput);
        mouseInput.input(window);

    }

    protected void update(float interval){

        gameLogic.update(interval, mouseInput);

    }

    protected void render() throws Exception {

        gameLogic.render(window);
        window.update();

    }
}
