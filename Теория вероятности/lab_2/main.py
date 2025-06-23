from math import ceil, erf, sqrt


def main():
    intervals = [
        ('менее 500', 58),
        ('500-1000', 96),
        ('1000-1500', 239),
        ('1500-2000', 328),
        ('2000-2500', 147),
        ('более 2500', 132),
    ]
    total_population = 20000
    sample_size = 1000

    midpoints = [250, 750, 1250, 1750, 2250, 2750]
    frequencies = [freq for _, freq in intervals]

    sample_mean = get_sample_mean(midpoints, frequencies, sample_size)
    sample_var = get_sample_var(midpoints, frequencies, sample_size, sample_mean)
    fcp, std_error = get_fcp(total_population, sample_size, sample_var)

    a(std_error)
    b(std_error, sample_mean)
    c(total_population, sample_var, std_error)


def a(std_error):
    """Вероятность того, что средний доход отличается не более чем на 45 руб"""

    delta = 45
    t = delta / std_error

    gamma = erf(t / sqrt(2))
    print(f'а) Вероятность того, что средний доход отличается не более чем на 45 руб.: {gamma:.4f}')


def b(std_error, sample_mean):
    """Доверительный интервал для среднего дохода с вероятностью 0.99"""

    t_b = 2.576  # Квантиль нормального распределения для 0.99
    delta_b = t_b * std_error

    lower_bound_b = sample_mean - delta_b
    upper_bound_b = sample_mean + delta_b

    print('\nб) Доверительный интервал для среднего дохода (γ=0.99):')
    print(f'Нижняя граница: {lower_bound_b:.2f} руб.')
    print(f'Верхняя граница: {upper_bound_b:.2f} руб.')


def c(total_population, sample_var, std_error):
    """Необходимый объем выборки для вероятности 0.9973"""

    t_b = 2.576  # Квантиль нормального распределения для 0.99
    t_c = 3  # Квантиль нормального распределения для 0.9973

    desired_delta = t_b * std_error
    n_required = get_non_repetitive_volume(total_population, t_c, sample_var, desired_delta)

    print(f'\nв) Необходимый объем выборки для γ=0.9973: {ceil(n_required)}')


def get_non_repetitive_volume(total_population, t, sample_var, desired_delta):
    """
    Объем бесповторной выборки

    Формула: N * t^2 * σ^2 / (N * Δ^2 + t^2 * σ^2)
    """
    return (total_population * t**2 * sample_var) / (total_population * desired_delta**2 + t**2 * sample_var)


def get_sample_mean(midpoints, frequencies, sample_size):
    """Выборочное среднее"""
    return sum(m * f for m, f in zip(midpoints, frequencies, strict=False)) / sample_size


def get_sample_var(midpoints, frequencies, sample_size, sample_mean):
    """
    Выборочная дисперсия.

    Это статистическая мера разброса данных в выборке относительно их среднего значения.
    Она показывает, насколько сильно значения в выборке отклоняются от среднего
    """
    return sum(f * (m - sample_mean) ** 2 for m, f in zip(midpoints, frequencies, strict=False)) / sample_size


def get_fcp(total_population, sample_size, sample_var):
    """
    Поправочный коэффициент для конечной совокупности.

    Бесповторная выборка уменьшает дисперсию, потому что
    в выборке нет дублирования элементов.
    Чем ближе размер выборки (sample_size) к размеру
    генеральной совокупности (total_population), тем меньше неопределённость
    """

    fpc = sqrt((total_population - sample_size) / (total_population - 1))
    std_error = sqrt(sample_var / sample_size) * fpc

    return fpc, std_error


if __name__ == '__main__':
    main()
