package main.java.util;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public interface Processor {
	public abstract Document process(Document document, JSONObject templateJsonData);
	public abstract void recurse(Node node, JSONObject templateJson);
}
