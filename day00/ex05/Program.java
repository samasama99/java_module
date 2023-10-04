import java.util.Scanner;

public class Program {

  static int getNames(String[] names, Scanner user_input) {
    int number_of_student = 0;
    while (number_of_student != 10) {
      System.out.print("-> ");
      String name = user_input.next();
      if (name.compareTo(".") == 0) {
        break;
      }
      names[number_of_student++] = name;
    }
    return number_of_student;
  }

  static int getTimetable(int[][] classes, Scanner user_input) {
    int number_of_classes = 0;

    while (number_of_classes != 10) {
      System.out.print("-> ");
      String hour = user_input.next();

      if (hour.compareTo(".") == 0) {
        break;
      }

      String day = user_input.next();
      int _day = 0;
      int _hour = Integer.parseInt(hour);
      switch (day) {
        case "SU":
          ++_day;
        case "SA":
          ++_day;
        case "FR":
          ++_day;
        case "TH":
          ++_day;
        case "WE":
          ++_day;
        case "TU":
          ++_day;
        case "MO":
        default:
      }
      classes[number_of_classes][0] = _day;
      classes[number_of_classes++][1] = _hour;
    }
    return number_of_classes;
  }

  static int getAttendance(int[][][] attendances, String[] names, Scanner user_input) {
    int number_of_attendances = 0;
    while (true) {
      System.out.print("-> ");
      String name = user_input.next();

      if (name.compareTo(".") == 0) {
        break;
      }

      int index = getNameIndex(name, names);
      int hour = user_input.nextInt();
      int day = user_input.nextInt();
      String attendance = user_input.next();
      int _attendance = -1;
      if (attendance.compareTo("HERE") == 0) {
        _attendance = 1;
      }
      attendances[index][day][hour] = _attendance;
    }

    return number_of_attendances;
  }

  private static int getNameIndex(String name, String[] names) {
    int index = 0;
    for (String n : names) {
      if (n.compareTo(name) == 0) {
        return index;
      }
      index++;
    }
    return -1;
  }

  static String getDayName(int day) {
    switch (day % 7) {
      case 0:
        return "MO";
      case 1:
        return "TU";
      case 2:
        return "WE";
      case 3:
        return "TH";
      case 4:
        return "FR";
      case 5:
        return "SA";
      case 6:
        return "SU";
    }
    return "NULL";
  }

  static boolean isClassExist(int[][] classes, int day, int hour) {
    for (int[] c : classes) {
      if (c[0] == day && c[1] == hour) return true;
    }
    return false;
  }

  public static void main(String[] args) {
    Scanner user_input = new Scanner(System.in);

    String[] names = new String[10];
    getNames(names, user_input);
    int[][] classes = new int[10][2];
    getTimetable(classes, user_input);
    int[][][] attendances = new int[10][30][7];
    getAttendance(attendances, names, user_input);

    System.out.println();
    for (int i = 1; i < 31; i++) {
      for (int j = 1; j < 7; j++) {
        if (isClassExist(classes, i % 7, j))
          System.out.printf("%1d:00%3s%3d|", j, getDayName(i), i);
      }
    }

    System.out.println();

    for (int i = 0; i < attendances.length; i++) {
      if (names[i] == null) continue;
      System.out.printf("%-10s|", names[i]);
      for (int j = 1; j < attendances[i].length; j++) {
        for (int j2 = 1; j2 < attendances[i][j].length; j2++) {
          if (attendances[i][j][j2] != 0) {
            System.out.printf("%10d|", attendances[i][j][j2]);
          } else if (isClassExist(classes, j % 7, j2)) {
            System.out.printf("%10s|", "");
          }
        }
      }
      System.out.println();
    }

    user_input.close();
  }
}
