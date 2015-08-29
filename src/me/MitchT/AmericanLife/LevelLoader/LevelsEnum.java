package me.MitchT.AmericanLife.LevelLoader;

public enum LevelsEnum
{
    LEVEL1(1, "/levels/1.xml"),
    LEVEL2(2, "/levels/2.xml"),
    LEVEL3(3, "/levels/3.xml");
    
    private int levelID;
    private String xmlFilePath;
    
    LevelsEnum(int levelID, String xmlFilePath)
    {
        this.levelID = levelID;
        this.xmlFilePath = xmlFilePath;
    }
    
    public static int getLevelIDByXmlPath(String xmlFilePath)
    {
        for(LevelsEnum enom : LevelsEnum.values())
        {
            if(enom.xmlFilePath.equals(xmlFilePath))
                return enom.levelID;
        }
        return -1;
    }
    
    public static String getXmlPathByLevelID(int levelID)
    {
        for(LevelsEnum enom : LevelsEnum.values())
        {
            if(enom.levelID == levelID)
                return enom.xmlFilePath;
        }
        return null;
    }
}
