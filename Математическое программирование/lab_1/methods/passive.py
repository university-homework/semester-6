from typing import Callable


def passive(a: float, b: float, N: int, f: Callable[[float], float]) -> tuple[float, float]:
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
