def b(std_error, sample_mean):
    """Доверительный интервал для среднего дохода с вероятностью 0.99"""

    t_b = 2.576  # Квантиль нормального распределения для 0.99
    delta_b = t_b * std_error

    lower_bound_b = sample_mean - delta_b
    upper_bound_b = sample_mean + delta_b

    print('\nб) Доверительный интервал для среднего дохода (γ=0.99):')
    print(f'Нижняя граница: {lower_bound_b:.2f} руб.')
    print(f'Верхняя граница: {upper_bound_b:.2f} руб.')
