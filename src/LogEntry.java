import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class LogEntry { // объект - это строка из файла
    final String ipAddr;
    //final String absentProperties="";
    final LocalDateTime dateTime;
    final HttpMethod method;
    final String path;
    final int responseCode;
    final Integer responseSize;
    final String referer; // путь на страницу c которой перешли на текущую
    final UserAgent userAgent;

    public LogEntry(String line){
        //задание 9,2- выдgentеление User-A
        String[] parts = line.split("\\(",2); // делим на 2 строки, начиная с ( NOTUSERAgent и UserAgent
        //UserAgent
        if (parts.length >= 2) { // если больше 2-х строк, то значит информация о UserAgent есть в строке и делим ее дальше
            //UserAgent
            String fragmentUserAgent = parts[1];
            this.userAgent=new UserAgent(fragmentUserAgent);
        }
        else {
            this.userAgent=new UserAgent("");
        }
        //Other fields
        String fragmentNotuserAgent=parts[0];
        String[] parts2 = line.split(" ",2); //выделяем ip, остальное во второй части   - - [25/Sep/2022:06:25:06 +0300]
            this.ipAddr =parts2[0];
        String[] parts3 = parts2[1].split("\\[",2); //выделяем дату, остальное во второй части  25/Sep/2022:06:25:06 +0300] "GET ( в первой части остались 2 незаполненных свойства)
        String[] parts4 = parts3[1].split("\\]",2); //выделяем дату, остальное во второй части -- 25/Sep/2022:06:25:06 +0300 и "GET /parliament/november-reports/cont
            //dateTime
                String[] parts4a = parts4[0].split(" +",2); // в первой части осталось 25/Sep/2022:06:25:06
                String[] parts4b= parts4a[0].split(":",2); // 2 части: 25/Sep/2022 и 06:25:06
                DateTimeFormatter format=DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss",new Locale("ENGLISH"));
                try {
                    LocalDateTime ld = LocalDateTime.parse(parts4a[0], format);
                    this.dateTime = ld;
                }
                catch (DateTimeParseException ex) {
                    throw new RuntimeException(ex);
                }
        String partsTrim=parts4[1].trim(); // убрали пробелы в начале ( и конце строки)
        String[] parts5 = partsTrim.split("\"",0); //GET и /parliament/november-reports/content/6377/58/?n=13 HTTP/1.0 и 200 8983 и "referer" и Mozilla/5.0 (
           //methodRequest,request
                String[] parts5a = parts5[1].split(" "); // отделяем метод запроса и путь запроса(0-ый элемент это пустота, т.к. " в начале был)
                this.path =parts5a[1];
                this.method = HttpMethod.valueOf(parts5a[0]);
           //answer
                partsTrim=parts5[2].trim();
                String[] parts5b = partsTrim.split(" ");
                this.responseCode =Integer.parseInt(parts5b[0]);
                this.responseSize =Integer.parseInt(parts5b[1]);
                if (responseSize <0) System.out.println("sizeBite<0");
        //referer
                this.referer=parts5[3];
    }
//Getters
    public String getIpAddr() {
        return ipAddr;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public int getResponseSize() {
        return responseSize;
    }

    public String getReferer() {
        return referer;
    }

    public UserAgent getUserAgent() {
        return userAgent;
    }
    /*
    //метод, возвращает количество найденных элементов по userAgent
    public int findCountElement (String findValue) {
        int FindCountElement=0;
        for (String item : userAgent){
            if (item.equals(findValue)) FindCountElement+=1;
        }
        return FindCountElement;
    }
    //метод, возвращает долю запросов по отнощению в общему числу
    public double findPercentElement (String findValue){
        int i=userAgent.size();
        int b=findCountElement(findValue);
        return ((double)b/i*100);
    }
*/
}
