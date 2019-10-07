package Utils;

import java.io.IOException;
//import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils{
  
 static String Projectpath;
  static XSSFWorkbook Workbook;
static XSSFSheet sheet;
  
  public ExcelUtils(String excelpath, String sheetname) {
 
     try {
		Workbook = new XSSFWorkbook(excelpath);
		sheet = Workbook.getSheet(sheetname);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      
  
  }
  public static void main(String[] args) throws IOException {

   String Projectpath = System.getProperty("user.dir");
	String excelPath = String.valueOf(Projectpath) + "/Excel/TestData.xlsx";
	String urls = String.valueOf(Projectpath) + "/Excel/urls.xlsx";

	
	XSSFWorkbook Workbook = new XSSFWorkbook(excelPath);
	XSSFSheet sheet = Workbook.getSheet("Sheet1");

	XSSFWorkbook Workbook1 = new XSSFWorkbook(urls);
	XSSFSheet sheet1 = Workbook1.getSheet("links");
	
	int rowcount = sheet.getPhysicalNumberOfRows();
	
  }
  public static int getRowCount() {
    int rowcount = 0;
    rowcount = sheet.getPhysicalNumberOfRows();
     System.out.println("Now of rows" + rowcount);
     return rowcount;
  }
 
  
  public static int getColumnCount() {
      int  colcount = 0;
         colcount = sheet.getRow(0).getPhysicalNumberOfCells();
      System.out.println("Now of columns" + colcount);
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

  
	
}