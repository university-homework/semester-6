from math import sqrt


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


def get_non_repetitive_volume(total_population, t, sample_var, desired_delta):
    """
    Объем бесповторной выборки

    Формула: N * t^2 * σ^2 / (N * Δ^2 + t^2 * σ^2)
    """
    return (total_population * t**2 * sample_var) / (total_population * desired_delta**2 + t**2 * sample_var)
