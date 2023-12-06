public class UserAgent {

    final EOperationSystem os;
    final String brouser;
    final Boolean isBot;


    public UserAgent(String userAgent) {
        if (userAgent == "") {

            this.os = null;
            this.brouser = null;
            this.isBot=null;
            return;
        }
        //OS
        String[] parts1 = userAgent.split(";");
        String[] parts1a = parts1[0].split(" ");//OS can be like "Macintosh;" "Windows NT 10.0" or without
        if (parts1a[0].toUpperCase().equals("IPHONE")||parts1a[0].toUpperCase().equals("IPAD")||parts1a[0].toUpperCase().equals("FEEDER.CO")) parts1a[0]="MACINTOSH";
        if (parts1a[0].toUpperCase().equals("X11")||parts1a[0].toUpperCase().equals("X11)")) parts1a[0]="LINUX";
        EOperationSystem a=null;
        try {
             a= EOperationSystem.valueOf(parts1a[0].toUpperCase());
        }
        catch (IllegalArgumentException ex){
            //System.out.println("Что то не то пришло. в переменной: " + parts1a[0].toUpperCase() /*+ "Ошибка:" + ex*/);
        }
        if (a!=null) this.os=a; //v botax bivaet bez OS
        else this.os=null;
        //brouser
        //отобрать ботов
        String[] parts2 = userAgent.split("compatible;");
        for (int i = 0; i < parts2.length; i++) {
            parts2[i] = parts2[i].replaceAll(" ", "");//убираем пробелы
        }
        if (parts2.length >= 2 || userAgent.toUpperCase().indexOf("BOT")>0) { // если больше 2-х строк, то значит compatible есть в строке и это боты. делим ее дальше (берем 2- или находится слова bot
            String fragment2 = parts2[0]; // фрагмент с Bot
            String[] parts3 = fragment2.split("/");
            //this.brouser = parts3[0]; //сохраненяем все найденные userAgent
            this.isBot = true;
            this.brouser=null; // пока не знаю надо ли определять и определяется ли
        }
        else {
            //определяем тип браузера
            this.isBot = false;
            String[] parts3 = userAgent.split(" ");
            int i = parts3.length-1;
            String[] parts3a =parts3[i].split("/");
            String element=parts3a[0];
            switch (element) {
                case "Firefox":this.brouser="Firefox";break;
                case "Safari": {
                        if (userAgent.contains("KHTML, like Gecko")) this.brouser = "Chrome";
                        else this.brouser="Safari";
                        break;
                        }
                case "OPR":this.brouser="Opera";break;
                case "Edge": this.brouser="Edge";break;
                default:this.brouser="Other";break;
            }
        }
    }
    //Getters

    public EOperationSystem getOs() {
        return os;
    }

    public String getBrouser() {
        return brouser;
    }
}