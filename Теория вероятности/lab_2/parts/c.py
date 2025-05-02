from math import ceil

from utils import math as utils_math


def c(total_population, sample_var, std_error):
    """Необходимый объем выборки для вероятности 0.9973"""

    t_b = 2.576  # Квантиль нормального распределения для 0.99
    t_c = 3  # Квантиль нормального распределения для 0.9973

    desired_delta = t_b * std_error
    n_required = utils_math.get_non_repetitive_volume(total_population, t_c, sample_var, desired_delta)

    print(f'\nв) Необходимый объем выборки для γ=0.9973: {ceil(n_required)}')
