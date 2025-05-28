package lab_7;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class Project extends JFrame {
    public Project() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Создаем панель для размещения компонентов
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. Поле для телефона (формат: +7 (XXX) XXX-XX-XX)
        panel.add(new JLabel("Телефон:"));
        JFormattedTextField phoneField = null;
        try {
            MaskFormatter phoneFormatter = new MaskFormatter("+7 (###) ###-##-##");
            phoneFormatter.setPlaceholderCharacter('_');
            phoneField = new JFormattedTextField(phoneFormatter);
            phoneField.setColumns(15);
            panel.add(phoneField);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ошибка создания маски телефона", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }

        // 2. Поле для ИНН (формат 6) - 12 цифр
        panel.add(new JLabel("ИНН:"));
        JFormattedTextField innField = null;
        try {
            MaskFormatter innFormatter = new MaskFormatter("############");
            innFormatter.setPlaceholderCharacter('0');
            innField = new JFormattedTextField(innFormatter);
            innField.setColumns(15);
            panel.add(innField);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ошибка создания маски ИНН", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }

        // Добавляем кнопку для демонстрации
        JButton showButton = new JButton("Показать данные");
        panel.add(showButton);

        // Обработчик кнопки
        JFormattedTextField finalPhoneField = phoneField;
        JFormattedTextField finalInnField = innField;
        showButton.addActionListener(e -> {
            StringBuilder message = new StringBuilder("Введенные данные:\n");

            if (finalPhoneField != null) {
                message.append("Телефон: ").append(finalPhoneField.getText()).append("\n");
            } else {
                message.append("Телефон: (не указано)\n");
            }

            if (finalInnField != null) {
                message.append("ИНН: ").append(finalInnField.getText());
            } else {
                message.append("ИНН: (не указано)");
            }

            JOptionPane.showMessageDialog(this, message.toString());
        });

        // Настройка окна
        setContentPane(panel);
        pack();
        setLocationRelativeTo(null); // Центрирование
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Project());
    }
}
