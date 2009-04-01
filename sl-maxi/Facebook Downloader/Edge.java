public class Edge implements java.io.Serializable {

	private static final long serialVersionUID = 2931191545664041859L;
	
	public long o1;
	public long o2;
	
	
	public String toXml(){
		return "<edge>"+o1+","+o2+"</edge>\r\n";
	}

	public Edge(){}
	public long getO1() {
		return o1;
	}
	public void setO1(long o1) {
		this.o1 = o1;
	}
	public long getO2() {
		return o2;
	}
	public void setO2(long o2) {
		this.o2 = o2;
	}
	public Edge(long o1, long o2) { this.o1 = o1; this.o2 = o2; }

	public static boolean same(Object o1, Object o2) {
		return o1 == null ? o2 == null : o1.equals(o2);
	}

	Object getFirst() { return o1; }
	Object getSecond() { return o2; }

	public int hashCode(){
		return Long.valueOf(o1).hashCode()*Long.valueOf(o2).hashCode();
		//return Integer.valueOf(""+o1+"00000"+o2+"00");
	}

	public boolean equals(Object obj) {
		if( ! (obj instanceof Edge))
			return false;
		Edge p = (Edge)obj;
		return (same(p.o1, this.o1) && same(p.o2, this.o2)) || (same(p.o1, this.o2) && same(p.o2, this.o1));
	}

	public String toString() {
		return "Pair{"+o1+", "+o2+"}";
	}
}