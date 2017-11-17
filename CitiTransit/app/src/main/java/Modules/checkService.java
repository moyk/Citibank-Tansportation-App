package Modules;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Created by yianmo on 11/15/17.
 */

public class checkService {
    private String linename,startstop,endstop;
    private ArrayList<ArrayList<String>> reslist;
    private InputStream inputstream;
    public checkService(String linename, String startstop, String endstop, InputStream inputstream){
        this.linename=linename;
        this.startstop=startstop;
        this.endstop=endstop;
        this.inputstream=inputstream;
        Log.i("test", "yes!!!!");

            readdata(linename, startstop, endstop);

    }

    public ArrayList<ArrayList<String>> getReslist(){
        return reslist;
    }

    public void readdata(String linename, String startstop, String endstop) {
        //Log.i("test", "start reading file");
        //File sdcard = Environment.getExternalStorageDirectory();
        //File file = new File(sdcard,"file.txt");
        //System.out.println(new File(".").getAbsoluteFile());
        BufferedReader reader = null;
        reader = new BufferedReader(new InputStreamReader(inputstream));


        // read file line by line
        String line = null;
        Scanner scanner = null;
        int index = 0;
        List<transitStop> stopList = new ArrayList<>();

        try {
            while ((line = reader.readLine()) != null) {
                //Log.i("test", "reading...");
                transitStop stop = new transitStop();
                String[]data= line.split(",");
                scanner = new Scanner(line);
                scanner.useDelimiter(",");
                stop.name = data[0];
                stop.Lang = data[1];
                stop.Long = data[2];
                stop.topToBottom = data[3];
                stop.reason1 = data[4];
                stop.botomToTop = data[5];
                stop.reason2 = data[6];
                Log.i("test","currently reading"+stop.name);

                stopList.add(stop);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            inputstream.close();
        } catch (IOException e) {
            throw new RuntimeException("Error while closing input stream");
        }
        stopList.remove(0);
        Log.i("test","size of stoplist"+ Integer.toString(stopList.size()));

        ArrayList<Double> distancelist1 = new ArrayList<>();
        ArrayList<Double> distancelist2 = new ArrayList<>();
        for (int i=0; i<stopList.size(); i++){
            transitStop stop=stopList.get(i);
            double distance1=similarity(startstop,stop.name);
            double distance2=similarity(endstop,stop.name);
            distancelist1.add(distance1);
            distancelist2.add(distance2);
        }


        int startIndex1 = distancelist1.indexOf(Collections.max(distancelist1));
        int endIndex2 = distancelist2.indexOf(Collections.max(distancelist2));
        transitStop startStop=stopList.get(startIndex1);
        transitStop endStop=stopList.get(endIndex2);

        System.out.println(startIndex1);
        System.out.println(endIndex2);

        int bottomup=-1;
        if (startIndex1>endIndex2) {
            bottomup = 1;
            int temp=startIndex1;
            startIndex1= endIndex2;
            endIndex2=temp;
        }

        reslist=new ArrayList<>();
        ArrayList<String> testList=new ArrayList<>();

        System.out.println(bottomup);
        if (bottomup==-1){
            if (!stopList.get(startIndex1).topToBottom.equals("good")){
                int tempIndex=0;
                while (!stopList.get(startIndex1+tempIndex).equals("good")){
                    tempIndex++;
                    if (startIndex1+tempIndex>=stopList.size())
                        break;
                }
                tempIndex--;
                System.out.println("startindex to "+ (startIndex1+tempIndex));
                ArrayList<String> latitude=new ArrayList<>();
                latitude.add(stopList.get(startIndex1).Lang);
                latitude.add(stopList.get(startIndex1).Long);
                reslist.add(latitude);
                latitude=new ArrayList<>();
                latitude.add(stopList.get(startIndex1+tempIndex).Lang);
                latitude.add(stopList.get(startIndex1+tempIndex).Long);
                reslist.add(latitude);
                testList.add(startStop.name);
                testList.add(stopList.get(startIndex1+tempIndex).name);

            }

            if (!stopList.get(endIndex2).topToBottom.equals("good")){
                int tempIndex=0;
                while (!stopList.get(endIndex2-tempIndex).topToBottom.equals("good")){
                    tempIndex++;

                    if (endIndex2-tempIndex<0)
                        break;
                }
                tempIndex--;
                ArrayList<String> latitude=new ArrayList<>();
                latitude.add(endStop.Lang);
                latitude.add(endStop.Long);
                reslist.add(latitude);
                latitude=new ArrayList<>();
                latitude.add(stopList.get(endIndex2-tempIndex).Lang);
                latitude.add(stopList.get(endIndex2-tempIndex).Long);
                reslist.add(latitude);
                testList.add(endStop.name);
                testList.add(stopList.get(endIndex2-tempIndex).name);
            }
        }
        if (bottomup==1){
            System.out.println(stopList.get(startIndex1).botomToTop);

            if (!stopList.get(startIndex1).botomToTop.equals("good")){
                int tempIndex=0;
                while (!stopList.get(startIndex1+tempIndex).botomToTop.equals("good")){
                    tempIndex++;
                    if (startIndex1+tempIndex>=stopList.size())
                        break;
                }
                tempIndex--;
                System.out.println("startindex to "+ (startIndex1+tempIndex));
                ArrayList<String> latitude=new ArrayList<>();
                latitude.add(stopList.get(startIndex1).Lang);
                latitude.add(stopList.get(startIndex1).Long);
                reslist.add(latitude);
                latitude=new ArrayList<>();
                latitude.add(stopList.get(startIndex1+tempIndex).Lang);
                latitude.add(stopList.get(startIndex1+tempIndex).Long);
                reslist.add(latitude);
                testList.add(stopList.get(startIndex1).name);
                testList.add(stopList.get(startIndex1+tempIndex).name);

            }

            if (!stopList.get(endIndex2).botomToTop.equals("good")){
                int tempIndex=0;
                while (!stopList.get(endIndex2-tempIndex).botomToTop.equals("good")){
                    tempIndex++;

                    if (endIndex2-tempIndex<0)
                        break;
                }
                tempIndex--;
                ArrayList<String> latitude=new ArrayList<>();
                latitude.add(stopList.get(endIndex2).Lang);
                latitude.add(stopList.get(endIndex2).Long);
                reslist.add(latitude);
                latitude=new ArrayList<>();
                latitude.add(stopList.get(endIndex2-tempIndex).Lang);
                latitude.add(stopList.get(endIndex2-tempIndex).Long);
                reslist.add(latitude);
                testList.add(stopList.get(endIndex2).name);
                testList.add(stopList.get(endIndex2-tempIndex).name);
            }

        }

        System.out.println(testList.size());
        for (int i=0;i<testList.size();i++){
            System.out.println("here"+testList.get(i));

        }

        for (int i=0; i<reslist.size();i++){
            ArrayList<String> current=reslist.get(i);
            System.out.println("here"+current.get(0));
            System.out.println("here"+current.get(1));
        }
        Log.i("test", "size of reslist inside is "+ reslist.size());

    }

    public static double similarity(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) { // longer should always have greater length
            longer = s2; shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) { return 1.0; /* both strings are zero length */ }
    /* // If you have StringUtils, you can use it to calculate the edit distance:
    return (longerLength - StringUtils.getLevenshteinDistance(longer, shorter)) /
                               (double) longerLength; */
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;

    }

    public static int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }
}
