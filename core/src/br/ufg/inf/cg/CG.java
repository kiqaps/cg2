package br.ufg.inf.cg;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import org.omg.CORBA.OBJ_ADAPTER;

import java.awt.Frame;

import br.ufg.inf.cg.models.Objeto3D;

public class CG extends ApplicationAdapter {

    int x, y;

    SpriteBatch batch;
    Texture img;
    Pixmap map;
    Objeto3D obj, obj2;
    FrameRate fps;

	@Override
	public void create () {
        batch = new SpriteBatch();
        map = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Pixmap.Format.RGB888);
        obj =  Objeto3D.create(Objeto3D.ObjType.CUBO);
        obj2 = Objeto3D.create(Objeto3D.ObjType.CUBO);

        obj.Tx = -150;
        obj.Tz = -200;
        obj2.Tx = 150;
        obj2.Tz = 200;
        fps = new FrameRate();
	}

	@Override
	public void render () {
	    map.setColor(new Color(1,1,1,1));
	    map.fill();

        obj.draw(map, new Color(1,0,0,1));
        obj2.draw(map, new Color(0,0,1,1));

	    img = new Texture(map);
	    batch.begin();
        batch.draw(img, 0, 0);
        batch.end();
        img.dispose();

        fps.update();
        fps.render(50, 50);
    }
	
	@Override
	public void dispose () {
	    batch.dispose();
	    img.dispose();
	    map.dispose();
	    fps.dispose();
    }
}
