package me.MitchT.AmericanLife.LevelLoader;

import java.awt.Point;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XMLHelper
{
 
    public static boolean doesElementContainChildren(Element parent, String ... childNames)
    {
        boolean missingChild = false;
        for(String childName : childNames)
        {
            if(parent.getElementsByTagName(childName).getLength() == 0)
            {
                missingChild = true;
            }
        }
        if(missingChild)
            return false;
        else 
            return true;
    }
    
    public static Element getFirstChildElement(Element parent, String childName)
    {
        if(parent.getElementsByTagName(childName).getLength() > 0)
        {
            return (Element) parent.getElementsByTagName(childName).item(0);
        }
        return null;
    }
    
    public static Point getChildAsPoint(Element parent, String childName)
    {
        if(doesElementContainChildren(parent, childName))
        {
            String pointString = getFirstChildElement(parent, childName).getTextContent();
            String[] coords = pointString.split(",");
            if(coords.length == 2)
            {
                Point point = new Point(Integer.parseInt(coords[0].replaceAll(" ", "")), Integer.parseInt(coords[1].replaceAll(" ", "")));
                return point;
            }
        }
        return null;
    }
    
    public static float getChildAsFloat(Element parent, String childName)
    {
        if(doesElementContainChildren(parent, childName))
        {
            return Float.parseFloat(getFirstChildElement(parent, childName).getTextContent());
        }
        return -1f;
    }
    
    public static int getChildAsInteger(Element parent, String childName)
    {
        if(doesElementContainChildren(parent, childName))
        {
            return Integer.parseInt(getFirstChildElement(parent, childName).getTextContent());
        }
        return -1;
    }
    
    public static String getChildAsString(Element parent, String childName)
    {
        if(doesElementContainChildren(parent, childName))
        {
            return getFirstChildElement(parent, childName).getTextContent();
        }
        return null;
    }
    
    public static String[] getChildrenAsStrings(Element parent, String childName)
    {
        if(doesElementContainChildren(parent, childName))
        {
            NodeList children = parent.getElementsByTagName(childName);
            String[] strings = new String[children.getLength()];
            for(int i = 0; i < children.getLength(); i++)
            {
                strings[i] = children.item(i).getTextContent();
            }
            return strings;
        }
        return null;
    }
    
}
