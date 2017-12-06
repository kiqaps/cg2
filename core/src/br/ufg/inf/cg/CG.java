package br.ufg.inf.cg;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import br.ufg.inf.cg.models.Objeto3D;

public class CG extends ApplicationAdapter {

    int x, y;

    SpriteBatch batch;
    Texture img;
    Pixmap map;
    Objeto3D obj;

	@Override
	public void create () {
        batch = new SpriteBatch();
        map = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Pixmap.Format.RGB888);
        obj = Objeto3D.create(Objeto3D.ObjType.CUBO);
	}

	@Override
	public void render () {
	    map.setColor(new Color(1,1,1,1));
	    map.fill();

        obj.draw(map);

	    img = new Texture(map);
	    batch.begin();
        batch.draw(img, 0, 0);
        batch.end();

        try {
            Thread.sleep((long)(1000/30-Gdx.graphics.getDeltaTime()));
        } catch (InterruptedException e) { }
    }
	
	@Override
	public void dispose () {
	    batch.dispose();
	    img.dispose();
	    map.dispose();
    }
}
