package demo1;

import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.classdiagram.ClassDiagram;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.Link;
import org.eclipse.elk.core.RecursiveGraphLayoutEngine;
import org.eclipse.elk.core.util.NullElkProgressMonitor;
import org.eclipse.elk.graph.ElkEdge;
import org.eclipse.elk.graph.ElkLabel;
import org.eclipse.elk.graph.ElkNode;
import org.eclipse.elk.graph.util.ElkGraphUtil;

import java.util.HashMap;
import java.util.Map;

public class ClassDiagramLayout {
    private static String samplePuml = "@startuml\n" +
            "Node1 -- Node2\n" +
            "Node1 -- Node3\n" +
            "Node3 -- Node4\n" +
            "Node3 -- Node5\n" +
            "@enduml";

    public static void main(String[] args) {
        SourceStringReader reader = new SourceStringReader(samplePuml);
        ClassDiagram diagram = (ClassDiagram) reader.getBlocks().get(0).getDiagram();
        final ElkNode root = ElkGraphUtil.createGraph();

        Map<ILeaf, ElkNode> leafNodeMap = new HashMap<>();
        for (ILeaf leaf : diagram.getLeafsvalues()) {
            String name = leaf.getCode().getFullName();
            ElkNode node = ElkGraphUtil.createNode(root);
            ElkLabel label = ElkGraphUtil.createLabel(node);
            label.setText(name);
            leafNodeMap.put(leaf, node);
        }

        for (Link link : diagram.getLinks()) {
            ElkEdge edge = ElkGraphUtil.createEdge(root);
            edge.getSources().add(leafNodeMap.get(link.getEntity1()));
            edge.getTargets().add(leafNodeMap.get(link.getEntity2()));
        }

        RecursiveGraphLayoutEngine engine = new RecursiveGraphLayoutEngine();
        engine.layout(root, new NullElkProgressMonitor());

        for (ElkNode node : leafNodeMap.values()) {
            String name = node.getLabels().get(0).getText();
            System.out.println("node " + name + " : " + node.getX() + ", " + node.getY());
        }
    }
}
