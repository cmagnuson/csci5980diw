import edu.stanford.ejalbert.BrowserLauncher;
import java.util.Arrays;
import java.util.HashSet;

import net.sf.fb4j.FacebookSession;
import net.sf.fb4j.client.FacebookClientException;
import net.sf.fb4j.desktop.DesktopApplication;
import net.sf.json.JSONException;
import net.sf.fb4j.model.*;
import java.util.*;
import edu.uci.ics.jung.graph.*;
import edu.uci.ics.jung.graph.impl.*;
import edu.uci.ics.jung.visualization.*;
import javax.swing.JFrame;
import java.awt.Dimension;
import java.io.*;
import edu.uci.ics.jung.visualization.control.*;
import java.awt.event.MouseEvent;
import edu.uci.ics.jung.graph.decorators.ToStringLabeller;
import java.beans.XMLEncoder;


//somewhat based off of http://forum.fb4j.org/viewtopic.php?f=2&t=29
public class Driver {

//	private static final String API_KEY = "be938b26993eb17af95e620b1d5d45fd";
//	private static final String SECRET = "e58e5f16fbfd3b90ab9c61aab80cc3c1";
	private static final String API_KEY = "5dd0ad0b7f09d65e567b9b0803809a05";
	private static final String SECRET = "1799b6f00248b0d4d2867559c6a21b6c";

	public static void readAndSave() throws Exception {
		DesktopApplication app=new DesktopApplication(API_KEY, SECRET);
		String t=app.requestToken();
		System.out.println("token="+t);
		String url=app.getLoginUrl(t);
		System.out.println(url);
		BrowserLauncher bl=new BrowserLauncher();
		bl.openURLinBrowser(url);
		Thread.sleep(4000);//awful, but need to wait page loading on browser
		FacebookSession s=app.requestSession(t);
		long myid = s.getUserId();
		System.out.println(s);
		HashSet<Edge> edges = new HashSet<Edge>();
		HashMap<Long, Person> friends = new HashMap<Long, Person>();
		HashMap<Long, Photo> photoList = new HashMap<Long, Photo>();
		long [] fidst = s.getFriendIds();
		long [] fids = new long[fidst.length+1];
		System.arraycopy(fidst, 0, fids, 0, fidst.length);
		fids[fidst.length] = myid;
		System.out.println(fids.length);
		for(int i=0;i<fids.length;i++){
			UserInfo ui = s.getUserInfo(fids[i], UserInfo.Field.NAME, UserInfo.Field.PIC_SMALL, UserInfo.Field.UID);
			Person p = new Person(ui);
			if(p.getPid()==null){
				i--;
				System.out.println("Null Pid");
				continue;
			}
			long [] pFid = new long[fids.length];
			Arrays.fill(pFid, fids[i]);
			Thread.sleep(300);
			Relationship[] rel = s.getRelationship(pFid, fids);
			for(int j=0; j<fids.length; j++){
				if(rel[j].areFriends()){
					p.addFriend(fids[j]);
					Edge e = new Edge(Math.min(p.getPid(), fids[j]), Math.max(p.getPid(), fids[j]));
					if(!edges.contains(e)){
						edges.add(e);
					}
				}
			}
			//System.out.println("Found: "+p.getFriends().size());
			friends.put(p.getPid(), p);

			//new photo method
			try {
				p.setPhotos(s.getPhotosOfUser(fids[i]));
				System.out.println("Number of photos " + p.ui.getName() + " is in:" + p.getPhotos().length);
			}
			catch (JSONException e){
				System.out.println("Could not look up photos of " + p.ui.getName());
				//break;
				continue;
			}
//			System.out.println("Photos: "+p.photos.length);
		}
		System.out.println(""+edges.size()+" total edges in graph");

		Photo pht = null;
		for (Person p:friends.values()) {
			System.out.println("Processing "+p.ui.getName());
			if (p.getPhotos() == null) {
				continue;
			}
			for(net.sf.fb4j.model.Photo photo: p.getPhotos()){
				if(!photoList.containsKey(photo.getId())){
					Date pTime = photo.getCreationDate();
					long pTaker = photo.getOwnerId();
					long pid = photo.getId();
					pht = new Photo(pTime, pTaker, pid);
					photoList.put(pid, pht);
				}
				else {
					pht = photoList.get(photo.getId());
				}
                pht.addMember(p.getPid());
			}
		}
		System.out.println(""+photoList.size()+" total photos");

		HashMap<Long, Photo> goodPhotoList = new HashMap<Long, Photo>();
		for (Photo p : photoList.values()) {
			HashSet<Long> friendMembers = new HashSet<Long>();
			for (long pid : p.getMembers()){
				if (friends.containsKey(pid)) friendMembers.add(pid);
			}
			p.setMembers(friendMembers);
			if (friendMembers.size() > 1) {
				goodPhotoList.put(p.getPid(), p);
			}
		}
		System.out.println(""+goodPhotoList.size()+" good photos");
		
		//Don't save user as a friend
		friends.remove(myid);

		try {
			FileOutputStream fout = new FileOutputStream("people.dat");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(friends);
			oos.close();

			fout = new FileOutputStream("edges.dat");
			oos = new ObjectOutputStream(fout);
			oos.writeObject(edges);
			oos.close();

			fout = new FileOutputStream("photos.dat");
			oos = new ObjectOutputStream(fout);
			oos.writeObject(goodPhotoList);
			oos.close();
		}
		catch (Exception e) { e.printStackTrace(); }
	}

	public static HashSet<Edge> loadEdges(){
		try {
			FileInputStream fin = new FileInputStream("edges.dat");
			ObjectInputStream ois = new ObjectInputStream(fin);
			Object o = ois.readObject();
			ois.close();
			return (HashSet<Edge>)o;
		}
		catch (Exception e) { e.printStackTrace(); 
		return null;}
	}

	public static HashMap<Long, Photo> loadPhotos(){
		try {
			FileInputStream fin = new FileInputStream("photos.dat");
			ObjectInputStream ois = new ObjectInputStream(fin);
			Object o = ois.readObject();
			ois.close();
			return (HashMap<Long, Photo>)o;
		}
		catch (Exception e) { e.printStackTrace(); 
		return null;}
	}

	public static HashMap<Long, Person> loadFriends(){
		try {
			FileInputStream fin = new FileInputStream("people.dat");
			ObjectInputStream ois = new ObjectInputStream(fin);
			Object o = ois.readObject();
			ois.close();
			return (HashMap<Long, Person>)o;
		}
		catch (Exception e) { e.printStackTrace(); 
		return null;}
	}

	public static void saveToXml(HashSet<Edge> edges, HashMap<Long, Person> friends, HashMap<Long, Photo> photos) throws Exception {

		FileWriter fos = new FileWriter("friends.xml");
		BufferedWriter bw = new BufferedWriter(fos);
		bw.write("<friends>\r\n");
		for(Person f: friends.values()){
			bw.write(f.toXml());
		}
		bw.write("</friends>\r\n");
		bw.flush();

		fos = new FileWriter("edges.xml");
		bw = new BufferedWriter(fos);
		bw.write("<edges>\r\n");
		for(Edge e: edges){
			bw.write(e.toXml());
		}
		bw.write("</edges>\r\n");
		bw.flush();

		
		fos = new FileWriter("photos.xml");
		bw = new BufferedWriter(fos);
		bw.write("<photos>\r\n");
		for(Photo p: photos.values()){
			bw.write(p.toXml());
		}
		bw.write("</photos>\r\n");
		bw.flush();
	}

	public static void main(String a[]) throws Exception{
		readAndSave();
		HashSet<Edge> edges = loadEdges();
		HashMap<Long, Person> friends = loadFriends();
		HashMap<Long, Photo> photos = loadPhotos();

		saveToXml(edges, friends, photos);

		// Graph<V, E> where V is the type of the vertices 
		// and E is the type of the edges 
		Graph g = new SparseGraph(); 
		HashMap<Long, UndirectedSparseVertex> ptov = new HashMap<Long, UndirectedSparseVertex>();

		for(Person p: friends.values()){
			ptov.put(p.getPid(), new MyUndirectedSparseVertex(p.ui.getName()));
			g.addVertex(ptov.get(p.getPid()));
		}
		for(Edge e: edges){
			//for(Person p: friends){
			//for(Long pp: p.getFriends()){
			g.addEdge(new UndirectedSparseEdge(ptov.get(e.o1), ptov.get(e.o2)));
			//}
		}

		System.out.println("The graph g = " + g.toString()); 
		System.out.println("Has: "+g.getEdges().size()+" edges, "+g.getVertices().size()+" vertices");

		Layout l = new FRLayout(g);
		PluggableRenderer r = new PluggableRenderer();
		System.out.println("JF Showing1");
		VisualizationViewer vv = new VisualizationViewer(l,r, new Dimension(1000,800));
		r.setVertexStringer(ToStringLabeller.setLabellerTo(g));
		PluggableGraphMouse gm = new PluggableGraphMouse(); 
		gm.add(new TranslatingGraphMousePlugin(MouseEvent.BUTTON1_MASK));
		gm.add(new ScalingGraphMousePlugin(new CrossoverScalingControl(), 0, 1.1f, 0.9f));

		vv.setGraphMouse(gm); 
		System.out.println("JF Showing2");
		JFrame jf = new JFrame();
		jf.getContentPane().add (vv);
		jf.setVisible(true);
		System.out.println("JF Showing3");
		for(int i=0; i<1000; i++){
			l.advancePositions();
		}
		System.out.println("4");
	}

}
