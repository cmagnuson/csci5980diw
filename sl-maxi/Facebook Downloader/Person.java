
import net.sf.fb4j.model.*;
import java.util.*;

public class Person implements java.io.Serializable {

	private HashSet<Long> friends = new HashSet<Long>();
	UserInfo ui;
	
	
	private static final long serialVersionUID = 1599980710196913471L;
	

	public String toXml(){
		String ret = "<friend>\r\n";
		ret+="<uid>"+ui.getId()+"</uid>\r\n";
		ret+="<name>"+ui.getName()+"</name>\r\n";

		ret+="<friendlist>\r\n";
		for(Long p: friends){
			ret+="<fid>"+p+"</fid>\r\n";
		}
		ret+="</friendlist>\r\n";
		
		ret+="</friend>\r\n";
		return ret;
	}
	
	public Person(){}
	
	public UserInfo getUi() {
		return ui;
	}

	public void setUi(UserInfo ui) {
		this.ui = ui;
	}

	public void setFriends(HashSet<Long> friends) {
		this.friends = friends;
	}

	Person(UserInfo ui){
		this.ui = ui;
	}
	
	public void addFriend(long fid){
		friends.add(fid);
	}
	
	public HashSet<Long> getFriends(){
		return friends;
	}
	
	public Long getPid(){
		return (ui.get(UserInfo.Field.UID)==null) ? null : ui.getId();
	}
	
}
