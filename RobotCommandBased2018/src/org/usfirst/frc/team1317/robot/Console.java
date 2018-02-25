package org.usfirst.frc.team1317.robot;


public class Console {
    public static int      level = 1;

    public static void show(int level, String msg) {
        if (Console.level < level) return;
        System.out.println(msg);
    }
}