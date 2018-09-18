package main.java.emailbuilder;

import java.io.File;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.json.JSONObject;
import org.w3c.dom.Document;

import main.java.util.NgForProcessor;
import main.java.util.StringBindingProcess;

public class EmailBuilder {

	private File htmlFile;
	private JSONObject templateDataJson;
	private Document document;

	public EmailBuilder(File htmlFile, JSONObject templateDataJson){
		this.htmlFile = htmlFile;
		this.templateDataJson = templateDataJson;
		try{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			this.document = docBuilder.parse(this.htmlFile);
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	public String process(){
		StringWriter writer = new StringWriter();
		try{

			NgForProcessor ng = new NgForProcessor();
			this.document = ng.process(this.document, this.templateDataJson);

			StringBindingProcess sb = new StringBindingProcess();
			this.document = sb.process(this.document, this.templateDataJson);

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			transformerFactory.setAttribute("indent-number", new Integer(2));
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			Source input = new DOMSource(this.document);

			StreamResult result = new StreamResult(writer);
			transformer.transform(input, result);

		}catch(Exception e){
			e.printStackTrace();
		}
		return writer.toString();
	}

}