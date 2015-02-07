package me.MitchT.AmericanLife.LevelLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import me.MitchT.AmericanLife.LevelLoader.Elements.EntityElement;
import me.MitchT.AmericanLife.LevelLoader.Elements.LevelElement;
import me.MitchT.AmericanLife.LevelLoader.Elements.PlayListElement;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class LevelManager
{
    
    private HashMap<String[], Class<? extends LevelElement>> levelElementMap = new HashMap<String[], Class<? extends LevelElement>>();
    
    public LevelManager()
    {
        registerElement(PlayListElement.class, "playList", "properties");
        registerElement(EntityElement.class, "entity", "entities");
    }
    
    private void registerElement(Class<? extends LevelElement> clazz, String elementName)
    {
        registerElement(clazz, elementName, null);
    }
    
    private void registerElement(Class<? extends LevelElement> clazz, String elementName, String parentElementName)
    {
        levelElementMap.put(new String[] {elementName, parentElementName}, clazz);
    }
    
    public void loadLevel(int levelID)
    {
        loadLevel(LevelsEnum.getXmlPathByLevelID(levelID));
    }
    
    public void loadLevel(String xmlFilePath)
    {
        try
        {
            System.out.println(xmlFilePath);
            InputStream inputStream = getClass().getResourceAsStream(xmlFilePath);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory
                    .newDocumentBuilder();
            Document xmlDoc = documentBuilder.parse(inputStream);
            
            xmlDoc.getDocumentElement().normalize();
            
            if(xmlDoc != null)
            {
                Element levelElement = xmlDoc.getDocumentElement();
                
                if(!levelElement.getNodeName().equals("level"))
                    throw new InvalidLevelException("Missing 'level' element.");
                
                String levelName;
                int levelID = LevelsEnum.getLevelIDByXmlPath(xmlFilePath);
                
                levelName = levelElement.getAttribute("name");
                System.out.println("Loading Level: " + levelName);
                
                //---- Level Properties ----//
                
                transverseElements(levelElement);
            }
            else
            {
                throw new InvalidLevelException("Level XML File does not exist at '" + xmlFilePath + "'!");
            }
        }
        catch(InvalidLevelException e)
        {
            e.printStackTrace();
        }
        catch(ParserConfigurationException e)
        {
            e.printStackTrace();
        }
        catch(SAXException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    private void transverseElements(Element parent)
    {
        NodeList childNodes = parent.getChildNodes();
        HashMap<Class<? extends LevelElement>, ArrayList<Element>> elementMap = new HashMap<Class<? extends LevelElement>, ArrayList<Element>>();
        
        for(int i = 0; i < parent.getChildNodes().getLength(); i++)
        {
            Node node = childNodes.item(i);
            if(node instanceof Element)
            {
                Element element = (Element) node;
                for(Entry<String[], Class<? extends LevelElement>> entrySet : levelElementMap.entrySet())
                {
                    if(entrySet.getKey()[1] == null || entrySet.getKey()[1].equals(element.getParentNode().getNodeName()))
                    {
                        if(entrySet.getKey()[0].equals(element.getNodeName()))
                        {
                            if(elementMap.containsKey(entrySet.getValue()))
                            {
                                elementMap.get(entrySet.getValue()).add(element);
                            }
                            else 
                            {
                                ArrayList<Element> elementList = new ArrayList<Element>();
                                elementList.add(element);
                                elementMap.put(entrySet.getValue(), elementList);
                            }
                        }
                    }
                }
                transverseElements(element);
            }
        }
        for(Entry<Class<? extends LevelElement>, ArrayList<Element>> entry : elementMap.entrySet())
        {
            try
            {
                entry.getKey().newInstance().processElements(entry.getValue());
            }
            catch(InstantiationException e)
            {
                e.printStackTrace();
            }
            catch(IllegalAccessException e)
            {
                e.printStackTrace();
            }
            catch(InvalidLevelException e)
            {
                e.printStackTrace();
            }
        }
    }
    
}
