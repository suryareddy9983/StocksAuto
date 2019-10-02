package Util;

import java.io.IOException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils
{
  static String Projectpath;
  static XSSFWorkbook Workbook;
  static XSSFSheet sheet;
  
  public ExcelUtils(String excelpath, String sheetname) {
    try {
      Workbook = new XSSFWorkbook(excelpath);
      sheet = Workbook.getSheet(sheetname);
    }
    catch (IOException e) {
      
      e.printStackTrace();
    } 
  }



  
  public static void main(String[] args) {}



  
  public static int getRowCount() {
    rowcount = 0;
    try {
      rowcount = sheet.getPhysicalNumberOfRows();
      System.out.println("Now of rows" + rowcount);
    } catch (Exception exception) {}


    
    return rowcount;
  }
  public static int getColumnCount() {
    colcount = 0;
    try {
      colcount = sheet.getRow(0).getPhysicalNumberOfCells();
      System.out.println("Now of columns" + colcount);
    } catch (Exception exception) {}


    
    return colcount;
  }
  public static String getCellDataString(int rowNum, int colNum) {
    String celldata = null;
    celldata = sheet.getRow(rowNum).getCell(colNum).getStringCellValue();
    System.out.println(celldata);
    return celldata;
  }

  
  public static double getCellDataNum(int rowNum, int colNum) {
    double celldata = 0.0D;
    celldata = sheet.getRow(rowNum).getCell(colNum).getNumericCellValue();
    System.out.println(celldata);
    return celldata;
  }

  
  public static int getRowNum(XSSFSheet sheet, String cellData) {
    int totalRows = sheet.getLastRowNum();
    Row row = null;
    int testRowNo = 0;
    for (int rowNo = 1; rowNo <= totalRows; rowNo++) {
      
      XSSFRow xSSFRow = sheet.getRow(rowNo);
      testRowNo++;
      
      if (xSSFRow.getCell(0).getStringCellValue().equalsIgnoreCase(cellData)) {
        break;
      }
    } 

    
    return testRowNo;
  }
}
