package Utils;




public class ExcelDataProvider
{
  public static void main(String[] args) { testData("C:/Users/sreddy6/JavaProjects/StocksAutomation/Excel/TestData.xlsx", "Sheet1"); }



  
  public static Object[][] testData(String excelPath, String sheetName) {
    int rowcount = ExcelUtils.getRowCount();
    int colcount = ExcelUtils.getColumnCount();
    
    Object[][] data = new Object[rowcount - 1][colcount];
    
    for (int i = 1; i < rowcount; i++) {
      
      for (int j = 0; j < colcount; j++) {
        
        String cellData = ExcelUtils.getCellDataString(i, j);
        
        System.out.println(cellData);
        
        data[i - 1][j] = cellData;
      } 
    } 
    
    return data;
  }
}

