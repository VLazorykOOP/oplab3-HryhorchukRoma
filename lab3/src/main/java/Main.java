import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

enum StudType {
  A_STUDENT, NORMAL_STUDENT
}

class Stud {
  private int id;
  private int scholarship;
  private StudType type;

  public Stud(int id, int scholarship, StudType type) {
    this.id = id;
    this.scholarship = scholarship;
    this.type = type;
  }

  public void printInfo() {
    System.out.println("ID: " + id + ", Scholarship: " + scholarship + ", Type: " + type);
  }
}

abstract class StudBuilder {
  protected Stud stud;

  public void createStud(int id, int scholarship) {
    stud = new Stud(id, scholarship, defineType());
  }

  public Stud getStud() {
    return stud;
  }

  protected abstract StudType defineType();
}

class AStudBuilder extends StudBuilder {
  @Override
  protected StudType defineType() {
    return StudType.A_STUDENT;
  }
}

class NormalStudBuilder extends StudBuilder {
  @Override
  protected StudType defineType() {
    return StudType.NORMAL_STUDENT;
  }
}

class Director {
  private StudBuilder builder;

  public void setBuilder(StudBuilder builder) {
    this.builder = builder;
  }

  public Stud buildStud(int id, int scholarship) {
    builder.createStud(id, scholarship);
    return builder.getStud();
  }
}

class StudFactory {
  private static final Map<StudType, Stud> studMap = new HashMap<>();

  public static Stud getStud(int id, int scholarship, StudType type) {
    Stud stud = studMap.get(type);
    if (stud == null) {
      stud = new Stud(id, scholarship, type);
      studMap.put(type, stud);
    }
    return stud;
  }
}

interface State {
  void handleRequest();
}

class ActiveState implements State {
  @Override
  public void handleRequest() {
    System.out.println("Student is active.");
  }
}

class GraduatedState implements State {
  @Override
  public void handleRequest() {
    System.out.println("Student has graduated.");
  }
}

class StudentContext {
  private State state;

  public void setState(State state) {
    this.state = state;
  }

  public void request() {
    state.handleRequest();
  }
}

public class Main {
  public static void main(String[] args) {
    simulate();
  }

  public static void simulate() {
    List<Stud> students = new ArrayList<>();
    Director director = new Director();
    StudBuilder aBuilder = new AStudBuilder();
    StudBuilder nBuilder = new NormalStudBuilder();

    for (int i = 0; i < 100; ++i) {
      Stud stud;
      if (Integer.toString(i).contains("5")) {
        director.setBuilder(aBuilder);
        stud = director.buildStud(i, 1500);
      } else {
        director.setBuilder(nBuilder);
        stud = director.buildStud(i, 1000);
      }
      students.add(stud);
    }

    for (Stud stud : students) {
      stud.printInfo();
    }

    StudentContext context = new StudentContext();
    context.setState(new ActiveState());
    context.request();
    context.setState(new GraduatedState());
    context.request();
  }
}
