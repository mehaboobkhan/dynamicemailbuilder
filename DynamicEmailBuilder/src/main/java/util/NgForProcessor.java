package main.java.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class NgForProcessor implements Processor{

	@Override
	public Document process(Document document, JSONObject templateJsonData) {
		try{
			this.recurse(document, templateJsonData);
		}catch(Exception e){
			e.printStackTrace();
		}
		return document;
	}

	@Override
	public void recurse(Node node, JSONObject templateJson) {
		try{
			processNgFor(node, templateJson);
			if(node.hasChildNodes()){
				NodeList children = node.getChildNodes();
				for(int i=0; i<children.getLength(); i++){
					this.recurse(children.item(i),templateJson);
				}
			} 
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	public void processNgFor(Node node, JSONObject templateJson){
		try{
			if(node.hasAttributes() && node.getAttributes().getNamedItem("ngFor")!=null){

				String attr = node.getAttributes().getNamedItem("ngFor").getNodeValue().replaceAll("\\s+", " ");
				String item = attr.substring(attr.indexOf("let ") + "let ".length(), attr.indexOf(" of")).trim();
				String list = attr.substring(attr.indexOf(" of") + " of".length()).trim();

				if(templateJson.has(list)){
					node.getAttributes().removeNamedItem("ngFor");
					JSONArray itemDetailsArray = (JSONArray) templateJson.get(list);
					for(int i =0; i < itemDetailsArray.length(); i++){
						JSONObject objectInArray = itemDetailsArray.getJSONObject(i);
						Node nodeClone = node.cloneNode(true);
						depthFirstLooper(nodeClone, objectInArray, item);
						node.getParentNode().insertBefore(nodeClone, node);
					}
					node.getParentNode().removeChild(node);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void depthFirstLooper(Node nodeClone, JSONObject objectInArray, String item){
		try{
			for(int j=0; j <nodeClone.getChildNodes().getLength(); j++){
				Node temp = nodeClone.getChildNodes().item(j);

				if(temp.hasChildNodes()){
					NodeList children = temp.getChildNodes();
					for(int k=0; k<children.getLength(); k++){
						this.depthFirstLooper(children.item(k),objectInArray, item);
					}
				}

				Pattern p = Pattern.compile("\\{\\s*\\{\\s*(.*?)\\s*\\}\\s*\\}");
				Matcher m = p.matcher(temp.getTextContent());
				while(m.find()) {
					String g = m.group(1);
					String[] tempStr = g.split("\\.");
					if(objectInArray.has(tempStr[1])){
						temp.setTextContent(temp.getTextContent().replaceAll("\\{\\{\\s*"+item+"\\."+tempStr[1]+"\\s*\\}\\}",
								objectInArray.getString(tempStr[1])));
					}
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}

}
