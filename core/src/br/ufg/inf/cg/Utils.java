package br.ufg.inf.cg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;

import java.util.ArrayList;

import br.ufg.inf.cg.models.Ponto;

public class Utils {

    public static double[][] Multiplica(double[][] mat1, double[][] mat2)
    {
        double[][] ret = new double[mat1.length][mat2[0].length];

        for(int i = 0; i < mat1.length; i++)
        {
            for (int j = 0; j < mat2[0].length; j++)
            {
                double soma = 0;
                for (int k = 0; k < mat2.length; k++)
                    soma += mat1[i][k] * mat2[k][j];
                ret[i][j] = soma;
            }
        }
        return ret;
    }

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

    public static void linhaDDA(Pixmap map, Ponto pi, Ponto pf, int quadrante)
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

        if ((quadrante == 0 && x < Gdx.graphics.getWidth() / 2) || (quadrante == 1 && x > Gdx.graphics.getWidth() / 2))
            map.drawPixel((int) Math.round(x), (int) Math.round(y));
        for (int i = 0; i < passos; i++)
        {
            x += incX;
            y += incY;

            if ((quadrante == 0 && x < Gdx.graphics.getWidth() / 2) || (quadrante == 1 && x > Gdx.graphics.getWidth() / 2))
                map.drawPixel((int) Math.round(x), (int) Math.round(y));
        }
    }
}
