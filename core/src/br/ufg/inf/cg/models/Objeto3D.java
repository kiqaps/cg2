package br.ufg.inf.cg.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;

import java.util.ArrayList;

import br.ufg.inf.cg.Utils;

/**
 * Created by kiqaps on 06/12/17.
 */

public class Objeto3D {

    public int Tx = 0, Ty = 0, Tz = 0;
    public int Sx = 200, Sy = 200, Sz = 200;
    public int Rx = 0, Ry = 0, Rz = 0;
    public int Fz = 1000;

    private ArrayList<Ponto> pontos = new ArrayList<Ponto>();
    private ArrayList<Ponto> pontos_base = new ArrayList<Ponto>();
    private ArrayList<int[]> linhas = new ArrayList<int[]>();

    public void addPonto(Ponto p)
    {
        pontos_base.add(p);
    }

    public void addLinha(Ponto p1, Ponto p2)
    {
        int pos = pontos_base.size();
        pontos_base.add(p1);
        pontos_base.add(p2);
        int[] linha = new int[2];
        linha[0] = pos;
        linha[1] = pos + 1;
        linhas.add(linha);
    }

    public void addLinha(int p1, int p2)
    {
        int[] linha = new int[2];
        linha[0] = p1;
        linha[1] = p2;
        linhas.add(linha);
    }

    public void draw(Pixmap map)
    {
        map.setColor(new Color(0,0,0,1));
        Utils.linhaDDA(map, new Ponto(Gdx.graphics.getWidth() / 2, 0, 0), new Ponto(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight(), 0));

        map.setColor(new Color(1,0,0,1));

        pontos.clear();
        for (Ponto p: pontos_base)
            pontos.add(new Ponto(p));

        aplicarEscala();
        aplicarRotacao();
        aplicarTranslocacao();
        aplicarProjecao();

        ArrayList<Ponto> esq = new ArrayList<Ponto>(), dir = new ArrayList<Ponto>();

        for (Ponto ponto : pontos)
        {
            ponto.x += Gdx.graphics.getWidth();
            ponto.y = Gdx.graphics.getHeight() - ponto.y;

            ponto.x = ponto.x / ponto.m;
            ponto.y = ponto.y / ponto.m;

            ponto.x = (Gdx.graphics.getWidth() * (ponto.x / (2f * Gdx.graphics.getWidth())));
            ponto.y = (Gdx.graphics.getHeight() * (ponto.y / (2f * Gdx.graphics.getHeight())));

            esq.add(new Ponto(ponto.x - Gdx.graphics.getWidth() / 4, ponto.y, 0));
            dir.add(new Ponto(ponto.x + Gdx.graphics.getWidth() / 4, ponto.y, 0));
        }

        for (int[] linha : linhas)
        {
            Utils.linhaDDA(map, esq.get(linha[0]), esq.get(linha[1]));
            Utils.linhaDDA(map, dir.get(linha[0]), dir.get(linha[1]));
        }
    }

    private void aplicarProjecao() {
        double Fx = Gdx.graphics.getWidth() * 1,
                Fy = Gdx.graphics.getHeight() * -1;

        double[][] mat1 = {
            {1, 0, 0, 0},
            {0, 1, 0, 0},
            {0, 0, 1, 0},
            {-Fx, -Fy, 0, 1}
        };

        double[][] mat2 = {
            {1, 0, 0, 0},
            {0, 1, 0, 0},
            {0, 0, 1, 0},
            {Fx, Fy, 0, 1}
        };

        double[][] _1ptFuga = {
            {1, 0, 0, 0},
            {0, 1, 0, 0},
            {0, 0, 0, -1f / Fz},
            {0, 0 ,0, 1}
        };

        _1ptFuga = Utils.Multiplica(mat1, _1ptFuga);
        _1ptFuga = Utils.Multiplica(_1ptFuga, mat2);

        pontos = Utils.Multiplica(pontos, _1ptFuga);
    }

    private void aplicarTranslocacao() {
        int[][] translocation = {
            {1, 0, 0, 0},
            {0, 1, 0, 0},
            {0, 0, 1, 0},
            {Tx, Ty, Tz, 1}
        };

        pontos = Utils.Multiplica(pontos, translocation);
    }

    private void aplicarRotacao() {

    }

    private void aplicarEscala() {
        int[][] scale = {
            {Sx, 0, 0, 0},
            {0, Sy, 0, 0},
            {0, 0, Sz, 0},
            {0, 0, 0, 1}
        };

        pontos = Utils.Multiplica(pontos, scale);
    }

    public enum ObjType {
        CUBO, PIRAMIDE
    };

    public static Objeto3D create(ObjType type)
    {
        Objeto3D obj = new Objeto3D();
        if (type == ObjType.CUBO)
        {
            obj.addLinha(new Ponto(1, 1, 1), new Ponto(-1, 1, 1));
            obj.addLinha(new Ponto(1, -1, 1), new Ponto(-1, -1, 1));
            obj.addLinha(new Ponto(1, 1, -1), new Ponto(-1, 1, -1));
            obj.addLinha(new Ponto(1, -1, -1), new Ponto(-1, -1, -1));

            obj.addLinha(0, 2);
            obj.addLinha(0, 4);
            obj.addLinha(1, 3);
            obj.addLinha(1, 5);
            obj.addLinha(2, 6);
            obj.addLinha(3, 7);
            obj.addLinha(4, 6);
            obj.addLinha(5, 7);
        }
        return obj;
    }
}
