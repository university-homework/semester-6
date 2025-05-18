from math import erf, sqrt


def a(std_error):
    """Вероятность того, что средний доход отличается не более чем на 45 руб"""

    delta = 45
    t = delta / std_error

    gamma = erf(t / sqrt(2))
    print(f'а) Вероятность того, что средний доход отличается не более чем на 45 руб.: {gamma:.4f}')
