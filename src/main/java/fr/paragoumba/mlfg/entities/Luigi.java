package fr.paragoumba.mlfg.entities;

import fr.paragoumba.mlfg.engine.graph.Texture;

public class Luigi extends Entity {

    public static final int FRONT_STAY = registerTextureIndex(new int[]{1, 0});
    public static final int FRONT_WALK_1 = registerTextureIndex(new int[]{0, 0});
    public static final int FRONT_WALK_2 = registerTextureIndex(new int[]{2, 0});

    public static final int LEFT_STAY = registerTextureIndex(new int[]{1, 1});
    public static final int LEFT_WALK_1 = registerTextureIndex(new int[]{0, 1});
    public static final int LEFT_WALK_2 = registerTextureIndex(new int[]{2, 1});

    public static final int BACK_STAY = registerTextureIndex(new int[]{1, 3});
    public static final int BACK_WALK_1 = registerTextureIndex(new int[]{0, 3});
    public static final int BACK_WALK_2 = registerTextureIndex(new int[]{2, 3});

    public static final int RIGHT_STAY = registerTextureIndex(new int[]{1, 2});
    public static final int RIGHT_WALK_1 = registerTextureIndex(new int[]{0, 2});
    public static final int RIGHT_WALK_2 = registerTextureIndex(new int[]{2, 2});

    public Luigi(int width, int height) {

        super(width, height);

        try {

            texture = new Texture("/textures/luigi.png");

        } catch (Exception e) {

            e.printStackTrace();

        }
    }
}
