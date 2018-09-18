package fr.paragoumba.mlfg.game;

import fr.paragoumba.mlfg.engine.*;
import fr.paragoumba.mlfg.engine.graph.*;
import fr.paragoumba.mlfg.entities.Luigi;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.*;

public class Game implements IGameLogic {

    private static final float MOUSE_SENSITIVITY = 0.2f;
    private static final float CAMERA_POS_STEP = 0.05f;

    private final Vector3f cameraInc;
    private final Renderer renderer;
    private final Camera camera;

    private SceneLight sceneLight;
    private Hud hud;

    private GameItem[] gameItems;
    private float lightAngle;

    private float spotAngle = 0;
    private float spotInc = 1;

    private boolean wWasPressed = false;
    private boolean aWasPressed = false;
    private boolean sWasPressed = false;
    private boolean dWasPressed = false;

    public Game(){

        camera = new Camera();
        renderer = new Renderer();
        cameraInc = new Vector3f();

    }

    @Override
    public void init(Window window) throws Exception {

        renderer.init(window);

        float reflectance = 1f;

        Mesh ground = new Mesh(
                new float[]{
                        -3, 0, 3,
                        3, 0, 3,
                        3, 0, -3,
                        -3, 0, -3
                        }, new float[0], new float[0],
                new int[]{
                        0, 1, 3,
                        1, 2, 3
                });
        Material groundMaterial = new Material(new Vector4f(0, 1, 0, 1), reflectance);

        ground.setMaterial(groundMaterial);

        GameItem groundItem = new GameItem(ground);

        groundItem.setPosition(0, -2, -9);

        Mesh bunny = OBJLoader.loadMesh("/models/bunny.obj");
        Material bunnyMaterial = new Material(new Vector4f(1, 1, 1, 1), reflectance);

        bunny.setMaterial(bunnyMaterial);

        GameItem bunnyItem = new GameItem(bunny);

        bunnyItem.setScale(0.5f);
        bunnyItem.setPosition(-5, 0, -9);

        Mesh quadMesh = new Mesh(
                new float[]{
                        -1.5f, 1, 0,
                        1.5f, 1, 0,
                        1.5f, -1, 0,
                        -1.5f, -1, 0
                }, new float[0], new float[0],
                new int[]{
                        0, 1, 3,
                        1, 2, 3
                });
        Material quadMaterial = new Material(new Vector4f(1, 0, 0, 1), reflectance);

        quadMesh.setMaterial(quadMaterial);

        GameItem quad = new GameItem(quadMesh);

        quad.setPosition(0, 0, -9);
        quad.setScale(2f);
        
        Mesh marioMesh = new Mesh(
                new float[]{
                        -0.5f, 0.5f, 0,
                        0.5f, 0.5f, 0,
                        0.5f, -0.5f, 0,
                        -0.5f, -0.5f, 0
                }, new float[]{
                        0.083f, 0,
                        0.166f, 0,
                        0.166f, 0.125f,
                        0.083f, 0.125f
                }, new float[]{}, new int[]{
                        0, 1, 3,
                        1, 2, 3
                });
        Texture marioTexture = new Texture("/textures/mario.png");
        Material marioMaterial = new Material(marioTexture, reflectance);

        marioMesh.setMaterial(marioMaterial);

        GameItem mario = new GameItem(marioMesh);

        mario.setPosition(-1, -1, -8);
        mario.setScale(2f);

        /*Mesh luigiMesh = new Mesh(
                new float[]{
                        -0.5f, 0.5f, 0,
                        0.5f, 0.5f, 0,
                        0.5f, -0.5f, 0,
                        -0.5f, -0.5f, 0
                }, new float[]{
                        0.083f, 0,
                        0.166f, 0,
                        0.166f, 0.125f,
                        0.083f, 0.125f
                }, new float[]{}, new int[]{
                        0, 1, 3,
                        1, 2, 3
                });*/

        Luigi luigiObject = new Luigi(0, 0);

        Mesh luigiMesh = new Mesh(
                new float[]{
                        -0.5f, 0.5f, 0,
                        0.5f, 0.5f, 0,
                        0.5f, -0.5f, 0,
                        -0.5f, -0.5f, 0
                }, luigiObject.getTexCoords(Luigi.FRONT_STAY), new float[]{},
                new int[]{
                        0, 1, 3,
                        1, 2, 3
                });

        Texture luigiTexture = new Texture("/textures/luigi.png");
        Material luigiMaterial = new Material(luigiTexture, reflectance);

        luigiMesh.setMaterial(luigiMaterial);

        GameItem luigi = new GameItem(luigiMesh);

        luigi.setPosition(1, -1, -8);
        luigi.setScale(2f);
        
        gameItems = new GameItem[]{groundItem, quad, bunnyItem, mario, luigi};

        sceneLight = new SceneLight();
        sceneLight.setAmbientLight(new Vector3f(0.3f, 0.3f, 0.3f));

        Vector3f lightColour = new Vector3f(1, 1, 1);
        Vector3f lightPosition = new Vector3f(0, 0, 1);
        float lightIntensity = 1.0f;
        PointLight pointLight = new PointLight(lightColour, lightPosition, lightIntensity);

        sceneLight.setPointLightList(new PointLight[]{pointLight});

        PointLight.Attenuation att = new PointLight.Attenuation(0.0f, 0.0f, 1.0f);
        pointLight.setAttenuation(att);

        SpotLight spotLight = new SpotLight(pointLight, new Vector3f(), 15);

        sceneLight.setSpotLightList(new SpotLight[]{spotLight});

        lightPosition = new Vector3f(-1, 0, 0);
        lightColour = new Vector3f(1, 1, 1);
        sceneLight.setDirectionalLight(new DirectionalLight(lightColour, lightPosition, lightIntensity));

    }

    @Override
    public void input(Window window, MouseInput mouseInput) {

        cameraInc.set(0, 0, 0);

        GameItem mario = gameItems[3];
        Mesh marioMesh = mario.getMesh();

        GameItem luigi = gameItems[4];
        Mesh luigiMesh = luigi.getMesh();

        Vector3f marioPos = mario.getPosition();
        Vector3f luigiPos = luigi.getPosition();

        boolean timeIsPair = System.currentTimeMillis() % 1000 / 100 < 5;

        if (window.isKeyPressed(GLFW_KEY_W)) {

            //cameraInc.z = -1;
            marioMesh.updateTextCoords(timeIsPair ? new float[]{
                    0, 0.375f,
                    0.083f, 0.375f,
                    0.083f, 0.500f,
                    0, 0.500f
            } : new float[]{
                    0.166f, 0.375f,
                    0.249f, 0.375f,
                    0.249f, 0.500f,
                    0.166f, 0.500f
            });

            luigiMesh.updateTextCoords(timeIsPair ? new float[]{
                    0, 0.375f,
                    0.083f, 0.375f,
                    0.083f, 0.500f,
                    0, 0.500f
            } : new float[]{
                    0.166f, 0.375f,
                    0.249f, 0.375f,
                    0.249f, 0.500f,
                    0.166f, 0.500f
            });

            marioPos.z -= 0.1f;
            luigiPos.z -= 0.1f;

            wWasPressed = true;

        } else if (wWasPressed) {

            marioMesh.updateTextCoords(new float[]{
                    0.083f, 0.375f,
                    0.166f, 0.375f,
                    0.166f, 0.500f,
                    0.083f, 0.500f
            });

            luigiMesh.updateTextCoords(new float[]{
                    0.083f, 0.375f,
                    0.166f, 0.375f,
                    0.166f, 0.500f,
                    0.083f, 0.500f
            });

            wWasPressed = false;

        }

        if (window.isKeyPressed(GLFW_KEY_S)) {

            //cameraInc.z = 1;
            marioMesh.updateTextCoords(timeIsPair ? new float[]{
                    0, 0,
                    0.083f, 0,
                    0.083f, 0.125f,
                    0, 0.125f
            } : new float[]{
                    0.166f, 0,
                    0.249f, 0,
                    0.249f, 0.125f,
                    0.166f, 0.125f
            });

            luigiMesh.updateTextCoords(timeIsPair ? new float[]{
                    0, 0,
                    0.083f, 0,
                    0.083f, 0.125f,
                    0, 0.125f
            } : new float[]{
                    0.166f, 0,
                    0.249f, 0,
                    0.249f, 0.125f,
                    0.166f, 0.125f
            });

            marioPos.z += 0.1f;
            luigiPos.z += 0.1f;

            sWasPressed = true;

        } else if (sWasPressed) {

            marioMesh.updateTextCoords(new float[]{
                    0.083f, 0,
                    0.166f, 0,
                    0.166f, 0.125f,
                    0.083f, 0.125f
            });

            luigiMesh.updateTextCoords(new float[]{
                    0.083f, 0,
                    0.166f, 0,
                    0.166f, 0.125f,
                    0.083f, 0.125f
            });

            sWasPressed = false;

        }

        if (window.isKeyPressed(GLFW_KEY_A)) {

            //cameraInc.x = -1;
            marioMesh.updateTextCoords(timeIsPair ? new float[]{
                    0, 0.125f,
                    0.083f, 0.125f,
                    0.083f, 0.250f,
                    0, 0.250f
            } : new float[]{
                    0.166f, 0.125f,
                    0.249f, 0.125f,
                    0.249f, 0.250f,
                    0.166f, 0.250f
            });

            luigiMesh.updateTextCoords(timeIsPair ? new float[]{
                    0, 0.125f,
                    0.083f, 0.125f,
                    0.083f, 0.250f,
                    0, 0.250f
            } : new float[]{
                    0.166f, 0.125f,
                    0.249f, 0.125f,
                    0.249f, 0.250f,
                    0.166f, 0.250f
            });

            marioPos.x -= 0.1f;
            luigiPos.x -= 0.1f;

            aWasPressed = true;

        } else if (aWasPressed) {

            marioMesh.updateTextCoords(new float[]{
                    0.083f, 0.125f,
                    0.166f, 0.125f,
                    0.166f, 0.250f,
                    0.083f, 0.250f
            });

            luigiMesh.updateTextCoords(new float[]{
                    0.083f, 0.125f,
                    0.166f, 0.125f,
                    0.166f, 0.250f,
                    0.083f, 0.250f
            });

            aWasPressed = false;

        }

        if (window.isKeyPressed(GLFW_KEY_D)) {

            //cameraInc.x = 1;
            marioMesh.updateTextCoords(timeIsPair ? new float[]{
                    0, 0.250f,
                    0.083f, 0.250f,
                    0.083f, 0.375f,
                    0, 0.375f
            } : new float[]{
                    0.166f, 0.250f,
                    0.249f, 0.250f,
                    0.249f, 0.375f,
                    0.166f, 0.375f
            });

            luigiMesh.updateTextCoords(timeIsPair ? new float[]{
                    0, 0.250f,
                    0.083f, 0.250f,
                    0.083f, 0.375f,
                    0, 0.375f
            } : new float[]{
                    0.166f, 0.250f,
                    0.249f, 0.250f,
                    0.249f, 0.375f,
                    0.166f, 0.375f
            });

            marioPos.x += 0.1f;
            luigiPos.x += 0.1f;

            dWasPressed = true;

        } else if (dWasPressed) {

            marioMesh.updateTextCoords(new float[]{
                    0.083f, 0.250f,
                    0.166f, 0.250f,
                    0.166f, 0.375f,
                    0.083f, 0.375f
            });

            luigiMesh.updateTextCoords(new float[]{
                    0.083f, 0.250f,
                    0.166f, 0.250f,
                    0.166f, 0.375f,
                    0.083f, 0.375f
            });

            dWasPressed = false;

        }

        /*if (window.isKeyPressed(GLFW_KEY_UP)){

            gameItems[3].setPosition((float) (gameItems[3].getPosition().x - 0.1 * Math.cos(gameItems[3].getRotation().x)), gameItems[3].getPosition().y, (float) (gameItems[3].getPosition().z - Math.sin(0.1)));
            lightPosition.x -= 0.1;

        } else if (window.isKeyPressed(GLFW_KEY_RIGHT)){

            gameItems[3].setRotation(gameItems[3].getRotation().x, gameItems[3].getRotation().y + 0.5f, gameItems[3].getRotation().z);
            lightPosition.z -= 0.1;

        } else if (window.isKeyPressed(GLFW_KEY_DOWN)){

            gameItems[3].setPosition((float) (gameItems[3].getPosition().x + 0.1 * Math.cos(gameItems[3].getRotation().x)), gameItems[3].getPosition().y, (float) (gameItems[3].getPosition().z - Math.sin(0.1)));
            lightPosition.x += 0.1;

        } else if (window.isKeyPressed(GLFW_KEY_LEFT)){

            gameItems[3].setRotation(gameItems[3].getRotation().x, gameItems[3].getRotation().y - 0.5f, gameItems[3].getRotation().z);
            lightPosition.z += 0.1;

        }*/

        if (window.isKeyPressed(GLFW_KEY_Z)) {

            cameraInc.y = -1;

        } else if (window.isKeyPressed(GLFW_KEY_X)) {

            cameraInc.y = 1;

        }

        SpotLight[] spotLightList = sceneLight.getSpotLightList();
        float lightPos = spotLightList[0].getPointLight().getPosition().z;

        if (window.isKeyPressed(GLFW_KEY_N)) {

            spotLightList[0].getPointLight().getPosition().z = lightPos + 0.1f;

        } else if (window.isKeyPressed(GLFW_KEY_M)) {

            spotLightList[0].getPointLight().getPosition().z = lightPos - 0.1f;

        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {

        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);

        if (mouseInput.isRightButtonPressed()) {

            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);

        }

        double spotAngleRad = Math.toRadians(spotAngle);
        SpotLight[] spotLightList = sceneLight.getSpotLightList();
        Vector3f coneDir = spotLightList[0].getConeDirection();
        coneDir.y = (float) Math.sin(spotAngleRad);

        DirectionalLight directionalLight = sceneLight.getDirectionalLight();
        lightAngle += 1.1f;

        if (lightAngle > 90) {

            directionalLight.setIntensity(0);

            if (lightAngle >= 360) {

                lightAngle = -90;

            }

        } else if (lightAngle <= -80 || lightAngle >= 80) {

            float factor = 1 - (Math.abs(lightAngle) - 80) / 10.0f;

            directionalLight.setIntensity(factor);

            directionalLight.getColor().y = Math.max(factor, 0.9f);
            directionalLight.getColor().z = Math.max(factor, 0.5f);

        } else {


            directionalLight.setIntensity(1);
            directionalLight.getColor().x = 1;
            directionalLight.getColor().y = 1;
            directionalLight.getColor().z = 1;

        }

        double angRad = Math.toRadians(lightAngle);
        directionalLight.getDirection().x = (float) Math.sin(angRad);
        directionalLight.getDirection().y = (float) Math.cos(angRad);

    }

    @Override
    public void render(Window window) {

        renderer.render(window, camera, gameItems, sceneLight, hud);

    }

    @Override
    public void cleanup() {

        renderer.cleanup();

        for (GameItem gameItem : gameItems) gameItem.getMesh().cleanup();

    }
}
