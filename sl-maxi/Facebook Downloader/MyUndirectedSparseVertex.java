import edu.uci.ics.jung.graph.impl.UndirectedSparseEdge;
import edu.uci.ics.jung.graph.*;
import edu.uci.ics.jung.graph.impl.*;
import edu.uci.ics.jung.visualization.*;

public class MyUndirectedSparseVertex extends UndirectedSparseVertex {

	private String ts;
	
	public MyUndirectedSparseVertex(String ts){
		super();
		this.ts = ts;
	}
	
	public String toString(){
		return ts;
	}
}
