import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Statistics {

   protected int totalTraffic; // V dannix
   protected LocalDateTime minTime;
   protected LocalDateTime maxTime;
   protected HashSet <String> pathsSuccess; // address pages with code=200
   protected HashSet <String> pathsFailed; // address pages with code=404
   protected HashMap<String, Integer> osHashMap; // os, count_OS
   protected HashMap<String, Integer> brouserHashMap; // brouser, count_brouser

    public Statistics (){
        this.totalTraffic=0;
        this.minTime=null;
        this.maxTime=null;
        this.pathsSuccess =new HashSet<>();
        this.pathsFailed=new HashSet<>();
        this.osHashMap=new HashMap<>();
        this.brouserHashMap=new HashMap<>();
    }

    public HashSet<String> getPathsSuccess() {
        return pathsSuccess;
    }

    public HashSet<String> getPathsFailed() {
        return pathsFailed;
    }

    public void setPathsFailed(HashSet<String> pathsFailed) {
        this.pathsFailed = pathsFailed;
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
            pathsSuccess.add(logEntry.getPath());
        } else if (logEntry.responseCode==404 && logEntry.path!="") {
            pathsFailed.add(logEntry.getPath());
        }
        //OS
        if (logEntry.userAgent.os!=null) {
            String os = logEntry.userAgent.getOs().name();
            if (osHashMap.isEmpty()){
                osHashMap.put(logEntry.userAgent.getOs().toString(), 1);
                //return;
            }
            if (osHashMap.containsKey(logEntry.userAgent.getOs().name())) {
                int count = osHashMap.get(logEntry.userAgent.getOs().name());
                count++;
                osHashMap.put(logEntry.userAgent.getOs().name(), count);
            } else {
                osHashMap.put(logEntry.userAgent.getOs().toString(), 1);
            }
        }
        //brouser
        if (logEntry.userAgent.brouser!=null) {
            String brouser = logEntry.userAgent.getBrouser();
            if (brouserHashMap.isEmpty()){
                brouserHashMap.put(logEntry.userAgent.getBrouser(), 1);
                return;
            }
            if (brouserHashMap.containsKey(logEntry.userAgent.getBrouser())) {
                int count = brouserHashMap.get(logEntry.userAgent.getBrouser());
                count++;
                brouserHashMap.put(logEntry.userAgent.getBrouser(), count);
            } else {
                brouserHashMap.put(logEntry.userAgent.getBrouser(), 1);
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
    // метод рассчитывает долю для каждого браузера (от 0 до 1) (=количество конкретного браузера/общее количество для всех боаузеров)
    public HashMap<String, Double> getbrouserRate (){
        HashMap<String, Double> brouserRate=new HashMap<>();
        double sum=0;
        for (Map.Entry entry: brouserHashMap.entrySet()){
            int i=(int)entry.getValue();
            sum+=i;
        }
        for (Map.Entry entry: brouserHashMap.entrySet()){
            int i=(int)entry.getValue();
            Double rate=i/sum;
            brouserRate.put(entry.getKey().toString(),rate);
        }
        return brouserRate;
    }
}
