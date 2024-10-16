/*
 * @ (#) Time.java       1.0     16/10/2024
 *
 *Copyright (c) 2024 IUH. All right reserved.
 */
package controller;

/*
 * @description:
 * @author: Hiep Nguyen
 * @date:   16/10/2024
 * version: 1.0
 */
public class Time {
    private int hour;
    private int minute;
    private int second;

    public Time(int hour, int minute, int second) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public Time(String currentTime) {
        String[] time = currentTime.split(":");
        hour = Integer.parseInt(time[0]);
        minute = Integer.parseInt(time[1]);
        second = Integer.parseInt(time[2]);
    }

    public String getCurrentTime(){
        return (hour < 10 ? "0" +hour : hour) + ":" + (minute < 10 ? "0" + minute : minute) + ":" + (second < 10 ? "0" + second : second);
    }

    public void oneSecondPassed(){
        second++;
        if(second == 60){
            minute++;
            second = 0;
            if(minute == 60){
                hour++;
                minute = 0;
                if(hour == 24){
                    hour = 0;
                    System.out.println("Next day");
                }
            }
        }
    }
}
