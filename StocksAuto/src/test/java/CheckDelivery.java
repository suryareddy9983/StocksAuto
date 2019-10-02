import io.github.bonigarcia.wdm.WebDriverManager;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import org.apache.commons.lang3.StringUtils;
//import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;



public class CheckDelivery
{

	public static int getRowNum(XSSFSheet sheet1, String cellData) {
		int totalRows = sheet1.getLastRowNum();
		//Row row = null;
		int testRowNo = 0;
		for (int rowNo = 1; rowNo <= totalRows; rowNo++) {
			XSSFRow xSSFRow = sheet1.getRow(rowNo);
			testRowNo++;
			if (xSSFRow.getCell(0).getStringCellValue().equalsIgnoreCase(cellData)) {
				break;
			}
		} 
		return testRowNo;
	}

	public static void main(String[] args) throws IOException {
		WebDriverManager.chromedriver().setup();
		ChromeDriver chromeDriver = new ChromeDriver();
		
		String projectpath = System.getProperty("user.dir");
		String excelPath = String.valueOf(projectpath) + "/Excel/TestData.xlsx";
		String urls = String.valueOf(projectpath) + "/Excel/urls.xlsx";

		@SuppressWarnings("resource")
		XSSFWorkbook Workbook = new XSSFWorkbook(excelPath);
		XSSFSheet sheet = Workbook.getSheet("Sheet1");

		@SuppressWarnings("resource")
		XSSFWorkbook Workbook1 = new XSSFWorkbook(urls);
		XSSFSheet sheet1 = Workbook1.getSheet("links");

		int rowcount = sheet.getPhysicalNumberOfRows();
		//int colcount = sheet.getRow(0).getPhysicalNumberOfCells();

		for (int i = 6; i < rowcount; i++) {

			String cellData = sheet.getRow(i).getCell(0).getStringCellValue();
			String onlineStockStatus = sheet.getRow(i).getCell(2).getStringCellValue();
			int rownr = getRowNum(sheet1, cellData);
			String acqlink = sheet1.getRow(rownr).getCell(1).getStringCellValue();
			System.out.println(String.valueOf(cellData) + " " + onlineStockStatus + " " + rownr + " " + acqlink);
			chromeDriver.get(acqlink);

			if (onlineStockStatus.equalsIgnoreCase("NDD")) {
				chromeDriver.findElement(By.xpath("/html[1]/body[1]/div[2]/div[3]/section[2]/div[2]/ul[1]/li[2]/div[1]/div[1]/span[1]/a[1]")).click();

				String date = chromeDriver.findElement(By.xpath("//span[@id='delivery-date']")).getText();

				String updateddate = date.substring(date.length() - 6);

				String comDate = null;

				if (updateddate.startsWith(" ")) {
					comDate = "0" + updateddate.substring(updateddate.length() - 5) + " 2019";
				}else {
					comDate = String.valueOf(updateddate) + " 2019";
				} 

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
				LocalDate delDate = LocalDate.parse(comDate, formatter);
				LocalDate currentdate = LocalDate.now();
				LocalDate NextDelDate = null;
				LocalTime currenttime = LocalTime.now();
				LocalTime cutofftime = LocalTime.parse("15:59:59");
				DayOfWeek dayOfWeek = DayOfWeek.from(currentdate);

				switch (dayOfWeek) {
				case FRIDAY:
					if (currenttime.isAfter(cutofftime)) {
						NextDelDate = currentdate.plusDays(4);
					}
					else {
						NextDelDate = currentdate.plusDays(3);
					} 
				case THURSDAY:
					if (currenttime.isAfter(cutofftime)) {
						NextDelDate = currentdate.plusDays(4);
					} else {
						NextDelDate = currentdate.plusDays(1);
					} 
				default:
					if (currenttime.isAfter(cutofftime)) {
						NextDelDate = currentdate.plusDays(2);
					}else {
						NextDelDate = currentdate.plusDays(1);
					} 
				}
				if (NextDelDate.equals(delDate)) {
					System.out.println("Pass");
				} else {
					System.out.println("Fail");
				}
			}
			else if (onlineStockStatus.equalsIgnoreCase("LOOS")) {
				String text = chromeDriver.findElement(By.xpath("/html[1]/body[1]/div[2]/div[3]/section[2]/div[2]/ul[1]/li[2]/div[1]/div[1]/span[1]/em[1]")).getText();
				if (text.equalsIgnoreCase("Out of stock")) {
					System.out.println("Pass");
				} else {
					System.out.println("Fail");
				}
			} else if (onlineStockStatus.equalsIgnoreCase("BACK")) {
				chromeDriver.findElement(By.xpath("/html[1]/body[1]/div[2]/div[3]/section[2]/div[2]/ul[1]/li[2]/div[1]/div[1]/span[1]/a[1]")).click();
				String Date = chromeDriver.findElement(By.xpath("//span[@id='delivery-date']")).getText();
				System.out.println("Device delibery is backed to" + Date);
			}else {

				System.out.println("No Chnage to the delivery dates on this device on online");
			} 
		} 


		/////Upgrades


		chromeDriver.get("https://www.three.co.uk/My3Account2018/My3Login");
		chromeDriver.findElement(By.xpath("//input[@name='msisdn']")).sendKeys(new CharSequence[] { "07378601485" });
		chromeDriver.findElement(By.xpath("//input[@name='password']")).sendKeys(new CharSequence[] { "onlinetest1" });
		chromeDriver.findElement(By.xpath("//button[@id='my3-login-submit']")).click();
		chromeDriver.findElement(By.xpath("//a[contains(text(),'Upgrades & offers.')]")).click();
		chromeDriver.findElement(By.xpath("//div[contains(text(),'Phone Upgrades.')]")).click();
		
		for (int i = 6; i < rowcount; i++) {
			String cellData = sheet.getRow(i).getCell(0).getStringCellValue();
			String upgStockStatus = sheet.getRow(i).getCell(3).getStringCellValue();
			int rownr = getRowNum(sheet1, cellData);
			String upglink = sheet1.getRow(rownr).getCell(2).getStringCellValue();
			System.out.println(String.valueOf(cellData) + upgStockStatus + rownr + upglink);
			chromeDriver.get(upglink);
			if (upgStockStatus.equalsIgnoreCase("NDD")) {
				String Date = chromeDriver.findElement(By.xpath("//*[@class='bullet-icon ng-hide: outOfStock(); ng-binding' and @ng-hide='outOfStock()']")).getText();
				String updateddate = Date.substring(Date.length() - 9);
				String comDate = null;
				if (updateddate.startsWith(" ")) {
					comDate = "0" + updateddate.substring(updateddate.length() - 8) + " 2019";
				} else {
					comDate = String.valueOf(updateddate) + " 2019";
				} 
				String deldateone = StringUtils.replaceEach(comDate, new String[] { "st", "nd", "rd", "th", "." }, new String[] { "", "", "", "", "" });
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
				LocalDate delDate = LocalDate.parse(deldateone, formatter);
				LocalDate currentdate = LocalDate.now();
				LocalDate NextDelDate = null;
				System.out.println(currentdate);
				LocalTime currenttime = LocalTime.now();
				LocalTime cutofftime = LocalTime.parse("15:59:59");
				DayOfWeek dayOfWeek = DayOfWeek.from(currentdate);

				switch (dayOfWeek) {
				case FRIDAY:
					if (currenttime.isAfter(cutofftime)) {
						NextDelDate = currentdate.plusDays(4);
					} else {
						NextDelDate = currentdate.plusDays(3);
						} 
				case THURSDAY:
					if (currenttime.isAfter(cutofftime)) {
						NextDelDate = currentdate.plusDays(4);
					} else {
						NextDelDate = currentdate.plusDays(1);
					}
				default:  
					if (currenttime.isAfter(cutofftime)) {
						NextDelDate = currentdate.plusDays(2);
					} else {
						NextDelDate = currentdate.plusDays(1);
					} 
				}
				if (NextDelDate.equals(delDate)) {
					System.out.println("Success");
				} else {
					System.out.println("Fail");
				}
			}
			else if (upgStockStatus.equalsIgnoreCase("LOOS")) {
				String text = chromeDriver.findElement(By.xpath("//a[@class='button span3 kermit-bg three-button']")).getText();
			System.out.println("device is out of stock " + text);
			}
			else if (upgStockStatus.equalsIgnoreCase("BACK")) {
				String backdate = chromeDriver.findElement(By.xpath("//*[@class='bullet-icon ng-hide: outOfStock(); ng-binding' and @ng-hide='outOfStock()']")).getText();
				System.out.println("Device will be delibered on" + backdate);
			}
			else {
				System.out.println("No Chnage to the delivery dates on this device on online");
			} 
		} 
		chromeDriver.close();
		chromeDriver.quit();
	}
}
