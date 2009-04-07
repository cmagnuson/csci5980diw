
import java.util.*;
import net.sf.fb4j.model.PhotoTag;

public class Photo implements java.io.Serializable {

	private long taker;
	private Date time;
	private long pid;
	private HashSet<Long> members;
	private String url;

	private static final long serialVersionUID = -1727565347711332372L;


	public String toXml(){
		String ret="<photo>\r\n";
		ret+="<taker>"+taker+"</taker>\r\n";
		ret+="<time>"+time.getTime()+"</time>\r\n";
		ret+="<photoid>"+pid+"</photoid>\r\n";
		ret+="<url>"+url+"</url>\r\n";

		ret+="<tagged>\r\n";
		for(Long l: members){
			ret+="<uid>"+l+"</uid>\r\n";
		}
		ret+="</tagged>\r\n";

		ret+="</photo>\r\n";
		return ret;
	}

	public Photo(){}

	public void setTaker(long taker) {
		this.taker = taker;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public void setMembers(HashSet<Long> members) {
		this.members = members;
	}

	public Photo(Date time, long taker, long pid){
		this.taker = taker;
		this.time = time;
		this.pid = pid;
		members = new HashSet<Long>();
		members.add(taker);
	}

	public long getTaker(){
		return taker;
	}
	public Date getTime(){
		return time;
	}
	public long getPid(){
		return pid;
	}
	public HashSet<Long> getMembers(){
		return members;
	}

	public void addMember(long m){
		members.add(m);
	}

	public boolean equals(Object o){
		if(o instanceof Photo){
			return ((Photo)o).pid==pid;
		}
		return false;
	}
	public int hashCode(){
		return Long.valueOf(pid).hashCode();
	}
}
