import numpy as np
from scipy.stats import f

data = {
    'Магазин 1': [19, 23, 26, 18, 20, 20, 18, 35],
    'Магазин 2': [20, 20, 32, 27, 40, 24, 22, 18],
    'Магазин 3': [16, 15, 18, 2, 19, 17, 19, 18],
}

all_values = np.concatenate([*data.values()])
N = len(all_values)  # Общее количество наблюдений
k = len(data)  # Количество групп (магазинов)

GM = np.mean(all_values)  # общее среднее

# сумма квадратов между группами
SSB = 0
for group in data.values():
    group_mean = np.mean(group)
    SSB += len(group) * (group_mean - GM) ** 2

# сумма квадратов внутри групп
SSW = 0
for group in data.values():
    group_mean = np.mean(group)
    SSW += sum((x - group_mean) ** 2 for x in group)

# общая сумма квадратов
SST = SSB + SSW

# степени свободы
df_between = k - 1
df_within = N - k
df_total = N - 1

# средние квадраты
MSB = SSB / df_between
MSW = SSW / df_within

# F-статистика (проверки гипотезы о равенстве средних значений в нескольких группах)
F = MSB / MSW

# вероятность получить такое же или более экстремальное значение F-статистики
alpha = 0.05
F_crit = f.ppf(1 - alpha, df_between, df_within)

# Вывод результатов
print(f'Сумма квадратов между группами = {SSB:.2f}')
print(f'сумма квадратов внутри групп = {SSW:.2f}')
print(f'общая сумма квадратов = {SST:.2f}')
print('\nСтепени свободы:')
print(f'df_between = {df_between}')
print(f'df_within = {df_within}')
print(f'df_total = {df_total}')
print('\nСредние квадраты:')
print(f'MSB = {MSB:.2f}')
print(f'MSW = {MSW:.2f}')
print('\nF-статистика и p-value:')
print(f'F = {F:.4f}')

# Проверка гипотезы
if F_crit < F:
    print('\nВывод: Нет статистически значимых различий между магазинами (p-value > 0.05).')
    print(f'Несмещенная оценка среднего: {GM:.2f} тыс. руб.')
    print(f'Несмещенная оценка дисперсии: {sum((x - GM) ** 2 for x in all_values) / (N - 1):.2f} (тыс. руб.)²')
else:
    print('\nВывод: Есть статистически значимые различия (p-value ≤ 0.05).')
