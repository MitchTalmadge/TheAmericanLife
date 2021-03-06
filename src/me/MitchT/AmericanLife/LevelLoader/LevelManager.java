package me.MitchT.AmericanLife.LevelLoader;

import me.MitchT.AmericanLife.LevelLoader.Elements.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class LevelManager {

    private HashMap<String[], Class<? extends LevelElement>> levelElementMap = new HashMap<>();
    private String currentLevelName = "";
    private int currentLevelId = 0;

    public LevelManager() {
        registerElement(StageElement.class, "stage", "properties");
        registerElement(PlayerElement.class, "player", "properties");
        registerElement(PlayListElement.class, "playList", "properties");
        registerElement(EntityElement.class, "entity", "entities");
        registerElement(DialogueElement.class, "dialogue", "dialogues");
    }

    private void registerElement(Class<? extends LevelElement> clazz, String elementName) {
        registerElement(clazz, elementName, null);
    }

    private void registerElement(Class<? extends LevelElement> clazz, String elementName, String parentElementName) {
        levelElementMap.put(new String[]{elementName, parentElementName}, clazz);
    }

    public void loadLevel(int levelID) {
        loadLevel(LevelsEnum.getXmlPathByLevelID(levelID));
    }

    public void loadLevel(String xmlFilePath) {
        try {
            InputStream inputStream = getClass().getResourceAsStream(xmlFilePath);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document xmlDoc = documentBuilder.parse(inputStream);

            xmlDoc.getDocumentElement().normalize();

            Element levelElement = xmlDoc.getDocumentElement();

            if (!levelElement.getNodeName().equals("level"))
                throw new InvalidLevelException("Missing 'level' element.");

            String levelName = levelElement.getAttribute("name");
            int levelID = LevelsEnum.getLevelIDByXmlPath(xmlFilePath);

            this.currentLevelName = levelName;
            this.currentLevelId = levelID;

            System.out.println("Loading Level " + levelID + ": " + currentLevelName);

            //---- Level Properties ----//

            transverseElements(levelElement);
        } catch (InvalidLevelException | ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    private void transverseElements(Element parent) {
        NodeList childNodes = parent.getChildNodes();
        HashMap<Class<? extends LevelElement>, ArrayList<Element>> elementMap = new HashMap<>();

        for (int i = 0; i < parent.getChildNodes().getLength(); i++) {
            Node node = childNodes.item(i);
            if (node instanceof Element) {
                Element element = (Element) node;
                for (Entry<String[], Class<? extends LevelElement>> entrySet : levelElementMap.entrySet()) {
                    if (entrySet.getKey()[1] == null || entrySet.getKey()[1].equals(element.getParentNode().getNodeName())) {
                        if (entrySet.getKey()[0].equals(element.getNodeName())) {
                            if (elementMap.containsKey(entrySet.getValue())) {
                                elementMap.get(entrySet.getValue()).add(element);
                            } else {
                                ArrayList<Element> elementList = new ArrayList<>();
                                elementList.add(element);
                                elementMap.put(entrySet.getValue(), elementList);
                            }
                        }
                    }
                }
                transverseElements(element);
            }
        }
        for (Entry<Class<? extends LevelElement>, ArrayList<Element>> entry : elementMap.entrySet()) {
            try {
                entry.getKey().newInstance().processElements(entry.getValue());
            } catch (InstantiationException | IllegalAccessException | InvalidLevelException e) {
                e.printStackTrace();
            }
        }
    }

    public String getCurrentLevelName() {
        return this.currentLevelName;
    }

    public int getCurrentLevelId() {
        return this.currentLevelId;
    }

}
