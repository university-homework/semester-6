def main():
    a, b, N = 4, 11, 22

    x_min_passive, error_passive = passive_method(a, b, N)
    actual_error_passive = abs(x_min_passive - 4)

    print('Метод пассивного поиска:')
    print('-' * 50)
    print(f'x ≈ {x_min_passive}')
    print(f'Теоретическая погрешность: {error_passive}')
    print(f'Фактическая погрешность: {actual_error_passive}')
    print('-' * 50, end='\n\n')

    x_min_fibonacci, error_fibonacci = method_fibonacci(a, b, N)
    actual_error_fibonacci = abs(x_min_fibonacci - 4)

    print('Метод Фибоначчи:')
    print('-' * 50)
    print(f'x ≈ {x_min_fibonacci}')
    print(f'Теоретическая погрешность: {error_fibonacci}')
    print(f'Фактическая погрешность: {actual_error_fibonacci}')
    print('-' * 50)


def f(x):
    return (x - 252) / (x - 3) + 5 * abs(x - 10)


def passive_method(a: float, b: float, N: int) -> tuple[float, float]:
    k = N // 2
    delta = (b - a) / (k + 1) * 0.001

    X = [a]
    for i in range(1, k + 1):
        x_2i = a + (b - a) * i / (k + 1)
        x_2i_minus_1 = x_2i - delta
        X.append(x_2i_minus_1)
        X.append(x_2i)
    X.append(b)

    f_values = [f(x) for x in X[1:-1]]

    min_idx = f_values.index(min(f_values)) + 1
    x_min = (X[min_idx - 1] + X[min_idx + 1]) / 2

    l_N_pas = (b - a) / (k + 1) + delta
    error = l_N_pas / 2

    return x_min, error


def method_fibonacci(a: float, b: float, N: int) -> tuple[float, float]:
    F = [1, 1]

    while len(F) < N + 1:
        F.append(F[-1] + F[-2])

    d = (b - a) / F[N] * 0.001
    error = 1 / 2 * ((b - a) / F[N] + d)

    x1 = a + F[N - 2] / F[N] * (b - a)
    x2 = a + F[N - 1] / F[N] * (b - a)

    f1 = f(x1)
    f2 = f(x2)

    for i in range(N - 3):  # сужение интервала
        if f1 <= f2:
            b = x2
            x2 = x1
            f2 = f1
            x1 = a + (b - x1)
            f1 = f(x1)
        else:
            a = x1
            x1 = x2
            f1 = f2
            x2 = a + (b - x1)
            f2 = f(x2)

    if f1 <= f2:
        b = x1  # Сдвигаем правую границу в x1
        x2 = x1 + d  # Добавляем маленькое смещение d к x1
        f2 = f(x2)  # Вычисляем f в новой точке x2
    else:
        a = x1  # Сдвигаем левую границу в x1
        x1 = x2  # Теперь x1 становится старым x2
        f1 = f2  # f1 теперь равно старому f2
        x2 = x1 + d  # Добавляем смещение d к новому x1
        f2 = f(x2)  # Вычисляем f в новой точке x2

    if f1 <= f2:
        b = x1
    else:
        a = x1

    xa = (a + b) / 2
    return xa, error



if __name__ == '__main__':
    main()
