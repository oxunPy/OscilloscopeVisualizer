package com.example.program.util;


import java.io.BufferedReader;
import java.io.InputStreamReader;

public class OSUtils {
    public static void main(String[] args) {
        System.out.println("HDD: " + OSUtils.getHDD());
        System.out.println("CPU: " + OSUtils.getCPU());
        System.out.println("Motherboard: " + OSUtils.getMotherboard());
        System.out.println("HardwareCode: " + OSUtils.getHardwareCode());
        System.out.println("PC-NAME: " + OSUtils.getPCName());
        System.out.println("PC-OWNER: " + OSUtils.getPCOwner());
    }

    private static String hardwareCode = null;

    public static String getHardwareCode() {
        if (hardwareCode == null)
            hardwareCode = Encryption.convert(String.format("%s=%s=%s", getHDD(), getCPU(), getMotherboard()/*, getMacAddress()*/));
        return hardwareCode;
    }

    /**
     * @return komputerdagi barcha HDD ning serial no`merlarini qaytaradi
     * masalan: SerialNumber#50026B724C01DCEC#252B8AVNS
     */
    public static String getHDD() {
        // USB bo`lib ulanmagan(MOTHER BOARD ni o`zidagilari) - HDD lar ro`yhatini olish.
        String command = "wmic diskdrive where interfacetype!='USB' get serialnumber";
//        String command = "wmic diskdrive get serialnumber";

        try {
            Process pa = Runtime.getRuntime().exec(command);
            pa.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(pa.getInputStream()));

            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
//                System.out.println(line);
                if (line.isEmpty()) continue;

                result.append(result.length() == 0 ? line : ("#" + line));
            }
//            System.out.println(result);
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /** @return kompyuterning nomini oladi
     */
    public static String getPCName(){
        String command = "hostname";

        try{
            Process pa = Runtime.getRuntime().exec(command);
            pa.waitFor();

            BufferedReader reader = new BufferedReader(new InputStreamReader(pa.getInputStream()));

            StringBuilder result = new StringBuilder();
            String line;

            while((line = reader.readLine()) != null){
                line = line.trim();
                if(line.isEmpty()) continue;
                result.append(result.length() != 0 ? line : ("#" + line));
            }
            return result.toString();
        } catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    /** @return get-about-me
     * */
    public static String getPCOwner(){
        String command = "whoami";

        try{
            Process pa = Runtime.getRuntime().exec(command);
            pa.waitFor();

            BufferedReader reader = new BufferedReader(new InputStreamReader(pa.getInputStream()));

            StringBuilder result = new StringBuilder();
            String line;

            while((line = reader.readLine()) != null){
                line = line.trim();
                if(line.isEmpty()) continue;
                result.append(result.length() != 0 ? line : ("#" + line));
            }
            return result.toString();
        } catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * @return komputerdagi CPU ning ProcessorId ni qaytaradi
     * masalan: ProcessorId#BFEBFBFF000306C3
     */
    public static String getCPU() {
        String command = "wmic CPU get ProcessorId";

        try {
            Process pa = Runtime.getRuntime().exec(command);
            pa.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(pa.getInputStream()));

            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
//                System.out.println(line);
                if (line.isEmpty()) continue;

                result.append(result.length() == 0 ? line : ("#" + line));
            }
//            System.out.println(result);
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return komputerdagi Motherboard ning serial no`merini qaytaradi
     * masalan: SerialNumber#140525709801930
     */
    public static String getMotherboard() {
        String command = "wmic BASEBOARD get serialnumber";

        try {
            Process pa = Runtime.getRuntime().exec(command);
            pa.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(pa.getInputStream()));

            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
//                System.out.println(line);
                if (line.isEmpty()) continue;

                result.append(result.length() == 0 ? line : ("#" + line));
            }
//            System.out.println(result);
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return komputerdagi Network kartalarning birinchisini MAC adresini qaytaradi
     */
//    public static String getMacAddress() {
//        String command = "/sbin/ifconfig";
//
//        String sOsName = System.getProperty("os.name");
//        if (sOsName.startsWith("Windows"))
//            command = "ipconfig /All";
//        else if (sOsName.startsWith("Linux") || sOsName.startsWith("Mac") || sOsName.startsWith("HP-UX"))
//            command = "/sbin/ifconfig";
//        else
////            System.out.println("The current operating system '" + sOsName + "' is not supported.");
//
//        Pattern p = Pattern.compile("([a-fA-F0-9]{1,2}(-|:)){5}[a-fA-F0-9]{1,2}");
//        try {
//            Process pa = Runtime.getRuntime().exec(command);
//            pa.waitFor();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(pa.getInputStream()));
//
//            String line;
//            Matcher m;
//            while ((line = reader.readLine()) != null) {
////                System.out.println(line);
//                m = p.matcher(line);
//
//                if (!m.find()) continue;
//
//                line = m.group();
//                break;
//            }
////            System.out.println(line);
//            return line;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
}
