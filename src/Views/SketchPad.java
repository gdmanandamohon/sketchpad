/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;

import Controllers.UtilityFunction;
import Models.DataContainer;
import Models.TwoDList;
import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Ananda Mohon Ghosh
 */
public class SketchPad extends Frame {

    ArrayList<DataContainer> objs = new ArrayList<>();
    ArrayList<DataContainer> reDoObjs = new ArrayList<>();

    int x0, y0, type, select = 0, indexNo=0, dmin = 9999999;
    ArrayList<Integer> groupIndexes = new ArrayList<>();
    Color clr = Color.BLACK;
    boolean isGroupSelected = false;
    //Move Paramenters
    int moveSelectObjectType = -1, moveSelectedIndexNo = -1, tX = 0, tY = 0, fX = -1, fY = -1;
    DataContainer closest = new DataContainer();

    UtilityFunction utF = new UtilityFunction();

    SketchPad() {

        setSize(850, 600);
        setBackground(Color.LIGHT_GRAY);
        setLayout(new FlowLayout());
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int response = JOptionPane.showConfirmDialog(SketchPad.this, "Really Quit?");
                if (response == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        Button saveButon = new Button("Save");
        Button openButon = new Button("Open");
        Button unDo = new Button("Undo");
        Button reDo = new Button("Redo");
        Button selectMove = new Button("Move");
        Button cutButton = new Button("Cut");
        Button pasteButton = new Button("Paste");

        Button lineButton = new Button("Line");
        Button rectButton = new Button("Rectangle");
        Button elipseButton = new Button("Ellipse");
        Button scribbleButton = new Button("Scribble");
        Button circleButton = new Button("Circle");
        Button squareButton = new Button("Square");
        Button openPolygonButton = new Button("Open Polygon");
        Button closePolygonButton = new Button("Closed Polygon");

        Button groupButton = new Button("Group");
        Button ungroupButton = new Button("Ungroup");

        add(openButon);
        add(saveButon);
        add(unDo);
        add(reDo);
        
        add(selectMove);
        add(cutButton);
        add(pasteButton);

        add(groupButton);
        add(ungroupButton);
        
        add(lineButton);
        add(rectButton);
        add(elipseButton);
        add(circleButton);
        add(scribbleButton);
        add(squareButton);
        add(openPolygonButton);
        add(closePolygonButton);

        

        Button clrButton = new Button("");
        clrButton.setBackground(clr);
        add(clrButton);

        clrButton.addActionListener(e -> {
            Color color = JColorChooser.showDialog(SketchPad.this, "Select Drawing Color", clr);
            if (color != null) {
                clr = color;
                repaint();
            }
        });

        openButon.addActionListener(e -> {
            try {

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                int result = fileChooser.showOpenDialog(this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    //System.out.println("Selected file: " + selectedFile.getAbsolutePath());

                    FileInputStream fileIn = new FileInputStream(selectedFile.getAbsolutePath());
                    ObjectInputStream in = new ObjectInputStream(fileIn);
                    objs = (ArrayList<DataContainer>) in.readObject();
                    repaint();
                }
            } catch (IOException i) {
                JOptionPane.showMessageDialog(null, "No Such Partial Sketch was Found!", "Failed", 0, null);
                return;
            } catch (ClassNotFoundException c) {
                JOptionPane.showMessageDialog(null, "No Such Partial Sketch was Found!", "Failed", 0, null);
                return;
            }
        });

        saveButon.addActionListener(e -> {
            try {

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                int result = fileChooser.showOpenDialog(this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    System.out.println(selectedFile);

                    FileOutputStream fileOut = new FileOutputStream(selectedFile);
                    //System.out.println("Selected file: " + fileOut);
                    ObjectOutputStream out = new ObjectOutputStream(fileOut);
                    out.writeObject(objs);
                    out.close();
                    fileOut.close();
                    repaint();
                }
                return;
            } catch (IOException i) {
                JOptionPane.showMessageDialog(null, "Something Went wrong!", "Failed", 0, null);
            }
        });

        /* Undo Redo */
        unDo.addActionListener(e -> {
            if (!objs.isEmpty()) {
                reDoObjs.add(objs.get(objs.size() - 1));
                objs.remove(objs.size() - 1);
            }
            repaint();
        });

        reDo.addActionListener(e -> {
            if (!reDoObjs.isEmpty()) {
                objs.add(reDoObjs.get(reDoObjs.size() - 1));
                reDoObjs.remove(reDoObjs.size() - 1);
            }
            repaint();
        });
        /* Undo Redo Ends*/

 /* Button to Add Actionlisteners */
        objs.add(new DataContainer(0, 0, 0, 0, 0, clr, false,indexNo++));

        lineButton.addActionListener(e -> {
            type = 0;
        });
        rectButton.addActionListener(e -> {
            type = 1;
        });
        elipseButton.addActionListener(e -> {
            type = 2;
        });
        circleButton.addActionListener(e -> {
            type = 3;
        });
        scribbleButton.addActionListener(e -> {
            type = 4;
        });
        squareButton.addActionListener(e -> {
            type = 5;
        });
        openPolygonButton.addActionListener(e -> {
            type = 6;
        });
        closePolygonButton.addActionListener(e -> {
            type = 7;
        });
        groupButton.addActionListener(e -> {
            isGroupSelected = true;
            type = 13;
            select = 1;
            groupIndexes.clear();
        });
        ungroupButton.addActionListener(e -> {
            isGroupSelected = false;
            select = 0;
            groupIndexes.clear();
            repaint();
        });

        //Move Item
        selectMove.addActionListener(e -> {
            select = 1;
            closest.isSelected = false;
            type = 10;
            repaint();
        });
        cutButton.addActionListener(e -> {
            type = 11;
            select = 1;
            closest.isSelected = false;
        });
        pasteButton.addActionListener(e -> {
            type = 12;
            select = 0;
            closest.isSelected = true;
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (type == 6 || type == 7) {
                    // For the open and closed polygon only
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        ArrayList<TwoDList> multiList = objs.get(objs.size() - 1).multiList;
                        multiList.add(new TwoDList(e.getX(), e.getY()));
                        objs.get(objs.size() - 1).setMultilist(multiList);
                    }
                    if (SwingUtilities.isRightMouseButton(e)) {
                        objs.get(objs.size() - 1).xj = e.getX();
                        objs.get(objs.size() - 1).yj = e.getY();
                        objs.add(new DataContainer(0, 0, 0, 0, 0, clr, false,indexNo++));
                        select = 0;
                    }
                } else if (type == 10) { //Move Drag - Drop
                    moveObject(e.getX(), e.getY(), moveSelectedIndexNo);
                } else if (type == 11) {
                    if (!objs.isEmpty()) {
                        reDoObjs.add(objs.get(moveSelectedIndexNo));
                        objs.remove(moveSelectedIndexNo);
                        tX = e.getX();
                        tY = e.getY();
                        select = 0;
                    }

                } else if (type == 12) {
                    if (!reDoObjs.isEmpty()) {
                        objs.add(reDoObjs.get(reDoObjs.size() - 1));
                        reDoObjs.remove(reDoObjs.size() - 1);
                        moveObject(e.getX(), e.getY(), objs.size() - 1);
                        select = 0;
                        objs.add(new DataContainer(0, 0, 0, 0, 0, clr, false,indexNo++));
                    }

                } else if (type == 13) {

                } else {
                    select = 0;
                    closest.isSelected = false;
                    objs.add(new DataContainer(x0, y0, x0, y0, type, clr, false,indexNo++));
                }
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {

                if (type == 10) {
                    tX = e.getX();
                    tY = e.getY();
                } else if (type == 4 || type == 7 || type == 6) {
                    if (objs.get(objs.size() - 1).isMulti) {

                    } else {
                        objs.add(new DataContainer(x0, y0, x0, y0, type, clr, true,indexNo++));
                    }
                } else if (type == 11) {
                } else if (type == 12) {
                } else if (type == 13) {
                    if (isGroupSelected) {
                        //System.out.println(objs.get(moveSelectedIndexNo).indexNo+" " +objs.size());
                        //objs.get(moveSelectedIndexNo).isSelected = true;
                        groupIndexes.add(objs.get(moveSelectedIndexNo).indexNo);                        
                        repaint();
                    } else {
                        System.out.println("Un Grouping");
                    }
                } else {
                    x0 = e.getX();
                    y0 = e.getY();
                }
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {

                if (select == 1 && objs.size() != 0) {
                    objs.forEach(ob -> {
                        ob.isSelected = false;
                        int d = ob.segdist(e.getX(), e.getY());
                        if (dmin > d) {
                            closest = ob;
                            dmin = d;
                            moveSelectedIndexNo = objs.indexOf(ob);
                        }
                    });
                    closest.isSelected = true;
                    moveSelectObjectType = closest.drawingType;
                    repaint();
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (type == 4) {
                    ArrayList<TwoDList> multiList = objs.get(objs.size() - 1).multiList;
                    multiList.add(new TwoDList(e.getX(), e.getY()));
                    objs.get(objs.size() - 1).setMultilist(multiList);
                    objs.get(objs.size() - 1).xj = e.getX();
                    objs.get(objs.size() - 1).yj = e.getY();
                } else if (type == 10) {
                } else if (type == 11) {
                } else if (type == 12) {
                } else if (type == 13) {

                } else {
                    objs.remove(objs.size() - 1);
                    objs.add(new DataContainer(x0, y0, e.getX(), e.getY(), type, clr, false, indexNo++));
                }
                repaint();
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        dmin = 9999999;
        objs.forEach(ob -> {
            if (ob.isSelected == true || groupIndexes.contains(ob.indexNo)) {
                g.setColor(Color.RED);
            } else {
                g.setColor(ob.color);
            }
            switch (ob.drawingType) {
                case 0:
                    // Line

                    g.drawLine(ob.xi, ob.yi, ob.xj, ob.yj);
                    break;
                case 4:
                    //Scibble
                    int[] xs = new int[ob.multiList.size()];
                    int[] ys = new int[ob.multiList.size()];
                    for (int i = 1; i < ob.multiList.size(); i++) {
                        xs[i] = ob.multiList.get(i).polyX;
                        ys[i] = ob.multiList.get(i).polyY;

                        g.drawLine(ob.multiList.get(i - 1).polyX, ob.multiList.get(i - 1).polyY, ob.multiList.get(i).polyX, ob.multiList.get(i).polyY);
                    }

                    break;
                case 6:
                    //Open  Polygon == Collection of line

                    int[] xs1 = new int[ob.multiList.size()];
                    int[] ys1 = new int[ob.multiList.size()];
                    for (int i = 1; i < ob.multiList.size(); i++) {
                        xs1[i] = ob.multiList.get(i).polyX;
                        ys1[i] = ob.multiList.get(i).polyY;

                        g.drawLine(ob.multiList.get(i - 1).polyX, ob.multiList.get(i - 1).polyY, ob.multiList.get(i).polyX, ob.multiList.get(i).polyY);
                    }
                    break;
                case 1:
                    //Rectangle
                    g.drawRect(utF.min(ob.xi, ob.xj), utF.min(ob.yi, ob.yj), utF.abs(ob.xi - ob.xj), utF.abs(ob.yi - ob.yj));
                    break;
                case 5:
                    // Square
                    g.drawRect(ob.xi, ob.yi, utF.abs(ob.xi - ob.xj), utF.abs(ob.xi - ob.xj));
                    break;
                case 2:
                    // Elipse
                    g.drawOval(utF.min(ob.xi, ob.xj), utF.min(ob.yi, ob.yj), utF.abs(ob.xi - ob.xj), utF.abs(ob.yi - ob.yj));
                    break;
                case 3:
                    // Circle
                    g.drawOval(ob.xi, ob.yi, utF.abs(ob.xi - ob.xj), utF.abs(ob.xi - ob.xj));
                    break;
                case 7:
                    //Closed  Polygon                    
                    int[] xs2 = new int[ob.multiList.size()];
                    int[] ys2 = new int[ob.multiList.size()];
                    for (int i = 0; i < ob.multiList.size(); i++) {
                        xs2[i] = ob.multiList.get(i).polyX;
                        ys2[i] = ob.multiList.get(i).polyY;
                    }
                    Polygon poly = new Polygon(xs2, ys2, ob.multiList.size());
                    g.drawPolygon(poly);
                    break;
                default:
                    break;
            }

            if (ob.isSelected == true) {
                g.setColor(Color.BLACK);
            }
        });
    }

    public void moveObject(int xfinall, int yfinall, int objectIndex) {
        fX = xfinall;
        fY = yfinall;
        fX = fX - tX;
        fY = fY - tY;

        objs.get(objectIndex).xi += fX;
        objs.get(objectIndex).yi += fY;
        objs.get(objectIndex).xj += fX;
        objs.get(objectIndex).yj += fY;

        int objType = objs.get(objectIndex).drawingType;

        if (objType == 4 || objType == 6 || objType == 7) {
            ArrayList<TwoDList> multiListTemp = objs.get(objectIndex).multiList;
            for (int i = 0; i < multiListTemp.size(); i++) {
                multiListTemp.get(i).polyX += fX;
                multiListTemp.get(i).polyY += fY;
            }
        }
    }

    public static void main(String[] args) {
        new SketchPad().setVisible(true);
    }
}
