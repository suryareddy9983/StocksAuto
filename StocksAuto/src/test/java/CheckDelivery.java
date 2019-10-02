import io.github.bonigarcia.wdm.WebDriverManager;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
//import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;



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
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless");
		ChromeDriver driver = new ChromeDriver(options);
		
		///Implicit wait
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		//-----------Explicit Wait-----------------
		//WebDriverWait wait = new WebDriverWait(driver, 10);
		//WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.name("asdc")));
		//-----------Fluent wait------------------------
		/*Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(10)).pollingEvery(Duration.ofSeconds(30)).ignoring(NoSuchElementException.class);
	    WebElement element = wait.until(new Function<WebDriver, WebElement>() {
			     public WebElement apply(WebDriver driver) {
			       return driver.findElement(By.id("foo"));
			     }
			   });*/
				
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
			driver.get(acqlink);

			if (onlineStockStatus.equalsIgnoreCase("NDD")) {
				driver.findElement(By.xpath("/html[1]/body[1]/div[2]/div[3]/section[2]/div[2]/ul[1]/li[2]/div[1]/div[1]/span[1]/a[1]")).click();

				String date = driver.findElement(By.xpath("//span[@id='delivery-date']")).getText();

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
					System.out.println("Device is Set to Next Day Delivery(NDD) and will be delivered on "+delDate );
				} else {
					System.out.println("Fail");
				}
			}
			else if (onlineStockStatus.equalsIgnoreCase("LOOS")) {
				String text = driver.findElement(By.xpath("/html[1]/body[1]/div[2]/div[3]/section[2]/div[2]/ul[1]/li[2]/div[1]/div[1]/span[1]/em[1]")).getText();
				if (text.equalsIgnoreCase("Out of stock")) {
					System.out.println("Device is currently Out of Stock(LOOS)");
				} else {
					System.out.println("Fail");
				}
			} else if (onlineStockStatus.equalsIgnoreCase("BACK")) {
				driver.findElement(By.xpath("/html[1]/body[1]/div[2]/div[3]/section[2]/div[2]/ul[1]/li[2]/div[1]/div[1]/span[1]/a[1]")).click();
				String date = driver.findElement(By.xpath("//span[@id='delivery-date']")).getText();
				System.out.println("Device will be delivered(BACK) on " + date);
			}else {

				System.out.println("No Chnage to the delivery dates on this device on online");
			} 
		} 


		/////Upgrades


		driver.get("https://www.three.co.uk/My3Account2018/My3Login");
		driver.findElement(By.xpath("//input[@name='msisdn']")).sendKeys(new CharSequence[] { "07378601485" });
		driver.findElement(By.xpath("//input[@name='password']")).sendKeys(new CharSequence[] { "onlinetest1" });
		driver.findElement(By.xpath("//button[@id='my3-login-submit']")).click();
		driver.findElement(By.xpath("//a[contains(text(),'Upgrades & offers.')]")).click();
		driver.findElement(By.xpath("//div[contains(text(),'Phone Upgrades.')]")).click();
		
		for (int i = 6; i < rowcount; i++) {
			String cellData = sheet.getRow(i).getCell(0).getStringCellValue();
			String upgStockStatus = sheet.getRow(i).getCell(3).getStringCellValue();
			int rownr = getRowNum(sheet1, cellData);
			String upglink = sheet1.getRow(rownr).getCell(2).getStringCellValue();
			System.out.println(String.valueOf(cellData) + upgStockStatus + rownr + upglink);
			driver.get(upglink);
			if (upgStockStatus.equalsIgnoreCase("NDD")) {
				String Date = driver.findElement(By.xpath("//*[@class='bullet-icon ng-hide: outOfStock(); ng-binding' and @ng-hide='outOfStock()']")).getText();
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
					System.out.println("Device is Set to Next Day Delivery(NDD) and will be delivered on "+delDate);
				} else {
					System.out.println("Fail");
				}
			}
			else if (upgStockStatus.equalsIgnoreCase("LOOS")) {
				String text = driver.findElement(By.xpath("//a[@class='button span3 kermit-bg three-button']")).getText();
				if (text.equalsIgnoreCase("Find a store")) {
					System.out.println("Device is currently Out of Stock(LOOS)");
				} else {
					System.out.println("Fail");
				}
			}
			else if (upgStockStatus.equalsIgnoreCase("BACK")) {
				String backdate = driver.findElement(By.xpath("//*[@class='bullet-icon ng-hide: outOfStock(); ng-binding' and @ng-hide='outOfStock()']")).getText();
				String updateddate = backdate.substring(backdate.length() - 9);
				System.out.println("Device will be delivered(BACK) on " + updateddate);
			}
			else {
				System.out.println("No Chnage to the delivery dates on this device on online");
			} 
		} 
		driver.close();
		driver.quit();
	}
}
