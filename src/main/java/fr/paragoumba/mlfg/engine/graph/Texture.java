package fr.paragoumba.mlfg.engine.graph;

import de.matthiasmann.twl.utils.PNGDecoder;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class Texture {

    private final int id;
    private final int width;
    private final int height;

    public Texture(String fileName) throws Exception {

        // Load Texture file
        PNGDecoder decoder = new PNGDecoder(Texture.class.getResourceAsStream(fileName));

        this.width = decoder.getWidth();
        this.height = decoder.getHeight();

        // Load texture contents into a byte buffer
        ByteBuffer buf = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
        decoder.decode(buf, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
        buf.flip();

        // Create a new OpenGL texture
        int textureId = glGenTextures();

        // Bind the texture
        glBindTexture(GL_TEXTURE_2D, textureId);

        // Tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte size
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        // Upload the texture data
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);

        // Generate Mip Map
        glGenerateMipmap(GL_TEXTURE_2D);

        this.id = textureId;

    }

    public void bind(){

        glBindTexture(GL_TEXTURE_2D, id);

    }

    public int getWidth() {

        return this.width;

    }

    public int getHeight() {

        return this.height;

    }

    public int getId(){

        return id;

    }

    public void cleanup(){

        glDeleteTextures(id);

    }
}
