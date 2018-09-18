package fr.paragoumba.mlfg.entities;

import fr.paragoumba.mlfg.engine.graph.Texture;

import java.util.Arrays;

abstract class Entity {

    public static int COL_NUMBER = 12;
    public static int ROW_NUMBER = 8;

    Entity(int width, int height){

        this.width = width;
        this.height = height;

        /* TODO Texture loading */
        //this.texture = texture;

    }

    int width;
    int height;
    Texture texture;
    static int[][] indices;

    public float[] getTexCoords(int sprite){

        float xStep = 1f / COL_NUMBER;
        float yStep = 1f / ROW_NUMBER;
        int[] index = indices[sprite];

        float[] array = new float[]{
                 index[0]      * xStep,  index[1]      * yStep,
                (index[0] + 1) * xStep,  index[1]      * yStep,
                (index[0] + 1) * xStep, (index[1] + 1) * yStep,
                 index[0]      * xStep, (index[1] + 1) * yStep
        };

        System.out.println(Arrays.toString(array));

        return array;

    }

    static int registerTextureIndex(int[] index){

        if (indices == null) indices = new int[0][0];

        for (int i = 0; i < indices.length; ++i) if (indices[i] == index) return i;

        indices = Arrays.copyOf(indices, indices.length + 1);
        indices[indices.length - 1] = index;

        return indices.length - 1;

    }
}
