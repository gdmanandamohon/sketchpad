/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author Ananda Mohon Ghosh
 */
public class DataContainer implements java.io.Serializable {

        public int xi, yi, xj, yj = 0;
        public int drawingType;
        public Color color = Color.BLACK;
        public boolean isMulti = false;
        public boolean isSelected = false;
        public int indexNo;
        //flag kept for future work didn't use it.
        public boolean isMultiPoint = false;
        
        public ArrayList<TwoDList> multiList = new ArrayList<>();
        
        public DataContainer() {
        }

        public DataContainer(int a, int b, int c, int d, int t, Color clr, boolean isMulti, int indexNo) {
            xi = a;
            yi = b;
            xj = c;
            yj = d;
            drawingType = t;
            color = clr;
            this.isMulti = isMulti;
            this.indexNo = indexNo;
        }

        public Ipair add(Ipair U, Ipair W) {
            return new Ipair(U.x + W.x, U.y + W.y);
        }

        public Ipair sub(Ipair U, Ipair W) {
            return new Ipair(U.x - W.x, U.y - W.y);
        }

        public Ipair scale(Ipair U, float s) {
            return new Ipair((int) (s * (float) U.x), (int) (s * (float) U.y));
        }

        public int dist(Ipair P, Ipair Q) {
            return (int) Math.sqrt((P.x - Q.x) * (P.x - Q.x) + (P.y - Q.y) * (P.y - Q.y));
        }

        public int dot(Ipair P, Ipair Q) {
            return P.x * Q.x + P.y * Q.y;
        }

        public int segdist(int xp, int yp) { // distance from point to line segment (initial diagonal pair)
            Ipair I = new Ipair(xi, yi);
            Ipair J = new Ipair(xj, yj);
            Ipair P = new Ipair(xp, yp);
            Ipair V, N;
            V = sub(J, I);             // V is the vector from I to J
            int k = dot(V, sub(P, I)); // k is the non-normalized projection from P-I to V
            int L2 = dot(V, V);         // L2 is the length of V, squared
            if (k <= 0) {
                N = I;          // if the projection is negative, I is nearest (N)
            } else if (k >= L2) {
                N = J;   // if the projection too large, J is nearest (N)
            } else {
                N = add(I, scale(V, (float) k / (float) L2)); // otherwise, N is scaled onto V by k/L2
            }
            return dist(P, N);
        }
        
        
        //Multilist CRUD function
        public void addMultiList(int x, int y)
        {
            multiList.add(new TwoDList(x,y));
        }
        public  void setMultilist(ArrayList<TwoDList> multiList){
            this.multiList = multiList;
        }
    }