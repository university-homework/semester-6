import numpy as np

def f(x):
    return pow(x, 4) / 4 + 3 * pow(x, 3) + 9 * pow(x, 2)

a = -7
b = 1
N = 22
M = 28
exact_min_f = -2

x_points = [a + (2 * i - 1) * (b - a) / (2 * N) for i in range(1, N + 1)]
f_values = [f(x) for x in x_points]

min_f_passive = min(f_values)
min_x_passive = x_points[np.argmin(f_values)]

theoretical_error = M * (b - a) / (2 * N)
actual_error = abs(min_f_passive - exact_min_f)

print(f"Метод перебора:")
print(f"Точка минимума: {min_x_passive}")
print(f"Значение: {min_f_passive}")
print(f"Теоретическая погрешность: {theoretical_error}")
print(f"Фактическая погрешность: {actual_error}")
