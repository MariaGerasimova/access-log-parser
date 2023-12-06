import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
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
   protected HashMap<String, Integer> ipAdressCountRequestHashMap; // ipaddress, count_request
   protected HashMap<Integer, Integer> countVisitPerSecHashMap; // second, count_request
   protected Integer countVisitUserNotBot; // Bot
   protected Integer countFailedRequests; // address pages with code=4XX or code=5XX
   protected HashSet <String> refererDomainSet; //referer with currentSite=nova-news.ru


    public Statistics (){
        this.totalTraffic=0;
        this.minTime=null;
        this.maxTime=null;
        this.pathsSuccess =new HashSet<>();
        this.pathsFailed=new HashSet<>();
        this.osHashMap=new HashMap<>();
        this.brouserHashMap=new HashMap<>();
        this.ipAdressCountRequestHashMap =new HashMap<>();
        this.countVisitPerSecHashMap=new HashMap<>();
        this.countVisitUserNotBot=0;
        this.countFailedRequests=0;
        this.refererDomainSet =new HashSet<>();
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
        //countFailedRequests
        if (logEntry.path!="" && (logEntry.responseCode>399 && logEntry.responseCode<600)){
            countFailedRequests+=1;
        }

        //OS
        if (logEntry.userAgent.os!=null) {
            String os = logEntry.userAgent.getOs().name();
            if (osHashMap.containsKey(logEntry.userAgent.getOs().name())) {
                int count = osHashMap.get(logEntry.userAgent.getOs().name());
                count++;
                osHashMap.put(logEntry.userAgent.getOs().name(), count);
            } else {
                osHashMap.put(logEntry.userAgent.getOs().toString(), 1);
            }
        }
        //brouser and countVisitUserNotBot
        if (logEntry.userAgent.brouser!=null && logEntry.userAgent.isBot==false) { //brouser!=null and NotBot
            this.countVisitUserNotBot+=1;
            String brouser = logEntry.userAgent.getBrouser();
            if (brouserHashMap.containsKey(logEntry.userAgent.getBrouser())) {
                int count = brouserHashMap.get(logEntry.userAgent.getBrouser());
                count++;
                brouserHashMap.put(logEntry.userAgent.getBrouser(), count);
            } else {
                brouserHashMap.put(logEntry.userAgent.getBrouser(), 1);
            }
        }
        //  ipAdressCountRequest (not Bot)
        if (logEntry.userAgent.os!=null && logEntry.userAgent.isBot==false)  {
            if (ipAdressCountRequestHashMap.containsKey(logEntry.getIpAddr())) {
                int count = ipAdressCountRequestHashMap.get(logEntry.getIpAddr());
                count++;
                ipAdressCountRequestHashMap.put(logEntry.getIpAddr(), count);
            } else {
                ipAdressCountRequestHashMap.put(logEntry.getIpAddr(), 1);
            }
        }
        //countVisitPerSecHashMap (second, count_request)
        if (logEntry.userAgent.os!=null && logEntry.userAgent.isBot==false)  {
            if (countVisitPerSecHashMap.containsKey(logEntry.getDateTime().getSecond())) {
                int count = countVisitPerSecHashMap.get(logEntry.getDateTime().getSecond());
                count++;
                countVisitPerSecHashMap.put(logEntry.getDateTime().getSecond(), count);
            } else {
                countVisitPerSecHashMap.put(logEntry.getDateTime().getSecond(), 1);
            }
        }
        //refererDomainSet
        String[] partReferer=logEntry. getReferer().split("https://|http://");
        if (partReferer.length>=2){ // т.е. нашли элемент
            String[] partReferer1=partReferer[1].split("\\/");
            if (partReferer1[0].contains("www")){
                partReferer1[0]=partReferer1[0].replace("www.","");
            }
            partReferer1[0]=partReferer1[0].trim();
            refererDomainSet.add(partReferer1[0]);
        }
    }
    //вычисляется период записи лога (между maxTime и minTime в часах)
    public long durationHoursLog(){
        DateTimeFormatter format=DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss");
        Duration duration=Duration.between(minTime,maxTime);
        return duration.toHours();
    }

    //метод getTrafficRate, в котором вычисляется разница между maxTime и minTime в часах и делите общий объём трафика на эту разницу
    public long getTrafficRate () {
        return totalTraffic/ durationHoursLog();

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
        double countBrousers=0;
        for (Map.Entry entry: brouserHashMap.entrySet()){
            int i=(int)entry.getValue();
            countBrousers+=i; // sum of all brousers
        }
        for (Map.Entry entry: brouserHashMap.entrySet()){
            int i=(int)entry.getValue();
            Double rate=i/countBrousers;
            brouserRate.put(entry.getKey().toString(),rate);
        }
        return brouserRate;
    }
    // метод расчета среднего посещения сайта за час (не боты)
    public long getTrafficVisitUser (){
        return countVisitUserNotBot/ durationHoursLog();
    }
    //метод расчета количества ошибочных запросов в час
    public long getTrafficFailedRequest (){
        return countFailedRequests/ durationHoursLog();
    }
    //метод расчета средней посещаемости одним пользователем(=ip adress) (sum count Requests/count ip-address)
    public long getTrafficUserPerHour () {
        int sumRequests=0;
        for (Map.Entry entry: ipAdressCountRequestHashMap.entrySet()){
            int i=(int)entry.getValue();
            sumRequests+=i; // sum of all brousers
        }
        return sumRequests/ipAdressCountRequestHashMap.size();
    }
    //Метод расчёта пиковой посещаемости сайта (в секунду) -  возвращает максимальное посещение сайта в какую то секунду
    public HashMap <Integer, Integer> getMaxRequestInSec () {
        HashMap <Integer, Integer> getMaxRequestInSec=new HashMap<>();
        //не знаю, как правильно написать это (следующие строки наверное не совсем красиво написаны)
        int key=countVisitPerSecHashMap.entrySet().stream().max(Comparator.comparingInt(Map.Entry::getValue)).get().getKey();
        int value=countVisitPerSecHashMap.entrySet().stream().max(Comparator.comparingInt(Map.Entry::getValue)).get().getValue();
        getMaxRequestInSec.put(key,value);
        return getMaxRequestInSec;
    }
    //Метод, возвращающий список сайтов, со страниц которых есть ссылки на текущий сайт
    public HashSet <String> getRefererDomainSet(){
        return refererDomainSet;
    }
    //Метод расчёта максимальной посещаемости одним пользователем (максимальное количество переходов среди ipaddress)
    public int getMaxVisitPerUsers(){
        return  ipAdressCountRequestHashMap.entrySet().stream().max(Comparator.comparingInt(Map.Entry::getValue)).get().getValue();
    }


}
