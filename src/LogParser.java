import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

public class LogParser {
    protected List<String> userAgent;

    public LogParser(){
        this.userAgent=new ArrayList<>();
    }
    public LogParser (String userAgentS){

        this.userAgent.add(userAgentS);
    }
    public LogParser (List<String> userAgent){
        List<String> userAgentCopy=new ArrayList<>(userAgent); // защитное копирование
        this.userAgent=userAgent;
    }

    //getters&setters
    public List<String> getUserAgent() {
        return userAgent;
    }
    public void setUserAgent(List<String> userAgent) {
        this.userAgent = userAgent;
    }
    public void setUserAgent(String userAgentS) {
        this.userAgent.add(userAgentS);
    }
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
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogParser logParser = (LogParser) o;
        return Objects.equals(userAgent, logParser.userAgent);
    }
    @Override
    public int hashCode() {
        return Objects.hash(userAgent);
    }
    @Override
    public String toString() {
        return "userAgent=" + userAgent;
    }
}
