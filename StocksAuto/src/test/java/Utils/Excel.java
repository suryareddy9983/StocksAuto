package Util;

import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import org.apache.commons.lang3.StringUtils;



















public class Excel
{
  public static void main(String[] args) throws ParseException {
    String date = "Estimated delivery.\r\nTue, 1st Oct.";
    
    String updateddate = date.substring(date.length() - 9);
    
    String comDate = null;
    
    if (updateddate.startsWith(" ")) {
      
      comDate = "0" + updateddate.substring(updateddate.length() - 8) + " 2019";
    }
    else {
      
      comDate = String.valueOf(updateddate) + " 2019";
    } 



    
    String deldateone = StringUtils.replaceEach(comDate, new String[] { "st", "nd", "rd", "th", "." }, new String[] { "", "", "", "", "" });
    
    System.out.println(comDate);
    System.out.println(deldateone);
    
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
    
    LocalDate delDate = LocalDate.parse(deldateone, formatter);
    
    System.out.println(delDate);

    
    LocalDate currentdate = LocalDate.now();
    LocalDate NextDelDate = null;
    System.out.println(currentdate);
    
    LocalTime currenttime = LocalTime.now();
    LocalTime cutofftime = LocalTime.parse("15:59:59");
    
    System.out.println(currenttime);
    
    DayOfWeek dayOfWeek = DayOfWeek.from(currentdate);
    
    System.out.println(dayOfWeek.minus(2L));
    
    switch ($SWITCH_TABLE$java$time$DayOfWeek()[dayOfWeek.ordinal()]) {
      case 5:
        if (currenttime.isAfter(cutofftime)) {
          
          NextDelDate = currentdate.plusDays(4L);
          
          System.out.println(NextDelDate);
        } else {
          
          NextDelDate = currentdate.plusDays(3L);
          System.out.println(NextDelDate);
        } 

      
      case 4:
        if (currenttime.isAfter(cutofftime)) {
          
          NextDelDate = currentdate.plusDays(4L);
          System.out.println(NextDelDate);
          break;
        } 
        NextDelDate = currentdate.plusDays(1L);
        System.out.println(NextDelDate);
        break;
    } 
    if (currenttime.isAfter(cutofftime)) {
      
      NextDelDate = currentdate.plusDays(2L);
      System.out.println(NextDelDate);
    } else {
      
      NextDelDate = currentdate.plusDays(1L);
      System.out.println(NextDelDate);
    } 


    
    if (NextDelDate.equals(delDate)) {
      
      System.out.println("Success");
    } else {
      
      System.out.println("Fail");
    } 
  }
}
