package general;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName ParaXML
 * Author weijian
 * DateTime 2020/4/18 6:37 PM
 * Version 1.0
 */
public class ParaXML {
    final static String filename = "/Users/weijian/Documents/unitfile/Required.xml";
    final static Logger logger = Logger.getLogger(ParaXML.class);
    @Test
    public void parsaXML(){
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance ();
        Map<String, List<Map<String,String>>> confirmFileConf = new HashMap<String, List<Map<String,String>>> ();
        List<Map<String,String>> attrConf;Map<String,String> attrItem;
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder ();
            Document document = documentBuilder.parse (filename);
            NodeList nodeList = document.getElementsByTagName ("file");
            for (int i = 0; i < nodeList.getLength (); i++){
                Node node = nodeList.item (i);
                attrConf = new ArrayList<Map<String, String>> ();
                NamedNodeMap namedNodeMap = node.getAttributes ();
                logger.info (namedNodeMap.getNamedItem ("filetype").getNodeValue ());
//                for (int j = 0; j < namedNodeMap.getLength (); j++){
//                    Node attr = namedNodeMap.item (j);
////                    confirmFileConf.put (attr.getNodeName ())
//                    logger.info (attr.getNodeName () + " : " + attr.getNodeValue ());
//                }

                NodeList childList = node.getChildNodes ();
                for (int j = 0 ; j < childList.getLength (); j++){
                    attrItem = new HashMap<String, String> ();
                    if(childList.item(j).getNodeType() == Node.ELEMENT_NODE){
                        Node child = childList.item (j);
                        NamedNodeMap childNodeMap = child.getAttributes ();
                        for (int t = 0; t < childNodeMap.getLength (); t++){
                            Node attr = childNodeMap.item (t);
                            attrItem.put (attr.getNodeName (),attr.getNodeValue ());
                            logger.info (attr.getNodeName () + " : " + attr.getNodeValue ());
                        }
//
                        attrConf.add (attrItem);
                    }

                }
                confirmFileConf.put (namedNodeMap.getNamedItem ("filetype").getNodeValue (),attrConf);

            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace ();
        } catch (SAXException e) {
            e.printStackTrace ();
        } catch (IOException e) {
            e.printStackTrace ();
        }
        logger.info (confirmFileConf.get ("02"));
    }
}
