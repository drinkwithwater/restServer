package client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class TcpClient {
	public static void main(String args[]) throws UnknownHostException, IOException{
		Socket socket=new Socket("127.0.0.1",8000);
		byte byteArray[]=new byte[1024];
		int size=0;
		while(true){
			Integer i=0;
			while(i!=10){
				i=System.in.read();
				byteArray[size++]=i.byteValue();
			}
			socket.getOutputStream().write(byteArray,0,size);
			socket.getOutputStream().flush();
			size=0;
		}
	}

}
