package demo1;

import org.eclipse.elk.core.RecursiveGraphLayoutEngine;
import org.eclipse.elk.core.options.CoreOptions;
import org.eclipse.elk.core.util.NullElkProgressMonitor;
import org.eclipse.elk.graph.ElkEdge;
import org.eclipse.elk.graph.ElkLabel;
import org.eclipse.elk.graph.ElkNode;
import org.eclipse.elk.graph.util.ElkGraphUtil;

public class HelloWorld {

    public static void main(String[] args) {

        ElkNode root = ElkGraphUtil.createGraph();

        // Probably use ElkNode for node1, node2, node3
        ElkNode node1 = ElkGraphUtil.createNode(root);
        ElkLabel label1 = ElkGraphUtil.createLabel(node1);
        label1.setText("node1");

        ElkNode node2 = ElkGraphUtil.createNode(root);
        ElkLabel label2 = ElkGraphUtil.createLabel(node2);
        label2.setText("node2");

        ElkNode node3 = ElkGraphUtil.createNode(root);
        ElkLabel label3 = ElkGraphUtil.createLabel(node3);
        label3.setText("node3");

        // Create edge. Use probably ElkEdge
        ElkEdge edge1 = ElkGraphUtil.createEdge(root);
        edge1.getSources().add(node1);
        edge1.getTargets().add(node2);

        ElkEdge edge2 = ElkGraphUtil.createEdge(root);
        edge2.getSources().add(node1);
        edge2.getTargets().add(node3);

        // Do the layout
        /* Default layout algorithm is layered.
           If you need change the algorithm, Add library to pom.xml and change identifier.
  	       algorithm list : https://www.eclipse.org/elk/reference/algorithms.html */
        root.setProperty(CoreOptions.ALGORITHM, "org.eclipse.elk.mrtree");

        RecursiveGraphLayoutEngine engine = new RecursiveGraphLayoutEngine();
        engine.layout(root, new NullElkProgressMonitor());

        // Print coords of nodes
        System.out.println("node1 = " + node1.getX() + ", " + node1.getY());
        System.out.println("node2 = " + node2.getX() + ", " + node2.getY());
        System.out.println("node3 = " + node3.getX() + ", " + node3.getY());
    }
}
