package distribute;

import java.io.Serializable;

@SuppressWarnings("serial")
public class BroadcastEvent implements Serializable{
	String content;
	public BroadcastEvent(String s){
		this.content=s;
	}
	public String getContent(){
		return content;
	}
}
