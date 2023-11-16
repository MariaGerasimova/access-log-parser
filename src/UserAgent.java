import java.util.Objects;

public class UserAgent {
    final String bot; // can be absent ""
    //final EOperationSystem OS;
    //final String brouser; //? enum or not?


    public UserAgent(String userAgent){
        if (userAgent==""){
            this.bot=null;
            //this.OS=null;
            //this.brouser=null;
        }
        else {
            String[] parts2 = userAgent.split(";");
            // agent
            for (int i = 0; i < parts2.length; i++) {
                parts2[i] = parts2[i].replaceAll(" ", "");//убираем пробелы
            }
            String fragment2 = parts2[0]; // фрагмент с Bot
            String[] parts3 = fragment2.split("/");
            this.bot = parts3[0]; //сохраненяем все найденные userAgent
        }

    }

    //getters
    public String getBot() {
        return bot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAgent logParser = (UserAgent) o;
        return Objects.equals(bot, logParser.bot);
    }
    @Override
    public int hashCode() {
        return Objects.hash(bot);
    }
    @Override
    public String toString() {
        return "userAgent=" + bot;
    }
}
