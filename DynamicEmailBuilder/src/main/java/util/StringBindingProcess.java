package main.java.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class StringBindingProcess implements Processor{

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
			if(node.hasChildNodes()){
				NodeList children = node.getChildNodes();
				for(int i=0; i<children.getLength(); i++){
					this.recurse(children.item(i),templateJson);
				}
			}
			processStringBinding(node, templateJson);
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	public void processStringBinding(Node node, JSONObject templateJson){
		try{
			if(node.getTextContent()!=null){
				Pattern stringBindingPattern = Pattern.compile("\\{\\{(.*?)\\}\\}");
				Matcher stringBindingMatcher = stringBindingPattern.matcher(node.getTextContent());  
				while(stringBindingMatcher.find()) {
					String match = stringBindingMatcher.group().replaceAll("\\{", "\\\\{").replaceAll("\\}", "\\\\}");
					if(templateJson.has(stringBindingMatcher.group(1))){
						String value = (String) templateJson.get(stringBindingMatcher.group(1));
						node.setTextContent(node.getTextContent().replaceAll(match, value));
					}
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}

}
