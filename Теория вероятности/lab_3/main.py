# Гипотеза: доходы имеют нормальное распределение
# Свойства нормального распределения: симметричность относительно среднего, Убывание частот по краям, отсутствие асимметрии

import matplotlib.pyplot as plt
import numpy as np
from scipy.stats import chi2, norm

intervals = ['<500', '500-1000', '1000-1500', '1500-2000', '2000-2500', '>2500']
frequencies = np.array([58, 96, 239, 328, 147, 132])
total = np.sum(frequencies)

midpoints = np.array([250, 750, 1250, 1750, 2250, 2750])

# Выборочное среднее и стандартное отклонение
vib_srednee = np.sum(midpoints * frequencies) / total
sigma = np.sqrt(np.sum((midpoints - vib_srednee) ** 2 * frequencies) / total)

print(f'Выборочное среднее: {vib_srednee:.2f}')
print(f'Выборочное стандартное отклонение: {sigma:.2f}')

# Границы интервалов
bounds = [-np.inf, 500, 1000, 1500, 2000, 2500, np.inf]

# Теоретические вероятности для интервалов
probs = []
for i in range(len(bounds) - 1):
    prob = norm.cdf(bounds[i + 1], loc=vib_srednee, scale=sigma) - norm.cdf(bounds[i], loc=vib_srednee, scale=sigma)
    probs.append(prob)

probs = np.array(probs)
theoretical_freq = probs * total

# Критерий Пирсона (сравнивает наблюдаемые частоты в интервалах с ожидаемыми (теоретическими) частотами)
chi2_stat = np.sum((frequencies - theoretical_freq) ** 2 / theoretical_freq)
chi2_crit = chi2.ppf(0.95, df=len(frequencies) - 3)  # df = k - 1 - 2

print('\nКритерий Пирсона:')
print(f'χ² = {chi2_stat:.3f}, крит. значение = {chi2_crit:.3f}')
print('Гипотеза не отвергается' if chi2_stat < chi2_crit else 'Гипотеза отвергается')

# Критерий Колмогорова (сравнивает эмпирическую функцию распределения (ECDF) с теоретической (CDF))
data = np.repeat(midpoints, frequencies)
data = np.sort(data)

# Эмпирическая CDF
n = len(data)
empirical_cdf = np.arange(1, n + 1) / n
# Теоретическая CDF
theoretical_cdf = norm.cdf(data, loc=vib_srednee, scale=sigma)
D = np.max(np.abs(empirical_cdf - theoretical_cdf))
critical_value = 1.36 / np.sqrt(n)

print('\nКритерий Колмогорова:')
print(f'Статистика D = {D:.4f}')
print(f'Критическое значение = {critical_value:.4f}')
print('Гипотеза не отвергается' if critical_value > D else 'Гипотеза отвергается')

# Построение графика
plt.figure(figsize=(10, 6))

# Строим гистограмму по данным
bin_edges = [0, 500, 1000, 1500, 2000, 2500, 3000]
plt.hist(
    data,
    bins=bin_edges,
    density=True,
    alpha=0.6,
    color='skyblue',
    edgecolor='black',
    label='Эмпирическое распределение',
)

# Нормальное распределение
x = np.linspace(0, 3000, 500)
plt.plot(x, norm.pdf(x, vib_srednee, sigma), 'r-', lw=2, label='Нормальное распределение')

plt.xlabel('Доход (руб.)')
plt.ylabel('Плотность вероятности')
plt.title('Сравнение эмпирического и нормального распределений')
plt.legend()
plt.grid(True)
plt.show()
