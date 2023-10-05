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
      classes[number_of_classes][1] = _hour;
      number_of_classes++;
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
      int hour = user_input.nextInt() - 1;
      int day = user_input.nextInt() - 1;
      String attendance = user_input.next();
      int _attendance = 0;
      if (attendance.compareTo("HERE") == 0) {
        _attendance = 1;
      } else if (attendance.compareTo("NOT_HERE") == 0) {
        _attendance = -1;
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
    switch (day) {
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

  static boolean isClass(int[][] classes, int day, int hour) {
    for (int[] c : classes) {
      if (c[0] == (day) && c[1] == (hour))
        return true;
    }
    return false;
  }

  public static void main(String[] args) {
    Scanner user_input = new Scanner(System.in);

    String[] names = new String[10];
    getNames(names, user_input);
    int[][] classes = new int[10][2];
    getTimetable(classes, user_input);
    int[][][] attendances = new int[10][30][5];
    getAttendance(attendances, names, user_input);

    System.out.println();
    System.out.print("           ");
    for (int day = 0; day < 30; day++) {
      for (int hour = 0; hour < 5; hour++) {
        if (isClass(classes, (day + 1) % 7, hour + 1))
          System.out.printf("%1d:00%3s%3d|", hour + 1, getDayName((day + 1) % 7), day + 1);
      }
    }

    System.out.println();

    for (int studentIndex = 0; studentIndex < names.length; studentIndex++) {
      if (names[studentIndex] == null)
        continue;
      System.out.printf("%-10s|", names[studentIndex]);
      for (int day = 0; day < 30; day++) {
        for (int hour = 0; hour < 5; hour++) {
          if (attendances[studentIndex][day][hour] != 0) {
            System.out.printf("%10d|", attendances[studentIndex][day][hour]);
          } else if (isClass(classes, (day + 1) % 7, hour + 1)) {
            System.out.printf("%10s|", "");
          }
        }
      }
      System.out.println();
    }

    user_input.close();
  }
}
