package simpleModule;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
public class ParseTry {
	public static void main(String args[]) throws ParserConfigurationException, SAXException, IOException{
		DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
		DocumentBuilder db=dbf.newDocumentBuilder();
		Document doc=db.parse(new File("src/main/resources/config.xml"));
		NodeList list=doc.getElementsByTagName("temp");
		for(int i=0;i<list.getLength();i++){
		}
	}

}
