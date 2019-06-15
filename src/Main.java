import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.awt.*;
import java.io.*;
import java.util.Scanner;

public class Main {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static Settings set = getSettings();
    private static double kickTime = set.getKickTime();

    private static Settings getSettings() {
        try {
            FileReader reader = new FileReader("Settings.json");
            Scanner scanner = new Scanner(reader);
            String str = "";
            while(scanner.hasNextLine()) str += scanner.nextLine();
            return GSON.fromJson(str, Settings.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void changeSettings(File file, String str) {
        try {
            FileWriter writer = new FileWriter(file);
            writer.append(str);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException, AWTException {
        if(args.length == 0) {
            FileWriter writer = new FileWriter("Breaker.ahk");

            writer.append("; Вспомогательный файл для AutoHotKey\n" +
                    "; Написано на Java, функционал AutoHotKey\n" +
                    "; by Den4ik\n\n");
            writer.append("changeLanguage()\n" +
                    "{\n" +
                    "  Active := WinExist(\"A\")\n" +
                    "  ThreadID := dllCall(\"GetWindowThreadProcessId\", \"uint\", Active)\n" +
                    "  code := dllCall(\"GetKeyboardLayout\", \"uint\", ThreadID, \"uint\") & 0xFFFF\n" +
                    "  if(code=1049)\n" +
                    "  {\n" +
                    "    PostMessage, 0x50, 2,,, A\n" +
                    "  }\n" +
                    "}\n\n");
            writer.append("changeLanguage()\n" +
                    "Sleep, 300\n");

            for(int i = 0; i < 1000; i++) {
                writer.append("Send t\r\nSleep, 60\r\nSend /{Enter}\r\n");
                double ms = Math.random() * kickTime * 1000 * 60;
                writer.append("Sleep, ").append(String.valueOf((int) ms)).append("\r\n");
            }
            writer.close();

            System.out.println("Создание дополнительного файла для работы прошла успешно, чтобы убедиться, что всё работает, через 5 секунд в чат будет отправлено \"/\"");

            Thread.sleep(5000);
            Process process = Runtime.getRuntime().exec("AutoHotKey.exe Breaker.ahk");

            while(true) {
                Scanner scanner = new Scanner(System.in);
                System.out.print("Введите команду (\"help\" для помощи): ");
                String str = scanner.nextLine();

                if(str.equals("help")) {
                    System.out.println("stop - чтобы выключить приложение");
                    System.out.println("set - изменить настройки");
                } else if(str.equals("stop")) {
                    process.destroy();
                    System.exit(0);
                } else if(str.equals("set")) {
                    Scanner scan = new Scanner(System.in);
                    System.out.print(" - чтобы изменить время кика с сервера введите 1:");
                    int num = scan.nextInt();
                    if(num == 1) {
                        Scanner sc = new Scanner(System.in);
                        System.out.print("Введите новое значение время кика с сервера: ");
                        double number = sc.nextDouble();

                        Settings settings = new Settings(number);

                        String newValue = GSON.toJson(settings);
                        changeSettings(new File("Settings.json"), newValue);
                    }
                }
            }
        }
    }
}
