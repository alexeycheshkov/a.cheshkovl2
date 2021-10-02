package task1.utils;

import aquality.selenium.core.logging.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class FileUtils {

    public static Properties loadProperties(String path) {
        Properties properties = null;
        try(FileInputStream fis = new FileInputStream(path)) {
            Logger.getInstance().info("Loading properties from: "+path);
            properties = new Properties();
            properties.load(fis);
        } catch (Exception e) {
            Logger.getInstance().error("Properties didn't loaded");
            e.printStackTrace();
        }
        return properties;
    }

    public static List<Object[]> parseExelData(String path){
        List<Object[]> dataList = new ArrayList<>();
        StringBuilder tempDataString = new StringBuilder();
        try (FileInputStream fileInputStream = new FileInputStream(path)) {
            Logger.getInstance().info("Loading test data from: "+path);
            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next();
            while (rowIterator.hasNext()){
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.iterator();
                while (cellIterator.hasNext()){
                    Cell cell = cellIterator.next();
                    if (cell.getCellType() == CellType.STRING) {
                        tempDataString.append(cell.getStringCellValue());
                    } else {
                        tempDataString.append(" ");
                    }
                    tempDataString.append("_");
                }
                dataList.add(tempDataString.toString().split("_"));
                tempDataString.setLength(0);
            }
        } catch (IOException e) {
            Logger.getInstance().error("Test data didn't loaded");
            e.printStackTrace();
        }
        return dataList;
    }

}
