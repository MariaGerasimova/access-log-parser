import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Statistics {

   protected int totalTraffic; // V dannix
   protected LocalDateTime minTime;
   protected LocalDateTime maxTime;

    public Statistics (){
        this.totalTraffic=0;
        this.minTime=null;
        this.maxTime=null;
    }
    // метод addEntry, принимающий в качестве параметра объект класса LogEntry
    public void addEntry(LogEntry logEntry){
        //minTime maxTime
        if (minTime==null || maxTime==null) {
            minTime = logEntry.getDateTime();
            maxTime = logEntry.getDateTime();
        }
        else {
            if (minTime.compareTo(logEntry.dateTime)>0) minTime=logEntry.dateTime;
            if (maxTime.compareTo(logEntry.dateTime)<0) maxTime=logEntry.dateTime;
        }
        // traffic
        totalTraffic+=logEntry.getResponseSize();
            /*if (totalTraffic>2147483647 || totalTraffic<0) {
                System.out.println("Превышение инт: i=" +i + "Трафик" + totalTraffic);}*/
    }

    //метод getTrafficRate, в котором вычисляйте разницу между maxTime и minTime в часах и делите общий объём трафика на эту разницу
    public long getTrafficRate () {
        DateTimeFormatter format=DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss");
        Duration duration=Duration.between(minTime,maxTime);
        long hours=duration.toHours();
        return totalTraffic/hours;
    }
}
