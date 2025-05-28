package lab_5;

public class OnOffSwitch {
    static Switch sw = new Switch();
    static void f() throws
            OnOffException1, OnOffException2 {}

    public static void main(String[] args) {
        try {
            sw.on();
            if (true)
                throw new RuntimeException("Ошибка!");
            f();
            sw.off();
        } catch(OnOffException1 e) {
            System.err.println("OnOffException1");
            sw.off();
        } catch(OnOffException2 e) {
            System.err.println("OnOffException2");
            sw.off();
        }
    }
}

class Switch {
    boolean state = false;
    boolean read() { return state; }
    void on() { state = true; }
    void off() { state = false; }
}

class OnOffException1 extends Exception {}
class OnOffException2 extends Exception {}
