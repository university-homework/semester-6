from typing import Callable


def passive(a: int, b: int, N: int, f: Callable) -> tuple[float, float]:
    X = [a] + [a + (b - a) / (N + 1) * i for i in range(1, N + 1)] + [b]

    f_values = [f(x) for x in X[1:]]
    min_f = min(f_values)
    j = f_values.index(min_f) + 1
    x_final = (X[j - 1] + X[j + 1]) / 2

    l_N_pas = 2 * (b - a) / (N + 1)

    error = l_N_pas / 2

    return x_final, error
