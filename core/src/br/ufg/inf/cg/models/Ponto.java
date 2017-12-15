package br.ufg.inf.cg.models;



public class Ponto {
    public double x, y, z, m;

    public Ponto(Ponto ponto)
    {
        this.x = ponto.x;
        this.y = ponto.y;
        this.z = ponto.z;
        this.m = ponto.m;
    }

    public Ponto(double x, double y, double z, double m)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.m = m;
    }

    public Ponto(double x, double y, double z)
    {
        this(x, y, z, 1);
    }

    public double get(int pos)
    {
        if (pos == 0) return x;
        else if (pos == 1) return y;
        else if (pos == 2) return z;
        else if (pos == 3) return m;
        return -1;
    }

    public void set(int pos, double value)
    {
        if (pos == 0)
            x = value;
        else if (pos == 1)
            y = value;
        else if (pos == 2)
            z = value;
        else if (pos == 3)
            m = value;
    }
}
