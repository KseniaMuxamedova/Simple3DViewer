package com.cgvsu.objwriter;


import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;
import com.cgvsu.utils.FileManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ObjWriter {

    public static void write(final String name, final Model model) throws IOException {
        final ArrayList<String> vertices = verticesToString(model.vertices);
        final ArrayList<String> textureVertices = textureVerticesToString(model.textureVertices);
        final ArrayList<String> normals = normalsToString(model.normals);
        final ArrayList<String> poly = polygonsToString(model.getPolygons());

        FileManager.createFileWithText(name, vertices, textureVertices, normals, poly);
    }

    public static ArrayList<String> verticesToString(final ArrayList<Vector3f> array) {
        ArrayList<String> l = new ArrayList<String>();

        for (int vertexInd = 0; vertexInd < array.size(); vertexInd++) {
            l.add("v " + array.get(vertexInd).getX() + " " + array.get(vertexInd).getY() + " " + array.get(vertexInd).getZ());
        }

        return l;
    }

    public static ArrayList<String> textureVerticesToString(final ArrayList<Vector2f> array) {
        ArrayList<String> l = new ArrayList<String>();

        for (int textureVertices = 0; textureVertices < array.size(); textureVertices++) {
            l.add("vt " + array.get(textureVertices).getX() + " " + array.get(textureVertices).getY());
        }

        return l;
    }

    public static ArrayList<String> normalsToString(final ArrayList<Vector3f> array) {
        ArrayList<String> l = new ArrayList<String>();

        for (Vector3f vector3f : array) {
            l.add("vn " + vector3f.getX() + " " + vector3f.getY() + " " + vector3f.getZ());
        }

        return l;
    }

    public static ArrayList<String> polygonsToString(final ArrayList<Polygon> polygons) {
        ArrayList<String> l = new ArrayList<String>();
        String s;
        Polygon polygon = new Polygon();
        ArrayList<Integer> vertex = new ArrayList<Integer>();
        ArrayList<Integer> textureVertex = new ArrayList<Integer>();
        ArrayList<Integer> normal = new ArrayList<Integer>();

        for (int poly = 0; poly < polygons.size(); poly++) {
            s = "f";
            polygon = polygons.get(poly);
            vertex = polygon.getVertexIndices();
            textureVertex = polygon.getTextureVertexIndices();
            normal = polygon.getNormalIndices();

            for (int v = 0; v < vertex.size(); v++) {
                s += " ";
                s += vertex.get(v) + 1;

                if (textureVertex.size() != 0) {
                    s += "/";
                    s += textureVertex.get(v) + 1;
                }

                if (normal.size() != 0) {
                    s += "/";
                    s += normal.get(v) + 1;
                }
            }

            l.add(s);
        }

        return l;
    }

}

