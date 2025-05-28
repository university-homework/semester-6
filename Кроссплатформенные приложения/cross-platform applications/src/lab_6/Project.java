package lab_6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Project {
    public static void main(String[] args) {
        List<HeavyBox> boxes = new ArrayList<>();
        boxes.add(new HeavyBox(10));
        boxes.add(new HeavyBox(20));
        boxes.add(new HeavyBox(30));
        boxes.add(new HeavyBox(40));

        System.out.println("Содержимое коллекции:");
        for (HeavyBox box : boxes) {
            System.out.println(box);
        }

        boxes.getFirst().setWeight(1);
        System.out.println("\nПосле изменения веса первого ящика:");
        System.out.println(boxes);

        boxes.removeLast();
        System.out.println("\nПосле удаления последнего ящика:");
        System.out.println(boxes);

        // Способ 1: Использование toArray() с заранее созданным массивом
        HeavyBox[] array1 = new HeavyBox[boxes.size()];
        boxes.toArray(array1);
        System.out.println("\nМассив (способ 1): " + Arrays.toString(array1));

        // Способ 2: Использование toArray() с пустым массивом
        HeavyBox[] array2 = boxes.toArray(new HeavyBox[0]);
        System.out.println("Массив (способ 2): " + Arrays.toString(array2));

        // Способ 3: Ручное копирование
        HeavyBox[] array3 = new HeavyBox[boxes.size()];
        for (int i = 0; i < boxes.size(); i++) {
            array3[i] = boxes.get(i);
        }
        System.out.println("Массив (способ 3): " + Arrays.toString(array3));

        boxes.clear();
        System.out.println("\nПосле удаления всех ящиков:");
        System.out.println("Коллекция: " + boxes);
        System.out.println("Размер коллекции: " + boxes.size());
    }
}

class HeavyBox {
    private int weight;

    public HeavyBox(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "HeavyBox{weight=" + getWeight() + "}";
    }
}
