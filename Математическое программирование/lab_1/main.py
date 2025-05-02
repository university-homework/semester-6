from lab_1 import methods


def main():
    a, b, N = 4, 11, 22

    x_min_passive, error_passive = methods.passive(a, b, N, f)
    actual_error_passive = abs(x_min_passive - 4)

    print('Метод пассивного поиска:')
    print('-' * 50)
    print(f'x ≈ {x_min_passive}')
    print(f'Теоретическая погрешность: {error_passive}')
    print(f'Фактическая погрешность: {actual_error_passive}')
    print('-' * 50, end='\n\n')

    x_min_fibonacci, error_fibonacci = methods.fibonacci(a, b, N, f)
    actual_error_fibonacci = abs(x_min_fibonacci - 4)

    print('Метод Фибоначчи:')
    print('-' * 50)
    print(f'x ≈ {x_min_fibonacci}')
    print(f'Теоретическая погрешность: {error_fibonacci}')
    print(f'Фактическая погрешность: {actual_error_fibonacci}')
    print('-' * 50)


def f(x):
    return (x - 252) / (x - 3) + 5 * abs(x - 10)


if __name__ == '__main__':
    main()
