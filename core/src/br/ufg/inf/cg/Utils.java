package br.ufg.inf.cg;

import com.badlogic.gdx.graphics.Pixmap;

import java.util.ArrayList;

import br.ufg.inf.cg.models.Ponto;

/**
 * Created by kiqaps on 06/12/17.
 */

public class Utils {

    public static ArrayList<Ponto> Multiplica(ArrayList<Ponto> pontos, int[][] mat)
    {
        ArrayList<Ponto> ret = new ArrayList<Ponto>();
        for (Ponto p : pontos)
            ret.add(new Ponto(p));

        for (int i = 0; i < pontos.size(); i++)
        {
            for (int j = 0; j < 4; j++)
            {
                double soma = 0;
                for (int k = 0; k < 4; k++)
                    soma += pontos.get(i).get(k) * mat[k][j];
                ret.get(i).set(j, soma);
            }
        }
        return ret;
    }

    public static ArrayList<Ponto> Multiplica(ArrayList<Ponto> pontos, double[][] mat)
    {
        ArrayList<Ponto> ret = new ArrayList<Ponto>();
        for (Ponto p : pontos)
            ret.add(new Ponto(p));

        for (int i = 0; i < pontos.size(); i++)
        {
            for (int j = 0; j < 4; j++)
            {
                double soma = 0;
                for (int k = 0; k < 4; k++)
                    soma += pontos.get(i).get(k) * mat[k][j];
                ret.get(i).set(j, soma);
            }
        }
        return ret;
    }

    public static void linhaDDA(Pixmap map, Ponto pi, Ponto pf)
    {
        int passos,
                dx = (int) Math.round(pf.x - pi.x),
                dy = (int) Math.round(pf.y - pi.y);

        double incX, incY, x = pi.x, y = pi.y;

        if (Math.abs(dx) > Math.abs(dy))
            passos = Math.abs(dx);
        else
            passos = Math.abs(dy);

        incX = dx / (double) passos;
        incY = dy / (double) passos;

        map.drawPixel((int) Math.round(x), (int) Math.round(y));
        for (int i = 0; i < passos; i++)
        {
            x += incX;
            y += incY;
            map.drawPixel((int) Math.round(x), (int) Math.round(y));
        }
    }
}
