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
    public void addEntry(List<LogEntry> logEntry){
        // traffic
        minTime=logEntry.get(0).getDateTime();
        maxTime=logEntry.get(0).getDateTime();
        for (int i=0;i<logEntry.size();i++){
            totalTraffic+=logEntry.get(i).getResponseSize();
            /*if (totalTraffic>2147483647 || totalTraffic<0) {
                System.out.println("Превышение инт: i=" +i + "Трафик" + totalTraffic);}*/
            if (i>0 && minTime.compareTo(logEntry.get(i).dateTime)>0) minTime=logEntry.get(i).dateTime;
            if (i>0 && maxTime.compareTo(logEntry.get(i).dateTime)<0) maxTime=logEntry.get(i).dateTime;
        }
    }

    //метод getTrafficRate, в котором вычисляйте разницу между maxTime и minTime в часах и делите общий объём трафика на эту разницу
    public long getTrafficRate () {
        DateTimeFormatter format=DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss");
        Duration duration=Duration.between(minTime,maxTime);
        long hours=duration.toHours();
        return totalTraffic/hours;
    }
}
