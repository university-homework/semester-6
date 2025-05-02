import matplotlib.pyplot as plt
import numpy as np

# Исходные данные
growth_data = np.array(
    [
        0.4,
        1.9,
        1.5,
        0.9,
        0.3,
        1.6,
        0.4,
        1.5,
        1.2,
        0.8,
        0.9,
        0.7,
        0.9,
        0.7,
        0.9,
        1.5,
        0.5,
        1.5,
        1.7,
        1.8,
    ]
)
n = len(growth_data)

# Создаем интервалы для гистограммы
bins = [0.2, 0.6, 1.0, 1.4, 1.8, 2.2]
intervals = ['0.2-0.6', '0.6-1.0', '1.0-1.4', '1.4-1.8', '1.8-2.2']
midpoints = np.array([0.4, 0.8, 1.2, 1.6, 2.0])

# Рассчитываем частоты для каждого интервала
frequencies, _ = np.histogram(growth_data, bins=bins)
print(frequencies)

# Построение гистограммы
plt.figure(figsize=(12, 6))
plt.bar(intervals, frequencies, width=0.7, color='skyblue', edgecolor='black')
plt.title('Гистограмма распределения еженедельного роста ржи', fontsize=14)
plt.xlabel('Интервалы роста (см)', fontsize=12)
plt.ylabel('Частота (количество недель)', fontsize=12)
plt.grid(axis='y', linestyle='--', alpha=0.7)
plt.xticks(fontsize=10)
plt.yticks(fontsize=10)
plt.show()

# Построение полигона
plt.figure(figsize=(12, 6))
plt.plot(midpoints, frequencies, marker='o', linestyle='-', color='blue', markersize=8)
plt.title('Полигон распределения еженедельного роста ржи', fontsize=14)
plt.xlabel('Середины интервалов роста (см)', fontsize=12)
plt.ylabel('Частота (количество недель)', fontsize=12)
plt.grid(True, linestyle='--', alpha=0.7)
plt.xticks(fontsize=10)
plt.yticks(fontsize=10)
plt.show()

# Построение кумуляты
cumulative = np.cumsum(frequencies)
plt.figure(figsize=(12, 6))
plt.plot(intervals, cumulative, marker='o', color='green', markersize=8)
plt.title('Кумулятивная кривая распределения роста ржи', fontsize=14)
plt.xlabel('Интервалы роста (см)', fontsize=12)
plt.ylabel('Накопленная частота', fontsize=12)
plt.grid(True, linestyle='--', alpha=0.7)
plt.xticks(fontsize=10)
plt.yticks(fontsize=10)
plt.show()

# Построение эмпирической функции распределения
sorted_values = np.sort(growth_data)
y = np.arange(1, len(sorted_values) + 1) / len(sorted_values)

plt.figure(figsize=(12, 6))
plt.step(sorted_values, y, where='post', color='red', linewidth=2)
plt.title('Эмпирическая функция распределения роста ржи', fontsize=14)
plt.xlabel('Рост (см)', fontsize=12)
plt.ylabel('F(x)', fontsize=12)
plt.grid(True, linestyle='--', alpha=0.7)
plt.xticks(fontsize=10)
plt.yticks(fontsize=10)
plt.show()

# Расчет статистических показателей
mean = np.mean(growth_data)

# Мода
values, counts = np.unique(growth_data, return_counts=True)
mode = values[np.argmax(counts)]

# Медиана
median = np.median(growth_data)

# Дисперсия и стандартное отклонение
variance = np.var(growth_data, ddof=0)  # смещенная дисперсия
std_dev = np.std(growth_data, ddof=0)  # смещенное стандартное отклонение

# Коэффициент вариации
coef_var = (std_dev / mean) * 100 if mean != 0 else np.nan

# Начальные моменты
moments = [np.mean(growth_data**k) for k in range(1, 5)]

# Центральные моменты
central_moments = [np.mean((growth_data - mean) ** k) for k in range(2, 5)]

skewness = central_moments[1] / std_dev**3  # коэффициент асимметрии
kurtosis_val = central_moments[2] / std_dev**4 - 3  # коэффициент эксцесса

# Доля ржи, которая вырастает более чем на 1 см в неделю
growth_above_1cm = np.sum(growth_data > 1) / n * 100

print(f'1. Средний еженедельный рост ржи: {mean:.2f} см')
print(f'2. Мода: {mode:.1f} см (наиболее часто встречающееся значение роста)')
print(f'3. Медиана: {median:.2f} см (серединное значение роста)')
print(f'4. Дисперсия: {variance:.4f}')
print(f'5. Среднее квадратическое отклонение: {std_dev:.4f} см')
print(f'6. Коэффициент вариации: {coef_var:.2f}%')
print(f'7. Доля ржи, растущей более чем на 1 см в неделю: {growth_above_1cm:.1f}%')
print(f'  3-го порядка: {central_moments[1]:.4f}')
print(f'  4-го порядка: {central_moments[2]:.4f}')
print(f'\nКоэффициент асимметрии: {skewness:.4f}')
print(f'Коэффициент эксцесса: {kurtosis_val:.4f}')
