package com.stepanew;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxConstants;
import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Render {

    private final String GRAPH_PNG;

    public Render(String graphPNG){
        this.GRAPH_PNG = graphPNG;
    }

    public void renderGraph(Graph<String, DefaultEdge> graph, String nameSuffix){
        renderGraph(graph, null, nameSuffix);
    }

    public void renderGraph(Graph<String, DefaultEdge> graph, Map<String, Integer> vertexColors, String nameSuffix){
        JGraphXAdapter<String, DefaultEdge> graphXAdapter = new JGraphXAdapter<>(graph);

        if (vertexColors != null && !vertexColors.isEmpty()) {
            for (String vertex : graph.vertexSet()) {
                Integer colorIndex = vertexColors.get(vertex);
                if (colorIndex != null) {
                    Color color = getColorByIndex(colorIndex);
                    graphXAdapter.setCellStyles(
                            mxConstants.STYLE_FILLCOLOR,
                            colorToHex(color),
                            new Object[]{graphXAdapter.getVertexToCellMap().get(vertex)}
                    );
                }
            }
        }

        mxIGraphLayout layout = new mxCircleLayout(graphXAdapter);
        layout.execute(graphXAdapter.getDefaultParent());

        BufferedImage image = mxCellRenderer.createBufferedImage(
                graphXAdapter,
                null,
                2,
                Color.WHITE,
                true,
                null
        );
        File imgFile = new File(GRAPH_PNG + nameSuffix + ".png");
        try {
            ImageIO.write(image, "PNG", imgFile);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    // Метод для получения цвета по индексу
    private Color getColorByIndex(int index) {
        // Можно определить набор цветов или генерировать их динамически
        Color[] colors = new Color[] {
                Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.ORANGE,
                Color.CYAN, Color.MAGENTA, Color.PINK, Color.LIGHT_GRAY, Color.GRAY
        };
        return colors[index % colors.length];
    }

    // Метод для преобразования цвета в шестнадцатеричный код
    private String colorToHex(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }
}
