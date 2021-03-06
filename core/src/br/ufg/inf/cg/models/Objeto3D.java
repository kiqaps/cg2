package br.ufg.inf.cg.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;

import java.util.ArrayList;
import java.util.Locale;

import br.ufg.inf.cg.Utils;

public class Objeto3D {

    public int dist = 50;
    public double Tx = 0, Ty = 0, Tz = 0;
    public double Sx = 100, Sy = 100, Sz = 100;
    public double Rx = 0, Ry = 0, Rz = 0;
    public double Fz = 1000 ;
    public double baseRRx, baseRRy, baseRRz, oldRRx, oldRRy, oldRRz;

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

    public void draw(Pixmap map, Color color)
    {
        map.setColor(new Color(0,0,0,1));
        Utils.linhaDDA(map, new Ponto(Gdx.graphics.getWidth() / 2, 0, 0), new Ponto(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight(), 0));

        map.setColor(color);

        pontos.clear();
        for (Ponto p: pontos_base)
            pontos.add(new Ponto(p));

        aplicarEscala();
        aplicarTranslocacao();

        aplicarRotacao();
        aplicarRotacaoUniversal();

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

            esq.add(new Ponto(ponto.x - Gdx.graphics.getWidth() / 4 + dist, ponto.y, 0));
            dir.add(new Ponto(ponto.x + Gdx.graphics.getWidth() / 4 - dist, ponto.y, 0));
        }

        for (int[] linha : linhas)
        {
            Utils.linhaDDA(map, esq.get(linha[0]), esq.get(linha[1]), 0);
            Utils.linhaDDA(map, dir.get(linha[0]), dir.get(linha[1]), 1);
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
        double[][] translocation = {
            {1, 0, 0, 0},
            {0, 1, 0, 0},
            {0, 0, 1, 0},
            {Tx, Ty, Tz, 1}
        };

        pontos = Utils.Multiplica(pontos, translocation);
    }

    private void aplicarRotacaoUniversal()
    {
        float[] mat = new float[4 * 4];
        Gdx.input.getRotationMatrix(mat);
        Matrix4 m = new Matrix4(mat);
        Quaternion q = m.getRotation(new Quaternion());

        double gyroX = Gdx.input.getGyroscopeX();
        double gyroY = Gdx.input.getGyroscopeY();
        double gyroZ = Gdx.input.getGyroscopeZ();
        double RRx = oldRRx, RRy = oldRRy,RRz = oldRRz;

        RRx += gyroX * 0.009;
        RRy += gyroY * 0.02;

        if(Double.compare(Math.abs(gyroZ * 0.023), 0.5) < 0)
            RRz += gyroZ * 0.03;



        Gdx.app.log("tag", String.format("old = %.2f, RRz: %.2f , delta = %.2f",oldRRz ,RRz, Math.abs(oldRRz - RRz)));

        double[][] RotZ = {
                {Math.cos(-RRz), Math.sin(-RRz), 0, 0},
                {-Math.sin(-RRz), Math.cos(-RRz), 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };
        oldRRz = RRz;
        pontos = Utils.Multiplica(pontos, RotZ);

        double TTx =  Math.round((Gdx.graphics.getWidth() * RRx) / (Math.PI/2) ) ;
        double[][] matRRx = {
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {TTx, 0, 0, 1}
        };
        oldRRx = RRx;
        pontos = Utils.Multiplica(pontos, matRRx);

        double TTy =  Math.round((Gdx.graphics.getHeight() * RRy) / (Math.PI/2));
        double[][] matRRy = {
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, TTy, 0, 1}
        };
        oldRRy = RRy;
        pontos = Utils.Multiplica(pontos, matRRy);

    }

    private void aplicarRotacao()
    {
        double[][] RotZ = {
                {Math.cos(Rz), Math.sin(Rz), 0, 0},
                {-Math.sin(Rz), Math.cos(Rz), 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };
        pontos = Utils.Multiplica(pontos, RotZ);

        double[][] RotX = {
                {1, 0, 0, 0},
                {0, Math.cos(Rx), Math.sin(Rx), 0},
                {0, -Math.sin(Rx), Math.cos(Rx), 0},
                {0, 0, 0, 1}
        };
        pontos = Utils.Multiplica(pontos, RotX);

        double[][] RotY = {
                {Math.cos(Ry), 0, -Math.sin(Ry), 0},
                {0, 1, 0, 0},
                {Math.sin(Ry), 0, Math.cos(Ry), 0},
                {0, 0, 0, 1}
        };
        pontos = Utils.Multiplica(pontos, RotY);

    }

    private void aplicarEscala() {
        double[][] scale = {
            {Sx, 0, 0, 0},
            {0, Sy, 0, 0},
            {0, 0, Sz, 0},
            {0, 0, 0, 1}
        };

        pontos = Utils.Multiplica(pontos, scale);
    }

    public enum ObjType {
        CUBO, ESTRELA
    };

    public static Objeto3D create(ObjType type)
    {
        Objeto3D obj = new Objeto3D();

        float[] mat = new float[4 * 4];
        Gdx.input.getRotationMatrix(mat);
        Matrix4 m = new Matrix4(mat);
        Quaternion q = m.getRotation(new Quaternion());

        obj.baseRRz = 0;
        obj.baseRRy = 0 ;
        obj.baseRRx = 0;

        obj.oldRRx = 0;
        obj.oldRRy = 0;
        obj.oldRRz = 0;

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

        else if (type == ObjType.ESTRELA)
        {
            obj.addLinha(new Ponto(0, 0, 2), new Ponto(0.5, 0, 0.5));
            obj.addLinha(new Ponto(2, 0, 0), new Ponto(0.5, 0, -0.5));
            obj.addLinha(new Ponto(0, 0, -2), new Ponto(-0.5, 0, -0.5));
            obj.addLinha(new Ponto(-2, 0, 0), new Ponto(-0.5, 0, 0.5));
            obj.addPonto(new Ponto(0, 1, 0));
            obj.addPonto(new Ponto(0, -1, 0));

            obj.addLinha(1, 2);
            obj.addLinha(3, 4);
            obj.addLinha(5, 6);
            obj.addLinha(7,0);

            for (int i = 0; i <= 7; i++) {
                obj.addLinha(8, i);
                obj.addLinha(9, i);
            }
        }
        return obj;
    }
}
