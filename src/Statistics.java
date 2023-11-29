import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Statistics {

   protected int totalTraffic; // V dannix
   protected LocalDateTime minTime;
   protected LocalDateTime maxTime;
   protected HashSet <String> pathsHasSet; // address pages with code=200
   protected HashMap<String, Integer> osHashMap; // os, count_OS

    public Statistics (){
        this.totalTraffic=0;
        this.minTime=null;
        this.maxTime=null;
        this.pathsHasSet=new HashSet<>();
        this.osHashMap=new HashMap<>();
    }

    public HashSet<String> getPathsHasSet() {
        return pathsHasSet;
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
        //paths
        if (logEntry.responseCode==200 && logEntry.path!=""){
            pathsHasSet.add(logEntry.getPath());
        }
        //osHashMap
        if (logEntry.userAgent.getOs()!=null) {
            String os = logEntry.userAgent.getOs().name();
            if (osHashMap.isEmpty()){
                osHashMap.put(logEntry.userAgent.getOs().toString(), 1);
                return;
            }
            if (osHashMap.containsKey(logEntry.userAgent.getOs().name())) {
                int count = osHashMap.get(logEntry.userAgent.getOs().name());
                count++;
                osHashMap.put(logEntry.userAgent.getOs().name(), count);
            } else {
                osHashMap.put(logEntry.userAgent.getOs().toString(), 1);
            }
        }
    }

    //метод getTrafficRate, в котором вычисляется разница между maxTime и minTime в часах и делите общий объём трафика на эту разницу
    public long getTrafficRate () {
        DateTimeFormatter format=DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss");
        Duration duration=Duration.between(minTime,maxTime);
        long hours=duration.toHours();
        return totalTraffic/hours;
    }
    // метод рассчитывает долю для каждой операционной системы (от 0 до 1) (=количество конкретной операционной системы/общее количество для всех операционных систем)
    public HashMap<String, Double> getOSRate (){
        HashMap<String, Double> OSRate=new HashMap<>();
        double sum=0;
        for (Map.Entry entry: osHashMap.entrySet()){
            int i=(int)entry.getValue();
            sum+=i;
        }
        for (Map.Entry entry: osHashMap.entrySet()){
            int i=(int)entry.getValue();
            Double rate=i/sum;
            OSRate.put(entry.getKey().toString(),rate);
        }
        return OSRate;
    }
}
